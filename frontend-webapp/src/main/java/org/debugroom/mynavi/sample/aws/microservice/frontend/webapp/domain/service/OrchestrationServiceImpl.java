package org.debugroom.mynavi.sample.aws.microservice.frontend.webapp.domain.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.amazonaws.xray.spring.aop.XRayEnabled;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.stereotype.Service;

import org.debugroom.mynavi.sample.aws.microservice.common.apinfra.exception.BusinessException;
import org.debugroom.mynavi.sample.aws.microservice.common.model.CredentialResource;
import org.debugroom.mynavi.sample.aws.microservice.common.model.UserResource;
import org.debugroom.mynavi.sample.aws.microservice.frontend.webapp.domain.repository.UserResourceRepository;


@XRayEnabled
@Service
public class OrchestrationServiceImpl implements OrchestrationService{

    @Autowired
    UserResourceRepository userResourceRepository;

    @Override
    public UserResource getUserResource(String loginId) throws BusinessException {
        return userResourceRepository.findOneByLoginId(loginId);
    }

    @Override
    public List<CredentialResource> addTokens(OidcIdToken oidcIdToken
            , OAuth2AccessToken oAuth2AccessToken, OAuth2RefreshToken oAuth2RefreshToken)
            throws BusinessException{
        List<CredentialResource> credentialResources = new ArrayList<>();
        int userId = Objects.hash(oidcIdToken.getSubject());
        credentialResources.add(CredentialResource.builder()
                .userId(userId)
                .credentialType("ID_TOKEN")
                .credentialKey(oidcIdToken.getTokenValue())
                .validDate(Timestamp.from(oidcIdToken.getExpiresAt()))
                .build());
        credentialResources.add(CredentialResource.builder()
                .userId(userId)
                .credentialType("ACCESS_TOKEN")
                .credentialKey(oAuth2AccessToken.getTokenValue())
                .validDate(Timestamp.from(oAuth2AccessToken.getExpiresAt()))
                .build());
        credentialResources.add(CredentialResource.builder()
                .userId(userId)
                .credentialType("REFLESH_TOKEN")
                .credentialKey(oAuth2RefreshToken.getTokenValue())
//                .validDate(Timestamp.from(oAuth2RefreshToken.getExpiresAt()))
                .build());
        return userResourceRepository.saveTokens(credentialResources);
    }

}
