<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../comm/include.inc.jsp"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>

<!doctype html>
<html>
<head> 
<base href="${basePath}"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@include file="../comm/link.inc.jsp" %>

<link rel="stylesheet" href="${oss_url}/static/front/css/user/reset.css" type="text/css"></link>
</head>
<body>
<%@include file="../comm/header.jsp" %>
	<div class="container-full main-con">
		<div class="container reset">
		<div class="row reset-box" style="margin-top: 100px !important;">
            <div class="col-xs-12">
                <span class="reset-title">您正通过
                    <span>电子邮件</span>找回登录密码
                </span>
            </div>
			<div class="col-xs-12 ">
				<span class="reset-process">
					<span id="resetprocess1" class="col-xs-3 col-xs-offset-3 active">
						<span class="reset-process-line"></span>
						<span class="reset-process-icon">1</span>
						<span class="reset-process-text">填写账户名</span>
					</span>
					<span id="resetprocess2" class="col-xs-3 ">
						<span class="reset-process-line"></span>
						<span class="reset-process-icon">2</span>
						<span class="reset-process-text">设置登录密码</span>
					</span>
					<span id="resetprocess3" class="col-xs-3">
						<span class="reset-process-line"></span>
						<span class="reset-process-icon">3</span>
						<span class="reset-process-text">成功</span>
					</span>
				</span>
			</div>
			<div class="col-xs-12 reset padding-top-30">
				<div class="col-xs-8 col-xs-offset-2 reset-body">
							<div class="col-xs-9 form-horizontal col-xs-offset-1">
								<div class="form-group ">
									<label for="reset-email" class="col-xs-4 control-label">邮箱地址</label>
									<div class="col-xs-8">
										<input id="reset-email" class="form-control" type="text">
									</div>
								</div>
								<div class="form-group ">
									<label for="reset-idcard" class="col-xs-4 control-label">证件类型</label>
									<div class="col-xs-8">
										<select class="form-control" id="reset-idcard">
											<option value="1">身份证</option>
										</select>
									</div>
								</div>
								<div class="form-group ">
									<label for="reset-idcardno" class="col-xs-4 control-label">证件号码</label>
									<div class="col-xs-8">
										<input id="reset-idcardno" class="form-control" type="text" placeholder="如果账户未实名认证此项可不填写">
									</div>
								</div>
								<div class="form-group ">
									<label for="reset-imgcode" class="col-xs-4 control-label">验证码</label>
									<div class="col-xs-8">
										<input id="reset-imgcode" class="form-control" type="text">
										<img class="btn btn-imgcode" src="/servlet/ValidateImageServlet?r=1473326869382"></img>
									</div>
								</div>
								<div class="form-group">
									<label for="reset-errortips" class="col-xs-4 control-label"></label>
									<div class="col-xs-8">
										<span id="reset-errortips" class="text-danger"></span>
									</div>
								</div>
								<div class="form-group ">
									<label for="btn-next" class="col-xs-4 control-label"></label>
									<div class="col-xs-8">
										<button id="btn-email-next" class="btn btn-danger btn-block">提交</button>
									</div>
								</div>
							</div>
						
					
				</div>
			</div>
		</div>
	</div>
</div>

<%@include file="../comm/footer.jsp" %>	
	<script type="text/javascript" src="${oss_url}/static/front/js/comm/msg.js"></script>
	<script type="text/javascript" src="${oss_url}/static/front/js/user/reset.js"></script>
</body>
</html>
