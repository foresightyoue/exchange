<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>个人收款账户</title>
</head>
<body>
	<table border="1px" cellspacing="0px">
		<tr>
			<th>用户账户</th>
			<th>收款类型</th>
			<th>收款名</th>
			<th>收款账户</th>
			<th>收款账户二维码</th>
			<th>银行名称</th>
			<th>银行名支行名称</th>
		</tr>
		<c:forEach items="${frList}" var="frs">
			<tr>
				<td>${frs.fUsr_id}</td>
				<td>
					<c:if test="${frs.fType ==0 }">
						银行
					</c:if>
					<c:if test="${frs.fType ==1 }">
						支付宝
					</c:if>
					<c:if test="${frs.fType ==2 }">
						微信
					</c:if>
				</td>
				<td>${frs.fName}</td>
				<td>${frs.fAccount}</td>
				<td>
					<%-- ${frs.fimgurl} --%>
					<img src="${frs.fImgUrl}" alt="收款账户二维码" width="150px" height="80px"/>
				</td>
				<td>${frs.fBankname}</td>
				<td>${frs.fBanknamez}</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>