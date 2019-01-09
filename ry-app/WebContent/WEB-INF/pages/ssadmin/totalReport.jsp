<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
</head>
<body>
	<div class="pageFormContent" layoutH="20">

		<fieldset>
			<legend>会员信息</legend>
			<dl>
				<dt>全站总会员数：</dt>
				<dd style="color:red;font-weight:bold;">
					<span class="unit">${totalUser}</span>
				</dd>
			</dl>
			<dl>
				<dt>全站已认证会员数：</dt>
				<dd style="color:red;font-weight:bold;">
					<span class="unit">${totalValidateUser}</span>
				</dd>
			</dl>
			<dl>
				<dt>今日新增会员数：</dt>
				<dd style="color:red;font-weight:bold;">
					<span class="unit">${todayTotalUser}</span>
				</dd>
			</dl>
			<dl>
				<dt>今日已认证会员数：</dt>
				<dd style="color:red;font-weight:bold;">
					<span class="unit">${todayValidateUser}</span>
				</dd>
			</dl>
		</fieldset>

		<fieldset>
			<legend>今日充值信息</legend>
			<c:forEach items="${amountInMap }" var="v">
			<dl>
				<dt >今日${v.key }充值金额：</dt>
				<dd style="color:red;font-weight:bold;">
					<span class="unit">￥<fmt:formatNumber
								value="${v.value}" pattern="#0.######" /></span>
				</dd>
			</dl>
			</c:forEach>
			 <dl>
				<dt>今日手工充值金额：</dt>
				<dd style="color:red;font-weight:bold;">
					<span class="unit">￥<fmt:formatNumber
								value="${todayOpAmt}" pattern="#0.######" /></span>
				</dd>
			</dl>
		</fieldset>
		
		<fieldset>
			<legend>累计充值信息</legend>
			<c:forEach items="${totalAmountInMap }" var="v">
			<dl>
				<dt >累计${v.key }充值金额：</dt>
				<dd style="color:red;font-weight:bold;">
					<span class="unit">￥<fmt:formatNumber
								value="${v.value}" pattern="#0.######" /></span>
				</dd>
			</dl>
			</c:forEach>
			<dl>
				<dt>累计手工充值金额：</dt>
				<dd style="color:red;font-weight:bold;">
					<span class="unit">￥<fmt:formatNumber
								value="${totalOpAmt}" pattern="#0.######" /></span>
				</dd>
			</dl>
		</fieldset>

		<fieldset>
			<legend>全部资金统计</legend>
			<c:forEach items="${virtualQtyList}" var="virtual">
				<dl>
					<dt>全站${virtual.fName}：</dt>
					<dd style="color:red;font-weight:bold;">
						<span class="unit"><fmt:formatNumber
								value="${virtual.totalQty}" pattern="#0.######" /> </span>
					</dd>
				</dl>
				<dl>
					<dt>含冻结：</dt>
					<dd style="color:red;font-weight:bold;">
						<span class="unit"><fmt:formatNumber
								value="${virtual.frozenQty}" pattern="#0.######" /> </span>
					</dd>
				</dl>
			</c:forEach>
		</fieldset>

		<fieldset>
			<legend>委托未成交统计</legend>
			<c:forEach items="${entrustBuyMap}" var="entrustBuy">
				<dl>
					<dt>买入${entrustBuy.fName}数量：</dt>
					<dd style="color:red;font-weight:bold;">
						<span class="unit"><fmt:formatNumber
								value="${entrustBuy.total}" pattern="#0.######" /> </span>
					</dd>
				</dl>
			</c:forEach>
			<c:forEach items="${entrustSellMap}" var="entrustSell">
				<dl>
					<dt>卖出${entrustSell.fName}数量：</dt>
					<dd style="color:red;font-weight:bold;">
						<span class="unit"><fmt:formatNumber
								value="${entrustSell.total}" pattern="#0.######" /> </span>
					</dd>
				</dl>
			</c:forEach>
		</fieldset>


		<fieldset>
			<legend>累计待提现信息</legend>
			<dl>
				<dt>人民币金额：</dt>
				<dd style="color:red;font-weight:bold;">
					<span class="unit">￥<fmt:formatNumber
							value="${amountOutWaitingMap.totalAmount}" pattern="#0.######" />
					</span>
				</dd>
			</dl>
			<c:forEach items="${virtualOutWaitingMap}" var="virtualOutWaiting">
				<dl>
					<dt>${virtualOutWaiting.fName}数量：</dt>
					<dd style="color:red;font-weight:bold;">
						<span class="unit"><fmt:formatNumber
								value="${virtualOutWaiting.amount}" pattern="#0.######" /> </span>
					</dd>
				</dl>
			</c:forEach>
		</fieldset>

		<fieldset>
			<legend>今日已提现汇总信息</legend>
			<dl>
				<dt>今日人民币提现金额：</dt>
				<dd style="color:red;font-weight:bold;">
					<span class="unit">￥<fmt:formatNumber
							value="${amountOutMap.totalAmount}" pattern="#0.######" /> </span>
				</dd>
			</dl>
			<c:forEach items="${virtualOutSuccessMap}" var="virtualOutSuccess">
				<dl>
					<dt>今日${virtualOutSuccess.fName}提现数量：</dt>
					<dd style="color:red;font-weight:bold;">
						<span class="unit"><fmt:formatNumber
								value="${virtualOutSuccess.amount}" pattern="#0.######" /> </span>
					</dd>
				</dl>
			</c:forEach>
		</fieldset>
		
		<fieldset>
			<legend>累计已提现汇总信息</legend>
			<dl>
				<dt>累计人民币提现金额：</dt>
				<dd style="color:red;font-weight:bold;">
					<span class="unit">￥<fmt:formatNumber
							value="${amountOutMap1.totalAmount}" pattern="#0.######" /> </span>
				</dd>
			</dl>
			<c:forEach items="${virtualOutSuccessTotalMap}" var="virtualOutSuccess">
				<dl>
					<dt>累计${virtualOutSuccess.fName}提现数量：</dt>
					<dd style="color:red;font-weight:bold;">
						<span class="unit"><fmt:formatNumber
								value="${virtualOutSuccess.amount}" pattern="#0.######" /> </span>
					</dd>
				</dl>
			</c:forEach>
		</fieldset>
		
		<fieldset>
			<legend>今日提现手续费汇总</legend>
			<dl>
				<dt>今日手续费金额：</dt>
				<dd style="color:red;font-weight:bold;">
					<span class="unit">￥<fmt:formatNumber
							value="${amountOutFeeMap1.totalAmount}" pattern="#0.######" /> </span>
				</dd>
			</dl>
			<c:forEach items="${virtualOutFeesMap}" var="virtualOutSuccess">
				<dl>
					<dt>今日${virtualOutSuccess.fName}提现手续费数量：</dt>
					<dd style="color:red;font-weight:bold;">
						<span class="unit"><fmt:formatNumber
								value="${virtualOutSuccess.amount}" pattern="#0.######" /> </span>
					</dd>
				</dl>
			</c:forEach>
		</fieldset>
		
		<fieldset>
			<legend>累计提现手续费汇总</legend>
			<dl>
				<dt>累计手续费金额：</dt>
				<dd style="color:red;font-weight:bold;">
					<span class="unit">￥<fmt:formatNumber
							value="${amountOutFeeMap2.totalAmount}" pattern="#0.######" /> </span>
				</dd>
			</dl>
			<c:forEach items="${virtualOutFeesTotalMap}" var="virtualOutSuccess">
				<dl>
					<dt>累计${virtualOutSuccess.fName}提现手续费数量：</dt>
					<dd style="color:red;font-weight:bold;">
						<span class="unit"><fmt:formatNumber
								value="${virtualOutSuccess.amount}" pattern="#0.######" /> </span>
					</dd>
				</dl>
			</c:forEach>
		</fieldset>
		
		<fieldset>
			<legend>交易手续费信息(人民币)</legend>
			<c:forEach items="${entrustSellFeesMap1}" var="entrustSell">
				<dl>
					<dt>今日卖出${entrustSell.fName}手续费：</dt>
					<dd style="color:red;font-weight:bold;">
						<span class="unit">￥<fmt:formatNumber
								value="${entrustSell.total}" pattern="#0.######" /> </span>
					</dd>
				</dl>
			</c:forEach>
			<c:forEach items="${entrustSellFeesMap2}" var="entrustSell">
				<dl>
					<dt>累计卖出${entrustSell.fName}手续费：</dt>
					<dd style="color:red;font-weight:bold;">
						<span class="unit">￥<fmt:formatNumber
								value="${entrustSell.total}" pattern="#0.######" /> </span>
					</dd>
				</dl>
			</c:forEach>
		</fieldset>
		
		<fieldset>
			<legend>交易手续费信息(虚拟币)</legend>
			<c:forEach items="${entrustBuyFeesMap1}" var="entrustSell">
				<dl>
					<dt>今日买入${entrustSell.fName}手续费：</dt>
					<dd style="color:red;font-weight:bold;">
						<span class="unit"><fmt:formatNumber
								value="${entrustSell.total}" pattern="#0.######" /> </span>
					</dd>
				</dl>
			</c:forEach>
			<c:forEach items="${entrustBuyFeesMap2}" var="entrustSell">
				<dl>
					<dt>累计买入${entrustSell.fName}手续费：</dt>
					<dd style="color:red;font-weight:bold;">
						<span class="unit"><fmt:formatNumber
								value="${entrustSell.total}" pattern="#0.######" /> </span>
					</dd>
				</dl>
			</c:forEach>
		</fieldset>
		
		<fieldset>
			<legend>交易手续费汇总信息(人民币)</legend>
			<dl style="width: 900px;">
					<dt style="width: 200px;">今日交易手续费金额：</dt>
					<dd style="color:red;font-weight:bold;">
						<span class="unit">￥<fmt:formatNumber
								value="${entrustSellFeesMap11}" pattern="#0.##" /> </span>
					</dd>
					<dt style="width: 200px;">累计交易手续费金额：</dt>
					<dd style="color:red;font-weight:bold;">
						<span class="unit">￥<fmt:formatNumber
								value="${entrustSellFeesMap22}" pattern="#0.##" /> </span>
					</dd>
				</dl>
			
		</fieldset>	
	</div>
</body>
</html>

