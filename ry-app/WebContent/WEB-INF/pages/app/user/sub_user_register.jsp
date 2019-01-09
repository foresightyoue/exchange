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
	<!-- <meta name="format-detection" content="email=no"> -->
	<meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
	<base href="${basePath}" />
	<title>注册</title>
	<link rel="stylesheet" type="text/css" href="/static/front/app/css/new_common.css" />
	<link rel="stylesheet" type="text/css" href="/static/front/app/css/intlTelInput.css" />
	<script type="text/javascript" src="/static/front/js/language/language_cn.js"></script>
	<script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js"></script>
	<script type="text/javascript" src="/static/front/js/layer/layer.js"></script>
	<script type="text/javascript" src="/static/front/js/comm/util.js"></script>
	<script type="text/javascript" src="/static/front/app/js/rem.js"></script>
	<script type="text/javascript" src="/static/front/app/js/intlTelInput.min.js"></script>
	<style type="text/css">
		.content {
			background: url(/static/front/app/images/login/login.png) no-repeat;
			background-size: 100% 100%;
			align-items: center;
			overflow-y: auto;
			min-width: 25rem;
		}

		.content>.head>.left>.text {
			position: absolute;
			color: #FFFDFD;
			width: 4rem;
			margin-top: -0.5rem;
			margin-left: 1.3rem;
			line-height: 1rem;
			font-size: 1rem;
		}

		.register {
			font-size: 1.6rem;
			color: #FFFFFF;
			width: 20rem;
		}

		.grow {
			flex-grow: 0.5;
		}

		.row {
			width: 20rem;
			line-height: 2rem;
			font-size: 1rem;
			color: #FEFEFE;
			flex-shrink: 0;
		}

		input {
			display: block;
			background-color: transparent;
			/* border-top: none;
		    border-right: none;
		    border-left: none;
		    border-bottom: 0.03rem solid #FFF8E4; */
			border: none;
			font-size: 1rem;
			outline: none;
			line-height: 2rem;
			color: #FEFEFE;
		}

		.forget {
			display: block;
			width: 20rem;
		}

		.get,
		.area {
			/* border-bottom: 0.03rem solid #FFF8E4; */
			border: none;
		}

		input::-webkit-input-placeholder {
			color: #BCBBBB;
		}

		input::-moz-placeholder {
			/* Mozilla Firefox 19+ */
			color: #BCBBBB;
		}

		input:-moz-placeholder {
			/* Mozilla Firefox 4 to 18 */
			color: #BCBBBB;
		}

		input:-ms-input-placeholder {
			/* Internet Explorer 10-11 */
			color: #BCBBBB;
		}

		.radio {
			background-color: #CAC8C8;
			border-radius: 50%;
			width: 0.87rem;
			height: 0.87rem;
			margin-right: 0.57rem;
			position: relative;
		}

		.radio.active>div {
			background-color: #F67003;
			border-radius: 50%;
			width: 0.47rem;
			height: 0.47rem;
			position: absolute;
			top: 50%;
			left: 50%;
			transform: translate(-50%, -50%);
		}

		.org {
			color: #EBA51D;
		}

		.confirm {
			line-height: 2.8rem;
			background-color: rgba(131, 131, 131, 0.6);
			color: #FFFFFF;
			text-align: center;
			border-radius: 0.4rem;
			margin-bottom: 0.93rem;
		}

		.confirm.active {
			background: #EBA51D;
		}

		/* 弹出的蒙版样式 */
		.mask {
			display: none;
			position: fixed;
			left: 0;
			top: 0;
			z-index: 101;
			width: 100vw;
			height: 100vh;
			background: rgba(0, 0, 0, 0.5);
			overflow: hidden;
			color: #fff;
		}

		.posBox {
			width: 80%;
			height: 14rem;
			background: rgba(51, 51, 51, 0.96);
			position: absolute;
			top: 50%;
			left: 50%;
			transform: translate(-50%, -50%);
			border-radius: 1rem;
			overflow: hidden;
		}

		.posBox>.contentWrapper {
			width: 100%;
			height: 100%;
			position: relative;
			align-items: stretch;
		}

		.posBox>.contentWrapper>div {
			flex: 0 0 100%;
			width: 100%;
			height: 100%;
			position: absolute;
			top: 0;
			left: 0;
			display: flex;
			flex-flow: nowrap column;
			box-sizing: border-box;
		}

		.posBox>.contentWrapper>.officalActivate,
		.posBox>.contentWrapper>.friendActivate {
			display: none;
		}

		.selectBox>.title {
			height: 3rem;
			line-height: 3rem;
			text-align: center;
			color: #EBA51D;

		}

		.selectBox>.info {
			width: 80%;
			height: 3rem;
			line-height: 1.5rem;
			color: #999;
			margin: auto;
		}

		.selectBox>.selectItem {
			flex: 1;
			display: flex;
			align-items: center;
			padding-left: 10%;
			box-sizing: border-box;
			position: relative;
			border-top: 1px solid rgba(255, 255, 255, 0.3);
		}

		.selectBox>.selectItem>i {
			position: absolute;
			right: 10%;
			top: 50%;
			width: 0.6rem;
			height: 0.6rem;
			border-top: 3px solid #fff;
			border-right: 3px solid #fff;
			transform: translate(0, -50%) rotate(45deg);
		}

		.officalActivate>.title {
			height: 3rem;
			line-height: 3rem;
			/* color:#; */
			padding-left: 10%;
		}

		.officalActivate>.info {
			height: 2rem;
			line-height: 2rem;
			padding-left: 10%;
			color: #999;
		}

		.officalActivate>.activeItem {
			flex: 1;
			display: flex;
			justify-content: space-between;
			align-items: center;
			padding: 0 10%;
			box-sizing: border-box;
			border-top: 1px solid rgba(255, 255, 255, 0.2);
		}

		.officalActivate>.activeItem>.activateBtn {
			align-self: stretch;
			flex: 0 0 30%;
			display: flex;
			align-items: center;
			color: #EBA51D;
			justify-content: flex-end;
		}

		.friendActivate>.title {
			height: 3rem;
			line-height: 3rem;
			padding-left: 10%;
			color: #fff;
		}

		.friendActivate>.info {
			color: #999;
			padding-left: 10%;
			line-height: 3rem;
			height: 3rem;
		}

		.friendActivate>.frientInfo {
			flex: 1;
			display: flex;
			align-items: center;
			justify-content: space-between;
			padding: 0 10%;
			border-top: 1px solid rgba(255, 255, 255, 0.3);
			border-bottom: 1px solid rgba(255, 255, 255, 0.3);
		}

		.friendActivate>.frientInfo>label {
			white-space: nowrap;
		}

		.friendActivate>.frientInfo>input {
			flex: 1;
			border: none;
		}

		.friendActivate>.confirmActivate {
			margin: 1rem auto;
			height: 2rem;
			width: 10rem;
			line-height: 2rem;
			text-align: center;
			color: #fff;
			background: #EBA51D;
			border-radius: 0.377rem;
		}

		.agree {
			border: none;
		}

		.line-bottom {
			border-bottom: 0.03rem solid #FFF8E4;
		}
	</style>
</head>

<body>
	<div class="content">
		<div class="head">
			<a href="/m/user/login.html" class="left"><i class="left_arrow"></i>
				<div class="text">返回</div>
			</a>
		</div>
		<div class="flex_grow"></div>
		<div class="register">新用户注册</div>
		<div class="grow"></div>
		<div class="line-bottom">
			<div class="flex row"><input type="tel" onkeyup="this.value=this.value.replace(/[, ]/g,'')"
				onblur="this.value=this.value.replace(/[, ]/g,'')" placeholder="输入手机号" id="user" value="" class="flex_grow" />
				<!-- <div class="area">+86中国</div> -->
			</div>
		</div>
		<div class="grow"></div>
		<div class="line-bottom">
			<div class="flex row"><input type="password" placeholder="请输入密码" id="pwd" value="" class="row" /></div>
		</div>
		<div class="grow"></div>
		<div class="line-bottom">
			<div class="flex row"><input type="password" placeholder="请再次输入密码" id="repwd" value="" class="row" /></div>
		</div>
		<div class="grow"></div>
		<div class="line-bottom">
			<div class="flex row"><input type="text" id="sms_verify" placeholder="短信验证" value="" class="flex_grow" />
				<div class="get">获取验证码</div>
			</div>
		</div>
		<div class="grow"></div>
		<div class="line-bottom">
			<div class="flex row"><input type="text" placeholder="推荐人" id="recommend" value="" class="row" /></div>
		</div>
		<div class="grow"></div>
		<div class="agree flex_left row">
			<div class="radio flex_center">
				<div></div>
			</div>我已同意<a href="${oss_url}/m/about/index.html?title=用户协议" class="org">《瑞银钱包用户协议》</a>
		</div>
		<div class="grow"></div>
		<div class="confirm row">确定</div>
		<div class="flex_grow"></div>
		<div class="flex_grow"></div>
	</div>
	<div class='mask'>
		<div class='posBox'>
			<div class='contentWrapper'>
				<div class='selectBox'>
					<div class='title'>您的账号未激活</div>
					<div class='info'>欢迎加入瑞银，请通过官方或者好友激活账号</div>
					<div class='selectItem officialOpt'>官方激活 <i></i></div>
					<div class='selectItem friendOpt'>寻找好友激活 <i></i></div>
				</div>
				<div class='officalActivate'>
					<div class='title'>官方激活</div>
					<div class='info'>请选择官方激活的账号</div>
					<div class='activeItem'><span>1，RY000000</span><span class='activateBtn'>激活</span></div>
					<div class='activeItem'><span>2，RY000000</span><span class='activateBtn'>激活</span></div>
					<div class='activeItem'><span>3，RY000000</span><span class='activateBtn'>激活</span></div>
				</div>
				<div class='friendActivate'>
					<div class='title'>好友激活</div>
					<div class='info'>请输入好友的账号或手机号激活</div>
					<div class='frientInfo'><label for='friendActivate'>推荐人：</label><input type='text' placeholder='请输入好友的手机或者账号' id='friendActivate'></div>
					<div class='confirmActivate'>确认</button>
					</div>
				</div>
			</div>
		</div>
</body>
<script type="text/javascript">
	$(function () {
		$("#user").intlTelInput({
				//是否允许下拉
				// allowDropdown: true,
				// if there is just a dial code in the input: remove it on blur, and re-add it on focus
				// autoHideDialCode: true,
				// 添加在所选国家例数输入占位符
				// autoPlaceholder: "polite",
				// 修改自动占位符
				// customPlaceholder: null,
				// 附加菜单到特定元素
				// dropdownContainer: "",
				// 不要显示这些国家
				// excludeCountries: [],
				// 在初始化过程中格式化的输入的值
				// formatOnDisplay: true,
				//更多的查找功能
				// geoIpLookup: null,
				//初始国家
				initialCountry: "cn",
				// 不要插入国际拨号码
				// nationalMode: false,
				//数字类型用于占位符
				// placeholderNumberType: "MOBILE",
				// display only these countries
				// onlyCountries: [],
				// 显示只有这些国家
				preferredCountries: ["us", "cn"],
				//在选定的标记旁边显示国家拨号码，因此它不是键入编号的一部分
				// separateDialCode: true,
				// 指定要启用验证/格式的一个专门用于脚本的路径
				// utilsScript: ""
				width: '100%',
			}

		);
		var intro_user = "${intro}";
		$(".agree").click(function (e) {
			$(".radio").toggleClass("active");
			$('.confirm').toggleClass('active');
		});
		$(".org").click(function (e) {
			e.stopPropagation();
		});
		// $('#user').blur(function () {
		// 	if (!util.checkMobile($(this).val())) {
		// 		layer.msg('手机格式不正确，请重新输入！');
		// 		$(this).focus();
		// 		return;
		// 	}
		// })
		$(".confirm").click(function (e) {
			// console.log($("#user").intlTelInput("getSelectedCountryData"));
			// console.log(util.trim($("#user").val()));
			
			// return
			if (!$(this).hasClass('active')) {
				return
			}
			if (!util.checkMobile($("#user").val())) {
				layer.msg('手机格式不正确，请重新输入！');
				$("#user").focus();
				return;
			}
			var regType = 0;
			// var areaCode = "+86";
			var regUserName = util.trim($("#user").val());
			
			
			var areaCode = $("#user").intlTelInput("getSelectedCountryData").dialCode
			var pwd = util.trim($("#pwd").val());
			var repwd = util.trim($("#repwd").val());
			var recommend = util.trim($("#recommend").val());


			if (!util.checkMobile(regUserName)) {
				layer.msg('手机格式不正确，请重新输入！');
				$("#user").focus();
				return;
			}
			var desc = util.isPassword(pwd);
			if (desc != "") {
				layer.msg(desc);
				return;
			}
			if (pwd != repwd) {
				layer.msg("两次密码不相同！");
				return;
			}
			var regPhoneCode = $("#sms_verify").val();
			if (regPhoneCode == "") {
				layer.msg(language["comm.error.tips.60"]);
				return;
			}
			layer.load(1, {
				shade: [0.1, '#000']
			});
			var regEmailCode = 0;
			$.ajax({
				url: "/m/user/reg/index.html?random=" + Math.round(Math.random() * 100),
				data: {
					regName: regUserName,
					password: pwd,
					regType: regType,
					phoneCode: regPhoneCode,
					ecode: regEmailCode,
					areaCode: areaCode,
					intro_user: intro_user,
					recommend: recommend
				},
				cache: false,
				dataType: "json",
				timeout: 5000,
				type: "POST",
				success: function (data) {
					layer.closeAll();
					if (data.code < 0) {
						layer.msg(data.msg);
					} else {
						layer.msg('注册成功');
						setTimeout(window.location.href = "/m/tradePwd.html", 2000);
					}
				},
				error: function () {
					layer.closeAll();
					layer.msg("网络错误！");
				}
			});
		});
		var time = 1;
		var getClick = function () {
			var phone = $("#user").val();
			var areaCode = $("#user").intlTelInput("getSelectedCountryData").dialCode
			if (!util.checkMobile(phone)) {
				layer.msg(language["comm.error.tips.10"]);
				return;
			}
			$(".get").unbind('click');
			time = 121;
			$.ajax({
				url: "/user/sendMsg.html?random=" + Math.round(Math.random() * 100),
				data: {
					type: 12,
					msgtype: 1,
					areaCode: areaCode,
					phone: phone
				},
				cache: false,
				dataType: "json",
				timeout: 5000,
				type: "POST",
				success: function (data) {
					layer.msg(data.msg);
				},
				error: function () {
					layer.msg("网络错误！");
				}
			});
		};
		setInterval(function () {
			if (time > 0) {
				time -= 1;
				if (time == 0) {
					$(".get").bind('click', getClick);
					$(".get").text("获取验证码")
				} else {
					$(".get").text(time + "秒");
				}
			}
		}, 1000);
	});
	$('.officialOpt').click(function () {
		$(this).parent().css('display', 'none');
		$('.officalActivate').css('display', 'flex');
	})
	$('.friendOpt').click(function () {
		$(this).parent().css('display', 'none');
		$('.friendActivate').css('display', 'flex');
	})
</script>

</html>