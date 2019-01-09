<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../comm/include.inc.jsp"%>
<%
    String path = request.getContextPath();
            String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                    + path;
%>

<!doctype html>
<html>
<head>
<jsp:include page="../comm/link.inc.jsp"></jsp:include>
<link href="${oss_url}/static/front/css/trade/trade.css"
    rel="stylesheet" type="text/css" media="screen, projection" />
<script type="text/javascript"
    src="/static/front/app/js/jquery-3.2.1.min.js"></script>
<script type="text/javascript"
    src="/static/front/app/js/swiper-3.4.2.jquery.min.js"></script>
<style>
body {
    background: #f8f8f8;
}

.trade {
    margin-top: 0px !important;
}

.trade * {
    box-sizing: border-box;
    margin-top: 0px !important;
    margin-bottom: 20px !important;
}

.trade .trade-body {
    height: auto;
    overflow: hidden;
    float: left;
    width: 64.66%;
    padding-right: 10px;
}

.trade table {
    width: 100%;
}

.trade table thead {
    background-color: #000000 !important;
}

.trade table thead tr th {
    background-color: black !important;
    color: white !important;
    text-align: center;
}

.trade table tr {
    border-bottom: none;
}

.trade td {
    text-align: center !important;
}

.trade .trade-body .trade-area {
    width: 100%;
    height: auto;
    float: left;
    padding: 25px 15px;
    background: #f6f9fc;
}

.trade .trade-body .trade-buysell {
    width: 47.5%;
    float: left;
}

.trade .trade-body .trade-disk {
    width: 5%;
    float: left;
    height: 10px;
}

.trade-buysell a {
    line-height: 31px;
}

.trade .trade-body .trade-buysell label {
    font-weight: normal;
}

.trade .trade-body .trade-group {
    margin-bottom: 10px;
    position: relative;
}

.trade .trade-body .trade-inputtips {
    position: absolute;
    left: 0;
    top: 0;
    padding-left: 30px;
    line-height: 45px;
    color: #b7b7b7;
    cursor: auto;
    font-size: 12px;
    margin-bottom: 0;
}

.trade .trade-body .trade-input {
    color: #333;
    border-radius: 0;
    text-align: right;
    padding-left: 100px;
    display: block;
    width: 100%;
    height: 45px;
    line-height: 45px;
    padding-right: 20px;
    outline: none;
    transition: all 0.3s;
    border: 1px solid #edf3f6;
    background: #fff;
}

.trade .trade-body .trade-input:hover, .trade .trade-body .trade-input:focus
    {
    box-shadow: 0 0 2px 0 rgba(98, 135, 175, 1);
}

.trade .trade-body .trade-error {
    height: 16px;
    display: block;
    position: relative;
    top: -8px;
}

.trade .trade-body .trade-btn {
    height: 35px;
    line-height: 35px;
    padding: 0;
}

.redtips {
    color: #c83935 !important;
}

.greentips {
    color: #009900 !important;
}

/*-------------------------------*/
.trade-amount .datatime-sel, .trade-amount .datatime:hover {
    background: #41c7f9 none repeat scroll 0 0;
    color: #fff;
}

.trade-amount .databtn {
    cursor: pointer;
    display: inline-block;
    margin: 0 10px;
    padding: 5px;
    height: 55px;
    width: 40px;
}

.panel-body table tr:first-child {
    border-bottom: 0px solid #dddddd;
}

.drop-down-menu {
    display: none;
    position: absolute;
    float: right;
    top: 46px;
    right: 11px;
    left: auto;
    margin: 0;
    padding: 5px;
    min-width: 84px;
    line-height: 14px;
    background-color: #fff;
    border: 1px solid #ccc;
    border: 1px solid rgba(0, 0, 0, 0.2);
    z-index: 1021
}

.drop-down-menu:before {
    display: inline-block;
    position: absolute;
    top: -7px;
    right: 12px;
    left: auto;
    border-left: 7px solid transparent;
    border-right: 7px solid transparent;
    border-bottom: 7px solid #ccc;
    border-bottom-color: rgba(0, 0, 0, 0.2);
    content: ''
}

.drop-down-menu:after {
    display: inline-block;
    position: absolute;
    top: -6px;
    left: auto;
    right: 13px;
    border-left: 6px solid transparent;
    border-right: 6px solid transparent;
    border-bottom: 6px solid white;
    content: ''
}

.drop-down-menu .drop-down-menu-item {
    display: inline-block;
    line-height: 20px;
    padding: 5px;
    white-space: nowrap;
    width: 100%;
    color: black;
    font-weight: normal;
    text-align: center;
    text-decoration: none
}

.drop-down-menu .drop-down-menu-item:hover {
    background-color: #47C5D5;
    color: white
}

.drop-down-menu .drop-down-menu-item .drop-down-menu-item-description {
    font-style: italic;
    color: #ccc;
    font-size: 12px
}

#b-menu {
    position: relative;
}

#b-menu:hover .drop-down-menu {
    display: inline-block
}

.col-xs-12 {
    padding-left: 0px !important;
}

.text-color1 {
    color: #e85600 !important;
}

.trade .trade-body .trade-bar .marker-top {
    background: #e85600 !important;
}

.trade .trade-body .trade-bar .marker .circle {
    color: #e85600 !important;
}

.btn-danger {
    background-color: #e85600 !important;
    border: none !important;
}

.btn-danger:hover {
    border: none !important;
}

.text-color2 {
    color: #029500 !important;
}

.trade .trade-bar .sell-marker .marker-top {
    background: #029500 !important;
}

.trade .trade-body .trade-bar .sell-marker .circle {
    color: #029500 !important;
}

.btn-success {
    background-color: #029500 !important;
    border: none !important;
}

.btn-success:hover {
    border: none !important;
}

.top {
    background: #f8f8f8;
}

.leftmenu {
    background: #f6f9fc;
}

.leftmenu .iconfont{
    padding-right: 0;
}

.trade .trade-buysell {
    background: #f6f9fc;
}

.row-con {
    background: #fff;
    padding: 15px;
}

.sellbox article {
    display: flex;
    justify-content: space-between;
    margin-bottom: 0 !important;
    line-height: 20px;
}

.sellbox article span {
    width: 33.33%;
    text-align: right;
    padding: 5px;
    margin-bottom: 0 !important;
}

.centre-cont {
    border-bottom: 1px solid #e6e6e6;
}

#marketDepthBuy article .market-font-sell, #marketDepthBuy article .depth-price
    {
    color: #da2e22;
}

#marketDepthSell article .market-font-sell, #marketDepthSell article .depth-price
    {
    color: #009900;
}

.trade .trade-body .trade-buysell {
    width: 100% !important;
    float: none !important;
    padding: 10px 0 0 10px;
    box-sizing: border-box;
    -webkit-box-sizing: border-box;
    -ms-box-sizing: border-box;
    -moz-box-sizing: border-box;
    -o-box-sizing: border-box;
    margin-bottom: 0 !important;
}

.trade-group {
    float: left;
    width: 48%;
    margin-right: 10px;
}

.trade .trade-body .trade-bar {
    height: 45px !important;
    padding: 10px 30px 0 30px !important;
}

.right_add a {
    float: left;
    margin-top: 16px !important;
}

.centre {
    padding-left: 10px;
    font-size: 16px;
}

.centre span {
    display: inline-block;
    margin-left: 10px;
}

#logbox, #sellbox, #buybox {
    box-sizing: border-box !important;
    padding: 0px 10px !important;
    margin: 0px !important;
}

#logbox li, #sellbox li, #buybox li {
    height: 28px !important;
    line-height: 28px !important;
    margin: 0px !important;
}

#logbox li *, #sellbox li *, #buybox li * {
    margin: 0px !important;
}

.trade * {
    box-sizing: border-box;
    margin-top: 0px !important;
    margin-bottom: 10px !important;
}

.trade .trade-buysell a {
    line-height: 50px;
}
.trade-total{
    width: 100%;
    height: auto;
}
.trade-total span{
    display:inline-block;
    width:32%;
    height:auto;
    text-align: left;
}
.transaction {
    width: 18px;
    height: 12px;
    display: inline-block;
    vertical-align: middle;
    margin-top: 6px !important;
    font-size: 0px;
}
.hangqing{
    margin-left: 3px;
    height: 15px;
    display: inline-block;
}
.container{
    min-width: 1150px;
}
/* .panel-collapse{
    height:0;
    overflow: hidden;
}
#t1{
    height:auto;
} */
</style>
</head>
<body>


    <jsp:include page="../comm/header.jsp"></jsp:include>

    <div class="container-full main-con">
        <div class="container">
            <div class="row">
                <div class="col-xs-12 padding-right-clear"
                    style="padding-left: 0px !important;">
                    <div class="col-xs-2 padding-left-clear leftmenu panel-group" id="accordion">
                            <c:forEach var="v" varStatus="vs" items="${ftrademappings }">
                        <div class="panel panel-default">
                                <div class="panel-heading" data-toggle="collapse" data-parent="accordion"
                                    href="#t${v.fid }">
                                    <a class="panel-title"> <i class="lefticon"
                                        style="background: url(${v.fvirtualcointypeByFvirtualcointype2.furl }) no-repeat 0 0;background-size:100% 100%;"></i>
                                        ${v.fvirtualcointypeByFvirtualcointype2.fShortName }/${coin1.fShortName}交易
                                    </a> <a class="pull-right"> <i class="iconfont">&#xe65a;</i>
                                    </a>
                                </div>
                                <div id="t${v.fid }" class="panel-collapse collapse ${coinType==v.fid?'in':'' }" >
                                    <div class="panel-body nav nav-pills nav-stacked">
                                        <li class="${coinType==v.fid?'active':'' }"><a
                                            href="/trade/coin.html?coinType=${v.fid }&tradeType=0"><i
                                                class="iconfont">&#xe624;</i>买入/卖出</a></li>
                                        <li><a
                                            href="/trade/entrust0.html?coinType=${v.fid }&symbol=${v.fid }&status=0"><i
                                                class="iconfont">&#xe64d;</i> 委托管理</a></li>
                                        <li><a
                                            href="/trade/entrust0.html?coinType=${v.fid }&symbol=${v.fid }&status=1"><i
                                                class="iconfont">&#xe622;</i>交易记录</a></li>
                                    </div>
                                </div>
                        </div>
                            </c:forEach>
                    </div>

                    <div class="row-con col-xs-10">
                        <div class="alert alert-danger">
                            <!-- <a href="#" class="close" data-dismiss="alert">×</a> --> <strong>交易提示</strong>:${coin2.fShortName }/${coin1.fShortName }<br>
                        </div>

                        <div class="trade" style="overflow: hidden;">
                            <div class="trade-body ">
                                <div class="trade-area">
                                    <div class="trade-buysell clearfix">
                                        <div class="trade-group"
                                            style="float: none; padding-left: 30px;">
                                            <span> 可用 </span> <span id="toptradecnymoney"
                                                class="text-color1">0</span> <span class="text-color1">${coin1.fShortName }</span>
                                        </div>
                                        <div class="trade-group">
                                            <label for="tradebuyprice" class="trade-inputtips">
                                                买入价${coin2.fShortName }/${coin1.fShortName }</label> <input
                                                id="tradebuyprice" class="trade-input"
                                                value="${recommendPrizebuy }" onblur="maxBuy();"
                                                oninput="maxBuy();" />
                                            <%-- <span>最大可买${coin2.fShortName }：<font class="text-color1" id="maxBuy">0.0000</font></span> --%>
                                        </div>

                                        <div class="trade-group">
                                            <label for="tradebuyamount" class="trade-inputtips">
                                                买入量${coin2.fShortName }</label> <input id="tradebuyamount"
                                                class="trade-input">
                                        </div>
                                        <div class="trade-group">
                                            <div id="buyBar" class="trade-bar">
                                                <div class="trade-barbox">
                                                    <div id="buyslider" class="slider" data-role="slider"
                                                        data-param-marker="marker" data-param-complete="complete"
                                                        data-param-type="0" data-param-markertop="marker-top">
                                                        <div class="complete" style="width: 2px;"></div>
                                                    </div>
                                                    <div class="slider-points">
                                                        <div class="proportioncircle proportion0" data-points="0">0%</div>
                                                        <div class="proportioncircle proportion1" data-points="20">20%</div>
                                                        <div class="proportioncircle proportion2" data-points="40">40%</div>
                                                        <div class="proportioncircle proportion3" data-points="60">60%</div>
                                                        <div class="proportioncircle proportion4" data-points="80">80%</div>
                                                        <div class="proportioncircle proportion5"
                                                            data-points="100">100%</div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="trade-group">
                                            <span class="trade-input"> <label
                                                class="trade-inputtips"> 交易额 </label> <span
                                                id="tradebuyTurnover">0</span> ${coin1.fShortName } <%-- <br/>买入手续费${ftrademapping.ftradedesc } --%>
                                            </span>

                                        </div>
                                        <div class="centre exchange">
                                            ≈<span id="buyCny" class="ml_10">0</span><span class="unit">CNY</span>
                                        </div>
                                        <div class="trade-group">
                                            <span id="buy-errortips" class="text-color1 trade-error"></span>
                                            <button id="buybtn"
                                                class="btn btn-danger btn-block trade-btn">
                                                买入${coin2.fShortName }</button>
                                        </div>
                                        <div class="trade-group right_add">
                                        <c:if test="${fisrecharge eq 1}">
                                            <c:choose>
                                                <c:when test="${coin1.ftype==0 }">
                                                    <a href="${oss_url}/account/rechargeCny.html" class="text-color1">立即充值<i
                                                        class="arrow-icon-small text-color1"></i></a>
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="${oss_url}/account/rechargeBtc.html?symbol=${coin1.fid }"
                                                        class="text-color1">立即充值<i
                                                        class="arrow-icon-small text-color1"></i></a>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:if>
                                        <c:if test="${fisrecharge eq 0}">
											<a class="NoSub_" class="text-color1">立即充值
											<i class="arrow-icon-small text-color1"></i></a>
                                        </c:if>
                                        </div>
                                    </div>
                                    <div class="trade-disk"></div>
                                    <div class="trade-buysell">
                                        <div class="trade-group" style="float: none;">
                                            <span> 可用 </span> <span id="toptrademtccoin"
                                                class="text-color2">0</span> <span class="text-color2">${coin2.fShortName }</span>
                                        </div>
                                        <div class="trade-group">
                                            <label for="tradesellprice" class="trade-inputtips">
                                                卖出价${coin2.fShortName }/${coin1.fShortName }</label> <input
                                                id="tradesellprice" class="trade-input"
                                                value="${recommendPrizesell }" oninput="maxSell();"
                                                onblur="maxSell();">
                                            <%--  <span>最大可卖${coin1.fShortName }：<font class="text-color1" id="maxSell">0.0000</font></span> --%>
                                        </div>
                                        <div class="trade-group">
                                            <label for="tradesellamount" class="trade-inputtips">
                                                卖出量${coin2.fShortName }</label> <input id="tradesellamount"
                                                class="trade-input">
                                        </div>
                                        <div class="trade-group">
                                            <div id="buyBar" class="trade-bar">
                                                <div class="trade-barbox">
                                                    <div id="sellslider" class="slider" data-role="slider"
                                                        data-param-marker="marker sell-marker"
                                                        data-param-complete="complete" data-param-type="1"
                                                        data-param-markertop="marker-top">
                                                        <div class="complete" style="width: 2px;"></div>
                                                    </div>
                                                    <div class="slider-points">
                                                        <div class="proportioncircle proportion0" data-points="0">0%</div>
                                                        <div class="proportioncircle proportion1" data-points="20">20%</div>
                                                        <div class="proportioncircle proportion2" data-points="40">40%</div>
                                                        <div class="proportioncircle proportion3" data-points="60">60%</div>
                                                        <div class="proportioncircle proportion4" data-points="80">80%</div>
                                                        <div class="proportioncircle proportion5"
                                                            data-points="100">100%</div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="trade-group">
                                            <div class="trade-input">
                                                <label class="trade-inputtips"> 交易额 </label> <span
                                                    id="tradesellTurnover">0</span> ${coin1.fShortName }
                                                <%-- <br/>卖出手续费${ftrademapping.ftradedesc } --%>
                                            </div>
                                        </div>
                                        <div class="centre exchange">
                                            ≈<span id="buyCny1" class="ml_10">0</span><span class="unit">CNY</span>
                                        </div>
                                        <div class="trade-group">
                                            <span id="sell-errortips" class="text-color1 trade-error"></span>
                                            <button id="sellbtn"
                                                class="btn btn-success btn-block trade-btn">
                                                卖出${coin2.fShortName }</button>
                                        </div>
                                        <div class="trade-group right_add">
	                                        <c:if test="${fisrecharge1 eq 1}">
	                                            <a href="${oss_url}/account/rechargeBtc.html?symbol=${coin1.fid }"
	                                                class="text-color2">立即充值<i
	                                                class="arrow-icon-small text-color2"></i></a> </a>
	                                        </c:if>
	                                        <c:if test="${fisrecharge1 eq 0}">
	                                            <a class="NoSub_" class="text-color2">立即充值
	                                            <i class="arrow-icon-small text-color2"></i></a> </a>
	                                        </c:if>
                                        </div>
                                    </div>
                                </div>
                            </div>


                            <div id="coinBoxbuybtc" style="width: 33.33%; float: right;">
                                <span class="trade-depth" style="padding-left: 0.6rem;">
                                ${ftrademapping.ftradedesc }：
                                <span
                                    class="lastprice"></span>
                                    <a class="hangqing" href="/trademarket.html?symbol=${ftrademapping.fid } "><i id="transaction" class="transaction" style="background: url(/static/front/images/index/transaction2.png);background-size:100% 100%;"></i></a>
                                </span>
                                <div class="trade-total" style="padding-left: .6rem;">
                                    <span>高:${high}</span>
                                    <span>低:${low}</span>
                                    <span>量:${total}</span>
                                </div>
                                <%--    <c:if test="${isLimittrade == true}">
                                <span class="trade-depth">
                                       涨停价：<span style="color:red;">${coin1.fSymbol }<fmt:formatNumber
                                            value="${upPrice }" pattern="##.##" maxIntegerDigits="15"
                                            maxFractionDigits="${ftrademapping.fcount2}" /> </span>, 跌停价：<span  style="color:red;">${coin1.fSymbol }<fmt:formatNumber
                                            value="${downPrice }" pattern="##.##" maxIntegerDigits="15"
                                            maxFractionDigits="${ftrademapping.fcount1}" /> </span>
                                </span>
                            </c:if> --%>
                                <div class="trade" style="margin-bottom: 0px !important;">
                                    <table
                                        class="table2 table-striped table-hover table-condensed border-light-grey trading-orderbook"
                                        style="margin-bottom: 0px !important;">
                                        <thead>
                                            <tr
                                                class="row-head row-narrow row-align-right regular-font-size">
                                                <th width="33.3%"
                                                    style="text-align: left; padding-left: 0.6rem;">委托</th>
                                                <th width="33.3%"
                                                    style="text-align: left; padding-left: 0.6rem;">单价</th>
                                                <th width="33.3%"
                                                    style="text-align: left; padding-left: 0.2rem;">委托量</th>
                                            </tr>
                                        </thead>
                                    </table>
                                </div>
                                <div class="centre-cont" style="border-bottom: 0;">
                                    <ul class="sellbox" id="marketDepthBuy"
                                        style="padding-left: 0;"></ul>
                                    <span>深度：</span>
                                    <span>
                                       <select name="page" id="page">
                                          <option value="1">1</option>
                                          <option value="2">2</option>
                                          <option value="3">3</option>
                                          <option value="4">4</option>
                                       </select>
                                    </span>
                                </div>
                                <div class="centre-cont">
                                    <ul class="buybox"></ul>
                                    <span>深度：</span>
                                    <span>
                                       <select name="page1" id="page1">
                                          <option value="1">1</option>
                                          <option value="2">2</option>
                                          <option value="3">3</option>
                                          <option value="4">4</option>
                                       </select>
                                    </span>
                                </div>

                                <h4 style="margin-top: 20px !important;">
                                    <font style="vertical-align: inherit;"> <font
                                        style="vertical-align: inherit;">最近成交记录</font></font>
                                </h4>
                                <ul id="logbox" class="list-group " style="line-height: 5px;">

                                </ul>
                            </div>

                            <div id="entrustInfo" style="width: 64%; float: left;"></div>
                        </div>
                    </div>


                </div>
            </div>
        </div>
    </div>
    <div class="modal modal-custom fade" id="tradepass" tabindex="-1"
        role="dialog" aria-labelledby="exampleModalLabel">
        <div class="modal-dialog modal-trading-dialog" role="document">
            <div class="modal-mark"></div>
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close btn-modal" data-dismiss="modal"
                        aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                    <span class="modal-title" id="exampleModalLabel">交易密码</span>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <input type="password" class="form-control" id="tradePwd"
                            placeholder="请输入交易密码">
                    </div>
                </div>
                <div class="modal-footer">
                    <button id="modalbtn" type="button" class="btn btn-primary">确定</button>
                </div>
            </div>
        </div>
    </div>
    <div class="modal modal-custom fade" id="entrustsdetail" tabindex="-1"
        role="dialog" aria-labelledby="exampleModalLabel">
        <div class="modal-dialog" role="document">
            <div class="modal-mark"></div>
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close btn-modal" data-dismiss="modal"
                        aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                    <span class="modal-title" id="exampleModalLabel"></span>
                </div>
                <div class="modal-body"></div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                </div>
            </div>
        </div>
    </div>
    <input id="coinshortName" type="hidden" value="${coin2.fShortName }">
    <input id="symbol" type="hidden" value="${ftrademapping.fid }">
    <input id="isopen" type="hidden" value="false">
    <input id="tradeType" type="hidden" value="0">
    <input id="userid" type="hidden" value="${userid }">

    <input id="tradePassword" type="hidden" value="${isTradePassword }">
    <input id="isTelephoneBind" type="hidden" value="${isTelephoneBind }">

    <input id="fcny" type="hidden" value="${fny}">
    <input id="coinCount1" type="hidden" value="${ftrademapping.fcount1 }">
    <input id="coinCount2" type="hidden" value="${ftrademapping.fcount2 }">
    <input id="minBuyCount" type="hidden"
        value="<fmt:formatNumber
                                    value="${ftrademapping.fminBuyCount }" pattern="##.##" maxIntegerDigits="15"
                                    maxFractionDigits="8" />">
    <input id="limitedType" type="hidden" value="0">
    <input id="fhasRealValidate" type="hidden"
        value="${fuser.fhasRealValidate }">
    <input id="authGrade" type="hidden" value="${fuser.authGrade}"/>
    <input id="fhasImgValidate" type="hidden" value="${fuser.fhasImgValidate }">
    <input id="fhasBankValidate" type="hidden" value="${fuser.fhasBankValidate}">
    <input id="ffAmount" type="hidden" value="<fmt:formatNumber value="${empty ffAmount == true ? '0':ffAmount }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="8"/>">
    <jsp:include page="../comm/footer.jsp"></jsp:include>
    <script type="text/javascript"
        src="/static/front/js/plugin/bootstrap.js"></script>
    <script type="text/javascript" src="/static/front/js/comm/util.js"></script>
    <script type="text/javascript" src="/static/front/js/comm/comm.js"></script>
    <script type="text/javascript"
        src="/static/front/js/language/language_cn.js"></script>
    <script type="text/javascript" src="/static/front/js/layer/layer.js"></script>
    <%-- <script type="text/javascript" src="/static/front/js/market/trademarket.js?t=<%=new java.util.Date().getTime()%>"></script> --%>
    <script type="text/javascript"
        src="${oss_url}/static/front/js/plugin/jquery.jslider.js"></script>
    <script type="text/javascript"
        src="${oss_url}/static/front/js/trade/trade.js?r=<%=new java.util.Date().getTime() %>"></script>
    <%-- <script type="text/javascript" src="${oss_url}/static/front/app/js/trade.js?r=<%=new java.util.Date().getTime() %>"></script> --%>

    <script>
        function maxBuy() {
            var toptradecnymoney = $("#toptradecnymoney").html();
            var tradebuyprice = $("#tradebuyprice").val();
            var maxBuy = (toptradecnymoney / tradebuyprice).toFixed(4);
            $("#maxBuy").html(maxBuy);
        }
        function maxSell() {
            var toptrademtccoin = $("#toptrademtccoin").html();
            var tradesellprice = $("#tradesellprice").val();
            var maxSell = (toptrademtccoin * tradesellprice).toFixed(4);
            $("#maxSell").html(maxSell);
        }
        function cny() {
            var tradebuyTurnover = $("#tradebuyTurnover").html();
            var fny = $("#fcny").val();
            var fny1 = (fny * tradebuyTurnover).toFixed(2);
            $("#buyCny").text(fny1);

        }
        function cny1() {
            var tradesellTurnover = $("#tradesellTurnover").html();
            var fny = $("#fcny").val();
            var fny1 = (fny * tradesellTurnover).toFixed(2);
            $("#buyCny1").text(fny1);
        }
        $("#tradebuyamount").focus(function(){
            document.getElementById("tradebuyamount").value="";
        })
        $("#tradesellamount").focus(function(){
            document.getElementById("tradesellamount").value="";
        })
        $(function(){
            var pnew = $(".p1-new").text();
            var fchangerate1 = "${fchangerate}";
            var fchangerate = parseFloat(fchangerate1);
            if(fchangerate > 0){
              $(".p1-new").html(pnew + " ↑");
              $(".p1-new").css("color", "green");
            }else if(fchangerate < 0){
              $(".p1-new").html(pnew + " ↓");
              $(".p1-new").css("color", "red");
            }else{
              $(".P1-new").html(pnew + " →");
              $(".p1-new").css("color", "green");
            }
        })
        $(function() {
            $('#tradebuyamount').bind('input propertychange', function() {
                var i1 = "${coin1.fcurrentCNY}";
                var tb = +$('#tradebuyamount').val();
                var tp = +$('#tradebuyprice').val();
                $("#buyCny").html(tb*tp*i1);
            });
            $('#tradebuyprice').bind('input propertychange', function() {
                var i1 = "${coin1.fcurrentCNY}";
                var tb = +$('#tradebuyamount').val();
                var tp = +$('#tradebuyprice').val();
                $("#buyCny").html(tb*tp*i1);
            });

        })
        $(function() {
            $('#tradesellamount').bind('input propertychange', function() {
                var i2 = "${coin1.fcurrentCNY}";
                var tb2 = +$('#tradesellamount').val();
                var tp2 = +$('#tradesellprice').val();
                $("#buyCny1").html(tb2*tp2*i2);
            });
            $('#tradesellprice').bind('input propertychange', function() {
                var i2 = "${coin1.fcurrentCNY}";
                var tb2 = +$('#tradesellamount').val();
                var tp2 = +$('#tradesellprice').val();
                $("#buyCny1").html(tb2*tp2*i2);
            });
        })
        $(document).ready(function(){
            // 鼠标移上移下事件：改变折线图图标的背景色
            $(".hangqing").mouseover(function(){
                $(this).find(".transaction").css("background","url(/static/front/images/index/transaction1.png)");
                $(this).find(".transaction").css("background-size","100% 100%");
            });
            $(".hangqing").mouseout(function(){
                $(this).find(".transaction").css("background","url(/static/front/images/index/transaction2.png)");
                $(this).find(".transaction").css("background-size","100% 100%");
            });
        });
        $(".NoSub_").click(function(){
        	  layer.msg("暂不支持此币种充值！")
       	});
    </script>
</body>
</html>
