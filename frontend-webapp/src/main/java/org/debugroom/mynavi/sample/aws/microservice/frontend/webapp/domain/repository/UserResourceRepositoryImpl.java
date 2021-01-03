package org.debugroom.mynavi.sample.aws.microservice.frontend.webapp.domain.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import org.debugroom.mynavi.sample.aws.microservice.common.model.CredentialResource;
import org.debugroom.mynavi.sample.aws.microservice.common.apinfra.exception.BusinessException;
import org.debugroom.mynavi.sample.aws.microservice.common.apinfra.exception.BusinessExceptionResponse;
import org.debugroom.mynavi.sample.aws.microservice.common.apinfra.exception.ErrorResponse;
import org.debugroom.mynavi.sample.aws.microservice.common.apinfra.exception.SystemException;
import org.debugroom.mynavi.sample.aws.microservice.common.model.UserResource;

//@Profile("v1")
@XRayEnabled
@Component
public class UserResourceRepositoryImpl implements UserResourceRepository{

    private static final String SERVICE_NAME = "/backend/user";
    private static final String API_VERSION = "/api/v1";

    @Autowired
    WebClient webClient;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MessageSource messageSource;

    @Override
    public UserResource findOne(String userId) {
        String endpoint = SERVICE_NAME + API_VERSION + "/users/{userId}";
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path(endpoint).build(userId))
                .retrieve()
                .bodyToMono(UserResource.class)
                .block();
    }

    @Override
    public UserResource findOneByLoginId(String loginId) throws BusinessException {
        String endpoint = SERVICE_NAME + API_VERSION + "/users/user";
        try{
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(endpoint).queryParam("loginId", loginId).build())
                    .retrieve()
                    .bodyToMono(UserResource.class)
                    .block();
        }catch (WebClientResponseException e){
             try {
                ErrorResponse errorResponse = objectMapper.readValue(
                        e.getResponseBodyAsString(), ErrorResponse.class);
                if(errorResponse instanceof BusinessExceptionResponse){
                    throw ((BusinessExceptionResponse)errorResponse).getBusinessException();
                }else {
                    String errorCode = "SE0002";
                    throw new SystemException(errorCode, messageSource.getMessage(
                            errorCode, new String[]{endpoint}, Locale.getDefault()), e);
                }
            }catch (IOException e1){
                String errorCode = "SE0002";
                throw new SystemException(errorCode, messageSource.getMessage(
                        errorCode, new String[]{endpoint}, Locale.getDefault()), e);
            }
        }
    }

    @Override
    public List<UserResource> findAll() {
        String endpoint = SERVICE_NAME + API_VERSION + "/users";
        return Arrays.asList(
                webClient.get()
                        .uri(uriBuilder -> uriBuilder.path(endpoint).build())
                        .retrieve().bodyToMono(UserResource[].class).block());
    }


    @Override
    public List<CredentialResource> saveTokens(List<CredentialResource> credentialResources) throws BusinessException {
        String endpoint = SERVICE_NAME + API_VERSION + "/users/{userId}/credentials";
        try {
            return webClient.post()
                    .uri(uriBuilder -> uriBuilder.path(endpoint).build(
                            credentialResources.get(0).getUserId()))
                    .bodyValue(credentialResources)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<ArrayList<CredentialResource>>(){})
                    .block();
        } catch (WebClientResponseException e) {
            try {
                ErrorResponse errorResponse = objectMapper.readValue(
                        e.getResponseBodyAsString(), ErrorResponse.class);
                if (errorResponse instanceof BusinessExceptionResponse) {
                    throw ((BusinessExceptionResponse) errorResponse).getBusinessException();
                }else {
                    String errorCode = "SE0002";
                    throw new SystemException(errorCode, messageSource.getMessage(
                            errorCode, new String[]{endpoint}, Locale.getDefault()), e);
                }
            } catch (IOException e1) {
                String errorCode = "SE0002";
                throw new SystemException(errorCode, messageSource.getMessage(
                        errorCode, new String[]{endpoint}, Locale.getDefault()), e1);
            }
        }
    }

}
