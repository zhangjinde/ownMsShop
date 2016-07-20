package com.masiis.shop.dao.beans.user;

import java.math.BigDecimal;

/**
 * 团队统计
 * @author muchaofeng
 * @date 2016/6/7 10:51
 */

public class CountGroup {
    private Integer count;
    private String groupSum;
    private Integer orderNum;
    private BigDecimal groupMoney;

    public void setGroupMomey(BigDecimal groupMoney) {
        this.groupMoney = groupMoney;
    }

    public BigDecimal getGroupMoney() {
        return groupMoney;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public Integer getCount() {
        return count;
    }

    public String getGroupSum() {
        return groupSum;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setGroupSum(String groupSum) {
        this.groupSum = groupSum;
    }
}