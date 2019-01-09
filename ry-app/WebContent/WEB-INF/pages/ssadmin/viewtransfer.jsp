<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">
	审核虚拟币划转<font color="red">(此过程不可逆,请谨慎操作)</font>
</h2>


<div class="pageContent">

	<form method="post" action="ssadmin/transferAuditing.html"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this,dialogAjaxDone)">
		<div class="pageFormContent nowrap" layoutH="97">
			<dl>
				<dt>登录名：</dt>
				<dd>
					<input type="hidden" name="fid" value="${ftransfer.fId}" />
					<input type="text" name="fname" maxlength="30" size="40" value="${ftransfer.frealName}" disabled="true" />
				</dd>
			</dl>
			<dl>
				<dt>虚拟币类型：</dt>
				<dd>
					<select type="combox" name="virtualcointype.fid" disabled="true">
						<option value="${ftransfer.cFid}">${ftransfer.fname}</option>
					</select>
				</dd>
			</dl>
			<dl>
				<dt>划转数量：</dt>
				<dd>
					<input type="text" name=famount maxlength="30"
						class="required number" size="40" value="${ftransfer.fAmount}"
						disabled="true" />
				</dd>
			</dl>
			<dl>
				<dt>划转账号：</dt>
				<dd>
				    <input type="hidden" name="account" value="${ftransfer.fAccount}"/>
					<input type="text" name="withdraw_virtual_address" maxlength="30"
						class="required number" size="40" value="${ftransfer.fAccount}"
						disabled="true" />
				</dd>
			</dl>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive">
						<div class="buttonContent">
							<button type="submit">审核</button>
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
	function customvalidXxx(element) {
		if ($(element).val() == "xxx")
			return false;
		return true;
	}
</script>
