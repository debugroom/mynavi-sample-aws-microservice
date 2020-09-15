package org.debugroom.mynavi.sample.aws.microservice.frontend.webapp.config;

import com.amazonaws.xray.AWSXRay;
import org.debugroom.mynavi.sample.aws.microservice.common.apinfra.cloud.aws.CloudFormationStackResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WebApp {

    public static void main(String[] args) {
        AWSXRay.beginSegment("frontend-webapp-init");
        SpringApplication.run(WebApp.class, args);
    }

    @Bean
    CloudFormationStackResolver cloudFormationStackResolver(){
        return new CloudFormationStackResolver();
    }

}
