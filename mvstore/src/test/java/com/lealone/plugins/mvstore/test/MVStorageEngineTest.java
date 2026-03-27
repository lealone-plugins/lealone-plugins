/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package com.lealone.plugins.mvstore.test;

import org.junit.Test;

import com.lealone.db.plugin.PluginManager;
import com.lealone.plugins.mvstore.MVStorageEngine;
import com.lealone.storage.Storage;
import com.lealone.storage.StorageBuilder;
import com.lealone.storage.StorageEngine;
import com.lealone.storage.StorageMap;
import com.lealone.test.TestBase;

public class MVStorageEngineTest extends TestBase {

    @Test
    public void run() {
        Storage storage = getStorage(MVStorageEngine.NAME);
        // storage.openMap("test", null).remove();
        testMap(storage);
        testAsyncMap(storage);
    }

    private static void testMap(Storage storage) {
        StorageMap<String, Integer> map = storage.openMap("test", null);
        map.put("a", 100);
        map.put("b", 200);

        Integer v = map.get("a");
        System.out.println(v);

        map.cursor().forEachRemaining(k -> {
            System.out.println(map.get(k));
        });

        map.save();
    }

    private static void testAsyncMap(Storage storage) {
        StorageMap<String, Integer> map = storage.openMap("test", null);
        map.put("c", 300, ac -> {
            System.out.println("Async old value: " + ac.getResult());
        });
        map.put("d", 400, ac -> {
            System.out.println("Async old value: " + ac.getResult());
        });
        Integer v = map.get("c");
        System.out.println(v);

        map.cursor().forEachRemaining(k -> {
            System.out.println(map.get(k));
        });

        map.save();
    }

    private static Storage getStorage(String name) {
        StorageEngine se = PluginManager.getPlugin(StorageEngine.class, name);
        StorageBuilder builder = se.getStorageBuilder();
        String dir = joinDirs(name);
        builder.storagePath(dir);
        Storage storage = builder.openStorage();
        return storage;
    }
}
