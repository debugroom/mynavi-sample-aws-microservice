package org.debugroom.mynavi.sample.aws.microservice.frontend.webapp.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "service")
public class ServiceProperties {

    ApplicationLoadBalancer applicationLoadBalancer = new ApplicationLoadBalancer();
    CloudFormation cloudFormation = new CloudFormation();
    SystemsManagerParameterStore systemsManagerParameterStore = new SystemsManagerParameterStore();

    @Data
    public class ApplicationLoadBalancer{
        String dns;
    }

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
