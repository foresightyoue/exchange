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
    <base href="${basePath}" />
    <title>版本更新</title>
    <link rel="stylesheet" href="/static/front/app/css/css.css" />
    <link rel="stylesheet" href="/static/front/app/css/style.css" />
    <link rel="stylesheet" href="/static/front/app/css/swiper-3.4.2.min.css" />
    <script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js"></script>
    <script type="text/javascript" src="/static/front/app/js/swiper-3.4.2.jquery.min.js"></script>
    <script type="text/javascript" src="/static/front/js/plugin/jquery.qrcode.min.js"></script>
    <!-- <script type="text/javascript" src="/static/front/js/plugin/qrcode.js"></script> -->
</head>
<style>
    html,body{
        width: 100%;
        height: 100%;
        background: transparent;
    }
    body {
        padding-top: 0rem;
    }

*, html, body, h1, h2, h3, h4, h5, h6, div, ul, li, dl, dd, dt, p {
    margin: 0;
    padding: 0;
}

h1, h2, h3, h4, h5, h6 {
    font-weight: normal;
}

table {
    border-collapse: collapse;
    border-spacing: 0;
}

* {
    -webkit-box-sizing: border-box;
    -o-box-sizing: bordr-box;
    -ms-box-sizing: border-box;
    -moz-box-sizing: borer-box;
    box-sizing: border-box;
}

table, td, th {
    vertical-align: middle;
}

a img {
    border: none;
}

em, cite {
    font-style: normal;
}

a {
    text-decoration: none;
    cursor: pointer;
}

dl, dt, dd, ol, ul, li {
    list-style: none;
}
/*  */
.lt {
    float: left;
}

.rt {
    float: right;
}

.clear {
    content: '';
    display: block;
    clear: both;
}

.container {
        width: 100%;
        height: 100%;
        background: url("/static/front/app/images/download.jpg") no-repeat scroll center;
        background-size: 100% 100%;
    }


/* .container {
    padding: 0 .8rem;
    overflow: hidden;
}

.content {
    padding-top: 4rem;
    padding-bottom: 4rem;
} */
nav{
    background: none;
}

.Personal-title{
    background: none;
}

.Personal-title span em{
    font-size: 20px;
    width: 0.8rem;
    /* height: 1.8rem; */
    position: absolute;
    top: 10px;
    left: 8px;
    color: #fff;
    line-height: 1.8rem;
}

.Personal-title span em img{
    width: 0.8rem;
    height: 1.2rem;
}

.Personal-title span strong{
    position: absolute;
    z-index: 99;
    left: 1.3rem;
    top: 9px;
    width: 3.25rem;
    font-size: 1.17rem;
    font-weight: normal;
    color: #474746;
    line-height: 1.8rem;
}


</style>


<body>
    <nav>
        <div class="Personal-title">
            <span>
                <a href="javascript:;" onClick="javascript :history.back(-1)">
                    <em>
                        <img src="/static/front/app/images/blck01.png" alt="">
                    </em>
                    <strong>
                        返回</strong>
                </a>
            </span>
        </div>
    </nav>
    <div class="container content" style="padding-bottom: 0px;">
    </div>
    <!-- <div class="winXin_text"><div>点击右上角按钮，然后再弹出的菜单中，点击浏览器中打开，即可安装</div><img alt="" src="/static/front/app/images/jiantou.png" style="position: absolute;right: 1rem;top: .62rem;width: 2rem;filter:alpha(opacity=50);-moz-opacity:0.5;-khtml-opacity: 0.5;opacity: 0.5; "></div> -->

</body>
<script type="text/javascript" src="/static/front/js/layer/layer.js"></script>
<script type="text/javascript" src="/static/front/js/comm/util.js"></script>
<script type="text/javascript" src="/static/front/js/comm/comm.js"></script>
<!-- <script type="text/javascript">
$("#checkNew").click(function() {
    $.ajaxSettings.async = true;
    var currentVersion;
    var clientName = $("#banbentitle .active").text();
    var type = clientName=="Android"?0:1
    $.getJSON("/m/appversion/newversion.html",{"type":type},function(result){
        if (result.status == false) {
            layer.msg("检测更新失败，请稍后重试");
        }else{
          //调用android或iphone方法检测更新
            var client = new ClientProxy();
             client.req = {
                        'version' : result.appVersion,
                        'url' : result.url,
                        'appUpdateReadme': result.appUpdateReadme
                    };
             if (!client.send("GetVersion")) {
                    console.log(client.getMessage());
                }
        }
    }).fail(function() {
        layer.msg("网络错误,请稍后重试");
    });
})

$("#download").click(function() {
    $.ajaxSettings.async = true;
    var currentVersion;
    var clientName = $("#banbentitle .active").text();
    var type = clientName=="Android"?0:1
    $.getJSON("/m/appversion/newversion.html",{"type":type},function(result){
        if (result.status == false) {
            layer.msg("获取版本信息失败，请稍后重试");
        }else{
            location.href = result.url;
        }
    }).fail(function() {
        layer.msg("网络错误,请稍后重试");
    });
})
</script> -->
<script type="text/javascript">
    $(function () {
        var name = window.location.hostname;
        // var name = 'test-exchange.zhongyinghui.com';
        if(name.indexOf('test')==0){
            $('.container').css('background', 'url("/static/front/app/images/download-test.jpg") no-repeat scroll center')
            $('.container').css('background-size', '100% 100%')
            console.log(name,'window.location.hostname');
        }
        
    })
</script>
</html>