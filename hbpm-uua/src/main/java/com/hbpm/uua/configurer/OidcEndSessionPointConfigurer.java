package com.hbpm.uua.configurer;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * OIDC end_session_endpoint配置
 * @see <a href="https://openid.net/specs/openid-connect-session-1_0-17.html#RPLogout">openid-connect-session-1_0-17.html#RPLogout</a>
 * @author huangxiuqi
 */
public class OidcEndSessionPointConfigurer extends AbstractHttpConfigurer<OidcEndSessionPointConfigurer, HttpSecurity> {

    private String endSessionPoint = "/openid-connect/logout";

    private RequestMatcher requestMatcher;

    @Override
    public void init(HttpSecurity http) throws Exception {
        requestMatcher = new AntPathRequestMatcher(endSessionPoint);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        OAuth2AuthorizationService authorizationService = http.getSharedObject(OAuth2AuthorizationService.class);
        RegisteredClientRepository registeredClientRepository = http.getSharedObject(RegisteredClientRepository.class);
        OidcEndSessionPointFilter filter = new OidcEndSessionPointFilter(getRequestMatcher(), authorizationService, registeredClientRepository);
        http.addFilterAfter(filter, FilterSecurityInterceptor.class);
    }

    public void setEndSessionPoint(String endSessionPoint) {
        this.endSessionPoint = endSessionPoint;
    }

    public String getEndSessionPoint() {
        return endSessionPoint;
    }

    public RequestMatcher getRequestMatcher() {
        return request -> requestMatcher.matches(request);
    }
}
