package com.hbpm.core.dao;

import com.hbpm.mapper.core.CoreUserBaseMapper;
import com.hbpm.model.core.CoreUserEntity;

/**
 * @author huangxiuqi
 */
public interface CoreUserDao extends CoreUserBaseMapper {

    /**
     * 根据用户名查询
     * @param username 用户名
     * @return 用户实体
     */
    CoreUserEntity findByUsername(String username);
}
