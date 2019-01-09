<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat"
    pageEncoding="UTF-8"%>
<%@include file="../../front/comm/include.inc.jsp"%>
<%
     String path = request.getContextPath();
         String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
         + path;
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <base href="${basePath}"/>
<!--     <link rel="stylesheet" href="/static/front/css/c2c/safe.css">
    <link rel="stylesheet" href="/static/front/css/c2c/module.common.css">
    <link rel="stylesheet" href="/static/front/css/c2c/module.base.css"> 

    <link href="//netdna.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="//at.alicdn.com/t/font_48446_inzlc0gmji3sor.css" rel="stylesheet">-->
    <link href="/static/front/app/js/c2c/common.css" rel="stylesheet">
    <link href="/static/front/app/js/c2c/otc-app.css" rel="stylesheet">
    <style>
        .set_hint{
            padding: .17rem;
            background-color: #FFFFCC;
        }
    </style>
</head>
<body>
<div class="header">
    <a href="javascript:history.go(-1);" class="prev"></a>
    <div class="head_title font15">收款账号管理</div>
    <!--<div class="more more-z">设置</div>-->
</div>
<div id="mat"></div>
<ul class="set_hint box-sizing">
    <li class="font13 mb_10">1、请设置您能支持的支付或收款方式，请务必保障是您本人所有的账号。</li>
    <li class="font13">2、您所设置的信息，将可自动呈现在您所发布的广告信息中。
</ul>
<ul class="news_list pl_10">
    <li class="pr_10">
        <a href="/m/otc/userReceiptgo.html?ftype=1&ying=1">
            <div class="flexLayout">
                <div class="list_left font16">
                    <img src="/static/front/images/c2c/Alipay.png" alt="" class="mr_10">
                    <span>支付宝设置</span>
                </div>
                <div class="list_right"><img src="/static/front/images/c2c/right.png" alt=""></div>
            </div>
        </a>
    </li>
    <li class="pr_10">
        <a href="/m/otc/userReceiptgo.html?ftype=2&ying=1">
            <div class="flexLayout">
                <div class="list_left font16">
                    <img src="/static/front/images/c2c/WeChat.png" alt="" class="mr_10">
                    <span>微信设置</span>
                </div>
                <div class="list_right"><img src="/static/front/images/c2c/right.png" alt=""></div>
            </div>
        </a>
    </li>
</ul>
<%-- <div class="bk-onekey financen ">
    <div class="container">
        <div class="finance-rd">
            <div class="bk-tabList">
                <div class="bk-c2c-nav bk-band clearfix">
                    <a href="javascript:void(0)" class="active main-nav">收款账号设置</a>
                    <a class="btn card-admin" role="button" href="/m/otc/index.html" style="color: #666" ><img alt="" src="/static/front/images/aui-icon-back.png"> 返回</a>
                </div>
                <div class="alert-info payment-settings-tips">
                    <ul>
                        <li>1、请设置您能支持的支付或收款方式，请务必保障是您本人所有的账号。</li>
                        <li>2、您所设置的信息，将可自动呈现在您所发布的广告信息中。<!-- <a target="_blank" href="/images/payment_example.jpg">点此查看示例</a> --></li>
                    </ul>
                </div>
                <div class="bk-pageTit" id="exchangeRecord">
                    <div class="table-responsive ">
                        <table id="billDetail" class="table table-striped table-bordered table-hover">
                            <thead>
                                <tr>
                                    <th>交易方式</th>
                                    <th>设置</th>
                                    <th>操作</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>
                                        <c:choose>
                                            <c:when test="${fr0.items.fType == 0 }"> 
                                                 <span>银行账号</span>
                                            </c:when>
                                            <c:when test="${fr0.items.fType == 1 }"> 
                                                 <span>支付宝</span>
                                            </c:when>
                                            <c:when test="${fr0.items.fType == 2 }"> 
                                                 <span>微信</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span>设置银行账号</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                            <c:if test="${bankStatus == '0' }">
                                                <span>已设置</span>
                                            </c:if> 
                                            <c:if test="${bankStatus == '1' }">
                                                 <span>未设置</span>
                                            </c:if>
                                    </td>
                                    <td class="tl">
                                        <a href="/financial/accountbank.html">管理</a>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <c:choose>
                                            <c:when test="${fr1.items.fType == 0 }"> 
                                                <span>银行账号</span>
                                            </c:when>
                                            <c:when test="${fr1.items.fType == 1 }"> 
                                                <span>支付宝</span>
                                            </c:when>
                                            <c:when test="${fr1.items.fType == 2 }"> 
                                                <span>微信</span>
                                            </c:when>
                                            <c:otherwise>
                                                 <span>设置支付宝</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${fr1.items.fType == 0 || fr1.items.fType == 1 || fr1.items.fType == 2 }"> 
                                                <span>已设置</span>
                                            </c:when>
                                            <c:otherwise>
                                                 <span>未设置</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="tl">
                                        <a href="/m/otc/userReceiptgo.html?ftype=1&ying=1">管理</a>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <c:choose>
                                            <c:when test="${fr2.items.fType == 0 }"> 
                                                 <span>银行账号</span>
                                            </c:when>
                                            <c:when test="${fr2.items.fType == 1 }"> 
                                                 <span>支付宝</span>
                                            </c:when>
                                            <c:when test="${fr2.items.fType == 2 }"> 
                                                 <span>微信</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span>设置微信</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${fr2.items.fType == 0 || fr2.items.fType == 1 || fr2.items.fType == 2 }"> 
                                                <span>已设置</span>
                                            </c:when>
                                            <c:otherwise>
                                                 <span>未设置</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="tl">
                                        <a href="/m/otc/userReceiptgo.html?ftype=2&ying=1">管理</a>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>  --%>
<script src="/static/front/js/c2c/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="/static/front/app/js/c2c/rem1.js"></script>
<script src="/static/front/js/c2c/jquery.SuperSlide.2.1.1.js"></script>
<script src="/static/front/js/comm/util.js"></script>
<script type="text/javascript">
$(function() {
	var msg="${msg}";
	if(""!=msg){
		layer.msg(msg);
	}
});
</script>
</body>
</html>