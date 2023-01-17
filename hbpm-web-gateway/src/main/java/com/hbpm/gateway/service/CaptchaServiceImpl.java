package com.hbpm.gateway.service;

import com.hbpm.model.core.CoreCaptchaEntity;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author huangxiuqi
 */
@Service
public class CaptchaServiceImpl implements CaptchaService {

    private final ReactiveRedisTemplate<String, String> redisTemplate;

    public CaptchaServiceImpl(ReactiveRedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Mono<Boolean> verify(CoreCaptchaEntity captcha) {
        return redisTemplate.opsForValue()
                .get(CoreCaptchaEntity.getRedisKey(captcha.getCaptchaId()))
                .switchIfEmpty(Mono.error(new RuntimeException()))
                .map(value -> {
                    if (value == null) {
                        return false;
                    }
                    return value.equalsIgnoreCase(captcha.getCaptchaValue());
                })
                .onErrorReturn(false);
    }
}
