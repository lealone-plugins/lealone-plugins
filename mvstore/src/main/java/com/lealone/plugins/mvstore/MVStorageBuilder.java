/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package com.lealone.plugins.mvstore;

import org.h2.mvstore.MVStore;
import com.lealone.storage.StorageBuilder;

public class MVStorageBuilder extends StorageBuilder {

    private final MVStore.Builder builder = new MVStore.Builder();

    @Override
    public MVStorage openStorage() {
        // 禁用自动提交，交由上层的事务引擎负责
        builder.autoCommitDisabled();
        return new MVStorage(builder.open(), config);
    }

    @Override
    public StorageBuilder storagePath(String storagePath) {
        builder.fileName(storagePath + ".db");
        return super.storagePath(storagePath);
    }

    @Override
    public StorageBuilder encryptionKey(char[] password) {
        builder.encryptionKey(password);
        return super.encryptionKey(password);
    }

    @Override
    public StorageBuilder readOnly() {
        builder.readOnly();
        return super.readOnly();
    }

    @Override
    public StorageBuilder inMemory() {
        return super.inMemory();
    }

    @Override
    public StorageBuilder cacheSize(int mb) {
        builder.cacheSize(mb);
        return super.cacheSize(mb);
    }

    @Override
    public StorageBuilder compress() {
        builder.compress();
        return super.compress();
    }

    @Override
    public StorageBuilder compressHigh() {
        builder.compressHigh();
        return super.compressHigh();
    }

    @Override
    public StorageBuilder pageSize(int pageSize) {
        builder.pageSplitSize(pageSize);
        return super.pageSize(pageSize);
    }
}
