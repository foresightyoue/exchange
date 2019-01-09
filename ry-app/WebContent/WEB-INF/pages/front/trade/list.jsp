<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="../comm/include.inc.jsp"%>
<%@include file="../comm/link.inc.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="/static/front/css/c2c/app.css">
<title>订单列表</title>
<style>
	.order_list{
		display: none;
	}
	.top-fixed-nav {
		position: fixed;
		left: 0;
		top: 0;
		z-index: 100;
	}
	.panel-body table tr {
		border-bottom: 1px solid #dddddd;
	}
	.panel-body table tr:last-child {
		border-bottom: 0;
	}
	.panel-default{
	    border-color: white;
	}
	.container{
        padding-top: 50px;
    } 
 #sort{
        padding: 5px 15px;
        color: #555;
        background-color: #f0f0f0;
        border: 1px solid #ddd;
        float: right;
        border-radius:5px;
        -webkit-border-radius:5px;
        -moz-border-radius:5px;
        -ms-border-radius:5px;
        -o-border-radius:5px;
        cursor: pointer;
    }
    #choose-type{
        top: 0;
        background-color: #F0F0F0;
        width: 140px;
        height: 35px;
        display: inline-block;
        line-height: 39px;
        border-radius: 3px;
        padding: 0 2px 0 2px;
        border: 1px solid #DDDDDD;
        padding-left: 10px;
        margin-right: 20px;
        float: right;
    }
    #order-type{
        border: 0;
        float: right;
        background: transparent;
        height: 22px;
        margin: 6px 0;
    }
</style>
</head>
<body>
<%@include file="../comm/header.jsp"%> 
<div class="container">

    <div class="row">
<%@include file="../comm/left_menu.jsp" %>
        <div class="col-md-10" style="background: white;">
            <div class="panel panel-default">
                <div class="panel-heading">C2C订单</div>
                <div class="panel-body">
                    <ul class="nav nav-tabs">
                        <li class="active">
                            <!--<a href="https://btcotc.latrell.me/order/list?type=Processing">进行中的交易</a>-->
                            <a >进行中的交易</a>
                        </li>
                        <li>
                            <!--<a href="https://btcotc.latrell.me/order/list?type=Completed">已完成的交易<span class="badge">1</span></a>-->
                            <a >已完成的交易<span class="badge"></span></a>
                        </li>
                        <span id="sort">顺序/倒序</span>
                        <span id="choose-type">
	                        <span>订单类型：</span>
	                        <select id="order-type">
	                            <option value="-1">全部</option>
	                            <option value="0">买入</option>
	                            <option value="1">卖出</option>
	                        </select>
                        </span>
                    </ul>
                </div>
                 <%
                 String[] status = new String[]{"待派单","待确认","交易完成","申诉中","已取消"};
                     request.setAttribute("status", status);
                 %>
                <div class="panel-body order_list" style="display:block;">
                    <table class="table" id="table-sort1">
                        <thead>
                            <tr>
                                <th>交易伙伴</th>
                                <th>编号</th>
                                <th style="width:3em;">类型</th>
                                <th>金额</th>
                                <th>数量</th>
                                <th class="time1">创建时间</th>
                                <th>状态</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${orderList}" var="list">
                            <c:if test="${list.fStatus != '2' && list.fStatus != '4'}">
	                            <tr>
	                                <td>
	                                    <div class="user">
	                                        <div class="avatar">
	                                            <span aria-hidden="true" class="glyphicon glyphicon-user"></span> 
	                                            <span class="user-status online"></span>
	                                        </div>                 
	                                        <div class="username"><a href="#">${currentUser.fid eq list.fBuyerId ? list.sellName : list.buyerName}</a></div>
	                                    </div>
	                                </td>
	                                <td>${list.fId}</td>
	                                <td>${list.fType eq 0 ? '买入' : '卖出'}</td>
 					<input type="hidden" class="fType" value="${list.fType}">
	                                <td>${list.fCnyTotal} CNY</td>
	                                <td>${list.fNum} ${list.cointype }</td>
	                               	<td><fmt:formatDate value="${list.fCreateTime}" type="both" pattern="yyyy-MM-dd HH:mm:ss" /></td>
	                                <td><span class="text-default">${status[list.fStatus]}</span></td>
	                                <td>
	                                    <a href="${oss_url}/ctc/details.html?fId=${list.fId }" class="btn btn-info btn-xs"><span aria-hidden="true" class="glyphicon glyphicon-comment"></span>详情</a>
                                        <c:if test="${status[list.fStatus]=='待确认' || status[list.fStatus] == '待派单'}">
                                        <a href="${oss_url}/ctc/appeal.html?orderId=${list.fId }" class="btn btn-info btn-xs"><span aria-hidden="true" class="glyphicon glyphicon-comment"></span>申诉</a>
                                        <c:if test="${list.fType eq 0 }">
                                        <a href="#" onclick="cancel('${list.fId }')" class="btn btn-info btn-xs"><span aria-hidden="true" class="glyphicon glyphicon-comment"></span>取消</a>
                                        </c:if>
                                        </c:if>
                                    </td>
	                            </tr>
	                            </c:if>
							</c:forEach>
                           
                            
                        </tbody>
                    </table>
                </div>

                <!--已完成订单-->
                <div class="panel-body order_list dealed">
                    <table class="table"  id="table-sort2">
                        <thead>
                            <tr>
                                <th>交易伙伴</th>
                                <th>编号</th>
                                <th style="width:3em;">类型</th>
                                <th>金额</th>
                                <th>数量</th>
                                <th class="time2">创建时间</th>
                                <th>状态</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${orderList}" var="list">
                            <c:if test="${list.fStatus != '0'&& list.fStatus !='1'&& list.fStatus != '3'}">
                                <tr>
                                    <td>
                                        <div class="user">
                                            <div class="avatar">
                                                <span aria-hidden="true" class="glyphicon glyphicon-user"></span>
                                                <span class="user-status online"></span>
                                            </div>
                                            <div class="username"><a href="#">${currentUser.fid eq list.fBuyerId ? list.sellName : list.buyerName}</a></div>
                                        </div>
                                    </td>
                                    <td>${list.fId}</td>
                                    <td>${list.fType eq 0 ? '买入':'卖出'}</td>
                                    <input type="hidden" class="fType" value="${list.fType}">
                                    <td>${list.fCnyTotal}</td>
                                    <td>${list.fNum}</td>
                                    <td><fmt:formatDate value="${list.fCreateTime}" type="both" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                                    <td><span class="text-default">${status[list.fStatus]}</span></td>
                                    <td>
                                        <a href="${oss_url}/ctc/details.html?fId=${list.fId }" class="btn btn-info btn-xs"><span aria-hidden="true" class="glyphicon glyphicon-comment"></span>详情</a>
                                    </td>
                                </tr>
                                </c:if>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<%@include file="../comm/footer.jsp"%>
<script src="/static/front/js/c2c/jquery-1.11.3.min.js"></script>
<script>
   $(".nav-tabs").on("click","li",function(){
       $(this).addClass("active").siblings().removeClass("active");
       $(".order_list").hide();
       $(".order_list").eq($(this).index()).show();
   })
   
   var flag = 1;
   var flag1 = 1;
   $("#sort").on("click",function(){
       $(".time1").click();
       $(".time2").click();
   });
   
   $(".time1").on("click",function(){
       var tbody = document.querySelector("#table-sort1").tBodies[0];
       var td = tbody.rows;
       sort(tbody,td,flag,this.cellIndex);
       flag = -flag;
   });
   
   $(".time2").on("click",function(){
       var tbody2 = document.querySelector("#table-sort2").tBodies[0];
       var td2 = tbody2.rows;
       sort(tbody2,td2,flag1,this.cellIndex);
       flag1 = -flag1;
   });
$(function(){
    showOrder($("#order-type").val());
      $("#order-type").change(function(){
          var chooseType = this.value;
          showOrder(chooseType);
    });
});
       function sort(tbody,td,flag, n) {
         var arr = [];
         for (var i = 0; i < td.length; i++) {
               arr.push(td[i]);
         };
         arr.sort(function(a, b) {
              return method(a.cells[n].innerHTML, b.cells[n].innerHTML) * flag;
          });
          for (var i = 0; i < arr.length; i++) {
               tbody.appendChild(arr[i]);
          };
     };
 
function showOrder(chooseType){
    $("tr .fType").each(function(){
        if(chooseType != -1){
            if(this.value == chooseType){
                $(this).parent().show();
            }else{
                $(this).parent().hide();
            }
        }else{
            $(this).parent().show();
        }
     });
    }

     function method(a, b) {
         return new Date(a.split('-').join('/')).getTime() - new Date(b.split('-').join('/')).getTime();
     };
  function cancel(orderId){
    var msg = "您真的确定要取消此订单吗？\n\n请确认！";
        if (confirm(msg)==true){
            $.ajax({
                url : "/ctc/cancel.html",
                data : {
                    "orderId" : orderId
                },
                type: "post",
                dataType: "json",
                secureuri: false,
                async : true,
                success: function (data) {
                    if(data.code == 0 ||data.code==500){
                        layer.msg(data.msg);
                        window.location.href="/ctc/list2.html";
                    }else{
                        layer.msg(data.msg);
                    }
                },
                error: function (data) {
                    layer.msg("让网络飞一会~");
                }
            });
        }else{
            return false;
        }
    }
</script>
</body>
</html>