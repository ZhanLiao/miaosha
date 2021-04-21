package com.zhanliao.service;

import com.zhanliao.erro.BusinessException;
import com.zhanliao.service.model.OrderModel;

/**
 * @Author: ZhanLiao
 * @Description:
 * @Date: 2021/4/8 8:40
 * @Version: 1.0
 */
public interface OrderService {
    // 两方式校验是否有秒杀商品
    //【使用这个】1、通过前端URL上传过来的秒杀活动id， 然后下单接口内校验对应id是否属于对应商品且活动一开始
    //2. 直接在下单接口内判断对应的商品是否存在秒杀活动，若进行中，则以秒杀价格下单
    OrderModel createOrder(Integer userId, Integer itemId, Integer promoId, Integer amount, String stockLogId) throws BusinessException;

}
