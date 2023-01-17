package com.hbpm.base.annotation;

import java.lang.annotation.*;

/**
 * 响应不包裹统一结构
 * 控制器或方法上有此注解的将不添加统一响应结构
 * @author huangxiuqi
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotWrapResponse {
}
