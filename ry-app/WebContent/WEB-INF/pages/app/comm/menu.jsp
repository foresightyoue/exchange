<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../../front/comm/include.inc.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>
<style>
.munu ul li{ 
	line-height: 1.8rem;
}
.munu ul li a{
	padding-top:1.35rem;
}

</style>
<div class="munu clearfix">
	<ul>
		<li <c:if test='${"main"==menuFlag}'>class="active"</c:if> ><a href="${oss_url}/m/index.html?menuFlag=main">行情</a></li>
		<li <c:if test='${"otc" == menuFlag}'>class="active"</c:if> ><a href="${oss_url}/m/otc/otcMenu.html?menuFlag=otc">OTC</a></li>
		<li <c:if test='${"assets"==menuFlag}'>class="active"</c:if> ><a href="${oss_url}/m/personal/assets.html?menuFlag=assets">个人资产</a></li>
		<li <c:if test='${"account"==menuFlag}'>class="active"</c:if> ><a href="${oss_url}/m/financial/index.html?menuFlag=account">个人</a></li>
	</ul>
</div>
<script>
	var menuFlag = ${empty menuFlag};
	if(menuFlag){
		$(".munu li:first-child").addClass("active");
	}
</script>