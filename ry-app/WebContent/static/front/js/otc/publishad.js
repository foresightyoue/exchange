
/***************************提交发布广告*****************************/
function publishad() {
	var type_ = $("input[name='type_']:checked").val();
	var adr_ = $("input[name='adr_']").val();
	var checked_ = $("input[name='checked_']").val();
	var checkedBank = $("input[name='checkedBank']").val();
	var Btype_ = $("input[name='Btype_']").val();
	var yijia_ = $("input[name='yijia_']").val();
	var price_ = $("input[name='price_']").val();
	var fLowprice_ = $("input[name='fLowprice_']").val();
	var fSmallprice_ = $("input[name='fSmallprice_']").val();
	var fBigprice_ = $("input[name='fBigprice_']").val();
	var fReceipttype_ = $("select[name='fReceipttype_']").val();
	var orderType_ = $("input[name='orderType_']").val();
	//var msg_ = $("input[name='msg_']").val();
	var msg_ = $("#msg_").val();
	var fid_ = $("input[name='fid_']").val();
	var fTotal = $("input[name='fTotal']").val();
	var isCertified_ = $("input[name='isCertified_']:checked").val();
	var ffee = $("input[name='ffee']").val();
	var limitMoney=(parseFloat(fBigprice_)/parseFloat(price_))*(1.0+parseFloat(ffee));
	if(checked_ == "0" && checkedBank == "0"){
		alert("您还未完善收款账户信息，请先完善收款账户信息后再发布广告！")
		return false;
	}
	if(type_ == ""){
		alert("发布类型不能为空！")
		return false;
	}
	if(adr_ == ""){
		alert("地区不能为空！")
		return false;
	}
	if(Btype_ == ""){
		alert("币种不能为空！")
		return false;
	}
	if(yijia_ == ""){
		alert("溢价不能为空！")
		return false;
	}
	if(fLowprice_ == ""){
		alert("最低价不能为空！")
		return false;
	}
	if(parseFloat(fLowprice_)<=0){
		alert("最低价不能小于0");
		return false;
	}
	if(fSmallprice_ == ""){
		alert("最小限额不能为空！")
		return false;
	}
	if(parseFloat(fSmallprice_) <= 0){
		alert("最小限额不能小于0");
		return false;
	}
	if(fBigprice_ == ""){
		alert("最大限额不能为空！")
		return false;
	}
	if(parseFloat(fBigprice_) <= 0){
		alert("最大限额不能小于0");
		return false;
	}
	if(fReceipttype_ == ""){
		alert("收款类型不能为空！")
		return false;
	}
	if(price_ == ""){
		alert("单价不能为空!")
		return false;
	}
	if(parseFloat(price_) <= 0){
		alert("单价不能小于0");
		return false;
	}
	if(msg_ == ""){
		alert("广告留言不能为空！")
		return false;
	}
	if(isCertified_ == ""){
		alert("发布类型不能为空！")
		return false;
	}
	if(parseFloat(fBigprice_)< parseFloat(fSmallprice_)){
		alert("最小限额不能大于最大限额！");
		return false;
	}
	if(fTotal <=limitMoney && type_ == 0){
		alert("你的账户余额不足，不能发布广告，请及时充值！")
		return false;
	}
    var msg = "您真的确定要发布此广告吗？\n\n请确认！";
    if (confirm(msg)==true){
        $.ajax({
            type: "post",
            url : "/otc/publishAd.html",
            data : {
            	"orderType_": orderType_,
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
                "isCertified_" : isCertified_,
                "fid_": fid_
            },
            dataType: "json",
            secureuri: false,
            async : true,
            success: function (data) {
                if(data.code == 0){
                    layer.msg(data.msg);
                    window.location.href="/otc/index.html?coinType="+Btype_;
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
    }else{
        return false;
    }
}

/******************************撤销广告********************************/
function undoAd(fid_) {
	var coin_id = $("#coin_id").val();
	if(fid_ == "" || fid_ == null){
		alert("此广告存在异常，请联系管理员！")
		return false;
	}
    var msg = "您真的确定要撤销此广告吗？\n\n请确认！";
    if (confirm(msg)==true){
        $.ajax({
            type: "post",
            url : "/otc/undoAd.html",
            data : {
                "fid_" : fid_
            },
            dataType: "json",
            secureuri: false,
            async : true,
            success: function (data) {
                if(data.code == 0){
                    layer.msg(data.msg);
                    window.location.href="/otc/otcUserOrder.html?coinType="+coin_id;
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
    }else{
        return false;
    }
}
