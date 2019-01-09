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
    <title>委托管理</title>
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
        委托管理
        <p><a href="javascript:;">全部</a><img src="/static/front/app/images/drop-down.png"></p>
    </div>
</nav>
<section>
    <div class="record">
        <div class="record-info">
            <c:if test="${fn:length(fentrusts)==0 }">
                <span>暂无委托</span>
            </c:if>
            <c:forEach items="${fentrusts }" var="v" varStatus="vs">
                <div class="record-more">
                    <dl>
                        <dt>
                        <h2>(檀香币/CNY) 买入</h2>
                        <p>数量：<span>T1000</span></p>
                        </dt>
                        <dd>
                            <h2><span>总金额：</span>￥100.86</h2>
                            <p>单价：<span>￥3.069</span></p>
                        </dd>
                    </dl>
                </div>
                <div class="record-state">状态：<span>未成交</span><a href="javascript:;">取消</a><em><i></i><i></i></em></div>
            </c:forEach>
        </div>
    </div>
</section>
<article>
    <div class="classification">
        <div class="classification-tab clearfix">
            <ul>
                <li class="active">全部</li>
                <li>人民币</li>
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
        $(".record-state em").on('click',function(){
            window.location.href="weituoguanli_xiangxi.html?type=0&symbol=1&tradeType=0";
        });
    });
</script>
</html>