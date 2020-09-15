package org.debugroom.mynavi.sample.aws.microservice.frontend.webapp.config;

import org.debugroom.mynavi.sample.aws.microservice.frontend.webapp.app.web.interceptor.AuditLoggingInterceptor;
import org.debugroom.mynavi.sample.aws.microservice.frontend.webapp.app.web.interceptor.SetMenuInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@ComponentScan("org.debugroom.mynavi.sample.aws.microservice.frontend.webapp.app.web")
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    AuditLoggingInterceptor auditLoggingInterceptor;

    @Bean
    public SetMenuInterceptor setMenuInterceptor(){
        return new SetMenuInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(setMenuInterceptor());
        registry.addInterceptor(auditLoggingInterceptor);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/login");
    }

}
