package com.hbpm.base.web.error;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;

/**
 * 自定义错误页面
 * @author huangxiuqi
 */
@AutoConfiguration(before = ErrorMvcAutoConfiguration.class)
public class WebErrorConfiguration {

    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public ServletErrorController servletErrorController(
            ErrorAttributes errorAttributes, ServerProperties serverProperties) {
        return new ServletErrorController(errorAttributes, serverProperties);
    }

    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public ServletErrorHandler servletErrorHandler() {
        return new ServletErrorHandler();
    }
}
