package com.hbpm.gateway.config;


import com.hbpm.gateway.utils.WebFluxUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.server.DelegatingServerAuthenticationEntryPoint;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationEntryPoint;
import org.springframework.security.web.server.util.matcher.AndServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;

import java.util.HashMap;
import java.util.Map;

/**
 * @author huangxiuqi
 */
public class SecurityServerAuthenticationEntryPoint {

    public static ServerAuthenticationEntryPoint createEntryPoint(String location) {

        ServerAuthenticationEntryPoint defaultEntryPoint = (exchange, ex) -> {
            Map<String, String> data = new HashMap<>(2);
            data.put("redirectUri", location);
            return WebFluxUtils.sendJsonResponse(exchange, HttpStatus.UNAUTHORIZED, ex.getLocalizedMessage(), data);
        };

        ServerWebExchangeMatcher defaultEntryPointMatcher = new AndServerWebExchangeMatcher(
                WebFluxUtils.notXhrMatcher, WebFluxUtils.htmlMatcher);
        DelegatingServerAuthenticationEntryPoint.DelegateEntry entry =
                new DelegatingServerAuthenticationEntryPoint.DelegateEntry(
                        defaultEntryPointMatcher, new RedirectServerAuthenticationEntryPoint(location));
        DelegatingServerAuthenticationEntryPoint entryPoint = new DelegatingServerAuthenticationEntryPoint(entry);
        entryPoint.setDefaultEntryPoint(defaultEntryPoint);
        return entryPoint;
    }
}
