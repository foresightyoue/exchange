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
        <title>转账</title>
        <link rel="stylesheet" href="/static/front/app/css/css.css" />
        <link rel="stylesheet" href="/static/front/app/css/style.css" />
        <link rel="stylesheet" href="/static/front/app/css/swiper-3.4.2.min.css" />
        <script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js" ></script>
        <script type="text/javascript" src="/static/front/app/js/swiper-3.4.2.jquery.min.js" ></script>
        <script type="text/javascript" src="/static/front/app/js/bankCardAttribution.js" ></script>
        
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
           body{
                background:#fff;
           }
           p{
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
                height: 2.5rem;
                display: flex;
                align-items: center;
                padding: 0 1rem;
                border-top: 1px solid #3333;
                border-bottom: 1px solid #3333;
           }
           .fillCard span{
                font-size:1rem;
           }
           .fillCard input{
                border: 1px solid #6666;
                outline: none;
                width: 60%;
                height: 1.5rem;
           }
           .fillCard input::-webkit-input-placeholder{
                color:#6666;
           }
           .nextBtn{
                display: block;
                width: 90%;
                text-align: center;
                line-height: 3rem;
                background: #efefef;
                margin: 2rem auto;
                color: #fff;
                background:#8470FF;
           }
        </style>
    </head>
    <body>
        <nav>
            <div class="Personal-title">
                <span>
                    <a href="/m/ryb/rybWallet.html">
                        <em>
                            <i></i>
                            <i></i>
                        </em>
                        <strong>返回</strong>
                    </a>
                </span>转账
            </div>
        </nav>
            <p>收益区可自由流通RYB数量 000.00</p>
            <div class='fillCard'>
                <span>RYH数量</span><input type="number" id="ryh" name="ryh'" placeholder="输入交易区RYH数量">
            </div>
            <div class='fillCard'>
                <span>转给谁</span><input type="text" id="user" name="user'" placeholder="输入转账用户">
            </div>
            
            <span class='nextBtn'>确定</span>

        <jsp:include page="../comm/menu.jsp"></jsp:include>
<script type="text/javascript" src="${oss_url}/static/front/js/comm/util.js"></script>
<script type="text/javascript" src="/static/front/app/js/index.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/plugin/layer/layer.js"></script>
</body>
</html>
