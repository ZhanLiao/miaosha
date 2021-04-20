package com.zhanliao.service.impl;

import com.zhanliao.dao.PromoDOMapper;
import com.zhanliao.dataobject.PromoDO;
import com.zhanliao.service.ItemService;
import com.zhanliao.service.PromoService;
import com.zhanliao.service.model.ItemModel;
import com.zhanliao.service.model.PromoModel;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @Author: ZhanLiao
 * @Description:
 * @Date: 2021/4/8 22:43
 * @Version: 1.0
 */
@Service
public class PromoServiceImpl implements PromoService {

    @Autowired
    PromoDOMapper promoDOMapper;

    @Autowired
    ItemService itemService;

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public PromoModel getPromoByItemId(Integer itemId) {
        // 获取商品对应的秒杀活动信息
        PromoDO promoDO = promoDOMapper.selectByItemId(itemId);

        // DO -> Model
        PromoModel promoModel = convertFromPromoDO(promoDO);
        if (promoModel == null) {
            return null;
        }

        // 判断当前时间是否秒杀即将开始的时间还是正在进行的时间
        if (promoModel.getStartDate().isAfterNow()){
            promoModel.setStatus(1);
        }else if (promoModel.getEndDate().isBeforeNow()){
            promoModel.setStatus(3);
        }else {
            promoModel.setStatus(2);
        }

        return promoModel;
    }

    @Override
    public void publishPromoItem(Integer promoId) {
        // 通过活动商品id获取商品信息
        PromoDO promoDO = promoDOMapper.selectByPrimaryKey(promoId);
        if (promoDO.getItemId() == null || promoDO.getItemId().intValue() == 0){
            return;
        }
        ItemModel itemModel = itemService.getItemById(promoDO.getItemId());

        // 将库存信息存到Redis缓存中
        redisTemplate.opsForValue().set("promo_item_stock_" + itemModel.getId(), itemModel.getStock());


    }

    private PromoModel convertFromPromoDO(PromoDO promoDO) {

        if(promoDO == null){
            return null;
        }
        PromoModel promoModel = new PromoModel();
        BeanUtils.copyProperties(promoDO, promoModel);
        promoModel.setPromoItemPrice(new BigDecimal(promoDO.getPromoItemPrice()));
        promoModel.setStartDate(new DateTime(promoDO.getStartDate()));
        promoModel.setEndDate(new DateTime(promoDO.getEndDate()));
        return promoModel;
    }
}
