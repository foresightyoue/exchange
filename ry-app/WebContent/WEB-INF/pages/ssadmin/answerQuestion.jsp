<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<div class="pageContent">

	<form method="post" action="ssadmin/answerQuestion.html"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this, dialogAjaxDone)">
		<div class="pageFormContent" layoutH="58">
			<div class="unit">
				<dt>提问人：</dt>
				<dd>
					<input type="hidden" name="fid" value="${fquestion.fqid }" /> <input
						type="text" name="fname" size="40" value="${fquestion.fuser.frealName}"
						readonly="true" />
				</dd>
			</div>
			<div class="unit">
					<dt>提问类型：</dt>
					<dd>
						<select type="combox" name="ftype" disabled="true">
							<c:forEach items="${typeMap}" var="type">
								<c:if test="${type.key == fquestion.ftype}">
									<option value="${type.key}" selected="true">${type.value}</option>
								</c:if>
								<c:if test="${type.key != fquestion.ftype}">
									<option value="${type.key}">${type.value}</option>
								</c:if>
							</c:forEach>
						</select>
					</dd>
			</div>
            <c:forEach items="${list}" var="list">
			  <div class="unit">
				 <dt>提问内容：</dt>
				 <dd>
					 <textarea name="fdesc" cols="80" rows="2" readonly="true">${list.fdesc}</textarea>
					 <input type="hidden" name="fqid" value="${list.fqid}" >
				 </dd>
			  </div>
			</c:forEach>

			<div class="unit">
				<dt>回复内容：</dt>
				<dd>
					<textarea name="fanswer" cols="80" rows="2" class="required"></textarea>
				</dd>
			</div>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive">
						<div class="buttonContent">
							<button type="submit">回复</button>
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
