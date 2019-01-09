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
    <title>推广中心</title>
    <link rel="stylesheet" href="/static/front/app/css/css.css" />
    <link rel="stylesheet" href="/static/front/app/css/style.css" />
    <script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js" ></script>
</head>
<style>
*{
    font-family: PingFang-SC-Bold;
}
.introl-title{
    height: 6.27rem;
    margin: 0.73rem auto;
    background: #EBA51D;
    padding: 1.67rem;
    color: #fff;
    display: flex;
    display: -webkit-flex;
    justify-content: space-around;
    align-items: center;
}
.introl-title>div{
    width: 100%;
    text-align: center;
}
.introl-title>div h3{
    font-size: 1.3rem;
}
.introl-title>div h4{
    font-size: 1.2rem;
    color: #fff;
}
.introl-nav{
    width: 100%;
    height: 2.87rem;
    background: #fff;
    display: flex;
    display: -webkit-flex;
    justify-content: space-around;
    align-items: center;
    color: #6e81a7;
    border-bottom: 1px solid #e6ebf1;
}
.introl-nav span{
    font-size: 1rem;
}
.introl-nav .active{
    width: 8em;
    height: 2.87rem;
    line-height: 2.87rem;
    border-bottom: 2px solid #5786d2;
    color: #5786d2;
    text-align: center;
}
.introl-con{
    width: 100%;
    height: auto;
    background: #fff;
    display: none;
}
.introl-con .my-referee{
    width:100%;
    border-bottom: 1px solid #e6ebf1;
}
.my-referee h3,.recommend h3{
    font-size: 1.07rem;
    color: #334f66;
    padding: 1rem 0.7rem;
}
.introl-con table{
    width: 100%;
}
.introl-con table tr td{
    text-align: center;
    color: #334f66;
    font-size: 0.87rem;
    line-height: 1.6rem;
}
.introl-con table tr:first-child td{
    color: #bec8d0;
    font-size: 0.7rem;
}
center{
    padding: 2em 0;
}
.Personal-title span em{
    width: 1.2rem;
    height: 1.2rem;
    position: absolute;
    top: 0.65rem;
    left: 0;
}
.Personal-title span em i:last-child{
    left: 0.22rem;
}
</style>
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
        </span>推广中心
    </div>
</nav>
<div class="introl-title">
    <div>
    	<h4>推广人数</h4>	
        <h3>${fusers.data.total }</h3>
    </div>
</div>
<!-- <div class="introl-nav">
    <span class="active">推荐中心</span>
    <span>推荐金额记录</span>
</div> -->
<div class="introl-con" style="display: block;">
    <div class="recommend">
        <h3>我推荐的人</h3>
        
        <table>
            <tr>
                <td>账户</td>
               <!--  <td>注册时间</td> -->
                <td>联系方式</td>
            </tr>
            <c:if test="${fn:length(fusers.data.user)!=0 }">
            <c:forEach var="item" items="${fusers.data.user }">
            <tr>
                <td>${item.userId }</td>
                <%-- <td><fmt:formatDate value="${item.fregisterTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td> --%>
                <td>
                    <%-- <c:if test="${!item.fpostRealValidate}">未实名认证</c:if>
                    <c:if test="${item.fhasRealValidate&&item.fpostImgValidate && !item.fhasImgValidate}">已实名,未审核</c:if>
                    <c:if test="${item.fhasImgValidate||item.authGrade eq 3 }">已通过审核</c:if> --%>
                    ${item.mobile}
                </td>
            </tr>
            </c:forEach>
            </c:if>
        </table>
       
        <c:if test="${fn:length(fusers.data.user)==0 }">
            <center> 暂无记录 </center>
        </c:if>
    </div>
</div>

</body>
<script type="text/javascript" src="/static/front/js/comm/util.js"></script>
<script type="text/javascript" src="/static/front/js/comm/comm.js"></script>
<script type="text/javascript">
$(function(){
	$(".introl-nav span").on("click",function(){
		$(this).addClass("active").siblings().removeClass("active");
		$(".introl-con").hide();
		$(".introl-con").eq($(this).index()).show();
	});
});
</script>
</html>