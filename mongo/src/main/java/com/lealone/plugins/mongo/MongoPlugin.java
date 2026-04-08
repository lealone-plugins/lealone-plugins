/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package com.lealone.plugins.mongo;

import java.util.Map;

import com.lealone.common.logging.Logger;
import com.lealone.common.logging.LoggerFactory;
import com.lealone.db.plugin.PluginBase;
import com.lealone.db.plugin.PluginManager;
import com.lealone.plugins.mongo.server.MongoServerEngine;
import com.lealone.server.ProtocolServer;
import com.lealone.server.ProtocolServerEngine;

public class MongoPlugin extends PluginBase {

    private static final Logger logger = LoggerFactory.getLogger(MongoPlugin.class);

    public static final String NAME = "Mongo";

    public MongoPlugin() {
        super(NAME);
    }

    @Override
    public void init(Map<String, String> config) {
        if (isInited())
            return;
        super.init(config);

        ProtocolServerEngine p = new MongoServerEngine();
        p.init(config); // 内部会自己注册
        // PluginManager.register(ProtocolServerEngine.class, p, getName());
    }

    @Override
    public void close() {
        stop();
        ProtocolServerEngine p = getProtocolServerEngine();
        if (p != null)
            PluginManager.deregister(ProtocolServerEngine.class, p);
        super.close();
    }

    @Override
    public void start() {
        if (isStarted())
            return;
        ProtocolServerEngine p = getProtocolServerEngine();
        if (p != null) {
            p.start();
            ProtocolServer ps = p.getProtocolServer();
            logger.info(ps.getName() + " started, host: {}, port: {}", ps.getHost(), ps.getPort());
        }
        super.start();
    }

    @Override
    public void stop() {
        if (isStopped())
            return;
        ProtocolServerEngine p = getProtocolServerEngine();
        if (p != null) {
            p.stop();
            ProtocolServer ps = p.getProtocolServer();
            logger.info(ps.getName() + " stopped");
        }
        super.stop();
    }

    private ProtocolServerEngine getProtocolServerEngine() {
        return PluginManager.getPlugin(ProtocolServerEngine.class, getName());
    }
}
