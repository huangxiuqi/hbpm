package com.hbpm.uua.service;

import com.hbpm.core.CoreOauth2ClientService;
import com.hbpm.uua.utils.RegisteredClientUtil;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

/**
 * @author huangxiuqi
 */
public class FeignRegisteredClientRepository implements RegisteredClientRepository {

    private final CoreOauth2ClientService coreOauth2ClientService;

    public FeignRegisteredClientRepository(CoreOauth2ClientService coreOauth2ClientService) {
        this.coreOauth2ClientService = coreOauth2ClientService;
    }

    @Override
    public void save(RegisteredClient registeredClient) {
        coreOauth2ClientService.save(RegisteredClientUtil.transform(registeredClient));
    }

    @Override
    public RegisteredClient findById(String id) {
        return coreOauth2ClientService.findById(id)
                .map(RegisteredClientUtil::transform)
                .orElse(null);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        return RegisteredClientUtil.transform(coreOauth2ClientService.findByClientId(clientId));
    }
}
