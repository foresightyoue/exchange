<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post"
    action="ssadmin/goTransferRecord.html">
    <input type="hidden" name="status" value="${param.status}"> <input
        type="hidden" name="keywords" value="${keywords}" /><input
        type="hidden" name="ftype" value="${ftype}" /><input type="hidden"
        name="pageNum" value="${currentPage}" /> <input type="hidden"
        name="numPerPage" value="${numPerPage}" /> <input type="hidden"
        name="orderField" value="${param.orderField}" /><input type="hidden"
        name="orderDirection" value="${param.orderDirection}" />
</form>
<div class="pageContent">
    <div class="panelBar">
        <ul class="toolBar">
            <li class="line">line</li>
            <li><a class="icon" href="ssadmin/viewtransferListExport.html"
                target="dwzExport" targetType="navTab" title="实要导出这些记录吗?"><span>导出</span>
            </a></li>
        </ul>
    </div>
    <table class="table" width="150%" layoutH="138">
        <thead>
            <tr>
                <th width="20" orderField="fid">会员UID</th>
                <th width="60" orderField="fuser.floginName"
                    <c:if test='${param.orderField == "ftransfer.floginName" }'> class="${param.orderDirection}"  </c:if>>登录名</th>
                <th width="60" orderField="fuser.fnickName"
                    <c:if test='${param.orderField == "ftransfer.fnickName" }'> class="${param.orderDirection}"  </c:if>>会员昵称</th>
                <th width="60" orderField="fuser.frealName"
                    <c:if test='${param.orderField == "ftransfer.frealName" }'> class="${param.orderDirection}"  </c:if>>会员真实姓名</th>
                <th width="60" orderField="fvirtualcointype.fname"
                    <c:if test='${param.orderField == "ftransfer.fname" }'> class="${param.orderDirection}"  </c:if>>虚拟币类型</th>
                <th width="60" orderField="fStatus"
                    <c:if test='${param.orderField == "ftransfer.foutput_code" }'> class="${param.orderDirection}"  </c:if>>状态</th>
                <th width="60" orderField="fStatus"
                    <c:if test='${param.orderField == "ftransfer.foutput_msg" }'> class="${param.orderDirection}"  </c:if>>提示信息</th>
                <th width="20">交易类型</th>
                <th width="60" orderField="fAmount"
                    <c:if test='${param.orderField == "ftransfer.fAmount" }'> class="${param.orderDirection}"  </c:if>>数量</th>
                <th width="60">划转账号</th>
                <th width="60" orderField="fCreateTime"
                    <c:if test='${param.orderField == "ftransfer.fCreateTime" }'> class="${param.orderDirection}"  </c:if>>创建时间</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${ftransferList}" var="ftransfer" varStatus="num">
                <tr target="sid_user" rel="${ftransfer.fId}">
                    <td>${ftransfer.uid}</td>
                    <td>${ftransfer.floginName}</td>
                    <td>${ftransfer.fnickName}</td>
                    <td>${ftransfer.frealName}</td>
                    <td>${ftransfer.finput_moneytype}</td>
                    <td>${ftransfer.foutput_code}</td>
                    <td>${ftransfer.foutput_msg}</td>
                    <td>虚拟币划转</td>
                    <td><fmt:formatNumber value="${ftransfer.finput_coins}"
                            pattern="##.######" maxIntegerDigits="15" maxFractionDigits="4" /></td>
                    <td>${ftransfer.finput_username}</td>
                    <td>${ftransfer.fcreatetime}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>
