<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/transferRecord.html">
    <input type="hidden" name="ftype" value="${ftype}" />
    <input type="hidden" name="pageNum" value="${currentPage}" /> 
    <input type="hidden" name="numPerPage" value="${numPerPage}" />
</form>


<div class="pageHeader">
    <form onsubmit="return navTabSearch(this);" action="ssadmin/transformRecord.html" method="post">
        <div class="searchBar">
            <table class="searchContent">
                <tr>
                    <td>开始时间： <input type="text" name="startDate" class="date"
                        readonly="true" value="${startDate }" />
                    </td> 
                    <td>结束时间： <input type="text" name="endDate" class="date"
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
        </div>
    </form>
</div>
<div class="pageContent">
    <div class="panelBar">
        <ul class="toolBar">
            <shiro:hasPermission name="ssadmin/addVirtualOperationLog">
                <li><a class="add"
                    href="ssadmin/goVirtualOperationLogJSP.html?url=ssadmin/addTransferRecord"
                    height="280" width="800" target="dialog" rel="addTransferRecord"><span>新增</span>
                </a></li>
            </shiro:hasPermission>
        </ul>
</div>
               
    <table class="table" width="100%" layoutH="138">
        <thead>
            <tr>
                <th width="30">编号</th>
                <th width="30">币种</th>
                <th width="60">转账数量</th>
                <th width="60">转账类型</th>
                <th width="60">转账管理员帐号</th>
                <th width="60">创建时间</th>
                <th width="60">修改时间</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${dataList}" var="item" varStatus="num">
                <tr target="sid_user" rel="${item.fId}">
                    <td>${item.fId}</td>
                    <td>${item.fShortName}</td>
                    <td>${item.fCount}</td>
                    <td>${item.fType eq 1 ? '转入':'转出'}</td>
                    <td>${item.fCreateUser}</td>
                    <td><fmt:formatDate value="${item.fCreateTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                    <td><fmt:formatDate value="${item.fUpdateTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
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
