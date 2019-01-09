<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="include.inc.jsp"%>

<link href="${oss_url}/static/front/css/bitbs/webpack-public-style.css" media="screen" rel="stylesheet">
<!--foot-->
<style type="text/css">
    .div1{
        margin:auto;
        width:1000px;
        color: white;
        line-height: 26px;
    }
    .div1_ul{
        margin:0px;
        padding:0px;
        list-style: none;
        float:left;
        margin-right:100px;
    }
    .div1_ul li{
        color:#959FA5;
    }
    .div1_ul a{
        color:#959FA5;
        font-size:13px;
    }
    div p img{
        marign-left:20px;
    }
    .largeImg{
        padding: 10px;
        position: absolute;
        top: 58px;
        right: -5px; 
        background-color: white;
        display: none; 
    }
    .triangle{
        height: 0px;
        width: 0px;
        border-top: 8px solid transparent;
        border-bottom: 10px solid white;
        border-left: 8px solid transparent;
        border-right: 8px solid transparent;
        position: absolute;
        top: -18px;
        left: 46%;
    }
    .smallImg{
         width: 40px;
         margin-top: 7px;
         margin-left: 10px;
    }
</style>
<footer class="container-full">
    <div class="div1">
        <ul class="div1_ul">
            <li><img alt="" width=180 height=30 src="/static/front/images/index/yshtb.png"/></li>
        </ul>
        <ul class="div1_ul">
            <li><h4>下载支持</h4></li>
            <li><a href="${oss_url}/about/index.html?id=81">API文档</a></li>
            <li><a href="${oss_url}/about/index.html?id=83">APP下载</a></li>
           <!--  <li><a href="/about/index.html?id=84">其他下载</a></li> -->
        </ul>
        <ul class="div1_ul">
            <li><h4>客户服务</h4></li>
            <li><a href="${oss_url}/about/index.html?id=1">使用教程</a></li>
            <li><a href="${oss_url}/about/index.html?id=90">常见问题</a></li>
            <li><a href="${oss_url}/about/index.html?id=4">用户协议</a></li>
            <li><a href="${oss_url}/about/index.html?id=2">费率标准</a></li>
        </ul>
        <ul class="div1_ul">
            <li><h4>其他</h4></li>
            <li><a href="${oss_url}/service/ourService.html?id=1&menuFlag=article">官方公告</a></li>
            <li><a href="${oss_url}/about/index.html?id=91">上币申请</a></li>
        </ul>
        <ul class="div1_ul" style="margin-right:0px;">
            <li><h4>联系我们</h4></li>
            <li>服务邮箱：service@RYHcoin.net</li>
            <li style="text-align: left;">业务邮箱：info@RYHcoin.net</li>
            <li style="text-align: left;position: relative;">
                <span style="float: left;">客服QQ群：</span>
                <img alt="" src="/static/front/images/er.png" class="smallImg">
                <div class="largeImg"><img alt="" src="/static/front/images/er.png" style="width: 150px;"><span class="triangle"></span></div>
            </li>
        </ul>
        <ul style="clear: both"></ul>
        <hr style="height:2px;border:none;border-top:1.5px solid #30404A">
    </div>
    <div class="div1">
        <p>
        <b style="color:#959FA5;font-size: 16px;">友情链接</b>&nbsp;&nbsp;&nbsp;
        <img width="80" height="20" src="/static/front/images/index/JINSE.png"/>&nbsp;&nbsp;&nbsp;
        <img width="80" height="20" src="/static/front/images/index/ETC.png"/>&nbsp;&nbsp;&nbsp;
        <img width="80" height="20" src="/static/front/images/index/Bitkan.png"/>&nbsp;&nbsp;&nbsp;
        <img width="80" height="20" src="/static/front/images/index/BTC123.png"/>&nbsp;&nbsp;&nbsp;
        <img width="80" height="20" src="/static/front/images/index/BOTVS.png"/>&nbsp;&nbsp;&nbsp;
        <img width="80" height="20" src="/static/front/images/index/ETHFANS.png"/>&nbsp;&nbsp;&nbsp;
        <img width="80" height="20" src="/static/front/images/index/CHAINFOR.png"/>&nbsp;&nbsp;&nbsp;
        <img width="80" height="20" src="/static/front/images/index/AIcoin.png"/>&nbsp;&nbsp;&nbsp;
        <img width="80" height="20" src="/static/front/images/index/FEIXIAOHAO.png"/>&nbsp;&nbsp;&nbsp;
        </p>
    </div>
    <p class="foot_logo"><img src="${oss_url}/static/front/images/index/yshtb.png" height="24"></p>
    <p class="copyright"><span style="color:#999;">2015-2018 RYH All Rights Reserved.</span><br>
        <a href="${oss_url}/about/index.html?id=5">使用条款</a>
    
        <a href="${oss_url}/about/index.html?id=3">法律声明</a>
    
        <a href="${oss_url}/about/index.html?id=2">费率标准</a>
    
        <a href="${oss_url}/about/index.html?id=1">如何注册</a>
        
    <!-- <a target="_blank" href="/about/index.html?id=88">客服中心</a> -->
        <a target="_blank" href="${oss_url}/question/question.html">客服中心</a>
    </p>
</footer> 
<!--end foot-->
<script type="text/javascript" src="${oss_url}/static/front/js/plugin/bootstrap.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/plugin/layer/layer.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/comm/util.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/comm/comm.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/language/language_cn.js"></script>
<script>
    $(function(){
        $("body").on("mouseover",".smallImg",function(){
            $(".largeImg").show();
        });
        $("body").on("mouseout",".smallImg",function(){
            $(".largeImg").hide();
        })
    })
</script>
