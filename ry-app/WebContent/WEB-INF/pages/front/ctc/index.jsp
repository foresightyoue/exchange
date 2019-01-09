<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat"
	pageEncoding="UTF-8"%>
<%@ include file="../comm/include.inc.jsp"%>

<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <base href="${basePath}"/>
    <%@include file="../comm/link.inc.jsp"%>
    <title></title>
    <link rel="stylesheet" href="/static/front/css/c2c/safe.css">
    <link rel="stylesheet" href="/static/front/css/c2c/module.common.css">
    <link rel="stylesheet" href="/static/front/css/c2c/module.base.css">

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
		    top: 50% !important;
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
    </style>
</head>
<%@include file="../comm/header.jsp"%> 
<body>
<div class="bk-onekey financen " style="padding-top: 50px;">
    <div class="container">
        <div class="finance-rd" style="width:100%; margin-left:0;">
            <div class="bk-tabList">
                <div class="bk-c2c-nav bk-band clearfix">
                    <c:forEach var="coin" items="${cointTypeList }"  >
                        <a href="${oss_url}/ctc/index.html?fId=${coin.fId }" class="${coinMap.fId eq coin.fId ?'active':''}">${coin.fShortName } 交易</span>
                    </c:forEach>
                    <!-- <a class="active" href="/exchange/qccny">QC 交易</a>
                    <a class="" href="/exchange/usdtcny">USDT 交易</a>
                    <span>BTC 交易</span>
                    <span>ETH 交易</span> 
                    <span>ETC 交易</span>-->
                    <c:if test="${login_user.fIsMerchant ne 0 && !empty login_user.fIsMerchant}">
                        <a class="btn card-add" role="button" href="/ctc/merchantCenter.html?fId=${login_user.fid }"><i class="iconfont icon-tianjialeimu"></i>商户订单</a>
                    </c:if>
                    <a class="btn card-admin" role="button" href="/financial/accountbank.html" ><i class="iconfont icon-cc-card-o"></i>银行卡管理</a>
                    <a class="introabtn " href="/about/index.html?id=93" target="_blank"><i class="fa fa-cube fa-fw"></i> 操作说明</a>
                </div>
                <div class="bk-tabList-bd bk-onekey-form bk-c2c-contlist">
                    <div class="bk-tabList-list-usdt active">
                        <div class="no-usdt-w ">
                        </div>
                        <div class="bk-c2c-bd">
                            <div class="row">
                                <div class="col-xs-12">
                                    <div class="row">
                                        <div class="col-xs-6 buy">
                                            <h3 class="b-title">买入 ${coinMap.fShortName }</h3>
                                            <div id="buyDefaultForm">
                                                <div class="form-group has-feedback form-subline">
                                                    <label for="buyUnitPrice" class="control-label"><span class="buyDefaultLabel">买入价</span> (￥)</label>
                                                    <div class="input-group">
                                                        <input type="hidden" value="1.0000">
                                                        <input type="text" id="buyUnitPrice" name="buyUnitPrice" readonly="readonly" value="${coinMap.fctcBuyPrice}" class="form-control form-second">
                                                    </div>
                                                </div>
                                                <div class="form-group has-feedback form-subline">
                                                    <label for="buyNumber" class="control-label">买入量 ( ${coinMap.fShortName })</label>
                                                    <div class="input-group">
                                                        <input type="number" id="buyNumber" name="buyNumber" class="form-control form-second">
                                                    </div>
                                                </div>
                                                <div id="buyfinish">需要<span id="needCny">0</span> CNY</div>
                                                <div class="form-group">
                                                    <button id="buyBtn" type="button" class="btn btn-primary btn-block ft16">立即买入</button>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-xs-6 sell">
                                            <h3 class="b-title">卖出 ${coinMap.fShortName}</h3>
                                            <div id="sellDefaultForm">
                                                <div class="form-group has-feedback form-subline">
                                                    <label for="buyUnitPrice" class="control-label"><span class="sellDefaultLabel">卖出价</span> (￥)</label>
                                                    <div class="input-group"><input type="hidden" value="0.9900">
                                                        <input type="text" id="sellUnitPrice" name="sellUnitPrice" readonly="readonly" value="${coinMap.fctcSellPrice}" class="form-control form-second">
                                                    </div>
                                                </div>
                                                <div class="form-group has-feedback form-subline">
                                                    <label for="sellNumber" class="control-label">卖出量 (${coinMap.fShortName})</label>
                                                    <div class="input-group">
                                                        <input type="number" id="sellNumber" name="sellNumber" class="form-control form-second">
                                                    </div>
                                                </div>
                                                <div id="sellfinish">可得 <span id="getCny" >0</span> CNY</div>
                                                <div class="form-group">
                                                    <button id="sellBtn" type="button" class="btn btn-second btn-block ft16">立即卖出</button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                           
                            <%
                                String[] status = new String[]{"待派单","待确认","交易完成","申诉中","已取消"};
                                request.setAttribute("status", status);
                            %>
                            <c:if test="${login_user.fIsMerchant eq 0 || empty login_user.fIsMerchant}">
                            <div class="row">
                                <div class="col-xs-6">
                                    <div class="exchangetlist" id="usdtcnybuylist">
                                        <div class="shd">
                                            <span style="width: 35%">商户</span>
                                            <b style="text-align: center;">成交数量</b>
                                            <span class="typeshow" style="width: 15%;">类型</span>
                                            <span class="status5">状态</span>
                                        </div>
                                        <div class="bd">
                                            <div class="tempWrap" style="overflow:hidden; position:relative; height:64px">
                                                <ul style="height: 1248px; position: relative; padding: 0px; margin: 0px;">
                                                   <c:forEach var="item" items="${sellList }">
	                                                   <li class="clone" style="height: 32px;">
	                                                        <span style="width: 35%;"><i class="fa fa-user fa-fw"></i>${item.buyerName}</span>
	                                                        <b style="text-align: center;">${item.fNum } ${item.fShortName }</b><span class="typeshow" style="width: 15%;">卖出</span><span  class="status5">${status[item.fStatus] }</span>
	                                                    </li>
                                                   </c:forEach>
                                                </ul>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-xs-6">
                                    <div class="exchangetlist" id="usdtcnyselllist">
                                        <div class="shd">
                                            <span style="width: 35%">商户</span>
                                            <b style="text-align: center;">成交数量</b>
                                            <span class="typeshow" style="width: 15%;">类型</span>
                                            <span  class="status5">状态</span>
                                        </div>
                                        <div class="bd">
                                            <div class="tempWrap" style="overflow:hidden; position:relative; height:64px">
                                                <ul style="height: 1376px; position: relative; padding: 0px; margin: 0px;">
                                                <c:forEach var="item" items="${buyList}">
                                                    <li class="clone" style="height: 32px;">
                                                        <span style="width: 35%;"><i class="fa fa-user fa-fw"></i>${item.sellerName }</span>
                                                        <b style="text-align: center;">${item.fNum } ${item.fShortName }</b><span class="typeshow" style="width: 15%;">买入</span><span class="status5">${status[item.fStatus] }</span>
                                                    </li>
                                                 </c:forEach>
                                                </ul>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            </c:if>
                        </div>
                        <form id="frmExchange" name="frmExchange" class="hide">
                            <div class="usdt-change">
                                <div class="u-cont">
                                    <div class="u-change-list">
                                        <div class="list">
                                            <h5><i class="icon-usdt-s"></i>兑换QC数量:</h5>
                                            <div class="input-show">
                                                <input type="text" id="amount" name="amount" placeholder="最多QC" class="form-control form-second pull-left inputlong smallfont">
                                                <input type="hidden" value="">
                                            </div>
                                        </div>
                                        <div class="list">
                                            <h5><i class="icon-cnyt-s"></i>可得 CNY:</h5>
                                            <div class="input-show">
                                                <input type="text" readonly="readonly" class="form-control form-second pull-left inputlong smallfont" style="background: rgb(241, 241, 241);">
                                            </div>
                                        </div>
                                        <div class="list-change-cent">
                                            <h5><i class="icon-change-s"></i><span>当前币价：</span><span><b></b> CNY</span></h5>
                                            <input type="hidden" value="">
                                        </div>
                                        <div class="clearfix"></div>
                                        <div class="list-btn line-4">
                                            <button id="btcConfirmBtn" type="button" data-loading-text="Loading..." onclick="doExchange();" class="btn btn-second btn-lg ft18">立即兑换</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="bk-tabList-list-btc">
                        <div class="bk-no-cont">
                            <div class="cont-title"><span>敬请期待</span></div>
                            <img src="" width="100%">
                        </div>
                    </div>
                    <div class="bk-tabList-list-eth">
                        <div class="bk-no-cont">
                            <div class="cont-title"><span>敬请期待</span></div>
                            <img src="" width="100%">
                        </div>
                    </div>
                    <div class="bk-tabList-list-etc">
                        <div class="bk-no-cont">
                            <div class="cont-title"><span>敬请期待</span></div>
                            <img src="" width="100%">
                        </div>
                    </div>
                </div>
                <div class="usdtnote">
                    <div class="notecont">
                        <p>1. 买卖商户均为实地考察认证商户，并提供100万usdt保证金，您每次兑换会冻结商户资产，商户资产不够时，不能接单，可放心兑换；</p>
                        <p>2. 买卖商户均为实名认证商户，并提供保证金，可放心兑换；</p>
                        <p>3. 如需申请成为商户请发邮件到info@RYHcoin.net；</p>
                        <p style="font-weight: bold;">4. 请使用本人绑定的银行卡进行汇款，其他任何方式汇款都会退款。（禁止微信和支付宝）</p>
                        <p>5. 商家处理时间9:00 - 21:00非处理时间的订单会在第二天9:00开始处理，一般接单后24小时内会完成打款。</p>
                        <p>6. 单天最多只能发起10笔卖出订单。</p>
                    </div>
                </div>
                <div class="bk-pageTit" id="exchangeRecord">
                    <h4 class="pull-left"><i class="bk-ico assetRecord"></i>最近兑换记录<a href="${oss_url}/ctc/list.html" style="float:right;">查看更多>></a></h4>
                    <div class="clearfix"></div>
                    <div class="table-responsive ">
                        <table id="billDetail" class="table table-striped table-bordered table-hover">
                            <thead>
                                <tr>
                                    <th width="15%">时间</th>
                                    <th width="20%" style="text-align:left;">类别</th>
                                    <th width="15%" style="text-align:left;">数量(${coinMap.fShortName})</th>
                                    <th width="15%" style="text-align:left;">单价(￥)</th>
                                    <th width="15%" style="text-align:left;">总价(￥)</th>
                                    <th width="10%">状态</th>
                                    <th width="10%">操作</th>
                                </tr>
                            </thead>
                            <tbody>
	                            <c:forEach var="item" items="${orderList }">
	                                <tr>
	                                    <td><fmt:formatDate value="${item.fCreateTime }" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
	                                    <td>${item.fType eq 0?"买入":"卖出" }</td>
	                                    <td>${item.fNum }</td>
	                                    <td>${item.fPrice }</td>
	                                    <td>${item.fNum * item.fPrice }</td>
	                                    <td>${status[item.fStatus] }</td>
	                                    <td><a href="${oss_url}/ctc/sjdetails.html?fId=${item.fId }">查看详情</a></td>
	                                </tr>
	                             </c:forEach>
                            </tbody>
                        </table>
                        <input type="hidden" id="pageIndex" value="1">
                        <div class="bk-moreBtn">
                            <button id="morebtn" class="btn btn-outline" type="button" style="display: none;"><i class="fa fa-angle-down fa-fw"></i>更多&gt;&gt;</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div> 
<script src="/static/front/js/c2c/jquery-1.11.3.min.js"></script>
<%@include file="../comm/footer.jsp"%>
<script src="/static/front/js/c2c/jquery.SuperSlide.2.1.1.js"></script>
<script src="/static/front/js/comm/util.js"></script>
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
    	/* $("#buyNumber,#sellNumber").keyup(function(){
    		var value = $(this).val();
    		var req = /^(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*))$/;
    		if(!req.test(value)){
    			$(this).val(value);
    		}
    	}); */
    	
		$("#buyBtn,#sellBtn").click(function() {
			//layer.msg("即将开放，敬请期待!");
			//return;
			
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
</script>
</body>
