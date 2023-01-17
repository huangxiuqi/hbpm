package com.hbpm.core.service.impl;

import com.hbpm.base.idworker.IdWorker;
import com.hbpm.core.dao.GatewayRouteDefineDao;
import com.hbpm.core.service.GatewayRouteDefineService;
import org.springframework.stereotype.Service;

/**
 * @author huangxiuqi
 */
@Service
public class GatewayRouteDefineServiceImpl implements GatewayRouteDefineService {

    private final GatewayRouteDefineDao gatewayRouteDefineDao;

    private final IdWorker<String> idWorker;

    public GatewayRouteDefineServiceImpl(GatewayRouteDefineDao gatewayRouteDefineDao,
                                         IdWorker<String> idWorker) {
        this.gatewayRouteDefineDao = gatewayRouteDefineDao;
        this.idWorker = idWorker;
    }

    @Override
    public GatewayRouteDefineDao getDao() {
        return gatewayRouteDefineDao;
    }

    @Override
    public IdWorker<String> getIdWorker() {
        return idWorker;
    }
}
