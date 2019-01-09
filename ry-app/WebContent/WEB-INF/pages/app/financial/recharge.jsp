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
        <title>转入余额</title>
        <link rel="stylesheet" href="/static/front/app/css/css.css" />
        <link rel="stylesheet" href="/static/front/app/css/style.css" />
        <link rel="stylesheet" href="/static/front/app/css/swiper-3.4.2.min.css" />
        <script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js" ></script>
        <script type="text/javascript" src="/static/front/app/js/swiper-3.4.2.jquery.min.js" ></script>
        
        <style>
            .swiper-pagination-bullet{ width: 6px; height: 6px; border-radius: 3px;}
            .swiper-pagination-bullet-active{ width: 1.25rem; height: 6px; border-radius: 3px;}
            .swiper-slide a{ display: inline-block; width: 100%; height: 100%;}
            .list_cont ul li{ position:relative;}
            .list_cont ul li a{ position:absolute; left:0;
                top:0; width: 100%; height: 100%; z-index: 2;}
            .top .fl a img {
                width:auto;
                height: 2.2em;
            }
            body{
                background:#fff;
            }
            .navigation ul li{
                text-align: center;
            }
            .tradeClick{
                display: flex;
                justify-content: space-between;
            }
            #list_cont .fl img{
                width: 2rem;
            }
            #list_cont .fl{
                padding-top: .7rem;
            }
            #list_cont li>div{
                width: 33.33%;
            }
            #list_cont span{
                display: inline-block;
                width: auto;    
            }
            #list_cont .coin_name{
                height: 100%;
                line-height: 1.3rem;
                margin-top: 0.3rem;
            }
            #list_cont .coin_jiaoyi{
                margin-top: .17rem;
                margin-right: 1rem;
                font-size: .7rem;
                color: #999;
            }
            .green{
                color:#0aa623;
            }
            #convertInto_RMB{
                font-size: .7rem;
                color: #999;
            }
            .list_cont{
                border-top: 0;
                border-bottom: 0;
                padding-top: 0.4rem;
                padding-bottom: .4rem;
            }
            .fid-num{
                display: inline-block;
                width: 5rem;
                height: 2rem;
                line-height: 2rem;
                text-align:center;
                color: #fff;
                border-radius: 0.2rem;
                -webkit-border-radius: 0.2rem;
            }
            .Minus{
                background: #03a473;
            }
            .list_bar span {
                margin-right: 2rem;
                font-weight: 600;
            }
            .add{
                background: #e76d42;
            }
            .fontWeight{
                font-size: 1rem !important;
                font-weight: bold !important;
            }
            .list_cont ul li{
                border-bottom: 0;
            }
            .radio {
                background-size: 1.15rem;
                background-position: 3%;
                width: 100%;
                height: 2.5rem;
                line-height: 2.5rem;
                position: fixed;
                bottom: 3.06rem;
                left: 0;
                border-bottom: 0;
                background-color: #FAEDBD;
                z-index: 100;
            }
            .notice_active li {
                line-height: 2.5rem;
            }
            .notice_active li a,.more a{
                font-size: 0.7rem;
                opacity: 0.8;
            }
            
            .banner {
                height: 9rem;
            }
            .cont-padding {
                padding-bottom: 11rem !important;
            }
            #convertInto_RMB{
                height: 1.5rem;
                line-height: 1.5rem;
            }
            .c2c_box{
                width: 100%;
                height: 5rem;
                position: fixed;
                left: 0;
                bottom: 6rem;
                padding: 0 .62rem;
                box-sizing: border-box;
                -webkit-box-sizing: border-box;
                -moz-box-sizing: border-box;
                -ms-box-sizing: border-box;
                -o-box-sizing: border-box;
            }
            .c2c_box a,.c2c_box a img{
                display: inline-block;
                width: 100%;
                height: 100%;
            }
            .munu ul li{ 
                line-height: 1.8rem;
            }
            .munu ul li a{
                padding-top:1.35rem;
            }
            .numInfo{
                background:#1db3b4;
                padding:0.2rem 0;
            }
            .numInfo>span{
                 display:block;
                 background:#fff;
                 width:95%;
                 heigh:1rem;
                 margin:1rem auto;
                 line-heigh:1rem;
                 padding:1rem;
                 border-radius:0.5rem;
            }
            .mumu{
                 display:flex;
                 flex-flow:wrap row;
                 justify-content:center;
                 width:100%;
                 margin-top: 10px;
                 padding-bottom:3.8rem;
            }
            .mumu li{
                 flex:0 0 33.3%;
                 height:6rem;
                 background:#fff;
            }
            .mumu li a{
                 width:100%;
                 height:100%;
                 display:flex;
                 flex-flow:nowrap column;
                 justify-content:space-around;
                 align-items:center;
            }
            .mumu li a i{
                 width:2rem;
                 height:2rem;
            }
            .mumu li:nth-of-type(1) a i{
                 background:url('/static/front/app/images/icon_add.png') no-repeat center;
                 background-size:100% auto;
            }
            .mumu li a span{
                text-align:center;
            }
            .radio{
                 position:initial;
                 margin-top: 5px;
            }
            .cardInfo{
                padding:1rem 0;
            }
            .bankCard{
                background: #efefef;
                width: 90%;
                margin: 0.5rem auto;
                border-radius: 0.677rem;
                padding: 1rem;
            }
            .bankCard>div{
                display: flex;
                flex-flow: nowrap row;
                align-items: center;
                margin-bottom: 1rem;
            }
            .bankCard img{
                width:3rem;
                margin-right: 1rem;
                height:3rem;
            }
            .bankCard h3{
                line-height: 1.5rem;
                font-size: 1.3rem;
            }
            .bankCard>p{
                line-height: 1.5rem;
                font-size: 1rem;
                padding-left: 15%;
            }
            .mumu li{
                flex:1;
            }
            .mumu li span{
                display:block;
                width:100%;
                color:#666;
            }
            .nextBtn {
                display: block;
                width: 90%;
                text-align: center;
                line-height: 3rem;
                background: #efefef;
                margin: 2rem auto;
                background:#FFBBFF;
            }
            .fillCard{
                height: 2.5rem;
                display: flex;
                align-items: center;
                padding: 0 1rem;
                border-top: 1px solid #3333;
                border-bottom: 1px solid #3333;
           }
        </style>
    </head>
    <body>
        <nav>
            <div class="Personal-title">
                <span>
                    <a href="/m/personal/assets.html?menuFlag=assets">
                        <em>
                            <i></i>
                            <i></i>
                        </em>
                        <strong>返回</strong>
                    </a>
                </span>转入余额
            </div>
        </nav>
        <c:if test="${add}">
            <ul class="mumu">
                <li ><a href="${oss_url}/m/add/bankCard.html"><i></i><span>您还未添加银行卡，请点击添加</span></a></li>
            </ul>
        </c:if>
        <c:if test="${!add}">
            <div class='cardInfo'>
                    <p>请选择银行卡</p>
                    <div class='fillCard'>
                        <select id="bankCard">
                            <c:forEach items="${bankCard }" var="bankCard">
                                <option value="${bankCard.fId}">${bankCard.bankName} **** ${bankCard.cardNum}</option>
                            </c:forEach>
                        </select>
                    </div>
            </div>
            <p>输入金额 (元)</p>
            <div class='fillCard'>
                <span>￥</span><input type="number" id="money" name="money" placeholder="请输入金额(每笔最低100元，最高49999.99元)">
            </div>
            <p>正常服务时间为9:30-21:30，充值后正常60分钟内到账，在服务时间外充值，正常情况下次日9:30到账。</p>
            <span class="nextBtn" onclick="recharge()">确认转入</span>
        </c:if>
<script type="text/javascript">
</script>
<script type="text/javascript" src="${oss_url}/static/front/js/comm/util.js"></script>
<script type="text/javascript" src="/static/front/app/js/index.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/plugin/layer/layer.js"></script>
<script type="text/javascript">
function recharge() {
    if(${add}){
        layer.msg('请添加银行卡！');
        return;
    }
    
    var money=+$("#money").val();
    if(money<100||money>49999.99){
        layer.msg('每笔最低100元，最高49999.99元');
        return;
    }
    var bankCard=$("#bankCard").val();
    //FIXME 请求
    
}

$(function(){
    var msg = '${msg}';
    if(msg != null && msg != ''){
        layer.msg(msg);
    }
})
function iphoneX(type){
    if(type==iphoneX){
        $(".munu").css("height","4.5rem");
        $(".radio").css("bottom","4.5rem");
    }else{
        $(".munu").css("height","3.06rem");
        $(".radio").css("bottom","3.06rem");
    }
}
</script>
</body>
</html>
