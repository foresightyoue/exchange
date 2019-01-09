<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../front/comm/include.inc.jsp"%>
<%
    String path = request.getContextPath();
            String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                    + path;
%>
<!doctype html>
<html>
<head>
<base href="${basePath}">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no, width=device-width">
<meta name="format-detection" content="telephone=no">
<meta name="format-detection" content="email=no">
<base href="${basePath}" />
<link rel="stylesheet" href="${oss_url}/static/front/css/finance/withdraw.css" type="text/css"></link>
<link rel="stylesheet" href="/static/front/app/css/css.css" />
<link rel="stylesheet" href="/static/front/app/css/style.css" />
<script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js"></script>
<script>
    $(function() {
        var security = "${securityEnvironment}";
        var googleCheck = "${sessionScope.login_user.fgoogleCheck}";
        if (security == "true") {
            $("#withdrawBtcAddrPhoneCode").closest('.form-group').hide();
            $("#withdrawPhoneCode").closest('.form-group').hide();
        }
        if (googleCheck == "false") {
            $("#withdrawBtcAddrTotpCode").closest('.form-group').hide();
            $("#withdrawTotpCode").closest('.form-group').hide();
        }
    });
</script>
<style type="text/css">
.withdraw_list {
    height: 2.75rem;
    background-color: #fff;
    padding-left: .62rem;
    border-bottom: 1px solid #e6e6e6;
    line-height: 2.75rem;
    position: relative;
    width: 100%;
    box-sizing: border-box;
    -webkit-box-sizing: border-box;
}

.withdraw_title {
    height: 2.75rem;
    background-color: #e0e0e0;
    padding-left: .62rem;
    line-height: 2.75rem;
    position: relative;
    text-align: center;
    display: inline-block;
    width: 100%;
    font-size: 1rem;
    font-weight: 600;
    margin-top: .14rem;
}

.withdraw_list .withdraw_hint {
    width: 6rem;
    display: inline-block;
}

#withdrawAddr {
    display: inline-block;
    width: 68%;
    height: 100%;
    border-left: 1px solid #E1E1E1;
    border-right: 1px solid #E1E1E1;
    border-top: none;
    border-bottom: none;
    padding-right: 1.2rem;
    box-sizing: border-box;
}

.pay-money select {
    width: 73%;
}

.withdraw .addtips {
    position: absolute;
    top: 0;
    right: .62rem;
}

.withdraw_input {
    width: 70%;
    height: 100%;
    padding-left: 0 !important;
}

#withdrawPhoneCode {
    width: 35%;
    height: 100%;
    padding-left: 0
}

#withdrawsendmessage {
    width: 30%;
    text-align: right;
}

.text-danger {
    color: #c83935 !important;
}

#withdrawBtcButton, #withdrawBtcAddrBtn {
    height: 3.06rem;
    line-height: 3.06rem;
    font-size: 0.87rem;
    background: #e0e0e0;
    border-radius: 0.15rem;
    text-align: center;
    background: #31435b;
    margin: .62rem auto;
    color: #fff;
    width: 94%;
}

.close {
    position: absolute;
    right: .62rem;
    top: 0.4rem;
    z-index: 2;
}

.close span {
    font-size: 1.4rem;
}

#withdrawBtcAddrPhoneCode {
    width: 47%;
    height: 100%;
}

input, .select {
    outline: none;
}

.pay-money select {
    width: 68%;
}

.mars {
    width: 10rem;
    height: 100%;
    position: absolute;
    right: 0;
    top: 0;
    display: none;
}

.mars1 {
    width: 10rem;
    height: 100%;
    position: absolute;
    right: 0;
    top: 0;
    display: none;
}

.pay-centre .addtips {
    position: absolute;
    right: .62rem;
    top: 1rem;
    color: white;
    font-size: .87rem;
}

select {
    outline: none;
}

#sVoice1 {
    height: 60px;
    line-height: 60px;
    text-align: center;
    font-size: 20px;
    color: blue;
}

#transferButton {
    height: 3.06rem;
    line-height: 3.06rem;
    font-size: 0.87rem;
    background: #e0e0e0;
    border-radius: 0.15rem;
    text-align: center;
    background: #31435b;
    margin: .62rem auto;
    color: #fff;
    width: 94%;
}

.flexLayout {
    display: flex;
    justify-content: space-between;
}

.huaZhuan_record li {
    padding: .62rem;
    border-bottom: 1px solid #e6e6e6;
}

.record_hint {
    font-weight: bold;
    margin-bottom: .3rem;
}

.huaZhuan_record {
    height: 21rem;
    overflow: auto;
    border-top: 1px solid #e6e6e6;
}
</style>
</head>
<body>
    <div class="cache_address">
        <nav>
            <div class="pay-centre">
                <a href="javascript:history.go(-1)">
                    <span>
                    <em> <i></i>
                        <i></i>
                    </em>
                    <strong>返回</strong>
                    </span>
                </a>
                <p>
                    <strong>划转至封神榜平台</strong>
                </p>
            </div>
        </nav>
        <input type="hidden" id="max_double" value="${requestScope.constant['maxwithdrawat'] }">
        <input type="hidden" id="min_double" value="${requestScope.constant['minwithdrawat'] }">
        <input type="hidden" id="type" value="2">
        <div class="container-full main-con">
            <div class="container displayFlex">
                <div class="row">
                    <div class="col-xs-10 padding-right-clear">
                        <div
                            class="col-xs-12 padding-right-clear padding-left-clear rightarea withdraw">
                            <div class="col-xs-12 rightarea-con">
                                <div class="withdraw_list pay-money">
                                    <span class="withdraw_hint">请选择币种：</span>
                                    <select class="nav nav-tabs rightarea-tabs" onchange="changebiZhong(this.value)">
                                        <c:forEach items="${requestScope.constant['allWithdrawCoins'] }" var="v">
                                            <c:choose>
                                                <c:when test="${v.fid==symbol}">
                                                    <option class="${v.fid==symbol?'active':'' }" ${v.fid==symbol?'selected':'' } value="${v.fid}">${v.fShortName }划转</option>
                                                </c:when>
                                            </c:choose>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="col-xs-12 padding-clear padding-top-30">
                                    <c:if test="${!empty fvirtualcointype}">
                                        <div class="col-xs-7 padding-clear form-horizontal">
                                            <div class="form-group withdraw_list">
                                                <label for="withdrawAmount" class="col-xs-3 control-label withdraw_hint">账户余额：</label>
                                                <span class="form-control border-fff"><fmt:formatNumber value="${fvirtualwallet.ftotal }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="4" /></span>
                                            </div>
                                            <div class="form-group withdraw_list">
                                                <label for="transferAccount" class="col-xs-3 control-label withdraw_hint">划转账号:</label>
                                                <input id="transferAccount" class="form-control withdraw_input" type="text" value="${fuser.floginName}">

                                            </div>
                                            <div class="form-group withdraw_list">
                                                <label for="withdrawAmount" class="col-xs-3 control-label withdraw_hint">划转数量：</label>
                                                <input id="withdrawAmount" class="form-control withdraw_input" type="text">
                                            </div>
                                            <div class="form-group withdraw_list">
                                                <label for="password" class="col-xs-3 control-label withdraw_hint">交易密码：</label>
                                                <input id="tradePwd" class="form-control withdraw_input" type="password">
                                            </div>
                                            <c:if test="${isBindGoogle ==true}">
                                                <div class="form-group withdraw_list">
                                                    <label for="withdrawTotpCode" class="col-xs-3 control-label withdraw_hint">谷歌验证码：</label>
                                                    <input id="withdrawTotpCode" class="form-control withdraw_input" type="text">
                                                </div>
                                            </c:if>
                                            <div style="height: 2rem; line-height: 2rem;">
                                                <label for="diyMoney" class="col-xs-3 control-label"></label>
                                                <span id="withdrawerrortips" class="text-danger"></span>
                                            </div>
                                            <div class="form-group" style="text-align: center; margin-bottom: 1rem;">
                                                <button id="transferButton" class="btn btn-danger btn-block">立即划转</button>
                                            </div>
                                        </div>
                                    </c:if>
                                    <ul class="huaZhuan_record">
                                        <c:forEach items="${ftransfer }" varStatus="vs" var="v">
                                            <li>
                                                <div style="margin-bottom: .62rem;">
                                                    <div class="flexLayout">
                                                        <span class="record_hint">划转时间</span> <span class="record_hint">划转状态</span>
                                                    </div>
                                                    <div class="flexLayout">
                                                        <span><fmt:formatDate value="${v.fCreateTime }" pattern="yyyy-MM-dd HH:mm:ss" /></span>
                                                        <c:if test="${v.fStatus==0 }">
                                                            <span>待审核</span>
                                                        </c:if>
                                                        <c:if test="${v.fStatus==1 }">
                                                            <span>已完成</span>
                                                        </c:if>
                                                        <c:if test="${v.fStatus==2 }">
                                                            <span>审核未通过</span>
                                                        </c:if>
                                                    </div>
                                                </div>
                                                <div>
                                                    <div class="flexLayout">
                                                        <span class="record_hint">划转账号</span>
                                                        <span class="record_hint">划转金额</span>
                                                    </div>
                                                    <div class="flexLayout">
                                                        <span>${v.fAccount }</span>
                                                        <span>${v.fAmount }</span>
                                                    </div>
                                                </div>
                                            </li>
                                        </c:forEach>
                                    </ul>
                                    <c:if test="${count==0 }">
                                        <div class="no-data-tips">您暂时没有划转记录。</div>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <input type="hidden" id="isEmptyAuth" value="${isEmptyAuth }">
    <input type="hidden" id="symbol" value="${fvirtualcointype.fid }">
    <input type="hidden" value="<fmt:formatNumber value="${fvirtualwallet.ftotal }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="4"/>" id="btcbalance" name="btcbalance">
    <input type="hidden" value="${fvirtualcointype.fShortName }" id="coinName" name="coinName">
    <input type="hidden" value="${fuser.authGrade}" id="authGrade" name="authGrade" />
    <input type="hidden" value="${priceRange}" id="priceRange" name="priceRange" />
    <script type="text/javascript" src="${oss_url}/static/front/js/plugin/bootstrap.js"></script>
    <script type="text/javascript" src="${oss_url}/static/front/js/language/language_cn.js"></script>
    <script type="text/javascript" src="${oss_url}/static/front/js/comm/util.js"></script>
    <script type="text/javascript" src="${oss_url}/static/front/js/comm/msg.js"></script>
    <script type="text/javascript" src="${oss_url}/static/front/js/comm/comm.js"></script>
    <script type="text/javascript" src="${oss_url}/static/front/js/finance/account.withdraw1.js"></script>
    <script type="text/javascript" src="${oss_url}/static/front/js/plugin/layer/layer.js"></script>
    <script type="text/javascript" src="/static/front/app/js/alert.js"></script>
    <script type="text/javascript">
        //币种选择
        function changebiZhong(value) {
            window.location.href = "${oss_url}/m/account/transfer.html?symbol=" + value;
        }
    </script>
</body>
</html>