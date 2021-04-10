package com.zhanliao.erro;

/**
 * @Author: ZhanLiao
 * @Description:  包装类模式的业务异常实现
 * @Date: 2021/3/10 11:13
 * @Version: 1.0
 */
public class BusinessException extends Exception implements CommonError {

    CommonError commonError;

    // 直接接受EmBusinessError的传参用于构造业务异常
    public BusinessException(CommonError commonError){
        super();
        this.commonError = commonError;
    }

    // 接受自定义errMsg的方式构造业务异常
    public BusinessException(CommonError commonError, String errMsg){
        super();
        this.commonError = commonError;
        commonError.setErrMsg(errMsg);
    }

    @Override
    public int getErrCode() {
        return this.commonError.getErrCode();
    }

    @Override
    public String getErrMsg() {
        return this.commonError.getErrMsg();
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.commonError.setErrMsg(errMsg);
        return this;
    }
}
