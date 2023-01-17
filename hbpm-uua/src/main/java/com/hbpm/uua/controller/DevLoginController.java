package com.hbpm.uua.controller;

import com.hbpm.core.CoreOauth2ClientService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author huangxiuqi
 */
@RequestMapping
public class DevLoginController {

    private final CoreOauth2ClientService coreOauth2ClientService;

    public DevLoginController(CoreOauth2ClientService coreOauth2ClientService) {
        this.coreOauth2ClientService = coreOauth2ClientService;
    }

    @RequestMapping("/login")
    public String index(Model model, HttpServletRequest request) {
        LoginController controller = new LoginController(coreOauth2ClientService);
        controller.login(model, request);
        return "dev-index";
    }
}
