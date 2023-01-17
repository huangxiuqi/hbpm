package com.hbpm.core.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hbpm.base.curd.BatchDeleteReqDTO;
import com.hbpm.base.curd.PageRequestDTO;
import com.hbpm.base.web.error.ApiException;
import com.hbpm.core.dto.gateway.route.GatewayRouteDefineDTO;
import com.hbpm.core.dto.gateway.route.RouteDefineMapper;
import com.hbpm.core.service.GatewayRouteDefineService;
import com.hbpm.model.gateway.GatewayRouteDefineEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * @author huangxiuqi
 */
@RestController
@RequestMapping("/api/gateway/route")
public class GatewayRouteDefineController {

    private final GatewayRouteDefineService gatewayRouteDefineService;

    public GatewayRouteDefineController(GatewayRouteDefineService gatewayRouteDefineService) {
        this.gatewayRouteDefineService = gatewayRouteDefineService;
    }

    @GetMapping("/{id}")
    public GatewayRouteDefineDTO get(@PathVariable("id") String id) {
        return gatewayRouteDefineService.findById(id)
                .map(RouteDefineMapper.INSTANCE::entity2dto)
                .orElseThrow(() -> new ApiException("实体不存在"));
    }

    @PostMapping("/page")
    public PageInfo<GatewayRouteDefineDTO> page(@RequestBody @Valid PageRequestDTO input) {
        return PageHelper.startPage(input.getPage() - 1, input.getSize())
                .doSelectPageInfo(() -> gatewayRouteDefineService.findAll()
                        .stream()
                        .map(RouteDefineMapper.INSTANCE::entity2dto)
                        .collect(Collectors.toList()));
    }

    @PostMapping
    public void save(@RequestBody @Valid GatewayRouteDefineDTO input) {
        GatewayRouteDefineEntity entity = RouteDefineMapper.INSTANCE.dto2entity(input);
        if (!StringUtils.hasText(entity.getId())) {
            entity.setCreateTime(LocalDateTime.now());
        }
        gatewayRouteDefineService.save(entity);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") String id) {
        gatewayRouteDefineService.deleteById(id);
    }

    @DeleteMapping("/batch")
    public void batchDelete(@RequestBody @Valid BatchDeleteReqDTO<String> input) {
        gatewayRouteDefineService.batchDeleteByIds(input.getIds());
    }
}
