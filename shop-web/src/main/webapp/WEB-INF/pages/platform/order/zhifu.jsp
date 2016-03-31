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
    <link rel="stylesheet" href="<%=path%>/static/css/zhifu.css">
    <link rel="stylesheet" href="<%=path%>/static/css/loading.css"/>
</head>
<body>
<div class="wrap">
    <div class="box">
        <header class="xq_header">
            <a href="javascript:;" onClick="javascript :history.go(-1);"><img src="<%=path%>/static/images/xq_rt.png"
                                                                              alt=""></a>
            <p>支付订单</p>
        </header>
        <main>
            <c:if test="${orderType==0}">
                <div class="xinxi">
                    <p>注册信息</p>
                    <p>支付订单</p>
                    <p>提交资料</p>
                </div>
            </c:if>
            <%--<div class="xinz" onclick="toChooseAddressPage()">
                <p><a>选择收货地址</a></p>
            </div>
            <section class="sec1">
                <img src="<%=path%>/static/images/zhifu_ad.png" alt="">
                <div onclick="toChooseAddressPage()">
                    <input style="display: none" type="text" id="addressId" value="${comUserAddress.id}"/>
                    <a href="#"><h2>收货人：<b>${comUserAddress.name}</b> <span>${comUserAddress.mobile}</span></h2></a>
                    <a href="#"><p>收货地址：
                        <span>${comUserAddress.provinceName}  ${comUserAddress.cityName}  ${comUserAddress.regionName} ${comUserAddress.address}</span><img
                                src="<%=path%>/static/images/next.png" alt=""></p></a>
                </div>
            </section>--%>
            <div class="paidan">
            <h1><img src="<%=path%>/static/images/loading.png" alt=""><b>在您前面还有<span>1233</span>人排单</b></h1>
            <p>奥斯卡了的骄傲是离开的骄傲了啥空间的卢卡斯惊呆了卡三季度来看阿斯利康三</p>
        </div>
            <%--${productInfo}--%>
            <section class="sec2">
                <p class="photo">
                    <img src="<%=path%>/static/images/shenqing_1.png.png" alt="">
                </p>
                <div>
                    <h2>抗引力——快速瘦脸精华<b style="float:right; margin-right:10px;font-size:12px;">x1</b></h2>
                    <h3>规格：<span>默认</span></h3>
                    <p>商品总价:<span>123123</span>保证金:<span>123123</span></p>
                </div>
            </section>
            <section class="sec3">
                <%--<p>运费<span>到付</span></p>
                <h1>共<b>${quantity}</b>件商品　运费：<span>到付</span><b>　合计：</b><span
                        style="padding-right: 10px;">￥${orderAmount}</span></h1>--%>
                <p>留言：<input type="text" id="userMessage" name="userMessage"></p>
            </section>
            <div>
                <p><img src="<%=path%>/static/images/lirun.png" alt=""></p>
                <h1>
                    <span>啊上来肯德基</span>
                    <span>啊上来肯德基</span>
                </h1>
            </div>
            <section class="sec4">
                <p><b>功夫v：</b><span>￥${orderAmount}</span></p>
                <p>（含保证金12000）</p>
                <%--<p><b>需付：</b><span>￥${receivableAmount}</span></p>--%>
            </section>
            <a href="javascript:;" onclick="submit()" class="weixin">微信支付</a>
            <%--<a href="javascript:;" class="xianxia">线下支付</a>--%>
        </main>
    </div>
</div>
</body>
<script src="<%=path%>/static/js/jquery-1.8.3.min.js"></script>
<script src="<%=path%>/static/js/commonAjax.js"/>
<script src="<%=path%>/static/js/iscroll.js"></script>
<script>
    $(document).ready(function () {
        var addressId = $("#addressId").val();
        if (addressId == "") {
            $(".xinz").show();
            $(".sec1").hide();
        } else {
            $(".xinz").hide();
            $(".sec1").attr("style", "display:-webkit-box;");
        }
    })
    var myScroll = new IScroll(".wrap", {
        preventDefault: false
    })

    function toChooseAddressPage() {
        var selectedAddressId = $("#addressId").val();
        window.location.href = "<%=path%>/userAddress/toChooseAddressPage.html?pageType=zhifu&selectedAddressId=" + selectedAddressId + "&orderId=${bOrderId}";
    }

    function submit() {
        if ($("#addressId").val() == null || $("#addressId").val() == "") {
            alert("请填写收获地址");
            return;
        }
        var paraData = {};
        paraData.bOrderId = "${bOrderId}";
        paraData.userMessage = $("#userMessage").val();
        paraData.userAddressId = $("#addressId").val();
        $.ajax({
            url: "<%=basePath%>border/payBOrderSubmit.do",
            type: "post",
            data: paraData,
            dataType: "json",
            success: function (data) {
                if (data.isError == false) {
                    window.location.href = "<%=basePath%>border/payBOrderReady.shtml?bOrderId=" + paraData.bOrderId;
                }
                else {
                    alert(data.message);
                }
            }
        });
    }
</script>
</html>