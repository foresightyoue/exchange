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
	<title>收益区转交易区</title>
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
.content>.main>.page{
	margin-top: 0.634rem;
	display: flex;
}
.content>.main>.page>.change{
	background-color: #999999;
    width: 100%;
	line-height: 2rem;
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
.content>.main>.page_contain>.page>.form {
	display: flex;
	justify-content: space-around;
	height: 6.734rem;
	padding: 0.781rem 0;
	margin-top: 0.699rem;
	flex-direction: column;
	font-size: 1.199rem;
	color: #333;
	background-color: #fff;
}

.content>.main>.page_contain>.page>.form>.row {
	display: flex;
	padding: 0 1.953rem;
	align-items: center;
	justify-content: space-around;
}

.content>.main>.page_contain>.page>.form>.row>label {
	flex-basis: 30%;
	text-align: right;
}

.content>.main>.page_contain>.page>.form>.row>input {
    padding: 0.832rem 0.464rem;
    width: 50%;
    line-height: 0.832rem;
	font-size: 0.832rem;
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
			<div class="text_center">收益区转交易区</div>
		</div>
		<div class="main">
			<div class="income">收益区RYH数量：&nbsp;&nbsp;&nbsp;&nbsp;<span id="ryhFlow">000.000000</span></div>
			<div class="income">收益区RYB数量：&nbsp;&nbsp;&nbsp;&nbsp;<span id="rybFlow">000.000000</span></div>
			<div class="page">
				<div class="change active">RYH</div>
				<div class="change">RYB</div>
			</div>
			<div class="page_contain">
				<div class="page">
					<!-- RYH start -->
					<div class="form">
						<div class="row">
							<label for="ryh_num">RYH数量</label>
							<input type="number" name="ryh_num" id="ryh_num" value="" placeholder="输入收益区RYH数量" />
						</div>
					</div>
					<!-- RYH end -->
				</div>
				<div class="page">
					<!-- RYB start -->
					<div class="form">
						<div class="row">
							<label for="ryb_num">RYB数量</label>
							<input type="number" name="ryb_num" id="ryb_num" value="" placeholder="输入收益区RYB数量" />
						</div>
					</div>
					<!-- RYB end -->
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
	<script type="text/javascript" src="${oss_url}/static/front/js/comm/util.js"></script>
	<script type="text/javascript" src="/static/front/app/js/index.js"></script>
	<script type="text/javascript" src="${oss_url}/static/front/js/plugin/layer/layer.js"></script>
	<script>
		$(function () {
			var msg = '${msg}';
			if (msg != null && msg != '') {
				layer.msg(msg);
			}
			var tid = '${tid}';
			$.post("/api/trading/getjson2.html", {
				"tid": tid,
				"uri": "/api/app/getCoinTotal"
			}, function (data) {
				$("#ryhFlow").html(util.numFormat(data.data.ryhFlow, 6));
				$("#rybFlow").html(util.numFormat(data.data.rybFlow, 6));
			});
			var allow = true;
			$(".content>.main>.page>.change").click(function (e) {
				if (allow) {
					allow = false;
					$(".content>.main>.page_contain").css("left", $(".content>.main>.page>.change.active").index() * -100 + "%");
					$(this).addClass("active").siblings().removeClass("active");
					$(".content>.main>.page_contain>.page").show();
					$(".content>.main>.page_contain").animate({
						left: $(this).index() * -100 + "%"
					}, 500, function (e) {
						$(".content>.main>.page_contain>.page").eq($(".content>.main>.page>.change.active").index()).siblings().hide();
						$(".content>.main>.page_contain").css("left", "0%");
						allow = true;
					});
				}
			});
			$('#ryh_num,#ryb_num').on('input', function () {
				var str = $(this).val();
				if (str.length == 0) {
					layer.msg('数字为空或者格式不正确，请输入！');
					$(this).val('');
					return
				}
				var re = /([0-9]+\.[0-9]{6})[0-9]*/;
				var aNew = str.replace(re, "$1");
				$(this).val(aNew);
			})
			$(".button").click(function (e) {
				var index = $(".change.active").index();
				var coinType = "RYH";
				var num = $("#ryh_num").val();
				if (index == 1) {
					coinType = "RYB";
					num = $("#ryb_num").val();
				}
				if (parseFloat(num) == 0) {
					layer.msg('数字必须大于0');
					return
				}
				layer.closeAll();
				$('.pswdMask').show();

			});
			
			//监听 弹窗取消按钮
			$('#tradeCancelBtn').on('click',function(){
				$('.pswdMask').hide().find('input').val('');
			})
			//监听 密码确定按钮
			$('.pswdMask #tradeConfirmBtn').off("click").on("click", pay);

			function pay() {
				layer.load(1, {
					shade: [0.1, '#000']
				});
				var index = $(".change.active").index();
				var coinType = "RYH";
				var num = $("#ryh_num").val();
				if (index == 1) {
					coinType = "RYB";
					num = $("#ryb_num").val();
				}
				if (parseFloat(num) <= 0 || num == '') {
					layer.closeAll();
					layer.msg('数量不能为0或空，请输入');
					$('.pswdMask #tradeConfirmBtn').off("click").one("click", pay);
					
					return
				}
				var pswd = $('.pswdMask').find('input').val(); //取得交易密码
				if (pswd.length < 6) {
					layer.closeAll();
					layer.msg('密码格式有误，请重新输入');
					$('.pswdMask #tradeConfirmBtn').off("click").one("click", pay);
					
					return
				}
				$.post("/m/trade/tradeExchange.html", {
					"coinType": coinType,
					"coins": num,
					"transferType": "RTT",
					'pswd': pswd
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
		});
	</script>
</body>

</html>