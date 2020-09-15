package org.debugroom.mynavi.sample.aws.microservice.backend.user.config;

import com.amazonaws.xray.AWSXRay;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import org.debugroom.mynavi.sample.aws.microservice.common.apinfra.cloud.aws.CloudFormationStackResolver;

@SpringBootApplication
public class App {

    public static void main(String[] args) {
        AWSXRay.beginSegment("backend-user-service-init");
        SpringApplication.run(App.class, args);
    }

    @Bean
    CloudFormationStackResolver cloudFormationStackResolver(){
        return new CloudFormationStackResolver();
    }

}
