/*
 * SfShopCartMapper.java
 * Copyright(C) 2014-2016 麦士集团
 * All rights reserved.
 * -----------------------------------------------
 * 2016-04-08 Created
 */
package com.masiis.shop.dao.mall.shop;

import com.masiis.shop.dao.po.SfShopCart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SfShopCartMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SfShopCart record);

    SfShopCart selectByPrimaryKey(Long id);

    List<SfShopCart> selectAll();

    int updateByPrimaryKey(SfShopCart record);

    List<SfShopCart>  getShopCartInfoByUserIdAndShopId(@Param("userId")Long userId,@Param("sfShopId")Long sfShopId);

    SfShopCart getProductInfoByUserIdAndShipIdAndSkuId(@Param("userId") Long userId, @Param("shopId") Long shopId,@Param("skuId") Integer skuId);
}