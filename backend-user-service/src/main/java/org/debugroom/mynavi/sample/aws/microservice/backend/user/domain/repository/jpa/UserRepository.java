package org.debugroom.mynavi.sample.aws.microservice.backend.user.domain.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.debugroom.mynavi.sample.aws.microservice.backend.user.domain.model.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.credentialsByUserId where u.userId = :userId")
    User findByUserId(@Param("userId") long userId);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.credentialsByUserId where u.loginId = :loginId")
    User findByLoginId(@Param("loginId") String loginId);

}
