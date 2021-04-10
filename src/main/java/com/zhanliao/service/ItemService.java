package com.zhanliao.service;

import com.zhanliao.erro.BusinessException;
import com.zhanliao.service.model.ItemModel;

import java.util.List;

/**
 * @Author: ZhanLiao
 * @Description:
 * @Date: 2021/4/7 11:57
 * @Version: 1.0
 */
public interface ItemService {

    // 创建商品
    ItemModel createItem(ItemModel itemModel) throws BusinessException;

    // 商品列表浏览
    List<ItemModel> listItem();

    // 商品详情浏览
    ItemModel getItemById(Integer id);

    // 库存扣减
    boolean decreaseStock(Integer itemId, Integer amount) throws BusinessException;

    // 商品销量增加
    void increaseSales(Integer itemId, Integer amount) throws BusinessException;
}
