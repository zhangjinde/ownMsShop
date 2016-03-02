/*
 * PfUserSkuExample.java
 * Copyright(C) 2014-2016 麦士集团
 * All rights reserved.
 * -----------------------------------------------
 * 2016-03-02 Created
 */
package com.masiis.shop.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PfUserSkuExample {

    protected String orderByClause;
    protected boolean distinct;
    protected List<Criteria> oredCriteria;

    public PfUserSkuExample() {
        oredCriteria = new ArrayList<Criteria>();
    }
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }
    public String getOrderByClause() {
        return orderByClause;
    }
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }
    public boolean isDistinct() {
        return distinct;
    }
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }
    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * 平台用户代理商品关系表
     * 
     * @author masiis
     * @version 1.0 2016-03-02
     */
    protected abstract static class GeneratedCriteria {

        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }
        public boolean isValid() {
            return criteria.size() > 0;
        }
        public List<Criterion> getAllCriteria() {
            return criteria;
        }
        public List<Criterion> getCriteria() {
            return criteria;
        }
        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }
        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }
        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }
        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }
        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }
        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }
        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }
        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }
        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }
        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }
        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }
        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }
        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }
        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }
        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }
        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }
        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }
        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }
        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }
        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }
        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }
        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }
        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }
        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }
        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }
        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }
        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }
        public Criteria andCodeIsNull() {
            addCriterion("code is null");
            return (Criteria) this;
        }
        public Criteria andCodeIsNotNull() {
            addCriterion("code is not null");
            return (Criteria) this;
        }
        public Criteria andCodeEqualTo(String value) {
            addCriterion("code =", value, "code");
            return (Criteria) this;
        }
        public Criteria andCodeNotEqualTo(String value) {
            addCriterion("code <>", value, "code");
            return (Criteria) this;
        }
        public Criteria andCodeGreaterThan(String value) {
            addCriterion("code >", value, "code");
            return (Criteria) this;
        }
        public Criteria andCodeGreaterThanOrEqualTo(String value) {
            addCriterion("code >=", value, "code");
            return (Criteria) this;
        }
        public Criteria andCodeLessThan(String value) {
            addCriterion("code <", value, "code");
            return (Criteria) this;
        }
        public Criteria andCodeLessThanOrEqualTo(String value) {
            addCriterion("code <=", value, "code");
            return (Criteria) this;
        }
        public Criteria andCodeLike(String value) {
            addCriterion("code like", value, "code");
            return (Criteria) this;
        }
        public Criteria andCodeNotLike(String value) {
            addCriterion("code not like", value, "code");
            return (Criteria) this;
        }
        public Criteria andCodeIn(List<String> values) {
            addCriterion("code in", values, "code");
            return (Criteria) this;
        }
        public Criteria andCodeNotIn(List<String> values) {
            addCriterion("code not in", values, "code");
            return (Criteria) this;
        }
        public Criteria andCodeBetween(String value1, String value2) {
            addCriterion("code between", value1, value2, "code");
            return (Criteria) this;
        }
        public Criteria andCodeNotBetween(String value1, String value2) {
            addCriterion("code not between", value1, value2, "code");
            return (Criteria) this;
        }
        public Criteria andPidIsNull() {
            addCriterion("pid is null");
            return (Criteria) this;
        }
        public Criteria andPidIsNotNull() {
            addCriterion("pid is not null");
            return (Criteria) this;
        }
        public Criteria andPidEqualTo(Integer value) {
            addCriterion("pid =", value, "pid");
            return (Criteria) this;
        }
        public Criteria andPidNotEqualTo(Integer value) {
            addCriterion("pid <>", value, "pid");
            return (Criteria) this;
        }
        public Criteria andPidGreaterThan(Integer value) {
            addCriterion("pid >", value, "pid");
            return (Criteria) this;
        }
        public Criteria andPidGreaterThanOrEqualTo(Integer value) {
            addCriterion("pid >=", value, "pid");
            return (Criteria) this;
        }
        public Criteria andPidLessThan(Integer value) {
            addCriterion("pid <", value, "pid");
            return (Criteria) this;
        }
        public Criteria andPidLessThanOrEqualTo(Integer value) {
            addCriterion("pid <=", value, "pid");
            return (Criteria) this;
        }
        public Criteria andPidIn(List<Integer> values) {
            addCriterion("pid in", values, "pid");
            return (Criteria) this;
        }
        public Criteria andPidNotIn(List<Integer> values) {
            addCriterion("pid not in", values, "pid");
            return (Criteria) this;
        }
        public Criteria andPidBetween(Integer value1, Integer value2) {
            addCriterion("pid between", value1, value2, "pid");
            return (Criteria) this;
        }
        public Criteria andPidNotBetween(Integer value1, Integer value2) {
            addCriterion("pid not between", value1, value2, "pid");
            return (Criteria) this;
        }
        public Criteria andSkuIdIsNull() {
            addCriterion("sku_id is null");
            return (Criteria) this;
        }
        public Criteria andSkuIdIsNotNull() {
            addCriterion("sku_id is not null");
            return (Criteria) this;
        }
        public Criteria andSkuIdEqualTo(Integer value) {
            addCriterion("sku_id =", value, "skuId");
            return (Criteria) this;
        }
        public Criteria andSkuIdNotEqualTo(Integer value) {
            addCriterion("sku_id <>", value, "skuId");
            return (Criteria) this;
        }
        public Criteria andSkuIdGreaterThan(Integer value) {
            addCriterion("sku_id >", value, "skuId");
            return (Criteria) this;
        }
        public Criteria andSkuIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("sku_id >=", value, "skuId");
            return (Criteria) this;
        }
        public Criteria andSkuIdLessThan(Integer value) {
            addCriterion("sku_id <", value, "skuId");
            return (Criteria) this;
        }
        public Criteria andSkuIdLessThanOrEqualTo(Integer value) {
            addCriterion("sku_id <=", value, "skuId");
            return (Criteria) this;
        }
        public Criteria andSkuIdIn(List<Integer> values) {
            addCriterion("sku_id in", values, "skuId");
            return (Criteria) this;
        }
        public Criteria andSkuIdNotIn(List<Integer> values) {
            addCriterion("sku_id not in", values, "skuId");
            return (Criteria) this;
        }
        public Criteria andSkuIdBetween(Integer value1, Integer value2) {
            addCriterion("sku_id between", value1, value2, "skuId");
            return (Criteria) this;
        }
        public Criteria andSkuIdNotBetween(Integer value1, Integer value2) {
            addCriterion("sku_id not between", value1, value2, "skuId");
            return (Criteria) this;
        }
        public Criteria andAgentLevelIdIsNull() {
            addCriterion("agent_level_id is null");
            return (Criteria) this;
        }
        public Criteria andAgentLevelIdIsNotNull() {
            addCriterion("agent_level_id is not null");
            return (Criteria) this;
        }
        public Criteria andAgentLevelIdEqualTo(Integer value) {
            addCriterion("agent_level_id =", value, "agentLevelId");
            return (Criteria) this;
        }
        public Criteria andAgentLevelIdNotEqualTo(Integer value) {
            addCriterion("agent_level_id <>", value, "agentLevelId");
            return (Criteria) this;
        }
        public Criteria andAgentLevelIdGreaterThan(Integer value) {
            addCriterion("agent_level_id >", value, "agentLevelId");
            return (Criteria) this;
        }
        public Criteria andAgentLevelIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("agent_level_id >=", value, "agentLevelId");
            return (Criteria) this;
        }
        public Criteria andAgentLevelIdLessThan(Integer value) {
            addCriterion("agent_level_id <", value, "agentLevelId");
            return (Criteria) this;
        }
        public Criteria andAgentLevelIdLessThanOrEqualTo(Integer value) {
            addCriterion("agent_level_id <=", value, "agentLevelId");
            return (Criteria) this;
        }
        public Criteria andAgentLevelIdIn(List<Integer> values) {
            addCriterion("agent_level_id in", values, "agentLevelId");
            return (Criteria) this;
        }
        public Criteria andAgentLevelIdNotIn(List<Integer> values) {
            addCriterion("agent_level_id not in", values, "agentLevelId");
            return (Criteria) this;
        }
        public Criteria andAgentLevelIdBetween(Integer value1, Integer value2) {
            addCriterion("agent_level_id between", value1, value2, "agentLevelId");
            return (Criteria) this;
        }
        public Criteria andAgentLevelIdNotBetween(Integer value1, Integer value2) {
            addCriterion("agent_level_id not between", value1, value2, "agentLevelId");
            return (Criteria) this;
        }
        public Criteria andIsPayIsNull() {
            addCriterion("is_pay is null");
            return (Criteria) this;
        }
        public Criteria andIsPayIsNotNull() {
            addCriterion("is_pay is not null");
            return (Criteria) this;
        }
        public Criteria andIsPayEqualTo(Integer value) {
            addCriterion("is_pay =", value, "isPay");
            return (Criteria) this;
        }
        public Criteria andIsPayNotEqualTo(Integer value) {
            addCriterion("is_pay <>", value, "isPay");
            return (Criteria) this;
        }
        public Criteria andIsPayGreaterThan(Integer value) {
            addCriterion("is_pay >", value, "isPay");
            return (Criteria) this;
        }
        public Criteria andIsPayGreaterThanOrEqualTo(Integer value) {
            addCriterion("is_pay >=", value, "isPay");
            return (Criteria) this;
        }
        public Criteria andIsPayLessThan(Integer value) {
            addCriterion("is_pay <", value, "isPay");
            return (Criteria) this;
        }
        public Criteria andIsPayLessThanOrEqualTo(Integer value) {
            addCriterion("is_pay <=", value, "isPay");
            return (Criteria) this;
        }
        public Criteria andIsPayIn(List<Integer> values) {
            addCriterion("is_pay in", values, "isPay");
            return (Criteria) this;
        }
        public Criteria andIsPayNotIn(List<Integer> values) {
            addCriterion("is_pay not in", values, "isPay");
            return (Criteria) this;
        }
        public Criteria andIsPayBetween(Integer value1, Integer value2) {
            addCriterion("is_pay between", value1, value2, "isPay");
            return (Criteria) this;
        }
        public Criteria andIsPayNotBetween(Integer value1, Integer value2) {
            addCriterion("is_pay not between", value1, value2, "isPay");
            return (Criteria) this;
        }
        public Criteria andIsCertificateIsNull() {
            addCriterion("is_certificate is null");
            return (Criteria) this;
        }
        public Criteria andIsCertificateIsNotNull() {
            addCriterion("is_certificate is not null");
            return (Criteria) this;
        }
        public Criteria andIsCertificateEqualTo(Integer value) {
            addCriterion("is_certificate =", value, "isCertificate");
            return (Criteria) this;
        }
        public Criteria andIsCertificateNotEqualTo(Integer value) {
            addCriterion("is_certificate <>", value, "isCertificate");
            return (Criteria) this;
        }
        public Criteria andIsCertificateGreaterThan(Integer value) {
            addCriterion("is_certificate >", value, "isCertificate");
            return (Criteria) this;
        }
        public Criteria andIsCertificateGreaterThanOrEqualTo(Integer value) {
            addCriterion("is_certificate >=", value, "isCertificate");
            return (Criteria) this;
        }
        public Criteria andIsCertificateLessThan(Integer value) {
            addCriterion("is_certificate <", value, "isCertificate");
            return (Criteria) this;
        }
        public Criteria andIsCertificateLessThanOrEqualTo(Integer value) {
            addCriterion("is_certificate <=", value, "isCertificate");
            return (Criteria) this;
        }
        public Criteria andIsCertificateIn(List<Integer> values) {
            addCriterion("is_certificate in", values, "isCertificate");
            return (Criteria) this;
        }
        public Criteria andIsCertificateNotIn(List<Integer> values) {
            addCriterion("is_certificate not in", values, "isCertificate");
            return (Criteria) this;
        }
        public Criteria andIsCertificateBetween(Integer value1, Integer value2) {
            addCriterion("is_certificate between", value1, value2, "isCertificate");
            return (Criteria) this;
        }
        public Criteria andIsCertificateNotBetween(Integer value1, Integer value2) {
            addCriterion("is_certificate not between", value1, value2, "isCertificate");
            return (Criteria) this;
        }
        public Criteria andPfCorderIdIsNull() {
            addCriterion("pf_corder_id is null");
            return (Criteria) this;
        }
        public Criteria andPfCorderIdIsNotNull() {
            addCriterion("pf_corder_id is not null");
            return (Criteria) this;
        }
        public Criteria andPfCorderIdEqualTo(Long value) {
            addCriterion("pf_corder_id =", value, "pfCorderId");
            return (Criteria) this;
        }
        public Criteria andPfCorderIdNotEqualTo(Long value) {
            addCriterion("pf_corder_id <>", value, "pfCorderId");
            return (Criteria) this;
        }
        public Criteria andPfCorderIdGreaterThan(Long value) {
            addCriterion("pf_corder_id >", value, "pfCorderId");
            return (Criteria) this;
        }
        public Criteria andPfCorderIdGreaterThanOrEqualTo(Long value) {
            addCriterion("pf_corder_id >=", value, "pfCorderId");
            return (Criteria) this;
        }
        public Criteria andPfCorderIdLessThan(Long value) {
            addCriterion("pf_corder_id <", value, "pfCorderId");
            return (Criteria) this;
        }
        public Criteria andPfCorderIdLessThanOrEqualTo(Long value) {
            addCriterion("pf_corder_id <=", value, "pfCorderId");
            return (Criteria) this;
        }
        public Criteria andPfCorderIdIn(List<Long> values) {
            addCriterion("pf_corder_id in", values, "pfCorderId");
            return (Criteria) this;
        }
        public Criteria andPfCorderIdNotIn(List<Long> values) {
            addCriterion("pf_corder_id not in", values, "pfCorderId");
            return (Criteria) this;
        }
        public Criteria andPfCorderIdBetween(Long value1, Long value2) {
            addCriterion("pf_corder_id between", value1, value2, "pfCorderId");
            return (Criteria) this;
        }
        public Criteria andPfCorderIdNotBetween(Long value1, Long value2) {
            addCriterion("pf_corder_id not between", value1, value2, "pfCorderId");
            return (Criteria) this;
        }
        public Criteria andIsAuthorizedIsNull() {
            addCriterion("is_authorized is null");
            return (Criteria) this;
        }
        public Criteria andIsAuthorizedIsNotNull() {
            addCriterion("is_authorized is not null");
            return (Criteria) this;
        }
        public Criteria andIsAuthorizedEqualTo(Integer value) {
            addCriterion("is_authorized =", value, "isAuthorized");
            return (Criteria) this;
        }
        public Criteria andIsAuthorizedNotEqualTo(Integer value) {
            addCriterion("is_authorized <>", value, "isAuthorized");
            return (Criteria) this;
        }
        public Criteria andIsAuthorizedGreaterThan(Integer value) {
            addCriterion("is_authorized >", value, "isAuthorized");
            return (Criteria) this;
        }
        public Criteria andIsAuthorizedGreaterThanOrEqualTo(Integer value) {
            addCriterion("is_authorized >=", value, "isAuthorized");
            return (Criteria) this;
        }
        public Criteria andIsAuthorizedLessThan(Integer value) {
            addCriterion("is_authorized <", value, "isAuthorized");
            return (Criteria) this;
        }
        public Criteria andIsAuthorizedLessThanOrEqualTo(Integer value) {
            addCriterion("is_authorized <=", value, "isAuthorized");
            return (Criteria) this;
        }
        public Criteria andIsAuthorizedIn(List<Integer> values) {
            addCriterion("is_authorized in", values, "isAuthorized");
            return (Criteria) this;
        }
        public Criteria andIsAuthorizedNotIn(List<Integer> values) {
            addCriterion("is_authorized not in", values, "isAuthorized");
            return (Criteria) this;
        }
        public Criteria andIsAuthorizedBetween(Integer value1, Integer value2) {
            addCriterion("is_authorized between", value1, value2, "isAuthorized");
            return (Criteria) this;
        }
        public Criteria andIsAuthorizedNotBetween(Integer value1, Integer value2) {
            addCriterion("is_authorized not between", value1, value2, "isAuthorized");
            return (Criteria) this;
        }
        public Criteria andRemarkIsNull() {
            addCriterion("remark is null");
            return (Criteria) this;
        }
        public Criteria andRemarkIsNotNull() {
            addCriterion("remark is not null");
            return (Criteria) this;
        }
        public Criteria andRemarkEqualTo(String value) {
            addCriterion("remark =", value, "remark");
            return (Criteria) this;
        }
        public Criteria andRemarkNotEqualTo(String value) {
            addCriterion("remark <>", value, "remark");
            return (Criteria) this;
        }
        public Criteria andRemarkGreaterThan(String value) {
            addCriterion("remark >", value, "remark");
            return (Criteria) this;
        }
        public Criteria andRemarkGreaterThanOrEqualTo(String value) {
            addCriterion("remark >=", value, "remark");
            return (Criteria) this;
        }
        public Criteria andRemarkLessThan(String value) {
            addCriterion("remark <", value, "remark");
            return (Criteria) this;
        }
        public Criteria andRemarkLessThanOrEqualTo(String value) {
            addCriterion("remark <=", value, "remark");
            return (Criteria) this;
        }
        public Criteria andRemarkLike(String value) {
            addCriterion("remark like", value, "remark");
            return (Criteria) this;
        }
        public Criteria andRemarkNotLike(String value) {
            addCriterion("remark not like", value, "remark");
            return (Criteria) this;
        }
        public Criteria andRemarkIn(List<String> values) {
            addCriterion("remark in", values, "remark");
            return (Criteria) this;
        }
        public Criteria andRemarkNotIn(List<String> values) {
            addCriterion("remark not in", values, "remark");
            return (Criteria) this;
        }
        public Criteria andRemarkBetween(String value1, String value2) {
            addCriterion("remark between", value1, value2, "remark");
            return (Criteria) this;
        }
        public Criteria andRemarkNotBetween(String value1, String value2) {
            addCriterion("remark not between", value1, value2, "remark");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {


        protected Criteria() {
            super();
        }
    }

    /**
     * 平台用户代理商品关系表
     * 
     * @author masiis
     * @version 1.0 2016-03-02
     */
    public static class Criterion {

        private String condition;
        private Object value;
        private Object secondValue;
        private boolean noValue;
        private boolean singleValue;
        private boolean betweenValue;
        private boolean listValue;
        private String typeHandler;

        public String getCondition() {
            return condition;
        }
        public Object getValue() {
            return value;
        }
        public Object getSecondValue() {
            return secondValue;
        }
        public boolean isNoValue() {
            return noValue;
        }
        public boolean isSingleValue() {
            return singleValue;
        }
        public boolean isBetweenValue() {
            return betweenValue;
        }
        public boolean isListValue() {
            return listValue;
        }
        public String getTypeHandler() {
            return typeHandler;
        }
        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }
        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }
        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }
        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }
        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}