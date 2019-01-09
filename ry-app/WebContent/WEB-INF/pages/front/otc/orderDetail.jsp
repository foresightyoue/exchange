
<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat"
	pageEncoding="UTF-8"%>
<%@page import="cn.cerc.jdb.core.TDateTime"%>
<%@page import="java.util.Date"%>
<%@ include file="../comm/include.inc.jsp"%>
	<title>订单详情</title>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <base href="${basePath}"/>
    <%@include file="../comm/link.inc.jsp"%>
    <link rel="stylesheet" href="/static/front/css/c2c/safe.css">
    <link rel="stylesheet" href="/static/front/css/c2c/module.common.css">
    <link rel="stylesheet" href="/static/front/css/c2c/module.base.css">
    <link rel="stylesheet" href="/static/front/js/layim-v3.7.7/src/css/layui.css" media="all">
    <link href="//netdna.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="//at.alicdn.com/t/font_48446_inzlc0gmji3sor.css" rel="stylesheet">
    <style type="text/css">
        .top-fixed-nav {
			position: fixed;
			left: 0;
			top: 0;
			z-index: 100;
		}
		.exchangetlist .status5 {
			width: 25%;
			display: inline-block;
			text-align: right;
			color: #333;
			float: right;
		}
		.layui-layer{
		    top: 50px !important; 
		    /* margin-top: -120px !important;    */
		}
		input::-webkit-outer-spin-button, input::-webkit-inner-spin-button {
		    -webkit-appearance: none !important;
		    margin: 0;
		}
		input[type="number"]{ 
		    -moz-appearance:textfield;
		}
		/* .alertMars{
		    width: 100%;
		    height: 100%;
		    background-color: black;
		    position: absolute;
		    left: 0;
		    top: 0;
		    z-index: 10;
		} */
		.alertContent{
		    width: 40%;
		    min-width: 420px;
		    /* height: 265px; */
		    position: absolute;
		    top: 50%;
		    left: 50%;
		    transform: translate(-50%,-50%);
		    background-color: white;
		    /* z-index: 100; */
		    border-radius:3px;
		    -webkit-border-radius:3px;
		    color: #333;
		    padding: 10px  20px 20px;
		    line-height: 30px;
            text-align: left;
		}
		.alertContent p span{
            display: inline-block;
            width: 150px;
            text-align: right;
            margin-right: 10px;
            background-color: rgba(255,152,0,.1);
            border-right: 1px solid #FF9800; 
            padding-right: 10px;
            color: #333;
        }
        .alertContent p .text-danger{
			border: 0;
			background-color: transparent;
			text-align: left;
        }
        .alertContent p{
            border: 1px solid #FF9800; 
            border-bottom: 0;
            margin-bottom: 0;
        }
        .message_hint{
            font-size: 13px;
            color: #808080;
            line-height: 30px;
        }
        .message_hint2{
            font-size: 13px;
            color: #808080;
            line-height: 30px;
            border-top: 1px solid #e6e6e6; 
        }
        .message_title{
            font-size: 18px;
            margin-bottom: 10px;
            line-height: 30px;
            height: 30px;
        }
        .error{
            position: absolute;
            width: 25px;
            top: 5px;
            right: 10px;
        }
        .kown{
            height: 28px;
            line-height: 25px;
            border: 0;
            background-color: #FF9800;
            color: white;
            padding: 0 10px;
            margin-top: 10px;
        }
        .text-danger{
            color: red !important;
        }
        .exchangetlist .clone{
            position: relative;
        }
        .exchangetlist .clone .typeshow{
            position: absolute;
            left: 59%;
        }
        .table>thead>tr>th, .table>tbody>tr>th, .table>tfoot>tr>th, .table>thead>tr>td, .table>tbody>tr>td, .table>tfoot>tr>td {
            font-size: 14px;
        }
        .table>thead>tr>th, .table>tbody>tr>td{
            text-align: left;
        }
        .table>tbody>tr>td{
            padding: 20px 10px;
        }
        .table>thead>tr {
            background-color: #fafafa;
        }
        #exchangeRecord{
            margin: 0;
        }
        #exchangeRecord .table{
            margin: 0;
        }
        .table-responsive {
            margin: 10px 20px;
        }
        .buy_message{
            font-size: 14px;
            color: #666;
            border-bottom: 1px solid #e6e6e6;
            padding: 10px 20px 45px 35px;
            text-align: left;
        }
        .font-w{
            font-weight: 600;
        }
        .order_number{
            font-size: 12px;
            color: #999;
            float: right;
            margin-top: 20px;
        }
        .important_hint{
            color: #1db3b4 !important;
        }
        .sum_money{
            background-color: #fafafa;
            padding: 10px 20px;
            line-height: 30px;
            font-size: 12px;
            color: #999;
            text-align: right;
        }
        .sum_money .font-w{
            font-size: 14px;
            color: #666;
        }
        .sum_money .important_hint.font-w{
            font-size: 16px;
        }
        .fl{
            float: left;
        }
        .pay_box{
            padding: 10px 20px 20px;
            font-size: 14px;
            color: white;
            border: 1px solid #e6e6e6;
            margin: 0 20px;
            text-align: left;
        }
        .pay_style{
            display: inline-block;
            padding: 10px;
            border: 1px solid #e6e6e6;
            border-radius: 4px;
            cursor: pointer;
        }
        .pay_hint{
            padding-bottom: 10px;
            width: 100%;
            border-bottom: 1px solid #e6e6e6;
            font-size: 14px;
            color: #666;
            margin-bottom: 10px;
        }
        .pay_imgBox{
            width: 100%;
            text-align: center;
        }
        .pay_img{
            width: 200px;
            height: 200px;
            margin: 20px auto;
        }
        .pay_attention{
            margin: 35px auto 30px;
            max-width: 500px;
            width: 100%;
            text-align: center;
            font-size: 14px;
        }
        .attention_hint{
            margin-bottom: 5px;
        }
        .btn_group{
            width: 100%;
        }
        .btn_group button{
            margin-bottom: 20px;
            padding: 10px 0;
            border-radius: 4px;
            width: 100%;
            border: 1px solid #e6e6e6;
            background-color: transparent;
        }
        .alert-info {
            background-color: #d9edf7;
            border-color: #bce8f1;
            color: #31708f;
            padding: 15px 15px 10px 30px;
            font-size: 14px;
            line-height: 25px;
            border-radius: 5px;
            padding: 15px 15px 10px 30px;
			margin: 20px;
        }
        .chat_box{
            margin: 20px 0;
            background-color: #F8F9FA;
            /* padding: 0 20px; */
        }
        .chat_hint{
            background-color: #EAEAEA;
            line-height: 40px;
            font-size: 16px;
            border-radius: 4px;
            padding-left: 20px;
            text-align: left;
        }
        #msgFromPush{
            height: 300px;
            overflow: auto;
            padding: 0 20px;
            border-bottom: 1px solid #e6e6e6;
        }
        .time-tips{
            padding: 5px 10px;
            background-color: white;
            margin-top: 5px;
            border-radius: 4px;
            display: inline-block;
            line-height: 25px;
        }
        .message_name{
            display: block;
            font-size: 14px;
        }
        .me-item{
            text-align: right;
            padding: 5px 0;
        }
        .friend-item{
            text-align: left;
            padding: 5px 0;
        }
        .send-area{
            padding: 20px;
            width: 100%;
        }
        #msg{
            width: 90%;
            border: 1px solid #e6e6e6;
            border-radius: 4px;
            height: 30px;
            line-height: 30px;
            padding-left: 15px;
            color: #666;
            font-size: 14px;
        }
        #btn{
            padding: 5px 20px;
            background: #1db3b4;
            margin-left: 20px;
            border-radius: 4px;
            font-size: 14px;
            color: white;
        }
        .me-msg{
            font-size: 14px;
            color: #666;
        }
        #bankImg div{
        	color: #666;
        }
        .layim-chat-main ul li,.layim-chat-tool{
            text-align: left;
        }
        .layim-chat-text .layui-layim-photos{
            width: 100px;
        }
        .pay_imgBox div{
            color: #666;
            text-align: center;
            line-height: 25px;
        }
        .btn_group a {
		    margin-bottom: 20px;
		    padding: 10px 0;
		    border-radius: 4px;
		    width: 100%;
		    border: 1px solid #e6e6e6;
		    background-color: transparent;
		    text-align: center; 	
		    display: inline-block; 	 	
		    margin-top: 10px;
		}
    </style>
</head>
<%@include file="../comm/header.jsp"%> 
<body>
<div class="bk-onekey financen " style="padding-top: 50px;">
    <div class="container">
        <div class="finance-rd" style="width:100%; margin-left:0;">
            <div class="bk-tabList">
                <div class="bk-c2c-nav bk-band clearfix">
                    <%-- <c:forEach var="coin" items="${cointTypeList }"  >
                        <a href="${oss_url}/ctc/index.html?fId=${coin.fId }" class="${coinMap.fId eq coin.fId ?'active':''}">${coin.fShortName } 交易</span>
                    </c:forEach> --%>
                    <!-- <a class="active" href="/exchange/qccny">QC 交易</a>
                    <a class="" href="/exchange/usdtcny">USDT 交易</a>
                    <span>BTC 交易</span>
                    <span>ETH 交易</span> 
                    <span>ETC 交易</span>-->
                    <%-- <c:if test="${login_user.fIsMerchant ne 0 && !empty login_user.fIsMerchant}">
                        <a class="btn card-add" role="button" href="/ctc/merchantCenter.html?fId=${login_user.fid }"><i class="iconfont icon-tianjialeimu"></i>商户订单</a>
                    </c:if> --%>
                    <a href="javascript:void(0)" class="active main-nav">订单支付</a>
                    <!-- <a class="btn card-admin" role="button" href="/otc/toPublishAd.html" ><i class="iconfont icon-cc-card-o"></i>刊登广告</a>
                    <a class="introabtn " href="/about/index.html?id=93" target="_blank"><i class="fa fa-cube fa-fw"></i> 操作说明</a> -->
                    <a class="btn card-admin" onclick="self.location=document.referrer;" role="button" href="javascript:history.go(-1)"  style="color: #666" ><img alt="" src="/static/front/images/aui-icon-back.png"> 返回</a>
                </div>
                <div class="bk-pageTit" id="exchangeRecord">
                    <%-- <h4 class="pull-left"><i class="bk-ico assetRecord"></i>订单详情<a href="${oss_url}/ctc/list.html" style="float:right;">查看更多>></a></h4> --%>
                    <!-- <div class="clearfix"></div> -->
                    <div class="buy_message">
                
                    	<p><span>买家：${buyuser }(${buyUsr_name })</span></p>
                    	<p><span>卖家：${selluser }(${sellUsr_name })</span></p>
                    	<%-- <p><span><a href="#" onclick="cd('${orderD.items.fOrderId}')" style="padding: 5px 10px;background-color: #1db3b4;color: white;border-radius: 4px;margin-bottom: 5px;float: left">催单</a></span></p> --%>
                    	<input id="urge" type="hidden" value="1"/>
                        <%-- <div>您向<span class="font-w">
                        <c:if test="${orderD.items.orderType == 1 }">${orderD.items.fTrade_Object }</c:if>
                        <c:if test="${orderD.items.orderType == 2 }">${orderD.items.fUsr_id }</c:if>
                        </span>
                        <span class="important_hint font-w">
                        	<c:if test="${orderD.items.orderType == 1 }">购买</c:if>
                        	<c:if test="${orderD.items.orderType == 2}">出售</c:if>
                        	</span>
                        <span class="font-w">${fShortName }</span></div> --%>
                        <div class="order_number">订单编号：${orderD.items.fOrderId }</div>
                        <input type="hidden" value="${orderD.items.fOrderId }" name="orderId">
                        <input type="hidden" value="${orderD.items.fAd_Id }" name="fAd_Id">
                        <input type="hidden" value="${fShortName }" name="fShortName">
                        <input type="hidden" value="${fw }" name="fw">
                    </div>
                    <div class="table-responsive ">
                        <table id="billDetail" class="table table-striped table-bordered table-hover">
                            <thead>
                                <tr>
                                    <th width="20%">交易金额</th>
                                    <th width="20%">交易数量</th>
                                    <th width="10%">单价</th>
                                    <th width="30%">付款期限</th>
                                    <th width="15%">交易方式</th>
                                    <th width="5%">订单状态</th>
                                </tr>
                            </thead>
                            <tbody>
	                                <tr style="background-color: white;">
	                                    <td>${orderD.items.fTrade_SumMoney }</td>
	                                    <td>${orderD.items.fTrade_Count }</td>
	                                    <td>${orderD.items.fTrade_Price }</td>
	                                    <td id="fTrade_DeadTime">
	                                    <fmt:formatDate value="${orderD.items.fTrade_DeadTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
	                                    </td>
	                                    <td>${orderD.items.fTrade_Method }</td>
	                                    <td>
	                                   		<c:if test="${orderD.items.fTrade_Status=='1' }">已下单</c:if> 
	                                   		<c:if test="${orderD.items.fTrade_Status=='2' }">待验证</c:if> 
	                                   		<c:if test="${orderD.items.fTrade_Status=='3' }">已完成 </c:if> 
	                                   		<c:if test="${orderD.items.fTrade_Status=='4' }">已取消</c:if> 
	                                    </td>
	                                </tr>
                            </tbody>
                        </table>
                        <div class="sum_money">
                            <span class="fl"><!-- 交易手续费：免费 --></span>
                            <input type="hidden" name="SumMoney_" value="${orderD.items.fTrade_SumMoney }">
                            <div>总额：<span class="important_hint font-w">${orderD.items.fTrade_SumMoney } CNY</span></div>
                            <div>兑换：<span class="font-w">${orderD.items.fTrade_Count } ${fShortName }</span></div>
                        </div>
                    </div>
                    <div class="pay_box">
                        <div class="pay_hint">卖家收款账号</div>
                        <c:if test="${fn:contains(orderD.items.fTrade_Method,'支付宝')==true}">
                            <span class="pay_style important_hint" id="zhifubao">支付宝</span>
<%--                             <c:if test="${orderD.items.fType == '2'  }">  --%>
                                <%-- <img src="${orderD.items.fImgUrl }"> --%>
                                <div class="pay_imgBox" id="zhifubaoImg" style="display:none;">
                                	<div>用户姓名：${method.items.fName }</div>
                                    <div>支付宝账号：${method.items.fAccount }</div>
                               		<img alt="" src="${method.items.fImgUrl }" class="pay_img">
                                </div>
<%--                             </c:if> --%>
                        </c:if>
                        <c:if test="${fn:contains(orderD.items.fTrade_Method,'微信')==true}">
                            <span class="pay_style important_hint" id="weixin">微信</span>
<%--                             <c:if test="${orderD.items.fType =='1' }"> --%>
                                <%-- <img src="${orderD.items.fImgUrl }"> --%>
                                <div class="pay_imgBox" id="weixinImg" style="display:none;">
                                	<div>用户姓名：${method.items.fName }</div>
                                	<div>微信账号：${method.items.fAccount }</div>
                                	<img alt="" src="${method.items.fImgUrl }" class="pay_img">
                                </div>
<%--                             </c:if> --%>
                        </c:if>
                        <c:if test="${fn:contains(orderD.items.fTrade_Method,'银行转账')==true}">
                            <span class="pay_style important_hint" id="bank">银行卡</span>
<%--                             <c:if test="${orderD.items.fType =='0' }"> --%>
                               <%--  <img src="${orderD.items.fImgUrl }"> --%>
                                <div class="pay_imgBox" id="bankImg" style="display:none;">
                                	<div>开户行：${bankNew.items.fName }</div>
                                	<div>用户名：${bankNew.items.fRealName }</div>
                                	<div>银行账号：${bankNew.items.fBankNumber }</div>
<%--                                 	<img alt="" src="${orderD.items.fImgUrl }" class="pay_img"> --%>
                                </div>
<%--                             </c:if> --%>
                        </c:if>
                    </div>
                    <div style="margin-top: 10px;">订单数字货币已被系统锁定托管</div>
                    <div style="color: #666;margin: 10px 0 0;">使用微信、支付宝、银行转账时，请<span style="color: red;">不要添加任何备注内容</span>!!!否则可能会被支付系统拦截</div>
                    <div id="BOX"></div>
                    <!-- <div class="chat_box">
                        <div class="chat_hint">聊天窗口</div>
                        <div id="msgFromPush">
						    <ul class="mag-block"></ul>
					    </div>
					    <div class="send-area">
					       <div style="position:relative;margin-bottom:  10px;">
					       <label for="add_file"><img alt="" src="/static/front/images/add_img.png" style="width: 25px;position: absolute;top: -24px;left: 0;"></label>
					       <input type="file" id="add_file" style="display: none">
					       </div>
                            <input type="text" id="msg">
                            <a href="javascript:void(0)" id="btn" onclick="send()">发送</a>
					    </div>
                    </div> -->
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
	                        
	                        
                            
	                        <!-- <div class="btn_group"><button>延长订单有效期</button></div> -->
	                        <c:if test="${orderD.items.fTrade_Status ==1}">
	                        	<div class="btn_group"><a href="/otc/cancleOrder.html?coinType=${coin_id}&orderId=${orderD.items.fOrderId }&fw=${fw}">取消交易</a></div>
	                        	
	                        </c:if>
	                        <div class="attention_hint1"><strong>请勿随意取消订单!</strong> 任意取消订单者，被交易对象投诉将影响系统信用分数，分数过低者帐号会被冻结数天。</div>
	                    </div>
                    </c:if>
                    <div class="alert-info text-left tips-box">
                        <ul>
                            <li>1、不需要提供钱包地址给卖家，交易完成后数字货币将自动转入您在 RYH上的钱包中</li>
                            <li>2、场外交易时间通常为45分钟，具体时间依据卖家是否在线，及确认帐款的时间而有所调整，请耐心等待</li>
                            <li>3、若长时间未收到卖家的回复，或卖家未释放数字货币，您可以提交工单发起申诉，客服人员将协助处理</li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div> 
<input type="hidden" id="uuid" value="">
<input type="hidden" class="coin_id" value="${coin_id}" name="coin_id">

<script src="/static/front/js/c2c/jquery-1.11.3.min.js"></script>
<script src="/static/front/js/layim-v3.7.7/src/layui.js"></script>
<script src="/static/front/js/layim-v3.7.7/src/lay/modules/upload.js"></script>
<%@include file="../comm/footer.jsp"%>
<script src="/static/front/js/c2c/jquery.SuperSlide.2.1.1.js"></script>
<script src="/static/front/js/comm/util.js"></script>
<script src="/static/front/js/otc/publishorder.js"></script>

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
		    	window.location.href="/otc/cancleOrder.html?coinType=${coin_id}&orderId="+orderId+"&fw="+fw;
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


</script> 
</body>