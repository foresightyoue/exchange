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
        <title></title>
		<link rel="stylesheet" type="text/css" href="/static/front/app/css/new_common.css"/>
        <link rel="stylesheet" href="/static/front/app/css/swiper-3.4.2.min.css" />
        <script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js" ></script>
        <script type="text/javascript" src="/static/front/app/js/swiper-3.4.2.jquery.min.js" ></script>
		<script type="text/javascript" src="/static/front/app/js/rem.js"></script>
        
        <style>
.content>.main>.trisection {
	display: flex;
	height: 6.167rem;
	background-color: #ffffff;
	font-size: 1rem;
	color: #333333;
}

.content>.main>.trisection>.part {
	flex-grow: 1;
	width: 0;
	display: flex;
	flex-direction: column;
	text-align: center;
	justify-content: space-between;
	text-align: center;
}

.content>.main>.trisection>.part:before, .content>.main>.trisection>.part:after
	{
	content: "";
	display: block;
}

.content>.main>.trisection>.part>.line {
	height: 1px;
	background-color: #dfdfdf;
}

.content>.main>.trisection>.part.btn {
	border-left: 1px solid #dfdfdf;
}

.content>.main>.trisection>.part>.btn {
	padding: 0.234rem 1.601rem;
	background-color: #eba51d;
	border-radius: 0.332rem;
	color: #ffffff;
	margin: auto;
}
.content>.main>.block {
	background-color: #fff4e0;
	border-radius: 0 0 0.265rem 0.265rem;
	margin: 0.332rem 0.398rem 0 0.367rem;
	font-size: 0.800rem;
	color: #333333;
}

.content>.main>.block:first-child {
	margin-top: 0.667rem;
}

.content>.main>.block>.row {
	height: 3.433rem;
	display: flex;
	flex-direction: column;
	padding-left: 0.300rem;
    padding-right: 0.300rem;
	position: relative;
}
.content>.main>.block>.row.more {
	height: 4.765rem;
}
.content>.main>.block>.row.important {
	background-color: #492c22;
	color: #ffffff;
	border-radius: 0 0 0.265rem 0.265rem;
}
.content>.main>.block>.row.button:after{
	content: "详情";
	background-color: #EBA51D;
	padding:0.265rem 0.601rem 0.234rem 0.632rem;
	right: 1rem;
	top: 0.800rem;
	position: absolute;
	border-radius: 0.332rem;
	flex-grow: 1;
	text-align: right;
	color: #FEFEFE;
	display: block;
}
.content>.main>.block>.row>.title {
	font-size: 1rem;
}

.content>.main>.block>.row>.title.important {
	color: #e34a06;
}

.content>.main>.block>.row>.space {
	flex-grow: 1;
}
.content>.main>.block>.row>.composition{
    display: flex;
}
.content>.main>.block>.row>.composition>div{
    width: 0;
    flex-grow: 1;
}
.content>.main>.block>.row>.important_text {
	color: #eba51d;
    display: flex;
}
.content>.main>.block>.row>.important_text>.normal {
	font-size: 0.800rem;
	color: #ffffff;
    flex-grow: 1;
    text-align: right;
}

.text_left{
    text-align: left;
}
.text_center{
    text-align: center;
}
.text_right{
    text-align: right;
}
</style>
    </head>
    <body>
		<div class="content">
			<div class="head">
				<a href="/m/personal/assets.html?menuFlag=assets" class="left">
					<i class="left_arrow"></i>
				</a>
				<div class="Personal-title">RYB钱包</div>
			</div>
			<div class="main">
				<div class="trisection">
					<div class="part">
						<div class="text">自由RYB总数</div>
						<div class="line"></div>
						<div class="text">冻结RYB总数</div>
						<div class="line"></div>
						<div class="text">释放RYB总数</div>
					</div>
					<div class="part">
						<div class="text">00000.00</div>
						<div class="line"></div>
						<div class="text">00000.00</div>
						<div class="line"></div>
						<div class="text">00000.00</div>
					</div>
					<div class="part btn">
						<div class="btn">转账</div>
					</div>
				</div>
                <div class="block">
                	<div class="row">
                		<div class="space"></div>
                		<div class="title important">锁仓210天&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(RYB)</div>
                		<div class="space"></div>
                        <div>钱包名显示</div>
                        <div class="space"></div>
                	</div>
                	<div class="row more important">
                        <div class="space"></div>
                		<div class="composition">
                            <div class="text_left">首次锁仓金额</div>
                            <div class="text_center">1000.00</div>
                            <div class="text_right">已释放100天</div>
                        </div>
                		<div class="space"></div>
                		<div class="composition">
                			<div class="text_left">09-28释放</div>
                			<div class="text_center">2018-12-31到期</div>
                			<div class="text_right">今日释放000.00</div>
                		</div>
                		<div class="space"></div>
                        <div class="composition">
                        	<div class="text_left">累积释放49.00</div>
                        	<div class="text_center">累积收益49.00</div>
                        	<div class="text_right"></div>
                        </div>
                        <div class="space"></div>
                    </div>
                </div>
                <div class="block">
                	<div class="row">
                		<div class="space"></div>
                		<div class="title important">锁仓210天&nbsp;&nbsp;&nbsp;（RYB）</div>
                		<div class="space"></div>
                        <div>钱包名显示</div>
                        <div class="space"></div>
                	</div>
                	<div class="row more important">
                		<div class="space"></div>
                		<div class="composition">
                			<div class="text_left">首次锁仓金额</div>
                			<div class="text_center">1000.00</div>
                			<div class="text_right">已释放100天</div>
                		</div>
                		<div class="space"></div>
                		<div class="composition">
                			<div class="text_left">09-28释放</div>
                			<div class="text_center">2018-12-31到期</div>
                			<div class="text_right">今日释放000.00</div>
                		</div>
                		<div class="space"></div>
                		<div class="composition">
                			<div class="text_left">累积释放49.00</div>
                			<div class="text_center">累积收益49.00</div>
                			<div class="text_right"></div>
                		</div>
                		<div class="space"></div>
                	</div>
                </div>
                <div class="block">
                    <div class="row">
                        <div class="space"></div>
                        <div class="title">精品推荐1</div>
                        <div class="space"></div>
                    </div>
                    <div class="row important">
                    	<div class="space"></div>
                    	<div>锁仓产品01（210天）</div>
                    	<div class="space"></div>
                        <div class="important_text">目标金额 1000.000.00起购<div class="normal">已完成45%</div></div>
                        <div class="space"></div>
                    </div>
                </div>
				<div class="obligate"></div>
			</div>
			<div class="menu"></div>
		</div>
<script type="text/javascript" src="${oss_url}/static/front/js/comm/util.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/plugin/layer/layer.js"></script>
<script type="text/javascript">
$(function(){
    var msg = '${msg}';
    if(msg != null && msg != ''){
        layer.msg(msg);
    }
})
</script>
</body>
</html>