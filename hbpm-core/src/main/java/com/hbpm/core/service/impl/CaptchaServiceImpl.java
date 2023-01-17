package com.hbpm.core.service.impl;

import com.hbpm.core.service.CaptchaService;
import com.hbpm.model.core.CoreCaptchaEntity;
import com.wf.captcha.GifCaptcha;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

/**
 * @author huangxiuqi
 */
@Service
public class CaptchaServiceImpl implements CaptchaService {

    private final RedisTemplate<String, String> redisTemplate;

    public CaptchaServiceImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public CoreCaptchaEntity generateGifCaptcha(Integer width, Integer height) {
        width = Optional.ofNullable(width).orElse(Integer.parseInt(DEFAULT_GIF_WIDTH));
        height = Optional.ofNullable(height).orElse(Integer.parseInt(DEFAULT_GIF_HEIGHT));

        GifCaptcha specCaptcha = new GifCaptcha(width, height, 5);
        String captchaId = UUID.randomUUID().toString();
        String captchaValue = specCaptcha.text().toLowerCase();

        CoreCaptchaEntity entity = new CoreCaptchaEntity();
        entity.setCaptchaId(captchaId);
        entity.setCaptchaImage(specCaptcha.toBase64());

        redisTemplate.opsForValue().set(CoreCaptchaEntity.getRedisKey(captchaId), captchaValue, Duration.ofMinutes(2));
        return entity;
    }
}
