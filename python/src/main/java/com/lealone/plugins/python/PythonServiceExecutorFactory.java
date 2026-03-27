/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package com.lealone.plugins.python;

import java.io.File;

import com.lealone.db.service.Service;
import com.lealone.db.service.ServiceExecutorFactoryBase;

public class PythonServiceExecutorFactory extends ServiceExecutorFactoryBase {

    public PythonServiceExecutorFactory() {
        super("python");
    }

    @Override
    public PythonServiceExecutor createServiceExecutor(Service service) {
        return new PythonServiceExecutor(service);
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
