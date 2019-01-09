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
    <title>安全设置</title>
    <link rel="stylesheet" href="/static/front/app/css/css.css" />
    <link rel="stylesheet" href="/static/front/app/css/style.css" />
    <link rel="stylesheet" href="/static/front/app/css/swiper-3.4.2.min.css" />
    <script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js" ></script>
    <script type="text/javascript" src="/static/front/app/js/swiper-3.4.2.jquery.min.js" ></script>
    <style type="text/css">
		.exit_btn ul{
			height: 3.06rem;
			line-height: 3.06rem;
			font-size: 0.87rem;
			border-radius: 0.15rem;
			text-align: center;
			margin: 4rem 0.62rem;
			background: #EBA51D;
			color: #fff;
		}
		.exit_btn{
			padding-left: 0;
			background-color: transparent;
			border: 0;
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
</head>
<body>
<nav>
    <div class="Personal-title">
				<span>
					<a href="${oss_url}/m/financial/index.html">
						<em>
							<i></i>
							<i></i>
						</em>
						<strong><!-- 返回 --></strong>
					</a>
				</span>
        安全设置
    </div>
</nav>
<section>
    <div class="user-list clearfix bg-img01">
        <ul>
            <li <c:if test="${isPostRealValidate == true}">class="active"</c:if>>
                <a href="${oss_url}/m/user/realCertification.html"></a>
                <p>实名认证</p>
                <%-- <span><c:if test="${isPostRealValidate == false  || fname =='admin'}">未认证</c:if><c:if test="${isPostRealValidate == true && fname!='admin'}">已认证</c:if></span> --%>
                <span>
                   <c:if test="${!fuser.fpostRealValidate ||fname =='admin' }">未认证</c:if>
                   <c:if test="${fuser.authGrade=='1' && fname !='admin'}">一级认证</c:if>
                   <c:if test="${fuser.authGrade=='2' && fuser.fpostImgValidate && !fuser.fhasImgValidate }">一级认证</c:if>
                   <c:if test="${fuser.authGrade=='3' && fuser.fpostImgValidate && fuser.fpostBankValidate &&!fuser.fhasImgValidate}">一级认证</c:if>
                   <c:if test="${fuser.authGrade=='3' && fuser.fpostBankValidate &&fuser.fpostImgValidate &&fuser.fhasImgValidate && !fuser.fpostBankValidate}">二级认证</c:if>
                   <c:if test="${fuser.authGrade=='2' && fuser.fhasImgValidate && fuser.fpostImgValidate}">二级认证</c:if>
                   <c:if test="${fuser.authGrade=='3' && fuser.fhasBankValidate && fuser.fpostBankValidate}">三级认证</c:if>
                </span>
            </li>
            <!-- <li <c:if test="${isBindEmail == true }">class="active"</c:if>>
                <a href="${oss_url}/m/securityEmail.html?type=0"></a>
                <p>邮箱绑定</p>
                <span><c:if test="${isBindEmail == false }">未绑定</c:if><c:if test="${isBindEmail == true }">已绑定</c:if></span>
            </li> -->
            <li <c:if test="${isBindTelephone == true }">class="active"</c:if>>
                <!-- <a href="/m/securityTelephone.html"></a> -->
                <a href="${oss_url}/m/securityEmail.html?type=1"></a>
                <p>手机绑定</p>
                <span><c:if test="${isBindTelephone == false }">未绑定</c:if><c:if test="${isBindTelephone == true }">已绑定</c:if></span>
            </li>
            <%-- <li <c:if test="${isBindGoogle == true }">class="active"</c:if>>
                <!-- <a href="/m/securityTelephone.html"></a> -->
                <a href="${oss_url}/m/security/securityGoogle.html"></a>
                <p>绑定谷歌验证</p>
                <span><c:if test="${sessionScope.login_user.fgoogleCheck == false}">已停用</c:if><c:if test="${sessionScope.login_user.fgoogleCheck == true}">已启用</c:if></span>
                <span><c:if test="${isBindGoogle == false }">未绑定</c:if><c:if test="${isBindGoogle == true }">已绑定</c:if></span>
            </li> --%>
        </ul>
    </div>
    <div class="user-list clearfix bg-img02">	
        <ul>
            <li>
                <!-- <a href="xiugaidenglumima.html"></a> -->
                <a href="${oss_url}/m/security/loginPwd.html"></a>
                <p>登录密码</p>
                <span>修改</span>
            </li>
            <li>
               <!--  <a href="xiugaizhifumima.html"></a> -->
               <a href="${oss_url}/m/securityEmail.html?type=2"></a>
                <p>支付密码</p>
                <span>修改</span>
            </li>
        </ul>
    </div>
<!--     <div class="user-list clearfix bg-img02 exit_btn">
        <ul>
            <li style="background: none; text-align: center; padding: 0;">
                <a onclick="clearUserInfo()" href="/m/user/logout.html"></a>
                退出登录
            </li>
        </ul>
    </div> -->
</section>
<%-- <jsp:include page="../comm/menu.jsp"></jsp:include> --%>
</body>
<script type="text/javascript" src="/static/front/js/comm/util.js"></script>
<script type="text/javascript" src="/static/front/js/comm/comm.js"></script>
<script>
    $(function(){
        $(".munu ul li").on('click',function(){
            $(this).addClass('active').siblings().removeClass('active');
        });
    });
    function clearUserInfo() {
        deleteCookie("loginName","/");
        deleteCookie("password","/");
    }
    function deleteCookie(name,path){   /**根据cookie的键，删除cookie，其实就是设置其失效**/  
        var name = escape(name);  
        var expires = new Date(0);  
        path = path == "" ? "" : ";path=" + path;  
        document.cookie = name + "="+ ";expires=" + expires.toUTCString() + path;  
    }
</script>
</html>