<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat"
    pageEncoding="UTF-8"%>
<%@ include file="../comm/include.inc.jsp"%>
    <title>收款账号设置</title>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <base href="${basePath}"/>
    <%@include file="../comm/link.inc.jsp"%>
    <link rel="stylesheet" href="/static/front/css/c2c/safe.css">
    <link rel="stylesheet" href="/static/front/css/c2c/module.common.css">
    <link rel="stylesheet" href="/static/front/css/c2c/module.base.css">

    <link href="//netdna.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="//at.alicdn.com/t/font_48446_inzlc0gmji3sor.css" rel="stylesheet">
    <style type="text/css">
        .top-fixed-nav {
            position: fixed;
            left: 0;
            top: 0;
            z-index: 100;
        }
        .exchangetlist .status5 {
            width: 25%;
            display: inline-block;
            text-align: right;
            color: #333;
            float: right;
        }
        .layui-layer{
            top: 50% !important;
            /* margin-top: -120px !important;    */
        }
        input::-webkit-outer-spin-button, input::-webkit-inner-spin-button {
            -webkit-appearance: none !important;
            margin: 0;
        }
        input[type="number"]{ 
            -moz-appearance:textfield;
        }
        .alertContent{
            width: 40%;
            min-width: 420px;
            /* height: 265px; */
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%,-50%);
            background-color: white;
            /* z-index: 100; */
            border-radius:3px;
            -webkit-border-radius:3px;
            color: #333;
            padding: 10px  20px 20px;
            line-height: 30px;
            text-align: left;
        }
        .alertContent p span{
            display: inline-block;
            width: 150px;
            text-align: right;
            margin-right: 10px;
            background-color: rgba(255,152,0,.1);
            border-right: 1px solid #FF9800; 
            padding-right: 10px;
            color: #333;
        }
        .alertContent p .text-danger{
            border: 0;
            background-color: transparent;
            text-align: left;
        }
        .alertContent p{
            border: 1px solid #FF9800; 
            border-bottom: 0;
            margin-bottom: 0;
        }
        .message_hint{
            font-size: 13px;
            color: #808080;
            line-height: 30px;
        }
        .message_hint2{
            font-size: 13px;
            color: #808080;
            line-height: 30px;
            border-top: 1px solid #e6e6e6; 
        }
        .message_title{
            font-size: 18px;
            margin-bottom: 10px;
            line-height: 30px;
            height: 30px;
        }
        .error{
            position: absolute;
            width: 25px;
            top: 5px;
            right: 10px;
        }
        .kown{
            height: 28px;
            line-height: 25px;
            border: 0;
            background-color: #FF9800;
            color: white;
            padding: 0 10px;
            margin-top: 10px;
        }
        .text-danger{
            color: red !important;
        }
        .exchangetlist .clone{
            position: relative;
        }
        .exchangetlist .clone .typeshow{
            position: absolute;
            left: 59%;
        }
        .table>thead>tr>th, .table>tbody>tr>th, .table>tfoot>tr>th, .table>thead>tr>td, .table>tbody>tr>td, .table>tfoot>tr>td {
            font-size: 14px;
            
        }
        .table>thead>tr>th,.table>tbody>tr>td{
            text-align: left;
        }
        .bk-c2c-nav {
            background: #fff;
            border-bottom: 1px solid #e6e6e6;
        }
        .bk-c2c-nav a, .bk-c2c-nav span{
            height: 70px;
            line-height: 70px;
        }
        .alert-info {
            background-color: #d9edf7;
            border-color: #bce8f1;
            color: #31708f;
            padding: 15px 15px 10px 30px;
            font-size: 14px;
            line-height: 25px;
            border-radius: 5px;
            padding: 15px 15px 10px 30px;
            margin: 20px;
            text-align: left;
        }
        #exchangeRecord .tl{
            text-align: left;
        }
        .tl a{
            color: #1db3b4;
        }
    </style>
</head>
<%@include file="../comm/header.jsp"%> 
<body>
<div class="bk-onekey financen " style="padding-top: 50px;">
    <div class="container">
        <div class="finance-rd" style="width:100%; margin-left:0;">
            <div class="bk-tabList">
                <div class="bk-c2c-nav bk-band clearfix">
                    <a href="javascript:void(0)" class="active main-nav">收款账号设置</a>
                    <a class="btn card-admin" role="button" href="/otc/index.html?coinType=${coin_id}" style="color: #666" ><img alt="" src="/static/front/images/aui-icon-back.png"> 返回</a>
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
                                        <a href="/otc/userReceiptgo.html?coinType=${coin_id}&ftype=1&ying=1">管理</a>
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
                                        <a href="/otc/userReceiptgo.html?coinType=${coin_id}&ftype=2&ying=1">管理</a>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <input type="hidden" name="coin_id" id="coin_id" value="${coin_id}">
    </div>
</div> 
<script type="text/javascript">
$(function() {
	var msg="${msg}";
	if(""!=msg){
		layer.msg(msg);
	}
});
</script>
<script src="/static/front/js/c2c/jquery-1.11.3.min.js"></script>
<%@include file="../comm/footer.jsp"%>
<script src="/static/front/js/c2c/jquery.SuperSlide.2.1.1.js"></script>
<script src="/static/front/js/comm/util.js"></script>
</body>




<%-- 
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>收款账号设置</title>
</head>
<body>
    <table border="1px" cellspacing="0px">
        <tr>
            <th>交易方式</th>
            <th>设置</th>
            <th>操作</th>
        </tr>
        <tr>
            <td>
                <c:choose>
                    <c:when test="${fr0.ftype == 0 }"> 
                         银行账号
                    </c:when>
                    <c:when test="${fr0.ftype == 1 }"> 
                         支付宝
                    </c:when>
                    <c:when test="${fr0.ftype == 2 }"> 
                         微信
                    </c:when>
                    <c:otherwise>
                        设置银行账号
                    </c:otherwise>
                </c:choose>
            </td>
            <td>
                <c:choose>
                    <c:when test="${fr0.ftype == 0 || fr0.ftype == 1 || fr0.ftype == 2 }"> 
                        已设置
                    </c:when>
                    <c:otherwise>
                         未设置
                    </c:otherwise>
                </c:choose>
            </td>
            <td>
                <a href="/otc/userReceiptgo.html?ftype=${fr0.ftype}&fusr_id=${fr0.fusr_id}&ying=0">管理</a>
            </td>
        </tr>
        <tr>
            <td>
                <c:choose>
                    <c:when test="${fr1.ftype == 0 }"> 
                        银行账号
                    </c:when>
                    <c:when test="${fr1.ftype == 1 }"> 
                        支付宝
                    </c:when>
                    <c:when test="${fr1.ftype == 2 }"> 
                        微信
                    </c:when>
                    <c:otherwise>
                         设置支付宝
                    </c:otherwise>
                </c:choose>
            </td>
            <td>
                <c:choose>
                    <c:when test="${fr1.ftype == 0 || fr1.ftype == 1 || fr1.ftype == 2 }"> 
                        已设置
                    </c:when>
                    <c:otherwise>
                         未设置
                    </c:otherwise>
                </c:choose>
            </td>
            <td>
                <a href="/otc/userReceiptgo.html?ftype=${fr1.ftype}&fusr_id=${fr1.fusr_id}">管理</a>
            </td>
        </tr>
        <tr>
            <td>
                <c:choose>
                    <c:when test="${fr2.ftype == 0 }"> 
                         银行账号
                    </c:when>
                    <c:when test="${fr2.ftype == 1 }"> 
                         支付宝
                    </c:when>
                    <c:when test="${fr2.ftype == 2 }"> 
                         微信
                    </c:when>
                    <c:otherwise>
                        设置微信
                    </c:otherwise>
                </c:choose>
            </td>
            <td>
                <c:choose>
                    <c:when test="${fr2.ftype == 0 || fr2.ftype == 1 || fr2.ftype == 2 }"> 
                        已设置
                    </c:when>
                    <c:otherwise>
                         未设置
                    </c:otherwise>
                </c:choose>
            </td>
            <td>
                <a href="/otc/userReceiptgo.html?ftype=${fr2.ftype}&fusr_id=${fr2.fusr_id}">管理</a>
            </td>
        </tr>
    </table>
</body>
</html> --%>