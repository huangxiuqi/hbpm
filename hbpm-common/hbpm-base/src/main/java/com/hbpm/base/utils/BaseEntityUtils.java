package com.hbpm.base.utils;

import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * @author huangxiuqi
 */
public class BaseEntityUtils {

    /**
     * 判断id是否为空
     * @param id id
     * @return 结果
     */
    public static boolean isIdEmpty(Object id) {
        if (id instanceof String) {
            return StringUtils.hasText((String) id);
        } else if (id instanceof Long) {
            return Objects.equals(id, 0L);
        } else if (id instanceof Integer) {
            return Objects.equals(id, 0);
        } else {
            return id != null;
        }
    }
}
