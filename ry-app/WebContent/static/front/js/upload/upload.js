$(function(){
	//$("form").attr("enctype","multipart/form-data");
	$("#component8").css({"width": "700px","margin": "0 auto","margin-right": "370px","text-align": "center","float":"rigth"});
	$("form").append("<input type='hidden' name='path' value=''>");
	$("[name=isShow]").after("<p style='color:red;font-size:8px;padding-left:30px;margin-top:5px;'>(允许上传的图片类型为jpg/png/gif,每次只能上传一张，建议尺寸为375*142)</p>");
	$("[name=submit]").wrap("<div id='buttonspan'></div>")
	$("#buttonspan").css("margin-left","40px");
	var show = "<div class='imgshow'><div>";
	$("[name=myfile]").wrap(show);
	
	if ($("[name=UID_]").val() != undefined) {
		$(".imgshow").each(function(j,e) {
			var imgUrl = $(e).find(".myfile").attr('value');
			$(e).css({"background-size":"150px 150px","background-image":"url("+imgUrl+")"});
			$(e).find(".myfile").attr("disabled","disabled");
		});
		$(".imgshow").prepend("<img class='cancel' src='/img/cancel.png'></img>");
		   if ($(".imgshow").length != 3) {
			  $("#component8").append("<div class='imgshow'> <div><input name='myfile' class='myfile'  type='file' placeholder='点击选择图片'> </div></div>");
		}
	}
	
	$("[name=submit]").click(function(){
		var filePath =  $("[name=path]").val();
           if(!(filePath.endsWith(".jpg")||filePath.endsWith(".png")||filePath.endsWith(".gif"))){
                layer.msg("图片上传有误，请刷新后重新选择图片");
               return false;
           }/*else{
           	    //提交表单
               	var title = $("[name=title]").val();
               	var order = $("[name=order]").val();
               	$("form").attr("action","FrmImg.upLoadImg?title="+title+"&order="+order);
               	$("form").submit();
           }*/
	});
	//取消上传的图片
	$("#component8").on("click",".imgshow .cancel",function(){
		var e = this;
		var backGroundImg = $(e).parent(".imgshow").css("background-image");
		var url = backGroundImg.substring(5,backGroundImg.length-2);
		var path = $("[name=path]").val();
		var pathLength =  path.split(",").length;
		if (pathLength == 1) {
			path = path.replace(url ,"");
		}else {
			var urlIndex = path.indexOf(url);
			var hasDot = path.indexOf(",",urlIndex) != -1 ? true : false;;
			if (hasDot) {
				path = path.replace(url + ",","");
			}else{
				path = path.replace("," + url,"");
			}
		}
		
		$("[name=path]").val(path);
		$(e).parent(".imgshow").remove();
		if (pathLength == 3) {
			 $("#component8").append("<div class='imgshow'> <div><input name='myfile' class='myfile'  type='file' placeholder='点击选择图片'> </div></div>");
		}
	});
	
	
	 //修改背景图为选择的图片，选择后上传
	function imgOpload(){
		alert(123);
    	 var imgLength = $(".imgshow").length;
    	 var e = this;
      	 var filePath =  $(e).val();
      	 if(!(filePath.endsWith(".jpg")||filePath.endsWith(".png")||filePath.endsWith(".gif"))){
               layer.msg("请选择jpg、png、gif格式的图片！");
               return false;
           }else{
        	   alert(456);
           	layer.msg('上传中', {icon: 16, shade: 0.01, time : 0});
           	 var formData = new FormData($("form")[0]);
                /*$.ajax({
                    url : "FrmApprovedData.upLoadImg",
                    type : "post",
                    dataType : "json",
                    data : formData,
                    cache: false,  
                    contentType: false,  
                    processData: false,
                    success : function(data){
                    	layer.closeAll('loading');
                        if(!(data.endsWith(".jpg")||data.endsWith(".png")||data.endsWith(".gif"))){
                           layer.msg("上传失败");
                           location.href="FrmImg.addImg";
                       }else{
                    	 layer.msg("上传成功");
                    	 //上传成功后显示图片，并在添加一个上传框
                    	 if (imgLength >= 3) {
                     		 layer.msg("最多只能上传3张图片！")
                     	 }if (imgLength >= 1) {
                     		 layer.msg("最多只能上传1张图片！")
                     	 }else {
                     		  $("#component8").append("<div class='imgshow'> <div><input name='myfile' class='myfile'  type='file' placeholder='点击选择图片'> </div></div>");
                     	 }
                         var path =  $("[name=path]").val();
                         if (path == "" || path == null) {
                        	 $("[name=path]").val(data);
						}else {
							$("[name=path]").val(path + "," + data);
						}
                         $(e).parents(".imgshow").css({"background-size":"150px 150px","background-image":"url("+data+")"});
                         $(e).parents(".imgshow").prepend("<img class='cancel' src='/img/cancel.png'></img>");
                         $(e).attr("disabled","disabled");
                       }
                    },
                    error : function(){
                         layer.msg("网络错误，请刷新后重试！");
                         layer.closeAll('loading');
                    }
                });*/
                 
           }
          
       });
	
	//删除
	$("tr td a:contains('删除')").click(function(){
	   if(confirm("是否删除该图片？")){
         var UID_ = this.href.split("=")[1];
            $.ajax({
                url: "FrmImg.removeImg",
                type : "post",
                async : false,
                dataType : "json",
                data : {"UID_":UID_},
                success : function(data){
                    if("success" == data){
                        alert("删除成功！");
                    }else{
                        alert("删除失败，"+data);
                    }
                },
                error : function(){
                    alert("网络错误，请刷新后重试！");
                }
            });
            location.href="FrmImg";
        }else{
            return false;
        }
	});
})