<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="include.inc.jsp"%>
<style>
.top-fixed-nav {
    width: 100%;
    background: rgba(37, 44, 52, .97);
    height: 50px;
    padding-left: 22px;
    position: fixed;
    top: 0;
    left: 0;
    z-index: 999;
    min-width: 750px;
}
</style>
<script type="text/javascript">
 $(function(){
    var url="/index/ctcStatus1.html";
    $.post(url,{},function(data){
        $("#text-center").html("");
        $.each(data,function(key,value){
            $("#text-center").append("<div> <a href='${oss_url}/trade/coin.html?tradeType=0&coinType="+key+"' style='font-size: 14px;'>"+value.fShortName+"区</a></div>");
        });
    });
    $.getJSON("/index/ctcStatus.html",{},function(data){
        if(data.status == 'off'){
            $("#c2c").hide();
        }else{
            $("#c2c").show();
        }
    });
    var url="/index/ctcMenu.html";
    $.post(url,{},function(data){
    	$("#c2c-center").html("");
    	$.each(data,function(key,value){
    		$("#c2c-center").append("<div><a href='${oss_url}/otc/index.html?coinType="+key+"' style='font-size:14px;'>"+value.fShortName+"区</a></div>");
    	});
    });
}) 
</script>
<!--header部分-->
    <div class="container-full top-fixed-nav padding-right-clear">
        <a href="${oss_url}"><img  alt="RYH" src="${oss_url}/static/front/images/index/yshtb.png" class="logo"/></a>
        
        <ul class="left nav">
            <li class="">
                <a href="${oss_url}">首页</a>
            </li>
            <li class="">
                <a href="javascript:void(0);" scribe_scanned="true">币币交易</a>
                <div id="text-center" class="subNav text-center">
                
                </div>
            </li>
            <%-- <li id="c2c" class="">
                <a href="${oss_url}/ctc/index.html">C2C</a>
            </li> --%>
            <li class="">
                <a href="javascript:void(0);" scribe_scanned="true">C2C</a>
                <div id="c2c-center" class="subNav text-center"></div>
            </li>
            <!-- <li class="">
                <a href="https://otcbtc.com/" target="_blank">OTC</a>
            </li> -->
            <!-- <li class="">
                <a href="javascript:void(0);" scribe_scanned="true">OTC</a>
                <div class="subNav text-center">
                <div><a href="https://www.coincola.com/buy/USDT?country_code=CN" target="_blank" style="font-size: 14px;">购买USDT</a></div>
                <div><a href="https://www.coincola.com/sell/USDT?country_code=CN" target="_blank" style="font-size: 14px;">出售USDT</a></div>
                </div>
            </li> -->
            <li class="">
                <a href="${oss_url}/financial/index.html">财务</a>
            </li>
            <li class="">
                <a href="${oss_url}/trademarket.html">行情</a>
            </li>
            <li class="">
                <a href="${oss_url}/user/security.html">安全</a>
            </li>
            <li class="">
                <a href="${oss_url}/about/index.html">帮助</a>
            </li>
        </ul>
        <div id="lang" class="right" style="margin-right: 25px;">
            <span class="lang"><a class="zh_CN left" href="javascript:util.clang(0);"></a><em class="left">简体中文</em></span>
            <!-- <div class="lanague">
                <ul style="position: absolute;right:-35px; top:-131px;">
                    <li class="zh_CN">
                        <a href="javascript:util.clang(0);">简体中文</a>
                    </li>
                    <li style="border-bottom: 0;" class="en_US">
                        <a href="javascript:util.clang(1);">English</a>
                    </li>
                </ul>
            </div> -->
        </div>
        <c:if test="${sessionScope.login_user==null }">
            <div class="top-login right">
                <a href="${oss_url}/user/login.html" class="loginBTN">登录</a>
                <a href="${oss_url}/user/register.html" class="loginBTN">注册</a>
            </div>
        </c:if>
        <c:if test="${sessionScope.login_user!=null }">
            <div class="top-login right">
                <a href="${oss_url}/user/security.html" class="loginBTN">昵称:${login_user.fnickName}</a>
                <a href="${oss_url}/user/logout.html" class="loginBTN">退出</a>
            </div>
        </c:if>
        
    </div>  <!--end header-->
