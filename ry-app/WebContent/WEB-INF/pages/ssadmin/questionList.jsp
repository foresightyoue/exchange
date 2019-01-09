<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/questionList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /> <input
		type="hidden" name="pageNum" value="${currentPage}" /> <input
		type="hidden" name="numPerPage" value="${numPerPage}" /> <input
		type="hidden" name="orderField" value="${param.orderField}" /><input
		type="hidden" name="orderDirection" value="${param.orderDirection}" /><input
		type="hidden" name="ftype" value="${ftype}"/><input
		type="hidden" name="timeSor" value="${timeSor}"/><input
		type="hidden" name="fstatus" value="${fstatus}"/>
		
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/questionList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>提问人手机号码：<input type="text" name="keywords" value="${keywords}"
						size="60" />
					</td>
					<td>时间排序：<select name="timeSor" id="timeSor" onchange="saveSelectIndex()">
						<option value="" selected="selected">默认</option>
						<option value="asc">升序</option>
						<option value="desc">降序</option>
					</select>
					</td>
					<td>提问类型：<select name="ftype" id="ftype" onchange="saveSelectIndex()">
						<option value="" selected="selected">全部</option>
						<option value="1">充值问题</option>
						<option value="2">提现问题</option>
						<option value="3">业务问题</option>
						<option value="4">绑定问题</option>
						<option value="5">其他问题</option>
					</select>
					</td>
					<td>状态：
                        <select name="fstatus" id="fstatus" onchange="saveSelectIndex()">
	                        <option value="" selected="selected">全部</option>
	                        <option value="2">解决</option>
	                        <option value="1">未解决</option>
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
			<%-- <shiro:hasPermission name="ssadmin/viewQuestion.html">
				<li><a class="edit"
					href="ssadmin/goQuestionJSP.html?url=ssadmin/viewQuestion1&uid={sid_user}"
					height="300" width="800" target="dialog" rel="updateLink"><span>查看</span>
				</a>
				</li>
			</shiro:hasPermission> --%>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="30">序号</th>
				<th width="60">提问人UID</th>
				<th width="60">提问人</th>
				<th width="60">提问手机号码</th>
				<th width="60" orderField="fstatus"
					<c:if test='${param.orderField == "fstatus"}'> class="${param.orderDirection}"  </c:if>>状态</th>
				<th width="60" orderField="ftype"
					<c:if test='${param.orderField == "ftype"}'> class="${param.orderDirection}"  </c:if>>提问类型</th>
				<th width="100">提问内容</th>
				<th width="100">回复内容</th>
				<th width="60">回复管理员</th>
				<th width="90" orderField="fcreateTime"
					<c:if test='${param.orderField == "fcreateTime"}'> class="${param.orderDirection}"  </c:if>>创建时间
				</th>
				<th width="60" orderField="fSolveTime"
					<c:if test='${param.orderField == "fSolveTime"}'> class="${param.orderDirection}"  </c:if>>回复时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${questionList}" var="question" varStatus="num">
				<tr target="sid_user" rel="${question.fid}">
					<td>${num.index +1}</td>
					<td>${question.fuser.fid}</td>
					<td>${question.fuser.frealName}</td>
					<td>${question.fuser.ftelephone}</td>
					<td>${question.fstatus_s}</td>
					<td>${question.ftype_s}</td>
					<td>${question.fdesc}</td>
					<td>${question.fanswer}</td>
					<td>${question.fname}</td>
					<td><fmt:formatDate value="${question.fcreateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td><fmt:formatDate value="${question.fSolveTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
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
	function saveSelectIndex() {
		var ftype = document.getElementById("ftype");
		var ftypeText = ftype.options[ftype.selectedIndex].value;
		var timeSor = document.getElementById("timeSor");
		var timeSorText = timeSor.options[timeSor.selectedIndex].value;
		var fstatus = document.getElementById("fstatus");
		var fstatusText = fstatus.options[fstatus.selectedIndex].value;
		//设置多个cookie 
		document.cookie = "ftypeText=" + ftypeText;
		document.cookie = "timeSorText=" + timeSorText;
		document.cookie = "fstatusText=" + fstatusText;
	}
	$(function() {
		//记得初始化，否则会出现undefined
		var ftypeText = 0;
		var timeSorText = 0;
		var fstatusText = 0;
		//获取多个cookie
		var coosStr = document.cookie;//注意此处分隔符是分号加空格
		var coos = coosStr.split("; ");
		for (var i = 0; i < coos.length; i++) {
			var coo = coos[i].split("=");
			if ("ftypeText" == coo[0]) {
				ftypeText = coo[1];
			}
			if ("timeSorText" == coo[0]) {
				timeSorText = coo[1];
			}
		    if ("fstatusText" == coo[0]) {
		        fstatusText = coo[1];
            }
		}
		reSet(timeSorText, "timeSor");
		reSet(ftypeText, "ftype");
		reSet(fstatusText, "fstatus");
	});
	function reSet(text, id) {
		var idName = document.getElementById(id);
		if (text == 0) {
			idName.selectedIndex = 0;
		} else {
			var length = idName.options.length;
			for (var i = 0; i < length; i++) {
				if (idName.options[i].value == text) {
					idName.selectedIndex = i;
					break;
				}
			}
		}
	}
</script>
