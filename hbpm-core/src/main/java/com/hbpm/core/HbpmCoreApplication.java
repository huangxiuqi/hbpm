package com.hbpm.core;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author huangxiuqi
 */
@EnableDiscoveryClient
@MapperScan("com.hbpm.core.dao")
@EnableFeignClients(basePackages = "com.hbpm")
@SpringBootApplication
public class HbpmCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(HbpmCoreApplication.class, args);
    }

}
