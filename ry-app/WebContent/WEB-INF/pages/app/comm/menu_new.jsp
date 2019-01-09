<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../../front/comm/include.inc.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>
<style>
	.munu {
		display: flex;
		background-color: #FFFFFF;
	}

	.munu>div {
		flex-grow: 1;
		text-align: center;
		background-position: center 0.06rem;
		background-size: 0.461rem;
		background-repeat: no-repeat;
		font-size: 0.7rem;
	}

	.munu>div>a {
		font-size: 0.2rem;
		color: #999999;
		text-decoration: none;
	}

	.munu div:nth-child(1) {
		background-image: url(/static/front/app/images/menu/icon_hangqing.png);
	}

	.munu div:nth-child(2) {
		background-image: url(/static/front/app/images/menu/icon_OTC.png);
	}

	.munu div:nth-child(3) {
		background-image: url(/static/front/app/images/menu/icon_caiwu.png);
	}

	.munu div:nth-child(4) {
		background-image: url(/static/front/app/images/menu/icon_geren.png);
	}

	.munu div.active:nth-child(1) {
		background-image: url(/static/front/app/images/menu/icon_hangqing_active.png);
	}

	.munu div.active:nth-child(2) {
		background-image: url(/static/front/app/images/menu/icon_OTC_active.png);
	}

	.munu div.active:nth-child(3) {
		background-image: url(/static/front/app/images/menu/icon_caiwu_active.png);
	}

	.munu div.active:nth-child(4) {
		background-image: url(/static/front/app/images/menu/icon_geren_active.png);
	}
</style>
<div class="munu">
	<div <c:if test='${"main"==menuFlag}'>class="active"</c:if> >
		<a href="${oss_url}/m/index.html?menuFlag=main">行情</a>
	</div>
	<div <c:if test='${"otc" == menuFlag}'>class="active"</c:if> >
		<a href="${oss_url}/m/otc/otcMenu.html?menuFlag=otc">OTC</a>
	</div>
	<div <c:if test='${"assets"==menuFlag}'>class="active"</c:if> >
		<a href="${oss_url}/m/personal/assets.html?menuFlag=assets">个人资产</a>
	</div>
	<div <c:if test='${"account"==menuFlag}'>class="active"</c:if> >
		<a href="${oss_url}/m/financial/index.html?menuFlag=account">个人</a>
	</div>
</div>
<script>
	var menuFlag = ${empty menuFlag};
	
	if (menuFlag) {
		$(".munu div:first-child").addClass("active");
	}
	$(".munu div").on('click',function(){
		$(this).addClass('active').siblings().removeClass('active');
	});
</script>
