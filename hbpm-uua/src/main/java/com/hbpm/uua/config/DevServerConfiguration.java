package com.hbpm.uua.config;

import com.hbpm.core.CoreOauth2ClientService;
import com.hbpm.uua.controller.DevLoginController;
import com.hbpm.uua.controller.LoginController;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author huangxiuqi
 */
@Configuration
@ConditionalOnMissingBean(LoginController.class)
public class DevServerConfiguration {

    @Bean
    public DevLoginController devIndexController(CoreOauth2ClientService coreOauth2ClientService) {
        return new DevLoginController(coreOauth2ClientService);
    }
}
