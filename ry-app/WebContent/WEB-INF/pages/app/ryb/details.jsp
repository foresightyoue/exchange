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
        <title>RYB</title>
        <link rel="stylesheet" href="/static/front/app/css/swiper-3.4.2.min.css" />
        <script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js" ></script>
        <script type="text/javascript" src="/static/front/app/js/swiper-3.4.2.jquery.min.js" ></script>
        <script type="text/javascript" src="/static/front/app/js/rem1.js"></script>
        <script type="text/javascript" src="/static/front/app/js/vue.js"></script>
        <style>
* {
	margin: 0;
	padding: 0;
}

html, body {
	background-color: #EEEEEE;
	height: 100%;
}

ul, ol, dl, dd, dt {
	list-style: none;
	margin: 0;
	padding: 0;
}

a {
	color: #000;
	text-decoration: none;
}

.content {
	display: flex;
	flex-direction: column;
	width: 100%;
	height: 100%;
}

.content .head {
	padding: 0.265rem 0rem 0.256rem 0rem;
	background-color: #333333;
	font-size: 0.29rem;
	color: #FFFFFF;
}

.content .head .left {
	padding: 0rem 0rem 0rem 0.171rem;
	position: absolute;
	color: #FFFFFF;
}

.Personal-title {
	text-align: center;
}

.left_arrow {
	border-bottom: 0.04rem #FFFFFF solid;
	border-left: 0.04rem #FFFFFF solid;
	display: block;
	margin-top: 0.04rem;
	margin-left: 0.04rem;
	width: 0.275rem;
	height: 0.275rem;
	transform: rotate(45deg);
	-ms-transform: rotate(45deg);
	-moz-transform: rotate(45deg);
	-webkit-transform: rotate(45deg);
	-o-transform: rotate(45deg);
}

.content>.main {
	flex-grow: 1;
	overflow-y: scroll;
}

.content>.main>.block {
	background-color: #fff4e0;
	border-radius: 0 0 0.068rem 0.068rem;
	margin:0.085rem 0.132rem 0 0.094rem;
	font-size: 0.205rem;
	color: #333333;
}

.content>.main>.block:first-child {
	margin-top: 0.132rem;
}

.content>.main>.block>.row {
	height: 0.879rem;
	display: flex;
	flex-direction: column;
	padding-left: 0.077rem;
    padding-right: 0.077rem;
	position: relative;
}
.content>.main>.block>.row.more {
	height: 1.22rem;
}
.content>.main>.block>.row.important {
	background-color: #ffffff;
	color: #000000;
	border-radius: 0 0 0.068rem 0.068rem;
}

.content>.main>.block>.row>.title {
	font-size: 0.256rem;
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
.content>.main>.row{
    display: flex;
    justify-content: space-between;
    flex-direction: column;
    margin-top: 0.085rem;
    font-size: 0.239rem;
    color: #333333;
    height: 0.879rem;
    background-color: #ffffff;
    padding:0 0.094rem;
}
.content>.main>.row>.space {
	flex-grow: 1;
}
.content>.main>.row>.text {
	display: flex;
}
.content>.main>.row>.text>div {
	flex-grow: 1;
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
[v-cloak]{
	display:none;
}
        </style>
    </head>
    <body>
        <div class="content">
        	<div class="head">
        		<a href="javascript:history.go(-1);" class="left">
        			<i class="left_arrow"></i>
        		</a>
        		<div class="Personal-title">RYH</div>
        	</div>
        	<div id="sy" class="main" v-cloak>
        		<div class="block">
        			<div class="row more important">
        				<div class="space"></div>
        				<div class="composition">
        					<div class="text_center">锁仓金额</div>
        					<div class="text_center">{{wellet.currencyTotal?wellet.currencyTotal:0 | toFixed}}</div>
                            <div class="text_center">累积收益</div>
        					<div class="text_center">{{wellet.totalProfit?wellet.totalProfit:0 | toFixed}}</div>
        				</div>
        				<div class="space"></div>
        				<div class="composition">
        					<div class="text_center">昨日释放</div>
        					<div class="text_center">{{wellet.dayRelease?wellet.dayRelease:0 | toFixed}}</div>
                            <div class="text_center">未领取</div>
        					<div class="text_center">{{wellet.releaseCount?wellet.releaseCount:0 | toFixed}}</div>
        				</div>
        				<div class="space"></div>
        			</div>
        		</div>
                <div v-for="(item,i) in sydata" class="row">
                    <div class="space"></div>
                    <div class="text"><div class="text_left">收益</div><div class="text_right">+{{item.profit | toFixed}}</div></div>
                    <div class="space"></div>
                    <div class="text"><div>{{item.date}}</div></div>
                    <div class="space"></div>
                </div>
        		<div style="height: 0.8rem"></div>
        	</div>
        	<div class="menu"></div>
        </div>
<script type="text/javascript" src="${oss_url}/static/front/js/comm/util.js"></script>
<script type="text/javascript" src="/static/front/app/js/index.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/plugin/layer/layer.js"></script>
<script type="text/javascript">
$(function(){
    var welletId = '${welletId}';
    var wellet = '${wellet}';
    var list =new Vue({
        el: '#sy',
        data: {
            sydata: [],
            wellet: []
        },
        methods: {
            getKey: function(item) {
                var key;
                for (var keys in item) {
                  key = keys;
                }
                return key;
            },
            getvalue: function (item) {
                var value;
                for (var keys in item) {
                    value = item[keys];
                }
                return value;
            }
        },
        filters:{
        	toFixed:function(value){
        		return util.numFormat(value,6)
        	}
        }
    });
    $.post("/api/trading/getjson2.html",{"walletId":welletId,"uri":"/api/app/smallWalletProfitRecord"},function(data){
        console.log(data);
        list.sydata = data.data;
    });
    var type = '${type}';
    if(type == 'small'){
	    $.post("/api/trading/getjson2.html",{"walletId":wellet,"uri":"/api/app/walletEdits"},function(data){
	        list.wellet = data.data;
	        for(var i = 0;i<data.data.length;i++){
	            if(data.data[i].tid == welletId){
	                list.wellet = data.data[i];
	            }
	        }
	        console.log(list.wellet);
	    });
    }else if(type == 'big'){
    	$.post("/api/trading/getjson2.html",{"uri":"/api/app/userLockProject"},function(data){
            for(var i = 0;i<data.data.length;i++){
            	if(data.data[i].walletId == welletId){
            		list.wellet = data.data[i];
            	}
            }
        });
    }
});
</script>
<script type="text/javascript">
function iphoneX(type){
/*    if(type==iphoneX){
        $(".munu").css("height","4.5rem");
        $(".radio").css("bottom","4.5rem");
    }else{
        $(".munu").css("height","3.06rem");
        $(".radio").css("bottom","3.06rem");
    } */
}
</script>
</body>
</html>
