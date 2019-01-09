<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post"
	action="ssadmin/transferCoinList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /><input
		type="hidden" name="ftype" value="${ftype}" /><input type="hidden"
		name="pageNum" value="${currentPage}" /> <input type="hidden"
		name="numPerPage" value="${numPerPage}" /> <input type="hidden"
		name="orderField" value="${param.orderField}" /><input type="hidden"
		name="orderDirection" value="${param.orderDirection}" />
</form>

<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/transferCoinList.html" method="post">
		<div class="searchBar">
			<table class="searchContent">
				<tr>
					<td>会员ID：<input type="text" name="keywords"
						value="${keywords}" size="60" /></td>
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
		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="20">会员UID</th>
				<th width="30" orderField="fuser.floginName"
					<c:if test='${param.orderField == "fuser.floginName" }'> class="${param.orderDirection}"  </c:if>>登录名</th>
				<th width="30" orderField="fuser.fnickName"
					<c:if test='${param.orderField == "fuser.fnickName" }'> class="${param.orderDirection}"  </c:if>>币种</th>
				<th width="30" orderField="ffees"
					<c:if test='${param.orderField == "ffees" }'> class="${param.orderDirection}"  </c:if>>交易数量</th>
				<th width="30" orderField="fstatus"
					<c:if test='${param.orderField == "fstatus" }'> class="${param.orderDirection}"  </c:if>>转入状态</th>
				<th width="30" orderField="fcreateTime"
					<c:if test='${param.orderField == "fcreateTime" }'> class="${param.orderDirection}"  </c:if>>创建时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${lists}" var="list" varStatus="num">
				<tr target="sid_user" rel="${list.fId}">
					<td>${list.fuserid}</td>
					<td>${list.finput_username}</td>
					<td>${list.fName}</td>
					<td><fmt:formatNumber value="${list.finput_count}" pattern="##.######" maxIntegerDigits="15" maxFractionDigits="4" /></td>
					<td>${list.foutput_msg}</td>
					<td><fmt:formatDate value="${list.fcreatetime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
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
