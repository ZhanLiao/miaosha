package com.zhanliao.config;

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
}
