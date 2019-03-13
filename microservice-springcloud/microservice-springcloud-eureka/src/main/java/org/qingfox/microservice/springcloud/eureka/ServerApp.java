package org.qingfox.microservice.springcloud.eureka;

import java.io.IOException;

import org.qingfox.framework.microservice.IServer;

/**
 * Hello world!
 *
 */
public class ServerApp {
    public static void main(String[] args) {
        IServer server = new SpringEurekaServer();
        server.setPort(8080);
        server.setScanPackage("org.qingfox.microservice");
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
 