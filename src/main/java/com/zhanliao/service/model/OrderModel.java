package com.zhanliao.service.model;

import java.math.BigDecimal;

/**
 * @Author: ZhanLiao
 * @Description:
 * @Date: 2021/4/8 8:25
 * @Version: 1.0
 */
public class OrderModel {
    //订单号
    private String id;

    // 用户id
    private Integer userId;

    // 购买的商品id
    private Integer itemId;

    // 购买商品时的单价, 若promoId不为空， 则表示秒杀价格
    private BigDecimal itemPrice;

    // 购买数量
    private Integer amount;

    // 购买金额， 若promoId不为空， 则表示秒杀价格
    private BigDecimal orderPrice;

    // 若非空，则表示以秒杀商品的方式下单
    private Integer promoId;

    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }
}
