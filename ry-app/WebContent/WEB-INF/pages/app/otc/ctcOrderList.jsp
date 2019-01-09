<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat"
    pageEncoding="UTF-8"%>
<%@include file="../../front/comm/include.inc.jsp"%>
<%
     String path = request.getContextPath();
         String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
         + path;
%>
<!DOCTYPE html>
<html style="font-size: 16px;">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <base href="${basePath}"/>
     <title>我的广告</title>
    <!-- <link rel="stylesheet" href="/static/front/css/c2c/safe.css">
    <link rel="stylesheet" href="/static/front/css/c2c/module.common.css">
    <link rel="stylesheet" href="/static/front/css/c2c/module.base.css">
    <link rel="stylesheet" href="/static/front/css/otc/common_otc.css">
    <link rel="stylesheet" href="/static/front/css/otc/buy_list.css"> 
    <link href="//netdna.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="//at.alicdn.com/t/font_48446_inzlc0gmji3sor.css" rel="stylesheet">-->
    <link href="/static/front/app/js/c2c/common.css" rel="stylesheet">
    <link href="/static/front/app/js/c2c/otc-app.css" rel="stylesheet">
    <style>
    .operation_btn2 .table_btn{
        width: 18%;
        height: 0.41rem;
        border-radius: 0.205rem;
        border: solid 0.017rem #EBA51D;
        color: #EBA51D ;
        line-height: .41rem;
        text-align: center;
        float: right;
        /* margin-top: .17rem; */
    }
    .operation_btn2{
        width: 100%;
        height: 0.425rem;
    }
    .advertisement_list li {
        margin-bottom: .17rem;
        margin-top: 0;
    }
    </style>
    
</head>
<body style="background-color: #f5f5f5;">
<div class="header">
    <a href="/m/otc/index.html?coinType=${coin_id}" class="prev"></a>
    <div class="head_title font15">我的广告</div>
    <!--<div class="more more-z">设置</div>-->
</div>
<div id="mat"></div>
<!-- <ul class="my_advertisement flexLayout">
    <li class="font15 tc nav-active"><span>购买</span></li>
    <li class="font15 tc "><span>出售</span></li>
</ul> -->
<div class="main-content">
    <input type="hidden" name="coin_id" class="coin_id"  value="${coin_id}">
    <ul class="buy_advertisement advertisement_list" style="display:block;">
        <c:forEach var="item" items="${ordersList}">
            <li>
                <a href="javascript: void (0);">
                    <div class="flexLayout">
                        <div class="font16">昵称：${item.fUsr_id}</div>
                        <div class="examine font13">
                            <c:if test="${item.isCheck eq 0 }">未审核</c:if>
                            <c:if test="${item.isCheck eq 1 }">审核成功</c:if>
                            <c:if test="${item.isCheck eq 2 }">审核失败</c:if>
                        </div>
                    </div>
                    <div class="flexLayout ad_time">
                        <div class="font12 text-gray">创建时间：<fmt:formatDate value="${item.fCreateTime}" pattern="yyyy-MM-dd HH:mm"></fmt:formatDate></div>
                    </div>
                    <div class="flexLayout ad_time">
                        <div class="font12 text-gray">广告类型:
                            <span>
                                <c:if test="${item.fOrdertype eq 0 }">出售</c:if>
                                <c:if test="${item.fOrdertype eq 1 }">收购</c:if>
                             </span>
                        </div>
                    </div>
                    <div class="flexLayout ad_time">
                        <div class="font13">付款方式:
                            <span>
                                <c:if test="${item.fReceipttype eq 0 }">银行转账</c:if>
                                <c:if test="${item.fReceipttype eq 1 }">支付宝</c:if>
                                <c:if test="${item.fReceipttype eq 2 }">微信</c:if>
                            </span>
                        </div>
                        <div class="font12">币种：${item.fShortName }</div>
                    </div>
                    <!-- <div class="ad_text font12 text-gray mb_10">广告语：诚信交易，童叟无欺！！！诚信交易，童叟无欺！！！诚信交易，童叟无欺！！！</div> -->
                    <div class="flexLayout">
                        <div>
                            <div class="tl font13 ad_time" style="margin-top: 0">最低价：￥ ${item.fUnitprice }</div>
                            <div class="tl font13 ad_time">限额：${item.fSmallprice }-${item.fBigprice }</div>
                        </div>
                    </div>
                    <div class="operation_btn2 font13">
                        <c:if test="${item.fStatus eq 0}">
                            <a class="table_btn" onclick="undoAd('${item.fId }')">撤销</a>
                            <a class="table_btn mr_10" href="/m/otc/editPublishAd.html?fid_=${item.fId }&types_=${item.fShortName }">修改</a>
                        </c:if>
                        <c:if test="${item.fStatus eq 1}">
                            <a class="table_btn" onclick="undoAd('${item.fId }')">撤销</a>
                            <a class="table_btn mr_10" href="/m/otc/editPublishAd.html?fid_=${item.fId }&types_=${item.fShortName }">修改</a>
                        </c:if>
                    </div>
                </a>
            </li>
        </c:forEach>
    </ul>
</div>
<%-- <div class="bk-onekey financen">
    <div class="container">
        <div class="bk-c2c-nav bk-band clearfix">
           <a href="javascript:void(0)" class="active main-nav">我的广告</a>
           <a class="btn card-admin" role="button" href="/m/otc/index.html" style="color: #666" ><img alt="" src="/static/front/images/aui-icon-back.png"> 返回</a>
        </div>
        <div class="bk-pageTit" id="exchangeRecord">
           <div class="clearfix"></div>
           <div class="table-responsive">
               <div id="billDetail" class="table table-striped table-bordered table-hover">
                   <c:forEach var="item" items="${ordersList}">
                       <div>
                          <ul>
                             <li>广告类型:
                               <span>
                                   <c:if test="${item.fOrdertype eq 0 }">出售</c:if>
                                   <c:if test="${item.fOrdertype eq 1 }">收购</c:if>
                                </span> &nbsp;&nbsp;&nbsp;&nbsp;
                                <span>
                                   <c:if test="${item.fStatus eq 0 }">挂单中</c:if>
                                   <c:if test="${item.fStatus eq 1 }">售卖中<a href="/m/otc/payOrder.html?orderId=${item.fOrderId }&type=w3c&eq=w3c"></a></c:if>
                                   <c:if test="${item.fStatus eq 2 }">已售卖</c:if>
                                   <c:if test="${item.fStatus eq 3 }">已撤销</c:if>
                                </span>
                              </li>
                              <li>
                                                                                创建时间:<fmt:formatDate value="${item.fCreateTime}" pattern="yyyy-MM-dd HH:mm"></fmt:formatDate>
                              </li>
                              <li>币种:<span>${item.fShortName }</span>&nbsp;&nbsp;&nbsp;&nbsp;
                                                                                                   付款方式:<span>
                                                <c:if test="${item.fReceipttype eq 0 }">银行转账</c:if>
                                                <c:if test="${item.fReceipttype eq 1 }">支付宝</c:if>
                                                <c:if test="${item.fReceipttype eq 2 }">微信</c:if>
                                            </span>
                              </li>
                              <li>最低价:￥<span>${item.fUnitprice }</span>&nbsp;&nbsp;&nbsp;&nbsp;审核状态:
                                         <span>
                                            <c:if test="${item.isCheck eq 0 }">未审核</c:if>
                                            <c:if test="${item.isCheck eq 1 }">审核成功</c:if>
                                            <c:if test="${item.isCheck eq 2 }">审核失败</c:if>
                                         </span>
                              </li>
                              <li>最小限额:<span>${item.fSmallprice }</span> &nbsp;&nbsp;&nbsp;&nbsp;最大限额<span>${item.fBigprice }</span></li>
                              <li>
                                 <c:if test="${item.fStatus eq 0}">
                                        <a class="table_btn" onclick="undoAd('${item.fId }')">撤销</a>
                                        <a class="table_btn" href="/m/otc/editPublishAd.html?fid_=${item.fId }&types_=${item.fShortName }">修改</a>
                                 </c:if>
                                 <c:if test="${item.fStatus eq 1}">
                                       <a class="table_btn" onclick="undoAd('${item.fId }')">撤销</a>
                                       <a class="table_btn" href="/m/otc/editPublishAd.html?fid_=${item.fId }&types_=${item.fShortName }">修改</a>
                                 </c:if>
                              </li>
                          </ul>
                       </div>
                   </c:forEach>
               </div>
           </div>
        </div>
    </div>
</div> --%>
<script src="/static/front/js/c2c/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="/static/front/app/js/c2c/rem1.js"></script>
<script src="/static/front/js/c2c/jquery.SuperSlide.2.1.1.js"></script>
<script src="/static/front/js/comm/util.js"></script>
<script type="text/javascript" src="/static/front/js/layer/layer.js"></script>
<script src="/static/front/js/otc/publishady.js"></script>
<script>
    jQuery("#usdtcnyselllist").slide({
        mainCell: ".bd ul",
        autoPage: true,
        effect: "topLoop",
        autoPlay: true,
        vis: 2,
        delayTime: 1000,
        interTime: 100000
    });
</script>
</body>