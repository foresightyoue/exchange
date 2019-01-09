<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<style>
.vip-userInfo{
    width: 100%;
    height: auto;
    margin-bottom: 15px;
}
.pageFormContent{
    padding-left: 30px;
}
.pageFormContent label{
    font-size: 16px;
    color: #000;
    padding-left: 10px;
    border-left: 2px solid #102a48;
}
.vip-userInfo table{
    width: 400px;
}
.vip-userInfo table tr{
    height: 30px;
    line-height: 30px;
}
.vip-userInfo table tr td span{
    display: inline-block;
    width: 65px;
}
.safety-info,.tixian,.chongzhi{
    width: 100%;
    height: auto;
    margin-bottom: 15px;
}
.safety-con{
    display: inline-block;
}
.tixian-con,.chongzhi-con{
    width: 450px;
    min-height: 50px;
}
.tixian-item,.chongzhi-item{
    line-height: 30px;
    height: 30px;
}
.tixian-item td,.chongzhi-item td{
    padding-right: 10px;
}
.safety-info .safety-con dt{
    width: 65px;
}
.safety-info .safety-con dd img{
    width: auto;
    height: 400px;
}
</style>
<h2 class="contentTitle">会员详情</h2>
<div class="pageContent">
    <div class="pageFormContent nowrap" layoutH="97">
    <div class="vip-userInfo">
     <label>用户基本信息:</label>
     <table>
      <tr>
        <td><span>昵称:</span>${info.fNickName}</td>
        <td><span>电话号码:</span>${info.fTelephone}</td>
      </tr>
      <tr>
         <td><span>真实姓名:</span>${info.fRealName}</td>
         <td><span>性别:</span>${info.fsex}</td>
      </tr>
      <tr>
         <td><span>登录账号:</span>${info.floginName}</td>
         <td><span>最后登录IP:</span>${info.flastLoginIp}</td>
      </tr>
     </table>
     </div>
     
     <div class="safety-info">
       <label>基本安全信息:</label>
       <div class="safety-con">
       <dl>
         <dt>邮箱:</dt>
         <dd>${info.fEmail}</dd>
       </dl>
       <dl>
          <dt>证件号码:</dt>
          <c:if test="${not empty info.fIdentityNo }">
              <dd>${info.fIdentityNo}</dd>
          </c:if>
          <c:if test="${empty info.fIdentityNo }">
             <span>暂无身份证号</span>
          </c:if>
       </dl>
       <dl>
          <dt>扫描件正面:</dt>
          <dd>
           <c:if test="${not empty info.fIdentityPath }">
             <img src="${info.fIdentityPath }"/>
           </c:if>
           <c:if test="${empty info.fIdentityPath }">
              <span>暂没有相关图片</span>
           </c:if>
            
          </dd>
       </dl>
       <dl>
         <dt>扫描件反面:</dt>
         <dd>
            <c:if test="${not empty info.fIdentityPath2 }">
               <img src="${info.fIdentityPath2 }" width="500"/>
            </c:if>
            <c:if test="${empty info.fIdentityPath2 }">
               <span>暂没有相关图片</span>
            </c:if>
         </dd>
       </dl>
       <dl>
         <dt>手持身份证:</dt>
         <dd>
           <c:if test="${not empty info.fIdentityPath3 }">
              <img src="${info.fIdentityPath3 }" width="500"/>
           </c:if>
            <c:if test="${empty info.fIdentityPath3 }">
               <span>暂没有相关图片</span>
            </c:if>
            
         </dd>
       </dl>
       </div>
     </div>
     
     <div class="tixian">
     <label>提现地址:</label>
     <table class="tixian-con">
	     <c:forEach var="addre" items="${addresses}">
		     <tr class="tixian-item">
		           <td>提现地址:${addre.fAdderess }</td>
		           <td>提现币类型:${addre.fShortName}</td>
		           <td>提现时间:${addre.fCreateTime}</td>
		      </tr>
	      </c:forEach>
	     </table>
     </div>
     <div class="chongzhi">
        <label>充值地址:</label>
        <table class="chongzhi-con">
	        <c:forEach var="alladdree" items="${alladdree}">
		        <tr class="chongzhi-item">
		              <td>充值地址:${alladdree.fAdderess}</td>
		              <td>充值币类型:${alladdree.fShortName}</td>
		              <td>充值时间:${alladdree.fCreateTime}</td>
		        </tr>
	        </c:forEach>
	     </table>
     </div>
        <%-- <dl>
           <dd>会员详情:</dd>
           <dd>
           
           </dd>
        </dl>
        <dl>
        <dt>昵称:</dt>
        <dd>
           <input type="text" name="fNickName" class="required" size="45" value="${adminDetail.fNickName }">
        </dd>
        </dl>
        <dl>
        <dt>电话号码:</dt>
        <dd>
           <input type="text" name="fTelephone" class="required" size="45" value="${adminDetail.fTelephone }">
        </dd>
        </dl>
        <dl>
        <dt>最后登录的IP:</dt>
        <dd>
           <input type="text" name="flastLoginIp" class="required" size="45" value="${adminDetail.flastLoginIp}">
        </dd>
        </dl>
        <dl>
        <dt>性别:</dt>
        <dd>
           <input type="text" name="fsex" class="required" size="45" value="${adminDetail.fsex}">
        </dd>
        </dl>
         <dl>
        <dt>最后交易价格:</dt>
        <dd>
           <input type="text" name="fprice" class="required" size="45" value="${adminDetail.fprice}">
        </dd>
        </dl>
          <dl>
        <dt>最后交易总量:</dt>
        <dd>
           <input type="text" name="ftotal" class="required" size="45" value="${adminDetail.ftotal}">
        </dd>
        </dl>
          <dl>
        <dt>最后提现地址:</dt>
        <dd>
           <input type="text" name="fAdderess" class="required" size="45" value="${adminDetail.fAdderess}">
        </dd>
        </dl> --%>
    </div>
</div>