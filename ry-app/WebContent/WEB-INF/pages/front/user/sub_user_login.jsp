<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../comm/include.inc.jsp"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path;
%>

<!DOCTYPE html>
<html>
<head> 
<base href="${basePath}"/>
<%@include file="../comm/link.inc.jsp"%>

    <link rel="stylesheet" href="${oss_url}/static/front/css/user/login.css" type="text/css"></link>
    <link rel="stylesheet" href="/static/front/app/css/verify.css" />
<style type="text/css">
.login-body-full {
    background: url("/static/front/images/user/login-bg.png") no-repeat 50% 0;
    height: 760px;
    padding-top: 50px;
}
.btn-imgcode {
    top: 8px;
}
.container-full{
    min-height: auto !important;
}
.verify-box{
    position: fixed;
    top: 50%;
    left: 50%;
    width: 410px;
    height: 250px;
    transform: translate(-50%,-50%);
    -webkit-transform: translate(-50%,-50%);
    background: #fff;
    border: 1px solid #e6e6e6;
    border-radius: 4px;
    -webkit-border-radius: 4px;
    display: none;
    padding: 10px;
    z-index: 9999;
}
.verify-box-cover{
   position: fixed;
   top: 0;
   left: 0;
   width: 100%;
   height: 100%;
   background: rgba(0,0,0,0.5);
   display: none;
   z-index: 9998;
}
.login-box{
    position: relative;
}
.login-block{
    width: 380px;
    position: absolute;
    top: 180px;
    right: 0;
    color: #fff;
    left: auto;
}
.login-title{
    width: 490px;
    position: absolute;
    top: 230px;
    left: 0;
}
.login-bg {
    position: absolute;
    height: 100%;
    width: 100%;
    z-index: 0;
    background: #fff;
    border-radius: 6px;
    -webkit-border-radius: 6px;
    -moz-border-radius: 6px;
    -ms-border-radius: 6px;
    -o-border-radius: 6px;
}
.login-cn {
    position: relative;
    z-index: 1;
    padding: 25px;
    padding-bottom: 15px;
    color: #333;
}
.login .login-ipt {
    border-radius: 0;
    padding: 0 12px 0 45px;
    line-height: 40px;
    height: 40px;
}
.login .ipt-icon {
    width: 21px;
    height: 21px;
    position: absolute;
    top: 10px;
    left: 13px;
}
.login .btn-login{
    line-height: 40px;
    height: 40px;
}
.login .form-group{
    margin-bottom: 20px;
}
</style>
</head>
<body >
    <%@include file="../comm/headerIndex.jsp"%>

<script type="text/javascript">
var headerpath = window.location.pathname;
var selectMenu = "trade";
var lis = $(".nav li") ;
if(headerpath.startWith("/index.html") || selectMenu.startWith("index")
 || headerpath.startWith("/service/") || headerpath.startWith("/question/") || headerpath.startWith("/user/login.html")){
    lis.eq(0).addClass("white") ;
}
else if(headerpath.startWith("/trade/")){
    lis.eq(1).addClass("white") ;
}
else if(headerpath.startWith("/financial/")||headerpath.startWith("/account/") 
|| headerpath.startWith("/crowd/logs") || headerpath.startWith("/introl/")||headerpath.startWith("/divide/")
|| headerpath.startWith("/lottery/logs")|| headerpath.startWith("/exchange/index")|| headerpath.startWith("/shop/myorder")|| headerpath.startWith("/shop/address")){
    lis.eq(2).addClass("white") ;

}
else if(headerpath.startWith("/market.html")){
    lis.eq(3).addClass("white") ;
}
else if(headerpath.startWith("/user/")){
    lis.eq(4).addClass("white") ;
}
else if(headerpath.startWith("/about/")){
    lis.eq(5).addClass("white") ;
}
</script>

    <div class="container-full login-body-full main-con">
        <div class="container login-box">
            <!-- <img class="login-title" src="/static/front/images/user/login-title.png"> -->
            <div class="login-block login">
                <div class="login-bg"></div>
                <div class="login-cn">
                    <div class="form-group">
                        <h3 class="margin-top-clear login-cn-title">登录RYH钱包</h3>
                    </div>
                    <div class="form-group ">
                        <label class="ipt-icon acc" for="login-account"></label>
                        <input id="login-account" class="form-control login-ipt" placeholder="输入手机" type="email">
                    </div>
                    <div class="form-group ">
                        <label class="ipt-icon pas" for="login-password"></label>
                        <input id="login-password" class="form-control login-ipt" placeholder="输入密码" type="password">
                    </div>
                    <!-- <div class="form-group ">
                        <label class="ipt-icon pas" for="login-imgCode"></label>
                        <input id="login-imgCode" class="form-control login-ipt" type="text" class="phone-code" value="" placeholder="请输入验证码" />
                        <span ><img class="btn-imgcode" src="/servlet/ValidateImageServlet?r=1501754647473" ></span>
                    </div> -->
                    <div class="form-group ">
                            <span id="login-errortips" class="text-danger"></span>
                        </div>
                    <div class="form-group ">
                        <button id="login-submit" class="btn btn-danger btn-block btn-login">立即登录</button>
                    </div>
                    <div class="form-group login-reset">
                        <a href="${oss_url}/validate/reset.html">忘记密码？</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <div class="verify-box">
        <div id="verify"></div>
    </div>
    <div class="verify-box-cover"></div>
    
    <input id="forwardUrl" type="hidden" value="${forwardUrl }">
    
<%@include file="../comm/footer.jsp" %>
<script type="text/javascript" src="${oss_url}/static/front/js/user/login.js"></script>
<script type="text/javascript" src="/static/front/js/verify.js"></script>
<script type="text/javascript">
$(function(){
    var msg = '${forwardUrl}';
    if(msg != null && msg != ''){
        if (sessionStorage.pagecount) {
            sessionStorage.pagecount = Number(sessionStorage.pagecount) +1;
        } else {
            sessionStorage.pagecount = 1;
        }
        if(sessionStorage.pagecount == 1){
            layer.msg("您尚未登录或者登录凭证已经过期，请登录!");
        }
    } else {
        if (sessionStorage.pagecount) {
            sessionStorage.clear();
        }
    } 
})

$(".verify-box-cover").on('click',function(){
    $("body").css("overflow","auto");
    $(".verify-box").hide();
    $(".verify-box-cover").hide();
    $('#verify').empty();
});

$(".btn-imgcode").on("click", function() {
    this.src = "/servlet/ValidateImageServlet?r=" + Math.round(Math.random() * 100);
});

function verify(callback){
    $("body").css("overflow","hidden");
    $(".verify-box").show();
    $(".verify-box-cover").show();
    if($(".verify-img-panel").length < 1){
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
                $("body").css("overflow","auto");
                layer.msg(language["comm.tips.20"]);
                $(".verify-box").hide();
                $(".verify-box-cover").hide();
                if(callback) callback();
                $('#verify').empty();
            },
            error : function() {
                layer.msg(language["comm.tips.21"]);
                $(".verify-refresh").click();
            }
        });
    }
}
</script>
</body>
</html>
