+function fileUpload(url){
    
}
/***************************ajax文件上传 end*****************************/


/***************************文件上传时，判断选择的文件是否符合 start*****************************/
/**
 * 文件上传时，判断文件上传的类型是否符合要求
 * @param inputId 上传框的id
 * @param fileType  文件类型，不传，则默认为img图片
 */
function checkFileType(inputId, fileType) {
    var check_flag = true;
    if(!fileType){
        fileType = "img";
    }

    //获取文件上传的后缀，进行格式校验
    var filepath = $("#"+inputId).val();
    var extStart = filepath.lastIndexOf(".");
    var ext = filepath.substring(extStart, filepath.length).toUpperCase();
    //图片校验
    if(fileType == "img"){
        if (ext != ".JPG" && ext != ".JPEG" && ext != ".PNG" && ext !=".BMP" && ext != ".APK") {
            alert("请上传jpg、png、jpeg、.bmp格式图片");
            $("#"+inputId).val("");
            check_flag = false;
            return check_flag;
        }
    }else if(fileType == "video") {  //音频校验
        if (ext != ".MP4" && ext != ".AVI" && ext != ".WMV" && ext != ".FLV" && ext != ".MKV" && ext != ".MOV") {
            alert("请上传mp4、avi、wmv、flv、mkv、mov格式的文件");
            $("#"+inputId).val("");
            check_flag = false;
            return check_flag;
        }
    }else if(fileType == "txt"){
        if (ext != ".TXT") {
            alert("请上传txt格式的文件");
            $("#"+inputId).val("");
            check_flag = false;
            return check_flag;
        }
    }else if(fileType == "dpdf") {
        if (ext != ".DOC" && ext != ".PDF" ) {
            alert("请上传doc、pdf格式的文件");
            $("#"+inputId).val("");
            check_flag = false;
            return check_flag;
        }
    }else{
        check_flag = false;
        alert("格式错误,fileType不符合限制");
    }
    return check_flag;
}

/***************************文件上传时，判断选择的文件是否符合 end*****************************/