<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">
	修改提现自动审核起始值
</h2>
<div class="pageContent">
	<form method="post" action="ssadmin/updatePrice.html"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this,dialogAjaxDone)">
		<div class="pageFormContent nowrap" layoutH="97">
			<dl>
				<dt>最小值：</dt>
				<dd>
					<input type="hidden" name="uid" value="${fuser.fid}" /> 
                    <input type="text" name="begin" size="70"  value="${begin}" />
				</dd>
			</dl>
			<dl>
				<dt>最大值：</dt>
				<dd>
                     <input type="text" name="end" size="70"  value="${end}" />
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
