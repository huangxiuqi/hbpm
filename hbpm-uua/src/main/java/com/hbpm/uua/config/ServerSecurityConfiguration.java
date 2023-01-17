package com.hbpm.uua.config;

import com.hbpm.base.security.servlet.SecurityAccessDeniedHandler;
import com.hbpm.base.security.servlet.SecurityAuthenticationEntryPoint;
import com.hbpm.base.security.servlet.SecurityAuthenticationFailureHandler;
import com.hbpm.base.security.servlet.SecurityAuthenticationSuccessHandler;
import com.hbpm.core.CoreOauth2ClientService;
import com.hbpm.uua.configurer.OidcEndSessionPointConfigurer;
import com.hbpm.uua.service.FeignRegisteredClientRepository;
import com.hbpm.uua.service.InMemoryOauth2AuthorizationServiceImpl;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContext;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.oidc.OidcProviderConfiguration;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * @author huangxiuqi
 */
@Configuration
public class ServerSecurityConfiguration {

    private static final Logger log = LoggerFactory.getLogger(ServerSecurityConfiguration.class);

    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {

        // end-session-point
        OidcEndSessionPointConfigurer endSessionPointConfigurer = new OidcEndSessionPointConfigurer();
        RequestMatcher endSessionPointRequestMatcher = endSessionPointConfigurer.getRequestMatcher();
        http.apply(endSessionPointConfigurer);

        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();
        RequestMatcher oauth2EndpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();
        RequestMatcher endpointsMatcher = new OrRequestMatcher(endSessionPointRequestMatcher, oauth2EndpointsMatcher);

        http.requestMatcher(endpointsMatcher)
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests.anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))
                .apply(authorizationServerConfigurer);

        Consumer<OidcProviderConfiguration.Builder> providerConfigurationCustomizer = providerConfiguration -> {
            AuthorizationServerContext authorizationServerContext = AuthorizationServerContextHolder.getContext();
            String issuer = authorizationServerContext.getIssuer();
            String endpointUri = UriComponentsBuilder.fromUriString(issuer)
                    .path(endSessionPointConfigurer.getEndSessionPoint())
                    .build()
                    .toUriString();
            providerConfiguration.claim("end_session_endpoint", endpointUri);
        };

        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .authorizationService(new InMemoryOauth2AuthorizationServiceImpl())
                .oidc(oidcConfigurer -> oidcConfigurer
                        .providerConfigurationEndpoint(oidcProviderConfigurationEndpointConfigurer ->
                                oidcProviderConfigurationEndpointConfigurer
                                        .providerConfigurationCustomizer(providerConfigurationCustomizer)));
        LoginUrlAuthenticationEntryPoint loginUrlAuthenticationEntryPoint = new LoginUrlAuthenticationEntryPoint("/login");
        loginUrlAuthenticationEntryPoint.setUseForward(true);
        http.exceptionHandling()
                .authenticationEntryPoint(loginUrlAuthenticationEntryPoint)
                .and()
                .oauth2ResourceServer()
                .jwt();


        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        CookieCsrfTokenRepository cookieCsrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        cookieCsrfTokenRepository.setSecure(false);
        cookieCsrfTokenRepository.setCookieDomain("hbpm.com");
        cookieCsrfTokenRepository.setCookieName("uua_csrf_token");
        return http
                .csrf()
                .csrfTokenRepository(cookieCsrfTokenRepository)
                .and()
                .cors()
                .disable()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/api/login/password")
                .failureHandler(new SecurityAuthenticationFailureHandler())
                .successHandler(new SecurityAuthenticationSuccessHandler())
                .and()
                .logout()
                .disable()
                .authorizeRequests()
                .antMatchers("/actuator/**")
                .permitAll()
                .antMatchers("/login/**", "/template/**")
                .permitAll()
                .antMatchers("/static/**", "/favicon.ico")
                .permitAll()
                .antMatchers("/js/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new SecurityAuthenticationEntryPoint())
                .accessDeniedHandler(new SecurityAccessDeniedHandler())
                .and()
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public RegisteredClientRepository registeredClientRepository() {
//        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
//                .clientId("hbpm")
//                .clientSecret("$2a$10$bA4zvxf8Ipf81mqaz02TTun4ZYz80mC501bPGH5c1ysNP.rb/717a")
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
//                .redirectUri("http://127.0.0.1:8080/login/oauth2/code/uua")
//                .redirectUri("http://hbpm.com/login/oauth2/code/uua")
//                .redirectUri("https://oidcdebugger.com/debug")
//                .postLogoutRedirectUri("http://127.0.0.1:20000")
//                .postLogoutRedirectUri("http://hbpm.com")
//                .scope(OidcScopes.OPENID)
//                .clientSettings(ClientSettings.builder().build())
//                .tokenSettings(TokenSettings.builder().build())
//                .build();
//
//        return new InMemoryRegisteredClientRepository(registeredClient);
//    }

    @Bean
    public RegisteredClientRepository registeredClientRepository(CoreOauth2ClientService coreOauth2ClientService) {
        return new FeignRegisteredClientRepository(coreOauth2ClientService);
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        }
        catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

//    @Bean
//    public CorsFilter corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.addAllowedOriginPattern("http://localhost:3000");
//        corsConfiguration.addAllowedHeader("*");
//        corsConfiguration.addAllowedMethod("*");
//        corsConfiguration.setAllowCredentials(true);
//        source.registerCorsConfiguration("/**", corsConfiguration);
//        return new CorsFilter(source);
//    }
}
