package org.debugroom.mynavi.sample.aws.microservice.frontend.webapp.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import lombok.AllArgsConstructor;
import net.minidev.json.JSONArray;
import org.debugroom.mynavi.sample.aws.microservice.common.apinfra.cloud.aws.CloudFormationStackResolver;
import org.debugroom.mynavi.sample.aws.microservice.frontend.webapp.app.web.security.CognitoLogoutSuccessHandler;
import org.debugroom.mynavi.sample.aws.microservice.frontend.webapp.app.web.security.CognitoOAuth2User;
import org.debugroom.mynavi.sample.aws.microservice.frontend.webapp.domain.ServiceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.CustomUserTypesOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@AllArgsConstructor
@EnableWebSecurity
public class CognitoOAuth2LoginSecurityConfig extends WebSecurityConfigurerAdapter {

    ServiceProperties serviceProperties;

    CloudFormationStackResolver cloudFormationStackResolver;

    @Bean
    public AWSSimpleSystemsManagement awsSimpleSystemsManagement(){
        return AWSSimpleSystemsManagementClientBuilder.defaultClient();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository(){
        return new InMemoryClientRegistrationRepository(cognitoClientRegistration());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(
                authorize -> authorize
                        .antMatchers("/favicon.ico").permitAll()
                        .antMatchers("/webjars/*").permitAll()
                        .antMatchers("/static/*").permitAll()
                        .anyRequest().authenticated())
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("/oauth2LoginSuccess", true)
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                        .userAuthoritiesMapper(authoritiesMapper())
                        .oidcUserService(oidcUserService())))
                .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessHandler(oidcLogoutSuccessHandler()));
    }

    private LogoutSuccessHandler oidcLogoutSuccessHandler(){
        CognitoLogoutSuccessHandler cognitoLogoutSuccessHandler =
                new CognitoLogoutSuccessHandler(clientRegistrationRepository());
        cognitoLogoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}");
        return cognitoLogoutSuccessHandler;
    }

    private OidcUserService oidcUserService(){
        OidcUserService oidcUserService = new OidcUserService();
        oidcUserService.setOauth2UserService(oAuth2UserService());
        return oidcUserService;
    }

    private OAuth2UserService oAuth2UserService(){
        Map<String, Class<? extends OAuth2User>> customUserTypes = new HashMap<>();
        customUserTypes.put("cognito", CognitoOAuth2User.class);
        return new CustomUserTypesOAuth2UserService(customUserTypes);
    }

    private GrantedAuthoritiesMapper authoritiesMapper(){
        return authorities -> {
            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            for (GrantedAuthority grantedAuthority : authorities){
                grantedAuthorities.add(grantedAuthority);
                if(OidcUserAuthority.class.isInstance(grantedAuthority)){
                    Map<String, Object> attributes = ((OidcUserAuthority)grantedAuthority).getAttributes();
                    JSONArray groups = (JSONArray) attributes.get("cognito:groups");
                    String isAdmin = (String) attributes.get("custom:isAdmin");
                    if(Objects.nonNull(groups) && groups.contains("admin")){
                        grantedAuthorities.add( new SimpleGrantedAuthority("ROLE_ADMIN"));
                    }else if (Objects.nonNull(isAdmin) && Objects.equals(isAdmin, "1")){
                        grantedAuthorities.add( new SimpleGrantedAuthority("ROLE_ADMIN"));
                    }
                }
            }
            return grantedAuthorities;
        };
    }

    private ClientRegistration cognitoClientRegistration(){
        String clientId = cloudFormationStackResolver.getExportValue(
                serviceProperties.getCloudFormation().getCognito().getAppClientId());
        String clientSecret = getParameterFromPrameterStore(
                serviceProperties.getSystemsManagerParameterStore().getCognito().getAppClientSecret(), true);
        String domain = cloudFormationStackResolver.getExportValue(
                serviceProperties.getCloudFormation().getCognito().getDomain());
        String redirectUri = cloudFormationStackResolver.getExportValue(
                serviceProperties.getCloudFormation().getCognito().getRedirectUri());
        String jwkSetUri = cloudFormationStackResolver.getExportValue(
                serviceProperties.getCloudFormation().getCognito().getJwkSetUri());
        Map<String, Object> configurationMetadata = new HashMap<>();
        configurationMetadata.put("end_session_endpoint", domain + "/logout");
        return ClientRegistration.withRegistrationId("cognito")
                .clientId(clientId)
                .clientSecret(clientSecret)
                .clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUriTemplate(redirectUri)
                .scope("openid","profile")
                .tokenUri(domain + "/oauth2/token")
                .authorizationUri(domain + "/oauth2/authorize")
                .userInfoUri(domain + "/oauth2/userInfo")
                .userNameAttributeName("cognito:username")
                .jwkSetUri(jwkSetUri)
                .clientName("Cognito")
                .providerConfigurationMetadata(configurationMetadata)
                .build();
    }

    private String getParameterFromPrameterStore(String paramName, boolean isEncripted){
        GetParameterRequest request = new GetParameterRequest();
        request.setName(paramName);
        request.setWithDecryption(isEncripted);
        return awsSimpleSystemsManagement().getParameter(request).getParameter().getValue();
    }

}
