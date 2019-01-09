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
	<input type="hidden" id="max_double" value="${requestScope.constant['maxwithdrawat'] }">
	<input type="hidden" id="min_double" value="${requestScope.constant['minwithdrawat'] }">
	<input type="hidden" id="type" value="1">


 <%@include file="../comm/header.jsp" %>

	<div class="container-full main-con">
		<div class="container displayFlex">
			<div class="row">
<%@include file="../comm/left_menu.jsp" %>

			<div class="col-xs-10 padding-right-clear">
				<div class="col-xs-12 padding-right-clear padding-left-clear rightarea withdraw">
					<div class="col-xs-12 rightarea-con">
						<ul class="nav nav-tabs rightarea-tabs">
							<c:forEach items="${requestScope.constant['allWithdrawCoins'] }" var="v">
								<c:choose>
									<c:when test="${v.fid==symbol}">
										<li class="${v.fid==symbol?'active':'' }">
											<a href="${oss_url}/account/transfer.html?symbol=${v.fid }">${v.fname }/${v.fShortName } 划转至优生活平台</a>
										</li>
									</c:when>
									<c:otherwise>
										<li class="${v.fid==symbol?'active':'' }">
											<a href="${oss_url}/account/transfer.html?symbol=${v.fid }">${v.fname }/${v.fShortName } 划转</a>
										</li>
									</c:otherwise>
								</c:choose>
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
											<label for="transferAccount" class="col-xs-3 control-label">划转账号</label>
											<div class="col-xs-6">
												<input id="transferAccount" class="form-control" type="text"
													value="${fuser.floginName}">
											</div>
										</div>
										<%-- <c:choose>
									<c:when test="${code==true}">
										<div class="form-group ">
											<label for="withdrawAmount" class="col-xs-3 control-label">划转账号</label>
											<div class="col-xs-6">
												<span class="form-control border-fff">${account}</span>
												<input id="transferAccount" class="form-control" type="hidden" value="${account}">
											</div>
										</div>
									</c:when>
									<c:otherwise>
										<div class="form-group ">
											<label for="transferAccount" class="col-xs-3 control-label">划转账号</label>
											<div class="col-xs-6">
												<input id="transferAccount" class="form-control" type="text" value="${fuser.floginName}">
											</div>
										</div>
									</c:otherwise>
								</c:choose> --%>
								<div class="form-group ">
									<label for="withdrawAmount" class="col-xs-3 control-label">划转数量</label>
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
										<button id="transferButton" class="btn btn-danger btn-block">立即划转</button>
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
										<span class="panel-title">划转须知</span>
									</div>
									<div class="panel-body">
									    <p>${fvirtualcointype.fwidDesc }</p>
									</div>
								</div>
							</div>
							<div class="col-xs-12 padding-clear padding-top-30">
								<div class="panel border">
									<div class="panel-heading">
										<span class="text-danger">${fvirtualcointype.fname }划转记录</span>
										<span class="pull-right recordtitle" data-type="0" data-value="0">收起 -</span>
									</div>
									<div  id="recordbody0" class="panel-body">
										<table class="table">
											<tr>
												<td>
													划转时间
												</td>
												<td>
													划转账号
												</td>
												<td>
													划转数量
												</td>
												<td>
													划转状态
												</td>
											</tr>
											
						<c:forEach items="${ftransfer }" varStatus="vs" var="v">
							<tr>
								<td width="170"><fmt:formatDate value="${v.fCreateTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td width="300">${v.fAccount }</td>
								<td width="120">${v.fAmount }</td>
								<c:if test="${v.fStatus==0 }">
									<td width="180"  class="opa-link">待审核</td>
								</c:if>
								<c:if test="${v.fStatus==1 }">
								    <td width="180" class="opa-link">已完成</td>
								</c:if> <c:if test="${v.fStatus==2 }">
								    <td width="180" class="opa-link">审核未通过</td>
								</c:if>
								</td>
							</tr>
						</c:forEach>
						
						<c:if test="${count==0 }">
								<tr>
									<td colspan="6" class="no-data-tips">
										<span>
											您暂时没有划转记录。
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
