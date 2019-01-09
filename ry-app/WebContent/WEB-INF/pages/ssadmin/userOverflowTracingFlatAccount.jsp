<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<style>
.details_table,.details_table tr th, .details_table tr td { 
	border:1px solid #999; 
	padding: 5px;
}
.details_table{
    border-collapse: collapse;
    border: 1px solid;
    width: 100%;
    margin-top: 20px;	
}
</style>
<h2 class="contentTitle">
 	修改会员<font color="red">${fnickName}</font>的钱包余额 
</h2>
<div class="pageContent">
	<form method="post" action="#"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this,dialogAjaxDone)">
		
		<div class="pageFormContent nowrap" layoutH="97">
		<c:forEach items="${UserOverflowTracingFlatAccount}"  var="UserOverflowTracingFlatAccountlist" varStatus="num">
			<dl>
				<dt>会员真实姓名：</dt>
				<dd>
					<input type="hidden" name="uid" value="${UserOverflowTracingFlatAccountlist.items.fid}" />
					<input type="hidden" name="fDifferenceUsdt" value="${UserOverflowTracingFlatAccountlist.items.fDifferenceUsdt}" /> 
					<input type="hidden" name="fDifferenceUsdtzen" value="${UserOverflowTracingFlatAccountlist.items.fDifferenceUsdtzen}" /> 
					<input type="hidden" name="fDifferenceAt" value="${UserOverflowTracingFlatAccountlist.items.fDifferenceAt}" /> 
					<input type="hidden" name="fDifferenceAtzen" value="${UserOverflowTracingFlatAccountlist.items.fDifferenceAtzen}" /> 
					<input type="hidden" name="fDifferencePc" value="${UserOverflowTracingFlatAccountlist.items.fDifferencePc}" />  
					<input type="hidden" name="fDifferencePczen" value="${UserOverflowTracingFlatAccountlist.items.fDifferencePczen}" /> 
                    <input type="text" name="frealName" size="40" readonly="readonly" value="${UserOverflowTracingFlatAccountlist.items.frealName}" />
				</dd>
			</dl>
			
			<table class="details_table">
				<tr>
					<td></td>
					<td>Usdt可用</td>
					<td>Usdt冻结</td>
					<td>AT可用</td>
					<td>AT冻结</td>
					<td>PC可用</td>
					<td>PC冻结</td>
				</tr>
				<tr>
					<td>开始金额</td>
					<td><fmt:formatNumber value="${UserOverflowTracingFlatAccountlist.items.fStartUsdt }" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td><fmt:formatNumber value="${UserOverflowTracingFlatAccountlist.items.fStartUsdtzen }" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td><fmt:formatNumber value="${UserOverflowTracingFlatAccountlist.items.fStartAt }" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td><fmt:formatNumber value="${UserOverflowTracingFlatAccountlist.items.fStartAtzen }" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td><fmt:formatNumber value="${UserOverflowTracingFlatAccountlist.items.fStartPc }" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td><fmt:formatNumber value="${UserOverflowTracingFlatAccountlist.items.fStartPczen }" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
				</tr>
				<tr>
					<td>结束金额</td>
					<td><fmt:formatNumber value="${UserOverflowTracingFlatAccountlist.items.fEndUsdt }" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td><fmt:formatNumber value="${UserOverflowTracingFlatAccountlist.items.fEndUsdtzen }" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td><fmt:formatNumber value="${UserOverflowTracingFlatAccountlist.items.fEndAt }" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td><fmt:formatNumber value="${UserOverflowTracingFlatAccountlist.items.fEndAtzen }" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td><fmt:formatNumber value="${UserOverflowTracingFlatAccountlist.items.fEndPc }" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td><fmt:formatNumber value="${UserOverflowTracingFlatAccountlist.items.fEndPczen }" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
				</tr>
				<tr>
					<td>变动金额</td>
					<td><fmt:formatNumber value="${UserOverflowTracingFlatAccountlist.items.fUsdtChangeSum }" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td><fmt:formatNumber value="${UserOverflowTracingFlatAccountlist.items.fUsdtzenChangeSum }" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td><fmt:formatNumber value="${UserOverflowTracingFlatAccountlist.items.fAtChangeSum }" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td><fmt:formatNumber value="${UserOverflowTracingFlatAccountlist.items.fAtzenChangeSum }" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td><fmt:formatNumber value="${UserOverflowTracingFlatAccountlist.items.fPcChangeSum }" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td><fmt:formatNumber value="${UserOverflowTracingFlatAccountlist.items.fPczenChangeSum }" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
				</tr>
				<tr>
					<td>差额</td>
					<td style="color:red"><fmt:formatNumber value="${UserOverflowTracingFlatAccountlist.items.fDifferenceUsdt }" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td style="color:red"><fmt:formatNumber value="${UserOverflowTracingFlatAccountlist.items.fDifferenceUsdtzen }" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td style="color:red"><fmt:formatNumber value="${UserOverflowTracingFlatAccountlist.items.fDifferenceAt }" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td style="color:red"><fmt:formatNumber value="${UserOverflowTracingFlatAccountlist.items.fDifferenceAtzen }" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td style="color:red"><fmt:formatNumber value="${UserOverflowTracingFlatAccountlist.items.fDifferencePc }" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
					<td style="color:red"><fmt:formatNumber value="${UserOverflowTracingFlatAccountlist.items.fDifferencePczen }" pattern="##.######" maxIntegerDigits="28" maxFractionDigits="8"/></td>
				</tr>
			</table>
			</c:forEach>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive">
						<div class="buttonContent">
							<button type="submit">保存</button>
						</div>
					</div>
				</li>
				<li><div class="button">
						<div class="buttonContent">
							<button type="button" class="close">取消</button>
						</div>
					</div>
				</li>
			</ul>
		</div>
	</form>

</div>
