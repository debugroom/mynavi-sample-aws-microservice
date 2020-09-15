package org.debugroom.mynavi.sample.aws.microservice.frontend.webapp.domain.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.debugroom.mynavi.sample.aws.microservice.common.apinfra.exception.BusinessException;
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
}
