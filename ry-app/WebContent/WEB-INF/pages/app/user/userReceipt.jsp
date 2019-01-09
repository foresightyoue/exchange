<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat"
    pageEncoding="UTF-8"%>
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
    <title></title>
    <!-- <link rel="stylesheet" href="/static/front/css/c2c/safe.css">
    <link rel="stylesheet" href="/static/front/css/c2c/module.common.css">
    <link rel="stylesheet" href="/static/front/css/c2c/module.base.css"> 
    <link href="//netdna.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="//at.alicdn.com/t/font_48446_inzlc0gmji3sor.css" rel="stylesheet">-->
    <link href="/static/front/app/js/c2c/common.css" rel="stylesheet">
    <link href="/static/front/app/js/c2c/otc-app.css" rel="stylesheet">
    <style>
    .imgShow{
        width: 50%;
        height: 2.5rem;
        margin: 0 auto;
        background-image: url(/static/front/images/c2c/add_bg.png);
        background-size: 100% 100%;
        background-position: center;
        background-repeat: no-repeat;
        border: 1px dashed #99a59e;
    }
    .account_bgImg{
        display: none;
    }
    </style>
</head>
<body>
<div class="header">
    <a href="javascript:history.go(-1);" class="prev"></a>
    <div class="head_title font15"><c:if test="${ftype == '1' }">支付宝</c:if><c:if test="${ftype == '2' }">微信</c:if>设置</div>
    <!--<div class="more more-z">设置</div>-->
</div>
<div id="mat"></div>
<form id="formId" action="/m/otc/userReceipt.html" method="post">
    <input type="hidden" id="ftype" value="${ftype}" name="ftype"/>
    <input type="hidden" id="fuser_id" value="${fuserId}" name="fusr_id"/>
	<ul class="news_list pl_10 account_set">
	    <li class="pr_10">
	        <a href="javascript:void(0)">
	            <div class="flexLayout font15">
	                <div class="list_left font16">
	                    <span>收款名：</span>
	                </div>
	                <div class="list_right"><input type="text" id="fname" value="${frNew.items.fName }" name="fname" class="app_ipt_style font15" placeholder="输入您的收款名"></div>
	            </div>
	        </a>
	    </li>
	    <li class="pr_10 border_0">
	        <a href="javascript:void(0)">
	            <div class="flexLayout font15">
	                <div class="list_left font16">
	                    <span>收款账号：</span>
	                </div>
	                <div class="list_right"><input type="text" id="faccount" value="${frNew.items.fAccount }" name="faccount" class="app_ipt_style font15" placeholder="输入您的收款账户"></div>
	            </div>
	        </a>
	    </li>
	</ul>
	<div class="title_hint pt_0 font14 pl_10" style="border-top: .17rem solid #F5F5F5;">上传收款二维码/账户信息</div>
	<div class="account_img tc border_0">
	     
         <div class="imgShow" style="background-image:url('${frNew.items.fImgUrl}')">
          <input id="pic1Url" type="hidden" name="fimgurl">
          <c:if test="${!empty frNew.items.fImgUrl}"><img src="${frNew.items.fImgUrl }" alt="" class="account_bgImg"></c:if>
         </div>
	    <label for="pic1" class="account_btn font14">上传图片</label>
	    <input id="pic1" type="file" onchange="uploadImg()" class="file optional verification-image add_file">
	    <!-- <div class="title_hint tc pt_0 font13 pl_0">上传收款二维码/账户信息</div> -->
	</div>
	<div class="btn_box input-cont mt_20">
		<c:if test="${frNew.items.fName == null && frNew.items.fAccount == null && frNew.items.fImgUrl == null }">
	    	<button id="sub_btn" class="btn_style_1 border font15" type="button" name="commit">提交</button>
		</c:if>
		<c:if test="${frNew.items.fName != null || frNew.items.fAccount != null || frNew.items.fImgUrl != null }">
	    	<button id="sub_btn" class="btn_style_1 border font15" type="button" name="commit">修改</button>
		</c:if>
	</div>
</form>
<%-- <div class="bk-onekey financen">
    <div class="container">
        <div class="finance-rd">
            <div class="bk-tabList">
                <div class="bk-c2c-nav bk-band clearfix">
                    <a href="javascript:void(0)" class="active main-nav">收款账号设置</a>
                    <a class="btn card-admin" role="button" href="/m/otc/index.html" style="color: #666" ><img alt="" src="/static/front/images/aui-icon-back.png"> 返回</a>
                </div>
                <div class="payment-settings-box">
                    <div class="row">
                        <div class="col-md-12">
                            <h4 class="mb_20">收款方式：<c:if test="${ftype == '0' }">银行转账</c:if><c:if test="${ftype == '1' }">支付宝</c:if><c:if test="${ftype == '2' }">微信</c:if></h4>
                        </div>
                        <div class="col-md-10">
                            <form id="formId" action="/m/otc/userReceipt.html" method="post">
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
                                <c:if test="${! empty frNew.items.fImgUrl }"><img src="${frNew.items.fImgUrl }" style="padding: 5px; height: 50px; width: 50px;"></c:if>
                                <input id="pic1Url" type="hidden" value="${frNew.items.fImgUrl }" name="fimgurl">
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
</div>  --%>
<script src="/static/front/js/c2c/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="/static/front/app/js/c2c/rem1.js"></script>
<!-- <script src="http://libs.baidu.com/jquery/2.0.0/jquery.min.js"></script> -->
<script type="text/javascript" src="${oss_url}/static/front/js/plugin/ajaxfileupload.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/plugin/fileCheck.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/comm/msg.js"></script>
<!-- <script type="text/javascript" src="${oss_url}/static/front/js/finance/city.min.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/finance/jquery.cityselect.js"></script> -->
<script type="text/javascript" src="${oss_url}/static/front/js/plugin/jquery.qrcode.min.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/plugin/jquery.autocomplete.min.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/user/kyc.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/user/kyct.js"></script>
<script src="/static/front/js/c2c/jquery.SuperSlide.2.1.1.js"></script>
<script type="text/javascript" src="/static/front/js/layer/layer.js"></script>
<script src="/static/front/js/comm/util.js"></script>
<script>
        function imgbakc(resultUrl) {
        	$("#pic1Url").parent().css({"background-image":"url("+resultUrl+")"});
            $("#pic1Url").val(resultUrl);
        }
        
        $(function(){
            $("#sub_btn").click(function(){
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
                    if(faccount == ""){
                        layer.close(index);
                        layer.msg("请填写收款账户");
                        return;
                    }
                    if(fbankname == ""){
                        layer.close(index);
                        layer.msg("请填写银行名称");
                        return;
                    }
                    if(fbanknamez == ""){
                        layer.close(index);
                        layer.msg("请填写银行支行名称");
                        return;
                    }
                    $.ajax({
                    	url:"/m/otc/userReceipt.html",
                    	data:{"fname":fname,"fuser_id":fuser_id,"faccount":faccount,"pic1Url":pic1Url,"fbankname":fbankname,"fbanknamez":fbanknamez,"ftype":ftype},
                    	type:"post",
                        dataType: "json",
                        secureuri: false,
                        async : true,
                        success:function(data){
                            if(data.code==0){
                               window.location.href="/m/bank/card.html";
                            } else{
                                layer.closeAll();
                                layer.msg("添加失败");
                            }
                        },
                        error: function(){
                            layer.closeAll();
                            layer.msg("网络异常，请稍后重试！");
                            setTimeout(function(){location.reload();},2000)
                        }
                    })
//                     $("#formId").submit();
                    //layer.closeAll();
                    flag = false;
                }else{
                    layer.closeAll();
                    layer.msg('尚未提交收款二维码图片！');
                }
            });
        })
        
        function uploadImg() {
            if (checkFileType('pic1', 'img')) {
                fileUpload("/otc/upload.html", "4", "pic1", "pic1Url", null, null, imgbakc, "resultUrl");
            }
        }
    </script>
</body>
</html>