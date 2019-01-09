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
    <title>订单中心</title>
    <link rel="stylesheet" href="/static/front/app/css/css.css" />
    <link rel="stylesheet" href="/static/front/app/css/style.css" />
    <link rel="stylesheet" href="/static/front/app/css/swiper-3.4.2.min.css" />
    <script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js" ></script>
    <script type="text/javascript" src="/static/front/app/js/swiper-3.4.2.jquery.min.js" ></script>
    <script type="text/javascript" src="/static/front/js/layer/layer.js"></script>
    <style type="text/css">
        .exit_btn ul{
            height: 3.06rem;
            line-height: 3.06rem;
            font-size: 0.87rem;
            border-radius: 0.15rem;
            text-align: center;
            margin: 4rem 0.62rem;
            background: #186ed5;
            color: #fff;
        }
        .exit_btn{
            padding-left: 0;
            background-color: transparent;
            border: 0;
        }
        
        .bg-img01 ul li:nth-child(1){ background: url(${oss_url}/static/front/app/images/1img002.png) no-repeat;background-size: 1.25rem; background-position: center left;}
        .user-list li{
            border-bottom: 1px solid #e6e6e6 !important;
        } 
        .user-list{
            border: 0;
        }
        .nsel{
        	color: #999999;
        }
        .sel{
        	color: black;
        }
        .userInfo ul li {
		    height:5rem;
		    border-bottom:1px solid #efefef;
		}
		.userInfo ul li:last-child{
			border-bottom:none;
		}
		.userInfo ul li a{
			width:100%;
			height:100%;
			display:block;
			background:white;
		}
		.userInfo ul li a p:first-child{
			display:flex;
			width:100%;
			justify-content:space-between;
			align-items:center;
			padding:0 1rem;
			font-size:0.87rem;
			color:#000;
			height:2.5rem;
		}
		.userInfo ul li a p:last-child{
			display:flex;
			width:100%;
			justify-content:space-between;
			align-items:center;
			padding:0 1rem;
			font-size:0.87rem;
			color:#666;
			height:2.5rem;
		}
    </style>
</head>
<body>
<nav>
    <div class="Personal-title">
                <span>
                    <a href="javascript:history.go(-1);">
                        <em>
                            <i></i>
                            <i></i>
                        </em>
                        <strong>返回</strong>
                    </a>
                </span>
        余额
    </div>
</nav>
<section>
    <div class="user-list clearfix bg-img01">
        <ul>
            <li style="padding-left:0rem;border-bottom: 0px solid #e6e6e6 !important;">
               <a href="${oss_url}/m/service/ourService.html?id=1"></a>
               <p>最新公告!!!</p>
            </li>
        </ul>
    </div>
    <div style="background-color: #0099ff;height: 5rem;width: 100%;color: white;">
    	<p style="padding-top: 0.85rem;padding-left: 0.5rem;">余额账户（RYB）</p>
    	<strong style="padding-top: 0.5rem;padding-left: 0.5rem;position: absolute;">${fTotal }</strong>
    	<button onclick="chongzhi(${cointype})" style="color: wheat;background-color: #008000;height: 1.5rem;width: 3rem;position: relative;margin-right: 6rem;float: right;margin-top: 0.5rem;">充值</button>
    	<button onclick="changebiZhong(${cointype})" style="color: wheat;background-color: #999999;height: 1.5rem;width: 3rem;position: relative;margin-right: -7rem;float: right;margin-top: 0.5rem;">提现</button>
    </div>
    <div class="jilu" style="color: white;height: 3rem;">
    	<ul>
    		<li style="float:left;padding: 0.8rem;"><a href="javascript:void(0);" onclick="fvilog(0)" class="jl">明细</a></li>
    		<li style="float:left;padding: 0.8rem;"><a href="javascript:void(0);" onclick="fvilog(1)" class="jl">充值</a></li>
    		<li style="float:left;padding: 0.8rem;"><a href="javascript:void(0);" onclick="fvilog(2)" class="jl">提现</a></li>
    		<li style="float:left;padding: 0.8rem;"><a href="javascript:void(0);" onclick="fvilog(3)" class="jl">消费</a></li>
    	</ul>
    </div>
    <div class="userInfo clearfix">
        <ul style="display: none;" id="history">
            <li><a href='javascript:void(0);'>
            	<p><span id="type">充值</span><span id="change">+100</span></p>
            	<p><span id="time">2018-10-10 10:10</span><span id="after">1200</span></p>
            </a></li>
        </ul>
        <ul id="history1">
            
        </ul>
        <input type="hidden" id="coin" class="coin_id" name="coin_id" value="${cointype}">
    </div>
   
</section>
<%-- <jsp:include page="../comm/menu.jsp"></jsp:include> --%>
</body>
<script type="text/javascript" src="/static/front/js/comm/util.js"></script>
<script type="text/javascript" src="/static/front/js/comm/comm.js"></script>
<script type="text/javascript">
$(function(){
    $.each($(".jl"),function(i,n){
        if(i==0){
            $(n).css("color","black");
        }else{
            $(n).css("color","#999999");
        }
    });
    var coin = $("#coin").val();
    $.post("/m/security/gethistoty.html",{'cointype':coin},function(data){
        var str = '' ;
        var d = JSON.parse(data);
        for(var i=0;i<d.length;i++){
            if(d[i].change<0){
                $("#change").html(d[i].change);
            }else{
                $("#change").html("+"+d[i].change);
            }
            $("#time").html(d[i].fupdatetime);
            $("#after").html(d[i].ftotalafter);
            if(d[i].fPurpose ==1){
                $("#type").html("充值");
            }else if(d[i].fPurpose ==2){
                $("#type").html("提现");
            }else if(d[i].fPurpose ==3){
                $("#type").html("消费");
            }
            str += $("#history").html();
        }
        $("#history1").html(str);
    });
});
</script>
<script type="text/javascript">
function changebiZhong(value){
    window.location.href = "${oss_url}/m/account/withdrawBtc.html?symbol="+value;
};

function chongzhi(value){
    window.location.href = "${oss_url}/m/account/rechargeBtc.html?symbol="+value;
};
function fvilog(type){
    var coin = $("#coin").val();
    $.each($(".jl"),function(i,n){
        if(i==0){
            $(n).css("color","black");
        }else{
            $(n).css("color","#999999");
        }
    });
    $.post("/m/security/gethistoty.html",{'cointype':coin,'type':type},function(data){
        var str = '' ;
        var d = JSON.parse(data);
        for(var i=0;i<d.length;i++){
            if(d[i].change<0){
                $("#change").html(d[i].change);
            }else{
                $("#change").html("+"+d[i].change);
            }
            $("#time").html(d[i].fupdatetime);
            $("#after").html(d[i].ftotalafter);
            if(d[i].fPurpose ==1){
                $("#type").html("充值");
            }else if(d[i].fPurpose ==2){
                $("#type").html("提现");
            }else if(d[i].fPurpose ==3){
                $("#type").html("消费");
            }
            str += $("#history").html();
        }
        $("#history1").html(str);
    });
}
</script>
</html>