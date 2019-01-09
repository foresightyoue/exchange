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
		<base href="${basePath}" />
		<title>帐户资产</title>
		<link rel="stylesheet" type="text/css" href="/static/front/app/css/new_common.css"/>
		<link rel="stylesheet" href="/static/front/app/css/swiper-3.4.2.min.css" />
		<script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js"></script>
		<script type="text/javascript" src="/static/front/app/js/swiper-3.4.2.jquery.min.js"></script>
		<script type="text/javascript" src="${oss_url}/static/front/js/comm/util.js"></script>
		<script type="text/javascript" src="${oss_url}/static/front/js/language/language_cn.js"></script>
		<script type="text/javascript" src="${oss_url}/static/front/js/plugin/layer/layer.js"></script>
		<script type="text/javascript" src="/static/front/app/js/rem.js"></script>
        <script type="text/javascript" src="/static/front/app/js/vue.js"></script>
	</head>
	<style type="text/css">
.content>.main>.page{
	margin-top: 0.234rem;
	display: flex;
}
.content>.main>.page>.change{
	background-color: #999999;
    width: 100%;
	line-height: 1.867rem;
	text-align: center;
	font-size: 1.132rem;
	color: #ffffff;
}
.content>.main>.page>.change.active{
	background-color: #eba51d;
}
.content>.main>.page_contain{
	position: relative;
    width: 200%;
	left: 0%;
	display: flex;
}
.content>.main>.page_contain>.page{
    width: 50%;
}
.content>.main>.page_contain>.page>.details{
    margin-top: 0.332rem;
	font-size: 1.132rem;
	background-color: #FFFFFF;
}
.content>.main>.page_contain>.page>.details>.line{
    height: 1px;
	width: 23.332rem;
	background-color: #dfdfdf;
	margin: 0 auto;
}
.content>.main>.page_contain>.page>.details>.row{
    display: flex;
	padding: 0.5rem 1.601rem;
}
.content>.main>.page_contain>.page>.details>.row:first-child{
	padding-top: 1.300rem;
}
.content>.main>.page_contain>.page>.details>.row:last-child{
	padding-bottom: 1.300rem;
}
.content>.main>.page_contain>.page>.details>.row>.left{
    flex-grow: 1;
}
.content>.main>.page_contain>.page>.charts{
    margin-top: 0.300rem;
	background-color: #FFFFFF;
}
.content>.main>.page_contain>.page>.charts>.title{
	padding-top: 1.332rem;
	padding-left: 1.667rem;
	font-size: 1.132rem;
	color: #333333;
}
.content>.main>.page_contain>.page>.charts>.title>span{
	color: #ec1818;
}
.content>.main>.page_contain>.page>.charts>.data{
	padding: 0 1.535rem 1rem 1.667rem;
}
.content>.main>.page_contain>.page>.charts>.data>div{
	display: flex;
	font-size: 1rem;
	padding-top:0.390rem ;
}
.content>.main>.page_contain>.page>.charts>.data>.table_title{
	color: #999999;
	padding-bottom: 0.234rem;
}
.content>.main>.page_contain>.page>.charts>.data>div>div:nth-child(1){
	text-align: left;
	flex:0 0 20%;
}
.content>.main>.page_contain>.page>.charts>.data>div>div:nth-child(2){
	text-align: center;
	flex:0 0 40%;
}
.content>.main>.page_contain>.page>.charts>.data>div>div:nth-child(3){
	text-align: right;
	flex:0 0 40%;
}
.left_arrow {
	border-bottom: 0.156rem #FFFFFF solid;
	border-left: 0.156rem #FFFFFF solid;
	position: absolute;
	margin-top: 0.156rem;
	margin-left: 0.156rem;
	width: 1.074rem;
	height: 1.074rem;
	transform: rotate(45deg);
	-ms-transform: rotate(45deg);
	-moz-transform: rotate(45deg);
	-webkit-transform: rotate(45deg);
	-o-transform: rotate(45deg);
}
.orange{
	color: #f67003;
}
[v-cloak]{
	display:none;
}
</style>
	<body>
		<div class="content">
			<div class="head">
				<a href="/m/personal/assets.html?menuFlag=assets" class="left">
					<i class="left_arrow"></i>
				</a>
				<div class="text_center">每日收益</div>
			</div>
			<div id="data" class="main" v-cloak>
<!-- 			    <div class="page">
				    <div class="change active">RYH</div>
			    	<div class="change">RYB</div>
		    	</div> -->
				<div class="page_contain">
					<div class="page">
						<!-- RYH start -->
						<div class="details">
							<div class="row">
								<div class="left">
									我的RYH
								</div>
								<div class="right orange">
									{{Profit.myCoin?Profit.myCoin:0 | toFixed}}
								</div>
							</div>
							<div class="line"></div>
							<div class="row">
								<div class="left">
									昨日收益
								</div>
								<div class="right">
									{{Profit.totalProfit?Profit.totalProfit:0 | toFixed}}
								</div>
							</div>
							<div class="row">
								<div class="left">
									持币分红
								</div>
								<div class="right">
									{{Profit.holdMoneyProfit?Profit.holdMoneyProfit:0 | toFixed}}
								</div>
							</div>
							<div class="row">
								<div class="left">
									推广收益
								</div>
								<div class="right">
									{{Profit.promotingProfit?Profit.promotingProfit:0 | toFixed}}
								</div>
							</div>
							<div class="line"></div>
							<div class="row">
								<div class="left">
									最佳持币量
								</div>
								<div class="right">
									{{Profit.bestHoldMoney?Profit.bestHoldMoney:0 | toFixed}}
								</div>
							</div>
						</div>
						<div class="charts">
							<div class="title">收益排行榜&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span></span></div>
							<div class="data">
								<div class="table_title">
									<div>排名</div>
									<div>用户昵称</div>
									<div>收益</div>
								</div>
								<div v-for="(item,i) in ranking" class="list">
									<div>{{item.ranking}}</div>
                                    <div>{{item.userNikeName}}</div>
                                    <div>{{item.profit}}</div>
								</div>
							</div>
						</div>
						<!-- RYH end -->
					</div>
					<!-- <div class="page">
						RYB start
						<div class="details">
							<div class="row">
								<div class="left">
									我的RYB
								</div>
								<div class="right orange">
									{{Profit1.myCoin}}
								</div>
							</div>
							<div class="line"></div>
							<div class="row">
								<div class="left">
									累计收益
								</div>
								<div class="right">
									{{Profit1.totalProfit ? Profit1.totalProfit:0}}
								</div>
							</div>
							<div class="row">
								<div class="left">
									持币分红
								</div>
								<div class="right">
									{{Profit1.holdMoneyProfit?Profit1.holdMoneyProfit:0}}
								</div>
							</div>
							<div class="row">
								<div class="left">
									推广收益
								</div>
								<div class="right">
									{{Profit1.promotingProfit?Profit1.promotingProfit:0}}
								</div>
							</div>
							<div class="line"></div>
							<div class="row">
								<div class="left">
									最佳持币量
								</div>
								<div class="right">
									{{Profit1.bestHoldMoney}}
								</div>
							</div>
						</div>
						<div class="charts">
							<div class="title">收益排行榜&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span></span></div>
							<div class="data">
								<div class="table_title">
									<div>名称</div>
									<div>用户昵称</div>
									<div>收益</div>
								</div>
								<div v-for="(item,i) in ranking1" class="list">
									<div>{{item.ranking}}</div>
                                    <div>{{item.userNikeName}}</div>
                                    <div>{{item.profit}}</div>
								</div>
							</div>
						</div>
						RYB end
					</div> -->
				</div>
				<div class="obligate"></div>
			</div>
			<div class="menu">
			</div>
		</div>
	</body>
    <script type="text/javascript">
    $(function(){
        var list = new Vue({
            el:"#data",
            data: {
                ranking: [],
                Profit: [],
                ranking1: [],
                Profit1: []
            },
            methods : {
                
            },
           filters:{
        	   toFixed:function(value){
        		   return util.numFormat(value,6);
        	   }
           }
        });
        $.post("/api/trading/getjson.html",{"type":"RYH","uri":"/api/app/everyDayProfit"},function(data){
            console.log(data);
            if(data.data){
                list.Profit = data.data.profit;
                list.ranking = data.data.rankings;
            }

        });
        /* $.post("/api/trading/getjson.html",{"type":"RYB","uri":"/api/app/everyDayProfit"},function(data){
            console.log(data);
           if(data.data){
            list.Profit1 = data.data.profit;
            list.ranking1 = data.data.rankings;
           }
        }); */
        /* $.post("/api/trading/getjson.html",{"ryhCoin":"1","srcWalletId":"RY881007933:RYHLock:23977405fae548d6b2e887034ab6fb4a","targetId":"RY002","uri":"/api/app/smallWalletTransfer"},function(data){
            console.log(data);
        }); */
    })
    </script>
	<script>
		$(function () {
            var msg = '${msg}';
            if(msg != null && msg != ''){
            	layer.msg(msg);
            }
            var allow=true;
			$(".content>.main>.page>.change").click(function (e) {
                if(allow){
                    allow=false;
                    $(".content>.main>.page_contain").css("left",$(".content>.main>.page>.change.active").index()*-100+"%");
                    $(this).addClass("active").siblings().removeClass("active");
                    $(".content>.main>.page_contain>.page").show();
                    $(".content>.main>.page_contain").animate({left:$(this).index()*-100+"%"},500,function (e) {
                    	$(".content>.main>.page_contain>.page").eq($(".content>.main>.page>.change.active").index()).siblings().hide();
                    	$(".content>.main>.page_contain").css("left","0%");
                        allow=true;
                    });
                }
			});
		});
	</script>
</html>