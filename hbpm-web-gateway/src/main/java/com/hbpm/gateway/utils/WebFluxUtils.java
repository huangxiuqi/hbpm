package com.hbpm.gateway.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.web.server.util.matcher.MediaTypeServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author huangxiuqi
 */
public class WebFluxUtils {

    private static final String XHR_HEADER_NAME = "X-Requested-With";

    private static final String XHR_HEADER_VALUE = "XMLHttpRequest";

    public static ServerWebExchangeMatcher notXhrMatcher;

    public static MediaTypeServerWebExchangeMatcher htmlMatcher;

    public static ServerWebExchangeMatcher xhrMatcher;

    static {
        htmlMatcher = new MediaTypeServerWebExchangeMatcher(
                MediaType.APPLICATION_XHTML_XML,
                new MediaType("image", "*"),
                MediaType.TEXT_HTML,
                MediaType.TEXT_PLAIN);
        htmlMatcher.setIgnoredMediaTypes(Collections.singleton(MediaType.ALL));
        xhrMatcher = (exchange) -> {
            if (exchange.getRequest().getHeaders().getOrEmpty(XHR_HEADER_NAME).contains(XHR_HEADER_VALUE)) {
                return ServerWebExchangeMatcher.MatchResult.match();
            }
            return ServerWebExchangeMatcher.MatchResult.notMatch();
        };
        notXhrMatcher = new NegatedServerWebExchangeMatcher(xhrMatcher);
    }

    public static Mono<Void> sendJsonResponse(ServerWebExchange exchange, HttpStatus status, String message) {
        return sendJsonResponse(exchange, status, message, null);
    }

    public static Mono<Void> sendJsonResponse(ServerWebExchange exchange, HttpStatus status, String message, Object data) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return exchange.getResponse().writeWith(Mono.fromCallable(() -> {
            Map<String, Object> body = new HashMap<>(4);
            body.put("code", status.value());
            body.put("message", Optional.ofNullable(message).orElse(status.getReasonPhrase()));
            if (data != null) {
                body.put("data", data);
            }
            DataBuffer dataBuffer = exchange.getResponse().bufferFactory().allocateBuffer();
            ObjectMapper objectMapper = new ObjectMapper();
            dataBuffer.write(objectMapper.writeValueAsString(body), StandardCharsets.UTF_8);
            return dataBuffer;
        }));
    }
}
