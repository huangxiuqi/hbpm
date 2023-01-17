package com.hbpm.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationFailureHandler;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import org.springframework.security.web.server.csrf.CsrfWebFilter;
import org.springframework.security.web.server.util.matcher.AndServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author huangxiuqi
 */
@EnableWebFluxSecurity
public class ServerSecurityConfiguration {

    private final ReactiveClientRegistrationRepository reactiveClientRegistrationRepository;

    private final List<ClientRegistration> clientRegistrations;

    public ServerSecurityConfiguration(ReactiveClientRegistrationRepository reactiveClientRegistrationRepository,
                                       Iterable<ClientRegistration> clientRegistrations) {
        this.reactiveClientRegistrationRepository = reactiveClientRegistrationRepository;
        this.clientRegistrations = new ArrayList<>();
        clientRegistrations.forEach(this.clientRegistrations::add);
    }

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {

        // OIDC注销
        OidcClientInitiatedServerLogoutSuccessHandler logoutSuccessHandler =
                new OidcClientInitiatedServerLogoutSuccessHandler(reactiveClientRegistrationRepository);
        logoutSuccessHandler.setPostLogoutRedirectUri("http://hbpm.com");

        // 忽略所有登录服务的认证，由登录服务自行处理
        HostMatcher ignoreMatcher = new HostMatcher("login.hbpm.com");
        ServerWebExchangeMatcher csrfMatcher = new AndServerWebExchangeMatcher(
                new NegatedServerWebExchangeMatcher(ignoreMatcher),
                CsrfWebFilter.DEFAULT_CSRF_MATCHER);

        // CSRF Token配置
        CookieServerCsrfTokenRepository cookieCsrfTokenRepository = CookieServerCsrfTokenRepository.withHttpOnlyFalse();
        cookieCsrfTokenRepository.setSecure(false);
        cookieCsrfTokenRepository.setCookieDomain("hbpm.com");
        cookieCsrfTokenRepository.setCookieName("gateway_csrf_token");

        http.authorizeExchange()
                .matchers(ignoreMatcher)
                .permitAll()
                .pathMatchers("/actuator/**", "/refresh_route")
                .permitAll()
                .pathMatchers("/api/captcha/**", "/inside/api/captcha/**")
                .permitAll()
                .anyExchange()
                .authenticated()
                .and()
                .csrf()
                .requireCsrfProtectionMatcher(csrfMatcher)
                .csrfTokenRepository(cookieCsrfTokenRepository)
                .and()
                .cors()
                .and()
                .formLogin()
                .disable()
                .httpBasic()
                .disable()
                .oauth2Login()
                .authenticationFailureHandler(new RedirectServerAuthenticationFailureHandler("/error"))
                .and()
                .oauth2Client(Customizer.withDefaults())
                .logout()
                .logoutSuccessHandler(logoutSuccessHandler);

        // 只有一个Oauth2认证服务时，未授权页面请求直接重定向至此认证服务，不展示自带登录页面
        if (clientRegistrations.size() == 1) {
            ClientRegistration client = clientRegistrations.get(0);
            ServerAuthenticationEntryPoint entryPoint = SecurityServerAuthenticationEntryPoint
                    .createEntryPoint("/oauth2/authorization/" + client.getRegistrationId());

            http.exceptionHandling()
                    .authenticationEntryPoint(entryPoint);
        }

        return http.build();
    }

    @Bean
    public ReactiveUserDetailsService reactiveUserDetailsService() {
        return username -> {
            throw new UsernameNotFoundException("");
        };
    }

    public static class HostMatcher implements ServerWebExchangeMatcher {

        private PathMatcher pathMatcher = new AntPathMatcher(".");

        private List<String> patterns = new ArrayList<>();

        public HostMatcher(String ...patterns) {
            this(Arrays.asList(patterns));
        }

        public HostMatcher(List<String> patterns) {
            this.patterns = patterns;
        }

        @Override
        public Mono<MatchResult> matches(ServerWebExchange exchange) {
            String host = exchange.getRequest().getHeaders().getFirst("Host");
            if (host == null) {
                return MatchResult.notMatch();
            }
            for (String pattern : patterns) {
                if (pathMatcher.match(pattern, host)) {
                    return MatchResult.match();
                }
            }
            return MatchResult.notMatch();
        }

        public List<String> getPatterns() {
            return patterns;
        }

        public void setPatterns(List<String> patterns) {
            this.patterns = patterns;
        }
    }
}
