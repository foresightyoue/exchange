<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../comm/include.inc.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path;
%>

<!doctype html>
<html>
<head>
<base href="${basePath}" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@include file="../comm/link.inc.jsp"%>

<link rel="stylesheet" href="${oss_url}/static/front/css/zhongchou/zc.css" type="text/css"></link>
</head>
<body>


	<%@include file="../comm/header.jsp"%>




	<div class="container-full main-con">
		<div class="container displayFlex">
<div class="row">
			<div class="autobox">

             <c:forEach items="${fsubscriptions }" var="v">
				<div class="crowd_con crowd_con_list clear">
					<div class="crowd_con_l crowd_con_list_l">
						<div class="crowd_bi clear">
							<span class="crowd_bi_hlb">
							<img src="${v.fvirtualcointype.furl }" width="50px" height="50px"/>
							</span>
							<h2>
								<a href="/crowd/view.html?fid=${v.fid }">${v.ftitle }</a>
							</h2>
							<div class="crowd_hui crowd_hui_${v.fstatus=='进行中'?'green':'red' }">
								<em></em>${v.fstatus }
							</div>
							<div style="float: right;width: 60px;height: 30px;border-radius: 2px;background-color: red;margin-top: 14px;
							margin-right: 10px;line-height: 28px;padding: 2px 8px;color: #fff;text-align: center;display: ${v.fisICO==true?'block':'none' };">ICO</div>
						</div>
						<ul class="crowd_subscribe clear">
							<li style="list-style-type:none;">
								<p>支持总量(${v.fvirtualcointype.fShortName })</p>
								<span class="red" style="float:left; padding:5px 20px;"> <fmt:formatNumber value="${v.ftotal }" pattern="##.##" maxIntegerDigits="20" maxFractionDigits="0"/></span></li>
							<li style="list-style-type:none;">
								<p>最多支持数量</p>
								<span class="red" style="float:left; padding:5px 20px;">
								<c:choose>
								<c:when test="${v.fbuyCount ==0 }">
								不限制
								</c:when>
								<c:otherwise>
								<c:if test="${v.fisICO==true}">${v.symbol1}</c:if>
								<fmt:formatNumber value="${v.fbuyCount }" pattern="##.##" maxIntegerDigits="20" maxFractionDigits="0"/>
								</c:otherwise>
								</c:choose>
								</span></li>
							<li style="list-style-type:none;">
								<p>最小支持数量</p>
								<span class="red" style="float:left; padding:5px 20px;">
								<c:if test="${v.fisICO==true}">${v.symbol1}</c:if>
                                <fmt:formatNumber value="${v.fminbuyCount }" pattern="##.##" maxIntegerDigits="20" maxFractionDigits="0"/>
								</span></li>	
							<li style="list-style-type:none;">
								<p>最多支持次数</p>
								<span class="red" style="float:left; padding:5px 20px;">
								<c:choose>
								<c:when test="${v.fbuyTimes ==0 }">
								不限制
								</c:when>
								<c:otherwise>
								<fmt:formatNumber value="${v.fbuyTimes }" pattern="##.##" maxIntegerDigits="20" maxFractionDigits="0"/>
								</c:otherwise>
								</c:choose>
								</span></li>
							
							<%--<li style="list-style-type:none;">--%>
								<%--<p>单价</p>--%>
								<%--<span class="red">--%>
								<%--<c:if test="${!v.fisICO }">--%>
								<%--${v.symbol1 } <fmt:formatNumber value="${v.fprice }" pattern="##.##" maxIntegerDigits="20" maxFractionDigits="4"/>--%>
								<%--</c:if>--%>
								<%--<c:if test="${v.fisICO }">--%>
								<%--以支持结果为准--%>
								<%--</c:if>--%>
								<%--</span>--%>
								<%--</li>--%>
							
						</ul>
					</div>
					
					<div class="crowd_con_list_r crowd_con_list_r">
						<p class="crowd_con_lp">
						<c:if test="${v.fisICO }">
						已支持${v.symbol }:
						</c:if>
						<c:if test="${!v.fisICO }">
						进度:
						</c:if>
							<%--<b class="green"> <fmt:formatNumber value="${v.fAlreadyByCount }" pattern="##.##" maxIntegerDigits="20" maxFractionDigits="0"/></b>--%>
						<span class="crowd_con_lp">
							<progress value="<fmt:formatNumber value="${v.fAlreadyByCount }" pattern="##.##" maxIntegerDigits="20" maxFractionDigits="0"/>" max="<fmt:formatNumber value="${v.ftotal }" pattern="##.##" maxIntegerDigits="20" maxFractionDigits="0"/>"></progress>
							<span><fmt:formatNumber value="${v.fAlreadyByCount/v.ftotal*100 }" pattern="##.##" maxIntegerDigits="20" maxFractionDigits="2"/>%</span>
						</span>
						</p>
						<p class="crowd_con_lp">
						<c:if test="${!v.fisICO }">
							已支持数量 ：<b class="green"><fmt:formatNumber value="${v.fAlreadyByCount }" pattern="##.##" maxIntegerDigits="20" maxFractionDigits="0"/></b>
						</c:if>	
						</p>
						
						<div class="login_button">
							<a href="/crowd/view.html?fid=${v.fid }" class="login_button_zc ${v.fstatus=='进行中'?'':'login_button_zc_js' }">
							${v.fstatus=='进行中'?'立即支持':v.fstatus }
							</a>
						</div>
					</div>
				</div>
           </c:forEach>
				

				
			</div>
		</div>
	</div>
</div>


	<%@include file="../comm/footer.jsp"%>


</body>
</html>