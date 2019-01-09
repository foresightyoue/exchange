<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html style="font-size: 16px;">

<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no, width=device-width">
    <meta name="format-detection" content="telephone=no">
    <meta name="format-detection" content="email=no">
    <base href="${basePath}" />
    <title>申请成为认证商家</title>
    <link rel="stylesheet" href="/static/front/app/css/css.css" />
    <link rel="stylesheet" href="/static/front/app/css/style.css" />
    <script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js"></script>
    <script type="text/javascript" src="/static/front/js/layer/layer.js"></script>
    <script type="text/javascript" src="/static/front/app/js/vue.js"></script>
</head>
<style>
    body{
	background:#efefef;
}
section{
	padding:0.5rem 1rem 3rem;
	background:#fff;
	box-shadow: 0 0 20px 5px #efefef;
}
.agreeBtn{	
	-webkit-appearance: checkbox;
}
.outLine{
	display: inline-block;
    width: 1rem;
    height: 1rem;
    border: 1px solid #919191;
    border-radius: 50%;
    margin-right: 0.4rem;
    vertical-align: sub;
    line-height: 1.5rem;
    position: relative;
    background:#ebebeb;
}
.fillDot{
	position: absolute;
    width: 50%;
    height: 50%;
    background: #cf8f1a;
    border-radius: 50%;
    top: 50%;
    left: 50%;
    transform: translate(-50%,-50%);
    display:none;
}
span.active .fillDot{
	display:block;
}
.selectItem{
	height: 3rem;
    border-top: 1px solid #efefef;
    border-bottom: 1px solid #efefef;
    display: flex;
    align-items: stretch
}
.selectItem label{
	line-height:3rem;
}
.selectItem>div{
	display:flex;
	align-items:center;
}
.modelRadio{
    width: 1rem;
    height: 1rem;
    border: 1px dotted #666;
    background: #efefef;
    position: relative;
    margin-right: 0.6rem;
}
.selectItem>div i{
	width: 100%;
    height: 100%;
    background:url('/static/front/images/udian/input_true.png') no-repeat center;
    background-size:100% auto;
    display: none;
    float:left;
}
.selectItem>div.active i{
	display:block
}
.normalShop,.vipShop{
	margin-right:2rem;
}
</style>

<body>
    <nav>
        <div class="Personal-title">
            <span>
                <a href="javascript:history.go(-1);">
                    <em>
                        <i></i>
                        <i></i>
                    </em>
                    <strong>返回</strong>
                </a>
            </span>
            申请成为认证商家
        </div>
    </nav>
    <section id="body">
        <div style="display:flex;justify-content:center;align-items:center;padding-bottom: 1.5rem;padding-top: 0.7rem;font-size: 1rem">风险提醒</div>
        <div style="padding-bottom: 1rem;"><span>一、在此郑重提醒广大用户理性投资，尊重市场、敬畏市场，牢记投资风险，量力而为。</span></div>
        <div style="padding-bottom: 1rem"><span>二、本平台仅作为中立的区块链资产交易平台提供方，不承担用户因参与区块链交易带来一切可能的损失。</span></div>
        <div style="padding-bottom: 1rem"><span>普通商家：实名认证用户即可申请普通商家，可发布OTC广告进行数字资产交易。</span></div>
        <div style="padding-bottom: 2rem"><span>VIP会员商家：需缴纳一定数量的RYB，同时享受更多权利，有效期三个月，到期自动降回普通用户。</span></div>
        <div style="padding-bottom: 2rem"><span class='outLine'><i class='fillDot'></i></span><span>我已阅读并承诺：本人资金来源合法可靠，本人已知悉数字资产及潜在风险，本人具备风险意识以及抗风险能力并自愿承担由此带来的一切风险。</span></div>
        <div class='selectItem'>
            <div v-if="IsMerchant<1" class='normalShop active'>
                <span class='modelRadio'><i></i></span>
                <span>普通商家</span>
            </div>
            <div v-if="IsMerchant<2" class='vipShop'>
                <span class='modelRadio'><i></i></span>
                <span>VIP商家</span>
            </div>
            <div v-if="IsMerchant<3" class='vipShop'>
                <span class='modelRadio'><i></i></span>
                <span>超级商家</span>
            </div>
        </div>
        <div class='selectItem' style='border-top:none;'>
            <label>商家的名称:</label><input type='text' id='shopName' v-bind:value="sellerName" placeholder='请输入商家名称'>
        </div>
        <div style="display:flex;justify-content:center;align-items:center;margin-top:2.5rem;"><button id="next" style="background-color: #cf8f1a;width: 90%;height: 3rem;color: white;border-radius: 0.3rem;font-size:1rem;">确定</button></div>
    </section>
    <script type="text/javascript">
        $(function () {
            var seller = new Vue({
                el: '#body',
                data: {
                    sellerLevel: '',
                    sellerName: '',
                    IsMerchant: 0
                }
            });
            $.post("/m/user/api/info/seller.html", {
                "userId": '${userId}'
            }, function (obj) {
                console.log(obj);
                seller.sellerLevel = obj.data.sellerLevel;
                seller.sellerName = obj.data.sellerName;
                seller.IsMerchant = obj.data.IsMerchant;
            });
            $('.outLine').click(function () {
                $(this).toggleClass('active');
            })
            $('.selectItem>div').click(function () {
                $(this).addClass('active').siblings().removeClass('active');
            })
            var isMerchant = '${IsMerchant}';
            $("#next").click(function checkSure() {
                if ($('.outLine').hasClass('active')) {
                    var type = ($('.selectItem>div.active').index() + 1) + seller.IsMerchant;
                    $.post("/m/user/seller/identity.html", {
                        "userId": '${userId}',
                        "sellerName": $("#shopName").val(),
                        "sellerLevel": type
                    }, function (data) {
                        console.log(JSON.parse(data));
                        layer.msg(JSON.parse(data).msg, {
                            time : 3000,
                            end: function (layero, index) {
                                if (JSON.parse(data).status == 200) {
                                    setTimeout(window.location.href =
                                        "/m/financial/index.html?menuFlag=account",
                                        4500);
                                }
                            }
                        });

                    });
                } else {
                    layer.msg("请先确定已阅读风险提醒！");
                }
            });
        });
    </script>
</body>
<script type="text/javascript" src="/static/front/js/comm/util.js"></script>
<script type="text/javascript" src="/static/front/js/comm/comm.js"></script>

</html>