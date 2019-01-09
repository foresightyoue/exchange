<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<style>
.clearfix:after{
    height:0;
    display:block;
    content:".";
    visibility:hidden;
    clear:both;
}
.clearfix{
    zoom:1;
}
.fl{
    float: left;
}
.fr{
    float: right;
}
.mars{
    width: 100%;
    height: 100%;
    background-color: #0000004f;
    position: absolute;
    left: 0;
    top: 0;
    z-index: 10;
    display: none;
}
.details_content{
    width: 500px;
    height: auto;
    background-color: white;
    position: absolute;
    left: 50%;
    top: 50%;
    z-index: 100;
    transform: translate(0,-50%);
    margin-left: -250px;
    padding-bottom: 15px;
    display: none;
}
.details_hint{
    height: 40px;
    line-height: 40px;
    padding: 0 10px;
    border-bottom: 1px solid #e6e6e6;
    margin-bottom: 10px;
}
.content_details{
    padding: 0 10px;
}
.img_box{
    margin: 10px 0;
}
.img_box img{
    width: 150px;
    height: 100px;
}
.remark_hint{
    width: 8%;
    margin-top: 4px;
    font-weight: 600;
}
.remark_text{
    width: 90%;
    line-height: 20px;
    font-size: 13px;
    color: #666;
}
.btn_group{
    text-align: center;
}
.btn_group input{
    height: 25px;
    padding: 0 10px;
    border: 1px solid #5195B7;
    outline: none;
    background-color: white;
    color: #183152;
    margin-top: 15px;
    border-radius: 4px;
    -webkit-border-radius: 4px;
}
.chaxun_btn{
    width: auto;
    height: 25px;
    margin: 0;
    padding: 0 5px 1px 5px;
    border: 0;
    font-size: 12px;
    font-weight: bold;
    cursor: pointer;
    border: 1px solid #0000ff63;
    background-color: transparent;
}
</style>
<form id="pagerForm" method="post" action="ssadmin/otcOrdersList.html">
    <input type="hidden" name="logDate" value="${logDate}" />
    <input type="hidden" name="logDate1" value="${logDate1}" />
    <input type="hidden" name="price" value="${price}" />
    <input type="hidden" name="price1" value="${price1}" /> 
    <input type="hidden" name="pageNum" value="${currentPage}" /> 
    <input type="hidden" name="numPerPage" value="${numPerPage}" /> 
    <input type="hidden" name="orderField" value="${param.orderField}" />
    <input type="hidden" name="orderDirection" value="${param.orderDirection}" />
    <input type="hidden" name="keywords" value="${keywords}"/>
    <input type="hidden" name="buyer" value="${buyer}"/>
    <input type="hidden" name="seller" value="${seller}"/>
    <input type="hidden" name="orderstatus" value="${orderstatus}">
    <input type="hidden" name="tradetype" value="${tradetype}"/>
    <input type="hidden" name="userlogin" value="${userlogin}"/>
    <input type="hidden" name="fStatus" value="${fStatus}"/>
</form>


<div class="pageHeader">
    <form onsubmit="return navTabSearch(this);"
        action="ssadmin/otcOrdersList.html" method="post">
        <div class="searchBar">
            <table class="searchContent">
                 <tr>
                    <td>订单号：<input type="text" name="keywords"
                        value="${keywords}" size="40" />
                    </td>
                    <td>用户账户：<input type="text" name="userlogin"
                        value="${userlogin}" size="40" />
                    </td>
                    <td>订单状态：
                    <select name="fStatus" id="fStatus">
                    <option value="" >全部</option>
					<option value="0" >挂单中</option>
					<option value="1" >售卖中</option>
					<option value="2" >已售卖</option>
					<option value="3" >已撤单</option>
                    </select>
                    </td>
                    <td><button type="submit" class="chaxun_btn">查询</button></td>
                </tr>
            </table>
        </div>
    </form>
</div>
<div class="pageContent">
    <div class="panelBar">
        <ul class="toolBar">
          <shiro:hasPermission name="ssadmin/userExport.html">
              <li><a class="icon" href="ssadmin/otctradeExport.html"
              target="dwzExport" targetType="navTab" title="确实要导出这些记录吗?"><span>导出</span>
              </a></li>
          </shiro:hasPermission>
        </ul>
    </div>
    <%
        String[] status = new String[]{"待派单","待确认","交易完成","申诉中","已取消"};
        request.setAttribute("status", status);
    %>
    <table class="table" width="100%" layoutH="138">
        <thead>
            <tr>
				<th width="20">序号</th>
                <th width="30">订单号</th>
                <th width="60">下单时间</th>
                <th width="50">币种名称</th>
                <th width="50">用户账户</th>
                <th width="50">单价</th>
                <th width="50">操作数量</th>
                <th width="85">操作币种产生人民币金额</th>
                <th width="60">订单类型</th>
                <th width="60">交易所在地</th>
                <th width="40">最小限额</th>
                <th width="40">最大限额</th>
                <th width="40">最低价</th>
                <th width="50">收款类型</th>
                <th width="60">广告留言</th>
                <th width="80">是否开启实名认证购买</th>
                <th width="40">是否撤销</th>
                <th width="40">订单状态</th>
                <th width="40">是否实名</th>
                <th width="40">下单用户昵称</th>
                <th width="40">审核状态</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${otcOrdersList}" var="item" varStatus="num">
                <tr target="sid_user" rel="${item.fid}">
                    <td>${num.index+1 }</td>
                    <td>${item.fId}</td>
                    <td><fmt:formatDate value="${item.fCreateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    <td>${item.fName}</td>
                    <td>${item.fUsr_id}</td>
                    <td>${item.fUnitprice}</td>
                    <td>${item.fCount}</td>
                    <td>${item.fMoney}</td>
                    <td>${item.fOrdertype eq 0 ? '买入' : '卖出'}</td>
                    <td>${item.fAdr }</td>
                    <td>${item.fSmallprice }</td>
                    <td>${item.fBigprice }</td>
                    <td>${item.fLowprice }</td>
                    <c:if test="${item.fReceipttype == 0}">
                        <td>银行</td>
                    </c:if>
                    <c:if test="${item.fReceipttype == 1}">
                        <td>支付宝</td>
                    </c:if>
                    <c:if test="${item.fReceipttype == 2}">
                        <td>微信</td>
                    </c:if>
                    <td>${item.fMsg}</td>
                    <td>${item.isCertified eq 0 ? '是' : '否' }</td>
                    <td>${item.isUndo eq 0 ? '是' : '否' }</td>
                    <c:if test="${item.fStatus == 0}">
                        <td>挂单中</td>
                    </c:if>
                    <c:if test="${item.fStatus == 1}">
                        <td>售卖中</td>
                    </c:if>
                    <c:if test="${item.fStatus == 2}">
                        <td>已售卖</td>
                    </c:if>
                    <c:if test="${item.fStatus == 3}">
                        <td>已撤销</td>
                    </c:if>
                    <td>${item.isRealName eq 0 ? '是' : '否' }</td>
                    <td>${item.fUsr_Name}</td>
                    <c:if test="${item.isCheck == 0}">
                        <td>未审核</td>
                    </c:if>
                    <c:if test="${item.isCheck == 1}">
                        <td>已审核</td>
                    </c:if>
                    <c:if test="${item.isCheck == 2}">
                        <td>审核失败</td>
                    </c:if>
                </tr>
            </c:forEach>
        </tbody>
    </table>

    <div class="panelBar">
        <div class="pages">
            <span>总共: ${totalCount}条</span>
        </div>
        <div class="pagination" targetType="navTab" totalCount="${totalCount}"
            numPerPage="${numPerPage}" pageNumShown="5"
            currentPage="${currentPage}"></div>
    </div>

</div>

<script>
	$(function(){
		var fStatus = $("input[name='fStatus']").val();
		var osel=document.getElementById("fStatus");
		var opts=osel.getElementsByTagName("option");
		for(var i=0;i<4;i++){
			if(fStatus==opts[i].value){
				opts[i].selected=true;
				break;
			}
		}
	})
</script>