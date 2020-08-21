package org.debugroom.mynavi.sample.aws.microservice.frontend.webapp.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "service")
public class ServiceProperties {

    private String dns;

}
