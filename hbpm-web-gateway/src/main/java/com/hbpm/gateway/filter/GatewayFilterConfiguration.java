package com.hbpm.gateway.filter;

import com.hbpm.gateway.filter.factory.CaptchaGatewayFilterFactory;
import com.hbpm.gateway.filter.factory.GuavaRateLimiterGatewayFilterFactory;
import com.hbpm.gateway.service.CaptchaService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author huangxiuqi
 */
@Configuration
public class GatewayFilterConfiguration {

    @Bean
    public GuavaRateLimiterGatewayFilterFactory gatewayRateLimiterFilterFactory() {
        return new GuavaRateLimiterGatewayFilterFactory();
    }

    @Bean
    public CaptchaGatewayFilterFactory captchaGatewayFilterFactory(CaptchaService captchaService) {
        return new CaptchaGatewayFilterFactory(captchaService);
    }
}
