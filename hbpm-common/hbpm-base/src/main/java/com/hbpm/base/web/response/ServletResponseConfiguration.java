package com.hbpm.base.web.response;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author huangxiuqi
 */
@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ServletResponseConfiguration {

    @Bean
    public ServletResponseBodyRewriter servletResponseBodyRewriter() {
        return new ServletResponseBodyRewriter();
    }
}
