<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">修改管理员<font color="red">${fadmin.fname}</font>的绑定手机号</h2>
<div class="pageContent">
	<form method="post" action="ssadmin/updatePhone.html"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this,dialogAjaxDone)">
		<div class="pageFormContent nowrap" layoutH="97">
			<dl>
				<dt>管理员名：</dt>
				<dd>
					<input type="hidden" name="uid" value="${fadmin.fid}" />
					<input type="text" name="fname" size="70" readonly="readonly" value="${fadmin.fname}" />
				</dd>
			</dl>
			<dl>
				<dt>已绑定手机号：</dt>
				<dd>
                     <input type="text" name="fphone" size="70" readonly="readonly" value="${fadmin.fphone}" />
				</dd>
			</dl>
			<dl>
				<dt>新绑定手机号：</dt>
				<dd>
                     <input type="text" name="fNewPhone" size="70"  value="" />
				</dd>
			</dl>
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


<script type="text/javascript">
function customvalidXxx(element){
	if ($(element).val() == "xxx") return false;
	return true;
}
</script>
