package com.hbpm.uua.config;

import com.hbpm.uua.model.CustomUserDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

/**
 * 自定义OIDC IdToken内容
 * @author huangxiuqi
 */
@Configuration
public class IdTokenCustomizerConfiguration {

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
        return (context) -> {
            if (OidcParameterNames.ID_TOKEN.equals(context.getTokenType().getValue())) {
                OidcUserInfo.Builder builder = OidcUserInfo.builder();
                if (context.getPrincipal() != null && context.getPrincipal().getPrincipal() instanceof CustomUserDetails) {
                    builder.subject(String.valueOf(((CustomUserDetails) context.getPrincipal().getPrincipal()).getId()));
                } else {
                    builder.subject(context.getPrincipal().getName());
                }
                OidcUserInfo userInfo = builder.build();
                context.getClaims().claims(claims -> claims.putAll(userInfo.getClaims()));
            }
        };
    }
}
