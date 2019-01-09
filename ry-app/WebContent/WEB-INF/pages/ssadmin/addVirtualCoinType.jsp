<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">添加虚拟币类型信息</h2>


<div class="pageContent">

	<form method="post" action="ssadmin/saveVirtualCoinType.html"
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
					<input type="text" name="fname" class="required"
						size="70" />
				</dd>
			</dl>
			<dl>
				<dt>币种简称：</dt>
				<dd>
					<input type="text" name="fShortName"
						class="required" size="70" />
				</dd>
			</dl>
			<dl>
				<dt>符号：</dt>
				<dd>
					<input type="text" name="fSymbol" maxlength="1" class="required"
						size="40" />
				</dd>
			</dl>
			<c:if test="${walletConfigEnable == 'true'}">
				<dl>
					<dt>ACCESS_KEY：</dt>
					<dd>
						<input type="password" name="faccess_key"
							   class="required" size="70" />
					</dd>
				</dl>
				<dl>
					<dt>SECRT_KEY：</dt>
					<dd>
						<input type="password" name="fsecrt_key"
							   class="required" size="70" />
					</dd>
				</dl>
				<dl>
					<dt>IP地址：</dt>
					<dd>
						<input type="text" name="fip" class="required"
							   size="70" />
					</dd>
				</dl>
				<dl>
					<dt>端口号：</dt>
					<dd>
						<input type="text" name="fport"
							   class="required number" size="30" />
					</dd>
				</dl>
			</c:if>
			<dl>
				<dt>是否可以充值：</dt>
				<dd>
					<input type="checkbox" name="fisrecharge" checked='1' />
				</dd>
			</dl>
			<dl>
				<dt>是否可以提现：</dt>
				<dd>
					<input type="checkbox" name="FIsWithDraw" checked='1' />
				</dd>
			</dl>
			<dl>
				<dt>是否可以c2c交易：</dt>
				<dd>
					<input type="checkbox" name="fisCtcCoin" />
				</dd>
			</dl>
			<dl>
				<dt>充值是否自动到账：</dt>
				<dd>
					<input type="checkbox" name="fisauto" checked='1' />
				</dd>
			</dl>
			<!-- <dl>
				<dt>是否开启转账：</dt>
				<dd>
					<input type="checkbox" name="fistransport" checked='1' />
				</dd>
			</dl> -->
			<dl>
				<dt>总量：</dt>
				<dd>
					<input type="text" name="ftotalqty"
						class="required number" size="30" />
				</dd>
			</dl>
			<dl>
				<dt>是否自动提币：</dt>
				<dd>
				<input type="checkbox" name="fisautosend" />
				</dd>
			</dl>
			<c:if test="${walletConfigEnable == 'true'}">
				<dl>
					<dt>钱包密码：</dt>
					<dd>
						<input type="password" name="fpassword"
							   class="" size="70"/>
					</dd>
				</dl>
			</c:if>

			 <dl>
				<dt>充值须知：</dt>
				<dd>
				<textarea rows="3" cols="80" name="fregDesc"></textarea>
				</dd>
			</dl>
			 <dl>
				<dt>提现须知：</dt>
				<dd>
				<textarea rows="3" cols="80" name="fwidDesc"></textarea>
				</dd>
			</dl>
			<dl>
				<dt>是否ETH：</dt>
				<dd>
					<input type="checkbox" name="fisEth" />
				</dd>
			</dl>
			<dl>
			<dt>是否ETH代币：</dt>
			<dd>
				<input type="checkbox"  id="istoken" name="fisToken" />
			</dd>
			</dl>
			<dl id="is" style="display: none;">
				<dt>ContractID：</dt>
				<dd>
					<input type="text" name="faddress"
						class="" size="70" value=""/>
				</dd>
			</dl>
			<c:if test="${walletConfigEnable == 'true'}">
				<dl>
					<dt>汇总地址：</dt>
					<dd>
						<input type="text" name="mainAddr"
							   class="" size="70" />
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
