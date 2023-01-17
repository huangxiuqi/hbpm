package com.hbpm.gateway.service;

import com.hbpm.gateway.dao.GatewayRouteDefineDao;
import com.hbpm.gateway.model.GatewayRouteDefineEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 * @author huangxiuqi
 */
@Service
public class GatewayRouteDefineServiceImpl implements GatewayRouteDefineService {

    private static final Logger log = LoggerFactory.getLogger(GatewayRouteDefineServiceImpl.class);

    private final GatewayRouteDefineDao gatewayRouteDefineDao;

    private final ApplicationEventPublisher publisher;


    public GatewayRouteDefineServiceImpl(GatewayRouteDefineDao gatewayRouteDefineDao,
                                         ApplicationEventPublisher publisher) {
        this.gatewayRouteDefineDao = gatewayRouteDefineDao;
        this.publisher = publisher;
    }

    @Override
    public Flux<GatewayRouteDefineEntity> findAll() {
        return gatewayRouteDefineDao.findAllByEnableTrue();
    }

    @Override
    public Mono<GatewayRouteDefineEntity> save(GatewayRouteDefineEntity entity) {
        return gatewayRouteDefineDao.save(entity);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return gatewayRouteDefineDao.deleteById(id);
    }

    @Override
    public Mono<Void> refreshRouteDefinition() {
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
        return Mono.empty();
    }
}
