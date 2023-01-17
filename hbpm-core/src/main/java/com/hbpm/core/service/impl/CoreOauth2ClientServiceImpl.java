package com.hbpm.core.service.impl;

import com.hbpm.core.CoreOauth2ClientService;
import com.hbpm.core.dao.CoreOauth2ClientDao;
import com.hbpm.model.core.CoreOauth2ClientEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

/**
 * @author huangxiuqi
 */
@Service
@RequestMapping(CoreOauth2ClientService.FEIGN_PATH)
public class CoreOauth2ClientServiceImpl implements CoreOauth2ClientService {

    private final CoreOauth2ClientDao coreOauth2ClientDao;

    public CoreOauth2ClientServiceImpl(CoreOauth2ClientDao coreOauth2ClientDao) {
        this.coreOauth2ClientDao = coreOauth2ClientDao;
    }

    @Override
    public Optional<CoreOauth2ClientEntity> findById(String id) {
        return coreOauth2ClientDao.selectByPrimaryKey(id);
    }

    @Override
    public CoreOauth2ClientEntity findByClientId(String clientId) {
        return coreOauth2ClientDao.findByClientId(clientId);
    }

    @Override
    public void save(CoreOauth2ClientEntity entity) {
        Optional<CoreOauth2ClientEntity> old = Optional.empty();
        if (entity.getId() != null) {
            old = findById(entity.getId());
        }

        if (old.isPresent()) {
            coreOauth2ClientDao.insert(entity);
        } else {
            coreOauth2ClientDao.updateByPrimaryKey(entity);
        }
    }
}
