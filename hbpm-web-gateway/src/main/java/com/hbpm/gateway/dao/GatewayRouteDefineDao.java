package com.hbpm.gateway.dao;

import com.hbpm.gateway.model.GatewayRouteDefineEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * @author huangxiuqi
 */
@Repository
public interface GatewayRouteDefineDao extends ReactiveCrudRepository<GatewayRouteDefineEntity, String> {

    /**
     * 查询所有启用的路由定义
     * @return 路由定义列表
     */
    Flux<GatewayRouteDefineEntity> findAllByEnableTrue();
}
