<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = "http" + "://" + request.getServerName() + path;
session.setAttribute("basePath", basePath);
%>
<jsp:forward page="/index.html"></jsp:forward>
