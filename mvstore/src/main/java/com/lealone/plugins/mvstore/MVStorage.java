/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package com.lealone.plugins.mvstore;

import java.util.Map;

import org.h2.mvstore.MVMap;
import org.h2.mvstore.MVStore;
import com.lealone.storage.StorageBase;
import com.lealone.storage.StorageMap;
import com.lealone.storage.type.StorageDataType;

public class MVStorage extends StorageBase {

    private final MVStore mvStore;

    public MVStorage(MVStore mvStore, Map<String, Object> config) {
        super(config);
        this.mvStore = mvStore;
    }

    @Override
    public <K, V> StorageMap<K, V> openMap(String name, StorageDataType keyType,
            StorageDataType valueType, Map<String, String> parameters) {
        MVMap.Builder<K, V> builder = new MVMap.Builder<>();
        builder.keyType(new MVDataType(keyType));
        builder.valueType(new MVDataType(valueType));
        MVMap<K, V> mvMap = mvStore.openMap(name, builder);
        MVStorageMap<K, V> map = new MVStorageMap<>(name, keyType, valueType, this, mvMap);
        maps.put(name, map);
        return map;
    }

    @Override
    public void save() {
        mvStore.commit();
    }

    @Override
    public void close() {
        super.close();
        mvStore.close();
    }

    @Override
    public void closeImmediately() {
        super.closeImmediately();
        mvStore.closeImmediately();
    }

    @Override
    public String getStorageName() {
        return MVStorageEngine.NAME;
    }
}
