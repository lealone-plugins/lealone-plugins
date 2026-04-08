/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package com.lealone.plugins.mongo.server;

import com.lealone.plugins.mongo.MongoPlugin;
import com.lealone.server.ProtocolServer;
import com.lealone.server.ProtocolServerEngineBase;

public class MongoServerEngine extends ProtocolServerEngineBase {

    public MongoServerEngine() {
        super(MongoPlugin.NAME);
    }

    @Override
    protected ProtocolServer createProtocolServer() {
        return new MongoServer();
    }
}
