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
        <title>帮助中心</title>
        <link rel="stylesheet" href="/static/front/app/css/css.css" />
        <link rel="stylesheet" href="/static/front/app/css/style.css" />
        <script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js" ></script>
        <style>
        	.contentBox{
        		background:#fff;
        		font-size:1rem;
        	}
        	.contentBox>h3{
        		font-size:1.3rem;
        		font-weight:600;
        		text-indent:1rem;
        		padding-top:1rem;
        	}
        	.contentBox>div{
        		padding:0 1rem;
        	}
        	.contentBox>div>h3{
        		color:black;
        		font-weight:600;
        		line-height:2rem;
        	}
        	.contentBox>div>p{
        		color:#666;
        		line-height:2rem;
        	}
        	.Personal-title span em{
            width: 1.2rem;
            height: 1.2rem;
            left: 0.4rem;
            top:0.65rem;
            }
            .Personal-title span em i:last-child{
                left:0.22rem;
            }
        </style>
    </head>
    <body>
        <nav>
            <div class="Personal-title">
                <span>
                    <a href="/m/financial/index.html?menuFlag=account">
                        <em>
                            <i></i>
                            <i></i>
                        </em>
                        <strong><!-- 返回 --></strong>
                    </a>
                </span>帮助中心
            </div>
        </nav>
        <section>
                <div class="affiche-cont" style="display:block;">
                    <ul>
                        <c:forEach items="${help}" var="info" varStatus="vs">
                            <li>
                                <p><a href="${oss_url}/m/help/info.html?id=${info.fid }" style="color:#000">${vs.index+1}.${info.ftitle }</a></p>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
        </section>
        <div class='contentBox'>
        	<h3>快速上手</h3>
        	<div>
        		<h3>1、注册账户</h3>
        		<p>使用手机或者邮箱注册瑞银账号，开启您的数字资产交易之旅。</p>
        	</div>
        	<div>
        		<h3>2、安全及认证</h3>
        		<p>设置资金安全密码，完成用户实名认证，确保账户资金安全。</p>
        	</div>
        	<div>
        		<h3>3、充值交易</h3>
        		<p>我没有数字资产，访问OTC使用CNY买入数字资产进行交易，在交易中获利。</p>
        	</div>
        </div>
    </body>
</html>