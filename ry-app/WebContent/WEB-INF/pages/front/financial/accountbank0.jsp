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
<base href="${basePath}"/>
<%@include file="../comm/link.inc.jsp" %>

<link rel="stylesheet" href="${oss_url}/static/front/css/finance/accountassets.css" type="text/css"></link>
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
        $("#addressTotpCode").closest('.form-group').hide();
    }
});
</script>
</head>
<body>
	



 <%@include file="../comm/header.jsp" %>

	<div class="container-full main-con">
		<div class="container displayFlex">
			<div class="row">
			<%@include file="../comm/left_menu.jsp" %>
			
			
			<div class="col-xs-10 padding-right-clear">
				<div class="col-xs-12 padding-right-clear padding-left-clear rightarea assets">
					<div class="col-xs-12 rightarea-con">
                          <span class="bank-title col-xs-12 rightarea-con1">添加银行卡</span>
						<div class="add-bank col-xs-12 padding-clear padding-top-30">
						    <div class="form-group ">
                                <label for="payeeAddr" class="col-xs-3 control-label" >开户姓名</label>
                                <div class="col-xs-8">
                                     <input id="payeeAddr" class="form-control" type="text" value="${fuser.frealName }" readonly="readonly"/>
                                     <span class="help-block text-danger">*银行卡账号名必须与您的实名认证姓名一致</span>
                                </div>
                             </div>
                             
                             <div class="form-group ">
                                <label for="withdrawAccountAddr" class="col-xs-3 control-label">银行卡号</label>
                                <div class="col-xs-8">
                                     <input id="withdrawAccountAddr" class="form-control" type="" text>
                                </div>
                             </div>
                             <div class="form-group ">
                                <label for="withdrawAccountAddr2" class="col-xs-3 control-label">确认卡号</label>
                                <div class="col-xs-8">
                                     <input id="withdrawAccountAddr2" class="form-control" type="" text>
                                </div>
                             </div>
                              <div class="form-group ">
                                <label for="openBankTypeAddr" class="col-xs-3 control-label">开户银行</label>
                                <div class="col-xs-8">
                                    <select id="openBankTypeAddr" class="form-control">
                                      <option value="-1">
                                                                                   请选择银行类型
                                      </option>
                                      <c:forEach items="${bankTypes }" var="v">
                                          <option value="${v.key }">${v.value }</option>
                                      </c:forEach>
                                     </select>
                               </div>
                               <div id="prov_city" class="form-group ">
                                 <label for="prov" class="col-xs-3 control-label">开户地址</label>
                                 <div class="col-xs-8 ">
                                    <div class="col-xs-4 padding-right-clear padding-left-clear margin-bottom-15">
                                       <select id="prov" class="form-control">
                                       </select>
                                    </div>
                                    <div class="col-xs-4 padding-right-clear margin-bottom-15">
                                        <select id="city" class="form-control">
                                        </select>
                                    </div>
                                    <div class="col-xs-4 padding-right-clear margin-bottom-15">
                                        <select id="dist" class="form-control prov">
                                        </select>
                                    </div>
                                    <div class="col-xs-12 padding-right-clear padding-left-clear">
                                        <input id="address" class="form-control" type="text" placeholder="请输入您的详细地址"/>
                                    </div>
                                </div>
                               </div>
                              </div>
                               <c:if test="${isBindTelephone == true }">
                                 <div class="form-group">
                                    <label for="addressPhoneCode" class="col-xs-3 control-label">短信验证码</label>
                                    <div class="col-xs-8">
                                      <input id="addressPhoneCode" class="form-control" type="text">
                                      <input id="sendVoice" type="hidden" value="false">
                                      <button id="bindsendmessage" onclick="sendVoice()" data-msgtype="10" data-tipsid="binderrortips" class="btn btn-sendmsg">发送验证码</button>
                                   </div>
                                   
                                   <div class="sendVoice form-group">
                                      <label class="col-xs-3">发送方式:</label>
                                      <div class="col-xs-8">
                                        <input name="sendcode" checked="checked" type="radio" id="sms"><label for="sms">短信</label>
                                        <input name="sendcode" type="radio" id="voice" style="margin-left: 20px;"><label for="voice">语音</label>
                                      </div>
                                   </div>
                                 </div>
                               </c:if>
                               <c:if test="${isBindGoogle ==true}">
                                <div class="form-group">
                                   <label for="addressTotpCode" class="col-xs-3 control-label">谷歌验证码</label>
                                   <div class="col-xs-8">
                                     <input id="addressTotpCode" class="form-control" type="text">
                                   </div>
                                </div>
                               </c:if>
                               <div class="form-group">
                                    <label for="binderrortips" class="col-xs-3 control-label"></label>
                                    <div class="col-xs-8">
                                        <span id="binderrortips" class="text-danger"></span>
                                    </div>
                               </div>
                               <div class="form-group">
                                   <label for="withdrawCnyAddrBtn" class="col-xs-3 control-label"></label>
                                     <div class="col-xs-8">
                                     <button id="withdrawCnyAddrBtn" class="btn btn-danger btn-block">确定提交</button>
                                   </div>
                             </div>
					    </div>
					    <div class="allbind-bank">
                           <label class="bank-title">我的银行卡</label>
                           <div class="col-xs-12 padding-clear padding-top-30">
                             <c:forEach items="${bankinfos}" var="v">
                             <div class="col-xs-4">
                              <div class="bank-item item1">
                                <div class="bank-item-top">
                                  <div class="col-xs-2">
                                    <i class="banklogo banklogo-item${v.fbankType }"></i>
                                  </div>
                                  <div class="col-xs-8">
                                    <p>${v.fname}</p>
                                    <p>${v.fbankNumber }</p>
                                  </div>
                                  <div class="col-xs-2">
                                    <span class="bank-item-del" data-fid="${v.fid}"></span>
                                  </div>
                                 </div>
                                <div class="bank-item-bot">
                                   <div class="col-xs-12">
                                    <p>${v.fname},${v.fothers }</p>
                                    <p>${v.faddress}</p>
                                   </div>
                               </div>
                            </div>
                            </div>
                           </c:forEach>
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
	<script type="text/javascript" src="${oss_url}/static/front/js/finance/account.assets.js"></script>
	<script type="text/javascript" src="${oss_url}/static/front/js/finance/city.min.js"></script>
	<script type="text/javascript" src="${oss_url}/static/front/js/finance/jquery.cityselect.js"></script>
	<script type="text/javascript">
	   function sendVoice(){
		   if($(".sendVoice input[type='radio']:checked").attr("id") == "sms"){
			   $("#sendVoice").val("false");
		   }else{
			   $("#sendVoice").val("true");
		   }
		   return true;
	   }
	</script>
</body>
</html>
