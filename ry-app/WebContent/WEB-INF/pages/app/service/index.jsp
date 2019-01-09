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
		<title>最新公告</title>
		<link rel="stylesheet" href="/static/front/app/css/css.css" />
		<link rel="stylesheet" href="/static/front/app/css/style.css" />
		<style>
			.nodataBg{
				position:fixed;
				z-index:-1;
				top:0;
				left:0;
				height:100VH;
				width:100VW;
			}
			.nodataBg>div{
				width:4rem;
				height:auto;
				position:absolute;
				left:50%;
				top:50%;
				transform:translate(-50%,-50%);
			}
			.nodataBg>div>img{
				width:100%;
				height:auto;
			}
			.nodataBg>div>span{
				display:block;
				text-align:center;
				color:#999;
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
		<script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js" ></script>
	</head>
	<body>
		<nav>
			<div class="Personal-title">
                <span>
                    <a onClick="javascript :history.back(-1)">
                        <em>
                            <i></i>
                            <i></i>
                        </em>
                        <strong><!-- 返回 --></strong>
                    </a>
                </span>最新公告
            </div>
		</nav>
		<section>
			<%-- <div class="affiche-tab">
				<ul>
					<c:forEach items="${farticletypes }" var="v" varStatus="vs">
						<li class="${vs.index==0?'active':'' }">${v.fname }</li>
					</c:forEach>
				</ul>
			</div> --%>
			<c:forEach items="${arts }" var="v" varStatus="vs">
				<div class="affiche-cont" style="display: block;">
				<ul>
					<c:forEach items="${v }" var="vv" >
					<li>
						<p><a href="${oss_url}/m/service/article.html?id=${vv.fid }">${vv.ftitle_short }</a></p>
						<span><fmt:formatDate value="${vv.flastModifyDate }" pattern="yyyy-MM-dd"/></span>
					</li>
					</c:forEach>
				</ul>
			</div>
			</c:forEach>
		</section>
		<div class='nodataBg'><div><img src='/static/front/app/images/nodata@x2.png'><span>暂无数据</span></div></div>
		<%-- <jsp:include page="../comm/menu.jsp"></jsp:include> --%>
		<script type="text/javascript" src="/static/front/js/comm/util.js"></script>
		<script type="text/javascript" src="/static/front/js/comm/comm.js"></script>
		<script>
			$(function(){
				$(".affiche-tab ul li").on('click',function(){
					$(this).addClass('active').siblings().removeClass('active');
			            var index =  $(".affiche-tab ul li").index(this);
			            $(".affiche-cont").eq(index).show().siblings('.affiche-cont').hide();
				});
				$(".munu ul li").on('click',function(){
					$(this).addClass('active').siblings().removeClass('active');
				});
				if($('li').length){
					$('.nodataBg').css('display','none');
				}
			})
			
		</script>
	</body>
</html>