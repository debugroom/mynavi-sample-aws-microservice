package org.debugroom.mynavi.sample.aws.microservice.backend.user.app.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import org.debugroom.mynavi.sample.aws.microservice.common.apinfra.cloud.aws.log.dynamodb.model.Log;
import org.debugroom.mynavi.sample.aws.microservice.common.apinfra.cloud.aws.log.dynamodb.repository.LogRepository;
import org.debugroom.mynavi.sample.aws.microservice.common.apinfra.util.DateStringUtil;

@Component
public class AuditLoggingInterceptor extends HandlerInterceptorAdapter {

    private static final String HEADER_KEY = "X-Amzn-Trace-Id";

    @Autowired(required = false)
    LogRepository logRepository;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {
        Log log = Log.builder()
                .userId("1")
                .traceId(getTraceId(request))
                .createdAt(DateStringUtil.now())
                .build();

        logRepository.save(log);
    }

    private String getTraceId(HttpServletRequest request){
        String header = request.getHeader(HEADER_KEY);
        String[] headerElements = StringUtils.split(header, ";");
        String rootElements = Arrays.stream(headerElements)
                .filter(headerElement -> headerElement.startsWith("Root="))
                .findFirst().get();
        String[] traceIdElement = StringUtils.split(rootElements, "=");
        return traceIdElement[1];
    }

}
