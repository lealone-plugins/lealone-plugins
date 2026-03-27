/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package com.lealone.plugins.mvstore;

import org.h2.mvstore.Cursor;
import com.lealone.storage.StorageMapCursor;

public class MVStorageMapCursor<K, V> implements StorageMapCursor<K, V> {

    private final Cursor<K, V> cursor;

    public MVStorageMapCursor(Cursor<K, V> cursor) {
        this.cursor = cursor;
    }

    @Override
    public K getKey() {
        return cursor.getKey();
    }

    @Override
    public V getValue() {
        return cursor.getValue();
    }

    @Override
    public boolean next() {
        if (cursor.hasNext()) {
            cursor.next();
            return true;
        }
        return false;
    }
}
