package com.hbpm.admin;

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
public class HbpmAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(HbpmAdminApplication.class, args);
    }

}
