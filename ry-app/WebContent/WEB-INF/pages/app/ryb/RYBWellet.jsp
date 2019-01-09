<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0,maximum-scale=1,minimum-scale=1;">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
<title>RYB钱包</title>
  <style>
   *{
      margin:0;
      padding:0;
    }
    body{
      font-size:1.8rem;
      background:#efefef;
    }
    li{
      list-style: none;
    }
    a{
      text-decoration:none;
      color:#333;
    }
    .purseBox{
      margin-top:1rem;
      display:flex;
      align-items: stretch;
      background:#fff;
    }
    .purseBox>div{
      box-sizing:border-box;
    }
    .purseBox>.left{
      flex:0 0 80%;
      border-right:1px solid #efefef;
    }
    .purseBox>.right{
      flex:0 0 20%;
      display:flex;
      justify-content: center;
      align-items: center;
    }
    .purseBox>.left>div{
      display:flex;
      padding:0 10%;
      align-items: center;
      height:5rem;
      border-bottom:1px solid #efefef;
    }
    .purseBox>.right>span{
      width: 4rem;
      height: 2.5rem;
      text-align: Center;
      line-height: 2.5rem;
      color: #fff;
      background: #EBA51D;
      border-radius: 0.3rem;
    }
    .logInfo{
      margin: 1rem 1rem 0;
      border-radius: 0.5rem;
      background: #fff;
      padding-bottom: 1rem;
    }
    .logInfo>p{
      padding-left:5%;
      line-height:4rem;
      font-size:2rem;
      color:#F67003;
    }
    .logInfo>div{
      display:flex;
      align-items: center;
      padding:0 5%;
      height:3rem;
      }
    .logInfo>div>span{
      flex:0 0 50%;
      box-sizing: border-box;
      font-size:1.2rem;
      color:#999;

    }
    .logInfo>div>span>span{
      color:#333;
    }
    .logInfo>div.timeBox>span{
      flex:1;
    }
    .head {
    padding: 1.2rem 0rem 1.2rem 0rem;
    background-color: #333333;
    font-size: 2rem;
    color: #FFFFFF;
}

.head .left {
    padding: 0rem 0rem 0rem 0.34rem;
    position: absolute;
    color: #FFFFFF;
}

.Personal-title {
    text-align: center;
}

.left_arrow {
    border-bottom: 0.25rem #FFFFFF solid;
    border-left: 0.25rem #FFFFFF solid;
    display: block;
    margin-top: 0.1rem;
    margin-left: 0.8rem;
    width: 1.5rem;
    height: 1.5rem;
    transform: rotate(45deg);
    -ms-transform: rotate(45deg);
    -moz-transform: rotate(45deg);
    -webkit-transform: rotate(45deg);
    -o-transform: rotate(45deg);
}
  </style>
  <script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js"></script>
  <script type="text/javascript" src="/static/front/js/comm/util.js"></script>
  <script type="text/javascript" src="/static/front/app/js/rem2.js"></script>
  <script type="text/javascript" src="/static/front/app/js/vue.js"></script>
</head>
<body>
    <div class="head">
        <a href="/m/ryb/index.html" class="left">
            <i class="left_arrow"></i>
        </a>
        <div class="Personal-title">RYB钱包</div>
    </div>
  <div class='purseBox'>
    <div class='left'>
      <div>自由RYB：<font id="rybFlow">0.000000</font></div>
      <div>冻结RYB：<font id="rybLock">0.000000</font></div>
      <div>受限RYB：<font id="rybFree">0.000000</font></div>
    </div>
    <div class='right'>
      <span onclick="window.location.href ='${oss_url}/m/ryb/transferAccounts.html'">转账</span>
    </div>
  </div>
  <div id="body">
	  <div v-for="(item,i) in content" class='logInfo'>
	    <p>钱包ID：{{item.walletId}}</p>
	    <div><span><span>原始锁仓：</span>{{item.currency}}</span><span><span>昨日释放：</span>{{item.toDayRelease}}</span></div>
	    <div><span><span>剩余锁仓：</span>{{item.lastCurrency}}</span><span><span>累计释放：</span>{{item.totalRelease}}</span></div>
	    <div><span><span>锁仓周期：</span>{{item.lockCycle}}天</span><span><span>已&nbsp;释&nbsp;放：</span>{{item.alreadyRelease}}天</span></div>
	    <div class='timeBox'><span><span>锁仓时间：</span>{{item.lockDate}}</span></div>
	  </div>
  </div>
</body>
    <script type="text/javascript">
    	$(function(){
    		var ryb = new Vue({
    			el : '#body',
    			data : {
    				content : []
    			}
    		});
    		$.post("/api/trading/getjson2.html",{"uri":"/api/app/goToRyb"},function(obj){
    			console.log(obj);
    			ryb.content = obj.data;
    		});
    		var tid = '${tid}';
            $.post("/api/trading/getjson2.html",{"tid":tid,"uri":"/api/app/getCoinTotal"},function(data){
            	if(data.data){
                $("#rybLock").html(util.numFormat(parseFloat(data.data.rybLock),6));
                $("#rybFlow").html(util.numFormat(parseFloat(data.data.rybFlow),6));
                $("#rybFree").html(util.numFormat(parseFloat(data.data.rybFree),6));
            	}
            });
    	});
    </script>
</html>