package org.debugroom.mynavi.sample.aws.microservice.backend.user.domain.service;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

import com.amazonaws.xray.spring.aop.XRayEnabled;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.debugroom.mynavi.sample.aws.microservice.backend.user.domain.model.entity.Credential;
import org.debugroom.mynavi.sample.aws.microservice.backend.user.domain.model.entity.User;
import org.debugroom.mynavi.sample.aws.microservice.backend.user.domain.repository.jpa.UserRepository;
import org.debugroom.mynavi.sample.aws.microservice.common.apinfra.exception.BusinessException;

@XRayEnabled
@Service
public class SampleServiceImpl implements SampleService{

    @Autowired
    MessageSource messageSource;

    @Autowired
    UserRepository userRepository;

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(Long id) throws BusinessException{
        User user = userRepository.findByUserId(id);
        if(Objects.isNull(user)){
            String errorCode = "BE0001";
            throw new BusinessException(errorCode, messageSource.getMessage(
                    errorCode, new String[]{id.toString()}, Locale.getDefault()));
        }
        return user;
    }

    @Override
    public User getUserByLoginId(String loginId) throws BusinessException {
        User user = userRepository.findByLoginId(loginId);
        if(Objects.isNull(user)){
            String errorCode = "BE0002";
            throw new BusinessException(errorCode, messageSource.getMessage(
                    errorCode, new String[]{loginId}, Locale.getDefault()));
        }
        return user;
    }

    @Override
    @Transactional
    public List<Credential> addCredentials(List<Credential> credentials)
            throws BusinessException {
//        Long updateTargetUserId = credentials.stream().
//                findFirst().get().getUserId();
        // In this example, Update existing user credentital.
        Integer updateTargetUserId = 0;
        credentials = credentials.stream().map(
                c -> { c.setUserId(updateTargetUserId); return c;})
                .collect(Collectors.toList());
        User user = userRepository.findByUserId(updateTargetUserId);
        if(Objects.isNull(user)){
            String errorCode = "BE0001";
            throw new BusinessException(errorCode, messageSource.getMessage(
                    errorCode, new String[]{updateTargetUserId.toString()}, Locale.getDefault()));
        }
        user.getCredentialsByUserId().clear();
        userRepository.flush();
        user.getCredentialsByUserId().addAll(credentials.stream().collect(Collectors.toSet()));
        return credentials;
    }

}