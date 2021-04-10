package com.zhanliao.controller.viewObject;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @Author: ZhanLiao
 * @Description:
 * @Date: 2021/4/7 15:05
 * @Version: 1.0
 */
public class ItemVO {
    private Integer id;

    // 商品名称
    private String title;

    // 商品价格
    private BigDecimal price;   // 【为什么不可以用Double类型，因为传到前端有个精度问题】

    // 商品库存
    private Integer stock;

    // 商品描述
    private String description;

    // 商品销量【销量不是创建商品的时候传入的】
    private Integer sales;

    // 商品图片url;
    private String imgUrl;

    // 记录秒杀状态：1还没开始；2正在进行；3已结束；
    private Integer promoStatus;

    // 秒杀的价格
    private BigDecimal promoPrice;

    // 秒杀活动的id
    private Integer promoId;

    // 秒杀开始的时间
    private String promoStartDate;

    private String promoEndDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getPromoStatus() {
        return promoStatus;
    }

    public void setPromoStatus(Integer promoStatus) {
        this.promoStatus = promoStatus;
    }

    public BigDecimal getPromoPrice() {
        return promoPrice;
    }

    public void setPromoPrice(BigDecimal promoPrice) {
        this.promoPrice = promoPrice;
    }

    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }

    public String getPromoStartDate() {
        return promoStartDate;
    }

    public void setPromoStartDate(String promoStartDate) {
        this.promoStartDate = promoStartDate;
    }

    public String getPromoEndDate() {
        return promoEndDate;
    }

    public void setPromoEndDate(String promoEndDate) {
        this.promoEndDate = promoEndDate;
    }
}
