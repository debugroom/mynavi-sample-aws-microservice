package org.debugroom.mynavi.sample.aws.microservice.lambda.app;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "service")
public class ServiceProperties {

    CloudFormation cloudFormation = new CloudFormation();
    SystemsManagerParameterStore systemsManagerParameterStore = new SystemsManagerParameterStore();

    @Data
    public class CloudFormation{
        Cognito cognito = new Cognito();
    }

    @Data
    public class SystemsManagerParameterStore{
        Cognito cognito = new Cognito();
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
