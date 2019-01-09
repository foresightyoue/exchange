<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../front/comm/include.inc.jsp"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>

<!doctype html>
<html>

<head>
	<base href="${basePath}" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no, width=device-width">
	<base href="${basePath}" />
	<link rel="stylesheet" href="/static/front/app/css/style.css" />
	<link rel="stylesheet" href="/static/front/app/css/css.css" />
	<link rel="stylesheet" href="/static/front/app/css/style.css" />
	<link rel="stylesheet" href="/static/front/app/css/swiper-3.4.2.min.css" />
	<script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js"></script>
	<script type="text/javascript" src="/static/front/app/js/swiper-3.4.2.jquery.min.js"></script>
	<style>
		html,body{
	width: 100%;
    height: 100%;
}
.main_content{
    width: 100%;
    height: 100%;
    background-color: white;
}
.question_style {
	height: 3rem;
	line-height: 3rem;
	width: 100%;
	Padding: 0 .62rem;
	box-sizing: border-box;
	-webkit-box-sizing: border-box;
	-moz-box-sizing: border-box;
	-ms-box-sizing: border-box;
	-o-box-sizing: border-box;
	position: relative;
}
label{
	width: 20%
}
select{
	width: 25%;
    border: 0;
    height: 50%;
    outline: none;
    background-color: white;
}
.miao{
	margin-left: .62rem;
	line-height: 3rem;
}
#question-desc{
    width: 97%;
    height: 9rem;
    border: 1px solid #e6e6e6;
    padding: .62rem;
}
button{
    width: 80%;
    margin: 0 10%;
    padding: .85rem 0;
    background-color: #EBA51D;
    color: white;
    border-radius: 4px;
    -webkit-border-radius: 4px;
    margin-top: 5rem;
}
#errortips{
	color: red;
    background-color: transparent;
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

.no_read {
	display: inline-block;
    width: .4rem;
    height: .4rem;
    border-radius: 50%;
    -webkit-border-radius: 50%;
    background-color: red;
    position: absolute;
    right: 1%;
    top: 31%;
    z-index: 99999;
}
</style>
</head>

<body>
	<nav>
		<div class="Personal-title">
			<span>
				<a href="/m/financial/index.html?menuFlag=account">
					<em>
						<i></i>
						<i id='backBT'></i>
					</em>
					<strong>
						<!-- 返回 --></strong>
				</a>
			</span>
			问题反馈 <div class="addtips" style="right: 0.62rem;text-align: right;top: 0.92rem;position:absolute;">问题列表</div>
			
		</div>
		<span id="no_read" ></span>
	</nav>
	<div class="main_content">
		<div class="question_style">
			<label for="question-type">问题类型：</label>
			<select id="question-type" class="form-control">
				<c:forEach items="${question_list }" var="v">
					<option value="${v.key }">${v.value }</option>
				</c:forEach>
			</select>
			<img src="/static/front/app/images/down.png">
		</div>
		<div>
			<label for="question-desc" class="miao">问题描述：</label>
			<div style="padding-left: .62rem;"><textarea id="question-desc" class="form-control" placeholder="请输入问题描述（不得少于10字）!"></textarea></div>
			<span id="errortips" class="text-danger"></span>
		</div>
		<button id="submitQuestion" class="btn btn-danger btn-block">提交问题</button>
	</div>
	<%-- <div class="row">
			<!-- 右侧 -->
			<div class="col-xs-10 question-right rightarea">
				<!-- <ul class="nav nav-tabs rightarea-tabs question-nav">
					<li class="active">
						<a href="/question/question.html">
							问答
						</a>
					</li>
					 <li class="">
						<a href="/question/questionlist.html">
							列表
						</a>
					</li> 
				</ul> -->
				<div class="col-xs-12 padding-clear padding-top-30 form-horizontal">
					<div class="form-group ">
						<label for="question-type" class="col-xs-2 control-label">
							问题类型
						</label>
							<select id="question-type" class="form-control">
								<c:forEach items="${question_list }" var="v">
									<option value="${v.key }">${v.value }</option>
								</c:forEach>
						</div>
					</div>
					<div class="form-group ">
						
							<textarea id="question-desc" class="form-control" rows="10">请输入问题描述（不得少于10字）!</textarea>
					</div>
						<!-- <div class="form-group ">
							<label for="" class="col-xs-2 control-label"> </label>
							<div class="col-xs-10">
								<span id="errortips" class="text-danger"></span>
							</div>
						</div> -->
					<div class="form-group ">
						<label for="diyMoney" class="col-xs-2 control-label"></label>
						<div class="col-xs-4">
							<button id="submitQuestion" class="btn btn-danger btn-block">提交问题</button>
						</div>
					</div>
				</div>
			</div> --%>
	<!-- </div>
	</div> -->
	<script type="text/javascript" src="${oss_url}/static/front/js/plugin/layer/layer.js"></script>
	<script type="text/javascript" src="${oss_url}/static/front/js/comm/util.js"></script>
	<script type="text/javascript" src="${oss_url}/static/front/js/comm/comm.js"></script>
	<script type="text/javascript" src="/static/front/js/comm/comm.js"></script>

	<script type="text/javascript" src="${oss_url}/static/front/js/language/language_cn.js"></script>
	<script type="text/javascript" src="${oss_url}/static/front/js/question/question.js"></script>
</body>
<script>
	$(function () {
		$.post("/m/trade/getcontact.html",function(obj){
			console.log(obj);
			
		if(obj.code == 200){
			if(obj.Count != 0){
				// $("#no_read").addClass("no_read");
				$("#no_read").attr('class',"no_read");
			}
			if(obj.Count == 0){
				$("#no_read").removeClass("no_read");
			}
		}
	});
	})

	$(".addtips").on('click', function () {
		window.location.href = "/m/financial/contactlist.html";
	})
</script>

</html>