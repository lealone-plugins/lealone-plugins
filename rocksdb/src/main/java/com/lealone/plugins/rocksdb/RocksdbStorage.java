/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package com.lealone.plugins.rocksdb;

import java.util.Map;

import com.lealone.storage.StorageBase;
import com.lealone.storage.StorageMap;
import com.lealone.storage.type.StorageDataType;

public class RocksdbStorage extends StorageBase {

    public RocksdbStorage(Map<String, Object> config) {
        super(config);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <K, V> StorageMap<K, V> openMap(String name, StorageDataType keyType,
            StorageDataType valueType, Map<String, String> parameters) {
        StorageMap<?, ?> map = maps.get(name);
        if (map == null) {
            synchronized (this) {
                map = maps.get(name);
                if (map == null) {
                    map = new RocksdbStorageMap<>(name, keyType, valueType, this);
                    maps.put(name, map);
                }
            }
        }
        return (StorageMap<K, V>) map;
    }

    @Override
    public String getStorageName() {
        return RocksdbStorageEngine.NAME;
    }

    @Override
    public boolean isByteStorage() {
        return true;
    }
}
