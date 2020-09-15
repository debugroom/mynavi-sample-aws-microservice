package org.debugroom.mynavi.sample.aws.microservice.frontend.webapp.app.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.Optional;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.entities.Segment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import org.debugroom.mynavi.sample.aws.microservice.common.apinfra.cloud.aws.log.dynamodb.model.Log;
import org.debugroom.mynavi.sample.aws.microservice.common.apinfra.cloud.aws.log.dynamodb.repository.LogRepository;
import org.debugroom.mynavi.sample.aws.microservice.common.apinfra.util.DateStringUtil;
import org.debugroom.mynavi.sample.aws.microservice.frontend.webapp.app.web.security.CustomUserDetails;

@Component
public class AuditLoggingInterceptor extends HandlerInterceptorAdapter {

    @Autowired(required = false)
    LogRepository logRepository;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {
        String userId = "0";
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if(Objects.nonNull(authentication)){
            Object principal = authentication.getPrincipal();
            if(principal instanceof CustomUserDetails){
               userId = ((CustomUserDetails)principal)
                       .getUserResource().getUserId();
            }
        }
        Log log = Log.builder()
                .userId(userId)
                .createdAt(DateStringUtil.now())
                .traceId(getTraceId())
                .build();

        logRepository.save(log);
    }

    private String getTraceId(){
        Optional<Segment> segment = AWSXRay.getCurrentSegmentOptional();
        if (segment.isPresent()){
            return segment.get().getTraceId().toString();
        }
        return null;
    }

}
