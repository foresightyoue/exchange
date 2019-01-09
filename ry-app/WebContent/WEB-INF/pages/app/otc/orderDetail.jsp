<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat"
	pageEncoding="UTF-8"%>
<%@page import="cn.cerc.jdb.core.TDateTime"%>
<%@page import="java.util.Date"%>
<%@include file="../../front/comm/include.inc.jsp"%>
<%
     String path = request.getContextPath();
         String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
         + path;
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <base href="${basePath}"/>
    <title>订单详情</title>
<!--     <link rel="stylesheet" href="/static/front/css/c2c/safe.css">
    <link rel="stylesheet" href="/static/front/css/c2c/module.common.css">
    <link rel="stylesheet" href="/static/front/css/c2c/module.base.css"> -->
    <link rel="stylesheet" href="/static/front/js/layim-v3.7.7/src/css/layui.mobile.css" media="all">
    <!-- <link href="//netdna.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="//at.alicdn.com/t/font_48446_inzlc0gmji3sor.css" rel="stylesheet"> -->
    <link href="/static/front/app/js/c2c/common.css" rel="stylesheet">
    <link href="/static/front/app/js/c2c/otc-app.css" rel="stylesheet">
    <style>
        .layui-layer.layui-layer-page.layui-box.layui-layim-chat{
            width: 90% !important;
            left: 5% !important;
            min-width: 90% !important;
            min-height: 300px !important;
            /* top: 18% !important; */
        }
        .layim-chat-other img {
            width: .425rem !important;
            height: .425rem !important;
        }
        body .layui-layim-chat .layui-layer-title{
            height: .68rem !important;
        }
        .layim-chat-title {
            top: -0.8rem !important;
            height: .68rem !important;
        }
        .layim-chat-title .layim-chat-username {
            top: 2px;
            font-size: 14px;
        }
        .layim-chat-title .layim-chat-other {
            padding-left: 40px;
        }
        .layim-chat-textarea {
            margin-right: 10px;
        }
        .layim-send-set{
            display: none !important;
        }
        .layim-send-btn{
            border-radius: 3px !important;
            -webkit-border-radius: 3px !important;
            -moz-border-radius: 3px !important;
            -ms-border-radius: 3px !important;
            -o-border-radius: 3px !important;
        }
        .layim-chat-bottom {
            height: 35px !important;
        }
        .layim-chat-send span {
            line-height: 28px !important;
        }
        .layim-chat .layim-chat-main {
            height: 160px;
        }
        .layim-chat .layim-chat-tool {
            height: 30px;
            line-height: 30px;
        }
        .layim-chat-footer .layim-chat-textarea textarea{
            padding: 0 0 0 0;
            height: 40px;
        }
        .layui-layer-max{
            display: none !important;
        }
    </style>
</head>
<body>
<div class="header">
<c:if test="${fw == 0}">
        <a href="/m/otc/orderList.html?coinType=${coin_id}" class="prev"></a>
    </c:if>
    <c:if test="${fw == 1}">
        <a href="/m/otc/orderShellList.html?coinType=${coin_id}" class="prev"></a>
    </c:if>
    <div class="head_title font15">订单支付</div>
    <!--<div class="more more-z">设置</div>-->
</div>
<div id="mat"></div>
<div class="payOrder_details">
    <input type="hidden" value="${orderD.items.fOrderId }" name="orderId">
    <input type="hidden" value="${orderD.items.fAd_Id }" name="fAd_Id">
    <input type="hidden" value="${fShortName }" name="fShortName">
    <input type="hidden" value="${fw }" name="fw">
    <div class="flexLayout order_num border_b">
        <div class="font13">订单号  ${orderD.items.fOrderId }</div>
        <div class="font12 examine">
            <c:if test="${orderD.items.fTrade_Status=='1' }">已下单</c:if> 
            <c:if test="${orderD.items.fTrade_Status=='2' }">待验证</c:if> 
            <c:if test="${orderD.items.fTrade_Status=='3' }">已完成 </c:if> 
            <c:if test="${orderD.items.fTrade_Status=='4' }">已取消</c:if> 
        </div>
    </div>
    <div class="font11 text-gray end_date pl_10">付款到期：<span id="fTrade_DeadTime"><fmt:formatDate value="${orderD.items.fTrade_DeadTime }" pattern="yyyy-MM-dd HH:mm"></fmt:formatDate></span></div>
    <div class="flexLayout order_data">
        <div class="font11">交易金额</div>
        <div class="tc font11">交易数量</div>
        <div class="tr font11">单价</div>
    </div>
    <div class="flexLayout order_data" style="margin-top: .1rem;">
        <div class="font11">${orderD.items.fTrade_SumMoney }</div>
        <div class="tc font11">${orderD.items.fTrade_Count }</div>
        <div class="tr font11">${orderD.items.fTrade_Price }</div>
    </div>
    <div class="flexLayout order_sum mt_20 pr_10 box-sizing">
        <div class="font13"></div>
        <div class="font11">总额：<span class="text-yellow font14">${orderD.items.fTrade_SumMoney } CNY</span></div>
        <input type="hidden" name="SumMoney_" value="${orderD.items.fTrade_SumMoney }">
    </div>
    <div class="flexLayout pl_10 pr_10 box-sizing pb_10">
        <div class="font13"><!-- 交易手续费：免费 --></div>
        <div class="font11">兑换：<span class="font12">${orderD.items.fTrade_Count } ${fShortName }</span></div>
    </div>
    <div class="account_img tc payOrder" style="line-height: .4rem;">
        <c:if test="${fn:contains(orderD.items.fTrade_Method,'支付宝')==true}">
            <div class="font13">用户姓名：${method.items.fName }</div>
            <div class="font13 mb_10">支付宝账号：${method.items.fAccount }</div>
            <img src="${method.items.fImgUrl }" alt="" class="pay_img">
        </c:if>
        <c:if test="${fn:contains(orderD.items.fTrade_Method,'微信')==true}">
            <div class="font13">用户姓名：${method.items.fName }</div>
            <div class="font13 mb_10">微信账号：${method.items.fAccount }</div>
            <img src="${method.items.fImgUrl }" alt="" class="pay_img">
        </c:if>
        <c:if test="${fn:contains(orderD.items.fTrade_Method,'银行转账') == true}">
            <div class="font13">开户行:${bankNew.items.fBankname }</div>
            <div class="font13">用户名:${bankNew.items.fName }</div>
            <div class="font13 mb_10">银行账号:${bankNew.items.fAccount }</div>
            <%-- <img src="${orderD.items.fImgUrl }" alt="" class="pay_img"> --%>
        </c:if>
        <!-- <div class="font13">收款名：i日期为</div>
        <div class="font13 mb_10">收款站好：任日307983279</div>
        <img src="img/bank.png" alt="" class="pay_img"> -->
    </div>
    <ul class="main_hintMessage tc pl_10 pr_10 box-sizing ">
        <li class="font11 text-gray">订单数字货币已被系统锁定托管</li>
        <li class="font11 text-gray mb_15">使用微信、支付宝、银行转账时，<span class="text-red">请不要添加任何备注内容!!!</span>否则可能会被支付系统拦截</li>
        <li class="font11 text-gray">${fShortName }托管时间剩余<span class="text-yellow" id="timespan"></span>逾期将自动取消</li>
        <li class="font11 text-gray">请及时付款并标记已付款</li>
    </ul>
    <div class="btn_box input-cont mt_10 mb_20">
        <!-- <button id="" class="btn_style_1 border font15 btn_style_2">标记付款已完成</button> -->
        <c:if test="${empty type_}">
            <c:if test="${orderD.items.fTrade_Status == 1}">
                <button id="ok" class="btn_style_1 border font15 btn_style_2">标记付款已完成</button>
            </c:if>
            <c:if test="${orderD.items.fTrade_Status == 2}">
                <button id="" class="btn_style_1 border font15 btn_style_2">等待卖家释放货币</button>
            </c:if>
        </c:if>
        <c:if test="${!empty type_}">
            <c:if test="${orderD.items.fTrade_Status == 1}">
                <button id="" class="btn_style_1 border font15 btn_style_2">等待买家支付</button>
            </c:if>
            <c:if test="${orderD.items.fTrade_Status == 2}">
                <button id="freed" class="btn_style_1 border font15 btn_style_2">释放数字货币</button>
            </c:if>
        </c:if>
    </div>
    <c:if test="${fuser.fid eq buyuser}">
        <div class="mt_10 font11 text-gray tc">对方联系方式：${sellUsr_name }</div>
    </c:if>
    <c:if test="${fuser.fid eq selluser}">
        <div class="mt_10 font11 text-gray tc">对方联系方式：${buyUsr_name }</div>
    </c:if>
    <!-- <div class="mt_10 font11 text-gray tc">对方联系方式：38403407234083490</div> -->
    <div class="btn_box input-cont mt_10" style="margin-bottom: 1.5rem;">
    <c:if test="${orderD.items.fTrade_Status ==1}">
        <a href="/m/otc/cancleOrder.html?coinType=${coin_id}&orderId=${orderD.items.fOrderId }&fw=${fw}" class="btn_style_1 border font15" style="line-height: .765rem;text-align: center;">取消订单</a>
    </c:if>
    </div>
    <input type="hidden" id="uuid" value="">
    <input type="hidden" class="coin_id" name="coin_id" value="${coin_id}">
</div>

<%--     <!-- <div class="header">
        <a href="javascript:history.go(-1);" class="prev">></a>
    </div> -->
    <div class="bk-onekey financen">
         <!-- <div>
           <marquee>使用微信、支付宝、银行转账时，请<span style="color: red;">不要添加任何备注内容</span>!!!否则可能会被支付系统拦截</marquee>
         </div> -->
         <div class="container">
            <div class="bk-tabList">
                 <div class="bk-c2c-nav bk-band clearfix">
                 </div>
                 <div class="bk-pageTit" id="exchangeRecord">
                   <div class="buy_message">
                     <input type="hidden" value="${orderD.items.fOrderId }" name="orderId">
                     <input type="hidden" value="${orderD.items.fAd_Id }" name="fAd_Id">
                     <input type="hidden" value="${fShortName }" name="fShortName">
                     <input type="hidden" value="${fw }" name="fw">
                   </div>
                   <div class="table-responsive">
                     <div>订单号:<span>${orderD.items.fOrderId }</span>&nbsp;&nbsp;&nbsp;&nbsp;
                        <span>
                             <c:if test="${orderD.items.fTrade_Status=='1' }">已下单</c:if> 
                             <c:if test="${orderD.items.fTrade_Status=='2' }">待验证</c:if> 
                             <c:if test="${orderD.items.fTrade_Status=='3' }">已完成 </c:if> 
                             <c:if test="${orderD.items.fTrade_Status=='4' }">已取消</c:if> 
                        </span>
                     </div>
                     <div>付款到期:<span><fmt:formatDate value="${orderD.items.fTrade_DeadTime }" pattern="yyyy-MM-dd HH:mm"></fmt:formatDate></span>
                     </div>
                     <div>交易金额:<span>${orderD.items.fTrade_SumMoney }</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;交易数量<span>${orderD.items.fTrade_Count }</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;单价:<span>${orderD.items.fTrade_Price }</span></div>
                     
                     <div class="sum_money">
                         <input type="hidden" name="SumMoney_" value="${orderD.items.fTrade_SumMoney }">
                         <div>总额：<span class="important_hint font-w">${orderD.items.fTrade_SumMoney } CNY</span></div>
                         <div><span class="fl">交易手续费:免费</span>兑换：<span class="font-w">${orderD.items.fTrade_Count } ${fShortName }</span></div>
                     </div>
                     <div class="pay_box">
                        <div class="pay_hint">卖家收款账号</div>
                        <c:if test="${fn:contains(orderD.items.fTrade_Method,'支付宝')==true}">
                           <span class="pay_style important_hint" id="zhifubao">支付宝</span>
                            <div class="pay_imgBox" id="zhifubaoImg" style="display:none;">
                                    <div>用户姓名：${method.items.fName }</div>
                                    <div>支付宝账号：${method.items.fAccount }</div>
                                    <img alt="" src="${method.items.fImgUrl }" class="pay_img">
                             </div>
                        </c:if>
                        <c:if test="${fn:contains(orderD.items.fTrade_Method,'微信')==true}">
                            <span class="pay_style important_hint" id="weixin">微信</span>
                            <div class="pay_imgBox" id="weixinImg" style="display:none;">
                                    <div>用户姓名：${method.items.fName }</div>
                                    <div>微信账号：${method.items.fAccount }</div>
                                    <img alt="" src="${method.items.fImgUrl }" class="pay_img">
                            </div>
                        </c:if>
                        <c:if test="${fn:contains(orderD.items.fTrade_Method,'银行转账')==true}">
                             <span class="pay_style important_hint" id="bank">银行卡</span>
                             <div class="pay_imgBox" id="bankImg" style="display:none;">
                                    <div>开户行：${bankNew.items.fName }</div>
                                    <div>用户名：${bankNew.items.fRealName }</div>
                                    <div>银行账号：${bankNew.items.fBankNumber }</div>
                                   <img alt="" src="${orderD.items.fImgUrl }" class="pay_img">
                               </div>
                        </c:if>
                        <c:if test="${orderD.items.fTrade_Status == 1 || orderD.items.fTrade_Status == 2}">
                            <div class="pay_attention">
                               <c:if test="${orderD.items.fTrade_Status == 1}">
                                 <div class="attention_hint">${fShortName }托管时间剩余<span class="important_hint" id="timespan"></span>逾期将自动取消</div>
                               </c:if>
                               <div class="attention_hint" style="margin-bottom: 10px;">请及时付款并标记已付款</div>
                               <c:if test="${empty type_}">
                                  <c:if test="${orderD.items.fTrade_Status == 1}">
                                     <div class="btn_group" id="ok"><button style="background-color: #1db3b4;color: white;margin-bottom: 50px;border:0;">标记付款已完成</button></div>
                                  </c:if>
                                  <c:if test="${orderD.items.fTrade_Status == 2}">
                                     <div class="btn_group"><button style="background-color: #1db3b4;color: white;margin-bottom: 50px;border:0;">等待卖家释放货币</button></div>
                                  </c:if>
                               </c:if>
                               <c:if test="${!empty type_}">
                                <c:if test="${orderD.items.fTrade_Status == 1}">
                                    <div class="btn_group"><span style="background-color: #1db3b4;color: white;margin-bottom: 50px;border:0;display: block;padding: 10px 0;border-radius: 4px;cursor: not-allowed;">等待买家支付</span></div>
                                </c:if>
                                <c:if test="${orderD.items.fTrade_Status == 2}">
                                    <div class="btn_group" id="freed"><button style="background-color: #1db3b4;color: white;margin-bottom: 50px;border:0;">释放数字货币</button></div>
                                </c:if>
                            </c:if>
                            <c:if test="${fuser.fid eq buyuser}">
                                <p><span>对方联系方式：${sellUsr_name }</span></p>
                            </c:if>
                            
                            <c:if test="${fuser.fid eq selluser}">
                                 <p><span>对方联系方式：${buyUsr_name }</span></p>
                            </c:if>
                            </div>
                        </c:if>
                     </div>
                   </div>
                 </div>
            </div>
         </div>
    </div> --%>

<script>
if(window.name != "bencalie"){
    location.reload();
    window.name = "bencalie";
 }
 else{
  window.name = "";
}
</script>
<script src="/static/front/js/c2c/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="/static/front/app/js/c2c/rem1.js"></script>
<script src="/static/front/js/layim-v3.7.7/src/layui.js"></script>
<script src="/static/front/js/layim-v3.7.7/src/lay/modules/upload.js"></script>
<script src="/static/front/js/c2c/jquery.SuperSlide.2.1.1.js"></script>
<script src="/static/front/js/comm/util.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/plugin/layer/layer.js"></script>
<script src="/static/front/js/otc/publishordery.js"></script>

<script>
	$("#zhifubao").on("click",function(){
		$("#zhifubaoImg").show();
	})
	$("#weixin").on("click",function(){
		$("#weixinImg").show();
	})
	$("#bank").on("click",function(){
		$("#bankImg").show();
	})
	
	//自动取消订单倒计时
	var dateOld=$("#fTrade_DeadTime").text().trim();
	var fw =$("input[name='fw']").val();
	var starttime = new Date(dateOld);
	var orderId =${orderD.items.fOrderId };
	var b = ${orderD.items.fTrade_Status};
	if(b==1){
		var interval =setInterval(function () {
		    var nowtime = new Date();
		    var time = starttime - nowtime;
		    var day = parseInt(time / 1000 / 60 / 60 / 24);
		    var hour = parseInt(time / 1000 / 60 / 60 % 24);
		    var minute = parseInt(time / 1000 / 60 % 60);
		    var seconds = parseInt(time / 1000 % 60);
		    if(minute <= 0 && seconds <= 0){
		    	clearInterval(interval);
		    	window.location.href="/m/otc/cancleOrder.html?orderId="+orderId+"&fw="+fw;
		    }
		    $('#timespan').html( minute + "分钟" + seconds + "秒");
		}, 1000);
	}
</script>
<script type="text/javascript">
	/* 
	 时间格式化工具 
	 把Long类型的yyyy-MM-dd日期还原yyyy-MM-dd格式日期  
	 */
	function dateFormatUtil(longTypeDate) {
		var dateTypeDate = "";
		var date = new Date();
		date.setTime(longTypeDate);
		dateTypeDate += date.getFullYear(); //年  
		dateTypeDate += "-" + getMonth(date); //月   
		dateTypeDate += "-" + getDay(date); //日 
		dateTypeDate += " " + date.getHours();
		dateTypeDate += ":" + date.getMinutes();
		dateTypeDate += ":" + date.getSeconds();
		return dateTypeDate;
	}

	//返回 01-12 的月份值   
	function getMonth(date) {
		var month = "";
		month = date.getMonth() + 1; //getMonth()得到的月份是0-11  
		if (month < 10) {
			month = "0" + month;
		}
		return month;
	}

	//返回01-30的日期  
	function getDay(date) {
		var day = "";
		day = date.getDate();
		if (day < 10) {
			day = "0" + day;
		}
		return day;
	}
</script>
<!-- <script type="text/javascript">
var to_ = '${to_}';
var user = '${user_}';
var addr = '${addr}';
console.log(to_+"++++"+user);
var webSocket = new WebSocket('ws://47.75.181.215:8081/FrmChatWebSocket/' + to_ + '/' + user);
webSocket.onerror = function(event) {
    onError(event)
};

webSocket.onopen = function(event) {
    onOpen(event)
};

webSocket.onmessage = function(event) {
    onMessage(event)
};

function onMessage(event) {
    var o = event.data;
    if(o.indexOf("history")==0){
        console.log('加载消息1');
        o = o.substring(8);
        //加载历史记录
        s = $('#msgFromPush .mag-block').html();
        var item = $.parseJSON(o);
        if (item.fro_ == user) {
            //我发的消息
            s = "<li class='me-item'><span class='message_name'>我:</span><div class='time-tips'><div class='me-msg'>"+item.msg_ +"</div>"+
            "<time value="+item.time_+"><span>"+item.time_+"</span></div></li>"+s;
            
        }else {
            //对方发的消息    
            s = "<li class='friend-item'><span class='message_name'>对方：</span><div class='time-tips'><div class='friend-msg'>"+item.msg_ + "</div>"+
            "<time value="+item.time_+"><span>"+item.time_+"</span></div></li>"+s;
        }
        
    }else{
        console.log('加载消息2');
        s = $('#msgFromPush .mag-block').html();
        var item = $.parseJSON(o);
        if (item.fro_ == user) {
            //我发的消息
            //s += "我(" + item.time_ + ")：" + item.msg_ + "<br/>";
            s += "<li class='me-item'><span class='message_name'>我:</span><div class='time-tips'><div class='me-msg'>"+item.msg_ +"</div>"+
            "<time value="+item.time_+"><span>"+item.time_+"</span></div></li>"+s;
            
        }else {
            //对方发的消息
            s += "<li class='friend-item'><span class='message_name'>对方：</span><div class='time-tips'><div class='friend-msg'>"+item.msg_ + "</div>"+
            "<time value="+item.time_+"><span>"+item.time_+"</span></div></li>"+s;
        }
    }
    $("#msgFromPush .mag-block").html(s);
//     $('html, body').scrollTop($('html, body')[0].scrollHeight);
    $('#msgFromPush').scrollTop($('#msgFromPush')[0].scrollHeight);
}

function onOpen(event) {
    console.log('open');
}

function onError(event) {
    console.log(error);
}

function send(){
    console.log('发送消息!');
    var msg = $("#msg").val();
    if(msg!=null && msg !=''){
        console.log(webSocket.readyState)
        if(webSocket.readyState!=1){
            //断线重连
        /*  //本地
             webSocket = new WebSocket('ws://localhost/FrmChatWebSocket/' + to_ + '/' + user); */
                //服务器
             webSocket = new WebSocket('ws://47.75.181.215:8081/FrmChatWebSocket/' + to_ + '/' + user);
        }
        var a = webSocket.send("msg:"+msg);
        var time = '${TDateTime.Now().toString()}';
        
        s = $('#msgFromPush .mag-block').html();
        /* s += "我：<p class='time-tips'><span>"+time+"</span></p><li class='me-item'><span class='me-msg'>"+msg +"</span>"; */
        s += "<li class='me-item'><span class='message_name'>我:</span><div class='time-tips'><div class='me-msg'>"+msg +"</div>"+
        "<span>"+time+"</span></div></li>";
        
        $("#msgFromPush .mag-block").html(s);
        //$('html, body').scrollTop($('html, body')[0].scrollHeight);
        $('#msgFromPush').scrollTop($('#msgFromPush')[0].scrollHeight);
        $("#msg").val("");
    }
    
}
function history(){
    var time = $(".mag-block p:first-child time").attr("value");
    console.log('查询历史消息');
    if(webSocket.readyState!=1){
        //断线重连
        console.log('断线');
            /* //本地     
         webSocket = new WebSocket('ws://localhost/FrmChatWebSocket/' + to_ + '/' + user); */
            //服务器
         webSocket = new WebSocket('ws://47.75.181.215:8081/FrmChatWebSocket/' + to_ + '/' + user);
    }
    webSocket.send("history:"+time)
    
}

$(function(){
    $(".history").click(function(){
        history();
    });
});

function back(){
    alert(window.history.back(-1));
    window.location.href=window.history.back(-1);
} 

var name = $("#urge").attr("id");
var value = $("#urge").val();

function cd(orderId){
    var status =  window.localStorage.getItem(name);
    if (status!="") {
        if (status == 1) {
            $.ajax({
                url : "/otc/urge.html",
                data : {
                    "orderId":orderId
                },
                type: "post",
                dataType: "json",
                secureuri: false,
                async : true,
                success: function (data) {
                    if(data.code == 0){
                        layer.msg('催单成功!');
                        document.getElementById("urge").value = 2;
                        name = $("#urge").attr("id");
                        value = $("#urge").val();
                        window.localStorage.setItem(name,value);
                        window.location.reload();
                    }else{
                        layer.msg('催单失败!');
                    }
                },
                error: function (data) {
                    layer.msg("上传错误");
                }
            });
        }else{
            layer.msg("不能重复催单！");
        }
    }else{
        $.ajax({
            url : "/otc/urge.html",
            data : {
                "orderId":orderId
            },
            type: "post",
            dataType: "json",
            secureuri: false,
            async : true,
            success: function (data) {
                if(data.code == 0){
                    layer.msg('催单成功!');
                    document.getElementById("urge").value = 2;
                    name = $("#urge").attr("id");
                    value = $("#urge").val();
                    window.localStorage.setItem(name,value);
                    window.location.reload();
                }else{
                    layer.msg('催单失败!');
                }
            },
            error: function (data) {
                layer.msg("上传错误");
            }
        });
    }
}
</script> -->
<script>
var to_ = '${to_}';
var user = '${user_}';
var addr = '${addr}';
var username = '${fuser.floginName}';
/* <c:if test="${fuser.fid eq buyuser}">
<p><span>对方联系方式：${sellUsr_name }</span></p>
</c:if>

<c:if test="${fuser.fid eq selluser}">
<p><span>对方联系方式：${buyUsr_name }</span></p>
</c:if> */
var fid = "${fuser.fid}";
var buyuser = "${buyuser}";
var selluser = "${selluser}";
var myname = 0;
if(parseInt(fid)== parseInt(buyuser)){
	myname ="${sellUsr_name }";
}else{
	myname = "${buyUsr_name }";
}
var timestamp = new Date().getTime();
//console.log(to_+"++++"+user);
layui.use('layim',function(layim){
	//建立WebSocket通讯
	//注意:如果你要兼容ie8+,建议你采用socket.io的版本.下面是以原生ws为例
	//var socket = new WebSocket('ws://localhost/FrmChatWebSocket/' + to_ + '/' + user);
	var socket = new WebSocket('ws://47.75.181.215:8081/FrmChatWebSocket/' + to_ + '/' + user);
	//连接成功时触发
	socket.onopen = function(){
	};
	//监听收到的消息
	socket.onmessage =  function(res){
        console.log("onmessage" + res.data);
        console.log("onmessage111==" + JSON.stringify((JSON.parse(res.data)).data));
	        var json = JSON.parse(res.data);
	         username = json.data.mine.username;
	        var avatar = json.data.mine.avatar;
	        var id = json.data.mine.id;
	        var content = json.data.mine.content;
	        var timestamp = json.data.mine.timestamp;
	        var type = json.data.to.type;
	        
	        layim.getMessage({
	            username: username //消息来源用户名
	           	,avatar: "/static/front/images/otc/other.png" //消息来源用户头像
	       		,id: id //消息的来源ID（如果是私聊，则是用户id，如果是群聊，则是群组id）
	       		,type: type //聊天窗口来源类型，从发送消息传递的to里面获取
	       		,content: content //消息内容
	       		,mine: false //是否我发送的消息，如果为true，则会显示在右方
	       		,timestamp: timestamp //服务端时间戳毫秒数。注意：如果你返回的是标准的 unix 时间戳，记得要 *1000
	       		,fromid:id
	        }); 
        //layim.getMessage(json.data);
	};
	layim.on('sendMessage',function(res){ 
	//监听到上述消息后，就可以轻松地发生的socket了,如:
	   socket.send(JSON.stringify({
		  data:res
	   }));
	});  
	//基础配置
	layim.config({
		brief:true, //是否简约模式(如果true则不显示住面板)
		init:{
			mine: {
                "username": username  //我的呢称
                , "id": user  //我的ID
                , "status": "online" //在线状态 online：在线、hide：隐身
                , "sign": "在深邃的编码世界，做一枚轻盈的纸飞机" //我的签名
                , "avatar": "/static/front/images/otc/my.png"  //我的头像
                ,"timestamp":timestamp
            }
		} //获取主面板列表信息,下文会做进一步介绍
		
		//上传图片接口(返回的数据格式见下文),若不开启图片上传,剔除该项即可
		,uploadImage:{
			url:'/sns/uploadFile.html'   //接口地址
			,type:'post' //默认post
		}
		, msgbox: layui.cache.dir + 'css/modules/layim/html/msgbox.html' //消息盒子页面地址，若不开启，剔除该项即可
        , find: layui.cache.dir + 'css/modules/layim/html/find.html' //发现页面地址，若不开启，剔除该项即可
        //,chatLog: layui.cache.dir + 'css/modules/layim/html/chatlog.html' //聊天记录页面地址，若不开启，剔除该项即可
	}).chat({
        name:myname
        ,type: 'friend'
        ,avatar: '/static/front/images/otc/other.png'
        ,id: to_
      });
	  layim.on('online', function(data){
	      console.log(data);
	    });
	layim.setChatMin();

})
$("body").on("focus",".layim-chat-textarea textarea",function(){
	$(".layui-layer.layui-layer-page.layui-box.layui-layim-chat").css("top","7%");
})
$("body").on("blur",".layim-chat-textarea textarea",function(){
	$(".layui-layer.layui-layer-page.layui-box.layui-layim-chat").css("top","25%");
})


</script> 
</body>