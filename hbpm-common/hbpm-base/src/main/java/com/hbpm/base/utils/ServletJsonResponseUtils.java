package com.hbpm.base.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hbpm.base.web.response.ResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author huangxiuqi
 */
public class ServletJsonResponseUtils {

    private static final Logger log = LoggerFactory.getLogger(ServletJsonResponseUtils.class);

    private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
    }

    public static void sendResponse(HttpServletResponse response, HttpStatus httpStatus) {
        sendResponse(response, httpStatus, httpStatus.getReasonPhrase());
    }

    public static void sendResponse(HttpServletResponse response, HttpStatus httpStatus, String message) {
        response.setStatus(httpStatus.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ResponseModel body = ResponseModel.of(httpStatus.value(), message, null);
        try {
            response.getWriter().write(OBJECT_MAPPER.writeValueAsString(body));
        } catch (IOException e) {
            log.error("序列化响应失败", e);
        }
    }
}
