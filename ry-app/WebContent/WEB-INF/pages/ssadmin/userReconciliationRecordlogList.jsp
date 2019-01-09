<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>

<form id="pagerForm" method="post"
	action="ssadmin/UserReconciliationRecordBykeywords.html"><input
		type="hidden" name="keywords" value="${keywords}" />
</form>
<div class="pageHeader">
    <form method="post" action="ssadmin/UserReconciliationRecordBykeywords.html" onsubmit="return navTabSearch(this);">
		<div class="searchBar">
			<table class="searchContent">
				<tr>
					<td>关键字：<input type="text" id="text" name="keywords" 
						size="40" value="${keyWords }" /> [会员名、昵称、真实姓名、身份证号、登录名、邮箱地址]</td>
					<td id="sh">
						<input type="hidden" id="fids" name="fids" />
<!-- 					    <button type="submit">确定</button> -->
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
<div class="pageContent">
	<div class="panelBar">
        <ul class="toolBar">
          <shiro:hasPermission name="ssadmin/userExport.html">
              <li>
	              <a class="icon" href="ssadmin/UserReconciliationRecordExport.html"
					target="dwzExport" targetType="navTab" title="确定要导出这些记录吗?"><span>导出</span>
				 </a>
              </li>
          </shiro:hasPermission>
        </ul>
    </div>
	<table class="table" width="250%" layoutH="138">
		<thead>
			<tr>
				<th width="90">UID</th>
				
			    <th width="90">USDT差额</th>
				<th width="90">AT差额</th>
				<th width="90">PC差额</th>
				<th width="90">SCC差额</th>
				
<!-- 				<th width="90">USDT可用</th> -->
<!-- 				<th width="90">USDT冻结</th> -->
				<th width="90">USDT总额</th>
					
<!-- 				<th width="90">AT可用</th> -->
<!-- 				<th width="90">AT冻结</th> -->
				<th width="90">AT总额</th>
					
<!-- 				<th width="90">PC可用</th> -->
<!-- 				<th width="90">PC冻结</th> -->
				<th width="90">PC总额</th>
				
<!-- 				<th width="90">SCC可用</th> -->
<!-- 				<th width="90">SCC冻结</th> -->
				<th width="90">SCC总额</th>
				
				<th width="90">USDT账面总额</th>
				<th width="90">AT账面总额</th>
				<th width="90">PC账面总额</th>
				<th width="90">SCC账面总额</th>
					
				<th width="90">USDTCZ</th>
				<th width="90">ATCZ</th>
					
				<th width="90">BB57HFUsdt</th>	
				<th width="90">BB57DDAT</th>
				<th width="90">BB57DDUsdt</th>
				<th width="90">BB57HFAT</th>
					
				<th width="90">BB60HFUsdt</th>
				<th width="90">BB60DDPC</th>
				<th width="90">BB60DDUsdt</th>
				<th width="90">BB60HFPC</th>
				
				<th width="90">BB61HFUsdt</th>
				<th width="90">BB61DDSCC</th>
				<th width="90">BB61DDUsdt</th>
				<th width="90">BB61HFSCC</th>
					
				<th width="90">奖励AT</th>
				<th width="90">HTTJUsdt</th>
				<th width="90">HTTJAT</th>
				<th width="90">HTTJPC</th>
				<th width="90">CTCBuyUsdt</th>
				<th width="90">CTCSellUsdt</th>
				<th width="90">划账AT</th>
				<th width="90">OTCBuyUsdt</th>
				<th width="90">OTCSellUsdt</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${userReconciliationRecord}"  var="userReconciliationRecordList" varStatus="num">
			    <tr target="sid_user" rel="${userReconciliationRecordList.items.fid }">
			    	<td>${userReconciliationRecordList.items.fusid}</td>
			    	
			    	<td><fmt:formatNumber value="${userReconciliationRecordList.items.fusdtDifference}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
			    	<td><fmt:formatNumber value="${userReconciliationRecordList.items.fatDifference}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
			    	<td><fmt:formatNumber value="${userReconciliationRecordList.items.fpcDifference}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
			    	<td><fmt:formatNumber value="${userReconciliationRecordList.items.fsccDifference}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
			    	
<%-- 			    	<td><fmt:formatNumber value="${userReconciliationRecordList.items.fusdtftotal}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td> --%>
<%-- 			    	<td><fmt:formatNumber value="${userReconciliationRecordList.items.fusdtffrozen}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td> --%>
			    	<td><fmt:formatNumber value="${userReconciliationRecordList.items.fusdtTotal}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
			    	
<%-- 			    	<td><fmt:formatNumber value="${userReconciliationRecordList.items.fatftotal}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td> --%>
<%-- 			    	<td><fmt:formatNumber value="${userReconciliationRecordList.items.fatffrozen}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td> --%>
			    	<td><fmt:formatNumber value="${userReconciliationRecordList.items.fatTotal}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
			    	
<%-- 			    	<td><fmt:formatNumber value="${userReconciliationRecordList.items.fpcftotal}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td> --%>
<%-- 			    	<td><fmt:formatNumber value="${userReconciliationRecordList.items.fpcffrozen}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td> --%>
			    	<td><fmt:formatNumber value="${userReconciliationRecordList.items.fpcTotal}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
			    	
<%-- 			    	<td><fmt:formatNumber value="${userReconciliationRecordList.items.fsccftotal}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td> --%>
<%-- 			    	<td><fmt:formatNumber value="${userReconciliationRecordList.items.fsccffrozen}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td> --%>
			    	<td><fmt:formatNumber value="${userReconciliationRecordList.items.fsccTotal}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
			    	
			    	<td><fmt:formatNumber value="${userReconciliationRecordList.items.faccountsUsdtTotal}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td><fmt:formatNumber value="${userReconciliationRecordList.items.faccountsAtTotal}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td><fmt:formatNumber value="${userReconciliationRecordList.items.faccountsPcTotal}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td><fmt:formatNumber value="${userReconciliationRecordList.items.faccountsSccTotal}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					
					<td><fmt:formatNumber value="${userReconciliationRecordList.items.fusdtCZ}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td><fmt:formatNumber value="${userReconciliationRecordList.items.fatCZ}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					
					<td><fmt:formatNumber value="${userReconciliationRecordList.items.fbuyATSellUsdtCount}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td><fmt:formatNumber value="${userReconciliationRecordList.items.fbuyATCount}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td><fmt:formatNumber value="${userReconciliationRecordList.items.fbuyUsdtSellATCount}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td><fmt:formatNumber value="${userReconciliationRecordList.items.fsellATCount}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					
					<td><fmt:formatNumber value="${userReconciliationRecordList.items.fbuyPCSellUsdtCount}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td><fmt:formatNumber value="${userReconciliationRecordList.items.fbuyPCCount}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td><fmt:formatNumber value="${userReconciliationRecordList.items.fbuyUsdtSellPCCount}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td><fmt:formatNumber value="${userReconciliationRecordList.items.fsellPCCount}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					
					<td><fmt:formatNumber value="${userReconciliationRecordList.items.fbuySccSellUsdtCount}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td><fmt:formatNumber value="${userReconciliationRecordList.items.fbuySccCount}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td><fmt:formatNumber value="${userReconciliationRecordList.items.fbuyUsdtSellSccCount}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td><fmt:formatNumber value="${userReconciliationRecordList.items.fsellSccCount}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					
					<td><fmt:formatNumber value="${userReconciliationRecordList.items.fatJiangLi}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td><fmt:formatNumber value="${userReconciliationRecordList.items.fusdtHTTJ}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td><fmt:formatNumber value="${userReconciliationRecordList.items.fatHTTJ}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td><fmt:formatNumber value="${userReconciliationRecordList.items.fpcHTTJ}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td><fmt:formatNumber value="${userReconciliationRecordList.items.fusdtCTCBuy}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td><fmt:formatNumber value="${userReconciliationRecordList.items.fusdtCTCSell}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td><fmt:formatNumber value="${userReconciliationRecordList.items.fAtftransfer}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td><fmt:formatNumber value="${userReconciliationRecordList.items.fotcBuyUsdt}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td><fmt:formatNumber value="${userReconciliationRecordList.items.fotcSellUsdt}" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
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
