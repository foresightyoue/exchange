<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%><%@include file="../../front/comm/include.inc.jsp" %><%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%><style>
.menu {
    display: flex;
    background-color: #FFFFFF;
	flex:0 0 3.27rem;
}

.menu>div {
    display: flex;
    justify-content: center;
    width: 0;
    flex-grow: 1;
}

.menu>div>a {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    color: #999;
    font-size: 0.73rem;
}

.menu>div>a>div {
    width: 1.8rem;
    height: 1.8rem;
}

.hangqing_img {
    background-image: url(/static/front/app/images/menu/icon_hangqing.png);
    background-size: 100% 100%;
}

.OTC_img {
    background-image: url(/static/front/app/images/menu/icon_OTC.png);
    background-size: 100% 100%;
}

.caiwu_img {
    background-image: url(/static/front/app/images/menu/icon_caiwu.png);
    background-size: 100% 100%;
}

.geren_img {
    background-image: url(/static/front/app/images/menu/icon_geren.png);
    background-size: 100% 100%;
}

.hangqing_img.active {
    background-image: url(/static/front/app/images/menu/icon_hangqing_active.png);
}

.OTC_img.active {
    background-image: url(/static/front/app/images/menu/icon_OTC_active.png);
}

.caiwu_img.active {
    background-image: url(/static/front/app/images/menu/icon_caiwu_active.png);
}

.geren_img.active {
    background-image: url(/static/front/app/images/menu/icon_geren_active.png);
}
.menu>div>a.themeColor{
	color:#D7AC02;
}
</style>
<div data-href="${oss_url}/m/index.html?menuFlag=main">
    <a >
        <div class="hangqing_img <c:if test="${'main'==menuFlag}">active</c:if>"></div> 行情
    </a>
</div>
<div data-href="${oss_url}/m/otc/otcMenu.html?menuFlag=otc">
    <a >
        <div class="OTC_img <c:if test="${'otc'==menuFlag}">active</c:if>"></div> OTC
    </a>
</div>
<div data-href="${oss_url}/m/personal/assets.html?menuFlag=assets">
    <a >
        <div class="caiwu_img <c:if test="${'assets'==menuFlag}">active</c:if>"></div> 财务
    </a>
</div>
<div data-href="${oss_url}/m/financial/index.html?menuFlag=account">
    <a >
        <div class="geren_img <c:if test="${'account'==menuFlag}">active</c:if>"></div> 我的
    </a>
</div>
<script>
    var menuFlag = ${empty menuFlag};
    
    if (menuFlag) {
        $(".menu a").eq(0).find("div").addClass("active").parent().addClass('themeColor');
    }
    switch(location.href.split('=')[1]){
    	case 'main':$(".menu a").eq(0).find("div").addClass("active").parent().addClass('themeColor');
    	break;
    	case 'otc' :$(".menu a").eq(1).find("div").addClass("active").parent().addClass('themeColor');
    	break;
    	case 'assets':$(".menu a").eq(2).find("div").addClass("active").parent().addClass('themeColor');
    	break;
    	case "account":$(".menu a").eq(3).find("div").addClass("active").parent().addClass('themeColor');
    	break;
    }
    window.onload = function(){
        
        $(".menu>div").on('click',function(){
        // console.log(location.href.split('?')[1],'location.href');
        // console.log($(this).data('href').split('?')[1],'location.href');
        if(location.href.split('?')[1] == $(this).data('href').split('?')[1]){
            return
        }
        
        window.location.href = $(this).data('href');
        
        $(this).find("div").addClass('active').parent().addClass('themeColor');
        $(this).siblings().find("div").removeClass('active').parent().removeClass('themeColor');
    });
    }
    
</script>
