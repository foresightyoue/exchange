<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@include file="../../front/comm/include.inc.jsp" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
%>

<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no, width=device-width">
    <meta name="format-detection" content="telephone=no">
    <meta name="format-detection" content="email=no">
    <base href="${basePath}"/>
    <title>我的海报</title>
    <link rel="stylesheet" href="/static/front/app/css/css.css"/>
    <link rel="stylesheet" href="/static/front/app/css/style.css"/>
    <script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js"></script>
    <style type="text/css">
        * {
            padding: 0;
            margin: 0;
        }

        html,
        body {
            width: 100%;
            height: 100%;
            background: transparent;
        }

        .introl-section {
            width: 100%;
            height: 100%;
            background: url('/static/front/app/images/share.png') repeat center;
            background-size: 100% 100%;

        }

        .qrcode-cont {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 55%;
            background: #fff;
            box-shadow: 0 0 0.9rem #666;
        }

        #qrcodeCanvas {
            position: absolute;
            top: 54%;
            left: 50%;
            transform: translate(-50%, -50%);
            -webkit-transform: translate(-50%, -50%);
        }

        .erMigs {
            text-align: center;
        }

        #qrcodeCanvas canvas {
            margin: 0 auto;
            vertical-align: middle;
            border: 3px solid #fff;
        }

        #qrcodeCanvas #qrCodeIco {
            position: absolute;
            display: block;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            margin: auto;
            width: 3rem;
            height: 3rem;
            background-color: #fff;
            background-repeat: no-repeat;
            background-position: center center;
            background-size: 80% auto;
            border-radius: 0.5rem;
        }

        .appName {
            text-align: center;
            color: #EBA51D;
            font-size: 1.4rem;
            margin-top: 18%;
            margin-bottom: 10%;
        }

        .info {
            text-align: center;
            font-size: 1rem;
            position: absolute;
            bottom: 10%;
            width: 100%;
        }

        .Personal-title span em {
            width: 1.2rem;
            height: 1.2rem;
            left: 0.4rem;
            top: 0.65rem;
        }

        .Personal-title span em i:last-child {
            left: 0.22rem;
        }

        .Limg {
            position: absolute;
            left: 90%;
            top: 30%;
        }

        .Limg > img {
            width: 1.5rem;
            height: 1.5rem;
        }

        /* 动画效果 */
        .am-share {
            font-size: 14px;
            border-radius: 0;
            bottom: 0;
            left: 0;
            position: fixed;
            text-align: center;
            -webkit-transform: translateY(100%);
            -ms-transform: translateY(100%);
            transform: translateY(100%);
            -webkit-transition: -webkit-transform 300ms;
            transition: transform 300ms;
            width: 100%;
            z-index: 1110;
        }

        .am-modal-active {
            transform: translateY(0px);
            -webkit-transform: translateY(0);
            -ms-transform: translateY(0);
            transform: translateY(0)
        }

        .am-modal-out {
            z-index: 1109;
            -webkit-transform: translateY(100%);
            -ms-transform: translateY(100%);
            transform: translateY(100%)
        }

        .am-share-title {
            background-color: #f8f8f8;
            border-bottom: 1px solid #fff;
            border-top-left-radius: 2px;
            border-top-right-radius: 2px;
            color: #555;
            font-weight: 400;
            margin: 0 10px;
            padding: 10px 0 0;
            text-align: center;
        }

        .am-share-title::after {
            border-bottom: 1px solid #dfdfdf;
            content: "";
            display: block;
            height: 0;
            margin-top: 10px;
            width: 100%;
        }

        .am-share-footer {
            margin: 10px;
        }

        .am-share-footer .share_btn {
            color: #555;
            display: block;
            width: 100%;
            background-color: #e6e6e6;
            border: 1px solid #e6e6e6;
            border-radius: 0;
            cursor: pointer;
            font-size: 16px;
            font-weight: 400;
            line-height: 1.2;
            padding: 0.625em 0;
            text-align: center;
            transition: background-color 300ms ease-out 0s, border-color 300ms ease-out 0s;
            vertical-align: middle;
            white-space: nowrap;
            font-family: "微软雅黑";
        }

        .am-share-sns {
            /* background-color: #f8f8f8; */
            border-radius: 0 0 2px 2px;
            /* margin: 0.5rem; */
            padding-top: 15px;
            height: 8rem;
            zoom: 1;
            overflow: auto;
            display: flex;
            justify-content: space-evenly;
            background: url('/static/front/app/images/round.png') no-repeat center;
            background-size: cover;
        }

        .am-share-sns li {
            margin-bottom: 15px;
            padding-top: 1rem;
            display: block;
            float: left;
            height: auto;
            width: 15%;
        }

        .am-share-sns a {
            color: #fff;
            display: block;
            text-decoration: none;
        }

        .am-share-sns span {
            display: block;
        }

        .am-share-sns li i {
            background-position: center 50%;
            background-repeat: no-repeat;
            background-size: 3.2rem 3.2rem;
            /* background-color: #ccc; */
            color: #fff;
            display: inline-block;
            font-size: 18px;
            height: 3.2rem;
            line-height: 3.2rem;
            margin-bottom: 5px;
            width: 3.2rem;
        }

        .am-share-sns .share-icon-duanxin {
            background-image: url('/static/front/app/images/duanxin.png');
        }

        .am-share-sns .share-icon-weixin {
            background-image: url('/static/front/app/images/weixin.png');
        }

        .am-share-sns .share-icon-pengyouquan {
            background-image: url('/static/front/app/images/pengyouquan.png');
        }

        .sharebg {
            /* background-color: rgba(0, 0, 0, 0.6); */
            bottom: 0;
            height: 100%;
            left: 0;
            opacity: 0;
            position: fixed;
            right: 0;
            top: 0;
            width: 100%;
            z-index: 1100;
            display: none;
        }

        .sharebg-active {
            opacity: 1;
            display: block;
        }

        .coded {
            width: 35%;
            position: absolute;
            left: 33%;
            height: 21.5%;
            top: 32.5%;
        }

        .codei {
            width: 100%;
            height: 100%;
            z-index: 9999;
            border-radius: 10%;
        }
    </style>
</head>

<body>
<nav>
    <div class="Personal-title">
            <span>
                <a href="javascript:;" onClick="javascript :history.back(-1)">
                    <em>
                        <i></i>
                        <i></i>
                    </em>
                    <strong>
                        <!-- 返回 --></strong>
                </a>
            </span>分享
        <div class="Limg" onclick="toshare()"><img src="/static/front/app/images/share2.png" alt=""></div>
    </div>
</nav>
<section class="introl-section">
    <!-- <div class="qrcode-cont">
    <p class='appName'>RYH Wallet</p>
    <div id="qrcodeCanvas" class="erMigs" style="display:none">
          <span id="qrCodeIco">
               <img src="/static/front/app/images/login/login_logo.png" style="padding: 5px; height: 3rem; width: 3rem;">
          </span>
    </div>
    <p class='info'>扫一扫<br>分享给好友</p>
</div> -->
    <div class="coded">
        <img class="codei" src="${qrCode}"> <%-- style="padding: 250px; height: 150px; width: 150px;"--%>
    </div>
</section>

<div class="am-share">
    <!-- <h3 class="am-share-title">分享到</h3> -->
    <ul class="am-share-sns">
        <li><a href="#"> <i class="share-icon-duanxin"></i><span>短信</span></a></li>
        <li><a href="#"> <i class="share-icon-weixin"></i><span>微信</span></a></li>
        <li><a href="#"> <i class="share-icon-pengyouquan"></i><span>朋友圈</span></a></li>
    </ul>
    <!-- <div class="am-share-footer"><button class="share_btn">取消</button></div> -->
</div>
</body>
<script type="text/javascript" src="/static/front/js/comm/util.js"></script>
<script type="text/javascript" src="/static/front/js/comm/comm.js"></script>
<script type="text/javascript" src="/static/front/js/plugin/jquery.qrcode.min.js"></script>

<script type="text/javascript">
    function pop() {
        console.log('sssssssssssssssssss');

    }

    // $(function() {
    //     QRcode("${spreadLink}");
    //     //生成二维码
    // })
    // function QRcode(url){
    //     $("#qrcodeCanvas").show();
    //     $("#qrcodeCanvas canvas").remove();
    //     $('#qrcodeCanvas').qrcode({
    //         width:110,//宽度
    //         height:110,//高度
    //         text:url
    //    });
    // }
    function toshare() {
        $(".am-share").addClass("am-modal-active");
        if ($(".sharebg").length > 0) {
            $(".sharebg").addClass("sharebg-active");
        } else {
            $("body").append('<div class="sharebg"></div>');
            $(".sharebg").addClass("sharebg-active");
        }
        $(".sharebg-active,.share_btn").click(function () {
            $(".am-share").removeClass("am-modal-active");
            setTimeout(function () {
                $(".sharebg-active").removeClass("sharebg-active");
                $(".sharebg").remove();
            }, 300);
        })
    }
</script>

</html>