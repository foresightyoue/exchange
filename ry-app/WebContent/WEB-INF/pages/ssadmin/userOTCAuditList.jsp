<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/userAuditList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /> <input
		type="hidden" name="pageNum" value="${currentPage}" /> <input
		type="hidden" name="numPerPage" value="${numPerPage}" /> <input
		type="hidden" name="orderField" value="${param.orderField}" /> <input
		type="hidden" name="orderDirection" value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/userOTCAuditList.html" method="post">
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
	<div class="panelBar">
		<ul class="toolBar">
				<li><a class="edit"
					href="ssadmin/goUserJSP.html?uid={sid_user}&url=ssadmin/auditOTCUser&type=otc"
					target="dialog" rel=auditUser height="400" width="800"><span>审核</span>
				</a>
				</li>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="22"><input type="checkbox" group="ids"
					class="checkboxCtrl">
				</th>
				<th width="100" orderField="floginName"
					<c:if test='${param.orderField == "floginName" }'> class="${param.orderDirection}"  </c:if>>登录名</th>
				<th width="60" orderField="fstatus"
					<c:if test='${param.orderField == "fstatus" }'> class="${param.orderDirection}"  </c:if>>会员状态</th>
				<th width="60">昵称</th>
				<th width="60">真实姓名</th>
				<th width="60">商家名称</th>
				<th width="60">商家等级</th>
				<th width="60" orderField="fSellerTime"
					<c:if test='${param.orderField == "fSellerTime" }'> class="${param.orderDirection}"  </c:if>>认证时间</th>
				<th width="60" orderField="flastLoginTime"
					<c:if test='${param.orderField == "flastLoginTime" }'> class="${param.orderDirection}"  </c:if>>上次登录时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${userList}" var="user" varStatus="num">
				<tr target="sid_user" rel="${user.fid}">
					<td><input name="ids" value="${user.fid}"
						type="checkbox">
					</td>
					<td>${user.floginName}</td>
					<td>${user.fstatus_s}</td>
					<td>${user.fnickName}</td>
					<td>${user.frealName}</td>
					<td>${user.fSellerName}</td>
					<td>
						<c:if test="${user.fSellerLevel == '1'}">普通商家</c:if>
						<c:if test="${user.fSellerLevel == '2'}">vip会员</c:if>
						<c:if test="${user.fSellerLevel == '3'}">超级会员</c:if>
					</td>
					<td><fmt:formatDate value="${user.fSellerTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    <td><fmt:formatDate value="${user.flastLoginTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
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
