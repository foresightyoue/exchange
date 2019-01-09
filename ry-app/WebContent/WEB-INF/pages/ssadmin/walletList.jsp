<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/walletList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /> <input
		type="hidden" name="pageNum" value="${currentPage}" /> <input
		type="hidden" name="numPerPage" value="${numPerPage}" /> <input
		type="hidden" name="orderField" value="${param.orderField}" /><input
		type="hidden" name="orderDirection" value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/walletList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>会员信息：<input type="text" name="keywords" value="${keywords}"
						size="60" /></td>
				</tr>
			</table>
			<div class="subBar">
				<ul>
					<li><div class="buttonActive">
							<div class="buttonContent">
								<button type="submit">查询</button>
							</div>
						</div>
					</li>
				</ul>
			</div>
		</div>
	</form>
</div>
<div class="pageContent">
	<div class="panelBar"></div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="20">序号</th>
				<th width="60">登录名</th>
				<th width="60">会员昵称</th>
				<th width="60">会员真实姓名</th>
				<th width="60" orderField="ftotal"
					<c:if test='${param.orderField == "ftotal" }'> class="${param.orderDirection}"  </c:if>>总金额</th>
				<th width="60" orderField="ffrozen"
					<c:if test='${param.orderField == "ffrozen" }'> class="${param.orderDirection}"  </c:if>>冻结金额</th>
				<th width="60">最后修改时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${walletList}" var="v" varStatus="num">
				<tr target="sid_user" rel="${v.fid}">
					<td>${num.index +1}</td>
					<td>${v.fuser.floginName}</td>
					<td>${v.fuser.fnickName}</td>
					<td>${v.fuser.frealName}</td>
					<td><fmt:formatNumber value="${v.ftotal}" pattern="##.######" maxIntegerDigits="15" maxFractionDigits="4"/></td>
					<td><fmt:formatNumber value="${v.ffrozen}" pattern="##.######" maxIntegerDigits="15" maxFractionDigits="4"/></td>
					<td>${v.flastUpdateTime}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<div class="panelBar">
		<div class="pages">
			<span>总共: ${totalCount}条</span>
		</div>
		<div class="pagination" targetType="navTab" totalCount="${totalCount}"
			numPerPage="${numPerPage}" pageNumShown="5"
			currentPage="${currentPage}"></div>
	</div>
</div>
