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
    <title>合并钱包</title>
    <link rel="stylesheet" href="/static/front/app/css/css.css" />
    <link rel="stylesheet" href="/static/front/app/css/style.css" />
    <script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js" ></script>
    <script type="text/javascript" src="/static/front/app/js/rem2.js" ></script>
    <script type="text/javascript" src="/static/front/js/layer/layer.js"></script>
    <script type="text/javascript" src="/static/front/app/js/vue.js"></script>
    <style type="text/css">
    *{
      margin:0;
      padding:0;
    }
    body{
      background:#fff;
      font-size:1.8rem;
      color:#333;
      padding-top:4.4rem;
      cursor:pointer;
    }
    .Personal-title span strong{
        position: absolute;
        z-index: 99;
        left: 0.5rem; 
        top: 0.75rem;
        width: 3.25rem;
        font-size: 1.67rem;
        font-weight: normal;
        color: #fff;
    }
    .Personal-title span{
        top: 0.6rem;
    }
    .top{
      height:4rem;
      display:flex;
      align-items: center;
      padding-left:2rem;
      border-bottom: 1px solid #efefef;
      font-size:2rem;
    }
    .top>.selectAllStd,.selectStd{
      width:1.4rem;
      height:1.4rem;
      background:#CAC8C8;
      position:relative;
      border-radius:50%;
    }
    .top>.selectAllStd>span,.selectStd>span{
      position:absolute;
      top:50%;
      left:50%;
      transform:translate(-50%,-50%);
      width:50%;
      height:50%;
      border-radius:50%;
      background:#F67003;
      display:none;
    }
    .top.active>.selectAllStd>span,.item.active>.selectStd>span{
      display:block;
    }
    .itemBox {
    padding: 0 2rem;
    }
    .item{
      display:flex;
      height:3rem;
      align-items:center;
      margin-top:1rem;
    }
    .item>.selectStd,.top>.selectAllStd{
      margin-right:1rem;
    }
    .item>div{
      flex:1;
      background:#EEEEEE;
      display:flex;
      align-items: center;
      padding:0 1rem;
      height:3rem;
      justify-content: space-between;
    }
    .clGrey{
      color:#999;
    }
    .mergeBtn{
      height:3rem;
      line-height:3rem;
      color:#fff;
      text-align:Center;
      background:#EBA51D;
      border-radius:0.25rem;
      margin-top: 3rem;
    }
    [v-cloak]{
        display:none;
    }
    .Personal-title{
        padding:1.2rem 0;
        font-size:2rem;
    }
    .Personal-title span em{
        width: 1.6rem;
        height: 1.6rem;
        left: 0.4rem;
    }
    .Personal-title span em i:last-child{
        left:0.32rem;
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
                        <strong><!-- 返回 --></strong>
                    </a>
                </span>
        合并钱包
    </div>
</nav>
<!-- <section id="wallet">
<div style="padding: 0.5rem;"><input onclick="alls();" type="checkbox" value="1" id="all" name="all" />全选（{{getzise(smallWallet)}}）</div>
<div style="background-color: white;padding: 1rem">
    <div v-for="(item,i) in smallWallet" style="margin-bottom: 1rem">
        <input v-bind:value="item.tid" name="smallwallet" type="checkbox"/>
        <div class="custom" style="float:right;background-color: #ececec;width: 92%;" >
            <span style="font-size: 0.8rem;">子钱包{{i+1}}:</span><span style="font-size: 0.8rem;float: right;padding-right: 1rem;">{{to(item.currencyTotal,2)}}</span><span style="font-size: 0.8rem;float: right;padding-right: 1rem;">锁仓金额</span>
        </div>
    </div>
    <div style="display:flex;justify-content:center;align-items:center;"><button onclick="hb()" style="background-color: #199ed8;width: 18%;font-size: 0.9rem;">合并</button></div>
</div>
</section> -->
 <div class='mainContain' id="wallet" v-cloak> 
        <div class='top'>
          <span class='selectAllStd'><span></span></span>
          全选（{{getsize(smallWallet)}}）
        </div>
        <div class='itemBox'>
          <div class='item'  v-for="(item,i) in smallWallet"  v-bind:value="item.tid">
            <span class='selectStd'><span></span></span>
            <div>
              <span>子钱包{{i+1}}</span>
              <span class='clGrey'>锁仓金额</span>
              <span>{{to(item.currencyTotal,2)}}</span>
            </div>
          </div>
        <div class='mergeBtn' onclick="hb()">合并</div>
        </div>
    </div>
<%-- <jsp:include page="../comm/menu.jsp"></jsp:include> --%>
</body>
<script type="text/javascript" src="/static/front/js/comm/util.js"></script>
<script type="text/javascript" src="/static/front/js/comm/comm.js"></script>
<script type="text/javascript">
$(function(){
    var Walletdata =new Vue({
        el: '#wallet',
        data: {
            smallWallet: []
        },
        methods: {
            sy: function (welletId) {
                window.location.href = "${oss_url}/m/ryb/details.html?welletId="+welletId;
            },
            to : function (item,i) {
                return parseFloat(item).toFixed(i);
            },
            getsize : function (items) {
                return items.length;
            }
        }
    });
    var walletId ="${walletId}";
    $.post("/api/trading/getjson2.html",{"walletId":walletId,"uri":"/api/app/walletEdits"},function(data){
        console.log(data);
        Walletdata.smallWallet = data.data;
    });
});
</script>
<script type="text/javascript">
$('body').on('touchend','.top',function(){
    $(this).toggleClass('active');
    if($(this).hasClass('active')){
      $('.item').addClass('active');
    }else{
      $('.item').removeClass('active');
    }
  })
  $('body').on('touchend','.item',function(){
    $(this).toggleClass('active');
    if($('.item.active').length==$('.item').length){
        $('.top').addClass('active');
    }else{
        $('.top').removeClass('active');
    }
  })

function hb(){
    var arr =[];
    var isAll=1;
    if($('.item.active').length<1){
        layer.msg('请选择要合并的钱包！');
        return
    }
    if($('.item.active').length==$('.item').length){
    	isAll=2;
    }
    $('.item.active').each(function(){
        arr.push($(this).attr('value'));
    })
    console.log(arr.join());
    $.post("/api/trading/getjson2.html",{"walletIds":arr.join(),"isAllMerge":isAll,"uri":"/api/app/mergeWallet"},function(data){
        console.log(data);
        if(data.status!=200){
            layer.msg(data.msg);
        }else{
            layer.msg(data.data);
	        setTimeout(function(){
                window.location.href = "javascript:history.go(-1);"
            },3000);
        }
    });
}
</script>
</html>