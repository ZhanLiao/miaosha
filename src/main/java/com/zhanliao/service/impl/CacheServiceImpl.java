package com.zhanliao.service.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.zhanliao.service.CacheService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * @Author: ZhanLiao
 * @Description:
 * @Date: 2021/4/19 21:02
 * @Version: 1.0
 */
@Service
public class CacheServiceImpl implements CacheService {
    private Cache<String, Object> commonCache = null;

    @PostConstruct
    public void init(){
        commonCache = CacheBuilder.newBuilder()
                // 设置缓存容器的初始容量
                .initialCapacity(10)
                // 设置缓存中最大可以存储100个key，超过之后按照LRU策略移除缓存项
                .maximumSize(100)
                // 设置写缓存后多少秒过期
                .expireAfterWrite(60, TimeUnit.SECONDS).build();
    }

    @Override
    public void setCommonCache(String key, Object value) {
        commonCache.put(key, value);
    }

    @Override
    public Object getFromCommonCache(String key) {
        return commonCache.getIfPresent(key);

    }
}
