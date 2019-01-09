<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">添加平台钱包转账记录</h2>


<div class="pageContent">

    <form method="post" action="ssadmin/saveTransferRecord.html"
        class="pageForm required-validate"
        onsubmit="return validateCallback(this,dialogAjaxDone)">
        <div class="pageFormContent nowrap" layoutH="97">
            <dl>
                <dt>虚拟币名称：</dt>
                <dd>
                    <select type="combox" name="fCoinType">
                        <c:forEach items="${allType}" var="type">
                            <c:if test="${type.fid == vid}">
                                <option value="${type.fid}" selected="true">${type.fname}</option>
                            </c:if>
                            <c:if test="${type.fid != vid}">
                                <option value="${type.fid}">${type.fname}</option>
                            </c:if>
                        </c:forEach>
                    </select>
                </dd>
            </dl>
            <dl>
                <dt>操作类型</dt>
                <dd>
                    <select type="combox" name="fType">
                        <option value="1" selected="true">转入</option>
                        <option value="0">转出</option>
                    </select>
                </dd>
             </dl>
            <dl>
                <dt>数量：</dt>
                <dd>
                    <input type="text" name="fCount" maxlength="50" size="40"
                        class="number required" />
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
