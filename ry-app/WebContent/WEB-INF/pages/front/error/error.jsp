<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../comm/include.inc.jsp"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>

<!DOCTYPE html>
<html>
<head>
 <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=0">
 <base href="${basePath}"/>
    <title>错误页面</title>
    <style>
        *{
            margin: 0;
            padding: 0;
        }
        html,body{
            width: 100%;
            height: 100%;
        }
        @media screen and (max-width: 768px) {
            .pageError {
                width: 100%;
                height: 100%;
                padding-top: 3.19rem;
                box-sizing: border-box;
                -webkit-box-sizing: border-box;
                -moz-box-sizing: border-box;
                -ms-box-sizing: border-box;
                -o-box-sizing: border-box;
                background-color: white;
                text-align: center;
            }
            .pcError{
                display: none;
            }
            header{
                width: 100%;
                height: .769rem;
                text-align: center;
                line-height: .769rem;
                background-color: #186ed5;
                position: fixed;
                top: 0;
                left: 0;
                font-size: .306rem;
                color: white;
                padding-top: .34rem;
            }
            .prev{
                width: .5rem;
                height: .769rem;
                position: absolute;
                background-image: url("/static/front/app/images/prev.png");
                background-position: center;
                background-repeat: no-repeat;
                background-size: 100%;
            }
            .error_img{
                height: 2.17rem;
            }
            .error_img img{
                width: 2.17rem;
                height: 2.17rem;
            }
            .error_hint{
                font-size: .187rem;
                color: #999;
            }
            .tryAgain{
                font-size: .272rem;
                text-decoration: none;
                color: #186ed5;
                float: left;
                margin-top: .425rem;
                margin-left: 2.4rem;
            }
        }
        @media screen and (min-width: 768px) {
            .pageError {
                display: none;
            }
            .pcError{
                width: 100%;
                height: 100%;
                padding-top: 180px;
                background-color: white;
                text-align: center;
                box-sizing: border-box;
                -webkit-box-sizing: border-box;
                -moz-box-sizing: border-box;
                -ms-box-sizing: border-box;
                -o-box-sizing: border-box;
                position: relative;
            }
            header{
                display: none;
            }
            .pc_error_img{
                height: 200px;
            }
            .pc_error_img img{
                height: 200px;
                width: 200px;
            }

            /*.pc_error_hint{*/
                /*font-size: 12px;*/
                /*color: #999;*/
                /*margin-top: 20px;*/
            /*}*/
            .try_box{
                height: 20px;
                line-height: 0;
            }
            .pc_tryAgain{
                font-size: 20px;
                text-decoration: none;
                color: #186ed5;
            }
        }
    </style>  
</head>
<body>
<!--<div class="error_mars"></div>-->
<div class="pageError">
    <header>
        <div class="prev"></div>
        <div class="head_title">网络错误</div>
    </header>
    <div class="error_img"><img src="/static/front/images/errorNet.png" alt=""></div>
    <div class="error_hint">无网络</div>
    <a href="${oss_url}/m/index.html" class="tryAgain">点击再次加载</a>
</div>
<div class="pcError">
    <div class="pc_error_img"><img src="/static/front/images/errorNet.png" alt=""></div>
    <div class="try_box"><a href="${oss_url}/index.html" class="pc_tryAgain">点击再次加载</a></div>
</div>

<script src="/static/front/app/js/jquery-3.2.1.min.js"></script>
<script src="/static/front/js/rem1.js"></script>
</body>
</html>