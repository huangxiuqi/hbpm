package com.hbpm.base.idworker;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.Optional;
import java.util.UUID;

/**
 * @author huangxiuqi
 */
@AutoConfiguration(after = IdWorkerProperties.class)
@EnableConfigurationProperties(IdWorkerProperties.class)
public class IdWorkerConfiguration {

    @Bean
    public IdWorker<Long> snowflakeIdWorker(IdWorkerProperties properties) {
        long workerId = Optional.ofNullable(properties.getWorkerId()).orElse(0L);
        long dataCenterId = Optional.ofNullable(properties.getDataCenterId()).orElse(0L);
        return new SnowflakeIdWorker(workerId, dataCenterId);
    }

    @Bean
    public IdWorker<String> uuidIdWorker() {
        return () -> UUID.randomUUID().toString();
    }
}
