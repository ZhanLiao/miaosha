package com.zhanliao.service;

import com.zhanliao.service.model.PromoModel;

/**
 * @Author: ZhanLiao
 * @Description:
 * @Date: 2021/4/8 22:39
 * @Version: 1.0
 */
public interface PromoService {

    // 根据itemid获取即将进行的或正在进行的秒杀活动
    PromoModel getPromoByItemId(Integer itemId);

    // 发布活动商品
    void publishPromoItem(Integer promoId);
}
