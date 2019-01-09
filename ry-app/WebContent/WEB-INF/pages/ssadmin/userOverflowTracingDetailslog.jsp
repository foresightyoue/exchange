<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/UserOverflowTracingDetails.html">
 <input type="hidden" name="pageNum" value="${currentPage}" /> <input
		type="hidden" name="numPerPage" value="${numPerPage}" /> <input
		type="hidden" name="uid" value="${uid}" />
</form>
<div class="pageContent">
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="20">序号</th>
				<th width="60">用户ID</th>
				<th width="60">币种类型</th>
				<th width="60">当前可用余额</th>
				<th width="60">当前冻结余额</th>
				<th width="70">可用余额变更值</th>
				<th width="70">冻结余额变更值</th>
				<th width="60">变更原因</th>
				<th width="75">变更时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${UserOverflowTracingDetailslog}" var="var" varStatus="num">
				<tr>
					<td>${num.index +1}</td>
					<td>${var.items.userId_}</td>
					<td>${var.items.coinId_}</td>
					<td>${var.items.total_}</td>
					<td>${var.items.frozen_}</td>
					<td>${var.items.totalChange_}</td>
					<td>${var.items.frozenChange_}</td>
					<td>${var.items.changeReason_}</td>
					<td>${var.items.changeDate_}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<div class="panelBar">
		<div class="pages">
			<span>总共: ${totalCount}条</span>
		</div>
		<div class="pagination" targetType="dialog" totalCount="${totalCount}"
			numPerPage="${numPerPage}" pageNumShown="5"
			currentPage="${currentPage}"></div>
	</div>
</div>
