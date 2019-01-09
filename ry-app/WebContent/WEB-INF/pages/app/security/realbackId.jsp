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
      <meta charset="utf-8"/>
      <meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no, width=device-width">
      <meta name="format-detection" content="telephone=no">
      <meta name="format-detection" content="email=no">
      <base href="${basePath}"/>
      <title>实名认证</title>
      <link rel="stylesheet" href="/static/front/app/css/css.css"/>
      <link rel="stylesheet" href="/static/front/app/css/style.css"/>
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
            background-color: #186ed5;
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
            background-color: #186ed5;
            margin: 4rem 5% 3rem;
            border-radius: .4rem;
        }
        .pic1show,.pic2show,.pic3show{
            width: 8rem;
            height: 5.5rem;
            background-size: 100%;
            background-position: center;
            background-repeat: no-repeat;
            margin-top: .85rem;
        }
        .pic1show img,.pic2show img,.pic3show img{
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
      </div>
   </nav>
   <section>
      
      <div class="mind">
         <strong>说明:</strong>请按照要求上传您身份证的正面照片,仅支持<span>jpg、bmp、png</span>格式,大小限制<span>1MB以内</span>.
      </div>
      <div class="to_up" style="display:${!fuser.fpostImgValidate == true ? 'block;':'none;'}">
          <p id="error" class="text-danger"></p>
          <c:choose>
              <c:when test="${fuser.fpostRealValidate && fuser.fhasRealValidate && !fuser.fpostImgValidate && fuser.authGrade == '2'}">
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
        src="/static/front/js/comm/util.js"></script>
    <script type="text/javascript"
        src="/static/front/js/layer/layer.js"></script>
</html>