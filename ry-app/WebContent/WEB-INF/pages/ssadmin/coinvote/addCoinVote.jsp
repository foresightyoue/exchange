<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../comm/include.inc.jsp"%>
<h2 class="contentTitle">添加投票信息</h2>

<!DOCTYPE h2 PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <base href="${basePath}"/>
    </head>
<div class="pageContent">

	<form method="post" action="ssadmin/saveCoinVote.html"
		class="pageForm required-validate" enctype="multipart/form-data"
		onsubmit="return iframeCallback(this, dialogAjaxDone);">
		<div class="pageFormContent nowrap" layoutH="97">
			<dl>
				<dt>中文名称：</dt>
				<dd>
					<input type="text" name="fcnName" class="required"
						size="70" />
				</dd>
			</dl>
			<dl>
				<dt>简称：</dt>
				<dd>
					<input type="text" name="fshortName" class="required"
						size="40" />
				</dd>
			</dl>
			<dl>
				<dt>宣传语：</dt>
				<dd>
					<input type="text" name="fenName" class="required"
						size="70" />
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
						upImgExt="jpg,jpeg,gif,png">
				</textarea>
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
</html>