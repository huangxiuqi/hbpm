package com.hbpm.contact.dao;

import com.hbpm.base.curd.BaseTreeCurdService;
import com.hbpm.base.curd.TreeRelationDao;
import com.hbpm.mapper.contact.ContactResourceBaseMapper;
import com.hbpm.model.contact.ContactResourceEntity;
import org.apache.ibatis.annotations.Param;

/**
 * @author huangxiuqi
 */
public interface ContactResourceDao extends ContactResourceBaseMapper, TreeRelationDao<Long> {

    /**
     * 查询子级数量
     * @param id 实体id
     * @return 子级数量
     */
    Long countChildren(@Param("id") Long id);
}
