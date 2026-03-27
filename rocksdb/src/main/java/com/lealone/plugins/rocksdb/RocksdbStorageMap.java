/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package com.lealone.plugins.rocksdb;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.rocksdb.BlockBasedTableConfig;
import org.rocksdb.FlushOptions;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.rocksdb.RocksIterator;

import com.lealone.common.exceptions.DbException;
import com.lealone.db.DataBuffer;
import com.lealone.db.value.ValueLong;
import com.lealone.storage.CursorParameters;
import com.lealone.storage.Storage;
import com.lealone.storage.StorageMapBase;
import com.lealone.storage.StorageMapCursor;
import com.lealone.storage.fs.FileUtils;
import com.lealone.storage.type.StorageDataType;

public class RocksdbStorageMap<K, V> extends StorageMapBase<K, V> {

    private final RocksDB db;
    private final String dbPath;
    private boolean closed;

    public RocksdbStorageMap(String name, StorageDataType keyType, StorageDataType valueType,
            RocksdbStorage storage) {
        super(name, keyType, valueType, storage);

        Options options = new Options();
        options.setCreateIfMissing(true);
        BlockBasedTableConfig config = new BlockBasedTableConfig();
        options.setTableFormatConfig(config);
        dbPath = storage.getStoragePath() + File.separator + name;
        try {
            db = RocksDB.open(options, dbPath);
        } catch (RocksDBException e) {
            throw ioe(e, "Failed to open " + dbPath);
        }
        setMaxKey(lastKey());
    }

    private static DbException ioe(Throwable e, String msg) {
        throw DbException.convertIOException(new IOException(e), msg);
    }

    private static byte[] toBytes(Object obj, StorageDataType type) {
        DataBuffer buff = DataBuffer.createHeap();
        type.write(buff, obj);
        ByteBuffer b = buff.getAndFlipBuffer();
        byte[] dest = new byte[b.limit()];
        System.arraycopy(b.array(), b.arrayOffset(), dest, 0, dest.length);
        return dest;
    }

    private byte[] k(Object key) {
        return toBytes(key, keyType);
    }

    private byte[] v(Object value) {
        return toBytes(value, valueType);
    }

    @SuppressWarnings("unchecked")
    K k(byte[] key) {
        return (K) keyType.read(ByteBuffer.wrap(key));
    }

    @SuppressWarnings("unchecked")
    V v(byte[] value) {
        if (value == null)
            return null;
        return (V) valueType.read(ByteBuffer.wrap(value));
    }

    @Override
    public Storage getStorage() {
        return storage;
    }

    @Override
    public V get(K key) {
        byte[] value;
        try {
            value = db.get(k(key));
        } catch (RocksDBException e) {
            throw ioe(e, "Failed to get " + key);
        }
        return v(value);
    }

    @Override
    public V put(K key, V value) {
        V old = get(key);
        try {
            db.put(k(key), v(value));
        } catch (RocksDBException e) {
            throw ioe(e, "Failed to put " + key);
        }
        return old;
    }

    @Override
    public V putIfAbsent(K key, V value) {
        if (get(key) != null)
            return put(key, value);
        return null;
    }

    @Override
    public V remove(K key) {
        V old = get(key);
        try {
            db.delete(k(key));
        } catch (RocksDBException e) {
            throw ioe(e, "Failed to remove " + key);
        }
        return old;
    }

    @Override
    public K firstKey() {
        try (RocksIterator iterator = db.newIterator()) {
            iterator.seekToFirst();
            if (iterator.isValid()) {
                return k(iterator.key());
            }
        }
        return null;
    }

    @Override
    public K lastKey() {
        try (RocksIterator iterator = db.newIterator()) {
            iterator.seekToLast();
            if (iterator.isValid()) {
                return k(iterator.key());
            }
        }
        return null;
    }

    @Override
    public K lowerKey(K key) { // 小于给定key的最大key
        return getMinMax(key, true, true);
    }

    @Override
    public K floorKey(K key) { // 小于或等于给定key的最大key
        return getMinMax(key, true, false);
    }

    @Override
    public K higherKey(K key) { // 大于给定key的最小key
        return getMinMax(key, false, true);
    }

    @Override
    public K ceilingKey(K key) { // 大于或等于给定key的最小key
        return getMinMax(key, false, false);
    }

    private K getMinMax(K key, boolean min, boolean excluding) {
        try (RocksIterator iterator = db.newIterator()) {
            for (iterator.seekToFirst(); iterator.isValid(); iterator.next()) {
                K k = k(iterator.key());
                if (keyType.compare(key, k) >= 0) {
                    if (min) {
                        if (excluding) {
                            iterator.prev();
                            if (iterator.isValid())
                                return k(iterator.key());
                            else
                                return null;
                        } else {
                            return k;
                        }
                    } else {
                        if (excluding) {
                            iterator.next();
                            if (iterator.isValid())
                                return k(iterator.key());
                            else
                                return null;
                        } else {
                            return k;
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public long size() {
        long size = 0;
        try (final RocksIterator iterator = db.newIterator()) {
            for (iterator.seekToFirst(); iterator.isValid(); iterator.next()) {
                size++;
            }
        }
        return size;
    }

    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    @Override
    public boolean isEmpty() {
        try (RocksIterator iterator = db.newIterator()) {
            iterator.seekToFirst();
            if (iterator.isValid()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isInMemory() {
        return false;
    }

    @Override
    public StorageMapCursor<K, V> cursor(CursorParameters<K> parameters) {
        K from = parameters.from;
        RocksIterator iterator = db.newIterator();
        if (from == null) {
            iterator.seekToFirst();
            return new RocksdbStorageMapCursor<>(this, iterator);
        }
        for (iterator.seekToFirst(); iterator.isValid(); iterator.next()) {
            K key = k(iterator.key());
            if (keyType.compare(key, from) >= 0) {
                break;
            }
        }
        return new RocksdbStorageMapCursor<>(this, iterator);
    }

    @Override
    public void clear() {
        remove();
    }

    @Override
    public void remove() {
        close();
        storage.closeMap(getName());
        FileUtils.deleteRecursive(dbPath, true);
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

    @Override
    public void close() {
        db.close();
        closed = true;
    }

    @Override
    public void save() {
        try {
            db.flush(new FlushOptions());
        } catch (RocksDBException e) {
            throw DbException.convert(e);
        }
    }

    @Override
    public K append(V value) {
        @SuppressWarnings("unchecked")
        K k = (K) ValueLong.get(size() + 1);
        put(k, value);
        return k;
    }
}
