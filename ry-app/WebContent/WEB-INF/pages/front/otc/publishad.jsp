<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat"
    pageEncoding="UTF-8"%>
<%@ include file="../comm/include.inc.jsp"%>
    <title>发布广告</title>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <base href="${basePath}"/>
    <%@include file="../comm/link.inc.jsp"%>
    <link rel="stylesheet" href="/static/front/css/c2c/safe.css">
    <link rel="stylesheet" href="/static/front/css/c2c/module.common.css">
    <link rel="stylesheet" href="/static/front/css/c2c/module.base.css">
    <link rel="stylesheet" href="/static/front/css/otc/common_otc.css">
    <link rel="stylesheet" href="/static/front/css/otc/jquery.range.css">
    <link rel="stylesheet" href="/static/front/css/otc/publish_advertisement.css">
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
        #yijia_{
            position: absolute;
            left: 500px;
            display: inline-block !important;
        }
    </style>
</head>
<%@include file="../comm/header.jsp"%> 
<body style="text-align: left;">
<div class="bk-onekey financen " style="padding-top: 50px;">
    <div class="container">
        <div class="finance-rd" style="width:100%; margin-left:0;">
            <div class="bk-tabList">
                <div class="bk-c2c-nav bk-band clearfix">
                    <a href="javascript: void(0);" class="active">发布广告</a>
                    <c:forEach var="coin" items="${cointTypeList }">
                    </c:forEach>
                    <%-- <c:if test="${login_user.fIsMerchant ne 0 && !empty login_user.fIsMerchant}">
                        <a class="btn card-add" role="button" href="/ctc/merchantCenter.html?fId=${login_user.fid }"><i class="iconfont icon-tianjialeimu"></i>商户订单</a>
                    </c:if>
                    <a class="btn card-admin" role="button" href="/otc/publishAd.html" ><i class="iconfont icon-cc-card-o"></i>刊登广告</a> --%>
                    <!-- <a class="introabtn " href="/about/index.html?id=93" target="_blank"><i class="fa fa-cube fa-fw"></i> 操作说明</a> -->
                    <a class="btn card-admin" role="button" href="/otc/index.html?coinType=${typeVal}" style="color: #666" ><img alt="" src="/static/front/images/aui-icon-back.png"> 返回</a>
                </div>
                <div class="container new-ad">
                    <h1 class="h1">发布一个${fShortName}${types_ }交易广告</h1>
                    <h3 class="h3">如果您经常交易 ${fShortName}，可以发布自己的  ${fShortName} 交易广告。
                    如果您只想购买或出售一次，我们建议您直接从购买或出售列表中下单交易。<br>发布一则交易广告是免费的。<br>您的 RYH 钱包中至少需要有 0.05 ${fShortName} ，您的广告才会显示在交易列表中。<!-- <br>发布交易广告的 coa 用户每笔完成的交易需要缴纳 0.7% 的费用。 --><br>您必须在交易广告或交易聊天中提供您的付款详细信息，发起交易后，价格会锁定，除非定价中有明显的错误。<br>所有交流必须在 RYH 上进行，请注意高风险有欺诈风险的付款方式。<br></h3>
                    <form id="newContent" class="form-cont clear-float">
                        <input type="hidden" name="crypto_currency" value="BTC">
                        <input type="hidden" name="checked_" value="${checked }">
                        <input type="hidden" name="checkedBank" value="${checkedBank }">
                        <input type="hidden" name="orderType_" value="${orderType_ }">
                        <input type="hidden" name="fid_" value="${ad.fId}">
                        <input type="hidden" name="fTotal" value="${fTotal}">
                        <input type="hidden" name="ffee" value="${ffee}">
                        <div class="form-title">
                            <span class="form-name">交易类型 </span>
                            <div class="line"></div>
                        </div>
                        <div class="input-cont">
                            <div class="input-name">
                                <div class="require">*</div>选择广告类型: <span class="input-name-decs">您想要创建什么样的交易广告？如果您希望出售 <c:if test="${typeVal == '6' }">USDT</c:if><c:if test="${typeVal == '7' }">AT</c:if> ，请确保您在 RYH 的钱包中有 <c:if test="${typeVal == '6' }">USDT</c:if><c:if test="${typeVal == '7' }">AT</c:if>。</span></div>
                            <div>
                                <input type="radio" id="ONLINE_SELL" name="type_" value="0" checked="checked"><label for="ONLINE_SELL">在线出售 </label>
                                <input type="radio" id="ONLINE_BUY" name="type_" value="1"><label for="ONLINE_BUY">在线购买 </label>
                            </div>
                        </div>
                        <div class="input-cont  mb-20">
                            <div class="input-name"><div class="require">*</div>所在地: <span class="input-name-decs">请选择你要发布广告的国家。</span></div>
                            <input type="text" name="adr_" value="CN 中国" readonly="readonly" class="input">
                        </div>
                        <div class="form-title">
                            <span class="form-name">更多信息</span>
                            <div class="line"></div>
                        </div>
                        <div class="input-cont ">
                            <div class="input-name"><div class="require">*</div>货币: <span class="input-name-decs">您希望交易付款的货币类型。</span></div>
                            <input type="text" name="Btypes_" value="CNY 人民币" readonly="readonly" class="input">
                            <input type="hidden" name="Btype_" value="${typeVal}" readonly="readonly" class="input">
                        </div>
                        <div class="input-cont x-small" style="display: none;">
                            <div class="input-name"><div class="require">*</div>溢价:<span class="input-name-decs">基于市场价的溢出比例，市场价是根据部分大型交易所实时价格得出的，确保您的报价趋于一个相对合理范围，比如当前价格为7000，溢价比例为10%，那么价格为7700。</span></div>
                            <input class="input" name="yijia_" type="number" value="0" id="yijia_">
                            <div class="input-after">%</div>
                            <div class="input-name-decs" style="margin:0;">市场参考价: <span style="color:#fd634f;"><span id="marketPrice">-</span> <span id="textCurrency">CNY</span>/<c:if test="${typeVal == '6' }">USDT</c:if><c:if test="${typeVal == '7' }">AT</c:if> </span></div>
                        </div>
                        <div class="input-cont ">
                            <div class="input-name"><div class="require">*</div>单价:<span class="input-name-decs">基于溢价比例得出的报价，10分钟更新一次。</span></div>
                            <input class="input" name="price_" type="number" value="${ad.fUnitprice}">
                            <div class="input-after input-currency">CNY</div>
                            <!-- <div class="input-name-decs" style="margin:0;margin-top:10px;">当前报价大概排在第 <span id="rank" style="color:#fd634f;">-</span> 位</div> -->
                        </div>
                        <div class="input-cont">
                            <div class="input-name"><div class="require">*</div>最低价:<span class="input-name-decs">广告最低可成交的价格，可帮助您在价格剧烈波动时保持稳定的盈利，比如最低价为12000，市场价处于12000以下时，您的广告将依旧以12000的价格展示出来</span></div>
                            <input class="input" name="fLowprice_" type="number" value="${ad.fLowprice}">
                            <div class="input-after input-currency">CNY</div>
                        </div>
                        <div class="input-cont" style="display: none;">
                            <div class="input-name">最高价（选填）:<span class="input-name-decs">广告最高可成交的价格，可帮助您在价格剧烈波动时保持稳定的盈利，比如最高价为12000，市场价处于12000以上时，您的广告将依旧以12000的价格展示出来</span></div>
                            <input class="input" name="max_price" type="number" value="${ad.fBigprice}">
                            <div class="input-after input-currency">CNY</div>
                        </div>
                        <div class="input-cont ">
                            <div class="input-name"><span class="require">*</span>最小限额:<span class="input-name-decs">一次交易的最低的交易限制。</span></div>
                            <input class="input" type="number" name="fSmallprice_" value="${ad.fSmallprice}" placeholder="请输入最小限额">
                            <div class="input-after input-currency">CNY</div>
                            <div class="form-tips"></div>
                            <div class="error-msg">最小交易额不可大于最大交易额，且大于或等于50</div>
                        </div>
                        <div class="input-cont ">
                            <div class="input-name"><span class="require">*</span>最大限额:<span class="input-name-decs">一次交易中的最大交易限制，您的钱包余额也会影响最大量的设置。</span></div>
                            <input class="input" type="number" name="fBigprice_" value="${ad.fBigprice}" placeholder="请输入最大限额">
                            <div class="input-after input-currency" >CNY</div>
                            <div class="form-tips"></div>
                            <div class="error-msg">最大交易额不可小于最小交易额</div>
                        </div>
                        <div class="input-cont input-payment_window_minutes" style="display: none">
                            <div class="input-name"><span class="require">*</span>付款期限:<span class="input-name-decs">您承诺在此期限内付款（分钟）</span></div>
                            <input class="input" type="number" name="payment_window_minutes" placeholder="付款期限">
                            <div class="form-tips"></div>
                            <div class="error-msg">付款期限设置范围: 5-45分钟</div>
                        </div>
                        <div class="input-cont ">
                            <div class="input-name"><div class="require">*</div>收款方式: </div>
                            <select class="select" name="fReceipttype_" >
                            <c:if test="${empty binding }">
                            	<option>请完善收款信息</option>
                            </c:if>
                            <c:if test="${not empty binding }">
	                            <c:forEach var="item" items="${binding }">
	                                <option value="${item.index }">${item.name }</option>
	                            </c:forEach>
                            </c:if>
                                <!-- <option value="0">银行转账</option>
                                <option value="1">支付宝</option>
                                <option value="2">微信</option> -->
                            </select>
                        </div>
                        <div class="input-cont">
                            <div class="input-name"><!-- <div class="require">*</div> -->广告留言: <span class="input-name-decs"></span></div>
                            <textarea class="input textarea" id="msg_" name="msg_"  placeholder="请说明有关您交易的相关要求，以便买家下单前先行查看，例如：
                1.请在有效期内付款并点击「标记已付款完成」按钮，我才会释放数字资产给您；
                2. 如果您无法在有效期付款，请及时取消订单；
                3. 下单后，数字资产将托管锁定，请放心交易。">${ad.fMsg}</textarea>
                            <div class="form-tips"></div>
                            <div class="error-msg">请输入少于4096字</div>
                        </div>
                
                        <div class="input-cont">
                            <div class="input-name">仅限实名认证的交易者：<span class="input-name-decs">启用后，仅限于已完成实名认证的用户与本广告交易。</span></div>
                            <div>
                                <input type="radio" name="isCertified_" value="0" checked="checked" id="safe_opt_true" ><label for="safe_opt_true">启用</label>
                                <input type="radio" name="isCertified_" value="1" id="safe_opt_false"><label for="safe_opt_false">不启用</label>
                            </div>
                        </div>
                        <!-- <div class="input-cont mb-40">
                            <div class="input-name">仅限受信任的交易者：<span class="input-name-decs">启用后，仅限于自己信任的用户与本广告交易。</span></div>
                            <div>
                                <input type="checkbox" name="require_trusted_by_advertiser">
                            </div>
                        </div> -->
                
                        <!-- <div class="high-opt hidden">
                            <div class="form-title">
                                <span class="form-name">高级设置</span>
                                <div class="line"></div>
                            </div>
                            <div class="input-name">开放时间：<span class="input-name-decs">您希望广告自动显示和隐藏的天数和小时数。</span></div>
                            <div class="input-cont  clear-float select2">
                                <div class="input-name">星期一: </div>
                                <select class="select select2" name="opening_hours_1_0">
                                    <option value="0">开始时间</option>
                                    <option value="-1">关闭</option>
                                </select>
                                <select class="select select2" name="opening_hours_1_1">
                                    <option value="96">结束时间</option>
                                    <option style="display:none" value="-1">关闭</option>
                                </select>
                            </div>
                            <div class="input-cont  clear-float select2">
                                <div class="input-name">星期二: </div>
                                <select class="select select2" name="opening_hours_2_0">
                                    <option value="0">开始时间</option>
                                    <option value="-1">关闭</option>
                                </select>
                                <select class="select select2" name="opening_hours_2_1">
                                    <option value="96">结束时间</option>
                                    <option style="display:none" value="-1">关闭</option>
                                </select>
                            </div>
                            <div class="input-cont  clear-float select2">
                                <div class="input-name">星期三: </div>
                                <select class="select select2" name="opening_hours_3_0">
                                    <option value="0">开始时间</option>
                                    <option value="-1">关闭</option>
                                </select>
                                <select class="select select2" name="opening_hours_3_1">
                                    <option value="96">结束时间</option>
                                    <option style="display:none" value="-1">关闭</option>
                                </select>
                            </div>
                            <div class="input-cont  clear-float select2">
                                <div class="input-name">星期四: </div>
                                <select class="select select2" name="opening_hours_4_0">
                                    <option value="0">开始时间</option>
                                    <option value="-1">关闭</option>
                                </select>
                                <select class="select select2" name="opening_hours_4_1">
                                    <option value="96">结束时间</option>
                                    <option style="display:none" value="-1">关闭</option>
                                </select>
                            </div>
                            <div class="input-cont  clear-float select2">
                                <div class="input-name">星期五: </div>
                                <select class="select select2" name="opening_hours_5_0">
                                    <option value="0">开始时间</option>
                                    <option value="-1">关闭</option>
                                </select>
                                <select class="select select2" name="opening_hours_5_1">
                                    <option value="96">结束时间</option>
                                    <option style="display:none" value="-1">关闭</option>
                                </select>
                            </div>
                            <div class="input-cont  clear-float select2">
                                <div class="input-name">星期六: </div>
                                <select class="select select2" name="opening_hours_6_0">
                                    <option value="0">开始时间</option>
                                    <option value="-1">关闭</option>
                                </select>
                                <select class="select select2" name="opening_hours_6_1">
                                    <option value="96">结束时间</option>
                                    <option style="display:none" value="-1">关闭</option>
                                </select>
                            </div>
                            <div class="input-cont  clear-float select2">
                                <div class="input-name">星期日: </div>
                                <select class="select select2" name="opening_hours_0_0">
                                    <option value="0">开始时间</option>
                                    <option value="-1">关闭</option>
                                </select>
                                <select class="select select2 mb-40" name="opening_hours_0_1">
                                    <option value="96">结束时间</option>
                                    <option style="display:none" value="-1">关闭</option>
                                </select>
                            </div>
                        </div> -->
                        <!-- <div class="line"></div>
                        <a href="javascript:;" class="link high-opt-display" id="highOpt">显示高级设置...</a> -->
                        <div class="pt-30"></div>
                        <a id="submit" class="btn submit disable" href="javascript:void(0);" onclick="publishad()">立即发布</a>
                        <div class="pt-30"></div>
                    </form>
                </div>
                <!-- <div class="bk-pageTit" id="exchangeRecord">
                    <input type="radio" name="type_" value="0" checked="checked">在线购买|
                    <input type="radio" name="type_" value="1">在线出售<br/>
                    所在地：<input type="text" name="adr_" value="CN" readonly="readonly"><br/>
                    货币：<input type="text" name="Btype_" value="CNY 人名币" readonly="readonly"><br/>
                    溢价：<input type="text" name="yijia_">%<br/>
                    价格：<input type="number" name="price_">CNY<br/>
                    最低价：（选填）<input type="number" name="fLowprice_">CNY<br/>
                    最小限额：<input type="number" name="fSmallprice_">CNY<br/>
                    最大限额：<input type="number" name="fBigprice_">CNY<br/>
                    收款方式：<select name="fReceipttype_">
                            <option value="0">银行转账</option>
                            <option value="1">支付宝</option>
                            <option value="2">微信</option>
                            </select><br/>
                    最大限额：<input type="text" name="msg_"><br/>
                    是否开放实名认证用户购买：<input type="radio" name="isCertified_" value="0" checked="checked">是|
                    <input type="radio" name="isCertified" value="1">否<br/>
                    
                    <input type="button" value="发布广告" onclick="publishad()">
                </div> -->
            </div>
        </div>
    </div>
</div> 
<script src="/static/front/js/c2c/jquery-1.11.3.min.js"></script>
<%@include file="../comm/footer.jsp"%>
<script src="/static/front/js/c2c/jquery.SuperSlide.2.1.1.js"></script>
<script src="/static/front/js/otc/publishad.js"></script>
<script src="/static/front/js/comm/util.js"></script>
<script src="/static/front/js/otc/jquery.range.js"></script>
<script>
/* 左右滑动滚动条 */
    $('#yijia_').jRange({
            from: -100,
            to: 100,
            step: 0.01,
            scale: [-100,-75,-50,-25,0,25,50,75,100],
            format: '%s',
            width: 450,
            showLabels: true,
            showScale: true
        });
    $(function(){
        $(".clickable-dummy").prev().hide();
    })

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
                                    modal(data.data.realName , data.data.fName , data.data.fBankNumber , data.data.fCnyTotal, result.orderFid , data.data.statusName);
                                }
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
