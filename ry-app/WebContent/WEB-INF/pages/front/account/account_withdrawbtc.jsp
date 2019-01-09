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

<link rel="stylesheet" href="${oss_url}/static/front/css/finance/withdraw.css" type="text/css"></link>
<style type="text/css">
#withdrawAddr{
    padding: 0 12px;
    height: 31px;
    line-height: 31px;
    outline: none;
}
</style>
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
</head>
<body>
	<input type="hidden" id="min_double" value="${minwithdrawbtc }">
	<input type="hidden" id="max_double" value="${maxwithdrawbtc }">
	<input type="hidden" id="withdraw_ffee" value="${ffee }">


 <%@include file="../comm/header.jsp" %>

	<div class="container-full main-con">
		<div class="container displayFlex">
			<div class="row">
<%@include file="../comm/left_menu.jsp" %>

			<div class="col-xs-10 padding-right-clear">
				<div class="col-xs-12 padding-right-clear padding-left-clear rightarea withdraw">
					<div class="col-xs-12 rightarea-con">
						<ul class="nav nav-tabs rightarea-tabs">
							<!-- <li class="">
								<a href="/account/withdrawCny.html">美元提现</a>
							</li> -->
							<c:forEach items="${requestScope.constant['allWithdrawCoins'] }" var="v">
								<li class="${v.fid==symbol?'active':'' }">
									<a href="${oss_url}/account/withdrawBtc.html?symbol=${v.fid }">${v.fname }/${v.fShortName } 提现</a>
								</li>
							</c:forEach>
							
						</ul>
						<div class="col-xs-12 padding-clear padding-top-30">
						<c:if test="${!empty fvirtualcointype }">
							<div class="col-xs-7 padding-clear form-horizontal">
								<div class="form-group ">
									<label for="withdrawAmount" class="col-xs-3 control-label">账户余额</label>
									<div class="col-xs-6">
										<span class="form-control border-fff"><fmt:formatNumber value="${fvirtualwallet.ftotal }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="4"/></span>
									</div>
								</div>
								<div class="form-group ">
									<label for="withdrawAddr" class="col-xs-3 control-label">提现地址</label>
									<div class="col-xs-9">
										<select id="withdrawAddr" class="form-control">
											<c:forEach items="${fvirtualaddressWithdraws }" var="v">
												<option value="${v.fid }">${v.fremark}-${v.fadderess }</option>
											</c:forEach>
										</select>
										<a href="#" class="text-primary addtips" data-toggle="modal" data-target="#address">去新增>></a>
									</div>
								</div>
								<div class="form-group ">
									<label for="withdrawAmount" class="col-xs-3 control-label">提现数量</label>
									<div class="col-xs-6">
										<input id="withdrawAmount" class="form-control" type="text">
									</div>
								</div>
								<div class="form-group ">
									<label for="password" class="col-xs-3 control-label">交易密码</label>
									<div class="col-xs-6">
										<input id="tradePwd" class="form-control" type="password">
									</div>
								</div>
							
							<c:if test="${isBindTelephone == true }">		
									<div class="form-group">
										<label for="withdrawPhoneCode" class="col-xs-3 control-label">短信验证码</label>
										<div class="col-xs-6">
											<input id="withdrawPhoneCode" class="form-control" type="text">
											<button id="withdrawsendmessage" data-msgtype="5" data-tipsid="withdrawerrortips" class="btn btn-sendmsg">发送验证码</button>
										</div>
									</div>
							</c:if>		
								
								<c:if test="${isBindGoogle ==true}">
									<div class="form-group">
										<label for="withdrawTotpCode" class="col-xs-3 control-label">谷歌验证码</label>
										<div class="col-xs-6">
											<input id="withdrawTotpCode" class="form-control" type="text">
										</div>
									</div>
								</c:if>	
								
								<div class="form-group">
									<label for="diyMoney" class="col-xs-3 control-label"></label>
									<div class="col-xs-6">
										<span id="withdrawerrortips" class="text-danger">
											
										</span>
									</div>
								</div>
								<div class="form-group">
									<label for="diyMoney" class="col-xs-3 control-label"></label>
									<div class="col-xs-6">
										<button id="withdrawBtcButton" class="btn btn-danger btn-block">立即提现</button>
									</div>
								</div>
							</div>
							</c:if>
							<c:if test="${ empty fvirtualcointype }">
		                        <div class="col-xs-12 padding-clear padding-top-30">
		                            <div class="panel panel-tips">
		                                <div class="panel-body">
		                                    <p style="height: 200px;line-height: 200px;font-size: 30px;" align="center">暂无可提现的币种</p>
		                                </div>
		                            </div>
		                        </div>
		                    </c:if>
							<div class="col-xs-12 padding-clear padding-top-30">
								<div class="panel panel-tips">
									<div class="panel-header text-center text-danger">
										<span class="panel-title">提现须知</span>
									</div>
									<div class="panel-body">
									    <p>${fvirtualcointype.fwidDesc }</p>
									</div>
								</div>
							</div>
							<div class="col-xs-12 padding-clear padding-top-30">
								<div class="panel border">
									<div class="panel-heading">
										<span class="text-danger">${fvirtualcointype.fname }提现记录</span>
										<span class="pull-right recordtitle" data-type="0" data-value="0">收起 -</span>
									</div>
									<div  id="recordbody0" class="panel-body">
										<table class="table">
											<tr>
												<td>
													提现时间
												</td>
												<td>
													提现地址
												</td>
												<td>
													提现数量
												</td>
												<td>
													手续费
												</td>
												<td>
													提现状态
												</td>
												<td>
													备注号
												</td>
											</tr>
											
						<c:forEach items="${fvirtualcaptualoperations }" varStatus="vs" var="v">
							<tr>
								<td width="170"><fmt:formatDate value="${v.fcreateTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td width="300">${v.withdraw_virtual_address }</td>
								<td width="120">${fvirtualcointype.fSymbol}<fmt:formatNumber value="${v.famount }" pattern="0.0000" maxIntegerDigits="15" maxFractionDigits="4"/></td>
								<td width="120">${fvirtualcointype.fSymbol}${v.ffees }</td>
								<td width="180"  class="opa-link">${v.fstatus_s }
								<c:if test="${v.fstatus==1 }">
								&nbsp;|&nbsp;
									<a class="cancelWithdrawBtc opa-link" href="javascript:void(0);" data-fid="${v.fid }">取消</a>
									</c:if>
								</td>
								<td width="100">
									${v.fid }
								</td>
							</tr>
						</c:forEach>
						
						<c:if test="${fn:length(fvirtualcaptualoperations)==0 }">
								<tr>
									<td colspan="6" class="no-data-tips">
										<span>
											您暂时没有提现记录。
										</span>
									</td>
								</tr>
						</c:if>
											
										</table>
										
										<input type="hidden" value="${cur_page }" name="currentPage" id="currentPage"></input>
											<div class="text-right">
												${pagin }
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
	</div>
	<div class="modal modal-custom fade" id="address" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-mark"></div>
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close btn-modal" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<span class="modal-title" id="exampleModalLabel">提现地址</span>
				</div>
				<div class="modal-body form-horizontal">
					<div class="form-group ">
						<label for="withdrawBtcAddr" class="col-xs-3 control-label">提现地址</label>
						<div class="col-xs-8">
							<input id="withdrawBtcAddr" class="form-control" type="text">
						</div>
					</div>
					<div class="form-group ">
						<label for="withdrawBtcRemark" class="col-xs-3 control-label">备注</label>
						<div class="col-xs-8">
							<input id="withdrawBtcRemark" class="form-control" type="text">
						</div>
					</div>
					<div class="form-group ">
						<label for="withdrawBtcPass" class="col-xs-3 control-label">交易密码</label>
						<div class="col-xs-8">
							<input id="withdrawBtcPass" class="form-control" type="password">
						</div>
					</div>
					
					<c:if test="${isBindTelephone == true }">
						<div class="form-group">
							<label for="withdrawBtcAddrPhoneCode" class="col-xs-3 control-label">短信验证码</label>
							<div class="col-xs-8">
								<input id="withdrawBtcAddrPhoneCode" class="form-control" type="text">
								<button id="bindsendmessage" data-msgtype="8" data-tipsid="binderrortips" class="btn btn-sendmsg">发送验证码</button>
							</div>
						</div>
					</c:if>	
					
					<c:if test="${isBindGoogle ==true}">
						<div class="form-group">
							<label for="withdrawBtcAddrTotpCode" class="col-xs-3 control-label">谷歌验证码</label>
							<div class="col-xs-8">
								<input id="withdrawBtcAddrTotpCode" class="form-control" type="text">
							</div>
						</div>
					</c:if>
					
					<div class="form-group">
						<label for="diyMoney" class="col-xs-3 control-label"></label>
						<div class="col-xs-8">
							<span id="binderrortips" class="text-danger"></span>
						</div>
					</div>
					<div class="form-group">
						<label for="diyMoney" class="col-xs-3 control-label"></label>
						<div class="col-xs-8">
							<button id="withdrawBtcAddrBtn" class="btn btn-danger btn-block">确定提交</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<input type="hidden" id="isEmptyAuth" value="${isEmptyAuth }">
	<input type="hidden" id="symbol" value="${fvirtualcointype.fid }">
	<input type="hidden" value="<fmt:formatNumber value="${fvirtualwallet.ftotal }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="4"/>" id="btcbalance" name="btcbalance">
	<input type="hidden" value="${fvirtualcointype.fShortName }" id="coinName" name="coinName">
	<input type="hidden" value="${fuser.authGrade}" id="authGrade" name="authGrade"/>
	<input type="hidden" value="${priceRange}" id="priceRange" name="priceRange"/>

 
<%@include file="../comm/footer.jsp" %>	

	<script type="text/javascript" src="${oss_url}/static/front/js/comm/msg.js"></script>
	<script type="text/javascript" src="${oss_url}/static/front/js/finance/account.withdraw.js"></script>
	<script type="text/javascript" src="/static/front/app/js/alert.js"></script>
</body>
</html>
