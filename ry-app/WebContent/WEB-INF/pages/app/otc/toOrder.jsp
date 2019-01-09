<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat"
    pageEncoding="UTF-8"%>
<%@include file="../../front/comm/include.inc.jsp"%>
<%
     String path = request.getContextPath();
         String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
         + path;
%>
<!DOCTYPE html>
<html style="font-size: 16px;">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no, width=device-width">
<meta name="format-detection" content="telephone=no">
<meta name="format-detection" content="email=no">
    <base href="${basePath}"/>
    <title>
       <c:if test="${type == '1' }">
           购买详情
       </c:if>
       <c:if test="${type == '2' }">
          出售详情
       </c:if>
    </title>
<!--     <link rel="stylesheet" href="/static/front/css/c2c/safe.css">
    <link rel="stylesheet" href="/static/front/css/c2c/module.common.css">
    <link rel="stylesheet" href="/static/front/css/c2c/module.base.css">
    <link rel="stylesheet" href="/static/front/css/otc/common_otc.css">
    <link rel="stylesheet" href="/static/front/css/otc/buy_details.css"> -->
    <!-- <link href="//netdna.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="//at.alicdn.com/t/font_48446_inzlc0gmji3sor.css" rel="stylesheet"> -->
    <link href="/static/front/app/js/c2c/common.css" rel="stylesheet">
    <link href="/static/front/app/js/c2c/otc-app.css" rel="stylesheet">
</head>
<body style="background-color: #F5F5F5;">
<div class="header">
    <a href="javascript:history.go(-1);" class="prev"></a>
    <div class="head_title font15">
       <c:if test="${type == '1' }">
       购买详情
       </c:if>
       <c:if test="${type == '2' }">
       出售详情
       </c:if>
    </div>
    <!--<div class="more more-z">设置</div>-->
</div>
<div id="mat"></div>
<div class="flexLayout order_details">
    <div class="order_details_img"><img alt="" src="/static/front/images/c2c/userImg.png"></div>
    <div class="order_details_content">
        <div class="order_details_name font15">${userId }</div>
        <span style="display: none" class="icon-message" id="userId">${userId }</span>
        <div class="text-gray font12">${fMsg}</div>
    </div>
    <div class="order_details_else tr">
        <div class="font10">单价</div>
        <div class="font14 mt_15">${buyPrice }</div>
    </div>
</div>
<ul class="basic_message flexLayout">
    <li>
        <div class="font13 text-yellow">${jiaoyi }<span style="display: none;">+</span></div>
        <div class="font13">交易次数</div>
    </li>
    <li>
        <div class="font13 text-yellow">0</div>
        <div class="font13">信任人数</div>
    </li>
    <li>
        <div class="font13 text-yellow">100%</div>
        <div class="font13">好评</div>
    </li>
    <li>
        <div class="font13 text-yellow">${jiaoyi }</div>
        <div class="font13">历史成交</div>
    </li>
</ul>
<input name="type" value="${type }" type="hidden">
<div class="mt_10 pl_10 pr_10 box-sizing pt_10 pb_10" style="background-color: white;">
    <div class="font13 ad_time">报价：<span id="price">${buyPrice }</span> CNY/${fShortName }</div>
    <div class="font13 ad_time">交易限额：<span id="limit_money">${limit_money }</span> CNY</div>
    <div class="flexLayout ad_time">
        <div class="font13">付款方式：<span id="payMethod">${payMethod }</span></div>
        <div class="font13">付款期限： 45分钟</div>
    </div>
</div>
<div class="bull_num mt_10">
    <input type="hidden" id="coin_id" value="${coin_id }">
    <input name="sellfTotal" value="${sellfTotal }" type="hidden">  
    <input name="buyfTotal" value="${buyfTotal }" type="hidden">  
    <input name="fId" value="${fId }" type="hidden">
    <input type="hidden" name="fShortName" value="${fShortName }">
    <input type="hidden" name="yh" value="${yh }">
    <input type="hidden" name="wx" value="${wx }">
    <input type="hidden" name="zfb" value="${zfb }">
    <input type="hidden" name="ffee" value="${ffee}">
    <c:if test="${type == '1' }">
        <div class="buy_hint font14">您想购买多少？</div>
    </c:if>
    <c:if test="${type == '2' }">
        <div class="buy_hint font14">您想出售多少？</div>
    </c:if>
    <!-- <div class="buy_hint font14">您想购买多少？</div> -->
    <div class="specific_num flexLayout">
        <div class="ipt_box">
            <input type="number" class="app_ipt_style font12" placeholder="50~2000" id="cny" onkeyup="jisuan1();">
            <span class="unit font13">CNY</span>
        </div>
        <div><img src="/static/front/images/c2c/change.png" alt=""></div>
        <div class="ipt_box">
            <input type="number" class="app_ipt_style font12" placeholder="输入买入数量" id="btc" onkeyup="jisuan2();">
            <span class="unit font13">${fShortName }</span>
        </div>
    </div>
</div>
<ul class="deal_hint mt_10 font12">
    <li>交易提醒</li>
    <li>1、交易前请详细了解卖家的交易信息；</li>
    <li>2、请通过平台进行沟通约定，并保存好相关聊天记录；</li>
    <li>3、如果遇到交易纠纷，请通过平台“帮助”解决问题；</li>
</ul>
<div class="btn_box input-cont mt_20">
    <c:if test="${type == '1' }">
        <button id="ok" class="btn_style_1 border font15">立即购买</button>
    </c:if>
    <c:if test="${type == '2' }">
        <button id="ok" class="btn_style_1 border font15">立即出售</button>
    </c:if>
    <!-- <button id="submit" class="btn_style_1 border font15">立即购买</button> -->
</div>
<div style="height: 5rem; display: none;" class="ipt_mat"></div>
<%-- <nav>
   <div class="Personal-title">
        <span>
            <a href="javascript:;" onClick="javascript :history.back(-1)">
                <em>
                    <i></i>
                    <i></i>
                </em>
                <strong>返回</strong>
            </a>
        </span>买入详情
    </div>
</nav>
<div class="container clear-float ad-detail">
    <!-- <a class="btn card-admin" role="button" href="/otc/index.html" style="color: #666" ><img alt="" src="/static/front/images/aui-icon-back.png"> 返回</a> -->
    <div class="detail-left ">
        <div class="poster-info clear-float">
            <!-- <a href="/user/314258455587039"><img class="poster-head fl" src="https://s3.ap-northeast-2.amazonaws.com/coincola.user/avatar/1000316493-1528690389.png" alt=""></a> -->
            <a href="#"><span class="poster-head fl"></span></a>
            <div class="fl">
                <div class="user-name">
                    <a>${userId }</a>
                    <span style="display: none" class="icon-message" id="userId">${userId }</span>
                </div>
                <div class="poster-info-cont clear-float">
                    <span class="poster-info-item">
                        <div class="item-d">${jiaoyi }<span style="display: none;">+</span></div>
                        <div class="item-n">交易次数</div>
                    </span>
                    <span class="poster-info-item">
                        <div class="item-d">0</div>
                        <div class="item-n">信任人数</div>
                    </span>
                    <span class="poster-info-item">
                        <div class="item-d">${haoping }0</div>
						<div class="item-d">100%</div>
                        <div class="item-n">好评度</div>
                    </span>
                    <span class="poster-info-item">
                        <div class="item-d">${jiaoyi }</div>
                        <div class="item-n">历史成交</div>
                    </span>
                </div>
            </div>
        </div>
        <input name="sellfTotal" value="${sellfTotal }" type="hidden">  
        <input name="buyfTotal" value="${buyfTotal }" type="hidden">  
        <input name="fId" value="${fId }" type="hidden">  
        <table class="table-info" style="margin-top: 30px;line-height: 25px;">
            <tr>
                <th>报价:</th><td></td><td class="price"><span id="price">${buyPrice }</span> CNY/${fShortName }</td>
            </tr>
            <tr>
                <th>交易限额:</th><td></td><td class="ad-limits"><span id="limit_money">${limit_money }</span> CNY</td>
            </tr>
            <tr>
                <th>付款方式:</th><td></td><td><span id="payMethod">${payMethod }</span></td>
            </tr>
            <tr>
                <th>付款期限:</th><td></td><td>45 分钟</td>
            </tr>
        </table>
        <input name="type" value="${type }" type="hidden">
        <div class="form-cont ad-detail clear-float">
            <div class="form-title">
                <c:if test="${type == '1' }">
                    <span class="form-name">你想购买多少？</span>
                </c:if>
                <c:if test="${type == '2' }">
                    <span class="form-name">你想出售多少？</span>
                </c:if>
                <span class="form-name"></span>
            </div>
            <div class="input-cont input2 mr-12" >
                <input type="number" placeholder="输入您想购买的金额" class=" input " id="cny" onkeyup="jisuan1();" >
                <div class="input-after">CNY</div>
            </div>
            <span class="icon-equal"></span>
            <div class="input-cont input2">
                <input type="number" placeholder="输入您想购买的数量" class="input" id="btc" onkeyup="jisuan2();" >
                <div class="input-after">${fShortName }</div>
            </div>
            <input type="hidden" id="coin_id" value="${coin_id }">
            <input type="hidden" name="fShortName" value="${fShortName }">
            <input type="hidden" name="yh" value="${yh }">
            <input type="hidden" name="wx" value="${wx }">
            <input type="hidden" name="zfb" value="${zfb }">
            <c:if test="${type == '1' }">
                 <a id="ok" href="javascript:void();" class="btn submit">立即购买</a>
            </c:if>
            <c:if test="${type == '2' }">
                 <a id="ok" href="javascript:void();" class="btn submit">立即出售</a>
            </c:if>
            <div class="form-title pt-30">
                <span class="form-name">交易须知</span>
            </div>
            <div class="line mt-20 mb-20"></div>
            <p class="p pb-30">
                1.交易前请详细了解对方的交易信息。<br>
                2.请通过平台进行沟通约定，并保存好相关聊天记录。<br>
                3.如遇到交易纠纷，可通过申诉来解决问题。<br>
                4.在您发起交易请求后，${fShortName } 被锁定在托管中，受到 RYH 保护。如果您是买家，发起交易请求后，请在付款周期内付款并把交易标记为付款已完成。卖家在收到付款后将会放行处于托管中的 ${fShortName }。<br>交易前请阅读《Coa服务条款》以及常见问题、交易指南等帮助文档。<br>
                5.请注意欺诈风险，交易前请检查该用户收到的评价和相关信用信息，并对新近创建的账户多加留意。<br>
                6.托管服务保护网上交易的买卖双方。在发生争议的情况下，我们将评估所提供的所有信息，并将托管的 ${fShortName } 放行给其合法所有者。<br>
            </p>
        </div>
    </div>
    <div class="detail-right">
        <div class="form-cont">
            <div class="form-title pt-30">
                <span class="form-name">广告留言</span>
            </div>
            <div class="line mt-20 mb-20"></div>
            <p class="p mb-20">永不增发的数字黄金，安全高效的未来财富！<br>广告在，人就在!<br>支付宝、微信、银行转账都可以。<br>行情波动快下单速付款。<br>货币的互联网平移，金钱的文艺复兴!<br>人类的数字资产！</p>

        </div>
    </div>
</div> --%>

<!-- <div class="chating-cont fixed" style="display:none;">
    <div class="chat-cont">
        <a style="display:none;" type="SELL" class="send icon-send"></a>
        <div class="order-info">
            <span class="order-item ader-name">kuaisu</span>
            <span class="order-item">报价: 44253.59 CNY/BTC</span>
            <span class="order-item">交易限额: 10000-19864 CNY</span>
            <span class="icon-del"></span>
        </div>
        <div class="chating">
        </div>
        <div class="tpl-message-cont">
            <div class="tpl-message">你好，请问在吗？</div>
            <div class="tpl-message">你好，请问可以更换支付方式吗？</div>
            <div class="tpl-message">你好，请问付款后能及时放币吗？</div>
            <div class="tpl-message">你好，请问可以设置信任进行交易吗？</div>
        </div>
    </div>
</div>
<div class="order-confirm" style="display: none;">
    <div class="h3">下单确认</div>
    <div class="confirm-item">
        <span class="item-t">购买价格: </span>
        <span class="item-d"><span>44253.59</span> CNY</span>
    </div>
    <div class="confirm-item">
        <span class="item-t">购买金额: </span>
        <span class="item-d"><span class="confirm-amount">-</span> CNY</span>
    </div>
    <div class="confirm-item">
        <span class="item-t">购买数量: </span>
        <span class="item-d"><span class="confirm-qty">-</span> BTC</span>
    </div>
    <div class="confirm-tips">提醒：请确认价格再下单，下单后此交易的 BTC 将托管锁定，请放心购买。</div>
 </div> -->
        <%--<div>
            <div>
                <span id="userId">${userId }</span>
            </div>
            <div>
                <span>交易次数：${jiaoyi }+</span><span>好评度：${haoping }</span>
            </div>
        </div>
        
        
        <div>
                <hr>
            <input name="fId" value="${fId }" type="hidden">                
            <div>
                <span>报价：</span><span id="price">${buyPrice }</span>CNY/BTC
            </div>
            <div>
                <span>交易限额：</span><span id="limit_money">${limit_money }</span>CNY
            </div><div>
                <span>付款方式：</span><span id="payMethod">${payMethod }</span>
            </div>
            <div>
                <span>付款期限：</span><span>15分钟</span>
            </div>
            <div>
                 <span>你想要购买多少？</span>
                 <input type="text" value="CNY" id="cny" onchange="jisuan1();"/>
                 <input type="text" value="BTC" id="btc" onchange="jisuan2();"/>
            </div>
            <button id="ok">立即购买</button>
     </div> --%>

<script src="/static/front/js/c2c/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="/static/front/app/js/c2c/rem1.js"></script>
<script src="/static/front/js/c2c/jquery.SuperSlide.2.1.1.js"></script>
    <script type="text/javascript" src="${oss_url}/static/front/js/plugin/layer/layer.js"></script>
<script src="/static/front/js/comm/util.js"></script>
<script>
function jisuan1(){
    var cny=$("#cny").val().trim();
    var price=$("#price").text();
    var btc=cny/price;
    $("#btc").val(btc);
}
function jisuan2(){
    var btc=$("#btc").val().trim();
    var price=$("#price").text();
    var cny=btc*price;
    $("#cny").val(cny);
}
$("#ok").on("click",function(){
    var type=$("input[name='type']").val();//交易类型
    var fId=$("input[name='fId']").val();//用户Id
    var coin_id=$("#coin_id").val();//用户Id
    var userId=$("#userId").text();//用户Id
    var buy_counts=parseFloat($("#btc").val().trim());//购买数量
    var buyPrice=$("#price").text();//购买价格
    var sum_money=$("#cny").val().trim();//总金额
    var limit_money=$("#limit_money").text();//单笔交易限额
    var sellfTotal=$("input[name='sellfTotal']").val();
    var buyfTotal=$("input[name='buyfTotal']").val();
    var limitMoney1=limit_money.split("-");
    var limit1=parseFloat(limitMoney1[0]);
    var limit2=parseFloat(limitMoney1[1]);
    var fShortName = $("input[name='fShortName']").val();
    var payMethod = $("#payMethod").text();
    var yh = $("input[name='yh']").val();
    var wx = $("input[name='wx']").val();
    var zfb = $("input[name='zfb']").val();
    var ffee = $("input[name='ffee']").val();
    if(type ==2 && yh == -4 && payMethod == "银行转账"){
    	layer.confirm('请绑定银行卡信息！', {
            btn : [ '确定', '取消' ]//按钮
        }, function(index) {
            layer.close(index);
            window.location.href = "/m/financial/accountbank.html";
        }); 
    	return;
    }
    if(type ==2 && wx == -5 && payMethod == "微信"){
    	layer.confirm('请绑定微信账号信息！', {
            btn : [ '确定', '取消' ]//按钮
        }, function(index) {
            layer.close(index);
            window.location.href = "/m/otc/userReceiptOption.html";
        }); 
    	return;
    }
    if(type ==2 && zfb == -6 && payMethod == "支付宝"){
    	layer.confirm('请绑定支付宝账号信息！', {
            btn : [ '确定', '取消' ]//按钮
        }, function(index) {
            layer.close(index);
            window.location.href = "/m/otc/userReceiptOption.html";
        }); 
    	return;
    }
    if(sum_money<limit1){
    	layer.msg("购买金额至少为"+limit1);
    	return;
    }
    if(sum_money>limit2){
        layer.msg("购买金额不能超过"+limit2);
    	return;
    } 
    var payMethod=$("#payMethod").text();//支付方式
    if(buy_counts<=0 || buy_counts==null){
        if(type == '1'){
            layer.msg("请输入相应的购买数量！");
        }else {
            layer.msg("请输入相应的出售数量！");
        }
        return;
    }
    if(parseFloat(buyfTotal)<(parseFloat(buy_counts)*(1.0+parseFloat(ffee))) && type == 1){
        layer.msg("该卖家的余额账户不足，不能继续购买！");
   		cd('${userId}');
   		return;
    }
    if(sellfTotal<buy_counts && type == 2){
        layer.msg("余额账户不足，不能出售！");
   		return;
    }
    layer.load(1);
    $.ajax({
         url:"/m/otc/orderPay.html",
         data:{"type":type,
        	   "coin_id":coin_id,
        	   "fId":fId,
        	   "userId":userId,
        	   "payMethod":payMethod,
        	   "buyPrice":buyPrice,
        	   "buy_counts":buy_counts,
        	   "sum_money":sum_money,
        	   "limit_money":limit_money ,
        	   "fShortName":fShortName },
         type:"post",
         dataType: "json",
         secureuri: false,
         async : true,
         success:function(data){
             if(data.code==0){
              //  alert("添加成功");
                window.location.href="/m/otc/orderList.html?coinType="+coin_id;
	            layer.close();
             } else{
                 alert("添加失败");
                 layer.close();
             }
         },
         error: function(){
             alert("网络异常，请稍后重试！");
             layer.close();
         }
     })
})
function cd(orderId){
	$.ajax({
    	url : "/otc/tourge.html",
        data : {
        	"orderId":userId
        },
        type: "post",
        dataType: "json",
        secureuri: false,
        async : true,
        success: function (data) {
            if(data.code == 0){
                layer.msg('已提醒卖家充值!');
                window.location.reload();
            }else{
                layer.msg('提醒卖家充值失败!');
            }
        },
        error: function (data) {
            layer.msg("网络异常,请稍后重试！");
        }
    });
}

$("body").on("focus",".app_ipt_style",function(){
    $(".ipt_mat").show();
    /* $("body").scrollTop(100); */
});
$("body").on("blur",".app_ipt_style",function(){
    $(".ipt_mat").hide();
    /* $("body").scrollTop(0); */
});
/* $(function(){
    addPhoneKeyBoardListener(function(){
        $("body").scrollTop(100);
    },function(){
        $("body").scrollTop(0);
    }); 
	setTimeout(function(){
	    document.body.scrollTop = document.body.scrollHeight;
	},300); 
	$('input[type="number"]').on('click', function () {
		  var target = this;
		  setTimeout(function(){
		        target.scrollIntoViewIfNeeded();
		      //  console.log('scrollIntoViewIfNeeded');
		      },400);
		});
}); */

/* 手机键盘遮住内容 */
/* (function(){
    window.addPhoneKeyBoardListener = function(show, hide) {
        var clientHeight = document.documentElement.clientHeight || document.body.clientHeight; 
        $(window).on('resize', function () {
            var nowClientHeight = document.documentElement.clientHeight || document.body.clientHeight;
            if (clientHeight - nowClientHeight > 100) {
                show();
            }else {
                hide();
            } 
        });
    }
})(); */


</script>
</body>