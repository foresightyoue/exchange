<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/viewUserWallet.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /><input
		type="hidden" name="cid" value="${cid}" /> <input type="hidden"
		name="pageNum" value="${currentPage}" /> <input type="hidden"
		name="numPerPage" value="${numPerPage}" /> <input type="hidden"
		name="orderField" value="${param.orderField}" /><input type="hidden"
		name="orderDirection" value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form onsubmit="return dialogSearch(this);"
		action="ssadmin/viewUserWallet.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<input type="hidden" name="cid" value="${cid}" />
					<td>会员信息：<input type="text" name="keywords"
						value="${keywords}" size="60" />
					</td>
				</tr>
			</table>
			<div class="subBar">
				<ul>
					<li><div class="buttonActive">
							<div class="buttonContent">
								<button type="submit">查询</button>
							</div>
						</div></li>
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
				<th width="60">UID</th>
				<th width="60">登录名</th>
				<th width="60">真实姓名</th>
				<th width="60">手机号码</th>
				<th width="60">资产名称</th>
				<th width="60">可用金额/数量</th>
				<th width="60">冻结金额/数量</th>
				<th width="60">充值金额/数量</th>
				<th width="60">提现金额/数量</th>
				<th width="60">买入金额/数量</th>
				<th width="60">卖出金额/数量</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${userList}" var="v" varStatus="num">
				<tr target="sid_user" rel="${v.uid}">
					<td>${num.index +1}</td>
					<td>${v.uid}</td>
					<td>${v.loginName}</td>
					<td>${v.realName}</td>
					<td>${v.telephone}</td>
					<td>${v.coinName}</td>
					<td><fmt:formatNumber value="${v.total}" pattern="##.######" maxIntegerDigits="15" maxFractionDigits="4"/></td>
					<td><fmt:formatNumber value="${v.frozen}" pattern="##.######" maxIntegerDigits="15" maxFractionDigits="4"/></td>
					<td><fmt:formatNumber value="${v.regAmt}" pattern="##.######" maxIntegerDigits="15" maxFractionDigits="4"/></td>
					<td><fmt:formatNumber value="${v.wAmt}" pattern="##.######" maxIntegerDigits="15" maxFractionDigits="4"/></td>
					<td><fmt:formatNumber value="${v.buy}" pattern="##.######" maxIntegerDigits="15" maxFractionDigits="4"/></td>
					<td><fmt:formatNumber value="${v.sell}" pattern="##.######" maxIntegerDigits="15" maxFractionDigits="4"/></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<div class="panelBar">
		<div class="pages">
			<span>总共: ${totalCount}条</span>
		</div>
		<div class="pagination" targetType="dialogTab" totalCount="${totalCount}"
			numPerPage="${numPerPage}" pageNumShown="5"
			currentPage="${currentPage}"></div>
	</div>
</div>
