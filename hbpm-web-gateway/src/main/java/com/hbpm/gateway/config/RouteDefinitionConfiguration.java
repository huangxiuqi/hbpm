package com.hbpm.gateway.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hbpm.gateway.model.GatewayRouteDefineEntity;
import com.hbpm.gateway.service.GatewayRouteDefineService;
import com.hbpm.gateway.utils.GatewayRouteDefineUtils;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URISyntaxException;

/**
 * @author huangxiuqi
 */
@Configuration
public class RouteDefinitionConfiguration {

    @Bean
    public DbRouteDefinitionRepository dbRouteDefinitionRepository(
            GatewayRouteDefineService gatewayRouteDefineService) {
        return new DbRouteDefinitionRepository(gatewayRouteDefineService);
    }

    public static class DbRouteDefinitionRepository implements RouteDefinitionRepository {

        private final GatewayRouteDefineService gatewayRouteDefineService;

        public DbRouteDefinitionRepository(GatewayRouteDefineService gatewayRouteDefineService) {
            this.gatewayRouteDefineService = gatewayRouteDefineService;
        }

        @Override
        public Flux<RouteDefinition> getRouteDefinitions() {

            return gatewayRouteDefineService.findAll()
                    .map(entity -> {
                        try {
                            return GatewayRouteDefineUtils.convert(entity);
                        } catch (URISyntaxException | JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }

        @Override
        public Mono<Void> save(Mono<RouteDefinition> route) {
            return route.flatMap(r -> {
                try {
                    GatewayRouteDefineEntity entity = GatewayRouteDefineUtils.convert(r);
                    return gatewayRouteDefineService.save(entity).then();
                } catch (JsonProcessingException e) {
                    return Mono.error(new RuntimeException(e));
                }
            });
        }

        @Override
        public Mono<Void> delete(Mono<String> routeId) {
            return routeId.flatMap(gatewayRouteDefineService::deleteById);
        }
    }
}
