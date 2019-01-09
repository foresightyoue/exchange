var security = {
    loadgoogleAuth: function () {
        var url = "/user/googleAuth.html?random="
            + Math.round(Math.random() * 100);
        var param = null;
        $
            .post(
                url,
                param,
                function (data) {
                    if (data.code == 0) {
                        if (navigator.userAgent.indexOf("MSIE") > 0) {
                            $('#qrcode').html("").qrcode({
                                text: data.qecode,
                                width: "140",
                                height: "140",
                                render: "table"
                            });
                        } else {
                            $('#qrcode').html("").qrcode({
                                text: data.qecode,
                                width: "140",
                                height: "140"
                            });
                        }
                        document.getElementById("bindgoogle-key").innerHTML = data.totpKey;
                        document.getElementById("bindgoogle-key-hide").value = data.totpKey;
                    }
                }, "json");
    },
    submitValidateEmailForm: function (email, flag) {
        var desc = '';
        if (email.indexOf(" ") > -1) {
            desc = language["comm.error.tips.11"];
        }
        if (email == '') {
            desc = language["comm.error.tips.7"];
        }
        if (!util.checkEmail(email)) {
            desc = language["comm.error.tips.16"];
        }
        if (desc != "") {
            util.showerrortips('bindemail-errortips', desc);
            return;
        } else {
            util.hideerrortips('bindemail-errortips');
        }
        var url = "/validate/postValidateMail.html?random="
            + Math.round(Math.random() * 100);
        $.post(url, {
            email: email
        }, function (data) {
            if (data.code == 0) {
                if (flag) {
                    util.showerrortips('', data.msg, {
                        okbtn: function () {
                            $('#alertTips').modal('hide');
                            location.reload(true);
                        }
                    });
                } else {
                    util.showerrortips('bindemail-errortips', data.msg);
                    window.setTimeout(function () {
                        window.location.href = '/user/security.html';// pc

                    }, 1000);
                }
            } else {
                if (flag) {
                    util.showerrortips('', data.msg);
                } else {
                    util.showerrortips('bindemail-errortips', data.msg);
                }
            }
        }, "json");

        return;
    },
    areaCodeChange: function (ele, setEle) {
        var code = $(ele).val();
        $("#" + setEle).html("+" + code);
    },
    submitValidatePhoneForm: function (isUpdate) {
        // debugger;
        var phone = document.getElementById("unbindphone-newphone").value;
        var oldcode = document.getElementById("unbindphone-msgcode").value;
        var newcode = document.getElementById("unbindphone-newmsgcode").value;
        // var areaCode = document.getElementById("unbindphone-newphone-areacode").innerHTML;
        var areaCode = $("#unbindphone-newphone").intlTelInput("getSelectedCountryData").dialCode
        // console.log(areaCode);
        // return
        var imgcode = document.getElementById("unbindphone-imgcode").value;
        var totpCode = 0;
        var desc = '';
        if (phone.indexOf(" ") > -1) {
            desc = language["comm.error.tips.8"];
            util.showerrortips('unbindphone-errortips', desc);
            return false;
        } else {
            if (phone == '') {
                desc = language["comm.error.tips.6"];
                util.showerrortips('unbindphone-errortips', desc);
                return false;
            }
        }
        // if (oldcode) {
        if (oldcode.indexOf(" ") > -1 || oldcode.length != 6
            || !/^[0-9]{6}$/.test(oldcode)) {
            desc = language["comm.error.tips.124"];
            util.showerrortips('unbindphone-errortips', desc);
            return false;
        }
        // }
        if (newcode.indexOf(" ") > -1 || newcode.length != 6
            || !/^[0-9]{6}$/.test(newcode)) {
            desc = language["comm.error.tips.124"];
            util.showerrortips('unbindphone-errortips', desc);
            return false;
        }
        if (document.getElementById("unbindphone-googlecode") != null
            && !$("#unbindphone-googlecode").is(":hidden")) {
            totpCode = document.getElementById("unbindphone-googlecode").value;
            if (totpCode.indexOf(" ") > -1 || totpCode.length != 6
                || !/^[0-9]{6}$/.test(totpCode)) {
                desc = language["comm.error.tips.98"];
                util.showerrortips('unbindphone-errortips', desc);
                return false;
            }
        }
        if (!util.checkMobile(phone)) {
            util.showerrortips('unbindphone-errortips',
                language["comm.error.tips.10"]);
            return;
        }
        util.hideerrortips('unbindphone-errortips');
        var url = "/user/validatePhone.html?random="
            + Math.round(Math.random() * 100);
        $(".next-mars").show();
        var param = {
            isUpdate: isUpdate,
            phone: phone,
            oldcode: oldcode,
            newcode: newcode,
            totpCode: totpCode,
            areaCode: areaCode,
            imgcode: imgcode
        };
        jQuery.post(url, param, function (result) {
            util.showerrortips('unbindphone-errortips', result.msg);
            // debugger;
            if (result.code == 0) {
                deleteCookie("loginName", "/");
                deleteCookie("password", "/");


                window.location.href = "/m/user/logout.html";
                /*
                 * window.setTimeout(function() { window.location.href =
                 * '/user/security.html'; }, 1000);
                 */
                // setTimeout(function(){history.go(-1)},3000);
            }
            $(".next-mars").hide();
        }, "json");
    },
    submitBindPhone: function (isUpdate) {
        var phone = document.getElementById("bindphone-newphone").value;
        var newcode = document.getElementById("bindphone-msgcode").value;
        var areaCode = document.getElementById("bindphone-newphone-areacode").innerHTML;
        var imgcode = document.getElementById("bindphone-imgcode").value;
        var totpCode = 0;
        var oldcode = 0;
        var desc = '';
        if (phone.indexOf(" ") > -1) {
            desc = language["comm.error.tips.8"];
            util.showerrortips('bindphone-errortips', desc);
            return false;
        } else {
            if (phone == '') {
                desc = language["comm.error.tips.6"];
                util.showerrortips('bindphone-errortips', desc);
                return false;
            }
        }
        if (newcode.indexOf(" ") > -1 || newcode.length != 6
            || !/^[0-9]{6}$/.test(newcode)) {
            desc = language["comm.error.tips.124"];
            util.showerrortips('bindphone-errortips', desc);
            return false;
        }
        if (document.getElementById("bindphone-googlecode") != null) {
            totpCode = document.getElementById("bindphone-googlecode").value;
            if (totpCode.indexOf(" ") > -1 || totpCode.length != 6
                || !/^[0-9]{6}$/.test(totpCode)) {
                desc = language["comm.error.tips.98"];
                util.showerrortips('bindphone-errortips', desc);
                return false;
            }
        }
        if (!util.checkMobile(phone)) {
            util.showerrortips('bindphone-errortips',
                language["comm.error.tips.10"]);
            return;
        }
        util.hideerrortips('bindphone-errortips');
        var url = "/user/validatePhone.html?random="
            + Math.round(Math.random() * 100);
        var param = {
            isUpdate: isUpdate,
            phone: phone,
            oldcode: oldcode,
            newcode: newcode,
            totpCode: totpCode,
            areaCode: areaCode,
            imgcode: imgcode
        };
        jQuery.post(url, param, function (result) {
            util.showerrortips('bindphone-errortips', result.msg);
            if (result.code == 0) {
                /*
                 * window.setTimeout(function() { window.location.href =
                 * '/user/security.html'; }, 1000);
                 */
            }
        }, "json");
    },
    submitbindGoogleForm: function () {
        var code = document.getElementById("bindgoogle-topcode").value;
        var totpKey = document.getElementById("bindgoogle-key-hide").value;
        var phoneCode = 0;
        var desc = '';
        if (!/^[0-9]{6}$/.test(code)) {
            desc = language["comm.error.tips.98"];
        }
        if (desc != "") {
            util.showerrortips('bindgoogle-errortips', desc);
            return;
        } else {
            util.hideerrortips('bindgoogle-errortips', false);
        }
        var url = "/user/validateAuthenticator.html?random="
            + Math.round(Math.random() * 100);
        var param = {
            code: code,
            totpKey: totpKey
        };
        jQuery.post(url, param, function (data) {
            if (data.code == 0) {
                util.showerrortips('bindgoogle-errortips', data.msg);
                window.setTimeout(function () {
                    if (/(iPhone|iPad|iPod|iOS)/i.test(navigator.userAgent)) {
                        // alert(navigator.userAgent);
                        window.location.href = '/m/security.html';// iphone
                    } else if (/(Android)/i.test(navigator.userAgent)) {
                        // alert(navigator.userAgent);
                        window.location.href = '/m/security.html';// android
                    } else {
                        window.location.href = '/user/security.html';// pc
                    }
                    ;
                }, 1000);
            } else {
                util.showerrortips('bindgoogle-errortips', data.msg);
                document.getElementById("bindgoogle-topcode").value = "";
            }
        }, "json");
    },
    submitlookbindGoogleForm: function () {
        var totpCode = 0;
        var desc = '';
        totpCode = document.getElementById("unbindgoogle-topcode").value;
        if (!/^[0-9]{6}$/.test(totpCode)) {
            desc = language["comm.error.tips.98"];
        }
        if (desc != "") {
            util.showerrortips('unbindgoogle-errortips', desc);
            return;
        } else {
            util.hideerrortips('unbindgoogle-errortips');
        }
        var url = "/user/getGoogleTotpKey.html?random="
            + Math.round(Math.random() * 100);
        var param = {
            totpCode: totpCode
        };
        jQuery.post(url, param, function (data) {
            if (data.code == -1) {
                util.showerrortips('unbindgoogle-errortips', data.msg);
                document.getElementById("unbindgoogle-topcode").value = "";
            } else if (data.code == 0) {

                if (data.code == 0) {
                    if (navigator.userAgent.indexOf("MSIE") > 0) {
                        jQuery('#unqrcode').qrcode({
                            text: data.qecode,
                            width: "140",
                            height: "140",
                            render: "table"
                        });
                    } else {
                        jQuery('#unqrcode').qrcode({
                            text: data.qecode,
                            width: "140",
                            height: "140"
                        });
                    }
                    $("#unbindgoogle-key").html(data.totpKey);
                    $(".unbindgoogle-hide-box").show();
                    $(".unbindgoogle-show-box").hide();
                    centerModals();
                }
            }
        }, "json");
    },
    submitPwdForm: function (pwdType, istrade) {
        var originPwdEle = "";
        var newPwdEle = "";
        var reNewPwdEle = "";
        var phoneCodeEle = "";
        var totpCodeEle = "";
        var errorEle = "";
        if (pwdType == 0) {
            originPwdEle = "unbindloginpass-oldpass";
            newPwdEle = "unbindloginpass-newpass";
            reNewPwdEle = "unbindloginpass-confirmpass";
            phoneCodeEle = "unbindloginpass-msgcode";
            totpCodeEle = "unbindloginpass-googlecode";
            errorEle = "unbindloginpass-errortips";
            var originPwd = util
                .trim(document.getElementById(originPwdEle).value);
            var originPwdTips = util.isOriginPassword(originPwd);
        } else {
            if (istrade == 1) {
                originPwdEle = "unbindtradepass-oldpass";
                newPwdEle = "unbindtradepass-newpass";
                reNewPwdEle = "unbindtradepass-confirmpass";
                phoneCodeEle = "unbindtradepass-msgcode";
                totpCodeEle = "unbindtradepass-googlecode";
                errorEle = "unbindtradepass-errortips";

            } else {
                originPwdEle = "bindtradepass-oldpass";
                newPwdEle = "bindtradepass-newpass";
                reNewPwdEle = "bindtradepass-confirmpass";
                phoneCodeEle = "bindtradepass-msgcode";
                totpCodeEle = "bindtradepass-googlecode";
                errorEle = "bindtradepass-errortips";
            }
        }
        if (istrade == 0) {

        }
        var newPwd = util.trim(document.getElementById(newPwdEle).value);
        var reNewPwd = util.trim(document.getElementById(reNewPwdEle).value);
        if (istrade == 0) {
            // var originPwdTips = util.isOriginPassword(originPwd);
        }
        var newPwdTips = util.isPassword(newPwd);
        var reNewPwdTips = util.isPassword(reNewPwd);
        var originTip = true;
        if (istrade == 0 && originPwdTips != "") {
            util.showerrortips(errorEle, originPwdTips);
            return;
        } else {
            util.hideerrortips(errorEle);
        }
        if (newPwdTips != "") {
            util.showerrortips(errorEle, newPwdTips);
            return;
        } else {
            util.hideerrortips(errorEle);
        }
        if (reNewPwdTips != "") {
            util.showerrortips(errorEle, reNewPwdTips);
            return;
        } else if (newPwd != reNewPwd) {
            util.showerrortips(errorEle, language["comm.error.tips.109"]);
            document.getElementById(reNewPwdEle).value = "";
            return;
        } else {
            util.hideerrortips(errorEle);
        }
        var phoneCode = "";
        var totpCode = "";
        if (document.getElementById(phoneCodeEle) != null
            && !$("#" + phoneCodeEle).is(":hidden")) {
            phoneCode = util.trim(document.getElementById(phoneCodeEle).value);
            if (phoneCode == "") {
                util.showerrortips(errorEle, language["comm.error.tips.60"]);
                return;
            }
            if (!/^[0-9]{6}$/.test(phoneCode)) {
                util.showerrortips(errorEle, language["comm.error.tips.124"]);
                return;
            } else {
                util.hideerrortips(errorEle);
            }
        }
        if (document.getElementById(totpCodeEle) != null
            && !$("#" + totpCodeEle).is(":hidden")) {
            totpCode = util.trim(document.getElementById(totpCodeEle).value);
            if (!/^[0-9]{6}$/.test(totpCode)) {
                util.showerrortips(errorEle, language["comm.error.tips.98"]);
                return;
            } else {
                util.hideerrortips(errorEle);
            }
        }
        // if (document.getElementById(phoneCodeEle) == null &&
        // document.getElementById(totpCodeEle) == null) {
        // util.showerrortips(errorEle, language["comm.error.tips.110"]);
        // return;
        // }
        var url = "/m/user/modifyPwd.html?random="
            + Math.round(Math.random() * 100);
        var param = {
            pwdType: pwdType,
            originPwd: originPwd,
            newPwd: newPwd,
            reNewPwd: reNewPwd,
            phoneCode: phoneCode,
            totpCode: totpCode
        };
        $(".next-mars").show();
        jQuery.post(url, param, function (data) {
            if (data.code == 0) {
                if (istrade) {
                    // "修改成功，请牢记您的" + (pwdType == 0 ? '登录' : '交易') + "密码！"
                    var type = (pwdType == 0 ? language["comm.error.tips.142"]
                        : language["comm.error.tips.143"]);
                    util.showerrortips(errorEle,
                        language["comm.error.tips.141"].format(type));
                    setTimeout(function () {
                        history.go(-1)
                    }, 2000);
                } else {
                    util.showerrortips(errorEle,
                        language["comm.error.tips.140"]);
                }
            } else if (data.code == -3) {
                util.showerrortips(errorEle, data.msg);
                document.getElementById(reNewPwdEle).value = "";
            } else if (data.code == -5) {
                util.showerrortips(errorEle, data.msg);
                document.getElementById(originPwdEle).value = "";
            } else if (data.code == -6) {
                util.showerrortips(errorEle, data.msg);
                document.getElementById(totpCodeEle).value = "";
            } else if (data.code == -7) {
                util.showerrortips(errorEle, data.msg);
                document.getElementById(phoneCodeEle).value = "";
            } else if (data.code == -10) {
                util.showerrortips(errorEle, data.msg);
            } else if (data.code == -13) {
                util.showerrortips(errorEle, data.msg);
            } else if (data.code == -20) {
                util.showerrortips(errorEle, data.msg);
            }
            $(".next-mars").hide();
        }, "json");
    }
};
$(function () {
    $(".btn-imgcode").on(
        "click",
        function () {
            this.src = "/servlet/ValidateImageServlet?r="
                + Math.round(Math.random() * 100);
        });
    $('#bindgoogle').on("show.bs.modal", function () {
        security.loadgoogleAuth();
    });
    $("#bindemail-Btn").on("click", function () {
        var email = $("#bindemail-email").val();
        security.submitValidateEmailForm(email, false);
    });
    $("#bindemail-send").on("click", function () {
        var email = $("#bindemail-send-email").val();
        security.submitValidateEmailForm(email, true);
    });
    $("#unbindphone-areaCode").on("change", function () {
        security.areaCodeChange(this, "unbindphone-newphone-areacode");
    });
    $(".btn-sendmsg").on(
        "click",
        function () {
            // debugger;
            if (this.id == "unbindphone-newsendmessage") {
                var areacode = $("#unbindphone-newphone").intlTelInput("getSelectedCountryData").dialCode
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
                msg.sendMsgCode($(this).data().msgtype,$(this).data().tipsid, this.id, areacode, phone,null, sendVoice);
            } else if (this.id == "bindphone-sendmessage") {
                // ****************77777777
                var phone = $("#bindphone-newphone").val();
                var sendVoice = $("#sendVoice").val();
                msg.sendMsgCode($(this).data().msgtype,
                    $(this).data().tipsid, this.id, areacode, phone,
                    null, sendVoice);
            } else {
                var sendVoice = $("#sendVoice").val();
                msg.sendMsgCode($(this).data().msgtype,
                    $(this).data().tipsid, this.id, null, null, null,
                    sendVoice);
            }
        });
    $("#unbindphone-Btn").on("click", function () {
        security.submitValidatePhoneForm(1);
    });
    $("#bindphone-areaCode").on("change", function () {
        security.areaCodeChange(this, "bindphone-newphone-areacode");
    });
    $("#bindphone-Btn").on("click", function () {
        security.submitBindPhone(0);
    });
    $("#bindgoogle-Btn").on("click", function () {
        security.submitbindGoogleForm();
    });
    $("#unbindgoogle-Btn").on("click", function () {
        security.submitlookbindGoogleForm();
    });
    $("#unbindloginpass-Btn").on("click", function () {
        security.submitPwdForm(0, true);
    });
    $("#unbindtradepass-Btn").on("click", function () {
        security.submitPwdForm(1, true);
    });
    $("#bindtradepass-Btn").on("click", function () {
        security.submitPwdForm(1, false);
    });
});


function deleteCookie(name, path) {
    /**根据cookie的键，删除cookie，其实就是设置其失效**/
    var name = escape(name);
    var expires = new Date(0);
    path = path == "" ? "" : ";path=" + path;
    document.cookie = name + "=" + ";expires=" + expires.toUTCString() + path;
};