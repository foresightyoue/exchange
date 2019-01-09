<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
 <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="../comm/include.inc.jsp"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>

<!doctype html>
<html>
<head> 
<base href="${basePath}"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@include file="../comm/link.inc.jsp" %>

<link rel="stylesheet" href="${oss_url}/static/front/css/question/question.css" type="text/css"></link>
<style>
.yonghu .panel-heading{
    padding: 0;
    background-color: white;
    border-bottom: 0;
}
.yonghu .panel-heading .panel-title{
    font-size: 12px;
}
.yonghu .question_content{
    font-size: 14px;
    padding: 10px 15px;
    display: inline-block;
    margin: 10px 20px 20px 20px;
    border-radius: 4px;
    -webkit-border-radius: 4px;
    background-color: rgba(0,0,0,.2);
}
.kefu .panel-heading{
    padding: 0;
    background-color: white;
    border-bottom: 0;
    text-align: right;
}
.kefu{
    text-align: right;
}
.kefu .answer_content{
    font-size: 14px;
    padding: 10px 15px;
    display: inline-block;
    margin: 10px 0 20px 20px;
    border-radius: 4px;
    -webkit-border-radius: 4px;
    background-color: #c83935;
    text-align: right;
    color: white;
}
.modal-body1{
    padding-left: 20px;
}
.modal-content{
    height: 500px;
}
.modal-body{
    height:  280px;
    overflow:  auto;
    border-bottom: 1px solid #e6e6e6;
}
.content .ll-content{
    width: 100%;
    border: 0;
    height: 80px;
    padding: 15px;
}    
.publish{
    background-color: #c83935;
    border: 0;
    padding: 5px 20px;
    border-radius: 3px;
    -webkit-border-radius: 3px;
    color: white;
}
.text-over{
    overflow: hidden;
    text-overflow:ellipsis;
    white-space: nowrap;
    width: 76px;
    display: inline-block;
}
#view-questiondetail .panel{
    border: 0;
}
#view-questiondetail .modal-body{
    height: 600px;
    overflow: auto;
    border-bottom: 0;
}
</style>

</head>
<body>
	

<%@include file="../comm/header.jsp" %>


	<div class="container main-con">
		<div class="row">
			<!-- 左侧 -->
			<div class="col-xs-2 padding-left-clear">
				<div class="text-center question-left">
					<div class="question-online">
						<p id="question-pic">
							<span>
								意见反馈
							</span>
						</p>
					</div>
					<div class="question-menu"></div>
					<div class="question-img"></div>
					<div class="question-menu"></div>
				</div>
			</div>
			<!-- 右侧 -->
			<div class="col-xs-10 question-right rightarea ">
				<ul class="nav nav-tabs rightarea-tabs">
					<li class="">
						<a href="${oss_url}/question/question.html">
							问答
						</a>
					</li>
					<li class="active">
						<a href="${oss_url}/question/questionlist.html">
							列表
						</a>
					</li>
				</ul>
				<div class="col-xs-12 padding-clear">
					<table class="table table-striped">
						<tr>
							<td class="col-xs-1">
								问题编号
							</td>
							<td class="col-xs-2">
								提交时间
							</td>
							<td class="col-xs-2">
								问题类型
							</td>
							<td class="col-xs-2">
							          问题叙述
							</td>
							<td class="col-xs-2">
								问题描述
							</td>							
							<td class="col-xs-1">
								问题状态
							</td>
							<td class="col-xs-1">
							    是否有未读信息
							</td>
							<td class="col-xs-1">
								操作
							</td>
						</tr>
						
						<c:forEach items="${list }" var="v">
						<tr>
									<td>${v.fid }</td>
									<td>
										<fmt:formatDate value="${v.fcreateTime }" type="both" pattern="yyyy-MM-dd HH:mm:ss" />
									</td>
									<td>${v.ftype_s }</td>
									<td class="opa-link">
									    <a class="view2 opa-link" href="javascript:void(0)" data-question="{&quot;questionid&quot;:${v.fqid}}">追问</a>
									</td>
									<td class="text-over">${v.fdesc }</td>											
									<td>${v.fstatus_s }</td>
									<c:if test="${v.isallsys == '1' }">
									    <td style="color:red;">有未读信息</td>
									</c:if>
									<c:if test="${v.isallsys == '0' }">
									     <td>暂无未读信息</td>
									</c:if>
									<td class="opa-link">
										<a class="delete opa-link" href="javascript:void(0)" data-question="{&quot;questionid&quot;:${v.fqid }}">
											删除
										</a>
		
							|<a class="view opa-link" href="javascript:void(0)" data-question="{&quot;questionid&quot;:${v.fid }}">查看</a>
									</td>
								</tr>
						</c:forEach>
						
						 <c:if test="${fn:length(list)==0 }">
							<tr class="no-data-tips text-center">
								<td colspan="6">
									<span>
										您暂时没有提问记录
									</span>
								</td>
							</tr>
						 </c:if>
							
							<tr>
								<td class="text-right" colspan="12">
									<div class="text-right">
										${pagin }
									</div>
								</td>
							</tr>
						
					</table>
				</div>
			</div>
		</div>
	</div>
	<c:forEach items="${list }" var="v">
        <div class="modal modal-custom fade" id="view-questiondetail" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-mark"></div>
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close btn-modal" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                        <span class="modal-title">问题详情</span>
                    </div>
                    <div class="modal-body form-horizontal" style="height: 390px;">
                                                                  
                    </div>
                </div>
            </div>
        </div>
    </c:forEach>
	    <div class="modal modal-custom fade" id="questiondetail" tabindex="-1" role="dialog">
                                    <div class="modal-dialog" role="document">
                                        <div class="modal-mark"></div>
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <button type="button" class="close btn-modal" data-dismiss="modal" aria-label="Close">
                                                    <span aria-hidden="true">&times;</span>
                                                </button>
                                                <span class="modal-title">提问</span><span class="modal-title1">回复</span>
                                            </div>
                                            <div class="modal-body form-horizontal">
                                                                                           
                                            </div>
                                            <div class="modal-body1 form-horizontal">
                                              <!-- <span style="float: left;">回复:</span> -->
                                              <div class="content">
                                                  <textarea rows="3" cols="20" class="ll-content"></textarea>
                                              <div>
                                              <div style="text-align: right;padding-right: 20px;">
                                                  <button class="publish">发表</button>
                                              </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                </div>
	</div>


<%@include file="../comm/footer.jsp" %>	

	<script type="text/javascript" src="${oss_url}/static/front/js/question/question.js"></script>
	<script type="text/javascript" src="${oss_url}/static/front/js/comm/util.js"></script>
	<script type="text/javascript" src="${oss_url}/static/front/js/language/language_cn.js"></script>
	
</body>
</html>