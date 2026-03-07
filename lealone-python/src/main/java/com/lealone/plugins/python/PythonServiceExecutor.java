/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package com.lealone.plugins.python;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.io.IOAccess;
import com.lealone.common.exceptions.DbException;
import com.lealone.common.util.CamelCaseHelper;
import com.lealone.db.service.Service;
import com.lealone.db.service.ServiceExecutorBase;
import com.lealone.db.service.ServiceMethod;
import com.lealone.db.value.Value;
import com.lealone.db.value.ValueNull;
import com.lealone.db.value.ValueString;

public class PythonServiceExecutor extends ServiceExecutorBase {

    private final Map<String, org.graalvm.polyglot.Value> functionMap;
    private Context context;

    public PythonServiceExecutor(Service service) {
        int size = service.getServiceMethods().size();
        serviceMethodMap = new HashMap<>(size);
        functionMap = new HashMap<>(size);

        Source source;
        org.graalvm.polyglot.Value bindings;
        ClassLoader cl = PythonServiceExecutor.class.getClassLoader();
        // 要切换ClassLoader，否则会出现以下错误:
        // No language and polyglot implementation was found on the classpath.
        ClassLoader currentContextClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(cl);
        try {
            context = Context.newBuilder().allowIO(IOAccess.ALL).allowHostAccess(HostAccess.ALL)
                    // allows access to all Java classes
                    .allowHostClassLookup(className -> true).build();
            source = Source.newBuilder("python", new File(service.getImplementBy())).build();
            context.eval(source);
            bindings = context.getBindings("python");
            if (size <= 0) {
                for (String key : bindings.getMemberKeys()) {
                    org.graalvm.polyglot.Value function = bindings.getMember(key);
                    if (function.canExecute())
                        functionMap.put(key.toUpperCase(), function);
                }
            } else {
                for (ServiceMethod serviceMethod : service.getServiceMethods()) {
                    String serviceMethodName = serviceMethod.getMethodName();
                    serviceMethodMap.put(serviceMethodName, serviceMethod);

                    String functionName = CamelCaseHelper.toCamelFromUnderscore(serviceMethodName);
                    try {
                        functionMap.put(serviceMethodName, bindings.getMember(functionName));
                    } catch (Exception e) {
                        throw new RuntimeException("Function not found: " + functionName, e);
                    }
                }
            }
        } catch (IOException e) {
            throw DbException.convert(e);
        } finally {
            Thread.currentThread().setContextClassLoader(currentContextClassLoader);
        }
    }

    @Override
    public Value executeService(String methodName, Value[] methodArgs) {
        Object[] args = getServiceMethodArgs(methodName, methodArgs);
        org.graalvm.polyglot.Value function = functionMap.get(methodName);
        try {
            String ret = function.execute(args).toString();
            if (ret == null)
                return ValueNull.INSTANCE;
            return ValueString.get(ret);
        } catch (Exception e) {
            throw DbException.convert(e);
        }
    }

    @Override
    public String executeService(String methodName, Map<String, Object> methodArgs) {
        Object[] args = getServiceMethodArgs(methodName, methodArgs);
        org.graalvm.polyglot.Value function = functionMap.get(methodName);
        try {
            String ret = function.execute(args).toString();
            if (ret == null)
                return null;
            return ret;
        } catch (Exception e) {
            throw DbException.convert(e);
        }
    }

    @Override
    public String executeService(String methodName, String json) {
        Object[] args = getServiceMethodArgs(methodName, json);
        org.graalvm.polyglot.Value function = functionMap.get(methodName);
        try {
            String ret = function.execute(args).toString();
            if (ret == null)
                return null;
            return ret;
        } catch (Exception e) {
            throw DbException.convert(e);
        }
    }
}
