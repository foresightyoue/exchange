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
        <title>添加银行卡</title>	
        <link rel="stylesheet" href="/static/front/app/css/css.css" />
        <link rel="stylesheet" href="/static/front/app/css/style.css" />
        <link rel="stylesheet" href="/static/front/app/css/swiper-3.4.2.min.css" />
        <script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js" ></script>
        <script type="text/javascript" src="/static/front/app/js/swiper-3.4.2.jquery.min.js" ></script>
        <script type="text/javascript" src="/static/front/app/js/bankCardAttribution.js" ></script>
        
        <style>
           body{
                background:#efefef;
           }
           p{
                color: #6666;
                padding-left: 1rem;
                line-height: 2rem;
           }
           body p:first-of-type{
                font-size:0.9rem;
                margin-top: 0.5rem;
           }
           body p:last-of-type{
                font-size:0.7rem;
           }
           .fillCard{
                height: 3rem;
                display: flex;
                align-items: center;
                padding: 0 1rem;
                margin-bottom:0.2rem;
                background:#fff;
           }
           .fillCard span{
                font-size:0.9rem;
                flex:0 0 25%;
           }
           .fillCard input{
                outline: none;
                width: 60%;
                height: 1.5rem;
           }
           .fillCard select{
           		border:none;
           		flex:1;
           		color: #333;
           		background:transparent;
           }
           .fillCard input::-webkit-input-placeholder{
                color:#6666;
           }
           .nextBtn{
                display: block;
                width: 90%;
                text-align: center;
                line-height: 3rem;
                background: #cfcfcf;
                margin: 2rem auto;
                color: #fff;
           }
           .bankKind{
           		color:#333;
           		background:#fff;
           		border:none;
           		border-bottom:1px solid #efefef;	
           }
           .bankKind:hover{
           		background:#red;
           		color:#fff;
           }
           .Personal-title span em{
            width: 1.2rem;
            height: 1.2rem;
            left: 0.4rem;
            top:0.65rem;
            }
            .Personal-title span em i:last-child{
                left:0.22rem;
            }
        </style>
    </head>
    <body>
        <nav>
            <div class="Personal-title">
                <span>
                    <a href="javascript:history.go(-1);">
                        <em>
                            <i></i>
                            <i></i>
                        </em>
                        <strong><!-- 返回 --></strong>
                    </a>
                </span>添加银行卡
            </div>
        </nav>
            <div class='fillCard'>
                <span>姓名：</span><input id='hostName' type="text" name="cardName" placeholder="请输入姓名">
            </div>
            <div class='fillCard'>
                <span>开户银行：</span><select id='cardBank'><option disabled selected class='defaultSelect'>请选择开户银行</option><option class='bankKind'>中国银行</option><option class='bankKind'>农业银行</option><option class='bankKind'>工商银行</option><option class='bankKind'>建设银行</option><option class='bankKind'>招商银行</option><option class='bankKind'>平安银行</option><option class='bankKind'>中信银行</option><option class='bankKind'>邮政储蓄银行</option><option class='bankKind'>光大银行</option><option class='bankKind'>农村信用社银行</option><option class='bankKind'>广州发展银行</option><option class='bankKind'>浦发银行</option><option class='bankKind'>海峡银行</option><option class='bankKind'>华夏银行</option><option class='bankKind'>浙商银行</option><option class='bankKind'>汇丰银行</option><option class='bankKind'>恒生银行</option><option class='bankKind'>比特币</option><option class='bankKind'>莱特币</option><option class='bankKind'>新加坡星展银行</option><option class='bankKind'>华侨银行</option><option class='bankKind'>大华银行</option><option class='bankKind'>马来西亚银行</option><option class='bankKind'>联昌国际银行</option><option class='bankKind'>大众银行</option><option class='bankKind'>美国花旗银行</option><option class='bankKind'>瑞士银行</option><option class='bankKind'>泰国中央银行</option><option class='bankKind'>菲律宾中央银行</option><option class='bankKind'>日本银行</option><option class='bankKind'>韩亚银行</option><option class='bankKind'>渣打银行</option><option class='bankKind'>印度银行</option><option class='bankKind'>台湾银行</option></select>
            </div>
            <div class='fillCard'>
                <span>银行卡号：</span><input id='cardNum' type="text" onblur="ts()" oninput="value=value.replace(/[^\d]/g,'')" placeholder="请输入银行卡号">
            </div>
            <div class='fillCard'>
                <span>交易密码：</span><input id='password' type="password" placeholder="请输入交易密码">
            </div>
            
            <span class='nextBtn'>下一步</span>

<script type="text/javascript">
</script>
<script type="text/javascript" src="${oss_url}/static/front/js/comm/util.js"></script>
<script type="text/javascript" src="/static/front/app/js/index.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/plugin/layer/layer.js"></script>
<script type="text/javascript">
 $('#cardNum').on("input",function(){
    if($(this).val().length >= 9){
        $(".nextBtn").css({'background':"#EBA51D",color:'#fff'}).off("click").one("click",addBankCard);
    }else{
        console.log(11111);
        $(".nextBtn").css({'background':"#cfcfcf",color:'#fff'}).off("click");
    }
})

function ts(){
     if($("#cardNum").val().length<9){
         layer.msg("卡号位数不正确");
     }
 }

 $('.nextBtn').on('click',addBankCard);
function addBankCard() {
    var hostName=$("#hostName").val();
    var cardBank=$("#cardBank").val();
    var cardNum=$("#cardNum").val();
    var password=$("#password").val();
    if(""==cardNum){
        layer.msg("请输入银行卡号！");
        $('.nextBtn').one("click",addBankCard);
        return;
    }
    var info=bankCardAttribution(cardNum);
    if("error"==info){
        layer.msg("银行卡号不合法！");
        $('.nextBtn').one("click",addBankCard);
        return;
    }
    
    if("储蓄卡"!=info.cardTypeName){
        layer.msg('该交易不支持'+info.cardTypeName);
        $('.nextBtn').one("click",addBankCard);
        return;
    }
    
    if(hostName==''){
    	layer.msg('请输入姓名！');
    	$('.nextBtn').one("click",addBankCard);
    	return;
    }
    
    if(password=='' || password.length< 6){
    	layer.msg('密码长度不符！');
    	$('.nextBtn').one("click",addBankCard);
    	return;
    }
    
    var param = {
            cardNum : cardNum,
            bankName : info.bankName,
            bankCode : info.bankCode,
            password : password
        };
    $.ajax({
        url : "/m/addBankCard.html",
        type : 'post',
        data : param,
        dataType : 'json',
        success:function(data){
            if (data.code < 0) {
                layer.msg(data.msg);
                $('.nextBtn').one("click",addBankCard);
            } else {
                layer.msg(data.msg);
                setTimeout(function(){
                    window.location.href="/m/bank/card.html";
                },1000);
            }
        },
        error:function(){
            layer.msg(data.msg);
            $('.nextBtn').one("click",addBankCard);
        }
     });
}
</script>
</body>
</html>
