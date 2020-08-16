package org.debugroom.mynavi.sample.aws.microservice.frontend.webapp.app.web.security;

import org.springframework.stereotype.Service;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return CustomUserDetails.builder()
                .authorities(AuthorityUtils.createAuthorityList("ROLE_USER"))
                .build();
    }

}
