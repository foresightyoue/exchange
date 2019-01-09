<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
<base href="${basePath}"/>
<title>安全校验</title>
<link rel="stylesheet" href="/static/front/css/security.css" />
<script type="text/javascript"
	src="/static/front/app/js/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="/static/front/js/rem1.js"></script>
</head>
<body>
	<div class="pcMars"></div>
	<div class="container">
		<nav>
			<div class="Personal-title">安全验证</div>
		</nav>
		<p class="promptMessage">系统检测到您当前环境发生变化，需要认证安全手机!</p>
		<form method="post" action="${action}">
			<%@ include file="SecurityVerify.jspf"%>
			<%-- <c:if test="${not empty message}">
	        <div style="padding:.73rem;color:red;text-align:left;font-size:.87rem;">${message}</div>
	    </c:if> --%>
			<button id="sure" name="checkSecurity">确认</button>
			<a href="" class="indexPage">首页</a>
		</form>

	</div>
	<script>
	$(function(){
	if( /Android|webOS|iPhone|iPad|iPod|BlackBerry/i.test(navigator.userAgent) ) {
		// 执行相应代码或直接跳转到手机页面
		//  alert(111);
		  $(".indexPage").prop("href","/m/index.html");
		} else {
		// 执行桌面端代码
		//	alert(2222)
			  $(".indexPage").prop("href","/index.html");
		}
	});
   	$(function(){
   	/* var browser={  
	    versions:function(){   
	           var u = navigator.userAgent, app = navigator.appVersion;   
	           return {//移动终端浏览器版本信息   
	                trident: u.indexOf('Trident') > -1, //IE内核  
	                presto: u.indexOf('Presto') > -1, //opera内核  
	                webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核  
	                gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核  
	                mobile: !!u.match("AppleWebKit.Mobile"), //是否为移动终端  
	                ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端  
	                android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或者uc浏览器  
	                iPhone: u.indexOf('iPhone') > -1 , //是否为iPhone或者QQHD浏览器  
	                iPad: u.indexOf('iPad') > -1, //是否iPad    
	                webApp: u.indexOf('Safari') == -1, //是否web应该程序，没有头部与底部  
	                weixin: u.indexOf('MicroMessenger') > -1, //是否微信   
	                qq: u.match(/\sQQ/i) == " qq" //是否QQ  
	            };  
	         }(),  
	         language:(navigator.browserLanguage || navigator.language).toLowerCase()  
	}    */
	  
	  /* if(browser.versions.mobile || browser.versions.ios || browser.versions.android ||   
	    browser.versions.iPhone || browser.versions.iPad){        
	        alert(111);
		  $(".indexPage").prop("href","/m/index.html");
	        
	  }else{
		  alert(2222)
		  $(".indexPage").prop("href","/index.html");
	  } */
	    if(window.location.pathname.indexOf("ssadmin") > -1){
		    $(".indexPage").prop("href","/ssadmin/index.html");
	  }
   	}) 
   	</script>
</body>
</html>