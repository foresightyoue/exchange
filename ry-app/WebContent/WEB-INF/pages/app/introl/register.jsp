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
<base href="${basePath}"/>
<title>APP下载</title>
<link rel="stylesheet" href="/static/front/app/css/css.css" />
<link rel="stylesheet" href="/static/front/app/css/style.css" />
<script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js" ></script>
</head>
<style>
.gift-logo{
    width: 100%;
    height: auto;
    text-align: center;
    color: #186ed5;
}
.gift-logo img{
    width: 100%;
    height: auto;
}
.gift-logo h1{
    font-size: 1.2rem;
    margin-bottom: 0.5em;
}
.gift-logo h3{
    font-size: 0.9rem;
}
.download-app{
    position: absolute;
    bottom: 0;
    left: 0;
    width: 100%;
    height: 16rem;
    background: url("/static/front/app/images/gift-bg.png") no-repeat scroll center;
    background-size: 100% 100%;
}
.download-app h3{
    position: absolute;
    bottom: 3rem;
    left: 0;
    width: 100%;
    text-align: center;
    color: rgba(255,255,255,0.6);
}
.download{
    position: absolute;
    top: 50%;
    left: 0;
    width: 100%;
    height: 2.46rem;
    transform:translateY(-50%);
    -webkit-transform:translateY(-50%);
    display: flex;
    display: -webkit-flex;
    justify-content: space-around;
    align-items: center;
}
.download a{
    color: #fff;
    font-size: 0.86rem;
    display: inline-block;
    width: auto;
    height: 2.46rem;
    line-height: 2.46rem;
    border: 1px solid #fff;
    border-radius: 0.5em;
    -webkit-border-radius: 0.5em;
    padding: 0 0.7rem;
}
.download a img{
    width: auto;
    height: 1.7rem;
    vertical-align: middle;
    margin-top: -0.2em;
    margin-right: 0.5em;
}
.gift-reg{
    position: absolute;
    top: 50%;
    left: 50%;
    transform:translate(-50%,-50%);
    -webkit-transform:translate(-50%,-50%);
    width: 80%;
    min-height: 17rem;
    background: #fff;
    border-radius: 1em;
    -webkit-border-radius: 1em;
    padding: 1.67rem;
    z-index: 9999;
}
.gift-reg h3{
    text-align: center;
    color: #186ed5;
    font-size: 1.2rem;
    margin-bottom: 1.5rem;
}
.gift-input{
    width: 100%;
    height: 2.7rem;
    line-height:2.7rem;
    border: solid 1px #cccccc;
    border-radius: 0.1rem;
    -webkit-border-radius: 0.1rem;
    margin-bottom: 1.3rem;
    padding: 0 0.7rem;
    position: relative;
}
.gift-input input{
    width: 70%;
}
.btn-sendmsg{
    color: #186ed5;
    position: absolute;
    top: 0;
    right: 0.7rem;
}
.cover{
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0,0,0,0.5);
    z-index: 9998;
}
.gift-suc{
    padding-top: 0;
}
.gift-suc img{
    width: auto;
    height: 7rem;
    margin-bottom: 0.5em;
}
.gift-suc h3{
    font-size: 1.6rem;
    margin-bottom: 0.5em;
}
.gift-suc h4{
    text-align:center;
    font-size: 1rem;
    color: black;
    margin-bottom: 1em;
}
body {
    padding-top: 0em;
}
</style>
<body style="background: #fff;">
<div class="gift-logo">
    <img src="/static/front/app/images/gift-logo.png">
    <h1>瑞银&nbsp;&nbsp;全新上线</h1>
    <h3>交易更自由，资金更安全</h3>
</div>
<div class="download-app">
    <div class="download">
        <a class="iphone" href="${ios}"><img src="/static/front/app/images/iphone.png">iPhone下载</a>
        <a class="android" href="${android}"><img src="/static/front/app/images/android.png">Android下载</a>
    </div>
    <h3>瑞银  满足您对数字货币的不同需求</h3>
</div>

<div class="cover">
</div>
<div class="gift-reg gift-register">
    <h3>输入手机号码领取</h3>
    <div class="gift-input">
        <span class="prefixion" id="register-phone-areacode">+86</span>
        <input class="phone-number" type="number" value="" placeholder="请输入手机号" id="register-phone" />
    </div>
    <div class="gift-input">
        <input type="number" class="phone-code" value="" placeholder="请输入短信验证码" id="register-msgcode"/>
        <span class="gain btn-sendmsg" id="register-sendphone"  data-msgtype="12">发送</span>
    </div>
    <div class="gift-input">
        <input type="password" class="phone-code" value="" placeholder="请设置密码" id="register-password"/>
    </div>
    <p id="tipElement_id" style="color:red;"></p>
    <div class="next next-active" id="register">领取</div>
</div>
<div class="gift-reg gift-suc" style="display: none;">
    <img src="/static/front/app/images/gift-suc.png">
    <h3>领取成功</h3>
    <h4></h4>
    <div class="next next-active" id="download">确定</div>
</div>
<input id="register-intro" type="hidden" value="${intro }">
<input id="userId" type="hidden" value="${userId }">
<script type="text/javascript" src="/static/front/js/comm/util.js"></script>
<script type="text/javascript" src="/static/front/js/language/language_cn.js"></script>
<script type="text/javascript" src="/static/front/js/layer/layer.js"></script>
<script type="text/javascript" src="/static/front/js/comm/msg.js"></script>
<script type="text/javascript" src="/static/front/js/comm/comm.js"></script>
<script type="text/javascript">
var reg = {
    checkRegUserName : function() {
        var regUserName = "";
        var desc = '';
        // 验证手机号
        regUserName = util.trim(document.getElementById("register-phone").value);
        if (regUserName.indexOf(" ") > -1) {
            desc = language["comm.error.tips.8"];
        } else {
            if (regUserName == '') {
                desc = language["comm.error.tips.9"];
            } else if (!util.checkMobile(regUserName)) {
                desc = language["comm.error.tips.10"];
            }
        }
        if (desc != "") {
            layer.msg( desc);
            return false;
        } else {
            util.hideerrortips("register-errortips");
            return true;
        }
    },
    regSubmit : function() {
        if (this.checkRegUserName()) {
            var userId = '${userId}';

            var regUserName = "";
            regUserName = util.trim(document.getElementById("register-phone").value);
            var regPhoneCode = "";
            regPhoneCode = document.getElementById("register-msgcode").value;
            if (regPhoneCode == "") {
                layer.msg( language["comm.error.tips.60"]);
                return;
            }
            password = document.getElementById("register-password").value;
            if (password == "") {
                layer.msg('请输入密码');
                return;
            }
            var intro_user = document.getElementById("register-intro").value;

            var userId = document.getElementById("userId").value;

            var param = {
                regName : regUserName,
                phoneCode : regPhoneCode,
                recommend : userId,
                areaCode : "86",
                password : password,
                intro_user : intro_user,
                random : Math.round(Math.random() * 100),
            };
            $.ajax({
               url : "/m/introl/index.html",
               type : 'post',
               data : param,
               dataType : 'json',
               success:function(data){
                   if (data.code < 0) {
                       // 注册失败
                       layer.msg(data.msg);
                   } else {
                      $(".gift-register").hide();
                      $(".gift-suc").find('h4').html(data.msg);
                      $(".gift-suc").show();
                   }
               },
               error:function(){
                   layer.msg('error');
               }
            });
        }
    }
};
$(function() {
    $(".btn-sendmsg").on("click", function() {
        var phone = $("#register-phone").val();
        console.log(phone);
        msg.sendMsgCode($(this).data().msgtype, "tipElement_id", this.id, '86', phone);
    });
    $("#register-phone").on("blur", function() {
        reg.checkRegUserName();
    });
    $("#register").on('click',function(){
        reg.regSubmit();
    });
    $("#download").on('click',function(){
        $(".gift-suc").hide();
        $(".cover").hide();
    });
});
</script>
</body>
</html>