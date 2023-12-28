package com.example.amelicanflight;

import java.util.concurrent.TimeUnit;

import org.cache2k.extra.spring.SpringCache2kCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * CachingConfig
 * API仕様において、期限付きキャッシュを実現するための設定
 */
@Configuration
@EnableCaching
public class CachingConfig {

    @Bean
    public CacheManager cacheManager() {
        return new SpringCache2kCacheManager()
                .defaultSetup(c -> c.entryCapacity(2000))
                .addCaches(
                        c -> c.name("flights").expireAfterWrite(60, TimeUnit.SECONDS).entryCapacity(10000),
                        c -> c.name("flight").expireAfterWrite(60, TimeUnit.SECONDS).entryCapacity(10000));
    }

}
