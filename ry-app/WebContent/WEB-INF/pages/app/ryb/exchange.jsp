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
	<title>CNY兑换RYB</title>
	<link rel="stylesheet" type="text/css" href="/static/front/app/css/new_common.css" />
	<link rel="stylesheet" href="/static/front/app/css/swiper-3.4.2.min.css" />
	<script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js"></script>
	<script type="text/javascript" src="/static/front/app/js/swiper-3.4.2.jquery.min.js"></script>
	<script type="text/javascript" src="/static/front/app/js/bankCardAttribution.js"></script>
	<script type="text/javascript" src="/static/front/app/js/rem.js"></script>
	<style>
		.content>.main>.income {
	line-height: 2.734rem;
	background-color: #ffffff;
	padding-left: 0.632rem;
	font-size: 1rem;
	color: #333333;
	margin-top: 0.601rem;
}

.content>.main>.form {
	display: flex;
	height: 10.332rem;
	padding: 0.781rem 0;
	margin-top: 0.699rem;
	flex-direction: row;
	font-size: 1.199rem;
	color: #333;
	background-color: #fff;
}

.content>.main>.form>.row {
	display: flex;
	padding: 0 1.953rem;
	align-items: flex-start;
	justify-content: space-around;
	flex:0 0 100%;
	box-sizing:border-box;
	transition:all 1s;
	padding-top:10%;
}
.content>.main>.form>.row>div>span{
	font-size:0.8rem;
}
.content>.main>.form>.row>div>span>.redTxt{
	color:#EBA51D ;
}
.content>.main>.form>.row>label {
	text-align: right;
	margin-right:1rem;
	margin-top:0.5rem;
}

.content>.main>.form>.row input {
    padding: 0.832rem 0.464rem;
    width: 80%;
    line-height: 0.832rem;
	font-size: 0.832rem;
    border:1px solid #afafaf;
}


.content>.main>.button{
    width: 20rem;
	line-height: 2.800rem;
	background-color: #eba51d;
	border-radius: 0.398rem;
    margin: 2.035rem auto;
    font-size: 1rem;
	color: #ffffff;
    text-align: center;
}
.content>.main>.page{
	margin-top: 1rem;
	display: flex;
	height:2rem;
}
.content>.main>.page>.change{
	background-color: #999999;
    width: 100%;
	line-height: 2rem;
	text-align: center;
	font-size: 0.69rem;
	color: #ffffff;
}
.content>.main>.page>.change.active{
	background:#eba51d;
}
.flex1{
    padding:1px;
    flex:1;
}
.pswdMask{
	position:fixed;
	height:100vh;
	width:100vw;
	left:0;
	top:0;
	z-index:999;
	background:rgba(0,0,0,0.1);
	display:none;
}
.pswdMask>.positionBox{
	position:absolute;
	top:50%;
	left:50%;
	transform:translate(-50%,-50%);
	border-radius:0.5rem;
	background:#fff;
    width:85%;
}
.pswdMask>.positionBox>.alertTitle{
    text-align: center;
    font-size: 1.026rem;
    line-height: 2.56rem;
    border-bottom: 1px solid #efefef;
    color:#666;
}
.pswdMask>.positionBox>input{
    display: block;
    outline: none;
    border: 1px solid #efefef;
    border-radius: 0.45rem;
    margin: 1.5rem auto;
    width: 55%;
    height: 2rem;
    padding: 0.2rem 0.5rem;
    letter-spacing: 0.3rem;
    font-size: 0.8rem;
}
.pswdMask>.positionBox>.pswdBox{
	height:3.5rem;
	display:flex;
	border-top:1px solid #efefef;
}
.pswdMask>.positionBox>.pswdBox>span{
	flex:1;
	line-height:3.5rem;
	text-align:Center;
	border-right:1px solid #efefef;
    color:#999;
}
.pswdMask>.positionBox>.pswdBox>span:last-child{
	border:none;
    color:#eba51d;
}
</style>
</head>

<body>
	<div class="content">
		<div class="head">
			<a href="/m/ryb/index.html" class="left">
				<i class="left_arrow"></i>
			</a>
			<div class="text_center">CNY兑换RYB</div>
		</div>
		<div class="main">
			<div class="income">交易区CNY数量：&nbsp;&nbsp;&nbsp;&nbsp;<font id="tradeRYH">0.000000</font>
			</div>

			<!-- <div class="income">收益区CNY数量：&nbsp;&nbsp;&nbsp;&nbsp;<font id="syRYH">0.000000</font> -->
			<!-- </div> -->

			<div class="page">
				<div class="change active">交易区</div>
				<!-- <div class="change">收益区</div> -->
			</div>
			<div class="form">
				<div class="row">
					<label for="ryh_num">CNY数量</label>
					<div class='flex1'>
						<input type="number" name="ryh_num" id="ryh_num" value="" />
						<br>
						<!-- <span>CNY当前的价格所得的<span id='ryb' class='redTxt'>0.0</span> RYB</span> -->
						<span>CNY兑换RYB 兑换比例 1:1</span>  
						<br>
						<span style="color :#ec5d5d">尊敬的用户，请尽快将CNY兑换为RYB，兑换截止时间以公告信息为准。</span>  
					</div>
				</div>
				<div class="row">
					<label for="ryb">CNY数量</label>
					<div class='flex1'>
						<input type="number" name="ryb" id="ryb_num" value="" disabled />
						<br>
						<span >根据CNY当前的价格所得的锁仓<span id='ryh' class='redTxt'>0.0</span> RYB</span>
					</div>

				</div>
			</div>
			<div class="button">确定</div>
			<div class="obligate"></div>
		</div>
		<div class="pswdMask">
			<div class="positionBox">
				<div class='alertTitle'>请输入交易密码</div>
				<input type="password" onInput='value=value.replace(/[^\d\w]/g,"")'>
				<div class='pswdBox'>
					<span id='tradeCancelBtn'>取消</span><span id='tradeConfirmBtn'>确定</span>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="/static/front/js/comm/util.js"></script>
	<script type="text/javascript" src="${oss_url}/static/front/js/plugin/layer/layer.js"></script>
	<script type="text/javascript">
		$(function () {
			$('.form .row').find('input').val("");
			var tid = '${tid}';
			$.post("/api/trading/getjson2.html", {
				"tid": tid,
				"uri": "/api/app/getCoinTotal"
			}, function (data) {
				$("#syRYH").html(util.numFormat(data.data.ryhFlow, 6));
				$("#syRYB").html(util.numFormat(data.data.rybFlow, 6));
			});
			$.post("/api/trading/getjson3.html", {
				"uri": "/api/trading/queryBlance.html"
			}, function (data) {
				$("#tradeRYB").html(util.numFormat(data.data.RYB.fTotal, 6));
				$("#tradeRYH").html(util.numFormat(data.data.CNY.fTotal, 6));
			});
			$('.change').click(function () {
				$(this).addClass('active').siblings().removeClass('active');
				var index = $(this).index();
				if (index == 0) {
					$('.form .row').eq(0).css('margin-left', "0%").find('input').attr('disabled', false).focus();
					$('.form .row').eq(1).find('input').val("");
					$('.form .row').eq(1).find('input').attr('disabled', 'disabled');
				} else {
					$('.form .row').eq(0).css('margin-left', '-100%').find('input').attr('disabled', 'disabled');
					$('.form .row').eq(0).find('input').val("");
					$('.form .row').eq(1).find('input').attr('disabled', false);
				}
			})
			var isRybUpdataed = false;
			var isRyhUpdataed = false;
			$("#ryh_num").bind('input propertychange', function (e) {
				var aa = util.numFormat($(this).val(), 6);
				var num = $(this).val();
				if (num) {
					//此时兑换比例为1:1 也可通过接口获得
					$("#ryb").text(num);
					isRybUpdataed = true;
					// $.post("/api/trading/getjson2.html", {
					// 	"RYH": num,
					// 	"uri": "/api/app/queryRealPrice"
					// }, function (data) {
					// 	$("#ryb").text(data.data.RYB);
					// 	isRybUpdataed = true;
					// });

				}
			}).on('input', function (event) {
				// isRybUpdataed = false;
				// isRyhUpdataed = false;
				var str = $(this).val();
				if (str.length == 0) {
					$(this).val('');
					layer.msg('数字为空或者格式不正确，请输入！');
					return
				}
				var re = /([0-9]+\.[0-9]{6})[0-9]*/;
				var aNew = str.replace(re, "$1");
				$(this).val(aNew);

			})
			// $("#ryb_num").bind('input propertychange', function (e) {
			// 	var num = $(this).val();
			// 	if (num) {
			// 		$.post("/api/trading/getjson2.html", {
			// 			"RYH": num,
			// 			"uri": "/api/app/queryRealPrice"
			// 		}, function (data) {
			// 			$("#ryh").text(data.data.RYB);
			// 			isRyhUpdataed = true;
			// 		});
			// 	}
			// }).on('input', function (event) {
			// 	isRybUpdataed = false;
			// 	isRyhUpdataed = false;
			// 	var str = $(this).val();
			// 	if (str.length == 0) {
			// 		$(this).val('');
			// 		layer.msg('数字为空或者格式不正确，请输入！');
			// 		return
			// 	}
			// 	str = parseFloat(str).toString();
			// 	var arr = str.split('.');
			// 	$(this).val(arr[1] ? [arr[0], arr[1].slice(0, 6)].join('.') : arr[0])

			// });
			//监听提交确定按钮
			$(".button").click(function (e) {
				var ryh = $("#ryh_num").val();
				var hadryh =  parseFloat($('#tradeRYH').html())


				var ryb = $("#ryb").html();
				var ryb1 = $("#ryh").html();
				var ryh1 = $("#ryb_num").val();
				
				if (ryh > 0 && !isRybUpdataed) {
					layer.msg('尚未查询到RYH数量！')
					return
				}

				if(ryh > hadryh || hadryh == 0){
					layer.msg('余额不足！')
					return
				}


				if (ryh.replace(/(^\s*)|(\s*$)/g, "") != "") {
					if (ryh <= 0) {
						layer.msg("数量必须大于0");
						return;
					} else {
						layer.closeAll();
						$('.pswdMask').show();
					}
				} else if (ryb1.replace(/(^\s*)|(\s*$)/g, "") != "") {
					if (ryb1 <= 0) {
						layer.msg("数量必须大于0");
						return;
					} else {
						layer.closeAll();
						$('.pswdMask').show();
					}
				} else {
					layer.msg("请输入数量");
					return;
				}
			});

			//点击遮罩关闭自己
			$('#tradeCancelBtn').on('click',function(){
				$('.pswdMask').hide().find('input').val('');
			})
			//监听 弹窗取消按钮
			// $('body').on('click','.pswdMask',function(event){
			// 	if(event.currentTarget==event.target){
			// 		$(this).hide().find('input').val('');
			// 	}
			// })
			//监听 密码确定按钮
			var isComplete = true
			$('.pswdMask #tradeConfirmBtn').off("click").one("click", pay);

			function pay() {
				var index = layer.load(1, {
					shade: [0.1, '#000']
				});
				var ryh = $("#ryh_num").val();
				var ryb = $("#ryb").html();
				var ryb1 = $("#ryh").html();
				var ryh1 = $("#ryb_num").val();
				var pswd = $('.pswdMask').find('input').val(); //取得交易密码
				if (pswd.length < 6) {
					layer.closeAll();
					layer.msg('密码格式有误，请重新输入!');
					$('.pswdMask #tradeConfirmBtn').off("click").one("click", pay);
					return
				}
				if (ryh.replace(/(^\s*)|(\s*$)/g, "") != "") {
					if (ryh <= 0) {
						layer.closeAll();
						layer.msg("数量必须大于0");
						$('.pswdMask #tradeConfirmBtn').off("click").one("click", pay);
						return;
					} else {
						$.post("/api/trading/getjson2.html", {
							"CNY": ryh,
							"RYB": ryb,
							"pswd": pswd,
							"type": "1",
							"transferType": "TTR",
							"uri": "/api/app/cnyExchangeRyb"
						}, function (data) {
							layer.closeAll();
							if (data.status == 200) {
								layer.msg('兑换成功', {
									shade: [0.1, '#000'],
									shadeClose: true,
									time: 1000,
									end: function () {
										location.reload();
									}
								});
							} else {
								layer.msg(data.msg, {
									shade: [0.1, '#000'],
									shadeClose: true,
									time: 1000
								});
								$('.pswdMask #tradeConfirmBtn').off("click").one("click", pay);
							}
							return;
						});
					}
				} else if (ryb1.replace(/(^\s*)|(\s*$)/g, "") != "") {
					if (ryb1 <= 0) {
						layer.closeAll();
						layer.msg("数量必须大于0");
						$('.pswdMask #tradeConfirmBtn').off("click").one("click", pay);
						return;
					} else {

						$.post("/api/trading/getjson2.html", {
							"CNY": ryh1,
							"RYB": ryb1,
							"pswd": pswd,
							"type": "1",
							"transferType": "RTR",
							"uri": "/api/app/cnyExchangeRyb"
						}, function (data) {
							layer.closeAll();
							if (data.status == 200) {
								layer.msg('兑换成功', {
									shade: [0.1, '#000'],
									shadeClose: true,
									time: 1000,
									end: function () {
										location.reload();
									}
								});
							} else {
								layer.msg(data.msg, {
									shade: [0.1, '#000'],
									shadeClose: true,
									time: 1000
								});
								$('.pswdMask #tradeConfirmBtn').off("click").one("click", pay);
							}
						});
					}
				} else {
					layer.closeAll();
					layer.msg("请输入数量");
					$('.pswdMask #tradeConfirmBtn').off("click").one("click", pay);
					return;
				}
			}
		});
	</script>
</body>

</html>