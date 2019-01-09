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
        <title>修改交易密码</title>
        <link rel="stylesheet" href="/static/front/app/css/css.css" />
        <link rel="stylesheet" href="/static/front/app/css/style.css" />
        <script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js" ></script>
        <script type="text/javascript" src="/static/front/app/js/swiper-3.4.2.jquery.min.js" ></script>
        <style>
            .information-cont ul li{
                padding-left: .68rem;
            }
            .information-cont ul li label{
                width: 4.5rem;
            }
            .next{
                margin: 5rem 0.62rem;
            }
			.information-cont ul li input[type="text"] {
				width: 70%;
			}
			#unbindtradepass-sendmessage{
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
			    margin-left: 0.6rem;
			    color: #808080;
			}
			.next-mars{
			    width: 100%;
			    height: 3.06rem;
			    position: absolute;
			    left: 0;
			    bottom: 106px;
			    z-index: 10;
			    display: none;
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
        <script>
		    $(function(){
		        var security = "${securityEnvironment}";
		        var googleCheck = "${sessionScope.login_user.fgoogleCheck}";
		        if (security == "true") {
		            $("#unbindtradepass-msgcode").closest('li').hide();
		        }
		        if (googleCheck == "false") {
		            $("#unbindtradepass-googlecode").closest('li').hide();
		        }
		    });
		</script>
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
                        <strong><!-- 返回 --></strong>
                    </a>
                </span>修改交易密码
            </div>
        </nav>
        <section>
            <div class="information">
                <div class="register_cont" style="display: block;">
                    <div class="hint"><img src="/static/front/app/images/hint.png">为保障您的资金安全，请设置交易密码，请务必牢记</div>
                    <div class="information-cont">
                        <ul>
                            <!-- <li>
                               <input type="password" class="form-control password" value="" id="unbindtradepass-oldpass" placeholder="旧交易密码" >
                               <em>
                                   <img src="/static/front/app/images/password_off.png" class="off" style="display: block;" />
                                   <img src="/static/front/app/images/password_on.png" class="on" />
                               </em>
                            </li> -->
                            <li>
                                <input type="password" class="form-control password" value="" id="unbindtradepass-newpass" placeholder="新交易密码" />
                                <em>
                                    <img src="/static/front/app/images/password_off.png" class="off" style="display: block;" />
                                    <img src="/static/front/app/images/password_on.png" class="on" />
                                </em>
                            </li>
                            <li>
                                <input type="password" class="form-control password" value=""  id="unbindtradepass-confirmpass" placeholder="确认新密码" />
                                <em>
                                    <img src="/static/front/app/images/password_off.png" class="off" style="display: block;" />
                                    <img src="/static/front/app/images/password_on.png" class="on" />
                                </em>
                            </li>
                            <c:if test="${isBindTelephone}">
                               <li>
                               	  <input type="hidden" id="bindphone-newphone" value="${fuser.ftelephone }"/>
                                  <input id="unbindtradepass-msgcode" type="text" class="form-control" value="" placeholder="短信验证码">
                                  <input id="sendVoice" type="hidden" value="false">
                                  <button id="bindphone-sendmessage"  onclick ="sendVoice()" data-msgtype="7" onclick="sendVoice()" data-tipsid="unbindtradepass-errortips" class="btn btn-sendmsg">发送验证码</button>
                               </li>
                               <li class="sendVoice">
                                   <span class="sendVoice_hint">发送方式</span>
                                   <input name="sendcode" checked="checked" type="radio" id="sms"><label for="sms">短信</label>
                                 <!--   <input name="sendcode" type="radio" id="voice"><label for="voice">语音</label> -->
                               </li>
                            </c:if>
                            <%--<c:if test="${isBindGoogle}">
                               <li>
                                 <input id="unbindtradepass-googlecode" class="form-control" type="text" placeholder="谷歌验证码">
                               </li>
                            </c:if>--%>
                        </ul>
                    </div>
                    <div class="next next-active" id="unbindtradepass-Btn">确认提交</div>
                    <div class="next-mars"></div>
                </div>
            </div>
        </section>
    </body>

       <script type="text/javascript" src="/static/front/js/comm/msg.js"></script>
       <script type="text/javascript" src="/static/front/js/comm/util.js"></script>
       <script type="text/javascript" src="/static/front/js/comm/comm.js"></script>
       <script type="text/javascript" src="/static/front/js/language/language_cn.js"></script>
       <script type="text/javascript" src="/static/front/js/layer/layer.js"></script>
       <script type="text/javascript" src="/static/front/js/plugin/jquery.qrcode.min.js"></script>
       <script type="text/javascript" src="/static/front/js/user/user.securityt.js"></script>
<script>
    $(function(){
        
        $(".information-cont .off").on('click',function(){
            $(this).hide();
            $(this).next().show();
            $(this).parent().siblings('input.password').attr('type','text');
        });
        
        $(".information-cont .on").on('click',function(){
            $(this).hide();
            $(this).prev().show();
            $(this).parent().siblings('input.password').attr('type','password');
        });
        
    /*  $(".next").on('click',function(){
            var password1 = $("#password1").val() ;
            var password2 = $("#password2").val() ;
            if(password1!=password2){
                layer.msg('两次密码输入不一致') ;
                return ;
            }
            if(password1.length<6){
                layer.msg('密码长度必须不小于6位') ;
                    return ;
            }
            
            $.post('/m/json/tradePwd.html',{password1:password1,password2:password2},function(data){
                var code = data.code ;
                var msg = data.msg ;
                if(code == 0 ){
                    window.location.href = '/m/realId.html';
                }
                layer.msg(msg) ;
            },'json') ;
            
        }); */
        
    });
    function sendVoice(){
        if($(".sendVoice input[type='radio']:checked").attr("id") == "sms"){
            $("#sendVoice").val("false");
        }else{
            $("#sendVoice").val("true");
        }
        return true;
    }
</script>
</html>