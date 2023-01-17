package com.hbpm.contact.service;

import com.hbpm.base.curd.BaseTreeCurdService;
import com.hbpm.contact.dto.resource.ResourceDTO;
import com.hbpm.model.contact.ContactResourceEntity;

import java.util.List;

/**
 * @author huangxiuqi
 */
public interface ContactResourceService extends BaseTreeCurdService<Long, ContactResourceEntity> {

    /**
     * 获取树形列表
     * @param name 资源名称搜索
     * @return 资源树形列表
     */
    List<ResourceDTO> tree(String name);
}
