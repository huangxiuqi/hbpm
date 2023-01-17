package com.hbpm.gateway.filter.factory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hbpm.base.constance.CaptchaConstance;
import com.hbpm.gateway.service.CaptchaService;
import com.hbpm.gateway.utils.WebFluxUtils;
import com.hbpm.model.core.CoreCaptchaEntity;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

/**
 * 图片验证码过滤器
 *
 * @author huangxiuqi
 */
public class CaptchaGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    private final ObjectMapper objectMapper;

    private final CaptchaService captchaService;

    public CaptchaGatewayFilterFactory(CaptchaService captchaService) {
        this.captchaService = captchaService;
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Collections.emptyList();
    }

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            MultiValueMap<String, String> queryParams = exchange.getRequest().getQueryParams();
            String captchaId = queryParams.getFirst(CaptchaConstance.CAPTCHA_ID_KEY);
            String captchaValue = queryParams.getFirst(CaptchaConstance.CAPTCHA_VALUE_KEY);

            if (!StringUtils.hasText(captchaId) || !StringUtils.hasText(captchaValue)) {
                return WebFluxUtils.sendJsonResponse(exchange, HttpStatus.BAD_REQUEST, "验证码不正确");
            }

            CoreCaptchaEntity captchaEntity = new CoreCaptchaEntity();
            captchaEntity.setCaptchaId(captchaId);
            captchaEntity.setCaptchaValue(captchaValue);

            return captchaService.verify(captchaEntity)
                    .flatMap(isValid -> {
                        if (isValid) {
                            return chain.filter(exchange);
                        } else {
                            return WebFluxUtils
                                    .sendJsonResponse(exchange, HttpStatus.BAD_REQUEST, "验证码不正确");
                        }
                    });
        };
    }
}
