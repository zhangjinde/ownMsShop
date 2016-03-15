/*
 * PfBorderPayment.java
 * Copyright(C) 2014-2016 麦士集团
 * All rights reserved.
 * -----------------------------------------------
 * 2016-03-15 Created
 */
package com.masiis.shop.dao.po;

import java.math.BigDecimal;
import java.util.Date;

public class PfBorderPayment {

    private Long id;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 订单号
     */
    private Long pfBorderId;
    /**
     * 支付金额
     */
    private BigDecimal amount;
    /**
     * 支付类型id
     */
    private Integer payTypeId;
    /**
     * 支付类型名称
     */
    private String payTypeName;
    /**
     * 是否有效(0否1是)
     */
    private Integer isEnabled;
    /**
     * 备注
     */
    private String remark;
    /**
     * 外部订单号
     */
    private String outOrderId;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public Long getPfBorderId() {
        return pfBorderId;
    }
    public void setPfBorderId(Long pfBorderId) {
        this.pfBorderId = pfBorderId;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public Integer getPayTypeId() {
        return payTypeId;
    }
    public void setPayTypeId(Integer payTypeId) {
        this.payTypeId = payTypeId;
    }
    public String getPayTypeName() {
        return payTypeName;
    }
    public void setPayTypeName(String payTypeName) {
        this.payTypeName = payTypeName == null ? null : payTypeName.trim();
    }
    public Integer getIsEnabled() {
        return isEnabled;
    }
    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
    public String getOutOrderId() {
        return outOrderId;
    }
    public void setOutOrderId(String outOrderId) {
        this.outOrderId = outOrderId == null ? null : outOrderId.trim();
    }
}