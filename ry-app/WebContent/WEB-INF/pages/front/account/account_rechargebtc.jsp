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
							<!-- <li>
								<a href="/account/rechargeCny.html?type=1">美元充值</a>
							</li> -->
							<c:forEach items="${requestScope.constant['allRechargeCoins'] }" var="v">
								<li class="${v.fid==symbol?'active':'' }">
									<a href="${oss_url}/account/rechargeBtc.html?symbol=${v.fid }">${v.fname }/${v.fShortName } 充值</a> 
								</li>
							</c:forEach>
							
						</ul>
					<c:if test="${!empty fvirtualcointype }">
						<div class="col-xs-12 padding-clear padding-top-30 recharge-qrcodetext">
							<div class="col-xs-2 text-right">
								<span>充值地址</span>
							</div>
							<div class="col-xs-7 recharge-qrcodecon address">
								
									<span class="code-txt" id="address">
									
									<c:if test="${fvirtualaddress.fadderess == null}">
									    <a class="getCoinAddress opa-link" href="javascript:void(0);" data-fid="${fvirtualcointype.fid }">获取地址</a>
									</c:if>
									<c:if test="${fvirtualaddress.fadderess != null}">
									     ${fvirtualaddress.fadderess}
									</c:if>
									</span>
									<c:if test="${fvirtualaddress.fadderess != null}">
									<span class="code-box">
										<span class="qrcode" id="qrcode"></span>
									</span>
									</c:if>
								
							</div>
						</div>
					</c:if>
					<c:if test="${ empty fvirtualcointype }">
						<div class="col-xs-12 padding-clear padding-top-30">
							<div class="panel panel-tips">
								<div class="panel-body">
									<p style="height: 200px;line-height: 200px;font-size: 30px;" align="center">暂无可充值的币种</p>
								</div>
							</div>
						</div>
					</c:if>
					<div class="col-xs-12 padding-clear padding-top-30">
                            <div class="panel panel-tips">
                                <div class="panel-header text-center text-danger">
                                    <span class="panel-title">充值须知</span>
                                </div>
                                <div class="panel-body">
                                    <p>${fvirtualcointype.fregDesc }</p>
                                </div>
                            </div>
                        </div>
						<div class="col-xs-12 padding-clear padding-top-30">
							<div class="panel border">
								<div class="panel-heading">
									<span class="text-danger">${fvirtualcointype.fname }充值记录</span>
								</div>
								<div  id="recordbody0" class="panel-body">
									<table class="table">
										<tr>
											<td>最后更新</td>
											<td>充值地址</td>
											<td>充值数量</td>
											<td>确认数</td>
											<td>状态</td>
										</tr>
										<c:forEach items="${fvirtualcaptualoperations }" var="v" varStatus="vs">
											<tr>
											    <td width="200">${v.flastUpdateTime }</td>
												<td width="350">${v.recharge_virtual_address }</td>
												<td width="100"><fmt:formatNumber value="${v.famount }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="4"/></td>
												<td width="100">${v.fconfirmations }</td>
												<td width="100">${v.fstatus_s }</td>
											</tr>
										</c:forEach>
										<c:if test="${fn:length(fvirtualcaptualoperations)==0 }">
											<tr>
													<td colspan="5" class="no-data-tips" align="center">
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




 <%@include file="../comm/footer.jsp" %>


<input type="hidden" id="address" value="${fvirtualaddress.fadderess}">
	<input type="hidden" id="symbol" value="${fvirtualcointype.fid }">
	<script type="text/javascript" src="${oss_url}/static/front/js/finance/account.recharge.js"></script>
	<script type="text/javascript" src="${oss_url}/static/front/js/plugin/jquery.qrcode.min.js"></script>
	<script type="text/javascript">
		jQuery(document).ready(function() {
		 if (navigator.userAgent.indexOf("MSIE") > 0) {
				jQuery('#qrcode').qrcode({
					text : '${fvirtualaddress.fadderess}',
					width : "149",
					height : "143",
					render : "table"
				});
			} else {
				jQuery('#qrcode').qrcode({
					text : '${fvirtualaddress.fadderess}',
					width : "149",
					height : "143"
				});
			}
		});
	</script>
</body>
</html>
