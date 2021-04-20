package com.zhanliao.controller;

import com.zhanliao.controller.viewObject.ItemVO;
import com.zhanliao.erro.BusinessException;
import com.zhanliao.response.CommonReturnType;
import com.zhanliao.service.CacheService;
import com.zhanliao.service.ItemService;
import com.zhanliao.service.PromoService;
import com.zhanliao.service.model.ItemModel;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * @Author: ZhanLiao
 * @Description:
 * @Date: 2021/4/7 15:04
 * @Version: 1.0
 */

@Controller("item")
@RequestMapping("/item")
//@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
//@CrossOrigin(allowCredentials = "true", origins = "http://127.0.0.1:8848")
@CrossOrigin(originPatterns = "*",allowCredentials="true")
public class ItemController extends BaseController{

    @Autowired
    ItemService itemService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CacheService cacheService;

    @Autowired
    PromoService promoService;

    @RequestMapping(value = "/createItem", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED}) //后面这个参数需要和前端对应
    @ResponseBody
    public CommonReturnType createItem(@RequestParam(name = "title") String title,
                                       @RequestParam(name = "description") String description,
                                       @RequestParam(name = "price") BigDecimal price,
                                       @RequestParam(name = "stock") Integer stock,
                                       @RequestParam(name = "imgUrl") String imgUrl) throws BusinessException {
        // 封装service请求用来创建商品
        final ItemModel itemModel = new ItemModel();
        itemModel.setTitle(title);
        itemModel.setDescription(description);
        itemModel.setPrice(price);
        itemModel.setStock(stock);
        itemModel.setImgUrl(imgUrl);
        ItemModel itemModelForReturn = itemService.createItem(itemModel);

        ItemVO itemVO = convertVOFromModel(itemModelForReturn);
        return CommonReturnType.create(itemVO);
    }

    @RequestMapping(value = "/get", method = {RequestMethod.GET})//
    @ResponseBody
    public CommonReturnType getItem(@RequestParam(name = "id") Integer id){
        /*ItemModel itemModel = itemService.getItemById(id);
        */

        // 加入本地缓存
        ItemModel itemModel = null;
        itemModel = (ItemModel) cacheService.getFromCommonCache("item_" + id);
        // 若本地缓存没有，到Redis缓存
        if(itemModel == null){
            // 1. 先从Redis缓存获取数据
            // 2. 若没有就从service访问
            itemModel = (ItemModel) redisTemplate.opsForValue().get("item_" + id);
            if(itemModel == null){
                itemModel = itemService.getItemById(id);
                redisTemplate.opsForValue().set("item_" + id, itemModel);
                redisTemplate.expire("item_" + id, 10, TimeUnit.MINUTES);

            }
            cacheService.setCommonCache("item_" + id, itemModel);
        }


        ItemVO itemVO = convertVOFromModel(itemModel);
        return CommonReturnType.create(itemVO);

    }

    @RequestMapping(value = "/listItem", method = {RequestMethod.GET})//
    @ResponseBody
    public CommonReturnType listItem(){
        final List<ItemModel> itemModelList = itemService.listItem();
        List<ItemVO> itemVOList = itemModelList.stream().map(itemModel -> {
            ItemVO itemVO = this.convertVOFromModel(itemModel);
            return itemVO;
        }).collect(Collectors.toList());

        return CommonReturnType.create(itemVOList);

    }

    private ItemVO convertVOFromModel(ItemModel itemModel) {
        if(itemModel == null){
            return null;
        }
        ItemVO itemVO = new ItemVO();
        BeanUtils.copyProperties(itemModel, itemVO);

        if (itemModel.getPromoModel() != null) {
            // 秒杀信息存到itemVO
            itemVO.setPromoStatus(itemModel.getPromoModel().getStatus());
            itemVO.setPromoId(itemModel.getPromoModel().getId());
            itemVO.setPromoPrice(itemModel.getPromoModel().getPromoItemPrice());
            itemVO.setPromoStartDate(itemModel.getPromoModel().getStartDate().toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
            itemVO.setPromoEndDate(itemModel.getPromoModel().getEndDate().toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
        }else {
            itemVO.setPromoStatus(3);
        }
        return itemVO;
    }


    // 发布活动商品，这是应该运维人员做的
    @RequestMapping(value = "/publishPromo", method = {RequestMethod.GET})//
    @ResponseBody
    public CommonReturnType publishPromo(@RequestParam(name = "id") Integer id){
        promoService.publishPromoItem(id);
        return CommonReturnType.create(null);
    }
}
