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
        <title>设置登录密码</title>
        <link rel="stylesheet" href="/static/front/app/css/css.css" />
        <link rel="stylesheet" href="/static/front/app/css/style.css" />
        <script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js" ></script>
        <script>
	        $(function(){
	            var security = "${securityEnvironment}";
	            var googleCheck = "${sessionScope.login_user.fgoogleCheck}";
	            /* if (security == "true") {
	                $("#unbindloginpass-msgcode").closest('li').hide().next().hide();
	            } */
	            if (googleCheck == "false") {
	                $("#unbindloginpass-googlecode").closest('li').hide();
	            }
	        });
	    </script>
    </head>
    <style>
    .information-cont ul li{
        padding-left: .68rem;
    }
    .information-cont ul li label{
        display: inline-block;
        width: 7.5em;
        padding-left: 0.7em;
    }
    .information-cont ul li input{
        width: 70%;
    }
    .information-cont ul li div{
        width: 30%;
        text-align: right;
        padding-right: .62rem;
    }
    #sms,#voice{
        -webkit-appearance: radio;
        width: auto;
        margin-top: 1rem;
    }
    .information-cont .sendVoice label{
        width: 2.5rem;
    }
    .information-cont .sendVoice{
        background-color: #fff;
        border-bottom: 0;
    }
    .sendVoice_hint{
        display: inline-block;
        width: 5rem;
        color: #808080;
    }
    .next-mars{
        width: 100%;
        height: 3.06rem;
        position: absolute;
        left: 0;
        bottom: 27px;
        z-index: 10;
        display: none;
    }
    .next-active{
    	margin-top:3rem;
    }
    .Personal-title span em{
            width: 1.2rem;
            height: 1.2rem;
            left: 0.4rem;
            top:0.65rem;
        }
        .Personal-title span em i:last-child{
            left:0.22rem;
        }
    </style>
    <body>
       <nav>
          <div class="Personal-title">
              <span>
                  <a href="javascript:;" onClick="javascript:history.back(-1)">
                       <em>
                          <i></i>
                          <i></i>
                          
                       </em>
                       <strong><!-- 返回 --></strong>
                  </a>
              </span>修改登录密码
          </div>
       </nav>
       <section>
            <div class="information">
                <div class="register_cont" style="display: block;">
                  <div class="hint"><img src="/static/front/app/images/hint.png">为保障您的账号安全,请设置登录密码,请务必牢记</div>
                  <div class="information-cont">
                      <ul>
                         <li>
                            <input type="password"  id="unbindloginpass-oldpass" class="form-control password" placeholder="旧登录密码" >
                            <em>
                               <img src="/static/front/app/images/password_off.png" class="off" style="display: block;" />
                               <img src="/static/front/app/images/password_on.png" class="on" />
                            </em>
                         </li>
                         <li>
                           <input type="password" id="unbindloginpass-newpass" class="form-control password" placeholder="新登录密码" >
                           <em>
                                <img src="/static/front/app/images/password_off.png" class="off" style="display: block;" />
                                <img src="/static/front/app/images/password_on.png" class="on" />
                           </em>
                         </li>
                         <li>	
                            <input type="password" id="unbindloginpass-confirmpass" class="form-control password" placeholder="确认新密码">
                            <em>
                                 <img src="/static/front/app/images/password_off.png" class="off" style="display: block;" />
                                <img src="/static/front/app/images/password_on.png" class="on" />
                            </em>
                         </li>
                         <c:if test="${isBindTelephone}">
                           <li>
                           	  <input type="hidden" id="bindphone-newphone" value="${fuser.ftelephone }"/>
                              <input id="unbindloginpass-msgcode" type="text" class="form-control" value="" placeholder="短信验证码">
                              <div><button id="bindphone-sendmessage" onclick="sendVoice()" data-msgtype="6" data-tipsid="unbindloginpass-errortips" class="btn btn-sendmsg">发送验证码</button></div>
                           </li>
                           <li class="sendVoice">
			                    <span class="sendVoice_hint">发送方式</span>
			                    <input name="sendcode" checked="checked" type="radio" id="sms"><label for="sms">短信</label>
			                    <!-- <input name="sendcode" type="radio" id="voice"><label for="voice">语音</label> -->
			                    <input type="hidden" id="sendVoice" value="false" />
			                </li>
                         </c:if>
                         <c:if test="${isBindGoogle }">
                            <li>
                               <input id="unbindloginpass-googlecode" class="form-control" type="text" placeholder="谷歌验证码">
                            </li>
                         </c:if>
                      </ul>
                  </div>
                  <div class="btn btn-danger btn-block next next-active" id="unbindloginpass-Btn">确认提交</div>
                  <div class="next-mars"></div>
                </div>
            </div>
       </section>
       <script type="text/javascript" src="${oss_url}/static/front/js/comm/msg.js"></script>
       <script type="text/javascript" src="${oss_url}/static/front/js/comm/util.js"></script>
       <script type="text/javascript" src="${oss_url}/static/front/js/comm/comm.js"></script>
       <script type="text/javascript" src="/static/front/js/comm/comm.js"></script>
       <script type="text/javascript" src="${oss_url}/static/front/js/language/language_cn.js"></script>
       <script type="text/javascript" src="${oss_url}/static/front/js/layer/layer.js"></script>
       <script type="text/javascript" src="${oss_url}/static/front/js/plugin/jquery.qrcode.min.js"></script>
       <script type="text/javascript" src="${oss_url}/static/front/js/user/user.securityt.js"></script>
       <script type="text/javascript">
			/* 显示和隐藏字符切换 */
			$(function(){
				$(".information-cont .off").on('click',function(){
					$(this).hide().next().show();
					$(this).parent().siblings('input.password').attr('type','text');
				});
				
				$(".information-cont .on").on('click',function(){
					$(this).hide().prev().show();;
					$(this).parent().siblings('input.password').attr('type','password');
				});
			});
			function sendVoice(){
				if($(".sendVoice input[type='radio']:checked").attr("id")== "sms"){
					 $("#sendVoice").val("false");
				}else{
					$("#sendVoice").val("true");
				}
				return true;
			}
       </script>
    </body>
</html>