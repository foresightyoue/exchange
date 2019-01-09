<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post"
	action="ssadmin/UserOverflowTracing.html">
	    <input type="hidden"
		name="pageNum" value="${currentPage}" /> <input type="hidden"
		name="numPerPage" value="${numPerPage}" /> 
</form>
<div class="pageHeader">
    <form rel="pagerForm" method="post" action="ssadmin/UserOverflowTracing.html" onsubmit="return navTabSearch(this);">
		<div class="searchBar">
			<table class="searchContent">
				<tr>
					<td>开始时间：
					   <input type="text" id="startTime" name="startTime" class="date"
                        readonly="true" value="${startTime }" />
					</td>
					<td>
					</td>
					<td>结束时间：(默认当前时间)</td>
					<td>
					</td>
				</tr>
			</table>
		</div>
		<div class="subBar" style="float: right;">
                <ul>
                    <li><div class="buttonActive">
                            <div class="buttonContent">
                                <button type="submit">查询</button>
                            </div>
                        </div>
                    </li>
                </ul>
            </div>
	</form>
</div>
<div class="pageContent" style="height: 100%;">
	<div class="panelBar">
        <ul class="toolBar">
          <shiro:hasPermission name="ssadmin/userExport.html">
              <li>
	              <a class="icon" href="ssadmin/UserOverflowTracingExport.html"
					target="dwzExport" targetType="navTab" title="确定要导出这些记录吗?"><span>导出</span>
				 </a>
				 <span>USDT可用总差额：</span><span>${USDTSum}</span>
				 <span>USDT冻结总差额：</span><span>${USDTZenSum}</span>
				 <span>AT可用总差额：</span><span>${ATSum}</span>
				 <span>AT冻结总差额：</span><span>${ATZenSum}</span>
				 <span>PC可用总差额：</span><span>${PCSum}</span>
				 <span>PC冻结总差额：</span><span>${PCZenSum}</span>
              </li>
          </shiro:hasPermission>
        </ul>
    </div>
	<table class="table" width="300%" layoutH="138">
		<thead>
			<tr>
				<th width="90">UID</th>
			    <th width="90">登录名</th>
				<th width="90">姓名</th>
				<th width="90">电话</th>
					
				<th width="90">初始USDT可用</th>
				<th width="90">当前USDT可用</th>
				<th width="110">区间USDT可用变动总额</th>
				<th width="120">Usdt可用实际与理论差值</th>
					
				<th width="90">初始USDT冻结</th>
				<th width="90">当前USDT冻结</th>
				<th width="110">区间USDT冻结变动总额</th>
				<th width="120">Usdt冻结实际与理论差值</th>
				
				<th width="90">初始AT可用</th>
				<th width="90">当前AT可用</th>
				<th width="110">区间AT可用变动总额</th>
				<th width="120">At可用实际与理论差值</th>
					
				<th width="90">初始AT冻结</th>
				<th width="90">当前AT冻结</th>
				<th width="110">区间AT冻结变动总额</th>	
				<th width="120">At冻结实际与理论差值</th>
					
				<th width="90">初始PC可用</th>
				<th width="90">当前PC可用</th>
				<th width="110">区间PC可用变动总额</th>
				<th width="120">PC可用实际与理论差值</th>
					
				<th width="90">初始PC冻结</th>
				<th width="90">当前PC冻结</th>
				<th width="110">区间PC冻结变动总额</th>	
				<th width="120">PC冻结实际与理论差值</th>
				<th width="90">操作列</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${UserOverflowTracing}"  var="UserOverflowTracinglist" varStatus="num">
			        <tr target="sid_user" rel="${UserOverflowTracinglist.items.fid }">
			    	<td>${UserOverflowTracinglist.items.fuserid}</td>
			    	<td>${UserOverflowTracinglist.items.floginName}</td>
			    	<td>${UserOverflowTracinglist.items.fRealName}</td>
			    	<td>${UserOverflowTracinglist.items.fTelephone}</td>
			    	
			    	<td><fmt:formatNumber value="${UserOverflowTracinglist.items.fStartUsdt}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
			    	<td><fmt:formatNumber value="${UserOverflowTracinglist.items.fEndUsdt}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
			    	<td><fmt:formatNumber value="${UserOverflowTracinglist.items.fUsdtChangeSum}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
			    	<td style="color:red"><fmt:formatNumber value="${UserOverflowTracinglist.items.fDifferenceUsdt}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
			    	
			    	<td><fmt:formatNumber value="${UserOverflowTracinglist.items.fStartUsdtzen}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
			    	<td><fmt:formatNumber value="${UserOverflowTracinglist.items.fEndUsdtzen}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
			    	<td><fmt:formatNumber value="${UserOverflowTracinglist.items.fUsdtzenChangeSum}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
			    	<td style="color:red"><fmt:formatNumber value="${UserOverflowTracinglist.items.fDifferenceUsdtzen}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
			    	
			    	<td><fmt:formatNumber value="${UserOverflowTracinglist.items.fStartAt}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
			    	<td><fmt:formatNumber value="${UserOverflowTracinglist.items.fEndAt}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
			    	<td><fmt:formatNumber value="${UserOverflowTracinglist.items.fAtChangeSum}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
			    	<td style="color:red"><fmt:formatNumber value="${UserOverflowTracinglist.items.fDifferenceAt}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
			    	
			    	<td><fmt:formatNumber value="${UserOverflowTracinglist.items.fStartAtzen}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td><fmt:formatNumber value="${UserOverflowTracinglist.items.fEndAtzen}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td><fmt:formatNumber value="${UserOverflowTracinglist.items.fAtzenChangeSum}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td style="color:red"><fmt:formatNumber value="${UserOverflowTracinglist.items.fDifferenceAtzen}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					
					<td><fmt:formatNumber value="${UserOverflowTracinglist.items.fStartPc}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td><fmt:formatNumber value="${UserOverflowTracinglist.items.fEndPc}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td><fmt:formatNumber value="${UserOverflowTracinglist.items.fPcChangeSum}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td style="color:red"><fmt:formatNumber value="${UserOverflowTracinglist.items.fDifferencePc}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					
					<td><fmt:formatNumber value="${UserOverflowTracinglist.items.fStartPczen}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td><fmt:formatNumber value="${UserOverflowTracinglist.items.fEndPczen}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td><fmt:formatNumber value="${UserOverflowTracinglist.items.fPczenChangeSum}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td style="color:red"><fmt:formatNumber value="${UserOverflowTracinglist.items.fDifferencePczen}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td>
			    	<a class="add" href="ssadmin/UserOverflowTracingDetails.html?uid=${UserOverflowTracinglist.items.fuserid}" height="500" width="780" target="dialog" rel="UserOverflowTracingDetails" >详情</a>
					</td>
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