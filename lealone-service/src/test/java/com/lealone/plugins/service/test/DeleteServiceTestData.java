/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package com.lealone.plugins.service.test;

import java.io.IOException;

import com.lealone.test.misc.DeleteTestData;

public class DeleteServiceTestData {

    public static void main(String[] args) {
        try {
            DeleteTestData.main(args);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
