package org.debugroom.mynavi.sample.aws.microservice.backend.user.app.model;

import java.util.List;
import java.util.stream.Collectors;

import org.debugroom.mynavi.sample.aws.microservice.backend.user.domain.model.entity.User;
import org.debugroom.mynavi.sample.aws.microservice.common.model.UserResource;

public interface UserResourceMapper {

    public static UserResource map(User user){
        return UserResource.builder()
                .userId(Long.toString(user.getUserId()))
                .firstName(user.getFirstName())
                .familyName(user.getFamilyName())
                .loginId(user.getLoginId())
                .isLogin(user.getLogin())
                .isAdmin(user.getAdmin())
                .build();
    }

    public static List<UserResource> map(List<User> users){
        return users.stream().map(UserResourceMapper::map)
                .collect(Collectors.toList());
    }

    public static UserResource mapWithCredentials(User user){
        UserResource userResource = map(user);
        userResource.setCredentialResources(
                user.getCredentialsByUserId().stream().map(
                        CredentialResourceMapper::map).collect(Collectors.toList()));
        return  userResource;
    }

    public static List<UserResource> mapWithCredentials(List<User> users){
        return users.stream().map(UserResourceMapper::mapWithCredentials)
                .collect(Collectors.toList());
    }

}
