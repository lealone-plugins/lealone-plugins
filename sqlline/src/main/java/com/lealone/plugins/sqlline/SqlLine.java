/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package com.lealone.plugins.sqlline;

import java.io.IOException;

import com.lealone.db.ConnectionInfo;
import com.lealone.main.Lealone;

public class SqlLine extends sqlline.SqlLine {

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            args = new String[8];
            args[0] = "-ac";
            args[1] = SqlLineApplication.class.getName();
            args[2] = "-u";
            args[3] = "jdbc:lealone:tcp://127.0.0.1:9210/lealone";
            args[4] = "-n";
            args[5] = "root";
            args[6] = "-p";
            args[7] = "";
        } else {
            boolean existsAc = false;
            for (int i = 0; args != null && i < args.length; i++) {
                String arg = args[i];
                arg = arg.trim();
                if (arg.isEmpty())
                    continue;
                if (arg.equals("-u")) {
                    String url = args[++i];
                    java.util.Properties info = new java.util.Properties();
                    ConnectionInfo ci = new ConnectionInfo(url, info);
                    if (ci.isEmbedded()) {
                        Lealone.embed();
                    }
                } else if (arg.equals("-ac")) {
                    existsAc = true;
                }
            }
            if (!existsAc) {
                String[] newArgs = new String[args.length + 2];
                newArgs[0] = "-ac";
                newArgs[1] = SqlLineApplication.class.getName();
                System.arraycopy(args, 0, newArgs, 2, args.length);
                args = newArgs;
            }
        }
        sqlline.SqlLine.main(args);
    }
}
