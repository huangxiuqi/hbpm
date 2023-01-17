package com.hbpm.core.controller;

import com.hbpm.core.service.CaptchaService;
import com.hbpm.model.core.CoreCaptchaEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huangxiuqi
 */
@RestController
@RequestMapping("/api/captcha")
public class CaptchaController {

    private final CaptchaService captchaService;

    public CaptchaController(CaptchaService captchaService) {
        this.captchaService = captchaService;
    }

    @GetMapping("/generate/gif")
    public CoreCaptchaEntity generateGif(
            @RequestParam(value = "width", required = false, defaultValue = CaptchaService.DEFAULT_GIF_WIDTH)
            Integer width,
            @RequestParam(value = "height", required = false, defaultValue = CaptchaService.DEFAULT_GIF_HEIGHT)
            Integer height) {
        return captchaService.generateGifCaptcha(width, height);
    }
}
