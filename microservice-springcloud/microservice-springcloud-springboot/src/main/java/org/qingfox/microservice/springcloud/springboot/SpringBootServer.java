package org.qingfox.microservice.springcloud.springboot;

import java.util.HashMap;
import java.util.Map;

import org.qingfox.framework.common.log.ILogger;
import org.qingfox.framework.common.log.LoggerFactory;
import org.qingfox.framework.microservice.MicroserviceServer;
import org.qingfox.microservice.springcloud.springboot.servlet.ServiceServlet;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import   org.apache.catalina.authenticator.jaspic.*;

@SpringBootApplication
public class SpringBootServer extends MicroserviceServer {

    private static final ILogger logger = LoggerFactory.getLogger(SpringBootServer.class);

    @Bean
    ServletWebServerFactory servletWebServerFactory() {
        return new TomcatServletWebServerFactory();
    }

    @Override
    protected void onStart() {
        SpringApplication app = new SpringApplication(this.getClass());
        Map<String, Object> defaultProperties = new HashMap<>();
        defaultProperties.put("server.port", this.getPort());
        app.addInitializers(new ApplicationContextInitializer<ConfigurableApplicationContext>() {
            @Override
            public void initialize(ConfigurableApplicationContext context) {
                for (Class<?> _class : getServiceList()) {
                    ServiceServlet<?> serviceServlet = new ServiceServlet<>(_class);
                    ServletRegistrationBean<ServiceServlet<?>> serviceServletRegistrationBean = new ServletRegistrationBean<>(serviceServlet, serviceServlet.getUrls());
                    serviceServletRegistrationBean.setServlet(serviceServlet);
                    context.getBeanFactory().registerSingleton(_class.getName(), serviceServletRegistrationBean);
                }
            } 
        });
      
        app.setDefaultProperties(defaultProperties);
        app.run(new String[0]);
    }

}
