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
<form id="pagerForm" method="post" action="ssadmin/userotcOrderList.html">
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
</form>


<div class="pageHeader">
    <form onsubmit="return navTabSearch(this);"
        action="ssadmin/userotcOrderList.html" method="post">
        <div class="searchBar">

            <table class="searchContent">
                 <tr>
                    <td>订单号：<input type="text" name="keywords"
                        value="${keywords}" size="40" />
                    </td>
                    <td>用户账户：<input type="text" name="userlogin"
                        value="${userlogin}" size="40" />
                    </td>
                    <td>交易状态：<select name="fTrade_Status" id="fTrade_Status">
                        <option value="" >全部</option>
                        <option value="1" >已下单</option>
                        <option value="2" >待验证</option>
                        <option value="3" >已完成</option>
                        <option value="4" >已取消</option>
                        <option value="5" >申述中</option>
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
        <shiro:hasPermission name="ssadmin/viewVirtualCaptual.html">
				<li id="who"><a class="edit" href="javascript:void(0);"
					height="320" width="800" target="ajaxTodo"
					rel="viewtransfer" title="确定要撤销吗?" id="cancel"><span>撤销</span>
				</a></li>
		  </shiro:hasPermission>
		  <li class="line">line</li>
          <shiro:hasPermission name="ssadmin/userExport.html">
              <li><a class="icon" href="ssadmin/userotctradeExport.html"
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
                <th width="22"><input type="checkbox" group="ids"
					class="checkboxCtrl">
				</th>
                <th width="30">订单id</th>
                <th width="60">创建时间</th>
                <th width="50">币种名称</th>
                <th width="50">用户账户</th>
                <th width="50">日志类型</th>
                <th width="50">订单号</th>
                <th width="85">下单时间</th>
                <th width="60">交易价格</th>
                <th width="60">交易数量</th>
                <th width="40">交易金额</th>
                <th width="40">交易对象</th>
                <th width="40">交易状态</th>
                <th width="40">交易方式</th>
                <th width="40">订单评价状态</th>
                <th width="40">截止期限</th>
                <th width="40">接单广场ID</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${userotcOrderList}" var="item" varStatus="num">
                <tr target="sid_user" rel="${item.fid}">
                	<td><input name="ids" value="${item.fId}"
						type="checkbox">
					</td>
                    <td>${item.fId}</td>
                    <td><fmt:formatDate value="${item.fCreateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    <td>${item.fName}</td>
                    <td>${item.fUsr_id}</td>
                    <c:if test="${item.fType == 1}">
                        <td>购买</td>
                    </c:if>
                    <c:if test="${item.fType == 2}">
                        <td>出售</td>
                    </c:if>
                    <td>${item.fOrderId}</td>
                    <td><fmt:formatDate value="${item.fOrder_Time}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    <td>${item.fTrade_Price}</td>
                    <td>${item.fTrade_Count }</td>
                    <td>${item.fTrade_SumMoney }</td>
                    <td>${item.fTrade_Object }</td>
                    <c:if test="${item.fTrade_Status == 1}">
                        <td>已下单</td>
                    </c:if>
                    <c:if test="${item.fTrade_Status == 2}">
                        <td>待验证</td>
                    </c:if>
                    <c:if test="${item.fTrade_Status == 3}">
                        <td>已完成</td>
                    </c:if>
                    <c:if test="${item.fTrade_Status == 4}">
                        <td>已取消</td>
                    </c:if>
                    <c:if test="${item.fTrade_Status == 5}">
                         <td>申诉中 <input type="button" onclick="seeDetails('${item.fOrderId}')"  value="查看详情"/></td>
                    </c:if>
                    <c:if test="${item.fTrade_Status == 6}">
+                        <td>申述完成</td>
                     </c:if>
                    <td>${item.fTrade_Method }</td>
                    <td>${item.fAssess eq 0 ? '好评' : '差评' }</td>
                    <td><fmt:formatDate value="${item.fTrade_DeadTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    <td>${item.fAd_Id }</td>
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

<script>
$(document).ready(function(){
	//撤销
	$("#who").hover(function() {
		var check= new Array();
		$('input[name="ids"]:checked').each(function(){ 
			check.push($(this).val()); 
			});
		var url="ssadmin/userotcOrdersCancel.html?uid="+check;
		$("#cancel").attr("href",url);
		/* console.log("check"+check);
		setTimeout(function(){
			window.location.reload();
		},5000);  */
	})
})
</script>

<div class="mars"></div>
<div class="details_content">
    <div class="details_hint clearfix">
        <span class="fl" style="font-weight: 600;margin-top: 15px;">查看申诉申请</span>
        <span class="fr error1" style="font-size: 18px;color: #666;margin-top: 10px;cursor: pointer;">X</span>
    </div>
    <div class="content_details">
        <div class="">申诉图片：</div>
        <div class="img_box">
            <img src="" alt="" id="imgpath1">
            <img src="" alt="" id="imgpath2">
            <img src="" alt="" id="imgpath3">
        </div>
        <div class="content_remark clearfix">
            <div class="fl remark_hint">备注:</div>
            <div class="fl remark_text" id="remark"></div>
        </div>
        <div class="btn_group" id="caozuo">
            <input type='button' id='fx' value='放行' />
            <input type='button' id='qxb' value='取消申述' />
        </div>
    </div>
</div>
    
    <script type="text/javascript">
    $(function() {
        $("#fTrade_Status").val("${fTrade_Status}");
     }); 
    function seeDetails(orderid,OrderNum){
    	var load = layer.load(1);
    	$.ajax({
            url : "/ssadmin/otcseeAppeal.html",
            data : {
                "orderId":orderid
            },
            type: "post",
            dataType: "json",
            secureuri: false,
            async : true,
            success: function (data) {
               $("#imgpath1").prop("src",data.imgpath1);
               $("#imgpath2").prop("src",data.imgpath2);
               $("#imgpath3").prop("src",data.imgpath3);
               $("#remark").html(data.remark);
               $("#caozuo").append("<input type='hidden' id='fx1' value='放行' data1='"+data.orderId+"'/>");
               $("#caozuo").append("<input type='hidden' id='qx1' value='取消申述' data1='"+data.orderId+"'/>");
               layer.close(load);
            },
            error: function (data) {
                layer.msg("申述失败!");
            }
        });
    	$(".mars").show().next().show();
    }
    $("body").on("click",".error1",function(){
    	$(".mars").hide().next().hide();
    })
    </script>
    <script type="text/javascript">
    $(function() {
       $("#orderstatus").val("${orderstatus}");
       $("#tradetype").val("${tradetype}");
    }); 
    
    $("body").on("click","#fx",function(){
    	var orderId = $("#fx1").attr("data1");
    	fx(orderId);
    })
    
    $("body").on("click","#qxb",function(){
    	var orderId = $("#qx1").attr("data1");
    	qx(orderId);
    })
    
    function fx(orderId){
    	layer.confirm('确定放行此订单？', {
            btn: ['确认放行','取消'] //按钮
          }, function(){
              $.ajax({
            	  url : "/ssadmin/userconfirmOrder1.html",
                  data : {
                	  "orderId":orderId
                  },
                  type: "post",
                  dataType: "json",
                  secureuri: false,
                  async : true,
                  success: function (data) {
                      if(data.code == 0){
                    	  layer.msg("放行成功!");
                    	  successAppeal(orderId);
                      }else{
                          layer.msg("放行失败!");
                      }
                  },
                  error: function (data) {
                      layer.msg("网络连接失败，请稍后重试！");
                  }
              });
          }, function(){
              layer.msg('取消放行');
          }); 
        
    }
    
    function successAppeal(orderId){
        $.ajax({
        	url : "/ssadmin/usersuccessAppeal.html",
            data : {
            	"orderId":orderId
            },
            type: "post",
            dataType: "json",
            secureuri: false,
            async : true,
            success: function (data) {
                if(data.code == 0){
                    layer.msg('放行成功!');
                    window.location.reload();
                }else{
                    layer.msg('放行成功,订单状态修改出错!');
                }
            },
            error: function (data) {
                layer.msg("网络连接失败，请稍后重试！");
            }
        });
    } 
    
    function qx(orderId){
    	layer.confirm('确定此订单取消申述？', {
            btn: ['确认取消申述','取消'] //按钮
          }, function(){
              $.ajax({
            	  url : "/ssadmin/usererrorAppeal.html",
                  data : {
                	  "orderId":orderId
                  },
                  type: "post",
                  dataType: "json",
                  secureuri: false,
                  async : true,
                  success: function (data) {
                      if(data.code == 0){
                    	  layer.msg("取消申述成功!");
                    	  window.location.reload();
                      }else{
                          layer.msg("取消申述失败!");
                      }
                  },
                  error: function (data) {
                      layer.msg("网络连接失败，请稍后重试！");
                  }
              });
          }, function(){
              layer.msg('取消申述');
          }); 
        
    }
    
</script>
</div>
