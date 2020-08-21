package org.debugroom.mynavi.sample.aws.microservice.frontend.webapp.app.web.security;

import java.util.Collection;
import java.util.Objects;

import org.debugroom.mynavi.sample.aws.microservice.common.model.UserResource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class CustomUserDetails implements UserDetails {

    private final UserResource userResource;
    private final Collection<GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
//        return "{noop}test";
        return userResource.getCredentialResources().stream()
                .filter(userResource -> Objects.equals(
                        "PASSWORD", userResource.getCredentialType()))
                .findFirst().get().getCredentialKey();
    }

    @Override
    public String getUsername() {
        return userResource.getLoginId();
//        return "test";
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
