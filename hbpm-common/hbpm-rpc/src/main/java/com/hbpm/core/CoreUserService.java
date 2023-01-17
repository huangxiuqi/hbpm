package com.hbpm.core;

import com.hbpm.base.annotation.NotWrapResponse;
import com.hbpm.base.constance.ServiceConstance;
import com.hbpm.model.core.CoreUserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author huangxiuqi
 */
@ResponseBody
@NotWrapResponse
@FeignClient(name = ServiceConstance.CORE_SERVICE,
        contextId = "coreUserService",
        path = CoreUserService.FEIGN_PATH,
        fallback = CoreUserService.CoreUserServiceFallbackImpl.class,
        configuration = CoreUserService.CoreUserServiceFallbackConfiguration.class)
public interface CoreUserService {

    String FEIGN_PATH = "/feign/api/user";

    /**
     * 根据用户名查询
     * @param username 用户名
     * @return 用户实体
     */
    @GetMapping("/find_by_username")
    CoreUserEntity findByUsername(@RequestParam("username") String username);

    class CoreUserServiceFallbackConfiguration {

        @Bean
        public CoreUserServiceFallbackImpl coreUserServiceFallback() {
            return new CoreUserServiceFallbackImpl();
        }
    }

    class CoreUserServiceFallbackImpl implements CoreUserService {

        private static final Logger log = LoggerFactory.getLogger(CoreUserServiceFallbackImpl.class);

        @Override
        public CoreUserEntity findByUsername(String username) {
            log.warn("CoreUserServiceFallback");
            return null;
        }
    }
}
