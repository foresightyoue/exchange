<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../front/comm/include.inc.jsp" %>
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
    <title>首页</title>
    <link rel="stylesheet" type="text/css" href="/static/front/app/css/new_common.css" />
    <link rel="stylesheet" href="/static/front/app/css/swiper-3.4.2.min.css" />
    <script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js"></script>
    <script type="text/javascript" src="/static/front/app/js/swiper-3.4.2.jquery.min.js"></script>
    <script type="text/javascript" src="/static/front/app/js/rem.js"></script>
    <script type="text/javascript" src="/static/front/app/js/vue.js"></script>
    <script>
        Vue.config.devtools = true;
    </script>
    <style>
        .content>.head>a {
    font-size: 0;
}
.content>.head>a>img {
    width: 1.47rem;
    height: 1.47rem;
}
.content>.head>.text_center{
    font-size:1.17rem;
}
.content>.head>.text_center:after{
    font-size:0.8rem;
}
.content>.main>.flex_left{
    background-color: #FFFFFF;
    margin-top: 0.33rem;
    overflow-x: auto;
    border-bottom:1px solid #efefef;
}
.content>.main>.flex_left::-webkit-scrollbar{
    display: none;
}
.content>.main>.flex_left>.zixuan{
    padding-left: 1.22rem;
    padding-right: 1.22rem;
    line-height: 2rem;
    padding-bottom: 0.13rem;
    flex-shrink: 0;
}
.content>.main>.flex_left>.select{
    padding-left: 1.22rem;
    padding-right: 1.22rem;
    line-height: 2rem;
    padding-bottom: 0.13rem;
    flex-shrink: 0;
}
.content>.main>.flex_left>.select.active{
    color: #F6AF48;
    border-bottom: 0.13rem solid #EDA640;
    padding-bottom: 0rem;
}
.content>.main>.page_contain{
	position: relative;
    width: 300%;
	left: 0%;
	display: flex;
}
.content>.main>.page_contain>.page{
    width: 33.3%;
    background-color: #FFFFFF;
}
.title{
    color: #999999;
    padding-left: 0.7rem;
    padding-right: 0.7rem;
    border-bottom: 0.03rem solid #efefef;
    line-height: 1.53rem;
}
.field{
    height: 3.67rem;
    display: flex;
    flex-direction: column;
    justify-content: center;
    padding-left: 1.07rem;
    border-bottom: 0.03rem solid #efefef;
}
.b{
    font-size: 0.93rem;
    line-height: 1rem;
}
.m{
    font-size: 0.87rem;
    line-height: 1rem;
}
.s{
    font-size: 0.8rem;
    line-height: 1rem;
    color: #666666;
}
.volume{
    font-size: 0.8rem;
}
.price{
    font-size: 0.93rem;
}
.unit_price{
    font-size:0.8rem;
    color: #666666;
}
.btn{
    width:4rem;
    line-height:2rem;
    border-radius: 0.27rem;
    text-align: center;
    color: #FFFFFF;
    font-size: 12px;
}
.red{
    background-color: #E30606;
}

.gray{
    background-color: #cac8c8;
}

.green{
    background-color: #00DB00;
}
.blue{
    background-color: #3296FA;
}

.add{
    background-color: #E30606;
}
.Minus{
    background-color: #0aa623
}
[v-cloak]{
	display:none;
}
        </style>
</head>

<body>
    <div class="content">
        <div class="head">
            <!-- <a class="left" id='sao' >
                <img src="/static/front/app/images/main/main/saoyisao.png">
            </a> -->
            <a href="/m/ryb/tosearch.html" class="right">
                <img src="/static/front/app/images/main/main/souyisou.png">
            </a>
            <div class="text_center">行情<br /></div>
        </div>
        <div class="main">
            <div class="flex_left">
                <div class="zixuan select text_center">自选</div>
                <c:forEach var="coin" items="${coins}" varStatus="i">
                    <div id="${coin.fShortName}_market" class="select text_center ${i.index == 0 ?'active':'' }">${coin.fShortName}区</div>
                    <!-- <div id="${coin.fShortName}_market" class="select text_center  }">${coin.fShortName}区</div> -->
                </c:forEach>
            </div>
            <div class="page_contain">
                <div class="page">
                    <!-- 自选 start -->
                    <div id="collection" class="flex" v-cloak>
                        <div class="flex_grow">
                            <div class="title">名称/24小时成交量</div>
                            <c:forEach items="${list }" var="v" varStatus="vs">
                                <c:if test="${userEnable != null && userEnable  ==  true }">
                                    <c:if test="${v.fisActive == true }">
                                        <a href="${oss_url}/m/trade/coin.html?coinType=${v.fid }&tradeType=0">
                                    </c:if>
                                </c:if>
                                    <div class="field">
                                        <div class="flex">
                                            <div class="b">${v.fvirtualcointypeByFvirtualcointype2.fShortName }</div>
                                            <div class="m">/</div>
                                            <div class="s">${v.fvirtualcointypeByFvirtualcointype1.fShortName }</div>
                                        </div>
                                        <div class="volume">成交量:<font id="${v.fid }_total_1">0</font>
                                        </div>
                                    </div>
                                </a>
                            </c:forEach>
                        </div>
                        <div class="flex_grow">
                            <div class="title text_center">当前价格</div>
                            <c:forEach items="${list }" var="v" varStatus="vs">
                                <c:if test="${userEnable != null && userEnable  ==  true }">
                                    <c:if test="${v.fisActive == true }">
                                        <a href="${oss_url}/m/trade/coin.html?coinType=${v.fid }&tradeType=0">
                                    </c:if>
                                </c:if>
                                    <div class="field">
                                        <div id="${v.fid }_price_1" class="price">0</div>
                                        <div id="convertInto_RMB_1" class="unit_price">￥0</div>
                                    </div>
                                </a>
                            </c:forEach>
                        </div>
                        <div class="flex_grow">
                            <div class="title text_center">涨幅</div>
                            <c:forEach items="${list }" var="v" varStatus="vs">
                                <c:if test="${userEnable == true }">
                                    <c:if test="${v.fisActive == true }">
                                        <a href="${oss_url}/m/trade/coin.html?coinType=${v.fid }&tradeType=0">
                                    </c:if>
                                </c:if>
                                <div class="field">
                                    <c:if test="${userEnable == null}">
                                        <c:if test="${v.fisActive == true }">
                                            <div id="${v.fid }_rose" class="gray btn">0</div>
                                        </c:if>
                                        <c:if test="${v.fisActive == false }">
                                            <div id="${v.fid }_rose_no" class="gray btn">未开放</div>
                                        </c:if>
                                    </c:if>

                                    <c:if test="${userEnable != null && userEnable  == true }">
                                        <c:if test="${v.fisActive == true }">
                                            <div id="${v.fid }_rose_1" class="red btn">0</div>
                                        </c:if>
                                    </c:if>

                                    <c:if test="${userEnable != null && userEnable  == false }">
                                        <c:if test="${v.fisActive == false }">
                                            <div id="${v.fid }_rose_no_1" class="gray btn">未开放</div>
                                        </c:if>
                                    </c:if>
                                </div>
                                </a>
                            </c:forEach>
                        </div>
                    </div>
                    <!-- 自选 end -->
                </div>
                <c:forEach var="vv" items="${fMap }" varStatus="vn">
                    <div class="page" class="CNY">
                        <!-- CNY start -->
                        <div class="flex">
                            <div class="flex_grow">
                                <div class="title">名称/24小时成交量</div>
                                <c:forEach items="${vv.value }" var="v" varStatus="vs">
                                    <c:if test="${userEnable != null && userEnable  ==  true }">
                                        <c:if test="${v.fisActive == true }">
                                            <a href="${oss_url}/m/trade/coin.html?coinType=${v.fid }&tradeType=0">
                                        </c:if>
                                    </c:if>
                                        <div class="field">
                                            <div class="flex">
                                                <div class="b">${v.fvirtualcointypeByFvirtualcointype2.fShortName }</div>
                                                <div class="m">/</div>
                                                <div class="s">${v.fvirtualcointypeByFvirtualcointype1.fShortName }</div>
                                            </div>
                                            <div class="volume">成交量:<font id="${v.fid }_total">0</font>
                                            </div>
                                        </div>
                                    </a>
                                </c:forEach>
                            </div>
                            <div class="flex_grow">
                                <div class="title text_center">当前价格</div>
                                <c:forEach items="${vv.value }" var="v" varStatus="vs">
                                    <c:if test="${userEnable != null && userEnable  ==  true }">
                                        <c:if test="${v.fisActive == true }">
                                            <a href="${oss_url}/m/trade/coin.html?coinType=${v.fid }&tradeType=0">
                                        </c:if>
                                    </c:if>
                                        <div class="field">
                                            <div id="${v.fid }_price" class="price">0</div>
                                            <div id="convertInto_RMB" class="unit_price">￥0</div>
                                        </div>
                                    </a>
                                </c:forEach>
                            </div>
                            <div class="flex_grow">
                                <div class="title text_center">涨幅</div>
                                <c:forEach items="${vv.value }" var="v" varStatus="vs">
                                    <c:if test="${userEnable != null && userEnable  ==  true }">
                                        <c:if test="${v.fisActive == true }">
                                            <a href="${oss_url}/m/trade/coin.html?coinType=${v.fid }&tradeType=0">

                                        </c:if>
                                    </c:if>
                                    <div class="field">
                                        <c:if test="${userEnable == null}">
                                            <c:if test="${v.fisActive == true }">
                                                <div id="${v.fid }_rose" class="gray btn">0</div>
                                            </c:if>
                                            <c:if test="${v.fisActive == false }">
                                                <div id="${v.fid }_rose_no" class="gray btn">未开放</div>
                                            </c:if>
                                        </c:if>

                                        <c:if test="${userEnable != null && userEnable  == true }">
                                            <c:if test="${v.fisActive == true }">
                                                <div id="${v.fid }_rose" class="gray btn">0</div>
                                            </c:if>
                                            <c:if test="${v.fisActive == false }">
                                                <div id="${v.fid }_rose_no" class="gray btn">未开放</div>
                                            </c:if>
                                        </c:if>
                                        <c:if test="${userEnable != null && userEnable  == false }">
                                            <div id="${v.fid }_rose_no" class="gray btn">未开放</div>
                                        </c:if>
                                        </div>
                                    </a>
                                </c:forEach>
                            </div>
                        </div>
                        <!-- CNY end -->
                    </div>
                </c:forEach>
            </div>
            <div class="obligate"></div>
        </div>
        <div class="menu">
            <jsp:include page="./comm/bottom_menu.jsp"></jsp:include>
        </div>
    </div>
    <script type="text/javascript">
    </script>
    <script type="text/javascript" src="${oss_url}/static/front/js/comm/util.js"></script>
    <script type="text/javascript" src="/static/front/app/js/index.js"></script>
    <script type="text/javascript" src="${oss_url}/static/front/js/plugin/layer/layer.js"></script>
    <script type="text/javascript">
        $(function () {
            // console.log();

            var flexLen = $(".flex_left > div").length;
            $('.content>.main>.page_contain').css('width', flexLen + '00%')
            $('.content>.main>.page_contain>.page').css('width', 100 / flexLen + '00%')
            $(".page_contain").css("left", "-100%");
            var collection = new Vue({
                el: '#collection',
                data: {
                    collections: []
                },
                watch: {
                    collections: function () {
                        console.log(this.collections);
                    }
                }
            });
            var msg = '${msg}';
            if (msg != null && msg != '') {
                layer.msg(msg);
            }
            var allow = true;
            $(".select").click(function () {
                if (allow) {
                    var now = $(".select.active").index();
                    var will = $(this).index();
                    if (now == will) {
                        return;
                    }
                    allow = false;
                    /*  $(".page").eq(will).show();
                    var changeTo="-100%";
                    if (will<now) {
                        $(".page_contain").css("left","-100%");
                        changeTo="0%";
                    } */

                    var changeTo = '0%'
                    changeTo = '-' + will + '00%';
                    // switch(will){

                    // 	// case 1:changeTo='-100%';
                    // 	// break;
                    // 	// case 2:changeTo='-200%';
                    // 	// break;
                    // 	// case 3:changeTo='-300%';
                    // 	// break;
                    // 	default:changeTo='0%';
                    // }
                    $(this).addClass("active").siblings().removeClass("active");
                    $(".content>.main>.page_contain").animate({
                        left: changeTo
                    }, 500, function (e) {
                        allow = true;
                        //$(".page").eq($(".select.active").index()).siblings().hide(); //版本检测hide  
                    });
                }
            });
        })

        function scanT() {
            zyh_js.startQrCodeScan();
        }

        $('.content>.head>.left').click(function () {
            var u = navigator.userAgent;
            var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Adr') > -1;
            var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/);
            if (isAndroid) {
                zyh_js.startQrCodeScan();
            } else if (isiOS) {
                window.webkit.messageHandlers.startQrCodeScan.postMessage({})
            }
        })

        window.scanResult = function (result) {
            console.log('ssssssssssssssssssss');
            layer.msg(result);
            console.log(result, 'ssssssssssssssssssss');
            return result;
        }
    </script>
</body>

</html>