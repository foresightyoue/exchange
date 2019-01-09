<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
    <title>联系我们</title>
    <link rel="stylesheet" href="/static/front/app/css/css.css" />
    <link rel="stylesheet" href="/static/front/app/css/style.css" />
    <link rel="stylesheet" href="/static/front/app/css/swiper-3.4.2.min.css" />
    <link rel="stylesheet" href="${oss_url}/static/front/css/question/question.css" type="text/css"></link>
    <link rel="stylesheet" href="${oss_url}/static/front/css/plugin/bootstrap.css" type="text/css"></link>
    
    <style type="text/css">
        .exit_btn ul{
            height: 3.06rem;
            line-height: 3.06rem;
            font-size: 0.87rem;
            border-radius: 0.15rem;
            text-align: center;
            margin: 4rem 0.62rem;
            background: #186ed5;
            color: #fff;
        }
        .exit_btn{
            padding-left: 0;
            background-color: transparent;
            border: 0;
        }
        @media screen and (max-width: 320px) {
    html {
         font-size:12px;
    }
}
@media screen and (min-width: 320px) and (max-width: 360px) {
    html {
         font-size:14px;
    }
}
@media screen and (min-width: 360px) and (max-width: 400px) {
    html {
         font-size:15px;
    }
}
@media screen and (min-width: 400px) and (max-width: 440px) {
    html {
        font-size:18px;
    }
}
@media screen and (min-width: 440px) and (max-width: 480px) {
    html {
         font-size:20px;
    }
}
@media screen and (min-width: 480px) and (max-width: 520px) {
    html {
         font-size:22px;
    }
}
@media screen and (min-width: 520px) and (max-width: 560px) {
    html {
         font-size:24px;
    }
}
@media screen and (min-width: 560px) and (max-width: 600px) {
    html {
         font-size:26px;
    }
}
@media screen and (min-width: 600px) and (max-width: 640px) {
    html {
         font-size:28px;
    }
}
@media screen and (min-width: 640px) and (max-width: 680px) {
    html {
         font-size:30px;
    }
}
@media screen and (min-width: 720px) {
    html {
         font-size:32px;
    }
}

        .modal{
            display: none;
        }
        .pageContent_list{
            background-color: white;
            padding-left: .62rem;
            width: 100%;
            overflow: hidden;
            box-sizing: border-box;
            -webkit-box-sizing: border-box;
        }
        .flexLayout{
            display: flex;
            justify-content: space-between;
        }
        .pageContent_list li{
            width: 120%;
            height: 100%;
            line-height: 1.5rem;
            border-bottom: 1px solid #e6e6e6;
        }
        .basic_message{
            width: 80%;
            padding: .87rem 0;
            box-sizing: border-box;
            -webkit-box-sizing: border-box;
        }
        .pursue_ask{
            width: 20%;
            padding: .87rem 0;
            box-sizing: border-box;
            -webkit-box-sizing: border-box;
            text-align: right;
            line-height: 5.2rem;
            margin-right: .62rem;
        }
        .delete_btn{
            width: 20%;
            text-align: center;
            line-height: 7rem;
            background-color: red;
        }
        .delete_btn a{
            color: white;
            font-size: 1rem;
        }
        .message_content{
            font-size: 1rem;
            width: 19rem;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }
        .message_time{
            font-size: .7rem;
            color: #808080;
            margin-bottom: .62rem;
        }
        .whether_solve,.read_message{
            background-color: rgba(49, 67, 91, 0.14);
            border-radius: 0.05rem;
            padding: .2rem .62rem;
            position: relative;
        }
        .no_read{
            display: inline-block;
            width: .4rem;
            height: .4rem;
            border-radius: 50%;
            -webkit-border-radius: 50%;
            background-color: red;
            position: absolute;
            right: -0.1rem;
            top: -0.1rem;
            z-index: 10;
        }
        .ui-loader.ui-corner-all.ui-body-a.ui-loader-default{
            display: none;
        }
        .modal-dialog{
            width: 100%;
            height: 100%;
            margin: 0 !important;
        }
        .modal-content{
            position: absolute;
            width: 90%;
            top: 50%;
            /* height: 20rem; */
            left: 50%;
            transform: translate(-50%,-50%);
            margin-top: 0 !important;
            border: 0;
        }
        .modal-header{
            padding: 10px;
        }
        .modal-body {
            padding: 0;
        }
        .panel-default>.panel-heading {
            color: #333333;
            background-color: white;
            border-bottom: 0;
            padding: 10px;
            width: 99%;
        }
        .panel-title{
            font-size: 12px;
        }
        .modal-body{
            max-height: 200px;
            overflow: auto;
            padding-bottom: 5px;
            /* border-bottom: 1px solid #e6e6e6; */
            margin-bottom: 10px;
        }
        .question_content{
            padding: 5px 10px;
            background-color: #666;
            color: white;
            border-radius: 4px;
            margin: 0 10px;
            display: inline-block;
            word-break: break-all;
        }
        .ll-content{
            border: 0;
            width: 100%;
            height: 75px !important;
            padding: 10px;
            box-sizing: border-box;
            -webkit-box-sizing: border-box;
            font-size: .87rem;
        }
        .content{
            border-top: 1px solid #e6e6e6;
        }
        .no-data-tips li{
            text-align: center;
            color: #666;
            font-size: 15px;
            margin-top: 6rem;
        }
        .Personal-title span em{
            width: 1.2rem;
            height: 1.2rem;
            left: 0.4rem;
            top:0.65rem;
            }
        .Personal-title span em i:last-child{
            left:0.22rem;
        }
    </style>
</head>
<body>
<nav style="z-index: 11">
    <div class="Personal-title">
                <span>
                    <a href="javascript:history.back();">
                        <em>
                            <i></i>
                            <i></i>
                        </em>
                        <strong><!-- 返回 --></strong>
                    </a>
                </span>
        问题列表
    </div>
</nav>
<section style="padding-top: 0.2rem;">
    <!-- <div class="user-list clearfix bg-img01"></div> -->
    <!-- <div class="pageContent"> -->
        <ul class="pageContent_list">
            <c:forEach items="${list}" var="v">
                <li class="opa-link flexLayout">
                    <div class="view basic_message" data-question="{&quot;questionid&quot;:${v.fid }}">
                        <div class="message_content">${v.fdesc}</div>
                        <div class="message_time"><fmt:formatDate value="${v.fcreateTime}" type="both" pattern="yyyy-MM-dd HH:mm:ss" /></div>
                        <div>
                            <span class="whether_solve">${v.fstatus_s}</span>
                            <c:if test="${v.isallsys == '1' }">
                               <span class="noRead_message read_message"><span>未读</span><span class="no_read"></span></span>
                           </c:if>
                           <c:if test="${v.isallsys == '0' }">
                               <span class="old_message read_message">已读</span>
                           </c:if>
                        </div>
                    </div>
                    <div class="pursue_ask"><a class="view2 opa-link" href="javascript:void(0)" data-question="{&quot;questionid&quot;:${v.fqid}}">追问</a></div>
                    <div class="delete_btn"><a class="delete opa-link" href="javascript:void(0)" data-question="{&quot;questionid&quot;:${v.fid }}">删除</a></div>
                </li>
            </c:forEach>
        </ul>
        <%-- <c:forEach items="${list}" var="v">
                 <ul class="flexLayout">
                    <li>${v.fid}</li>
                    <li>${v.fcreateTime}</li>
                    <li class="opa-link">
                       <a class="view2 opa-link" href="javascript:void(0)" data-question="{&quot;questionid&quot;:${v.fqid}}">追问</a>
                    </li>
                    <li class="text-over">${v.fdesc}</li>
                    <li>${v.fstatus_s}</li>
                    <c:if test="${v.isallsys == '1' }">
                       <li>有未读信息</li>
                    </c:if>
                    <c:if test="${v.isallsys == '0' }">
                       <li>暂无未读信息</li>
                    </c:if>
                    <li class="opa-link">
                      <a class="delete opa-link" href="javascript:void(0)" data-question="{&quot;questionid&quot;:${v.fid }}">删除</a>
                      <a class="view opa-link" href="javascript:void(0)" data-question="{&quot;questionid&quot;:${v.fid }}">查看</a>
                    </li>
                 </ul>
        </c:forEach> --%>
        <c:if test="${fn:length(list) == 0 }">
            <ul class="no-data-tips">
                <li>你暂时没有提问记录</li>
            </ul>
        </c:if>
    <!-- </div> -->
   <c:forEach items="${list}" var="v">
       <div class="modal modal-custom fade" id="view-questiondetail" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-mark"></div>
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close btn-modal" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true" style="font-size: 1.5rem;">&times;</span>
                        </button>
                        <span class="modal-title">问题详情</span>
                    </div>
                    <div class="modal-body form-horizontal">
                                                                  
                    </div>
                </div>
            </div>
        </div>
   </c:forEach>
   <!-- 追问 -->
    <div class="modal modal-custom fade" id="questiondetail" tabindex="-1" role="dialog">
         <div class="modal-dialog" role="document">
            <div class="modal-mark"></div>
            <div class="modal-content">
            <div class="modal-header">
                 <button type="button" class="close btn-modal" data-dismiss="modal" aria-label="Close">
                      <span aria-hidden="true" style="font-size: 1.5rem;">&times;</span>
                 </button>
                 <span class="modal-title">提问</span><span class="modal-title1">回复</span>
                 </div>
                 <div class="modal-body form-horizontal">
                                                                                           
                 </div>
                 <div class="modal-body1 form-horizontal">
                    <!-- <span style="float: left;">回复:</span> -->
                    <div class="content">
                         <textarea class="ll-content"></textarea>
                    <div>
                    <div style="text-align: right;padding-right: 10px;padding-bottom: 10px;font-size: 1rem;color: #337ab7;">
                         <button class="publish">发表</button>
                    </div>
                 </div>
              </div>
            </div>
         </div>
      </div>
    </div>
</section>
<script type="text/javascript" src="/static/front/js/plugin/jquery.min.js"></script>
<script type="text/javascript" src="/static/front/app/js/swiper-3.4.2.jquery.min.js" ></script>
<script src="https://apps.bdimg.com/libs/jquerymobile/1.4.5/jquery.mobile-1.4.5.min.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/comm/util.js"></script>
<script type="text/javascript" src="/static/front/js/comm/comm.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/plugin/bootstrap.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/question/question.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/language/language_cn.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/plugin/layer/layer.js"></script>
<script type="text/javascript">
$('.pageContent_list li').on("swipeleft",function(){//左滑显示隐藏按键
//    alert(111);
    $(this).animate({"margin-left":'-20%'},200,'linear').siblings().animate({"margin-left":'0'},200,'linear');
}); 
$('.pageContent_list li').on("swiperight",function(){//右滑恢复 
 $(this).animate({"margin-left":'0'},200);
});
</script>
</body>
</html>