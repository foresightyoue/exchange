<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/queryCoinNumber.html">
	<input type="hidden" name="ftype" value="${ftype}" />
	<input type="hidden" name="pageNum" value="${currentPage}" /> 
	<input type="hidden" name="numPerPage" value="${numPerPage}" /> 
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/queryCoinNumber.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>虚拟币类型： <select type="combox" name="ftype">
                            <c:forEach items="${allType}" var="type">
                                <c:if test="${type.fid == ftype}">
                                    <option value="${type.fid}" selected="true">${type.fname}</option>
                                </c:if>
                                <c:if test="${type.fid != ftype}">
                                    <option value="${type.fid}">${type.fname}</option>
                                </c:if>
                            </c:forEach>
                    </select>
                    </td>
                    <td>持有量前 <select type="combox" name="number">
                        <c:if test="${number != null}">
                            <option value="${number}">${number}</option>
                        </c:if>
                        <option value="10">10</option>
                        <option value="30">30</option>
                        <option value="50">50</option>
                        <option value="100">100</option>
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

        </ul>
    </div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr align=center>
				<th width="20">会员UID</th>
				<th width="40">排名</th>
				<th width="40">登录名</th>
				<th width="40">会员昵称</th>
				<th width="40">会员真实姓名</th>
				<th width="40">虚拟币类型</th>
				<th width="40">持有量</th>
				<th width="40">身份证号</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${coinNumberList}"
				var="item" varStatus="num">
				<tr align=center>
					<td>${item.items.fId}</td>
					<td>${num.index + 1}</td>
					<td>${item.items.floginName}</td>
					<td>${item.items.fNickName}</td>
					<td>${item.items.fRealName}</td>
					<td>${item.items.fName}</td>
					<td>${item.items.fTotal}</td>
					<td>${item.items.fIdentityNo}</td>
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
