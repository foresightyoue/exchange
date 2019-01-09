<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../../front/comm/include.inc.jsp" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>

<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no, width=device-width">
    <meta name="format-detection" content="telephone=no">
    <meta name="format-detection" content="email=no">
    <base href="${basePath}" />
    <title>
        <c:if test="${isBindTelephone == true }">修改</c:if>
        <c:if test="${isBindTelephone == false }">绑定</c:if>手机号
    </title>
    <link rel="stylesheet" href="/static/front/app/css/css.css" />
    <link rel="stylesheet" href="/static/front/app/css/style.css" />
    <link rel="stylesheet" type="text/css" href="/static/front/app/css/intlTelInput.css" />
    <link rel="stylesheet" href="/static/front/app/css/swiper-3.4.2.min.css" />
    <script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js"></script>
    <script type="text/javascript" src="/static/front/app/js/swiper-3.4.2.jquery.min.js"></script>
    <script type="text/javascript" src="/static/front/app/js/intlTelInput.min.js"></script>
    <style>
        .Personal-cont p img{
            padding:0;
        }
        .information-cont{
            margin-top: 0;
        }
        .information-cont ul li{
            padding-left: .62rem;
            padding-right: .62rem;
        }
        #information-cont ul li input{
            padding-left: 0;
        }
        #sms,#voice,#tsms{
           -webkit-appearance: radio;
           width: auto;
           margin-top: 1rem;
       }
       .information-cont .sendVoice label,.information-cont .sendVoice1 label{
           width: 2.5rem;
       }
       .information-cont .sendVoice,.information-cont .sendVoice1{
           background-color: transparent;
           border-bottom: 0;
       }
       .sendVoice_hint{
           display: inline-block;
           width: 5rem;
           color: #808080;
       }
       .next-mars{
            width: 100%;
            height: 3.06rem;
            position: absolute;
            left: 0;
            bottom: 0;
            z-index: 10;
            display: none;
        }
       #unbindphone-newphone-areacode{
           display: inline-block;
           width: 2.5rem;
       }
        .Personal-title span em{
            width: 1.2rem;
            height: 1.2rem;
            left: 0.4rem;
            top:0.65rem;
        }
        .Personal-title span em i:last-child{
            left:0.22rem;
        }
        .intl-tel-input {
            width: 100%;
            height: 100%;
        }
    </style>
    <script>
        $(function(){
            var security = "${securityEnvironment}";
            var googleCheck = "${sessionScope.login_user.fgoogleCheck}";
            if (security == "true") {
                $("#unbindphone-msgcode").closest('li').hide();
            }
            if (googleCheck == "false") {
                $("#unbindphone-googlecode").closest('li').hide();
            }
        });
    </script>
</head>

<body style="background: #fff;">
    <nav>
        <div class="Personal-title">
            <span>
                <a href="javascript:;" onClick="javascript :history.back(-1)">
                    <em>
                        <i></i>
                        <i></i>
                    </em>
                    <strong>
                        <!-- 返回 --></strong>
                </a>
            </span>
            <c:if test="${isBindTelephone == true }">修改</c:if>
            <c:if test="${isBindTelephone == false }">绑定</c:if>手机号
        </div>
    </nav>
    <section>
        <div class="information-cont" id="information-cont">
            <c:if test="${isBindTelephone == false }">
                <ul>
                    <li style="border-top:1px solid #e1e1e1;">
                        <input id="bindphone-newphone" class="phone-number" type="number" value="" placeholder="请输入手机号">
                    </li>
                    <li>
                        <input type="number" class="phone-code" value="" id="bindphone-msgcode" placeholder="手机验证码">
                        <span id="bindphone-sendmessage1" data-msgtype="2" onclick="sendVoice()" data-tipsid="bindphone-errortips"
                            class="btn btn-sendmsg gain">获取验证码</span>
                    </li>
                    <c:if test="${isBindGoogle ==true}">
                        <li>
                            <input id="bindphone-googlecode" class="form-control" type="text" placeholder="谷歌验证码">
                        </li>
                    </c:if>
                    <li>
                        <input type="text" class="phone-code" value="" id="bindphone-imgcode" placeholder="请输入验证码">
                        <span class="gain1 btn btn-imgcode"><img src="/servlet/ValidateImageServlet?r=1501754647473"
                                style="width: 100%; height: 100%;"></span>
                    </li>
                </ul>
                <div class="next-padding">
                    <div id="bindphone-Btn" class="next next-active">提交</div>
                </div>
            </c:if>
            <c:if test="${isBindTelephone == true }">
                <ul>
                    <li>
                        <span class="btn">已绑定手机号：</span>
                        <span id="unbindphone-phone" class="phone-code">${telNumber}</span>
                        <input type="hidden" id="bindphone-newphone" value="${fuser.ftelephone }" />
                    </li>
                    <li>
                        <input id="unbindphone-msgcode" type="number" class="phone-code" value="" placeholder="手机验证码">
                        <button id="bindphone-sendmessage" onclick="tsendVoice()" data-msgtype="3" data-tipsid="unbindphone-errortips"
                            class="btn btn-sendmsg gain1">发送验证码</button>
                    </li>
                    <li class="sendVoice1" style="border-bottom:1px solid #e6e6e6;">
                        <span class="sendVoice_hint">发送方式</span>
                        <input name="tsendcode" checked="checked" type="radio" id="tsms"><label for="tsms">短信</label>
                        <!-- <input name="tsendcode" type="radio" id="voice"><label for="voice">语音</label> -->
                    </li>

                    <li>
                        <!-- <input id="unbindphone-newphone" class="phone-number" type="tel" value="" placeholder="更换手机号"> -->
                        <div style="flex: 1;width: 100%;height: 100%;">
                            <input style="width: 100%;height: 100%;padding-left: 15%;" id="unbindphone-newphone" class="phone-number"
                                type="tel" value="" placeholder="更换手机号">
                        </div>
                    </li>
                    <li>
                        <input id="unbindphone-newmsgcode" type="number" class="phone-code" value="" placeholder="手机验证码">
                        <input id="sendVoice" type="hidden" value="false">
                        <button id="unbindphone-newsendmessage" onclick="sendVoice()" data-msgtype="2" data-tipsid="unbindphone-errortips"
                            class="btn uncertifiedSendMsgCode gain1">发送验证码</button>
                        <!-- <div class="sendVoice">
                       <input name="sendcode" checked="checked" type="radio" id="sms"><label for="sms">短信</label>
                       <input name="sendcode" type="radio" id="voice"><label for="voice">语音</label>
                    </div> -->
                    </li>
                    <c:if test="${isBindGoogle ==true}">
                        <li>
                            <input id="unbindphone-googlecode" class="form-control" type="text" placeholder="谷歌验证码">
                        </li>
                    </c:if>
                    <li>
                        <input id="unbindphone-imgcode" type="text" class="phone-code" value="" placeholder="请输入验证码">
                        <span class="gain1"><img id="imgcode" class="btn-imgcode" src="/servlet/ValidateImageServlet?r=1501754647473"
                                style="width: 100%; height: 100%;"></span>
                    </li>
                    <li class="sendVoice">
                        <span class="sendVoice_hint">发送方式</span>
                        <input name="sendcode" checked="checked" type="radio" id="sms"><label for="sms">短信</label>
                        <!-- <input name="sendcode" type="radio" id="voice"><label for="voice">语音</label> -->
                    </li>
                </ul>
                <div class="next-padding">
                    <div id="unbindphone-Btn" class="next next-active">提交</div>
                </div>
                <div class="next-mars"></div>
            </c:if>
        </div>
    </section>
</body>
<input type="hidden" id="isphone" value="1">
<script type="text/javascript" src="${oss_url}/static/front/js/user/user.securityt.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/comm/msg.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/plugin/bootstrap.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/plugin/layer/layer.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/comm/util.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/comm/comm.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/language/language_cn.js"></script>
<script>
    $(function () {
        $("#unbindphone-newphone").intlTelInput({
                //是否允许下拉
                // allowDropdown: true,
                // if there is just a dial code in the input: remove it on blur, and re-add it on focus
                // autoHideDialCode: true,
                // 添加在所选国家例数输入占位符
                // autoPlaceholder: "polite",
                // 修改自动占位符
                // customPlaceholder: null,
                // 附加菜单到特定元素
                // dropdownContainer: "",
                // 不要显示这些国家
                // excludeCountries: [],
                // 在初始化过程中格式化的输入的值
                // formatOnDisplay: true,
                //更多的查找功能
                // geoIpLookup: null,
                //初始国家
                initialCountry: "cn",
                // 不要插入国际拨号码
                // nationalMode: true,
                //数字类型用于占位符
                // placeholderNumberType: "MOBILE",
                // display only these countries
                // onlyCountries: [],
                // 显示只有这些国家
                preferredCountries: ["us", "cn"],
                //在选定的标记旁边显示国家拨号码，因此它不是键入编号的一部分
                // separateDialCode: false,
                // 指定要启用验证/格式的一个专门用于脚本的路径
                // utilsScript: ""
            }

        );
        /*  $(".next").on('click',function(){
             window.location.href = 'gerenzhongxin.html';
         }); */
        $(".btn-imgcode").on("click", function () {
            this.src = "/servlet/ValidateImageServlet?r=" + Math.round(Math.random() * 100);
        });
    });

    function sendVoice() {
        if ($(".sendVoice input[type='radio']:checked").attr("id") == "sms") {
            $("#sendVoice").val("false");
        } else {
            $("#sendVoice").val("true");
        }
        return true;
    }

    function tsendVoice() {
        if ($(".sendVoice1 input[type='radio']:checked").attr("id") == "tsms") {
            $("#sendVoice").val("false");
        } else {
            $("#sendVoice").val("true");
        }
        return true;
    }

    $(".uncertifiedSendMsgCode").on("click", function (date) {
        var areacode = $("#unbindphone-newphone").intlTelInput("getSelectedCountryData").dialCode;
        var phone = $("#unbindphone-newphone").val();
        if (phone == "") {
            util.showerrortips("unbindphone-errortips",
                language["comm.error.tips.6"]);
            return;
        }
        if (!util.checkMobile(phone)) {
            layer.msg("请输入正确的手机号");
            return;
        }
        var sendVoice = $("#sendVoice").val();
        uncertifiedSendMsgCode($(this).data().msgtype, $(this).data().tipsid, this.id, areacode, phone, null,
            sendVoice);

    })

    /* $(".uncertifiedSendMsgCode").onclick(function () {
         var areacode = $("#unbindphone-newphone").intlTelInput("getSelectedCountryData").dialCode;
         var phone = $("#unbindphone-newphone").val();
         if (phone == "") {
             util.showerrortips("unbindphone-errortips",
                 language["comm.error.tips.6"]);
             return;
         }
         if (!util.checkMobile(phone)) {
             layer.msg("请输入正确的手机号");
             return;
         }
         var sendVoice = $("#sendVoice").val();
         uncertifiedSendMsgCode($(this).data().msgtype,$(this).data().tipsid, this.id, areacode, phone,null, sendVoice);
     });*/

    function uncertifiedSendMsgCode(type, tipElement_id, button_id, areaCode, phone, vcode, sendVoice) {
        secs = 121;
        var that = this;
        var tipElement = document.getElementById(tipElement_id);
        var button = document.getElementById(button_id);
        if (typeof (areaCode) == 'undefined') {
            areaCode = 0;
        }
        if (typeof (phone) == 'undefined') {
            phone = 0;
        } else {
            if (!util.checkMobile(phone)) {
                util.showerrortips(tipElement_id, language["comm.error.tips.10"]);
                return;
            }
        }

        var url = "/user/sendMsg.html?random=" + Math.round(Math.random() * 100);
        $.post(url, {
            type: type,
            msgtype: this.msgtype,
            areaCode: areaCode,
            phone: phone,
            vcode: vcode,
            sendVoice: sendVoice,
            sendNumberAuth: false
        }, function (data) {
            console.log(data,'  ');
            
            $("#sVoice1").css("display", "none");
            if (data.code < 0) {
                $("#register-errortips").empty();
                layer.msg(data.msg);
                $(".btn-imgcode").click();
            } else if (data.code == 0) {
                util.showerrortips(tipElement_id, data.msg);
                button.disabled = true;
                for (var num = 1; num <= secs; num++) {
                    window.setTimeout("updateNumber(" + num + ",'" + button_id + "',2)", num * 1000);
                }
            }
        }, "json");
    }

    function updateNumber(num, button_id, isVoice) {
        var button = document.getElementById(button_id);
        if (num == secs) {
            button.innerHTML = language["comm.error.tips.33"];
            button.disabled = false;
        } else {
            var printnr = secs - num;
            button.innerHTML = language["comm.error.tips.32"].format(printnr);
        }
        if (num == 121) {
            $("#sVoice1").css("display", "");
        }
    }
</script>

</html>