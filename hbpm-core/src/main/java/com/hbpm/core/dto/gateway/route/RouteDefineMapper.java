package com.hbpm.core.dto.gateway.route;

import com.hbpm.model.gateway.GatewayRouteDefineEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author huangxiuqi
 */
@Mapper
public interface RouteDefineMapper {

    RouteDefineMapper INSTANCE = Mappers.getMapper(RouteDefineMapper.class);

    /**
     * 实体转dto
     * @param entity
     * @return
     */
    GatewayRouteDefineDTO entity2dto(GatewayRouteDefineEntity entity);

    /**
     * dto转实体
     * @param dto
     * @return
     */
    GatewayRouteDefineEntity dto2entity(GatewayRouteDefineDTO dto);

    /**
     * 实体列表转dto列表
     * @param entityList
     * @return
     */
    List<GatewayRouteDefineDTO> entity2dto(List<GatewayRouteDefineEntity> entityList);
}
