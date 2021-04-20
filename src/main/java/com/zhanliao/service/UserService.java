package com.zhanliao.service;

import com.zhanliao.erro.BusinessException;
import com.zhanliao.service.model.UserModel;

/**
 * @Author: ZhanLiao
 * @Description: 用户服务接口
 * @Date: 2021/3/9 10:36
 * @Version: 1.0
 */
public interface UserService {
    // 通过用户id获取用户对象
    public UserModel getUserById(Integer id);
    // 通过Redis缓存获取用户对象
    public UserModel getUserByIdInCache(Integer id);

    public void register(UserModel userModel) throws BusinessException;

    /**
     * @param telephone 用户手机
     * @param encryptPassword 用户加密后的密码
     * @throws BusinessException
     * @return
     */
    public UserModel validateLogin(String telephone, String encryptPassword) throws BusinessException;
}
