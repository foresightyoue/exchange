<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/userAuditList1.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /> <input
		type="hidden" name="pageNum" value="${currentPage}" /> <input
		type="hidden" name="numPerPage" value="${numPerPage}" /> <input
		type="hidden" name="orderField" value="${param.orderField}" /> <input
		type="hidden" name="orderDirection" value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/userAuditList1.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>会员信息：<input type="text" name="keywords" value="${keywords}"
						size="60" /></td>
					<td>身份证审核状态：
						<select name="faudit" id="faudit">
							<option value="3" ${faudit == "3" ? 'selected':''}>全部</option>
							<option value="2" ${faudit == "2" ? 'selected':''}>审核中</option>x
							<option value="1" ${faudit == "1" ? 'selected':''}>审核通过</option>
							<option value="0" ${faudit == "0" ? 'selected':''}>审核失败或未审核</option>
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
					href="ssadmin/goUserJSP.html?uid={sid_user}&url=ssadmin/auditUser1"
					target="dialog" rel="auditUser1" height="400" width="800"><span>审核</span>
				</a>
				</li>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<%--<th width="22"><input type="checkbox" group="ids"--%>
					<%--class="checkboxCtrl">--%>
				<%--</th>--%>
				<th width="100" orderField="floginName"
					<c:if test='${param.orderField == "floginName" }'> class="${param.orderDirection}"  </c:if>>登录名</th>
				<th width="60" orderField="fstatus"
					<c:if test='${param.orderField == "fstatus" }'> class="${param.orderDirection}"  </c:if>>会员状态</th>
				<th width="60">昵称</th>
				<th width="60">真实姓名</th>
				<th width="60">证件类型</th>
				<th width="60">证件号码</th>
				<th width="60">审核状态</th>
				<th width="60">备注信息</th>
				<th width="60" orderField="fregisterTime"
					<c:if test='${param.orderField == "fregisterTime" }'> class="${param.orderDirection}"  </c:if>>注册时间</th>
				<th width="60" orderField="flastLoginTime"
					<c:if test='${param.orderField == "flastLoginTime" }'> class="${param.orderDirection}"  </c:if>>上次登录时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${userList}" var="user" varStatus="num">
				<tr target="sid_user" rel="${user.fid}">
					<%--<td><input name="ids" value="${user.fid}"--%>
						<%--type="checkbox">--%>
					<%--</td>--%>
					<td>${user.floginName}</td>
					<td>${user.fstatus_s}</td>
					<td>${user.fnickName}</td>
					<td>${user.frealName}</td>
					<td>${user.fidentityType_s}</td>
					<td>${user.fidentityNo}</td>
					<%--<td>${user.faudit}</td>--%>
					<c:choose>
						<c:when test="${user.faudit == 0 && user.fidentityNo == null && user.fpostImgValidate == false && user.fhasImgValidate == false  }">
							<td>未提交实名认证</td>
						</c:when>
						<c:when test="${user.faudit == 0 && user.fidentityNo != null && user.fIdentifyReason != null }">
							<td>审核不通过</td>
						</c:when>
						<c:when test="${user.faudit == 1 }">
							<td>审核通过</td>
						</c:when>
						<c:when test="${user.faudit == 2 }">
							<td>审核中</td>
						</c:when>
						<c:otherwise>
							<td>未提交实名认证</td>
						</c:otherwise>
					</c:choose>
					<td>${user.fIdentifyReason}</td>
					<td><fmt:formatDate value="${user.fregisterTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
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
			 currentPage="${currentPage}">
		</div>
	</div>
</div>
