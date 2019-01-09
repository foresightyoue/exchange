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
    <title>子钱包</title>
    <link rel="stylesheet" href="/static/front/app/css/css.css" />
    <link rel="stylesheet" href="/static/front/app/css/style.css" />
    <link rel="stylesheet" href="/static/front/app/css/swiper-3.4.2.min.css" />
    <script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js"></script>
    <script type="text/javascript" src="/static/front/app/js/swiper-3.4.2.jquery.min.js"></script>
    <script type="text/javascript" src="/static/front/js/comm/util.js"></script>
    <script type="text/javascript" src="/static/front/js/comm/clipboard.min.js"></script>
    <script type="text/javascript" src="/static/front/js/layer/layer.js"></script>
    <script type="text/javascript" src="/static/front/app/js/vue.js"></script>
    <style type="text/css">
        * {
            font-size: 0.67rem;
        }

        #title {
            font-size: 1rem;
        }

        .purseItem {
            margin: 0.1rem 0.539rem 0.539rem;
            background: #fff;
            border-radius: 0.3rem;
            overflow: hidden;
        }

        .purseTitle {
            font-weight: 600;
            text-indent: 1rem;
            height: 2rem;
            line-height: 2rem;
            border-bottom: 1px solid #efefef;
            display: flex;
        }

        .purseTitle {
            flex-grow: 1;
            text-indent: 0.2rem;
            color: #EBA51D;
            justify-content: space-around;
        }


        .infos {
            border-bottom: 1px solid #efefef;
        }

        .infos>div {
            display: flex;
            height: 2rem;
        }

        .infos>div>span {
            flex: 1;
            text-align: center;
            line-height: 2rem;
        }

        .btnBox {
            padding: 0.5rem;
            display: flex;
            height: 3rem;
            align-items: center;
            justify-content: space-evenly;
        }

        .btnBox button {
            width: 27%;
            height: 1.5rem;
            line-height: 1.5rem;
            text-align: center;
            border: none;
            background: #CAC8C8;
            color: #fff;
            border-radius: 0.3rem;
        }

        .btnBox button:first-child {
            background: #EDC31A;
        }

        .btnBox button:nth-child(2) {
            background: #FE9917;
        }

        .btnBox button:last-child {
            background: #F67003;
        }

        .elitRecmmond {
            height: 2rem;
            line-height: 2rem;
            background: #fff;
            color: #333;
            text-indent: 1rem;
        }

        [v-cloak] {
            display: none;
        }

        .purseBox {
            background: #fff;
        }

        .rowItem {
            display: flex;
            align-items: stretch;
            height: 2.5rem;
            border-bottom: 1px solid #efefef;
        }

        .rowItem>div {
            flex: 0 0 50%;
            display: flex;
            align-items: center;
            padding: 0 0.5rem;
        }

        .rowItem>div>span:first-child {
            flex: 0 0 40%;
        }

        .rowItem>div>span:last-child {
            flex: 1;
        }

        .rowItem>div.oneline {
            flex: 1;
        }

        .rowItem button {
            padding: 0.25rem 1.6rem;
            background: #EDC31A;
            color: #fff;
            border-radius: 0.25rem;
        }

        .rowItem>div>span.fl18 {
            flex: 0 0 18%;
        }

        .btnBox button.active3 {
            background: #efefef;
        }

        .Personal-title {
            padding: 0.8rem 0;
        }

        .Personal-title span em {
            width: 1.2rem;
            height: 1.2rem;
            position: absolute;
            top: 0.65rem;
            left: 0;
        }

        .Personal-title span em i:last-child {
            left: 0.25rem;
        }

        /* .copyBT {
            align-items: center;
            background: #EBA51D;
            height: 1.4rem;
            line-height: 1.4rem;
            margin: 0.2rem 0.4rem;
            border-radius: 0.368rem;
            text-align: center;
            color: #fff;
            width: 5.5rem;
        } */

        .textN {
            opacity: 0;
            width: 0;
            height: 0;
            border: none;
            padding: 0;
            margin: 0;
            z-index: -1;
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
                    <strong>
                        <!-- 返回 --></strong>
                </a>
            </span>
            <font id="title">子钱包</font>
        </div>
    </nav>
    <section>
        <div class="user-list clearfix bg-img01">
            <ul>
                <li style="padding-left:0rem;border-bottom: 0px solid #e6e6e6 !important;">
                    <a href="${oss_url}/m/service/ourService.html?id=1"></a>
                    <p style="padding-left: 2rem">最新公告!!!</p>
                </li>
            </ul>
        </div>
        <div id="data" v-cloak class='purseBox'>
            <div class='rowItem'>
                <div class='oneline'>
                    <span class="fl18">主钱包ID：</span><span id="ryhLock">{{userLock.walletId}}</span>
                </div>
            </div>
            <div class='rowItem'>
                <div>
                    <span>锁仓总额：</span><span id="ryhLock">{{userLock.currencyTotal}}</span>
                </div>
                <div>
                    <!-- <span>累计收益：</span><span id="totalProfits">0.00</span> -->
                </div>
            </div>
            <div class='rowItem'>
                <div>
                    <span>昨日释放：</span><span>{{userLock.dayRelease}}</span>
                </div>
                <div>
                    <!-- <span>累计释放：</span><span id="releaseCurrency">0.00</span> -->
                </div>
            </div>
            <div class='rowItem'>
                <div class='oneline'>
                    <span class="fl18">到期时间：</span><span>{{userLock.startRelaseDate}}——{{userLock.endRelaseDate}}</span>
                </div>
            </div>
            <div class='rowItem'>
                <div>
                    <span>子钱包数：</span><span>{{userLock.smallWalletNum}}</span>
                </div>
                <div>
                    <button v-on:click="hb(userLock.walletId)">合并</button>
                </div>
            </div>
        </div>

        <div id="wallet" style="width: 100%;color:#333;justify-content: center;align-items: center; margin-top: 0.539rem"
            v-cloak>
            <div v-for="(item,i) in smallWallet" class='purseItem'>
                <div class='purseTitle'>
                    <span :class='item.tid'>钱包ID：{{item.tid}}</span>
                    <!-- <input type="text" class="textN"> -->
                    <div class="copyBT" v-on:click="copy(item.tid)">复制</div>
                </div>

                <div v-on:click="sy(item.tid)" class='infos'>
                    <div>
                        <span>锁仓金额：</span><span style="text-align: left;">{{item.currencyTotal | toFixed}}</span>
                        <span>累计释放：</span><span style="text-align: left;">{{item.totalProfit | toFixed}}</span>
                    </div>
                    <div>
                        <span>昨日释放：</span><span style="text-align: left;">{{item.dayRelease | toFixed}}</span>
                        <span>未领取的：</span><span style="text-align: left;">{{item.releaseCount | toFixed}}</span>
                    </div>
                </div>
                <div class='btnBox'>
                    <button v-on:click="transfer(item.tid)">转账</button>
                    <!-- <button v-on:click="cf(i)">拆分</button> -->
                    <button :class="returnClass(i)" v-on:click="lq(i)">领取</button>
                </div>
            </div>
        </div>

        <div>
            <!-- <div class='elitRecmmond'>精品推荐</div>
   		<div id="chanping"></div>
   </div> -->
        </div>
    </section>
    <%-- <jsp:include page="../comm/menu.jsp"></jsp:include> --%>
    <script type="text/javascript">
        $(function () {
            var lock_depot_data = new Vue({
                el: '#data',
                data: {
                    userLock: [],
                    smallWallet: []
                },
                methods: {
                    hb: function (welletId) {
                        window.location.href = "${oss_url}/m/trade/Hbwellet.html?walletId=" +
                            welletId;
                    }
                }
            });
            var Walletdata = new Vue({
                el: '#wallet',
                data: {
                    smallWallet: [],
                    Wallet: "",
                },
                methods: {
                    sy: function (welletId) {
                        window.location.href = "${oss_url}/m/ryb/details.html?type=small&welletId=" +
                            welletId + "&wellet=" + this.Wallet;
                    },
                    transfer: function (welletId) {
                        window.location.href = "${oss_url}/m/ryb/childTranster.html?walletId=" +
                            welletId + "&walletId1=" + this.Wallet;
                    },
                    copy: function (tid) {
                        var clipboard = new ClipboardJS('.copyBT', {
                            text: function (trigger) {
                                return tid; //复制内容
                            }
                        })
                        clipboard.on('success', function (e) {
                            console.info('Text:', e.text); //复制文本
                            e.clearSelection(); //取消选择节点
                            parent.layer.msg("复制成功");
                        });
                        clipboard.on('error', function (e) {
                            console.error('Trigger:', e.trigger);
                            parent.layer.msg("复制失败");
                        });
                        // const input = document.createElement('input');
                        // document.body.appendChild(input);
                        // input.setAttribute('value', tid);
                        // input.select();
                        // if (document.execCommand('copy')) {
                        // 	document.execCommand('copy');
                        // 	parent.layer.msg("复制成功");
                        // }
                        // document.body.removeChild(input);

                    },
                    to: function (item, i) {
                        return parseFloat(item).toFixed(i);
                    },
                    cf: function (index) {
                        if (this.smallWallet[index].currencyTotal < 100) {
                            layer.msg("钱包金额小于100不能进行拆分！");
                            return;
                        }
                        window.location.href =
                            "${oss_url}/m/trade/CFwellet.html?Wellettype=small&walletId=" + this.smallWallet[
                                index].tid + "&wellet=" + this.Wallet;
                    },
                    lq: function (index) {
                        if (this.smallWallet[index].releaseCount <= 0) {
                            return;
                        }
                        $.post("/api/trading/getjson2.html", {
                            "walletId": this.smallWallet[index].tid,
                            "uri": "/api/app/oneKeyReceive"
                        }, function (data) {
                            //layer.msg(data.data);
                            if (data.status == 200) {
                                layer.msg('领取成功', {
                                    shade: [0.1, '#000'],
                                    shadeClose: true,
                                    time: 2000,
                                    end: function () {
                                        location.reload();
                                    }
                                });
                            } else {
                                layer.msg(data.msg, {
                                    shade: [0.1, '#000'],
                                    shadeClose: true,
                                    time: 2000
                                });
                            }
                            //setTimeout(function(){location.reload()},6000);
                        });
                    },
                    returnClass: function (index) {
                        return {
                            active3: this.smallWallet[index].releaseCount <= 0
                        }
                    }
                },
                filters: {
                    toFixed: function (value) {
                        return util.numFormat(value, 2)
                    }
                },
                computed: {
                    returnClass: function (index) {
                        return {
                            active3: this.smallWallet[index].currencyTotal < 100
                        }
                    }
                }
            });
            var walletId = "${walletId}";
            Walletdata.Wallet = walletId;
            $.post("/api/trading/getjson2.html", {
                "walletId": walletId,
                "uri": "/api/app/walletEdits"
            }, function (data) {
                Walletdata.smallWallet = data.data;
                var totalProfits = 0;
                for (var i = 0; i < data.data.length; i++) {
                    totalProfits += data.data[i].totalProfit - 0;
                }
                $("#totalProfits").html(util.numFormat(totalProfits, 2));
            });
            $.post("/api/trading/getjson2.html", {
                "uri": "/api/app/userLockProject"
            }, function (data) {
                for (var i = 0; i < data.data.length; i++) {
                    if (data.data[i].walletId == walletId) {
                        lock_depot_data.userLock = data.data[i];
                        $("#releaseCurrency").html(util.numFormat(data.data[i].releaseCurrency, 2));
                    }
                }
            });
        });
    </script>
    <script type="text/javascript">
        /* $(function(){
    $.post("/api/trading/getjson2.html",{"uri":"/api/app/getLockProduct"},function(data){
        console.log(data);
        var str = "";
        for(var i = 0 ;i<data.data.length;i++){
            str += "<div>"+
            "<div>"+
            "<div>"+
                "<span>"+data.data[i].projectName+"</span>"+
            "</div>"+
            "<div>"+
                "<span>目标金额：1000,000.00 （"+data.data[i].smallCurrency+"起购）</span>"+
            "</div>"+
        "</div>"+
        "<div>"+
            "<span>0.00%</span>"+
        "</div>"+
        "</div>";
        }
        $("#chanping").append(str);
    });
}); */
    </script>
</body>
<script type="text/javascript" src="/static/front/js/comm/util.js"></script>
<script type="text/javascript" src="/static/front/js/comm/comm.js"></script>
<script type="text/javascript">
    function chongzhi(value) {
        window.location.href = "${oss_url}/m/account/rechargeBtc.html?symbol=" + value;
    };

    function hb() {
        window.location.href = "${oss_url}/m/trade/Hbwellet.html";
    }

    function sy() {
        window.location.href = "${oss_url}/m/ryb/details.html";
    }

    function fvilog(type) {
        var coin = $("#coin").val();
        $.each($(".jl"), function (i, n) {
            if (i == 0) {
                $(n).css("color", "black");
            } else {
                $(n).css("color", "#999999");
            }
        });
        $.post("/m/security/gethistoty.html", {
            'cointype': coin,
            'type': type
        }, function (data) {
            var str = '';
            var d = JSON.parse(data);
            for (var i = 0; i < d.length; i++) {
                if (d[i].change < 0) {
                    $("#change").html(d[i].change);
                } else {
                    $("#change").html("+" + d[i].change);
                }
                $("#time").html(d[i].fupdatetime);
                $("#after").html(d[i].ftotalafter);
                if (d[i].fPurpose == 1) {
                    $("#type").html("充值");
                } else if (d[i].fPurpose == 2) {
                    $("#type").html("提现");
                } else if (d[i].fPurpose == 3) {
                    $("#type").html("消费");
                }
                str += $("#history").html();
            }
            $("#history1").html(str);
        });
    }
</script>

</html>