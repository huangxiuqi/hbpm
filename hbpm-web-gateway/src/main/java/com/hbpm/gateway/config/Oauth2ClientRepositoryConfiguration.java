package com.hbpm.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author huangxiuqi
 */
@Configuration
public class Oauth2ClientRepositoryConfiguration {

    @Bean
    public InMemoryReactiveClientRegistrationRepository customInMemoryReactiveClientRegistrationRepository() {

        Map<String, Object> metadata = new HashMap<>(2);
        metadata.put("end_session_endpoint", "http://login.hbpm.com/openid-connect/logout");

        ClientRegistration clientRegistration = ClientRegistration.withRegistrationId("uua")
                .clientId("hbpm")
                .clientSecret("123456a")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("http://hbpm.com/login/oauth2/code/{registrationId}")
                .scope("openid")
                .clientName("uua")
                .authorizationUri("http://login.hbpm.com/oauth2/authorize")
                .tokenUri("http://login.hbpm.com/oauth2/token")
                .jwkSetUri("http://login.hbpm.com/oauth2/jwks")
                .userInfoUri("http://login.hbpm.com/userinfo")
                .userNameAttributeName("sub")
                .providerConfigurationMetadata(Collections.unmodifiableMap(metadata))
                .build();

        return new InMemoryReactiveClientRegistrationRepository(clientRegistration);
    }
}
