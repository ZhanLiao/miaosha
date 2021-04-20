package com.zhanliao.service;

/**
 * @Author: ZhanLiao
 * @Description:
 * @Date: 2021/4/19 21:00
 * @Version: 1.0
 */
public interface CacheService {
    public void setCommonCache(String key, Object value);

    public Object getFromCommonCache(String key);
}
