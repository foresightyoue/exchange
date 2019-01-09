<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<base href="${basePath}"/>
<form id="pagerForm" method="post" action="ssadmin/userList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /><input
		type="hidden" name="uid" value="${uid}" /><input
		type="hidden" name="startDate" value="${startDate}" /> <input
		type="hidden" name="troUid" value="${troUid}" /> <input
		type="hidden" name="troNo" value="${troNo}" /> <input type="hidden"
		name="ftype" value="${ftype}" /> <input type="hidden" name="pageNum"
		value="${currentPage}" /> <input type="hidden" name="numPerPage"
		value="${numPerPage}" /> <input type="hidden" name="orderField"
		value="${param.orderField}" /> <input type="hidden"
		name="orderDirection" value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/userList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>会员信息：<input type="text" name="keywords"
						value="${keywords}" size="40" />
					</td>
					<td>推荐人UID：<input type="text" name="troUid" value="${troUid}"
						size="10" />
					</td>
					<td>会员等级:<input type="text" name="flevel" value="${flevel}"
					   size="10"/>
					</td>
					<td>会员状态： <select type="combox" name="ftype">
							<c:forEach items="${typeMap}" var="type">
								<c:if test="${type.key == ftype}">
									<option value="${type.key}" selected="true">${type.value}</option>
								</c:if>
								<c:if test="${type.key != ftype}">
									<option value="${type.key}">${type.value}</option>
								</c:if>
							</c:forEach>
					</select></td>
					<td>身份证照片认证：
					   <select name="fhasImgValidate" id="fhasImgValidate">
                            <option value="">全部</option>
                            <option value="1" ${fhasImgValidate == "1" ? 'selected':''}>是</option>
                            <option value="0" ${fhasImgValidate == "0" ? 'selected':''}>否</option>
					   </select>
					</td>
					<td>是否为商户：
                       <select name="fIsMerchant" id="fIsMerchant">
                            <option value="">全部</option>
                             <option value="2" ${fIsMerchant == "2" ? 'selected':''}>超级商户</option>
                            <option value="1" ${fIsMerchant == "1" ? 'selected':''}>普通商户</option>
                            <option value="0" ${fIsMerchant == "0" ? 'selected':''}>否</option>
                            <option value="-1" ${fIsMerchant == "-1" ? 'selected':''}>申请普通商户</option>
                            <option value="-2" ${fIsMerchant == "-2" ? 'selected':''}>申请超级商户</option>
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
						</div>
					</li>
				</ul>
			</div>
		</div>
	</form>
</div>
<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<shiro:hasPermission name="ssadmin/userForbbin1.html">
				<li><a class="delete"
					href="ssadmin/userForbbin.html?uid={sid_user}&status=1&rel=listUser"
					target="ajaxTodo" title="确定要禁用吗?"><span>禁用</span> </a>
				</li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/userForbbin2.html">
				<li><a class="edit"
					href="ssadmin/userForbbin.html?uid={sid_user}&status=2&rel=listUser"
					target="ajaxTodo" title="确定要解除禁用吗?"><span>解除禁用</span> </a>
				</li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/userForbbin3.html">
				<li><a class="edit"
					href="ssadmin/userForbbin.html?uid={sid_user}&status=3&rel=listUser"
					target="ajaxTodo" title="确定要重设登录密码吗?"><span>重设登录密码</span> </a>
				</li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/userForbbin4.html">
				<li><a class="edit"
					href="ssadmin/userForbbin.html?uid={sid_user}&status=4&rel=listUser"
					target="ajaxTodo" title="确定要重设交易密码吗?"><span>重设交易密码</span> </a>
				</li>
		   </shiro:hasPermission>
		   <shiro:hasPermission name="ssadmin/userForbbin5.html">
				<li><a class="edit"
					href="ssadmin/cancelGoogleCode.html?uid={sid_user}"
					target="ajaxTodo" title="确定要重设GOOGLE验证吗?"><span>重置GOOGLE</span>
				</a>
				</li>
		   </shiro:hasPermission>
		   <shiro:hasPermission name="ssadmin/userForbbin6.html">
				<li><a class="edit"
					href="ssadmin/cancelTel.html?uid={sid_user}"
					target="ajaxTodo" title="确定要重置手机号码吗?"><span>重置手机号码</span>
				</a>
				</li>
				<li><a class="edit"
					href="ssadmin/getUserInfo.html?uid={sid_user}&amp;url=ssadmin/setMerchant"
					target="dialog" rel="" ><span>商户设置</span>
				</a>
				</li>
				<!-- <li><a class="edit"
                    href="ssadmin/getUserInfo.html?uid={sid_user}&amp;url=ssadmin/setLeader"
                    target="dialog" rel="" ><span>团队长设置</span>
                </a>
				</li>
                <li><a class="edit"
                    href="ssadmin/getUserInfo.html?uid={sid_user}&amp;url=ssadmin/setSystemPro"
                    target="dialog" rel="" ><span>分配登录团队系统权限</span>
                </a>
                </li> -->
				<li>
                    <a class="edit" href="ssadmin/getUserInfo.html?uid={sid_user}&amp;url=ssadmin/auditUserTelPhone" 
                    target="dialog" rel="auditUserTelPhone" height="300" width="600"><span>修改绑定手机号</span></a>
				</li>
		</shiro:hasPermission>
		<shiro:hasPermission name="ssadmin/userForbbin6.html">
		        <li><a class="adminDetail edit" 
		        href="ssadmin/goAdminDetailJSP.html?url=ssadmin/adminDetail&uid={sid_user}" target="navTab" title="会员信息" rel="adminDetail"><span>详情</span>
		        </a></li>
		</shiro:hasPermission>
		<shiro:hasPermission name="ssadmin/userForbbin6.html">
		       <li><a class="edit"
                    href="ssadmin/setTiger.html?uid={sid_user}"
                    target="ajaxTodo" title="确定要设置操盘手吗?"><span>设置操盘手</span>
                </a>
                </li> 
		</shiro:hasPermission>
		<shiro:hasPermission name="ssadmin/userForbbin7.html">
			<li><a class="edit"
					href="ssadmin/goUserJSP.html?url=ssadmin/updateUserGrade&uid={sid_user}"
					height="250" width="700" target="dialog" rel="updateUserGrade"><span>修改等级</span>
				</a></li>
            <!-- <li><a class="edit"
					href="ssadmin/setTiger.html?uid={sid_user}"
					target="ajaxTodo" title="确定要设置操盘手吗?"><span>设置操盘手</span>
				</a>
				</li> -->
			<li><a class="edit"
				href="ssadmin/goUserJSP.html?uid={sid_user}&url=ssadmin/updateIntroPerson"
				height="240" width="800" target="dialog" rel="updateIntroPerson"><span>修改推荐人</span>
			</a></li>		
	  </shiro:hasPermission>	
	  <shiro:hasPermission name="ssadmin/userForbbin6.html">
			   <li> 
				 <a class="edit" href="ssadmin/getUserInfo.html?uid={sid_user}&amp;url=ssadmin/auditUserAuthGrade" 
                    target="dialog" rel="auditUserAuthGrade" height="260" width="600"><span>修改用户认证级别</span>
                 </a></li>
	  </shiro:hasPermission>		
				<li class="line">line</li>
	<shiro:hasPermission name="ssadmin/userExport.html">
				<li><a class="icon" href="ssadmin/userExport.html"
					target="dwzExport" targetType="navTab" title="确实要导出这些记录吗?"><span>导出</span>
				</a></li>
	</shiro:hasPermission>		
		</ul>
		<ul></ul>
	</div>
	<table class="table" width="150%" layoutH="138">
		<thead>
			<tr>
				<th width="40" orderField="fid"
					<c:if test='${param.orderField == "fid" }'> class="${param.orderDirection}"  </c:if>>会员UID</th>
				<th width="60">注册类型</th>
                <th width="40" orderField="fIntroUser_id.fid"
					<c:if test='${param.orderField == "fIntroUser_id.fid" }'> class="${param.orderDirection}"  </c:if>>推荐人UID</th>
				<th width="60" orderField="floginName"
					<c:if test='${param.orderField == "floginName" }'> class="${param.orderDirection}"  </c:if>>登录名</th>				
				<th width="60">昵称</th>
				<th width="60">真实姓名</th>
				<th width="60">邮箱地址</th>
				<th width="60" orderField="fstatus"
					<c:if test='${param.orderField == "fstatus" }'> class="${param.orderDirection}"  </c:if>>会员状态</th>
				<th width="60" orderField="fscore.flevel"
					<c:if test='${param.orderField == "fscore.flevel" }'> class="${param.orderDirection}"  </c:if>>会员等级</th>
				<th width="60">证件类型</th>
				<th width="60">证件号码</th>
				<th width="60">手机号码</th>
				<th width="60">身份证照片认证</th>
				<%--<th width="60">视频认证</th>--%>
				<%--<th width="60">是否操盘手</th>--%>
				<th width="60">是否是商户</th>
				<th width="60" orderField="fregisterTime"
					<c:if test='${param.orderField == "fregisterTime" }'> class="${param.orderDirection}"  </c:if>>注册时间</th>
			    <th width="60">注册IP</th>
			    <th width="60" orderField="flastLoginTime"
					<c:if test='${param.orderField == "flastLoginTime" }'> class="${param.orderDirection}"  </c:if>>上次登录时间</th>
			    <th width="60">上次登录IP</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${userList}" var="user" varStatus="num">
				<tr target="sid_user" rel="${user.fid}">
					<td>${user.fid}</td>
					<td>${user.fregtype_s}</td>
					<td>${user.fIntroUser_id.fid}</td>
					<td>${user.floginName}</td>
					<td>${user.fnickName}</td>
					<td>${user.frealName == 'admin'?'':user.frealName }</td>
					<td>${user.femail}</td>
					<td>${user.fstatus_s}</td>
					<td>${user.fscore.flevel}</td>
					<td>${user.fidentityType_s}</td>
					<td>${user.fidentityNo == '500233199301011414'?'':user.fidentityNo}</td>
					<td>${user.ftelephone}</td>
					<td>
						<c:if test="${user.fhasImgValidate eq true}">
					   	                      是
						</c:if>
						<c:if test="${user.fhasImgValidate eq false}">
						       	否
						</c:if>
					</td>
					<%--<td>
						<c:if test="${user.fhasVideoValidate eq true}">
					 	                      是
						</c:if>
						<c:if test="${user.fhasVideoValidate eq false}">
						       	否
						</c:if>
					</td>
					<td>
						<c:if test="${user.fistiger eq true}">
					 	                      是
						</c:if>
						<c:if test="${user.fistiger eq false}">
						       	否
						</c:if>
					</td>--%>
					<td>
					   <c:if test="${user.fIsMerchant eq 0}">
					                   非商户
					   </c:if>
					   <c:if test="${user.fIsMerchant eq 1}">
					                   普通商户
					   </c:if>
					   <c:if test="${user.fIsMerchant eq 2}">
					                   超级商户
					   </c:if>
                       <c:if test="${user.fIsMerchant eq -1}">
                                       申请普通商户
                       </c:if>
                       <c:if test="${user.fIsMerchant eq -2}">
                                       申请超级商户
                       </c:if>
					</td>
					<td><fmt:formatDate value="${user.fregisterTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td>${user.fregIp}</td>
					<td><fmt:formatDate value="${user.flastLoginTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td>${user.flastLoginIp}</td>
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
    $(function(){
        $("#fisleader").val("${fisleader}");
        $("#fIsMerchant").val("${fIsMerchant}");
    })    
    </script>
