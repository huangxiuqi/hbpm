package com.hbpm.base.web.converter;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * Long类型序列化为String，防止前端精度丢失
 * 注入Jackson的module Bean，Spring会自动将其注册到ObjectMapper中
 * @author huangxiuqi
 */
@AutoConfiguration
public class LongToStringConverter {

    @Bean
    public SimpleModule jacksonLongToStringModule() {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        return simpleModule;
    }
}
