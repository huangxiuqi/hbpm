package com.hbpm.base.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.TimeZone;


/**
 * 配置默认时区为UTC
 * @author huangxiuqi
 */
public class TimeZoneConfiguration implements SpringApplicationRunListener {

    private static final Logger log = LoggerFactory.getLogger(TimeZoneConfiguration.class);

    public TimeZoneConfiguration(SpringApplication application, String[] args) { }

    @Override
    public void starting(ConfigurableBootstrapContext bootstrapContext) {
        System.setProperty("user.timezone", "UTC");
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        TimeZone zone = TimeZone.getDefault();
        log.info("The default timezone is {}", zone.getID());
    }
}
