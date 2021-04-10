package com.zhanliao.controller;

import com.zhanliao.erro.BusinessException;
import com.zhanliao.erro.EmBusinessError;
import com.zhanliao.response.CommonReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * @Author: ZhanLiao
 * @Description:
 * @Date: 2021/3/10 20:28
 * @Version: 1.0
 */
public class BaseController {
    public static final String CONTENT_TYPE_FORMED="application/x-www-form-urlencoded";
//    public static final String CONTENT_TYPE_FORMED="application/json";

    // 定义exceptionHandle解决未被controller层吸收的exception
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)  // service层出错的时候，拦截tomcat的响应状态码，将设置为OK状态，从而可以处理异常
    @ResponseBody       // 这个返回来的序列化方式和 EmBusinessError.UNKNOWN_ERROR的序列化方式会冲突，需要存在一个map返回
    public Object handlerException(HttpServletRequest request, Exception ex){
        HashMap<String, Object> responseData = new HashMap<>();
        if (ex instanceof BusinessException){
            // 需要强转为ex,序列化和反序列化
            BusinessException businessException = (BusinessException)ex;
            responseData.put("errCode", businessException.getErrCode());
            responseData.put("errMsg", businessException.getErrMsg());
        }else {
            // 将对应不属于BusinessException下的异常，直接返回未知错误信息
            responseData.put("errCode", EmBusinessError.UNKNOWN_ERROR.getErrCode());
            responseData.put("errMsg", EmBusinessError.UNKNOWN_ERROR.getErrMsg());
        }
        return CommonReturnType.create(responseData, "fail");
    }
}
