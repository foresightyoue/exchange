<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/feeList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /><input
		type="hidden" name="logDate" value="${logDate}" /><input
		type="hidden" name="logDate1" value="${logDate1}" /><input
		type="hidden" name="price" value="${price}" /><input
		type="hidden" name="price1" value="${price1}" /><input
		type="hidden" name="ftype" value="${ftype}" /><input
		type="hidden" name="ftype1" value="${ftype1}" /><input
		type="hidden" name="status" value="${status}" /><input
		type="hidden" name="entype" value="${entype}" /> <input type="hidden"
		name="pageNum" value="${currentPage}" /> <input type="hidden"
		name="numPerPage" value="${numPerPage}" /> <input type="hidden"
		name="orderField" value="${param.orderField}" /><input type="hidden"
		name="orderDirection" value="${param.orderDirection}" /><input 
		type="hidden" name="category" value="${category}">
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/feeList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>会员信息：<input type="text" name="keywords" value="${keywords}"
						size="20" /></td>
					<td>法币： <select type="combox" name="ftype1">
							<c:forEach items="${typeMap1}" var="type">
								<c:if test="${type.key == ftype1}">
									<option value="${type.key}" selected="true">${type.value}</option>
								</c:if>
								<c:if test="${type.key != ftype1}">
									<option value="${type.key}">${type.value}</option>
								</c:if>
							</c:forEach>
					</select>
					</td>	
					<td>交易虚拟币： <select type="combox" name="ftype">
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
					<td>开始时间： <input type="text" name="logDate" class="date"
						readonly="true" value="${logDate }"  dateFmt="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td>结束时间： <input type="text" name="logDate1" class="date"
						readonly="true" value="${logDate1 }"  dateFmt="yyyy-MM-dd HH:mm:ss"/>
					</td>
				</tr>
				<tr>
					
					<%-- <td>开始价格：<input type="text" name="price" value="${price}"
						size="10" /></td>
					<td>结束价格：<input type="text" name="price1" value="${price1}"
						size="10" /></td>	 --%>
					<%-- <td>状态： <select type="combox" name="status">
							<c:forEach items="${statusMap}" var="s">
								<c:if test="${s.key == status}">
									<option value="${s.key}" selected="true">${s.value}</option>
								</c:if>
								<c:if test="${s.key != status}">
									<option value="${s.key}">${s.value}</option>
								</c:if>
							</c:forEach>
					</select>
					</td> --%>
					<td>类型： <select type="combox" name="entype">
							<c:forEach items="${entypeMap}" var="t">
								<c:if test="${t.key == entype}">
									<option value="${t.key}" selected="true">${t.value}</option>
								</c:if>
								<c:if test="${t.key != entype}">
									<option value="${t.key}">${t.value}</option>
								</c:if>
							</c:forEach>
					</select>
					
					</td>	
					<td>交易类别： <select type="combox" name="category">
                            <c:forEach items="${categoryMap}" var="c">
                                <c:if test="${c.key == category}">
                                    <option value="${c.key}" selected="true">${c.value}</option>
                                </c:if>
                                <c:if test="${c.key != category}">
                                    <option value="${c.key}">${c.value}</option>
                                </c:if>
                            </c:forEach>
                    </select>
                    </td>
					<td><button type="submit">查询</button></td>
				</tr>
			</table>
			
		</div>
	</form>
</div>
<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
		<shiro:hasPermission name="ssadmin/cancelEntrust.html">
			<li><a title="确定要取消吗?" target="selectedTodo" rel="ids"
				postType="delete" href="ssadmin/cancelEntrust.html"
				class="edit"><span>取消</span> </a>
			</li>
			<li class="line">line</li>
		<li><a class="icon" href="ssadmin/entrustExport.html"
					target="dwzExport" targetType="navTab" title="实要导出这些记录吗?"><span>导出</span>
				</a></li>
		</shiro:hasPermission>
		
		</ul>
	</div>
	<table class="table" width="150%" layoutH="138">
		<thead>
			<tr>
<!-- 				<th width="20">订单ID</th> -->
				<th width="40">用户UID</th>
				<th width="40" orderField="fuser.floginName"
					<c:if test='${param.orderField == "fuser.floginName"}'> class="${param.orderDirection}"  </c:if>>登录名</th>
				<th width="40">法币</th>
				<th width="40">交易虚拟币</th>
				<th width="40" orderField="fstatus"
					<c:if test='${param.orderField == "fstatus"}'> class="${param.orderDirection}"  </c:if>>类型</th>
				<th width="40" orderField="famount"
					<c:if test='${param.orderField == "famount"}'> class="${param.orderDirection}"  </c:if>>总金额</th>
				<th width="40" orderField="fsuccessAmount"
					<c:if test='${param.orderField == "fsuccessAmount"}'> class="${param.orderDirection}"  </c:if>>成交总金额</th>
				<th width="40">总手续费</th>
				<!-- <th width="40">剩余手续费</th>	 -->
				<th width="60" orderField="flastUpdatTime"
					<c:if test='${param.orderField == "flastUpdatTime"}'> class="${param.orderDirection}"  </c:if>>创建时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${entrustList}" var="entrust" varStatus="num">
				<tr target="sid_user" rel="${entrust.fid}">
					<td width="40">${entrust.fuser.fid}</td>
					<td width="40">${entrust.fuser.floginName}</td>
					<td width="40">${entrust.ftrademapping.fvirtualcointypeByFvirtualcointype1.fname}</td>
					<td width="40">${entrust.ftrademapping.fvirtualcointypeByFvirtualcointype2.fname}</td>
					<td width="40">${entrust.fentrustType_s}</td>
					<td width="40"><fmt:formatNumber value="${entrust.famount}" pattern="##.######" maxIntegerDigits="15" maxFractionDigits="6"/></td>
					<td width="40"><fmt:formatNumber value="${entrust.fsuccessAmount}" pattern="##.######" maxIntegerDigits="15" maxFractionDigits="6"/></td>
					<td width="40"><fmt:formatNumber value="${entrust.ffees}" pattern="##.######" maxIntegerDigits="15" maxFractionDigits="6"/></td>
					<%-- <td width="40"><fmt:formatNumber value="${entrust.fleftfees}" pattern="##.######" maxIntegerDigits="15" maxFractionDigits="6"/></td> --%>
					<td width="60"><fmt:formatDate value="${entrust.flastUpdatTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<div class="panelBar">
		 <div class="pages">
			<span>总共: ${totalCount}条；
			  当前页手续费：<font color="red"><fmt:formatNumber value="${fees }" pattern="##.######" maxIntegerDigits="15" maxFractionDigits="6"/></font></span>
		</div> 
		<div class="pagination" targetType="navTab" totalCount="${totalCount}"
			numPerPage="${numPerPage}" pageNumShown="5"
			currentPage="${currentPage}"></div>
	</div>
</div>
