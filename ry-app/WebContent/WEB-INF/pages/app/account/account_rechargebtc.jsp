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
    <title>地址充值</title>
    <link rel="stylesheet" href="/static/front/app/css/css.css" />
    <link rel="stylesheet" href="/static/front/app/css/style.css" />
    <link rel="stylesheet" href="/static/front/app/css/swiper-3.4.2.min.css" />
    <script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js" ></script>
    <script type="text/javascript" src="/static/front/app/js/swiper-3.4.2.jquery.min.js" ></script>
    <style type="text/css">
    	#address_value{
			width: 100%;
			text-align: center;
    	}
    </style>
</head>
<body>
<nav>
    <div class="Personal-title">
                <span>
                    <a href="javascript:;" onClick="javascript :history.back(-1)">
                        <em>
                            <i></i>
                            <i></i>
                        </em>
                        <strong>返回</strong>
                    </a>
                </span>
        充值
        <p><a href="${oss_url}/m/account/rechargeBtcList.html?symbol=${symbol }">充值记录</a></p>
    </div>
</nav>
<section>
    <div class="pay-money">
        <p>充值地址</p>
        <!-- <input type="text" disabled="disabled" value="JUo32Eo15Xa8KJAGBfbNmhhwNSsWSw3Uf9" /> -->
    </div> 
    <div class="col-xs-12 padding-clear padding-top-30 recharge-qrcodetext">
        <div class="col-xs-7 recharge-qrcodecon address" style="text-align: center;line-height: 2rem;">
                <c:if test="${fvirtualaddress.fadderess != null}">
                <span class="code-box">
                    <span class="qrcode" id="qrcode"></span>
                </span>
                </c:if>
                <div class="code-txt" id="address">
                <c:if test="${fvirtualaddress.fadderess == null}">
                    <a class="getCoinAddress opa-link" href="javascript:void(0);" data-fid="${fvirtualcointype.fid }">获取地址</a>
                </c:if>
                <c:if test="${fvirtualaddress.fadderess != null}">
                     <input readonly="readonly" id="address_value" value="${fvirtualaddress.fadderess}">
                </c:if>
                </div>
        </div>
    </div>
    <div class="operation">
        <button class="active">点击复制充值地址</button>
        <p style="font-size: 1rem;">充值提示：</p>
        <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${fvirtualcointype.fregDesc }</p>
        <p>1.请勿向上述地址充值任何非<span>${fvirtualcointype.fShortName}</span>资产，否则资产将不可找回。</p>
        <p>2.您充值至上述地址后，需要整个网络节点的确认1次网络确认后到账，6次网络确认后可提币。</p>
        <p>3.最小充值金额:0.001<span>${fvirtualcointype.fShortName}</span>，小于最小金额的充值将不会上账。</p>
        <p>4.您的充值地址不会经常改变，可以重复充值;如有更改，我们会尽量通过网站公告或邮件通知您。</p>
        <p>5.请务必确认电脑及浏监器安全，防止信息被篡改或泄露。</p>
    </div>
</section>
</body>
<input type="hidden" id="address" value="${fvirtualaddress.fadderess}">
<input type="hidden" id="symbol" value="${fvirtualcointype.fid }">
<script type="text/javascript" src="${oss_url}/static/front/js/finance/account.recharge.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/plugin/jquery.qrcode.min.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/plugin/bootstrap.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/plugin/layer/layer.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/comm/util.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/comm/comm.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/language/language_cn.js"></script>
<script type="text/javascript">
    jQuery(document).ready(function() {
     if (navigator.userAgent.indexOf("MSIE") > 0) {
            jQuery('#qrcode').qrcode({
                text : '${fvirtualaddress.fadderess}',
                width : "149",
                height : "143",
                render : "table"
            });
        } else {
            jQuery('#qrcode').qrcode({
                text : '${fvirtualaddress.fadderess}',
                width : "149",
                height : "143"
            });
        }
    });
    $(function() {
        $(".active").on("click",function(){
            jsCopy();
        });
    });
    function jsCopy(){  
        var e=document.getElementById("address_value");//对象是copy-num1  
        e.select(); //选择对象  
        document.execCommand("Copy"); //执行浏览器复制命令  
        layer.msg("复制成功");;  
    }  
</script>

</html>