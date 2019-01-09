<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../../front/comm/include.inc.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>

<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="../comm/header.jsp"></jsp:include>
        <meta charset="utf-8" />
        <meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no, width=device-width">
        <meta name="format-detection" content="telephone=no">
        <meta name="format-detection" content="email=no">
        <base href="${basePath}"/>
        <title>找回密码</title>
		<link rel="stylesheet" type="text/css" href="/static/front/app/css/new_common.css"/>
		<script type="text/javascript" src="/static/front/js/language/language_cn.js"></script>
        <script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js" ></script>
		<script type="text/javascript" src="/static/front/js/layer/layer.js"></script>
		<script type="text/javascript" src="/static/front/js/comm/util.js"></script>
        <script type="text/javascript" src="/static/front/app/js/rem.js"></script>
    </head>
        <style>
.content{
    background: url(/static/front/app/images/login/login.png) no-repeat;
    background-size: 100% 100%;
    align-items: center;
	overflow-y: auto;
	min-width: 25rem;
}
.content>.head>.left>.text{
	position: absolute;
    color: #FFFDFD;
    width: 2rem;
    margin-top: -0.5rem;
    margin-left: 1.3rem;
    line-height: 1rem;
    font-size: 1rem;
}
.find{
	font-size: 1.6rem;
	color: #FFFFFF;
	width: 20rem;
}
.grow{
    flex-grow: 0.5;
}
.row{
    width: 20rem;
    line-height: 2rem;
    font-size: 1rem;
    color: #FEFEFE;
    flex-shrink: 0;
}
input{
    display: block;
    background-color: transparent;
    border : none;
    font-size: 1rem;
    outline: 0;
    line-height: 2rem;
	color: #FEFEFE;
}
.forget{
    display: block;
    width: 20rem;
}
/* .get,.area{
    border-bottom: 0.03rem solid #FFF8E4;
} */
input::-webkit-input-placeholder{
    color:#BCBBBB;
}
input::-moz-placeholder{   /* Mozilla Firefox 19+ */
    color:#BCBBBB;
}
input:-moz-placeholder{    /* Mozilla Firefox 4 to 18 */
    color:#BCBBBB;
}
input:-ms-input-placeholder{  /* Internet Explorer 10-11 */ 
    color:#BCBBBB;
}
.confirm{
    line-height: 2.8rem;
    background-color: #EBA51D;
    color: #FFFFFF;
    text-align: center;
    border-radius: 0.4rem;
    margin-bottom: 0.93rem;
}
.line-bottom {
			border-bottom: 0.03rem solid #FFF8E4;
		}

        </style>
    <body>
		<div class="content">
		    <div class="head">
		        <a href="/m/user/login.html" class="left"><i class="left_arrow"></i><div class="text"  style="width: 5rem;">返回</div></a>
		    </div>
		    <div class="flex_grow"></div>
		    <div class="find">找回登录密码</div>
		    <div class="grow"></div>
			<div class="line-bottom"><div class="flex row"><input type="number" onkeyup="this.value=this.value.replace(/[, ]/g,'')"
				onblur="this.value=this.value.replace(/[, ]/g,'')" placeholder="请输入账号" id="user" value="" class="flex_grow"/><div class="area"></div></div></div>
			<div class="grow"></div>
			<div class="line-bottom"><div class="flex row"><input type="text" id="sms_verify" placeholder="短信验证" value="" class="flex_grow"/><div class="get">获取验证码</div></div></div>
		    <div class="grow"></div>
		    <div class="line-bottom"><input type="password" placeholder="请输入密码" id="pwd" value="" class="row" /></div>
		    <div class="grow"></div>
		    <div class="line-bottom"><input type="password" placeholder="请再次输入密码" id="repwd" value="" class="row"/></div>
		    <div class="grow"></div>
		    <div class="confirm row">确定</div>
		    <div class="flex_grow"></div>
			<div class="flex_grow"></div>
		</div>
    </body>
<script type="text/javascript">
$(function() {
	var time=1;
	var getClick=function () {
		var phone=$("#user").val();
		/*if (!util.checkMobile(phone)) {
			layer.msg(language["comm.error.tips.10"]);
			return;
		}*/
		$(".get").unbind('click');
		time=121;
		$.ajax({
			url:"/m/login/sendMsg.html?random=" + Math.round(Math.random() * 100),
			data:{
			    loginName : phone
			},
			cache:false,
			dataType:"json",
			timeout:5000,
			type:"POST",
			success:function(data){
                layer.msg(data.msg);
			},
			error:function(){
				layer.msg("网络错误！");
			}
		});
	};
	setInterval(function () {
		if (time>0) {
			time-=1;
			if (time==0) {
				$(".get").bind('click',getClick);
				$(".get").text("获取验证码")
			}else{
				$(".get").text(time+"秒");
			}
		}
	},1000);
	var confirm_click=function () {
		var phone=$("#user").val();
		var msgcode=$("#sms_verify").val();
		var password = $("#pwd").val();
		var password1 = $("#repwd").val();
		if (!util.checkMobile(phone)) {
			layer.msg(language["comm.error.tips.10"]);
			return;
		}
		if (msgcode == "" || !/^[0-9]{6}$/.test(msgcode)) {
            layer.msg(language["reset.error.tips.2"]);
            return;
        }
		if($('#pwd').val() != $('#repwd').val()){
			layer.msg(language["comm.error.tips.109"]);
			return;
		}
		console.log('detected')
		$(".confirm").unbind('click');
		$.ajax({
			url:"/m/validate/resetPhoneValidation1.html?random=" + Math.round(Math.random() * 100),
			data:{
				phone : phone,
				msgcode : msgcode,
				password : password
			},
			cache:false,
			dataType:"json",
			timeout:5000,
			type:"POST",
			success:function(data){
				if (data.code != 200) {
					$(".confirm").bind('click',confirm_click);
					layer.msg(data.msg);
				}else{
					layer.msg("修改密码成功！");
					$(".confirm").bind('click',confirm_click);
					setTimeout(window.location.href = "/m/user/login.html",5000);
				}
			},
			error:function(){
				$(".confirm").bind('click',confirm_click);
				layer.msg("网络错误！");
			}
		});
	};
	$(".confirm").bind('click',confirm_click);
});
</script>
</html>