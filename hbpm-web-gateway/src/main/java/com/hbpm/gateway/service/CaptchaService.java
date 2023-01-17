package com.hbpm.gateway.service;

import com.hbpm.model.core.CoreCaptchaEntity;
import reactor.core.publisher.Mono;

/**
 * @author huangxiuqi
 */
public interface CaptchaService {

    /**
     * 默认图片验证码宽度
     */
    String DEFAULT_GIF_WIDTH = "100";

    /**
     * 默认图片验证码高度
     */
    String DEFAULT_GIF_HEIGHT = "40";

    /**
     * 校验验证码
     * @param captcha 验证码
     * @return 校验结果
     */
    Mono<Boolean> verify(CoreCaptchaEntity captcha);
}
