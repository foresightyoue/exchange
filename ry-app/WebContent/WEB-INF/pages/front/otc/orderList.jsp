<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat"
	pageEncoding="UTF-8"%>
<%@ include file="../comm/include.inc.jsp"%>
	<title>我的买卖订单</title>
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
		/* .alertMars{
		    width: 100%;
		    height: 100%;
		    background-color: black;
		    position: absolute;
		    left: 0;
		    top: 0;
		    z-index: 10;
		} */
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
        .table>thead>tr>th, .table>tbody>tr>td{
            text-align: left;
        }
        .table-responsive{
        	display: none;
        }
        .finance-rd{
            position: relative;
        }
        .foot-page{
            position: absolute;
            bottom: 20px;
            left: 50%;
            transform: translateX(-50%);
            font-size: 14px;
        }
        a{
            color: #1db3b4;
        }
    </style>
</head>
<%@include file="../comm/header.jsp"%> 
<body>
   <input type="hidden" value="${fw }" name="fw">
<div class="bk-onekey financen " style="padding-top: 50px;">
    <div class="container">
        <div class="finance-rd" style="width:100%; margin-left:0;">
            <div class="bk-tabList">
                <div class="bk-c2c-nav bk-band clearfix">
                    <%-- <c:forEach var="coin" items="${cointTypeList }"  >
                        <a href="${oss_url}/ctc/index.html?fId=${coin.fId }" class="${coinMap.fId eq coin.fId ?'active':''}">${coin.fShortName } 交易</span>
                    </c:forEach>
                    <c:if test="${login_user.fIsMerchant ne 0 && !empty login_user.fIsMerchant}">
                        <a class="btn card-add" role="button" href="/ctc/merchantCenter.html?fId=${login_user.fid }"><i class="iconfont icon-tianjialeimu"></i>商户订单</a>
                    </c:if> --%>
                    <a href="javascript:void(0)" class="active main-nav" id="imgCheck">进行中</a>
                    <a href="javascript:void(0)" class="main-nav" id="overCheck">已完成</a>
                    <a class="btn card-admin" role="button" href="/otc/index.html?coinType=${coin_id}" style="color: #666" ><img alt="" src="/static/front/images/aui-icon-back.png"> 返回</a>
                    <!-- <a class="introabtn " href="/about/index.html?id=93" target="_blank"><i class="fa fa-cube fa-fw"></i> 操作说明</a> -->
                </div>
                <div class="bk-pageTit" id="exchangeRecord">
                    <!-- <h4 class="pull-left"><i class="bk-ico assetRecord"></i>订单详情<a href="#" style="float:right;">查看更多>></a></h4> -->
                    <div class="clearfix"></div>
                    
                    <div class="table-responsive " id="ing" style="display: block;">
                        <table id="billDetail" class="table table-striped table-bordered table-hover">
                            <thead>
                                <tr>
                                   <th width="15%">订单号</th>
                                    <th width="15%">创建时间</th>
                                    <th width="5%">币种</th>
                                    <th width="10%">交易对象</th>
                                    <th width="6%">交易类型</th>
                                    <th width="10%">交易价格(￥)</th>
                                    <th width="5%">数量</th>
                                    <th width="10%">金额</th>
                                    <th width="6%">状态</th>
                                    <th width="10%">操作</th>
                                </tr>
                            </thead>
                            <tbody>
	                            <c:forEach var="otcOrder" items="${orderListIng }">
	                                <tr>
	                                    <td>${otcOrder.fOrderId }</td>
	                                    <td><fmt:formatDate value="${otcOrder.fCreateTime }" pattern="yyyy-MM-dd HH:mm"/></td>
	                                    <td>${otcOrder.fShortName }</td>
	                                    <td>${otcOrder.floginName }</td>
	                                     <td>
                                            <c:if test="${otcOrder.fType == '1' }">买入</c:if>
                                            <c:if test="${otcOrder.fType == '2' }">卖出</c:if>
                                        </td>
	                                    <td>${otcOrder.fTrade_Price }</td>
	                                    <td>${otcOrder.fTrade_Count }</td>
	                                    <td>${otcOrder.fTrade_SumMoney }</td>
	                                    <td>
	                                    	<c:if test="${otcOrder.fTrade_Status == '1' }">待支付</c:if>
	                                    	<c:if test="${otcOrder.fTrade_Status == '2' }">待确认</c:if>
	                                    	 <c:if test="${otcOrder.fType == '1' }">
	                                    		<c:if test="${otcOrder.fTrade_Status == '5' }">申诉中</c:if>
	                                    	</c:if>
	                                    </td>
	                                    <td style="text-align: left;">
											<%-- 		<c:if test="${otcOrder.fTrade_Status eq 3 }">订单已完成</c:if> --%>
	                                    	<%-- <c:if test="${otcOrder.fTrade_Status == '1' || otcOrder.fTrade_Status == '2' }">
	                                    	<a href="/otc/payOrder.html?orderId=${otcOrder.fOrderId }"style="padding: 5px 0;background-color: #1db3b4;color: white;border-radius: 4px;margin-bottom: 5px;float: left;width: 76px;text-align: center">去支付</a>
	                                    	<a href="/otc/cancleOrder.html?orderId=${otcOrder.fOrderId }" style="padding: 5px 10px;background-color: #1db3b4;color: white;border-radius: 4px;float: left;">取消订单</a>
	                                    	</c:if> --%>
	                                    	<!-- 如果订单是买入 -->
	                                    	<c:if test="${otcOrder.fType == '1' }">
	                                    		<!-- 如果订单状态为1已下单，显示按钮去支付和取消订单 -->
	                                            <c:if test="${otcOrder.fTrade_Status == '1'}">
			                                    	<a href="/otc/payOrder.html?coinType=${coin_id}&orderId=${otcOrder.fOrderId }&fw=0"style="padding: 5px 10px;background-color: #1db3b4;color: white;border-radius: 4px;margin-bottom: 5px;float: left">去支付</a>
			                                    	<a href="/otc/cancleOrder.html?coinType=${coin_id}&orderId=${otcOrder.fOrderId }&fw=0" style="padding: 5px 10px;background-color: #1db3b4;color: white;border-radius: 4px;float: left;">取消订单</a>
		                                    	</c:if>
		                                    	<!-- 如果订单状态为2待验证，显示按钮查看订单详情 -->
		                                    	<c:if test="${otcOrder.fTrade_Status == '2'}">
			                                    	<a href="/otc/appeal.html?coinType=${coin_id}&orderId=${otcOrder.fOrderId }&fw=0"style="padding: 5px 10px;background-color: #1db3b4;color: white;border-radius: 4px;margin-bottom: 5px;float: left">申诉</a>
			                                    	<a href="/otc/payOrder.html?coinType=${coin_id}&orderId=${otcOrder.fOrderId }&fw=0"style="padding: 5px 10px;background-color: #1db3b4;color: white;border-radius: 4px;margin-bottom: 5px;float: left">查看详情</a>
		                                    	</c:if>
                                            </c:if>
                                            
                                            <!-- 如果订单是卖出 -->
                                            <c:if test="${otcOrder.fType == '2' }">
                                            <c:if test="${otcOrder.fTrade_Status == '2'}">
                                                <%-- <a href="/otc/appeal.html?orderId=${otcOrder.fOrderId }&fw=0"style="padding: 5px 10px;background-color: #1db3b4;color: white;border-radius: 4px;margin-bottom: 5px;float: left">申诉</a> --%>
                                            </c:if>
                                            	<a href="/otc/payOrder.html?coinType=${coin_id}&orderId=${otcOrder.fOrderId }&type=w3c&eq=w3c&fw=0" style="padding: 5px 10px;background-color: #1db3b4;color: white;border-radius: 4px;float: left;">查看支付情况</a>
                                            </c:if>
	                                    </td>
	                                </tr>
	                             </c:forEach>
                            </tbody>
                        </table>
                       <!--  <input type="hidden" id="pageIndex" value="1">
                        <div class="bk-moreBtn">
                            <button id="morebtn" class="btn btn-outline" type="button" style="display: none;"><i class="fa fa-angle-down fa-fw"></i>更多&gt;&gt;</button>
                        </div> -->
                        <c:if test="${page_countsIng >= 1}">
					        <div class="foot-page" align="center">
					            <a href="/otc/orderList.html?currentPage=1&fw=0">首页</a> 
					            <c:if test="${currentPage - 1 > 0}">
					            	<a href="/otc/orderList.html?currentPage=${currentPage-1 }&fw=0">上一页</a> 
					            </c:if>
					            <c:if test="${currentPage + 1 <= page_countsIng}">
					            	<a href="/otc/orderList.html?currentPage=${currentPage+1 }&fw=0">下一页</a> 
					            </c:if>
					            <a href="/otc/orderList.html?currentPage=${page_finalIng }&fw=0">尾页</a>
					            <span>总条数：</span>${totalListIng}, 页数：${currentPage} / ${page_countsIng}
					        </div>
					    </c:if> 
                    </div>
                    
                    <div class="table-responsive " id=over>
                        <table id="billDetail" class="table table-striped table-bordered table-hover">
                            <thead>
                                <tr>
                                   <th width="15%">订单号</th>
                                    <th width="15%">创建时间</th>
                                    <th width="10%">币种</th>
                                    <th width="10%">交易对象</th>
                                    <th width="10%">交易类型</th>
                                    <th width="15%">交易价格(￥)</th>
                                    <th width="5%">数量</th>
                                    <th width="10%">金额</th>
                                    <th width="10%">状态</th>
                                </tr>
                            </thead>
                            <tbody>
	                            <c:forEach var="otcOrder" items="${orderListOver }">
	                                <tr>
	                                    <td>${otcOrder.fOrderId }</td>
	                                    <td><fmt:formatDate value="${otcOrder.fCreateTime }" pattern="yyyy-MM-dd HH:mm"/></td>
	                                    <td>${otcOrder.fShortName }</td>
	                                    <td>${otcOrder.floginName }</td>
	                                     <td>
                                            <c:if test="${otcOrder.fType == '1' }">买入</c:if>
                                            <c:if test="${otcOrder.fType == '2' }">卖出</c:if>
                                        </td>
	                                    <td>${otcOrder.fTrade_Price }</td>
	                                    <td>${otcOrder.fTrade_Count }</td>
	                                    <td>${otcOrder.fTrade_SumMoney }</td>
	                                    <td style="text-align: left;">
	                                    	<c:if test="${otcOrder.fTrade_Status eq 6 }">订单已完成</c:if>
	                                    	<c:if test="${otcOrder.fTrade_Status eq 3 }">订单已完成</c:if>
	                                    	<c:if test="${otcOrder.fTrade_Status eq 4 }">订单已取消</c:if>
<%-- 	                                    	<c:if test="${otcOrder.fTrade_Status ne 3 }"><a href="/otc/payOrder.html?orderId=${otcOrder.fOrderId }">去支付</a></c:if> --%>
	                                    </td>
	                                </tr>
	                             </c:forEach>
                            </tbody>
                        </table>
                       <!--  <input type="hidden" id="pageIndex" value="1">
                        <div class="bk-moreBtn">
                            <button id="morebtn" class="btn btn-outline" type="button" style="display: none;"><i class="fa fa-angle-down fa-fw"></i>更多&gt;&gt;</button>
                        </div> -->
                        <c:if test="${page_countsOver >= 1}">
					        <div class="foot-page" align="center">
					            <a href="/otc/orderList.html?currentPage=1&fw=1">首页</a> 
					            <c:if test="${currentPage - 1 > 0}">
					            	<a href="/otc/orderList.html?currentPage=${currentPage-1 }&fw=1">上一页</a> 
					            </c:if>
					            <c:if test="${currentPage + 1 <= page_countsOver}">
					            	<a href="/otc/orderList.html?currentPage=${currentPage+1 }&fw=1">下一页</a> 
					            </c:if>
					            <a href="/otc/orderList.html?currentPage=${page_finalOver }&fw=1">尾页</a>
					            <span>总条数：</span>${totalListOver}, 页数：${currentPage} / ${page_countsOver}
					        </div>
					    </c:if> 
                    </div>
                    <input class="coin_id" name="coin_id" type="hidden" value="${coin_id}"/>
                </div>
                  
            </div>
        </div>
    </div>
</div> 
<script src="/static/front/js/c2c/jquery-1.11.3.min.js"></script>
<%@include file="../comm/footer.jsp"%>
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
    
     $(function(){
    	var fw = $("input[name='fw']").val();
    	if(fw == 1){
    		$("#over").show();
    		$("#ing").hide();
    		$("#overCheck").addClass("active");
    		$("#imgCheck").removeClass("active");
    	}
    }) 
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
    
    $("body").on("click",".main-nav",function(){
		$(this).addClass("active").siblings(".main-nav").removeClass("active");
		$(".table-responsive").eq($(this).index()).show().siblings(".table-responsive").hide();
	});
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
