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
	<title>锁仓钱包</title>
	<!-- <link rel="stylesheet" href="/static/front/app/css/css.css" />
    <link rel="stylesheet" href="/static/front/app/css/style.css" /> -->
	<link rel="stylesheet" href="/static/front/app/css/swiper-3.4.2.min.css" />
	<script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js"></script>
	<script type="text/javascript" src="/static/front/app/js/swiper-3.4.2.jquery.min.js"></script>
	<script type="text/javascript" src="/static/front/js/comm/clipboard.min.js"></script>
	<script type="text/javascript" src="/static/front/js/layer/layer.js"></script>
	<script type="text/javascript" src="/static/front/app/js/rem1.js"></script>
	<script type="text/javascript" src="/static/front/app/js/vue.js"></script>
	<style type="text/css">
		* {
			margin: 0;
			padding: 0;
		}

		html,
		body {
			background-color: #EEEEEE;
			height: 100%;
		}

		ul,
		ol,
		dl,
		dd,
		dt {
			list-style: none;
			margin: 0;
			padding: 0;
		}

		a {
			color: #000;
			text-decoration: none;
		}

		.content {
			display: flex;
			flex-direction: column;
			width: 100%;
			height: 100%;
		}

		.content .head {
			padding: 0.265rem 0rem 0.256rem 0rem;
			background-color: #333333;
			font-size: 0.29rem;
			color: #FFFFFF;
		}

		.content .head .left {
			padding: 0rem 0rem 0rem 0.171rem;
			position: absolute;
			color: #FFFFFF;
		}

		.Personal-title {
			text-align: center;
		}

		.left_arrow {
			border-bottom: 0.04rem #FFFFFF solid;
			border-left: 0.04rem #FFFFFF solid;
			display: block;
			margin-top: 0.04rem;
			margin-left: 0.04rem;
			width: 0.275rem;
			height: 0.275rem;
			transform: rotate(45deg);
			-ms-transform: rotate(45deg);
			-moz-transform: rotate(45deg);
			-webkit-transform: rotate(45deg);
			-o-transform: rotate(45deg);
		}

		.content>.main {
			flex-grow: 1;
			overflow-y: scroll;
		}

		.content>.main>.has {
			display: none;
			line-height: 0.683rem;
			font-size: 0.29rem;
			color: #f67003;
			height: 0.683rem;
			background-color: #ffffff;
			box-shadow: 0.001rem -0.009rem 0.043rem 0rem rgba(153, 153, 153, 0.56);
			text-align: center;
		}

		.content>.main>.trisection {
			display: flex;
			margin-top: 0.085rem;
			background-color: #ffffff;
			font-size: 0.256rem;
			color: #333333;
		}

		.content>.main>.trisection:nth-child(2) {
			height: 1.109rem;
		}

		.content>.main>.trisection:nth-child(3) {
			height: 2.338rem;
			margin-bottom: 0.15rem;
		}

		.content>.main>.trisection>.title {
			width: 1.229rem;
			display: flex;
			justify-content: center;
			align-items: center;
			border-right: 1px solid #dfdfdf;
		}

		.content>.main>.trisection>.part {
			flex-grow: 1;
			width: 0;
			display: flex;
			flex-direction: column;
			text-align: center;
			justify-content: space-between;
		}

		.content>.main>.trisection>.part:before,
		.content>.main>.trisection>.part:after {
			content: "";
			display: block;
		}

		.content>.main>.trisection>.part>.line {
			height: 1px;
			background-color: #dfdfdf;
		}

		.content>.main>.block {
			background-color: #fff4e0;
			border-radius: 0.2rem;
			margin: 0.1rem 0.15rem 0.15rem;
			font-size: 0.205rem;
			color: #333333;
			overflow: hidden;
		}

		.content>.main>.block:first-child {
			margin-top: 0.171rem;
		}

		.content>.main>.block>.row {
			height: 0.879rem;
			display: flex;
			flex-direction: column;
			padding-left: 0.077rem;
			padding-right: 0.077rem;
			position: relative;
			background: #fff;
			border-bottom: 1px solid #efefef;
		}

		.content>.main>.block>.row.more {
			height: auto
		}

		.content>.main>.block>.row.important {
			background-color: #fff;
			color: #333;
			border-radius: 0.068rem;
			padding: 0.2rem;
		}

		.content>.main>.block>.row.button:after {
			content: "详情";
			background-color: #EBA51D;
			padding: 0.068rem 0.154rem 0.06rem 0.162rem;
			right: 0.256rem;
			top: 0.205rem;
			position: absolute;
			border-radius: 0.085rem;
			flex-grow: 1;
			text-align: right;
			color: #FEFEFE;
			display: block;
		}

		.content>.main>.block>.row>.title {
			font-size: 0.205rem;
			display: flex;
		}

		.content>.main>.block>.row>.title>.day {
			flex-grow: 1;
			text-indent: 0.3rem;
			color: #EBA51D;
			line-height: 0.5rem;
		}

		.content>.main>.block>.row>.title>.split {
			background-color: #eba51d;
			border-radius: 0.085rem;
			width: 1.007rem;
			color: #999999;
			line-height: 0.333rem;
			font-size: 0.205rem;
			color: #ffffff;
			text-align: center;
			margin-right: 0.247rem;
		}

		.content>.main>.block>.row>.title>.Receive {
			background-color: #f67003;
			border-radius: 0.085rem;
			width: 1.007rem;
			color: #999999;
			line-height: 0.333rem;
			font-size: 0.205rem;
			color: #ffffff;
			text-align: center;
			margin-right: 0.222rem;
		}

		.content>.main>.block>.row>.space {
			flex-grow: 1;
		}

		.content>.main>.block>.row>.composition {
			display: flex;
		}

		.content>.main>.block>.row>.composition>div {
			width: 0;
			flex: 0 0 50%;
			height: 0.4rem;
			display: flex;
			align-items: center;
			color: #999;
		}

		.content>.main>.block>.row>.composition>div>span {
			margin-right: 0.2rem;
			flex: 0 0 1.2rem;
			text-align: right;
			color: #333;
		}

		.content>.main>.block>.row>.important_text {
			color: #eba51d;
			display: flex;
		}

		.content>.main>.block>.row>.important_text>.normal {
			font-size: 0.205rem;
			color: #ffffff;
			flex-grow: 1;
			text-align: right;
		}

		.text_left {
			text-align: left;
		}

		.text_center {
			text-align: center;
		}

		.text_right {
			text-align: right;
		}

		.btnBox {
			display: flex;
			height: 0.5rem;
			align-items: center;
			width: 100%;
			padding: 0.2rem 0;
			background: #fff;
			justify-content: space-evenly;
		}

		.btnBox div {
			height: 0.5rem;
			line-height: 0.5rem;
			color: #fff;
			background: #CAC8C8;
			margin: 0 0.3rem;
			padding: 0 0.3rem;
			border-radius: 0.068rem;
			width: 20%;
			text-align: center;
		}

		.btnBox .splActive {
			background: #EDC31A;
		}

		.btnBox .rcvActive {
			background: #FE9917;
		}

		.btnBox .spledActive {
			background: #F67003;
		}

		[v-cloak] {
			display: none;
		}

		.copyBT {
			flex-grow: 1;
			align-items: center;
			/* background: #EBA51D; */
			height: 0.5rem;
			line-height: 0.5rem;
			padding: 0 0.2rem;
			border-radius: 0.068rem;
			text-align: center;
			color: #EBA51D
		}
	</style>
</head>

<body>
	<div class="content">
		<div class="head">
			<a href="javascript:history.back();" class="left">
				<i class="left_arrow"></i>
			</a>
			<div class="Personal-title">锁仓钱包</div>
		</div>
		<div class="main" id="data" v-cloak>
			<div class="has">最佳持币量：55</div>
			<div class="trisection">
				<div class="title">自由区</div>
				<div class="part">
					<div class="text">RYH</div>
					<!-- <div class="line"></div> -->
					<!-- <div class="text">昨日收益</div> -->
				</div>
				<div class="part">
					<div id="ryhFlowCoin" class="text">0.000000</div>
					<!-- <div class="line"></div> -->
					<!-- <div id="ryhFlowHoldCoin" class="text">0.000000</div> -->
				</div>
			</div>
			<div class="trisection">
				<div class="title">锁仓区</div>
				<div class="part">
					<div class="text">锁仓余额</div>
					<div class="line"></div>
					<div class="text">昨日释放总数</div>
					<div class="line"></div>
					<!-- <div class="text">昨日总收益</div>
					<div class="line"></div> -->
					<div class="text">未领取总数</div>
				</div>
				<div class="part">
					<div id="ryhLockCoin" class="text">0.000000</div>
					<div class="line"></div>
					<div id="ryhLockProfit" class="text">0.000000</div>
					<div class="line"></div>
					<!-- <div id="ryhLockHoldMoneyCount" class="text">0.000000</div>
					<div class="line"></div> -->
					<div id="ryhLockReleaseCount" class="text">0.000000</div>
				</div>
			</div>
			<div v-for="(item,index) in suo" class="block">
				<div class="row">
					<div class="space"></div>
					<div class="title">
						<span class="day">钱包ID: {{item.walletId }}</span>
						<div class="copyBT" v-on:click="copy(index)">复制</div>
					</div>
					<div class="space"></div>
				</div>
				<a v-bind:href="href(item)" class="row more important">
					<div class="composition">
						<div class=""><span>锁仓金额:</span>{{item.currencyTotal?item.currencyTotal:0 | toFixed}}</div>
						<div class=""><span>子钱包数:</span>{{item.smallWalletNum}}</div>
					</div>
					<div class="composition">
						<div class=""><span>已&nbsp;&nbsp;释&nbsp;&nbsp;放:</span>{{item.releaseTotalDay}} / {{item.releaseCountDay}}</div>
						<div class=""><span>累积释放:</span>{{item.releaseCurrency?item.releaseCurrency:0 | toFixed}}</div>

					</div>
					<div class="composition">
						<div class=""><span>释放时间:</span>{{item.startRelaseDate}}</div>
						<div class=""><span>到期时间:</span>{{item.endRelaseDate}}</div>
					</div>
					<div class="composition">
						<div class=""><span>未领取数:</span>{{item.releaseCount?item.releaseCount:0 | toFixed}}</div>
					</div>
				</a>
				<div class='btnBox'>
					<!-- <div v-if="item.currencyTotal>=100&&item.smallWalletNum<=0" :class="[item.currencyTotal>=100&&item.smallWalletNum<=0?splActive:'']"
					 v-on:click="cf(index)" class="split">拆分</div> -->
					<div :class="[item.releaseCount>0?rcvActive:'']" v-on:click="lq(index)" class="Receive">一键领取</div>
					<div v-if="item.smallWalletNum<=0" v-on:click="transfer(index)" class="splActive">转账</div>
				</div>
			</div>
			<!-- <div class="block">
  			<div class="row">
  				<div class="space"></div>
  				<div class="title">精品推荐</div>
  				<div class="space"></div>
  			</div>
	        <a v-for="item in items" class="row important" :href="'/m/ryb/scproduct.html?fid='+item.tid">
	          <div class="space"></div>
	          <div>{{item.projectName}}</div>
	          <div class="space"></div>
	          <div class="important_text">目标金额 1000.00&nbsp;&nbsp;{{item.smallCurrency}}起购<div class="normal">已完成45%</div></div>
	          <div class="space"></div>
	        </a>
  		</div> -->
			<div style="height: 0.8rem"></div>
		</div>
		<div class="menu"></div>
	</div>
	<script type="text/javascript" src="/static/front/js/comm/util.js"></script>
	<script type="text/javascript" src="/static/front/js/comm/comm.js"></script>
	<script type="text/javascript">
		$(function () {
			var msg = '${msg}';
			if (msg != null && msg != '') {
				layer.msg(msg);
			}
			var lock_depot_data = new Vue({
				el: '#data',
				data: {
					items: [],
					suo: [],
					splActive: 'splActive',
					rcvActive: 'rcvActive',
					spledActive: 'spledActive'
				},
				methods: {
					cf: function (index) {
						if (this.suo[index].currencyTotal >= 100 && this.suo[index].smallWalletNum > 0) {
							return;
						}
						window.location.href = "${oss_url}/m/trade/CFwellet.html?Wellettype=big&walletId=" + this.suo[index].walletId;
					},
					transfer: function (index) {
						window.location.href = "${oss_url}/m/ryb/RYHTranster.html?walletId=" + this.suo[index].walletId;
					},
					copy: function (index) {
						var id = this.suo[index].walletId;
						var clipboard = new ClipboardJS('.copyBT', {
							text: function (trigger) {
								return id; //复制内容
							}
						})
						clipboard.on('success', function (e) {
							console.info('Text:', e.text); //复制文本
							e.clearSelection(); //取消选择节点
							parent.layer.msg("复制成功");
						});
						clipboard.on('error', function (e) {
							console.error('Trigger:', e.trigger);
							parent.layer.msg("复制失败");
						});
						// const input = document.createElement('input');
						// document.body.appendChild(input);
						// input.setAttribute('value', id);
						// input.select();
						// if (document.execCommand('copy')) {
						// 	document.execCommand('copy');
						// 	parent.layer.msg("复制成功");
						// }
						// document.body.removeChild(input);

					},
					lq: function (index) {
						if (this.suo[index].releaseCount <= 0) {
							return;
						}
						$.post("/api/trading/getjson2.html", {
							"walletId": this.suo[index].walletId,
							"uri": "/api/app/oneKeyReceive"
						}, function (data) {
							if (data.status == 200) {
								layer.msg('领取成功', {
									shade: [0.1, '#000'],
									shadeClose: true,
									time: 2000,
									end: function () {
										location.reload();
									}
								});
							} else {
								layer.msg(data.msg, {
									shade: [0.1, '#000'],
									shadeClose: true,
									time: 2000
								});
							}
						});
					},
					href: function (item) {
						if (item.smallWalletNum > 0) {
							return '/m/trade/Zcwellet.html?walletId=' + item.walletId
						} else {
							return '${oss_url}/m/ryb/details.html?type=big&welletId=' + item.walletId
						}
					}
				},
				filters: {
					toFixed: function (value) {
						return util.numFormat(value)
					}
				}
			});
			$.post("/api/trading/getjson2.html", {
				"uri": "/api/app/getLockProduct"
			}, function (data) {
				lock_depot_data.items = data.data;
			});
			$.post("/api/trading/getjson2.html", {
				"uri": "/api/app/userLockProject"
			}, function (data) {
				lock_depot_data.suo = data.data;
			});
			$.post("/api/trading/getjson2.html", {
				"uri": "/api/app/bestHoldMoney"
			}, function (data) {
				$(".has").html("最佳持币量：" + parseFloat(data.data.bestHoldMoney).toFixed(2));
			});
			$.post("/api/trading/getjson2.html", {
				"uri": "/api/app/lockWalletCoin"
			}, function (data) {
				$("#ryhFlowCoin").html(util.numFormat(data.data.ryhFlowCoin, 6));
				$("#ryhFlowHoldCoin").html(util.numFormat(data.data.ryhFlowHoldCoin, 6));
				$("#ryhLockCoin").html(util.numFormat(data.data.ryhLockCoin, 6));
				$("#ryhLockProfit").html(util.numFormat(data.data.ryhLockProfit, 6));
				$("#ryhLockHoldMoneyCount").html(util.numFormat(data.data.ryhLockHoldMoneyCount, 6));
				$("#ryhLockReleaseCount").html(util.numFormat(data.data.ryhLockReleaseCount, 6));
			});

		});
	</script>
</body>

</html>