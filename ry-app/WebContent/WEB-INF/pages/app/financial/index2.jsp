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
	<title>个人中心</title>
	<link rel="stylesheet" type="text/css" href="/static/front/app/css/new_common.css" />
	<script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js"></script>
	<script type="text/javascript" src="/static/front/app/js/rem.js"></script>
	<script type="text/javascript" src="${oss_url}/static/front/js/plugin/layer/layer.js"></script>
	<style type="text/css">
		.head {
			height: 16.87rem;
			display: flex;
			flex-direction: column;
			justify-content: center;
			align-items: center;
			color: #FFFFFF;
			background-image: url(/static/front/app/images/main/geren/mine_bg.png);
		}

		.head>div {
			margin-top: 0.5rem;
		}

		.photo {
			width: 5rem;
			height: 5rem;
			border-radius: 50%;
		}

		.cardid {
			width: 4.93rem;
			line-height: 1.5rem;
			background: rgba(153, 153, 153, 1);
			border-radius: 0.4rem;
			font-size: 0.73rem;
			text-align: center;
		}

		.row {
			margin-top: 0.2rem;
			line-height: 3rem;
			display: flex;
			font-size: 1.13rem;
			align-items: center;
			background-color: #FFFFFF;
		}

		.no_read {
			display: inline-block;
			width: .4rem;
			height: .4rem;
			border-radius: 50%;
			-webkit-border-radius: 50%;
			background-color: red;
			position: absolute;
			right: -0.1rem;
			top: -0.1rem;
			z-index: 10;
		}

		.row>.logo {
			width: 1.2rem;
			height: 1.2rem;
			margin: 0.9rem 0.9rem 0.9rem 0.9rem;
		}

		.row>.text {
			flex: 1 1 auto;
		}

		.row>.right {
			margin: 0.97rem 1rem;
			width: 0.6rem;
			height: 1.07rem;
			font-size: 1.13rem;
		}

		.row>.tip {
			color: #999999;
		}

		#login_out {
			margin: 1.67rem 0.67rem 0 0.67rem;
			background-color: #EBA51D;
			color: #FFFFFF;
			text-align: center;
			line-height: 2.8rem;
			border-radius: 0.4rem;
		}
	</style>
</head>

<body class="body-gray">
	<div class="content">
		<div class="main">
			<div class="head">
				<img class="photo" src="/static/front/app/images/main/geren/touxiang.png">
				<div class="user">${login_user.ftelephone}</div>
				<div class="useri">${login_user.floginName}</div>
				<div class="cardid">普通用户</div>
			</div>
			<a href="javascript:void(0);" onclick="todivide()" class="row"><img src="/static/front/app/images/main/geren/tuiguang.png"
				 class="logo">
				<div class="text">推广中心</div><img class="right" src="/static/front/app/images/main/geren/right.png" alt="" />
			</a>
			<a href="javascript:void(0);" onclick="ToActivation()" class="row"><img src="/static/front/app/images/main/geren/jihuo.png"
				 class="logo">
				<div class="text">激活<font style="float: right;color: #cccccc;" id="IsActivation"></font>
				</div><img class="right" src="/static/front/app/images/main/geren/right.png" alt="" />
			</a>
			<a href="javascript:void(0);" onclick="toOtcApply()" class="row"><img src="/static/front/app/images/main/geren/shangjiarenzheng.png"
				 class="logo">
				<div class="text">OTC商家认证<font style="float: right;color: #cccccc;" id="fIsMerchant"></font>
				</div><img class="right" src="/static/front/app/images/main/geren/right.png" alt="" />
			</a>
			<a href="${oss_url}/m/security.html" class="row"><img src="/static/front/app/images/main/geren/anquan.png" class="logo">
				<div class="text">安全设置</div><img class="right" src="/static/front/app/images/main/geren/right.png" alt="" />
			</a>
			<a href="javascript:void(0);" onclick="tomyposter()" class="row"><img src="/static/front/app/images/main/geren/fenxiang.png"
				 class="logo">
				<div class="text">分享</div><img class="right" src="/static/front/app/images/main/geren/right.png" alt="" />
			</a>
			<!-- <a href="${oss_url}/m/about/index.html?title=关于我们" class="row"><img src="/static/front/app/images/main/geren/guanyu.png" class="logo" ><div class="text">关于我们</div><img class="right" src="/static/front/app/images/main/geren/right.png" alt=""/></a> -->
			<a href="javascript:void(0);" onclick="toappversion()" class="row"><img src="/static/front/app/images/main/geren/shuaxin.png"
				 class="logo">
				<div class="text">下载地址<font style="float: right;color: #cccccc;" id="version"></font>
				</div><img class="right" src="/static/front/app/images/main/geren/right.png" alt="" />
			</a>
			<a href="javascript:void(0);" onclick="toOurService()" class="row"><img src="/static/front/app/images/main/geren/gonggao.png"
				 class="logo">
				<div class="text">公告信息</div><img class="right" src="/static/front/app/images/main/geren/right.png" alt="" />
			</a>
			<a href="${oss_url}/m/question/question.html" class="row"><img src="/static/front/app/images/main/geren/yijian.png"
				 class="logo">
				<div class="text"><span style="position: relative;">意见反馈<span id="no_read" class=""></span></span></div><img class="right"
				 src="/static/front/app/images/main/geren/right.png" alt="" />
			</a>
			<a href="${oss_url}/m/mine/help.html" class="row"><img src="/static/front/app/images/main/geren/bangzhu.png" class="logo">
				<div class="text">帮助中心</div><img class="right" src="/static/front/app/images/main/geren/right.png" alt="" />
			</a>
			<div id="login_out">退出登录</div>
			<div class="obligate"></div>
		</div>
		<div class="menu">
			<jsp:include page="../comm/bottom_menu.jsp"></jsp:include>
		</div>
	</div>
</body>
<script type="text/javascript">
	var device = '${device}';

	function appReturnBtnClick() {
		if (device == 'android')
			JSobj.finisWeb();
		else {
			window.location.href = "${oss_url}/m/index.html";
		}
	}
	if (device == 'android') {
		JSobj.pullrefresh(false);
	}
	$(function () {
		$("#login_out").click(function () {
			deleteCookie("loginName", "/");
			deleteCookie("password", "/");
			window.location.href = "/m/user/logout.html";
		});
		var deleteCookie = function (name, path) { /**根据cookie的键，删除cookie，其实就是设置其失效**/
			var name = escape(name);
			var expires = new Date(0);
			path = path == "" ? "" : ";path=" + path;
			document.cookie = name + "=" + ";expires=" + expires.toUTCString() + path;
		};
	});
</script>
<script type="text/javascript">
	$(function () {
		var msg = '${msg}';
		if (msg != '') {
			layer.msg(msg);
		}
		var u = navigator.userAgent;
		var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Adr') > -1;
		var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/);
		if (isAndroid) {
			zyh_js.getVersionName();
		} else if (isiOS) {
			window.webkit.messageHandlers.getVersionName.postMessage({})
		}

		window.getVersion = function(msg) {
			$("#version").text(msg)
		}


		var userId = '${fuser.floginName}';
		if (userId == '') {
			userId = '${userId}';
		}

		$.post("/m/user/api/info/seller.html", {
			"userId": userId
		}, function (obj) {
			if (obj.data.sellerLevel > 0 && obj.data.sellerStatus == 1) {
				$("#fIsMerchant").text("认证中");
			} else if (obj.data.IsMerchant > 0 && obj.data.sellerStatus == 2) {
				$("#fIsMerchant").text("已认证");
			} else if (obj.data.sellerLevel >= 0 && obj.data.sellerStatus == 3) {
				$("#fIsMerchant").text("认证失败");
			} else if (obj.data.IsMerchant == 0) {
				$("#fIsMerchant").text("未认证");
			}
			if (obj.data.hasActived) {
				$("#IsActivation").text("已激活");
			} else {
				$("#IsActivation").text("未激活");
			}

		});
		$.post("/m/trade/getcontact.html", function (obj) {
			if (obj.code == 200) {
				if (obj.Count > 0) {
					$("#no_read").addClass("no_read");
				}
			}
		});
	});
</script>
<script type="text/javascript">
	function todivide() {
		// $.post("/m/trade/IsCheckIdentity.html",function(obj){
		// 	if(obj.code == 200){
		window.location.href = "/m/introl/mydivides.html";
		// 	}else{
		// 		layer.msg(obj.message);
		// 	}
		// });
	}

	function ToActivation() {
		// $.post("/m/trade/IsCheckIdentity.html",function(obj){
		// 	if(obj.code == 200){
		window.location.href = "/m/trade/ToActivation.html";
		// 	}else{
		// 		layer.msg(obj.message);
		// 	}
		// });
	}

	function toOtcApply() {
		$.post("/m/trade/IsCheckIdentity.html", function (obj) {
			if (obj.code == 200) {
				window.location.href = "/m/otc/OtcApply.html";
			} else {
				layer.msg(obj.message);
			}
		});
	}

	function tomyposter() {
		// $.post("/m/trade/IsCheckIdentity.html",function(obj){
		// 	if(obj.code == 200){
		window.location.href = "/m/introl/myposter.html";
		// 	}else{
		// 		layer.msg(obj.message);
		// 	}
		// });
	}

	function toappversion() {
		// $.post("/m/trade/IsCheckIdentity.html",function(obj){
		// 	if(obj.code == 200){
		window.location.href = "/m/appversion/appversion.html";
		// 	}else{
		// 		layer.msg(obj.message);
		// 	}
		// });
	}

	function toOurService() {
		// $.post("/m/trade/IsCheckIdentity.html",function(obj){
		// 	if(obj.code == 200){
		window.location.href = "/m/service/ourService.html?id=1";
		// 	}else{
		// 		layer.msg(obj.message);
		// 	}
		// });
	}
</script>

</html>