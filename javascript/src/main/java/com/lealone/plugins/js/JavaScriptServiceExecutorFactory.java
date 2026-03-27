/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package com.lealone.plugins.js;

import java.io.File;

import com.lealone.db.service.Service;
import com.lealone.db.service.ServiceExecutorFactoryBase;

public class JavaScriptServiceExecutorFactory extends ServiceExecutorFactoryBase {

    public JavaScriptServiceExecutorFactory() {
        super("js");
    }

    @Override
    public JavaScriptServiceExecutor createServiceExecutor(Service service) {
        return new JavaScriptServiceExecutor(service);
    }

    @Override
    public boolean supportsGenCode() {
        return true;
    }

    @Override
    public void genCode(Service service) {
        if (new File(service.getImplementBy()).exists()) {
            return;
        }
    }
}
