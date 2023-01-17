package com.hbpm.base.idworker;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author huangxiuqi
 */
@ConfigurationProperties(prefix = "id-worker")
public class IdWorkerProperties {

    /**
     * 数据中心Id
     */
    private Long dataCenterId;

    /**
     * 部署机器Id
     */
    private Long workerId;

    public Long getDataCenterId() {
        return dataCenterId;
    }

    public void setDataCenterId(Long dataCenterId) {
        this.dataCenterId = dataCenterId;
    }

    public Long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Long workerId) {
        this.workerId = workerId;
    }
}
