package com.hbpm.base.web.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.pagehelper.PageInfo;
import com.hbpm.base.annotation.NotWrapResponse;
import com.hbpm.base.curd.PageResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;

/**
 * 重写Response，包装统一响应格式
 * @author huangxiuqi
 */
@ControllerAdvice(basePackages = {"com.hbpm"})
public class ServletResponseBodyRewriter implements ResponseBodyAdvice<Object> {

    private static final Logger log = LoggerFactory.getLogger(ServletResponseBodyRewriter.class);

    private static final String JSON_VALUE = "json";

    private final ObjectMapper objectMapper;

    public ServletResponseBodyRewriter() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> converterType) {
        // 判断方法上是否有NotWrapResponse注解
        if (methodParameter.hasMethodAnnotation(NotWrapResponse.class)) {
            return false;
        }
        // 判断类型上是否有NotWrapResponse注解
        return AnnotationUtils.findAnnotation(methodParameter.getDeclaringClass(), NotWrapResponse.class) == null;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public Object beforeBodyWrite(Object o,
                                  MethodParameter returnType,
                                  MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        Object body = o;

        try {
            // 统一返回值格式 ResponseModel
            if (!(body instanceof ResponseModel)) {
                // String 额外处理
                if (body instanceof String && !mediaType.getType().contains(JSON_VALUE)) {
                    body = objectMapper.writeValueAsString(ResponseModel.success(body));
                } if (body instanceof PageInfo) {
                    PageResponseDTO<Object> newBody = new PageResponseDTO<>();
                    newBody.setList(((PageInfo<Object>) body).getList());
                    newBody.setTotal(((PageInfo<?>) body).getTotal());
                    body = ResponseModel.success(newBody);
                } else {
                    body = ResponseModel.success(body);
                }
            }
        } catch (Exception e) {
            log.error("request uri path: {}, format response body error", request.getURI().getPath(), e);
        }

        return body;
    }
}
