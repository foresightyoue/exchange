<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = "http" + "://"
			+ request.getServerName() + ":" + request.getLocalPort()
			+ path;
	  session.setAttribute("basePath", basePath);
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
	Date date = new Date();
	Calendar c = Calendar.getInstance();
	c.setTime(date);
	c.set(Calendar.DAY_OF_MONTH, 1);
	String startDate = sdf.format(c.getTime());
	String endDate = sdf1.format(c.getTime()) + "-"
			+ c.getMaximum(Calendar.DAY_OF_MONTH);
%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<base href="${basePath}" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${requestScope.constant['webinfo'].ftitle }</title>

<link href="${oss_url}/static/ssadmin/js/themes/default/style.css" rel="stylesheet"
	type="text/css" media="screen" />
<link href="${oss_url}/static/ssadmin/js/themes/css/core.css" rel="stylesheet"
	type="text/css" media="screen" />
<link href="${oss_url}/static/ssadmin/js/themes/css/print.css" rel="stylesheet"
	type="text/css" media="print" />
<link href="${oss_url}/static/ssadmin/js/uploadify/css/uploadify.css"
	rel="stylesheet" type="text/css" media="screen" />
<link href="${oss_url}/static/ssadmin/js/ztree/css/zTreeStyle.css" rel="stylesheet"
	type="text/css" media="screen" />
<link href="${oss_url}/static/ssadmin/js/treeTable/themes/default/treeTable.css"
	rel="stylesheet" type="text/css" />
<link href="${oss_url}/static/ssadmin/js/ztree/css/keta.css" rel="stylesheet"
	type="text/css" />



<!--[if IE]>
<link href="${oss_url}/static/ssadmin/js/themes/css/ieHack.css" rel="stylesheet" type="text/css" media="screen"/>
<![endif]-->

<!--[if lte IE 9]>
<script src="${oss_url}/static/ssadmin/js/js/speedup.js" type="text/javascript"></script>
<![endif]-->

<script src="${oss_url}/static/ssadmin/js/js/jquery-1.7.2.js"
	type="text/javascript"></script>
<script src="${oss_url}/static/ssadmin/js/js/jquery.cookie.js"
	type="text/javascript"></script>
<script src="${oss_url}/static/ssadmin/js/js/jquery.validate.js"
	type="text/javascript"></script>
<script src="${oss_url}/static/ssadmin/js/js/jquery.bgiframe.js"
	type="text/javascript"></script>
<script src="${oss_url}/static/ssadmin/js/xheditor/xheditor-1.2.1.min.js"
	type="text/javascript"></script>
<script src="${oss_url}/static/ssadmin/js/xheditor/xheditor_lang/zh-cn.js"
	type="text/javascript"></script>
<script src="${oss_url}/static/ssadmin/js/uploadify/scripts/jquery.uploadify.js"
	type="text/javascript"></script>

<!-- svg图表  supports Firefox 3.0+, Safari 3.0+, Chrome 5.0+, Opera 9.5+ and Internet Explorer 6.0+ -->
<script type="text/javascript" src="${oss_url}/static/ssadmin/js/chart/raphael.js"></script>
<script type="text/javascript"
	src="${oss_url}/static/ssadmin/js/chart/g.raphael.js"></script>
<script type="text/javascript" src="${oss_url}/static/ssadmin/js/chart/g.bar.js"></script>
<script type="text/javascript" src="${oss_url}/static/ssadmin/js/chart/g.line.js"></script>
<script type="text/javascript" src="${oss_url}/static/ssadmin/js/chart/g.pie.js"></script>
<script type="text/javascript" src="${oss_url}/static/ssadmin/js/chart/g.dot.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/plugin/layer/layer.js"></script>

<script src="${oss_url}/static/ssadmin/js/js/dwz.core.js" type="text/javascript"></script>
<script src="${oss_url}/static/ssadmin/js/js/dwz.util.date.js"
	type="text/javascript"></script>
<script src="${oss_url}/static/ssadmin/js/js/dwz.validate.method.js"
	type="text/javascript"></script>
<script src="${oss_url}/static/ssadmin/js/js/dwz.barDrag.js" type="text/javascript"></script>
<script src="${oss_url}/static/ssadmin/js/js/dwz.drag.js" type="text/javascript"></script>
<script src="${oss_url}/static/ssadmin/js/js/dwz.tree.js" type="text/javascript"></script>
<script src="${oss_url}/static/ssadmin/js/js/dwz.accordion.js"
	type="text/javascript"></script>
<script src="${oss_url}/static/ssadmin/js/js/dwz.ui.js" type="text/javascript"></script>
<script src="${oss_url}/static/ssadmin/js/js/dwz.theme.js" type="text/javascript"></script>
<script src="${oss_url}/static/ssadmin/js/js/dwz.switchEnv.js"
	type="text/javascript"></script>
<script src="${oss_url}/static/ssadmin/js/js/dwz.alertMsg.js"
	type="text/javascript"></script>
<script src="${oss_url}/static/ssadmin/js/js/dwz.contextmenu.js"
	type="text/javascript"></script>
<script src="${oss_url}/static/ssadmin/js/js/dwz.navTab.js" type="text/javascript"></script>
<script src="${oss_url}/static/ssadmin/js/js/dwz.tab.js" type="text/javascript"></script>
<script src="${oss_url}/static/ssadmin/js/js/dwz.resize.js" type="text/javascript"></script>
<script src="${oss_url}/static/ssadmin/js/js/dwz.dialog.js" type="text/javascript"></script>
<script src="${oss_url}/static/ssadmin/js/js/dwz.dialogDrag.js"
	type="text/javascript"></script>
<script src="${oss_url}/static/ssadmin/js/js/dwz.sortDrag.js"
	type="text/javascript"></script>
<script src="${oss_url}/static/ssadmin/js/js/dwz.cssTable.js"
	type="text/javascript"></script>
<script src="${oss_url}/static/ssadmin/js/js/dwz.stable.js" type="text/javascript"></script>
<script src="${oss_url}/static/ssadmin/js/js/dwz.taskBar.js" type="text/javascript"></script>
<script src="${oss_url}/static/ssadmin/js/js/dwz.ajax.js" type="text/javascript"></script>
<script src="${oss_url}/static/ssadmin/js/js/dwz.pagination.js"
	type="text/javascript"></script>
<script src="${oss_url}/static/ssadmin/js/js/dwz.database.js"
	type="text/javascript"></script>
<script src="${oss_url}/static/ssadmin/js/js/dwz.datepicker.js"
	type="text/javascript"></script>
<script src="${oss_url}/static/ssadmin/js/js/dwz.effects.js" type="text/javascript"></script>
<script src="${oss_url}/static/ssadmin/js/js/dwz.panel.js" type="text/javascript"></script>
<script src="${oss_url}/static/ssadmin/js/js/dwz.checkbox.js"
	type="text/javascript"></script>
<script src="${oss_url}/static/ssadmin/js/js/dwz.history.js" type="text/javascript"></script>
<script src="${oss_url}/static/ssadmin/js/js/dwz.combox.js" type="text/javascript"></script>
<script src="${oss_url}/static/ssadmin/js/js/dwz.print.js" type="text/javascript"></script>

<%-- zTree --%>
<script src="${oss_url}/static/ssadmin/js/ztree/js/jquery.ztree.all-3.5.min.js"
	type="text/javascript"></script>
<script src="${oss_url}/static/ssadmin/js/ztree/js/keta.js" type="text/javascript"></script>
<script src="${oss_url}/static/ssadmin/js/treeTable/jquery.treeTable.min.js"
	type="text/javascript"></script>

<!-- 可以用dwz.min.js替换前面全部dwz.*.js (注意：替换是下面dwz.regional.zh.js还需要引入)
<script src="bin/dwz.min.js" type="text/javascript"></script>
-->
<script src="${oss_url}/static/ssadmin/js/js/dwz.regional.zh.js"
	type="text/javascript"></script>

<script type="text/javascript">
	$(function() {
		DWZ.init("/static/ssadmin/js/dwz.frag.xml", {
			loginUrl : "login_dialog.html",
			loginTitle : "登录", // 弹出登录对话框
			//		loginUrl:"login.html",	// 跳到登录页面
			statusCode : {
				ok : 200,
				error : 300,
				timeout : 301
			}, //【可选】
			pageInfo : {
				pageNum : "pageNum",
				numPerPage : "numPerPage",
				orderField : "orderField",
				orderDirection : "orderDirection"
			}, //【可选】
			debug : false, // 调试模式 【true|false】
			callback : function() {
				initEnv();
				$("#themeList").theme({
					themeBase : "${oss_url}/static/ssadmin/js/themes"
				}); // themeBase 相对于index页面的主题base路径
			}
		});
	});
</script>
<style>
	.main-content{
		width: 90%;
		height: 100%;
		min-width: 1000px;
		padding: 20px 20px;
		font-family: PingFang-SC-Medium;
		/* border: 1px solid #ccc; */
		box-sizing: border-box;
		-webkit-box-sizing: border-box;
	}
	.hint{
		padding-left: 10px;
		height: 35px;
		line-height: 35px;
		border-bottom: 1px solid #ccc;
		font-size: 14px;
	}
	.clearfloat:after{
		display:block;
		clear:both;
		content:"";
		visibility:hidden;
		height:0
	} 
	.clearfloat{
		zoom:1;
	} 
	.fl{
		float: left;
	}
	.fr{
		float: right;
	}
	.num_money{
		width: 32%;
		height: 90%;
		background-color: #ffffff;
		margin-right: 10px;
		background-image: url("/static/front/app/images/home_bg.png");
		background-position: bottom;
		background-repeat: no-repeat;
		background-size: 100%; 
	}
	.num_money .clearfix{
		width: 100%;
		height: 100%;
		display: inline-block;
		position: relative;
		padding-top: 50px;
		padding-left: 30px;
		box-sizing: border-box;
		-webkit-box-sizing: border-box;
		-moz-box-sizing: border-box;
		-ms-box-sizing: border-box;
		-o-box-sizing: border-box;
	}
	.basic_data .clearfloat li{
		float: left;
		width: 32%;
		height: 43%;
		background-color: #ffffff;
	} 
	.ml_10{
		margin-left: 10px;
	}
	.mr_10{
		margin-right: 10px;
	}
	.mb_10{
		margin-bottom: 10px;
	}
	.mt_10{
		margin-top: 10px;
	}
	.basic_data{
		height: 38%
	}
	.right_list{
		height: 100%;
	}
	.right_list .fl{
	    height: 100%;
	}
	.right_list .fl img{
	   height: 90%;
	   width: auto;
	}
	.basic_data ul li .clearfix{
		width: 100%;
		height: 100%;
		display: inline-block;
		position: relative;
		padding-top: 14px;
		padding-left: 14px;
		box-sizing: border-box;
		-webkit-box-sizing: border-box;
		-moz-box-sizing: border-box;
		-ms-box-sizing: border-box;
		-o-box-sizing: border-box;
	}
	.basic_data img{
		width: 87px;
		height: 76px;
	}
	.num,.num_hint{
		text-align: center;
	}
	.num{
		font-size: 26px;
		color: #000000;
		margin-top: 10px;
		line-height: 40px;
	}
	.num_hint{
		font-size: 12px;
		color: #4d4d4d;
	}
	.bi-message{
		width: 100%;
		height: 62%
	}
	.bi-message_left,.bi-message_right{
		width: 48%;
		height: 100%;
		background-color: white;
		height: 340px;
	}
	.bi_style{
		height: 30px;
		border-bottom: 1px solid #ccc;
	}
	.bi_style li{
		float: left;
		width: 33%;
		text-align: center;
		height: 100%;
		line-height: 30px;
		cursor: pointer;
	}
	.bi_style .active{
		color: red;
	}
	.data_list{
		display: none; 
	}
	.data_list li{
		line-height: 30px;
	}
	.yun_list,.bi_list{
		text-align: center;
	}
	.deal_list{
		width: 100%;
		text-align: center;
	}
	.deal_list th{
		height: 25px;
		font-weight: 600;
		border-bottom: 1px solid #e6e6e6;
		border-right: 1px solid #e6e6e6;
	}
	.deal_list td{
		height: 24px;
		border-bottom: 1px solid #e6e6e6;
		border-right: 1px solid #e6e6e6;
	}
	.deal_list tr:last-child td{
		border-bottom: 0;
	}
	.deal_list tr td:last-child {
		border-right: 0;
	}
	.message_list{
		padding-left: 50px;
		padding-top: 20px;
	}
	.message_list li{
		line-height: 30px;
	}
	.message_list li span:first-child{
		display: inline-block;
		width: 100px;
	}
	.huiyuan{
		width: 30%;
		height: 2em;
		float: left;
		text-align: left;
		padding-top: 20px;
		padding-left: 50px;
	}
	.huiyuan>span{
        line-height: 30px;
    }
</style>

</head>

<body scroll="no">
	<div id="layout">
		<div id="header">
			<div class="headerNav">
				<a class="logo" href="#">标志</a>
				<ul class="nav">
					<shiro:hasPermission name="webBaseInfo">
						<li><a href="ssadmin/webBaseInfoList.html" target="dialog"
							width="600" title="网站信息设置">网站信息设置</a></li>
					</shiro:hasPermission>
					<li><a
						href="ssadmin/goAdminJSP.html?url=ssadmin/updateAdminPassword"
						target="dialog" width="700" title="修改密码">修改密码</a></li>
					<li><a href="ssadmin/loginOut.html">退出</a>
					</li>
				</ul>
				<!-- <ul class="themeList" id="themeList">
					<li theme="azure"><div class="selected">蓝色</div>
					</li>
					<li theme="green"><div>绿色</div>
					</li>
					<li theme="red"><div>红色</div></li>
					<li theme="purple"><div>紫色</div>
					</li>
					<li theme="silver"><div>银色</div>
					</li>
					<li theme="azure"><div>天蓝</div>
					</li>
				</ul> -->
			</div>

			<!-- navMenu -->

		</div>

		<div id="leftside">
			<div id="sidebar_s">
				<div class="collapse">
					<div class="toggleCollapse">
						<div></div>
					</div>
				</div>
			</div>
			<div id="sidebar">
				<div class="toggleCollapse">
					<h2>主菜单</h2>
					<div>收缩</div>
				</div>
             <%@ include file="comm/detail.jsp"%>
			</div>
		</div>

		<div id="container">
			<div id="navTab" class="tabsPage">
				<div class="tabsPageHeader">
					<div class="tabsPageHeaderContent">
						<!-- 显示左右控制时添加 class="tabsPageHeaderMargin" -->
						<ul class="navTab-tab">
							<li tabid="main" class="main"><a href="javascript:;"><span><span
										class="home_icon">我的主页</span> </span> </a>
							</li>
						</ul>
					</div>
					<div class="tabsLeft">left</div>
					<!-- 禁用只需要添加一个样式 class="tabsLeft tabsLeftDisabled" -->
					<div class="tabsRight">right</div>
					<!-- 禁用只需要添加一个样式 class="tabsRight tabsRightDisabled" -->
					<div class="tabsMore">more</div>
				</div>
				<ul class="tabsMoreList">
					<li><a href="javascript:;">我的主页</a>
					</li>
				</ul>

				<div class="navTab-panel tabsPageContent layoutBox">
					<div class="page unitBox" style="background-color: #f5f5f5;">
						<div class="accountInfo">
							<p>
								<span><font color="red">欢迎管理员: <b>${login_admin.fname}</b>
										登录,当前时间:${dateTime}</font> </span>
							</p>
							<%-- <div class="content1">
							   <input type="radio" name="isversion" class="numbu" value="1" ${fisversion == 'true'?'checked':''}/>正式版
                               <input type="radio" name="isversion"  class="gudbu" value="0" ${fisversion == 'false' ?'checked':''}/>测试版
							</div> --%>
						</div>
						<%-- <div style="background-color: #0d2446;height: 100%;">
						<img src="${oss_url}/static/ssadmin/img/welcome.png"style="width: 100%;"></img>
						</div> --%>
						<div class="main-content">
							<div class="basic_data clearfloat">
								<!-- <div class="hint">基本数据</div>	 -->
								<div class="num_money fl">
									<div class="clearfix">
										<div class="fl"><img src="/static/front/app/images/home_money.png" alt="" />  </div>
										<div class="num"><fmt:formatNumber value="${fAmount}" pattern="###.###E0"/></div>
										<div class="num_hint"><span>交易金额</span></div>
									</div>
								</div>
								<ul class="right_list clearfloat">
									<li class="mr_10 mb_10">
										<div href="#" class="clearfix"> 
											<div class="fl"><img src="/static/front/app/images/home_add.png" alt="" />  </div>
											<div class="num">${Rtotal}</div>
											<div class="num_hint"><span>充值</span></div>
										</div>
									</li>
									<li class="mb_10">
										<div href="#" class="clearfix"> 
											<div class="fl"><img src="/static/front/app/images/home_get.png" alt="" />  </div>
											<div class="num">${Wtotal}</div>
											<div class="num_hint"><span>提现</span></div>
										</div>
									</li>
									<li class="mr_10">
										<div href="#" class="clearfix"> 
											<div class="fl"><img src="/static/front/app/images/home_member.png" alt="" />  </div>
											<div class="num">${count}</div>
											<div class="num_hint"><span>会员数量</span></div>
										</div>
									</li>
									<li>
										<div href="#" class="clearfix"> 
											<div class="fl"><img src="/static/front/app/images/home_num.png" alt="" />  </div>
											<div class="num">${fCount }</div>
											<div class="num_hint"><span>交易数量</span> </div>
										</div>
									</li>
								</ul>	
							</div>
							<div class="bi-message clearfloat">
								<div class="bi-message_left fl" style="overflow-y:auto;overflow-x:auto;width:48%; height:280px;">
									<div class="hint">基本数据</div>	
									<ul class="bi_style clearfloat">
										<li class="active">会员(近三天)</li>
										<li>币种</li>
										<li>交易</li>
										<!-- <li>被攻击情况</li> -->
									</ul>
									<table class="deal_list data_list" style="display: table;">
                                        <tr>
                                            <th>会员UID</th>
                                            <th>注册类型</th>
                                            <th>登录名</th>
                                            <th>会员昵称</th>
                                            <th>会员状态</th>
                                        </tr>
                                        <c:forEach var ="item" items="${userList}">
                                            <tr>
                                                <td>${item.items.fId}</td>
                                                <td>${item.items.fregType}</td>
                                                <td>${item.items.floginName}</td>
                                                <td>${item.items.fNickName}</td>
                                                <td>${item.items.fStatus}</td>
                                            </tr>
                                        </c:forEach>
									</table>
                                    <table class="deal_list data_list">
                                        <tr>
                                            <th>币种名</th>
                                            <th>币种代号</th>
                                            <th>充值数量(最近三天)</th>
                                            <th>提现数量(最近三天)</th>
                                        </tr>
                                    <c:forEach var ="item" items="${list}">
                                            <tr>
                                                <td>${item.items.fName}</td>
                                                <td>${item.items.fShortName}</td>
                                                <c:choose>
                                                    <c:when test="${item.items.chongzhi != null}">
                                                        <td>${item.items.chongzhi}</td>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <td>0.00</td>
                                                    </c:otherwise>
                                                </c:choose>
                                                <c:choose>
                                                    <c:when test="${item.items.tixian != null}">
                                                        <td>${item.items.tixian}</td>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <td>0.00</td>
                                                    </c:otherwise>
                                                </c:choose>
                                            </tr>
                                    </c:forEach>
                                    </table>
									<table class="deal_list data_list">
										<tr>
											<th>成交价</th>
											<th>总金额</th>
											<th>成交总金额</th>
											<th>数量</th>
											<th style="border-right: 0">剩余数量</th>
										</tr>
										<c:forEach var="list2" items="${list2}">
											<tr>
												<td>${list2.fprize }</td>
												<td>${list2.fAmount}</td>
												<td>${list2.fsuccessAmount }</td>
												<td>${list2.fCount }</td>
												<td>${list2.fleftCount}</td>
											</tr>
										</c:forEach>
									</table>
									<!-- <ul class="danger_list data_list">
										<li>0</li>
									</ul> -->
								</div>
								<div class="bi-message_right fl" style="margin-left: 20px;">
									<div class="hint">基本信息</div>
									<ul class="message_list">
									    <li>
                                            <span>JAVA 版本：</span>
                                            <span>1.8.0_101</span> 
                                        </li>
										<li>
                                            <span>TomCat 版本：</span>
                                            <span>8.0.38</span> 
                                        </li>
										<li>
											<span>MySQL 版本：</span>
											<span>5.7</span> 
										</li>
										<li>
											<span>产品最新版本：</span>
											<span>1.0.1</span> 
										</li>
										<li>
											<span>当前版本:：</span>
											<span>1.0</span> 
										</li>
										<li>
											<span> 编码:</span>
											<span>103032</span> 
										</li>
									</ul>	
								</div>
							</div>
                        </div>
					</div>
				</div>
			</div>
		</div>
	</div>


	<div id="footer">
		${requestScope.constant['webinfo'].fcopyRights }
	</div>
<script>
	$(function(){
		$(".bi_style").on("click","li",function(){
			$(this).addClass("active").siblings().removeClass("active");
			$(".data_list").hide();
			$(".data_list").eq($(this).index()).show();
		})
		$(".numbu").on('click',function(){
			var isversion = $(".numbu").val();
			var url = "/ssadmin/goVirtualCoinTypeJSP3.html";
			var param = {
					"isversion":isversion
			}
			jQuery.post(url,param,function(data){
		         if(data.code =="200"){
		        	 layer.msg(data.msg);
		         }
			})
		})
		$(".gudbu").on('click',function(){
			var isversion = $(".gudbu").val();
			var url = "/ssadmin/goVirtualCoinTypeJSP3.html";
            var param = {
                    "isversion":isversion
            }
            jQuery.post(url,param,function(data){
                 if(data.code =="200"){
                	 layer.msg(data.msg);
                 }
                
            })
			
		})
		
	})
	
</script>
</body>
</html>