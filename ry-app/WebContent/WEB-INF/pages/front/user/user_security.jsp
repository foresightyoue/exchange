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

<link rel="stylesheet" href="${oss_url}/static/front/css/user/user.css" type="text/css"></link>
<script>
    $(function(){
        var security = "${securityEnvironment}";
        var googleCheck = "${sessionScope.login_user.fgoogleCheck}";
        if (security == "true") {
	        $("#unbindloginpass-msgcode").closest('.form-group').hide();
	        $("#unbindtradepass-msgcode").closest('.form-group').hide();
	        $("#unbindphone-sendmessage").closest('.form-group').hide();
	        $("#unbindtradepass-sendmessage").closest('.form-group').hide();
	        $("#bindtradepass-sendmessage").closest('.form-group').hide();
	        $("#bindtradepass-msgcode").closest('.form-group').hide();
	        $("#unbindgoogle-msgcode").closest('.form-group').hide();
        }
        if (googleCheck == "false") {
            $("#unbindtradepass-googlecode").closest('.form-group').hide();
            $("#unbindloginpass-googlecode").closest('.form-group').hide();
            $("#unbindphone-googlecode").closest('.form-group').hide();
        }
    });
</script>
<style type="text/css">
	.now_status{
	    position: relative;
	   
	}
	.now_status img{
	    position: absolute;
	    left: 153px;
        width: 50px;
	}
	#google-status{
		opacity:0;
		filter:alpha(opacity=0); 
	}
	@media (min-width: 768px){
	    .now_status .control-label {
	    text-align: right;
	    margin-bottom: 0;
	    padding-top: 2px;
	}
	
}
	.trade {
	    margin-top: 0 !important;
	}
	#google_status1 .status_details{
	    display: inline-block;
	    width: 70px ;
	}
	.form-horizontal .form-group {
	    margin-top: -12px;
	}
	.modal .form-qrcode-quotes {
	    height: 30px;
	}
	.btn-imgcode {
	    position: absolute;
	    right: 15px;
	    top: 0;
	    outline: none;
	    padding: 0;
	    height: 31px;
	}
</style>
</head>
<body>
	


<%@include file="../comm/header.jsp" %>

 

	<div class="container-full main-con">
		<div class="container displayFlex">
		<div class="row">

<%@include file="../comm/left_menu.jsp" %>
			<div class="col-xs-10 padding-right-clear">
				<div class="col-xs-12 padding-right-clear padding-left-clear rightarea user">
					<div class="col-xs-12 rightarea-con">
						<div class="user-top-icon">
							<div class="col-xs-2 text-center">
								<i class="top-icon"></i>
							</div>
							<div class="col-xs-6 padding-left-clear">
								<div class="h5">
									
										<span class="top-title1">
                                            <c:if test="${fuser.frealName == 'admin'}">${loginName}</c:if>
                                            <c:if test="${fuser.frealName != 'admin'}">${fuser.frealName}</c:if>
										</span>
									
									<span class="top-vip vip${level }">VIP${level }</span>
								</div>
								<div>
									<span class="top-title2">UID:${fuser.fid }</span><span class="top-title3">${loginName}</span>
								</div>
							</div>
							<div class="col-xs-4">
								<h5 class="top-title4">
									您已设置 <span class="top-title6"> ${bindType } </span> 个保护项，还有 <span class="top-title6">${5-bindType } </span> 个保护项可设置
								</h5>
								<h5 class="top-title5">本平台实时保护您的账户和资金安全。</h5>
							</div>
						</div>
						<div class="col-xs-12 padding-clear">
							<div class="con-items">
										<div class="title">
											<span>绑定邮箱</span>
											<c:if test="${isBindEmail == false }">
														<a class="text-primary text-link" href="#" data-toggle="modal" data-target="#bindemail">绑定>></a>
													</c:if>
										</div>
										<div class="content">
											<div class="content-lt">
												<span><i class="email ${isBindEmail == true?'':'settting' }"></i></span>
											</div>
											<div class="content-rt">
												<p class="icon-${isBindEmail == false?'no':'ok' }">${isBindEmail == false?'未绑定':'已绑定' }</p>
												<c:if test="${isBindEmail == true}">
												<p>您绑定的邮箱为：</p>
												<p>${email}</p>
												</c:if>
											</div>
										</div>
									
									
									
								
							</div>
							<div class="con-items center">


										<div class="title">
											<span>绑定手机</span>
									        <c:if test="${isBindTelephone == false }">
												<a class="text-link" href="javascript:void(0)" data-toggle="modal" data-target="#bindphone">绑定&gt;&gt;</a>
											</c:if>
											<c:if test="${isBindTelephone == true}">
											<a class="text-link" href="javascript:void(0)" data-toggle="modal" data-target="#unbindphone">修改&gt;&gt;</a>
											</c:if>
										</div>
										<div class="content">
											<div class="content-lt">
												<span><i class="phone ${isBindTelephone == true?'':'settting' }"></i></span>
											</div>

											<div class="content-rt">
												<p class="icon-${isBindTelephone == false?'no':'ok' }">${isBindTelephone == false?'未绑定':'已绑定' }</p>
												<c:if test="${isBindTelephone == true}">
												<p>您绑定的手机为：</p>
												<p>${telNumber}</p>
												</c:if>
											</div>

										</div>

							</div>
							<div class="con-items">


										<div class="title">
											<span>绑定谷歌验证码</span>
									        <c:choose>
											<c:when test="${isBindGoogle }">
											<a class="text-link unbindgoogle" href="javascript:void(0)" data-toggle="modal" data-target="#unbindgoogle">查看&gt;&gt;</a>
											</c:when>
											<c:otherwise>
											<a class="text-link" href="javascript:void(0)" data-toggle="modal" data-target="#bindgoogle">绑定&gt;&gt;</a>
											</c:otherwise>
											</c:choose>

										</div>
										<div class="content">
											<div class="content-lt">
												<span><i class="google ${isBindGoogle == true?'':'settting' }"></i></span>
											</div>
											<div class="content-rt" id="google_status1">
												<p class="status_details icon-${isBindGoogle == false?'no':'ok' }">${isBindGoogle == false?'未绑定':'已绑定' }</p>
												<p class="status_details icon-${fGoogleCheck == false?'no':'ok' }">${fGoogleCheck == true?'已启用':'已停用' }</p>
												<p>提现、修改密码及安全设置时需要输入谷歌验证码。</p>
											</div>
										</div>



							</div>
							<div class="con-items">
								
									
										<div class="title">
											<span>登录密码</span>
											<a class="text-link" href="javascript:void(0)" data-toggle="modal" data-target="#unbindloginpass">修改&gt;&gt;</a>
										</div>
										<div class="content">
											<div class="content-lt">
												<span><i class="login"></i></span>
											</div>
											<div class="content-rt">
												<p class="icon-ok">已设置</p>
												<p>登录${requestScope.constant['webName']}时使用。</p>
											</div>
										</div>
									
									
								
							</div>
							<div class="con-items center">
								
									
										<div class="title">
											<span>交易密码</span>
											<c:if test="${isTradePassword == false }">
												<a class="text-link" href="javascript:void(0)" data-toggle="modal" data-target="#bindtradepass">设置>></a>
											</c:if>
											<c:if test="${isTradePassword == true }">
											    <a class="text-link" href="javascript:void(0)" data-toggle="modal" data-target="#unbindtradepass">修改&gt;&gt;</a>
											</c:if>
										</div>
										<div class="content">
											<div class="content-lt">
												<span><i class="trade ${isTradePassword == true?'':'settting' }"></i></span>
											</div>
											<div class="content-rt">
												<p class="icon-${isTradePassword == false?'no':'ok' }">${isTradePassword == false?'未设置':'已设置' }</p>
												<p>账户资金变动时，需先验证交易密码。</p>
											</div>
										</div>
									
									
								
							</div>
							<div class="con-items">
                                        <div class="title">
                                            <span>绑定身份证</span>
                                            <c:if test="${fuser.frealName == 'admin' }">
                                                        <a class="text-primary text-link" href="#" data-toggle="modal" data-target="#bindrealinfo">绑定>></a>
                                                    </c:if>
                                        </div>
                                        <div class="content">
                                            <div class="content-lt">
                                                <span><i class="realname ${fuser.frealName !='admin' ?'':'settting' }"></i></span>
                                            </div>
                                            <div class="content-rt">
                                                <p class="icon-${fuser.frealName =='admin'?'no':'ok' }">${fuser.frealName == 'admin'?'未绑定':'已绑定' }</p>
                                                <c:if test="${fuser.frealName !='admin'}">
                                                <p>您的账号已通过实名认证。</p>
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
	
	<!-- 修改绑定手机 -->
	<c:if test="${isBindTelephone ==true}">
			<div class="modal modal-custom fade" id="unbindphone" tabindex="-1" role="dialog">
				<div class="modal-dialog" role="document">
					<div class="modal-mark"></div>
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close btn-modal" data-dismiss="modal" aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
							<span class="modal-title">修改绑定手机</span>
						</div>
						<div class="modal-body form-horizontal">
							<div class="form-group text-center">
								<span class="modal-info-tips">
									您正在为账户
									<span class="text-danger">${loginName}</span>
									修改绑定手机
								</span>
							</div>
							<div class="form-group ">
								<label for="unbindphone-phone" class="col-xs-3 control-label">原手机号码</label>
								<div class="col-xs-6">
									<span id="unbindphone-phone" class="form-control border-fff" type="text">${telNumber}</span>
								</div>
							</div>
							<div class="form-group ">
								<label for="unbindphone-msgcode" class="col-xs-3 control-label">短信验证码</label>
								<div class="col-xs-6">
									<input id="unbindphone-msgcode" class="form-control" type="text">
									<button id="unbindphone-sendmessage" data-msgtype="3" onclick="bsendVoice1()" data-tipsid="unbindphone-errortips" class="btn btn-sendmsg">发送验证码</button>
								</div>
								<!-- <div class="ybindVoice">
								   <input name="ybindcode" checked="checked" type="radio" id="ysms"><label for="ysms">短信</label>
                                    <input name="ybindcode" type="radio" id="yvoice"><label for="yvoice">语音</label>
								</div> -->
							</div>
							<div class="ybindVoice form-group">
		                      <label class="col-xs-3" style="text-align: right;">发送方式</label>
		                      <div class="col-xs-8">
		                        <input name="ybindcode" checked="checked" type="radio" id="ysms"><label for="ysms">短信</label>
                                <input name="ybindcode" type="radio" id="yvoice" style="margin-left: 20px;"><label for="yvoice">语音</label>
		                      </div>
		                   </div>
							<div class="form-group ">
								<label for="unbindphone-areaCode" class="col-xs-3 control-label">所在地</label>
								<div class="col-xs-6">
									<select class="form-control" id="unbindphone-areaCode">
										<option value="86">中国大陆(China)</option>
									</select>
								</div>
							</div>
							<div class="form-group ">
								<label for="unbindphone-newphone" class="col-xs-3 control-label">更换手机号码</label>
								<div class="col-xs-6">
									<span id="unbindphone-newphone-areacode" class="btn btn-areacode">+86</span>
									<input id="unbindphone-newphone" class="form-control padding-left-92" type="text">
								</div>
							</div>
						
						<c:if test="${isBindTelephone }">		
							<div class="form-group ">
								<label for="unbindphone-newmsgcode" class="col-xs-3 control-label">短信验证码</label>
								<div class="col-xs-6">
									<input id="unbindphone-newmsgcode" class="form-control" type="text">
									<button id="unbindphone-newsendmessage" onclick="bsendVoice()" data-msgtype="2" data-tipsid="unbindphone-errortips" class="btn btn-sendmsg">发送验证码</button>
								</div>
								<!-- <div class="bindVoice">
								    <input name="bindcode" checked="checked" type="radio" id="bsms"><label for="bsms">短信</label>
                                    <input name="bindcode" type="radio" id="bvoice"><label for="bvoice">语音</label>
								</div> -->
							</div>
							<div class="bindVoice form-group">
                              <label class="col-xs-3" style="text-align: right;">发送方式</label>
                              <div class="col-xs-8">
                                <input name="bindcode" checked="checked" type="radio" id="bsms"><label for="bsms">短信</label>
                                <input name="bindcode" type="radio" id="bvoice" style="margin-left: 20px;"><label for="bvoice">语音</label>
                              </div>
                           </div>
						</c:if>	
						
						<c:if test="${isBindGoogle ==true}">	
								<div class="form-group">
									<label for="unbindphone-googlecode" class="col-xs-3 control-label">谷歌验证码</label>
									<div class="col-xs-6">
										<input id="unbindphone-googlecode" class="form-control" type="text">
									</div>
								</div>
						</c:if>		
							
							<div class="form-group ">
								<label for="unbindphone-imgcode" class="col-xs-3 control-label">验证码</label>
								<div class="col-xs-6">
									<input id="unbindphone-imgcode" class="form-control" type="text">
									<img class="btn btn-imgcode" src="/servlet/ValidateImageServlet?r=<%=new java.util.Date().getTime() %>"></img>
								</div>
							</div>
							<div class="form-group">
								<label for="unbindphone-errortips" class="col-xs-3 control-label"></label>
								<div class="col-xs-6">
									<span id="unbindphone-errortips" class="text-danger"></span>
								</div>
							</div>
							<div class="form-group">
								<label for="unbindemail-Btn" class="col-xs-3 control-label"></label>
								<div class="col-xs-6">
									<button id="unbindphone-Btn" class="btn btn-danger btn-block">确定提交</button>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
	</c:if>	
	
	
		
		
			<!-- 谷歌查看 -->
			<c:if test="${isBindGoogle ==true}">
			<div class="modal modal-custom fade" id="unbindgoogle" tabindex="-1" role="dialog">
				<div class="modal-dialog" role="document">
					<div class="modal-mark"></div>
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close btn-modal" data-dismiss="modal" aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
							<span class="modal-title"></span>
						</div>
						<div class="modal-body form-horizontal">
							<div class="form-group unbindgoogle-hide-box" style="display:none">
								<div class="col-xs-12 clearfix">
									<div id="unbindgoogle-code" class="form-control border-fff ">
										<div class="form-qrcode" id="unqrcode"></div>
									</div>
								</div>
							</div>
							<div class="form-group unbindgoogle-hide-box" style="display:none">
								<label for="unbindgoogle-key" class="col-xs-3 control-label">密匙</label>
								<div class="col-xs-6">
									<span id="unbindgoogle-key" class="form-control border-fff"></span>
								</div>
							</div>
							<div class="form-group unbindgoogle-hide-box" style="display:none">
								<label for="unbindgoogle-device" class="col-xs-3 control-label">设备名称</label>
								<div class="col-xs-6">
									<span id="unbindgoogle-device" class="form-control border-fff">${device_name }</span>
								</div>
							</div>
							<div>
							 
							</div>
							<div class="form-group unbindgoogle-show-box ">
								<label for="unbindgoogle-topcode" class="col-xs-3 control-label">谷歌验证码</label>
								<div class="col-xs-6">
									<input id="unbindgoogle-topcode" ${fGoogleCheck ==false ? 'readonly' : '' } class="form-control" type="text">
								</div>
							</div>
							<c:if test="${isBindTelephone}">
                                <div class="form-group">
                                    <label for="unbindgoogle-msgcode" class="col-xs-3 control-label">短信验证码</label>
                                    <div class="col-xs-6">
                                        <input id="unbindgoogle-msgcode" class="form-control" type="text">
                                        <button id="unbindgoogle-sendmessage" data-msgtype="7" data-tipsid="unbindgoogle-errortips" class="btn btn-sendmsg">发送验证码</button>
                                    </div>
                                </div>
                            </c:if>
							<div class="form-group unbindgoogle-show-box now_status">
							    <label class="col-xs-3 control-label">当前状态</label>
							    <c:if test="${fGoogleCheck ==false }">
								    <img alt="" src="/static/front/images/open.png" style="display: none;">
								    <img alt="" src="/static/front/images/close.png" class="listImg" style="display: block">
							    </c:if>
							    <c:if test="${fGoogleCheck ==true }">
								    <img alt="" src="/static/front/images/open.png" style="display: block;">
								    <img alt="" src="/static/front/images/close.png" class="listImg" style="display: none">
							    </c:if>
							    <input type="checkbox" id="google-status" ${fGoogleCheck ==true ? "checked" : "" }>
							</div>
							<div class="form-group unbindgoogle-show-box">
                                <label for="unbindgoogle-errortips" class="col-xs-3 control-label"></label>
                                <div class="col-xs-6">
                                    <span id="unbindgoogle-errortips" class="text-danger"></span>
                                </div>
                            </div>
							<div class="form-group unbindgoogle-show-box">
								<label for="unbindgoogle-Btn" class="col-xs-3 control-label"></label>
								<div class="col-xs-6">
									<button id="unbindgoogle-Btn" class="btn btn-danger btn-block">确定提交</button>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			</c:if>
	
	
		<!-- 邮箱绑定 -->
		<div class="modal modal-custom fade" id="bindemail" tabindex="-1" role="dialog">
			<div class="modal-dialog" role="document">
				<div class="modal-mark"></div>
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close btn-modal" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<span class="modal-title">绑定邮箱</span>
					</div>
					<div class="modal-body form-horizontal">
						<div class="form-group ">
							<label for="bindemail-email" class="col-xs-3 control-label">邮箱地址</label>
							<div class="col-xs-6">
								<input id="bindemail-email" class="form-control" type="text">
							</div>
						</div>
						<div class="form-group">
							<label for="bindemail-errortips" class="col-xs-3 control-label"></label>
							<div class="col-xs-6">
								<span id="bindemail-errortips" class="text-danger"></span>
							</div>
						</div>
						<div class="form-group">
							<label for="bindemail-Btn" class="col-xs-3 control-label"></label>
							<div class="col-xs-6">
								<button id="bindemail-Btn" class="btn btn-danger btn-block">确定提交</button>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	
	
		
			<!-- 绑定手机 -->
			<div class="modal modal-custom fade" id="bindphone" tabindex="-1" role="dialog">
				<div class="modal-dialog" role="document">
					<div class="modal-mark"></div>
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close btn-modal" data-dismiss="modal" aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
							<span class="modal-title">绑定手机</span>
						</div>
						<div class="modal-body form-horizontal">
							<div class="form-group text-center">
								<span class="modal-info-tips">
									您正在为账户
									<span class="text-danger">${loginName}</span>
									绑定手机
								</span>
							</div>
							<div class="form-group ">
								<label for="bindphone-areaCode" class="col-xs-3 control-label">所在地</label>
								<div class="col-xs-6">
									<select class="form-control" id="bindphone-areaCode">
										<option value="86">中国大陆(China)</option>
									</select>
								</div>
							</div>
							<div class="form-group ">
								<label for="bindphone-newphone" class="col-xs-3 control-label">手机号码</label>
								<div class="col-xs-6">
									<span id="bindphone-newphone-areacode" class="btn btn-areacode">+86</span>
									<input id="bindphone-newphone" class="form-control padding-left-92" type="text">
								</div>
							</div>
							<div class="form-group ">
								<label for="bindphone-msgcode" class="col-xs-3 control-label">短信验证码</label>
								<div class="col-xs-6">
									<input id="bindphone-msgcode" class="form-control" type="text">
									<button id="bindphone-sendmessage" data-msgtype="2" data-tipsid="bindphone-errortips" class="btn btn-sendmsg">发送验证码</button>
								</div>
							</div>
							
							<c:if test="${isBindGoogle ==true}">
							<div class="form-group">
									<label for="bindphone-googlecode" class="col-xs-3 control-label">谷歌验证码</label>
									<div class="col-xs-6">
										<input id="bindphone-googlecode" class="form-control" type="text">
									</div>
								</div>
							</c:if>	
							
							<div class="form-group ">
								<label for="bindphone-imgcode" class="col-xs-3 control-label">验证码</label>
								<div class="col-xs-6">
									<input id="bindphone-imgcode" class="form-control" type="text">
									<img class="btn btn-imgcode" src="/servlet/ValidateImageServlet?r=<%=new java.util.Date().getTime() %>"></img>
								</div>
							</div>
							<div class="form-group">
								<label for="bindphone-errortips" class="col-xs-3 control-label"></label>
								<div class="col-xs-6">
									<span id="bindphone-errortips" class="text-danger"></span>
								</div>
							</div>
							<div class="form-group">
								<label for="bindemail-Btn" class="col-xs-3 control-label"></label>
								<div class="col-xs-6">
									<button id="bindphone-Btn" class="btn btn-danger btn-block">确定提交</button>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		
		
	
	
		
			<!-- 谷歌绑定 -->
			<div class="modal modal-custom fade" id="bindgoogle" tabindex="-1" role="dialog">
				<div class="modal-dialog" role="document">
					<div class="modal-mark"></div>
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close btn-modal" data-dismiss="modal" aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
							<span class="modal-title">绑定谷歌验证器</span>
						</div>
						<div class="modal-body form-horizontal">
							<div class="form-group ">
								<div class="col-xs-12 clearfix">
									<div id="bindgoogle-code" class="form-control border-fff  form-qrcodebox">
										<div class="col-xs-12 clearfix form-qrcode-quotes form-qrcode-quotess"></div>
										<div class="form-qrcodebox-cld">
											<div class="form-qrcode-coded">
												<div class="form-qrcode-title text-center">
													<span>下载谷歌验证器</span>
												</div>
												<div class="form-qrcode">
													<img class="form-qrcode-img" src="${requestScope.constant['googleImages'] }"/>
												</div>
											</div>
											<div class="form-qrcode-tips">
												<span>
													若未安装谷歌验证器请
													<span class="text-danger">扫码下载</span>
													。
												</span>
											</div>
										</div>
										<div class="form-qrcodebox-cld">
											<div class="form-qrcode-codec">
												<div class="form-qrcode-title text-center">
													<span>绑定谷歌验证器</span>
												</div>
												<div class="form-qrcode" id="qrcode">
												</div>
											</div>
											<div class="form-qrcode-tips">
												<span>
													请扫码或手工输入密钥，将手机上生成的
													<span class="text-danger">动态验证码</span>
													填到下边输入框。
												</span>
											</div>
										</div>
										<div class="col-xs-12 clearfix form-qrcode-quotes form-qrcode-quotese"></div>
									</div>
								</div>
							</div>
							<div class="form-group ">
								<label for="bindgoogle-key" class="col-xs-3 control-label">密匙</label>
								<div class="col-xs-7">
									<span id="bindgoogle-key" class="form-control border-fff"></span>
									<input id="bindgoogle-key-hide" type="hidden">
								</div>
							</div>
							<div class="form-group ">
								<label for="bindgoogle-device" class="col-xs-3 control-label">设备名称</label>
								<div class="col-xs-7">
									<span id="bindgoogle-device" class="form-control border-fff">${device_name }</span>
								</div>
							</div>
							<div class="form-group ">
								<label for="bindgoogle-topcode" class="col-xs-3 control-label">谷歌验证码</label>
								<div class="col-xs-7">
									<input id="bindgoogle-topcode" class="form-control" type="text">
								</div>
							</div>
							<div class="form-group">
								<label for="bindgoogle-errortips" class="col-xs-3 control-label"></label>
								<div class="col-xs-7">
									<span id="bindgoogle-errortips" class="text-danger"></span>
								</div>
							</div>
							<div class="form-group">
								<label for="bindgoogle-Btn" class="col-xs-3 control-label"></label>
								<div class="col-xs-7">
									<button id="bindgoogle-Btn" class="btn btn-danger btn-block">确定提交</button>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		
		
	
	<!-- 登录密码修改 -->
	<div class="modal modal-custom fade" id="unbindloginpass" tabindex="-1" role="dialog">
		<div class="modal-dialog" role="document">
			<div class="modal-mark"></div>
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close btn-modal" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<span class="modal-title">修改登录密码</span>
				</div>
				<div class="modal-body form-horizontal">
					<div class="form-group ">
						<label for="unbindloginpass-oldpass" class="col-xs-3 control-label">旧登录密码</label>
						<div class="col-xs-6">
							<input id="unbindloginpass-oldpass" class="form-control" type="password">
						</div>
					</div>
					<div class="form-group ">
						<label for="unbindloginpass-newpass" class="col-xs-3 control-label">新登录密码</label>
						<div class="col-xs-6">
							<input id="unbindloginpass-newpass" class="form-control" type="password">
						</div>
					</div>
					<div class="form-group ">
						<label for="unbindloginpass-confirmpass" class="col-xs-3 control-label">确认新密码</label>
						<div class="col-xs-6">
							<input id="unbindloginpass-confirmpass" class="form-control" type="password">
						</div>
					</div>
				
				<c:if test="${isBindTelephone}">	
					<div class="form-group">
						<label for="unbindloginpass-msgcode" class="col-xs-3 control-label">短信验证码</label>
						<div class="col-xs-6">
							<input id="unbindloginpass-msgcode" class="form-control" type="text">
							<input id="sendVoice" class="sendVoice" type="hidden" value="false">
							<button id="unbindloginpass-sendmessage" onclick="sendVoice()" data-msgtype="6" data-tipsid="unbindloginpass-errortips" class="btn btn-sendmsg">发送验证码</button>
						</div>
						<!-- <div class="sendVoice">你希望以何种方式发送:
						  <input name="sendcode" checked="checked" type="radio" id="sms"><label for="sms">短信</label>
                          <input name="sendcode" type="radio" id="voice"><label for="voice">语音</label>
						</div> -->
					</div>
					<div class="sendVoice form-group">
                      <label class="col-xs-3" style="text-align: right;">发送方式</label>
                      <div class="col-xs-8">
                       <input name="sendcode" type="radio" id="sms" checked><label for="sms">短信</label>
                        <input name="sendcode" type="radio" id="voice" style="margin-left: 20px;"><label for="voice">语音</label>
                      </div>
                   </div>
				</c:if>	
				
				<c:if test="${isBindGoogle }">
					<div class="form-group">
						<label for="unbindloginpass-googlecode" class="col-xs-3 control-label">谷歌验证码</label>
						<div class="col-xs-6">
							<input id="unbindloginpass-googlecode" class="form-control" type="text">
						</div>
					</div>
				</c:if>	
					
					<div class="form-group">
						<label for="unbindloginpass-errortips" class="col-xs-3 control-label"></label>
						<div class="col-xs-6">
							<span id="unbindloginpass-errortips" class="text-danger "></span>
						</div>
					</div>
					<div class="form-group">
						<label for="unbindloginpass-Btn" class="col-xs-3 control-label"></label>
						<div class="col-xs-6">
							<button id="unbindloginpass-Btn" class="btn btn-danger btn-block">确定提交</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	
		
			<!-- 交易密码设置 -->
			<div class="modal modal-custom fade" id="bindtradepass" tabindex="-1" role="dialog">
				<div class="modal-dialog" role="document">
					<div class="modal-mark"></div>
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close btn-modal" data-dismiss="modal" aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
							<span class="modal-title">设置交易密码</span>
						</div>
						<div class="modal-body form-horizontal">
							<div class="form-group ">
								<label for="bindtradepass-newpass" class="col-xs-3 control-label">新交易密码</label>
								<div class="col-xs-6">
									<input id="bindtradepass-newpass" class="form-control" type="password">
								</div>
							</div>
							<div class="form-group ">
								<label for="bindtradepass-confirmpass" class="col-xs-3 control-label">确认新密码</label>
								<div class="col-xs-6">
									<input id="bindtradepass-confirmpass" class="form-control" type="password">
								</div>
							</div>
							
							<c:if test="${isBindTelephone}">
							<div class="form-group">
									<label for="bindtradepass-msgcode" class="col-xs-3 control-label">短信验证码</label>
									<div class="col-xs-6">
										<input id="bindtradepass-msgcode" class="form-control" type="text">
										<input id="tsendVoice"  class="sendVoice" type="hidden" value="false">
										<button id="bindtradepass-sendmessage" data-msgtype="7" onclick="tsendVoice()" data-tipsid="bindtradepass-errortips" class="btn btn-sendmsg">发送验证码</button>
									</div>
									<!-- <div class="tradecode">您希望以何种方式发送:
									    <input name="sendVoice" checked="checked" type="radio" id="tsms"><label for="tsms">短信</label>
									    <input name="sendVoice"  type="radio" class="voice"><label for="voice">语音</label>
									</div> -->
								</div>
								<div class="tradecode form-group">
                                  <label class="col-xs-3" style="text-align: right;">发送方式</label>
                                  <div class="col-xs-8">
                                    <input name="sendVoice" checked="checked" type="radio" id="tsms"><label for="tsms">短信</label>
                                        <input name="sendVoice"  type="radio" class="voice" style="margin-left: 20px;"><label for="voice">语音</label>
                                  </div>
                               </div>
							</c:if>
							
							<c:if test="${isBindGoogle }">
							<div class="form-group">
									<label for="bindtradepass-googlecode" class="col-xs-3 control-label">谷歌验证码</label>
									<div class="col-xs-6">
										<input id="bindtradepass-googlecode" class="form-control" type="text">
									</div>
								</div>	
							</c:if>
							
							<div class="form-group">
								<label for="bindtradepass-errortips" class="col-xs-3 control-label"></label>
								<div class="col-xs-6">
									<span id="bindtradepass-errortips" class="text-danger"></span>
								</div>
							</div>
							<div class="form-group">
								<label for="bindtradepass-Btn" class="col-xs-3 control-label"></label>
								<div class="col-xs-6">
									<button id="bindtradepass-Btn" class="btn btn-danger btn-block">确定提交</button>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			
			
			<!-- 交易密码修改 -->
			<c:if test="${isTradePassword ==true}">
			<div class="modal modal-custom fade" id="unbindtradepass" tabindex="-1" role="dialog">
				<div class="modal-dialog" role="document">
					<div class="modal-mark"></div>
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close btn-modal" data-dismiss="modal" aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
							<span class="modal-title">修改交易密码</span>
						</div>
						<div class="modal-body form-horizontal">
							<!-- <div class="form-group ">
								<label for="unbindtradepass-oldpass" class="col-xs-3 control-label">旧交易密码</label>
								<div class="col-xs-6">
									<input id="unbindtradepass-oldpass" class="form-control" type="password">
								</div>
							</div> -->
							<div class="form-group ">
								<label for="unbindtradepass-newpass" class="col-xs-3 control-label">新交易密码</label>
								<div class="col-xs-6">
									<input id="unbindtradepass-newpass" class="form-control" type="password">
								</div>
							</div>
							<div class="form-group ">
								<label for="unbindtradepass-confirmpass" class="col-xs-3 control-label">确认新密码</label>
								<div class="col-xs-6">
									<input id="unbindtradepass-confirmpass" class="form-control" type="password">
								</div>
							</div>
							
							<c:if test="${isBindTelephone}">
								<div class="form-group">
									<label for="unbindtradepass-msgcode" class="col-xs-3 control-label">短信验证码</label>
									<div class="col-xs-6">
										<input id="unbindtradepass-msgcode" class="form-control" type="text">
										<button id="unbindtradepass-sendmessage" onclick="upsendVoice()" data-msgtype="7" data-tipsid="unbindtradepass-errortips" class="btn btn-sendmsg">发送验证码</button>
									</div>
									<!-- <div class="upsendVoice">您希望以何种方式发送:
                                      <input name="sendcode" checked="checked" type="radio" id="usms"><label for="usms">短信</label>
                                      <input name="sendcode" type="radio" id="uvoice"><label for="uvoice">语音</label>
                                   </div> -->
								</div>
								<div class="upsendVoice form-group">
			                      <label class="col-xs-3" style="text-align: right;">发送方式</label>
			                      <div class="col-xs-8">
			                        <input name="upsendcode" checked="checked" type="radio" id="usms"><label for="usms">短信</label>
                                    <input name="upsendcode" type="radio" id="uvoice" style="margin-left: 20px;"><label for="uvoice">语音</label>
			                      </div>
			                   </div>
							</c:if>
							
							<c:if test="${isBindGoogle }">
								<div class="form-group">
									<label for="unbindtradepass-googlecode" class="col-xs-3 control-label">谷歌验证码</label>
									<div class="col-xs-6">
										<input id="unbindtradepass-googlecode" class="form-control" type="text">
									</div>
								</div>
							</c:if>	
							
							<div class="form-group">
								<label for="unbindtradepass-errortips" class="col-xs-3 control-label"></label>
								<div class="col-xs-6">
									<span id="unbindtradepass-errortips" class="text-danger"></span>
								</div>
							</div>
							<div class="form-group">
								<label for="unbindtradepass-Btn" class="col-xs-3 control-label"></label>
								<div class="col-xs-6">
									<button id="unbindtradepass-Btn" class="btn btn-danger btn-block">确定提交</button>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</c:if>
    <!-- 实名认证 -->
    <div class="modal modal-custom fade" id="bindrealinfo" tabindex="-1"
        role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-mark"></div>
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close btn-modal" data-dismiss="modal"
                        aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                    <span class="modal-title" id="exampleModalLabel">01身份认证</span>
                    <!-- <div class="KYC1-types text-center">
                        <span class="KYC1-type active">01身份认证</span>
                    </div> -->
                </div>
                <div class="modal-body form-horizontal">
                    <div class="form-group ">
                        <label for="bindrealinfo-realname" class="col-xs-3 control-label">真实姓名</label>
                        <div class="col-xs-6">
                            <input id="bindrealinfo-realname" class="form-control"
                                placeholder="请填写您的真实姓名" type="text" autocomplete="off">
                            <span id="bindrealinfo-realname-errortips "
                                class="text-danger certificationinfocolor">*请填写真实姓名，认证后不能更改，提现时需要与银行等姓名信息保持一致。</span>
                        </div>
                    </div>
                    <div class="form-group ">
                        <label for="bindrealinfo-areaCode" class="col-xs-3 control-label">地区/国家</label>
                        <div class="col-xs-6">
                            <select class="form-control" id="bindrealinfo-address">
                                <option value="86" selected>中国大陆(China)</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group ">
                        <label for="bindrealinfo-areaCode" class="col-xs-3 control-label">证件类型</label>
                        <div class="col-xs-6">
                            <select class="form-control" id="bindrealinfo-identitytype">
                                <option value="0">身份证</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group ">
                        <label for="bindrealinfo-imgcode" class="col-xs-3 control-label">证件号码</label>
                        <div class="col-xs-6">
                            <input id="bindrealinfo-identityno" class="form-control"
                                type="text" autocomplete="off">
                        </div>
                    </div>
                    <div class="form-group ">
                        <label for="bindrealinfo-ckinfo" class="col-xs-3 control-label"></label>
                        <div class="col-xs-6">
                            <div class="checkbox disabled">
                                <label id="bindrealinfo-ckinfolb"> <input
                                    type="checkbox" value="" checked id="bindrealinfo-ckinfo">
                                    我保证提交的信息实属本人所有，非盗用他人证件
                                </label>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="bindrealinfo-errortips" class="col-xs-3 control-label"></label>
                        <div class="col-xs-6">
                            <span id="certificationinfo-errortips" class="text-danger"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="bindemail-Btn" class="col-xs-3 control-label"></label>
                        <div class="col-xs-6">
                            <button id="bindrealinfo-Btn" class="btn btn-danger btn-block">确定提交</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
	
	<input type="hidden" id="changePhoneCode" value="${fuser.ftelephone }" />
	<input type="hidden" id="isEmptyPhone" value="${isBindTelephone ==true?1:0 }" />
	<input type="hidden" id="isEmptygoogle" value="${isBindGoogle==true?1:0 }" />
	<input id="messageType" type="hidden" value="0" />
	


<%@include file="../comm/footer.jsp" %>	
<script type="text/javascript" src="/static/front/js/comm/util.js"></script>
<script type="text/javascript" src="/static/front/js/language/language_cn.js"></script>
<script type="text/javascript" src="/static/front/js/layer/layer.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/comm/msg.js"></script>
<script type="text/javascript" src="/static/front/js/comm/util.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/plugin/jquery.qrcode.min.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/user/user.security.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/user/kycyy.js"></script>
	<!-- <script type="text/javascript">
	  $(function(){
		 $(".btn-sendmsg").on("click",function(){
			 alert(111);
		 }) 
	  })
	</script> -->
	
	<script>
	$(function(){
	    $("body .now_status").on("click","img",function(){
            $(this).hide().siblings("img").show();
            $("#google-status").click();
            if($("#google-status").prop("checked")){
                $("#unbindgoogle-topcode").removeAttr("readonly");
            }else{
                $("#unbindgoogle-topcode").attr("readonly","readonly");
            }
        })
	});
        function sendVoice(){
	    	if($(".sendVoice input[type='radio']:checked").attr("id") == "sms"){
		      $("#sendVoice").val("false");
	        }else{
	        	$("#sendVoice").val("true");
	        }
		    return true;
	    }
        function tsendVoice(){
            if($(".tradecode input[type='radio']:checked").attr("id") == "tsms"){
              $("#sendVoice").val("false");
            }else{
                $("#sendVoice").val("true");
            }
            return true;
        }
        function bsendVoice(){
        	if($(".bindVoice input[type='radio']:checked").attr("id") == "bsms"){
        		$("#sendVoice").val("false");
        	}else{
        		$("#sendVoice").val("true");
        	}
        	return true;
        }
        function bsendVoice1(){
        	if($(".ybindVoice input[type='radio']:checked").attr("id") == "ysms"){
                $("#sendVoice").val("false");
            }else{
                $("#sendVoice").val("true");
            }
            return true;
        	
        }
        function upsendVoice(){
        	if($(".upsendVoice input[type='radio']:checked").attr("id")=="usms"){
        		$("#sendVoice").val("false");
        	}else{
        		$("#sendVoice").val("true");
        	}
        }
	
	</script>
</body>
</html>
