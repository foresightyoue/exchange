<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="include.inc.jsp"%>

<link href="${oss_url}/static/front/css/bitbs/webpack-public-style.css" media="screen" rel="stylesheet">
<style>
    .control-group .control-label{ margin-bottom: 8px;}

    .controls .controls-right{ right: 10px !important;}
    
    
    .modal{ border: none !important; background: none !important; overflow: visible !important; overflow-x: visible !important; overflow-y: visible !important; } 
    .modal-content{ margin-top: 0px !important;}
    
    .row{ margin: 15px auto 0px auto !important;}
    .container-full {
        min-width: 1170px !important;
        margin-top: 0 !important;
    }
    #nav_bar .nav>li>a {
        position: relative;
        display: block;
        padding: 0;
    }
    #nav_bar .nav>li>a:hover {
        background-color: transparent;
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
    <div class="container-full top-fixed-nav padding-right-clear header" id="nav_bar">
        <a href="${oss_url}"><img  alt="优生活" src="${oss_url}/static/front/images/index/yshtb.png" class="logo"/></a>
        <ul class="left nav">
            <li class="">
                <a href="${oss_url}">首页</a>
            </li>
            <li class="">
                <a href="javascript:void(0);" scribe_scanned="true">币币交易</a>
                <div id="text-center" class="subNav text-center a">
                
                </div>
            </li>
            <%--  <li id="c2c" class="">
                <a href="${oss_url}/ctc/index.html">C2C</a>
            </li> --%>
            <li class="">
                <a href="javascript:void(0);" scribe_scanned="true">C2C</a>
                <div id="c2c-center" class="subNav text-center"></div>
            </li>
            <!-- <li class="">
                <a href="https://otcbtc.com/" target="_blank">OTC</a>
            </li> -->
           <!--  <li class="">
                <a href="javascript:void(0);" scribe_scanned="true">OTC</a>
                <div class="subNav text-center b">
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
            <span class="langss"><a class="zh_CN left" href="javascript:util.clang(0);"></a><em class="left">简体中文</em></span>
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
    
    <%-- <div>
        <div class="ticker-bar">
            <div class="inner-col" style="width: 1170px !important;">
                <div class="pull-left korbit-ticker" style=" width: 50%; height: 20px; overflow: hidden; text-overflow:ellipsis; white-space: nowrap; color: #fff;text-align: right;">
                    <c:forEach items="${requestScope.constant['news']}" var="v" begin="0" end="0">
                        <a href="${v.url }" target="_blank" style="color: #fff;">${v.ftitle }</a>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div> --%>
