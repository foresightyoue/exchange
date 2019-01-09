<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<%
	String path = request.getContextPath();
%>
<form id="pagerForm" method="post"
	action="ssadmin/tradeMappingList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /> <input
		type="hidden" name="pageNum" value="${currentPage}" /> <input
		type="hidden" name="numPerPage" value="${numPerPage}" /> <input
		type="hidden" name="orderField" value="${param.orderField}" /><input
		type="hidden" name="orderDirection" value="${param.orderDirection}" />
</form>

<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/tradeMappingList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>关键字：<input type="text" name="keywords" value="${keywords}"
						size="60" />[名称、简称、描述]</td>
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
				<li><a class="add"
					href="ssadmin/goTradeMappingJSP.html?url=ssadmin/addTradeMapping"
					height="350" width="800" target="dialog" rel="addTradeMapping"><span>新增</span>
				</a>
				</li>
				<li><a class="delete"
					href="ssadmin/deleteTradeMapping.html?uid={sid_user}"
					target="ajaxTodo" title="确定要禁用吗?"><span>禁用</span> </a>
				</li>
				<li><a class="edit"
					href="ssadmin/goTradeMapping.html?uid={sid_user}"
					target="ajaxTodo" title="确定要启用吗?"><span>启用</span> </a>
				</li>
				<li><a class="edit"
					href="ssadmin/goTradeMappingJSP.html?url=ssadmin/updateTradeMapping&uid={sid_user}"
					height="350" width="800" target="dialog"
					rel="updateTradeMapping"><span>修改</span> </a>
				</li>
			    <li><a class="edit"
					href="ssadmin/goTradeMappingJSP.html?url=ssadmin/updateTradeFees&uid={sid_user}"
					height="500" width="750" target="dialog"
					rel="updateTradeFees"><span>修改手续费</span> </a>
				</li> 
			
			
		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
			    <th width="20">序号</th>
				<th width="60">法币名称</th>
				<th width="60">交易币名称</th>
				<th width="60">交易时间</th>
				<th width="60">状态</th>
				<th width="60">是否开放</th>
				<th width="60">数量小数位</th>
				<th width="60">单价小数位</th>
				<th width="60">最小买入数量</th>
				<th width="60">最小买入单价</th>
				<th width="60">最小买入金额</th>
				<th width="60">最小卖出数量</th>
				<th width="60">最小卖出单价</th>
				<th width="60">最小卖出金额</th>
				<th width="60">开盘价</th>
				<!-- <th width="60">是否限卖</th>
				<th width="60">限卖比例</th> -->
				<th width="60">级别</th>
			<!-- 	<th width="60">买币是否奖励</th>
				<th width="60">操盘手ID</th>
				<th width="60">奖励比例</th> -->
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${tradeMappingList}" var="t"
				varStatus="num">
				<tr target="sid_user" rel="${t.fid}">
				    <td>${num.index+1 }</td>
					<td>${t.fvirtualcointypeByFvirtualcointype1.fname}</td>
                    <td>${t.fvirtualcointypeByFvirtualcointype2.fname}</td>
                    <td>${t.ftradeTime }</td>
                    <td>${t.fstatus_s }</td>
					<td>${t.fisActive }</td>
                    <td><fmt:formatNumber value="${t.fcount2 }" pattern="##.######" maxIntegerDigits="20" maxFractionDigits="8"/></td>
                    <td><fmt:formatNumber value="${t.fcount1 }" pattern="##.######" maxIntegerDigits="20" maxFractionDigits="8"/></td>
                    <td><fmt:formatNumber value="${t.fminBuyCount }" pattern="##.######" maxIntegerDigits="20" maxFractionDigits="8"/></td>
                    <td><fmt:formatNumber value="${t.fminBuyPrice }" pattern="##.######" maxIntegerDigits="20" maxFractionDigits="8"/></td>
                    <td><fmt:formatNumber value="${t.fminBuyAmount }" pattern="##.######" maxIntegerDigits="20" maxFractionDigits="8"/></td>
                    <td><fmt:formatNumber value="${t.fminSellCount }" pattern="##.######" maxIntegerDigits="20" maxFractionDigits="8"/></td>
                    <td><fmt:formatNumber value="${t.fminSellPrice }" pattern="##.######" maxIntegerDigits="20" maxFractionDigits="8"/></td>
                    <td><fmt:formatNumber value="${t.fminSellAmount }" pattern="##.######" maxIntegerDigits="20" maxFractionDigits="8"/></td>
					<td><fmt:formatNumber value="${t.fprice}" pattern="##.######" maxIntegerDigits="20" maxFractionDigits="8"/></td>
					<%-- <td>${t.fislimit}</td>
					<td><fmt:formatNumber value="${t.ftraderate}" pattern="##.##" maxIntegerDigits="10" maxFractionDigits="6"/></td> --%>
					<td>${t.ftype_s}</td>
					<%-- <td>${t.fisIntrol}</td>
					<td>${t.ftigerUid}</td>
					<td>${t.fintrolRate}</td> --%>
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

