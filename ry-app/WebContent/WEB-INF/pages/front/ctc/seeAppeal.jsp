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
<title>查看申诉申请</title>

</head>
<body>
<%@include file="../comm/header.jsp"%> 
<div class="container">
    <div class="order_details">
        <div class="order_details_hint">
            <img src="/static/front/app/images/details.png" alt="">
            <span class="text-danger">查看申诉申请</span>
        </div>
        
            <div style="height: 100px;width: 100px"><img src="${imgpath1}"></div>
            
            <div style="height: 100px;width: 100px"><img src="${imgpath2}"></div>
            
            <div style="height: 100px;width: 100px"><img src="${imgpath3}"></div>

                        备注:${remark}
            
            <br>
            <c:if test="${type == 0}">
                <input type="button" id="fx" value="放行"/>
            </c:if>
            <c:if test="${type == 1}">
                <input type="button" id="qx" value="取消此订单"/>
            </c:if>
    </div>
</div>
<%@include file="../comm/footer.jsp"%>
<script src="/static/front/js/c2c/jquery-1.11.3.min.js"></script>
<script type="text/javascript">
    $(function(){
        $("#fx").click(function(){
            var orderId = '${orderId}';
            var type = '${type}';
            $.ajax({
                url : "/ssadmin/confirmOrder1.html",
                data : {
                    "orderNo":orderId
                },
                type: "post",
                dataType: "json",
                secureuri: false,
                async : true,
                success: function (data) {
                	if(data.code == 0){
                        $.ajax({
                            url : "/ssadmin/successAppeal.html",
                            data : {
                                "orderNo":orderId,
                                "type":type
                            },
                            type: "post",
                            dataType: "json",
                            secureuri: false,
                            async : true,
                            success: function (data) {
                                if(data.code == 0){
                                    layer.msg('放行成功!');
                                    setTimeout(function(){
                                    	window.location.href="/ssadmin/ctctradeList.html";
                                    }, 
                                    	2000);
                                }else{
                                    layer.msg('放行成功,订单状态修改出错!');
                                }
                            },
                            error: function (data) {
                                layer.msg("上传错误");
                            }
                        });
                    }else{
                        layer.msg(data.msg);
                    }
                },
                error: function (data) {
                    layer.msg("上传错误");
                }
            });
        })
        
        
        $("#qx").click(function(){
            var orderId = '${orderId}';
            $.ajax({
                url : "/ssadmin/confirmOrder2.html",
                data : {
                    "orderNo":orderId
                },
                type: "post",
                dataType: "json",
                secureuri: false,
                async : true,
                success: function (data) {
                    if(data.code == 200){
                        layer.msg(data.msg);
                        setTimeout(function(){
                            window.location.href="/ssadmin/ctctradeList.html";
                        }, 
                            2000);
                    }else{
                    	
                        layer.msg(data.msg);
                    }
                },
                error: function (data) {
                    layer.msg("上传错误");
                }
            });
        })
    })
</script>
</body>

</html>