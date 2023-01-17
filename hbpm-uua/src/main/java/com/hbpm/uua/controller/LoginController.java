package com.hbpm.uua.controller;

import com.hbpm.core.CoreOauth2ClientService;
import com.hbpm.model.core.CoreOauth2ClientEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author huangxiuqi
 */
@Controller
public class LoginController {

    private final CoreOauth2ClientService coreOauth2ClientService;

    public LoginController(CoreOauth2ClientService coreOauth2ClientService) {
        this.coreOauth2ClientService = coreOauth2ClientService;
    }

    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request) {
        String clientId = request.getParameter("client_id");
        model.addAttribute("pageVars", "window.__LOGIN_PAGE_VARS = {\n" +
                "        clientInfo: {\n" +
                "          name: '管理后台1',\n" +
                "          desc: '管理后台1',\n" +
                "          icon: 'https://g.alicdn.com/dingding/web/0.2.3/img/logo.png',\n" +
                "        }\n" +
                "      };");
        model.addAttribute("pageTitle", "统一身份认证");
        if (StringUtils.hasText(clientId)) {
            CoreOauth2ClientEntity client = coreOauth2ClientService.findByClientId(clientId);
            if (client != null) {
                model.addAttribute("pageVars", "window.__LOGIN_PAGE_VARS = {\n" +
                        "        clientInfo: {\n" +
                        "          name: '" + client.getClientName() + "',\n" +
                        "          desc: '" + client.getClientName() + "',\n" +
                        "          icon: 'https://g.alicdn.com/dingding/web/0.2.3/img/logo.png',\n" +
                        "        }\n" +
                        "      };");
                model.addAttribute("pageTitle", client.getClientName() + " - 统一身份认证");
            }
        }
        return "index";
    }
}
