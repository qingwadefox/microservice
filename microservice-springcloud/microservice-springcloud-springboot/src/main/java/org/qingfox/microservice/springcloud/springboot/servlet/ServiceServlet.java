package org.qingfox.microservice.springcloud.springboot.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.qingfox.framework.common.log.ILogger;
import org.qingfox.framework.common.log.LoggerFactory;
import org.qingfox.framework.common.utils.URIUtil;
import org.qingfox.framework.microservice.annotations.Register;
import org.qingfox.framework.microservice.annotations.Service;
import org.qingfox.framework.microservice.context.ServiceRequest;
import org.qingfox.framework.microservice.context.ServiceResponse;
import org.qingfox.framework.microservice.enums.RespCode;
import org.qingfox.framework.microservice.exception.ServiceRegisterException;

import com.alibaba.fastjson.JSONObject;

public class ServiceServlet<T> extends HttpServlet {
    private static final ILogger logger = LoggerFactory.getLogger(ServiceServlet.class);

    private static final long serialVersionUID = -6099344159592596093L;

    private T service;
    private String[] urls;
    private Map<String, Method> methodMap;

    public ServiceServlet(Class<T> _class) {
        super();
        try {
            T service = _class.newInstance();
            this.initService(service);
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error(e);
        }
    }

    public ServiceServlet(T service) {
        super();
        this.initService(service);
    }

    private void initService(T service) {
        Register register = service.getClass().getAnnotation(Register.class);
        if (register == null) {
            throw new ServiceRegisterException("register class - " + service.getClass().getName() + " error : do not have annotation register");
        }

        String parentUrl = URIUtil.appendUrlPath("", register.value());
        parentUrl = URIUtil.appendUrlPath(parentUrl, register.version());

        methodMap = new HashMap<>();
        Method[] methods = service.getClass().getMethods();
        for (Method method : methods) {
            Service serviceAnn = method.getAnnotation(Service.class);
            if (serviceAnn == null) {
                continue;
            }
            String serviceUrl = URIUtil.appendUrlResource(parentUrl, serviceAnn.value());
            methodMap.put(serviceUrl, method);
            urls = ArrayUtils.add(urls, serviceUrl);
        }
        this.service = service;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServiceResponse<Object> serviceResponse = null;
        Method method = methodMap.get(req.getServletPath());

        if (method == null) {
            logger.debug("request method is null");
            serviceResponse = new ServiceResponse<>();
            serviceResponse.setCode(RespCode.UNFINDMETHOD);
        } else {
            logger.debug("request method is - ", method.getName());
            Object[] parameter = new Object[0];
            Object result = null;

            BufferedReader bodyBR = req.getReader();
            String str = null;
            StringBuilder bodyStr = new StringBuilder();
            while ((str = bodyBR.readLine()) != null) {
                bodyStr.append(str + "\n");
            }
            bodyBR.close();
            ServiceRequest serviceRequest = new ServiceRequest(req.getParameterMap(), bodyStr.toString());

            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes != null && parameterTypes.length > 0) {
                for (Class<?> parameterType : parameterTypes) {
                    if (parameterType.equals(HttpServletRequest.class)) {
                        parameter = ArrayUtils.add(parameter, req);
                    } else if (parameterType.equals(HttpServletResponse.class)) {
                        parameter = ArrayUtils.add(parameter, resp);
                    } else if (parameterType.equals(ServiceRequest.class)) {
                        parameter = ArrayUtils.add(parameter, serviceRequest);
                    } else {
                        parameter = ArrayUtils.add(parameter, null);
                    }
                }
            }

            try {
                result = method.invoke(service, parameter);
                if (result == null) {
                    serviceResponse = new ServiceResponse<>();
                } else if (result.getClass().equals(ServiceResponse.class)) {
                    serviceResponse = (ServiceResponse<Object>) result;
                } else {
                    serviceResponse = new ServiceResponse<>();
                    serviceResponse.setResult(result);
                }
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                serviceResponse = new ServiceResponse<>();
                serviceResponse.setCode(RespCode.FAILE);
                serviceResponse.setErrorStack(e);
                logger.error(e);
            }
        }

        resp.getWriter().write(JSONObject.toJSONString(serviceResponse));
    }

    public String[] getUrls() {
        return urls;
    }

}
