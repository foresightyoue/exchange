<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../comm/include.inc.jsp"%>
<base href="${basePath}"/>
<h2 class="contentTitle">修改投票信息</h2>


<div class="pageContent">

	<form method="post" action="ssadmin/updateCoinVote.html"
		class="pageForm required-validate" enctype="multipart/form-data"
		onsubmit="return iframeCallback(this, dialogAjaxDone);">
		<div class="pageFormContent nowrap" layoutH="97">
			<input name="fid" type="hidden" value="${fcoinvote.fid }">
			<dl>
				<dt>中文名称：</dt>
				<dd>
					<input type="text" name="fcnName" class="required"
						size="70"  value="${fcoinvote.fcnName }"/>
				</dd>
			</dl>
			<dl>
				<dt>简称：</dt>
				<dd>
					<input type="text" name="fshortName" class="required"
						size="40"  value="${fcoinvote.fshortName }"/>
				</dd>
			</dl>
			<dl>
				<dt>宣传语：</dt>
				<dd>
					<input type="text" name="fenName" class="required"
						size="70"  value="${fcoinvote.fenName }"/>
				</dd>
			</dl>
			<dl>
				<dt>图片：</dt>
				<dd>
					<input type="file" class="inputStyle" value="" name="filedata"
						id="filedata" />
				</dd>
			</dl>
			<dl>
				<dt>简介：</dt>
				<dd>
					<textarea class="editor {width:'780px'}" name="fdesc" rows="20" cols="100"  upImgUrl="ssadmin/upload.html"
						upImgExt="jpg,jpeg,gif,png">${fcoinvote.fdesc }</textarea>
				</dd>
			</dl>
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
