/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package com.lealone.plugins.tomcat.test;

import java.util.Map;

import com.lealone.main.Lealone;
import com.lealone.plugins.service.http.HttpServer;
import com.lealone.plugins.tomcat.TomcatRouter;
import com.lealone.plugins.tomcat.TomcatServer;
import com.lealone.plugins.tomcat.test.EmbedTomcatStart.VirtualThreadTestServlet;

public class LealoneTomcatStart extends TomcatRouter {

    // http://localhost:8080/index.html
    public static void main(String[] args) {
        Lealone.main(args);
    }

    @Override
    public void init(HttpServer server, Map<String, String> config) {
        super.init(server, config);
        TomcatServer tomcatServer = (TomcatServer) server;
        tomcatServer.addServlet("virtualThreadTestServlet", new VirtualThreadTestServlet());
        tomcatServer.addServletMappingDecoded("/test", "virtualThreadTestServlet");
    }
}
