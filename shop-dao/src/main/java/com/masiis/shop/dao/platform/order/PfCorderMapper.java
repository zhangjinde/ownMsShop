package com.masiis.shop.dao.platform.order;


import com.masiis.shop.dao.po.PfCorder;


import java.util.List;

/**
 * Created by 49134 on 2016/3/3.
 */
public interface PfCorderMapper {


    PfCorder selectById(Long id);

    List<PfCorder> selectByCondition(PfCorder pfCorder);

    PfCorder selectByOrderCode(String orderCode);

    /**
     * 添加一条记录
     * @param pfCorder
     */
    void insert(PfCorder pfCorder);

    void updateById(PfCorder pfCorder);

    void deleteById(Long id);

    PfCorder trialCorder(PfCorder pfCorder);

}
