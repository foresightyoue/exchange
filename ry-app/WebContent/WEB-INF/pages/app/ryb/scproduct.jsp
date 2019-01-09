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
        <title>锁仓产品</title>
        <link rel="stylesheet" href="/static/front/app/css/css.css" />
        <link rel="stylesheet" href="/static/front/app/css/style.css" />
        <style>
			.goalsInfo{
				background:#EBA51D ;
				border-radius:0.4rem;
				margin:0.5rem;
				color:#fff;
			}
			.goalsInfo p{
				height:2rem;
				display:flex;
				align-items:Center;
			}
			.goalsInfo p .first{
				flex:0 0 30%;
			}
			.goalsInfo p .first.one{
				box-sizing:border-box;
				padding-left:1rem;
			}
			.last{
				display:flex;
				align-items:center;
				justify-content:flex-end;
			}
			.second{
				display:flex;
				align-items:center;
				justify-content:center;
			}
			.proInfo{
			    flex: 0 0 50%;
			    height: 0.3rem;
			    background: #D7BF92;
			    margin-right: 5%;
			}
			.proInfo>span{
				height:100%;
				background:#F67003 ;
				width:50%;
				display:block;
			}
			.fillCard{
				margin-top:1rem;
				padding:0.5rem;
				display:flex;
				background:#fff;
				margin-bottom:1rem;
			}
			.fillCard>span{
				margin-right:0.5rem;
				line-height: 3rem;
			}
			.fillCard>div{
				width:60%;
				border-radius:0.5rem;
				border:1px solid #efefef;
				padding:0.5rem;
			}
			.fillCard>div>p{
				line-height:2rem;
				color:#666;
			}
			.buyNumBox{
				height:3rem;
				display:flex;
				padding:0 0.5rem;
				align-items:Center;
				background:#fff;
				margin-bottom:2rem;
			}
			.buyNumBox>span{
				margin-right:0.5rem;
			}
			.buyNumBox>input{
				flex:0 0 60%;
			}
			.nextBtn{
			    display: block;
			    width: 90%;
			    background: #EBA51D;
			    color: #fff;
			    height: 2.5rem;
			    line-height: 2.5rem;
			    margin: auto;
			    text-align: Center;
			    border-radius: 0.2rem;
			}
        </style>
        <script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js" ></script>
        <script type="text/javascript" src="/static/front/app/js/bankCardAttribution.js"></script>
    </head>
    <body>
        <nav>
            <div class="Personal-title">
                <span>
                    <a href="javascript:;" onClick="javascript :history.back(-1)">
                        <em>
                            <i></i>
                            <i></i>
                        </em>
                        <strong>返回</strong>
                    </a>
                </span><font id="projectName">锁仓产品</font>
            </div>
        </nav>
        <div class='goalsInfo'>
            <p><span class='first one'>目标金额:</span><span class='first '>1000,000.00</span><span class='first last'>锁定<font id="releaseDay">0</font>天</span></p>
            <p><span class='first one'>最低购买</span><span class='first '><font id="smallCurrency">0</font></span></p>
            <p><span class='first one'>完成度</span><span class='proInfo'><span class='colorBar'></span></span><span class='percent'>50%</span></p>
        </div>
        <div class='fillCard'>
            <span>项目介绍</span><div><p>项目介绍。。。。。。</p><p>项目介绍。。。。。。</p></div>
        </div>
        <div class='buyNumBox' style="height: 2.5rem">
            <span>购买数量：</span><input type="number" id=num name="num" placeholder='请输入购买数量'>
        </div>
        
        <span class='nextBtn'>确定</span>


<script type="text/javascript" src="${oss_url}/static/front/js/comm/util.js"></script>
<script type="text/javascript" src="/static/front/app/js/index.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/plugin/layer/layer.js"></script>
<script type="text/javascript">
$(function(){
    var tid1 = "${fid}";
    $.post("/api/trading/getjson2.html",{"uri":"/api/app/getLockProduct"},function(data){
        console.log(data);
        for(var i = 0 ;i<data.data.length;i++){
            if(data.data[i].tid = tid1){
                $("#projectName").html(data.data[i].projectName);
                $("#smallCurrency").html(data.data[i].smallCurrency);
                $("#releaseDay").html(data.data[i].releaseDay);
            }
        }
    });
});
</script>
</body>
</html>
