/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package com.lealone.plugins.memory;

import java.util.Map;

import com.lealone.storage.StorageBase;
import com.lealone.storage.type.StorageDataType;

public class MemoryStorage extends StorageBase {

    public MemoryStorage() {
        super(null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <K, V> MemoryMap<K, V> openMap(String name, StorageDataType keyType,
            StorageDataType valueType, Map<String, String> parameters) {
        MemoryMap<K, V> map = (MemoryMap<K, V>) maps.get(name);
        if (map == null) {
            synchronized (this) {
                map = (MemoryMap<K, V>) maps.get(name);
                if (map == null) {
                    map = new MemoryMap<>(name, keyType, valueType, this);
                    maps.put(name, map);
                }
            }
        }
        return map;
    }

    @Override
    public String getStoragePath() {
        return null;
    }

    @Override
    public boolean isInMemory() {
        return true;
    }

    @Override
    public String getStorageName() {
        return MemoryStorageEngine.NAME;
    }
}
