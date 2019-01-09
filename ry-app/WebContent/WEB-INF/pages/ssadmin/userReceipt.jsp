<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>个人收款账户</title>
</head>
<script src="http://libs.baidu.com/jquery/2.0.0/jquery.min.js"></script>
<script type="text/javascript"
		src="${oss_url}/static/front/js/plugin/ajaxfileupload.js"></script>
	<script type="text/javascript"
		src="${oss_url}/static/front/js/plugin/fileCheck.js"></script>
	<script type="text/javascript"
		src="${oss_url}/static/front/js/comm/msg.js"></script>
	<script type="text/javascript"
		src="${oss_url}/static/front/js/finance/city.min.js"></script>
	<script type="text/javascript"
		src="${oss_url}/static/front/js/finance/jquery.cityselect.js"></script>
	<script type="text/javascript"
		src="${oss_url}/static/front/js/plugin/jquery.qrcode.min.js"></script>
	<script type="text/javascript"
		src="${oss_url}/static/front/js/plugin/jquery.autocomplete.min.js"></script>
	<script type="text/javascript"
		src="${oss_url}/static/front/js/user/kyc.js"></script>
	<script type="text/javascript"
		src="${oss_url}/static/front/js/user/kyct.js"></script>
<body>
	<form id="formId" action="/otc/userReceipt.html" method="post">
		id：<input type="text" id="fid" value="" name="fid"/><br/>
		时间：<input type="text" id="fCreateTime" value="" name="fCreateTime"/><br/>
		用户账户id：<input type="text" id="fuser_id" value="" name="fusr_id"/><br/>
		收款类型：
		<input type="radio" checked="checked" value="0" name="ftype"/>银行卡
		<input type="radio"  value="1" name="ftype"/>支付宝
		<input type="radio"  value="2" name="ftype"/>微信<br/>
		null：<input type="text" id="fname" value="" name="fname"/><br/>
		收款账户：<input type="text" id="faccount" value="" name="faccount"/><br/>
		收款账户二维码：
		<div>
			<input id="pic1" type="file" onchange="uploadImg()">
			<input id="pic1Url" type="hidden" value="" name="fimgurl"> 
			<span class="s2"> 
				<span>未选择文件</span> 
				<em class="pic1name"></em>
			</span>
		</div><br/>
		银行名称：<input type="text" id="fbankname" value="" name="fbankname"/><br/>
		银行支行名称：<input type="text" id="fbanknamez" value="" name="fbanknamez"/><br/>
		<input type="button" id="btn" value="提交"/> 
	</form>
</body>
	<script>
		var flag = false;
		function uploadImg() {
			alert("进来了4544");
		    if (checkFileType('pic1', 'img')) {
		    	alert("怎么了");
		        fileUpload("/otc/upload.html", "4", "pic1", "pic1Url", null, null, imgbakc, "resultUrl");
		    }
		}
		
		function imgbakc(resultUrl) {
			flag = true;
			alert("到imgslsld方法中："+resultUrl);
			$("#pic1Url").val(resultUrl);
		}
		$(function(){
			$("#btn").click(function(){
				console.log("flag="+flag);
				//防止图片没有上传就提交了
				if(flag){
					
					var fid = $("#fid").val();//id
					var fuser_id = $("#fuser_id").val();//用户账户id
					var faccount = $("#faccount").val();//收款账户
					var pic1Url = $("#pic1Url").val();//收款二维码
					var fbankname = $("#fbankname").val();
					var fbanknamez = $("#fbanknamez").val();
					
					if(fid == ""){
						alert("请填写id");
						return;
					}
					if(fuser_id == ""){
						alert("请填写账户id");
						return;
					}else{
						
						$.ajax({
							url:"",
							type:"post",
							data:{
								fuser_id : fuser_id
							},
							success:function(data){
								
							}
						});
						
					}
					if(faccount == ""){
						alert("请填写收款账户");
						return;
					}
					if(fbankname == ""){
						alert("请填写银行名称");
						return;
					}
					if(fbanknamez == ""){
						alert("请填写银行支行名称");
						return;
					}
					
					$("#formId").submit();
					flag = false;
				}else{
					alert("请上传二维码图片");
				}
			});
		})
	</script>
</html>












