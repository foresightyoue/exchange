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
    <title>系统充值记录</title>
    <link rel="stylesheet" href="/static/front/app/css/css.css" />
    <link rel="stylesheet" href="/static/front/app/css/style.css" />
    <link rel="stylesheet" href="/static/front/app/css/swiper-3.4.2.min.css" />
    <link rel="stylesheet" href="/static/front/app/css/mobiscroll.min.css" />
    <script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js"></script>
    <script type="text/javascript" src="/static/front/app/js/swiper-3.4.2.jquery.min.js"></script>
    <script type="text/javascript" src="/static/front/js/comm/clipboard.min.js"></script>
    <script type="text/javascript" src="/static/front/js/layer/layer.js"></script>
    <script type="text/javascript" src="/static/front/app/js/vue.js"></script>
    <script type="text/javascript" src="/static/front/app/js/mobiscroll.min.js"></script>
    <script type="text/javascript" src="/static/front/app/js/mobi.js"></script>
    <style type="text/css">
        * {
            font-size: 0.77rem;
        }

        .selectBox {
            padding: 0.5rem;
            height: 3rem;
            display: flex;
            background: #fff;
            margin-bottom: 0.6rem;
            align-items: center;
        }

        .selectBox select {
            border: none;
            background: #fff;
            width: 30%;
        }

        .firstSelect {
            border: none;
            background: #fff;
        }

        .itemBox {
            padding: 0.3rem 1rem;
            background: #fff;
            margin: 0.542rem;
            border-radius: 0.3rem;
        }

        .itemBox p {
            line-height: 2rem;
            display: flex;
            align-items: center;
        }

        .itemBox p span {
            font-size: 0.9rem !important;
        }

        .itemBox p:first-child {
            color: #EBA51D;
            border-bottom: 1px solid #efefef;
        }

        .waterNum {
            width: 60%;
            text-overflow: ellipsis;
            overflow: hidden;
            white-space: nowrap;
        }

        [v-cloak] {
            display: none;
        }

        .dataBox {
            border: none;
            outline: none;
            width: 30%;
        }

        .Personal-title span em {
            width: 1.2rem;
            height: 1.2rem;
            position: absolute;
            top: 0.65rem;
            left: 0;
        }

        .Personal-title span em i:last-child {
            background: #333;
            position: absolute;
            left: 0.22rem;
            top: 0;
        }

        .pRight {
            margin-left: auto;
            margin-right: 2%;
            color: #EBA51D;
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
                </a>
            </span>
            系统充值记录
        </div>
    </nav>


    <section id="wallet" v-cloak>
        <!-- <div class='selectBox'>
           
            <div style="width: 100%;">
                <input style="width: 100%;" v-on:change="request" type='text' v-model='date' placeholder='点击选择日期(默认展示最近一周数据  )' readonly class='dateBox'
                    id='dateBox' onClick="mobiDate('dateBox')">
            </div>
        </div> -->
        <div>
            <div v-for="(item,i) in bills" class='itemBox'>
                <!-- <%--<p><span>钱包 ID：</span><span>{{item.walletId}}</span> <span class="pRight right1" v-on:click="copy(item.walletId,'.right1')">复制</span></p>--%> -->
                <p><span>币种类型：</span><span>{{item.type}}</span></p>
                <p><span>数&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;量：</span><span>{{item.number}}</span></p>
                <p><span>创建时间：</span><span>{{item.date}}</span></p>
                <p><span>流&nbsp;&nbsp;水&nbsp;号：</span><span class='waterNum'>{{item.serialNo}}</span> <span class="pRight right2" v-on:click="copy(item.serialNo,'.right2')">复制</span></p>
                <p><span>说&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;明：</span><span>{{item.remark}}</span></p>
            </div>
        </div>
    </section>
    <%-- <jsp:include page="../comm/menu.jsp"></jsp:include> --%>
</body>
<script type="text/javascript" src="/static/front/js/comm/util.js"></script>
<script type="text/javascript" src="/static/front/js/comm/comm.js"></script>
<script type="text/javascript">
    /* $(function(){ */
    var Bill = new Vue({
        el: '#wallet',
        data: {
            coin: "ALL",
            type: "ALL",
            coinType: [],
            date: '',
            capitalType: [],
            bills: []
        },
        methods: {
            request: function () {
                var that = this;
                if (this.date != "") {
                    $.post("/m/user/wallet/transfer/log.html", {
                    }, function (data) {
                        that.bills = data.data;
                    })
                } else {
                    $.post("/m/user/wallet/transfer/log.html", {

                    }, function (data) {
                        console.log(data);
                        that.bills = data.data;
                    })
                }
            },
            copy: function (tid,pClass) {
                var clipboard = new ClipboardJS(pClass, {
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

            },
        },
        watch: {
            date: function () {
                this.request();
            }
        }
    });
    // $.post("/api/trading/getjson2.html", {
    //     "uri": "/api/app/billCoinTypeAndCapitalType"
    // }, function (data) {
    //     console.log(data);
    //     Bill.coinType = data.data.coinType;
    //     Bill.capitalType = data.data.capitalType;
    // });
    $.post("/m/user/wallet/transfer/log.html", {
    }, function (data) {
        Bill.bills = data.data;
    })
    // var oldVal = $('#dateBox').val();
    // setInterval(function () {
    //     if (oldVal == $('#dateBox').val()) {
    //         return
    //     } else {
    //         Bill.date = $('#dateBox').val();
    //         oldVal = $('#dateBox').val();
    //     }
    // }, 100)
    /* }); */
</script>

</html>