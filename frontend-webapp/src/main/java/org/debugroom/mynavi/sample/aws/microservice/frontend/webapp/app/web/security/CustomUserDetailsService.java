package org.debugroom.mynavi.sample.aws.microservice.frontend.webapp.app.web.security;

import org.debugroom.mynavi.sample.aws.microservice.common.apinfra.exception.BusinessException;
import org.debugroom.mynavi.sample.aws.microservice.common.model.UserResource;
import org.debugroom.mynavi.sample.aws.microservice.frontend.webapp.domain.repository.UserResourceRepository;
import org.debugroom.mynavi.sample.aws.microservice.frontend.webapp.domain.service.OrchestrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Locale;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    MessageSource messageSource;

    @Autowired
    OrchestrationService orchestrationService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return CustomUserDetails.builder()
//                .authorities(AuthorityUtils.createAuthorityList("ROLE_USER"))
//                .build();
        try{
            UserResource userResource = orchestrationService.getUserResource(username);
            List<GrantedAuthority> authorities = null;
            if(userResource.isAdmin()){
                authorities = AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER");
            }else {
                authorities = AuthorityUtils.createAuthorityList("ROLE_USER");
            }
            return CustomUserDetails.builder()
                    .userResource(userResource)
                    .authorities(authorities)
                    .build();
        }catch (BusinessException e){
            throw new UsernameNotFoundException(messageSource.getMessage(
                    "BE0001", null, Locale.getDefault()), e);
        }
    }

}
