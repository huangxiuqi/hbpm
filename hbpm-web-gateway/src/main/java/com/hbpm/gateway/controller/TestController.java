package com.hbpm.gateway.controller;

import com.hbpm.gateway.service.GatewayRouteDefineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.WebSession;
import org.springframework.web.server.session.DefaultWebSessionManager;
import org.springframework.web.server.session.InMemoryWebSessionStore;
import org.springframework.web.server.session.WebSessionManager;
import org.springframework.web.server.session.WebSessionStore;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @author huangxiuqi
 */
@RestController
public class TestController {

    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    private final GatewayRouteDefineService gatewayRouteDefineService;

    private final WebSessionManager webSessionManager;

    public TestController(GatewayRouteDefineService gatewayRouteDefineService,
                          WebSessionManager webSessionManager) {
        this.gatewayRouteDefineService = gatewayRouteDefineService;
        this.webSessionManager = webSessionManager;
    }

//    @RequestMapping("/")
//    public Mono<String> index() {
//        return ReactiveSecurityContextHolder.getContext()
//                .map(SecurityContext::getAuthentication)
//                .map(Authentication::getPrincipal)
//                .cast(DefaultOidcUser.class)
//                .map(user -> {
//                    log.info("user is " + user);
//                    return user;
//                })
//                .flatMap(name -> Mono.just("Hi " + name));
//    }

    @RequestMapping("/refresh_route")
    public Mono<String> refreshRoute() {
        return gatewayRouteDefineService.refreshRouteDefinition()
                .thenReturn("refresh success");
    }

    @RequestMapping("/sso_logout")
    public Mono<Map<String, WebSession>> ssoLogout() {
        if (webSessionManager instanceof DefaultWebSessionManager) {
            WebSessionStore webSessionStore = ((DefaultWebSessionManager) webSessionManager).getSessionStore();
            if (webSessionStore instanceof InMemoryWebSessionStore) {
                return Mono.just(((InMemoryWebSessionStore) webSessionStore).getSessions());
            }
        }
        return Mono.empty();
    }
}
