package org.qingfox.microservice.springcloud.springboot;

import java.io.IOException;

import org.qingfox.framework.common.utils.ThreadUtil;
import org.qingfox.framework.microservice.IServer;

public class Test {
  public static void main(String[] args) throws IOException {
    IServer server = new SpringBootServer();
    server.setPort(8080);
    server.setScanPackage("org.qingfox.microservice");
    server.start();
  }
}
