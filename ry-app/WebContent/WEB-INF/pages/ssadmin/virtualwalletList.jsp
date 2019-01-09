<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post"
	action="ssadmin/virtualwalletList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /><input
		type="hidden" name="ftype" value="${ftype}" /> <input type="hidden"
		name="pageNum" value="${currentPage}" /> <input type="hidden"
		name="numPerPage" value="${numPerPage}" /> <input type="hidden"
		name="orderField" value="${param.orderField}" /><input type="hidden"
		name="orderDirection" value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/virtualwalletList.html" method="post">
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
					<td>最后修改时间： <input type="text" name="logDate" class="date"
                        readonly="true" value="${logDate }" />
                    </td>   
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
	<li><a class="icon" href="ssadmin/virtualwalletExport.html"
					target="dwzExport" targetType="navTab" title="实要导出这些记录吗?"><span>导出</span>
				</a></li>
	</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="20">会员UID</th>
				<th width="60">登录名</th>
				<th width="60">会员昵称</th>
				<th width="60">会员真实姓名</th>
				<th width="60" orderField="fvirtualcointype.fid"
					<c:if test='${param.orderField == "fvirtualcointype.fid" }'> class="${param.orderDirection}"  </c:if>>币种类型</th>
				<th width="60" orderField="ftotal"
					<c:if test='${param.orderField == "ftotal" }'> class="${param.orderDirection}"  </c:if>>总数量</th>
				<th width="60" orderField="ffrozen"
					<c:if test='${param.orderField == "ffrozen" }'> class="${param.orderDirection}"  </c:if>>冻结数量</th>
				<th width="60" orderField="ftotal"
					<c:if test='${param.orderField == "ftotal" }'> class="${param.orderDirection}"  </c:if>>今日可卖数量（针对限卖）</th>	
				<th width="60" orderField="flastUpdateTime"
					<c:if test='${param.orderField == "flastUpdateTime" }'> class="${param.orderDirection}"  </c:if>>最后修改时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${virtualwalletList}" var="virtualwallet"
				varStatus="num">
				<tr target="sid_user" rel="${virtualwallet.fid}">
					<td>${virtualwallet.fuser.fid}</td>
					<td>${virtualwallet.fuser.floginName}</td>
					<td>${virtualwallet.fuser.fnickName}</td>
					<td>${virtualwallet.fuser.frealName}</td>
					<td>${virtualwallet.fvirtualcointype.fname}</td>
					<td><fmt:formatNumber value="${virtualwallet.ftotal}" pattern="##.######" maxIntegerDigits="15" maxFractionDigits="4"/></td>
					<td><fmt:formatNumber value="${virtualwallet.ffrozen}" pattern="##.######" maxIntegerDigits="15" maxFractionDigits="4"/></td>
					<td><fmt:formatNumber value="${virtualwallet.fcanSellQty}" pattern="##.######" maxIntegerDigits="15" maxFractionDigits="4"/></td>
					<td><fmt:formatDate value="${virtualwallet.flastUpdateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
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
