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
    <title>拆分钱包</title>
    <!-- <link rel="stylesheet" href="/static/front/app/css/css.css" />
    <link rel="stylesheet" href="/static/front/app/css/style.css" /> -->
    <link rel="stylesheet" href="/static/front/app/css/swiper-3.4.2.min.css" />
    <script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js" ></script>
    <script type="text/javascript" src="/static/front/app/js/swiper-3.4.2.jquery.min.js" ></script>
    <script type="text/javascript" src="/static/front/js/layer/layer.js"></script>
    <script type="text/javascript" src="/static/front/app/js/rem1.js"></script>
    <script type="text/javascript" src="/static/front/js/comm/util.js"></script>
    <script type="text/javascript" src="/static/front/app/js/vue.js"></script>
    <style type="text/css">
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

.content>.head {
	padding: 0.265rem 0rem 0.256rem 0rem;
	background-color: #333333;
	font-size: 0.29rem;
	color: #FFFFFF;
}

.content>.head>.left {
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
.content>.main>.info{
    display: flex;
    flex-wrap: wrap;
    color: #ffffff;
    font-size: 0.29rem;
    background-color: #eba51d;
	border-radius: 0.068rem;
    margin: 0.162rem 0.102rem 0 0.094rem;
    padding: 0.147rem 0.247rem;
    line-height: 0.5rem;
}
.content>.main>.info>div{
	flex:0 0 50%;
	text-align:center;
}
.content>.main>form>.row{
    padding-left: 0.666rem;
    padding-right: 0.666rem;
    background-color: #ffffff;
    color: #333333;
    margin-top: 0.154rem;
}
.content>.main>form>.row>.field{
    display: flex;
    line-height: 0.8rem;
}
.content>.main>form>.row>.field[v-if]{
    display: none;
}
.content>.main>form>.row>.field>.left{
    width: 1.3rem;
    font-size: 0.29rem;
}
.content>.main>form>.row>.field>.qian{
    font-size: 0.239rem;
}
.content>.main>form>.row>.field>.fill{
    flex-grow: 1;
    text-align: center;
    font-size: 0.239rem;
    display:flex;
    align-items:center;
}
.content>.main>form>.row>.field>.fill>input{
	height:50%;
}
.content>.main>form>input{
    margin: 0.35rem auto;
    width: 5.12rem;
    background-color: #eba51d;
	border-radius: 0.102rem;
    text-align: center;
    line-height: 0.717rem;
    color: #ffffff;
    font-size: 0.256rem;
    display: block;
    border-style: hidden;
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
    		<div class="Personal-title">拆分钱包</div>
    	</div>
    	<div id="form" class="main" v-cloak>
            <div class="info">
                <div v-if="userLock.projectName">{{userLock.projectName}}</div>
                <div v-if="userLock.endRelaseDate">{{userLock.endRelaseDate}}到期</div>
                <div>锁仓金额:{{userLock.currencyTotal ? userLock.currencyTotal:'0' | toFixed}}</div>
            </div>
            <form action="" method="post">
                <div class="row">
                    <div class="field"><div class="left">分配方式</div><div class="fill"><input checked="checked" type="radio" name="way" value="0" v-model="picked"/><label for="way">平均分配</label></div><div class="fill"><input type="radio" name="way" value="1" v-model="picked"/><label for="way">自定义分配</label></div></div>
                    <template v-if="picked=='1'">
                        <div v-for="n in number" class="field"><div class="qian left">子钱包{{n}}</div><div class="fill"><input type="text" :name="'num'+n" :id="'num'+n" value="" /></div></div>
                    </template>
                    <div class="field" key="num-input"><div class="left">拆分数量</div><div class="fill"><input v-model="number" type="number" id="num" max="50" value="0" /></div></div>
                </div>
            	<input type="submit" value="确定"/>
            </form>
    		<div style="height: 0.8rem"></div>
    	</div>
    	<div class="menu"></div>
    </div>
</body>
<script type="text/javascript" src="/static/front/js/comm/util.js"></script>
<script type="text/javascript" src="/static/front/js/comm/comm.js"></script>
<script type="text/javascript">
$(function(){
    var msg = '${msg}';
    if (msg != null && msg != '') {
    	layer.msg(msg);
    }
    var select=new Vue({
    	el: '#form',
    	data: {
    		picked: "0",
            number: 0 ,
            userLock : []
    	},
        watch: {
            number: function (newNumber, oldNumber) {
                if(newNumber==""){
                    return;
                }
                if(isNaN(parseInt(newNumber,10))){
                	this.number=oldNumber;
                }else{
                	this.number=parseInt(newNumber,10);
                }
            }
        },
        filters:{
        	toFixed:function(value){
        		return util.numFormat(value,6)
        	}
        }
    });
    var walletId = '${walletId}';
    var wellet = '${wellet}';
    var Wellettype = '${Wellettype}';
    if(Wellettype == 'big'){
	    $.post("/api/trading/getjson2.html",{"uri":"/api/app/userLockProject"},function(data){
	        for(var i = 0;i<data.data.length;i++){
	        	if(data.data[i].walletId == walletId){
	        		select.userLock = data.data[i];
	        	}
	        }
	    });
    }else if (Wellettype == 'small'){
    	$.post("/api/trading/getjson2.html",{"walletId":wellet,"uri":"/api/app/walletEdits"},function(data){
            console.log(data);
            for(var i = 0;i<data.data.length;i++){
                if(data.data[i].tid == walletId){
                	select.userLock = data.data[i];
                }
            }
        });
    }
    $("form").submit(function(e){
        e.preventDefault();
        if (select.picked== "1") {
            var arr = new Array(select.number);
            for (var i = 1; i <= select.number; i++) {
            	arr[i-1] = parseFloat($("#num"+i).val());
            }
            console.log(arr.join());
        	$.post("/api/trading/getjson2.html",{"walletId":"${walletId}","type":"2","num":select.number,"coin":arr.join(),"uri":"/api/app/splitWallet"},function(data){
        		console.log(data);
        		if(data.status!=200){
                    layer.msg(data.msg);
                }else{
                    layer.msg(data.data);
	        		setTimeout(window.location.href = "javascript:history.go(-1);",3000);
                }
        	});
        } else{
        	$.post("/api/trading/getjson2.html",{"walletId":"${walletId}","type":"1","num":select.number,"uri":"/api/app/splitWallet"},function(data){
        		console.log(data);
        		if(data.status!=200){
                    layer.msg(data.msg);
                }else{
                    layer.msg(data.data);
	                setTimeout(window.location.href = "javascript:history.go(-1);",3000);
                }
        	});
        }
        
    });
});
</script>
</html>