package com.hbpm.uua;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author huangxiuqi
 */
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.hbpm")
@SpringBootApplication
public class HbpmUuaApplication {

    public static void main(String[] args) {

        SpringApplication.run(HbpmUuaApplication.class, args);
    }

}
