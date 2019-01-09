<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../front/comm/include.inc.jsp"%>
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
    <title>委托详情</title>
    <link rel="stylesheet" href="/static/front/app/css/css.css" />
    <link rel="stylesheet" href="/static/front/app/css/style.css" />
    <script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js" ></script>
    <style type="text/css">
	    .no_cont{
	        width: 100%;
	        height: auto;
	        text-align: center;
	    }
	    #entrustInfo{
		margin-left: .62rem;
	    }
	    .present_entrust{
		margin-bottom: .62rem;
		border-bottom: 1px solid #e6e6e6; 
		width: 148%;
		padding-bottom: .62rem;
		line-height: 1.5rem;
	    }
	    .present_entrust span{
		display: inline-block;
		width: 6rem;
	    }
	    .flexLayout{
			display: flex;
			justify-content: space-between;
	    }
	    .flexLayout:first-child{
			margin-bottom: .62rem;
	    }
	    .present_entrust .flexLayout li{
			width: 33.33%;
			text-align: center;
	    }
	    .present_entrust .flexLayout .text-l{
			text-align: left;
	    }
	    .present_entrust .flexLayout .text-r{
			text-align: right;
	    }
	    .present_entrust .flexLayout .text-r a{
			color: #5090dc;
	    }
	    .present_entrust .flexLayout .red,.present_entrust .flexLayout .green{
			width: 10%;
			font-size: .87rem;
			font-weight: 600;
			font-stretch: normal;
			letter-spacing: 0rem;
	    }
	    .present_entrust .flexLayout .red{
			color: #fa8221;
	    }
	    .present_entrust .flexLayout .green{
			color: #03c087;
	    }
	    .present_entrust .flexLayout .date{
			width: 50%;
			text-align: left;
			font-weight: normal;
			font-stretch: normal;
			letter-spacing: 0rem;
			color: #93a4b2;
	    }
	    .entrust_num{
			color: #bec8d0;
			font-size: .7rem
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
            <a href="javascript:;" onClick="javascript :history.back(-1)">
                <em>
                    <i></i>
                    <i></i>
                </em>
                <strong><!-- 返回 --></strong>
            </a>
        </span>委托详情
    </div>
</nav>
<div>
    <c:forEach items="${fentrusts1 }" var="v" varStatus="vs">
	    <div class="present_entrust">
			<ul class="flexLayout">
				<li class="${v.fentrustType==0?'red':'green' } text-l">${v.fentrustType_s}${v.fisLimit==true?'[市价]':'' }</li>
				<li class="date"><fmt:formatDate value="${v.fcreateTime }" pattern="yyyy-MM-dd"/></li>
				<li class="text-r">
					<c:if test="${v.fstatus==1 || v.fstatus==2}">
						<a href="javascript:void(0);" class="tradecancel opa-link" data-value="${v.fid}">取消</a>
					</c:if>
					<c:if test="${v.fstatus==3}">
						<a href="javascript:void(0);" class="tradelook opa-link" data-value="${v.fid}">查看</a>
					</c:if>
				</li>
			</ul>
			<ul class="flexLayout">
				<li class="text-l"><div class="entrust_num">单价</div><fmt:formatNumber value="${v.fprize }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${ftrademapping.fcount1 }"/></li>
				<li><div class="entrust_num">委托量</div><fmt:formatNumber value="${v.fcount }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${ftrademapping.fcount2 }"/></li>
				<li class="text-r"><div class="entrust_num">实际价格</div><fmt:formatNumber value="${v.famount }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${ftrademapping.fcount1 }"/></li>
			</ul>
	    </div>
    </c:forEach>
    
    <%-- <tr>
        <td colspan="10" class="text-small-2x" style="text-align: center;" id="entrutsCurFot"><a href="/trade/entrust.html?status=0&symbol=${ftrademapping.fid }">查看更多&gt;&gt;</a></td>
    </tr> --%>
<%-- <c:if test="${fentrusts2.size() >0 }">
    <c:forEach items="${fentrusts2 }" var="v" varStatus="vs">
        <li class="border-bottom">
            <span><fmt:formatDate value="${v.fcreateTime }" pattern="HH:mm:ss"/></span>
            <span><em class="<c:if test="${'买入'==v.fentrustType_s}">sell</c:if><c:if test="${'卖出'==v.fentrustType_s}">pay</c:if>"><i>${v.fentrustType_s}</i>${v.fisLimit==true?'[市价]':'' }${coin1.fSymbol}<fmt:formatNumber value="${v.fprize }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${ftrademapping.fcount1 }"/></em></span>
            <span>${coin2.fSymbol}<fmt:formatNumber value="${v.fcount-v.fleftCount }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${ftrademapping.fcount2 }"/></span>
            <span>${coin1.fSymbol}<fmt:formatNumber value="${v.famount }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${ftrademapping.fcount1 }"/></span>
        </li>
    </c:forEach>
</c:if>
<c:if test="${fentrusts2.size() <= 0 }">
    <div class="no_cont">
        <img src="/static/front/app/images/no_cont.png">
        <p>暂无数据</p>
    </div>
</c:if> --%>
</div>

</body>
<script type="text/javascript" src="/static/front/js/comm/comm.js"></script>
</html>