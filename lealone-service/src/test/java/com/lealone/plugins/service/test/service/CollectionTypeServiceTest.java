/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package com.lealone.plugins.service.test.service;

import org.junit.Test;
import com.lealone.db.LealoneDatabase;
import com.lealone.test.sql.SqlTestBase;

import com.lealone.plugins.service.test.orm.SqlScript;

public class CollectionTypeServiceTest extends SqlTestBase {

    public CollectionTypeServiceTest() {
        super(LealoneDatabase.NAME);
    }

    @Test
    public void testService() throws Exception {
        SqlScript.createCollectionTypeService(this);
    }
}
