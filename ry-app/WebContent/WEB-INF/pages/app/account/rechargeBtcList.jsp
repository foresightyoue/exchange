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
    <title>充值记录</title>
    <link rel="stylesheet" href="/static/front/app/css/css.css" />
    <link rel="stylesheet" href="/static/front/app/css/style.css" />
    <link rel="stylesheet" href="/static/front/app/css/swiper-3.4.2.min.css" />
    <script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js" ></script>
    <script type="text/javascript" src="/static/front/app/js/swiper-3.4.2.jquery.min.js" ></script>
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
        充值记录
    </div>
</nav>
<section>
    <c:if test="${not empty fvirtualcaptualoperations }">
    <c:forEach items="${fvirtualcaptualoperations }" var="v" varStatus="vs">
        <div class="record">
            <div class="record-info">
                <div class="record-more">
                    <dl>
                        <dt>
                        <h2>${v.fvirtualcointype.fname}</h2>
                        <p>时间：<span>${v.flastUpdateTime }</span></p>
                        </dt>
                        <dd>
                            <h2><fmt:formatNumber value="${v.famount }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="4"/>个</h2>
                            <p>${v.fconfirmations }次确认</p>
                        </dd>
                    </dl>
                </div>
                <div class="record-state">状态：<span>${v.fstatus_s }</span></div>
            </div>
        </div>
    </c:forEach>
    </c:if>
    <c:if test="${empty fvirtualcaptualoperations }">
        <div style="background-color: #f9f9f9;position: fixed ;margin:auto;text-align:center;width: 100%;height: 100%;">
				<div style="top: 30%;position: relative;">
					<div><img src="/static/front/app/images/not fount.png" /></div>
					<div><span><font size="6" style="color: lightblue; font-family: 黑体;font-weight:bold;">暂无充值记录!!!</font></span></div>
				</div>
		</div>
    </c:if>
</section>
<article>
    <div class="classification">
        <div class="classification-tab clearfix">
            <ul>
                <li class="active">檀香币</li>
                <li>努比亚</li>
                <li>贝蓝币</li>
                <li>檀香币</li>
                <li>微云币</li>
                <li>优优币</li>
            </ul>
        </div>
    </div>
</article>
</body>
<script type="text/javascript" src="/static/front/js/comm/comm.js"></script>
</html>
<script>
    $(function(){
        $(".Personal-title p").on('click',function(){
            $(".classification").toggle();
        });
        $(".classification-tab ul li").on('click',function(){
            $(this).addClass('active').siblings().removeClass('active');
            $(".classification").hide();
            $(".Personal-title p a").html($(this).html());
        });
    });
</script>