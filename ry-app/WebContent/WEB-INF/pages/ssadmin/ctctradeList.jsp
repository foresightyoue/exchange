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
<form id="pagerForm" method="post" action="ssadmin/ctctradeList.html">
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
</form>


<div class="pageHeader">
    <form onsubmit="return navTabSearch(this);"
        action="ssadmin/ctctradeList.html" method="post">
        <div class="searchBar">

            <table class="searchContent">
                 <tr>
                    <td>订单号：<input type="text" name="keywords"
                        value="${keywords}" size="40" />
                    </td>
                    <td>交易类型：<select name="tradetype" id="tradetype">
                        <option value="-1" >全部</option>
                        <option value="0" >买入</option>
                        <option value="1" >卖出</option>
                    </select>
                    </td>
                    <td>买方账号:<input type="text" name="buyer" value="${buyer}"
                       size="11"/>
                    </td>
                    <td>卖方账号:<input type="text" name="seller" value="${seller}"
                       size="11"/>
                    </td>
                 </tr>
                 <tr>
                    <td>订单状态：<select name="orderstatus" id="orderstatus">
                        <option value="-1" >全部</option>
                        <option value="0" >待派单</option>
                        <option value="1" >待确认</option>
                        <option value="2" >已确认</option>
                        <option value="3" >申诉中</option>
                    </select>
                    </td>
                    <td>开始日期： <input type="text" name="logDate" class="date"
                        readonly="true" value="${logDate }" />
                    </td>
                    <td>结束日期： <input type="text" name="logDate2" class="date"
                        readonly="true" value="${logDate2 }" />
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
              <li><a class="icon" href="ssadmin/ctctradeExport.html"
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
                <th width="22">序号</th>
                <th width="60">订单号</th>
                <th width="60">交易币种</th>
                <th width="60">订单创建者</th>
                <th width="60">类型</th>
                <th width="60">买方</th>
                <th width="60">卖方</th>
                <th width="60">单价</th>
                <th width="60">数量</th>
                <th width="60">总金额(￥)</th>
                <th width="60">状态</th>
                <th width="60">下单时间</th>
                <th width="60">订单最后改动用户</th>
                <th width="60">确认时间</th>
                <th width="60">备注</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${ctctradeList}" var="item" varStatus="num">
                <tr target="sid_user" rel="${item.fid}">
                    <td>${num.index+1 }</td>
                    <td>${item.fId}</td>
                    <td>${item.fCoinType}</td>
                    <td>${item.fCreateUserName}</td>
                    <td>${item.fType eq 0 ? '买入' : '卖出'}</td>
                    <td>${item.fBuyerName }</td>
                    <td>${item.fSellerName }</td>
                    <td><fmt:formatNumber value="${item.fPrice}" pattern="##.######" maxIntegerDigits="15" maxFractionDigits="6"/></td>
                    <td>${item.fNum}</td>
                    <td><fmt:formatNumber value="${item.fCnyTotal}" pattern="##.######" maxIntegerDigits="15" maxFractionDigits="6"/></td>
                    <c:if test="${item.fappeal == 0}">
                        <td>${status[item.fStatus]}</td>
                    </c:if>
                    <c:if test="${item.fappeal == 2}">
                        <td>${status[item.fStatus]}</td>
                    </c:if>
                    <c:if test="${item.fappeal == 1}">
                        <td>${status[item.fStatus]} <input type="button" onclick="seeDetails('${item.fId}','${item.fOrderNum}')"  value="查看详情"/> </td>
                    </c:if>
                    <td><fmt:formatDate value="${item.fCreateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    <td>${item.fUpdateUserName}</td>
                    <td><fmt:formatDate value="${item.fConfirmTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    <td>${item.fremark}</td>
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
            
            
        </div>
    </div>
</div>
    
    <script type="text/javascript">
    function seeDetails(orderid,OrderNum){
    	var load = layer.load(1);
    	$.ajax({
            url : "/ssadmin/seeAppeal.html",
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
               if(data.type == 0){
            	   $("#caozuo").append("<input type='button' id='fx' value='放行' data1='"+data.orderId+"' data2='"+OrderNum+"'/>");
               }else{
            	   $("#caozuo").append("<input type='button' id='qxz' value='取消此订单' data1='"+data.orderId+"' data2='"+OrderNum+"'/>");
               }
               layer.close(load);
            },
            error: function (data) {
                layer.msg("上传错误");
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
    	var orderId = $(this).attr("data1");
    	var OrderNum = $(this).attr("data2");
    	fx(orderId,OrderNum);
    })
    
    $("body").on("click","#qxz",function(){
        var orderId = $(this).attr("data1");
        var OrderNum = $(this).attr("data2");
        qx(orderId,OrderNum);
    })
    
    function fx(orderId,OrderNum){
    	layer.confirm('确定放行此订单？', {
            btn: ['确认放行','取消'] //按钮
          }, function(){
              $.ajax({
                  url : "/ssadmin/confirmOrder1.html",
                  data : {
                      "OrderNum":OrderNum
                  },
                  type: "post",
                  dataType: "json",
                  secureuri: false,
                  async : true,
                  success: function (data) {
                      if(data.code == 0){
                    	  layer.msg("放行成功");
                    	  successAppeal(orderId);
                      }else{
                          layer.msg(data.msg);
                      }
                  },
                  error: function (data) {
                      layer.msg("上传错误");
                  }
              });
          }, function(){
              layer.msg('取消放行');
          }); 
        
    }
    	
    function qx(orderId){
    	layer.confirm('确定取消此订单？', {
            btn: ['确认取消','取消'] //按钮
          }, function(){
        	$.ajax({
                url : "/ssadmin/confirmOrder2.html",
                data : {
                    "orderNo":orderId
                },
                type: "post",
                dataType: "json",
                secureuri: false,
                async : true,
                success: function (data) {
                    if(data.code == 200){
                        layer.msg(data.msg);
                        window.reload();
                    }else{
                        layer.msg(data.msg);
                    }
                },
                error: function (data) {
                    layer.msg("上传错误");
                }
            });
          }, function(){
          }); 
    }
    
    function successAppeal(orderId){
        $.ajax({
            url : "/ssadmin/successAppeal.html",
            data : {
                "orderNo":orderId
            },
            type: "post",
            dataType: "json",
            secureuri: false,
            async : true,
            success: function (data) {
                if(data.code == 0){
                    layer.msg('放行成功!');
                    window.reload();
                }else{
                    layer.msg('放行成功,订单状态修改出错!');
                }
            },
            error: function (data) {
                layer.msg("上传错误");
            }
        });
    }
</script>
</div>
