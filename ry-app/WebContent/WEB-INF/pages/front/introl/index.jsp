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
<link rel="stylesheet" href="${oss_url}/static/front/css/user/user.css" type="text/css"></link>
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
						<ul class="nav nav-tabs rightarea-tabs">
							<li class="active">
								<a href="javascript:void(0)">会员推广记录</a>
							</li>
							<li>
								<a href="${oss_url}/introl/mydivide.html?type=2">收益记录</a>
							</li>
						</ul>
						
						<div class="user-top-icon">
							<div class="col-xs-2 text-center">
								<i class="top-icon intro"></i>
							</div>
							<div class="col-xs-10 padding-left-clear">
								<div>
									<h5 class="top-title5" style="font-size:14px; color: #e06924;padding-top: 10px;">推广链接：${spreadLink }</h5>
								</div>
							</div>
						</div>
						
						<div class="col-xs-12 padding-clear padding-top-30">
							<table class="table table-striped">
								<tr class="bg-gary">
									<td class="col-xs-4">会员UID</td>
									<td class="col-xs-4">推荐时间</td>
									<td class="col-xs-4 text-right">是否实名认证</td>
								</tr>
								
								<c:forEach items="${fusers }" var="v" >
								<tr>
									<td>${v.fid }</td>
									<td><fmt:formatDate
									value="${v.fregisterTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
									<td  class="text-right">
									   <c:if test="${!v.fpostRealValidate}">未实名认证</c:if>
									   <c:if test="${v.fhasRealValidate && v.fpostImgValidate && !v.fhasImgValidate}">已实名,未审核</c:if>
									   <c:if test="${v.fhasImgValidate||v.authGrade eq 3 }">已通过审核</c:if>
									</td>
								</tr>
								</c:forEach>
								
								<c:if test="${fn:length(fusers)==0 }">
								<tr>
										<td colspan="3" class="no-data-tips">
											<span> 暂无记录 </span>
										</td>
									</tr>
								</c:if>	
							</table>
							
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


<%@include file="../comm/footer.jsp" %>	


</body>
</html>