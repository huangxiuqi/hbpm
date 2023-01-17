package com.hbpm.uua.configurer;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;
import java.util.Objects;

/**
 * OIDC end_session_endpoint过滤器
 * @author huangxiuqi
 */
public class OidcEndSessionPointFilter extends OncePerRequestFilter {

    private final static String LOCALHOST = "localhost";

    private final static int IPV4_LENGTH = 4;

    private final static String IPV6_LOCALHOST = "[0:0:0:0:0:0:0:1]";

    private final static String IPV6_LOCALHOST_SHORT = "[::1]";

    private final String ID_TOKEN_HINT_KEY = "id_token_hint";

    private final String POST_LOGOUT_REDIRECT_URI_KEY = "post_logout_redirect_uri";

    public final static OAuth2TokenType ID_TOKEN = new OAuth2TokenType("id_token");

    private final RequestMatcher requestMatcher;

    private final OAuth2AuthorizationService authorizationService;

    private final RegisteredClientRepository registeredClientRepository;

    public OidcEndSessionPointFilter(RequestMatcher requestMatcher,
                                     OAuth2AuthorizationService authorizationService,
                                     RegisteredClientRepository registeredClientRepository) {
        this.requestMatcher = requestMatcher;
        this.authorizationService = authorizationService;
        this.registeredClientRepository = registeredClientRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (!requestMatcher.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        SecurityContext context = SecurityContextHolder.getContext();

        String idTokenString = request.getParameter(ID_TOKEN_HINT_KEY);
        OAuth2Authorization token = null;
        if (StringUtils.hasText(idTokenString)) {
            // 校验IdToken与当前登录Session是否匹配
            token = authorizationService.findByToken(idTokenString, ID_TOKEN);

            if (token == null) {
                response.sendError(400, "invalid parameter id_token_hint");
                return;
            }
            Principal principal = token.getAttribute("java.security.Principal");
            if (!Objects.equals(principal, context.getAuthentication())) {
                response.sendError(400, "invalid parameter id_token_hint");
                return;
            }
        }

        String redirectUri = request.getParameter(POST_LOGOUT_REDIRECT_URI_KEY);
        if (StringUtils.hasText(redirectUri)) {
            if (!StringUtils.hasText(idTokenString) || token == null) {
                response.sendError(400, "missing parameter id_token_hint");
                return;
            }
            RegisteredClient registeredClient = registeredClientRepository.findById(token.getRegisteredClientId());
            if (registeredClient == null) {
                response.sendError(400, "invalid client");
                return;
            }
            if (validatePostLogoutRedirectUri(registeredClient, redirectUri, response)) {
                response.sendRedirect(redirectUri);
            }
        }

        // 清理session
        HttpSession session = request.getSession();
        if (session != null) {
            session.invalidate();
        }
        context.setAuthentication(null);
        SecurityContextHolder.clearContext();
    }

    /**
     * 参考实现
     * org.springframework.security.oauth2.server.authorization.authentication#validateRedirectUri
     * @param registeredClient
     * @param requestedRedirectUri
     * @param response
     * @return
     * @throws IOException
     */
    private static boolean validatePostLogoutRedirectUri(RegisteredClient registeredClient,
                                               String requestedRedirectUri,
                                               HttpServletResponse response) throws IOException {

        UriComponents requestedRedirect = null;
        try {
            requestedRedirect = UriComponentsBuilder.fromUriString(requestedRedirectUri).build();
        } catch (Exception ex) {
        }
        if (requestedRedirect == null || requestedRedirect.getFragment() != null) {
            response.sendError(400, "invalid parameter post_logout_redirect_uri");
            return false;
        }

        String requestedRedirectHost = requestedRedirect.getHost();
        if (requestedRedirectHost == null || LOCALHOST.equals(requestedRedirectHost)) {
            response.sendError(400, "invalid parameter post_logout_redirect_uri");
            return false;
        }

        if (!isLoopbackAddress(requestedRedirectHost)) {
            if (!registeredClient.getPostLogoutRedirectUri().contains(requestedRedirectUri)) {
                response.sendError(400, "invalid parameter post_logout_redirect_uri");
                return false;
            }
        } else {
            boolean validRedirectUri = false;
            for (String registeredRedirectUri : registeredClient.getPostLogoutRedirectUri()) {
                UriComponentsBuilder registeredRedirect = UriComponentsBuilder.fromUriString(registeredRedirectUri);
                registeredRedirect.port(requestedRedirect.getPort());
                if (registeredRedirect.build().toString().equals(requestedRedirect.toString())) {
                    validRedirectUri = true;
                    break;
                }
            }
            if (!validRedirectUri) {
                response.sendError(400, "invalid parameter post_logout_redirect_uri");
                return false;
            }
        }
        return true;
    }

    private static boolean isLoopbackAddress(String host) {
        // IPv6 loopback address should either be "0:0:0:0:0:0:0:1" or "::1"
        if (IPV6_LOCALHOST.equals(host) || IPV6_LOCALHOST_SHORT.equals(host)) {
            return true;
        }
        // IPv4 loopback address ranges from 127.0.0.1 to 127.255.255.255
        String[] ipv4Octets = host.split("\\.");
        if (ipv4Octets.length != IPV4_LENGTH) {
            return false;
        }
        try {
            int[] address = new int[ipv4Octets.length];
            for (int i=0; i < ipv4Octets.length; i++) {
                address[i] = Integer.parseInt(ipv4Octets[i]);
            }
            return address[0] == 127 && address[1] >= 0 && address[1] <= 255 && address[2] >= 0 &&
                    address[2] <= 255 && address[3] >= 1 && address[3] <= 255;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
}
