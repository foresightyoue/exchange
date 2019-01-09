<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../comm/include.inc.jsp"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>

<!doctype html>
<html>
<head> 
<base href="${basePath}"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<base href="${basePath}"/>
<%@include file="../comm/link.inc.jsp" %>

<link rel="stylesheet" href="${oss_url}/static/front/css/user/about.css" type="text/css"></link>

<style type="text/css">
.ftype {
    font-size: 14px;
    padding-left: 30px !important;
    font-weight:bold;
    cursor: pointer;
}

</style>

</head>
<body>



<%@ include file="../comm/header.jsp" %>

<div class="container about main-con">
    <div class="row">
        <div class="col-xs-2 padding-clear">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <i class="iconfont"></i> 帮助分类
                </div>
                <div id="collapseOne" class="panel-collapse collapse in">
                    <div class="panel-body" style="padding: 0px;">
                        <table class="table table-hover" style="margin-bottom: 0px;">
                            <tbody class="left_nav">
                                <c:forEach items="${ftypes}" var="ftype" varStatus="vs">
                                    <tr><td class="ftype" onclick="displaySwitchID('${vs.index}')">${ftype}</td></tr>
		                                <c:forEach items="${abouts}" var="v">
			                                <c:if test="${v.ftype == ftype}">
				                                <tr class="${vs.index} ${v.fid eq fabout.fid ? 'active' : ''} submenu">
				                                    <td>
				                                        <a href="javascript:findFaboutID('${v.fid }')"> <i class="iconfont"></i>${v.ftitle }</a>
				                                    </td>
				                                </tr>
			                                </c:if>
		                                </c:forEach>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-xs-10 padding-right-clear">
            <p>RYH--version   201806270925</p>
            <div class="col-xs-12 main-right aboutcontent">${fabout.fcontent}</div>
            <!---end]]-->
        </div>
    </div>
</div>
    
<%@include file="../comm/footer.jsp"%>
</body>
<script type="text/javascript">
$(function(){
    $('.submenu').on("click",function(){
    	$(this).addClass("active").siblings().removeClass("active");
    });
}); 

function findFaboutID(id) {
	$.ajax({
		url:"/about/index1.html",
		data:{"id":id},
		dataType:"json",
		success:function(data){
			var fcontent=data.fabout.fcontent;
			$(".aboutcontent").html(fcontent);
	    }
	});
}

function displaySwitchID(id) {
    if ($('.'+id).css('display') == "none")
        $('.'+id).removeAttr("style");
    else
        $('.'+id).css('display', "none");
}

</script>

</html>
