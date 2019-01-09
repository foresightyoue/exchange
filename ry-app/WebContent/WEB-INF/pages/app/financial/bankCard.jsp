<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../../front/comm/include.inc.jsp"%>
<%
    String path = request.getContextPath();
			String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
					+ path;
%>

<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no, width=device-width">
    <meta name="format-detection" content="telephone=no">
    <meta name="format-detection" content="email=no">
    <base href="${basePath}" />
    <title>支付</title>
    <%--<link rel="stylesheet" href="/static/front/app/css/css.css" />--%>
    <link rel="stylesheet" href="/static/front/app/css/style.css" />
    <link href="/static/front/app/js/c2c/common.css" rel="stylesheet">
    <link href="/static/front/app/js/c2c/otc-app.css" rel="stylesheet">
    <link rel="stylesheet" href="/static/front/app/css/swiper-3.4.2.min.css" />
    <script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js"></script>
    <script type="text/javascript" src="/static/front/app/js/swiper-3.4.2.jquery.min.js"></script>
    <script type="text/javascript" src="/static/front/app/js/c2c/rem1.js"></script>
    <script src="/static/front/js/c2c/jquery.SuperSlide.2.1.1.js"></script>
    <script src="/static/front/js/comm/util.js"></script>
    <style>
    .prev{
        background-size: 130%;
    }
        .set_hint {
    padding: .17rem;
    background-color: #FFFFCC;
}

.swiper-pagination-bullet {
    width: 6px;
    height: 6px;
    border-radius: 3px;
}

.swiper-pagination-bullet-active {
    width: 1.25rem;
    height: 6px;
    border-radius: 3px;
}

.swiper-slide a {
    display: inline-block;
    width: 100%;
    height: 100%;
}

.list_cont ul li {
    position: relative;
}

.list_cont ul li a {
    position: absolute;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    z-index: 2;
}

.top .fl a img {
    width: auto;
    height: 2.2em;
}

body {
    background: #fff;
}

.navigation ul li {
    text-align: center;
}

.tradeClick {
    display: flex;
    justify-content: space-between;
}

#list_cont .fl img {
    width: 2rem;
}

#list_cont .fl {
    padding-top: .7rem;
}

#list_cont li>div {
    width: 33.33%;
}

#list_cont span {
    display: inline-block;
    width: auto;
}

#list_cont .coin_name {
    height: 100%;
    line-height: 1.3rem;
    margin-top: 0.3rem;
}

#list_cont .coin_jiaoyi {
    margin-top: .17rem;
    margin-right: 1rem;
    font-size: .7rem;
    color: #999;
}

.green {
    color: #0aa623;
}

#convertInto_RMB {
    font-size: .7rem;
    color: #999;
}

.list_cont {
    border-top: 0;
    border-bottom: 0;
    padding-top: 0.4rem;
    padding-bottom: .4rem;
}

.fid-num {
    display: inline-block;
    width: 5rem;
    height: 2rem;
    line-height: 2rem;
    text-align: center;
    color: #fff;
    border-radius: 0.2rem;
    -webkit-border-radius: 0.2rem;
}

.Minus {
    background: #03a473;
}

.list_bar span {
    margin-right: 2rem;
    font-weight: 600;
}

.add {
    background: #e76d42;
}

.fontWeight {
    font-size: 1rem !important;
    font-weight: bold !important;
}

.list_cont ul li {
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

.notice_active li a, .more a {
    font-size: 0.7rem;
    opacity: 0.8;
}

.banner {
    height: 9rem;
}

.cont-padding {
    padding-bottom: 11rem !important;
}

#convertInto_RMB {
    height: 1.5rem;
    line-height: 1.5rem;
}

.c2c_box {
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

.c2c_box a, .c2c_box a img {
    display: inline-block;
    width: 100%;
    height: 100%;
}

.munu ul li {
    line-height: 1.8rem;
}

.munu ul li a {
    padding-top: 1.35rem;
}

.numInfo {
    background: #1db3b4;
    padding: 0.2rem 0;
}

.numInfo>span {
    display: block;
    background: #fff;
    width: 95%;
    heigh: 1rem;
    margin: 1rem auto;
    line-heigh: 1rem;
    padding: 1rem;
    border-radius: 0.5rem;
}

.mumu {
    display: flex;
    flex-flow: wrap row;
    justify-content: center;
    width: 100%;
    margin-top: 10px;
    padding-bottom: 3.8rem;
}

.mumu li {
    flex: 0 0 33.3%;
    height: 6rem;
    background: #fff;
}

.mumu li a {
    width: 100%;
    height: 100%;
    display: flex;
    flex-flow: nowrap column;
    justify-content: space-around;
    align-items: center;
}

.mumu li a i {
    width: 2rem;
    height: 2rem;
}

.mumu li:nth-of-type(1) a i {
    background: url('/static/front/app/images/icon_add.png') no-repeat center;
    background-size: 100% auto;
}

.mumu li a span {
    text-align: center;
}

.radio {
    position: initial;
    margin-top: 5px;
}

.bankCard {
    background: #efefef;
    width: 90%;
    margin: 0.5rem auto;
    border-radius: 0.677rem;
    padding: 0.3rem;
    box-sizing: border-box;
    position: relative;
}

.bankCard>div {
    display: flex;
    flex-flow: nowrap row;
    align-items: center;
    margin-bottom: 0.5rem;
}

.bankCard img {
    width: 1rem;
    margin-right: 1rem;
    height: 1rem;
}

.bankCard h3 {
    line-height: 0.8rem;
    font-size: 0.3rem;
}

.bankCard>p {
    line-height: 0.6rem;
    font-size: 0.4rem;
    padding-left: 15%;
}

.mumu li {
    flex: 1;
}

.mumu li span {
    display: block;
    width: 100%;
    color: #666;
}

span.deletCard {
    width: 0.8rem;
    height: 0.8rem;
    position: absolute;
    right: -0.2rem;
    top: -0.2rem;
    background: url('/static/front/app/images/icon_add.png') no-repeat left top;
    background-size: 100% auto;
    transform: rotate(45deg);
}
</style>
</head>

<body>
    <div class="header">
        <a href="javascript:history.go(-1);" class="prev"></a>
        <div class="head_title font15">收款账号管理</div>
        <!--<div class="more more-z">设置</div>-->
    </div>
    <%--<nav>--%>
    <%--<div class="Personal-title">--%>
    <%--<span>--%>
    <%--<a href="/m/personal/assets.html?menuFlag=assets">--%>
    <%--<em>--%>
    <%--<i></i>--%>
    <%--<i></i>--%>
    <%--</em>--%>
    <%--<strong>返回</strong>--%>
    <%--</a>--%>
    <%--</span>支付--%>
    <%--</div>--%>
    <%--</nav>--%>

    <div style="padding-top: 10%;">
        <ul class="set_hint box-sizing">
            <li class="font13 mb_10">1、请设置您能支持的支付或收款方式，请务必保障是您本人所有的账号。</li>
            <li class="font13">2、您所设置的信息，将可自动呈现在您所发布的广告信息中。
        </ul>
        <ul class="news_list pl_10">
            <li class="pr_10"><a href="/m/otc/userReceiptgo.html?ftype=1&ying=1">
                    <div class="flexLayout">
                        <div class="list_left font16">
                            <img src="/static/front/images/c2c/Alipay.png" alt="" class="mr_10"> <span>支付宝设置</span>
                        </div>
                        <div class="list_right">
                            <img src="/static/front/images/c2c/right.png" alt="">
                        </div>
                    </div>
                </a></li>
            <li class="pr_10"><a href="/m/otc/userReceiptgo.html?ftype=2&ying=1">
                    <div class="flexLayout">
                        <div class="list_left font16">
                            <img src="/static/front/images/c2c/WeChat.png" alt="" class="mr_10"> <span>微信设置</span>
                        </div>
                        <div class="list_right">
                            <img src="/static/front/images/c2c/right.png" alt="">
                        </div>
                    </div>
                </a></li>
            <li class="pr_10"><a href="${oss_url}/m/add/bankCard.html">
                    <div class="flexLayout">
                        <div class="list_left font16">
                            <img src="/static/front/images/c2c/bank.png" alt="" class="mr_10"> <span>银行卡设置</span>
                        </div>
                        <div class="list_right">
                            <img src="/static/front/images/c2c/right.png" alt="">
                        </div>
                    </div>
                </a></li>
        </ul>
    </div>
    <div class='cardInfo'>
        <c:forEach items="${bankCard }" var="bankCard">
            <div class='bankCard'>
                <div>
                    <img src='/static/front/app/images/icon_bank_card.png' />
                    <h3>
                        ${bankCard.bankName}<br /> <span>储蓄卡</span>
                    </h3>
                </div>
                <p>****&nbsp;****&nbsp;****&nbsp;${bankCard.cardNum}</p>
                <span value='${bankCard.fId}' class='deletCard'></span>
            </div>
        </c:forEach>
    </div>
    <script type="text/javascript">

    </script>
    <script type="text/javascript" src="${oss_url}/static/front/js/comm/util.js"></script>
    <script type="text/javascript" src="/static/front/app/js/index.js"></script>
    <script type="text/javascript" src="${oss_url}/static/front/js/plugin/layer/mobile/layer.js"></script>
    <script type="text/javascript">
        $(function () {
            var msg = '${msg}';
            if (msg != null && msg != '') {
                layer.msg(msg);
            }
        })
        function iphoneX(type) {
            if (type == iphoneX) {
                $(".munu").css("height", "4.5rem");
                $(".radio").css("bottom", "4.5rem");
            } else {
                $(".munu").css("height", "3.06rem");
                $(".radio").css("bottom", "3.06rem");
            }
        }
        $('.deletCard').click(function () {
        	var id=$(this).attr('value');
           layer.open({
               content:'是否删除此卡片',
               btn:['是','否'],
               anim:'up',
               skin:'footer',
               yes:function(index){
                   layer.close(index);
                   layer.open({
                       content:'执行删除任务中...',
                       success:function(){
							$.post("/m/delBankCard.html",{"cardfId":id},function(obj){
								window.location.reload();
                    	   	})
                       }
                   })
                   
               },
               no:function(index){
                   layer.close(index);
               }
           })
        })
    </script>
</body>

</html>