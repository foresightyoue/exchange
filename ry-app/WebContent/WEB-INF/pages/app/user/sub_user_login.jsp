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
		<base href="${basePath}"/>
		<title>登录</title>
        <link rel="stylesheet" type="text/css" href="/static/front/app/css/new_common.css"/>
		<link rel="stylesheet" href="/static/front/app/css/verify.css" />
		<script type="text/javascript" src="/static/front/js/language/language_cn.js"></script>
		<script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js" ></script>
		<script type="text/javascript" src="/static/front/js/layer/layer.js"></script>
		<script type="text/javascript" src="/static/front/js/verify.js"></script>
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
.logo{
    width: 5.1rem;
    height: 5.1rem;
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
.white{
	line-height: 2rem;
	font-size: 1rem;
	color: #FEFEFE;
}
input{
    display: block;
    background-color: transparent;
    /* border-top: 0;
    border-right: 0;
    border-left: 0;
    border-bottom: 0.03rem solid #FFF8E4; */
	border: none;
    font-size: 1rem;
    outline: none;
    line-height: 2rem;
	color: #FEFEFE;
}
.forget{
    display: block;
    width: 20rem;
}
/* .get{
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
.login{
    line-height: 2.8rem;
    background-color: #EBA51D;
    color: #FFFFFF;
    text-align: center;
    border-radius: 0.4rem;
    margin-bottom: 0.93rem;
}
.register{
    line-height: 2.8rem;
    background-color: rgba(131,131,131,0.6);
    color: #FFFFFF;
    text-align: center;
    border-radius: 0.4rem;
    margin-bottom: 0.93rem;
}
.verify-box-cover{
	position: fixed;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	background: rgba(0,0,0,0.5);
	display: none;
}
.verify-box{
	position: fixed;
	top:50%;
	left: 5%;
    box-sizing: border-box;
	width: 90%;
	height: 250px;
	transform:translateY(-50%);
	-webkit-transform:translateY(-50%);
	background: #fff;
	border: 1px solid #e6e6e6;
	border-radius: 4px;
	-webkit-border-radius: 4px;
	display: none;
	padding: 10px;
	z-index: 99;
}
#send{
	background: none;
    color: #fff;
    border: none;
}
.line-bottom{
    border-bottom: 0.03rem solid #FFF8E4;
}


body .layui-bg-orange {
	background: rgba(235, 165, 29, 0.8);
	border-radius: 10px;
	border: none;
	text-align: center;
}
	</style>
	<body>
        <div class="content">
            <div class="flex_grow"></div>
            <img src="/static/front/app/images/login/login_logo.png" class="logo">
            <div class="flex_grow"></div>
			<div class="line-bottom"> <input type="text" 
				onkeyup="this.value=this.value.replace(/[, ]/g,'')" 
				onblur="this.value=this.value.replace(/[, ]/g,'')" placeholder="输入手机号" id="user" name="user" value="" class="row"/></div>
            <div class="grow"></div>
            <div class="line-bottom"><input type="password" placeholder="请输入密码" id="pwd" name="pwd" value="" class="row"/></div>
            <div class="grow"></div>
            <!-- <div class="flex row"><input type="text" id="sms_verify" placeholder="短信验证" name="sms_verify" value="" class="flex_grow"/><div class="get"><button id="send">获取验证码</button></div></div> -->
            <div class="row"><a href="${oss_url}/m/validate/reset.html" class="white">忘记密码</a></div>
            <div class="grow"></div>
            <div class="login row">登录</div>
            <a href="${oss_url}/m/user/register.html" class="register row">新用户注册</a>
            <div class="flex_grow"></div>
        </div>
		<div class="verify-box-cover"></div>
		<div class="verify-box">
		    <div id="verify"></div>
		</div>
	</body>
    <script type="text/javascript">
$(function () {
	
	$(".verify-box-cover").on('click',function(){
		$(".verify-box").hide();
		$(".verify-box-cover").hide();
		$('#verify').empty();
	});
	$("#send").click(function(){
	    $(this).prop('disabled','disabled');
	    var uName = $("#user").val();
	    if(uName.replace(/(^\s*)|(\s*$)/g, "") != ""){
    	    $.post("/m/login/sendMsg.html",{"loginName":uName},function(e){
    	        layer.msg(e.msg,{skin: 'layui-bg-orange'});
    	        if(e.status == 200){
        	        var time=120;
        	        var i = setInterval(function () {
        	            if (time>=0) {
        	                time-=1;
        	                if (time==0) {
        	                    $("#send").text("获取验证码");
        	                    $("#send").prop('disabled',false);
        	                    window.clearInterval(i);
        	                }else{
        	                    $("#send").text(time+"秒");
        	                }
        	            }
        	        },1000);
    	        }else{
    	            $("#send").prop('disabled',false);
    	        }
    	    });
	    }else{
	        $("#send").prop('disabled',false);
	        layer.msg("请输入账号");
	    }
	});
	$(".login").click(function (e) {
		var url = "/m/user/login/index.html?random=" + Math.round(Math.random() * 100);
		var uName = $("#user").val();
		var pWord = $("#pwd").val();
		var vCode=$("#sms_verify").val() || '123456';
		var longLogin = 0;
		if (util.checkEmail(uName)) {
			longLogin = 1;
		}
		function checkCount(val){
			var reg=/^[a-zA-Z]*\d{9,11}$/ig;
			return reg.test(val);
		}
		if (!util.checkEmail(uName) && !util.checkMobile(uName)) {
			layer.msg( language["comm.error.tips.1"],{skin: 'layui-bg-orange'});
			return
		}
		if (pWord == "") {
			layer.msg( language["comm.error.tips.2"],{skin: 'layui-bg-orange'});
			return;
		} else if (pWord.length < 6) {
			layer.msg( language["comm.error.tips.3"],{skin: 'layui-bg-orange'});
			return;
		}
		
		if (vCode == ""){
		    layer.msg("请输入验证码！",{skin: 'layui-bg-orange'});
		    return;
		}
		var param = {
			loginName: uName,
			password: pWord,
			type: longLogin,
			vCode: vCode
		};
		$(".verify-box").show();
		$(".verify-box-cover").show();
		$('#verify').slideVerify({
			type : 2,       //类型
			vOffset : 5,    //误差量，根据需求自行调整
			vSpace : 5, //间隔
			imgName : ['1.jpg', '2.jpg','3.jpg', '4.jpg','5.jpg', '6.jpg','7.jpg', '8.jpg','9.jpg', '10.jpg'],
			imgSize : {
				width: '100%',
				height: '180px',
			},
			blockSize : {
				width: '40px',
				height: '40px',
			},
			barSize : {
				width : '100%',
				height : '40px',
			},
			ready : function() {
			},
			success : function() {
				layer.msg(language["comm.tips.20"],{skin: 'layui-bg-orange',});
				// return;
				$(".verify-box").hide();
				$(".verify-box-cover").hide();
				$('#verify').empty();
				$.ajax({
					url:url,
					data:param,
					cache:false,
					dataType:"json",
					timeout:10000,
					type:"POST",
					success:function(data){
						if (data.code == 0) {
							window.location.href = "/m/tradePwd.html";
						} else if (data.code <0) {
							layer.msg( data.msg,{skin: 'layui-bg-orange'});
							if(data.code != -3){
								$("#pwd").val("");
							}
						}
					},
					error:function(){
						layer.msg("网络错误！",{skin: 'layui-bg-orange'});
					}
				});
			},
			error : function() {
				layer.msg(language["comm.tips.21"],{skin: 'layui-bg-orange'});
				$(".verify-refresh").click();
			}
		});
	});
});
    </script>
</html>