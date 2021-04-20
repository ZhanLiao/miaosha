package com.zhanliao.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.zhanliao.serializer.JodaDateTimeJsonDeserializer;
import com.zhanliao.serializer.JodaDateTimeJsonSerializer;
import org.joda.time.DateTime;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.stereotype.Component;

/**
 * @Author: ZhanLiao
 * @Description:
 * @Date: 2021/4/16 10:57
 * @Version: 1.0
 */
@Component
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 3600)
public class RedisConfig {
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // key的序列化方式
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);

        // value的序列化方式
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer =  new Jackson2JsonRedisSerializer(Object.class);
        //解决Redis时间信息的序列化
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(DateTime.class, new JodaDateTimeJsonSerializer());
        simpleModule.addDeserializer(DateTime.class, new JodaDateTimeJsonDeserializer());

        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL); // 加上这个，才能解析序列化之后的数据？

        objectMapper.registerModule(simpleModule);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);

        return redisTemplate;
    }
}
