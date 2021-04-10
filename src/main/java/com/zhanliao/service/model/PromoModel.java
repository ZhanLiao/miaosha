package com.zhanliao.service.model;

import org.joda.time.DateTime;

import java.math.BigDecimal;

/**
 * @Author: ZhanLiao
 * @Description:
 * @Date: 2021/4/8 22:24
 * @Version: 1.0
 */
public class PromoModel {
    private Integer id;

    //秒杀活动的状态：1还没开始；2正在进行；3已结束；
    private Integer status;

    // 秒杀活动名称
    private String promoName;

    // 秒杀开始的时间
    private DateTime startDate;

    // 秒杀结束时间
    private DateTime endDate;

    // 秒杀活动的适用商品
    private Integer itemId;

    // 秒杀活动的商品价格
    private BigDecimal promoItemPrice;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPromoName() {
        return promoName;
    }

    public void setPromoName(String promoName) {
        this.promoName = promoName;
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public BigDecimal getPromoItemPrice() {
        return promoItemPrice;
    }

    public void setPromoItemPrice(BigDecimal promoItemPrice) {
        this.promoItemPrice = promoItemPrice;
    }
}
