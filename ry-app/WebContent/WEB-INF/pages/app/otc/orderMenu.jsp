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
    <title>订单中心</title>
    <link rel="stylesheet" href="/static/front/app/css/css.css" />
    <link rel="stylesheet" href="/static/front/app/css/style.css" />
    <link rel="stylesheet" href="/static/front/app/css/swiper-3.4.2.min.css" />
    <script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js" ></script>
    <script type="text/javascript" src="/static/front/app/js/swiper-3.4.2.jquery.min.js" ></script>
    <style type="text/css">
        .exit_btn ul{
            height: 3.06rem;
            line-height: 3.06rem;
            font-size: 0.87rem;
            border-radius: 0.15rem;
            text-align: center;
            margin: 4rem 0.62rem;
            background: #186ed5;
            color: #fff;
        }
        .exit_btn{
            padding-left: 0;
            background-color: transparent;
            border: 0;
        }
        
        .bg-img01 ul li:nth-child(1){ background: url(${oss_url}/static/front/app/images/img002.png) no-repeat;background-size: 1.25rem; background-position: center left;}
        .bg-img01 ul li:nth-child(2){ background: url(${oss_url}/static/front/app/images/img001.png) no-repeat;background-size: 1.25rem; background-position: center left;}
        .user-list ul li img{
         	float:right;
         	width:0.8rem;
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
                    <a href="/m/otc/index.html?coinType=${coin_id}">
                        <em>
                            <i></i>
                            <i></i>
                        </em>
                        <strong><!-- 返回 --></strong>
                    </a>
                </span>
        订单分类
    </div>
</nav>
<section>
    <div class="user-list clearfix bg-img01">
        <ul>
            <li>
               <a href="/m/otc/orderList.html?coinType=${coin_id}"></a>
               <p>我的买卖订单</p>
               <img src='/static/front/app/images/right.png' class=''>
            </li>
            
             <li>
               <a href="/m/otc/orderShellList.html?coinType=${coin_id}"></a>
               <p>我的广告订单</p>
               <img src='/static/front/app/images/right.png' class=''>
            </li>
        </ul>
        <input type="hidden" class="coin_id" name="coin_id" value="${coin_id}">
    </div>
   
</section>
<%-- <jsp:include page="../comm/menu.jsp"></jsp:include> --%>
</body>
<script type="text/javascript" src="/static/front/js/comm/util.js"></script>
<script type="text/javascript" src="/static/front/js/comm/comm.js"></script>
</html>