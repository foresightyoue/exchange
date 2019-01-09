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
    <title>帐户资产</title>
    <link rel="stylesheet" type="text/css" href="/static/front/app/css/new_common.css"/>
    <link rel="stylesheet" href="/static/front/app/css/swiper-3.4.2.min.css"/>
    <script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js"></script>
    <script type="text/javascript" src="/static/front/app/js/swiper-3.4.2.jquery.min.js"></script>
    <script type="text/javascript" src="${oss_url}/static/front/js/comm/util.js"></script>
    <script type="text/javascript" src="${oss_url}/static/front/js/language/language_cn.js"></script>
    <script type="text/javascript" src="${oss_url}/static/front/js/plugin/layer/layer.js"></script>
    <script type="text/javascript" src="/static/front/app/js/rem.js"></script>
</head>
<style type="text/css">
    .announcement {
        height: 2rem;
        background: #fff;
        display: flex;
        align-items: center;
        font-size: 0.9rem;
        padding-left: 0.5rem;
        margin: 0.5rem 0;
    }

    .notice_active {
        flex: 1;
        height: 100%;
    }

    .notice_active > ul {
        height: 100%;
        width: 100%;
    }

    .notice_active > ul > li {
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        line-height: 2rem;
    }

    .main .money {
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        font-size: 0.800rem;
        height: 4.464rem;
        color: #333333;
        background-color: #ffd584;
        padding-left: 0.667rem;
        margin-top: 0.199rem;
    }

    .main .money span {
        font-size: 1.199rem;
        font-weight: bold;
    }

    .main > .hidden {
        font-size: 0.800rem;
        line-height: 1.667rem;
        padding-left: 0.667rem;
        margin-top: 0.199rem;
        background-color: #FFFFFF;
    }

    .main > .hidden > input {
        vertical-align: middle;
        margin-right: 0.300rem;
        width: 0.734rem;
        height: 0.734rem;
    }

    .property_con {
        margin-top: 0.199rem;
        padding: 0.699rem 0.667rem 0.566rem 0.667rem;
        background-color: #FFFFFF;
    }

    .property_style > span {
        padding-top: 0.664rem;
    }

    .property_style > div {
        padding-top: 0.632rem;
    }

    .property_operate {
        width: 100%;
    }

    .flexLayout {
        display: flex;
        justify-content: space-between;
    }

    .property_con .property_list span {
        font-size: 0.820rem;
        color: #333333;
    }

    .property_con .property_style div {
        font-size: 0.667rem;
        color: #999999;
    }

    .property_operate span {
        font-size: 0.898rem;
        color: #333333;
        padding-top: 0.703rem;
    }

    .property_style {
        width: 50%;
    }

    .property_con span {
        display: block;
    }

    .property_con img {
        width: 1.015rem;
        height: 1.015rem;
        vertical-align: middle;
    }

    .normalShop {
        display: flex;
        align-items: center;
    }

    .modelRadio {
        width: 0.8rem;
        height: 0.8rem;
        border: 1px dotted #666;
        background: #efefef;
        position: relative;
        margin-right: 0.6rem;
    }

    .modelRadio > i {
        width: 100%;
        height: 100%;
        border: 1px dotted #666;
        position: relative;
        margin-right: 0.6rem;
        background: url('/static/front/app/images/bingo.png') no-repeat center center;
        background-size: 100% auto;
        display: none;
    }

    .normalShop.active > .modelRadio > i {
        display: block;
    }

    .main .money1 span {
        font-size: 0.950rem;
    }

    .Limg {
            position: absolute;
            left: 90%;
            top: 1.5%;
        }

        .Limg > a > img {
            width: 60%;
            height: 60%;
        }

</style>
<body>
<div class="content">
    <div class="head">
        <a href="/m/personal/assets.html?menuFlag=assets" class="left">
            <i class="left_arrow"></i>
        </a>
        <div class="text_center">钱包</div>
        <div class="Limg"><a href="/m/financial/walletBill.html"> <img src="/static/front/app/images/bill2.png" alt=""></a>
        </div>
    </div>
    <div class="main">
        <div class="announcement">
            公告：
            <div class="notice_active swiper-container">
                <ul class='swiper-wrapper'>
                    <c:forEach items="${requestScope.constant['news']}" var="v">
                        <li class="notice_active_ch swiper-slide"><a href="${oss_url}/m${v.url }">${v.ftitle }</a></li>
                    </c:forEach>
                </ul>
            </div>
        </div>
        <div class="money money1" style="line-height: 2.464rem;height: auto">
            <div></div>
            <!-- <div>总金额（元）</div> -->
            <span> CNY &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <span id="ownerCNY"><fmt:formatNumber value="${CNY}"
                                                                                                  pattern="##0.00"
                                                                                                  maxIntegerDigits="20"
                                                                                                  maxFractionDigits="2"/></span></span>
            <div></div>
        </div>
        <div class="money">
            <div></div>
            <div>总金额（元）</div>
            <div>≈ <span><fmt:formatNumber value="${exchangerate}" pattern="##0.00" maxIntegerDigits="20"
                                           maxFractionDigits="2"/></span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;CNY
            </div>
            <div></div>
        </div>
        <div class="hidden">
            <!-- <input type="checkbox" name="if_zero_hidden" id="if_zero_hidden" value="1"/><label for="if_zero_hidden">隐藏资产为0的币种</label> -->
            <div class="normalShop active"><span class="modelRadio"><i></i></span> <span>隐藏资产为0的币种</span></div>
        </div>
        <c:forEach items="${fvirtualwallets }" var="v" varStatus="vs" begin="0">
            <div id="${v.value.fvirtualcointype.fid }" class="property_con">
                <div class="property_info clearfix property_list">
                    <span>${v.value.fvirtualcointype.fShortName }</span>
                    <div class="flexLayout">
                        <div class="usable_property property_style"><span id="${v.value.fvirtualcointype.fid }_ftotal">
										<fmt:formatNumber value="${v.value.ftotal }" pattern="0.000000"
                                                          maxIntegerDigits="20" maxFractionDigits="6"/></span>
                            <div>可用</div>
                        </div>
                        <div class="freeze_property property_style"><span id="${v.value.fvirtualcointype.fid }_ffrozen">
										<fmt:formatNumber value="${v.value.ffrozen }" pattern="0.000000"
                                                          maxIntegerDigits="20" maxFractionDigits="6"/></span>
                            <div>冻结</div>
                        </div>
                    </div>
                </div>
                <div class="property_operate flexLayout">
                    <c:choose>
                        <c:when test="${v.value.fvirtualcointype.fisrecharge==true && userEnable == true}">
                            <span><a href="${oss_url}/m/account/rechargeBtc.html?symbol=${v.key }"><img
                                    src="/static/front/app/images/chongzhi.png"> 充币</a></span>
                        </c:when>
                        <c:otherwise>
                            <span><img src="/static/front/app/images/chongzhi1.png"> 充币</span>
                        </c:otherwise>
                    </c:choose>
                    <c:choose>
                        <c:when test="${v.value.fvirtualcointype.FIsWithDraw==true && userEnable == true}">
                            <span><a onclick="checkLevel(${v.key })" href="javascript:void(0);"><img
                                    src="/static/front/app/images/tibi.png"> 提币</a></span>
                            <%-- <span>
                                <a onclick="" href="${oss_url}/m/account/transfer.html?symbol=${v.key }"><img src="https://hg-oss-yf2.oss-cn-beijing.aliyuncs.com/vcoin/ssadmin/CoinType/18051214.png"> 划拨</a>
                            </span> --%>
                        </c:when>
                        <c:otherwise>
                            <span><img src="/static/front/app/images/tibi1.png"> 提币</span>
                        </c:otherwise>
                    </c:choose>
                    <span><a href="${oss_url}/m/zichan/bill.html?symbol=${v.key }&recordType=3&from=bill"><img
                            src="/static/front/app/images/bill.png"> 账单</a></span>
                </div>
            </div>
        </c:forEach>
        <div class="obligate"></div>
    </div>
    <div class="menu">
    </div>
</div>
</body>
<script>
    var mySwiper = new Swiper('.swiper-container', {
        autoplay: 1,
        speed: 8000,
        loop: true,
        freeMode: true,
        autoplayDisableOnInteraction: false,
        freeModeSticky: true,
        spaceBetween: 30
    })

    function getZiCan() {
        $.getJSON("/json/fcurrentZiCan.html", {}, function (result) {
            if (result.status === 200) {
                var data = result.data;
                if (data.CNY > 0) {
                    $("#ownerCNY").text(data.CNY);
                }
                $(".money div>span").text(data.exchangerate.toFixed(2));
                data.listMoney.forEach(function (wallet) {
                    // console.log("#" +  wallet.fid +"_ffrozen",'1_ffrozen')
                    // console.log($("#" +  '${wallet.fid }'+"_ftotal" ),'$("#" +  \'${wallet.fid }\'+"_ftotal" )')
                    $("#" + wallet.fid + "_ftotal").html(parseFloat(wallet.ftotal).toFixed(6) );
                    $("#" + wallet.fid + "_ffrozen").html(parseFloat(wallet.ffrozen).toFixed(6));
                })
            }
        })
    }

    setInterval(getZiCan, 5000);

    function checkLevel(key) {
        var authGrade = parseInt($("#authGrade").val());
        var fhasImgValidate = $("#fhasImgValidate").val();
        if (authGrade <= 1) {
            util.layerAlert("", language["trade.error.tips.16"], 4);
        } else if (authGrade == 2 && "false" == fhasImgValidate) {
            util.layerAlert("", language["trade.error.tips.16"], 4);
        } else {
            window.location.href = "/m/account/withdrawBtc.html?symbol=" + key;
        }
    }

    $(function () {
        console.log('${wallet}', 'ssssssssssssssssssssssss');
        var CNY = '${CNY}';
        if (CNY = '' || CNY <= 0) {
            $('.main .money1  span').css('display', 'none')
        }
        var msg = '${msg}';
        if (msg != null && msg != '') {
            layer.msg(msg);
        }
        // var timer = function(opj) {
        // 	$(opj).find('ul').animate({
        // 		marginTop : "-1.667rem"
        // 	}, 500, function() {
        // 		$(this).css({
        // 			marginTop : "0"
        // 		}).find("li:first").appendTo(this);
        // 	})
        // };
        // var num = $('.notice_active').find('li').length;
        // if (num > 1) {
        // 	var time = setInterval(function() {
        // 		timer(".notice_active")
        // 	}, 3500);
        // 	$('.gg_more a').mousemove(function() {
        // 		clearInterval(time);
        // 	}).mouseout(function() {
        // 		time = setInterval(function() {
        // 			timer(".notice_active")
        // 		}, 3500);
        // 	});
        // }
        var coosStr = document.cookie;
        var coos = coosStr.split("; ");
        for (var i = 0; i < coos.length; i++) {
            var coo = coos[i].split("=");
            if ("checked" == coo[0]) {
                checked = coo[1];
                if (checked == 1) {
                    $(".normalShop").addClass('active');
                } else {
                    $(".normalShop").removeClass('active');
                }
            }
        }
        if ($(".normalShop").hasClass('active')) { <c:forEach items = "${fvirtualwallets }" var = "v" varStatus = "vs" begin = "0" >
            if (${ v.value.ftotal == 0 && v.value.ffrozen == 0 }) {
                $("#" + ${ v.value.fvirtualcointype.fid }).css("display", "none");
            }
            </c:forEach>
        } else { <c:forEach items = "${fvirtualwallets }" var = "v" varStatus = "vs" begin = "0" >
            $("#" + ${v.value.fvirtualcointype.fid }).removeAttr("style");
            </c:forEach>
        }
    });

    $(".normalShop").click(function () {
        $(this).toggleClass('active');
        var isActive = $(this).hasClass('active')
        if (isActive) { <c:forEach items = "${fvirtualwallets }" var = "v" varStatus = "vs" begin = "0" >
            if (${v.value.ftotal == 0 && v.value.ffrozen == 0 }) {
                $("#" + ${v.value.fvirtualcointype.fid }).css("display", "none");
            }
            </c:forEach>
            document.cookie = 'checked=1';
        } else { <c:forEach items = "${fvirtualwallets }" var = "v" varStatus = "vs" begin = "0" >
            $("#" + ${v.value.fvirtualcointype.fid}).removeAttr("style");
            </c:forEach>
            document.cookie = 'checked=0';
        }
    });
</script>
</html>