package org.debugroom.mynavi.sample.aws.microservice.backend.user.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.debugroom.mynavi.sample.aws.microservice.backend.user.domain.ServiceProperties;
import org.debugroom.mynavi.sample.aws.microservice.common.apinfra.cloud.aws.CloudFormationStackResolver;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@EnableWebSecurity
public class CognitoAuthorizationConfig extends WebSecurityConfigurerAdapter {

    ServiceProperties serviceProperties;

    CloudFormationStackResolver cloudFormationStackResolver;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String jwkSetUri = cloudFormationStackResolver.getExportValue(
                serviceProperties.getCloudFormation().getCognito().getJwkSetUri());
        http.authorizeRequests(authroze -> authroze.anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(
                        jwt -> jwt.jwkSetUri(jwkSetUri)));
    }

}
