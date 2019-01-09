<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat"
    pageEncoding="UTF-8"%>
<%@ include file="../comm/include.inc.jsp"%>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <base href="${basePath}"/>
    <%@include file="../comm/link.inc.jsp"%>
    <title></title>
    <link rel="stylesheet" href="/static/front/css/c2c/safe.css">
    <link rel="stylesheet" href="/static/front/css/c2c/module.common.css">
    <link rel="stylesheet" href="/static/front/css/c2c/module.base.css">

    <link href="//netdna.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="//at.alicdn.com/t/font_48446_inzlc0gmji3sor.css" rel="stylesheet">
    <style type="text/css">
        .top-fixed-nav {
            position: fixed;
            left: 0;
            top: 0;
            z-index: 100;
        }
        .exchangetlist .status5 {
            width: 25%;
            display: inline-block;
            text-align: right;
            color: #333;
            float: right;
        }
        .layui-layer{
            top: 50% !important;
            /* margin-top: -120px !important;    */
        }
        input::-webkit-outer-spin-button, input::-webkit-inner-spin-button {
            -webkit-appearance: none !important;
            margin: 0;
        }
        input[type="number"]{ 
            -moz-appearance:textfield;
        }
        .alertContent{
            width: 40%;
            min-width: 420px;
            /* height: 265px; */
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%,-50%);
            background-color: white;
            /* z-index: 100; */
            border-radius:3px;
            -webkit-border-radius:3px;
            color: #333;
            padding: 10px  20px 20px;
            line-height: 30px;
            text-align: left;
        }
        .alertContent p span{
            display: inline-block;
            width: 150px;
            text-align: right;
            margin-right: 10px;
            background-color: rgba(255,152,0,.1);
            border-right: 1px solid #FF9800; 
            padding-right: 10px;
            color: #333;
        }
        .alertContent p .text-danger{
            border: 0;
            background-color: transparent;
            text-align: left;
        }
        .alertContent p{
            border: 1px solid #FF9800; 
            border-bottom: 0;
            margin-bottom: 0;
        }
        .message_hint{
            font-size: 13px;
            color: #808080;
            line-height: 30px;
        }
        .message_hint2{
            font-size: 13px;
            color: #808080;
            line-height: 30px;
            border-top: 1px solid #e6e6e6; 
        }
        .message_title{
            font-size: 18px;
            margin-bottom: 10px;
            line-height: 30px;
            height: 30px;
        }
        .error{
            position: absolute;
            width: 25px;
            top: 5px;
            right: 10px;
        }
        .kown{
            height: 28px;
            line-height: 25px;
            border: 0;
            background-color: #FF9800;
            color: white;
            padding: 0 10px;
            margin-top: 10px;
        }
        .text-danger{
            color: red !important;
        }
        .exchangetlist .clone{
            position: relative;
        }
        .exchangetlist .clone .typeshow{
            position: absolute;
            left: 59%;
        }
        .table>thead>tr>th, .table>tbody>tr>th, .table>tfoot>tr>th, .table>thead>tr>td, .table>tbody>tr>td, .table>tfoot>tr>td {
            font-size: 14px;
            
        }
        .table>thead>tr>th,.table>tbody>tr>td{
            text-align: left;
        }
        .bk-c2c-nav {
            background: #fff;
            border-bottom: 1px solid #e6e6e6;
        }
        .bk-c2c-nav a, .bk-c2c-nav span{
            height: 70px;
            line-height: 70px;
        }
        .alert-info {
            background-color: #d9edf7;
            border-color: #bce8f1;
            color: #31708f;
            padding: 15px 15px 10px 30px;
            font-size: 14px;
            line-height: 25px;
            border-radius: 5px;
            padding: 15px 15px 10px 30px;
            margin: 20px;
            text-align: left;
        }
        #exchangeRecord .tl{
            text-align: left;
        }
        .tl a{
            color: #1db3b4;
        }
        body{
            font-size: 14px;
            text-align: left;
            color: #666;
        }
        .payment-settings-box .verification-image {
            border: 1.5px solid #ECECEC;
            padding: 10px;
            width: 100%;
            border-radius: 4px;
        }
        .payment-settings-box .btn-theme {
            margin-top: 15px;
            width: 100%;
            height: 40px;
            box-shadow: 0 2px 4px 0 #1db3b4;
            color: white;
            background-color: #1db3b4;
        }
        .font-w{
            font-weight: 600;
        }
        #fname,#faccount,#fbankname,#fbanknamez{
            width: 200px;
            height: 30px;
            padding-left: 15px;
            border: 1px solid #e6e6e6;
            border-radius: 4px;
            line-height: 30px;
        }
        .ipt_hint{
            display: inline-block;
            width: 100px;
        }
        .border_b{
            border-bottom: 1px solid #e6e6e6;
        }
        .payment_setting .border_b{
            padding-bottom: 10px;
        }
        .account_message{
            padding: 10px 0 20px;
            line-height: 50px;
        }
        .pb_10{
            padding-bottom: 10px;
        }
        .git_code{
            padding: 15px 0;
        }
        .mb_20{
            margin-bottom: 20px;
        }
    </style>
</head>
<%@include file="../comm/header.jsp"%> 
<body>
<div class="bk-onekey financen " style="padding-top: 50px;">
    <div class="container">
        <div class="finance-rd" style="width:100%; margin-left:0;">
            <div class="bk-tabList">
                <div class="bk-c2c-nav bk-band clearfix">
                    <a href="javascript:void(0)" class="active main-nav">收款账号设置</a>
                    <a class="btn card-admin" role="button" href="/otc/index.html?coin_id=${coin_id}" style="color: #666" ><img alt="" src="/static/front/images/aui-icon-back.png"> 返回</a>
                </div>
                <div class="payment-settings-box">
                    <div class="row">
                        <div class="col-md-12">
                            <h4 class="mb_20">收款方式：<c:if test="${ftype == '0' }">银行转账</c:if><c:if test="${ftype == '1' }">支付宝</c:if><c:if test="${ftype == '2' }">微信</c:if></h4>
                        </div>
                        <div class="col-md-10">
                            <form id="formId" action="/otc/userReceipt.html" method="post">
                            <input type="hidden" id="ftype" value="${ftype}" name="ftype"/>
                            <input type="hidden" id="fuser_id" value="${fuserId}" name="fusr_id"/><br/>
                                <div class="payment_setting">
                                    <div class="border_b"><strong>账号信息</strong></div>
                                    <div class="account_message">
                                        <span class="ipt_hint">收款名：</span><input type="text" id="fname" value="${frNew.items.fName }" name="fname"/><br/>
                                        <span class="ipt_hint">收款账户：</span><input type="text" id="faccount" value="${frNew.items.fAccount }" name="faccount"/><br/>
                                        <c:if test="${frNew.items.fType == 0 || ying == 0}">
                                            <span class="ipt_hint">银行名称：</span><input type="text" id="fbankname" value="${frNew.items.fBankname }" name="fbankname"/><br/>
                                            <span class="ipt_hint">银行支行名称：</span><input type="text" id="fbanknamez" value="${frNew.items.fBanknamez }" name="fbanknamez"/><br/>
                                        </c:if>
                                    </div>
                                </div>
                                <div class="pb_10">上传收款二维码 / 账户信息</div>
                                <input id="pic1" type="file" onchange="uploadImg()" class="file optional verification-image">
                                <c:if test="${! empty frNew.items.fImgUrl }"><img src="${frNew.items.fImgUrl }"></c:if>
                                <input id="pic1Url" type="hidden" value="${frNew.items.fImgUrl }" name="fimgurl">
                                <input type="hidden" id="coin_id" name="coin_id" value="${coin_id}"/>
                                <!-- <a target="_blank" href="https://support.otcbtc.com/hc/zh-cn/articles/115002541231">如何获取支付宝收款码？</a> -->
<!--                                 <div class="git_code"><a href="javascript:void();">如何获取支付宝收款码？</a></div> -->
                                <!-- <div class="form-group boolean optional payment_setting_display_in_order"><div class="checkbox"><input value="0" type="hidden" name="payment_setting[display_in_order]"><label class="boolean optional" for="payment_setting_display_in_order"><input class="boolean optional" type="checkbox" value="1" checked="checked" name="payment_setting[display_in_order]" id="payment_setting_display_in_order">展示在订单页面</label></div></div> -->
                                <input type="button" name="commit" value="提交" class="btn btn-theme" id="btn">
                            </form>   
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div> 
<script src="/static/front/js/c2c/jquery-1.11.3.min.js"></script>
<!-- <script src="http://libs.baidu.com/jquery/2.0.0/jquery.min.js"></script> -->
<script type="text/javascript" src="${oss_url}/static/front/js/plugin/ajaxfileupload.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/plugin/fileCheck.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/comm/msg.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/finance/city.min.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/finance/jquery.cityselect.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/plugin/jquery.qrcode.min.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/plugin/jquery.autocomplete.min.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/user/kyc.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/user/kyct.js"></script>
<%@include file="../comm/footer.jsp"%>
<script src="/static/front/js/c2c/jquery.SuperSlide.2.1.1.js"></script>
<script src="/static/front/js/comm/util.js"></script>
<script>
        function imgbakc(resultUrl) {
            $("#pic1Url").val(resultUrl);
        }
        
        $(function(){
            $("#btn").click(function(){
                var index = layer.load(1);
                console.log("flag="+flag);
                //防止图片没有上传就提交了
                if($("#pic1Url").val() != ""){
                    
                    //var fid = $("#fid").val();//id
                    var fname = $("#fname").val();//用户名
                    var ftype = $("#ftype").val();//用户名
                    var fuser_id = $("#fuser_id").val();//用户账户id
                    var faccount = $("#faccount").val();//收款账户
                    var pic1Url = $("#pic1Url").val();//收款二维码
                    var fbankname = $("#fbankname").val();
                    var fbanknamez = $("#fbanknamez").val();
                    var coin_id = $("#coin_id").val();
                    if(faccount == ""){
                        layer.close(index);
                        alert("请填写收款账户");
                        return;
                    }
                    if(fbankname == ""){
                        layer.close(index);
                        alert("请填写银行名称");
                        return;
                    }
                    if(fbanknamez == ""){
                        layer.close(index);
                        alert("请填写银行支行名称");
                        return;
                    }
                    $.ajax({
                    	url:"/otc/userReceipt.html",
                    	data:{"fname":fname,"fuser_id":fuser_id,"faccount":faccount,"pic1Url":pic1Url,"fbankname":fbankname,"fbanknamez":fbanknamez,"ftype":ftype},
                    	type:"post",
                        dataType: "json",
                        secureuri: false,
                        async : true,
                        success:function(data){
                            if(data.code==0){
                               window.location.href="/otc/userReceiptOption.html?coinType="+coin_id;
               	            	layer.close();
                            } else{
                                alert("添加失败");
                                layer.close();
                            }
                        },
                        error: function(){
                            alert("网络异常，请稍后重试！");
                            layer.close();
                        }
                    })
//                     $("#formId").submit();
                    layer.close();
                    flag = false;
                }
            });
        })
        
        function uploadImg() {
            if (checkFileType('pic1', 'img')) {
                fileUpload("/otc/upload.html", "4", "pic1", "pic1Url", null, null, imgbakc, "resultUrl");
            }
        }
    </script>
<%-- <head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>个人收款账户</title>
</head>
<script src="http://libs.baidu.com/jquery/2.0.0/jquery.min.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/plugin/ajaxfileupload.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/plugin/fileCheck.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/comm/msg.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/finance/city.min.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/finance/jquery.cityselect.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/plugin/jquery.qrcode.min.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/plugin/jquery.autocomplete.min.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/user/kyc.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/user/kyct.js"></script>
<body>
    <form id="formId" action="/otc/userReceipt.html" method="post">
        <input type="hidden" id="fid" value="${fr.items.fId}" name="fid"/><br/>
        <input type="hidden" id="fCreateTime" value="${fr.items.fCreateTime }" name="fCreateTime"/><br/>
        用户账户id：
        <c:if test="${fr.items.fUsr_id == null}">
            <input type="text" id="fuser_id" value="${fuserId}" name="fusr_id"/><br/>
        </c:if>
        <c:if test="${fr.items.fUsr_id != null}">
            <input type="text" id="fuser_id" value="${fr.items.fUsr_id }" name="fusr_id"/><br/>
        </c:if>
        收款类型：
        <c:if test="${fr.items.fType == 0 || fr.items.fType == null}">
            <input type="radio" <c:if test="${fr.items.fType == 0}">checked="checked"</c:if> value="0" name="ftype"/>银行卡<br/>
        </c:if>
        <c:if test="${fr.items.fType == 1 || fr.items.fType == null}">
            <input type="radio" <c:if test="${fr.items.fType == 1}">checked="checked"</c:if> value="1" name="ftype"/>支付宝<br/>
        </c:if>
        <c:if test="${fr.items.fType == 2 || fr.items.fType == null}">
            <input type="radio" <c:if test="${fr.items.fType == 2}">checked="checked"</c:if> value="2" name="ftype"/>微信<br/>
        </c:if>
        收款名：<input type="text" id="fname" value="${fr.items.fName }" name="fname"/><br/>
        收款账户：<input type="text" id="faccount" value="${fr.items.fAccount }" name="faccount"/><br/>
        收款账户二维码：
        <div>
            <input id="pic1" type="file" onchange="uploadImg()">
            <input id="pic1Url" type="hidden" value="${fr.items.fImgUrl }" name="fimgurl"> 
            <span class="s2"> 
                <span>未选择文件</span> 
                <em class="pic1name"></em>
            </span>
        </div><br/>
        <c:if test="${fr.items.fType == 0  || ying == 0}">
            银行名称：<input type="text" id="fbankname" value="${fr.items.fBankname }" name="fbankname"/><br/>
            银行支行名称：<input type="text" id="fbanknamez" value="${fr.items.fBanknamez }" name="fbanknamez"/><br/>
        </c:if>
        <input type="button" id="btn" value="提交"/> 
    </form>
</body>
    <script>
        function uploadImg() {
            if (checkFileType('pic1', 'img')) {
                fileUpload("/otc/upload.html", "4", "pic1", "pic1Url", null, null, imgbakc, "resultUrl");
            }
        }
        
        function imgbakc(resultUrl) {
            $("#pic1Url").val(resultUrl);
        }
        $(function(){
            $("#btn").click(function(){
                console.log("flag="+flag);
                //防止图片没有上传就提交了
                if($("#pic1Url").val() != ""){
                    
                    //var fid = $("#fid").val();//id
                    var fuser_id = $("#fuser_id").val();//用户账户id
                    var faccount = $("#faccount").val();//收款账户
                    var pic1Url = $("#pic1Url").val();//收款二维码
                    var fbankname = $("#fbankname").val();
                    var fbanknamez = $("#fbanknamez").val();
                    
                    /* if(fid == ""){
                        alert("请填写id");
                        return;
                    } */
                    if(fuser_id == ""){
                        alert("请填写账户id");
                        return;
                    }
                    if(faccount == ""){
                        alert("请填写收款账户");
                        return;
                    }
                    if(fbankname == ""){
                        alert("请填写银行名称");
                        return;
                    }
                    if(fbanknamez == ""){
                        alert("请填写银行支行名称");
                        return;
                    }
                    
                    $("#formId").submit();
                    flag = false;
                }/* else{
                    alert("请上传二维码图片");
                } */
            });
        })
    </script> --%>
</body>
</html>