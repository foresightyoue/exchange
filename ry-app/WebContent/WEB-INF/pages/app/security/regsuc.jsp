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
        <title>上传成功</title>
        <link rel="stylesheet" href="/static/front/app/css/css.css" />
        <link rel="stylesheet" href="/static/front/app/css/style.css" />
        <script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js" ></script>
        <script type="text/javascript" src="/static/front/app/js/swiper-3.4.2.jquery.min.js" ></script>
        <style>
            .success_img{
                text-align: center;
                margin-top: 4rem;
            }
            .success_img img{
                 width: 6rem;
                 height: 6rem;
            }
            .success_hint{
                font-size: 1rem;
                margin-top: 1.5rem;
                text-align: center;
            }
            .homepage_btn{
                width: 100%;
                text-align: center;
            }
            .homepage_btn a{
                padding: .3rem 1.5rem;
                background-color: #03a473;
                color: white;
                font-size: 1rem;
                display: inline-block;
                margin-top: 1rem;
                border-radius: 4px;
                -webkit-border-radius: 4px;
            }
            
        </style>
    </head>
    <body style="background-color: white;">
        <nav>
            <div class="Personal-title">
                <span>
                    <a href="${oss_url}/m/index.html">
                        <em>
                            <i></i>
                            <i></i>
                        </em>
                        <strong>返回</strong>
                    </a>
                </span>上传成功
            </div>
        </nav>
        <section>
            <div class="success_img"><img src="/static/front/app/images/succeed.png"></div>
            <div class="success_hint">上传成功,待审核</div>
            <%-- <p>注册账号：${fuser.floginName }</p>
            <p>认证姓名：${fuser.frealName }</p>
            <p>证件类型：身份证</p>
            <p>证件号码：${fuser.fidentityNo }</p> --%>
           
                  <div class="homepage_btn" style="text-align: center;"><a href="${oss_url}/m/index.html">首页</a></div>
                <!-- <ul>
                    <li><a href="/m/index.html">首页</a></li>
                    <li><a href="/m/user/login.html">马上登录</a></li>
                </ul> -->
          <!--   </div>  -->
        </section>
    </body>
    <script type="text/javascript" src="/static/front/js/comm/util.js"></script>
    <script type="text/javascript" src="/static/front/js/comm/comm.js"></script>
</html>