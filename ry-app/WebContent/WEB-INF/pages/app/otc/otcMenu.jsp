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
    <title>订单中心</title>
	<link rel="stylesheet" type="text/css" href="/static/front/app/css/new_common.css"/>
    <script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js" ></script>
	<script type="text/javascript" src="/static/front/app/js/rem.js" ></script>
    <style type="text/css">
.row{
	line-height: 3rem;
	display: flex;
	font-size: 1.13rem;
	align-items: center;
	background-color: #FFFFFF;
	border-bottom: 1px solid #efefef;
}
.row>.text{
	flex: 1 1 auto;
	margin-left: 0.9rem;
}
.row>.right{
	margin: 0.97rem 1rem;
	width: 0.6rem;
	height: 1.07rem;
	font-size: 1.13rem;
}
.row>.tip{
	color: #999999;
}
.btn{
	width:4rem;
	line-height:2rem;
	border-radius: 0.27rem;
	text-align: center;
	color: #FFFFFF;
}
.gray{
	color: #cac8c8;
}
    </style>
</head>
<body>
	<div class="content">
	    <div class="head">
	        <div class="text_center">交易币种</div>
	    </div>
	    <div class="main">
			<c:forEach var ="menList" items="${menuList}">
				<c:if test="${userEnable == true}">
					<c:if test="${menList.fisActive== true}">
						<a href="/m/otc/index.html?coinType=${menList.coinType}" class="row">
							<div class="text">${menList.fShortName}</div>
							<img class="right" src="/static/front/app/images/main/geren/right.png" alt=""/>
						</a>
					</c:if>
					<c:if test="${menList.fisActive== false}">
						<div  class="row">
							<div class="text">${menList.fShortName}</div>
							<span class="gray " style="margin-right: 5px;font-size: 14px;">未开放</span>
							<img class="right" src="/static/front/app/images/main/geren/right.png" alt=""/>
						</div>
					</c:if>
				</c:if>
				<c:if test="${userEnable == false}">
					<div  class="row">
						<div class="text">${menList.fShortName}</div>
						<span class="gray " style="margin-right: 5px;font-size: 14px;">未开放</span>
						<img class="right" src="/static/front/app/images/main/geren/right.png" alt=""/>
					</div>
				</c:if>


			</c:forEach>
	        <div class="obligate"></div>
	    </div>
	    <div class="menu">
	        <jsp:include page="../comm/bottom_menu.jsp"></jsp:include>
	    </div>
	</div>
</body>
</html>