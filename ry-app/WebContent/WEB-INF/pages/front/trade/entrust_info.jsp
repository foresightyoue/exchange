<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../comm/include.inc.jsp"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>

<h4 id="currentH4">
	<font style="vertical-align: inherit;cursor: pointer">
		<font style="vertical-align: inherit;cursor: pointer" id="currentFont1" class="text-color2" onclick="showCurrent();">当前委托</font></font>
	<font style="vertical-align: inherit;cursor: pointer">
		<font style="vertical-align: inherit;cursor: pointer" onclick="showMydeal();" id="currentFont2">我的成交</font></font>
	<font style="float: right;cursor: pointer;">
		<font style="vertical-align: inherit;cursor: pointer" id="yijianCancel" class="allcancel opa-link" data-value="${ftrademapping.fid }">全部撤单</font></font>
</h4>
<div class="trade" id="currentDiv">
	<table class="table2 table-striped table-hover table-condensed border-light-grey trading-orderbook" id="fentrustsbody1">
		<thead>
		<tr class="row-head row-narrow row-align-right regular-font-size">
			<th>委托时间</th>
			<th>类型</th>
			<th>委托价格</th>
			<th>委托数</th>
			<th>总金额</th>
			<th>成交数量</th>
			<th>成交金额</th>
			<th>手续费</th>
			<th>状态</th>
			<th>操作</th>
		</tr>
		</thead>
		<tbody id="entrutsCurData">
		<c:forEach items="${fentrusts1 }" var="v" varStatus="vs">
			<tr>
				<td class="text-small-2x gray"><fmt:formatDate value="${v.fcreateTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td class="text-small-2x ${v.fentrustType==0?'text-danger':'text-success' }">${v.fentrustType_s}${v.fisLimit==true?'[市价]':'' }</td>
				<td class="text-small-2x"><fmt:formatNumber value="${v.fprize }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${ftrademapping.fcount1 }"/></td>
				<td class="text-small-2x"><fmt:formatNumber value="${v.fcount }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${ftrademapping.fcount2 }"/></td>
				<td class="text-small-2x"><fmt:formatNumber value="${v.famount }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${ftrademapping.fcount1 }"/></td>
				<td class="text-small-2x"><fmt:formatNumber value="${v.fcount-v.fleftCount }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${ftrademapping.fcount2 }"/></td>
				<td class="text-small-2x"><fmt:formatNumber value="${v.fsuccessAmount }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${ftrademapping.fcount1 }"/></td>
				<c:choose>
					<c:when test="${v.fentrustType==0 }">
						<td class="text-small-2x"><fmt:formatNumber value="${v.ffees}" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${ftrademapping.fcount1 }"/></td>
					</c:when>
					<c:otherwise>
						<td class="text-small-2x"><fmt:formatNumber value="${v.ffees}" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${ftrademapping.fcount1 }"/></td>
					</c:otherwise>
				</c:choose>
				<td class="text-small-2x">
						${v.fstatus_s }
				</td>
				<td class="text-small-2x opa-link">
					<c:if test="${v.fstatus==1 || v.fstatus==2}">
						<a href="javascript:void(0);" class="tradecancel opa-link" data-value="${v.fid}">取消</a>
					</c:if>
					<c:if test="${v.fstatus==3}">
						<a href="javascript:void(0);" class="tradelook opa-link" data-value="${v.fid}">查看</a>
					</c:if>
				</td>
			</tr>
		</c:forEach>

		<tr>
			<td colspan="10" class="text-small-2x" style="text-align: center;" id="entrutsCurFot"><a href="${oss_url}/trade/entrust.html?status=0&symbol=${ftrademapping.fid }">查看更多&gt;&gt;</a></td>
		</tr>
		</tbody>
	</table>
</div>

<h4 id="mydealH4" style="display: none">
	<font style="vertical-align: inherit;cursor: pointer">
		<font style="vertical-align: inherit;cursor: pointer" id="myDealFont1" onclick="showCurrent();">当前委托</font></font>
	<font style="vertical-align: inherit;cursor: pointer">
		<font style="vertical-align: inherit;cursor: pointer" id="myDealFont2" onclick="showMydeal();">我的成交</font></font>
</h4>

<div class="trade" id="mydealDiv" style="display: none">
	<table class="table2 table-striped table-hover table-condensed border-light-grey trading-orderbook" id="fentrustsbody0">
		<thead>
		<tr class="row-head row-narrow row-align-right regular-font-size">
			<th>委托时间</th>
			<th>类型</th>
			<th>委托价格</th>
			<th>委托数</th>
			<th>总金额</th>
			<th>成交数量</th>
			<th>成交金额</th>
			<th>平均成交价</th>
			<th>手续费</th>
			<th>状态</th>
		</tr>
		</thead>
		<tbody id="entrutsCurData">
		<c:forEach items="${fentrusts2 }" var="v" varStatus="vs">
			<tr>
				<td class="text-small-2x gray"><fmt:formatDate value="${v.fcreateTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td class="text-small-2x ${v.fentrustType==0?'text-danger':'text-success' }">${v.fentrustType_s}${v.fisLimit==true?'[市价]':'' }</td>
				<td class="text-small-2x"><fmt:formatNumber value="${v.fprize }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${ftrademapping.fcount1 }"/></td>
				<td class="text-small-2x"><fmt:formatNumber value="${v.fcount }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${ftrademapping.fcount2 }"/></td>
				<td class="text-small-2x"><fmt:formatNumber value="${v.famount }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${ftrademapping.fcount1 }"/></td>
				<td class="text-small-2x"><fmt:formatNumber value="${v.fcount-v.fleftCount }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${ftrademapping.fcount2 }"/></td>
				<td class="text-small-2x"><fmt:formatNumber value="${v.fsuccessAmount }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${ftrademapping.fcount1 }"/></td>
				<td class="text-small-2x"><fmt:formatNumber value="${((v.fcount-v.fleftCount)==0)?0:  v.fsuccessAmount/((v.fcount-v.fleftCount)) }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${ftrademapping.fcount1 }"/></td>
				<c:choose>
					<c:when test="${v.fentrustType==0 }">
						<td class="text-small-2x"><fmt:formatNumber value="${v.ffees-v.fleftfees}" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${ftrademapping.fcount1 }"/></td>
					</c:when>
					<c:otherwise>
						<td class="text-small-2x"><fmt:formatNumber value="${v.ffees-v.fleftfees}" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${ftrademapping.fcount1 }"/></td>
					</c:otherwise>
				</c:choose>
				<td class="text-small-2x">
						${v.fstatus_s }
				</td>
			</tr>
		</c:forEach>

		<tr>
			<td colspan="10" class="text-small-2x" style="text-align: center;" id="entrutsCurFot"><a href="${oss_url}/trade/entrust.html?status=1&symbol=${ftrademapping.fid }">查看更多&gt;&gt;</a></td>
		</tr>
		</tbody>
	</table>
</div>

<script>
	function showCurrent()
	{
		$("#currentH4").show();
        $("#currentDiv").show();
        $("#mydealH4").hide();
        $("#mydealDiv").hide();
        $("#currentFont1").attr("class","text-color2");
        $("#myDealFont1").attr("class","text-color2");
        $("#currentFont2").attr("class","");
        $("#myDealFont2").attr("class","");
        $("body").removeClass("entrustInfo-active");
	}

	function showMydeal()
	{
        $("#currentH4").hide();
        $("#currentDiv").hide();
        $("#mydealH4").show();
        $("#mydealDiv").show();
        $("#currentFont1").attr("class","");
        $("#myDealFont1").attr("class","");
        $("#currentFont2").attr("class","text-color2");
        $("#myDealFont2").attr("class","text-color2");
        $("body").addClass("entrustInfo-active");
	}
</script>