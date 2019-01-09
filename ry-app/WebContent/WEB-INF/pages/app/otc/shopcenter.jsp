<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <link rel="stylesheet" href="/static/front/app/css/css.css">
  <link rel="stylesheet" href="/static/front/app/css/style.css">
  <title>商家个人中心</title>
  <style>
    * {
      margin: 0;
      padding: 0;
    }

    body {
      background: #efefef;
      font-family: PingFangSC-Medium;
    }

    .titleBox {
      background: #fff;
      height: 2.5rem;
      display: flex;
      justify-content: space-around;
      align-items: stretch;
      margin-bottom: 3px;
      margin-top:0.6rem;
    }

    .titleBox>span {
      line-height: 2.5rem;
      height: 2.5rem;
      border-bottom: 3px solid transparent;
      color: #222;
    }

    .titleBox>span.active {
      border-color: #EBA51D;
      color: #EBA51D;
    }

    .mainContainer {
      font-size:0.9rem;
    }

    .shopInfo {
      height: 3rem;
      display: flex;
      border-top: 1px solid #efefef;
      align-items: center;
      background: #fff;
    }
    .shopInfo span:first-child{
      flex:0 0 30%;
      margin-left:3%;
    }
    .shopInfo .themColor{
      color:#EBA51D;
    }
    .exchangerInfo{
      display: flex;
      flex-flow: wrap row;
      background:#fff;
      box-shadow:0 10px 10px 1px #d9d9d9;
    }
    .exchangerInfo>div{
      height:4rem;
      flex:0 0 50%;
      box-sizing:border-box;
      display:flex;
      align-items:center;
      flex-flow:nowrap column;
    }
    .exchangerInfo>div>span:first-child{
	    flex: 0 0 48%;
	    width: 100%;
	    display: flex;
	    align-items: center;
	    justify-content: center;
	    text-align: Center;
    }
    .exchangerInfo>div:nth-child(even){
      border-bottom:1px solid #efefef;
    }
    .exchangerInfo>div:nth-child(odd){
      border-bottom:1px solid #efefef;
      border-right:1px solid #efefef;
    }
    .exchangerInfo>div .nums{
      color:#666;
      flex:1;
      text-align:Center;
    }
    .mainContainer>div{
      display:none;
    }
    .mainContainer>div.active{
      display:block;
    }
    .orderItems{
      width:93%;
      border-radius:0.5rem;
      background:#fff;
      overflow:hidden;
      margin:1rem auto 0;
      box-shadow:0 0 10px 3px #999;
    }
    .orderItems>.orderTitle{
      height: 2.5rem;
      line-height: 2.5rem;
      background: #EBA51D;
      text-indent: 1rem;
      color: #ffff;
      font-size: 1.1rem;
    }
    .orderItems>.orderItem{
      height:2rem;
      border-bottom:1px solid #efefef;
      display:flex;
      align-items:Center;
    }
    .orderItem>span:first-child{
      flex:0 0 20%;
      color:#333;
      margin-left:1rem;
      margin-right:0.5rem;
    }
    .orderItem .num{
      color:#666;
    }
    .checkBtn{
      padding:0.2rem 1.5rem;
      color:#fff;
      background:#EBA51D;
      border-radius:0.3rem;
    }
    .onlineBox{
      background:#fff;
      height:3.5rem;
      display:flex;
      justify-content: center;
      align-items:center;
      margin-bottom:0.5rem;
      color:#fff;
    }
    .onlineBox>div{
        flex:0 0 48%;
        background:#CAC8C8;
        height:2.2rem;
        line-height:2.2rem;
        text-align:Center;
    }
    .onlineBox>div.active{
      background:#EBA51D;
    }
    .onlineBox>div:first-child{
      border-top-left-radius: 0.3rem;
      border-bottom-left-radius:0.3rem;
    }
    .onlineBox>div:last-child{
      border-top-right-radius: 0.3rem;
      border-bottom-right-radius:0.3rem;
    }
    .onlineItemBox>div{
      display:none;
    }
    .onlineItemBox>div.active{
      display:block;
    }
    .onlineBuyStd>div,.onlineSellStd>div{
      width:95%;
      border-radius:0.3rem;
      margin:0 auto 1rem;
      overflow:hidden;
    }
    .publishItem>span:first-child{
      flex:0 0 30%;
      color:#333;
      margin-left:0.5rem;
    }
    .publishItem{
      border-bottom:1px solid #efefef;
      display:flex;
      height:2.5rem;
      align-items:center;
      background:#fff;
    }
    .publishItem .info{
      color:#666;
    }
    
    .mask{
    		display:none;
			position:fixed;
			left:0;
			top:0;
			z-index:101;
			width:100vw;
			height:100vh;
			background:rgba(0,0,0,0.5);
			overflow:hidden;
			color:#fff;
		}
		.posBox{
		    width: 80%;
		    height: 14rem;
		    background: rgba(51,51,51,0.96);
		    position: absolute;
		    top: 50%;
		    left: 50%;
		    transform: translate(-50%,-50%);
		    border-radius: 1rem;
		    overflow: hidden;
		    position:relative;
		}
		.posBox>p{
			line-height: 3rem;
		    padding-left: 2rem;
		    color: #EBA51D;
		    font-size: 1.1rem;
		    border-bottom: 1px solid rgba(255,255,255,0.2);
		}
		.posBox>.upperSeller{
			height:4rem;
			display:flex;
			align-items:center;
			justify-content:center;
		}
		.posBox>.upperSeller>span{
			font-size:1.2rem;
			color:#dfdfdf;
		}
		.posBox>.upperSeller>.selectRd{
			width:1rem;
			height:1rem;
			border:1px dotted #666;
			margin-right:2rem;
		}
		.posBox>.upperSeller.active>.selectRd{
			background:#dfdfdf url('/static/front/images/udian/input_true.png') no-repeat left top;
			background-size:auto 100%;
		}
		.upLvBtnBox{
			position:absolute;
			bottom:0;
			left:0;
			padding:0.5rem;
			width:100%;
		}
		.upLvBtnBox>span{	
			display:block;
			width:6rem;
			height:2rem;
			line-height:2rem;
			text-align:center;
			background:#EBA51D;
			border-radius:0.25rem;
			color:#fff;
			margin:auto;
		}
		nav>a{
			display:block;
			position:absolute;
			right:0;
			top:0;
			height:100%;
			width:3rem;
			z-index:100;
		}
		nav>a>img{
			position:absolute;
			top:50%;
			left:50%;
			transform:translate(-50%,-50%);
			width:80%;
			height:auto;
		}
		.info>span{
			display: inline-block;
		    width: 3.5rem;
		    height: 1.5rem;
		    text-align: center;
		    background: #EBA51D;
		    color: #fff;
		    line-height: 1.5rem;
		    border-radius: 0.2rem;
		    margin-left: 0.5rem;
		}
		[v-cloak]{
			display:none;
		}
  </style>
  <script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js" ></script>
  <script type="text/javascript" src="/static/front/app/js/vue.js"></script>
  <script type="text/javascript" src="/static/front/js/layer/layer.js"></script>
  <script type="text/javascript" src="/static/front/js/comm/util.js"></script>
</head>

<body>
<nav>
    <div class="Personal-title">
        <span>
            <a href="javascript:;" onclick="javascript :history.back(-1)">
                <em>
                    <i></i>
                    <i></i>
                </em>
                <strong>返回</strong>
            </a>
        </span>商家个人中心
    </div>
    <!-- <a href='/m/otc/index.html?coinType=1'>
    	<img src='/static/front/images/publish.png'>
    </a> -->
</nav>
    <!-- <div class='titleBox'>
      <span class='active'>个人中心</span>
      <span class=''>出售订单 </span>
      <span class=''>购买订单</span>
      <span class=''>我的广告</span>
    </div> -->
    <div id = "container" v-cloak>
    <div class="mainContainer">
      <div class="personCenter active">
        <div class='shopInfo'><span>商家名称</span><span class="themColor">${name }</span></div>
        <div class='shopInfo'><span>商家等级</span>
        <c:if test="${IsMerchant == 1}"><span style='flex:1;' class='sellerLv'>普通商家</span></c:if>
        <c:if test="${IsMerchant == 2}"><span style='flex:1;' class='sellerLv'>VIP商家</span></c:if>
        <c:if test="${IsMerchant == 3}"><span style='flex:1;' class='sellerLv'>超级商家</span></c:if>
        <span v-if="isApply" class="themColor callMask" style='margin-right:1rem;'>升级商家  &gt;</span>
        <span v-if="!isApply" class="themColor" id="call" style='margin-right:1rem;'>升级商家  &gt;</span></div>
        <div class='shopInfo'><span>保证金</span>
        	<c:if test="${IsMerchant == 1 }"><span style='flex:1;' class='sellerLv'><span class="themColor">0RYB</span></span></c:if>
        	<c:if test="${IsMerchant == 2 }"><span style='flex:1;' class='sellerLv'><span class="themColor">100RYB</span></span></c:if>
        	<c:if test="${IsMerchant == 3 }"><span style='flex:1;' class='sellerLv'><span class="themColor">300RYB</span></span></c:if>
        </div>
        <div class='shopInfo' style='margin-bottom:10px;'><span>注册时间</span><span class="themColor">${time }</span></div>
        <div class='exchangerInfo'>
          <div><span>总出售成交量</span><span class="nums">{{center.selloutAmountTotal?center.selloutAmountTotal:'0'}}</span></div>
          <div><span>总购买成交量</span><span class="nums">{{center.buyAmountTotal?center.buyAmountTotal:'0'}}</span></div>
          <div><span>今日出售成交量</span><span class="nums">{{center.selloutAmountToday?center.selloutAmountToday:'0'}}</span></div>
          <div><span>今日购买成交量</span><span class="nums">{{center.buyAmountToday?center.buyAmountToday:'0'}}</span></div>
          <div><span>近日出售成交量</span><span class="nums">{{center.selloutAmountWeek?center.selloutAmountWeek:'0'}}</span></div>
          <div><span>近日购买成交量</span><span class="nums">{{center.buyAmountWeek?center.buyAmountWeek:'0'}}</span></div>
          <div><span>实名认证</span><span class="nums">是</span></div>
          <div><span>近30日是否成交</span><span class="nums">{{(center.selloutAmountTotal != 0 && center.selloutAmountTotal ) ||(center.buyAmountTotal != 0 && center.buyAmountTotal) ? '是':'否'  }}</span></div>
        </div>
        <div class='mask'>
        	<div class='posBox'>
        		<p>升级为：</p>
        		<div class='VIPseller upperSeller'>
        			<span class='selectRd'><span></span></span><span>VIP商家</span><input type='hidden' value='VIP'>
        		</div>
        		<div class='SUPERseller upperSeller'>
        			<span class='selectRd'><span></span></span><span>超级商家</span><input type='hidden' value='SUPER'>
        		</div>
        		<div class='upLvBtnBox'><span class='upLvBtn'>确定</span></div>
        	</div>
      	</div>
      	</div>
      <div class="sellOrders">
        <div v-for="(item,i) in Orders" class='orderItems'>
          <div class="orderTitle">出售{{item.fShortName}}</div>
          <div class="orderItem"><span class="item">订&nbsp;&nbsp;单&nbsp;&nbsp;号 :</span><span class="num">{{item.fOrderId}}</span></div>
          <div class="orderItem"><span class="item">交易金额&nbsp;:</span><span class="num">{{item.fTrade_SumMoney}}</span></div>
          <div class="orderItem"><span class="item">数&nbsp;&nbsp;&nbsp;量&nbsp;&nbsp;&nbsp;&nbsp; :</span><span class="num">{{item.fTrade_Count | toFixed}}</span></div>
          <div class="orderItem"><span class="item">价&nbsp;&nbsp;&nbsp;格&nbsp;&nbsp;&nbsp;&nbsp; :</span><span class="num">{{item.fTrade_Price}}</span></div>
          <div class="orderItem"><span class="item">状&nbsp;&nbsp;&nbsp;态&nbsp;&nbsp;&nbsp;&nbsp; :</span><span class="num">充值完成</span></div>
          <div class="orderItem"><span class="item">备&nbsp;&nbsp;&nbsp;注&nbsp;&nbsp;&nbsp;&nbsp; :</span><span class="num"></span></div>
          <div class="orderItem"><span class="item">操&nbsp;&nbsp;&nbsp;作&nbsp;&nbsp;&nbsp;&nbsp; : </span><span class="checkBtn">查看</span></div>
        </div>
        <div v-for="(item,i) in Orders1" class='orderItems'>
          <div class="orderTitle">出售{{item.fShortName}}</div>
          <div class="orderItem"><span class="item">订&nbsp;&nbsp;单&nbsp;&nbsp;号 :</span><span class="num">{{item.fOrderId | toFixed}}</span></div>
          <div class="orderItem"><span class="item">交易金额&nbsp;:</span><span class="num">{{item.fTrade_SumMoney}}</span></div>
          <div class="orderItem"><span class="item">数&nbsp;&nbsp;&nbsp;量&nbsp;&nbsp;&nbsp;&nbsp; :</span><span class="num">{{item.fTrade_Count | toFixed}}</span></div>
          <div class="orderItem"><span class="item">价&nbsp;&nbsp;&nbsp;格&nbsp;&nbsp;&nbsp;&nbsp; :</span><span class="num">{{item.fTrade_Price}}</span></div>
          <div class="orderItem"><span class="item">状&nbsp;&nbsp;&nbsp;态&nbsp;&nbsp;&nbsp;&nbsp; :</span><span class="num">充值完成</span></div>
          <div class="orderItem"><span class="item">备&nbsp;&nbsp;&nbsp;注&nbsp;&nbsp;&nbsp;&nbsp; :</span><span class="num"></span></div>
          <div class="orderItem"><span class="item">操&nbsp;&nbsp;&nbsp;作&nbsp;&nbsp;&nbsp;&nbsp; : </span><span class="checkBtn">查看</span></div>
        </div>
      </div>
      <div class="buyOrder">
           <div v-for="(item,i) in OrderShells" class='orderItems'>
            <div class="orderTitle">购买{{item.fShortName}}</div>
            <div class="orderItem"><span class="item">订&nbsp;&nbsp;单&nbsp;&nbsp;号 :</span><span class="num">{{item.fOrderId}}</span></div>
            <div class="orderItem"><span class="item">交易金额&nbsp;:</span><span class="num">{{item.fTrade_SumMoney}}</span></div>
            <div class="orderItem"><span class="item">数&nbsp;&nbsp;&nbsp;量&nbsp;&nbsp;&nbsp;&nbsp; :</span><span class="num">{{item.fTrade_Count | toFixed}}</span></div>
            <div class="orderItem"><span class="item">价&nbsp;&nbsp;&nbsp;格&nbsp;&nbsp;&nbsp;&nbsp; :</span><span class="num">{{item.fTrade_Price}}</span></div>
            <div class="orderItem"><span class="item">状&nbsp;&nbsp;&nbsp;态&nbsp;&nbsp;&nbsp;&nbsp; :</span><span class="num">充值完成</span></div>
            <div class="orderItem"><span class="item">备&nbsp;&nbsp;&nbsp;注&nbsp;&nbsp;&nbsp;&nbsp; :</span><span class="num"></span></div>
            <div class="orderItem"><span class="item">操&nbsp;&nbsp;&nbsp;作&nbsp;&nbsp;&nbsp;&nbsp; : </span><span class="checkBtn">查看</span></div>
        </div>
        <div v-for="(item,i) in OrderShells1" class='orderItems'>
            <div class="orderTitle">购买{{item.fShortName}}</div>
            <div class="orderItem"><span class="item">订&nbsp;&nbsp;单&nbsp;&nbsp;号 :</span><span class="num">{{item.fOrderId}}</span></div>
            <div class="orderItem"><span class="item">交易金额&nbsp;:</span><span class="num">{{item.fTrade_SumMoney}}</span></div>
            <div class="orderItem"><span class="item">数&nbsp;&nbsp;&nbsp;量&nbsp;&nbsp;&nbsp;&nbsp; :</span><span class="num">{{item.fTrade_Count | toFixed}}</span></div>
            <div class="orderItem"><span class="item">价&nbsp;&nbsp;&nbsp;格&nbsp;&nbsp;&nbsp;&nbsp; :</span><span class="num">{{item.fTrade_Price}}</span></div>
            <div class="orderItem"><span class="item">状&nbsp;&nbsp;&nbsp;态&nbsp;&nbsp;&nbsp;&nbsp; :</span><span class="num">充值完成</span></div>
            <div class="orderItem"><span class="item">备&nbsp;&nbsp;&nbsp;注&nbsp;&nbsp;&nbsp;&nbsp; :</span><span class="num"></span></div>
            <div class="orderItem"><span class="item">操&nbsp;&nbsp;&nbsp;作&nbsp;&nbsp;&nbsp;&nbsp; : </span><span class="checkBtn">查看</span></div>
        </div>
      </div>
      <div class="published">
          <div class="onlineBox">
            <div :class="[isActive?active:'']" @click='isActive=true;'>购买</div>
            <div :class="[isActive?'':'active']" @click='isActive=false;'>出售</div>
          </div>
          <div class='onlineItemBox'>
            <div class='onlineBuyStd' :class="[isActive?'active':'']">
              <div>
                <div class='publishItem'><span>币种:</span><span class="info">RYH</span></div>
                <div class='publishItem'><span>支付方式:</span><span class="info">银行卡</span></div>
                <div class='publishItem'><span>数量:</span><span class="info">1.00000</span></div>
                <div class='publishItem'><span>价格/RYH:</span><span class="info">5.00(65-50)</span></div>
                <div class='publishItem'><span>发布时间:</span><span class="info">2018-13-52</span></div>
                <div class='publishItem' style='border-bottom:none;'><span>状态:</span><span class="info">审核中/未通过/通过/已下架 <span>上架</span></span></div>
              </div>
            </div>
            <div class='onlineSellStd' :class="[isActive?'':'active']">
                <div v-for="(item,i) in UserOrders"  v-if="item.isCheck == 1 && item.fOrdertype == 0">
                    <div class='publishItem'><span>币种:</span><span class="info">{{item.fShortName}}</span></div>
                    <div class='publishItem'><span>支付方式:</span><span class="info">{{getfReceipttype(item.fReceipttype)}}</span></div>
                    <div class='publishItem'><span>数量:</span><span class="info">{{item.fBigprice | toFixed}}</span></div>
                    <div class='publishItem'><span>价格/RYH:</span><span class="info">￥{{item.fUnitprice }}</span></div>
                    <div class='publishItem'><span>发布时间:</span><span class="info">{{getTime(item.fCreateTime.time)}}</span></div>
                    <div class='publishItem' style='border-bottom:none;'><span>状态:</span><span class="info">审核中/未通过/通过/已下架 <span>下架/上架</span></span></div>
             </div>
            </div>
              
                
          </div>
         
      </div>
    </div>
  </div>
  <script>
    $('.titleBox span').click(function () {
      $(this).addClass('active').siblings().removeClass('active');
      $('.mainContainer>div').removeClass('active').eq($(this).index()).addClass('active').si
    })
    $('body').on('click','.callMask',function(){
    	window.location.href="${oss_url}/m/otc/OtcApply1.html";
/*     	var txt=$('.sellerLv').text();
    	
    	if(txt.indexOf('超级商家')>-1){
    		layer.msg('已是最高级别！');
    	}
    	if(txt.indexOf('VIP')>-1){
    		$('.mask').css('display','block');
    		$('.VIPseller').remove();
    	}
    	
    	if(txt.indexOf('普通商家')>-1){
    		$('.mask').css('display','block');
    	}
    	$('.upperSeller').eq(0).addClass('active');
 */
    })
    $('body').on('click','.upperSeller',function(){
    	$(this).addClass('active').siblings('.upperSeller').removeClass('active');
    })
  </script>
  <script type="text/javascript">
  $(function(){
      var centerdata = new Vue({
          el: "#container",
          data: {
        	  center : [],
              Orders : [],
              Orders1 : [],
              OrderShells : [],
              OrderShells1 : [],
              UserOrders : [],
              isActive:true,
              active:'active',
              isApply:false
          },
          methods: {
              getfReceipttype : function(type){
                  if(type == 0){
                      return '银行转账';
                  }else if(type == 1){
                      return '支付宝';
                  }else if(type == 2){
                      return '微信';
                  }
              },
              getTime : function (times) {
                  var d = new Date(times);    //根据时间戳生成的时间对象
                  var yy = d.getFullYear(); //年
                  var mm = d.getMonth() + 1; //月
                  var dd = d.getDate(); //日 
                  var hh = d.getHours(); //时 
                  var ii = d.getMinutes(); //分
                  var ss = d.getSeconds(); //秒 
                  var clock = yy + "-";
                  if(mm < 10) clock += "0";
                  clock += mm + "-";
                  if(dd < 10) clock += "0";
                  clock += dd + " ";
                  if(hh < 10) clock += "0";
                  clock += hh + ":";
                  if (ii < 10) clock += "0"; 
                  clock += ii + ":";
                  if (ss < 10) clock += "0"; 
                  clock += ss;
                  return clock;
              }
          },
          filters:{
        	  toFixed:function(value){
        		  return util.numFormat(value,6)
        	  }
          }
      });
      $.post("/m/user/api/info/seller/detail.html",{"userId":'${userId}'},function(obj){
    	  console.log(obj);
    	  centerdata.Orders.center = obj.data;
      });
      $.post("/m/user/api/info/seller.html",{"userId":'${userId}'},function(obj){
			console.log(obj);
			if(obj.data.sellerLevel < 3 && obj.data.sellerStatus != 1){
				centerdata.isApply = true;
			}else if(obj.data.sellerLevel == 3 && obj.data.sellerStatus != 2 && obj.data.sellerStatus != 1){
				centerdata.isApply = true;
			}
			if(obj.data.sellerLevel == 3 && obj.data.sellerStatus == 1){
				$("#call").text("超级商家申请审核中");
			}else if(obj.data.sellerLevel == 2 && obj.data.sellerStatus == 1){
				$("#call").text("vip商家申请审核中");
			}
		});
      /* $.post("/m/trade/getOrders.html",function(obj){
          console.log(obj);
          centerdata.Orders = obj.IngOrders;
          centerdata.Orders1 = obj.overOrders;
      });
      $.post("/m/trade/getOrderShells.html",function(obj){
          console.log(obj);
          centerdata.OrderShells = obj.IngOrders;
          centerdata.OrderShells1 = obj.overOrders;
      });
      $.post("/m/trade/getUserOrders.html",function(obj){
          console.log(obj);
          centerdata.UserOrders = obj.UserOrders;
      }); */
  });
  </script>
</body>

</html>