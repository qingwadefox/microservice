package org.qingfox.microservice.springcloud.eureka;

import java.util.HashMap;
import java.util.Map;

import org.qingfox.framework.microservice.MicroserviceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableEurekaServer
public class SpringEurekaServer extends MicroserviceServer {

    @Override
    protected void onStart() {
        SpringApplication app = new SpringApplication(this.getClass());
        Map<String, Object> defaultProperties = new HashMap<>();
        defaultProperties.put("spring.application.name", "eureka");
        defaultProperties.put("server.port", this.getPort());
        defaultProperties.put("eureka.instance.hostname", "localhost");
//
////        defaultProperties.put("eureka.server.enableSelfPreservation", "false");
//
        defaultProperties.put("spring.freemarker.prefer-file-system-access", "false");
        defaultProperties.put("eureka.client.registerWithEureka", "false");
        defaultProperties.put("eureka.client.fetchRegistry", "false");
        defaultProperties.put("eureka.client.serviceUrl.defaultZone", "http://${eureka.instance.hostname}:${server.port}/eureka");
        app.setDefaultProperties(defaultProperties);
        app.run(new String[0]);
    }

}
