<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">
    设置<font color="red">${fuser.fnickName}</font>会员身份
</h2>
<div class="pageContent">
    <form method="post" action="ssadmin/setSystemPro.html"
        class="pageForm required-validate"
        onsubmit="return validateCallback(this,dialogAjaxDone)">
        <div class="pageFormContent nowrap" layoutH="97">
            <dl>
                <dt>会员真实姓名：</dt>
                <dd>
                    <input type="hidden" name="uid" value="${fuser.fid}" /> 
                    <input type="text" name="frealName" size="70" readonly="readonly" value="${fuser.frealName}" />
                </dd>
            </dl>
            <dl>
                <dt>是否有登录权限:</dt>
                    <select id="fCanLoginTeamManager" name="fCanLoginTeamManager">
                       <option value="0" >不具备登录权限</option>
                       <option value="1" >具备登录权限</option>
                    </select>
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
$(function() {
	var fCanLoginTeamManager = "${fuser.fCanLoginTeamManager}" == 'true' ? "1":"0";
    $("#fCanLoginTeamManager").val(fCanLoginTeamManager);
});
</script>