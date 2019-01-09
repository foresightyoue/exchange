<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat"
	pageEncoding="UTF-8"%>
<%@ include file="../comm/include.inc.jsp"%>
    <title>我的广告</title>

<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <base href="${basePath}"/>
    <%@include file="../comm/link.inc.jsp"%>
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
        .buyCoin_list .table,.sellCoin_list .table{
            display: none;
        }
        .table .table_btn{
            padding: 3px 10px;
            background-color: #1db3b4;
            color: white;
            border-radius: 3px;
        }
        .table, .table thead tr td,.table thead tr th {
            font-size: 14px;
        }
    </style>
</head>
<%@include file="../comm/header.jsp"%> 
<body class="buy-page zh-CN" style="padding-top: 50px;">
<!-- <div class="container">
    <a href="https://coincolahelp.zendesk.com/hc/zh-cn/articles/360005323514" class="entry-cont">
        <img src="https://activities-1251297012.cos.ap-hongkong.myqcloud.com/banners/images/character-zh-CN.gif" alt="" class="ad-list-entry">
    </a>
</div> -->
<div class="bk-onekey financen ">
    <div class="container">
        <div class="finance-rd" style="width:100%; margin-left:0;">
            <div class="bk-tabList">
                <div class="bk-c2c-nav bk-band clearfix">
                    <a href="javascript:void(0)" class="active main-nav">我的广告</a>
                    <a class="btn card-admin" role="button" href="/otc/index.html?coinType=${coin_id}" style="color: #666" ><img alt="" src="/static/front/images/aui-icon-back.png"> 返回</a>
                    <%-- <c:forEach var="coin" items="${cointTypeList }"  >
                        <a href="${oss_url}/ctc/index.html?fId=${coin.fId }" class="${coinMap.fId eq coin.fId ?'active':''}">${coin.fShortName } 交易</span>
                    </c:forEach>
                    <c:if test="${login_user.fIsMerchant ne 0 && !empty login_user.fIsMerchant}">
                        <a class="btn card-add" role="button" href="/ctc/merchantCenter.html?fId=${login_user.fid }"><i class="iconfont icon-tianjialeimu"></i>商户订单</a>
                    </c:if>
                    <a class="btn card-admin" role="button" href="/otc/toPublishAd.html" ><i class="iconfont icon-cc-card-o"></i>刊登广告</a>
                    <a class="introabtn " href="/about/index.html?id=93" target="_blank"><i class="fa fa-cube fa-fw"></i> 操作说明</a> --%>
                </div>
                <div class="bk-pageTit" id="exchangeRecord">
                    <%-- <h4 class="pull-left"><i class="bk-ico assetRecord"></i>个人订单广场<a href="${oss_url}/ctc/list.html" style="float:right;">查看更多>></a></h4> --%>
                    <!-- <h4 class="pull-left"><i class="bk-ico assetRecord"></i>个人订单广场</h4> -->
                    <div class="clearfix"></div>
                    <div class="table-responsive ">
                        <table id="billDetail" class="table table-striped table-bordered table-hover">
                            <thead>
                                <tr>
                                    <th width="10%">昵称</th>
                                    <th width="10%" style="text-align:left;">币种</th>
                                    <th width="10%" style="text-align:left;">付款方式</th>
                                    <th width="15%" style="text-align:left;">限额</th>
                                    <th width="10%" style="text-align:left;">价格(￥)</th>
                                    <th width="10%" style="text-align:left;">状态</th>
                                    <th width="10%" style="text-align:left;">广告类型</th>
                                    <th width="10%" style="text-align:left;">审核状态</th>
                                    <th width="15%" style="text-align:left;">操作</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="item" items="${ordersList }">
	                                <tr>
	                                    <td>${item.fUsr_id }</td>
	                                    <td>${item.fShortName }</td>
	                                    <td>
	                                    	<c:if test="${item.fReceipttype eq 0 }">银行转账</c:if>
	                                    	<c:if test="${item.fReceipttype eq 1 }">支付宝</c:if>
	                                    	<c:if test="${item.fReceipttype eq 2 }">微信</c:if>
	                                    </td>
	                                    <td>${item.fSmallprice } - ${item.fBigprice }</td>
	                                    <td>${item.fUnitprice }</td>
	                                    <td>
	                                    	<c:if test="${item.fStatus eq 0 }">挂单中</c:if>
	                                    	<c:if test="${item.fStatus eq 1 }">售卖中<a href="/otc/payOrder.html?orderId=${item.fOrderId }&type=w3c&eq=w3c"></a></c:if>
	                                    	<c:if test="${item.fStatus eq 2 }">已售卖</c:if>
	                                    	<c:if test="${item.fStatus eq 3 }">已撤销</c:if>
	                                    </td>
	                                    <td>
	                                    	<c:if test="${item.fOrdertype eq 0 }">出售</c:if>
	                                    	<c:if test="${item.fOrdertype eq 1 }">收购</c:if>
	                                    </td>
	                                    <td style="text-align: left;">
	                                    	<c:if test="${item.isCheck eq 0 }">未审核</c:if>
	                                    	<c:if test="${item.isCheck eq 1 }">审核成功</c:if>
	                                    	<c:if test="${item.isCheck eq 2 }">审核失败</c:if>
	                                    </td>
	                                    <td style="text-align: left;">
	                                    	<c:if test="${item.fStatus eq 0}">
		                                    	<a class="table_btn" onclick="undoAd('${item.fId }')">撤销</a>
		                                    	<a class="table_btn" href="/otc/editPublishAd.html?fid_=${item.fId }&types_=${item.fShortName }">修改</a>
	                                    	</c:if>
	                                    	<c:if test="${item.fStatus eq 1}">
		                                    	<a class="table_btn" onclick="undoAd('${item.fId }')">撤销</a>
		                                    	<a class="table_btn" href="/otc/editPublishAd.html?fid_=${item.fId }&types_=${item.fShortName }">修改</a>
	                                    	</c:if>
	                                    </td>
	                                </tr>
	                             </c:forEach>
                            </tbody>
                        </table>
                        <input type="hidden" id="pageIndex" value="1">
                        <input type="hidden" id="coin_id" name="coin_id" value="${coin_id}">
                        <div class="bk-moreBtn">
                            <button id="morebtn" class="btn btn-outline" type="button" style="display: none;"><i class="fa fa-angle-down fa-fw"></i>更多&gt;&gt;</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="/static/front/js/c2c/jquery-1.11.3.min.js"></script>
<%@include file="../comm/footer.jsp"%>
<script src="/static/front/js/c2c/jquery.SuperSlide.2.1.1.js"></script>
<script src="/static/front/js/comm/util.js"></script>
<script src="/static/front/js/otc/publishad.js"></script>
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