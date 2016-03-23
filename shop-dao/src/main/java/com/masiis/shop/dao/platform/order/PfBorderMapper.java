/*
 * PfBorderMapper.java
 * Copyright(C) 2014-2016 麦士集团
 * All rights reserved.
 * -----------------------------------------------
 * 2016-03-03 Created
 */
package com.masiis.shop.dao.platform.order;

import com.masiis.shop.dao.po.PfBorder;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PfBorderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PfBorder record);

    PfBorder selectByPrimaryKey(Long id);

    List<PfBorder> selectAll();

    int updateById(PfBorder pfBorder);

    PfBorder selectByOrderCode(String orderId);

    List<PfBorder> selectByUserId(@Param("userId") Long userId,@Param("orderStatus") Integer orderStatus,@Param("shipStatus") Integer shipStatus);

    List<PfBorder> selectByCondition(PfBorder pfBorder);

    void updateByPrimaryKey(PfBorder pfBorder);

    List<PfBorder> selectByUserAndDate( @Param("userid") Long userid,
                                        @Param("start") Date countStartDay,
                                        @Param("end") Date countEndDay);
}