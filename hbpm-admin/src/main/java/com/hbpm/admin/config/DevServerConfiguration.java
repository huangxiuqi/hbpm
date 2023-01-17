package com.hbpm.admin.config;

import com.hbpm.admin.controller.DevIndexController;
import com.hbpm.admin.controller.IndexController;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author huangxiuqi
 */
@Configuration
@ConditionalOnMissingBean(IndexController.class)
public class DevServerConfiguration {

    @Bean
    public DevIndexController devIndexController() {
        return new DevIndexController();
    }
}
