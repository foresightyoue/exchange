<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${requestScope.constant['webinfo'].ftitle }</title>
<link href="${oss_url}/static/ssadmin/js/themes/css/login.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js" ></script>
<style>
#sec-msg{
    color: red;
    font-size: 10px;
    margin-left: 22px;
}
</style>
<c:if test="${error != null}">
<script type="text/javascript">
alert("${error}") ;
</script>
</c:if>
<script type="text/javascript">
    if(window.location.pathname.indexOf("/ssadmin/index.html") > -1){
        alert("您的登录凭证已经过期，请重新登录!");
        window.location.reload();
    }
</script>
</head>
<body>
	<div id="login">
		<div id="login_header">
			<h1 class="login_logo">
				<a href="/"><img src="${oss_url}/static/ssadmin/js/themes/default/images/login_logo.gif" /></a>
			</h1>
			<div class="login_headerContent">
				<div class="navList">

				</div>
				<h2 class="login_title"><img src="${oss_url}/static/ssadmin/js/themes/default/images/login_title.png" /></h2>
			</div>
		</div>
		<script type="text/javascript">
			function validate(f){
				f.src = "/servlet/ValidateImageServlet?"+Math.random() ;
			}
		</script>
		<div id="login_content">
			<div class="loginForm">
				<form action="/ssadmin/d6d5d4d37164a3d0b363d0d64d1f782a.html" method="post">
					<p>
						<label>用户名：</label>
						<input type="text" name="name" id="name" size="20" class="login_input" />
					</p>
					<p>
						<label>密码：</label>
						<input type="password" name="password" size="20" class="login_input" />
					</p>
					<!-- <p>
						<label>谷歌验证：</label>
						<input type="text" name="google" size="20" class="login_input" />
					</p> -->
                    <p>
                        <div class="col-xs-6">
                            <input id="msgcode" name="msgcode" class="form-control" type="text" style="width: 69px;" placeholder="短信验证码">
                            <button id="bindphone-sendmessage" type="button" data-msgtype="2" onclick="sendSecurityCode(false)" class="btn btn-sendmsg" style="width: 88px;">发送验证码</button>
                        </div>
                    </p>
					<p id="sendVoice" style="display: none">
						<span class="codes" id="voice_t" style="margin-right: 16px; color: #333">收不到验证码？</span>
						<a href="javascript:sendSecurityCode(true)" class="codes" id="voice" style="color: #BA8C3E; text-decoration: none;width: 50px">发送语音</a>
					</p>
					<div id="sec-msg"></div>
					<p>
						<!-- <label>验证码：</label> -->
						<input class="code" type="text" size="5" name="vcode" style="width: 69px;" placeholder="验证码"/>
						<span><img src="/servlet/ValidateImageServlet" alt="" width="75" height="24" onclick="validate(this);"/></span>
					</p>
					<div class="login_bar">
						<input class="sub" type="submit" value=" " />
					</div>
					
				</form>
			</div>
			<div class="login_banner"><img src="${oss_url}/static/ssadmin/js/themes/default/images/login_banner.jpg" /></div>
			<div class="login_main">
				<ul class="helpList">

				</ul>
				<div class="login_inner">
					<p>实时短信提醒，确保用户账户和资金安全</p>
					<p>比特币钱包多层加密，离线存储，保障资产安全 </p>
					<p>HTTPS高级安全加密协议，客户资料全加密传输，防止通过网络泄漏 ……</p>
				</div>
			</div>
		</div>
		<div id="login_footer">
			${requestScope.constant['webinfo'].fcopyRights }
		</div>
	</div>
</body>
<script>
var countdown=120; 
function sendemail(){
var obj = $("#bindphone-sendmessage");
settime(obj);

}
function settime(obj) { //发送验证码倒计时
if (countdown == 0) { 
    obj.attr('disabled',false);
    obj.html("发送验证码");
    countdown = 120; 
    $("#sendVoice").css("display","");
    return;
} else { 
    obj.attr('disabled',true);
    obj.html(countdown + "s后重发");
    countdown--; 
} 
setTimeout(function() { 
settime(obj) }
,1000) 
}
function sendSecurityCode(sendVoice) {
    var url = "/ssadmin/sendmsg.html?random=" + Math.round(Math.random() * 100);
    var name=document.getElementById("name").value;
    var param = {
    		name : name,
    		'sendVoice' : sendVoice
    };
    jQuery.post(url, param, function(data) {
    	if (data.status) {
            sendemail();
            $("#sec-msg").text(data.message);
            $("#sendVoice").css("display","none");
        } else {
            alert(data.message);
        }
    }, "json");
}
</script>
</html>
