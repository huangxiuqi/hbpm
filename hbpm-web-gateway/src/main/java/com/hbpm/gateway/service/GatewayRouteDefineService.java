package com.hbpm.gateway.service;

import com.hbpm.gateway.model.GatewayRouteDefineEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author huangxiuqi
 */
public interface GatewayRouteDefineService {

    /**
     * 查询所有实体
     *
     * @return 路由实体列表
     */
    Flux<GatewayRouteDefineEntity> findAll();

    /**
     * 保存
     *
     * @param entity 路由实体
     * @return
     */
    Mono<GatewayRouteDefineEntity> save(@RequestBody @Validated GatewayRouteDefineEntity entity);

    /**
     * 根据id删除实体
     *
     * @param id 实体id
     * @return
     */
    Mono<Void> deleteById(@PathVariable("id") String id);

    /**
     * 刷新路由定义
     *
     * @return
     */
    Mono<Void> refreshRouteDefinition();
}
