package com.hbpm.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

/**
 * 订阅CSRF Token触发生成token
 *
 * @author huangxiuqi
 */
@Configuration
public class CsrfSubscribeConfiguration {

    @Bean
    public WebFilter addCsrfTokenFilter() {
        return (exchange, chain) -> Mono.just(exchange)
                .flatMap(ex -> ex.getAttributeOrDefault(CsrfToken.class.getName(), Mono.empty()))
                .doOnNext(ex -> {
                })
                .then(chain.filter(exchange));
    }
}
