
/***************************提交发布广告*****************************/

$("#ok").click(function(){
	var orderId = $("input[name='orderId']").val();
	var fAd_Id = $("input[name='fAd_Id']").val();
	var fw = $("input[name='fw']").val();
	var coin_id = $("input[name='coin_id']").val();
	if(orderId == ""){
		layer.msg("此订单存在异常，请联系管理员！")
		return false;
	}
	if(fAd_Id == ""){
		layer.msg("此订单存在异常，请联系管理员！")
		return false;
	}
    var msg = "您真的确定您已经支付完成了吗?";
    layer.confirm(msg,{btn:['确定','取消']},function(){
        $.ajax({
            type: "post",
            url : "/m/otc/orderok.html",
            data : {
                "orderId" : orderId,
                "fAd_Id" : fAd_Id
            },
            dataType: "json",
            secureuri: false,
            async : true,
            success: function (data) {
                if(data.code == 100){
                    layer.msg(data.message);
                    if(fw == 0){
                    	window.location.href="/m/otc/orderList.html?coinType="+coin_id+"&fw=0";
                    }else{
                    	window.location.href="/m/otc/orderShellList.html?coinType="+coin_id+"&fw=0";
                    }
                }else if(data.code == 200){
                    layer.msg(data.message);
                }else{
                    layer.msg(data.message);
                }
            },
            error: function (data) {
                layer.msg("网络繁忙，请稍后再试！");
            }
        });
    }),function(){
    	layer.closeAll();
    }
})

$("#freed").click(function(){
	var orderId = $("input[name='orderId']").val();
	var fAd_Id = $("input[name='fAd_Id']").val();
	var SumMoney_ = $("input[name='SumMoney_']").val();
	var fw = $("input[name='fw']").val();
	var fShortName = $("input[name='fShortName']").val();
	var coin_id = $("input[name='coin_id']").val();
	if(orderId == ""){
	    layer.msg("此订单存在异常，请联系管理员！")
		return false;
	}
	if(fAd_Id == ""){
		layer.msg("广告单存在异常，请联系管理员！")
		return false;
	}
    var msg = "请确认收款并释放数字币？\n\n此操作将会使数字货币立即转到买家账户，请务必在释放数字币之前确认已经收到款项"+ SumMoney_ +"，此过程无法逆转，你确认要释放数字币吗？\n\n请确认！";
   layer.confirm(msg,{btn:['确认','取消']},function(){
	   $.ajax({
           type: "post",
           url : "/m/otc/orderfreed.html",
           data : {
               "orderId" : orderId,
               "fAd_Id" : fAd_Id,
               "fShortName":fShortName
           },
           dataType: "json",
           secureuri: false,
           async : true,
           success: function (data) {
               if(data.code == 100){
                   layer.msg(data.message);
                   if(fw == 0){
                   	window.location.href="/m/otc/orderList.html?coinType="+coin_id+"&fw=0";
                   }else{
                   	window.location.href="/m/otc/orderShellList.html?coinType="+coin_id+"&fw=0";
                   }
               }else if(data.code == 200){
                   layer.msg(data.message);
               }else{
                   layer.msg(data.message);
               }
           },
           error: function (data) {
               layer.msg("网络繁忙，请稍后再试！");
           }
       });
   }),function(){
	   layer.closeAll();
   }
})
function publishad() {
	var type_ = $("input[name='type_']:checked").val();
	var adr_ = $("input[name='adr_']").val();
	var Btype_ = $("input[name='Btype_']").val();
	var yijia_ = $("input[name='yijia_']").val();
	var price_ = $("input[name='price_']").val();
	var fLowprice_ = $("input[name='fLowprice_']").val();
	var fSmallprice_ = $("input[name='fSmallprice_']").val();
	var fBigprice_ = $("input[name='fBigprice_']").val();
	//var fReceipttype_ = $("select[name='fReceipttype_']").val();
	var fReceipttype_ = $("input[name = 'fReceipttype_']:checked").val();
	var msg_ = $("input[name='msg_']").val();
	var isCertified_ = $("input[name='isCertified_']:checked").val();
	alert(type_);
	if(type_ == ""){
		layer.msg("发布类型不能为空！")
		return false;
	}
	if(adr_ == ""){
		layer.msg("地区不能为空！")
		return false;
	}
	if(Btype_ == ""){
		layer.msg("币种不能为空！")
		return false;
	}
	if(yijia_ == ""){
		layer.msg("溢价不能为空！")
		return false;
	}
	if(fLowprice_ == ""){
		layer.msg("最低价不能为空！")
		return false;
	}
	if(fSmallprice_ == ""){
		layer.msg("最小限额不能为空！")
		return false;
	}
	if(fBigprice_ == ""){
		layer.msg("最大限额不能为空！")
		return false;
	}
	if(fReceipttype_ == ""){
		layer.msg("收款类型不能为空！")
		return false;
	}
	if(msg_ == ""){
		layer.msg("广告留言不能为空！")
		return false;
	}
	if(isCertified_ == ""){
		layer.msg("发布类型不能为空！")
		return false;
	}
    var msg = "您真的确定要发布此广告吗？\n\n请确认！";
    layer.confirm(msg,{btn:['确认','取消']},function(){
        $.ajax({
            type: "post",
            url : "/m/otc/publishAd.html",
            data : {
                "type_" : type_,
                "adr_" : adr_,
                "Btype_" : Btype_,
                "yijia_" : yijia_,
	            "price_" : price_,
                "fLowprice_" : fLowprice_,
                "fSmallprice_" : fSmallprice_,
                "fBigprice_" : fBigprice_,
                "fReceipttype_" : fReceipttype_,
                "msg_" : msg_,
                "isCertified_" : isCertified_
            },
            dataType: "json",
            secureuri: false,
            async : true,
            success: function (data) {
                if(data.code == 0){
                    layer.msg(data.msg);
                    window.location.href="/m/otc/index.html?typeVal="+type;
                }else if(data.code == 1){
                    layer.msg(data.msg);
                }else{
                    layer.msg(data.msg);
                }
            },
            error: function (data) {
                layer.msg("网络繁忙，请稍后再试！");
            }
        });
    }),function(){
    	layer.closeAll();
    }
}
