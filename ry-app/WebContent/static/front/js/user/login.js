var login = {
	loginNameOnblur : function() {
		var uName = document.getElementById("login-account").value;
		if (!util.checkEmail(uName) && !util.checkMobile(uName)) {
			util.layerTips("login-account", language["comm.error.tips.1"]);
		}
	},
	checkLoginUserName : function() {
		var uName = document.getElementById("login-account").value;
		if (uName == "") {
			util.layerTips("login-account", language["comm.error.tips.5"]);
			return false;
		} else if (!util.checkEmail(uName) && !util.checkMobile(uName)) {
			util.layerTips("login-account", language["comm.error.tips.1"]);
			return false;
		}
		util.hideerrortips("login-errortips");
		return true;
	},
	checkLoginPassword : function() {
		var password = document.getElementById("login-password").value;
		if (password == "") {
			util.layerTips("login-password", language["comm.error.tips.2"]);
			return false;
		} else if (password.length < 6) {
			util.layerTips("login-password", language["comm.error.tips.3"]);
			return false;
		}
		util.hideerrortips("login-errortips");
		return true;
	},
	loginSubmit : function() {
		if (login.checkLoginUserName() && login.checkLoginPassword()) {
			var url = "/user/login/index.html?random=" + Math.round(Math.random() * 100);
			var uName = document.getElementById("login-account").value;
			var pWord = document.getElementById("login-password").value;
			/*var imgCode = document.getElementById("login-imgCode").value;*/
			var longLogin = 0;
			/*if (imgCode == "") {
				util.layerTips("login-imgCode", language["comm.error.tips.28"]);
				return false;
			}*/
			if (util.checkEmail(uName)) {
				longLogin = 1;
			}
			var forwardUrl = "";
			if (document.getElementById("forwardUrl") != null) {
				forwardUrl = document.getElementById("forwardUrl").value;
			}
			var param = {
				loginName : uName,
				password : pWord,
				type : longLogin
				/*imgCode : imgCode*/
			};
            verify(function(){
               $.post(url, param, function(data) {
                   if (data.code < 0) {
                       if(data.code == -2){
                           $("#login-password").val("");
                           util.layerTips("login-password", data.msg);
                       }else if (data.code == -3) {
                           util.layerTips("login-imgCode", data.msg);
                       }else{
                           util.layerTips("login-account", data.msg);
                       }
                   } else {
                       if (forwardUrl.trim() == "") {
                           window.location.href = "/index.html";
                       } else {
                           window.location.href = forwardUrl;
                       }
                   }
               }, "json");
           });
        }
    }
};
$(function() {
	$("#login-password").on("focus", function() {
		login.loginNameOnblur();
		util.callbackEnter(login.loginSubmit);
	});
	$("#login-submit").on("click", function() {
		login.loginSubmit();
	});
});