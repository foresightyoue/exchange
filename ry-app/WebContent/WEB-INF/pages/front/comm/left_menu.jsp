 <%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="include.inc.jsp"%>

<style>
	.container-full{ margin-top: 20px;}
	.leftmenu a {
	    color: #337ab7;
}
</style>

<div class="col-xs-2 padding-left-clear leftmenu">
<div class="panel panel-default">
    <div class="panel-heading" data-toggle="collapse" href="#t1">
         <a class="panel-title">
         <i class="iconfont">&#xe790;</i>我的资产</a>         
         
         <a class="pull-right">
         <i class="iconfont">&#xe65a;</i>
         </a>
    </div>
    <div id="t1" class="panel-collapse in">
        <div class="panel-body nav nav-pills nav-stacked">
        <li class="${leftMenu=='financial'?'active':'' }"><a href="${oss_url}/financial/index.html">
        			<i class="iconfont">&#xe624;</i>账户资产</a></li>
		<li class="${leftMenu=='record'?'active':'' }"><a href="${oss_url}/account/record.html">
        			<i class="iconfont">&#xe6e2;</i>财务日志</a></li>			
		<li class="${leftMenu=='recharge'?'active':'' }"><a href="${oss_url}/account/rechargeBtc.html">
        			<i class="iconfont">&#xe686;</i>充值</a></li>
		<li class="${leftMenu=='withdraw'?'active':'' }"><a href="${oss_url}/account/withdrawBtc.html">
        			<i class="iconfont">&#xe616;</i>提现</a></li>
        <%-- <li class="${leftMenu=='accountAdd'?'active':'' }"><a href="/financial/accountbank.html">
        			<i class="iconfont">&#xe6e2;</i>资金帐户</a></li>		 --%>	
        </div>
    </div>
    
    <div class="panel-heading" data-toggle="collapse" href="#t2">
         <a class="panel-title">
         <i class="iconfont">&#xe623;</i>我的交易</a>
         
         <a class="pull-right">
         <i class="iconfont">&#xe65a;</i>
         </a>
    </div>
    <div id="t2" class="panel-collapse in">
        <div class="panel-body nav nav-pills nav-stacked">
        <li class="${leftMenu=='entrust0'?'active':'' }"><a href="${oss_url}/trade/entrust.html?status=0"><i class="iconfont">&#xe64d;</i>委托管理</a></li>
		<li class="${leftMenu=='entrust1'?'active':'' }"><a href="${oss_url}/trade/entrust.html?status=1"><i class="iconfont">&#xe622;</i>我的成交</a></li>
<%-- 		<li class="${leftMenu=='c2cOrder'?'active':'' }"><a href="${oss_url}/ctc/list2.html"><i class="iconfont">&#xe677;</i>C2C订单</a></li> --%>
		<li class="${leftMenu=='c2cOrder'?'active':'' }"><a href="/otc/orderList.html?currentPage=1&fw=0"><i class="iconfont">&#xe677;</i>OTC订单</a></li>
        <%-- <li class="${leftMenu=='assetsrecord'?'active':'' }"><a href="/financial/assetsrecord.html"><i class="iconfont">&#xe64d;</i>资产记录</a></li> --%>
<%-- 		<li class="${leftMenu=='mylogs'?'active':'' }"><a href="/crowd/logs.html"><i class="iconfont">&#xe60a;</i>我的ICO</a></li> --%>
        </div>
    </div>
 
    <!---->
    <div class="panel-heading" data-toggle="collapse" href="#t4">
         <a class="panel-title">
         <i class="iconfont">&#xe60b;</i>安全中心</a>
         
         <a class="pull-right">
         <i class="iconfont">&#xe65a;</i>
         </a>
    </div>
    <div id="t4" class="panel-collapse in">
        <div class="panel-body nav nav-pills nav-stacked">
		<li  class="${leftMenu=='security'?'active':'' }"><a href="${oss_url}/user/security.html"><i class="iconfont">&#xe646;</i>安全中心</a></li>
		<li  class="${leftMenu=='loginlog'?'active':'' }"><a href="${oss_url}/user/userloginlog.html?type=1"><i class="iconfont">&#xe64d;</i>登录记录</a></li>
        <li  class="${leftMenu=='real'?'active':'' }"><a href="${oss_url}/user/realCertification.html"><i class="iconfont">&#xe62a;</i>身份认证</a></li>
        <li  class="${leftMenu =='accountAdd'?'active':'' }"><a href="${oss_url}/financial/accountbank.html"><i class="iconfont">&#xe62e;</i>银行卡管理</a></li>
        </div>
    </div>
    <!---->
    <div class="panel-heading" data-toggle="collapse" href="#t5">
         <a class="panel-title">
         <i class="iconfont">&#xe600;</i>账户中心</a>
         <a class="pull-right">
         <i class="iconfont">&#xe65a;</i>
         </a>
    </div>
    <div id="t5" class="panel-collapse in">
        <div class="panel-body nav nav-pills nav-stacked">
		<li  class="${leftMenu=='intro'?'active':'' }"><a href="${oss_url}/introl/mydivide.html"><i class="iconfont">&#xe60f;</i>邀请好友</a></li>
        </div>
    </div>
</div>

</div>