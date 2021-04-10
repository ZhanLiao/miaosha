package com.zhanliao.response;

/**
 * @Author: ZhanLiao
 * @Description:
 * @Date: 2021/3/10 9:09
 * @Version: 1.0
 */
public class CommonReturnType {

    // 表明请求的返回结果是“success”或“fail
    private String status;

    // 若为success，则data内返回前端需要的json数据
    // 若为fail， 则data内使用通用的错误码格式（后续补充）
    private Object data;

    public static CommonReturnType create(Object result){
        return CommonReturnType.create(result, "success");
    }

    public static CommonReturnType create(Object result, String status) {
        CommonReturnType type = new CommonReturnType();
        type.setStatus(status);
        type.setData(result);
        return type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
