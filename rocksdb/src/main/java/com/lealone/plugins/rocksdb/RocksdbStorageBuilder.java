/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package com.lealone.plugins.rocksdb;

import com.lealone.storage.StorageBuilder;

public class RocksdbStorageBuilder extends StorageBuilder {
    @Override
    public RocksdbStorage openStorage() {
        return new RocksdbStorage(config);
    }
}
