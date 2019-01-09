<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../comm/include.inc.jsp"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>

<!DOCTYPE html>
<html lang="ko" class=" js ">
<head> 
<base href="${basePath}"/>

<%@include file="../comm/link.inc.jsp" %>

<link rel="stylesheet" href="/static/front/css/user/login.css" type="text/css"></link>
<style type="text/css">
.login-body-full {
    background: url("/static/front/images/user/login-bg.png") no-repeat 50% 0;
    height: 760px;
    padding-top: 50px;
}
.container-full{
    min-height: auto !important;
}
.sendVoice{
    color: #333;
    margin-bottom: 10px;
}
.sendVoice>label{
    width: auto;
    cursor: pointer;
    margin-right: 15px;
}
.sendVoice input[type="radio"]{
    margin-right: 5px;
    vertical-align: middle;
    margin-top: 0;
}
.login-box{
    position: relative;
}
.login-block{
    width: 380px;
    position: absolute;
    top: 80px;
    right: 0;
    color: #fff;
    left: auto;
}
.login-title{
    width: 490px;
    position: absolute;
    top: 230px;
    left: 0;
}
.login-bg {
    position: absolute;
    height: 100%;
    width: 100%;
    z-index: 0;
    background: #fff;
    border-radius: 6px;
    -webkit-border-radius: 6px;
    -moz-border-radius: 6px;
    -ms-border-radius: 6px;
    -o-border-radius: 6px;
}
.login-cn {
    position: relative;
    z-index: 1;
    padding: 25px;
    padding-bottom: 15px;
    color: #333;
}
.login .login-ipt {
    border-radius: 0;
    padding: 0 12px 0 45px;
    line-height: 40px;
    height: 40px;
}
.login .ipt-icon {
    width: 21px;
    height: 21px;
    position: absolute;
    top: 10px;
    left: 13px;
}
.login .btn-login{
    line-height: 40px;
    height: 40px;
}
.login .form-group{
    margin-bottom: 10px;
}
.register label{
    color: #333;
}
.register .register-areacode {
    height: 38px;
    line-height: 38px;
    left: 1px;
    top: 1px;
}
.register .register-msg,.register .register-imgmsg {
    height: 38px;
    line-height: 38px;
    top: 1px;
    right: 1px;
}
.next-mars{
    width: 100%;
    height: 40px;
    position: absolute;
    left: 0;
    bottom: 52px;
    z-index: 10;
    display: none;
}
.register .text-white{
    color: #333;
}
</style>
</head>
<body >

<%@include file="../comm/headerIndex.jsp" %>

<script type="text/javascript">
var headerpath = window.location.pathname;
var selectMenu = "trade";
var lis = $(".nav li") ;
if(headerpath.startWith("/index.html") || selectMenu.startWith("index")
 || headerpath.startWith("/service/") || headerpath.startWith("/question/")){
    lis.eq(0).addClass("white") ;
}
else if(headerpath.startWith("/trade/")){
    lis.eq(1).addClass("white") ;
}
else if(headerpath.startWith("/financial/")||headerpath.startWith("/account/") 
|| headerpath.startWith("/crowd/logs") || headerpath.startWith("/introl/")||headerpath.startWith("/divide/")
|| headerpath.startWith("/lottery/logs")|| headerpath.startWith("/exchange/index")|| headerpath.startWith("/shop/myorder")|| headerpath.startWith("/shop/address")){
    lis.eq(2).addClass("white") ;

}
else if(headerpath.startWith("/market.html")){
    lis.eq(3).addClass("white") ;
}
else if(headerpath.startWith("/user/")){
    lis.eq(4).addClass("white") ;
}
else if(headerpath.startWith("/about/")){
    lis.eq(5).addClass("white") ;
}
</script>

<div class="container-full login-body-full main-con">
        <div class="container login-box" style="height:800px;">
            <!-- <img class="login-title" src="/static/front/images/user/login-title.png"> -->
            <div class="login-block login register">
                <div class="login-bg"></div>
                <div class="login-cn">
                        <div class="form-group">
	                        <h3 class="margin-top-clear login-cn-title">手机注册</h3>
	                    </div>
                        <!--<div class="form-group login-header">
                            <span class="register-tab active" data-show="register-phone"
                                data-hide="register-email" data-type="0"><i
                                class="register-icon icon-phone"></i>手机注册<i class="split"></i>
                            </span>  <span class="register-tab" data-show="register-email"
                                data-hide="register-phone" data-type="1"><i
                                class="register-icon icon-email"></i>邮箱注册</span>
                        </div> -->

                        <div class="form-group register-phone">
                            <select class="form-control login-ipt" id="register-areaCode" style="padding-left: 12px;" onchange="changeCountry()">
                               <!--  <option value="940">阿布哈兹(Abkhazia)</option>
                                <option value="93">阿富汗(Afghanistan)</option>
                                <option value="355">阿尔巴尼亚(Albania)</option>
                                <option value="213">阿尔及利亚(Algeria)</option> -->
                                <option value="1">美国(America)(敬请期待)</option>
                               <!--  <option value="376">安道尔(Andorra)</option>
                                <option value="244">安哥拉(Angola)</option>
                                <option value="1264">安圭拉岛(Anguilla)</option>
                                <option value="1268">安提瓜(Antigua)</option>
                                <option value="54">阿根廷(Argentina)</option>
                                <option value="374">亚美尼亚(Armenia)</option>
                                <option value="297">阿鲁巴(Aruba)</option>
                                <option value="61">澳大利亚(Australia)</option>
                                <option value="43">奥地利(Austria)</option>
                                <option value="994">阿塞拜疆(Azerbaijan)</option>
                                <option value="973">巴林(Bahrain)</option>
                                <option value="880">孟加拉国(Bangladesh)</option>
                                <option value="1246">巴巴多斯(Barbados)</option>
                                <option value="375">白俄罗斯(Belarus)</option>
                                <option value="32">比利时(Belgium)</option>
                                <option value="501">伯利兹(Belize)</option>
                                <option value="229">贝宁(Benin)</option>
                                <option value="1441">百慕大(Bermuda)</option>
                                <option value="975">不丹(Bhutan)</option>
                                <option value="591">玻利维亚(Bolivia)</option>
                                <option value="267">博茨瓦纳(Botswana)</option>
                                <option value="55">巴西(Brazil)</option>
                                <option value="673">文莱(Brunei Darussalam)</option>
                                <option value="359">保加利亚(Bulgaria)</option>
                                <option value="226">布基纳法索(Burkina Faso)</option>
                                <option value="95">缅甸(Burma)</option>
                                <option value="257">布隆迪(Burundi)</option>
                                <option value="855">柬埔寨(Cambodia)</option>
                                <option value="237">喀麦隆(Cameroon)</option> -->
                                <option value="1">加拿大(Canada)(敬请期待)</option>
                               <!--  <option value="238">佛得角(Cape Verde)</option>
                                <option value="1345">开曼群岛(Cayman Islands)</option>
                                <option value="235">乍得(Chad)</option>
                                <option value="56">智利(Chile)</option> -->
                                <option value="86" selected="selected">中国大陆(China)</option>
                               <!--  <option value="57">哥伦比亚(Colombia)</option>
                                <option value="269">科摩罗和马约特(Comoros)</option>
                                <option value="682">库克群岛(Cook Islands)</option>
                                <option value="506">哥斯达黎加(Costa Rica)</option>
                                <option value="385">克罗地亚(Croatia)</option>
                                <option value="53">古巴(Cuba)</option>
                                <option value="357">塞浦路斯(Cyprus)</option>
                                <option value="420">捷克共和国(Czech Republic)</option>
                                <option value="45">丹麦(Denmark)</option>
                                <option value="253">吉布提(Djibouti)</option>
                                <option value="1767">多米尼克(Dominica)</option>
                                <option value="593">厄瓜多尔(Ecuador)</option>
                                <option value="20">埃及(Egypt)</option>
                                <option value="503">萨尔瓦多(El Salvador)</option>
                                <option value="291">厄立特里亚(Eritrea)</option>
                                <option value="372">爱沙尼亚(Estonia)</option>
                                <option value="251">埃塞俄比亚(Ethiopia)</option>
                                <option value="298">法罗群岛(Faroe Islands)</option>
                                <option value="679">斐济(Fiji)</option>
                                <option value="358">芬兰(Finland)</option> -->
                                <option value="33">法国(France)(敬请期待)</option>
                              <!--   <option value="241">加蓬(Gabon)</option>
                                <option value="995">格鲁吉亚(Georgia)</option> -->
                                <option value="49">德国(Germany)(敬请期待)</option>
                               <!--  <option value="233">加纳(Ghana)</option>
                                <option value="350">直布罗陀(Gibraltar)</option>
                                <option value="30">希腊(Greece)</option>
                                <option value="299">格陵兰(Greenland)</option>
                                <option value="1473">格林纳达(Grenada)</option>
                                <option value="502">危地马拉(Guatemala)</option>
                                <option value="224">几内亚(Guinea)</option>
                                <option value="245">几内亚比绍(Guinea-Bissau)</option>
                                <option value="592">圭亚那(Guyana)</option>
                                <option value="509">海地(Haiti)</option>
                                <option value="504">洪都拉斯(Honduras)</option> -->
                                <option value="852">香港(Hong Kong)(敬请期待)</option>
                               <!--  <option value="36">匈牙利(Hungary)</option>
                                <option value="354">冰岛(Iceland)(敬请期待)</option> -->
                                <option value="91">印度(India)(敬请期待)</option>
                               <!--  <option value="62">印度尼西亚(Indonesia)</option>
                                <option value="98">伊朗(Iran)</option>
                                <option value="964">伊拉克(Iraq)</option>
                                <option value="353">爱尔兰(Ireland)</option>
                                <option value="972">以色列(Israel)</option> -->
                                <option value="39">意大利(Italy)(敬请期待)</option>
                               <!--  <option value="1876">牙买加(Jamaica)</option> -->
                                <option value="81">日本(Japan)(敬请期待)</option>
                               <!--  <option value="962">约旦(Jordan)</option>
                                <option value="254">肯尼亚(Kenya)</option>
                                <option value="850">北韓(Korea, North)</option>
                                <option value="82">南韓(Korea, South)</option>
                                <option value="965">科威特(Kuwait)</option>
                                <option value="996">吉尔吉斯斯坦(Kyrgyzstan)</option>
                                <option value="856">老挝(Laos)</option>
                                <option value="371">拉脱维亚(Latvia)</option>
                                <option value="961">黎巴嫩(Lebanon)</option>
                                <option value="266">莱索托(Lesotho)</option>
                                <option value="231">利比里亚(Liberia)</option>
                                <option value="218">利比亚(Libya)</option>
                                <option value="423">列支敦士登(Liechtenstein)</option>
                                <option value="370">立陶宛(Lithuania)</option>
                                <option value="352">卢森堡(Luxembourg)</option> -->
                                <option value="853">澳门(Macao)(敬请期待)</option>
                                <option value="261">马达加斯加(Madagascar)(敬请期待)</option>
                                <option value="265">马拉维(Malawi)(敬请期待)</option>
                             <!--    <option value="60">马来西亚(Malaysia)</option>
                                <option value="960">马尔代夫(Maldives)</option>
                                <option value="223">马里(Mali)</option>
                                <option value="356">马耳他(Malta)</option>
                                <option value="596">马提尼克岛(Martinique)</option>
                                <option value="222">毛里塔尼亚(Mauritania)</option>
                                <option value="230">毛里求斯(Mauritius)</option> -->
                                <option value="52">墨西哥(Mexico)(敬请期待)</option>
                              <!--   <option value="373">摩尔多瓦(Moldova)</option>
                                <option value="377">摩纳哥(Monaco)</option>
                                <option value="976">蒙古(Mongolia)</option>
                                <option value="1664">蒙特塞拉特(Montserrat)</option>
                                <option value="212">摩洛哥(Morocco)</option>
                                <option value="258">莫桑比克(Mozambique)</option>
                                <option value="264">纳米比亚(Namibia)</option>
                                <option value="674">瑙鲁(Nauru)</option>
                                <option value="977">尼泊尔(Nepal)</option>
                                <option value="31">荷兰(Netherlands)</option>
                                <option value="687">新喀里多尼亚(New Caledonia)</option>
                                <option value="64">新西兰(New Zealand)</option>
                                <option value="505">尼加拉瓜(Nicaragua)</option>
                                <option value="227">尼日尔(Niger)</option>
                                <option value="234">尼日利亚(Nigeria)</option>
                                <option value="47">挪威(Norway)</option>
                                <option value="968">阿曼(Oman)</option>
                                <option value="92">巴基斯坦(Pakistan)</option>
                                <option value="680">帕劳(Palau)</option>
                                <option value="507">巴拿马(Panama)</option>
                                <option value="595">巴拉圭(Paraguay)</option>
                                <option value="51">秘鲁(Peru)</option> -->
                               <!--  <option value="63">菲律宾(Philippines)</option>
                                <option value="48">波兰(Poland)</option> -->
                                <option value="351">葡萄牙(Portugal)(敬请期待)</option>
                               <!--  <option value="974">卡塔尔(Qatar)</option>
                                <option value="262">留尼汪(Reunion)</option>
                                <option value="40">罗马尼亚(Romania)</option> -->
                                <option value="7">俄罗斯(Russia)、哈萨克斯坦(敬请期待)</option>
                               <!--  <option value="250">卢旺达(Rwanda)</option>
                                <option value="1758">圣卢西亚(Saint Lucia)</option>
                                <option value="685">萨摩亚(Samoa)</option>
                                <option value="378">圣马力诺(San Marino)</option>
                                <option value="966">沙特阿拉伯(Saudi Arabia)</option>
                                <option value="221">塞内加尔(Senegal)</option>
                                <option value="248">塞舌尔(Seychelles)</option>
                                <option value="232">塞拉利昂(Sierra Leone)</option> -->
                                <option value="65">新加坡(Singapore)(敬请期待)</option>
                               <!--  <option value="421">斯洛伐克(Slovakia)</option>
                                <option value="386">斯洛文尼亚(Slovenia)</option>
                                <option value="677">所罗门群岛(Solomon Islands)</option>
                                <option value="252">索马里(Somalia)</option>
                                <option value="27">南非(South Africa)</option>
                                <option value="34">西班牙(Spain)</option>
                                <option value="94">斯里兰卡(Sri Lanka)</option>
                                <option value="249">苏丹(Sudan)</option>
                                <option value="597">苏里南(Suriname)</option>
                                <option value="268">斯威士兰(Swaziland)</option>
                                <option value="46">瑞典(Sweden)</option>
                                <option value="41">瑞士(Switzerland)</option>
                                <option value="963">叙利亚(Syria)</option> -->
                                <option value="886">台灣(Taiwan)(敬请期待)</option>
                              <!--   <option value="992">塔吉克斯坦(Tajikistan)</option>
                                <option value="255">坦桑尼亚(Tanzania)</option>
                                <option value="66">泰国(Thailand)</option>
                                <option value="1242">巴哈马(The Bahamas)</option>
                                <option value="220">冈比亚(The Gambia)</option>
                                <option value="228">多哥(Togo)</option>
                                <option value="676">汤加(Tonga)</option>
                                <option value="216">突尼斯(Tunisia)</option>
                                <option value="90">土耳其(Turkey)</option>
                                <option value="993">土库曼斯坦(Turkmenistan)</option>
                                <option value="256">乌干达(Uganda)</option>
                                <option value="380">乌克兰(Ukraine)</option> -->
                                <option value="44">英国(United Kingdom)(敬请期待)</option>
                              <!--   <option value="598">乌拉圭(Uruguay)</option>
                                <option value="998">乌兹别克斯坦(Uzbekistan)</option>
                                <option value="678">瓦努阿图(Vanuatu)</option>
                                <option value="58">委内瑞拉(Venezuela)</option>
                                <option value="84">越南(Vietnam)</option>
                                <option value="967">也门(Yemen)</option>
                                <option value="260">赞比亚(Zambia)</option>
                                <option value="263">津巴布韦(Zimbabwe)</option> -->
                            </select>
                        </div>
                        <div class="form-group register-phone">
                            <span id="register-phone-areacode" class="btn btn-areacode register-areacode">+86</span>
                            <input id="register-phone" class="form-control login-ipt padding-left-92" onchange="checkMobile()" type="text" placeholder="手机号码">
                        </div>
                        <div class="form-group register-email">
                        <label class="ipt-icon pas" for="register-imgcode"></label>
                            <input id="register-email" class="form-control login-ipt" type="text" placeholder="邮箱地址">
                        </div>
                        <div class="form-group ">
                        <label class="ipt-icon code" for="register-imgcode"></label>
                            <input id="register-imgcode" class="form-control login-ipt" type="text" placeholder="验证码">
                            <img class="btn btn-imgcode register-imgmsg" src="/servlet/ValidateImageServlet?r=1520408443451"></img>
                        </div>
                        <div class="form-group register-phone">
                        <label class="ipt-icon code" for="register-imgcode"></label>
                            <input id="register-msgcode" class="form-control login-ipt" type="text" placeholder="短信验证码">
                            <input id="sendVoice" type="hidden" value="false">
                            <!-- <button id="register-sendmessage" onclick="sendVoice(true)" data-msgtype="12" data-tipsid="" class="btn btn-sendmsg register-msg" style="right: 106px">发送语音</button> -->
                            <button id="register-sendmessage" onclick="sendVoice()" data-msgtype="12" data-tipsid="register-errortips" class="btn btn-sendmsg register-msg">发送验证码</button>
                        </div>
                        <div class="sendVoice">您希望以何种方式发送：
	                        <input name="sendcode" checked="checked" type="radio" id="sms"><label for="sms">短信</label>
	                        <input name="sendcode" type="radio" id="voice"><label for="voice">语音</label>
                        </div>                      
                        <div class="form-group register-email">
                        <label class="ipt-icon code" for="register-imgcode"></label>
                            <input id="register-email-code" class="form-control login-ipt" type="text" placeholder="邮箱验证码">
                            <button id="register-sendemail" data-msgtype="3" data-tipsid="register-errortips" class="btn btn-sendemailcode register-msg">发送验证码</button>
                        </div>
                        <div class="form-group ">
                        <label class="ipt-icon pas" for="register-imgcode"></label>
                            <input id="register-password" class="form-control login-ipt" type="password" placeholder="密码">
                        </div>
                        <div class="form-group ">
                        <label class="ipt-icon pas" for="register-imgcode"></label>
                            <input id="register-confirmpassword" class="form-control login-ipt" type="password" placeholder="确认密码">
                        </div>
                         <%   
                              String r = request.getParameter("r");
                              request.setAttribute("r", r);
                         %>
                        <div class="form-group ">
                        <label class="ipt-icon code" for="register-imgcode"></label>
                            <input id="register-intro" class="form-control login-ipt" type="text" value="${r }" placeholder="邀请码(选填)">
                        </div>
                    
                        <div class="checkbox">
                            <label>
                                <input id="agree" type="checkbox">
                                阅读并同意
                                <a target="_blank" href="/about/index.html?id=4" class="text-danger">《RYH钱包用户协议》</a>
                            </label>
                        </div>
                        <div class="form-group ">
                            <span id="register-errortips" class="text-danger"></span>
                        </div>
                        <div class="form-group ">
                            <button id="register-submit" class="btn btn-danger btn-block btn-login">注册</button>
                        </div>
                        <div class="next-mars "></div>
                        <div class="form-group">
                            <span class="text-white">已有帐号？</span>
                            <a href="${oss_url}/user/login.html" class="text-danger">直接登录>></a>
                        </div>
                    </div>
            </div>
        </div>
    </div>
    
    <%@include file="../comm/footer.jsp" %>
    
    <input id="regType" type="hidden" value="0">
    <input id="intro_user" type="hidden" value="${intro }">
    <script type="text/javascript" src="${oss_url}/static/front/js/comm/msg.js"></script>
    <script type="text/javascript" src="${oss_url}/static/front/js/user/register.js"></script>
</body>
<script type="text/javascript">
 $(function() {
	$("#register-confirmpassword").on('mouseleave',function(){
		var pwd =  $("#register-password").val();
		var rePwd =  this.value;
		if(pwd != rePwd){
			util.showerrortips("register-errortips", "两次密码输入不一致");
            return false;
		}else {
			util.hideerrortips("register-errortips");
		}
	})
})
function sendVoice () {
	if ($(".sendVoice input[type='radio']:checked").attr("id") == "sms") {
		$("#sendVoice").val("false");
	} else {
		$("#sendVoice").val("true");
		
	}
	return true;
}
 function checkMobile(){ 
	 var sMobile = $("#register-phone").val();
	 if(!(/^1[3|4|5|6|7|8|9][0-9]\d{8}$/.test(sMobile))){ 
	     alert("请输入正确的手机号"); 
	     $("#register-phone").val("");
	     return false; 
	     } 
	 }
 function changeCountry(){
	 var Country=$("#register-areaCode").val();
	 if(Country != 86){
		 alert("敬请期待");
		 $("#register-areaCode").val(86);
	 } 
 }
</script>
</html>
