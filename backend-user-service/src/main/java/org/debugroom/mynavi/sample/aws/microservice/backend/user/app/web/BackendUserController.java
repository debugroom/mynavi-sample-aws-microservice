package org.debugroom.mynavi.sample.aws.microservice.backend.user.app.web;

import java.util.List;

import com.amazonaws.xray.spring.aop.XRayEnabled;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.debugroom.mynavi.sample.aws.microservice.backend.user.app.model.CredentialResourceMapper;
import org.debugroom.mynavi.sample.aws.microservice.common.model.CredentialResource;
import org.debugroom.mynavi.sample.aws.microservice.backend.user.app.model.UserResourceMapper;
import org.debugroom.mynavi.sample.aws.microservice.backend.user.domain.service.SampleService;
import org.debugroom.mynavi.sample.aws.microservice.common.apinfra.exception.BusinessException;
import org.debugroom.mynavi.sample.aws.microservice.common.model.UserResource;

@XRayEnabled
@RestController
@RequestMapping("api/v1")
public class BackendUserController {

    @Autowired
    SampleService sampleService;

    @GetMapping("/users")
    public List<UserResource> getUsers(){
        return UserResourceMapper.mapWithCredentials(sampleService.getUsers());
    }

    @GetMapping("/users/{id:[0-9]+}")
    public UserResource getUser(@PathVariable Long id) throws BusinessException {
        return UserResourceMapper.mapWithCredentials(
                sampleService.getUser(id));
    }

    @GetMapping("/users/user")
    public UserResource getUserByLoginId(
            @RequestParam("loginId") String loginId) throws BusinessException {
        return UserResourceMapper.mapWithCredentials(
                sampleService.getUserByLoginId(loginId));
    }

    @PostMapping("/users/{id:[0-9]+}/credentials")
    public List<CredentialResource> addTokens(@RequestBody List<CredentialResource> credentialResources)
            throws BusinessException{
        return CredentialResourceMapper.map(sampleService.addCredentials(
                CredentialResourceMapper.mapToEntity(credentialResources)));
    }

}
