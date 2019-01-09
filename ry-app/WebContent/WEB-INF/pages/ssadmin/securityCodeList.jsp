<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/querySecurityCode.html">
    <input type="hidden" name="mobile" value="${mobile}" /> 
    <input type="hidden" name="pageNum" value="${currentPage}" /> 
    <input type="hidden" name="numPerPage" value="${numPerPage}" /> 
</form>

<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/querySecurityCode.html" method="post">
		<div class="searchBar">
			<table class="searchContent">
				<tr>
					<td>手机号码：<input type="text" name="mobile" value="${mobile}"
						size="60" /></td>
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

        </ul>
    </div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr  align=center>
				<th width="60">手机号</th>
				<th width="60">短信类型</th>
				<th width="60">验证码</th>
				<th width="60">服务商</th>
				<th width="60">发送时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${smsLogList}" var="item" varStatus="sms">
				<tr align=center>
					<td>${item.items.mobile}</td>
					<td>${item.items.type_}</td>
                    <td>${item.items.verifyCode_}</td>
                    <td>${item.items.name_}</td>
                    <td>${item.items.createTime_}</td>
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
<script type="text/javascript">
$(function(){
    var msg = '${error}';
    if(msg!=null && msg !=''){
        alert(msg);
    }
});
</script>
