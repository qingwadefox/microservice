package org.qingfox.microservice.springcloud.admin;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Configuration;

import de.codecentric.boot.admin.server.config.EnableAdminServer;

/**
 * Hello world!
 *
 */
@Configuration
@EnableAutoConfiguration
@EnableAdminServer
@EnableEurekaClient
public class App {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(App.class);
        Map<String, Object> defaultProperties = new HashMap<>();
        defaultProperties.put("spring.application.name", "eureka");

        defaultProperties.put("server.port", 8081);
//        defaultProperties.put("eureka.instance.hostname", "localhost");
//        defaultProperties.put("eureka.dashboard.path", "/");

//        
//        
////        defaultProperties.put("eureka.server.enableSelfPreservation", "false");
//        
//        defaultProperties.put("spring.freemarker.prefer-file-system-access", "false");
//        defaultProperties.put("eureka.client.registerWithEureka", "false");
//        defaultProperties.put("eureka.client.fetchRegistry", "false");
        defaultProperties.put("eureka.client.serviceUrl.defaultZone", "http://localhost:8080/eureka");
        app.setDefaultProperties(defaultProperties);
        app.run(new String[0]);

    }
}
