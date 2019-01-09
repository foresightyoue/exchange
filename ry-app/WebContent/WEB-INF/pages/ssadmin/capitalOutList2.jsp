<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/capitalOutList2.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /><input
		type="hidden" name="capitalId" value="${capitalId}" /><input
		type="hidden" name="logDate" value="${logDate}" /> <input
		type="hidden" name="logDate2" value="${logDate2}" /><input
		type="hidden" name="pageNum" value="${currentPage}" /> <input
		type="hidden" name="numPerPage" value="${numPerPage}" /> <input
		type="hidden" name="orderField" value="${param.orderField}" /><input
		type="hidden" name="orderDirection" value="${param.orderDirection}" />
</form>

<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/capitalOutList2.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>会员信息：<input type="text" name="keywords" value="${keywords}"
						size="40" /></td>
					<td>充值ID：<input type="text" name="capitalId"
						value="${capitalId}" size="10" />
					</td>
					<td>开始日期： <input type="text" name="logDate" class="date"
						readonly="true" value="${logDate }" />
					</td>
					<td>结束日期： <input type="text" name="logDate2" class="date"
						readonly="true" value="${logDate2 }" />
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
			<shiro:hasPermission name="ssadmin/capitalOutAudit1.html">
				<li><a title="确实要复审通过这些记录吗?" target="selectedTodo" rel="ids"
				postType="string" href="ssadmin/capitalOutaAuditAll2.html"
				class="edit"><span>全部复审通过</span> </a>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/capitalOutAudit4.html">
				<li><a class="edit"
					href="ssadmin/capitalOutAudit.html?uid={sid_user}&type=4"
					target="ajaxTodo" title="确定要取消提现吗?"><span>取消提现</span> </a>
				</li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/capitalOutAudit2.html">
				<li><a class="edit"
					href="ssadmin/capitalOutAudit.html?uid={sid_user}&type=2"
					target="ajaxTodo" title="确定要锁定吗?"><span>锁定</span> </a>
				</li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/capitalOutAudit5.html">
			<li><a title="确实要锁定这些记录吗?" target="selectedTodo" rel="ids"
				postType="string" href="ssadmin/capitalOutAuditAll.html"
				class="edit"><span>全部锁定</span> </a>
			</li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/capitalOutAudit3.html">
				<li><a class="edit"
					href="ssadmin/capitalOutAudit.html?uid={sid_user}&type=3"
					target="ajaxTodo" title="确定要取消锁定吗?"><span>取消锁定</span> </a>
				</li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/capitalOutAudit6.html">
			<li><a class="edit"
				href="ssadmin/viewUserWallet.html?cid={sid_user}" height="320"
				width="800" target="dialog"><span>查看会员资金情况</span> </a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/capitalOutAudit7.html">	
			<li><a class="icon" href="ssadmin/capitalOutExport2.html"
				target="dwzExport" targetType="navTab" title="实要导出这些记录吗?"><span>导出EXCEL</span>
			</a></li>	
			</shiro:hasPermission>
		</ul>
	</div>
	<table class="table" width="120%" layoutH="138">
		<thead>
			<tr>
				<th width="22"><input type="checkbox" group="ids"
					class="checkboxCtrl">
				</th>
				<th width="20">订单ID</th>
				<th width="20">会员UID</th>
				<th width="60" orderField="fuser.floginName"
					<c:if test='${param.orderField == "fuser.floginName" }'> class="${param.orderDirection}"  </c:if>>登录名</th>
				<th width="60" orderField="fuser.fnickName"
					<c:if test='${param.orderField == "fuser.fnickName" }'> class="${param.orderDirection}"  </c:if>>会员昵称</th>
				<th width="60" orderField="fuser.frealName"
					<c:if test='${param.orderField == "fuser.frealName" }'> class="${param.orderDirection}"  </c:if>>会员真实姓名</th>
				<th width="60" orderField="ftype"
					<c:if test='${param.orderField == "ftype" }'> class="${param.orderDirection}"  </c:if>>类型</th>
				<th width="60" orderField="fstatus"
					<c:if test='${param.orderField == "fstatus" }'> class="${param.orderDirection}"  </c:if>>状态</th>
				<th width="60" orderField="famount"
					<c:if test='${param.orderField == "famount" }'> class="${param.orderDirection}"  </c:if>>金额</th>
				<th width="60" orderField="famount"
						<c:if test='${param.orderField == "famount" }'> class="${param.orderDirection}"  </c:if>>CNY金额</th>
				<th width="60" orderField="ffees"
					<c:if test='${param.orderField == "ffees" }'> class="${param.orderDirection}"  </c:if>>手续费</th>
				<th width="60" orderField="fBank"
					<c:if test='${param.orderField == "fBank" }'> class="${param.orderDirection}"  </c:if>>银行</th>
				<th width="60" orderField="fAccount"
					<c:if test='${param.orderField == "fAccount" }'> class="${param.orderDirection}"  </c:if>>收款帐号</th>
				<th width="60" orderField="fPhone"
					<c:if test='${param.orderField == "fPhone" }'> class="${param.orderDirection}"  </c:if>>手机号码</th>
				<th width="60">开户行地址</th>
				<th width="60" orderField="fcreateTime"
					<c:if test='${param.orderField == "fcreateTime" }'> class="${param.orderDirection}"  </c:if>>创建时间</th>
				<th width="60" orderField="fLastUpdateTime"
					<c:if test='${param.orderField == "fLastUpdateTime" }'> class="${param.orderDirection}"  </c:if>>最后修改时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${capitaloperationList}" var="capitaloperation"
				varStatus="num">
				<tr target="sid_user" rel="${capitaloperation.fid}">
					<td><input name="ids" value="${capitaloperation.fid}"
						type="checkbox">
					</td>
					<td>${capitaloperation.fid}</td>
					<td>${capitaloperation.fuser.fid}</td>
					<td>${capitaloperation.fuser.floginName}</td>
					<td>${capitaloperation.fuser.fnickName}</td>
					<td>${capitaloperation.fuser.frealName}</td>
					<td>${capitaloperation.ftype_s}</td>
					<td>${capitaloperation.fstatus_s}</td>
					<td>${capitaloperation.famount}</td>
					<td>${capitaloperation.famount*requestScope.constant['rate']}</td>
					<td>${capitaloperation.ffees}</td>
					<td>${capitaloperation.fBank}</td>
					<td>${capitaloperation.faccount_s}</td>
					<td>${capitaloperation.fPhone}</td>
					<td>${capitaloperation.faddress}</td>
					<td>${capitaloperation.fcreateTime}</td>
					<td>${capitaloperation.fLastUpdateTime}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<div class="panelBar">
		<div class="pages">
			<span>总共: ${totalCount}条;
			<font color="red">当前待提现金额：￥<fmt:formatNumber
							value="${currentAmt}" pattern="#0.######" />;</font>
			<font color="red">当前累计待提现金额：￥<fmt:formatNumber
							value="${amountOutWaitingMap.totalAmount}" pattern="#0.######" />;</font>
			<font color="red">今日提现金额：￥<fmt:formatNumber
							value="${amountOutMap.totalAmount}" pattern="#0.######" />;</font>
			</span>
		</div>
		<div class="pagination" targetType="navTab" totalCount="${totalCount}"
			numPerPage="${numPerPage}" pageNumShown="5"
			currentPage="${currentPage}"></div>
	</div>
</div>
