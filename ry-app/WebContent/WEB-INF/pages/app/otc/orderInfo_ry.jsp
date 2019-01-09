<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>订单详情</title>
  <style>
    *{
      margin:0;
      padding:0;
    }
    body{
      background:#efefef;
      font-size:1.8rem;
    }
    .infoBox{
      border-radius:0.3rem;
      overflow:hidden;
      background:#fff;
      width:95%;
      margin:1rem auto;
    }
    .infoBox>p{
      height:3rem;
      line-height:3rem;
      text-indent:1rem;
      background:#EBA51D;
      color:#fff;
    }
    .itemBox{
      display:flex;
      height:3rem;
      border-bottom:1px solid #efefef;
      align-items:center;
      padding:0 1rem;
    }
    .itemBox>.item{
      color:#333;
      flex:0 0 30%;
    }
    .itemBox>span:last-child{
      color:#999;
    }
    .hang{
      display:flex;
      padding:1rem;
      border-bottom:1px solid #efefef;
    }
    .hang>span{
      flex:0 0 30%;
      line-height:3rem;
    }
    .hang>p{
      line-height:1.8rem;
      padding:0.5rem 0;
      color:#999;
    }
    .communication{
      display:flex;
      align-items:center;
      height:4rem;
      padding-left:1rem;
    }
    .communication>span:first-child{
      flex:0 0 30%;
      line-height:4rem;
    }
    .communication>span:last-child{
      width:8rem;
      height:2.5rem;
      line-height:2.5rem;
      background:#EBA51D;
      color:#fff;
      text-align:center;
      border-radius:0.3rem;  
    }
   .hang>p.warnInfo{
     color:#EBA51D;
   }
  </style>
  <script src='jquery-1.11.3.min.js'></script>
  <script src='rem2.js'></script>

</head>
<body>
    <div class='infoBox'>
      <p>出售RYB</p>
      <div class='itemBox'><span class="item">订单号：</span><span class="info">asdfasdfasdfasdfa</span></div>
      <div class='itemBox'><span class="item">交易金额：</span><span class="info">10CNY</span></div>
      <div class='itemBox'><span class="item">下单日期：</span><span class="info">2018-09-15 15:08：16</span></div>
      <div class='itemBox'><span class="item">数量：</span><span class="info">10.000000</span></div>
      <div class='itemBox'><span class="item">手续费：</span><span class="info">0</span></div>
      <div class='itemBox'><span class="item">价格：</span><span class="info">1.0CNY</span></div>
      <div class="hang">
        <span>支付信息：</span>
        <p>赵四 农业银行 <br>河南阿里教授的发链接啊是的发 <br>467643134634676431
        </p>
      </div>
      <div class="hang">
        <span>备注</span>
        <p class='warnInfo'>打款后请在订单详情页面点击（联系对方），并留言大款人的姓名，RY账号以及打款金额，以方便收款方核对信息并放币</p>
      </div>
      <div class="communication">
        <span>沟通：</span><span>联系对方</span>
      </div>
    </div>
</body>
</html>