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
<meta name="viewport"
    content="initial-scale=1, maximum-scale=1, user-scalable=no, width=device-width">
<meta name="format-detection" content="telephone=no">
<meta name="format-detection" content="email=no">
    <base href="${basePath}"/>
    <title>订单广场</title>
    <!-- <link rel="stylesheet" href="/static/front/css/c2c/safe.css">
    <link rel="stylesheet" href="/static/front/css/c2c/module.common.css">
    <link rel="stylesheet" href="/static/front/css/c2c/module.base.css">
    <link rel="stylesheet" href="/static/front/css/otc/common_otc.css">
    <link rel="stylesheet" href="/static/front/css/otc/buy_list.css"> -->

    <!-- <link href="//netdna.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="//at.alicdn.com/t/font_48446_inzlc0gmji3sor.css" rel="stylesheet"> -->
    <link href="/static/front/app/js/c2c/common.css" rel="stylesheet">
    <link href="/static/front/app/js/c2c/otc-app.css" rel="stylesheet">
</head> 
<body style="background-color: #f5f5f5;">
<div class="header">
    <a href="/m/otc/otcMenu.html?menuFlag=otc" class="prev"></a>
    <div class="head_title font15">
        <input type="hidden" value="${code }" name="code">
        <input type="hidden" value="${message }" name="message">
        <ul class="flexLayout font15">
            <li class="tc border_r nav-active main-nav"><a href="javascript: void(0);" class="main-nav-a">购买</a></li>
            <!-- <li class="tc border_r active">购买</li> -->
            <li class="tc main-nav"><a href="javascript: void(0);" class="main-nav-a">出售</a></li>
        </ul>
    </div>
    <div class="more more-z"><a href="/m/otc/orderMenu.html?coinType=${coinType}"><img src="/static/front/images/c2c/ordeLrist.png" alt=""></a></div>
</div>
<div id="mat"></div>
<%-- <ul class="order_navBtn flexLayout">
    <!--<li class="font13">-->
        <!--<div>ETC</div>-->
        <!--<div>暂未上线</div>-->
    <!--</li>-->
    <li class="nav-active font13">
        <div>${coinTypeName}</div>
        <!-- <div>参考价6-7</div> -->
    </li>
 <!--    <li class="font13">
        <div>AT</div>
        <div>暂未上线</div>
    </li> -->
</ul> --%>

  <%-- <div class="container">
     <input type="hidden" value="${code }" name="code">
     <input type="hidden" value="${message }" name="message">
     <a href="javascript:void(0)" class="active main-nav">购买</a>
     <a href="javascript:void(0)" class="main-nav">出售</a>
 </div> --%>
  
<!-- ==================================买入==================================== -->
<div class="container main-content" style="padding-bottom: .935rem;">
      <%-- <div class="coin-tab buycoin-tab">
           <c:forEach var="coin" items="${cointTypeList }">
               <a href="javascript:void(0)" class="coin ${coinMap.fId eq coin.fId ?'current':''}">${coin.fShortName }</a>
           </c:forEach>
      </div> --%>
    <div class="buyCoin_list">
        <div class="table buy">
             <!-- 币种为6的订单 -->
            <c:forEach var="item" items="${ordersList}" varStatus="vs">
                    <div class="sell_mainList mainList">
                        <ul class="order_list mt_10">
                            <li class="flexLayout">
                                <div class="user_img">
                                    <input class="coin_Id" value="${coinType}" type="hidden">
                                    <input class="fId" value="${item.fId }" type="hidden">
                                    <input class="fUsr_id" value="${item.fUsr_id }" type="hidden">
                                    <img src="/static/front/images/c2c/userImg.png" alt="">
                                    <c:if test="${item.isRealName eq 1}">
                                        <div class="realName font10"><span>未实名</span></div>
                                    </c:if>
                                    <c:if test="${item.isRealName eq 0}">
                                        <div class="realName font10"><span>已实名</span></div>
                                    </c:if>
                                    
                                </div>
                                <div class="user_message">
                                    <div>
                                        <span class="font15">${item.fUsr_Name }</span>
                                        <c:if test="${fn:contains(item.fReceipttype,0) }"><span class="pay_style font10 payMethod">银行转账</span></c:if>
                                        <c:if test="${fn:contains(item.fReceipttype,1) }"><span class="pay_style font10 payMethod">支付宝</span></c:if>
                                        <c:if test="${fn:contains(item.fReceipttype,2) }"><span class="pay_style font10 payMethod">微信</span></c:if>
                                    </div>
                                    <div class="font12 text-gray td-normal">交易<span class="jiaoyi">${item.counts }</span> <%-- | 好评度 <span class="haoping">${item.assess }</span>% | 信任 528 --%></div>
                                    <div class="font12 text-gray limit_money">${item.fSmallprice } - ${item.fBigprice } CNY</div>
                                </div>
                                <div class="user_operation tr">
                                    <div class="unit_Price font13 td-price">${item.fUnitprice }CNY</div>
                                    <div class="operation_btn font12" style="cursor: pointer;" onclick="buy(this)">购买${item.fShortName}</div>
                                </div>
                            </li>
                        </ul>
                    </div>
                  <%-- <div>
                     <ul> 
                        <li>
                          <div>
                                <input class="coin_Id" value="${coinMap.fId}" type="hidden">
                                <input class="fId" value="${item.fId }" type="hidden">
                                <input class="fUsr_id" value="${item.fUsr_id }" type="hidden">
                                <a href="javascript:void(0);" class="td-usernameImg"><span class="user-head"></span></a><a class="td-username" href="javascript:void(0);">${item.fUsr_Name }<br>
                                 <c:if test="${item.isRealName eq 1}">
                                    <span class="is-realname">未实名</span>
                                 </c:if>
                                  <c:if test="${item.isRealName eq 0}">
                                      <span class="is-realname">已实名</span>
                                  </c:if>
                                </a>
                           </div>
                        </li>
                        <li class="td-normal">交易<span class="jiaoyi">${item.counts }</span> | 好评度 <span class="haoping">${item.assess }</span>% | 信任 528</li>
                        <li class="payMethod">
                          <c:if test="${item.fReceipttype eq 0 }">银行转账</c:if>
                                <c:if test="${item.fReceipttype eq 1 }">支付宝</c:if>
                                <c:if test="${item.fReceipttype eq 2 }">微信</c:if>
                        </li>
                        <li class="td-name limit_money">${item.fSmallprice } - ${item.fBigprice }CNY</li>
                        <li class="td-price">${item.fUnitprice }CNY</li>
                        <li><a href="javascript:void();" class="btn submit" style="line-height: 23px;" onclick="buy(this)">购买USDT</a></li>
                     </ul>
                  </div> --%>
            </c:forEach>
            <%-- <div class="table buy" style="margin: 0;border-top: 0;display: none;">
               <!-- 币种为7的订单 -->
                <c:forEach var="item" items="${ordersList}" varStatus="vs">
                    <c:if test ="${item.fAm_fId eq 7 }">
                    <div class="sell_mainList mainList">
                        <ul class="order_list mt_10">
                            <li class="flexLayout">
                                <div class="user_img">
                                    <input class="coin_Id" value="${coinMap.fId}" type="hidden">
                                    <input class="fId" value="${item.fId }" type="hidden">
                                    <input class="fUsr_id" value="${item.fUsr_id }" type="hidden">
                                    <img src="/static/front/images/c2c/userImg.png" alt="">
                                    <c:if test="${item.isRealName eq 1}">
                                        <div class="realName font10"><span>未实名</span></div>
                                    </c:if>
                                    <c:if test="${item.isRealName eq 0}">
                                        <div class="realName font10"><span>已实名</span></div>
                                    </c:if>
                                    
                                </div>
                                <div class="user_message">
                                    <div>
                                        <span class="font15">${item.fUsr_Name }</span>
                                        <c:if test="${item.fReceipttype eq 0 }"><span class="pay_style font10 payMethod">银行转账</span></c:if>
                                        <c:if test="${item.fReceipttype eq 1 }"><span class="pay_style font10 payMethod">支付宝</span></c:if>
                                        <c:if test="${item.fReceipttype eq 2 }"><span class="pay_style font10 payMethod">微信</span></c:if>
                                    </div>
                                    <div class="font12 text-gray td-normal">交易<span class="jiaoyi">${item.counts }</span> | 好评度 <span class="haoping">${item.assess }</span>% | 信任 528</div>
                                    <div class="font12 text-gray limit_money">${item.fSmallprice } - ${item.fBigprice } CNY</div>
                                </div>
                                <div class="user_operation tr">
                                    <div class="unit_Price font13 td-price">${item.fUnitprice }CNY</div>
                                    <div class="operation_btn font12" style="cursor: pointer;" onclick="buy(this)">购买AT</div>
                                </div>
                            </li>
                        </ul>
                    </div> --%>
                  <%-- <div>
                     <ul> 
                        <li>
                          <div>
                                <input class="coin_Id" value="${coinMap.fId}" type="hidden">
                                <input class="fId" value="${item.fId }" type="hidden">
                                <input class="fUsr_id" value="${item.fUsr_id }" type="hidden">
                                <a href="javascript:void(0);" class="td-usernameImg"><span class="user-head"></span></a><a class="td-username" href="javascript:void(0);">${item.fUsr_Name }<br>
                                 <c:if test="${item.isRealName eq 1}">
                                    <span class="is-realname">未实名</span>
                                 </c:if>
                                  <c:if test="${item.isRealName eq 0}">
                                      <span class="is-realname">已实名</span>
                                  </c:if>
                                </a>
                           </div>
                        </li>
                        <li class="td-normal">交易<span class="jiaoyi">${item.counts }</span> | 好评度 <span class="haoping">${item.assess }</span>% | 信任 528</li>
                        <li class="payMethod">
                          <c:if test="${item.fReceipttype eq 0 }">银行转账</c:if>
                                <c:if test="${item.fReceipttype eq 1 }">支付宝</c:if>
                                <c:if test="${item.fReceipttype eq 2 }">微信</c:if>
                        </li>
                        <li class="td-name limit_money">${item.fSmallprice } - ${item.fBigprice }CNY</li>
                        <li class="td-price">${item.fUnitprice }CNY</li>
                        <li><a href="javascript:void();" class="btn submit" style="line-height: 23px;" onclick="buy(this)">购买AT</a></li>
                     </ul>
                  </div> --%>
       

        </div>
    </div>
 </div>
 </div>
 
<!-- ====================================卖出============================================= -->
<div class="container main-content" style="display: none;padding-bottom: .935rem;">
   <%-- <div class="coin-tab sellcoin-tab">
       <c:forEach var="coin" items="${cointTypeList }"  >
            <a href="javascript:void(0);" class="coin ${coinMap.fId eq coin.fId ?'current':''}">${coin.fShortName }</a>
        </c:forEach>
   </div> --%>
    <div class="sellCoin_list">
       <!-- 币种为6的卖出订单 -->
        <div class="table buy">
            <c:forEach var="item" items="${ordersSellList}" varStatus="vs">
                    <div class="sell_mainList mainList">
                        <ul class="order_list mt_10">
                            <li class="flexLayout">
                                <div class="user_img">
                                    <input class="coin_Id" value="${coinType}" type="hidden">
                                    <input class="fId" value="${item.fId }" type="hidden">
                                    <input class="fUsr_id" value="${item.fUsr_id }" type="hidden">
                                    <img src="/static/front/images/c2c/userImg.png" alt="">
                                    <c:if test="${item.isRealName eq 1}">
                                        <div class="realName font10"><span>未实名</span></div>
                                    </c:if>
                                    <c:if test="${item.isRealName eq 0}">
                                        <div class="realName font10"><span>已实名</span></div>
                                    </c:if>
                                    
                                </div>
                                <div class="user_message">
                                    <div>
                                        <span class="font15">${item.fUsr_Name }</span>
                                        <c:if test="${fn:contains(item.fReceipttype,0) }"><span class="pay_style font10 payMethod">银行转账</span></c:if>
                                        <c:if test="${fn:contains(item.fReceipttype,1) }"><span class="pay_style font10 payMethod">支付宝</span></c:if>
                                        <c:if test="${fn:contains(item.fReceipttype,2) }"><span class="pay_style font10 payMethod">微信</span></c:if>
                                    </div>
                                    <div class="font12 text-gray td-normal">交易<span class="jiaoyi">${item.counts }</span> <%-- | 好评度 <span class="haoping">${item.assess }</span>% | 信任 528 --%></div>
                                    <div class="font12 text-gray limit_money">${item.fSmallprice } - ${item.fBigprice } CNY</div>
                                </div>
                                <div class="user_operation tr">
                                    <div class="unit_Price font13 td-price">${item.fUnitprice }CNY</div>
                                    <div class="operation_btn font12" style="cursor: pointer;" onclick="sale(this)">出售${item.fShortName}</div>
                                </div>
                            </li>
                        </ul>
                    </div>
                    <%-- <div>
                     <ul> 
                        <li>
                          <div>
                                <input class="coin_Id" value="${coinMap.fId}" type="hidden">
                                <input class="fId" value="${item.fId }" type="hidden">
                                <input class="fUsr_id" value="${item.fUsr_id }" type="hidden">
                                <a href="javascript:void(0);" class="td-usernameImg"><span class="user-head"></span></a><a class="td-username" href="javascript:void(0);">${item.fUsr_Name }<br>
                                 <c:if test="${item.isRealName eq 1}">
                                    <span class="is-realname">未实名</span>
                                 </c:if>
                                  <c:if test="${item.isRealName eq 0}">
                                      <span class="is-realname">已实名</span>
                                  </c:if>
                                </a>
                           </div>
                        </li>
                        <li class="td-normal">交易<span class="jiaoyi">${item.counts}</span> | 好评度 <span class="haoping">${item.assess }</span>% | 信任 528</li>
                        <li class="payMethod">
                          <c:if test="${item.fReceipttype eq 0 }">银行转账</c:if>
                                <c:if test="${item.fReceipttype eq 1 }">支付宝</c:if>
                                <c:if test="${item.fReceipttype eq 2 }">微信</c:if>
                        </li>
                        <li class="td-name limit_money">${item.fSmallprice } - ${item.fBigprice }CNY</li>
                        <li class="td-price">${item.fUnitprice }CNY</li>
                        <li><a href="javascript:void();" class="btn submit" style="line-height: 23px;" onclick="buy(this)">出售USDT</a></li>
                     </ul>
                  </div> --%>
             </c:forEach>
           </div>
             
        <%-- <div class="table buy" style="margin: 0;border-top: 0;display: none;">
            <!-- 币种为7的卖出订单 -->
            <c:forEach var="item" items="${ordersSellList}" varStatus="vs">
                    <div class="sell_mainList mainList">
                        <ul class="order_list mt_10">
                            <li class="flexLayout">
                                <div class="user_img">
                                    <input class="coin_Id" value="${coinMap.fId}" type="hidden">
                                    <input class="fId" value="${item.fId }" type="hidden">
                                    <input class="fUsr_id" value="${item.fUsr_id }" type="hidden">
                                    <img src="/static/front/images/c2c/userImg.png" alt="">
                                    <c:if test="${item.isRealName eq 1}">
                                        <div class="realName font10"><span>未实名</span></div>
                                    </c:if>
                                    <c:if test="${item.isRealName eq 0}">
                                        <div class="realName font10"><span>已实名</span></div>
                                    </c:if>
                                    
                                </div>
                                <div class="user_message">
                                    <div>
                                        <span class="font15">${item.fUsr_Name }</span>
                                        <c:if test="${item.fReceipttype eq 0 }"><span class="pay_style font10 payMethod">银行转账</span></c:if>
                                        <c:if test="${item.fReceipttype eq 1 }"><span class="pay_style font10 payMethod">支付宝</span></c:if>
                                        <c:if test="${item.fReceipttype eq 2 }"><span class="pay_style font10 payMethod">微信</span></c:if>
                                    </div>
                                    <div class="font12 text-gray td-normal">交易<span class="jiaoyi">${item.counts }</span> | 好评度 <span class="haoping">${item.assess }</span>% | 信任 528</div>
                                    <div class="font12 text-gray limit_money">${item.fSmallprice } - ${item.fBigprice } CNY</div>
                                </div>
                                <div class="user_operation tr">
                                    <div class="unit_Price font13 td-price">${item.fUnitprice }CNY</div>
                                    <div class="operation_btn font12" style="cursor: pointer;" onclick="sale(this)">出售AT</div>
                                </div>
                            </li>
                        </ul>
                    </div> --%>
                  <%-- <div>
                     <ul> 
                        <li>
                          <div>
                                <input class="coin_Id" value="${coinMap.fId}" type="hidden">
                                <input class="fId" value="${item.fId }" type="hidden">
                                <input class="fUsr_id" value="${item.fUsr_id }" type="hidden">
                                <a href="javascript:void(0);" class="td-usernameImg"><span class="user-head"></span></a><a class="td-username" href="javascript:void(0);">${item.fUsr_Name }<br>
                                 <c:if test="${item.isRealName eq 1}">
                                    <span class="is-realname">未实名</span>
                                 </c:if>
                                  <c:if test="${item.isRealName eq 0}">
                                      <span class="is-realname">已实名</span>
                                  </c:if>
                                </a>
                           </div>
                        </li>
                        <li class="td-normal">交易<span class="jiaoyi"></span> | 好评度 <span class="haoping">${item.assess }</span>% | 信任 528</li>
                        <li class="payMethod">
                          <c:if test="${item.fReceipttype eq 0 }">银行转账</c:if>
                                <c:if test="${item.fReceipttype eq 1 }">支付宝</c:if>
                                <c:if test="${item.fReceipttype eq 2 }">微信</c:if>
                        </li>
                        <li class="td-name limit_money">${item.fSmallprice } - ${item.fBigprice }CNY</li>
                        <li class="td-price">${item.fUnitprice }CNY</li>
                        <li><a href="javascript:void();" class="btn submit" style="line-height: 23px;" onclick="buy(this)">出售AT</a></li>
                     </ul>
                  </div> --%>
             
           </div>
           <input type="hidden" name="coinType" class="coinType" value="${coinType}"/>
   </div>
</div>
<%-- <c:if test="${login_user ne null}"> --%>
   <div class="flexLayout foot_btn">
       <div><a href="/m/otc/otcUserOrder.html?coinType=${coinType}" class="font16">我的广告</a></div>
       <div onclick="publishAD();" style="cursor: pointer;">
           <a href="javascript:void (0);" class="font16">发布广告</a>
       </div>
   </div>
<!-- <div>
    <span>
     <a class="introabtn " href="/m/otc/otcUserOrder.html"><i class="fa fa-cube fa-fw"></i>个人广告</a>
   </span>
   <span>
       <a class="btn card-admin advertise" href="javascript:void(0);" ><i class="iconfont icon-cc-card-o"></i>刊登广告
    <ul id="coins"> 
          <li onclick="publishAD(this);">AT<input type="hidden" value="7" name="AT"></li>
       <li onclick="publishAD(this);"><input type="hidden" value="6" name="USDT"></li>
      </ul>
        </a>
   </span>
</div> -->
<%-- </c:if> --%>

<script src="/static/front/js/c2c/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="/static/front/js/plugin/layer/layer.js"></script>
<script type="text/javascript" src="/static/front/app/js/c2c/rem1.js"></script>
<script src="/static/front/js/c2c/jquery.SuperSlide.2.1.1.js"></script>
<script src="/static/front/js/comm/util.js"></script>
<script>
	$(document).ready(
		function(){
			var code = $("input[name='code']").val();
			var message = $("input[name='message']").val();
			if(code == 200){
				alert(message);
			}
		}
	)
</script>
<script type="text/javascript">
	function buy(node){
		var coin_id=$(node).parent().parent().find(".coin_Id").val();
		var jiaoyi=$(node).parent().parent().find(".jiaoyi").text();
		var haoping=$(node).parent().parent().find(".haoping").text();
		var fId=$(node).parent().parent().find(".fId").val();//用户id
		var userId=$(node).parent().parent().find(".fUsr_id").val().trim();//用户id
		var payMethod=$(node).parent().parent().find(".payMethod").text().trim();//支付方式
		var limit_money=$(node).parent().parent().find(".limit_money").text().trim();//单笔限额
		var buyPrice=$(node).parent().parent().find(".td-price").text();//购买价格
		window.location.href="/m/otc/toOrder.html?type=1&fId="+fId+"&coin_id="+coin_id+"&qqq="+haoping+"&jiaoyi="+jiaoyi+"&userId="+userId+"&payMethod="+payMethod+"&limit_money="+limit_money+"&buyPrice="+buyPrice;
	}
	function sale(node){
		var coin_id=$(node).parent().parent().find(".coin_Id").val();
		var jiaoyi=$(node).parent().parent().find(".jiaoyi").text();
		var haoping=$(node).parent().parent().find(".haoping").text();
		var fId=$(node).parent().parent().find(".fId").val();//用户id
		var userId=$(node).parent().parent().find(".fUsr_id").val().trim();//用户id
		var payMethod=$(node).parent().parent().find(".payMethod").text().trim();//支付方式
		var limit_money=$(node).parent().parent().find(".limit_money").text().trim();//单笔限额
		var buyPrice=$(node).parent().parent().find(".td-price").text();//购买价格
		window.location.href="/m/otc/toOrder.html?type=2&fId="+fId+"&coin_id="+coin_id+"&jiaoyi="+jiaoyi+"&haoping="+haoping+"&userId="+userId+"&payMethod="+payMethod+"&limit_money="+limit_money+"&buyPrice="+buyPrice;
	}
	
	function publishAD(){
		$.post("/m/user/api/info/seller.html",{"userId":'${login_user.floginName}'},function(obj){
			console.log(obj);
			if(obj.data.IsMerchant == 0 && obj.data.sellerStatus == 1){
				layer.msg("您的商家申请审核中，请耐心等候");
			}else if(obj.data.IsMerchant>0){
				var typeVal = $(".coinType").val();
				window.location.href="/m/otc/toPublishAd.html?typeVal="+typeVal;
			}else{
				layer.msg("请先申请成为商家");
			};
		});
	}
</script>
<script>
    jQuery("#usdtcnyselllist").slide({
        mainCell: ".bd ul",
        autoPage: true,
        effect: "topLoop",
        autoPlay: true,
        vis: 2,
        delayTime: 1000,
        interTime: 100000
    });
</script>
<script>
    jQuery("#usdtcnybuylist").slide({
        mainCell: ".bd ul",
        autoPage: true,
        effect: "topLoop",
        autoPlay: true,
        vis: 2,
        delayTime: 1000,
        interTime: 60000
    });
    $(function() {
		$("#buyBtn,#sellBtn").click(function() {
			var user = "${login_user}";
			var flag = "${login_user.fpostRealValidate}";
			var cointType = "${coinMap.fId}";
			var fis="${login_user.fIsMerchant}";
			var price;
			var num;
			var tradeType;
			if (user == null || user == "") {
				layer.msg("请先登录!");
				return false;
			}
			if($(this).attr("id") == 'buyBtn'){
				price = $("#buyUnitPrice").val();
				num = $("#buyNumber").val();
				var CnyCount=util.accMul(num, price);
				if(fis==1&&CnyCount>10000000){
                    layer.msg("买入总金额不可超过一千万");
                    $("#needCny").text(CnyCount);
                    return;
                }
                if(fis==0&&CnyCount>1000000){
                    layer.msg("买入总金额不可超过一百万");
                    $("#needCny").text(CnyCount);
                    return;
                }
                if(fis==2&&CnyCount>20000000){
                    layer.msg("买入总金额不可超过两千万");
                    $("#needCny").text(CnyCount);
                    return;
                }
                else{
                     $("#needCny").text(CnyCount);
                }
				tradeType = 0;
			}else{
				price = $("#sellUnitPrice").val();
                num = $("#sellNumber").val();
                tradeType = 1;
			}
			if(num == ""){
				layer.msg("请输入数量！！！");
				return false;
			}
			if(flag == false || flag == "false"){
				layer.msg("请先进行实名认证!!");
				return false;
			}
			var load =  layer.load(1);
			$.post("/ctc/trade.html",{"tradeType":tradeType,"coinType":cointType,"price":price,"num":num,"fis":fis},function(result){
				if(result.code!=0){
					if(result.code == -4){
						layer.confirm('尚未绑定银行卡，是否绑定？', {
							  btn: ['确定','取消'] //按钮
							}, function(){
							  location.href = "/financial/accountbank.html";
							}),function(){
							layer.closeAll();
						}
						layer.close(load);
					}else{
						layer.msg(result.msg);
	                    layer.close(load);
					}
				}else{
					if (tradeType == 0) {
						$.getJSON("/ctc/orderInfo.html",{"orderFid":result.orderFid},function(data){
							layer.close(load);
							if(data.code == 0){
								var content;
								if(data.data.fStatus == 0){
									content = "系统派单中，稍后进入订单详情查看卖家收款信息。";
								}else{
									modal(data.data.realName , data.data.fName , data.data.fBankNumber , data.data.fCnyTotal, result.orderFid , data.data.statusName)
									/* content = "<div class='alertMars'></div><div class='alertContent'><p><span>收款方户名:</span> "+data.data.realName+"</p>"
		                                    +"<p><span>收款方开户行:</span>  "+data.data.fName+"</p>"
		                                    +"<p><span>收款方帐号: </span>  "+data.data.fBankNumber+"</p>"
		                                    +"<p><span>转账金额(￥):</span>  "+data.data.fCnyTotal+"</p>"
		                                    +"<p><span>状态: </span> "+data.data.statusName+"</p><button class='kown'>我知道了</button></div>"; */
								}
								/* layer.open({
						              type: 1,
						              skin: 'layui-layer-rim', //加上边框
						              area: ['420px', '265px'], //宽高
						              content: content,
						              title : "收款方信息",
						              cancel :function(){
						            	  location.reload();
						              },
						              btn : ['我知道了'],
						              yes: function(index, layero){
									    layer.close(index); //如果设定了yes回调，需进行手工关闭
									    location.reload();
									  }
						            }); */
							}else{
								layer.msg(data.data);
							}
						}).fail(function() {
							layer.msg("网络错误，请稍后重试！");
							layer.close(load);
						})
					}else{ 
						layer.msg("下单成功！");
						layer.close(load);
						location.reload();
					 } 
				}
			},"json")
			.fail(function(){layer.msg("网络错误，请稍后重试");layer.close(load);})
		});
		$("#buyNumber ,#sellNumber").on("keyup change",function(){
			var number =  this.value;
			var price =  0;
			if (this.id == "buyNumber") {
				price =  $("#buyUnitPrice").val();
				var CnyCount=util.accMul(number, price);
				$("#needCny").text(CnyCount);
            }else {
				price = $("#sellUnitPrice").val();
				$("#getCny").text(util.accMul(number, price));
			}
		});
	})
	
	function modal(a,b,c,d,e,f){
    var oDiv=document.createElement("div");
    oDiv.className="mars1";
    oDiv.innerHTML=
    	"<div class='alertContent'>"
    	+"<div class='message_title'>汇款订单<img class='error' src='/static/front/app/images/close.png'></div>"
    	+"<div class='message_hint'>1.请按提示信息向该卖家汇款；</div>"
        +"<p><span>收款方户名:</span> "+a+"</p>"
        +"<p><span>收款方开户行:</span>  "+b+"</p>"
        +"<p><span>收款方帐号: </span>  "+c+"</p>"
        +"<p><span>转账金额(￥):</span><span class='text-danger'>"+d+"</span></p>"
        +"<p><span>汇款时备注内容:</span><span class='text-danger'>"+e+"</span></p>"
        +"<p style='border: 1px solid #FF9800;'><span>状态: </span> "+f+"</p>"
        +"<div class='message_hint'>2.买家为认证商户，可放心付款；</div>"
        +"<div class='message_hint'>3.汇款时请一定要填写备注信息；</div>"
        +"<div class='message_hint'>4.买家确认收到款后，自动充值到钱包。如超过24小时未收到币，请向客服反馈解决。</div>"
        +"<div class='message_hint'>5.单天最多能发起10笔卖出订单</div>"
        +"<div class='message_hint2'>温馨提示：如有任何疑问请联系在线客服或查看帮助中心</div>" 
        +"<div style='text-align: center;'><button class='kown'>我知道了</button></div></div>";
    var oStyle=document.createElement("style");
    oStyle.innerHTML=
        ".mars1{"+"width: 100%;height: 100%;background-color: rgba(0, 0, 0, 0.6);position: fixed;top: 0;left:0;z-index: 1000;"+"}";
    document.getElementsByTagName('html')[0].getElementsByTagName('head')[0].appendChild(oStyle);
    document.body.insertBefore(oDiv,document.body.children[0]);

    $("body").on("click",".kown,.error",function(){
        $(".mars1").remove();
    });
};

$(function(){
	/* 购买出售切换 */
	/* $("body").on("click",".main-nav",function(){
		$(".main-nav").each(fuction(index){
				$(this).addClass("active").siblings(".main-nav").removeClass("active");
				$(".main-content").eq(index).show().siblings(".main-content").hide();
		})
	}) */
	$("body").on("click",".main-nav",function(){
		$(this).addClass("nav-active").siblings(".main-nav").removeClass("nav-active");
		$(".main-content").eq($(this).index()-2).show().siblings(".main-content").hide();
	});
	
	/* 购买出售币种切换 */
	$("body").on("click",".buycoin-tab a",function(){
		$(this).addClass("current").siblings().removeClass("current");
		$(".buyCoin_list .table").eq($(this).index()).show().siblings().hide();
	});
	$("body").on("click",".sellcoin-tab a",function(){
		$(this).addClass("current").siblings().removeClass("current");
		$(".sellCoin_list .table").eq($(this).index()).show().siblings().hide();
	});
	
});
</script>
</body>