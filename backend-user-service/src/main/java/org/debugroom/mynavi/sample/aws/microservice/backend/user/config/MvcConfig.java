package org.debugroom.mynavi.sample.aws.microservice.backend.user.config;

import org.debugroom.mynavi.sample.aws.microservice.backend.user.app.web.interceptor.AuditLoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.debugroom.mynavi.sample.aws.microservice.common.apinfra.exception.CommonExceptionHandler;

@ComponentScan("org.debugroom.mynavi.sample.aws.microservice.backend.user.app.web")
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    AuditLoggingInterceptor auditLoggingInterceptor;

    @Bean
    CommonExceptionHandler commonExceptionHandler(){
        return new CommonExceptionHandler();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(auditLoggingInterceptor);
    }

}
