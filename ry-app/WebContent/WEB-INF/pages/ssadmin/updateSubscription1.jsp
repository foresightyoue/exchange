<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">修改众筹信息</h2>

<div class="pageContent">

	<form method="post" action="ssadmin/updateSubscription1.html"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this,dialogAjaxDone)">
		<div class="pageFormContent nowrap" layoutH="97">
		     <dl>
				<dt>标题：</dt>
				<dd>
					<input type="text" name="ftitle"
						class="required" size="70" value="${subscription.ftitle}"/>
				</dd>
			</dl>
			<dl>
				<dt>白皮书链接：</dt>
				<dd>
					<input type="text" name="fbaipi" maxlength="300" value="${subscription.fbaipi}"
						   size="70" />
				</dd>
			</dl>
			<dl>
				<dt>支付类型：</dt>
				<dd>
					<c:set value=",${subscription.fcost_vi_ids}," var="fcost_vi_idsStr"></c:set>
					<c:forEach items="${coins}" var="type">
						<c:set value=",${type.fid}," var="fidStr"></c:set>
						<input type="checkbox" name="fvi_ids" <c:if test="${fn:indexOf(fcost_vi_idsStr,fidStr) > -1}">checked="checked"</c:if> value="${type.fid}"/>${type.fname}
					</c:forEach>
				</dd>
			</dl>
			<dl>
				<dt>兑换比例：</dt>
				<dd>
					<input type="text" name="fprices" value="${subscription.fprices}" class="required" size="70"/><br/>【多个支付类型用英文/分隔，如0.5/1】
				</dd>
			</dl>
			<dl>
				<dt>众筹虚拟币名称：</dt>
				<dd>
					<input type="hidden" name="fid" value="${subscription.fid }" /> <select
						type="combox" name="vid" class="required">
						<c:forEach items="${allType}" var="type">
							<c:if test="${type.fid == subscription.fvirtualcointype.fid}">
								<option value="${type.fid}" selected="true">${type.fname}</option>
							</c:if>
							<c:if test="${type.fid != subscription.fvirtualcointype.fid}">
								<option value="${type.fid}">${type.fname}</option>
							</c:if>
						</c:forEach>
					</select>
				</dd>
			</dl>
			<dt>解冻类型：</dt>
				<dd>
					<select
						type="combox" name="ffrozenType" class="required">
						<c:forEach items="${frozenType}" var="type">
							<c:if test="${type.key == subscription.ffrozenType}">
								<option value="${type.key}" selected="true">${type.value}</option>
							</c:if>
							<c:if test="${type.key != subscription.ffrozenType}">
								<option value="${type.key}">${type.value}</option>
							</c:if>
						</c:forEach>
					</select>
				</dd>
			</dl>
			<dl>
				<dt>是否开启解冻：</dt>
				<dd>
					<c:choose>
						<c:when test="${subscription.fisstart}">
							<input type="checkbox" name="fisstart" checked='1' />
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="fisstart" />
						</c:otherwise>
					</c:choose>
					<span>每天0点解冻，按月的每月一号解冻</span>
				</dd>
			</dl>
			<dl>
				<dt>解冻比例：</dt>
				<dd>
					<input type="text" name="frate" class="required number" value="${subscription.frate }"/>
				</dd>
			</dl>
			<dl>
				<dt>是否开放众筹：</dt>
				<dd>
					<c:choose>
						<c:when test="${subscription.fisopen}">
							<input type="checkbox" name="fisopen" checked='1' />
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="fisopen" />
						</c:otherwise>
					</c:choose>
				</dd>
			</dl>
			<dl>
				<dt>众筹总数量：</dt>
				<dd>
					<input type="text" name="ftotal" class="required number"
						value="<fmt:formatNumber value="${subscription.ftotal}" pattern="##.######" maxIntegerDigits="15" maxFractionDigits="4"/>" />
				</dd>
			</dl>
			
			<dl>
				<dt>每人最大众筹数量：</dt>
				<dd>
					<input type="text" name="fbuyCount" class="required digits"
						value="${subscription.fbuyCount}" /><span>0为无限</span>
				</dd>
			</dl>
			<dl>
				<dt>每人最小众筹数量：</dt>
				<dd>
					<input type="text" name="fminbuyCount" class="required digits"
						value="${subscription.fminbuyCount}" />
				</dd>
			</dl>
			<dl>
				<dt>每人最多众筹次数：</dt>
				<dd>
					<input type="text" name="fbuyTimes" class="required digits"
						value="${subscription.fbuyTimes}" /><span>0为无限</span>
				</dd>
			</dl>
			<dl>
				<dt>开始时间：</dt>
				<dd>
					<input type="text" name="fbeginTime" class="required date"
						readonly="true" dateFmt="yyyy-MM-dd HH:mm:ss" size="40"
						value="${s}" /> <a class="inputDateButton"
						href="javascript:;">选择</a>
				</dd>
			</dl>
			<dl>
				<dt>结束时间：</dt>
				<dd>
					<input type="text" name="fendTime" class="required date"
						readonly="true" dateFmt="yyyy-MM-dd HH:mm:ss" size="40"
						value="${e}" /> <a class="inputDateButton"
						href="javascript:;">选择</a>
				</dd>
			</dl>
			<dl>
				<dt>项目介绍：</dt>
				<dd>
					<textarea class="editor" name="fcontent" rows="20" cols="105"
						tools="simple" upImgUrl="ssadmin/upload.html"
						upImgExt="jpg,jpeg,gif,png">
						${subscription.fcontent}
				</textarea>
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
