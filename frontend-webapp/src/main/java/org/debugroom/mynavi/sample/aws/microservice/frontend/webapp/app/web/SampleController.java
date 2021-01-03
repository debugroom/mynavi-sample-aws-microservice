package org.debugroom.mynavi.sample.aws.microservice.frontend.webapp.app.web;

import javax.servlet.http.HttpSession;

import com.amazonaws.xray.spring.aop.XRayEnabled;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.debugroom.mynavi.sample.aws.microservice.common.apinfra.exception.BusinessException;
import org.debugroom.mynavi.sample.aws.microservice.frontend.webapp.domain.service.OrchestrationService;
import org.debugroom.mynavi.sample.aws.microservice.frontend.webapp.app.model.PortalInformation;
import org.debugroom.mynavi.sample.aws.microservice.frontend.webapp.app.web.security.CustomUserDetails;

@XRayEnabled
@Controller
public class SampleController {

    @Autowired
    OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    @Autowired
    OrchestrationService orchestrationService;

    @GetMapping("/error")
    public String error(){
        return "error";
    }

    @GetMapping(value = "/login")
    public String login(){
        return "login";
    }

    @GetMapping(value= "/portal")
    public String portal(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                         Model model, HttpSession httpSession){
//    public String portal(Model model, HttpSession httpSession){
        model.addAttribute("portalInformation",
                PortalInformation.builder()
                        .userResource(customUserDetails.getUserResource())
                        .build());
        String sessionId = httpSession.getId();
        model.addAttribute("sessionId", sessionId);
        return "portal";
    }

    @GetMapping(value = "/timeout")
    public String timeout(){
        return "timeout";
    }

    @GetMapping(value = "/oauth2LoginSuccess")
    public String oauth2SuccessPortal(@AuthenticationPrincipal OidcUser oidcUser
            , OAuth2AuthenticationToken oAuth2AuthenticationToken, Model model){
        OAuth2AuthorizedClient oAuth2AuthorizedClient =
                oAuth2AuthorizedClientService.loadAuthorizedClient(
                        oAuth2AuthenticationToken.getAuthorizedClientRegistrationId(),
                        oAuth2AuthenticationToken.getName());
        model.addAttribute("oidcUser", oidcUser);

        model.addAttribute( oAuth2AuthorizedClientService
                .loadAuthorizedClient(
                        oAuth2AuthenticationToken.getAuthorizedClientRegistrationId(),
                        oAuth2AuthenticationToken.getName()));

        model.addAttribute("accessToken", oAuth2AuthorizedClient.getAccessToken());
        try{
            orchestrationService.addTokens(oidcUser.getIdToken(), oAuth2AuthorizedClient.getAccessToken(),
                    oAuth2AuthorizedClient.getRefreshToken());
        }catch (BusinessException e){
            // If the Business Exception occur, You should transit to Error page.
            return "error";
        }
        return "oauth2Portal";
    }

}
