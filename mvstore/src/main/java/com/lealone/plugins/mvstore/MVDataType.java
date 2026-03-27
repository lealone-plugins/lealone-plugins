/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package com.lealone.plugins.mvstore;

import java.nio.ByteBuffer;

import org.h2.mvstore.WriteBuffer;

import com.lealone.db.DataBuffer;
import com.lealone.storage.type.StorageDataType;

public class MVDataType extends org.h2.mvstore.type.BasicDataType<Object> {

    private final StorageDataType type;

    public MVDataType(StorageDataType type) {
        this.type = type;
    }

    @Override
    public int compare(Object a, Object b) {
        return type.compare(a, b);
    }

    @Override
    public int getMemory(Object obj) {
        return type.getMemory(obj);
    }

    @Override
    public void write(WriteBuffer buff, Object obj) {
        DataBuffer db = DataBuffer.create();
        type.write(db, obj);
        buff.put(db.getAndFlipBuffer());
    }

    @Override
    public void write(WriteBuffer buff, Object obj, int len) {
        DataBuffer db = DataBuffer.create();
        for (int i = 0; i < len; i++) {
            db.reset();
            type.write(db, cast(obj)[i]);
            buff.put(db.getAndFlipBuffer());
        }
    }

    @Override
    public Object read(ByteBuffer buff) {
        return type.read(buff);
    }

    @Override
    public void read(ByteBuffer buff, Object obj, int len) {
        for (int i = 0; i < len; i++) {
            cast(obj)[i] = read(buff);
        }
    }

    @Override
    public Object[] createStorage(int size) {
        return new Object[size];
    }
}
