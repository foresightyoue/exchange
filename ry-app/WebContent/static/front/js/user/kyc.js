var flag =true;
var security = {
    submitRealCertificationForm: function () {
    	if(!flag) return;
		flag =false;
        var realname = document.getElementById("bindrealinfo-realname").value;
        var address = document.getElementById("bindrealinfo-address").value;
        var identitytype = document.getElementById("bindrealinfo-identitytype").value;
        var identityno = document.getElementById("bindrealinfo-identityno").value;
        var ckinfo = document.getElementById("bindrealinfo-ckinfo").checked;
        var desc = '';
        // 验证是否同意
        if (!ckinfo) {
            desc = language["certification.error.tips.1"];
            util.showerrortips('certificationinfo-errortips', desc);
            flag =true;
            return;
        }
        //验证姓名
        if (realname.length > 32 || realname.trim() == "") {
            desc = language["certification.error.tips.2"];
            util.showerrortips('certificationinfo-errortips', desc);
            flag =true;
            return;
        }
        // 验证证件类型
        if (identitytype != 0) {
            desc = language["certification.error.tips.3"];
            util.showerrortips('certificationinfo-errortips', desc);
            flag =true;
            return;
        }
        // 验证身份证
        var isIDCard = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
        var re = new RegExp(isIDCard);
        if (!re.test(identityno)) {
            desc = language["certification.error.tips.4"];
            util.showerrortips('certificationinfo-errortips', language["comm.error.tips.119"]);
            flag =true;
            return false;
        }
        // 隐藏错误消息
        util.hideerrortips('certificationinfo-errortips');
        // 提交信息

        var url = "/user/validateIdentity.html?random=" + Math.round(Math.random() * 100);
        var param = {
            realName: realname,
            identityType: identitytype,
            identityNo: identityno
        };
        jQuery.post(url, param, function (data) {
        	if (data.code == 0) {
				util.showerrortips('certificationinfo-errortips', data.msg);
				window.location.reload(false);
			} else {
				util.showerrortips('certificationinfo-errortips', data.msg);
				flag =true;
			}
        }, "json");
    },
    submitInfo: function () {
        var identityUrlOn = $('#pic1Url').val();
        var identityUrlOff = $('#pic2Url').val();
        var identityHoldUrl = $('#pic3Url').val();

        if (!identityUrlOn) {
            util.showerrortips("error", '请上传身份证正面照片');
            return false;
        }

        if (!identityUrlOff) {
            util.showerrortips("error", '请上传身份证反面照片');
            return false;
        }

        if (!identityHoldUrl) {
            util.showerrortips("error", '请上传手持身份证照片');
            return false;
        }

        var parma = {
            identityUrlOn: identityUrlOn,
            identityUrlOff: identityUrlOff,
            identityHoldUrl: identityHoldUrl
        }

        $.post("/user/validateKyc.html", parma, function (data, textStatus, jqXHR) {

            if (data.code == 0) {
                setTimeout("location.reload()", 500)
            }
            util.showerrortips("error", data.msg);
        }, "json");
    },
    //提交上传图片的功能
    submitInfo1: function(){
    	var identityUrlOn = $('#pic1Url').val();
        var identityUrlOff = $('#pic2Url').val();
        var identityHoldUrl = $('#pic3Url').val();
        
        if (!identityUrlOn) {
            util.showerrortips("error", '请上传身份证正面照片');
            return false;
        }
        if (!identityUrlOff) {
            util.showerrortips("error", '请上传身份证反面照片');
            return false;
        }
        if (!identityHoldUrl) {
            util.showerrortips("error", '请上传手持身份证照片');
            return false;
        }
        var parma = {
                identityUrlOn: identityUrlOn,
                identityUrlOff: identityUrlOff,
                identityHoldUrl: identityHoldUrl
        }
        $.post("/user/validateKyc.html", parma, function (data, textStatus, jqXHR) {

            if (data.code == 0) {
                setTimeout("location.reload()", 500);
                //setTimeout("loginSuc()",3000);
            	//window.location.href="/m/regSuc.html";
            	//util.showerrortips('certificationinfo-errortips',data.msg);
            }
            util.showerrortips("error", data.msg);
        }, "json");
    },
    //提交上传图片的功能
    submitInfo2: function(){
    	var identityUrlOn = $('#picBank1Url').val();
    	var identityUrlOff = $('#picBank2Url').val();
    	var identityHoldUrl = $('#picBank3Url').val();
    	if (!identityUrlOn) {
            util.showerrortips("error", '请上传身份证正面照片');
            return false;
        }
        if (!identityUrlOff) {
            util.showerrortips("error", '请上传身份证反面照片');
            return false;
        }
        if (!identityHoldUrl) {
            util.showerrortips("error", '请上传手持身份证照片');
            return false;
        }
        var param = {
                identityUrlOn: identityUrlOn,
                identityUrlOff: identityUrlOff,
                identityHoldUrl: identityHoldUrl
        }
        $.post("/user/validateKyc1.html",param,function(data,textStatus,jqXHR){
        	if (data.code == 0) {
                //setTimeout("location.reload()", 500);
                setTimeout("loinPage()",3000);
            	//window.location.href="/m/regSuc.html";
            	//util.showerrortips('certificationinfo-errortips',data.msg);
            }
            util.showerrortips("error", data.msg);
        	
        },"json");
    	
    },
    submitInfo3: function () {
        var identityUrlOn = $('#picBank1Url').val();
        var identityUrlOff = $('#picBank2Url').val();
        var identityHoldUrl = $('#picBank3Url').val();

        if (!identityUrlOn) {
            util.showerrortips("error", '请上传银行卡正面照片');
            return false;
        }

        if (!identityUrlOff) {
            util.showerrortips("error", '请上传本人手持银行卡照片');
            return false;
        }

        if (!identityHoldUrl) {
            util.showerrortips("error", '请上传银行卡授权书照片');
            return false;
        }

        var parma = {
            identityUrlOn: identityUrlOn,
            identityUrlOff: identityUrlOff,
            identityHoldUrl: identityHoldUrl
        }

        $.post("/user/validateKyc1.html", parma, function (data, textStatus, jqXHR) {

            if (data.code == 0) {
                
                setTimeout("location.reload()", 500);
                
            }
            util.showerrortips("error", data.msg);
        }, "json");
    },
  //提交上传图片的功能
    submitInfo4: function(){
    	var identityUrlOn = $('#pic1Url').val();
        var identityUrlOff = $('#pic2Url').val();
        var identityHoldUrl = $('#pic3Url').val();
        
        if (!identityUrlOn) {
            util.showerrortips("error", '请上传身份证正面照片');
            return false;
        }
        if (!identityUrlOff) {
            util.showerrortips("error", '请上传身份证反面照片');
            return false;
        }
        if (!identityHoldUrl) {
            util.showerrortips("error", '请上传手持身份证照片');
            return false;
        }
        var parma = {
                identityUrlOn: identityUrlOn,
                identityUrlOff: identityUrlOff,
                identityHoldUrl: identityHoldUrl
        }
        $.post("/user/validateKyc.html", parma, function (data, textStatus, jqXHR) {

            if (data.code == 0) {
                //setTimeout("location.reload()", 500);
                //setTimeout("loginSuc()",3000);
            	window.location.href="/m/regSuc.html";
            	//util.showerrortips('certificationinfo-errortips',data.msg);
            }
            util.showerrortips("error", data.msg);
        }, "json");
    },
}

$(function () {

    //实名认证
    $("#bindrealinfo-Btn").on("click", function () {
        security.submitRealCertificationForm(false);
    });

    $('#withdrawCnyAddrBtn').on('click', function () {
        security.submitWithdrawCnyAddress();
    });


    $(".btn-sendmsg").on("click", function () {
        if (this.id == "unbindphone-newsendmessage") {
            var areacode = $("#unbindphone-newphone-areacode").html();
            var phone = $("#unbindphone-newphone").val();
            if (phone == "") {
                util.showerrortips("unbindphone-errortips", language["comm.error.tips.6"]);
                return;
            }
            msg.sendMsgCode($(this).data().msgtype, $(this).data().tipsid, this.id, areacode, phone);
        } else if (this.id == "unbindbank-megbtn") {
            var areacode = $("#bindphone-newphone-areacode").html();
            var phone = $("#unbindBank-phone").val();
            msg.sendMsgCode($(this).data().msgtype, $(this).data().tipsid, this.id, areacode, phone);

        } else if (this.id == "bindphone-sendmessage") {
            var areacode = $("#bindphone-newphone-areacode").html();
            var phone = $("#bindphone-newphone").val();
            msg.sendMsgCode($(this).data().msgtype, $(this).data().tipsid, this.id, areacode, phone);

        } else if (this.id == "bindsendmessage") {
            var areacode = '86';
            var phone = $("#dialogPhone").val();
            msg.sendMsgCode($(this).data().msgtype, $(this).data().tipsid, this.id, areacode, phone);

        } else if (this.id == "retrievePassword-sendmessage") {
            var imgcode = $('#retrievePassword-imgpwd').val();
            msg.sendMsgCode($(this).data().msgtype, $(this).data().tipsid, this.id, areacode, phone, imgcode);
        } else {
            msg.sendMsgCode($(this).data().msgtype, $(this).data().tipsid, this.id);
        }
    });

    //提交
    $('.to_up .s1').on('click', function () {
        security.submitInfo();
    });
    //提交
    $('.to_up .s3').on('click',function(){
    	 security.submitInfo1();
    	 $(".kyc_02").hide();
    	 $(".kyc_03").show();
    });
    $('.to_up1 .s4').on('click',function(){
    	security.submitInfo3();
    })
    $('.to_up1 .s3').on('click',function(){
    	security.submitInfo2();
    })
    $('.to_up .s5').on('click',function(){
    	security.submitInfo4();
    })
})



//上传图片
function uploadImg1() {
    if (checkFileType('pic1', 'img')) {
        fileUpload("/common/upload.html", "4", "pic1", "pic1Url", null, null, imgbakc1, "resultUrl");
    }
}

function imgbakc1(resultUrl) {
    $(".pic1show").css("background-image", "url(" + resultUrl + ")");
    $('label[for="pic1"]').text('重新上传');
    $('.pic1name').text($('#pic1').val().split('\\').pop())
        .siblings().text('已选择');
}

function uploadImg2() {
    if (checkFileType('pic2', 'img')) {
        fileUpload("/common/upload.html", "4", "pic2", "pic2Url", null, null, imgbakc2, "resultUrl");
    }
}

function imgbakc2(resultUrl) {
    $(".pic2show").css("background-image", "url(" + resultUrl + ")");
    $('label[for="pic2"]').text('重新上传');
    $('.pic2name').text($('#pic2').val().split('\\').pop())
       .siblings().text('已选择');
}

function uploadImg3() {
    if (checkFileType('pic3', 'img')) {
        fileUpload("/common/upload.html", "4", "pic3", "pic3Url", null, null, imgbakc3, "resultUrl");
    }
}

function imgbakc3(resultUrl) {
    $(".pic3show").css("background-image", "url(" + resultUrl + ")");
    $('label[for="pic3"]').text('重新上传');
    $('.pic3name').text($('#pic3').val().split('\\').pop())
       .siblings().text('已选择');
}

function uploadImg4(){
    if(checkFileType('pic4','img')){
          fileUpload("/ssadmin/upload1.html","4","pic4","pic4Url",null,null,imgbakc4,"resultUrl");
    }
}
function imgbakc4(resultUrl) {
    $(".pic4show").css("background-image", "url(" + resultUrl + ")");
    $('label[for="pic4"]').text('重新上传');
    $('.pic4name').text($('#pic4').val().split('\\').pop())
       .siblings().text('已选择');
}
function loginSuc(){
	//window.location.href="/m/realbackId.html";
	$(".bankid").show();
	$(".idten").hide();
	$(".to_up1").show();
	$(".to_up").hide();
	$(".kyc_02").hide();
}

function loinPage(){
   window.location.href="/m/regSuc.html";
}
