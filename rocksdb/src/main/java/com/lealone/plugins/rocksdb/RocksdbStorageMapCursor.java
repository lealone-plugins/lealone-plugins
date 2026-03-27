/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package com.lealone.plugins.rocksdb;

import com.lealone.storage.StorageMapCursor;
import org.rocksdb.RocksIterator;

//RocksIterator的行为跟java.util.Iterator不一样，
//java.util.Iterator是先判断hasNext()，如果为true再调用next()取到当前值，
//而RocksIterator是判断isValid()为true时就要把当前值取好了，调用next是转到下一行了。
public class RocksdbStorageMapCursor<K, V> implements StorageMapCursor<K, V> {

    private final RocksdbStorageMap<K, V> map;
    private final RocksIterator iterator;
    private K k;
    private V v;

    public RocksdbStorageMapCursor(RocksdbStorageMap<K, V> map, RocksIterator iterator) {
        this.map = map;
        this.iterator = iterator;
    }

    @Override
    public K getKey() {
        return k;
    }

    @Override
    public V getValue() {
        return v;
    }

    public boolean hasNext() {
        boolean isValid = iterator.isValid();
        if (isValid) {
            k = map.k(iterator.key());
            v = map.v(iterator.value());
        } else {
            k = null;
            v = null;
        }
        return isValid;
    }

    @Override
    public boolean next() {
        if (hasNext()) {
            iterator.next();
            return true;
        }
        return false;
    }
}
