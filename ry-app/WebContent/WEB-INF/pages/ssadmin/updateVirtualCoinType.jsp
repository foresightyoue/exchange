<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>

<h2 class="contentTitle">修改虚拟币类型信息</h2>


<div class="pageContent">

	<form method="post" action="ssadmin/updateVirtualCoinType.html"
		class="pageForm required-validate" enctype="multipart/form-data"
		onsubmit="return iframeCallback(this, dialogAjaxDone);">
		<div class="pageFormContent nowrap" layoutH="97">
		    <dl>
				<dt>图标链接：</dt>
				<dd>
					<input type="file" class="inputStyle" value="" name="filedata"
						id="filedata" />
				</dd>
			</dl>
			<dl>
				<dt>币种名称：</dt>
				<dd>
					<input type="hidden" name="fid" value="${virtualCoinType.fid}" />
					<input type="text" name="fname" class="required"
						size="70" value="${virtualCoinType.fname}"  readonly="readonly"/>
				</dd>
			</dl>
			<dl>
				<dt>币种简称：</dt>
				<dd>
					<input type="text" name="fShortName"
						class="required" size="70" value="${virtualCoinType.fShortName}"  readonly="readonly"/>
				</dd>
			</dl>
			<dl>
				<dt>符号：</dt>
				<dd>
					<input type="text" name="fSymbol" class="required"
						size="40" value="${virtualCoinType.fSymbol}"  readonly="readonly"/>
				</dd>
			</dl>
			<c:if test="${walletConfigEnable == 'true'}">
				<dl>
					<dt>ACCESS_KEY：</dt>
					<dd>
						<input type="password" name="faccess_key"
							   class="required" size="70" value="${virtualCoinType.faccess_key}" />
					</dd>
				</dl>
				<dl>
					<dt>SECRT_KEY：</dt>
					<dd>
						<input type="password" name="fsecrt_key"
							   class="required" size="70" value="${virtualCoinType.fsecrt_key}" />
					</dd>
				</dl>
				<dl>
					<dt>IP地址：</dt>
					<dd>
						<input type="text" name="fip" class="required"
							   size="70" value="${virtualCoinType.fip}" />
					</dd>
				</dl>
				<dl>
					<dt>端口号：</dt>
					<dd>
						<input type="text" name="fport"
							   class="required number" size="30" value="${virtualCoinType.fport}" />
					</dd>
				</dl>
			</c:if>

			<dl>
				<dt>是否otc开放：</dt>
				<dd>
					<c:choose>
						<c:when test="${virtualCoinType.fisActive}">
							<input type="checkbox" name="fisActive" checked='1' />
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="fisActive" />
						</c:otherwise>
					</c:choose>
				</dd>
			</dl>
			<dl>
				<dt>是否可以充值：</dt>
				<dd>
					<c:choose>
						<c:when test="${virtualCoinType.fisrecharge}">
							<input type="checkbox" name="fisrecharge" checked='1' />
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="fisrecharge" />
						</c:otherwise>
					</c:choose>
				</dd>
			</dl>
			<dl>
				<dt>是否可以提现/转出：</dt>
				<dd>
					<c:choose>
						<c:when test="${virtualCoinType.FIsWithDraw}">
							<input type="checkbox" name="FIsWithDraw" checked='1' />
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="FIsWithDraw" />
						</c:otherwise>
					</c:choose>
				</dd>
			</dl>
			<dl>
				<dt>每天允许提现次数：</dt>
				<dd>
					<input type="text" name="fWithDrawTime" value="${virtualCoinType.fWithdrawTimes}" oninput="value=value.replace(/[^\d]/g,'')"/>
				</dd>
			</dl>
			<dl>
				<dt>每天提现百分比*100：</dt>
				<dd>
					<input type="text" name="fWithDrawLimit" value="${virtualCoinType.fWithdrawLimit}" oninput="value=value.replace(/[^\d]/g,'')"/>
				</dd>
			</dl>
			<dl>
				<dt>是否可以c2c交易：</dt>
				<dd>
					<c:choose>
						<c:when test="${virtualCoinType.fisCtcCoin}">
							<input type="checkbox" name="fisCtcCoin" checked='1' />
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="fisCtcCoin" />
						</c:otherwise>
					</c:choose>
				</dd>
			</dl>
			<dl>
				<dt>是否可以otc交易：</dt>
				<dd>
					<c:choose>
						<c:when test="${virtualCoinType.fisOtcCoin}">
							<input type="checkbox" name="fisOtcCoin" checked='1' />
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="fisOtcCoin" />
						</c:otherwise>
					</c:choose>
				</dd>
			</dl>
			<dl>
				<dt>充值是否自动到账：</dt>
				<dd>
					<c:choose>
						<c:when test="${virtualCoinType.fisauto}">
							<input type="checkbox" name="fisauto" checked='1' />
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="fisauto" />
						</c:otherwise>
					</c:choose>
				</dd>
			</dl>
			
			<%-- <dl>
				<dt>是否开启转账：</dt>
				<dd>
				    <c:choose>
						<c:when test="${virtualCoinType.fistransport}">
							<input type="checkbox" name="fistransport" checked='1' />
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="fistransport" />
						</c:otherwise>
					</c:choose>
				</dd>
			</dl> --%>
			<dl>
				<dt>总量：</dt>
				<dd>
					<input type="text" name="ftotalqty"
						class="required number" size="30" value="<fmt:formatNumber value="${virtualCoinType.ftotalqty}" pattern="##.######" maxIntegerDigits="15" maxFractionDigits="4"/>"/>
				</dd>
			</dl>
			<dl>
				<dt>c2c卖出人民币价格：</dt>
				<dd>
					<input type="text" name="fctcSellPrice"
						class="required number" size="30" value="<fmt:formatNumber value="${virtualCoinType.fctcSellPrice}" pattern="##.######" maxIntegerDigits="15" maxFractionDigits="4"/>"/>
				</dd>
			</dl>
			<dl>
				<dt>c2c买入人民币价格：</dt>
				<dd>
					<input type="text" name="fctcBuyPrice"
						class="required number" size="30" value="<fmt:formatNumber value="${virtualCoinType.fctcBuyPrice}" pattern="##.######" maxIntegerDigits="15" maxFractionDigits="4"/>"/>
				</dd>
			</dl>
			<dl>
				<dt>是否自动提币：</dt>
				<dd>
				<c:choose>
						<c:when test="${virtualCoinType.fisautosend}">
							<input type="checkbox" name="fisautosend" checked='1' />
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="fisautosend" />
						</c:otherwise>
					</c:choose>
				</dd>
			</dl>
			<c:if test="${walletConfigEnable == 'true'}">
				<dl>
					<dt>钱包密码：</dt>
					<dd>
						<input type="password" name="fpassword"
							   class="" size="70" value="${virtualCoinType.fpassword }"/>
					</dd>
				</dl>
			</c:if>
			 <dl>
				<dt>充值须知：</dt>
				<dd>
				<textarea rows="3" cols="80" name="fregDesc">${virtualCoinType.fregDesc }</textarea>
				</dd>
			</dl>
			 <dl>
				<dt>提现须知：</dt>
				<dd>
				<textarea rows="3" cols="80" name="fwidDesc">${virtualCoinType.fwidDesc }</textarea>
				</dd>
			</dl>
			<dl>
				<dt>是否ETH：</dt>
				<dd>
				<c:choose>
						<c:when test="${virtualCoinType.fisEth}">
							<input type="checkbox" name="fisEth" checked='1' />
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="fisEth" />
						</c:otherwise>
					</c:choose>
				</dd>
			</dl>
			<dl>
				<dt>是否ETH代币：</dt>
				<dd>
					<c:choose>
						<c:when test="${virtualCoinType.fisToken}">
							<input type="checkbox" id="istoken" name="fisToken" checked='1' />
						</c:when>
						<c:otherwise>
							<input type="checkbox" id="istoken" name="fisToken" />
						</c:otherwise>
					</c:choose>
				</dd>
			</dl>
			<dl id="is" style="display: none;">
				<dt>ContractID：</dt>
				<dd>
					<input type="text" name="faddress"
						class="" size="70" value="${virtualCoinType.faddress }"/>
				</dd>
			</dl>
			<c:if test="${walletConfigEnable == 'true'}">
				<dl>
					<dt>汇总地址：</dt>
					<dd>
						<input type="text" name="mainAddr"
							   class="" size="70" value="${virtualCoinType.mainAddr }"/>
					</dd>
				</dl>
			</c:if>
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
<script type="text/javascript">
	$(function(){
		if($("#istoken").is(':checked')){
			$("#is").css("display","block");
		}else{
			$("#is").css("display","none");
		}
		$("#istoken").click(function(){
			if($("#istoken").is(':checked')){
				$("#is").css("display","block");
			}else{
				$("#is").css("display","none");
			}
		});
	});
</script>
