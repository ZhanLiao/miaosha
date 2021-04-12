package com.zhanliao.controller;

import com.zhanliao.erro.BusinessException;
import com.zhanliao.erro.EmBusinessError;
import com.zhanliao.response.CommonReturnType;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * @Author: ZhanLiao
 * @Description:
 * @Date: 2021/4/12 16:46
 * @Version: 1.0
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    // 2-6 的补充不能被捕获的通用错误
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public CommonReturnType doError(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Exception ex){
        ex.printStackTrace();
        HashMap<String, Object> responseData = new HashMap<>();
        if (ex instanceof BusinessException){
            BusinessException businessException = (BusinessException) ex;
            responseData.put("errCode", businessException.getErrCode());
            responseData.put("errMsg", businessException.getErrMsg());
        }else if(ex instanceof ServletRequestBindingException){
            responseData.put("errCode", EmBusinessError.UNKNOWN_ERROR.getErrCode());
            responseData.put("errMsg", "url绑定路由错误");
        }else if(ex instanceof NoHandlerFoundException){
            responseData.put("errCode", EmBusinessError.UNKNOWN_ERROR.getErrCode());
            responseData.put("errMsg", "没有找到对应的访问路径");
        }else {
            responseData.put("errCode", EmBusinessError.UNKNOWN_ERROR.getErrCode());
            responseData.put("errMsg", "url绑定路由错误");
        }
        return CommonReturnType.create(responseData, "fail");
    }
}
