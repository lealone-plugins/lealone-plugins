/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package com.lealone.plugins.spring;

import java.io.File;
import java.net.InetAddress;

import org.apache.catalina.startup.Tomcat;
import com.lealone.common.util.CaseInsensitiveMap;
import com.lealone.plugins.tomcat.TomcatServer;
import com.lealone.plugins.tomcat.TomcatServerEngine;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.ServletContextInitializer;

public class LealoneTomcatWebServerFactory extends TomcatServletWebServerFactory {

    public LealoneTomcatWebServerFactory() {
        this(TomcatServer.DEFAULT_PORT);
    }

    public LealoneTomcatWebServerFactory(int port) {
        super(port);
    }

    public LealoneTomcatWebServerFactory(String contextPath, int port) {
        super(contextPath, port);
    }

    @Override
    public WebServer getWebServer(ServletContextInitializer... initializers) {
        CaseInsensitiveMap<String> config = new CaseInsensitiveMap<>();
        TomcatServer server = (TomcatServer) new TomcatServerEngine().getProtocolServer();
        InetAddress address = getAddress();
        server.setHost(address != null ? address.getHostName() : "127.0.0.1");
        server.setPort(getPort());
        File webRoot = getDocumentRoot();
        if (webRoot == null) {
            webRoot = new File("./target/web");
            if (!webRoot.exists())
                webRoot.mkdirs();
            setDocumentRoot(webRoot.getAbsoluteFile());
            server.setWebRoot(webRoot.getAbsolutePath());
        }
        config.put("base_dir", webRoot.getAbsolutePath());
        config.put("context_path", "/");
        config.put("init_tomcat", "false");
        // setContextPath("");
        server.init(config);
        Tomcat tomcat = server.getTomcat();
        prepareContext(tomcat.getHost(), initializers);
        return new LealoneTomcatWebServer(server);
    }
}
