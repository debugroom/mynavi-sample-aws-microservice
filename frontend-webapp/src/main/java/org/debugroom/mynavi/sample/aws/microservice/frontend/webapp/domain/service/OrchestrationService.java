package org.debugroom.mynavi.sample.aws.microservice.frontend.webapp.domain.service;

import java.util.List;

import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;

import org.debugroom.mynavi.sample.aws.microservice.common.apinfra.exception.BusinessException;
import org.debugroom.mynavi.sample.aws.microservice.common.model.CredentialResource;
import org.debugroom.mynavi.sample.aws.microservice.common.model.UserResource;

public interface OrchestrationService {

    public UserResource getUserResource(String loginId) throws BusinessException;

    public List<CredentialResource> addTokens(OidcIdToken oidcIdToken
            , OAuth2AccessToken oAuth2AccessToken, OAuth2RefreshToken oAuth2RefreshToken) throws BusinessException;

}
