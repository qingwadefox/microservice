package org.qingfox.microservice.springcloud.eureka;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@EnableEurekaServer
@SpringBootApplication
public class SpringEurekaTest {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SpringEurekaTest.class, args);
    }

}
