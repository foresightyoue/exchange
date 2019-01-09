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
    <title>谷歌认证</title>
    <link rel="stylesheet" href="/static/front/app/css/css.css" />
    <link rel="stylesheet" href="/static/front/app/css/style.css" />
    <link rel="stylesheet" href="/static/front/app/css/swiper-3.4.2.min.css" />
    <script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js" ></script>
    <script type="text/javascript" src="/static/front/app/js/swiper-3.4.2.jquery.min.js" ></script>
    <script type="text/javascript" src="/static/front/js/comm/clipboard.min.js"></script>
</head>
<style>
    .Personal-cont ul li span{
        width: 5.5rem;
    }
    .bindgoogle{
        display: flex !important;
        display: -webkit-flex !important;
        justify-content: space-around;
        align-items: center;
    }
    #bindgoogle-key{
        padding-left: 0.62rem;
        display: inline-block;
    }
    #bindgoogle-key-hide{
        padding-left: 0.62rem;
        display: inline-block;
        width: 12em;
    }
    .text-danger {
	    color: #c83935 !important;
	    padding: 0;
	    background: #fff;
	    padding-left: 1rem;
	}
	#copy{
	   float: right;
	   background: #EBA51D;
       color: #fff;
       display: inline-block;
       text-align: center;
	}
	.Personal-cont{
	   border-bottom: 0;
	}
	.unbindgoogle-hide-box{
	   text-align: center;
	   margin: 1rem;
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
<style type="text/css">
    .now_status{
        position: relative;
    }
    .now_status img{
        position: absolute;
		width: 3.5rem;
		right: .62rem;
		top: .8rem;
    }
    @media (min-width: 768px){
        .now_status .control-label {
        text-align: right;
        margin-bottom: 0;
        padding-top: 2px;
    }
    
}
    .trade {
        margin-top: 0 !important;
    }
    #google_status1 .status_details{
        display: inline-block;
        width: 70px ;
    }
</style>
<body style="background: #fff;">
<nav>
    <div class="Personal-title">
        <span>
            <a href="${oss_url}/m/security.html">
                <em>
                    <i></i>
                    <i></i>
                </em>
                <strong><!-- 返回 --></strong>
            </a>
        </span>谷歌认证
    </div>
</nav>
<section>
    <c:if test="${isBindGoogle == true }">
        <div class="Personal-cont">
            <p><img src="/static/front/app/images/succeed.png"></p>
            <h2 class="clearfix">已绑定谷歌认证</h2>
            <ul class="clearfix unbindgoogle-show-box">
                <li style="border-bottom: 1px solid #e6e6e6;"><span>谷歌验证码：</span><input id="unbindgoogle-topcode" class="form-control" type="text"></li>
            </ul>
            <ul class="clearfix unbindgoogle-show-box now_status">
                <li style="border-bottom: 1px solid #e6e6e6; position: relative;">
	                <span>当前状态：</span>
	                <c:if test="${sessionScope.login_user.fgoogleCheck==false}">
	                    <img alt="" src="/static/front/images/open1.png" style="display: none;">
	                    <img alt="" src="/static/front/images/close.png" class="listImg" style="display: block">
	                </c:if>
	                <c:if test="${sessionScope.login_user.fgoogleCheck==true}">
	                    <img alt="" src="/static/front/images/open1.png" style="display: block;">
	                    <img alt="" src="/static/front/images/close.png" class="listImg" style="display: none">
	                </c:if>
	                <input type="checkbox" id="google-status" ${sessionScope.login_user.fgoogleCheck ==true ? "checked" : "" }>
	            </li>
            </ul>
            <div class="form-group unbindgoogle-show-box">
	             <label for="unbindgoogle-errortips" class="col-xs-3 control-label"></label>
	             <div class="col-xs-6">
	                 <span id="unbindgoogle-errortips" class="text-danger"></span>
	             </div>
	         </div>
            <div class="next-padding unbindgoogle-show-box">
               <!--  <div id="unbindgoogle-Btn" class="next next-active">查看</div> -->
            </div>
            <div class="form-group unbindgoogle-hide-box" style="display:none">
                <div class="col-xs-12 clearfix">
                    <div id="unbindgoogle-code" class="form-control border-fff ">
                        <div class="form-qrcode" id="unqrcode"></div>
                    </div>
                </div>
            </div>
            <div class="form-group unbindgoogle-hide-box" style="display:none">
	            <label for="unbindgoogle-key" class="col-xs-3 control-label">密匙：</label>
	            <span id="unbindgoogle-key" class="form-control border-fff"></span>
	        </div>
	        <div class="form-group unbindgoogle-hide-box" style="display:none">
	            <label for="unbindgoogle-device" class="col-xs-3 control-label">设备名称：</label>
	            <span id="unbindgoogle-device" class="form-control border-fff">${device_name }</span>
	        </div>
        </div>
    </c:if>
    <c:if test="${isBindGoogle == false }">
        <div class="Personal-cont">
            <p><img src="/static/front/app/images/no_pass.png"></p>
            <h2 class="clearfix">未绑定谷歌认证</h2>
            <ul class="clearfix">
                <li><span>密钥：</span><p class="bindgoogle"><input id="bindgoogle-key-hide" type="text" readonly="readonly"><span id="copy">复制</span></p></li>
                <span id="bindgoogle-key" style="display: none;"></span>
                <li><span>谷歌认证码：</span><p><input type="text" id="bindgoogle-topcode" placeholder="请输入谷歌认证码"></p></li>
            </ul>
        </div>
        <span id="bindgoogle-errortips" class="text-danger"></span>
        <div class="next-padding">
            <div id="bindgoogle-Btn" class="next next-active">确认绑定</div>
        </div>
    </c:if>


</section>
</body>
<script type="text/javascript" src="/static/front/js/comm/util.js"></script>
<script type="text/javascript" src="/static/front/js/comm/comm.js"></script>
<script type="text/javascript" src="/static/front/js/language/language_cn.js"></script>
<script type="text/javascript" src="/static/front/js/layer/layer.js"></script>

<script type="text/javascript" src="/static/front/js/comm/msg.js"></script>
<script type="text/javascript" src="/static/front/js/plugin/jquery.qrcode.min.js"></script>
<script type="text/javascript" src="/static/front/js/user/user.security.js"></script>
<script type="text/javascript">
    $(function(){
        security.loadgoogleAuth();
        $("#copy").on("click",function(){
        	var tid=document.getElementById("bindgoogle-key-hide").value;
            var clipboard = new ClipboardJS('#copy', {
                            text: function (trigger) {
                                return tid; //复制内容
                            }
                        })
                        clipboard.on('success', function (e) {
                            console.info('Text:', e.text); //复制文本
                            e.clearSelection(); //取消选择节点
                            parent.layer.msg("复制成功");
                        });
                        clipboard.on('error', function (e) {
                            console.error('Trigger:', e.trigger);
                            parent.layer.msg("复制失败");
                        });
            
            // document.execCommand("Copy");// 执行浏览器复制命令
            // util.showerrortips('bindgoogle-errortips', "复制成功");
        });
    });
</script>
<script>
    $(function(){
        $("body .now_status").on("click","img",function(){
            $(this).hide().siblings("img").show();
            $("#google-status").click();
            if($("#google-status").prop("checked")){
                $("#unbindgoogle-topcode").removeAttr("readonly");
            }else{
                $("#unbindgoogle-topcode").attr("readonly","readonly");
            }
        })
    });
    
    </script>
    <script type="text/javascript">
    $(function(){
        security.loadgoogleAuth();
        var desc = '';
        $("#google-status").on("click",function(){
            var status=$("#google-status").prop("checked");
            var statusCode=(status==false ? 0:1);
            var msgcode=$("#unbindgoogle-msgcode").value;
            var url = "/user/getGoogleCheck.html?random=" + Math.round(Math.random() * 100);
            var param = {
                    statusCode : statusCode,
                    phoneCode : ""
                };
            jQuery.post(url, param, function(result) {
                util.showerrortips('unbindgoogle-errortips', result.msg);
                if (result.code == 0) {
                    location.reload();
                }
            }, "json");
        });
    });
    </script>
</html>