package com.zhanliao.erro;

/**
 * @Author: ZhanLiao
 * @Description: 枚举类统一管理错误码和错误返回的信息
 * @Date: 2021/3/10 10:52
 * @Version: 1.0
 */
public enum EmBusinessError implements CommonError{
    // 10001通用错误类型,以后需要什么错误类型，可以直接在这里定义
    PARAMETER_VALIDATION_ERROR(10001, "参数不合法"),
    UNKNOWN_ERROR(10002, "未知错误"),

    //20001开头为用户信息相关错误定义
    USER_NOT_EXIST(20001,"用户不在"),
    USER_LOGIN_FAIL(20002, "手机号或密码错误"),
    USER_NOT_LOGIN(20003, "用户未登录"),

    // 30001开头为交易信息错误
    STOCK_NOT_ENOUGH(30001, "库存不足"),
    MQ_SEND_FAIL(30002, "异步扣减库存失败");

    private int errCode;
    private String errMsg;

    EmBusinessError(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    @Override
    public int getErrCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        /**
         * 可以定制化修改错误类型
         */
        this.errMsg = errMsg;
        return this;
    }
}
