/*
 * SfShop.java
 * Copyright(C) 2014-2016 麦士集团
 * All rights reserved.
 * -----------------------------------------------
 * 2016-04-08 Created
 */
package com.masiis.shop.dao.po;

import java.util.Date;

public class SfShop {

    private Long id;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 小铺名称
     */
    private String name;
    /**
     * 小铺归属人id
     */
    private Long userId;
    /**
     * 小铺状态(0:关店1:开店)
     */
    private Integer status;
    /**
     * 小铺logo
     */
    private String logo;
    /**
     * 小铺说明
     */
    private byte[] explanation;
    /**
     * 呐喊次数
     */
    private Long shoutNum;
    /**
     * 小铺浏览量
     */
    private Long pageviews;
    /**
     * 小铺销售额
     */
    private Long saleAmount;
    /**
     * 二维码图片全称(不包括路径)
     */
    private String qrCode;
    /**
     * 备注
     */
    private String remark;

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
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getLogo() {
        return logo;
    }
    public void setLogo(String logo) {
        this.logo = logo == null ? null : logo.trim();
    }
    public byte[] getExplanation() {
        return explanation;
    }
    public void setExplanation(byte[] explanation) {
        this.explanation = explanation;
    }
    public Long getShoutNum() {
        return shoutNum;
    }
    public void setShoutNum(Long shoutNum) {
        this.shoutNum = shoutNum;
    }
    public Long getPageviews() {
        return pageviews;
    }
    public void setPageviews(Long pageviews) {
        this.pageviews = pageviews;
    }
    public Long getSaleAmount() {
        return saleAmount;
    }
    public void setSaleAmount(Long saleAmount) {
        this.saleAmount = saleAmount;
    }
    public String getQrCode() {
        return qrCode;
    }
    public void setQrCode(String qrCode) {
        this.qrCode = qrCode == null ? null : qrCode.trim();
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}