package com.hbpm.base.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.List;


/**
 * 自动加载子模块配置文件
 * 配置文件应放置于resources/config目录下，文件后缀为.yml
 * @author huangxiuqi
 */
public class CommonConfigEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private static final Logger log = LoggerFactory.getLogger(CommonConfigEnvironmentPostProcessor.class);

    private final YamlPropertySourceLoader loader = new YamlPropertySourceLoader();

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {

        try {
            Resource[] resources = new PathMatchingResourcePatternResolver()
                    .getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + "config/*.yml");
            for (Resource resource : resources) {
                List<PropertySource<?>> configs = loader.load(resource.getFilename(), resource);
                if (configs != null && !configs.isEmpty()) {
                    environment.getPropertySources().addLast(configs.get(0));
                }
            }
        } catch (IOException e) {
            log.error("Load module config fail", e);
            throw new RuntimeException(e);
        }
    }
}
