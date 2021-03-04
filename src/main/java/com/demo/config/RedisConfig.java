package com.demo.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * RedisTemplate的相关配置
 * 配置后redis可以存储对象（通过将对象序列化与反序列化）
 * 否则无法存储对象
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory); // 配置连接工厂
        // 使用RedisSerializer来序列化和反序列化redis的key值
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        // 使用GenericFastJsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        GenericFastJsonRedisSerializer genericFastJsonRedisSerializer = new GenericFastJsonRedisSerializer();

        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(genericFastJsonRedisSerializer);
        redisTemplate.setHashValueSerializer(genericFastJsonRedisSerializer);
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

}
