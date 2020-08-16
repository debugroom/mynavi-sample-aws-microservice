package org.debugroom.mynavi.sample.aws.microservice.frontend.webapp.app.web;

import javax.servlet.http.HttpSession;

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
    public String portal(Model model, HttpSession httpSession){
        String sessionId = httpSession.getId();
        model.addAttribute("sessionId", sessionId);
        return "portal";
    }

    @GetMapping(value = "/timeout")
    public String timeout(){
        return "timeout";
    }

}
