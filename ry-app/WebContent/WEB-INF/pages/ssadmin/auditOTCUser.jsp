<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">
    会员<font color="red">${fuser.fnickName}</font>商家OTC认证
</h2>


<div class="pageContent">

    <form method="post" action="ssadmin/auditUser.html?type=otc"
        class="pageForm required-validate"
        onsubmit="return validateCallback(this,dialogAjaxDone)">
        <div class="pageFormContent nowrap" layoutH="97">
            <dl>
                <dt>审核结果：</dt>
                <dd>
                    <select type="combox" name="status" class="required">
                        <option value="1">通过</option>
                        <option value="2">不通过</option>
                    </select>
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
                <dt>商家等级：</dt>
                <dd>
                    <input type="hidden" name="fSellerLevel" value="${fuser.fSellerLevel}" /> <input
                        type="text" name="fSellerLevel" size="70" readonly="readonly"
                        value="${fuser.fSellerLevel}" />
                </dd>
            </dl>
            <dl>
                <dt>商家名称：</dt>
                <dd>
                    <input type="text" name="fSellerName" size="70" readonly="readonly"
                        value="${fuser.fSellerName}" />
                </dd>
            </dl>
            <dl>
                <dt>认证时间：</dt>
                <dd>
                    <input type="text" name="fSellerTime" size="70" readonly="readonly"
                           value="${fuser.fSellerTime}" />
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
