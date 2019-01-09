<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">
	修改会员<font color="red">${fuser.fnickName}</font>的身份认证级别
</h2>
<div class="pageContent">
	<form method="post" action="ssadmin/auditUserAuthGrade.html"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this,dialogAjaxDone)">
		<div class="pageFormContent nowrap" layoutH="97">
			<dl>
				<dt>会员真实姓名：</dt>
				<dd>
					<input type="hidden" name="uid" value="${fuser.fid}" /> 
                    <input type="text" name="frealName" size="60" readonly="readonly" value="${fuser.frealName}" />
				</dd>
			</dl>
			<dl>
				<dt>身份认证级别：</dt>
				<dd>
                    <select id="selectRz" name="selectRz" style="width:416px"> 
  	  		    		<option value="1">K1认证</option> 
	  		    		<option value="2">K2认证</option>  
	  		    		<option value="3">K3认证</option>  
 	  		    	</select> 
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
