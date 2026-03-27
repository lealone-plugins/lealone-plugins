/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package com.lealone.plugins.mvstore;

import org.h2.mvstore.MVMap;
import org.h2.mvstore.MVStore;

import com.lealone.db.value.ValueLong;
import com.lealone.storage.CursorParameters;
import com.lealone.storage.Storage;
import com.lealone.storage.StorageMapBase;
import com.lealone.storage.StorageMapCursor;
import com.lealone.storage.type.StorageDataType;

public class MVStorageMap<K, V> extends StorageMapBase<K, V> {

    private final MVMap<K, V> mvMap;

    public MVStorageMap(String name, StorageDataType keyType, StorageDataType valueType, Storage storage,
            MVMap<K, V> mvMap) {
        super(name, keyType, valueType, storage);
        this.mvMap = mvMap;
        setMaxKey(lastKey());
    }

    @Override
    public int hashCode() {
        return mvMap.hashCode();
    }

    @Override
    public V get(K key) {
        return mvMap.get(key);
    }

    @Override
    public V put(K key, V value) {
        return mvMap.put(key, value);
    }

    @Override
    public V putIfAbsent(K key, V value) {
        return mvMap.putIfAbsent(key, value);
    }

    @Override
    public String getName() {
        return mvMap.getName();
    }

    @Override
    public V remove(K key) {
        return mvMap.remove(key);
    }

    @Override
    public K firstKey() {
        return mvMap.firstKey();
    }

    @Override
    public K lastKey() {
        return mvMap.lastKey();
    }

    @Override
    public K lowerKey(K key) {
        return mvMap.lowerKey(key);
    }

    @Override
    public K floorKey(K key) {
        return mvMap.floorKey(key);
    }

    @Override
    public boolean equals(Object obj) {
        return mvMap.equals(obj);
    }

    @Override
    public K higherKey(K key) {
        return mvMap.higherKey(key);
    }

    @Override
    public K ceilingKey(K key) {
        return mvMap.ceilingKey(key);
    }

    @Override
    public long size() {
        return mvMap.sizeAsLong();
    }

    @Override
    public boolean containsKey(K key) {
        return mvMap.containsKey(key);
    }

    @Override
    public boolean isEmpty() {
        return mvMap.isEmpty();
    }

    @Override
    public boolean isInMemory() {
        return mvMap.isVolatile();
    }

    @Override
    public StorageMapCursor<K, V> cursor(CursorParameters<K> parameters) {
        return new MVStorageMapCursor<>(mvMap.cursor(parameters.from));
    }

    @Override
    public void clear() {
        mvMap.clear();
    }

    @Override
    public void remove() {
        mvMap.clear();
    }

    @Override
    public boolean isClosed() {
        return !storage.hasMap(name);
    }

    @Override
    public void close() {
        storage.closeMap(name);
        // MVMap的close不是public的，并且没实际用处
    }

    @Override
    public void save() {
        MVStore store = mvMap.getStore();
        if (!store.isClosed())
            store.commit();
    }

    @Override
    public K append(V value) {
        @SuppressWarnings("unchecked")
        K k = (K) ValueLong.get(mvMap.size() + 1);
        mvMap.append(k, value);
        return k;
    }
}
