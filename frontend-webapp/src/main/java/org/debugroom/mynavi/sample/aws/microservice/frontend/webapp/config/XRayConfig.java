package org.debugroom.mynavi.sample.aws.microservice.frontend.webapp.config;

import java.io.IOException;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClientBuilder;
import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.AWSXRayRecorderBuilder;
import com.amazonaws.xray.handlers.TracingHandler;
import com.amazonaws.xray.javax.servlet.AWSXRayServletFilter;
import com.amazonaws.xray.spring.aop.AbstractXRayInterceptor;
import com.amazonaws.xray.strategy.sampling.LocalizedSamplingStrategy;

import lombok.AllArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.debugroom.mynavi.sample.aws.microservice.frontend.webapp.domain.ServiceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.util.ResourceUtils;

import org.debugroom.mynavi.sample.aws.microservice.common.apinfra.cloud.aws.CloudFormationStackResolver;

@AllArgsConstructor
@Aspect
@Configuration
@EnableAspectJAutoProxy
public class XRayConfig extends AbstractXRayInterceptor {

    ServiceProperties serviceProperties;

    CloudFormationStackResolver cloudFormationStackResolver;

    static {
        try{
            AWSXRayRecorderBuilder builder = AWSXRayRecorderBuilder.standard()
                    .withSamplingStrategy(new LocalizedSamplingStrategy(
                            ResourceUtils.getURL("classpath:sampling-rules.json")));
            AWSXRay.setGlobalRecorder(builder.build());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Bean
    public AWSXRayServletFilter awsXrayServletFitler(){
        return new AWSXRayServletFilter("MynaviSampleMicroServiceFrontendApp");
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(awsXrayServletFitler());
        filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return filterRegistrationBean;
    }

    @Bean
    AmazonDynamoDB amazonDynamoDB(){
        String endpoint = cloudFormationStackResolver.getExportValue(
                serviceProperties.getCloudFormation().getDynamodb().getEndpoint());
        String region = cloudFormationStackResolver.getExportValue(
                serviceProperties.getCloudFormation().getDynamodb().getRegion());
        return AmazonDynamoDBAsyncClientBuilder.standard()
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(endpoint,region))
                .withRequestHandlers(new TracingHandler(AWSXRay.getGlobalRecorder()))
                .build();
    }

    @Override
    @Pointcut("@within(com.amazonaws.xray.spring.aop.XRayEnabled) " +
            " && execution(* org.debugroom.mynavi.sample.aws.microservice..*.*(..))" )
    protected void xrayEnabledClasses() {
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onStartup(){
        AWSXRay.endSegment();
    }


}
