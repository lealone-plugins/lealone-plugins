/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package com.lealone.plugins.spring;

import java.io.IOException;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lealone.common.util.CaseInsensitiveMap;
import com.lealone.plugins.service.ServiceHandler;
import com.lealone.plugins.tomcat.TomcatServiceServlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class LealoneServiceController {

    private static Map<String, String> config = new CaseInsensitiveMap<>();
    static {
        config.put("default_database", "lealone");
        config.put("default_schema", "public");
    }

    public static void setConfig(Map<String, String> config) {
        LealoneServiceController.config.putAll(config);
    }

    private TomcatServiceServlet servlet = new TomcatServiceServlet(new ServiceHandler(config));

    @RequestMapping(path = "/service/**", method = { RequestMethod.POST, RequestMethod.GET })
    public String service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 禁用动态编译，spring boot 不支持java compiler api
        return servlet.executeService(request, response, true);
    }
}
