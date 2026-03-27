/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package com.lealone.plugins.memory;

import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentSkipListMap;

import com.lealone.db.value.ValueLong;
import com.lealone.storage.CursorParameters;
import com.lealone.storage.StorageMapBase;
import com.lealone.storage.StorageMapCursor;
import com.lealone.storage.type.StorageDataType;

/**
 * A skipList-based memory map
 * 
 * @param <K> the key class
 * @param <V> the value class
 * 
 * @author zhh
 */
public class MemoryMap<K, V> extends StorageMapBase<K, V> {

    private static class KeyComparator<K> implements java.util.Comparator<K> {
        StorageDataType keyType;

        public KeyComparator(StorageDataType keyType) {
            this.keyType = keyType;
        }

        @Override
        public int compare(K k1, K k2) {
            return keyType.compare(k1, k2);
        }
    }

    protected final ConcurrentSkipListMap<K, V> skipListMap;
    protected boolean closed;

    public MemoryMap(String name, StorageDataType keyType, StorageDataType valueType,
            MemoryStorage memoryStorage) {
        super(name, keyType, valueType, memoryStorage);
        skipListMap = new ConcurrentSkipListMap<>(new KeyComparator<K>(keyType));
    }

    @Override
    public V get(K key) {
        return skipListMap.get(key);
    }

    @Override
    public V put(K key, V value) {
        setMaxKey(key);
        return skipListMap.put(key, value);
    }

    @Override
    public V putIfAbsent(K key, V value) {
        setMaxKey(key);
        return skipListMap.putIfAbsent(key, value);
    }

    @Override
    public V remove(K key) {
        return skipListMap.remove(key);
    }

    @Override
    public K firstKey() {
        try {
            return skipListMap.firstKey();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public K lastKey() {
        try {
            return skipListMap.lastKey();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public K lowerKey(K key) { // 小于给定key的最大key
        try {
            return skipListMap.lowerKey(key);
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public K floorKey(K key) { // 小于或等于给定key的最大key
        try {
            return skipListMap.floorKey(key);
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public K higherKey(K key) { // 大于给定key的最小key
        try {
            return skipListMap.higherKey(key);
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public K ceilingKey(K key) { // 大于或等于给定key的最小key
        try {
            return skipListMap.ceilingKey(key);
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public long size() {
        return skipListMap.size();
    }

    @Override
    public boolean containsKey(K key) {
        return skipListMap.containsKey(key);
    }

    @Override
    public boolean isEmpty() {
        return skipListMap.isEmpty();
    }

    @Override
    public boolean isInMemory() {
        return true;
    }

    @Override
    public StorageMapCursor<K, V> cursor(CursorParameters<K> parameters) {
        return new MemoryMapCursor<>(parameters.from == null ? skipListMap.entrySet().iterator()
                : skipListMap.tailMap(parameters.from).entrySet().iterator());
    }

    @Override
    public void clear() {
        skipListMap.clear();
    }

    @Override
    public void remove() {
        clear();
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

    @Override
    public void close() {
        clear();
        closed = true;
    }

    @Override
    public void save() {
    }

    @Override
    public K append(V value) {
        @SuppressWarnings("unchecked")
        K k = (K) ValueLong.get(skipListMap.size() + 1);
        skipListMap.put(k, value);
        return k;
    }
}
