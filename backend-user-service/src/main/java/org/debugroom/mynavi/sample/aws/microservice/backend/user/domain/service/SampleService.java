package org.debugroom.mynavi.sample.aws.microservice.backend.user.domain.service;

import java.util.List;

import org.debugroom.mynavi.sample.aws.microservice.backend.user.domain.model.entity.User;
import org.debugroom.mynavi.sample.aws.microservice.common.apinfra.exception.BusinessException;

public interface SampleService {

    public List<User> getUsers();
    public User getUser(Long id) throws BusinessException;
    public User getUserByLoginId(String loginId) throws BusinessException;

}
