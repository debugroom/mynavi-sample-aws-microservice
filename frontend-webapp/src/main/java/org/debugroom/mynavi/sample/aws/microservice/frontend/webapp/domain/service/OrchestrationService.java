package org.debugroom.mynavi.sample.aws.microservice.frontend.webapp.domain.service;

import org.debugroom.mynavi.sample.aws.microservice.common.apinfra.exception.BusinessException;
import org.debugroom.mynavi.sample.aws.microservice.common.model.UserResource;

public interface OrchestrationService {

    public UserResource getUserResource(String loginId) throws BusinessException;

}
