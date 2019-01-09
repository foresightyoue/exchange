<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@include file="../../front/comm/include.inc.jsp"%>
<%
     String path = request.getContextPath();
         String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
         + path;
%>
<!DOCTYPE html>
<html style="font-size: 16px;">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <base href="${basePath}"/>
    <title>提交申诉申请</title>
    <link href="/static/front/app/js/c2c/common.css" rel="stylesheet">
    <link href="/static/front/app/js/c2c/otc-app.css" rel="stylesheet">
    <style>
        /* .add_bgImg{
            position: relative;
        } */
        .appeal_showImg{
            width: 100%;
            height: 100%;
        }
        .appeal_add_bgImg {
            display: inline-block;
            width: 30%;
            height: 1.5rem;
            margin-right: .14rem;
        }
    </style>
</head>
<body>
<div class="header">
    <a href="javascript:history.go(-1);" class="prev"></a>
    <div class="head_title font15">申诉</div>
    <!--<div class="more more-z">设置</div>-->
</div>
<div id="mat"></div>
<form action="" class="appeal">
    <div class="title_hint font13 pt_0">请描述你遇到的问题（必填）</div>
    <div class="text_box">
        <textarea id="remark" name="remark" placeholder="请输入不少于20字的备注。" class="textarea border font13"></textarea>
    </div>
    <div class="title_hint font13 pt_0">增加申诉图片，以便客服更便捷的处理申诉</div>
    <div class="add_img">
        <label for="img1" class="appeal_add_bgImg">
            <input type="file" id="img1"  name='myfile' onchange="fileUpload('img1')" class="add_file">
            <img src="/static/front/images/addimg.png" id="echoImg1" class="appeal_showImg">
        </label>
        <label for="img2" class="appeal_add_bgImg">
            <input type="file" id="img2"  name='myfile' onchange="fileUpload('img2')" class="add_file">
            <img src="/static/front/images/addimg.png"  id="echoImg2" class="appeal_showImg">
        </label>
        <label for="img3" class="appeal_add_bgImg">
            <input type="file" id="img3"  name='myfile' onchange="fileUpload('img3')" class="add_file">
            <img src="/static/front/images/addimg.png" id="echoImg3" class="appeal_showImg">
        </label>
    </div>
    <div class="btn_box input-cont mt_20">
        <button id="submit" type="button" class="btn_style_1 border font15">提交申诉</button>
    </div>
    <input type='hidden' id="path1" name='path1' value=""/>
    <input type='hidden' id="path2" name='path2' value=""/>
    <input type='hidden' id="path3" name='path3' value=""/>
    <input type='hidden' id="orderId" name='orderId' value="${orderId}"/>
    <input type='hidden' id="fw" name='fw' value="${fw}"/>
    <input type='hidden' id="coin_id" name="coin_id" value="${coin_id}"/>
</form>
<%-- <div class="container">
    <div class="bk-tabList">
       <a class="btn card-admin" role="button" href="/m/otc/index.html" style="color: #666" ><img alt="" src="/static/front/images/aui-icon-back.png"> 返回</a>
    </div>
    <div class="row">
        <div class="col-md-10" style="background: white;">
            <div class="order_details panel panel-default">
                <div class="panel-heading">提交申诉申请</div>
                <form action="/m/otc/addAppeal" method="post">
                    <label class="img_hint">增加申诉图片，以便客服更便捷的处理申诉:</label>
                    <div class="clearfix" style="padding-bottom: 10px;">
                        <div class="imgBox">
                            <input class='myfile' type='file' id="img1"  name='myfile' onchange="fileUpload('img1')" value=''/>
                            <img src="/static/front/images/addimg.png" id="echoImg1" style="width:220px;height:154px">
                        </div>
                        <div class="imgBox">
                            <input class='myfile' type='file' id="img2"  name='myfile' onchange="fileUpload('img2')" value=''/>
                            <img src="/static/front/images/addimg.png" id="echoImg2" style="width:220px;height:154px">
                        </div>
                        <div class="imgBox">
                            <input class='myfile' type='file' id="img3"  name='myfile' onchange="fileUpload('img3')" value=''/>
                            <img src="/static/front/images/addimg.png" id="echoImg3" style="width:220px;height:154px">
                        </div>
                    </div>
                    
                    <input type='hidden' id="path1" name='path1' value=""/>
                    <input type='hidden' id="path2" name='path2' value=""/>
                    <input type='hidden' id="path3" name='path3' value=""/>
                    <input type='hidden' id="orderId" name='orderId' value="${orderId}"/>
                    <input type='hidden' id="fw" name='fw' value="${fw}"/>
                    
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
</div> --%>
<script src="/static/front/js/c2c/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="/static/front/app/js/c2c/rem1.js"></script>
<script src="/static/front/js/plugin/ajaxfileupload.js"></script>
<script src="/static/front/js/upload/imgCheck.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/plugin/layer/layer.js"></script>
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
                url : "/m/otc/addAppeal.html",
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
                        	window.location.href="/m/otc/orderList.html?coinType="+coin_id+"&fw=0";
                        }else{
                        	window.location.href="/m/otc/orderShellList.html?coinType="+coin_id+"&fw=0";
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