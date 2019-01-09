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
    <title>实名认证</title>
    <link rel="stylesheet" href="/static/front/app/css/css.css" />
    <link rel="stylesheet" href="/static/front/app/css/style.css" />
    <link rel="stylesheet" href="/static/front/app/css/swiper-3.4.2.min.css" />
    <script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js" ></script>
    <script type="text/javascript" src="/static/front/app/js/swiper-3.4.2.jquery.min.js" ></script>
</head>
<body style="background: #fff;">
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
                </span>实名认证
    </div>
</nav>
<section>
    <div class="Personal-cont">
        <p><img src="/static/front/app/images/succeed.png"></p>
        <h2 class="clearfix">已通过实名认证</h2>
        <ul class="clearfix">
            <c:if test="${fuser.fpostRealValidate }">
                <li><span>姓名：</span>${fuser.frealName }</li>
                <li><span>身份证：</span>${fuser.fidentityNo_s }</li>
            </c:if>
            <c:if test="${fuser.authGrade =='2'||fuser.authGrade == '3'}">
                <li><span>二级身份证认证:</span>已经成功</li>
            </c:if>
            <c:if test="${fuser.authGrade != '2' && fuser.authGrade != '3'}">
                <li><span>三级银行卡认证:未进行认证,请点击此<a href="${oss_url}/m/realIdentity.html">进行验证</a></span></li>
            </c:if>
            <c:if test="${fuser.authGrade == '3' }">
                <li><span>三级银行卡认证:</span>已经成功</li>
            </c:if>
            <c:if test="${fuser.authGrade != '3' }">
                <li><span>三级银行卡认证:未进行认证<a href="${oss_url}/m/realIdentity.html?flag=trade">进行验证</a></span></li>
            </c:if>
        </ul>
    </div>
</section>
</body>
<script type="text/javascript" src="/static/front/js/comm/comm.js"></script>
</html>