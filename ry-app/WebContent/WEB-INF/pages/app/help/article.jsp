<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../../front/comm/include.inc.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no, width=device-width">
        <meta name="format-detection" content="telephone=no">
        <meta name="format-detection" content="email=no">
        <base href="${basePath}"/>
        <title>帮助详情</title>
        <link rel="stylesheet" href="/static/front/app/css/css.css" />
        <link rel="stylesheet" href="/static/front/app/css/style.css" />
        <link rel="stylesheet" href="/static/front/app/css/swiper-3.4.2.min.css" />
        <script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js" ></script>
        <script type="text/javascript" src="/static/front/app/js/swiper-3.4.2.jquery.min.js" ></script>
        <style type="text/css">
            .advertisement_list img{
                width: 1rem;
            }
        </style>
    </head>
    <body>
        <nav>
            <div class="Personal-title">
                <span>
                    <a href="/m/mine/help.html">
                        <em>
                            <i></i>
                            <i></i>
                        </em>
                        <strong>返回</strong>
                    </a>
                </span>帮助详情
            </div>
        </nav>
        <section id="cont-padding">
            <div class="detailed">
                <h2>${help.items.ftitle }</h2>
                <div style="width: 100%" class="advertisement_list">${help.items.fcontent }
                </div>
            </div>
        </section>
    </body>
<script type="text/javascript" src="/static/front/js/comm/util.js"></script>
<script type="text/javascript" src="/static/front/js/comm/comm.js"></script>
</html>
