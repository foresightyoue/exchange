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
	<base href="${basePath}" />
	<title>激活</title>
	<link rel="stylesheet" href="/static/front/app/css/style.css" />
	<link rel="stylesheet" href="/static/front/app/css/css.css" />
	<link rel="stylesheet" href="/static/front/app/css/style.css" />
	<link rel="stylesheet" type="text/css" href="/static/front/app/css/new_common.css" />
	<script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js" ></script>
	<script type="text/javascript" src="${oss_url}/static/front/js/plugin/layer/layer.js"></script>
	<script type="text/javascript" src="${oss_url}/static/front/js/comm/util.js"></script>
	<script type="text/javascript" src="${oss_url}/static/front/js/language/language_cn.js"></script>
	<!-- <script type="text/javascript" src="${oss_url}/static/front/js/plugin/layer/layer.js"></script> -->
	<script type="text/javascript" src="/static/front/app/js/rem.js"></script>
	<script type="text/javascript" src="/static/front/app/js/vue.js"></script>
</head>
<style type="text/css">
	nav{
		position: relative;
	}

	.Personal-cont ul li{
    		position:relative;
    	}
    	.Personal-cont ul li>
    	.Personal-cont ul li p input{
    		width:70%;
    	}
        .Personal-title span em{
        width: 1.2rem;
        height: 1.2rem;
        position: absolute;
        top: 0.65rem;
        left: 0;
    }
    .Personal-title span em i:last-child{
        left:0.25rem;
    }
	.Personal-cont p img{
		width: 5.5rem;
	}
</style>
<body style="background: #fff;">
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
					</span>激活
		</div>
	</nav>
	<section>
		<div>
			<div class="Personal-cont">
				<p><img src="/static/front/app/images/icon_intrest.png"></p>
				<h2 class="clearfix">请输入激活账号(瑞银ID或手机号)</h2>
				<ul class="clearfix">
					 <li><span>请输入：</span><p><input type="email" id="emial-verify-code" placeholder="请输入激活账号"></p></li>
				</ul>
			</div>
			<div class="next-padding">
				<div class="next next-active" id="activateBtn">确认</div>
			</div>
		</div>
	</section>
	</body>


<script>
	
	$(function () {
		$('#activateBtn').click(function () {
			var date =  $('#emial-verify-code').val()
			if(date){
			    // alert(date)
                $.post("/m/user/api/userActivation.html", {
                    "recommend": date
                }, function (data) {
                    console.log('ssdddddddddddddddddddddddddddds')
                    console.log(date,'sss')
                    layer.msg(data.msg, {
                        // icon: 1,
                        time: 1500,
                        end: function () {
                            setTimeout(window.location.href = "javascript:history.go(-1);", 1500);
                        }
                    });
                }).error(function (data) {
                    layer.msg("激活失败!")
                });
			}else{
				layer.msg('请输入激活码');
			}
		})
	})


	function ref() {
		var referee = $("#referees").val();
		$.post("/m/trade/Activation.html", {
			"tid": userId,
			"recommendId": referee,
			"uri": "/api/settlement/synUserNexus"
		}, function (data) {
			layer.msg(data.msg);
			setTimeout(window.location.href = "javascript:history.go(-1);", 3000);
		});
	}
</script>

</html>