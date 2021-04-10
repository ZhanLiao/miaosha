package com.zhanliao.service.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @Author: ZhanLiao
 * @Description:
 * @Date: 2021/4/7 10:58
 * @Version: 1.0
 */
public class ItemModel {

    private Integer id;

    // 商品名称
    @NotBlank(message = "商品名称不能为空")
    private String title;

    // 商品价格
    @NotNull(message = "商品价格不能为空")
    @Min(value = 0,message = "商品价格必须大于0")
    private BigDecimal price;   // 【为什么不可以用Double类型，因为传到前端有个精度问题】

    // 商品库存
    @NotNull(message = "商品库存不能为空")
    private Integer stock;

    // 商品描述
    @NotBlank(message = "商品描述不能为空")
    private String description;

    // 商品销量【销量不是创建商品的时候传入的】
    private Integer sales;

    // 商品图片url;
    @NotBlank(message = "商品图片信息不能为空")
    private String imgUrl;

    // 模型集合的方式，如果这个字段不为空，则即将秒杀或者正在秒杀的商品
    private PromoModel promoModel;

    public PromoModel getPromoModel() {
        return promoModel;
    }

    public void setPromoModel(PromoModel promoModel) {
        this.promoModel = promoModel;
    }

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
}
