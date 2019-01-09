<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../../front/comm/include.inc.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>

<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="../comm/header.jsp"></jsp:include>
        <meta charset="utf-8" />
        <meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no, width=device-width">
        <meta name="format-detection" content="telephone=no">
        <meta name="format-detection" content="email=no">
        <base href="${basePath}"/>
        <title>找回密码</title>
        <link rel="stylesheet" href="/static/front/app/css/css.css" />
        <link rel="stylesheet" href="/static/front/app/css/style.css" />
        <script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js" ></script>
        <script type="text/javascript" src="/static/front/app/js/swiper-3.4.2.jquery.min.js" ></script>
    </head>
    <body>
        <nav>
            <div class="Personal-title">
		        <span>
		            <a href="javascript:;" onClick="javascript :history.back(-1)">
		                <em>
		                    <i></i>
		                    <i></i>
		                </em>
		                <strong>返回</strong>
		            </a>
		        </span>找回密码
		    </div>
        </nav>
       <section>
            <div class="information">
                <div id="secondstep" class="register_cont" style="display: block;">
                    <div class="information-cont">
                        <ul>
                            <li>
                                <input type="text" class="phone-code" id="reset-loginname" value="${fuser.ftelephone }" placeholder="登录账号"/>
                            </li>
                            <li>
                                <input type="password" class="phone-code" id="reset-newpass" value="" placeholder="新密码" id="reset-imgcode"/>
                            </li>
                            <li>
                                <input type="password" class="phone-code" value="" placeholder="确认密码" id="reset-confirmpass"/>
                            </li>
                        </ul>
                    </div>
                    <div id="btn-email-success" class="next next-active">下一步</div>
                </div>
            </div>
            <!-- <div style="display:none" class="col-xs-12 padding-top-30 successstep text-center" id="successstep">
                <div>
                    <i class="successstep-icon"></i>恭喜，重置密码成功
                </div>
                <a class="btn btn-danger btn-find" href="/m/user/login.html">立即登录</a>
            </div> -->
            <div class="Personal-cont successstep text-center" id="successstep" style="display:none;border-bottom: 0;">
	            <p><img src="/static/front/app/images/succeed.png"></p>
	            <h2 class="clearfix">恭喜，重置密码成功</h2>
	            <div class="next-padding">
		            <a class="next next-active" href="/m/user/login.html" style="display: block;">立即登录</a>
		        </div>
	        </div>
        </section>
    </body>
<input type="hidden" id="fid" value="${fuser.fid}"/>
<input type="hidden" id="ev_id" value="0"/>
<input type="hidden" id="newuuid" value="0"/>
<input type="hidden" id="mtype" value="1"/>
<script type="text/javascript" src="${oss_url}/static/front/js/comm/msg.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/user/reset.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/plugin/bootstrap.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/plugin/layer/layer.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/comm/util.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/comm/comm.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/language/language_cn.js"></script>
</html>