package org.debugroom.mynavi.sample.aws.microservice.frontend.webapp.app.web.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

public class SessionExpiredDetectingLoginUrlAuthenticationEntryPoint
        extends LoginUrlAuthenticationEntryPoint {

    public SessionExpiredDetectingLoginUrlAuthenticationEntryPoint(
            String loginFormUrl) {
        super(loginFormUrl);
    }

    @Override
    protected String buildRedirectUrlToLoginPage(HttpServletRequest request
            , HttpServletResponse response, AuthenticationException authException) {
        String redirectUrl = super.buildRedirectUrlToLoginPage(
                request, response, authException);
        if(isRequestedSessionInvalid(request)){
            redirectUrl = "timeout";
        }
        return redirectUrl;
    }

    private boolean isRequestedSessionInvalid(HttpServletRequest request){
        return Objects.nonNull(request.getRequestedSessionId())
                && !request.isRequestedSessionIdValid();
    }

}
