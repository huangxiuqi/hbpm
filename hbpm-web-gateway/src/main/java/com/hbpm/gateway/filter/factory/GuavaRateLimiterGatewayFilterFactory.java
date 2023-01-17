package com.hbpm.gateway.filter.factory;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.RateLimiter;
import com.hbpm.gateway.utils.WebFluxUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.springframework.cloud.gateway.support.GatewayToStringStyler.filterToStringCreator;

/**
 * @author huangxiuqi
 */
public class GuavaRateLimiterGatewayFilterFactory extends AbstractGatewayFilterFactory<GuavaRateLimiterGatewayFilterFactory.Config> {

    private final static Logger log = LoggerFactory.getLogger(GuavaRateLimiterGatewayFilterFactory.class);

    private final static String LIMIT_KEY = "limit";

    public GuavaRateLimiterGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList(LIMIT_KEY);
    }

    @Override
    public GatewayFilter apply(Config config) {
        int limit = config.limit;
        return new GatewayFilter() {

            private final Cache<String, RateLimiter> LOCAL_LIMITER_CACHE = CacheBuilder
                    .newBuilder()
                    .expireAfterAccess(Duration.ofMinutes(5))
                    .build();

            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                String ip = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
                try {
                    RateLimiter limiter = LOCAL_LIMITER_CACHE.get(ip, this::createNewRateLimiter);
                    if (limiter.tryAcquire()) {
                        return chain.filter(exchange);
                    }
                } catch (ExecutionException e) {
                    log.error("限流令牌桶异常", e);
                }
                return WebFluxUtils.sendJsonResponse(exchange, HttpStatus.TOO_MANY_REQUESTS, null);
            }

            private RateLimiter createNewRateLimiter() {
                return RateLimiter.create(limit);
            }

            @Override
            public String toString() {
                return filterToStringCreator(GuavaRateLimiterGatewayFilterFactory.this)
                        .append("limit", config.limit).toString();
            }
        };
    }

    public static class Config {

        private int limit;

        public int getLimit() {
            return limit;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }
    }
}
