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
    <title>关于我们</title>
    <link rel="stylesheet" href="/static/front/app/css/css.css" />
    <link rel="stylesheet" href="/static/front/app/css/style.css" />
    <link rel="stylesheet" href="/static/front/app/css/swiper-3.4.2.min.css" />
    <script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js" ></script>
    <script type="text/javascript" src="/static/front/app/js/swiper-3.4.2.jquery.min.js" ></script>
    <style type="text/css">
		.exit_btn ul{
			height: 3.06rem;
			line-height: 3.06rem;
			font-size: 0.87rem;
			border-radius: 0.15rem;
			text-align: center;
			margin: 4rem 0.62rem;
			background: #186ed5;
			color: #fff;
		}
		.exit_btn{
			padding-left: 0;
			background-color: transparent;
			border: 0;
		}
    </style>
</head>
<body>
<nav>
    <div class="Personal-title">
				<span>
					<a href="javascript:;" onClick="javascript :history.back(-1)">
						<em>
							<i></i>
							<i></i>
						</em>
						<strong>返回</strong>
					</a>
				</span>
        关于我们
    </div>
</nav>
<section> 
               <p>关乎我们</p>
   
</section>
<jsp:include page="../comm/menu.jsp"></jsp:include>
</body>
<script type="text/javascript" src="/static/front/js/comm/comm.js"></script>
</html>