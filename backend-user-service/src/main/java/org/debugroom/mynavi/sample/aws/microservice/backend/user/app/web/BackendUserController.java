package org.debugroom.mynavi.sample.aws.microservice.backend.user.app.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.debugroom.mynavi.sample.aws.microservice.backend.user.app.model.UserResourceMapper;
import org.debugroom.mynavi.sample.aws.microservice.backend.user.domain.service.SampleService;
import org.debugroom.mynavi.sample.aws.microservice.common.apinfra.exception.BusinessException;
import org.debugroom.mynavi.sample.aws.microservice.common.model.UserResource;

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

}
