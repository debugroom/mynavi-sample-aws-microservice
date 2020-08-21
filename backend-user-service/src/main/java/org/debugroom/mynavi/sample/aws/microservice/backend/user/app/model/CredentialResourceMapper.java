package org.debugroom.mynavi.sample.aws.microservice.backend.user.app.model;

import java.util.List;
import java.util.stream.Collectors;

import org.debugroom.mynavi.sample.aws.microservice.backend.user.domain.model.entity.Credential;
import org.debugroom.mynavi.sample.aws.microservice.common.model.CredentialResource;

public interface CredentialResourceMapper {

    public static CredentialResource map(Credential credential){
        return CredentialResource.builder()
                .userId(credential.getUserId())
                .credentialType(credential.getCredentialType())
                .credentialKey(credential.getCredentialKey())
                .validDate(credential.getValidDate())
                .build();
    }

    public static List<CredentialResource> map(List<Credential> credentials){
        return credentials.stream().map(CredentialResourceMapper::map)
                .collect(Collectors.toList());
    }

}
