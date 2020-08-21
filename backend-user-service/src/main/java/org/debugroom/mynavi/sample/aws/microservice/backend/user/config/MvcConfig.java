package org.debugroom.mynavi.sample.aws.microservice.backend.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.debugroom.mynavi.sample.aws.microservice.common.apinfra.exception.CommonExceptionHandler;

@ComponentScan("org.debugroom.mynavi.sample.aws.microservice.backend.user.app.web")
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Bean
    CommonExceptionHandler commonExceptionHandler(){
        return new CommonExceptionHandler();
    }
}
