package com.hbpm.base.idworker;

/**
 * @author huangxiuqi
 */
public interface IdWorker<ID> {

    /**
     * 获取id
     * @return id值
     */
    ID nextId();
}
