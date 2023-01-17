package com.hbpm.core.dao;

import com.hbpm.mapper.core.CoreOauth2ClientBaseMapper;
import com.hbpm.model.core.CoreOauth2ClientEntity;

/**
 * @author huangxiuqi
 */
public interface CoreOauth2ClientDao extends CoreOauth2ClientBaseMapper {

    /**
     * 根据client_id查询
     * @param clientId client_id
     * @return 客户端实体
     */
    CoreOauth2ClientEntity findByClientId(String clientId);
}
