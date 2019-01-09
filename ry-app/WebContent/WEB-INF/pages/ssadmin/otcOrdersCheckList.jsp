<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post"
	action="ssadmin/otcOrdersCheckList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /><input
		type="hidden" name="ftype" value="${ftype}" /><input type="hidden"
		name="pageNum" value="${currentPage}" /> <input type="hidden"
		name="numPerPage" value="${numPerPage}" /> <input type="hidden"
		name="orderField" value="${param.orderField}" /><input type="hidden"
		name="orderDirection" value="${param.orderDirection}" />
</form>

<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/otcOrdersCheckList.html" method="post">
		<div class="searchBar">
			<table class="searchContent">
				<tr>
					<td>订单号:<input type="text" name="keywords" value="${keywords}"
						size="60" /></td>
					<td>用户账户：<input type="text" name="userlogin"
                        value="${userlogin}" size="40" />
                    </td>
					<td><button type="submit" class="chaxun_btn">查询</button></td>
				</tr>
			</table>
			
		</div>
	</form>
</div>
<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<shiro:hasPermission name="ssadmin/viewVirtualCaptual.html">
				<li id="she"><a class="edit" href="javascript:void(0);"
					height="320" width="800" target="ajaxTodo" title="确定要审核吗?" id="abqzz"><span>审核</span>
				</a></li>
			</shiro:hasPermission>
			<li class="line">line</li>
			<shiro:hasPermission name="ssadmin/viewVirtualCaptual.html">
				<li id="sh1"><a class="edit" href="javascript:void(0);"
					height="320" width="800" target="ajaxTodo" title="确定要取消审核吗?" id="abq1"><span>取消审核</span>
				</a></li>
			</shiro:hasPermission>
			<li class="line">line</li>
			<li><a class="icon" href="ssadmin/otcChecktradeExport.html"
				target="dwzExport" targetType="navTab" title="是否导出这些记录吗?"><span>导出</span>
			</a></li>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
        <thead>
            <tr>
                <th width="22"><input type="checkbox" group="ids"
					class="checkboxCtrl">
				</th>
                <th width="40">订单号</th>
                <th width="60">下单时间</th>
                <th width="60">币种名称</th>
                <th width="60">用户账户</th>
                <th width="60">单价</th>
                <th width="60">操作数量</th>
                <th width="85">操作币种产生人民币金额</th>
                <th width="60">订单类型</th>
                <th width="60">交易所在地</th>
                <th width="60">最小限额</th>
                <th width="60">最大限额</th>
                <th width="60">最低价</th>
                <th width="50">收款类型</th>
                <th width="60">广告留言</th>
                <th width="80">是否开启实名认证购买</th>
                <th width="60">是否撤销</th>
                <th width="60">订单状态</th>
                <th width="60">是否实名</th>
                <th width="60">下单用户昵称</th>
                <th width="60">审核状态</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${otcOrdersCheckList}" var="item" varStatus="num">
                <tr target="sid_user" rel="${item.fid}">
                    <td><input name="ids" value="${item.fId}"
						type="checkbox">
					</td>
                    <td>${item.fId}</td>
                    <td><fmt:formatDate value="${item.fCreateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    <td>${item.fName}</td>
                    <td>${item.fUsr_id}</td>
                    <td>${item.fUnitprice}</td>
                    <td>${item.fCount}</td>
                    <td><fmt:formatNumber value="${item.fMoney}" pattern="##.######" maxIntegerDigits="15" maxFractionDigits="6"/></td>
                    <td>${item.fOrdertype eq 0 ? '买入' : '卖出'}</td>
                    <td>${item.fAdr }</td>
                    <td>${item.fSmallprice }</td>
                    <td>${item.fBigprice }</td>
                    <td>${item.fLowprice }</td>
                    <c:if test="${item.fReceipttype == 0}">
                        <td>银行</td>
                    </c:if>
                    <c:if test="${item.fReceipttype == 1}">
                        <td>支付宝</td>
                    </c:if>
                    <c:if test="${item.fReceipttype == 2}">
                        <td>微信</td>
                    </c:if>
                    <td>${item.fMsg}</td>
                    <td>${item.isCertified eq 0 ? '是' : '否' }</td>
                    <td>${item.isUndo eq 0 ? '是' : '否' }</td>
                    <c:if test="${item.fStatus == 0}">
                        <td>挂单中</td>
                    </c:if>
                    <c:if test="${item.fStatus == 1}">
                        <td>售卖中</td>
                    </c:if>
                    <c:if test="${item.fStatus == 2}">
                        <td>已售卖</td>
                    </c:if>
                    <td>${item.isRealName eq 0 ? '是' : '否' }</td>
                    <td>${item.fUsr_Name}</td>
                    <c:if test="${item.isCheck == 0}">
                        <td>未审核</td>
                    </c:if>
                    <c:if test="${item.isCheck == 1}">
                        <td>已审核</td>
                    </c:if>
                    <c:if test="${item.isCheck == 2}">
                        <td>审核失败</td>
                    </c:if>
                </tr>
            </c:forEach>
        </tbody>
    </table>

	<div class="panelBar">
		<div class="pages">
			<span>总共: ${totalCount}条</span>
		</div>
		<div class="pagination" targetType="navTab" totalCount="${totalCount}"
			numPerPage="${numPerPage}" pageNumShown="5"
			currentPage="${currentPage}"></div>
	</div>
</div>

<script>
$(document).ready(function(){
	//审核
	$("#she").hover(function() {
		var check= new Array();
		$('input[name="ids"]:checked').each(function(){ 
			check.push($(this).val()); 
			});
		var url="ssadmin/otcOrdersCheck.html?uid="+check;
		$("#abqzz").attr("href",url);
	})
})

$(document).ready(function(){
	//取消审核
	$("#sh1").hover(function() {
		var check= new Array();
		$('input[name="ids"]:checked').each(function(){ 
			check.push($(this).val()); 
			});
		var url="ssadmin/otcOrdersNoCheck.html?uid="+check;
		$("#abq1").attr("href",url);
	})
})
</script>
