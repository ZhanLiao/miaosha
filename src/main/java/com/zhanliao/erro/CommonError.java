package com.zhanliao.erro;

/**
 * @Author: ZhanLiao
 * @Description:
 * @Date: 2021/3/10 10:48
 * @Version: 1.0
 */
public interface CommonError {
    public int getErrCode();
    public String getErrMsg();
    public CommonError setErrMsg(String errMsg);
}
