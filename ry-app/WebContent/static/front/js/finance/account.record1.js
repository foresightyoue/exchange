var  record= {
	search : function(begindate, enddate) {
		var url = $("#recordType").val();
		//var datetype = $(".datetype").val();
		var datetype = $("#datetype option:selected").val();
		var begindate = begindate ? begindate : $("#begindate").val();
		var enddate = enddate ? enddate : $("#enddate").val();
		if (datetype > 0) {
			url="/m"+ url + "&datetype=" + datetype;
		} else {
			url="/m"+ url + "&datetype=" + datetype + "&begindate=" + begindate + "&enddate=" + enddate;
		}
		window.location.href = url;
		
	}
};
$(function() {
	$('#begindate').change(function() {
		var date = $("#begindate").val();
		var datetype = $("#datetype option:selected").val(0);
				record.search(date, null);
	});
	$('#enddate').change(function() {
		var date = $("#enddate").val()
		var datetype = $("#datetype option:selected").val(0);
				record.search(null, date);
	});
	$(".datatime").change(function() {
		//$("#datetype").val($(this).data().type);
		record.search();
	});
	$("#recordType").change(function() {
		record.search();
	});
});