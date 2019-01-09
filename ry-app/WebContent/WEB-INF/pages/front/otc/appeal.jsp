<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="../comm/include.inc.jsp"%>
<%@include file="../comm/link.inc.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<base href="${basePath }"/>
<link rel="stylesheet" href="/static/front/css/c2c/common.css">
<link rel="stylesheet" href="/static/front/css/c2c/app.css">
<title>提交申诉申请</title>
<style type="text/css">
.container .panel{
    border: 0;
    box-shadow: 0 0 0 0;
}
.img_hint{
    line-height: 50px;
}
.imgBox{
    width: 150px;
    height: 100px;
    margin-bottom: 15px;
    position: relative;
    float: left;
    margin-right: 15px;
}
.imgBox img{
    width: 100%;
    height: 100%;
}
.imgBox input{
    width: 100%;
    height: 100%;
    position: absolute;
    left: 0;
    top: 0;
    z-index: 10;
    filter:alpha(opacity=0);
    -moz-opacity:0;
    -khtml-opacity: 0;
    opacity: 0;
}
.clearfix:after{
    height:0;
    display:block;
    content:".";
    visibility:hidden;
    clear:both;
}
.clearfix{
    zoom:1;
}
.fl{
    float: left;
}
.fr{
    float: right;
}
#remark{
    width: 95%;
    height: 150px;
    border: 1px solid #e6e6e6;
}
.btn_group{
    text-align: center;
    margin-top: 30px;
}
.btn_group input{
    padding: 5px 15px;
    border: 0;
    outline: none;
    background-color: #1db3b4;
    
    color: white;
    border-radius: 4px;
    -webkit-border-radius: 4px;
    -o-border-radius: 4px;
    -ms-border-radius: 4px;
    -moz-border-radius: 4px;
}
footer.container-full{
    padding: 30px 0;
}
</style>
</head>
<body>
<%@include file="../comm/header.jsp"%> 
<div class="container">
    <div class="row">
        <%@include file="../comm/left_menu.jsp" %>
        <div class="col-md-10" style="background: white;">
            <div class="order_details panel panel-default">
                <div class="panel-heading">提交申诉申请</div>
                <form action="/otc/addAppeal" method="post">
                    <label class="img_hint">增加申诉图片，以便客服更便捷的处理申诉:</label>
                    <div class="clearfix" style="padding-bottom: 10px;">
                        <div class="imgBox">
                            <input class='myfile' type='file' id="img1"  name='myfile' onchange="fileUpload('img1')" value=''/>
                            <img src="/static/front/images/addimg.png" id="echoImg1">
                        </div>
                        <div class="imgBox">
                            <input class='myfile' type='file' id="img2"  name='myfile' onchange="fileUpload('img2')" value=''/>
                            <img src="/static/front/images/addimg.png" id="echoImg2">
                        </div>
                        <div class="imgBox">
                            <input class='myfile' type='file' id="img3"  name='myfile' onchange="fileUpload('img3')" value=''/>
                            <img src="/static/front/images/addimg.png" id="echoImg3">
                        </div>
                    </div>
                    
                    <input type='hidden' id="path1" name='path1' value=""/>
                    <input type='hidden' id="path2" name='path2' value=""/>
                    <input type='hidden' id="path3" name='path3' value=""/>
                    <input type='hidden' id="orderId" name='orderId' value="${orderId}"/>
                    <input type='hidden' id="fw" name='fw' value="${fw}"/>
                    <input type='hidden' id="coin_id" name='coin_id' value="${coin_id}">
                    
                    <div>
                        <label class="fl" style="margin-right: 10px;">备注:</label>
                        <textarea id="remark" name="remark" placeholder="请输入不少于20字的备注。"></textarea>
                        <div style="float: right;margin-right: 20px;">
                            <span id="overText">还可以输入</span>
                            <span id="sp">50</span>个字
                        </div>
                        <div class="btn_group"><input type="button" id="submit" value="提交申诉"/></div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<%@include file="../comm/footer.jsp"%>
<script src="/static/front/js/c2c/jquery-1.11.3.min.js"></script>
<script src="/static/front/js/plugin/ajaxfileupload.js"></script>
<script src="/static/front/js/upload/imgCheck.js"></script>
</body>
<script type="text/javascript">
	function GetQueryString(name) {
   		 var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    	 var r = window.location.search.substr(1).match(reg);
    	 if (r != null) return unescape(r[2]); return null;
	}
    $(function(){
        $("#submit").click(function(){
            var path1 = $("#path1").val();
            var path2 = $("#path2").val();
            var path3 = $("#path3").val();
            var orderId = $("#orderId").val();
            var remark = $("#remark").val();
            var coin_id =$("#coin_id").val();
            var fw = GetQueryString("fw");
            if(path1 == "" && path2 == "" && path3 ==""){
            	layer.msg("最少添加一张图片!");
            	return false;
            }
            if(remark.length <= 20){
            	layer.msg("备注少于20字!");
            	return false;
            }
            if(remark.length > 50){
                layer.msg("备注大于50字!");
                return false;
            }
            $.ajax({
                url : "/otc/addAppeal.html",
                data : {
                    "path1":path1,
                    "path2":path2,
                    "path3":path3,
                    "orderId":orderId,
                    "remark":remark,
                    "fw":fw
                },
                type: "post",
                dataType: "json",
                secureuri: false,
                async : true,
                success: function (data) {
                    if(data.code == 0){
                        layer.msg(data.msg);
                        if(fw == 0){
                        	window.location.href="/otc/orderList.html?coinType="+coin_id+"&fw=0";
                        }else{
                        	window.location.href="/otc/orderShellList.html?coinType="+coin_id+"&fw=0";
                        }
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
    
    $("#remark").keyup(function(){
        var a=$("#remark").val().length;
        var b=50-a;
        function abs(x){
            return x>0?x:-x;
        }
        if(b<0){
            $("#sp").css("color","red").html(abs(b));
            $("#overText").html("已超出")
        }else{
            $("#sp").css("color","black").html(abs(b));
            console.log(b);
            $("#overText").html("还可以输入")
        }
    });
</script>
</html>