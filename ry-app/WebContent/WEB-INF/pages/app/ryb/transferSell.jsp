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
    <title>商城转账</title>
    <link rel="stylesheet" href="/static/front/app/css/swiper-3.4.2.min.css"/>
    <script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js"></script>
    <script type="text/javascript" src="/static/front/app/js/swiper-3.4.2.jquery.min.js"></script>
    <script type="text/javascript" src="/static/front/app/js/bankCardAttribution.js"></script>
    <script type="text/javascript" src="/static/front/app/js/rem1.js"></script>
    <style>
        * {
            margin: 0;
            padding: 0;
        }

        html, body {
            background-color: #EEEEEE;
            height: 100%;
        }

        ul, ol, dl, dd, dt {
            list-style: none;
            margin: 0;
            padding: 0;
        }

        a {
            color: #000;
            text-decoration: none;
        }

        .content {
            display: flex;
            flex-direction: column;
            width: 100%;
            height: 100%;
        }

        .content .head {
            padding: 0.265rem 0rem 0.256rem 0rem;
            background-color: #333333;
            font-size: 0.29rem;
            color: #FFFFFF;
        }

        .content .head .left {
            padding: 0rem 0rem 0rem 0.171rem;
            position: absolute;
            color: #FFFFFF;
        }

        .Personal-title {
            text-align: center;
        }

        .left_arrow {
            border-bottom: 0.04rem #FFFFFF solid;
            border-left: 0.04rem #FFFFFF solid;
            display: block;
            margin-top: 0.04rem;
            margin-left: 0.04rem;
            width: 0.275rem;
            height: 0.275rem;
            transform: rotate(45deg);
            -ms-transform: rotate(45deg);
            -moz-transform: rotate(45deg);
            -webkit-transform: rotate(45deg);
            -o-transform: rotate(45deg);
        }

        .content > .main {
            flex-grow: 1;
            overflow-y: auto;
            overflow-x: hidden;
            height: 0;
            width: 100%;
        }

        .content > .main > .income {
            line-height: 0.7rem;
            background-color: #ffffff;
            padding-left: 0.162rem;
            font-size: 0.256rem;
            color: #333333;
            margin-top: 0.154rem;
        }

        .content > .main > .page {
            margin-top: 0.16rem;
            display: flex;
        }

        .content > .main > .page > .change {
            background-color: #999999;
            width: 100%;
            line-height: 0.5rem;
            text-align: center;
            font-size: 0.29rem;
            color: #ffffff;
        }

        .content > .main > .page > .change.active {
            background-color: #eba51d;
        }

        .content > .main > .page_contain {
            position: relative;
            width: 200%;
            left: 0%;
            display: flex;
        }

        .content > .main > .page_contain > .page {
            width: 50%;
        }

        .content > .main > .page_contain > .page > .form {
            display: flex;
            justify-content: space-around;
            height: 4.724rem;
            padding: 0.2rem 0;
            margin-top: 0.179rem;
            flex-direction: column;
            font-size: 0.307rem;
            color: #333;
            background-color: #fff;
        }

        .content > .main > .page_contain > .page > .form > .row {
            display: flex;
            padding: 0 0.5rem;
            align-items: center;
            justify-content: space-around;
        }

        .content > .main > .page_contain > .page > .form > .row > label {
            flex-basis: 30%;
            text-align: right;
        }

        .content > .main > .page_contain > .page > .form > .row > input {
            padding: 0.213rem 0.119rem;
            width: 50%;
            line-height: 0.213rem;
            font-size: 0.213rem;
        }

        .content > .main > .button {
            width: 5.12rem;
            line-height: 0.717rem;
            background-color: #eba51d;
            border-radius: 0.102rem;
            margin: 0.521rem auto;
            font-size: 0.256rem;
            color: #ffffff;
            text-align: center;
        }

        .pswdMask {
            position: fixed;
            height: 100vh;
            width: 100vw;
            left: 0;
            top: 0;
            z-index: 999;
            background: rgba(0, 0, 0, 0.1);
            display: none;
        }

        .pswdMask > .positionBox {
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            border-radius: 0.2rem;
            background: #fff;
            width: 85%;
        }

        .pswdMask > .positionBox > .alertTitle {
            text-align: center;
            font-size: 0.27rem;
            line-height: 0.66rem;
            border-bottom: 1px solid #efefef;
            color: #666;
        }

        .pswdMask > .positionBox > input {
            display: block;
            outline: none;
            border: 1px solid #efefef;
            border-radius: 0.075rem;
            margin: 0.5rem auto;
            width: 55%;
            height: 0rem;
            padding: 0.3rem 0.3rem;
            letter-spacing: 0.05rem;
            font-size: 0.3rem;
        }

        .pswdMask > .positionBox > .pswdBox {
            height: 0.8rem;
            display: flex;
            border-top: 1px solid #efefef;
        }

        .pswdMask > .positionBox > .pswdBox > span {
            flex: 1;
            line-height: 0.8rem;
            text-align: Center;
            border-right: 1px solid #efefef;
            color: #999;
            font-size: 0.3rem;
        }

        .pswdMask > .positionBox > .pswdBox > span:last-child {
            border: none;
            color: #eba51d;
        }

        .Limg {
            position: absolute;
            left: 90%;
            top: 2%;
        }

        .Limg > a > img {
            width: 0.4rem;
            height: 0.5rem;
        }

        .inputRow {
            width: 68%;
            padding-bottom: 0.3rem !important;
            border-bottom: 1px solid #ccc;
            margin: auto;
        }

        .tips {
            justify-content: left !important;
            font-size: 14px;
            line-height: 20px;

        }

        .orange {
            color: #EBA51D;
        }

        .kefu {
            width: 80%;
            margin: 0 auto;
        }

        .kefu img {
            width: 100%;
        }

    </style>
</head>

<body>
<div class="content">
    <div class="head">
        <a href="javascript:history.back();" class="left">
            <i class="left_arrow"></i>
        </a>
        <div class="Personal-title">商城转账</div>
        <div class="Limg"><a href="/m/ryb/transferBill.html"> <img src="/static/front/app/images/bill2.png" alt=""></a>
        </div>
    </div>
    <div class="main">
        <div class="income">收益区RYH数量&nbsp;&nbsp;&nbsp;&nbsp;<span id="ryhFlow">0.000000</span></div>
        <div class="income">交易区RYH数量&nbsp;&nbsp;&nbsp;&nbsp;<span id="rybFlow">0.000000</span></div>
        <div class="page">
            <div class="change active">收益区RYH</div>
            <div class="change">交易区RYH</div>
        </div>
        <div class="page_contain">
            <div class="page">
                <!-- RYH start -->
                <div class="form">
                    <div class="row inputRow">
                        <label for="ryh_num ">RYH数量</label>
                        <input type="number" name="ryh_num" id="ryh_num" value="" placeholder="输入收益区RYH数量"/>
                    </div>
                    <div class="row tips">
                        <span>1、请提前注册商城，再进行商城转账。</span><br>
                    </div>
                    <div class="row tips">
                        <span>2、注册时请保持与瑞银钱包账号一致，否则充值无法到账。</span><br>
                    </div>
                    <div class="row tips">
                        <span>3、充值后第二天查看商城是否到账，正常24小时内到账。</span><br>
                    </div>
                    <div class="row tips">
                        <span>4、当月转账到商城限额600RYH。</span>
                    </div>
                    <div class="row tips">
                        <span>5、如需帮助请联系客服人员。</span>
                    </div>
                    <div class="row tips">
                        <span> 微信号：wxid_5p4uaigbal7s22 <span class="kefuma orange">(点击查看二维码)</span></span><br>
                    </div>
                </div>
                <!-- RYH end -->
            </div>
            <div class="page">
                <!-- RYB start -->
                <div class="form">
                    <div class="row inputRow">
                        <label for="ryb_num">RYH数量</label>
                        <input type="number" name="ryb_num" id="ryb_num" value="" placeholder="输入交易区RYH数量"/>
                    </div>
                    <div class="row tips">
                        <span>1、请提前注册商城，再进行商城转账。</span><br>
                    </div>
                    <div class="row tips">
                        <span>2、注册时请保持与瑞银钱包账号一致，否则充值无法到账。</span><br>
                    </div>
                    <div class="row tips">
                        <span>3、充值后第二天查看商城是否到账，正常24小时内到账。</span><br>
                    </div>
                    <div class="row tips">
                        <span>4、当月转账到商城限额600RYH。</span>
                    </div>
                    <div class="row tips">
                        <span>5、如需帮助请联系客服人员。</span>
                    </div>
                    <div class="row tips">
                        <span> 微信号：wxid_5p4uaigbal7s22 <span class="kefuma orange">(点击查看二维码)</span></span><br>
                    </div>
                </div>
                <!-- RYB end -->
            </div>
        </div>
        <div class="button">确定</div>
        <!-- <div style="height: 0.8rem" class="kefu">
            <img src="/static/front/app/images/kefu.png" alt="">
        </div> -->
    </div>
    <div class="pswdMask">
        <div class="positionBox">
            <div class='alertTitle'>请输入交易密码</div>
            <input type="password" onInput='value=value.replace(/[^\d\w]/g,"")'>
            <div class='pswdBox'>
                <span id='tradeCancelBtn'>取消</span><span id='tradeConfirmBtn'>确定</span>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="${oss_url}/static/front/js/language/language_cn.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/comm/util.js"></script>
<script type="text/javascript" src="/static/front/app/js/index.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/plugin/layer/layer.js"></script>
<script>
    $(function () {
        var msg = '${msg}';
        if (msg != null && msg != '') {
            layer.msg(msg);
        }
        var tid = '${tid}';
        $.post("/api/trading/getjson2.html", {
            "tid": tid,
            "uri": "/api/app/getCoinTotal"
        }, function (data) {
            $("#ryhFlow").html(util.numFormat(data.data.ryhFlow, 6));
            // $("#rybFlow").html(util.numFormat(data.data.rybFlow, 6));
        });
        $.post("/api/trading/getjson3.html", {
            "uri": "/api/trading/queryBlance.html"
        }, function (data) {
            try {
                $("#rybFlow").html(util.numFormat(data.data.RYH.fTotal, 6));
                // $("#tradeRYH").html(util.numFormat(data.data.RYH.fTotal,6));
            } catch (e) {
                console.log(e);
            }

        });
        var allow = true;
        $(".content>.main>.page>.change").click(function (e) {
            if (allow) {
                allow = false;
                $(".content>.main>.page_contain").css("left", $(".content>.main>.page>.change.active").index() * -100 + "%");
                $(this).addClass("active").siblings().removeClass("active");
                $(".content>.main>.page_contain>.page").show();
                $(".content>.main>.page_contain").animate({
                    left: $(this).index() * -100 + "%"
                }, 500, function (e) {
                    $(".content>.main>.page_contain>.page").eq($(".content>.main>.page>.change.active").index()).siblings().hide();
                    $(".content>.main>.page_contain").css("left", "0%");
                    allow = true;
                });
            }
        });
        $('#ryh_num,#ryb_num').on('input', function () {
            var str = $(this).val();
            if (str.length == 0) {
                layer.msg('数字为空或者格式有误，请输入！');
                $(this).val('');
            }
            var re = /([0-9]+\.[0-9]{6})[0-9]*/;
            var aNew = str.replace(re, "$1");
            $(this).val(aNew);
            // $(this).val(arr[1] ? [arr[0], arr[1].slice(0, 6)].join('.') : arr[0]);
        })
        $(".button").on('click', function (e) {
            var index = $(".change.active").index();
            var coinType = "RYH";
            var num = $("#ryh_num").val();
            var user = $("#ryh_user").val();
            var ryhFlow = parseFloat($("#ryhFlow").html()); //收益

            if (index == 1) {
                coinType = "RYB";
                num = $("#ryb_num").val();
                user = $("#ryb_user").val();
                ryhFlow = parseFloat($("#rybFlow").html()); //交易

            }
            if (num > ryhFlow || ryhFlow == 0) {
                layer.msg('余额不足！')
                return
            }
            if (parseFloat(num) <= 0 || num == '') {
                layer.msg('数量不能为0，请重新输入！');
                return
            }


            layer.closeAll();
            $('.pswdMask').show();
        });
        //点击遮罩关闭自己
        $('#tradeCancelBtn').on('click', function () {
            $('.pswdMask').hide().find('input').val('');
        })
        //监听 弹窗取消按钮
        // $('body').on('click','.pswdMask',function(event){
        // 	if(event.currentTarget==event.target){
        // 		$(this).hide().find('input').val('');
        // 	}
        // })
        $('.kefuma').click(
            function () {
                layer.open({
                    type: 1,
                    title: false,
                    closeBtn: 0,
                    area: '100%',
                    skin: 'layui-layer-nobg', //没有背景色
                    shadeClose: true,
                    content: '<div  class="kefu"><img src="/static/front/app/images/kefu.png" alt=""></div>'
                });

            })


        //监听 密码确定按钮
        $('.pswdMask #tradeConfirmBtn').off("click").one("click", pay);

        function pay() {
            layer.load(1, {
                shade: [0.1, '#000']
            });
            var index = $(".change.active").index();
            var type = "1";
            var num = $("#ryh_num").val();
            var user = $("#ryh_user").val();
            if (index == 1) {
                type = "2";
                num = $("#ryb_num").val();
                user = $("#ryb_user").val();
            }
            var pswd = $('.pswdMask').find('input').val(); //取得交易密码
            if (pswd.length < 6) {
                layer.closeAll();
                layer.msg('密码格式有误，请重新输入!');
                $('.pswdMask #tradeConfirmBtn').off("click").one("click", pay);
                return
            }

            $.post("/api/trading/getjson2.html", {
                "type": type,
                "coin": num,
                "pswd": pswd,
                "targetUserId": user,
                "uri": "/api/app/transferMall"
            }, function (data) {
                layer.closeAll();
                if (data.status == 200) {
                    layer.msg('转账成功', {
                        shade: [0.1, '#000'],
                        shadeClose: true,
                        time: 1000,
                        end: function () {
                            location.reload();
                        }
                    });
                } else {
                    layer.msg(data.msg, {
                        shade: [0.1, '#000'],
                        shadeClose: true,
                        time: 1000
                    });
                    $('.pswdMask #tradeConfirmBtn').off("click").one("click", pay);
                }
            });
        }
    });
</script>
</body>

</html>