package com.hbpm.contact;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author huangxiuqi
 */
@EnableDiscoveryClient
@MapperScan("com.hbpm.contact.dao")
@EnableFeignClients(basePackages = "com.hbpm")
@SpringBootApplication
public class HbpmContactApplication {

    public static void main(String[] args) {
        SpringApplication.run(HbpmContactApplication.class, args);
    }

}
