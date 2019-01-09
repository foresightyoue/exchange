(function() {
    if (window.jQuery === undefined)
        throw new Error("alert插件依赖于jQuery");
    /**
     * title -->弹窗标题 content -->弹窗内容 callback_cancel -->弹窗取消按钮的回调函数
     * callback_confirm -->弹窗确定按钮的回调函数 flag -->是否显示‘取消’按钮 true为显示，false为不显示 用法：
     * Alert("退出系统","您真的要退出系统？如果您遇到任何的系统操作问题，可以<a href='#'>联系客服</a>",null,function(){
     * alert(1); },true);
     */
    var theme = "#a0791a";
    $("head").append('<style>.alert-shadow{position:fixed;top:0;left:0;width:100%;height:100%;background:rgba(0, 0, 0, 0.4);z-index: 999;}.alert-box{position:absolute;top:50%;left:7.5%;width:21.5em;min-height:160px;background:#fff;border-radius:8px;-webkit-border-radius:8px;transform:translateY(-50%);-webkit-transform:translateY(-50%);}.alert-box .alert-title{width:100%;height:74px;line-height:74px;text-align:center;font-size:21px;color:#333}.alert-box .alert-con{width:100%;line-height:24px;text-align:center;font-size:17px;color:#666;padding:0 15px;box-sizing:border-box;-webkit-box-sizing:border-box;margin-bottom: 15px;}.alert-box .alert-con a{color:'+theme+';text-decoration:underline}.alert-box ul{width:100%;height:46px;border-top:1px solid #e6e6e6}.alert-box ul li{list-style:none;width:50%;height:45px;line-height:45px;text-align:center;float:left;color:'+theme+';font-size:19px;cursor:pointer;}</style>');
    
    alert = function(message) {
        Alert("提示信息", message, null, function() {
            return false;
        }, false);
    }
    
    Alert = function(title, message, callback_cancel, callback_confirm, flag) {
        $("body").append('<div class="alert-shadow" id="alert"></div>');
        var parent = $(".alert-shadow");
        var cancelBtn = '<li class="alert-cancel">取消</li>';
        if (flag) {
            $(".alert-box ul li").css("width", "50%");
            $(".alert-box ul li:first-child").css("border-right",
                    "1px solid #e6e6e6");
            cancelBtn = '<li class="alert-cancel">取消</li>';
        } else {
            $(".alert-box ul li").css("width", "100%");
            cancelBtn = '';
        }
        var htmlStr = '<div class="alert-box">' + '<div>'
                + '<h1 class="alert-title">' + title + '</h1>'
                + '<p class="alert-con">' + message + '</p>' + '</div>'
                + '<ul>' + cancelBtn + '<li class="alert-confirm">确认</li>'
                + '</ul>' + '</div>';
        parent.append(htmlStr);
        $(".alert-box").css("left",
                ($(window).width() - $(".alert-box").width()) / 2);
        $("body").css('overflow', 'hidden');
        parent.css('display', 'block');
        $(".alert-cancel").on("click", function() {
            if (callback_cancel)
                callback_cancel();
            parent.remove();
            $("body").css('overflow', 'auto');
            return;
        });
        $(".alert-confirm").on("click", function() {
            if (callback_confirm)
                callback_confirm();
            parent.remove();
            $("body").css('overflow', 'auto');
            return;
        });

        if (flag) {
            $(".alert-box ul li").css("width", "50%");
            $(".alert-box ul li:first-child").css("border-right",
                    "1px solid #e6e6e6");
        } else {
            $(".alert-box ul li").css("width", "100%");
        }
    }
})();