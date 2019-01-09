<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post"
	action="ssadmin/withDrawalList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /> <input
		type="hidden" name="pageNum" value="${currentPage}" /><input
		type="hidden" name="logDate" value="${logDate}" /><input
		type="hidden" name="logDate2" value="${logDate2}" /> <input
		type="hidden" name="numPerPage" value="${numPerPage}" /> <input
		type="hidden" name="orderField" value="${param.orderField}" /><input
		type="hidden" name="orderDirection" value="${param.orderDirection}" /><input
		type="hidden" name="bztype" value="${bztype}"/>
</form>

<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/withDrawalList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>用户（手机号）：<input type="text" name="userPhone" value="${userphone}"
						size="30" /></td>
					<td>挂单类型： 
						<select type="combox" name="potype_">
							<option value="2">==请选择==</option>
							<option value="0">买单</option>
							<option value="1">卖单</option>
						</select>
					</td>
					<td>币种类型：<select type="combox" name="bztype">
						<c:choose>
							<c:when test="${empty bztype}">
								<option value="">==请选择==</option>
							</c:when>
							<c:otherwise>
								<option value="${bztype}">${bztype}</option>
							</c:otherwise>
						</c:choose>
						<c:forEach items="${tradedescList}" var="tradedescList">
							<option value="${tradedescList.ftradedesc}">${tradedescList.ftradedesc}</option>
						</c:forEach>
					</select>
					</td>
					<!-- <td>成交状态：
						<select type="combox" name="status">
							<option value="2" selected="selected">==请选择==</option>
							<option value="0">未成交</option>
							<option value="1">部分成交</option>
						</select>
					</td> -->
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
			<shiro:hasPermission name="ssadmin/addVirtualOperationLog">
				<li><a class="edit"
					href="ssadmin/withDrawalCancel.html?uid={sid_user}"
					target="ajaxTodo" title="确定要手动撤销订单吗？"><span>撤销</span> </a></li>	
			</shiro:hasPermission>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="20">序号</th>
				<th width="60" orderField="fuser.fTelephone"
					<c:if test='${param.orderField == "fuser.fTelephone" }'> class="${param.orderDirection}"  </c:if>>用户（手机号）</th>
				<th width="60" orderField="fuser.fnickName"
					<c:if test='${param.orderField == "fuser.fnickName" }'> class="${param.orderDirection}"  </c:if>>挂单类型</th>
				<th width="60" orderField="fuser.frealName"
					<c:if test='${param.orderField == "fuser.frealName" }'> class="${param.orderDirection}"  </c:if>>挂单数量</th>
				<th width="60" orderField="fstatus"
					<c:if test='${param.orderField == "fstatus" }'> class="${param.orderDirection}"  </c:if>>单价</th>
				<th width="60" orderField="fvirtualcointype.fname"
					<c:if test='${param.orderField == "fvirtualcointype.fname" }'> class="${param.orderDirection}"  </c:if>>币种类型</th>
				<th width="60" orderField="fqty"
					<c:if test='${param.orderField == "fqty" }'> class="${param.orderDirection}"  </c:if>>挂单时间</th>
				<th width="60" orderField=fcreateTime
					<c:if test='${param.orderField == "fcreateTime" }'> class="${param.orderDirection}"  </c:if>>成交状态</th>
				<th width="60" orderField="fcreator.fname"
					<c:if test='${param.orderField == "fcreator.fname" }'> class="${param.orderDirection}"  </c:if>>成交数量</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${ctctradeList}"
				var="ctctradeList" varStatus="num">
				<tr target="sid_user" rel="${ctctradeList.fId}">
					<td>${num.index +1}</td>
					<td>${ctctradeList.fTelephone}</td>
					<td>
						<c:if test="${ctctradeList.fEntrustType eq '0'}">
							买单
						</c:if>
						<c:if test="${ctctradeList.fEntrustType eq '1'}">
							卖单
						</c:if>
					</td>
					<td><fmt:formatNumber value="${ctctradeList.fCount}" pattern="##.######" maxIntegerDigits="15" maxFractionDigits="8"/></td>
					<td><fmt:formatNumber value="${ctctradeList.fPrize}" pattern="##.######" maxIntegerDigits="15" maxFractionDigits="8"/></td>
					<td>${ctctradeList.ftradedesc}</td>
					<td><fmt:formatDate value="${ctctradeList.fCreateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td>
						<c:if test="${ctctradeList.fStatus eq '1'}">
							未成交
						</c:if>
						<c:if test="${ctctradeList.fStatus eq '2'}">
							部分成交
						</c:if>
						<c:if test="${ctctradeList.fStatus eq '4'}">
							撤销
						</c:if>
					</td>
					<td><fmt:formatNumber value="${ctctradeList.fCount - ctctradeList.fleftCount}" pattern="##.######" maxIntegerDigits="15" maxFractionDigits="8"/></td>
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
