<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../comm/include.inc.jsp"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>





 



<!doctype html>
<html>
<head>
<jsp:include page="../comm/link.inc.jsp"></jsp:include>
<link rel="stylesheet" href="${oss_url}/static/front/css/finance/accountrecord.css" type="text/css"></link>
</head>
<body>
	
<jsp:include page="../comm/header.jsp"></jsp:include>



	<div class="container-full main-con">
		<div class="container displayFlex">
		<div class="row">
			
<%@include file="../comm/left_menu.jsp" %>


			<div class="col-xs-10 padding-right-clear">
				<div class="col-xs-12 padding-right-clear padding-left-clear rightarea record">
					<div class="col-xs-12 rightarea-con">
					
                        <div class="panel-heading padding-bottom-clear">
							<div class="form-group">
								<span>挂单类型：</span>
								
								<select class="form-control typeselect" id="recordType">
								<option value="/trade/entrust.html?status=${status }&symbol=0">全部</option>
								<c:forEach var="v" varStatus="vs" items="${requestScope.constant['tradeMappingss'] }">
										<option value="/trade/entrust.html?status=${status }&symbol=${v.fid}" ${ftrademapping.fid==v.fid?'selected':'' }>${v.fvirtualcointypeByFvirtualcointype2.fShortName }/${v.fvirtualcointypeByFvirtualcointype1.fShortName }</option>
								</c:forEach>		
								</select>
								<span>挂单状态：</span>
								<select class="form-control typeselect" id="recordType1" name="fstatus">
								    <c:if test="${ status == 0}">
								    <option value="/trade/entrust.html?status=0&symbol=0" ${fstatus==null?'selected':''}>全部</option>
								    <option value="/trade/entrust.html?status=0&fstatus=1" ${fstatus==1?'selected':''}>未成交</option>
								    <option value="/trade/entrust.html?status=0&fstatus=2" ${fstatus==2?'selected':''}>部分成交</option>
								    </c:if>
								    
								    <c:if test="${status == 1}">
								    <option value="/trade/entrust.html?status=1&symbol=0" ${fstatus==null?'selected':''}>全部</option>
								    <option value="/trade/entrust.html?status=1&fstatus=3" ${fstatus==3?'selected':''}>完全成交</option>
                                    <option value="/trade/entrust.html?status=1&fstatus=4" ${fstatus==4?'selected':''}>用户撤销</option>
								    </c:if>
								</select>
							</div>
						</div>
                
						<div class="col-xs-12 padding-clear">
							<table class="table table-striped text-left">
					<tr class="bg-gary">
									<td class="text-center">
										委托时间
									</td>
									<td>
										类型
									</td>
									<td>
										数量
									</td>
									<td>
										价格
									</td>
									<td>
										金额
									</td>
									<td>
										成交量
									</td>
									<td>
										成交金额
									</td>
									<td>
										手续费
									</td>
									<td>
										平均成交价
									</td>
									<td>
										状态/操作
									</td>
								</tr>
					
									<c:if test="${fn:length(fentrusts)==0 }">
										<tr>
										<td class="no-data-tips" colspan="10">
											<span>
												暂无委托,<a href="${oss_url}/trade/coin.html?tradeType=0"><font size="4" color="red">进入交易</font></a>
											</span>
										</td>
									</tr>
									</c:if>
									
									<c:forEach items="${fentrusts }" var="v" varStatus="vs">
					<tr>
						<td class="gray"><fmt:formatDate value="${v.fcreateTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						<td class="${v.fentrustType==0?'text-danger':'text-success' }">(${v.ftrademapping.fvirtualcointypeByFvirtualcointype2.fShortName}/${v.ftrademapping.fvirtualcointypeByFvirtualcointype1.fShortName})${v.fentrustType_s}${v.fisLimit==true?'[市价]':'' }</td>
						<td><fmt:formatNumber value="${v.fcount }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${v.ftrademapping.fcount2 }"/></td>
						<td><fmt:formatNumber value="${v.fprize }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${v.ftrademapping.fcount1 }"/></td>
						<c:choose>
						    <c:when test="${status == 0 }">
						         <td><fmt:formatNumber value="${v.fcount * v.fprize}" pattern ="##.##" maxIntegerDigits="15" maxFractionDigits="${v.ftrademapping.fcount1 }" /></td>
						    </c:when>
						    <c:otherwise>
						         <td><fmt:formatNumber value="${v.famount}" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${v.ftrademapping.fcount1 }"/></td>
						    </c:otherwise>
						</c:choose>
						<td><fmt:formatNumber value="${v.fcount-v.fleftCount }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${v.ftrademapping.fcount2 }"/></td>
						<td><fmt:formatNumber value="${afPrizeMap[v.fid] * (v.fcount-v.fleftCount)}" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${v.ftrademapping.fcount1 }"/></td>
						<c:choose>
						   <c:when test="${status== 0 }">
						      <td><fmt:formatNumber value="0" pattern ="##.##" maxIntegerDigits="15" maxFractionDigits="${v.ftrademapping.fcount1 }" /></td>
						   </c:when>
						   <c:otherwise>
						     <c:choose>
                              <c:when test="${v.fentrustType==0 }">
                                <td><fmt:formatNumber value="${v.ffees}" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${v.ftrademapping.fcount1 }"/></td>
                              </c:when>
                              <c:otherwise>
                                <td><fmt:formatNumber value="${v.ffees}" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${v.ftrademapping.fcount1 }"/></td>
                              </c:otherwise>
                           </c:choose>
						   </c:otherwise>
						</c:choose>
						<td><fmt:formatNumber value="${afPrizeMap[v.fid] }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${v.ftrademapping.fcount1 }"/></td>
						<td>
						${v.fstatus_s }
						<c:if test="${v.fstatus==1 || v.fstatus==2}">
						&nbsp;|&nbsp;<a href="javascript:void(0);" class="tradecancel" data-value="${v.fid}" refresh="1">取消</a>
						</c:if>
						<c:if test="${v.fstatus==3}">
						&nbsp;|&nbsp;<a href="javascript:void(0);" class="tradelook" data-value="${v.fid}">查看</a>
						</c:if>
						</td>
                          </tr>
			</c:forEach>
			
								
							</table>
							<div class="text-right">
								${pagin}
							</div>
							
						</div>
						<input type="hidden" value="${currentPage}" id="currentPage">
					</div>
				</div>
			</div>
		</div>
	</div>
	</div>
	<div class="modal modal-custom fade" id="entrustsdetail" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-mark"></div>
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close btn-modal" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<span class="modal-title" id="exampleModalLabel"></span>
				</div>
				<div class="modal-body" style="max-height: 400px;overflow: auto;"></div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				</div>
			</div>
		</div>
	</div>
	
<input id="fhasRealValidate" type="hidden" value="true">
<input type="hidden" id="symbol" value="${ftrademapping.fid }">
<jsp:include page="../comm/footer.jsp"></jsp:include>
<script type="text/javascript" src="${oss_url}/static/front/js/trade/trade.js"></script>
<script type="text/javascript">
$("#recordType1").change(function() {
	var url = $("#recordType1").val();
    window.location.href = url;
});

/* $(function(){
	$("recordType1").val("${fstatus}");
}); */
</script>


</body>
</html>
