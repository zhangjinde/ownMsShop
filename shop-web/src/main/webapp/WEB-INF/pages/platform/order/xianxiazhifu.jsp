<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" import="java.util.*" contentType="text/html; utf-8" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <title>订单详情</title>
    <%@ include file="/WEB-INF/pages/common/head.jsp" %>
    <link rel="stylesheet" href="${path}/static/css/xianxiazhifu.css">
</head>
<body>
<div class="wrap">
    <header class="xq_header">
<%--        <a href="index.html"><img src="${path}/static/images/xq_rt.png" alt=""></a>--%>
        <p>支付订单</p>
    </header>
    <div class="na">
        <p></p>
        <h1>
            <span>麦链合伙人</span>
            <span>关注可查资金，管理店铺，发展下级</span>
        </h1>
        <label class="add">加关注</label>
    </div>
    <div class="xinxi">
        <p>注册信息</p>
        <p style="color:#F74A11;">选择拿货方式</p>
        <p style="color:#F74A11;">支付订单</p>
    </div>
    <div class="sec1">
        <div>
            <h1>￥${border.productAmount}元</h1>
            <p>您需要在${latestTime}前将￥${border.productAmount}转到麦链合伙人对公账户。</p>
        </div>
        <p>*请在汇款单的附言处注明“${orderItem.skuName}合伙申请${border.productAmount}万元套餐+您的手机号”（<span>非常重要！</span>）</p>
        <h1><span></span>麦链对公账户信息</h1>
        <h2><span>开户行：</span><span>${supplierBank.bankName}</span></h2>
        <h2><span>开户名：</span><span>${supplierBank.accountName}</span></h2>
        <h2><span>卡号：</span><span>${supplierBank.cardNumber}</span></h2>
    </div>
    <h1>线下支付流程：</h1>
    <img src="${path}/static/images/zhifu.png" alt="">
</div>
<div class="back_box">
    <div class="back"></div>
    <div class="back_f">
        <p>关注公众账号查资金，管理店铺，发展下级</p>
        <span class="close">×</span>
        <img src="${path}/static/images/asd.JPG" alt="">
    </div>
</div>
<footer>
    <button onclick="returnMarket()">返回市场</button>
</footer>
<script src="${path}/static/js/jquery-1.8.3.min.js"></script>
<script>
    $(".add").on("click", function () {
        $(".back_box").show()
    })
    $(".close").on("click", function () {
        $(".back_box").hide()
    })
    function returnMarket(){
        window.location.href = "${path}/marketGood/market";
    }
</script>
</body>
</html>