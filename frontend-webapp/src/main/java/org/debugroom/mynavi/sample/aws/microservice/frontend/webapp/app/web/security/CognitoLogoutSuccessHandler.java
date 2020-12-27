package org.debugroom.mynavi.sample.aws.microservice.frontend.webapp.app.web.security;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

public class CognitoLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    private final ClientRegistrationRepository clientRegistrationRepository;

    private  String postLogoutRedirectUri;

    public CognitoLogoutSuccessHandler(ClientRegistrationRepository clientRegistrationRepository){
        Assert.notNull(clientRegistrationRepository, "clientRegistrationRepository cannot be null");
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {
        String targetUrl = null;
        URI endSessionEndpoint;
        if (authentication instanceof OAuth2AuthenticationToken && authentication.getPrincipal() instanceof OidcUser) {
            String registrationId = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
            ClientRegistration clientRegistration = this.clientRegistrationRepository
                    .findByRegistrationId(registrationId);
            endSessionEndpoint = this.endSessionEndpoint(clientRegistration);
            if (endSessionEndpoint != null) {
                URI postLogoutRedirectUri = postLogoutRedirectUri(request);
                targetUrl = endpointUri(endSessionEndpoint, clientRegistration, postLogoutRedirectUri);
            }
        }
        if (targetUrl == null) {
            targetUrl = super.determineTargetUrl(request, response);
        }

        return targetUrl;
    }

    private URI endSessionEndpoint(ClientRegistration clientRegistration) {
        URI result = null;
        if (clientRegistration != null) {
            Object endSessionEndpoint = clientRegistration.getProviderDetails().getConfigurationMetadata()
                    .get("end_session_endpoint");
            if (endSessionEndpoint != null) {
                result = URI.create(endSessionEndpoint.toString());
            }
        }

        return result;
    }

    private String idToken(Authentication authentication) {
        return ((OidcUser) authentication.getPrincipal()).getIdToken().getTokenValue();
    }

    private URI postLogoutRedirectUri(HttpServletRequest request) {
        if (this.postLogoutRedirectUri == null) {
            return null;
        }
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(UrlUtils.buildFullRequestUrl(request))
                .replacePath(request.getContextPath())
                .replaceQuery(null)
                .fragment(null)
                .build();
        return UriComponentsBuilder.fromUriString(this.postLogoutRedirectUri)
                .buildAndExpand(Collections.singletonMap("baseUrl", uriComponents.toUriString()))
                .toUri();
    }

    private String endpointUri(URI endSessionEndpoint, ClientRegistration clientRegistration, URI postLogoutRedirectUri) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUri(endSessionEndpoint);
        builder.queryParam("client_id", clientRegistration.getClientId());
        if (postLogoutRedirectUri != null) {
            builder.queryParam("logout_uri", postLogoutRedirectUri);
        }
        return builder.encode(StandardCharsets.UTF_8).build().toUriString();
    }

    public void setPostLogoutRedirectUri(String postLogoutRedirectUri) {
        Assert.notNull(postLogoutRedirectUri, "postLogoutRedirectUri cannot be null");
        this.postLogoutRedirectUri = postLogoutRedirectUri;
    }

}
