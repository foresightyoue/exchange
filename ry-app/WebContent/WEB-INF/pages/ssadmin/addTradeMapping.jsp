<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">添加法币类型匹配信息</h2>


<div class="pageContent">

	<form method="post" action="ssadmin/saveTradeMapping.html"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this,dialogAjaxDone)">	
		<div class="pageFormContent nowrap" layoutH="97">
			<dt>法币类型：</dt>
				<dd>
					<select
						type="combox" name="fvirtualcointype1" class="required">
						<c:forEach items="${fvirtualcointypes_fb}" var="type">
							<option value="${type.fid}">${type.fname}</option>
						</c:forEach>
					</select>
				</dd>
			</dl>
			<dl>
				<dt>交易币类型：</dt>
				<dd>
					<select type="combox" name="fvirtualcointype2" class="required">
						<c:forEach items="${fvirtualcointypes}" var="type">
							<option value="${type.fid}">${type.fname}</option>
						</c:forEach>
					</select>
				</dd>
			</dl>
			<dl>
				<dt>单价小数位：</dt>
				<dd>
					<input type="text" name="fcount1"
						class="required number" size="30" />
				</dd>
			</dl>
			<dl>
				<dt>数量小数位：</dt>
				<dd>
					<input type="text" name="fcount2"
						class="required number" size="30" />
				</dd>
			</dl>
			<dl>
				<dt>交易时间：</dt>
				<dd>
					<input type="text" name="ftradeTime"
						class="required"/>
						<span>不交易填0，24小时交易填24,8:30-18:00,早上8点半到下午六点</span>		
				</dd>
			</dl>
			<dl>
				<dt>开盘价：</dt>
				<dd>
					<input type="text" name="fprice"
						class="required number" size="30" />
				</dd>
			</dl>
            <dl>
				<dt>最小买入数量：</dt>
				<dd>
					<input type="text" name="fminBuyCount"
						class="required number" size="30" />
				</dd>
			</dl>
			<dl>
				<dt>最小买入单价：</dt>
				<dd>
					<input type="text" name="fminBuyPrice"
						class="required number" size="30" />
				</dd>
			</dl>
			<dl>
				<dt>最小买入金额：</dt>
				<dd>
					<input type="text" name="fminBuyAmount"
						class="required number" size="30" />
				</dd>
			</dl>
			 <dl>
				<dt>最小卖出数量：</dt>
				<dd>
					<input type="text" name="fminSellCount"
						class="required number" size="30" />
				</dd>
			</dl>
			<dl>
				<dt>最小卖出单价：</dt>
				<dd>
					<input type="text" name="fminSellPrice"
						class="required number" size="30" />
				</dd>
			</dl>
			<dl>
				<dt>最小卖出金额：</dt>
				<dd>
					<input type="text" name="fminSellAmount"
						class="required number" size="30" />
				</dd>
			</dl>
			<dl>
				<dt>每天最大挂单次数：</dt>
				<dd>
					<input type="text" name="fmaxtimes"
						class="required number" size="30" />
					<span>0为不限制</span>	
				</dd>
			</dl>
			 <dl>
				<dt>最大买入数量：</dt>
				<dd>
					<input type="text" name="fmaxBuyCount"
						class="required number" size="30"/>
				</dd>
			</dl>
			 <dl>
				<dt>最大卖出数量：</dt>
				<dd>
					<input type="text" name="fmaxSellCount"
						class="required number" size="30"/>
				</dd>
			</dl>
		<!-- 	<dl>
				<dt>是否限卖：</dt>
				<dd>
					<input type="checkbox" name="fislimit" checked='1' />
				</dd>
			</dl>
			<dl>
				<dt>限卖比例：</dt>
				<dd>
					<input type="text" name="ftraderate"
						class="required number" size="30" />
				</dd>
			</dl> -->
			<dl>
				<dt>级别：</dt>
				<dd>
					<select type="combox" name="ftype">
					<c:forEach items="${type}" var="t">
						<option value="${t.key}">${t.value}</option>
					</c:forEach>
		            </select>
				</dd>
			</dl>
		   <dl>
				<dt>交易说明：</dt>
				<dd>
				<textarea rows="3" cols="80" name="ftradedesc"></textarea>
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
