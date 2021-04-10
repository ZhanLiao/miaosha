package com.zhanliao.validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;


import java.util.Set;

/**
 * @Author: ZhanLiao
 * @Description: 初始化这个类对象之后，会回调afterPropertiesSet()这个方法
 * @Date: 2021/4/1 12:21
 * @Version: 1.0
 */
@Component
public class ValidationImpl implements InitializingBean {

    private Validator validator;

    // 实现校验方法返回校验结果
    public ValidationResult validate(Object bean){
        final ValidationResult validationResult = new ValidationResult();
        // 当所要校验的bean有错误的时候set就会存在值
        Set<ConstraintViolation<Object>> constraintViolationSet = validator.validate(bean);
        if(constraintViolationSet.size() > 0){
            validationResult.setHasError(true);
            // 采用lambda表达式，遍历set的字段，bean字段和对应错误信息
            constraintViolationSet.forEach(constraintViolation->{
                String errMsg = constraintViolation.getMessage();
                String propertyName = constraintViolation.getPropertyPath().toString();
                validationResult.getErrorMsgMap().put(propertyName,errMsg);
            });
        }
        return validationResult;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 将hibernate validation通过工厂的初始化方式使其实例化
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
}
