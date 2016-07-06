<%@ page language="java" import="java.util.*" contentType="text/html; utf-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <title>麦链合伙人</title>
    <%@include file="/WEB-INF/pages/common/head.jsp" %>
    <link rel="stylesheet" href="${path}/static/css/material/Library.css">
</head>
<body>

<div class="wrap">
    <header class="xq_header">
        <a href="index.html"><img src="${path}/static/images/xq_rt.png" alt=""></a>
        <p>素材库</p>
    </header>
    <main>
        <p>
            <span><img src="${path}/static/images/material/library.png" alt=""></span>
            <span class="add"><b>+添加订阅</b> | 1234</span>
        </p>
    </main>
    <img src="${path}/static/images/material/FAB.png" alt="" onclick="clickShow()">
</div>
<div class="black">
    <div class="back_b"></div>
    <div class="b_t">
        <h1>亲爱的代理，</h1>
        <p>
            线下素材的图片像素比较大，请您留下您的邮箱地址，系统稍后会将线下素材的下载链接发到您的邮箱，请注意查收！
        </p>
        <input type="text" placeholder="请输入邮箱地址" id="email">
        <button onclick="saveEmail()">留下邮箱</button>
        <b class="off" onclick="clickHide()">×</b>
    </div>
</div>
<script src="${path}/static/js/jquery-1.8.3.min.js"></script>
<script src="${path}/static/js/definedAlertWindow.js"></script>
<script>
    var loginWidtn=$(".wrap").width()/2-1;

    $("main p").width(loginWidtn);

    $(".add").toggle(function(){
        $(this).addClass("on");
        $(this).find("b").html("取消订阅");
    },function(){
        $(this).removeClass("on");
        $(this).find("b").html("+添加订阅");
    })
    function clickShow(){
        $("#email").val("");
        $(".black").show();
    }
    function clickHide(){
        $(".black").hide();
    }

    function saveEmail() {
        var email = $("#email").val();
        var reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
        if (email == null || email == "") {
            alert("邮箱不能为空");
            return false;
        }
        if (!reg.test(email)) {
            alert("请输入正确的邮箱格式");
            return false;
        }
        $.ajax({
            url: '${basePath}materielApply/addEmail.do',
            type: 'post',
            data: {email: email},
            dataType: 'json',
            success: function (data) {
                if (data.isError == false) {
                    alert("邮箱上传成功，请注意查收邮件");
                    $(".black").hide();
                }
            }
        });
    }
</script>
</body>
</html>