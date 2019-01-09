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
    <title>实名认证</title>
    <link rel="stylesheet" href="/static/front/app/css/css.css" />
    <link rel="stylesheet" href="/static/front/app/css/style.css" />
    <link rel="stylesheet" href="/static/front/app/css/swiper-3.4.2.min.css" />
    <script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js" ></script>
    <script type="text/javascript" src="/static/front/app/js/swiper-3.4.2.jquery.min.js" ></script>
    <script type="text/javascript" src="/static/front/js/layer/layer.js"></script>
</head>
<style>
.realCer{
    width: 100%;
}
.realCer p{
    color: #2b2b2b;
}
.realCer a{
    display: inline-block;
    width: 4.5rem;
    height: 2rem;
    line-height: 2rem;
    text-align: center;
    color: #fff;
    background: #EBA51D;
    border-radius: 0.3rem;
    -webkit-border-radius: 0.3rem;
    margin-top: 0.5rem;
}
.Personal-cont ul li{
    position: relative;
}
.rezbtn{
    display: inline-block;
    width: 5rem;
    height: 2rem;
    line-height: 2rem;
    text-align: center;
    color: #fff;
    background: #EBA51D;
    border-radius: 0.3rem;
    -webkit-border-radius: 0.3rem;
    position: absolute;
    top: 0.5rem;
    right: 0.5rem;
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
				</span>
        实名认证
    </div>
</nav>
<section>
    <div class="Personal-cont">
        <p><img src="/static/front/app/images/succeed.png"></p>
        <h2 class="clearfix">已通过实名认证</h2>
        <ul class="clearfix">
            <c:if test="${fuser.fpostRealValidate }">
                <li><span>姓名：</span>${fuser.frealName }</li>
              <li><span>身份证：</span>${fuser.fidentityNo_s }</li>
            </c:if>
            <c:if test="${fuser.authGrade =='2'||fuser.authGrade == '3' ||fuser.authGrade == '5'}">
              <c:if test="${fuser.fpostImgValidate == true && fuser.fhasImgValidate == false }">
                <li><span>身份证认证:</span>审核中</li>
              </c:if>
              <c:if test="${fuser.fhasImgValidate == true &&fuser.fpostImgValidate == true}">
                 <li><span>身份证认证:</span>认证成功</li>
                 </c:if>
                </c:if>
                 <c:if test="${fuser.fhasRealValidate&&fuser.authGrade == '2' && fuser.faudit == '0' }">
                 <li><span>身份证认证:</span>认证失败
                   <button class="renz rezbtn">请重新认证</button>
                 </li>
            </c:if>
           <%--  <c:if test="${fuser.authGrade != '2' && fuser.authGrade != '3' && fuser.authGrade != '4' && fuser.authGrade !='5'}"> --%>
             <c:if test="${fuser.fhasRealValidate&&fuser.authGrade == '1' }">
                <li class="realCer"><p>身份证未进行认证</p>
                    <c:choose>
                            <c:when test="${fuser.fhasImgValidate && fuser.fpostImgValidate && fuser.faudit == '1' }">
                                <label for="pic" class="s1">已认证</label>
                            </c:when>
                            <c:when test="${!fuser.fhasImgValidate && fuser.fpostImgValidate && fuser.faudit == '2'}">
                                <label for="pic1" class="s1">审核中</label>
                            </c:when>
                            <c:otherwise>
                                <a href="${oss_url}/m/realIdentity.html">去验证</a>
                            </c:otherwise>
                        </c:choose>
                </li>
            </c:if>
            <%-- <c:if test="${fuser.authGrade == '3' }">
              <c:if test="${fuser.fpostBankValidate == true && fuser.fhasBankValidate == false && fuser.faudit == '2'}">
                <li><span>银行卡认证:</span>审核中</li>
              </c:if>
              <c:if test="${fuser.fhasBankValidate == true &&fuser.fpostBankValidate == true && fuser.faudit == '1'}">
                <li><span>银行卡认证:</span>认证成功</li>
                </c:if>
               </c:if>
               <c:if test="${fuser.fhasImgValidate&&fuser.authGrade == '2' && fuser.faudit=='0'}">
                <c:if test="${fuser.fhasBankValidate == false && fuser.fpostBankValidate == false}">
                <li><span>银行卡认证:</span>认证失败
                    <button class="renz1 rezbtn">请重新认证</button>
                </li>
                </c:if>
            </c:if>
            <c:if test="${fuser.fhasImgValidate == true &&fuser.fpostImgValidate == true}">
                <li class="realCer"><p>银行卡未进行认证</p><a onclick="checkLevel()" href="javascript:void(0);">去验证</a></li>
            </c:if> --%>
        </ul>
    </div>
</section>
</body>
<script type="text/javascript" src="/static/front/js/comm/util.js"></script>
<script type="text/javascript" src="/static/front/js/comm/comm.js"></script>
<script>
$(".renz1").click(function(){
    var url ="/m/user/validateKyc4.html?random=" + Math.round(Math.random() * 100);
    jQuery.post(url,{},function(data){
        if(data.code == 0){
        	window.location.href="/m/realIdentity.html?flag=trade";
         }else{
            util.showerrortips('certificationinfo-errortips',data.msg);
        }
    })
})
$(".renz").click(function(){
    var url = "/m/user/validateKyc3.html?random=" + Math.round(Math.random() * 100);
    jQuery.post(url,{},function(data){
        if(data.code == 0){
        	window.location.href="/m/realIdentity.html";
        }else{
            util.showerrortips('certificationinfo-errortips',data.msg);
        }
      })
})
function checkLevel(){
	var authGrade = "${fuser.authGrade}";
	if(authGrade != 2 || authGrade != '2'){
	   if(!${fuser.fpostRealValidate}){
	        layer.msg("请先进行实名认证！");
	    }
	    if(${fuser.fpostImgValidate == true && fuser.fhasImgValidate == false}){
	        layer.msg("请等待审核通过！");
	    }
	    if(${fuser.fhasImgValidate == false && fuser.fpostImgValidate == false}){
	        layer.msg("请认证身份证！");
	    }
	}else{
		if(${fuser.fpostImgValidate && !fuser.fhasImgValidate}){
		  layer.msg("请等待一级审核通过!");
		}else{
			window.location.href="/m/realIdentity.html?flag=trade";
		}
	}
}
</script>
</html>