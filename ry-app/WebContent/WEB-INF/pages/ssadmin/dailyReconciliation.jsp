<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/dailyReconciliation.html">
    <input type="hidden" name="ftype" value="${ftype}" />
    <input type="hidden" name="pageNum" value="${currentPage}" /> 
    <input type="hidden" name="numPerPage" value="${numPerPage}" />
    <input type="hidden" name="startDate" value="${startDate}" />
    <input type="hidden" name="endDate" value="${endDate}" />
</form>


<div class="pageHeader">
    <form onsubmit="return navTabSearch(this);" action="ssadmin/dailyReconciliation.html" method="post">
        <div class="searchBar">
            <table class="searchContent">
                <tr>
                    <td>开始时间： <input type="text" id="startDate" name="startDate" class="date"
                        readonly="true" value="${startDate }" />
                    </td> 
                    <td>结束时间： <input type="text" id="endDate" name="endDate" class="date"
                        readonly="true" value="${endDate }" />
                    </td> 
                    <td>虚拟币类型： <select type="combox" name="ftype">
                            <c:forEach items="${typeMap}" var="type">
                                <c:if test="${type.key eq ftype}">
                                    <option value="${type.key}" selected="true">${type.value}</option>
                                </c:if>
                                <c:if test="${type.key ne ftype}">
                                    <option value="${type.key}">${type.value}</option>
                                </c:if>
                            </c:forEach>
                    </select>
                    </td>
                </tr>
            </table>
            <div class="subBar">
                <ul>
                    <li><div class="buttonActive">
                            <div class="buttonContent">
                                <button type="submit">查询</button>
                            </div>
                        </div></li>
                </ul>
            </div>
            <div class="panelBar">
                <ul style="float: left;">
                    <li><a id="doAutoCheckBalance" class="buttonActive"
                        href=""
                        target="ajaxTodo" title="统计多天数据将等待过长时间，建议只统计1天的数据，是否现在统计?"><span>统计当前时间</span> </a>
                    </li>
                </ul>
            </div>
        </div>
    </form>
</div>
<div class="pageContent">
    <div class="panelBar">
    <ul class="toolBar">
    <li><a class="icon" href="ssadmin/dailyReconciliationExport.html?startDate=${startDate}&endDate=${endDate}"
                    target="dwzExport" targetType="navTab" title="实要导出这些记录吗?"><span>导出</span>
                </a></li>
    </ul>
    </div>
    <table class="table" width="100%" layoutH="138">
        <thead>
            <tr>
                <th width="30">编号</th>
                <th width="30">币种</th>
                <th width="60">统计时间</th>
                <th width="60">前一日账面余额</th>
                <th width="60">前一日钱包实际余额</th>
                <th width="60">前一日总差额</th>
                <th width="60">账面余额</th>
                <th width="60">钱包余额</th>
                <th width="60">总差额</th>
                <th width="60">账面充值数额</th>
                <th width="60">用户钱包实际转入额</th>
                <th width="60">账面提现数额</th>
                <th width="60">账面申请提现中数额</th>
                <th width="60">用户钱包实际转出额</th>
                <th width="60">买入总额</th>
                <th width="60">卖出总额</th>
                <th width="60">后台手工调整数额</th>
                <th width="60">财务人员手工转入数额</th>
                <th width="60">财务人员手工转出数额</th>
                <th width="60">更新时间</th>
                <th width="60">创建时间</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${dataList}" var="item" varStatus="num">
                <tr target="sid_user" rel="${item.fId}">
                    <td>${item.fId}</td>
                    <td>${item.fShortName}</td>
                    <td>${item.fStatisticsTime}</td>
                    <td>${item.fBeforeBookBalance}</td>
                    <td>${item.fBeforeWalletBalance}</td>
                    <td>${item.fBeforeDifference}</td>
                    <td><fmt:formatNumber value="${item.fCurrentBookBalance}" pattern="##.######" maxIntegerDigits="15" maxFractionDigits="4"/></td>
                    <td><fmt:formatNumber value="${item.fCurrentWalletBalance}" pattern="##.######" maxIntegerDigits="15" maxFractionDigits="4"/></td>
                    <td><fmt:formatNumber value="${item.fCurrentDifference}" pattern="##.######" maxIntegerDigits="15" maxFractionDigits="4"/></td>
                    <td>${item.fBookRecharge}</td>
                    <td>${item.fWalletRecharge}</td>
                    <td>${item.fBookWithdrawal}</td>
                    <td>${item.fBookWithdrawaling}</td>
                    <td>${item.fWalletWithdrawal}</td>
                    <td>${item.fBuyOrderSum}</td>
                    <td>${item.fSellOrderSum}</td>
                    <td>${item.fManualAdjustment}</td>
                    <td>${item.fHandIn}</td>
                    <td>${item.fHandOut}</td>
                    <td><fmt:formatDate value="${item.updateDate_}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    <td><fmt:formatDate value="${item.createDate_}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
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
<script type="text/javascript">
    $("#doAutoCheckBalance").click(function() {
        var startDate = $("#startDate").val();
        var endDate = $("#endDate").val();
        this.href = "ssadmin/doAutoCheckBalance.html?startDate="+ startDate + "&endDate=" + endDate;
    })
    
</script>
