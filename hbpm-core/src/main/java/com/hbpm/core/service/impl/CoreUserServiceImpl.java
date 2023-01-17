package com.hbpm.core.service.impl;

import com.hbpm.core.CoreUserService;
import com.hbpm.core.dao.CoreUserDao;
import com.hbpm.model.core.CoreUserEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author huangxiuqi
 */
@Service
@RequestMapping(CoreUserService.FEIGN_PATH)
public class CoreUserServiceImpl implements CoreUserService {

    private final CoreUserDao coreUserDao;

    public CoreUserServiceImpl(CoreUserDao coreUserDao) {
        this.coreUserDao = coreUserDao;
    }

    @Override
    public CoreUserEntity findByUsername(String username) {
        return coreUserDao.findByUsername(username);
    }
}
