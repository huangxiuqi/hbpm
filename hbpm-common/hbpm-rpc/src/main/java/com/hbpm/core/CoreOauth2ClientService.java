package com.hbpm.core;

import com.hbpm.base.annotation.NotWrapResponse;
import com.hbpm.base.constance.ServiceConstance;
import com.hbpm.model.core.CoreOauth2ClientEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @author huangxiuqi
 */
@ResponseBody
@NotWrapResponse
@FeignClient(name = ServiceConstance.CORE_SERVICE,
        contextId = "coreOauth2ClientService",
        path = CoreOauth2ClientService.FEIGN_PATH)
public interface CoreOauth2ClientService {

    String FEIGN_PATH = "/feign/api/oauth2/client";

    /**
     * 根据Id查询
     *
     * @param id 客户端Id
     * @return 客户端实体
     */
    @GetMapping("/find_by_id")
    Optional<CoreOauth2ClientEntity> findById(@RequestParam("id") String id);

    /**
     * 根据client_id查询
     * @param clientId client_id
     * @return 客户端实体
     */
    @GetMapping("/find_by_client_id")
    CoreOauth2ClientEntity findByClientId(@RequestParam("clientId") String clientId);

    /**
     * 保存客户端
     * @param entity 客户端实体
     */
    @PostMapping("/save")
    void save(@RequestBody CoreOauth2ClientEntity entity);
}
