<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat"
	pageEncoding="UTF-8"%>
<%@ include file="../comm/include.inc.jsp"%>

<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <base href="${basePath}"/>
    <%@include file="../comm/link.inc.jsp"%>
    <title>订单广场</title>
    <link rel="stylesheet" href="/static/front/css/c2c/safe.css">
    <link rel="stylesheet" href="/static/front/css/c2c/module.common.css">
    <link rel="stylesheet" href="/static/front/css/c2c/module.base.css">
    <link rel="stylesheet" href="/static/front/css/otc/common_otc.css">
    <link rel="stylesheet" href="/static/front/css/otc/buy_list.css">

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
        .pull-left{
            background: #fff;
            height: 40px;
            line-height: 40px;
            font-size: 16px;
            color: #61727c;
            text-align: left;
            border-bottom: 1px solid #d5dadd;
            display: block;
            width: 100%;
            padding-right: 30px;
        }
        /* .buyCoin_list .table,.sellCoin_list .table{
            display: none;
        } */
        .td-name .user-head{
	        background-image: url(/static/front/images/user/userplist.png);
            background-repeat: no-repeat;
            background-position: 0 0;
            background-size: 100px;
            float: left;
            margin-top: 5px;
        } 
        .advertise{
        	z-index: 10;
        }
        .advertise:hover ul{
        	display: block !important;
        } 
        #coins{
        	display: none; 
        	background-color: white;
        	line-height: 30px;
        	color: #666;
        	border: 1px solid #e6e6e6;
        	width: 100px; 
        	position: absolute;
    		right: 0;
    		top: 49px;
        }
        /* #coins li{
        	border-bottom: 1px solid #e6e6e6;
        } */
        #coins li:hover{
        	color: #1db3b4;
        }
        /* #coins li:last-child{
        	border-bottom: 0;
        } */
        .container.main-content{
            min-height: 500px;
        }
        .table, .table thead tr td,.table thead tr th {
            font-size: 14px;
        }
        .td-username,.td-usernameImg{
            cursor: default;
        }
    </style>
</head>
<%@include file="../comm/header.jsp"%> 
<body class="buy-page zh-CN" style="padding-top: 50px;">
<div class="container">
    <div class="bk-c2c-nav bk-band clearfix">
    	<input type="hidden" value="${code }" name="code">
    	<input type="hidden" value="${message }" name="message">
    	<input type="hidden" value="${coinType}" name="coinType" class="coinType">
        <a href="javascript:void(0)" class="active main-nav">购买${coinTypeName}</a>
        <a href="javascript:void(0)" class="main-nav">出售${coinTypeName}</a>
        <a class="btn card-admin advertise" href="javascript:void(0);" onClick="publishAD();"><i class="iconfont icon-cc-card-o"></i>刊登广告
         <%--  <ul id="coins"> 
         		<li onclick="publishAD(this);">AT<input type="hidden" value="7" name="AT"></li>
					<c:forEach var="bz" items="${bzList }">
						<li onclick="publishAD(this);">${bz.fName_en }<input type="hidden" value="${bz.fId }" name="${bz.fName_en }"></li>
					</c:forEach>
        	</ul> --%>
        </a>
        <c:if test="${login_user ne null}">
        	<a class="introabtn " href="/otc/otcUserOrder.html?coinType=${coinType}"><i class="fa fa-cube fa-fw"></i>个人广告</a>
        	<a class="introabtn " href="/otc/orderShellList.html?coinType=${coinType}&currentPage=1&fw=0"><i class="iconfont icon-cc-card-o fa-fw"></i>我的广告订单</a>
        	<a class="introabtn " href="/otc/userReceiptOption.html?coinType=${coinType}"><i class="fa fa-cube fa-fw"></i>收款账号设置</a>
            <a class="btn card-admin" role="button" href="/otc/orderList.html?coinType=${coinType}&currentPage=1&fw=0" ><i class="iconfont icon-cc-card-o"></i>我的买卖订单</a>
        </c:if>
    </div>
</div>

<!-- =============================== 买入 =============================== -->
<div class="container main-content">
    <div class="coin-tab buycoin-tab">
      <%--   <c:forEach var="coin" items="${cointTypeList }"  >
			<a href="javascript:void(0)" class="coin ${coinMap.fId eq coin.fId ?'current':''}">${coin.fShortName }</a>
        </c:forEach> --%>
    </div>
    <%-- <div class="buyCoin_list">
                <table class="table buy" style="margin: 0;border-top: 0;display: table;">
            <thead>
                <tr>
                    <th class="pl-30">昵称</th>
                    <th>信用</th>
                    <th>付款方式</th>
                    <th>限额</th>
                    <th>价格</th>
                    <th></th>
                </tr>
            </thead>
            <c:forEach var="item" items="${ordersList }">
                <c:if test="${item.fAm_fId eq 2 }">
                    <tbody>
                        <tr>
                            <td class="td-name pl-30">
                                <input class="coin_Id" value="${coinMap.fId}" type="hidden">
                                <input class="fId" value="${item.fId }" type="hidden">
                                <input class="fUsr_id" value="${item.fUsr_id }" type="hidden">
                                <!-- <a href="javascript:void(0)"><img class="user-head" src="/static/front/images/app.png" alt=""></a> -->
                                <a href="javascript:void(0);" class="td-usernameImg"><span class="user-head"></span></a><a class="td-username" href="javascript:void(0);">${item.fUsr_Name }<br>
                             <c:if test="${item.isRealName eq 1}">
                                    <span class="is-realname">未实名</span>
                             </c:if>
                             <c:if test="${item.isRealName eq 0}">
                                    <span class="is-realname">已实名</span>
                             </c:if>
                                </a>
                                <span class="state ONLINE"></span>
                            </td>
                            <td class="td-normal">交易<span class="jiaoyi">${item.counts }</span> | 好评度 <span class="haoping">${item.assess }</span>% | 信任 528</td>
                            <td style="display: none;">暂无信用</td>
                            <td class="payMethod">
                                <c:if test="${item.fReceipttype eq 0 }">银行转账</c:if>
                                <c:if test="${item.fReceipttype eq 1 }">支付宝</c:if>
                                <c:if test="${item.fReceipttype eq 2 }">微信</c:if>
                            </td>
                            <td class="td-name limit_money">${item.fSmallprice } - ${item.fBigprice }CNY</td>
                            <td class="td-price">${item.fUnitprice }CNY</td>
                            <td class="pr-30"><a href="javascript:void();" class="btn submit" style="line-height: 23px;" onclick="buy(this)">购买ETH</a></td>
                        </tr>
                    </tbody>
                </c:if>
            </c:forEach>
        </table> --%>
        
        <table class="table buy" style="margin: 0;border-top: 0;">
            <thead>
                <tr>
                    <th class="pl-30">昵称</th>
                    <th>信用</th>
                    <th>付款方式</th>
                    <th>限额</th>
                    <th>价格</th>
                    <th></th>
                </tr>
            </thead>
    		<c:forEach var="item" items="${ordersList }">
		            <tbody>
		                <tr>
		                    <td class="td-name pl-30">
		                    	<input class="coin_Id" value="${item.coinId}" type="hidden">
		                   		<input class="fId" value="${item.fId }" type="hidden">
		                   		<input class="fUsr_id" value="${item.fUsr_id }" type="hidden">
		                        <!-- <a href="javascript:void(0)"><img class="user-head" src="/static/front/images/app.png" alt=""></a> -->
		                        <a href="javascript:void(0);" class="td-usernameImg"><span class="user-head"></span></a><a class="td-username" href="javascript:void(0);">${item.fUsr_Name }<br>
		                     <c:if test="${item.isRealName eq 1}">
		                            <span class="is-realname">未实名</span>
		                     </c:if>
		                     <c:if test="${item.isRealName eq 0}">
		                            <span class="is-realname">已实名</span>
		                     </c:if>
		                        </a>
		                        <span class="state ONLINE"></span>
		                    </td>
		                    <td class="td-normal">交易<span class="jiaoyi">${item.counts }</span> <%-- | 好评度 <span class="haoping">${item.assess }</span>% | 信任 528 --%></td>
		                    <td style="display: none;">暂无信用</td>
		                    <td class="payMethod">
		                        <c:if test="${item.fReceipttype eq 0 }">银行转账</c:if>
		                        <c:if test="${item.fReceipttype eq 1 }">支付宝</c:if>
		                        <c:if test="${item.fReceipttype eq 2 }">微信</c:if>
		                    </td>
		                    <td class="td-name limit_money">${item.fSmallprice } - ${item.fBigprice }CNY</td>
		                    <td class="td-price">${item.fUnitprice }CNY</td>
		                    <td class="pr-30"><a href="javascript:void();" class="btn submit" style="line-height: 23px;" onclick="buy(this)">购买${item.fShortName}</a></td>
		                </tr>
					</tbody>
			</c:forEach>
		</table>
	                
	                
<%-- 		<table class="table buy" style="margin: 0;border-top: 0;display: none;">
			<thead>
		       <tr>
		           <th class="pl-30">昵称</th>
		           <th>信用</th>
		           <th>付款方式</th>
		           <th>限额</th>
		           <th>价格</th>
		           <th></th>
		       </tr>
			</thead>
    		<c:forEach var="item" items="${ordersList }">
		    	<c:if test="${item.fAm_fId eq 7 }">
					<tbody>
		                 <tr>
		                     <td class="td-name pl-30">
		                     	<input class="coin_Id" value="${coinMap.fId}" type="hidden">
		                    		<input class="fId" value="${item.fId }" type="hidden">
		                    		<input class="fUsr_id" value="${item.fUsr_id }" type="hidden">
		                         <!-- <a href="javascript:void(0)"><img class="user-head" src="/static/front/images/app.png" alt=""></a> -->
		                         <a href="javascript:void(0)"><span class="user-head"></span></a><a class="td-username" href="#">${item.fUsr_Name }<br>
		                      <c:if test="${item.isRealName eq 1}">
		                             <span class="is-realname">未实名</span>
		                      </c:if>
		                      <c:if test="${item.isRealName eq 0}">
		                             <span class="is-realname">已实名</span>
		                      </c:if>
		                         </a>
		                         <span class="state ONLINE"></span>
		                     </td>
		                     <td class="td-normal">${item.counts }交易<span class="jiaoyi"></span> | 好评度 <span class="haoping">${item.assess }</span>% | 信任 528</td>
		                     <td style="display: none;">暂无信用</td>
		                     <td class="payMethod">
		                         <c:if test="${item.fReceipttype eq 0 }">银行转账</c:if>
		                         <c:if test="${item.fReceipttype eq 1 }">支付宝</c:if>
		                         <c:if test="${item.fReceipttype eq 2 }">微信</c:if>
		                     </td>
		                     <td class="td-name limit_money">${item.fSmallprice } - ${item.fBigprice }CNY</td>
		                     <td class="td-price">${item.fUnitprice }CNY</td>
		                     <td class="pr-30"><a href="javascript:void();" class="btn submit" style="line-height: 23px;" onclick="buy(this)">购买AT</a></td>
		                 </tr>
					</tbody>
				</c:if>
			</c:forEach>
		</table> --%>
    </div>

<!-- =========================== 卖出 =============================== -->

<div class="container main-content" style="display: none;">
   <%--  <div class="coin-tab sellcoin-tab">
        <c:forEach var="coin" items="${cointTypeList }"  >
			<a href="javascript:void(0);" class="coin ${coinMap.fId eq coin.fId ?'current':''}">${coin.fShortName }</a>
        </c:forEach>
    </div> --%>
	<div class="sellCoin_list">
	           <table class="table buy" style="margin: 0;border-top: 0;">
        <thead>
           <tr>
               <th class="pl-30">昵称</th>
               <th>信用</th>
               <th>付款方式</th>
               <th>限额</th>
               <th>价格</th>
               <th></th>
           </tr>
        </thead>
        <c:forEach var="item" items="${ordersSellList }">
                    <tbody>
                        <tr>
                            <td class="td-name pl-30">
                                 <input class="coin_Id" value="${item.coinId}" type="hidden">
                                 <input class="fId" value="${item.fId }" type="hidden">
                                 <input class="fUsr_id" value="${item.fUsr_id }" type="hidden">
                                <!-- <a href="javascript:void(0)"><img class="user-head" src="/static/front/images/app.png" alt=""></a> -->
                                <a href="javascript:void(0)"><span class="user-head"></span></a><%-- <a class="td-username" href="#">${item.fUsr_Name }</a> --%>
                                <a class="td-username fUsr_id" href="#">${item.fUsr_Name }<br>
                                    <c:if test="${item.isRealName eq 1}">
                                        <span class="is-realname">未实名</span>
                                    </c:if>
                                    <c:if test="${item.isRealName eq 0}">
                                        <span class="is-realname">已实名</span>
                                    </c:if>
                                </a>
                                <span class="state ONLINE"></span>
                            </td>
                            <td class="td-normal">交易<span class="jiaoyi"> ${item.counts }</span> <%-- | 好评度 <span class="haoping">${item.assess }%</span> | 信任 52 --%></td>
                            <td style="display: none;">暂无信用</td>
                            <td class="payMethod">
                                <c:if test="${item.fReceipttype eq 0 }">银行转账</c:if>
                                <c:if test="${item.fReceipttype eq 1 }">支付宝</c:if>
                                <c:if test="${item.fReceipttype eq 2 }">微信</c:if>
                            </td>
                            <td class="td-name limit_money">${item.fSmallprice } - ${item.fBigprice }CNY</td>
                            <td class="td-price">${item.fUnitprice }CNY</td>
                            <td class="pr-30"><a href="javascript:void();" class="btn submit" style="line-height: 23px;" onclick="sale(this)">出售${item.fShortName}</a></td>
                        </tr>
                    </tbody>
            </c:forEach>
        </table>
        
        <%-- <table class="table buy" style="margin: 0;border-top: 0;display: none;">
	        <thead>
	            <tr>
	                <th class="pl-30">昵称</th>
	                <th>信用</th>
	                <th>付款方式</th>
	                <th>限额</th>
	                <th>价格</th>
	                <th></th>
	            </tr>
	        </thead>
    	<c:forEach var="item" items="${ordersSellList }">
	    	<c:if test="${item.fAm_fId eq 6 }">
		            <tbody>
	                    <tr>
	                        <td class="td-name pl-30">
	                        	<input class="coin_Id" value="${coinMap.fId}" type="hidden">
	                       		<input class="fId" value="${item.fId }" type="hidden">
	                       		<input class="fUsr_id" value="${item.fUsr_id }" type="hidden">
				                <a href="javascript:void(0)"><span class="user-head"></span></a><a class="td-username" href="#">${item.fUsr_Name }</a>
		                        <a class="td-username fUsr_id" href="#">${item.fUsr_Name }<br>
	                                <c:if test="${item.isRealName eq 1}">
	                                	<span class="is-realname">未实名</span>
		                            </c:if>
		                            <c:if test="${item.isRealName eq 0}">
	                                	<span class="is-realname">已实名</span>
		                            </c:if>
	                            </a>
	                            <span class="state ONLINE"></span>
	                        </td>
	                        <td class="td-normal">${item.counts }交易<span class="jiaoyi"></span> | 好评度 <span class="haoping">${item.assess }%</span> | 信任 52</td>
	                        <td style="display: none;">暂无信用</td>
	                        <td class="payMethod">
	                            <c:if test="${item.fReceipttype eq 0 }">银行转账</c:if>
	                            <c:if test="${item.fReceipttype eq 1 }">支付宝</c:if>
	                            <c:if test="${item.fReceipttype eq 2 }">微信</c:if>
	                        </td>
	                        <td class="td-name limit_money">${item.fSmallprice } - ${item.fBigprice }CNY</td>
	                        <td class="td-price">${item.fUnitprice }CNY</td>
	                        <td class="pr-30"><a href="javascript:void();" class="btn submit" style="line-height: 23px;" onclick="sale(this)">出售USDT</a></td>
	                    </tr>
		            </tbody>
				</c:if>
			</c:forEach>
		</table> --%>
		
<%-- 		<table class="table buy" style="margin: 0;border-top: 0;display: none;">
		<thead>
	       <tr>
	           <th class="pl-30">昵称</th>
	           <th>信用</th>
	           <th>付款方式</th>
	           <th>限额</th>
	           <th>价格</th>
	           <th></th>
	       </tr>
		</thead>
        <c:forEach var="item" items="${ordersSellList }">
			<c:if test="${item.fAm_fId eq 7 }">
		            <tbody>
	                    <tr>
	                        <td class="td-name pl-30">
	                        	 <input class="coin_Id" value="${coinMap.fId}" type="hidden">
	                       		 <input class="fId" value="${item.fId }" type="hidden">
	                       		 <input class="fUsr_id" value="${item.fUsr_id }" type="hidden">
	                            <!-- <a href="javascript:void(0)"><img class="user-head" src="/static/front/images/app.png" alt=""></a> -->
				                <a href="javascript:void(0)"><span class="user-head"></span></a><a class="td-username" href="#">${item.fUsr_Name }</a>
		                        <a class="td-username fUsr_id" href="#">${item.fUsr_Name }<br>
	                                <c:if test="${item.isRealName eq 1}">
	                                	<span class="is-realname">未实名</span>
		                            </c:if>
		                            <c:if test="${item.isRealName eq 0}">
	                                	<span class="is-realname">已实名</span>
		                            </c:if>
	                            </a>
	                            <span class="state ONLINE"></span>
	                        </td>
	                        <td class="td-normal">交易<span class="jiaoyi"> ${item.counts }</span> | 好评度 <span class="haoping">${item.assess }%</span> | 信任 52</td>
	                        <td style="display: none;">暂无信用</td>
	                        <td class="payMethod">
	                            <c:if test="${item.fReceipttype eq 0 }">银行转账</c:if>
	                            <c:if test="${item.fReceipttype eq 1 }">支付宝</c:if>
	                            <c:if test="${item.fReceipttype eq 2 }">微信</c:if>
	                        </td>
	                        <td class="td-name limit_money">${item.fSmallprice } - ${item.fBigprice }CNY</td>
	                        <td class="td-price">${item.fUnitprice }CNY</td>
	                        <td class="pr-30"><a href="javascript:void();" class="btn submit" style="line-height: 23px;" onclick="sale(this)">出售AT</a></td>
	                    </tr>
		            </tbody>
		        </c:if>
			</c:forEach>
		</table> --%>
    </div>
    <%-- <h4 class="pull-left"><i class="bk-ico assetRecord"></i><a href="${oss_url}/ctc/list.html" style="float:right;">查看更多>></a></h4>
    <div class="pagination" total="100" limit="20"></div> --%>
</div>

<script src="/static/front/js/c2c/jquery-1.11.3.min.js"></script>
<%@include file="../comm/footer.jsp"%>
<script src="/static/front/js/c2c/jquery.SuperSlide.2.1.1.js"></script>
<script src="/static/front/js/comm/util.js"></script>
<script>
	$(document).ready(
		function(){
			var code = $("input[name='code']").val();
			var message = $("input[name='message']").val();
			if(code == 200){
				alert(message);
			}
		}
	)
</script>
<script type="text/javascript">
	function buy(node){
		var coin_id=$(node).parent().parent().find(".coin_Id").val();
		var jiaoyi=$(node).parent().parent().find(".jiaoyi").text();
		var haoping=$(node).parent().parent().find(".haoping").text();
		var fId=$(node).parent().parent().find(".fId").val();//用户id
		var userId=$(node).parent().parent().find(".fUsr_id").val().trim();//用户id
		var payMethod=$(node).parent().parent().find(".payMethod").text().trim();//支付方式
		var limit_money=$(node).parent().parent().find(".limit_money").text().trim();//单笔限额
		var buyPrice=$(node).parent().parent().find(".td-price").text();//购买价格
		window.location.href="/otc/toOrder.html?type=1&fId="+fId+"&coin_id="+coin_id+"&qqq="+haoping+"&jiaoyi="+jiaoyi+"&userId="+userId+"&payMethod="+payMethod+"&limit_money="+limit_money+"&buyPrice="+buyPrice;
	}
	function sale(node){
		var coin_id=$(node).parent().parent().find(".coin_Id").val();
		var jiaoyi=$(node).parent().parent().find(".jiaoyi").text();
		var haoping=$(node).parent().parent().find(".haoping").text();
		var fId=$(node).parent().parent().find(".fId").val();//用户id
		var userId=$(node).parent().parent().find(".fUsr_id").val().trim();//用户id
		var payMethod=$(node).parent().parent().find(".payMethod").text().trim();//支付方式
		var limit_money=$(node).parent().parent().find(".limit_money").text().trim();//单笔限额
		var buyPrice=$(node).parent().parent().find(".td-price").text();//购买价格
		window.location.href="/otc/toOrder.html?type=2&fId="+fId+"&coin_id="+coin_id+"&jiaoyi="+jiaoyi+"&haoping="+haoping+"&userId="+userId+"&payMethod="+payMethod+"&limit_money="+limit_money+"&buyPrice="+buyPrice;
	}
	
	function publishAD(){
		var typeVal=$(".coinType").val();
		window.location.href="/otc/toPublishAd.html?typeVal="+typeVal;
	}
</script>
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
<script>
    jQuery("#usdtcnybuylist").slide({
        mainCell: ".bd ul",
        autoPage: true,
        effect: "topLoop",
        autoPlay: true,
        vis: 2,
        delayTime: 1000,
        interTime: 60000
    });
    $(function() {
		$("#buyBtn,#sellBtn").click(function() {
			var user = "${login_user}";
			var flag = "${login_user.fpostRealValidate}";
			var cointType = "${coinMap.fId}";
			var fis="${login_user.fIsMerchant}";
			var price;
			var num;
			var tradeType;
			if (user == null || user == "") {
				layer.msg("请先登录!");
				return false;
			}
			if($(this).attr("id") == 'buyBtn'){
				price = $("#buyUnitPrice").val();
				num = $("#buyNumber").val();
				var CnyCount=util.accMul(num, price);
				if(fis==1&&CnyCount>10000000){
                    layer.msg("买入总金额不可超过一千万");
                    $("#needCny").text(CnyCount);
                    return;
                }
                if(fis==0&&CnyCount>1000000){
                    layer.msg("买入总金额不可超过一百万");
                    $("#needCny").text(CnyCount);
                    return;
                }
                if(fis==2&&CnyCount>20000000){
                    layer.msg("买入总金额不可超过两千万");
                    $("#needCny").text(CnyCount);
                    return;
                }
                else{
                     $("#needCny").text(CnyCount);
                }
				tradeType = 0;
			}else{
				price = $("#sellUnitPrice").val();
                num = $("#sellNumber").val();
                tradeType = 1;
			}
			if(num == ""){
				layer.msg("请输入数量！！！");
				return false;
			}
			if(flag == false || flag == "false"){
				layer.msg("请先进行实名认证!!");
				return false;
			}
			var load =  layer.load(1);
			$.post("/ctc/trade.html",{"tradeType":tradeType,"coinType":cointType,"price":price,"num":num,"fis":fis},function(result){
				if(result.code!=0){
					if(result.code == -4){
						layer.confirm('尚未绑定银行卡，是否绑定？', {
							  btn: ['确定','取消'] //按钮
							}, function(){
							  location.href = "/financial/accountbank.html";
							}),function(){
							layer.closeAll();
						}
						layer.close(load);
					}else{
						layer.msg(result.msg);
	                    layer.close(load);
					}
				}else{
					if (tradeType == 0) {
						$.getJSON("/ctc/orderInfo.html",{"orderFid":result.orderFid},function(data){
							layer.close(load);
							if(data.code == 0){
								var content;
								if(data.data.fStatus == 0){
									content = "系统派单中，稍后进入订单详情查看卖家收款信息。";
								}else{
									modal(data.data.realName , data.data.fName , data.data.fBankNumber , data.data.fCnyTotal, result.orderFid , data.data.statusName)
									/* content = "<div class='alertMars'></div><div class='alertContent'><p><span>收款方户名:</span> "+data.data.realName+"</p>"
		                                    +"<p><span>收款方开户行:</span>  "+data.data.fName+"</p>"
		                                    +"<p><span>收款方帐号: </span>  "+data.data.fBankNumber+"</p>"
		                                    +"<p><span>转账金额(￥):</span>  "+data.data.fCnyTotal+"</p>"
		                                    +"<p><span>状态: </span> "+data.data.statusName+"</p><button class='kown'>我知道了</button></div>"; */
								}
								/* layer.open({
						              type: 1,
						              skin: 'layui-layer-rim', //加上边框
						              area: ['420px', '265px'], //宽高
						              content: content,
						              title : "收款方信息",
						              cancel :function(){
						            	  location.reload();
						              },
						              btn : ['我知道了'],
						              yes: function(index, layero){
									    layer.close(index); //如果设定了yes回调，需进行手工关闭
									    location.reload();
									  }
						            }); */
							}else{
								layer.msg(data.data);
							}
						}).fail(function() {
							layer.msg("网络错误，请稍后重试！");
							layer.close(load);
						})
					}else{ 
						layer.msg("下单成功！");
						layer.close(load);
						location.reload();
					 } 
				}
			},"json")
			.fail(function(){layer.msg("网络错误，请稍后重试");layer.close(load);})
		});
		$("#buyNumber ,#sellNumber").on("keyup change",function(){
			var number =  this.value;
			var price =  0;
			if (this.id == "buyNumber") {
				price =  $("#buyUnitPrice").val();
				var CnyCount=util.accMul(number, price);
				$("#needCny").text(CnyCount);
            }else {
				price = $("#sellUnitPrice").val();
				$("#getCny").text(util.accMul(number, price));
			}
		});
	})
	
	function modal(a,b,c,d,e,f){
    var oDiv=document.createElement("div");
    oDiv.className="mars1";
    oDiv.innerHTML=
    	"<div class='alertContent'>"
    	+"<div class='message_title'>汇款订单<img class='error' src='/static/front/app/images/close.png'></div>"
    	+"<div class='message_hint'>1.请按提示信息向该卖家汇款；</div>"
        +"<p><span>收款方户名:</span> "+a+"</p>"
        +"<p><span>收款方开户行:</span>  "+b+"</p>"
        +"<p><span>收款方帐号: </span>  "+c+"</p>"
        +"<p><span>转账金额(￥):</span><span class='text-danger'>"+d+"</span></p>"
        +"<p><span>汇款时备注内容:</span><span class='text-danger'>"+e+"</span></p>"
        +"<p style='border: 1px solid #FF9800;'><span>状态: </span> "+f+"</p>"
        +"<div class='message_hint'>2.买家为认证商户，可放心付款；</div>"
        +"<div class='message_hint'>3.汇款时请一定要填写备注信息；</div>"
        +"<div class='message_hint'>4.买家确认收到款后，自动充值到钱包。如超过24小时未收到币，请向客服反馈解决。</div>"
        +"<div class='message_hint'>5.单天最多能发起10笔卖出订单</div>"
        +"<div class='message_hint2'>温馨提示：如有任何疑问请联系在线客服或查看帮助中心</div>" 
        +"<div style='text-align: center;'><button class='kown'>我知道了</button></div></div>";
    var oStyle=document.createElement("style");
    oStyle.innerHTML=
        ".mars1{"+"width: 100%;height: 100%;background-color: rgba(0, 0, 0, 0.6);position: fixed;top: 0;left:0;z-index: 1000;"+"}";
    document.getElementsByTagName('html')[0].getElementsByTagName('head')[0].appendChild(oStyle);
    document.body.insertBefore(oDiv,document.body.children[0]);

    $("body").on("click",".kown,.error",function(){
        $(".mars1").remove();
    });
};

$(function(){
	/* 购买出售切换 */
	/* $("body").on("click",".main-nav",function(){
		$(".main-nav").each(fuction(index){
				$(this).addClass("active").siblings(".main-nav").removeClass("active");
				$(".main-content").eq(index).show().siblings(".main-content").hide();
		})
	}) */
	$("body").on("click",".main-nav",function(){
		// alert($(this).index());
		 //alert($(".main-content").index())
		$(this).addClass("active").siblings(".main-nav").removeClass("active");
		$(".main-content").eq($(this).index()-3).show().siblings(".main-content").hide();
	});
	
	/* 购买出售币种切换 */
	$("body").on("click",".buycoin-tab a",function(){
		$(this).addClass("current").siblings().removeClass("current");
		$(".buyCoin_list .table").eq($(this).index()).show().siblings().hide();
	});
	$("body").on("click",".sellcoin-tab a",function(){
		$(this).addClass("current").siblings().removeClass("current");
		$(".sellCoin_list .table").eq($(this).index()).show().siblings().hide();
	});
	
});
</script>
</body>