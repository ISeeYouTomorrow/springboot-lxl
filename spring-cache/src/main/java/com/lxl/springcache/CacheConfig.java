package com.lxl.springcache;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * 缓存配置信息
 * @author lxl lukas
 * @description
 * @create 2018/1/29
 */
@Configuration
@EnableCaching //标注启动缓存.
public class CacheConfig {

    private EhCacheManagerFactoryBean ehCacheManagerFactoryBean(){
        EhCacheManagerFactoryBean beanFactory = new EhCacheManagerFactoryBean();
        beanFactory.setConfigLocation(new ClassPathResource("ehcache.xml"));
        beanFactory.setShared(true);
        return beanFactory;
    }

    @Bean(name = "cacheManager")
    public EhCacheCacheManager ehCacheCacheManager(){
        return new EhCacheCacheManager(ehCacheManagerFactoryBean().getObject());
    }
}
