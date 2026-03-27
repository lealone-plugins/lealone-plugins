/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package com.lealone.plugins.mvstore;

import com.lealone.db.DataHandler;
import com.lealone.storage.Storage;
import com.lealone.storage.StorageBuilder;
import com.lealone.storage.StorageEngineBase;
import com.lealone.storage.lob.LobStorage;

public class MVStorageEngine extends StorageEngineBase {

    public static final String NAME = "MVStore";

    public MVStorageEngine() {
        super(NAME);
    }

    @Override
    public StorageBuilder getStorageBuilder() {
        return new MVStorageBuilder();
    }

    @Override
    public LobStorage getLobStorage(DataHandler dataHandler, Storage storage) {
        return null;
    }
}
