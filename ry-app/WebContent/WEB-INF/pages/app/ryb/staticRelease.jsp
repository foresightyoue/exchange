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
        <title>静态释放</title>
        <link rel="stylesheet" href="/static/front/app/css/css.css" />
        <link rel="stylesheet" href="/static/front/app/css/style.css" />
        <link rel="stylesheet" href="/static/front/app/css/swiper-3.4.2.min.css" />
        <script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js" ></script>
        <script type="text/javascript" src="/static/front/app/js/swiper-3.4.2.jquery.min.js" ></script>
        <script type="text/javascript" src="/static/front/app/js/bankCardAttribution.js" ></script>
        
        <style>
				.ylwHeader{
					background: #EBA51D;
				    width: 96%;
				    padding: 1rem;
				    margin: auto;
				    border-bottom-left-radius: 0.2rem;
				    border-bottom-right-radius: 0.2rem;
				    color: #fff;
				    font-size: 1rem;
				}
				.ylwHeader p{
					line-height:2.5rem;
				}
				.firstLine{
					display: flex;
				    justify-content: space-between;
				    align-items: center;
				}
				.fillCard{
					margin-top:0.5rem;
					background:#fff;
					display:flex;
					flex-flow:nowrap row;
					line-height:2rem;
					padding: 0.5rem;
				}
				.fillCard span{
					line-height:2rem;
					font-size:1rem;
					flex: 0 0 6rem;
    				text-align: center;
				}
				.fillCard p{
					flex:0 0 60%;
					padding:0.5rem;
					border:1px solid #dfdfdf;
					border-radius:0.2rem;
				}
				.nextBtn{
					display: block;
				    width: 90%;
				    text-align: center;
				    margin: auto;
				    background: #EBA51D;
				    height: 2.6rem;
				    line-height: 2.5rem;
				    color: #fff;
				    border-radius: 0.2rem;
				    margin-top: 3.5rem;
				}
				.process-item{
					display:flex;
					flex-flow:nowrap row;
					justify-content:flex-start;
					align-items:center;
				}
				.process-wraper{
					flex: 0 0 55%;
				    height: 0.25rem;
				    background: #dfdfdf;
				    margin: 0 0.6rem;
			    }
			    .process-bar{
		    	    display: block;
				    height: 100%;
				    background: #F67003;
				    width: 43.5%;
			    }
        </style>
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
                </span>静态释放第3期
            </div>
        </nav>
        <div class='ylwHeader'>
         	<p class='firstLine'><span>目标金额:&nbsp;1000,000.00</span>锁定:&nbsp;210天<span></span></p>
            <p>最低购买&nbsp;100</p>
            <p class='process-item'>完成度<span class='process-wraper'><span class='process-bar'></span></span>43.5%</p>
        </div>
           
         <div class='fillCard'>
             <span>项目介绍</span><p>项目介绍。。。。。。<br/>项目介绍。。。。。。</p>
         </div>
         <div class='fillCard'>
             <span>购买数量</span><input type="number" id='num' name="num" placeholder='请输入购买数量'>
         </div>
         
         <span class='nextBtn'>确定</span>

        <jsp:include page="../comm/menu.jsp"></jsp:include>
<script type="text/javascript" src="${oss_url}/static/front/js/comm/util.js"></script>
<script type="text/javascript" src="/static/front/app/js/index.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/plugin/layer/layer.js"></script>
<script>
	$('#num').on('blur',function(){
		$(this).val(parseFloat($(this).val()).toFixed(4));
	})
</script>
</body>
</html>
