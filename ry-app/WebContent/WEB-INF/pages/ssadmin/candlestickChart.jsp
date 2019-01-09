<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/candlestickChart.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /> <input
		type="hidden" name="pageNum" value="${currentPage}" /> <input
		type="hidden" name="numPerPage" value="${numPerPage}" /> <input
		type="hidden" name="orderField" value="${param.orderField}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/candlestickChart.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>交易对ID：<input type="text" name="keywords"
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
	<div class="panelBar">
		<ul class="toolBar">
			<shiro:hasPermission name="ssadmin/userForbbin2.html">
				<li><a class="edit"
					href="ssadmin/setCandlestickChart.html?state=0&fid={sid_user}"
					height="400" width="850" target="ajaxTodo" rel="createRole"><span>使用本站数据</span>
				</a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/userForbbin2.html">
				<li><a class="edit"
					href="ssadmin/setCandlestickChart.html?state=1&fid={sid_user}"
					height="400" width="850" target="ajaxTodo" rel="updateLink"><span>使用第三方数据</span>
				</a></li>
			</shiro:hasPermission>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="20">序号</th>
				<th width="60">交易对ID</th>
				<th width="60">k线图数据来源</th>
				<th width="60">虚拟币名称</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${lists}" var="list" varStatus="num">
				<tr target="sid_user" rel="${list.fid}">
					<td>${num.index +1}</td>
					<td>${list.fid}</td>
					<c:choose>
						<c:when test="${list.state == 0}">
							<td>本站数据</td>
						</c:when>
						<c:otherwise>
							<td>第三方数据</td>
						</c:otherwise>
					</c:choose>
					<td>${list.ftradedesc}</td>
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
