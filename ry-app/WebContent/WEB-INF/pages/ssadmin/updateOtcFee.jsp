<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">
	修改<font color="red">${virtualCoinType.fname}</font>手续费信息
</h2>

<div class="pageContent">
	<form method="post" action="ssadmin/updateOTCFee.html"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this,dialogAjaxDone)">
		<div class="pageFormContent nowrap" layoutH="97">
		<input type="hidden" name="fid" value="${virtualCoinType.fid}"/>
			<c:forEach items="${list}" var="fee" varStatus="status">
				<dl>
					<dt>
						等级[ <b><font color="red"> ${fee.flevel}  </font> </b> ]OTC手续费：
					</dt>
					<dd>
						<input type="text" name="fee${status.index+1}"
							value="${fee.ffee}" 
							size="70" class="required number" />
					</dd>
				</dl>		
			</c:forEach>
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
