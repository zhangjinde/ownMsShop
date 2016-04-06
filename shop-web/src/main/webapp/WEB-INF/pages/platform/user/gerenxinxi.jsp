<%@ page language="java" import="java.util.*" contentType="text/html; utf-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <title>麦链商城</title>
    <link rel="stylesheet" href="<%=path%>/static/css/base.css">
    <link rel="stylesheet" href="<%=path%>/static/css/reset.css">
    <link rel="stylesheet" href="<%=path%>/static/css/header.css">
    <link rel="stylesheet" href="<%=path%>/static/css/gerenxinxi.css">
    <script type="text/javascript" src="<%=path%>/static/js/jquery/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="<%=path%>/static/js/definedAlertWindow.js"></script>
</head>
<body>
<div class="wrap">
    <header class="xq_header">
        <a href="#" onClick="javascript :history.go(-1);"><img src="<%=path%>/static/images/xq_rt.png" alt=""></a>
        <p>个人信息</p>
    </header>
    <main>
        <div class="head_img" style="background: url('<%=path%>/static/images/icon_27.png')">
            <p><img src="${comUser.wxHeadImg}" alt=""></p>
            <div><h1>${comUser.realName}</h1>
                <h1>
                    <span><img src="<%=path%>/static/images/admin.png" alt="">等级A1</span>
                    <span><img src="<%=path%>/static/images/admin.png" alt="">等级A1</span>
                </h1>
            </div>
        </div>
        <div class="sec1" style="margin-top:5px;background:white url('<%=path%>/static/images/people.png') no-repeat 10px;  background-size: 13px;">
            <p>手机号</p>
            <p>${comUser.mobile}</p>
        </div>
        <div class="sec1" id="weChatNumberId" style="background:white url('<%=path%>/static/images/weixin.png') no-repeat 7px;  background-size: 20px;">
            <p>微信号</p>
            <p><img src="<%=path%>/static/images/next.png" alt=""></p>
        </div>
        <div class="sec1" id = "identityAuthId" style="padding-left: 10px">
            <p>实名认证</p>
            <p><b>${auditStatusName}</b><img src="<%=path%>/static/images/next.png" alt="" style="    margin-top:4px"></p>
            <input id="auditStatusId" style="display: none" value="${comUser.auditStatus}" />
        </div>
        <div class="sec1" style="background:white url('<%=path%>/static/images/qianban.png') no-repeat 7px;  background-size: 20px;margin-bottom: 0;">
            <p>我的资金</p>
            <p><b>${auditStatusName}</b><img src="<%=path%>/static/images/next.png" alt="" style="    margin-top:4px"></p>
        </div>
        <div class="sec1" style="background:white url('<%=path%>/static/images/yinh.png') no-repeat 7px;  background-size: 20px;">
            <p>我的银行卡</p>
            <p><img src="<%=path%>/static/images/next.png" alt=""></p>
        </div>
        <div  id="addressManageId" class="sec1" style="padding-left: 10px">
            <p>地址管理</p>
            <p><img  src="<%=path%>/static/images/next.png" alt=""></p>
        </div>
    </main>
</div>
</body>
<script type="text/javascript" src="<%=path%>/static/js/personalInfo.js"></script>
</html>