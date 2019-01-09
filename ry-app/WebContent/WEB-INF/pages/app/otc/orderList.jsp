<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat"
    pageEncoding="UTF-8"%>
<%@include file="../../front/comm/include.inc.jsp"%>
<%
     String path = request.getContextPath();
         String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
         + path;
%>
<!DOCTYPE html>
<head>
    <meta charset="utf-8" />
    <meta name="viewport"
       content="initial-scale=1, maximum-scale=1, user-scalable=no, width=device-width">
    <meta name="format-detection" content="telephone=no">
    <meta name="format-detection" content="email=no">
    <base href="${basePath}"/>
    <title>我的买卖订单</title>
<!--     <link rel="stylesheet" href="/static/front/css/c2c/safe.css">
    <link rel="stylesheet" href="/static/front/css/c2c/module.common.css">
    <link rel="stylesheet" href="/static/front/css/c2c/module.base.css"> 
    <link href="//netdna.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="//at.alicdn.com/t/font_48446_inzlc0gmji3sor.css" rel="stylesheet">-->
    <link href="/static/front/app/js/c2c/common.css" rel="stylesheet">
    <link href="/static/front/app/js/c2c/otc-app.css" rel="stylesheet">
    <style>
    .operation_btn2 .table_btn{
        width: auto;
        padding: 0 .17rem;
        height: 0.41rem;
        border-radius: 0.205rem;
        border: solid 0.017rem #EBA51D ;
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
    .my_advertisement .nav-active a{
        border-bottom: .034rem solid #EBA51D ;
        color: #EBA51D ;
        display: inline-block;
        height: 95%;
        width: 30%;
    }
    </style>
</head>
<body style="background-color: #F5F5F5;">
<div class="header">
    <a href="/m/otc/orderMenu.html?coinType=${coin_id}" class="prev"></a>
    <div class="head_title font15">我的买卖订单</div>
    <!--<div class="more more-z">设置</div>-->
</div>
<div id="mat"></div>
<ul class="my_advertisement flexLayout">
    <li class="font15 tc nav-active"><a href="javascript: void(0);">进行中</a></li>
    <li class="font15 tc "><a href="javascript: void(0);">已完成</a></li>
</ul>
<div class="main-content">
    <!-- 未完成订单 -->
    <ul class="buy_advertisement advertisement_list" style="display:block;">
        <c:forEach var="otcOrder" items="${orderListIng }">
            <li style="margin-top: 0;">
                <a href="javascript: void (0);">
                    <div class="flexLayout">
                        <div class="font16">订单编号： ${otcOrder.fOrderId }</div>
                        <div class="examine font13">
                            <c:if test="${otcOrder.fTrade_Status == '1' }">待支付</c:if>
                            <c:if test="${otcOrder.fTrade_Status == '2' }">待确认</c:if>
                            <c:if test="${otcOrder.fType == '1' }">
                               <c:if test="${otcOrder.fTrade_Status == '5' }">申诉中</c:if>
                            </c:if>
                        </div>
                    </div>
                    <div class="flexLayout ad_time">
                        <div class="font12 text-gray">创建时间：<fmt:formatDate value="${otcOrder.fCreateTime}" pattern="yyyy-MM-dd HH:mm"/></div>
                    </div>
                    <div class="flexLayout ad_time">
                        <div class="font12 text-gray">交易对象：${otcOrder.floginName }</div>
                    </div>
                    <div class="flexLayout ad_time">
                        <div class="font12">币种：${otcOrder.fShortName }</div>
                        <div class="font13">交易类型：
                            <c:if test="${otcOrder.fType == '1' }">买入</c:if>
                            <c:if test="${otcOrder.fType == '2' }">卖出</c:if>
                        </div>
                    </div>
                    <div class="flexLayout specific_number font12 ad_time mt_10">
                        <div>单价</div>
                        <div class="tc">数量</div>
                        <div class="tr">金额</div>
                    </div>
                    <div class="flexLayout specific_number font12 pb_10 border_b">
                        <div class="text-gray">${otcOrder.fTrade_Price }</div>
                        <div class="tc text-gray">${otcOrder.fTrade_Count }</div>
                        <div class="tr text-gray">${otcOrder.fTrade_SumMoney }</div>
                    </div>
                    <div class="tr pt_10 operation_btn2">
                        <!-- <span class="operation_btn1 font13 mt_0">取消</span> -->
                        
	                     <c:if test="${otcOrder.fType == '1'}">
	                         <c:if test="${otcOrder.fTrade_Status == '1'}">
	                            <a href="/m/otc/payOrder.html?coinType=${coin_id}&orderId=${otcOrder.fOrderId }&fw=0" class="table_btn font13 mt_0">去支付</a>
	                            <a href="/m/otc/cancleOrder.html?coinType=${coin_id}&orderId=${otcOrder.fOrderId }&fw=0" class="table_btn font13 mt_0 mr_10">取消订单</a>
	                        </c:if>
                            <!-- 如果订单状态为2待验证，显示按钮查看订单详情 -->
                            <c:if test="${otcOrder.fTrade_Status == '2'}">
                                <a href="/m/otc/appeal.html?coinType=${coin_id}&orderId=${otcOrder.fOrderId }&fw=0" class="table_btn font13 mt_0">申诉</a>
                                <a href="/m/otc/payOrder.html?coinType=${coin_id}&orderId=${otcOrder.fOrderId }&fw=0" class="table_btn font13 mt_0 mr_10">查看详情</a>
                            </c:if>
                         </c:if>
                        <c:if test="${otcOrder.fType == '2' }">
                        <c:if test="${otcOrder.fTrade_Status == '2'}">
                            <%-- <a href="/otc/appeal.html?orderId=${otcOrder.fOrderId }&fw=0"style="padding: 5px 10px;background-color: #1db3b4;color: white;border-radius: 4px;margin-bottom: 5px;float: left">申诉</a> --%>
                        </c:if>
                            <a href="/m/otc/payOrder.html?coinType=${coin_id}&orderId=${otcOrder.fOrderId }&type=w3c&eq=w3c&fw=0" class="table_btn font13 mt_0 mr_10">查看支付情况</a>
                        </c:if>
                    </div>
                </a>
            </li>
        </c:forEach>
    </ul>
    
    <!-- 已完成订单 -->
    <ul class="buy_advertisement advertisement_list">
        <c:forEach var="otcOrder" items="${orderListOver }">
            <li style="margin-top: 0;">
                <a href="javascript: void (0);">
                    <div class="flexLayout">
                        <div class="font16">订单编号： ${otcOrder.fOrderId }</div>
                        <div class="examine font13">
                            <c:if test="${otcOrder.fTrade_Status eq 6 }">订单已完成</c:if>
                            <c:if test="${otcOrder.fTrade_Status eq 3 }">订单已完成</c:if>
                            <c:if test="${otcOrder.fTrade_Status eq 4 }">订单已取消</c:if>
                        </div>
                    </div>
                    <div class="flexLayout ad_time">
                        <div class="font12 text-gray">创建时间：<fmt:formatDate value="${otcOrder.fCreateTime}" pattern="yyyy-MM-dd HH:mm"/></div>
                    </div>
                    <div class="flexLayout ad_time">
                        <div class="font12 text-gray">交易对象：${otcOrder.floginName }</div>
                    </div>
                    <div class="flexLayout ad_time">
                        <div class="font12">币种：${otcOrder.fShortName }</div>
                        <div class="font13">交易类型：
                            <c:if test="${otcOrder.fType == '1' }">买入</c:if>
                            <c:if test="${otcOrder.fType == '2' }">卖出</c:if>
                        </div>
                    </div>
                    <div class="flexLayout specific_number font12 ad_time mt_10">
                        <div>单价</div>
                        <div class="tc">数量</div>
                        <div class="tr">金额</div>
                    </div>
                    <div class="flexLayout specific_number font12 pb_10">
                        <div class="text-gray">${otcOrder.fTrade_Price }</div>
                        <div class="tc text-gray">${otcOrder.fTrade_Count }</div>
                        <div class="tr text-gray">${otcOrder.fTrade_SumMoney }</div>
                    </div>
                    <%-- <div class="tr pt_10 operation_btn2">
                        <!-- <span class="operation_btn1 font13 mt_0">取消</span> -->
                        <c:if test="${otcOrder.fTrade_Status == '1'}">
                            <a href="/m/otc/payOrder.html?orderId=${otcOrder.fOrderId }&fw=0" class="table_btn font13 mt_0 mr_10">去支付</a>
                            <a href="/m/otc/cancleOrder.html?orderId=${otcOrder.fOrderId }&fw=0" class="table_btn font13 mt_0">取消订单</a>
                        </c:if>
                        <!-- 如果订单状态为2待验证，显示按钮查看订单详情 -->
                        <c:if test="${otcOrder.fTrade_Status == '2'}">
                            <a href="/m/otc/appeal.html?orderId=${otcOrder.fOrderId }&fw=0" class="table_btn font13 mt_0 mr_10">申诉</a>
                            <a href="/m/otc/payOrder.html?orderId=${otcOrder.fOrderId }&fw=0" class="table_btn font13 mt_0">查看详情</a>
                        </c:if>
                        <c:if test="${otcOrder.fType == '2' }">
                        <c:if test="${otcOrder.fTrade_Status == '2'}">
                            <a href="/otc/appeal.html?orderId=${otcOrder.fOrderId }&fw=0"style="padding: 5px 10px;background-color: #1db3b4;color: white;border-radius: 4px;margin-bottom: 5px;float: left">申诉</a>
                        </c:if>
                            <a href="/m/otc/payOrder.html?orderId=${otcOrder.fOrderId }&type=w3c&eq=w3c&fw=0" class="table_btn font13 mt_0">查看支付情况</a>
                        </c:if>
                    </div> --%>
                </a>
            </li>
        </c:forEach>
    </ul>
    <input type="hidden" name="coin_id" class="coin_id" value="${coin_id}"/>
</div>
   <%-- <div class="bk-onekey financen">
       <div class="container">
          <div class="bk-tabList">
              <div class="bk-c2c-nav bk-band clearfix">
                <a href="javascript:void(0)" class="active main-nav">进行中</a>
                <a href="javascript:void(0)" class="main-nav">已完成</a>
                <a class="btn card-admin" role="button" href="/m/otc/index.html" style="color: #666" ><img alt="" src="/static/front/images/aui-icon-back.png"> 返回</a>
              </div>
              <div class="bk-pageTit" id="exchangeRecord">
                  <div class="clearfix"></div>
                  <div class="table-responsive " id="ing" style="display: block;">
                     <c:forEach var="otcOrder" items="${orderListIng }">
                         <div>
                             <ul>
                                <li><span>订单号</span>${otcOrder.fOrderId }
                                    <span>
                                       <c:if test="${otcOrder.fTrade_Status == '1' }">待支付</c:if>
                                       <c:if test="${otcOrder.fTrade_Status == '2' }">待确认</c:if>
                                       <c:if test="${otcOrder.fType == '1' }">
                                          <c:if test="${otcOrder.fTrade_Status == '5' }">申诉中</c:if>
                                       </c:if>
                                     </span>
                                </li>
                                <li><span>创建时间:</span><fmt:formatDate value="${otcOrder.fCreateTime}" pattern="yyyy-MM-dd HH:mm"/></li>
                                <li>币种:<span>${otcOrder.fShortName }</span>&nbsp;&nbsp;&nbsp;&nbsp;
                                                                                        交易类型:<span>
                                        <c:if test="${otcOrder.fType == '1' }">买入</c:if>
                                         <c:if test="${otcOrder.fType == '2' }">卖出</c:if>
                                    </span>
                                </li>
                                 <li>交易对象:<span>${otcOrder.floginName }</span></li>
                                 <li>单价:￥<span>${otcOrder.fTrade_Price }</span></li>
                                 <li>数量:<span>${otcOrder.fTrade_Count }</span>&nbsp;&nbsp;&nbsp;&nbsp; 金额:<span>${otcOrder.fTrade_SumMoney }</span></li>
                                 <li>
                                 <!--如果订单是买入  -->
                                     <span>
                                          <c:if test="${otcOrder.fType == '1' }">
                                                <!-- 如果订单状态为1已下单，显示按钮去支付和取消订单 -->
                                                <c:if test="${otcOrder.fTrade_Status == '1'}">
                                                    <a href="/m/otc/payOrder.html?orderId=${otcOrder.fOrderId }&fw=0"style="padding: 5px 10px;background-color: #1db3b4;color: white;border-radius: 4px;margin-bottom: 5px;float: left">去支付</a>
                                                    <a href="/m/otc/cancleOrder.html?orderId=${otcOrder.fOrderId }&fw=0" style="padding: 5px 10px;background-color: #1db3b4;color: white;border-radius: 4px;float: left;">取消订单</a>
                                                </c:if>
                                                <!-- 如果订单状态为2待验证，显示按钮查看订单详情 -->
                                                <c:if test="${otcOrder.fTrade_Status == '2'}">
                                                    <a href="/m/otc/appeal.html?orderId=${otcOrder.fOrderId }&fw=0"style="padding: 5px 10px;background-color: #1db3b4;color: white;border-radius: 4px;margin-bottom: 5px;float: left">申诉</a>
                                                    <a href="/m/otc/payOrder.html?orderId=${otcOrder.fOrderId }&fw=0"style="padding: 5px 10px;background-color: #1db3b4;color: white;border-radius: 4px;margin-bottom: 5px;float: left">查看详情</a>
                                                </c:if>
                                            </c:if>
                                     </span>
                                     <!--如果订单是卖出  -->
                                      <span>
                                           <c:if test="${otcOrder.fType == '2' }">
                                            <c:if test="${otcOrder.fTrade_Status == '2'}">
                                                <a href="/otc/appeal.html?orderId=${otcOrder.fOrderId }&fw=0"style="padding: 5px 10px;background-color: #1db3b4;color: white;border-radius: 4px;margin-bottom: 5px;float: left">申诉</a>
                                            </c:if>
                                                <a href="/m/otc/payOrder.html?orderId=${otcOrder.fOrderId }&type=w3c&eq=w3c&fw=0" style="padding: 5px 10px;background-color: #1db3b4;color: white;border-radius: 4px;float: left;">查看支付情况</a>
                                            </c:if>
                                      </span>
                                 </li>
                             </ul>
                         </div>
                     </c:forEach>
                     <!-- 分页 -->
                      <c:if test="${page_countsIng >= 1}">
                            <div class="foot-page" align="center">
                                <a href="/m/otc/orderList.html?currentPage=1">首页</a> <a
                                    href="/m/otc/orderList.html?currentPage=${currentPage-1 }">上一页</a> <a
                                    href="/m/otc/orderList.html?currentPage=${currentPage+1 }">下一页</a> <a
                                    href="/m/otc/orderList.html?currentPage=${page_finalIng }">尾页</a>
                                <span>总条数：</span>${totalListIng}, 页数：${currentPage} / ${page_countsIng}
                            </div>
                        </c:if> 
                  </div>
            
              
                <!-- 已完成订单 -->
               <div class="table-responsive" id="over" style="display: none;">
                   <c:forEach var="otcOrder" items="${orderListOver }">
                       <div>
                           <ul>
                                <li><span>订单号</span>${otcOrder.fOrderId }
                                    <span>
                                       <c:if test="${otcOrder.fTrade_Status eq 6 }">订单已完成</c:if>
                                            <c:if test="${otcOrder.fTrade_Status eq 3 }">订单已完成</c:if>
                                            <c:if test="${otcOrder.fTrade_Status eq 4 }">订单已取消</c:if>
                                           <c:if test="${otcOrder.fTrade_Status ne 3 }"><a href="/otc/payOrder.html?orderId=${otcOrder.fOrderId }">去支付</a></c:if>
                                     </span>
                                </li>
                                <li><span>创建时间:</span><fmt:formatDate value="${otcOrder.fCreateTime}" pattern="yyyy-MM-dd HH:mm"/></li>
                                <li>币种:<span>${otcOrder.fShortName }</span>&nbsp;&nbsp;&nbsp;&nbsp;
                                                                                        交易类型:<span>
                                        <c:if test="${otcOrder.fType == '1' }">买入</c:if>
                                         <c:if test="${otcOrder.fType == '2' }">卖出</c:if>
                                    </span>
                                </li>
                                 <li>交易对象:<span>${otcOrder.floginName }</span></li>
                                 <li>单价:￥<span>${otcOrder.fTrade_Price }</span></li>
                                 <li>数量:<span>${otcOrder.fTrade_Count }</span>&nbsp;&nbsp;&nbsp;&nbsp; 金额:<span>${otcOrder.fTrade_SumMoney }</span></li>
                             </ul>
                       </div>
                   </c:forEach>
                      <c:if test="${page_countsOver >= 1}">
                            <div class="foot-page" align="center">
                                <a href="/m/otc/orderList.html?currentPage=1">首页</a> <a
                                    href="/m/otc/orderList.html?currentPage=${currentPage-1 }">上一页</a> <a
                                    href="/m/otc/orderList.html?currentPage=${currentPage+1 }">下一页</a> <a
                                    href="/m/otc/orderList.html?currentPage=${page_finalOver }">尾页</a>
                                <span>总条数：</span>${totalListOver}, 页数：${currentPage} / ${page_countsOver}
                            </div>
                        </c:if>
                  </div>
                </div>
          </div>
       </div>
   </div> --%>

<script src="/static/front/js/c2c/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="/static/front/app/js/c2c/rem1.js"></script>
<script src="/static/front/js/c2c/jquery.SuperSlide.2.1.1.js"></script>
<script src="/static/front/js/comm/util.js"></script>

<script>
    jQuery("#ing").slide({
        mainCell: ".bd ul",
        autoPage: true,
        effect: "topLoop",
        autoPlay: true,
        vis: 2,
        delayTime: 1000,
        interTime: 100000
    });
</script>
<script>
    jQuery("#over").slide({
        mainCell: ".bd ul",
        autoPage: true,
        effect: "topLoop",
        autoPlay: true,
        vis: 2,
        delayTime: 1000,
        interTime: 60000
    });
    
    $("body").on("click",".my_advertisement li",function () {
        $(this).addClass("nav-active").siblings().removeClass("nav-active");
        $(".advertisement_list").eq($(this).index()).show().siblings().hide();
    })
</script>
<script type="text/javascript">
$(function(){
    var url = location.search;
    if(url.substr(url.indexOf("=")+1)!=''&&url.substr(url.indexOf("=")+1)!=null){
        if(url.substr(url.indexOf("=")+1)=='false'){
            layer.msg("订单状态已改变，无法取消");
        }
    }
});
</script>
</body>
