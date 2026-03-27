/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package com.lealone.plugins.rocksdb;

import java.util.Map;

import com.lealone.db.DataHandler;
import com.lealone.storage.Storage;
import com.lealone.storage.StorageBuilder;
import com.lealone.storage.StorageEngineBase;
import com.lealone.storage.lob.LobStorage;
import org.rocksdb.RocksDB;

public class RocksdbStorageEngine extends StorageEngineBase {

    public static final String NAME = "RocksDB";

    public RocksdbStorageEngine() {
        super(NAME);
    }

    @Override
    public StorageBuilder getStorageBuilder() {
        return new RocksdbStorageBuilder();
    }

    @Override
    public LobStorage getLobStorage(DataHandler dataHandler, Storage storage) {
        return null;
    }

    @Override
    public void init(Map<String, String> config) {
        RocksDB.loadLibrary();
        super.init(config);
    }
}
