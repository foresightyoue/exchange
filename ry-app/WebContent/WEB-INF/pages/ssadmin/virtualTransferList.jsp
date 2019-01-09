<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post"
	action="ssadmin/virtualTransferList.html">
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
		action="ssadmin/virtualTransferList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>会员信息：<input type="text" name="keywords" value="${keywords}"
						size="60" /></td>
					<td>虚拟币类型： <select type="combox" name="ftype">
							<c:forEach items="${typeMap}" var="type">
								<c:if test="${type.key == ftype}">
									<option value="${type.key}" selected="true">${type.value}</option>
								</c:if>
								<c:if test="${type.key != ftype}">
									<option value="${type.key}">${type.value}</option>
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
	<div class="panelBar">
		<ul class="toolBar">
			<shiro:hasPermission name="ssadmin/viewVirtualCaptual.html">
				<li id="shsh"><a class="edit" href="javascript:void(0)"
					height="320" width="800" target="ajaxTodo"
					rel="viewtransfer" title="确定要审核吗?" id="auditing"><span>审核</span>
				</a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/viewVirtualCaptual.html?type=3">
				<li id="qx"><a class="edit"href="javascript:void(0)"
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
			</shiro:hasPermission>
			<li class="line">line</li>
			<li><a class="icon" href="ssadmin/viewtransferListExport.html"
				target="dwzExport" targetType="navTab" title="实要导出这些记录吗?"><span>导出</span>
			</a></li>
		</ul>
	</div>
	<table class="table" width="150%" layoutH="138">
		<thead>
			<tr>
				<th width="22"><input type="checkbox" group="ids"
					class="checkboxCtrl">
				</th>
				<th width="20" orderField="fid">会员UID</th>
				<th width="60" orderField="fuser.floginName"
					<c:if test='${param.orderField == "ftransfer.floginName" }'> class="${param.orderDirection}"  </c:if>>登录名</th>
				<th width="60" orderField="fuser.fnickName"
					<c:if test='${param.orderField == "ftransfer.fnickName" }'> class="${param.orderDirection}"  </c:if>>会员昵称</th>
				<th width="60" orderField="fuser.frealName"
					<c:if test='${param.orderField == "ftransfer.frealName" }'> class="${param.orderDirection}"  </c:if>>会员真实姓名</th>
				<th width="60" orderField="fvirtualcointype.fname"
					<c:if test='${param.orderField == "ftransfer.fname" }'> class="${param.orderDirection}"  </c:if>>虚拟币类型</th>
				<th width="60" orderField="fStatus"
					<c:if test='${param.orderField == "ftransfer.fStatus" }'> class="${param.orderDirection}"  </c:if>>状态</th>
				<th width="20">交易类型</th>
				<th width="60" orderField="fAmount"
					<c:if test='${param.orderField == "ftransfer.fAmount" }'> class="${param.orderDirection}"  </c:if>>数量</th>
				<th width="60">划转账号</th>
				<th width="60" orderField="fCreateTime"
					<c:if test='${param.orderField == "ftransfer.fCreateTime" }'> class="${param.orderDirection}"  </c:if>>创建时间</th>
				<th width="60" orderField="flastUpdateTime"
					<c:if test='${param.orderField == "ftransfer.flastUpdateTime" }'> class="${param.orderDirection}"  </c:if>>最后修改时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${ftransferList}"
				var="ftransfer" varStatus="num">
				<tr target="sid_user" rel="${ftransfer.fId}">
					<td><input name="ids" value="${ftransfer.fId}"
						type="checkbox">
					</td>
					<td>${ftransfer.uid}</td>
					<td>${ftransfer.floginName}</td>
					<td>${ftransfer.fnickName}</td>
					<td>${ftransfer.frealName}</td>
					<td>${ftransfer.fname}</td>
					<td>待审核</td>
					<td>虚拟币划转</td>
					<td><fmt:formatNumber value="${ftransfer.fAmount}" pattern="##.######" maxIntegerDigits="15" maxFractionDigits="4"/></td>
					<td>${ftransfer.fAccount}</td>
					<td>${ftransfer.fCreateTime}</td>
					<td>${ftransfer.flastUpdateTime}</td>
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

<script>
$(document).ready(function(){
	//审核
	$("#shsh").hover(function() {
		var check= new Array();
		$('input[name="ids"]:checked').each(function(){ 
			check.push($(this).val()); 
			});
		var url="ssadmin/transferAuditing.html?uid="+check;
		$("#auditing").attr("href",url);
		console.log("check"+check);
	})
	//取消划转
	$("#qx").hover(function() {
		var qxCheck= new Array();
		$('input[name="ids"]:checked').each(function(){ 
			qxCheck.push($(this).val()); 
			});
		var url="ssadmin/goTransferChangeStatus.html?uid="+qxCheck;
		$("#qxCheck").attr("href",url);
		console.log("check"+qxCheck);
 	})
})
</script>
