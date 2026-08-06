package com.TrungHieu.DoAnTotNgiep.Config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.Cache;

@Configuration
public class RedisConfig implements CachingConfigurer {

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        // ObjectMapper hỗ trợ serialize/deserialize Java Time + lưu type info
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );

        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        // Cấu hình mặc định: TTL 10 phút, key là String, value là JSON
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer))
                .disableCachingNullValues();

        // Tuỳ chỉnh TTL riêng cho từng cache name
        Map<String, RedisCacheConfiguration> cacheConfigs = new HashMap<>();
        cacheConfigs.put("users",           defaultConfig.entryTtl(Duration.ofMinutes(5)));
        cacheConfigs.put("userInfo",        defaultConfig.entryTtl(Duration.ofMinutes(5)));
        cacheConfigs.put("userStats",       defaultConfig.entryTtl(Duration.ofMinutes(3)));
        cacheConfigs.put("teacherList",     defaultConfig.entryTtl(Duration.ofMinutes(10)));
        cacheConfigs.put("classes",         defaultConfig.entryTtl(Duration.ofMinutes(10)));
        cacheConfigs.put("classByTeacher",  defaultConfig.entryTtl(Duration.ofMinutes(5)));
        cacheConfigs.put("studentCount",    defaultConfig.entryTtl(Duration.ofMinutes(3)));
        cacheConfigs.put("exams",           defaultConfig.entryTtl(Duration.ofMinutes(10)));
        cacheConfigs.put("examTypes",       defaultConfig.entryTtl(Duration.ofMinutes(15)));
        cacheConfigs.put("examCountMap",    defaultConfig.entryTtl(Duration.ofMinutes(5)));
        cacheConfigs.put("errors",          defaultConfig.entryTtl(Duration.ofMinutes(10)));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigs)
                .build();
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return new CacheErrorHandler() {
            @Override
            public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
                System.err.println("[Cache Error] Unable to get from cache " + cache.getName() + ": " + exception.getMessage());
            }

            @Override
            public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
                System.err.println("[Cache Error] Unable to put into cache " + cache.getName() + ": " + exception.getMessage());
            }

            @Override
            public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
                System.err.println("[Cache Error] Unable to evict from cache " + cache.getName() + ": " + exception.getMessage());
            }

            @Override
            public void handleCacheClearError(RuntimeException exception, Cache cache) {
                System.err.println("[Cache Error] Unable to clean cache " + cache.getName() + ": " + exception.getMessage());
            }
        };
    }
}
