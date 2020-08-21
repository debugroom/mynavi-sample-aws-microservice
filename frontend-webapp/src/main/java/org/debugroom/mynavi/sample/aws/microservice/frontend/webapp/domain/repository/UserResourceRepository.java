package org.debugroom.mynavi.sample.aws.microservice.frontend.webapp.domain.repository;

import java.util.List;

import org.debugroom.mynavi.sample.aws.microservice.common.apinfra.exception.BusinessException;
import org.debugroom.mynavi.sample.aws.microservice.common.model.UserResource;

public interface UserResourceRepository {

    public UserResource findOne(String userId);
    public UserResource findOneByLoginId(String loginId) throws BusinessException;
    public List<UserResource> findAll();

}
