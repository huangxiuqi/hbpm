package com.hbpm.core.service;

import com.hbpm.model.core.CoreCaptchaEntity;

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
     * 生成图片验证码
     * @param width 图片宽
     * @param height 图片高
     * @return 验证码
     */
    CoreCaptchaEntity generateGifCaptcha(Integer width, Integer height);
}
