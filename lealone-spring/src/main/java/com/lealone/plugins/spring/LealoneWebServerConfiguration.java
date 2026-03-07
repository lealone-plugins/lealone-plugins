/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package com.lealone.plugins.spring;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class LealoneWebServerConfiguration {
    @Bean
    public LealoneTomcatWebServerFactory embeddedServletContainerFactory() {
        return new LealoneTomcatWebServerFactory();
    }
}
