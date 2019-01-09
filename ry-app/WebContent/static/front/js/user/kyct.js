//上传图片
function uploadBankImg1() {
    if (checkFileType('picBank1', 'img')) {
        fileUpload("/common/upload.html", "4", "picBank1", "picBank1Url", null, null, imgbakcBank1, "resultUrl");
    }
}

function imgbakcBank1(resultUrl) {
    $(".picBank1show").css("background-image", "url(" + resultUrl + ")");
    $('label[for="picBank1"]').text('重新上传');
    /*$('.pic1name').text($('#pic1').val().split('\\').pop())
        .siblings().text('已选择');*/
}

function uploadBankImg2() {
    if (checkFileType('picBank2', 'img')) {
        fileUpload("/common/upload.html", "4", "picBank2", "picBank2Url", null, null, imgbakcBank2, "resultUrl");
    }
}

function imgbakcBank2(resultUrl) {
    $(".picBank2show").css("background-image", "url(" + resultUrl + ")");
    $('label[for="picBank2"]').text('重新上传');
    /*$('.pic2name').text($('#pic2').val().split('\\').pop())
       .siblings().text('已选择');*/
}

function uploadBankImg3() {
    if (checkFileType('picBank3', 'img')) {
        fileUpload("/common/upload.html", "4", "picBank3", "picBank3Url", null, null, imgbakcBank3, "resultUrl");
    }
}

function imgbakcBank3(resultUrl) {
    $(".picBank3show").css("background-image", "url(" + resultUrl + ")");
    $('label[for="picBank3"]').text('重新上传');
    /*$('.pic3name').text($('#pic3').val().split('\\').pop())
       .siblings().text('已选择');*/
}


