/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package com.lealone.plugins.python;

import java.io.File;
import java.io.IOException;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.io.IOAccess;

public class PythonServiceTest {

    public static void main(String[] args) {
        try (Context context = Context.newBuilder().allowIO(IOAccess.ALL).build()) {
            Source source;
            try {
                File file = new File("./src/test/resources/python/hello_service.py");
                source = Source.newBuilder("python", file).build();
                context.eval(source);
                Value bindings = context.getBindings("python");
                for (String key : bindings.getMemberKeys()) {
                    System.out.println(key);
                    Value function = bindings.getMember(key);
                    function.canExecute();
                }
                Value function = bindings.getMember("hello");
                String s = function.execute("zhh").asString();
                System.out.println(s);
                s = function.execute("zhh").asString();
                System.out.println(s);
                s = function.execute("zhh").asString();
                System.out.println(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
