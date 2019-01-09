<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../../front/comm/include.inc.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>

<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="../comm/header.jsp"></jsp:include>
		<meta charset="utf-8" />
		<meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no, width=device-width">
		<meta name="format-detection" content="telephone=no">
		<meta name="format-detection" content="email=no">
		<base href="${basePath}"/>
		<title>实名认证</title>
		<link rel="stylesheet" href="/static/front/app/css/css.css" />
		<link rel="stylesheet" href="/static/front/app/css/style.css" />
		<script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js" ></script>
		<script type="text/javascript" src="/static/front/app/js/swiper-3.4.2.jquery.min.js" ></script>
	</head>
	<style>
	.information{ 
	    padding: 0;
	}
	.skip{
		width: 3rem;
	    height: 3.06rem;
	    position: absolute;
	    top: -3.6rem;
	    right: 0;
	    text-align: center;
	    line-height: 3.06rem;
	    z-index: 100;
	    font-size: 1rem;
	    color: white;
	}   
	</style>
	<body>
		<nav>
			<div class="Personal-title">
			    <span>
			        <a href="javascript:;" onClick="javascript :history.back(-1)">
			            <em>
			                <i></i>
			                <i></i>
			            </em>
			            <strong>返回</strong>
			        </a>
			    </span>实名认证
			</div>
		</nav>
		<section>
			<div class="information">
				<div class="register_cont" style="display: block;">
					<div class="information-cont">
						<ul>
							<li>
								<input type="text" class="phone-code" value="" placeholder="请输入您的姓名" id="bindrealinfo-realname"/>
							</li>
							<li >
								<select name="" id="bindrealinfo-address" >
									<option value="86">中国大陆(China)</option>
									<option value="86">其他地区</option>
								</select>
							</li>
							<li>
								<select name="" id="bindrealinfo-identitytype">
									<option value="0">身份证</option>
									<option value="2">护照</option>
								</select>
							</li>
							<li>
								<input type="text" class="phone-code" value="" placeholder="请输入您的证件号码" id="bindrealinfo-identityno"/>
							</li>
						</ul>
					</div>
					<div class="next next-active">下一步</div>
					<div class="skip">跳过</div>
				</div>
			</div>
		</section>
	</body>
</html>

<script type="text/javascript" src="/static/front/js/comm/util.js"></script>
<script type="text/javascript" src="/static/front/js/language/language_cn.js"></script>
<script type="text/javascript" src="/static/front/js/layer/layer.js"></script>
<script type="text/javascript" src="/static/front/js/comm/comm.js"></script>
<script>

function submitRealCertificationForm () {
    	// debugger;
    	// 姓名
		var realname = document.getElementById("bindrealinfo-realname").value;
		// 地区
		var address = document.getElementById("bindrealinfo-address").value;
		// 身份证，护照
		var identitytype = document.getElementById("bindrealinfo-identitytype").value;
		// 证件号码
		var identityno = document.getElementById("bindrealinfo-identityno").value;
		var desc = '';
		//验证姓名
		if (realname.length > 6 || realname.trim() == "") {
			desc = language["certification.error.tips.2"];
			util.showerrortips('certificationinfo-errortips', desc);
			return;
		}
		// 验证证件类型
		// if (identitytype != 0) {
		// 	desc = language["certification.error.tips.3"];
		// 	util.showerrortips('certificationinfo-errortips', desc);
		// 	return;
		// }
		// 验证身份证
		if(identitytype == 0){
			var isIDCard = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
			var re = new RegExp(isIDCard);
			if (!re.test(identityno)) {
				desc = language["certification.error.tips.4"];
				util.showerrortips('certificationinfo-errortips', language["comm.error.tips.119"]);
				return false;
			}
		}
		// 隐藏错误消息
		util.hideerrortips('certificationinfo-errortips');
		// 提交信息
		
		var url = "/user/validateIdentity.html?random=" + Math.round(Math.random() * 100);
		var param = {
			realName : realname,
			identityType : identitytype,
			identityNo : identityno
		};
		console.log(param,'sssssssss');
		jQuery.post(url, param, function(data) {
			if (data.code == 0) {
				util.showerrortips('certificationinfo-errortips', data.msg);
				//window.location.href = "/m/regSuc.html" ;
				window.location.href="/m/realIdentity.html";
				//window.location.href="/m/realbackId.html";
			} else {
				util.showerrortips('certificationinfo-errortips', data.msg);
			}
		}, "json");

	}
	
	
	$(".next").on('click',function(){
		submitRealCertificationForm();
	});
	$(".skip").on('click',function(){
		var url = "/user/validateIdentity1.html?random=" +Math.round(Math.random() * 100);
		var param = {
		    realName : "admin",
		    identityType:"0",
		    identityNo :"500233199301011414"
		}
		jQuery.post(url,param,function(data){
			if(data.code == 0){
				//util.showerrortips('certificationinfo-errortips',data.msg);
				window.location.href="/m/index.html";
			}else{
				util.showerrortips('certificationinfo-errortips', data.msg);
			}

		},"json");
	})
</script>