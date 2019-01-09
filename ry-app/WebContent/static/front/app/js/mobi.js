$(function(){
	/* 选择币种 */
	/*$('#currencyChose').on('click', function() {
        $('#moneyType').mobiscroll().treelist({
            theme : "ios7",
            lang : "zh",
            display : 'bottom',
            inputClass : 'tmp',
            headerText : "${rs_chooseMoney}",
            circular : [ true, false, false ],
            defaultValue : [ 1 ],
            setText : "${rs_alert}",
            cancelText : "${rs_cancel}",
            onSelect : function(valueText) {
                var curbz=$(this).find('li').eq(valueText).find('input').val();
                $('#choseResulet').val(curbz);
                var key = $(this).find('li').eq(valueText).find('input').attr('name')+"";
                var obj = new Function("return" + '${sys}')();//转换后的JSON对象
                var rate = obj[key];
                $("#moneyRate").val(parseFloat(rate));
                var amount = $("#amount").val();
                var price = $("#price").val();
                if (amount != null && amount !='' && price!=null && price!='') {
                    var getMoney = accMul(parseFloat(amount), accMul(price,rate))+'('+curbz+')';
                    $("#getMoney").val(getMoney);
                }
            },
            onChange : function(event, inst) {
                
            }
        });
        $("input[id^=moneyType]").focus();
    });*/
	
});

function mobiDate(node){
	$("#"+node).mobiscroll().date({ 
        theme: 'ios7', 
        display: 'bottom',
        mode: 'mixed', //日期选择模式
        dateFormat: 'yy-mm-dd', // 日期输出格式
        timeFormat: 'HH:ii:ss', // 日期输出格式
        dateOrder: 'yymmdd', //面板中日期排列格式
        timeWheels: 'HHiiss', //面板中日期排列格式
        setText: '确定', //确认按钮名称
        cancelText: '取消',//取消按钮名籍我
        dayText: '日', //面板中日文字
        monthText: '月', //面板中月文字
        yearText: '年', //面板中年文字
        endYear: 2020, //结束年份
        hourText: '时',
        minuteText: '分',
        secText: '秒',
        animate: 'slidevertical',
        lang: 'zh',
    });
	$("input[id^="+node+"]").focus();
}

function mobiTreeList(inputNode, listNode, callback){
	$('#currencyChose').on('click', function() {
        $('#moneyType').mobiscroll().treelist({
            theme : "ios7",
            lang : "zh",
            display : 'bottom',
            inputClass : 'tmp',
            headerText : "${rs_chooseMoney}",
            circular : [ true, false, false ],
            defaultValue : [ 1 ],
            setText : "${rs_alert}",
            cancelText : "${rs_cancel}",
            onSelect : function(valueText) {
            	callback();
            },
            onChange : function(event, inst) {
                
            }
        });
        $("input[id^=moneyType]").focus();
    });
}