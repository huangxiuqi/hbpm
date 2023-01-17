package com.hbpm.base.curd;

/**
 * 树形实体
 *
 * @author huangxiuqi
 */
public interface BaseTreeNodeEntity<ID> extends BaseEntity<ID> {

    /**
     * 获取上级id
     * @return 上级id
     */
    ID getParentId();
}
