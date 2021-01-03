package org.debugroom.mynavi.sample.aws.microservice.backend.user.domain;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "service")
public class ServiceProperties {

    CloudFormation cloudFormation = new CloudFormation();

    @Data
    public class CloudFormation{
        Dynamodb dynamodb = new Dynamodb();
        Cognito cognito = new Cognito();
    }

    @Data
    public class SystemsManagerParameterStore{
        Dynamodb dynamodb = new Dynamodb();
        Cognito cognito = new Cognito();
    }

    @Data
    public class Dynamodb{
        String endpoint;
        String region;
    }

    @Data
    public class Cognito{
        String appClientId;
        String appClientSecret;
        String userPoolId;
        String domain;
        String redirectUri;
        String jwkSetUri;
        String region;
    }
}
