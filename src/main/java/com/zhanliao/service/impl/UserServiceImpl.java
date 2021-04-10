package com.zhanliao.service.impl;

import com.zhanliao.dao.UserDOMapper;
import com.zhanliao.dao.UserPasswordDOMapper;
import com.zhanliao.dataobject.UserDO;
import com.zhanliao.dataobject.UserPasswordDO;
import com.zhanliao.erro.BusinessException;
import com.zhanliao.erro.EmBusinessError;
import com.zhanliao.service.UserService;
import com.zhanliao.service.model.UserModel;
import com.zhanliao.validator.ValidationImpl;
import com.zhanliao.validator.ValidationResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @description: 用户实现类
 * @author: zhanliao
 * @create: 2021-03-09 10:39
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDOMapper userDOMapper;

    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;

    @Autowired
    private ValidationImpl validator;

    @Override
    public UserModel getUserById(Integer id) {
        // 调用userdomapper获取到对应的用户dataObject
        UserDO userDO = userDOMapper.selectByPrimaryKey(id);

        if (userDO == null){
            return null;
        }
        // 通过用户id获取对应的用户加密密码信息
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
        return convertFromDataObject(userDO, userPasswordDO);
    }

    @Override
    @Transactional
    public void register(UserModel userModel) throws BusinessException {
        if(userModel == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        /*if(StringUtils.isEmpty(userModel.getName())
            || userModel.getAge() == null
            || userModel.getGender() == null
            || StringUtils.isEmpty(userModel.getTelephone())){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }*/
        ValidationResult result = validator.validate(userModel);
        if (result.isHasError()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, result.getErrorMsg());
        }

        // 实现Mode转为dataObject
        UserDO userDO = convertFromMode(userModel);
        try {
            userDOMapper.insertSelective(userDO);
        }catch (DuplicateKeyException ex){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"手机号重复注册");
        }
        userModel.setId(userDO.getId());
        UserPasswordDO userPasswordDO = convertFromPasswordMode(userModel);
        userPasswordDOMapper.insertSelective(userPasswordDO);
    }

    @Override
    public UserModel validateLogin(String telephone, String encryptPassword) throws BusinessException {
        // 通过用户的手机获取用户信息
        UserDO userDO = userDOMapper.selectByTelephone(telephone);
        if (userDO == null){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
        UserModel userModel = convertFromDataObject(userDO, userPasswordDO);

        // 比对用户内加密的密码是否和传输进来的密码相匹配
        if (!StringUtils.equals(encryptPassword, userModel.getEncryptPassword())){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        };
        return userModel;
    }

    private UserPasswordDO convertFromPasswordMode(UserModel userModel) {
        if (userModel == null){
            return null;
        }
        UserPasswordDO userPasswordDO = new UserPasswordDO();
        userPasswordDO.setEncryptPassword(userModel.getEncryptPassword());
        userPasswordDO.setUserId(userModel.getId());
        return userPasswordDO;
    }

    private UserDO convertFromMode(UserModel userModel) {
        if(userModel == null){
            return null;
        }

        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userModel, userDO);
        return userDO;
    }


    private UserModel convertFromDataObject(UserDO userDO, UserPasswordDO userPasswordDO){
        if(userDO == null){
            return null;
        }
        UserModel userModel = new UserModel();
        if(userPasswordDO != null){
            BeanUtils.copyProperties(userDO, userModel);
            userModel.setEncryptPassword(userPasswordDO.getEncryptPassword());
        }
        return userModel;
    }
}
