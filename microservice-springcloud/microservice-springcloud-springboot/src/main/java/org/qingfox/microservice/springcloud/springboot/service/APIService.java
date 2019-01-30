package org.qingfox.microservice.springcloud.springboot.service;

import javax.servlet.http.HttpServletRequest;

import org.qingfox.framework.microservice.annotations.Register;
import org.qingfox.framework.microservice.annotations.Service;
import org.qingfox.framework.microservice.context.ServiceRequest;

@Register("api")
public class APIService {

    @Service("test")
    public String test(String test, ServiceRequest request) {
        return "test - " + test + " request - " + request.getBodyString("s");
    }

}
