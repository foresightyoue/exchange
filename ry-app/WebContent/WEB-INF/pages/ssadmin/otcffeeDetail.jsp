<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post"
	action="ssadmin/otcfeeList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /><input
		type="hidden" name="coinId" value="${coinId}" /><input type="hidden"
		name="pageNum" value="${currentPage}" /> <input type="hidden"
		name="numPerPage" value="${numPerPage}" /> 
</form>

<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/otcfeeList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>会员信息:<input type="text" name="keywords" value="${keywords}"
						size="60" /></td>
					<td>币种：<input type="text" name="coinId" value="${coinId}"
					size="60" /></td>
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
			<%-- <shiro:hasPermission name="ssadmin/viewVirtualCaptual.html">
				<li id="sh"><a class="edit" href="#"
					height="320" width="800" target="ajaxTodo"
					rel="viewtransfer" title="确定要审核吗?" id="abq"><span>审核</span>
				</a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/viewVirtualCaptual.html?type=3">
				<li id="qx"><a class="edit"href="#"
					height="320" width="800" target="ajaxTodo"
					rel="viewVirtualCapitaloperation" title="确定要取消划转吗?" id="qxCheck"><span>取消划转</span>
				</a>
				</li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/viewVirtualCaptual.html?type=3">
				<li><a class="edit"
					href="ssadmin/goTransferRecord.html?uid={sid_user}&url=ssadmin/transferRecord"
					target="dialog" rel="transferRecord" height="400" width="800"><span>划转记录</span>
				</a></li>
			</shiro:hasPermission> --%>
			<!-- <li class="line">line</li>
			<li><a class="icon" href="ssadmin/viewtransferListExport.html"
				target="dwzExport" targetType="navTab" title="实要导出这些记录吗?"><span>导出</span>
			</a></li> -->
		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<!-- <th width="22"><input type="checkbox" group="ids"
					class="checkboxCtrl">
				</th> -->
				<th width="20">会员UID</th>
				<th width="20">用户名</th>
				<th width="20">币种</th>
				<th width="20">手续费</th>
				<th width="20">订单类型</th>
				<th width="20">交易时间</th>
				<th width="20">交易对象</th>
				<!-- <th width="20">交易类型</th> -->
				<th width="20">交易数量</th>
				<th width="20">手续费金额</th>
				<th width="20">可用金额</th>
				<th width="20">冻结金额</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${otcfeeList}"
				var="otcfeeList" varStatus="num">
				<tr target="sid_user" rel="${otcfeeList.fId}">
					<td>${otcfeeList.fUser_id}</td>
					<td>${otcfeeList.floginName}</td>
					<td>${otcfeeList.fShortName}</td>
					<td>${otcfeeList.ffee * 100}%</td>
					<c:if test="${otcfeeList.fOrder_type == 2}">
                        <td>OTC手续费</td>
                    </c:if>
					<td>${otcfeeList.ffee_time}</td>
					<td>${otcfeeList.fOrder_object}</td>
					<%-- <td>${otcfeeList.fTrade_type eq 1 ? '买入' : '卖出'}</td> --%>
					<td>${otcfeeList.fTrade_count}</td>
					<td>${otcfeeList.ffee_sum}</td>
					<td>${otcfeeList.fTotal}</td>
					<td>${otcfeeList.fFrozen}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<div class="panelBar">
		<div class="pages">
			<span>总共: ${totalCount}条;
			当前页手续费：<font color="red"><fmt:formatNumber value="${fees }" pattern="##.######" maxIntegerDigits="15" maxFractionDigits="6"/></font></span>
		</div>
		<div class="pagination" targetType="navTab" totalCount="${totalCount}"
			numPerPage="${numPerPage}" pageNumShown="5"
			currentPage="${currentPage}"></div>
	</div>
</div>

