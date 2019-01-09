<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/introlinfoList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /> <input
		type="hidden" name="pageNum" value="${currentPage}" /> <input
		type="hidden" name="numPerPage" value="${numPerPage}" /> <input
		type="hidden" name="orderField" value="${param.orderField}" /><input
		type="hidden" name="orderDirection" value="${param.orderDirection}" /><input
		type="hidden" name="logDate" value="${logDate}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/introlinfoList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>会员信息：<input type="text" name="keywords"
						value="${keywords}" size="60" /></td>
					<%-- <td>日期： <input type="text" name="logDate" class="date"
						readonly="true" value="${logDate }" /></td> --%>
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
		<shiro:hasPermission name="ssadmin/introlinfoExport.html">
			<li><a class="icon" href="ssadmin/introlinfoExport.html"
				target="dwzExport" targetType="navTab" title="实要导出这些记录吗?"><span>导出</span>
			</a>
			</li>
		</shiro:hasPermission>	
		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="20" orderField="fuser.fid"
					<c:if test='${param.orderField == "fuser.fid" }'> class="${param.orderDirection}"  </c:if>>UID</th>
				<th width="60">登录名</th>
				<th width="60">昵称</th>
				<th width="60">真实姓名</th>
				<th width="60">是否人民币</th>
				<th width="100">收益名称</th>
				<th width="60">收益数量</th>
				<th width="60" orderField="fcreatetime"
					<c:if test='${param.orderField == "fcreatetime" }'> class="${param.orderDirection}"  </c:if>>创建时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${introlinfoList}" var="introlinfo" varStatus="num">
				<tr target="sid_user" rel="${introlinfo.fid}">
					<td>${introlinfo.fuser.fid}</td>
					<td>${introlinfo.fuser.floginName}</td>
					<td>${introlinfo.fuser.fnickName}</td>
					<td>${introlinfo.fuser.frealName}</td>
					<td>${introlinfo.fiscny}</td>
					<td>${introlinfo.ftitle}</td>
					<td>${introlinfo.fqty}</td>
					<td><fmt:formatDate value="${introlinfo.fcreatetime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
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
