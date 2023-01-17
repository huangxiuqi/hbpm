package com.hbpm.base.curd;

import java.io.Serializable;

/**
 * @author huangxiuqi
 */
public interface BaseEntity<ID> extends Serializable {

    /**
     * 获取实体id
     * @return 实体id
     */
    ID getId();

    /**
     * 设置实体id
     * @param id 实体id
     */
    void setId(ID id);
}
