package com.hbpm.contact.service.impl;

import com.hbpm.base.curd.BaseTreeCurdServiceImpl;
import com.hbpm.base.curd.TreeRelationDao;
import com.hbpm.base.idworker.IdWorker;
import com.hbpm.base.utils.TreeUtils;
import com.hbpm.base.web.error.ApiException;
import com.hbpm.contact.dao.ContactResourceDao;
import com.hbpm.contact.dto.resource.ResourceDTO;
import com.hbpm.contact.dto.resource.ResourceMapper;
import com.hbpm.contact.service.ContactResourceService;
import com.hbpm.model.contact.ContactResourceEntity;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author huangxiuqi
 */
@Service
public class ContactResourceServiceImpl extends BaseTreeCurdServiceImpl<Long, ContactResourceEntity, ContactResourceDao> implements ContactResourceService {

    private final ContactResourceDao contactResourceDao;

//    private final TreeRelationDao<Long> treeRelationDao;

    private final IdWorker<Long> idWorker;

    public ContactResourceServiceImpl(ContactResourceDao contactResourceDao,
//                                      TreeRelationDao<Long> treeRelationDao,
                                      IdWorker<Long> idWorker) {
        this.contactResourceDao = contactResourceDao;
//        this.treeRelationDao = treeRelationDao;
        this.idWorker = idWorker;
    }

    @Override
    public ContactResourceDao getDao() {
        return contactResourceDao;
    }

    @Override
    protected TreeRelationDao<Long> getTreeRelationDao() {
        return contactResourceDao;
    }

    @Override
    protected String getTreeRelationTableName() {
        return "contact_resource_tree";
    }

    @Override
    public IdWorker<Long> getIdWorker() {
        return idWorker;
    }

    @Override
    public List<ResourceDTO> tree(String name) {
        List<ContactResourceEntity> list = contactResourceDao.selectAll()
                .stream()
                .filter(i -> {
                    if (name == null) {
                        return true;
                    }
                    return i.getName().toLowerCase().contains(name.toLowerCase());
                })
                .sorted(Comparator.comparingInt(ContactResourceEntity::getOrderNumber))
                .collect(Collectors.toList());
        return TreeUtils.listToTree(
                list,
                ResourceMapper.INSTANCE::entity2dto,
                ResourceDTO::getId,
                ResourceDTO::getParentId,
                ResourceDTO::setChildren);
    }
}
