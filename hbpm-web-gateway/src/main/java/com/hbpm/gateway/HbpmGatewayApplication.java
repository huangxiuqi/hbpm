package com.hbpm.gateway;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author huangxiuqi
 */
@EnableDiscoveryClient
@SpringBootApplication
public class HbpmGatewayApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(HbpmGatewayApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        System.out.println("ApplicationRunner");
//        new Thread(() -> {
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//            System.exit(0);
//        }).start();
    }
}
