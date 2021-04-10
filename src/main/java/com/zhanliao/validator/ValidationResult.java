package com.zhanliao.validator;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: ZhanLiao
 * @Description:
 * @Date: 2021/4/1 12:05
 * @Version: 1.0
 */
public class ValidationResult {
    // 定义是否有错误信息
    boolean hasError = false;
    Map<String, String> errorMsgMap = new HashMap<>();

    // 存放错误信息的map
    public boolean isHasError() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

    public Map<String, String> getErrorMsgMap() {
        return errorMsgMap;
    }

    public void setErrorMsgMap(Map<String, String> errorMsgMap) {
        this.errorMsgMap = errorMsgMap;
    }

    // 实现通用的通过格式化字符串信息获取错误结果的msg的方法
    public String getErrorMsg(){
        /**
         * 将map的所有值用，分割开，串成字符串
         */
        return StringUtils.join(errorMsgMap.values().toArray(), ",");
    }
}
