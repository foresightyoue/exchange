<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/userWalletReport.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" />
		<input type="hidden" name="pageNum" value="${currentPage}" /> <input type="hidden"
		name="numPerPage" value="${numPerPage}" /> <input type="hidden"
		name="orderField" value="${param.orderField}" /><input type="hidden"
		name="orderDirection" value="${param.orderDirection}" /> <input type="hidden"
		name="coin" value="${coin}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/userWalletReport.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>会员信息：<input type="text" name="keywords"
						value="${keywords}" size="60" />
					</td>
					<td>虚拟币类型： <select type="combox" name="coin">
							<c:forEach items="${allList}" var="type">
								<c:if test="${type}">
									<option value="${type}" selected="true">${type}</option>
								</c:if>
								<c:if test="${type != coin}">
									<option value="${type}">${type}</option>
								</c:if>
							</c:forEach>
					</select>
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
				<th width="60">资产名称</th>
				<th width="60">可用金额/数量</th>
				<th width="60">冻结金额/数量</th>
				<th width="60">充值金额/数量</th>
				<th width="60">提现金额/数量</th>
				<th width="60">买入金额/数量</th>
				<th width="60">卖出金额/数量</th>
				<th width="60">盈利(充值-余额-提现)</th>
				<th width="60">汇总(充值+买入-余额-提现-卖出)</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${userList}" var="v" varStatus="num">
				<tr target="sid_user" rel="${v.uid}">
					<td>${num.index +1}</td>
					<td>${v.uid}</td>
					<td>${v.loginName}</td>
					<td>${v.coinName}</td>
					<td><fmt:formatNumber value="${v.total}" pattern="##.######" maxIntegerDigits="15" maxFractionDigits="4"/></td>
					<td><fmt:formatNumber value="${v.frozen}" pattern="##.######" maxIntegerDigits="15" maxFractionDigits="4"/></td>
					<td><fmt:formatNumber value="${v.regAmt}" pattern="##.######" maxIntegerDigits="15" maxFractionDigits="4"/></td>
					<td><fmt:formatNumber value="${v.wAmt}" pattern="##.######" maxIntegerDigits="15" maxFractionDigits="4"/></td>
					<td><fmt:formatNumber value="${v.buy}" pattern="##.######" maxIntegerDigits="15" maxFractionDigits="4"/></td>
					<td><fmt:formatNumber value="${v.sell}" pattern="##.######" maxIntegerDigits="15" maxFractionDigits="4"/></td>
					<td style="color:red;"><fmt:formatNumber value="${v.regAmt-v.total-v.frozen-v.wAmt}" pattern="##.######" maxIntegerDigits="15" maxFractionDigits="4"/></td>
					<td style="color:red;"><fmt:formatNumber value="${v.regAmt+v.buy-v.total-v.frozen-v.wAmt-v.sell}" pattern="##.######" maxIntegerDigits="15" maxFractionDigits="4"/></td>
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
