package com.hbpm.model.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author huangxiuqi
 */
@Schema(name = "验证码实体")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CoreCaptchaEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "验证码Id")
    @NotBlank(message = "验证码Id不能为空")
    private String captchaId;

    @Schema(name = "验证码图片，Base64编码")
    private String captchaImage;

    @Schema(name = "验证码值")
    private String captchaValue;

    public String getCaptchaId() {
        return captchaId;
    }

    public void setCaptchaId(String captchaId) {
        this.captchaId = captchaId;
    }

    public String getCaptchaImage() {
        return captchaImage;
    }

    public void setCaptchaImage(String captchaImage) {
        this.captchaImage = captchaImage;
    }

    public String getCaptchaValue() {
        return captchaValue;
    }

    public void setCaptchaValue(String captchaValue) {
        this.captchaValue = captchaValue;
    }

    public static String getRedisKey(String id) {
        return "hbpm:captcha:" + id;
    }
}
