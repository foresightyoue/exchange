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

<link rel="stylesheet" href="${oss_url}/static/front/css/finance/recharge.css" type="text/css"></link>
</head>
<body>
	




 
 <%@include file="../comm/header.jsp" %>

	<div class="container-full main-con">
		<div class="container displayFlex">
			<div class="row">
<%@include file="../comm/left_menu.jsp" %>

			<div class="col-xs-10 padding-right-clear">
				<div class="col-xs-12 padding-right-clear padding-left-clear rightarea recharge">
					<div class="col-xs-12 rightarea-con">
						<ul class="nav nav-tabs rightarea-tabs">
							<li class="active">
								<a href="${oss_url}/account/rechargeCny.html?type=1">美元充值</a>
							</li>
							<c:forEach items="${requestScope.constant['allRechargeCoins'] }" var="v">
								<li class="${v.fid==symbol?'active':'' }">
									<a href="${oss_url}/account/rechargeBtc.html?symbol=${v.fid }">${v.fname } 充值</a>
								</li>
							</c:forEach>
							
						</ul>
						
						<%--<div class="col-xs-12 padding-clear padding-top-40">--%>
	<%--<!-- 						<a class="recharge-type alipay" href="/account/rechargeCny.html?type=3"> </a>--%>
							<%--<a class="recharge-type wechat" href="/account/rechargeCny.html?type=4"></a> -->--%>
							<%--<a class="recharge-type bank active" href="/account/rechargeCny.html?type=0"></a>--%>
						<%--</div>--%>
						
						<div class="col-xs-12 padding-clear padding-top-30">
							<div class="recharge-box clearfix padding-top-30">
								<span class="recharge-process">
									<span id="rechargeprocess1" class="col-xs-3 active">
										<span class="recharge-process-line"></span>
										<span class="recharge-process-icon">1</span>
										<span class="recharge-process-text">请填写汇款金额</span>
									</span>
									<span id="rechargeprocess2" class="col-xs-3">
										<span class="recharge-process-line"></span>
										<span class="recharge-process-icon">2</span>
										<span class="recharge-process-text">前往网银汇款</span>
									</span>
									<span id="rechargeprocess4" class="col-xs-3">
										<span class="recharge-process-line"></span>
										<span class="recharge-process-icon">3</span>
										<span class="recharge-process-text">完成汇款</span>
									</span>
								</span>
								<div class="col-xs-12 padding-clear padding-top-30">
									<div id="rechage1" class="col-xs-7 padding-clear form-horizontal">
										<div class="form-group ">
											<label for="diyMoney" class="col-xs-3 control-label">充值美元</label>
											<div class="col-xs-6">
												<input id="dollarMoney" class="form-control" type="text" onblur="addCNY();" onkeyup="addCNY();">
												当前汇率:1$=${requestScope.constant['rate'] } ¥
											</div>
										</div>
										<div class="form-group ">
											<label for="diyMoney" class="col-xs-3 control-label">支付CNY</label>
											<div class="col-xs-6">
												<input id="diyMoney" class="form-control" type="text" readonly="readonly">
												<input type="hidden" value=".${randomMoney }" id="random">
												<label for="diyMoney" class="control-label randomtips">.${randomMoney }</label>
											</div>
										</div>
										<div class="form-group">
											<label class="col-xs-3 control-label"></label>
											<div class="col-xs-8">
											<font color="orange">
												* 为快速到账，请按上述金额汇款，包括小数点后两位
											</font>
											</div>
										</div>
										<div class="form-group">
											<label for="sbank" class="col-xs-3 control-label">选择银行</label>
											<div class="col-xs-6">
												<select id="sbank" class="form-control">
													<c:forEach items="${bankInfo }" var="v">
														<option value="${v.fid }">${v.fbankName }</option>
													</c:forEach>
												</select>
											</div>
										</div>
										<div class="form-group">
											<label for="rechargebtn" class="col-xs-3 control-label"></label>
											<div class="col-xs-6">
												<button id="rechargebtn" class="btn btn-danger btn-block">确定充值</button>
											</div>
										</div>
									</div>
									<div id="rechage2" class="col-xs-6 padding-clear form-horizontal" style="display:none;">
										<div class="form-group">
											<span>请登录您的银行网银，选择转账汇款，或去银行柜台办理转账汇款按照 下面内容填写银行汇款信息表单</span>
										</div>
										<div class="form-group">
										<div class="recharge-infobox">
												<div class="form-group">
													<span>收款人：</span> <span id="fownerName">xx</span>
												</div>
												<div class="form-group">
													<span>收款帐号：</span> <span id="fbankNumber">--</span>
												</div>
												<div class="form-group">
													<span>收款帐号开户行：</span> <span id="fbankAddress">--</span>
												</div>
												<div class="form-group">
													<span>付款金额（CNY）：</span> <span id="bankMoney" class="text-danger font-size-16">--</span>
												</div>
												<div class="form-group">
													<span>备注/附言/摘要：</span> <span id="bankInfo" class="text-danger font-size-16">--</span>
												</div>
												<div class="form-group margin-bottom-clear">
													<span class="control-label text-left text-danger rechage-tips margin-bottom-clear" style="border-top-color:#fff0e4;line-height: 18px;padding-left: 0;display: inline-block;">
													<i class="icon"></i> 转账时必须填写 <span id="bankInfotips">--</span>
													</span>
												</div>
											</div>
										 </div>	
										<div class="form-group">
										<label for="diyMoney" class="col-xs-3 control-label"></label>
											<div class="col-xs-6 padding-left-clear">
												<button id="rechargenextbtn" class="btn btn-danger btn-block">我已完成充值，下一步</button>
											</div>
										</div>
									</div>
									<div id="rechage3" class="col-xs-7 padding-clear form-horizontal" style="display:none;">
										<div class="form-group ">
											<label for="fromBank" class="col-xs-3 control-label">您的汇出银行</label>
											<div class="col-xs-6">
												<select id="fromBank" class="form-control">
													<option value="-1">
														请选择银行类型
													</option>
													<c:forEach items="${bankTypes }" var="v">
														<option value="${v.key }">${v.value }</option>
													</c:forEach>
												</select>
											</div>
										</div>
										<div class="form-group ">
											<label for="fromAccount" class="col-xs-3 control-label">银行帐号</label>
											<div class="col-xs-6">
												<input id="fromAccount" class="form-control" type="text">
											</div>
										</div>
										<div class="form-group ">
											<label for="fromPayee" class="col-xs-3 control-label">开户名</label>
											<div class="col-xs-6">
												<input id="fromPayee" class="form-control" type="text">
											</div>
										</div>
										<div class="form-group ">
											<label for="fromPhone" class="col-xs-3 control-label">手机号码</label>
											<div class="col-xs-6">
												<input id="fromPhone" class="form-control" type="text" value="">
											</div>
										</div>
										<div class="form-group">
											<label for="rechargesuccessbtn" class="col-xs-3 control-label"></label>
											<div class="col-xs-6">
												<button id="rechargesuccessbtn" class="btn btn-danger btn-block">提交</button>
											</div>
										</div>
									</div>
									<div id="rechage4" class="col-xs-7 padding-clear form-horizontal" style="display:none;">
										<span class="rechare-success">
											<span class="success-icon">提交成功！</span>
											<a href="${oss_url}/account/rechargeCny.html?type=0">继续充值 >></a>
											<p>我们收到汇款后会尽快为您处理，请您耐心等待。</p>
										</span>
									</div>
									<%--<div class="col-xs-5 padding-clear text-center">--%>
										<%--<a target="_blank"  href="/about/index.html?id=44" class="recharge-help"> </a>--%>
									<%--</div>--%>
								</div>
							</div>
							<div class="col-xs-12 padding-clear padding-top-30">
								<div class="panel panel-tips">
									<div class="panel-header text-center text-danger">
										<span class="panel-title">充值须知</span>
									</div>
									<div class="panel-body">
									    <p>&lt ${requestScope.constant['rechageBankDesc'] }</p>
									</div>
								</div>
							</div>
							<div class="col-xs-12 padding-clear padding-top-30">
								<div class="panel border">
									<div class="panel-heading">
										<span class="text-danger">美元充值记录</span>
										<span class="pull-right recordtitle" data-type="0" data-value="0">收起 -</span>
									</div>
									<div  id="recordbody0" class="panel-body">
										<table class="table">
											<tr>
												<td>订单号</td>
												<td>充值时间</td>
												<td>收款信息</td>
												<td>充值方式</td>
												<td>充值金额($)</td>
												<td>状态</td>
												<td>操作</td>
											</tr>
											 <c:forEach items="${list}" var="v">
													<tr>
														<td class="gray">${v.fid }</td>
														<td><fmt:formatDate value="${v.fcreateTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
														<td>${v.systembankinfo.fownerName }<br/>
																${v.systembankinfo.fbankNumber }<br/>
																${v.systembankinfo.fbankAddress}
														</td>
														<td>${v.fremittanceType }</td>
														<td>${v.famount }</td>
														<td>${v.fstatus_s }</td>
														<td class="opa-link">
														<c:if test="${(v.fstatus==1 || v.fstatus==2)}">
														<a class="rechargecancel opa-link" href="javascript:void(0);" data-fid="${v.fid }">取消</a>
														<c:if test="${v.fstatus==1}">
														&nbsp;|&nbsp;&nbsp;<a class="rechargesub opa-link" href="javascript:void(0);" data-fid="${v.fid }">提交充值</a>
														</c:if>
														</c:if>
														<c:if test="${(v.fstatus==3 || v.fstatus==4)}">
														--
														</c:if>
														</td>
									                 </tr>
									          </c:forEach>
											  <c:if test="${fn:length(list)==0 }">
												<tr>
													<td colspan="6" class="no-data-tips" align="center">
														<span>
															您暂时没有充值数据
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
	<input type="hidden" value="${type }" name="finType" id="finType"></input>
	<input id="minRecharge" value="${minRecharge }" type="hidden">
	<input type="hidden" value="0" name="desc" id="desc"></input>


<%@include file="../comm/footer.jsp" %>	

	<script type="text/javascript" src="${oss_url}/static/front/js/finance/account.recharge.js"></script>
	<script type="text/javascript">
		function addCNY()
		{
		    var rate = '${requestScope.constant['rate'] }';
		    var dollar = $("#dollarMoney").val();
		    var cny = dollar * rate;
		    cny = cny.toFixed(0);
		    $("#diyMoney").val(cny);
		}
	</script>
</body>
</html>
