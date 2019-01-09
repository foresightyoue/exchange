<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<%
	String path = request.getContextPath();
%>
<h2 class="contentTitle">修改法币类型匹配信息</h2>


<div class="pageContent">

		<form method="post" action="ssadmin/updateTradeMapping.html"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this,dialogAjaxDone)">
		<div class="pageFormContent nowrap" layoutH="97">
			<dt>法币类型：</dt>
				<dd>
					<input type="hidden" name="uid" value="${ftradeMapping.fid}" />
					<span>${ftradeMapping.fvirtualcointypeByFvirtualcointype1.fname }</span>
				</dd>
			</dl>
			<dl>
				<dt>交易币类型：</dt>
				<dd>
					<span>${ftradeMapping.fvirtualcointypeByFvirtualcointype2.fname }</span>
				</dd>
			</dl>
			<dl>
				<dt>是否开放：</dt>
				<dd>
					<c:choose>
						<c:when test="${ftradeMapping.fisActive}">
							<input type="checkbox" name="fisActive" checked='1' />
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="fisActive" />
						</c:otherwise>
					</c:choose>
				</dd>
			</dl>
			<dl>
				<dt>单价小数位：</dt>
				<dd>
					<input type="text" name="fcount1"
						class="required number" size="30" value="<fmt:formatNumber value="${ftradeMapping.fcount1 }" pattern="##.######" maxIntegerDigits="20" maxFractionDigits="8"/>"/>
				</dd>
			</dl>
			<dl>
				<dt>数量小数位：</dt>
				<dd>
					<input type="text" name="fcount2"
						class="required number" size="30"  value="<fmt:formatNumber value="${ftradeMapping.fcount2 }" pattern="##.######" maxIntegerDigits="20" maxFractionDigits="8"/>"/>
				</dd>
			</dl>
			<dl>
				<dt>交易时间：</dt>
				<dd>
					<input type="text" name="ftradeTime"
						class="required" value="${ftradeMapping.ftradeTime }"/>
					<span>不交易填0，24小时交易填24,8:30-18:00,早上8点半到下午六点</span>		
				</dd>
			</dl>
			<dl>
				<dt>开盘价：</dt>
				<dd>
					<input type="text" name="fprice"
						class="required number" size="30"  value="<fmt:formatNumber value="${ftradeMapping.fprice }" pattern="##.######" maxIntegerDigits="20" maxFractionDigits="8"/>"/>
				</dd>
			</dl>
            <dl>
				<dt>最小买入数量：</dt>
				<dd>
					<input type="text" name="fminBuyCount"
						class="required number" size="30"  value="<fmt:formatNumber value="${ftradeMapping.fminBuyCount }" pattern="##.######" maxIntegerDigits="20" maxFractionDigits="8"/>"/>
				</dd>
			</dl>
			<dl>
				<dt>最小买入单价：</dt>
				<dd>
					<input type="text" name="fminBuyPrice"
						class="required number" size="30"  value="<fmt:formatNumber value="${ftradeMapping.fminBuyPrice }" pattern="##.######" maxIntegerDigits="20" maxFractionDigits="8"/>"/>
				</dd>
			</dl>
			<dl>
				<dt>最小买入金额：</dt>
				<dd>
					<input type="text" name="fminBuyAmount"
						class="required number" size="30"  value="<fmt:formatNumber value="${ftradeMapping.fminBuyAmount }" pattern="##.######" maxIntegerDigits="20" maxFractionDigits="8"/>"/>
				</dd>
			</dl>
			 <dl>
				<dt>最小卖出数量：</dt>
				<dd>
					<input type="text" name="fminSellCount"
						class="required number" size="30"  value="<fmt:formatNumber value="${ftradeMapping.fminSellCount }" pattern="##.######" maxIntegerDigits="20" maxFractionDigits="8"/>"/>
				</dd>
			</dl>
			<dl>
				<dt>最小卖出单价：</dt>
				<dd>
					<input type="text" name="fminSellPrice"
						class="required number" size="30"  value="<fmt:formatNumber value="${ftradeMapping.fminSellPrice }" pattern="##.######" maxIntegerDigits="20" maxFractionDigits="8"/>"/>
				</dd>
			</dl>
			<dl>
				<dt>最小卖出金额：</dt>
				<dd>
					<input type="text" name="fminSellAmount"
						class="required number" size="30"  value="<fmt:formatNumber value="${ftradeMapping.fminSellAmount }" pattern="##.######" maxIntegerDigits="20" maxFractionDigits="8"/>"/>
				</dd>
			</dl>
			<dl>
				<dt>每天最大买入次数：</dt>
				<dd>
					<input type="text" name="fmaxtimes"
						class="required number" size="30" value="${ftradeMapping.fmaxtimes }"/>
					<span>0为不限制</span>	
				</dd>
			</dl>
			 <dl>
				<dt>最大买入数量：</dt>
				<dd>
					<input type="text" name="fmaxBuyCount"
						class="required number" size="30"  value="<fmt:formatNumber value="${ftradeMapping.fmaxBuyCount }" pattern="##.######" maxIntegerDigits="20" maxFractionDigits="8"/>"/>
				</dd>
			</dl>
			 <dl>
				<dt>最大卖出数量：</dt>
				<dd>
					<input type="text" name="fmaxSellCount"
						class="required number" size="30"  value="<fmt:formatNumber value="${ftradeMapping.fmaxSellCount }" pattern="##.######" maxIntegerDigits="20" maxFractionDigits="8"/>"/>
				</dd>
			</dl>
			<dl>
				<dt>级别：</dt>
				<dd>
					<select type="combox" name="ftype">
					<c:forEach items="${type}" var="t">
					<c:if test="${t.key == ftradeMapping.ftype}">
				    	<option value="${t.key}" selected="true">${t.value}</option>
					</c:if>
					<c:if test="${t.key != ftradeMapping.ftype}">
				    	<option value="${t.key}">${t.value}</option>
					</c:if>
					</c:forEach>
		            </select>
				</dd>
			</dl>
			<dl>
				<dt>交易说明：</dt>
				<dd>
				<textarea rows="3" cols="80" name="ftradedesc">${ftradeMapping.ftradedesc}</textarea>
				</dd>
			</dl>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive">
						<div class="buttonContent">
							<button type="submit">保存</button>
						</div>
					</div></li>
				<li><div class="button">
						<div class="buttonContent">
							<button type="button" class="close">取消</button>
						</div>
					</div></li>
			</ul>
		</div>
	</form>

</div>


<script type="text/javascript">
function customvalidXxx(element){
	if ($(element).val() == "xxx") return false;
	return true;
}
</script>
