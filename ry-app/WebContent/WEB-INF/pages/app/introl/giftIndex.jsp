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
    <title>领取大礼包</title>
    <link rel="stylesheet" href="/static/front/app/css/css.css" />
    <link rel="stylesheet" href="/static/front/app/css/style.css" />
    <script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js" ></script>
    <style type="text/css">
    html,body{
        width: 100%;
        height: 100%;
        background: transparent;
    }
    body {
        padding-top: 0rem;
    }
    .introl-section{
        width: 100%;
        height: 100%;
        background: url("/static/front/app/images/gift.png") no-repeat scroll center;
        background-size: 100% 100%;
    }
    .introl-section a{
        position: absolute;
        bottom: 3rem;
        left:50%;
        transform: translateX(-50%);
        -webkit-transform: translateX(-50%);
        display: inline-block;
        width: 80%;
        height: 3.13rem;
        line-height: 3.13rem;
        text-align: center;
        color: #fff;
        font-size: 1.2rem;
        background: #ffaf0a;
        border-radius: 1.56rem;
        -webkit-border-radius: 1.56rem;
    }
    </style>
</head>
<body>
<section class="introl-section">
    <a href="${url}">免费领取糖果大礼包</a>
</section>
</body>
<script type="text/javascript" src="/static/front/js/comm/util.js"></script>
<script type="text/javascript" src="/static/front/js/comm/comm.js"></script>

</html>