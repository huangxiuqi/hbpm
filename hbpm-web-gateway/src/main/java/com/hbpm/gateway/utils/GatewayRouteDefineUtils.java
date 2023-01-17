package com.hbpm.gateway.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hbpm.gateway.model.GatewayRouteDefineEntity;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

/**
 * @author huangxiuqi
 */
public class GatewayRouteDefineUtils {

    private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static RouteDefinition convert(GatewayRouteDefineEntity entity) throws URISyntaxException, JsonProcessingException {
        RouteDefinition definition = new RouteDefinition();
        definition.setId(entity.getId());
        definition.setUri(new URI(entity.getUri()));
        definition.setOrder(entity.getOrder());
        if (entity.getPredicates() != null) {
            List<PredicateDefinition> predicateDefinitions = OBJECT_MAPPER.readValue(
                    entity.getPredicates(),
                    new TypeReference<List<PredicateDefinition>>() { });
            definition.setPredicates(predicateDefinitions);
        }

        if (entity.getFilters() != null) {
            List<FilterDefinition> filterDefinitions = OBJECT_MAPPER.readValue(
                    entity.getFilters(),
                    new TypeReference<List<FilterDefinition>>() { });
            definition.setFilters(filterDefinitions);
        }

        if (entity.getMetadata() != null) {
            Map<String, Object> metadata = OBJECT_MAPPER.readValue(
                    entity.getMetadata(),
                    new TypeReference<Map<String, Object>>() {});
            definition.setMetadata(metadata);
        }

        return definition;
    }

    public static GatewayRouteDefineEntity convert(RouteDefinition definition) throws JsonProcessingException {
        GatewayRouteDefineEntity entity = new GatewayRouteDefineEntity();
        entity.setId(definition.getId());
        entity.setUri(definition.getUri().toString());
        entity.setPredicates(OBJECT_MAPPER.writeValueAsString(definition.getPredicates()));
        entity.setFilters(OBJECT_MAPPER.writeValueAsString(definition.getFilters()));
        entity.setMetadata(OBJECT_MAPPER.writeValueAsString(definition.getMetadata()));
        entity.setOrder(definition.getOrder());
        return entity;
    }
}
