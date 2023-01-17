package com.hbpm.uua.service;

import com.hbpm.core.CoreUserService;
import com.hbpm.model.core.CoreUserEntity;
import com.hbpm.uua.model.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * @author huangxiuqi
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final CoreUserService coreUserService;

    public UserDetailsServiceImpl(CoreUserService coreUserService) {
        this.coreUserService = coreUserService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CoreUserEntity userEntity = coreUserService.findByUsername(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException("");
        }
        return new CustomUserDetails(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getPassword(),
                Collections.emptyList());
    }
}
