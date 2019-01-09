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
</head>
<body>



<%@ include file="../comm/header.jsp" %>

<div class="container about main-con">
    <div class="row">
        <div class="col-xs-10 padding-right-clear">
            <div class="col-xs-12 main-right aboutcontent">${fabout.fcontent}</div>
        </div>
    </div>
</div>
    
<%@include file="../comm/footer.jsp"%>
</body>
</html>
