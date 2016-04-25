/*
 * ComAgentLevel.java
 * Copyright(C) 2014-2016 麦士集团
 * All rights reserved.
 * -----------------------------------------------
 * 2016-03-03 Created
 */
package com.masiis.shop.dao.po;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 代理等级表
 * 
 * @author masiis
 * @version 1.0 2016-03-03
 */
public class ComAgentLevel {

    /**
     * 代理等级
     */
    private Integer id;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 代理等级名称
     */
    private String name;
    /**
     * 图片地址
     */
    private String imgUrl;
    private String remark;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
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
    public String getImgUrl() {
        return imgUrl;
    }
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl == null ? null : imgUrl.trim();
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    @Override
    public String toString() {
        return "ComAgentLevel{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", name='" + name + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}