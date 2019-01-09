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
<jsp:include page="../comm/link.inc.jsp"></jsp:include>
<link rel="stylesheet" href="${oss_url}/static/front/css/user/kyc.css"
	type="text/css"></link>
</head>
<style>
.skip{
    text-align: right;
    color: white;
    font-size: 14px;
    background-color: #1db3b4;
    padding: 5px 15px;
    border: 0;
    border-radius: 4px;
    -webkit-border-radius: 4px;
    cursor: pointer;
    position: absolute;
    right: 15px;
    z-index: 100;
}
.rezbtn{
    text-align: right;
    color: white;
    font-size: 14px;
    background-color: #1db3b4;
    padding: 5px 15px;
    border: 0;
    border-radius: 4px;
    -webkit-border-radius: 4px;
    cursor: pointer;
    z-index: 100;
    margin-top: 15px;
}
.right .s2{
    color: #da2e22;
    font-size: 16px;
}
.kyc_03 .step_all>div img {
    width: 80px;
}
</style>
<body>


	<jsp:include page="../comm/header.jsp"></jsp:include>


	<div class="container-full main-con">
		<div class="container displayFlex">
			<div class="row">
				<jsp:include page="../comm/left_menu.jsp"></jsp:include>


				<div class="col-xs-10 padding-right-clear">
					<div
						class="col-xs-12 padding-right-clear padding-left-clear rightarea user">
						<div class="col-xs-12 rightarea-con">
							<div class="wrap">
								<div class="section">
									<div class="top">
										<p>【温馨提示】为了全方位保障用户资产安全，请您配合完成相关验证。</p>
									</div>
									<!-- kyc1验证 -->
									<div class="kyc_01">
										<div class="title">
											<span class="s1">KYC1认证</span>
											<c:if
												test="${fuser.fpostRealValidate && fuser.fhasRealValidate}">
												<span class="s2">(已认证)</span>
											</c:if>
											<c:if
												test="${!fuser.fpostRealValidate || !fuser.fhasRealValidate}">
												<span class="s2">(未认证)</span>
											</c:if>
										</div>
										<div class="user_set">
											<div class="lead">
												<p>
													您只需要进行<span class="s1">真实身份认证</span>即可完成KYC1认证
												</p>
											</div>
											<c:if test="${!fuser.fpostRealValidate && !fuser.fhasRealValidate}">
												<button class="skip" value="1">跳过</button>
                                            </c:if>
											<div class="nav_btn">
												<div class="id_card all_card">
													<p class="p1">
														<span><em>Step1</em>身份认证</span>
													</p>
													<p class="p3">
														<c:if test="${fuser.fpostRealValidate }">
                                                ${fuser.frealName }:${fuser.fidentityNo_s }
                                                </c:if>
													</p>
													<p class="p2">
														<c:if
															test="${fuser.fpostRealValidate && !fuser.fhasRealValidate}">
															<span class="step1-btn disabled">审核中</span>
														</c:if>
														<c:if
															test="${fuser.fpostRealValidate && fuser.fhasRealValidate}">
															<span class="step1-btn disabled">已认证</span>
														</c:if>
														<c:if
															test="${!fuser.fpostRealValidate && !fuser.fhasRealValidate}">
															<span class="step1-btn" data-toggle="modal"
																data-target="#bindrealinfo">点击认证</span>
														</c:if>
													</p>
												</div>
											</div>
										</div>
									</div>

									<!-- kyc2验证 -->
									<div class="kyc_02"
										style="display: ${requestScope.constant['isHideKYC'] ==1?'none;':'block;'}">
										<div class="title">
											<span class="s1">KYC2认证</span> <span class="s2">(进行此认证前请务必完成KYC1认证)</span>
										</div>
										<div class="step_all">
											<div class="left">
												<img
													src="${oss_url}/static/front/images/user/step-1_icon_default.png"
													alt="">
												<p>Step 1 本人身份证照片认证</p>
												
											</div>
											<%-- <div class="right">
												<img
													src="${oss_url}/static/front/images/user/step-1_icon_default.png"
													alt="">
												<p>Step 2 本人视频认证</p>
											</div> --%>
											<div class="right">
											<c:if test="${fuser.authGrade == '2' || fuser.authGrade == '3' || fuser.authGrade == '5'}">
											       <c:if test="${fuser.fpostImgValidate && !fuser.fhasImgValidate}">
											            <span class="s2">(身份证照审核中)</span>
											       </c:if>
											       <c:if test="${fuser.fhasImgValidate && fuser.fpostImgValidate}">
											            <span class="s2">(身份证照认证成功)</span>
											       </c:if>
											</c:if>
											<c:if test="${fuser.faudit == '1'}">     
											       <c:if test="${!fuser.fhasImgValidate && !fuser.fpostImgValidate }">
											            <span class="s2">(身份证照认证失败)</span>
											            <button class="renz rezbtn">请重新认证</button>
											       </c:if>
                                                </c:if>
                                            </div>
											<p class="zhu">*正常审核时间为一个工作日，如有问题请 联系客服</p>
										</div>
										</div>
										<c:if test="${fuser.fhasImgValidate &&fuser.fpostBankValidate || fuser.faudit == 2}">
										<div class="kyc_03">
										<div class="title">
										  <span class="s1">KYC3认证</span><span class="s2">(进行此认证前请务必完成KYC2认证)</span>
										</div>
										   <div class="step_all">
                                            <div class="left">
                                                <img
                                                    src="${oss_url}/static/front/images/user/step-1_icon_default.png"
                                                    alt="">
                                                <p>Step 1 本人身份证照片认证</p>
                                                
                                            </div>
                                            <%-- <div class="right">
                                                <img
                                                    src="${oss_url}/static/front/images/user/step-1_icon_default.png"
                                                    alt="">
                                                <p>Step 2 本人视频认证</p>
                                            </div> --%>
                                            <div class="right">
                                            <c:if test="${fuser.authGrade == '3'}">
                                               <c:if test="${fuser.fpostBankValidate && !fuser.fhasBankValidate }">
                                                    <span class="s2">(银行卡照审核中)</span>
                                               </c:if>
                                               <c:if test="${fuser.fpostBankValidate && fuser.fhasBankValidate }">
                                                    <span class="s2">(银行卡照认证成功)</span>
                                               </c:if>
                                              </c:if>
                                              <c:if test="${fuser.faudit == '2'}">
                                               <c:if test="${!fuser.fpostBankValidate && !fuser.fhasBankValidate }">
                                                    <span class="s2">(银行卡照认证失败)</span>
                                                    <button class="renz1 rezbtn" onclick="check()">请重新认证</button>
                                               </c:if>
                                              </c:if>
                                            </div>
                                            <p class="zhu">*正常审核时间为一个工作日，如有问题请 联系客服</p>
										</div>
										</div>
										</c:if>
										<!-- 上传身份证 ---------------------------------------->
										<!-- kyc2验证第一步 -->
										<c:if test="${!fuser.fpostImgValidate&& fuser.faudit == '0'}">
										<%-- <c:if test="${fuser.authGrade == '0' || fuser.authGrade == '1' }"> --%>
										<button class="skip" style="margin-top: 30px; right: 30px;" value="2">跳过</button>
										<div class="idten" style="display:${empty flag == true ?'block;':'none;'}">
											<div class="step_01">
												<div class="sub_title">
													<span class="s1">Step 2</span> <span class="">本人身份证照片认证</span>
												</div>
												<p class="mind">
													请按照要求上传您身份证的正面照片，仅支持<span>jpg、bmp、png</span>格式，大小限制<span>3MB以内</span>。
												</p>
												<div class="up_img_01 all_up">
													<p class="left">本人身份证正面照片</p>
													<div class="user_up">
														<div class="work_info">
															<c:choose>
																<c:when
																	test="${fuser.fhasImgValidate && fuser.fpostImgValidate}">
																	<label for="pic1" class="s1">已认证</label>
																</c:when>
																<c:when
																	test="${!fuser.fhasImgValidate && fuser.fpostImgValidate}">
																	<label for="pic1" class="s1">审核中</label>
																</c:when>
																<c:otherwise>
																	<label class="s1" onclick="checkLevel('pic1')">上传</label>
																</c:otherwise>
															</c:choose>
															<input id="pic1" type="file" onchange="uploadImg1()">
															<input id="pic1Url" type="hidden"> 
															<span class="s2"> 
																<span>未选择文件</span> 
																<em class="pic1name"></em>
															</span> 
															<span class="f2">示例</span>
														</div>
														<div class="imgs">
															<div class="pic1show">
																<c:if test="${fuser.fpostImgValidate}">
																	<img src="${fuser.fIdentityPath }" alt="">
																</c:if>
															</div>
															<div class="exemple">
																<img
																	src="${oss_url}/static/front/images/user/zheng_image.png"
																	alt="">
															</div>
														</div>
													</div>
												</div>
												<div class="up_img_02 all_up">
													<p class="left">本人身份证反面照片</p>
													<div class="user_up">
														<div class="work_info">
															<c:choose>
																<c:when
																	test="${fuser.fhasImgValidate && fuser.fpostImgValidate}">
																	<label for="pic2" class="s1">已认证</label>
																</c:when>
																<c:when
																	test="${!fuser.fhasImgValidate && fuser.fpostImgValidate}">
																	<label for="pic2" class="s1">审核中</label>
																</c:when>
																<c:otherwise>
																	<label  class="s1" onclick="checkLevel('pic2')">上传</label>
																</c:otherwise>
															</c:choose>
															<input id="pic2" type="file" onchange="uploadImg2()">
															<input type="hidden" id="pic2Url"> <span
																class="s2"> <span>未选择文件</span> <em
																class="pic2name"></em></span> <span class="f2">示例</span>
														</div>
														<div class="imgs">
															<div class="pic2show">
																<c:if test="${fuser.fpostImgValidate}">
																	<img src="${fuser.fIdentityPath2 }" alt="">
																</c:if>
															</div>
															<div class="exemple">
																<img
																	src="${oss_url}/static/front/images/user/fan_image.png"
																	alt="">
															</div>
														</div>
													</div>
												</div>
												<div class="up_img_03 all_up">
													<p class="left">手持本人身份证照片</p>
													<div class="user_up">
														<div class="work_info">
															<c:choose>
																<c:when
																	test="${fuser.fhasImgValidate && fuser.fpostImgValidate}">
																	<label for="pic3" class="s1">已认证</label>
																</c:when>
																<c:when
																	test="${!fuser.fhasImgValidate && fuser.fpostImgValidate}">
																	<label for="pic3" class="s1">审核中</label>
																</c:when>
																<c:otherwise>
																	<label class="s1" onclick="checkLevel('pic3')">上传</label>
																</c:otherwise>
															</c:choose>
															<input id="pic3" type="file" onchange="uploadImg3()">
															<input type="hidden" id="pic3Url"> <span
																class="s2"> <span>未选择文件</span> <em
																class="pic3name"></em></span> <span class="f2">示例</span>
														</div>
														<div class="imgs">
															<div class="pic3show">
																<c:if test="${fuser.fpostImgValidate}">
																	<img src="${fuser.fIdentityPath3 }" alt="">
																</c:if>
															</div>
															<div class="exemple">
																<img
																	src="${oss_url}/static/front/images/user/chi_image.png"
																	alt="">
															</div>
														</div>
													</div>
												</div>
											</div>
										</div>
										</c:if>
										<!-- 上传身份证 ---------------------------------------->
										<!-- 上传银行卡 ------------------------------------------>
										<c:if test="${fuser.fhasImgValidate&&fuser.authGrade == '2'&& fuser.faudit == '0'||fuser.faudit == '1'}">
										<div class="bankid" style="display:${empty flag == true ? 'none;':'block;'}">
											<div class="step_01">
												<div class="sub_title">
													<span class="">Step  3</span><span class="">银行卡认证</span>
												</div>
												<div class="up_img_01 all_up">
													<p class="left">银行卡正面照片</p>
													<div class="user_up">
														<div class="work_info">
															<c:choose>
																<c:when
																	test="${fuser.fhasBankValidate && fuser.fpostBankValidate}">
																	<label for="pic" class="s1">已认证</label>
																</c:when>
																<c:when
																	test="${!fuser.fhasBankValidate && fuser.fpostBankValidate}">
																	<label for="pic" class="s1">审核中</label>
																</c:when>
																<c:otherwise>
																	<label for="picBank1" class="s1">上传</label>
																</c:otherwise>
															</c:choose>
															<input id="picBank1" type="file" class="files"
																onchange="uploadBankImg1()"> <input
																id="picBank1Url" type="hidden"> <span><span
																class="s2">未选择文件</span><em
                                                                class="picBank1name"></em></span><span class="f2">示例</span>
														</div>
														<div class="imgs">
															<div class="picBank1show">
																<c:if test="${fuser.fpostBankValidate }">
																	<img src="${fuser.fBankPath}" alt="">
																</c:if>
															</div>
															<div class="exemple">
															  <img 
															    src="${oss_url}/static/front/images/user/bank.png" 
															    alt="">
															</div>
														</div>
													</div>
												</div>
												<div class="up_img_02 all_up">
													<p class="left">银行卡反面照片</p>
													<div class="user_up">
														<div class="work_info">
															<c:choose>
																<c:when
																	test="${fuser.fhasBankValidate && fuser.fpostBankValidate}">
																	<label for="pic" class="s1">已认证</label>
																</c:when>
																<c:when
																	test="${!fuser.fhasBankValidate && fuser.fpostBankValidate}">
																	<label for="pic" class="s1">审核中</label>
																</c:when>
																<c:otherwise>
																	<label for="picBank2" class="s1">上传</label>
																</c:otherwise>
															</c:choose>
															<input id="picBank2" type="file" class="files"
																onchange="uploadBankImg2()"> <input
																type="hidden" id="picBank2Url"> <span class="s2"><span>未选择文件</span><em
                                                                class="picBank2name"></em></span><span class="f2">示例</span>
														</div>
														<div class="imgs">
															<div class="picBank2show">
																<c:if test="${fuser.fpostBankValidate}">
																	<img src="${fuser.fBankPath2 }" alt="">
																</c:if>
															</div>
															<div class="exemple">
															     <img
                                                                    src="${oss_url}/static/front/images/user/bank1.png"
                                                                    alt="">
															</div>
														</div>
													</div>
												</div>
												<div class="up_img_03 all_up">
													<p class="left">手持银行卡照片</p>
													<div class="user_up">
														<div class="work_info">
															<c:choose>
																<c:when
																	test="${fuser.fhasBankValidate && fuser.fpostBankValidate}">
																	<label for="pic" class="s1">已认证</label>
																</c:when>
																<c:when
																	test="${!fuser.fhasBankValidate && fuser.fpostBankValidate}">
																	<label for="pic" class="s1">审核中</label>
																</c:when>
																<c:otherwise>
																	<label for="picBank3" class="s1">上传</label>
																</c:otherwise>
															</c:choose>
															<input id="picBank3" type="file" class="files"
																onchange="uploadBankImg3()"> <input
																type="hidden" id="picBank3Url"> <span class="s2"><span>未选择文件</span><em
                                                                class="picBank2name"></em></span><span class="f2">示例</span>
														</div>
														<div class="imgs">
															<div class="picBank3show">
																<c:if test="${fuser.fpostBankValidate }">
																	<img src="${fuser.fBankPath3 }" alt="">
																</c:if>
															</div>
															<div class="exemple">
															   <img
                                                                    src="${oss_url}/static/front/images/user/bank3.png"
                                                                    alt="">
															</div>
														</div>
													</div>
												</div>
											</div>
										</div>
										</c:if>
										<!-- 上传银行卡 --------------------------------------------->
										<%-- <div class="to_up"
											style="display:${empty flag == true ?'block;':'none;'}"> --%>
											<c:if test="${!fuser.fpostImgValidate&& fuser.faudit == '0'}">
					                    <%--  <c:if test="${fuser.authGrade != '4' && fuser.authGrade != '5' &&  fuser.authGrade != '3'}"> --%>
										<div class="to_up" style="display:${!fuser.fpostImgValidate == true ?'block;':'none;' && fuser.authGrade != '3'?'block;':'none;' && fuser.authGrade != '4'?'block;':'none;' &&fuser.authGrade !='5'?'block;':'none;'}">
											<p id="error" class="text-danger"></p>
											<c:choose>
												<c:when test="${fuser.fpostRealValidate && fuser.fhasRealValidate && !fuser.fpostImgValidate}">
													<span class="s3 btn-danger">提交</span>
												</c:when>
												<c:otherwise>
													<span class="s2 btn-danger">提交</span>
												</c:otherwise>
											</c:choose>
										</div>
										</c:if>
										<c:if test="${fuser.fhasImgValidate&&fuser.authGrade == '2' && fuser.faudit == '0'||fuser.faudit == '1'}">
										<%-- <c:if test="${fuser.fhasImgValidate }"> --%>
										<div class="to_up1" style="display:${empty flag == true ?'none;':'block;'}">
											<p id="error" class="text-danger"></p>
											<c:choose>
												<c:when
													test="${fuser.fpostRealValidate && fuser.fhasRealValidate && !fuser.fpostBankValidate}">
													<span class="s4 btn-danger">提交</span>
												</c:when>
												<c:otherwise>
													<span class="s2 btn-danger">提交</span>
												</c:otherwise>
											</c:choose>
										</div>
										</c:if>
										<!-- kyc2验证第二步 -->
										<%-- <div class="step_02">
                                    <div class="sub_title">
                                        <span class="s1">Step 2</span>
                                        <span class="">本人视频认证</span>
                                    </div>
                                    <div class="up_video">
                                        <div class="left">
                                            <div class="video_btn">
                                                <span class="s1">进行视频认证请联系客服</span>
                                                <c:if test="${!fuser.fhasVideoValidate }">
                                                <label for="vedio" class="s2"><a href="http://wpa.qq.com/msgrd?v=3&uin=${requestScope.constant['serviceQQ'] }&menu=yes" style="color: #fff;">联系客服</a></label>
                                                </c:if>
                                                <c:if test="${fuser.fhasVideoValidate }">
                                                <label for="vedio" class="s2">已认证</label>
                                                </c:if>
                                            </div>
                                        </div>
                                    </div>
                                </div> --%>

								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- 实名认证 -->
	<div class="modal modal-custom fade" id="bindrealinfo" tabindex="-1"
		role="dialog">
		<div class="modal-dialog" role="document">
			<div class="modal-mark"></div>
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close btn-modal" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<span class="modal-title" id="exampleModalLabel">01身份认证</span>
					<!-- <div class="KYC1-types text-center">
                        <span class="KYC1-type active">01身份认证</span>
                    </div> -->
				</div>
				<div class="modal-body form-horizontal">
					<div class="form-group ">
						<label for="bindrealinfo-realname" class="col-xs-3 control-label">真实姓名</label>
						<div class="col-xs-6">
							<input id="bindrealinfo-realname" class="form-control"
								placeholder="请填写您的真实姓名" type="text" autocomplete="off">
							<span id="bindrealinfo-realname-errortips "
								class="text-danger certificationinfocolor">*请填写真实姓名，认证后不能更改，提现时需要与银行等姓名信息保持一致。</span>
						</div>
					</div>
					<div class="form-group ">
						<label for="bindrealinfo-areaCode" class="col-xs-3 control-label">地区/国家</label>
						<div class="col-xs-6">
							<select class="form-control" id="bindrealinfo-address">
								<option value="86" selected>中国大陆(China)</option>
							</select>
						</div>
					</div>
					<div class="form-group ">
						<label for="bindrealinfo-areaCode" class="col-xs-3 control-label">证件类型</label>
						<div class="col-xs-6">
							<select class="form-control" id="bindrealinfo-identitytype">
								<option value="0">身份证</option>
							</select>
						</div>
					</div>
					<div class="form-group ">
						<label for="bindrealinfo-imgcode" class="col-xs-3 control-label">证件号码</label>
						<div class="col-xs-6">
							<input id="bindrealinfo-identityno" class="form-control"
								type="text" autocomplete="off">
						</div>
					</div>
					<div class="form-group ">
						<label for="bindrealinfo-ckinfo" class="col-xs-3 control-label"></label>
						<div class="col-xs-6">
							<div class="checkbox disabled">
								<label id="bindrealinfo-ckinfolb"> <input
									type="checkbox" value="" checked id="bindrealinfo-ckinfo">
									我保证提交的信息实属本人所有，非盗用他人证件
								</label>
							</div>
						</div>
					</div>
					<div class="form-group">
						<label for="bindrealinfo-errortips" class="col-xs-3 control-label"></label>
						<div class="col-xs-6">
							<span id="certificationinfo-errortips" class="text-danger"></span>
						</div>
					</div>
					<div class="form-group">
						<label for="bindemail-Btn" class="col-xs-3 control-label"></label>
						<div class="col-xs-6">
							<button id="bindrealinfo-Btn" class="btn btn-danger btn-block">确定提交</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	


	<jsp:include page="../comm/footer.jsp"></jsp:include>

	<script type="text/javascript"
		src="${oss_url}/static/front/js/plugin/ajaxfileupload.js"></script>
	<script type="text/javascript"
		src="${oss_url}/static/front/js/plugin/fileCheck.js"></script>
	<script type="text/javascript"
		src="${oss_url}/static/front/js/comm/msg.js"></script>
	<script type="text/javascript"
		src="${oss_url}/static/front/js/finance/city.min.js"></script>
	<script type="text/javascript"
		src="${oss_url}/static/front/js/finance/jquery.cityselect.js"></script>
	<script type="text/javascript"
		src="${oss_url}/static/front/js/plugin/jquery.qrcode.min.js"></script>
	<script type="text/javascript"
		src="${oss_url}/static/front/js/plugin/jquery.autocomplete.min.js"></script>
	<script type="text/javascript"
		src="${oss_url}/static/front/js/user/kyc.js"></script>
	<script type="text/javascript"
		src="${oss_url}/static/front/js/user/kyct.js"></script>
	<script>
		 $(".skip").on('click',function(){
			var skip = $(".skip").val();
			 if(skip== 1 ||skip == "1"){
			   var url = "/user/validateIdentity1.html?random=" +Math.round(Math.random() * 100);
			   var param = {
				  realName :"admin",
				  identityType:"0",
				  identityNo:"500233199301011414"
			   }
			   jQuery.post(url,param,function(data){
				   if(data.code == 0){
					  window.location.href="/index.html";
				   }else{
					  util.showerrortips('certificationinfo-errortips',data.msg);
				   }
			   },"json");
			 }else{
				 window.location.href="/index.html"; 
			 }
		 })
		 $(function(){
			 var authGrade = "${fuser.authGrade}";
			 if(authGrade == '2'){
				 $(".bankid").css("display","block");
				 $(".to_up").css("display","none");
				 $(".to_up1").css("display","block");
			 }
			 $(".renz").click(function(){
				 var url = "/user/validateKyc3.html?random=" + Math.round(Math.random() * 100);
				 jQuery.post(url,{},function(data){
					 if(data.code == 0){
					    window.location.reload();
					 }else{
						 util.showerrortips('certificationinfo-errortips',data.msg);
					 }
					 
				 })
			 })
			
		 })
		 function check(){
			 var mm ="${fuser.faudit}";
			 if(mm == 1 || mm =="1"){
				 layer.msg("请先进行二级认证!!!");
				 return;
			 }else{
				 renzz();
			 }
		 }
		 function checkLevel(node){
			  var flag = "${fuser.fpostRealValidate}";
			 if(!${fuser.fpostRealValidate}){
				 layer.msg("请先进行实名认证!");
				 return;
			 }else{
				 $("#"+node).click();
			 }
		 };
		 function renzz(){
             var url ="/user/validateKyc4.html?random=" + Math.round(Math.random() * 100);
             jQuery.post(url,{},function(data){
                 console.log(data);
                 if(data.code == 0){
                     window.location.reload();
                 }else{
                     util.showerrortips('certificationinfo-errortips',data.msg);
                 }
             })
         }
	</script>
</body>
</html>
