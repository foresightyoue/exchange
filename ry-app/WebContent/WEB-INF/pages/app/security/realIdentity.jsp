<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../../front/comm/include.inc.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>
<!DOCTYPE html>
<html>
<head>
        <jsp:include page="../comm/header.jsp"></jsp:include>
        <meta charset="utf-8" />
        <meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no, width=device-width">
        <meta name="format-detection" content="telephone=no">
        <meta name="format-detection" content="email=no">
        <base href="${basePath}"/>
        <title>实名认证</title>
        <link rel="stylesheet" href="/static/front/app/css/css.css" />
        <link rel="stylesheet" href="/static/front/app/css/style.css" />
        <script type="text/javascript" src="/static/front/app/js/jquery-3.2.1.min.js" ></script>
        <script type="text/javascript" src="/static/front/app/js/swiper-3.4.2.jquery.min.js" ></script>
	<style type="text/css">
	    .sub_title{
			height: 3.06rem;
			line-height: 3.06rem;
			background-color: white;
			padding-left: .62rem;
			font-size: .87rem;
	    }
	    .all_up{
			padding: 1rem .62rem;
			background-color: #fff;
			overflow: hidden;
			font-size: .87rem;
			color: #808080;
			margin-top: .62rem;
			position: relative;
	    }
	    .work_info{
	    	margin-top: .62rem;
	    }
	    .s1{
	    	/* display: inline-block;
			width: 8rem;
			height: 5.5rem;
			border: 1px solid #CCCCCC;
			background-color: #FAFAFA;
			background-image: url(/static/front/app/images/fileBG.png);
			background-repeat: no-repeat;
			background-size: 80% auto;
			background-position: center center;
			position: relative; */
			padding: .3rem 1rem;
			background-color: #EBA51D;
			color: white;
            margin-right: .62rem;
	    }
	    .files{
	    	display: none;
	    }
		.mind{
			width: 90%;
			margin: 0 5%;
			padding: 1rem .62rem;
			border: 1px solid #e6e6e6; 
			border-radius: 4px;
			-webkit-border-radius: 4px;
			background-color: white;
			font-size: .7rem;
			margin-top: 1rem;
		}
		.btn-danger{
			width: 90%;
			display: block;
			height: 3.06rem;
			line-height: 3.06rem;
			color: #fff;
			text-align: center;
			font-size: 1.12rem;
			background-color: #EBA51D;
			margin: 4rem 5% 3rem;
			border-radius: .4rem;
		}
		.pic1show,.pic2show,.pic3show,.imgs>div{
			width: 8rem;
			height: 5.5rem;
			background-size: 100%;
			background-position: center;
			background-repeat: no-repeat;
			margin-top: .85rem;
		}
		.pic1show img,.pic2show img,.pic3show img,.imgs img{
            width: 100%;
            height: 100%;
        }
	</style>
</head>
<body>
     <nav>
        <div class="Personal-title">
           <span>
              <a href="javascript:;" onClick="javascript:history.back(-1)">
                  <em>
                     <i></i>
                     <i></i>
                  </em>
                  <strong>返回</strong>
              </a>
           </span>实名认证
           <p class="skip">跳过</p>
        </div>
     </nav>
     <section>
     <!-- 上传身份证-------------------------------- -->
     <div class="idten" style="display:${empty flag == true ?'block;':'none;'}">
         <!--kyc验证第一步  -->
         <div class="step_01">
            <div class="sub_title">
               <span class="">Step 2</span> <span class="">本人身份证照认证</span>
            </div>
			<div class="up_img_01 all_up">
				<p class="left">本人证件正面照片</p>
				<div class="user_up">
					<div class="work_info">
						<c:choose>
							<c:when test="${fuser.fhasImgValidate && fuser.fpostImgValidate }">
								<!-- <label for="pic1" class="s1">已认证</label> -->
								<label for="pic" class="s1">已认证</label>
							</c:when>
							<c:when test="${!fuser.fhasImgValidate && fuser.fpostImgValidate}">
								<!-- <label for="pic1" class="s1">审核中</label> -->
								<label for="pic" class="s1">审核中</label>
							</c:when>
							<c:otherwise>
								<label for="pic1" class="s1">上传</label>
							</c:otherwise>
						</c:choose>
						<input id="pic1" type="file" class="files" onchange="uploadImg1()">
						<input id="pic1Url" type="hidden">
						<span class="s2"><span>未选择文件</span><!-- <em class="pic1name"></em> --></span>
						<!-- <span class="f2">示例</span> -->
					</div>
					<div class="imgs">
						<div class="pic1show">
							<c:if test="${fuser.fpostImgValidate }">
								<img src="${fuser.fIdentityPath}" alt="">
							</c:if>
						</div>
                 <%-- <div class="exemple">
                      <img src="${oss_url}/static/front/images/user/zheng_image.png" 
                           alt="">
                 </div> --%>
					</div>
				</div>
			</div>
		</div>
		<div class="up_img_02 all_up">
			<p class="left">本人证件反面照片</p>
			<div class="user_up">
				<div class="work_info">
					<c:choose>
						<c:when test="${fuser.fhasImgValidate && fuser.fpostImgValidate}">
							<!-- <label for="pic2" class="s1">已认证</label> -->
							<label for="pic" class="s1">已认证</label>
						</c:when>
						<c:when test="${!fuser.fhasImgValidate && fuser.fpostImgValidate }">
							<!-- <label for="pic2" class="s1">审核中</label> -->
							<label for="pic" class="s1">审核中</label>
						</c:when>
						<c:otherwise>
							<label for="pic2" class="s1">上传</label>
						</c:otherwise>
					</c:choose>
					<input id="pic2" type="file" class="files" onchange="uploadImg2()">
					<input type="hidden" id="pic2Url">
					<span class="s2"> <span>未选择文件</span><!-- <em class="pic2name"></em> --></span> 
					<!-- <span class="f2">示例</span> -->
				</div>
				<div class="imgs">
					<div class="pic2show">
						<c:if test="${fuser.fpostImgValidate }">
							<img src="${fuser.fIdentityPath2 }" alt="">
						</c:if>
					</div>
                   <%-- <div class="exemple">
                    <img
                        src="${oss_url}/static/front/images/user/fan_image.png"
                        alt="">
                   </div> --%>
				</div>
			</div>
		</div>
		<div class="up_img_03 all_up">
			<p class="left">手持本人证件照片</p>
			<div class="user_up">
				<div class="work_info">
					<c:choose>
						<c:when test="${fuser.fhasImgValidate && fuser.fpostImgValidate }">
							<!-- <label for="pic3" class="s1">已认证</label> -->
							<label for="pic" class="s1">已认证</label>
						</c:when>
						<c:when test="${!fuser.fhasImgValidate && fuser.fpostImgValidate }">
							<!-- <label for="pic3" class="s1">上传</label> -->
							<label for="pic" class="s1">审核中</label>
						</c:when>
						<c:otherwise>
						    <label for="pic3" class="s1">上传</label>
						</c:otherwise>
					</c:choose>
					<input id="pic3" type="file" class="files" onchange="uploadImg3()">
					<input type="hidden" id="pic3Url"> 
					<span class="s2"> <span>未选择文件</span><!-- <em class="pic3name"></em> --></span> 
					<!-- <span class="f2">示例</span> -->
				</div>
				<div class="imgs">
					<div class="pic3show">
						<c:if test="${fuser.fpostImgValidate }">
							<img src="${fuser.fIdentityPath3 }" alt="">
						</c:if>
					</div>
                     <%-- <div class="exemple">
                         <img 
                            src="${oss_url}/static/front/images/user/chi_image.png"
                            alt="">
                     </div> --%>
				</div>
			</div>
		</div>
		</div>
		<!-- 上传身份证-------------------------------- -->
		<!-- 上传银行卡-------------------------------------------->
		
		<!-- <div class="bankid" style="display: none;"> -->
		<div class="bankid" style="display:${empty flag == true ?'none;':'block;'}">
		<div class="step_01">
         <div class="sub_title">
             <span class="">Step 3</span><span class="">银行卡认证</span>
         </div>
         <div class="up_img_01 all_up">
              <p class="left">银行卡正面照片</p>
              <div class="work_info">
                  <c:choose>
                      <c:when test="${fuser.fhasBankValidate && fuser.fpostBankValidate && fuser.authGrade == '3'}">
                         <label for="pic" class="s1">已认证</label>
                      </c:when>
                      <c:when test="${!fuser.fhasBankValidate && fuser.fpostBankValidate}">
                          <label for="pic" class="s1">审核中</label>
                      </c:when>
                      <c:otherwise>
                          <label for="picBank1" class="s1">上传</label>
                      </c:otherwise>
                  </c:choose>
                  <input id="picBank1" type="file" class="files" onchange="uploadBankImg1()">
                  <input id="picBank1Url" type="hidden">
                  <span><span class="s2">未选择文件</span></span>
              </div>
              <div class="imgs">
                 <div class="picBank1show">
                     <c:if test="${fuser.fpostBankValidate}">
                        <img src="${fuser.fBankPath}" alt="">
                     </c:if>
                 </div>
              </div>
         </div>
      </div>
      <div class="up_img_02 all_up">
           <p class="left">手持本人银行卡照片</p>
           <div class="user_up">
              <div class="work_info">
                 <c:choose>
                    <c:when test="${fuser.fhasBankValidate && fuser.fpostBankValidate}">
                        <label for="pic" class="s1">已认证</label>
                    </c:when>
                    <c:when test="${!fuser.fhasBankValidate && fuser.fpostBankValidate}">
                        <label for="pic" class="s1">审核中</label>
                    </c:when>
                    <c:otherwise>
                        <label for="picBank2" class="s1">上传</label>
                    </c:otherwise>
                 </c:choose>
                 <input id="picBank2" type="file" class="files" onchange="uploadBankImg2()">
                 <input type="hidden" id="picBank2Url">
                 <span class="s2"><span>未选择文件</span></span>
              </div>
              <div class="imgs">
                  <div class="picBank2show">
                     <c:if test="${fuser.fpostBankValidate}">
                         <img src="${fuser.fBankPath2 }" alt="">
                     </c:if>
                  </div>
              </div>
           </div>
      </div>
      <div class="up_img_03 all_up">
          <p class="left">银行卡授权认证照片</p>
          <div class="user_up">
              <div class="work_info">
                <c:choose>
                   <c:when test="${fuser.fhasBankValidate && fuser.fpostBankValidate}">
                       <label for="pic" class="s1">已认证</label>
                   </c:when>
                   <c:when test="${!fuser.fhasBankValidate && fuser.fpostBankValidate}">
                        <label for="pic" class="s1">审核中</label>
                   </c:when>
                   <c:otherwise>
                        <label for="picBank3" class="s1">上传</label>
                   </c:otherwise>
                </c:choose>
                <input id="picBank3" type="file" class="files" onchange="uploadBankImg3()">
                <input type="hidden" id="picBank3Url">
                <span class="s2"><span>未选择文件</span></span>
              </div>
              <div class="imgs">
                  <div class="picBank3show">
                     <c:if test="${fuser.fpostBankValidate }">
                        <img src="${fuser.fBankPath3 }" alt="">
                     </c:if>
                  </div>
              </div>
          </div>
      </div>
		</div>
		<div class="mind">
			<strong style="color:red;">说明：</strong>请按照要求上传您证件的正面照片,仅支持<span>jpg、bmp、png</span>格式,大小限制<span>3MB以内</span>.
		</div>
		<%-- <div class="to_up" style="display:${!fuser.fpostImgValidate== true ?'block;':'none;'}"> --%>
		   <div class="to_up" style="display:${empty flag == true ?'block;':'none;'}">
			<p id="error" class="text-danger"></p>
			<c:choose>
				<c:when test="${fuser.fpostRealValidate && fuser.fhasRealValidate && !fuser.fpostImgValidate}">
					<span class="s5 btn-danger">提交</span>
				</c:when>
				<c:otherwise>
					<span class="s2 btn-danger">提交</span>
				</c:otherwise>
			</c:choose>
		</div>
		<!-- <div class="to_up1" style="display:none;"> -->
		<div  class="to_up1" style="display:${empty flag == true ?'none;':'block;'}">
            <p id="error" class="text-danger"></p>
            <c:choose>
                <c:when test="${fuser.fpostRealValidate && fuser.fhasRealValidate && !fuser.fpostBankValidate}">
                    <span class="s3 btn-danger">提交</span>
                </c:when>
                <c:otherwise>
                    <span class="s2 btn-danger">提交</span>
                </c:otherwise>
            </c:choose>
        </div>
	</section>
</body>
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
    <script type="text/javascript"
        src="/static/front/js/comm/util.js"></script>
    <script type="text/javascript"
        src="/static/front/js/layer/layer.js"></script>
        <script type="text/javascript" src="/static/front/js/comm/comm.js"></script>
    <script>
    $(".skip").on('click',function(){
    	window.location.href="/m/index.html";
    })
    </script>
</html>