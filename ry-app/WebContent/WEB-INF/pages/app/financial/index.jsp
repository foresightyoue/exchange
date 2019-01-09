<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../../front/comm/include.inc.jsp" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no, width=device-width">
    <meta name="format-detection" content="telephone=no">
    <meta name="format-detection" content="email=no">
    <base href="${basePath}"/>
    <title>个人中心</title>
    <link rel="stylesheet" href="/static/front/app/css/css.css" />
    <link rel="stylesheet" href="/static/front/app/css/style.css" />
    <link rel="stylesheet" href="/static/front/app/css/swiper-3.4.2.min.css" />
    <script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js" ></script>
    <script type="text/javascript" src="/static/front/app/js/swiper-3.4.2.jquery.min.js" ></script>
    <style type="text/css">
    .Personal-info .active{
	    font-size: 0.93rem;
	    float: right;
	    display: inline-block;
	    /* margin-left: 0.62rem; */
	    color: white;
	    border: 1px solid #fff;
	    padding: 0.2rem 1.2rem;
	    border-radius: 0.93rem;
	    margin-right: .5rem;
	    margin-top: .8rem;
    }
    .user-money ul li{
        width: 100%;
    }
    .tradeNum{
        margin-left: 1rem;
	    font-size: 1.4rem;
	    vertical-align: middle;
    }
	.Personal-info {
		padding: 1.2rem 0 2rem 1.25rem;
	}
	#cont-padding .right_img{
		float: right;
		width: .5rem;
		margin-right: .5rem;
	}
	.user-list ul li img {
	    margin-top: 1.2rem;
	}
    </style>
</head>
<body style="padding-top: 7.1rem;">
<nav>
    <!-- <div class="Personal-title">个人中心
    </div> -->

<div class="Personal-top">
    <div class="Personal-info clearfix">
        <div class="pic"><a href="javascript:;"><img src="/static/front/app/images/img_pic.png"></a></div>
        <div class="user-info">
            <h2 class="clearfix"><strong>${login_user.fnickName}</strong><em><img src="/static/front/app/images/certification.png"></em></h2>
            <p>UID：${login_user.fid} </p>
        </div>
    </div>
    <%-- <div class="user-money clearfix">
        <ul>
           <%--  <li>
                <p>预估总资产 ($)<span class="tradeNum"><fmt:formatNumber
                        value="${totalCapitalTrade }" pattern="##.##"
                        maxIntegerDigits="20" maxFractionDigits="4" /></span>
                </p>
            </li> --%>
            <%-- <li>
                <h2><fmt:formatNumber
                        value="${fwallet.ftotal }" pattern="##.##"
                        maxIntegerDigits="20" maxFractionDigits="4" /></h2>
                <p>可用美元($)</p>
            </li>
        </ul>
    </div> --%>
</div>
</nav>

<section id="cont-padding">
    <div class="user-list clearfix img_left">
        <ul>
           <!--  <li>
               <a href="/m/financial/zichan.html"></a>
                <img src="/static/front/app/images/zichan.png">
                <p>账户资产</p>
                <img class="right_img" alt="" src="/static/front/app/images/right.png">
            </li> -->
         <!--    <li>
                <a href="/m/block/blockQuery.html"></a>
               <a href="/m/account/rechargeCny.html" class="active"></a>
                <img src="/static/front/app/images/set_chongzhi.png">
                <p>账户充值</p>
            </li> -->
            <li>
                <a href="${oss_url}/m/trade/trade_entrust1.html?status=0&type=1"></a>
                <img src="/static/front/app/images/set_entrust.png">
                <p>委托管理</p>
                <img class="right_img" alt="" src="/static/front/app/images/right.png">
            </li>
            <li>
               <!--  <a href="wodechengjiao.html"></a> -->
                <a href="${oss_url}/m/trade/trade_entrust1.html?status=1&type=1"></a>
                <img src="/static/front/app/images/set_deal.png">
                <p>我的成交</p>
                <img class="right_img" alt="" src="/static/front/app/images/right.png">
            </li>
            <li>
                <a href="${oss_url}/m/account/record.html?from=financial"></a>
                <img src="/static/front/app/images/set_demand.png">
                <p>财务日志</p>
                <img class="right_img" alt="" src="/static/front/app/images/right.png">
            </li>
            <li>
               <a href="${oss_url}/m/security.html"></a>
               <img src="/static/front/app/images/set_info.png">
               <p>安全设置</p>
               <img class="right_img" alt="" src="/static/front/app/images/right.png">
            </li>
            <li>
               <a href="${oss_url}/m/service/ourService.html?id=1"></a>
               <img src="/static/front/app/images/set_article.png">
               <p>用户公告</p>
               <img class="right_img" alt="" src="/static/front/app/images/right.png">
            </li>
             <li>
               <a href="${oss_url}/m/appversion/appversion.html"></a>
               <img src="/static/front/images/yitai2.png">
               <p>版本更新</p>
               <img class="right_img" alt="" src="/static/front/app/images/right.png">
            </li>
            <li>
               <a href="${oss_url}/m/introl/myposter.html"></a>
               <img src="/static/front/images/myposter.png">
               <p>我的海报</p>
               <img class="right_img" alt="" src="/static/front/app/images/right.png">
            </li>
             <li>
               <a href="${oss_url}/m/introl/mydivides.html"></a>
               <img src="/static/front/images/mydivide.png">
               <p>推广中心</p>
               <img class="right_img" alt="" src="/static/front/app/images/right.png">
            </li>
            
            <li>
               <a href="${oss_url}/m/financial/FrontContactController/contact.html"></a>
                <img src="/static/front/app/images/contact_us.png">
               <p>联系我们</p>
               <img class="right_img" alt="" src="/static/front/app/images/right.png">
            </li>
           <li>
               <a href="/m/otc/userReceiptOption.html"></a>
               <img src="/static/front/app/images/account_set.png">
               <p>收款账号设置</p>
               <img class="right_img" alt="" src="/static/front/app/images/right.png">
            </li> 
        </ul>
    </div>
</section>
<jsp:include page="../comm/menu.jsp"></jsp:include>
<script>
    $(function(){
        $(".munu ul li").on('click',function(){
            $(this).addClass('active').siblings().removeClass('active');
        });
    });
    function iphoneX(type){
    	if(type==iphoneX){
    		$(".munu").css("height","4.5rem");
    	}else{
    		$(".munu").css("height","3.06rem");
    	}
    }
</script>
</body>
</html>