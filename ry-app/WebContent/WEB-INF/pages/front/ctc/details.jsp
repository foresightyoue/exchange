<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <%@ include file="../comm/include.inc.jsp"%>
<%@include file="../comm/link.inc.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<base href="${basePath}"/>
<link rel="stylesheet" href="/static/front/css/c2c/common.css">
    <link rel="stylesheet" href="/static/front/css/c2c/order_details.css">
<title>订单详情</title>
<style type="text/css">
footer {
    width: 100%;
    height: auto;
    background: #252c34;
    padding: 30px 0;
    padding-bottom: 60px;
}
.btn_sure:hover,.btn_sure:focus{
	color: #fff;
	text-decoration: none;
}
</style>
</head>
<body>
<%@include file="../comm/header.jsp"%> 
<div class="container">
    <div class="order_details">
        <div class="order_details_hint">
            <img src="/static/front/app/images/details.png" alt="">
            <span class="text-danger">订单详情</span>
        </div>
        <%
        String[] status = new String[]{"待派单","待确认","交易完成","申诉中","已取消"};
            request.setAttribute("status", status);
        %>
        <div class="messageBox clearfix">
            <div class="order_messageBox fl">
                <div class="hint">订单信息</div>
                <ul class="order_message">
	                <c:forEach items="${detailsList}" var="details">
	                    <li>
	                        <span class="message_hint">订单号：</span>
	                        <span>${details.fOrderNum}</span>
	                    </li>
	                    <li>
	                        <span class="message_hint">下单时间：</span>
	                        <span>${details.fCreateTime}</span>
	                    </li>
	                    <li>
	                        <span class="message_hint">卖方：</span>
	                        <span>${details.sellName}</span>
	                    </li>
	                    <li>
	                        <span class="message_hint">买方：</span>
	                        <span>${details.buyerName}</span>
	                    </li>
	                    <li>
	                        <span class="message_hint">类型：</span>
	                        <span style="color: red;">${details.fType eq 0 ? '买入' : '卖出'}</span>
	                    </li>
	                     <li>
                        <span class="message_hint">买方银行：</span>
                        <span>${fBuyName }</span>
                    </li>
                     <li>
                        <span class="message_hint">买方卡号：</span>
                        <span>${fBuyBankNumber } </span>
                     <%--   ${fn:substring(fBuyBankNumber, 0,3)}******  ${fn:substring(fBuyBankNumber, 12,19)} --%>
                       
                       
                    </li>
	                    <li>
	                        <span class="message_hint">币种：</span>
	                        <span>${details.cointype}</span>
	                    </li>
	                    <li>
	                        <span class="message_hint">单价(CNY)：</span>
	                        <span>${details.fPrice}</span>
	                    </li>
	                    <li>
	                        <span class="message_hint">数量：</span>
	                        <span>${details.fNum}</span>
	                    </li>
	                    <li>
	                        <span class="message_hint">总价(CNY)：</span>
	                        <span>${details.fCnyTotal}</span>
	                    </li>
	                    <li>
	                        <span class="message_hint">状态：</span>
	                        <span style="color: red;">${status[details.fStatus]}</span>
	                    </li>
                    </c:forEach>
                </ul>
            </div>
            <div class="deal_operationBox fr">
                <div class="hint">交易操作</div>
                <ul class="deal_operation">
                    <li>
                        <span class="message_hint">付款方式：</span>
                        <span>转账</span>
                    </li>
                    <li>
                        <span class="message_hint">银行：</span>
                        <span>${payType.fName}</span>
                    </li>
                    <li>
                        <span class="message_hint">姓名：</span>
                        <span>${payType.realName }</span>
                    </li>
                    <li>
                        <span class="message_hint">账号：</span>
                        <span>${payType.fBankNumber}</span>
                    </li>
                    <li>
                        <span class="message_hint">地址：</span>
                        <span>${payType.faddress}</span>
                    </li>
                </ul>
                <div class="text_hint">
                    <span style="color: #333; font-size: 15px; ">交易提示：</span>
                    <span style="line-height: 150%;">请买方主动联系卖方，确认是否收到线下打款。如果实际已打款而对方无回应，请联系平台客服处理。</span>
                </div>
                <div id="confirm" class="btn_sure" style="display:${currentUser.fid eq detailsList[0].fSellerId && detailsList[0].fStatus eq 1 ? 'block' : 'none'}">确认放行</div>
            </div>
        </div>
    </div>
</div>
<%@include file="../comm/footer.jsp"%>
<script src="/static/front/js/c2c/jquery-1.11.3.min.js"></script>

 <%-- <c:forEach items="${detailsList}" var="details">
	<p>${details.fOrderNum}</p>
	<p>${details.fCreateTime}</p>
	<p>${details.sellName}</p>
	<p>${details.buyerName}</p>
	<p>${details.fType}</p>
	<p>${details.fCoinType}</p>
	<p>${details.fPrice}</p>
	<p>${details.fNum}</p>
	<p>${details.fCnyTotal}</p>
	<p>${details.fStatus}</p>
	 </c:forEach> --%>
	 
	<%--  ${payType.fName}
	 ${payType.realName }
	 ${payType.fBankNumber}
	 ${payType.faddress} --%>
</body>
<script type="text/javascript">
	$(function(){
		$("#confirm").click(function(){
			layer.load(1);
			var orderNo = "${detailsList[0].fOrderNum}";
			$.post("/ctc/confirmOrder.html",{"orderNo":orderNo},function(result){
				if(result.code == 0){
					layer.msg(result.msg);	
					location.reload();
				}else{
					layer.msg(result.msg);
					location.reload();
				}
			},"json").fail(function(){
				layer.msg("网络错误");
				layer.close(load);
			})
		})
	})
</script>
</html>