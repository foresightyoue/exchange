<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../../front/comm/include.inc.jsp" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no,width=device-width">
    <meta name="format-detection" content="telephone=no">
    <meta name="format-detection" content="email=no">
    <base href="${basePath}"/>
    <title>财务日志</title>
    <link rel="stylesheet" href="/static/front/app/css/css.css" />
    <link rel="stylesheet" href="/static/front/app/css/style.css" />
    <link rel="stylesheet" href="/static/front/app/css/mobiscroll.min.css">
    <script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js" ></script>
    <script type="text/javascript" src="/static/front/app/js/mobi.js"></script>
    <script type="text/javascript" src="/static/front/app/js/mobiscroll.min.js"></script>
    <style>
        form.search {
		    padding: 1em 0 1rem .62rem;
		    background: #ececec;
		    clear: both;
		    text-align: center;
		}
		form.search ul {
		    list-style-type: none;
		}
		form.search ul li {
		    min-width: 6em;
		    padding-top: 0.25em;
		    position: relative;
		}
		.downImg{
			position: absolute;
			right: .62rem;
			top: 50%;
			/* transform: translate(0,-50%); */
		}
		form.search ul li input {
		    /* width: 50%; */
		    width: 35%;
		    min-width: 6em;
		    height: 1.75em;
		    line-height: 1.75em;
		    border-radius: .4em;
		    -webkit-border-radius: .4em;
		    border: 1px solid #e6e6e6;
		    padding-left: .6em;
		    color: #333;
		}
		form.search ul li select {
		    /* width: 50%; */
		    width: 25%;
		    min-width: 5em;
		    height: 1.75em;
		    line-height: 1.75em;
		    border-radius: .4em;
		    -webkit-border-radius: .4em;
		    border: 1px solid #e6e6e6;
		    padding-left: .6em;
		    color: #333;
		}
		#enddate{
			margin-left: .3rem;
		}
		form.search ul li input{
		    border: none;
		    outline: none;
		    background-color: white;
		}
		[readonly=readonly] {
		    background-color: #f7f7f7;
		}
		form.search ul li label {
		    display: inline-block;
		    text-align: right;
		    min-width: 5em;
		    max-width: 7em;
		}
		ol.context {
		    list-style-type: none;
		    padding: 0.25em;
		    margin-top: 0;
		}
		ol.context li {
		    padding: 0.5em 0.75em;
		    border-bottom: 1px solid #8c8c8c;
		    list-style: none;
		}
		ol.context li section span {
		    margin-right: 0.5em;
		}
		ol.context li table {
		    width: 100%;
		    border-collapse: collapse;
		}
		table tr {
		    width: 100%;
		    height: 2em;
		    line-height: 2em;
		    cursor: pointer;
		}
		ol.context li table tr td {
		    text-align: left;
		}
		.no-data-tips{
		    display:inline-block;
		    width: 100%;
		    height: 3em;
		    line-height: 3em;
		    text-align: center;
		}
		.content{
			padding-left: .62rem;
			box-sizing: border-box;
			-webkit-box-sizing: border-box;
			background-color: white;
			padding-top: .62rem;
		}
		.content li{
			padding: 0 .62rem 0.62rem 0;
			box-sizing: border-box;
			-webkit-box-sizing: border-box;
			/* margin-top: .62rem; */
			border-bottom: 1px solid #e6e6e6;
		}
		.content li span{
			font-size: .7rem;
			color: #999999;
		}
		.flexLayout{
			display: flex;
			justify-content: space-between;
			line-height: 1.6rem;
			
		}
		.deal_time,.deal_money{
			width: 60%
		}
		
		.deal_stype,.serviceMoney{
			width: 35%;
		}
		.deal_status{
			text-align: right;
			color: red;
		}
		/* .deal_time>div,.deal_money>div{
			margin-bottom: .8rem;
			line-height: 1.3rem;
		} */
    </style>
</head>
<body>
<nav>
    <div class="Personal-title">
          <span>
             <a href="#" onClick="backJump()">
                  <em>
                    <i></i>
                    <i></i>
                  </em>
                  <strong>返回</strong>
             </a>
          </span>财务日志
    </div>
</nav>
<div class="main-con">
    <form action="" class="search">
        <ul style="text-align: left;">
            <li style="margin-bottom: .62rem;">
                <label for="begindate">起始时间：</label>
                <input class="databtn datainput" id="begindate" value="${begindate }" readonly="readonly" onClick="mobiDate('begindate')">
            <!-- </li>
            <li> -->
                <!-- <label for="enddate">结束时间：</label> -->
                -<input class="databtn datainput" id="enddate" value="${enddate }" readonly="readonly" onClick="mobiDate('enddate')">
            </li>
            <li style="display: inline-block;">
                <label for="datetype" style="text-align: left;">时间段：</label>
                <select id="datetype" name="datatype" class="datatime">
                    <option value="1" ${datetype==1?'selected':''}>今天</option>
                    <option value="2" ${datetype==2?'selected':''}>7天</option>
                    <option value="3" ${datetype==3?'selected':''}>15天</option>
                    <option value="4" ${datetype==4?'selected':''}>30天</option>
                </select>
                <img alt="" src="${cdn }/static/front/app/images/down.png" class="downImg">
            </li>
            <li style="float: right; margin-right: .62rem;">
                <label for="recordType">操作类型：</label>
                <select id="recordType" name="recordType" style="width: 7em;">
                    <c:forEach items="${filters }" var="v">
                       <c:choose>
                          <c:when test="${select==v.value}">
                             <option value="${v.key }" selected="selected">${v.value }</option>
                          </c:when>
                          <c:otherwise>
                              <option value="${v.key }">${v.value }</option>
                          </c:otherwise>
                       </c:choose>
                    </c:forEach>
                </select>
                <img alt="" src="${cdn }/static/front/app/images/down.png" class="downImg">
            </li>
        </ul>
    </form>
    <section role="content">
        <ol class="content">
            <c:choose>
                 <c:when test="${recordType==1 || recordType==2 }">
                    <%--人民币充值,提现 --%>
                    <c:forEach items="${list }" var="v">
                    <li>
                    	<div class="flexLayout">
                   			<div class="deal_time">交易时间：<span><fmt:formatDate value="${v.fcreateTime }" pattern="yyyy-MM-dd HH:mm:ss"/></span></div>
                   			<div class="deal_stype">交易类型：<span>${select }</span></div>
                    	</div>
                    	<div class="flexLayout">
                   			<div class="deal_money">交易金额：<span>$<fmt:formatNumber value="${v.famount }" pattern="00.00" maxIntegerDigits="15" maxFractionDigits="4"/></span></div>
                   			<div class="serviceMoney">手续费：<span>$<fmt:formatNumber value="${v.ffees }" pattern="00.00" maxIntegerDigits="15" maxFractionDigits="4"/></span></div>
                    	</div>
                    	<div class="deal_status">${v.fstatus_s }</div>
                       <%-- <table>
		                    <tr>
		                       <td><fmt:formatDate value="${v.fcreateTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
		                       <td>${select }</td>
		                       <td class="red">$<fmt:formatNumber value="${v.famount }" pattern="00.00" maxIntegerDigits="15" maxFractionDigits="4"/></td>
		                       <td>$<fmt:formatNumber value="${v.ffees }" pattern="00.00" maxIntegerDigits="15" maxFractionDigits="4"/></td>
		                       <td>${v.fstatus_s }</td>
		                    </tr>
                       </table> --%>
                    </li>
                    </c:forEach>
                  </c:when>
                  <c:when test="${recordType==3 || recordType==4 }">
                    <%--充值,提现--%>
                    <c:forEach items="${list }" var="v">
                    <li>
                    	<div class="flexLayout">
                   			<div class="deal_time">交易时间：<span><fmt:formatDate value="${v.fcreateTime }" pattern="yyyy-MM-dd HH:mm:ss"/></span></div>
                   			<div class="deal_stype">交易类型：<span>${select }</span></div>
                    	</div>
                    	<div class="flexLayout">
                   			<div class="deal_money">交易金额：<span>${v.fvirtualcointype.fSymbol }<fmt:formatNumber value="${v.famount }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="4"/></span></div>
                   			<div class="serviceMoney">手续费：<span>${v.fvirtualcointype.fSymbol }<fmt:formatNumber value="${v.ffees }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="4"/></span></div>
                    	</div>
                    	<div class="deal_status">${v.fstatus_s }</div>
                    
                   <%--  <table>
                    <tr>
                       <td><fmt:formatDate value="${v.fcreateTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                       <td>${select }</td>
                       <td class="red">${v.fvirtualcointype.fSymbol }<fmt:formatNumber value="${v.famount }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="4"/></td>
                       <td>${v.fvirtualcointype.fSymbol }<fmt:formatNumber value="${v.ffees }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="4"/></td>
                       <td>${v.fstatus_s }</td>
                    </tr>
                    </table> --%>
                    </li>
                    </c:forEach>
                  </c:when>
            </c:choose>
            <c:if test="${fn:length(list)==0 }">
                <span class="no-data-tips">你暂时没有账单记录
                </span>
            </c:if>
        </ol>
    </section>
</div>
<script type="text/javascript" src="${oss_url}/static/front/js/finance/account.record1.js"></script>
<script type="text/javascript" src="/static/front/js/comm/util.js"></script>
<script type="text/javascript" src="/static/front/js/comm/comm.js"></script>
<script type="text/javascript">
    function backJump() {
		if (${from eq 'bill'}) {
			location.href='${oss_url}/m/financial/zichan.html?menuFlag=zichan';
		}else {
			location.href='${oss_url}/m/financial/index.html?menuFlag=account';
		}
	}
</script>
</body>
</html>