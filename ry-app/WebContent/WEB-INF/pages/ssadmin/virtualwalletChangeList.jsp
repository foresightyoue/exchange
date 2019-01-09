<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post"
	action="ssadmin/virtualwalletChangeList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="userId" value="${userId}" /><input
		type="hidden" name="coinId" value="${coinId}" /><input
		type="hidden" name="changeDate" value="${changeDate}" /> <input type="hidden"
		name="pageNum" value="${currentPage}" /> <input type="hidden"
		name="numPerPage" value="${numPerPage}" /> <input type="hidden"
		name="orderField" value="${param.orderField}" /><input type="hidden"
		name="orderDirection" value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/virtualwalletChangeList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>会员ID：<input type="text" name="userId" value="${userId}"
						/></td>
					<td>币种：<select type="combox" name="fShortName">
							<c:forEach items="${typeMap}" var="type">
								<c:if test="${type.value == fShortName}">
									<option value="${type.value}" selected="true">${type.value}</option>
								</c:if>
								<c:if test="${type.value != fShortName}">
									<option value="${type.value}">${type.value}</option>
								</c:if>
							</c:forEach>
					</select> <%-- <input type="text" name="fShortName" value="${fShortName}"
					/> --%></td>
					<td>变更时间： <input type="text" name="changeDate" class="date"
                        readonly="true" value="${changeDate }" />
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
	<!-- <div class="panelBar">
	<ul class="toolBar">
	<li><a class="icon" href="ssadmin/virtualwalletExport.html"
					target="dwzExport" targetType="navTab" title="实要导出这些记录吗?"><span>导出</span>
				</a></li>
	</ul>
	</div> -->
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="30">会员UID</th>
				<th width="30">币种Id</th>
				<th width="60">当前钱包可用余额</th>
				<th width="60">当前钱包冻结余额</th>
				<th width="60">可用余额变更值</th>
				<th width="60">冻结余额变更值</th>
				<th width="60">变更原因</th>
				<th width="60">变更时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${virtualwalletChangeList}" var="virtualwallet"
				varStatus="num">
				<tr target="sid_user" rel="${virtualwallet.uid_}">
					<td>${virtualwallet.userId_}</td>
					<td>${virtualwallet.fShortName}</td>
					<td>${virtualwallet.total_}</td>
					<td>${virtualwallet.frozen_}</td>
					<td>${virtualwallet.totalChange_}</td>
					<td>${virtualwallet.frozenChange_}</td>
					<td>${virtualwallet.changeReason_}</td>
					<td>${virtualwallet.changeDate_}</td>
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
