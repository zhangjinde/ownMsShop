<%@ page language="java" import="java.util.*" contentType="text/html; utf-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
    <link rel="stylesheet" href="<%=path%>/static/css/pageCss/reset.css">
    <link rel="stylesheet" href="<%=path%>/static/plugins/swipwr/swiper.3.1.7.min.css">
    <link rel="stylesheet" href="<%=path%>/static/css/pageCss/base.css">
    <link rel="stylesheet" href="<%=path%>/static/css/pageCss/newshouye.css">
</head>
<body>
<div class="wrap">
    <c:if test="${userPid!=user.id && userPid != sfShop.userId && userPid !=null && userPid !=0}">
        <div class="na">
            <p style="background: url('${pUser.wxHeadImg}') no-repeat;background-size: 100% 100%"></p>
            <h1>
                <span>我是${pUser.wxNkName}</span><br>我为好友代言，跟我一起分享赚佣金！
            </h1>
        </div>
    </c:if>
    <div class="banner">
        <div class="swiper-container">
            <div class="swiper-wrapper">
                <%--<div class="swiper-slide"><img src="<%=path%>/static/images/br1.png" alt=""></div>--%>
                <%--<c:forEach items="${SfShopDetails}" var="SfShopDetails" begin="0" end="4">--%>
                <%--<div class="swiper-slide"><img src="${SfShopDetails.skuUrl}" alt=""></div>--%>
                <%--</c:forEach>--%>
                <%--<div class="swiper-slide"><img src="<%=path%>/static/images/br1.png" alt=""></div>--%>
            </div>
            <!-- 如果需要分页器 -->
            <div class="swiper-pagination"></div>
            <div class="banner_b"></div>
        </div>
        <div class="banner_1">
            <div class="br_1">
                <h1>${sfShop.name}</h1>
                <p>${sfShop.explanation}</p>
                <p id="icon">
                    <%--<img src="<%=path%>/static/images/1.png" alt="">--%>
                    <%--<img src="<%=path%>/static/images/3.png" alt="">--%>
                    <%--<c:forEach items="${SfShopDetails}" var="SfShopDetails">--%>
                    <%--<img src="${SfShopDetails.icon}" alt="">--%>
                    <%--</c:forEach>--%>
                </p>
            </div>
            <div class="br_2">
                <h1><b>已有</b><img src="<%=path%>/static/images/zuo.png" alt=""><span>${countByShopId}</span><img src="<%=path%>/static/images/you.png" alt=""><b>人</b></h1>
                <p>成为TA店铺粉丝</p>
            </div>
            <div class="br_3">
                <div onclick="javascript:window.location.replace('<%=basePath%>shop/sharePlan?shopId=${sfShop.id}');"><img src="<%=path%>/static/images/woyao.png" alt=""></div>
                <p>&nbsp;&nbsp;</p>
                <div onclick="javascript:window.location.replace('http://mp.weixin.qq.com/s?__biz=MzI1OTIxNzgwNA==&mid=2247483656&idx=1&sn=555876e87000a8b289d535fb12ce4333&scene=0#wechat_redirect');"><img src="<%=path%>/static/images/daiyan.png" alt=""></div>
            </div>
        </div>
    </div>
    <main id="main">
        <%--<c:forEach items="${SfShopDetails}" var="sd">--%>
        <%--<div class="sec1" onclick="javascript:window.location.replace('<%=basePath%>shop/detail.shtml/?skuId=${sd.skuId}&shopId=${sfShop.id}');">--%>
        <%--<div><img src="${sd.skuImageUrl}" alt=""></div>--%>
        <%--<div>--%>
        <%--<h1>${sd.skuAssia}</h1>--%>
        <%--<p>-${sd.slogan}-</p>--%>
        <%--<h1>￥${sd.priceRetail}</h1>--%>
        <%--<button>立即购买</button>--%>
        <%--</div>--%>
        <%--</div>--%>
        <%--</c:forEach>--%>
    </main>
</div>
<footer>
    <div>
        <p onclick="javascript:window.location.replace('<%=basePath%>${shopId}/${userPid}/shop.shtml');">
            <span><img src="<%=path%>/static/images/dibu11.png" alt=""></span>
            <span class="active">首页</span>
        </p>
        <p onclick="javascript:window.location.replace('<%=basePath%>shop/sharePlan?shopId=${sfShop.id}');">
            <span><img src="<%=path%>/static/images/dibu2.png" alt=""></span>
            <span>二维码</span>
        </p>
        <p onclick="javascript:window.location.replace('<%=path%>/sfOrderManagerController/toBorderManagement?fm=1');">
            <span><img src="<%=path%>/static/images/dibu3.png" alt=""></span>
            <span>个人中心</span>
        </p>
    </div>
</footer>
<script src="<%=path%>/static/plugins/swipwr/swiper.3.1.7.min.js"></script>
<script src="<%=path%>/static/js/plugins/jquery/jquery-1.8.3.min.js"></script>
<script>
    $(function () {

        $.ajax({
            type:"POST",
            url : "<%=path%>/product.do",
            dataType:"Json",
            success:function(data){
                var trHtml = "";
                $.each(data, function(i, SfShopDetails) {
                    trHtml+="<div class=\"swiper-slide\"><img src=\""+SfShopDetails.skuUrl+"\" alt=\"\"></div>";
                })
                $(".swiper-wrapper").html(trHtml);
                var trHtml1 = "";
                trHtml1+="<p id=\"icon\">";
                trHtml1+="<img src=\"<%=path%>/static/images/1.png\" alt=\"\">";
                trHtml1+="<img src=\"<%=path%>/static/images/3.png\" alt=\"\">";
                $.each(data, function(i, SfShopDetails) {
                    trHtml1+="<img src=\""+SfShopDetails.icon+"\" alt=\"\">";
                })
                trHtml1+="</p>";
                $("#icon").html(trHtml1);
                var trHtml2 = "";
                var shopId= ${sfShop.id};
                $.each(data, function(i, SfShopDetails) {
                    trHtml2+="<div class=\"sec1\" onclick=\"javascript:window.location.replace('<%=basePath%>shop/detail.shtml/?skuId="+SfShopDetails.skuId+"&shopId="+shopId+"');\">";
                    trHtml2+="<div><img src=\""+SfShopDetails.skuImageUrl+"\" alt=\"\"></div>";
                    trHtml2+="<div><h1>"+SfShopDetails.skuAssia+"</h1> <p>-"+SfShopDetails.slogan+"-</p> <h1>￥"+SfShopDetails.priceRetail+"</h1>";
                    trHtml2+="<button>立即购买</button> </div> </div>";
                })
                $("#main").html(trHtml2);
                var bWidth=$(".swiper-slide").width(),
                        bHeight=$(".swiper-slide").height();
                $(".banner_b").width(bWidth).height(bHeight);
                var mySwiper = new Swiper ('.swiper-container', {
                    direction: 'horizontal',
                    loop: true,
                    autoplay: 3000,
                    // 如果需要分页器
                    pagination: '.swiper-pagination'
                })
            }
        })
    })

</script>
</body>
</html>