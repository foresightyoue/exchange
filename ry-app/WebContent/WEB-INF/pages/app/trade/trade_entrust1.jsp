<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../../front/comm/include.inc.jsp" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no, width=device-width">
    <meta name="format-detection" content="telephone=no">
    <meta name="format-detection" content="email=no">
    <base href="${basePath}"/>
    <title>委托管理</title>
    <link rel="stylesheet" href="/static/front/app/css/css.css" />
    <link rel="stylesheet" href="/static/front/app/css/style.css" />
    <link rel="stylesheet" href="/static/front/app/css/swiper-3.4.2.min.css" />
    <script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js" ></script>
    <script type="text/javascript" src="/static/front/app/js/swiper-3.4.2.jquery.min.js" ></script>
    <style type="text/css">
    .typeselect{
        -webkit-appearance: button;
        background-color: #31435b;
        border: 0;
        color: white;
        outline: none;
       direction: rtl; 
       padding-right: 10px;
    }
    .typeselect option{
       /* direction: ltr;
       padding-left: .62rem; 
       
      text-align:center;
       float: left;  */
    } 
    .table {
        line-height: 1.5rem;
        background-color: #fff;
        text-align: left;
        width: 93%;
        margin: 0 auto;
        border: 1px solid #e6e6e6;
        padding: .62rem;
        margin-bottom: 1rem;
    }
    body{
        background-color: rgb(247, 247, 247);
    }
    .table li span:first-child{
        display: inline-block;
        width: 7rem;
    }
    .align_style{
    display: inline-block;
    margin-left: -.3rem;
    }
    html,body{
        height: 100%;
    }
    section{
        width: 100%;
        height: 100%;
        padding-top: 1rem;
    }
    .padding-right-clear{
    text-align: center;
    }
    .text-danger {
        line-height: 0;
        background: #fff;
        padding: 0;
    }
    .tradelook,.lookLog{
        color: #e23434;
    }
    .Personal-title p img {
        margin-top: 0.34rem !important;
    }
    .no_cont{
        width: 100%;
        height: auto;
        text-align: center;
    }
    #entrustInfo{
        margin-left: .62rem;
    }
    .present_entrust{
        margin-bottom: .62rem;
        border-bottom: 1px solid #e6e6e6; 
        /* width: 148%; */
        padding-bottom: .62rem;
        line-height: 1.5rem;
        padding: 0 .62rem;
    }
    rightarea-con .present_entrust:last-child{
        border-bottom: 0; 
    }
    .present_entrust span{
        display: inline-block;
        width: 6rem;
    }
    .flexLayout{
        display: flex;
        justify-content: space-between;
    }
    .flexLayout:first-child{
        margin-bottom: .62rem;
    }
    .present_entrust .flexLayout li{
        /* width: 33.33%; */
        text-align: center;
    }
    .present_entrust .flexLayout .text-l{
        text-align: left;
    }
    .present_entrust .flexLayout .text-r{
        text-align: right;
        width: 28%;
    }
    .present_entrust .flexLayout .text-r a{
        color: #5090dc;
    }
    .present_entrust .flexLayout .red,.present_entrust .flexLayout .text-success{
        width: 27%;
        font-size: .87rem;
        font-weight: 600;
        font-stretch: normal;
        letter-spacing: 0rem;
        text-align: left;
    }
    .present_entrust .flexLayout .red{
        color: #fa8221;
    }
    .present_entrust .flexLayout .text-success{
        color: #03c087;
    }
    .present_entrust .flexLayout .date{
        width: 50%;
        text-align: left;
        font-weight: normal;
        font-stretch: normal;
        letter-spacing: 0rem;
        color: #93a4b2;
    }
    .entrust_num{
        color: #bec8d0;
        font-size: .7rem
    }
    .details_mars{
        width: 100%;
        height: 100%;
        background-color: rgba(0,0,0,.3);
        position: fixed;
        left: 0;
        top: 0;
        z-index: 10;
        display: none;
        cursor: pointer;
    }
    #entrustsdetail{
        width: 80%;
        background-color: white;
        position: fixed;
        left: 50%;
        top: 50%;
        z-index: 1000;
        transform: translate(-50%,-50%);
        display: none;
    }
    .modal-title{
        height: 2.5rem;
        line-height: 2.5rem;
        text-align: center;
        font-size: 1rem;
        border-bottom: 1PX solid #e6e6e6;
    }
    .modal-body .table {
	    line-height: 1.5rem;
	    background-color: #fff;
	    text-align: left;
	    width: 100%;
	    margin: 0 auto;
	    border: 0;
	    margin-bottom: 1rem;
	    box-sizing: border-box;
        -webkit-box-sizing: border-box;
        font-size: .87rem;
        padding: .62rem;
	}
	.modal-body{
		max-height: 25rem;
	    overflow: auto;
	}
	.error{
	    background-image: url("/static/front/app/images/close.png");
	    background-repeat: no-repeat;
	    background-position: center;
	    background-size: 100%;
	    width: 2rem;
	    height: 2rem;
	    border-radius: 50%;
	    -webkit-border-radius: 50%;
	    position: absolute;
        right: -0.7rem;
        top: -0.8rem;	    
        background-color: white;
        cursor: pointer;
	}
	.table tr{
        text-align: center;
	}
	.table tr th:first-child,.table tr td:first-child{
	    text-align: left;
	}
	.table tr th{
	    font-size: .87rem;
	}
	.table tr td{
        font-size: .7rem;
    }
	.table tr th:last-child,.table tr td:last-child{
        text-align: right;
    }
    .trade-select{
        padding-right: 1.2em;
        background: url(/static/front/app/images/down.png) no-repeat 100% center;
        background-size: 0.75rem 0.45rem;
        float: left;
        padding-left: 0;
    }
    .fentrustContent_list1 li{
        width: 33.33%
    }
    .fentrustContent_list2 li{
        width: 50%
    }
    </style>
</head>
<body>
<nav>
    <div class="Personal-title">
           <span>
           <c:if test="${type == 1 }">
              <a href="${oss_url}/m/financial/index.html?menuFlag=account" style="display: inline-block;width: 3rem;height: 2.8rem;">
                 <em>
                    <i></i>
                    <i></i>
                 </em>
                 <strong>返回</strong>
              </a>
           </c:if>
           <c:if test="${type == 0 }">
                    <a href="javascript:;" onClick="javascript :history.back(-1)" style="display: inline-block;width: 3rem;height: 2.8rem;">
                        <em>
                            <i></i>
                            <i></i>
                        </em>
                        <strong>返回</strong>
                    </a>
           </c:if>
                    
                </span>
           <c:if test="${status == 0 }">
                           委托管理
           </c:if>
           <c:if test="${status == 1 }">
                            我的成交
           </c:if>

           <p><select class="form-control typeselect" id="recordType">
                          <option value="/m/trade/trade_entrust1.html?status=${status}&symbol=0&type=${type}">全部</option>
                          <c:forEach var="v" varStatus="vs" items="${requestScope.constant['tradeMappingss'] }">
                             <option id="${v.fid }" value="/m/trade/trade_entrust1.html?status=${status }&symbol=${v.fid}&type=${type}" ${ftrademapping.fid==v.fid?'selected':'' }>${v.fvirtualcointypeByFvirtualcointype2.fShortName }/${v.fvirtualcointypeByFvirtualcointype1.fShortName }</option>
                          </c:forEach>
                       </select>
           <img src="/static/front/app/images/drop-down.png"></p>
    </div>
</nav>
<section>
   <div class="col-xs-10 padding-right-clear" style="padding-bottom: 1.5rem;">
       <div class="col-xs-12 padding-right-clear padding-left-clear rightarea">
           <div class="col-xs-12 rightarea-con">
                <!-- <div class="panel-heading padding-bottom-clear">
                    <div class="form-group">
                       <span>挂单类型:</span>
                    </div>
                </div> -->
                     <c:if test="${fn:length(fentrusts) ==0 }">
                         <span>
                                                                                       暂无委托
                         </span>
                     </c:if>

                     <c:if test="${fn:length(fentrusts) !=0 }">                     
                     <div  class="screen" style="overflow: auto;text-align: initial;padding-left: 1rem;margin-bottom: 1rem;">
                         <div style="float: left;margin-right: 1.3rem;display:inline-block;">
	                         <span style="float: left;font-size: 1.05rem;font-weight: bold;color: #999;">交易类型：</span>
	                         <select class="trade-select" id="sell" style="font-size: 1.05rem;width:3.5em;border: 0;background-color:rgba(255,255,255,0);">
	                                <option value="0">全部</option>
	                                <option value="1">买入</option>
	                                <option value="2">卖出</option>
	                         </select>
                         </div>
                         <div style="float: right;display:inline-block;padding-right: 1rem;">
	                         <span style="float: left;font-size: 1.05rem;font-weight: bold;color: #999;">排序：</span>
	                         <select class="trade-select" id="time" style="font-size: 1.05rem;width:5.5em;border: 0;background-color:rgba(255,255,255,0);">
	                                <option value="0">时间递减</option>
	                                <option value="1">时间递增</option>
	                         </select>
                         </div>
                     </div>
                     </c:if>
                    <div class="fentrustContent">
						<c:forEach items="${fentrusts }" var="v" varStatus="vs">
							<div id="${vs.index}" class="present_entrust ${v.fentrustType}">
								<ul class="flexLayout">
									<li class="${v.fentrustType==0?'red':'text-success' }">(${v.ftrademapping.fvirtualcointypeByFvirtualcointype2.fShortName})${v.fentrustType_s}${v.fisLimit==true?'[市价]':'' }</li>
									<li class="date"><fmt:formatDate value="${v.fcreateTime }" pattern="yyyy-MM-dd HH:mm"/></li>
									<li class="text-r">
										<c:if test="${v.fstatus==1 || v.fstatus==2}">
										     <c:if test="${v.fstatus==1}">
										        <a href="javascript:void(0);" data-value="${v.fid}">未成交</a>
										     </c:if>
										     <c:if test="${v.fstatus==2}">
                                                <a href="javascript:void(0);" data-value="${v.fid}">部分成交</a>
                                             </c:if>
					                    	<a href="javascript:void(0);" class="tradecancel" data-value="${v.fid}" refresh="1">取消</a>
					                     </c:if>
					                    <c:if test="${v.fstatus==3}">
					                        <a href="javascript:void(0);" data-value="${v.fid}">完全成交</a>
					                    	<a href="javascript:void(0);" class="lookLog" data-value="${v.fid}">查看</a>
					                    </c:if>
					                    <c:if test="${v.fstatus == 4}">
					                        <a href="javascript:void(0);" data-value="${v.fid}">用户撤销</a>
					                    </c:if>
									</li>
								</ul>
								<ul class="flexLayout fentrustContent_list1" style="line-height: 1.3rem;margin-bottom: .4rem;">
									<li class="text-l"><div class="entrust_num">委托价</div><fmt:formatNumber value="${v.fprize }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${v.ftrademapping.fcount1 }"/></li>
									<li><div class="entrust_num">委托量</div><fmt:formatNumber value="${v.fcount }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${v.ftrademapping.fcount2 }"/></li>
									<li style="text-align: right;"><div class="entrust_num">成交量</div><fmt:formatNumber value="${v.fcount-v.fleftCount }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${v.ftrademapping.fcount2 }"/></li>
								</ul>
								<ul class="flexLayout fentrustContent_list2" style="line-height: 1.3rem;margin-bottom: .62rem;">
									<li style="text-align: left;"><div class="entrust_num">成交均价</div><fmt:formatNumber value="${afPrizeMap[v.fid]}" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${v.ftrademapping.fcount1 }"/></li>
									<li style="text-align: right;"><div class="entrust_num">成交总额</div><fmt:formatNumber value="${afPrizeMap[v.fid] * (v.fcount-v.fleftCount)}" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${v.ftrademapping.fcount1 }"/></li>
								</ul>
							</div>
						</c:forEach>
                    </div>
						<c:if test="${fn:length(fentrusts) !=0 }">
						    <%
						       int total = (int)request.getAttribute("total") ;
						       int maxNum =(int)(request.getAttribute("maxNum"));
						       int totalPage = (int)Math.ceil(total * 1.0 / maxNum);
						       request.setAttribute("totalPage", totalPage);
						    %>
							<div> 
							  <a href="javascript:void(0);" style="color: #5A96DE;cursor: pointer;" id="prePage">上一页</a>
							  <a href="javascript:void(0);"  style="color: #5A96DE;cursor: pointer;" id="nextPage">下一页</a>
							</div>
							<div>页数: ${currentPage }/<fmt:formatNumber value="${totalPage}" pattern="#"></fmt:formatNumber>  
							<a id="firstPage"  style="color: #5A96DE">首页</a> <a id="lastPage"  style="color: #5A96DE;cursor: pointer;">末页</a></div>
						</c:if>
                   <%--  <c:forEach items="${fentrusts }" var="v" varStatus="vs">
                        <div class="present_entrust">
                            <ul class="flexLayout">
                                <li class="${v.fentrustType==0?'red':'text-success' }">(${v.ftrademapping.fvirtualcointypeByFvirtualcointype2.fShortName})${v.fentrustType_s}${v.fisLimit==true?'[市价]':'' }</li>
                                <li class="date"><fmt:formatDate value="${v.fcreateTime }" pattern="yyyy-MM-dd"/></li>
                                <li class="text-r">
                                    <c:if test="${v.fstatus==1 || v.fstatus==2}">
                                        <a href="javascript:void(0);" class="tradecancel" data-value="${v.fid}" refresh="1">取消</a>
                                     </c:if>
                                    <c:if test="${v.fstatus==3}">
                                        <a href="javascript:void(0);" class="tradelook" data-value="${v.fid}">查看</a>
                                    </c:if>
                                </li>
                            </ul>
                            <ul class="flexLayout">
                                <li class="text-l"><div class="entrust_num">价格</div><fmt:formatNumber value="${v.fprize }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${v.ftrademapping.fcount1 }"/></li>
                                <li><div class="entrust_num">成交量</div><fmt:formatNumber value="${v.fcount-v.fleftCount }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${v.ftrademapping.fcount2 }"/></li>
                                <li class="text-r"><div class="entrust_num">平均成交价</div><fmt:formatNumber value="${afPrizeMap[v.fid] }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${v.ftrademapping.fcount1 }"/></li>
                                <li class="text-r"><div class="entrust_num">成交金额</div><fmt:formatNumber value="${afPrizeMap[v.fid] * v.fcount }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${v.ftrademapping.fcount1 }"/></li>
                            </ul>
                        </div>
                    </c:forEach> --%>
                     <%-- <c:forEach items="${fentrusts }" var="v" varStatus="vs">
                         <ul class="table">
                            <li><span>委托时间:</span><span><fmt:formatDate value="${v.fcreateTime }" pattern="yyyy-MM-dd HH:mm:ss"/></span></li>
                            <li><span>类型 :</span><span class="${v.fentrustType==0?'text-danger':'text-success' }">(${v.ftrademapping.fvirtualcointypeByFvirtualcointype2.fShortName})${v.fentrustType_s}${v.fisLimit==true?'[市价]':'' }</span></li>
                            <li><span>数量:</span><span><fmt:formatNumber value="${v.fcount }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${v.ftrademapping.fcount2 }"/></span></li>
                            <li><span>价格:</span><span><fmt:formatNumber value="${v.fprize }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${v.ftrademapping.fcount1 }"/></span></li>
                            <li><span>金额:</span><span><fmt:formatNumber value="${v.famount}" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${v.ftrademapping.fcount1 }"/></span></li>
                            <li><span>成交量:</span><span><fmt:formatNumber value="${v.fcount-v.fleftCount }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${v.ftrademapping.fcount2 }"/></span></li>
                            <li><span>成交金额:</span><span><fmt:formatNumber value="${v.fsuccessAmount }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${v.ftrademapping.fcount1 }"/></span></li>
                            <li><span>手续费:</span>
                                <c:choose>
                                    <c:when test="${v.fentrustType==0 }">
                                    <span class="align_style"><fmt:formatNumber value="${v.ffees-v.fleftfees}" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${v.ftrademapping.fcount1 }"/></span>
                                    </c:when>
                                    <c:otherwise>
                                    <span class="align_style"><fmt:formatNumber value="${v.ffees-v.fleftfees}" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${v.ftrademapping.fcount1 }"/></span>
                                    </c:otherwise>
                                </c:choose>
                            </li>
                            <li><span>平均成交价:</span><span><fmt:formatNumber value="${((v.fcount-v.fleftCount)==0)?0:  v.fsuccessAmount/((v.fcount-v.fleftCount)) }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${v.ftrademapping.fcount1 }"/></span></li>
                            <li><span>状态<c:if test="${status == 0 }">/操作:</c:if></span>
                                <span>
                                    ${v.fstatus_s }
                                     <c:if test="${v.fstatus==1 || v.fstatus==2}">
                                     &nbsp;|&nbsp;<a href="javascript:void(0);" class="tradecancel" data-value="${v.fid}" refresh="1">取消</a>
                                     </c:if>
                                    <c:if test="${v.fstatus==3}">
                                    &nbsp;|&nbsp;<a href="javascript:void(0);" class="tradelook" data-value="${v.fid}">查看</a>
                                    </c:if>
                                </span>
                            </li>
                        </ul>
                    </c:forEach>   --%>
                   <%--  <c:forEach items="${fentrusts }" var="v" varStatus="vs">
                    <td class="gray"><fmt:formatDate value="${v.fcreateTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    <td class="${v.fentrustType==0?'text-danger':'text-success' }">(${v.ftrademapping.fvirtualcointypeByFvirtualcointype2.fname})${v.fentrustType_s}${v.fisLimit==true?'[市价]':'' }</td>
                    <td><fmt:formatNumber value="${v.fcount }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${v.ftrademapping.fcount2 }"/></td>
                    <td><fmt:formatNumber value="${v.fprize }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${v.ftrademapping.fcount1 }"/></td>
                    <td><fmt:formatNumber value="${v.famount}" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${v.ftrademapping.fcount1 }"/></td>
                    <td><fmt:formatNumber value="${v.fcount-v.fleftCount }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${v.ftrademapping.fcount2 }"/></td>
                    <td><fmt:formatNumber value="${v.fsuccessAmount }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${v.ftrademapping.fcount1 }"/></td>
                    <c:choose>
                        <c:when test="${v.fentrustType==0 }">
                        <td><fmt:formatNumber value="${v.ffees-v.fleftfees}" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${v.ftrademapping.fcount1 }"/></td>
                        </c:when>
                        <c:otherwise>
                        <td><fmt:formatNumber value="${v.ffees-v.fleftfees}" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${v.ftrademapping.fcount1 }"/></td>
                        </c:otherwise>
                    </c:choose>
                    <td><fmt:formatNumber value="${((v.fcount-v.fleftCount)==0)?0:  v.fsuccessAmount/((v.fcount-v.fleftCount)) }" pattern="##.##" maxIntegerDigits="15" maxFractionDigits="${v.ftrademapping.fcount1 }"/></td>
                    <td>
                     ${v.fstatus_s }
                     <c:if test="${v.fstatus==1 || v.fstatus==2}">
                     &nbsp;|&nbsp;<a href="javascript:void(0);" class="tradecancel" data-value="${v.fid}" refresh="1">取消</a>
                     </c:if>
                    <c:if test="${v.fstatus==3}">
                    &nbsp;|&nbsp;<a href="javascript:void(0);" class="tradelook" data-value="${v.fid}">查看</a>
                    </c:if>
                    </td>
               </tr>
                     </c:forEach> --%>
                   <div class="text-right">
                      <%-- ${pagin } --%>
                   </div>
                </div>
                <input type="hidden" value="${currentPage}" id="currentPage">
           </div>
       </div>
   <input id="fhasRealValidate" type="hidden" value="true">
<input type="hidden" id="symbol" value="${ftrademapping.fid }">
</section>
<div class="details_mars"></div>
<div id="entrustsdetail">
    <div class="error"></div> 
    <div class="modal-title"></div>
    <div class="modal-body"></div>
</div>
<script type="text/javascript" src="${oss_url}/static/front/js/plugin/bootstrap.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/trade/trade.js"></script>
<script type="text/javascript" src="/static/front/js/comm/util.js"></script>
<script type="text/javascript" src="/static/front/js/language/language_cn.js"></script>
<script type="text/javascript" src="/static/front/js/layer/layer.js"></script>
<script type="text/javascript" src="/static/front/js/comm/comm.js"></script>
<script type="text/javascript">
function ret(){
    var symbol=$("#recordType :selected").attr("id");
    location.href="/m/trade/coin.html?coinType=1&tradeType=0&menuFlag=trade&symbol="+symbol;
}
$("body").on("click",".lookLog",function(){
	layer.load(1);
	var id =  $(this).attr("data-value");
	var url = "/m/trade/entrustLog.html?random=" + Math.round(Math.random() * 100);
    var param = {
        id : id
    };
    jQuery.get(url, param, function(data) {
        if (data != null && data.result == true) {
            var modal = $("#entrustsdetail");
            modal.find('.modal-title').html(data.title);
            modal.find('.modal-body').html(data.content);
            modal.show();
            $(".details_mars").show();
            layer.closeAll("loading");
            $('html,body').css("overflow","hidden")
        }
    }, "json").fail(function() {
		layer.msg("网络错误!请稍后重试!");
		layer.closeAll("loading");
	});
});
$("body").on("click",".error",function(){
    $('html,body').css("overflow","auto")
    $("#entrustsdetail").hide();
    $(".details_mars").hide();
})
$("body").on("click",".details_mars",function(){
    $('html,body').css("overflow","auto")
    $("#entrustsdetail").hide();
    $(this).hide();
})  

$("#sell").change(function () {
	if($(this).val()==0){
		$(".present_entrust").removeAttr("style");
	}else{
      if($(this).val()==1){
          <c:forEach items="${fentrusts }" var="v" varStatus="vs">
          	$(".1").css("display","none");
          	$(".0").removeAttr("style");
          </c:forEach> 
      }else{
    	  <c:forEach items="${fentrusts }" var="v" varStatus="vs">
        	$(".0").css("display","none");
        	$(".1").removeAttr("style");
          </c:forEach>
      }
	}
});

$("#time").change(function () {
	if($(this).val()==0){
  	  var bigContainer = document.querySelectorAll(".fentrustContent>div");  
        for(var i=bigContainer.length-1;i>-1;i--){  
            document.querySelector(".fentrustContent").appendChild(bigContainer[i]);  
        } 
    }else{
  	  var bigContainer = document.querySelectorAll(".fentrustContent>div");  
        for(var i=bigContainer.length-1;i>-1;i--){  
            document.querySelector(".fentrustContent").appendChild(bigContainer[i]);  
        }
    }
});
$(function() {
    var currentPage = Number("${currentPage}");
    var nextPage = currentPage + 1;
    var totalPage = ${totalPage};
    var status = ${status};
    if (totalPage <= currentPage ) {
        $("#nextPage").hide();
        $("#lastPage").hide();
    } 
    if (1 >= currentPage ) {
       $("#prePage").hide();
       $("#firstPage").hide();
    } 
    
    $("#nextPage,#firstPage,#lastPage,#prePage").click(function() {
        if(this.id =='firstPage'){
            nextPage = 1;
        }
        if(this.id =='lastPage'){
            nextPage = totalPage;
        }
        if(this.id =='prePage'){
            nextPage = currentPage - 1;
        }
        location.href = "/m/trade/trade_entrust1.html?status="+ status +"&type=1&currentPage=" + nextPage;
    });
   
})
</script>
</body>
</html>
