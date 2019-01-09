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
    <title>邮箱认证</title>
    <link rel="stylesheet" href="/static/front/app/css/css.css" />
    <link rel="stylesheet" href="/static/front/app/css/style.css" />
    <link rel="stylesheet" href="/static/front/app/css/swiper-3.4.2.min.css" />
    <style>
    	.Personal-cont ul li{
    		position:relative;
    	}
    	.Personal-cont ul li>
    	.Personal-cont ul li p input{
    		width:70%;
    	}
        .Personal-title span em{
        width: 1.2rem;
        height: 1.2rem;
        position: absolute;
        top: 0.65rem;
        left: 0;
    }
    .Personal-title span em i:last-child{
        left:0.25rem;
    }
    </style>
    <script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js" ></script>
    <script type="text/javascript" src="/static/front/app/js/swiper-3.4.2.jquery.min.js" ></script>
</head>
<body style="background: #fff;">
<nav>
    <div class="Personal-title">
				<span>
					<a href="javascript:;" onClick="javascript :history.back(-1)">
						<em>
							<i></i>
							<i></i>
						</em>
						<strong><!-- 返回 --></strong>
					</a>
				</span>
        邮箱认证 
    </div>
</nav>
<section>
    <c:if test="${isBindEmail == true }">
        <div class="Personal-cont">
            <p><img src="/static/front/app/images/succeed.png"></p>
            <h2 class="clearfix">已绑定邮箱</h2>
            <ul class="clearfix">
                <li><span>邮  箱：</span>	${email}</li>
            </ul>
        </div>
    </c:if>
    <c:if test="${isBindEmail == false }">
        <div class="Personal-cont">
            <p><img src="/static/front/app/images/no_pass.png"></p>
            <h2 class="clearfix">未绑定邮箱</h2>
            <ul class="clearfix">
                <li><span>邮箱地址：</span><p><input type="email" id="bindemail-email" placeholder="请输入您的邮箱地址"></p><span class='sdEmailCode'>发送验证码</span></li>
                 <li><span>验证码：</span><p><input type="email" id="emial-verify-code" placeholder="请输入验证码"></p></li>
            </ul>
        </div>
        <div class="next-padding">
            <div class="next next-active">确认绑定</div>
        </div>
    </c:if>


</section>
</body>
<script type="text/javascript" src="/static/front/js/comm/util.js"></script>
<script type="text/javascript" src="/static/front/js/language/language_cn.js"></script>
<script type="text/javascript" src="/static/front/js/layer/layer.js"></script>
<script type="text/javascript" src="/static/front/js/comm/comm.js"></script>

<script type="text/javascript" src="${oss_url}/static/front/js/comm/msg.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/plugin/jquery.qrcode.min.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/user/user.security.js"></script>
<script type="text/javascript">
    $(".next-padding").on("click", function() {
        var email = $("#bindemail-email").val();
        var code=$('#emial-verify-code').val();
        var reg=/^\d{4,8}$/g;
        var flag = false;
        if (email.indexOf(" ") > -1) {
            layer.msg( language["comm.error.tips.11"]);
            return;
        }
        if (email == '') {
            layer.msg( language["comm.error.tips.7"]);
            return;
        }
        if (!util.checkEmail(email)) {
            layer.msg( language["comm.error.tips.16"]);
            return;
        }
        if(!reg.test(code)){
        	layer.msg('请输入正确的邮箱验证码！');
        	return
        }
        var url = "/validate/postValidateMail.html?random=" + Math.round(Math.random() * 100);
        $.post(url, {
            email : email
        }, function(data) {
            if (data.code == 0) {
                layer.msg(data.msg);
                setTimeout(function(){location.reload(true)},3000);
            } else {
                layer.msg(data.msg);
                setTimeout(function(){location.reload(true)},3000);
                /* location.reload(true); */
            }
        }, "json");
    });
</script>

</html>