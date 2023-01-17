package com.hbpm.uua.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hbpm.model.core.CoreOauth2ClientEntity;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.ConfigurationSettingNames;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;

/**
 * 转换工具类
 * @author huangxiuqi
 */
public class RegisteredClientUtil {

    private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();
        ClassLoader classLoader = JdbcRegisteredClientRepository.class.getClassLoader();
        List<Module> securityModules = SecurityJackson2Modules.getModules(classLoader);
        OBJECT_MAPPER.registerModules(securityModules);
        OBJECT_MAPPER.registerModule(new OAuth2AuthorizationServerJackson2Module());
    }

    public static RegisteredClient transform(CoreOauth2ClientEntity entity) {
        LocalDateTime clientIdIssuedAt = entity.getClientIdIssuedAt();
        LocalDateTime clientSecretExpiresAt = entity.getClientSecretExpiresAt();
        Set<String> clientAuthenticationMethods = StringUtils.commaDelimitedListToSet(entity.getClientAuthenticationMethods());
        Set<String> authorizationGrantTypes = StringUtils.commaDelimitedListToSet(entity.getAuthorizationGrantTypes());
        Set<String> redirectUris = StringUtils.commaDelimitedListToSet(entity.getRedirectUris());
        Set<String> clientScopes = StringUtils.commaDelimitedListToSet(entity.getScopes());

        // @formatter:off
        RegisteredClient.Builder builder = RegisteredClient.withId(entity.getId())
                .clientId(entity.getClientId())
                .clientIdIssuedAt(clientIdIssuedAt != null ? clientIdIssuedAt.toInstant(ZoneOffset.UTC) : null)
                .clientSecret(entity.getClientSecret())
                .clientSecretExpiresAt(clientSecretExpiresAt != null ? clientSecretExpiresAt.toInstant(ZoneOffset.UTC) : null)
                .clientName(entity.getClientName())
                .clientAuthenticationMethods((authenticationMethods) ->
                        clientAuthenticationMethods.forEach(authenticationMethod ->
                                authenticationMethods.add(resolveClientAuthenticationMethod(authenticationMethod))))
                .authorizationGrantTypes((grantTypes) ->
                        authorizationGrantTypes.forEach(grantType ->
                                grantTypes.add(resolveAuthorizationGrantType(grantType))))
                .redirectUris((uris) -> uris.addAll(redirectUris))
                .scopes((scopes) -> scopes.addAll(clientScopes));
        // @formatter:on

        Map<String, Object> clientSettingsMap = parseMap(entity.getClientSettings());
        builder.clientSettings(ClientSettings.withSettings(clientSettingsMap).build());

        Map<String, Object> tokenSettingsMap = parseMap(entity.getTokenSettings());
        TokenSettings.Builder tokenSettingsBuilder = TokenSettings.withSettings(tokenSettingsMap);
        if (!tokenSettingsMap.containsKey(ConfigurationSettingNames.Token.ACCESS_TOKEN_FORMAT)) {
            tokenSettingsBuilder.accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED);
        }
        builder.tokenSettings(tokenSettingsBuilder.build());

        return builder.build();
    }

    public static CoreOauth2ClientEntity transform(RegisteredClient client) {
        LocalDateTime clientIdIssuedAt = client.getClientIdIssuedAt() != null ?
                LocalDateTime.ofInstant(client.getClientIdIssuedAt(), ZoneId.of("UTC")) :
                LocalDateTime.ofInstant(Instant.now(), ZoneId.of("UTC"));

        LocalDateTime clientSecretExpiresAt = client.getClientSecretExpiresAt() != null ?
                LocalDateTime.ofInstant(client.getClientSecretExpiresAt(), ZoneId.of("UTC")) : null;

        List<String> clientAuthenticationMethods = new ArrayList<>(client.getClientAuthenticationMethods().size());
        client.getClientAuthenticationMethods().forEach(clientAuthenticationMethod ->
                clientAuthenticationMethods.add(clientAuthenticationMethod.getValue()));

        List<String> authorizationGrantTypes = new ArrayList<>(client.getAuthorizationGrantTypes().size());
        client.getAuthorizationGrantTypes().forEach(authorizationGrantType ->
                authorizationGrantTypes.add(authorizationGrantType.getValue()));

        CoreOauth2ClientEntity entity = new CoreOauth2ClientEntity();
        entity.setId(client.getId());
        entity.setClientId(client.getClientId());
        entity.setClientIdIssuedAt(clientIdIssuedAt);
        entity.setClientSecret(client.getClientSecret());
        entity.setClientSecretExpiresAt(clientSecretExpiresAt);
        entity.setClientName(client.getClientName());
        entity.setClientAuthenticationMethods(StringUtils.collectionToCommaDelimitedString(clientAuthenticationMethods));
        entity.setAuthorizationGrantTypes(StringUtils.collectionToCommaDelimitedString(authorizationGrantTypes));
        entity.setRedirectUris(StringUtils.collectionToCommaDelimitedString(client.getRedirectUris()));
        entity.setScopes(StringUtils.collectionToCommaDelimitedString(client.getScopes()));
        entity.setClientSettings(writeMap(client.getClientSettings().getSettings()));
        entity.setTokenSettings(writeMap(client.getTokenSettings().getSettings()));
        return entity;
    }

    private static AuthorizationGrantType resolveAuthorizationGrantType(String authorizationGrantType) {
        if (AuthorizationGrantType.AUTHORIZATION_CODE.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.AUTHORIZATION_CODE;
        } else if (AuthorizationGrantType.CLIENT_CREDENTIALS.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.CLIENT_CREDENTIALS;
        } else if (AuthorizationGrantType.REFRESH_TOKEN.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.REFRESH_TOKEN;
        }
        return new AuthorizationGrantType(authorizationGrantType);
    }

    private static ClientAuthenticationMethod resolveClientAuthenticationMethod(String clientAuthenticationMethod) {
        if (ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
        } else if (ClientAuthenticationMethod.CLIENT_SECRET_POST.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.CLIENT_SECRET_POST;
        } else if (ClientAuthenticationMethod.NONE.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.NONE;
        }
        return new ClientAuthenticationMethod(clientAuthenticationMethod);
    }

    private static Map<String, Object> parseMap(String data) {
        try {
            return OBJECT_MAPPER.readValue(data, new TypeReference<Map<String, Object>>() {});
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }

    private static String writeMap(Map<String, Object> data) {
        try {
            return OBJECT_MAPPER.writeValueAsString(data);
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }
}
