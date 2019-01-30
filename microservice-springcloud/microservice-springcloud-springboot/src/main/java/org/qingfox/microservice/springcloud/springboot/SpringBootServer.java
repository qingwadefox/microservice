package org.qingfox.microservice.springcloud.springboot;

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
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SpringBootServer extends MicroserviceServer {

    private static final ILogger logger = LoggerFactory.getLogger(SpringBootServer.class);

    @Override
    protected void onStart() {
        SpringApplication app = new SpringApplication(this.getClass());
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
        app.run(new String[0]);

    }

}
