<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../comm/include.inc.jsp"%>
<%
	String path = request.getContextPath();
            String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                    + path;
%>

<!doctype html>
<html>
<head> 
<base href="${basePath}"/> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<base href="${basePath}"/>
<%@include file="../comm/link.inc.jsp"%>

<link rel="stylesheet" href="${oss_url}/static/front/css/finance/index.css" type="text/css"></link>
<style type="text/css">
.font-size-30 {
    font-size: 20px !important;
}
.top-icon {
    padding-top: 20px;
}
</style>
</head>



<body>

	<%@include file="../comm/header.jsp"%>

	<div class="container-full main-con">
		<div class="container displayFlex">
			<div class="row">
				<%@include file="../comm/left_menu.jsp"%>

				<div class="col-xs-10 padding-right-clear">
					<div
						class="col-xs-12 padding-right-clear padding-left-clear rightarea user">
						<div class="col-xs-12 rightarea-con ">
							<div class="index-top-icon clearfix">
								<div class="col-xs-4 top-icon">
									<div class="col-xs-12">
										<span class="assets-icon"><i class="gross"></i>
											总资产(BTC)</span>
										<div class="col-xs-12" style="margin-top: 20px;">
											<span class="text-danger font-size-30"><fmt:formatNumber
													value="${totalCapitalTrade}" pattern="${totalCapitalTrade==0?'00.000000':'0.000000'}"
													maxIntegerDigits="20" maxFractionDigits="6" /></span> <span
												id="rmb" class="text-danger font-size-30"><br>≈<fmt:formatNumber
													value="${exchangerate}" pattern="${exchangerate==0?'00.000000':'0.000000'}"
													maxIntegerDigits="20" maxFractionDigits="2" />CNY
											</span>
										</div>
									</div>
									<%-- <div class="col-xs-12" style="margin-top: 20px;">
									<span class="text-danger font-size-30">
									<fmt:formatNumber
										value="${totalCapitalTrade }" pattern="##.##"
										maxIntegerDigits="20" maxFractionDigits="4" />
									</span>
								</div> --%>
									<span class="split"></span>
								</div>

								<%-- <div class="col-xs-4 top-icon">
								<div class="col-xs-12">
									<span class="assets-icon"><i class="net"></i>可用美元($)</span>
								</div>
								<div class="col-xs-12" style="margin-top: 20px;">
								<span class="text-danger font-size-30">
								<fmt:formatNumber
										value="${fwallet.ftotal }" pattern="##.##"
										maxIntegerDigits="20" maxFractionDigits="4" />
								</span>
								</div>
								<span class="split"></span>
							</div> --%>
								<!-- <div class="col-xs-4 text-center top-btn">
								<a href="/account/withdrawCny.html" class="btn ">提现</a>
								<a href="/account/rechargeCny.html" class="btn btn-re">充值</a>
							</div> -->
							</div>
							<div class="col-xs-12 padding-clear padding-top-30">
								<div  class="screen" style="margin-bottom: 15px;vertical-align:middle;">
									<input id="screen" type="checkbox" style="vertical-align:middle;margin-top: 0;">
									<span style="margin-left: 6px;vertical-align:middle;">隐藏资产为0的币种</span>
								</div>
								<table class="table table-striped text-center">
									<tr class="bg-gary">
										<td class="border-top-clear">币种名称</td>
										<td class="border-top-clear">可用资产</td>
										<td class="border-top-clear">冻结资产</td>
										<td class="border-top-clear">总量</td>
										<%--<td class="border-top-clear">估值($)</td> --%>
										<td class="border-top-clear">操作</td>
										<td class="border-top-clear">交易</td>
									</tr>
									<%-- 	<tr>
							<td class="border-top-clear">美元</td>
							<td class="border-top-clear"><fmt:formatNumber value="${fwallet.ftotal }" pattern="##.##" maxIntegerDigits="20" maxFractionDigits="4"/></td>
							<td class="border-top-clear"><fmt:formatNumber value="${fwallet.ffrozen }" pattern="##.##" maxIntegerDigits="20" maxFractionDigits="4"/></td>
							<td class="border-top-clear"><fmt:formatNumber value="${fwallet.ftotal+fwallet.ffrozen }" pattern="##.##" maxIntegerDigits="20" maxFractionDigits="4"/></td>
							<td class="border-top-clear"><fmt:formatNumber value="${fwallet.ftotal+fwallet.ffrozen }" pattern="##.##" maxIntegerDigits="20" maxFractionDigits="4"/></td>
							<td class="border-top-clear">
		                        <a href="/account/rechargeCny.html"><span class="label label-default">充值</span></a>
								<a href="/account/withdrawCny.html"><span class="label label-success">提现</span></a>
                            </td>
                            <td class="border-top-clear">
                            </td>
						</tr>	 --%>
									<c:forEach items="${fvirtualwallets }" var="v" varStatus="vs"
										begin="0">
										<tr id="${v.value.fvirtualcointype.fid }" >
											<td class="border-top-clear">${v.value.fvirtualcointype.fShortName }</td>
											<td class="border-top-clear"><fmt:formatNumber
													value="${v.value.ftotal }" pattern="${v.value.ftotal==0?'00.0000':'0.0000'}"
													maxIntegerDigits="20" maxFractionDigits="4" /></td>
											<td class="border-top-clear"><fmt:formatNumber
													value="${v.value.ffrozen }" pattern="${v.value.ffrozen==0?'00.0000':'0.0000'}"
													maxIntegerDigits="20" maxFractionDigits="4" /></td>
											<td class="border-top-clear"><fmt:formatNumber
													value="${v.value.ftotal+v.value.ffrozen }" pattern="${v.value.ftotal+v.value.ffrozen==0?'00.0000':'0.0000'}"
													maxIntegerDigits="20" maxFractionDigits="4" /></td>
											<!--  <td class="border-top-clear"><fmt:formatNumber value="${(v.value.ftotal+v.value.ffrozen)*v.value.fprice }" pattern="00.00" maxIntegerDigits="20" maxFractionDigits="4"/></td>
							-->
											<td class="border-top-clear"><c:choose>
													<c:when test="${v.value.fvirtualcointype.fisrecharge==true}">
														<a href="${oss_url}/account/rechargeBtc.html?symbol=${v.value.fvirtualcointype.fid }"><span
															class="label label-success">充值</span></a>
													</c:when>
													<c:otherwise>
														<!-- <a class="label-alert"><span class="label label-default">充值</span></a> -->
														<span class="label label-default">充值</span>
													</c:otherwise>
												</c:choose>
												<c:choose>
													<c:when test="${v.value.fvirtualcointype.FIsWithDraw==true}">
														<a href="/account/withdrawBtc.html?symbol=${v.value.fvirtualcointype.fid }"><span
															class="label label-success">提现</span></a>
												<%-- 			<c:choose>
																<c:when test="${v.value.fvirtualcointype.fid==7}">
																	<a href="javaScript:" onclick="choice(${v.value.fvirtualcointype.fid })">
																		<span class="label label-success">划转</span>
																	</a>
															</c:when>
																<c:otherwise>
																	<a href="/account/transfer.html?symbol=${v.value.fvirtualcointype.fid }">
																		<span class="label label-success">划转</span>
																	</a>
																</c:otherwise>
															</c:choose> --%>
															 <a href="/account/transfer.html?symbol=${v.value.fvirtualcointype.fid }">
                                                                        <span class="label label-success">划转</span>
                                                                    </a>
													</c:when>
													<c:otherwise>
														<span class="label label-default">提现</span>
														<span class="label label-default">划转</span>
													</c:otherwise>
												</c:choose></td>
											<td class="border-top-clear"><a
												class="btn market-trading"
												href="/trade/coin.html?coinType=${v.value.ftradempingid }&tradeType=0">去交易<i
													class="iconfont">&#xe64f;</i></a></td>
										</tr>
									</c:forEach>

								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>



	<%@include file="../comm/footer.jsp"%>

</body>

<script type="text/javascript">
	/* $(".label-alert").on("click",function(){
		var list = document.getElementsByClass("border-top-clear")
		for(var i=0;i<list.length;i++){
			alert(this.list[i].innerHTML);
		}
		alert("暂时无法对 ** 充值");
	}) */
	/* $(".label-alert").on("click",function(){
		alert("暂不支持此币种充值！");
	}) */
	
/* 	function choice(id) {
		
		layer.confirm('请选择要划转的平台', {
			btn: ['优生活', '封神榜']
		,btn2: function(index, layero){
			var src="/account/transform.html?symbol="+id;
			window.location.href=src;
			}
		}, function(index){
			var src="/account/transfer.html?symbol="+id;
			window.location.href=src;
		});
	} */
	
	var login = {

		refreshMarket : function() {
			$(function() {

				//var areas=document.getElementsByClassName("text-danger")[1].dataset.key.toLowerCase();
				//$("#area").val(areas)
				var url = "/financial/indexs.html";

				$.ajax({
					url : "http://api.zb.com/data/v1/ticker?market=btc_qc",
					type : "get",
					data : {},
					dataType : "json",
					success : function(data) {
						//$("#last").val()
						var lasts = data.ticker.last;
						

						$.post(url, {
							last : lasts
						}, function(data) {
							$.each(data, function(key, value) {
							    //
								$("#rmb").html("≈" + value.toFixed(2) + "CNY");

							});
						});
					}
				});

				/*
				
				$.ajax({
					url:"/financial/index.html",
					type:"get",
					data:{},
					dataType:"json",
					success:function(data){
						
						 var lasts=data.ticker.last;
						$("#last").val(data.ticker.last) 
						alert(data.exchangerate);
					}
				});
				 *
				 */

			});

			window.setTimeout(function() {
				login.refreshMarket();
			}, 5000);
		}

	};
	$(function() {
		login.refreshMarket();
		
	});
	$("#screen").change(function(){
        if($("#screen").prop("checked")){
			<c:forEach items="${fvirtualwallets }" var="v" varStatus="vs" begin="0">
				if(${v.value.ftotal==0&&v.value.ffrozen==0}){
		        	$("#"+${v.value.fvirtualcointype.fid }).css("display","none");
				}
			</c:forEach> 
        }else{
        	<c:forEach items="${fvirtualwallets }" var="v" varStatus="vs" begin="0">
        		$("#"+${v.value.fvirtualcointype.fid }).removeAttr("style");
			</c:forEach>
        }
    })
</script>


</html>