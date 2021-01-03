package org.debugroom.mynavi.sample.aws.microservice.frontend.webapp.app.web.interceptor;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


import org.debugroom.mynavi.sample.aws.microservice.frontend.webapp.app.model.Menu;
import org.debugroom.mynavi.sample.aws.microservice.frontend.webapp.app.web.security.CustomUserDetails;

public class SetMenuInterceptor extends HandlerInterceptorAdapter {

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        if(Objects.nonNull(modelAndView)
                && Objects.isNull(modelAndView.getModel().get("menuList"))){
            SecurityContext securityContext = SecurityContextHolder.getContext();
            Authentication authentication = securityContext.getAuthentication();
            if(Objects.nonNull(authentication)){
                Object principal = authentication.getPrincipal();
                if(principal instanceof CustomUserDetails){
                    if(((CustomUserDetails) principal).getAuthorities()
                            .contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
                        modelAndView.addObject("menuList", getAdminMenuList());
                    }else {
                        modelAndView.addObject("menuList", getMenuList());

                    }
                }else if(principal instanceof DefaultOidcUser){
                    if(((DefaultOidcUser) principal).getAuthorities()
                            .contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
                        modelAndView.addObject("menuList", getAdminMenuList());
                    }else {
                        modelAndView.addObject("menuList", getMenuList());
                    }
                }
            }

        }
    }

    private List<Menu> getAdminMenuList(){
        return Arrays.asList(Menu.PORTAL, Menu.LOGOUT,
                Menu.USER_MANAGEMENT);
    }

    private List<Menu> getMenuList(){
        return Arrays.asList(Menu.PORTAL, Menu.LOGOUT);
    }

}
