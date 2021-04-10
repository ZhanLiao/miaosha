package com.zhanliao.service.impl;

import com.zhanliao.dao.OrderDOMapper;
import com.zhanliao.dao.SequenceDOMapper;
import com.zhanliao.dataobject.OrderDO;
import com.zhanliao.dataobject.SequenceDO;
import com.zhanliao.erro.BusinessException;
import com.zhanliao.erro.EmBusinessError;
import com.zhanliao.service.ItemService;
import com.zhanliao.service.OrderService;
import com.zhanliao.service.UserService;
import com.zhanliao.service.model.ItemModel;
import com.zhanliao.service.model.OrderModel;
import com.zhanliao.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author: ZhanLiao
 * @Description:
 * @Date: 2021/4/8 8:42
 * @Version: 1.0
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    ItemService itemService;

    @Autowired
    UserService userService;

    @Autowired
    OrderDOMapper orderDOMapper;

    @Autowired
    SequenceDOMapper sequenceDOMapper;

    @Override
    @Transactional
    public OrderModel createOrder(Integer userId, Integer itemId, Integer promoId, Integer amount) throws BusinessException {
        // 1.校验下单状态， 下单的商品是否存在， 用户是否合法， 购买的数量是否正确
        final ItemModel itemModel = itemService.getItemById(itemId);
        if (itemModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "商品不存在");
        }
        final UserModel userModel = userService.getUserById(userId);
        if (userModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "用户信息不合法");
        }
        if (amount <=0 || amount > 99){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "下单的商品数量不合法");
        }

        //校验活动信息
        if (promoId != null){
            // (1) 校验对应活动商品是否存在
            if(promoId.intValue() != itemModel.getPromoModel().getId()){
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "活动商品信息不对");
            }
            // (2) 校验活动是否正在进行
            else if(itemModel.getPromoModel().getStatus() != 2){
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "活动商品还没开始");
            }
        }

        // 2. 落单减库存 or 支付减库存
        final boolean result = itemService.decreaseStock(itemId, amount);
        if (!result){
            throw new BusinessException(EmBusinessError.STOCK_NOT_ENOUGH);
        }

        // 3. 订单入库
        OrderModel orderModel = new OrderModel();
        orderModel.setUserId(userId);
        orderModel.setItemId(itemId);
        orderModel.setPromoId(promoId);
        orderModel.setAmount(amount);
        if(promoId != null){
            orderModel.setItemPrice(itemModel.getPromoModel().getPromoItemPrice());
        }else {
            orderModel.setItemPrice(itemModel.getPrice());
        }

        orderModel.setOrderPrice(orderModel.getItemPrice().multiply(new BigDecimal(amount)));
        // 生成交易流水号
        orderModel.setId(generateOrderNum());
        OrderDO orderDO = convertFromOrderModel(orderModel);
        orderDOMapper.insertSelective(orderDO);

        // 加上商品的销量
        itemService.increaseSales(itemId, amount);
        // 4. 返回前端
        return orderModel;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    String generateOrderNum() {
        // 一般为16位
        StringBuilder stringBuilder = new StringBuilder();
        // 前8位为时间序列
        LocalDateTime now = LocalDateTime.now();
        String nowDate = now.format(DateTimeFormatter.ISO_DATE).replace("-", "");
        stringBuilder.append(nowDate);

        // 中间6位为自增序列
        int sequence = 0;
        SequenceDO sequenceDO = sequenceDOMapper.getSequenceByName("order_name");
        sequence = sequenceDO.getCurrentValue();
        sequenceDO.setCurrentValue(sequence + sequenceDO.getStep());
        sequenceDOMapper.updateByPrimaryKeySelective(sequenceDO);
        String sequenceStr = String.valueOf(sequence);
        for (int i = 0; i < 6 - sequenceStr.length(); i++) {
            stringBuilder.append(0);

        }
        stringBuilder.append(sequenceStr);

        // 最后2位为分库分表[暂时固定]
        stringBuilder.append("00");

        return stringBuilder.toString();
    }

    private OrderDO convertFromOrderModel(OrderModel orderModel) {
        if (orderModel == null) {
            return null;
        }
        OrderDO orderDO = new OrderDO();
        BeanUtils.copyProperties(orderModel, orderDO);
        orderDO.setItemPrice(orderModel.getItemPrice().doubleValue());
        orderDO.setOrderPrice(orderModel.getOrderPrice().doubleValue());
        return orderDO;
    }
}
