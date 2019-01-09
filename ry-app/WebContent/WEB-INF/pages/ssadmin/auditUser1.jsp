<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">
	会员<font color="red">${fuser.fnickName}</font>复印件认证
</h2>
<style>
	#js-identifyReason{
		display: none;
	}
</style>

<div class="pageContent">

	<form method="post" action="ssadmin/auditUser1.html"
		  class="pageForm required-validate"
		  onsubmit="return thisSubmit(this,dialogAjaxDone)">

		<div class="pageFormContent nowrap" layoutH="97">
			<dl>
				<dt>审核结果：</dt>
				<dd>
					<select type="combox" name="status" class="required" id="js-status">
						<option value="1">通过</option>
						<option value="2">不通过</option>
					</select>
				</dd>
			</dl>
			<dl id="js-identifyReason">
				<dt >原因：</dt>
				<dd>
					<textarea id="identifyReasonT" style="width: 452px;height: 40px;" type="text" name="identifyReason" />
				</dd>
			</dl>
			<dl>
				<dt>会员真实姓名：</dt>
				<dd>
					<input type="hidden" name="uid" value="${fuser.fid}" /> <input
						type="text" name="frealName" size="70" readonly="readonly"
						value="${fuser.frealName}" />
				</dd>
			</dl>
			<dl>
				<dt>证件类型：</dt>
				<dd>
					<select type="combox" name="fidentityType" readonly="true" disabled>
						<c:forEach items="${identityTypeMap}" var="identityType">
							<c:if test="${identityType.key == fuser.fidentityType}">
								<option value="${identityType.key}" selected="true">${identityType.value}</option>
							</c:if>
							<c:if test="${identityType.key != fuser.fidentityType}">
								<option value="${identityType.key}">${identityType.value}</option>
							</c:if>
						</c:forEach>
					</select>
				</dd>
			</dl>
			<dl>
				<dt>证件号码：</dt>
				<dd>
					<input type="text" name="fidentityNo" size="70" readonly="readonly"
						   value="${fuser.fidentityNo}" />
				</dd>
			</dl>
			<dl>
				<dt>本人身份证正面照片：</dt>
				<dd>
					<img src="${fuser.fIdentityPath }" width="500" />
				</dd>
			</dl>
			<dl>
				<dt>本人身份证反面照片：</dt>
				<dd>
					<img src="${fuser.fIdentityPath2 }" width="500" />
				</dd>
			</dl>
			<dl>
				<dt>手持本人身份证照片：</dt>
				<dd>
					<img src="${fuser.fIdentityPath3 }" width="500" />
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

    function thisSubmit(from,dialogAjaxDone) {
        if($('#js-identifyReason').css("display")=='block'){
            if($('#identifyReasonT').val() == ''){
                alert('请输入备注')
                return false;
			}
        }else{
            console.log('none')
        }
        return validateCallback(from,dialogAjaxDone);
    }

    $("#js-status").change(function(){
        var val =$("#js-status").val();
        var identifyReason = document.getElementById("js-identifyReason");
        if(val==2) {
            // debugger
            identifyReason.style.setProperty('display','block');
        }else {
            identifyReason.style.setProperty('display','none');
		}


    });


</script>
