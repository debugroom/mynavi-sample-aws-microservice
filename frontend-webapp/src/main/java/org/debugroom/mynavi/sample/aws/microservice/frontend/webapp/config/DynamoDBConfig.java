package org.debugroom.mynavi.sample.aws.microservice.frontend.webapp.config;

import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableDynamoDBRepositories(
        basePackages = "org.debugroom.mynavi.sample.aws.microservice.common.apinfra.cloud.aws.log.dynamodb.repository"
)
public class DynamoDBConfig {
}
