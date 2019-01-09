<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../front/comm/include.inc.jsp"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>

<!doctype html>
<html>

<head>
    <base href="${basePath}" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no, width=device-width">
    <meta name="format-detection" content="telephone=no">
    <meta name="format-detection" content="email=no">
    <base href="${basePath}" />

    <link rel="stylesheet" href="${oss_url}/static/front/css/finance/withdraw.css" type="text/css">
    </link>
    <link rel="stylesheet" href="/static/front/app/css/css.css" />
    <link rel="stylesheet" href="/static/front/app/css/style.css" />
    <script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js"></script>
    <script>
        $(function(){
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

        .withdraw_list .right {
            position: absolute;
            /* width: 50px;
        height: 50px; */
            left: 90%;
            top: 15%;
        }

        .withdraw_list .right img {
            width: 1.5rem;
            height: 1.5rem;

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
            width: 60%;
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

        #withdrawBtcButton,
        #withdrawBtcAddrBtn {
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

        #withdrawBtcButtonDisable {
            height: 3.06rem;
            line-height: 3.06rem;
            font-size: 0.87rem;
            background: #e0e0e0;
            border-radius: 0.15rem;
            text-align: center;
            background: #9698a6;
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

        input,
        .select {
            outline: none;
        }

        .pay-money select {
            width: 68%;
        }

        .mars {
            width: 10rem;
            height: 100%;
            /* background-color: red; */
            position: absolute;
            right: 0;
            top: 0;
            display: none;
        }

        .mars1 {
            width: 10rem;
            height: 100%;
            /* background-color: red; */
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
    </style>
</head>

<body>
    <div class="cache_address">
        <nav>
            <div class="pay-centre">
                <!-- <a href="/m/financial/zichan.html"> -->
                <a href="javascript:history.go(-1)">
                    <span>
                        <em>
                            <i></i>
                            <i></i>
                        </em>
                        <strong>返回</strong>
                    </span>
                </a>
                <p><strong>提现</strong></p>
                <button class="text-primary addtips">新增地址</button>
            </div>
        </nav>
        <input type="hidden" id="max_double" value="${requestScope.constant['maxwithdrawbtc'] }">
        <input type="hidden" id="min_double" value="${requestScope.constant['minwithdrawbtc'] }">
        <input type="hidden" id="withdraw_ffee" value="${ffee}">
        <div class="container-full main-con">
            <div class="container displayFlex">
                <div class="row">
                    <div class="col-xs-10 padding-right-clear">
                        <div class="col-xs-12 padding-right-clear padding-left-clear rightarea withdraw">
                            <div class="col-xs-12 rightarea-con">
                                <div class="withdraw_list pay-money">
                                    <span class="withdraw_hint">请选择币种：</span>
                                    <select class="nav nav-tabs rightarea-tabs" onchange="changebiZhong(this.value)">
                                        <c:forEach items="${requestScope.constant['allWithdrawCoins'] }" var="v">
                                            <option class="${v.fid==symbol?'active':'' }" ${v.fid==symbol?'selected':''
                                                } value="${v.fid }">${v.fShortName }</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="col-xs-12 padding-clear padding-top-30">
                                    <div class="col-xs-7 padding-clear form-horizontal">
                                        <div class="form-group withdraw_list">
                                            <label for="withdrawAmount" class="col-xs-3 control-label withdraw_hint">账户余额：</label>
                                            <span class="form-control border-fff">
                                                <fmt:formatNumber value="${fvirtualwallet.ftotal }" pattern="##.##"
                                                    maxIntegerDigits="15" maxFractionDigits="4" /></span>
                                        </div>
                                        <div class="form-group withdraw_list pay-money">
                                            <label for="withdrawAddr" class="col-xs-3 control-label withdraw_hint">提现地址：</label>
                                            <select id="withdrawAddr" class="form-control" style="height: 95%;border: 0;">
                                                <c:forEach items="${fvirtualaddressWithdraws }" var="v">
                                                    <option value="${v.fid }">${v.fremark}-${v.fadderess }</option>
                                                </c:forEach>
                                            </select>

                                        </div>
                                        <div class="form-group withdraw_list">
                                            <label for="withdrawAmount" class="col-xs-3 control-label withdraw_hint">提现数量：</label>
                                            <input id="withdrawAmount" class="form-control withdraw_input" type="text">
                                            <input id="hasWithdrawAmount"  type="hidden" value="${hasWithdrawAmount}">
                                            <input id="canWithdrawTime"  type="hidden" value="${canWithdrawTime}">
                                            <input id="canWithdrawLimit"  type="hidden" >
                                            <input id="walletTotal"  type="hidden" value="${fvirtualwallet.ftotal }">
                                            <input id="withdrawAmountLimit" type="hidden" value="${fvirtualcointype.fWithdrawLimit}">
                                        </div>
                                        <div class="form-group withdraw_list">
                                            <label for="password" class="col-xs-3 control-label withdraw_hint">交易密码：</label>
                                            <input id="tradePwd" class="form-control withdraw_input" type="password">
                                        </div>
                                        <c:if test="${isBindTelephone == true }">
                                            <div class="form-group withdraw_list">
                                                <label for="withdrawPhoneCode" class="col-xs-3 control-label withdraw_hint">短信验证码：</label>
                                                <input id="withdrawPhoneCode" class="form-control" type="text">
                                                <button id="withdrawsendmessage" data-msgtype="5" data-tipsid="withdrawerrortips"
                                                    class="btn btn-sendmsg">发送验证码</button>
                                                <div class="mars"></div>
                                            </div>
                                        </c:if>
                                        <c:if test="${isBindGoogle ==true}">
                                            <div class="form-group withdraw_list">
                                                <label for="withdrawTotpCode" class="col-xs-3 control-label withdraw_hint">谷歌验证码：</label>
                                                <input id="withdrawTotpCode" class="form-control withdraw_input" type="text">
                                            </div>
                                        </c:if>
                                        <div class="form-group">
                                            <label for="diyMoney" class="col-xs-3 control-label"></label>
                                            <div class="col-xs-6">
                                                <span id="withdrawerrortips" class="text-danger">
                                                      <c:if test="${canWithdrawTime == false}">
                                                         当日该币种提币次数已到最大数=${fvirtualcointype.getfWithdrawTimes()}
                                                      </c:if>

                                                </span>
                                            </div>
                                        </div>
                                        <div id="withdrawBtcButtonDiv" class="form-group" style="text-align: center;">
                                            <label for="diyMoney" class="col-xs-3 control-label"></label>
                                            <c:if test="${canWithdrawTime == false}">
                                                <button id="withdrawBtcButtonDisable" class="btn btn-danger btn-block">立即提现</button>
                                            </c:if>
                                            <c:if test="${canWithdrawTime == true}">
                                                <button id="withdrawBtcButton" class="btn btn-danger btn-block">立即提现</button>
                                            </c:if>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="address_box" style="display: none;">
        <nav>
            <div class="pay-centre">
                <a href="${oss_url}/m/financial/zichan.html">
                    <span>
                        <em>
                            <i></i>
                            <i></i>
                        </em>
                        <strong>返回</strong>
                    </span>
                </a>
                <p><strong>提现</strong></p>
            </div>
        </nav>
        <div class="modal modal-custom fade" id="address" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
            <div class="modal-dialog" role="document">
                <div class="modal-mark"></div>
                <div class="modal-content">
                    <div class="modal-header" style="position: relative;">
                        <button type="button" class="close btn-modal" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                        <span class="modal-title withdraw_title" id="exampleModalLabel">提现地址</span>
                    </div>
                    <div class="modal-body form-horizontal">
                        <div class="form-group withdraw_list">
                            <label for="withdrawBtcAddr" class="col-xs-3 control-label withdraw_hint">提现地址：</label>
                            <input id="withdrawBtcAddr" class="form-control withdraw_input" type="text" >
                            <a class="right" >
                                <img src="/static/front/app/images/sao2.png">
                            </a>
                        </div>
                        <div class="form-group withdraw_list">
                            <label for="withdrawBtcRemark" class="col-xs-3 control-label withdraw_hint">备注：</label>
                            <input id="withdrawBtcRemark" class="form-control withdraw_input" type="text">
                        </div>
                        <div class="form-group withdraw_list">
                            <label for="withdrawBtcPass" class="col-xs-3 control-label withdraw_hint">交易密码</label>
                            <!-- <div class="col-xs-8"> -->
                            <input id="withdrawBtcPass" class="form-control withdraw_input" type="password">
                            <!-- </div> -->
                        </div>
                        <c:if test="${isBindTelephone == true }">
                            <div class="form-group withdraw_list">
                                <label for="withdrawBtcAddrPhoneCode" class="col-xs-3 control-label withdraw_hint">短信验证码</label>
                                <input id="withdrawBtcAddrPhoneCode" class="form-control" type="text">
                                <button id="bindsendmessage" data-msgtype="8" msgType="msg" data-tipsid="binderrortips"
                                    class="btn btn-sendmsg">发送验证码</button>
                                <div class="mars1"></div>
                            </div>
                        </c:if>

                        <c:if test="${isBindGoogle ==true}">
                            <div class="form-group withdraw_list">
                                <label for="withdrawBtcAddrTotpCode" class="col-xs-3 control-label withdraw_hint">谷歌验证码</label>
                                <input id="withdrawBtcAddrTotpCode" class="form-control withdraw_input" type="text">
                            </div>
                        </c:if>

                        <div class="form-group" id="sVoice1" style="display: none;">
                            <label for="diyMoney" class="col-xs-3 control-label"></label>
                            <div class="col-xs-8">
                                <span id="sendVoice" class="">没收到验证码？ 点击发送语音验证码</span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="diyMoney" class="col-xs-3 control-label"></label>
                            <div class="col-xs-8">
                                <span id="binderrortips" class="text-danger"></span>
                            </div>
                        </div>
                        <div class="form-group" style="text-align: center;">
                            <label for="diyMoney" class="col-xs-3 control-label"></label>
                            <div class="col-xs-8">
                                <button id="withdrawBtcAddrBtn" class="btn btn-danger btn-block">确定提交</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <input type="hidden" id="isEmptyAuth" value="${isEmptyAuth }">
    <input type="hidden" id="symbol" value="${fvirtualcointype.fid }">
    <input type="hidden" value="<fmt:formatNumber value=" ${fvirtualwallet.ftotal }" pattern="##.##" maxIntegerDigits="15"
        maxFractionDigits="4" />" id="btcbalance" name="btcbalance">
    <input type="hidden" value="${fvirtualcointype.fShortName }" id="coinName" name="coinName">
    <input type="hidden" value="${fuser.authGrade}" id="authGrade" name="authGrade" />
    <input type="hidden" value="${priceRange}" id="priceRange" name="priceRange" />


    <script type="text/javascript" src="/static/front/js/plugin/bootstrap.js"></script>
    <script type="text/javascript" src="/static/front/js/comm/util.js"></script>
    <script type="text/javascript" src="/static/front/js/comm/comm.js"></script>
    <script type="text/javascript" src="/static/front/js/language/language_cn.js"></script>
    <script type="text/javascript" src="/static/front/js/layer/layer.js"></script>
    <script type="text/javascript" src="/static/front/js/comm/msg.js"></script>
    <script type="text/javascript" src="/static/front/js/finance/account.withdraw.js"></script>
    <script type="text/javascript" src="/static/front/app/js/alert.js"></script>
    <script type="text/javascript">
        //币种选择
        function changebiZhong(value) {
            window.location.href = "${oss_url}/m/account/withdrawBtc.html?symbol=" + value;
        };


        $('.withdraw_list .right').click(function () { 
            var u = navigator.userAgent;
            var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Adr') > -1; 
            var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); 
            if(isAndroid){
                zyh_js.startQrCodeScan();
            }else if(isiOS){
                window.webkit.messageHandlers.startQrCodeScan.postMessage({})
            }
        })

        window.scanResult = function (result) {
            result = result.split(':')[1]
            if(result){
                $('#withdrawBtcAddr').val(result)
            }
            // layer.msg(result);
        }

        $(function () {
            $("body").on("click", ".addtips", function () {
                $(".address_box").show().prev().hide();
            });
            $("body").on("click", ".close", function () {
                $(".address_box").hide().prev().show();
            });
            $("#sendVoice").click(function () {
                $("#bindsendmessage").attr("msgType", "voice");
                $(".btn-sendmsg").click();
            });
        })
    </script>
</body>

</html>