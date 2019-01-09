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
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <base href="${basePath}"/>
    <title>发布广告</title>
<!--     <link rel="stylesheet" href="/static/front/css/c2c/safe.css">
    <link rel="stylesheet" href="/static/front/css/c2c/module.common.css">
    <link rel="stylesheet" href="/static/front/css/c2c/module.base.css">
    <link rel="stylesheet" href="/static/front/css/otc/common_otc.css">
    <link rel="stylesheet" href="/static/front/css/otc/jquery.range.css">
    <link rel="stylesheet" href="/static/front/css/otc/publish_advertisement.css"> -->
    <!-- <link href="//netdna.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="//at.alicdn.com/t/font_48446_inzlc0gmji3sor.css" rel="stylesheet"> -->
    <link href="/static/front/app/js/c2c/common.css" rel="stylesheet">
    <link href="/static/front/app/js/c2c/otc-app.css" rel="stylesheet">
    <style>
    .pay_style {
        background-color: white;
        color: #101010;
    }
    .pay_style>div{
        width: 60%;
        text-align: left;
    }
    .pay_style>div:first-child{
        width: 60%;
    }
    </style>
</head>
<body>
<div class="header">
    <a href="/m/otc/index.html?coinType=${typeVal}" class="prev"></a>
    <div class="head_title font15">发布广告</div>
    <!--<div class="more more-z">设置</div>-->
</div>
<div id="mat"></div>
<div class="new-ad">
    <h1 class="new-ad_title font16 fw">发布一个${fShortName}${types_ }交易广告</h1>
    <h3 class="new-ad_hint font13">如果您经常交易${fShortName} ，可以发布自己的 ${fShortName}  交易广告。
                    如果您只想购买或出售一次，我们建议您直接从购买或出售列表中下单交易。<br>发布一则交易广告是免费的。<br>您的 RYH 钱包中至少需要有 0.05${fShortName} ，您的广告才会显示在交易列表中。<!-- <br>发布交易广告的 RYH 用户每笔完成的交易需要缴纳 0.7% 的费用。 --><br>您必须在交易广告或交易聊天中提供您的付款详细信息，发起交易后，价格会锁定，除非定价中有明显的错误。<br>所有交流必须在 RYH 上进行，请注意高风险有欺诈风险的付款方式。<br></h3>
    <form id="newContent" class="new-ad_content form-cont clear-float">
        <input type="hidden" name="crypto_currency" value="BTC">
        <input type="hidden" name="checked_" value="${checked }">
        <input type="hidden" name="checkedBank" value="${checkedBank }">
        <input type="hidden" name="orderType_" value="${orderType_ }">
        <input type="hidden" name="fid_" value="${ad.fId}">
        <input type="hidden" name="fTotal" value="${fTotal}">
        <input type="hidden" name="ffee" value="${ffee}">
        <input type="hidden" name="" value="">
        <div class="input-cont">
            <div class="input-name">
                <span class="require text-red">*</span>
                <span class="font14">选择广告类型: </span>
                <div class="input-name-decs font12 text-gray">您想要创建什么样的交易广告？如果您希望出售 <c:if test="${typeVal == '6' }">USDT</c:if><c:if test="${typeVal == '7' }">AT</c:if> ，请确保您在 RYH 的钱包中有 <c:if test="${typeVal == '6' }">USDT</c:if><c:if test="${typeVal == '7' }">AT</c:if>。</div>
            </div>
            <div>
                <input type="radio" id="ONLINE_SELL" name="type_" value="0" checked="checked">
                <label for="ONLINE_SELL" class="font14">在线出售 </label>
                <input type="radio" id="ONLINE_BUY" name="type_" value="1">
                <label for="ONLINE_BUY" class="font14">在线购买 </label>
            </div>
        </div>
        <div class="input-cont">
            <div class="input-name">
                <span class="require text-red">*</span>
                <span class="font14">所在地: </span>
                <div class="input-name-decs font12 text-gray">请选择你要发布广告的国家。</div>
                <input type="text" name="adr_" value="CN 中国" readonly="readonly" class="ipt_style">
            </div>
        </div>
        <!--<div class="moreMessage_hint border_b">更多信息</div>-->
        <div class="input-cont ">
            <div class="input-name">
                <span class="require text-red">*</span>
                <span class="font14">货币: </span>
                <div class="input-name-decs font12 text-gray">您希望交易付款的货币类型。</div>
                <input type="text" name="Btypes_" value="CNY 人民币" readonly="readonly" class="ipt_style">
                <input type="hidden" name="Btype_" value="${typeVal}" readonly="readonly" class="input">
            </div>
        </div>
        <div class="input-cont">
            <div class="input-name">
                <span class="require text-red">*</span>
                <span class="font14">价格:</span>
                <div class="input-name-decs font12 text-gray">基于溢价比例得出的报价，10分钟更新一次。</div>
                <div class="unit_box">
                    <input name="price_" type="number" value="${ad.fUnitprice}" class="ipt_style">
                    <span class="unit">CNY</span>
                </div>
            </div>
        </div>
        <div class="input-cont">
            <div class="input-name">
                <span class="font14">最低价（选填）:</span>
                <div class="input-name-decs font12 text-gray">广告最低可成交的价格，可帮助您在价格剧烈波动时保持稳定的盈利，比如最低价为12000，市场价处于12000以下时，您的广告将依旧以12000的价格展示出来</div>
                <div class="unit_box">
                    <input class="ipt_style" name="fLowprice_" type="number" value="${ad.fLowprice}">
                    <span class="unit">CNY</span>
                </div>
            </div>
        </div>
        <div class="input-cont">
            <div class="input-name">
                <span class="require text-red">*</span>
                <span class="font14">最小限额:</span>
                <div class="input-name-decs font12 text-gray">一次交易的最低的交易限制。</div>
                <div class="unit_box">
                    <input class="ipt_style" type="number" name="fSmallprice_" value="${ad.fSmallprice}" placeholder="请输入最小限额">
                    <span class="unit">CNY</span>
                </div>
            </div>
        </div>
        <div class="input-cont">
            <div class="input-name">
                <span class="require text-red">*</span>
                <span class="font14">最大限额:</span>
                <div class="input-name-decs font12 text-gray">一次交易中的最大交易限制，您的钱包余额也会影响最大量的设置。</div>
                <div class="unit_box">
                    <input class="ipt_style" type="number" name="fBigprice_" value="${ad.fBigprice}" placeholder="请输入最大限额">
                    <span class="unit">CNY</span>
                </div>
            </div>
            <div class="error-msg">最大交易额不可小于最小交易额</div>
        </div>
        <div class="input-cont ">
            <div class="input-name">
                <span class="require text-red">*</span>
                <span class="font14">收款方式:</span>
                <div class="pay_style flexLayout mt_10 mb_10">
                    <!-- <div>
                        <input type="radio" checked="" value="bankTransfer" id="bankTransfer" name="trade_type">
                        <label for="bankTransfer" class="font14">
                            <img src="img/bank.png" alt="">
                            <span>银行转账</span>
                        </label>
                    </div> -->
                    <c:if test="${empty binding}">
                        <div>请完善收款信息</div>
                    </c:if>
                    <c:if test="${not empty binding}">
                        <c:forEach var="item" items="${binding}">
                               <div>
                                  <input type="radio" value="${item.index}" id="Alipay" name="fReceipttype_">
                                  <label for="Alipay" class="font14">
                                    <c:if test="${item.index == 0 }">
                                       <img src="/static/front/images/c2c/bank.png" alt="">
                                    </c:if>
                                    <c:if test="${item.index == 1}">
                                       <img src="/static/front/images/c2c/Alipay.png" alt="">
                                    </c:if>
                                    <c:if test="${item.index == 2}">
                                       <img src="/static/front/images/c2c/WeChat.png" alt="">
                                    </c:if>
                                    <span>${item.name }</span>
                                  </label>
                               </div>
                        </c:forEach>
                    </c:if>
                 <%-- <c:if test="${not empty binding}">
                   <div>
                        <input type="radio" value="0" id="bankTransfer" name="fReceipttype_">
                        <label for="bankTransfer" class="font14">
                           <img src="/static/front/images/c2c/bank.png" alt="">
                           <span>银行卡</span>
                        </label>
                   </div>
                  <div>
                        <input type="radio" value="1" id="Alipay" name="fReceipttype_">
                        <label for="Alipay" class="font14">
                            <img src="/static/front/images/c2c/Alipay.png" alt="">
                            <span>支付宝</span>
                        </label>
                    </div>
                    <div>
                        <input type="radio" value="2" id="WeChat" name="fReceipttype_">
                        <label for="WeChat" class="font14">
                            <img src="/static/front/images/c2c/WeChat.png" alt="">
                            <span>微信</span>
                        </label>
                    </div>
                 </c:if> --%>
                </div>
            </div>
        </div>
        <div class="input-cont">
            <div class="input-name">
                <div class="font14 mb_10">广告留言:</div>
                <c:if test="${empty ad.fMsg}">
                <textarea class="input textarea border font13" id="msg_" placeholder="请说明有关您交易的相关要求，以便买家下单前先行查看，例如：
1.请在有效期内付款并点击「标记已付款完成」按钮，我才会释放数字资产给您；
2. 如果您无法在有效期付款，请及时取消订单；
3. 下单后，数字资产将托管锁定，请放心交易。"></textarea>
                </c:if>
                <c:if test="${not empty ad.fMsg}">
                <textarea class="input textarea border font13" id="msg_">${ad.fMsg}</textarea>
                </c:if>
                
            </div>
            <div class="error-msg">请输入少于4096字</div>
        </div>
        <div class="input-cont">
            <span class="font14">仅限实名认证的交易者:</span>
            <div class="input-name-decs font12 text-gray">启用后，仅限于已完成实名认证的用户与本广告交易。</div>
            <div>
                <input type="radio" name="isCertified_" value="0" checked="checked" id="safe_opt_true">
                <label for="safe_opt_true"  class="font14">启用</label>
                <input type="radio" name="isCertified_" value="1" id="safe_opt_false">
                <label for="safe_opt_false" class="font14">不启用</label>
            </div>
        </div>
        <div class="btn_box input-cont mt_20">
            <button type="button" id="submit" class="btn_style_1 border font15" onclick="publishad()">立即发布</button>
        </div>
    </form>
</div>
<%-- <div class="bk-onekey financen ">
    <div class="container">
        <div class="finance-rd">
            <div class="bk-tabList">
                <div class="bk-c2c-nav bk-band clearfix">
                    <a href="javascript: void(0);" class="active">发布广告</a>
                    <c:forEach var="coin" items="${cointTypeList }">
                    </c:forEach>
                    <c:if test="${login_user.fIsMerchant ne 0 && !empty login_user.fIsMerchant}">
                        <a class="btn card-add" role="button" href="/ctc/merchantCenter.html?fId=${login_user.fid }"><i class="iconfont icon-tianjialeimu"></i>商户订单</a>
                    </c:if>
                    <a class="btn card-admin" role="button" href="/otc/publishAd.html" ><i class="iconfont icon-cc-card-o"></i>刊登广告</a>
                    <!-- <a class="introabtn " href="/about/index.html?id=93" target="_blank"><i class="fa fa-cube fa-fw"></i> 操作说明</a> -->
                    <a class="btn card-admin" role="button" href="/m/otc/index.html" style="color: #666" ><img alt="" src="/static/front/images/aui-icon-back.png"> 返回</a>
                </div>
                <div class="container new-ad">
                    <h1 class="h1">发布一个<c:if test="${typeVal == '6' }">USDT</c:if><c:if test="${typeVal == '7' }">AT</c:if> ${types_ }交易广告</h1>
                    <h3 class="h3">如果您经常交易<c:if test="${typeVal == '6' }">USDT</c:if><c:if test="${typeVal == '7' }">AT</c:if> ，可以发布自己的 <c:if test="${typeVal == '6' }">USDT</c:if><c:if test="${typeVal == '7' }">AT</c:if>  交易广告。
                    如果您只想购买或出售一次，我们建议您直接从购买或出售列表中下单交易。<br>发布一则交易广告是免费的。<br>您的 RYH 钱包中至少需要有 0.05<c:if test="${typeVal == '6' }">USDT</c:if><c:if test="${typeVal == '7' }">AT</c:if> ，您的广告才会显示在交易列表中。<br>发布交易广告的 RYH 用户每笔完成的交易需要缴纳 0.7% 的费用。<br>您必须在交易广告或交易聊天中提供您的付款详细信息，发起交易后，价格会锁定，除非定价中有明显的错误。<br>所有交流必须在 RYH 上进行，请注意高风险有欺诈风险的付款方式。<br></h3>
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
                            <input class="input" type="number" name="fSmallprice_" value="${ad.fSmallprice}" placeholder="请输入最小限额, 大于50">
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
                            <textarea class="input textarea" name="msg_" value="${ad.fMsg}" placeholder="请说明有关您交易的相关要求，以便买家下单前先行查看，例如：
                1.请在有效期内付款并点击「标记已付款完成」按钮，我才会释放数字资产给您；
                2. 如果您无法在有效期付款，请及时取消订单；
                3. 下单后，数字资产将托管锁定，请放心交易。"></textarea>
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
                        <div class="pt-30"></div>
                        <a id="submit" class="btn submit disable" href="javascript:void(0);" onclick="publishad()">立即发布</a>
                        <div class="pt-30"></div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>  --%>
<script src="/static/front/js/c2c/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="/static/front/app/js/c2c/rem1.js"></script>
<script src="/static/front/js/c2c/jquery.SuperSlide.2.1.1.js"></script>
<script src="/static/front/js/comm/util.js"></script>
<script src="/static/front/js/otc/publishady.js"></script>
<script type="text/javascript" src="/static/front/js/layer/layer.js"></script>
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
