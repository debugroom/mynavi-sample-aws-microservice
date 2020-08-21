package org.debugroom.mynavi.sample.aws.microservice.frontend.webapp.config;

import org.debugroom.mynavi.sample.aws.microservice.frontend.webapp.domain.ServiceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;

@Profile("dev")
@Configuration
public class DevConfig {

    @Autowired
    ServiceProperties serviceProperties;

    @Bean
    public WebClient userWebClient(){
        return WebClient.builder()
                .baseUrl(serviceProperties.getDns())
                .build();
    }

}
