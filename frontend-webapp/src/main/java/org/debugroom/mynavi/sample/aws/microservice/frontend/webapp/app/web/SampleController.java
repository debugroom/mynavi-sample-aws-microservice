package org.debugroom.mynavi.sample.aws.microservice.frontend.webapp.app.web;

import javax.servlet.http.HttpSession;

import org.debugroom.mynavi.sample.aws.microservice.frontend.webapp.app.model.PortalInformation;
import org.debugroom.mynavi.sample.aws.microservice.frontend.webapp.app.web.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SampleController {

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

}
