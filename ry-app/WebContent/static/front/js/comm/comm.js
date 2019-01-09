String.prototype.format = function(args) {
	var result = this;
	if (arguments.length > 0) {
		for ( var i = 0; i < arguments.length; i++) {
			if (arguments[i] != undefined) {
				var reg = new RegExp("({[" + i + "]})", "g");
				result = result.replace(reg, arguments[i]);
			}
		}
	}
	return result;
};

function centerModals() {
	$('.modal').each(function(i) {
		/*var $clone = $(this).clone().css('display', 'block').appendTo('body');
		var modalHeight = $clone.find('.modal-content').height();
		var width = $clone.find('.modal-content').width();
		var top = Math.round(($clone.height() - modalHeight) / 2);
		top = top > 0 ? top : 0;
		$clone.remove();
		$(this).find('.modal-content').css("margin-top", top);
		$(this).find('.modal-mark').css("height", 0).css("width", 0);*/
	});
}

//原生调用h5返回方法
function ReturnBtnClick(){
	window.location.href="javascript:history.back(-1)";
}


function ClientProxy() { // 安卓代理，对应android源码中的JavaScriptProxy
    this.active = false; // 标识为true时，表示当前为summer-android专用浏览器
    this.machine = null; // 当前的JavaScriptProxy对象
    this.req = {}; // 请求参数
    this.resp = {}; // 返回参数
    this.message = null; // 返回的消息
    this.device = "browser";

    //android 浏览器
    if (window.JSobj) { // 兼容老的JSobj
        this.machine = window.JSobj;
        this.device="android";
        this.active = true;
    }else if (window.jsAndroid) { // 原JSobj改名为jsAndroid
        this.machine = window.jsAndroid;
        this.device="android";
        this.active = true;
    }
    // iphone 浏览器
    if(!this.active){
        var ua = navigator.userAgent.toLowerCase();
        if (/iphone|ipad|ipod/.test(ua)){
            this.device="iphone";
            this.active = true;
        }
    }
    //c#浏览器
    if(!this.active){
        if(window.external){ 
            this.machine = window.external;
            try{
                window.external.send("GetVersionName", "{}");
                this.device="window";
                this.active = true;
            } catch (e) {
                this.active = false;
            }
        }
    }
    
    // 判断是否支持指定的功能函数
    this.support = function(classCode) { 
        if (!this.active) {
            this.message = "您的版本太旧，不支持指令：" + classCode;
            return false;
        }
        try {
            var dataOut = this.machine.support(classCode);
            this.resp = JSON.parse(dataOut);
            if (!this.resp.result && this.resp.message)
                this.message = this.resp.message;
            return this.resp.result;
        } catch (e) {
            this.message = "您的版本太旧，不支持指令：support";
            return false;
        }
    }

    //列出所有可用的服务
    this.list = function(){
        if (!this.active)
            return null;
        var result = this.machine.list();
        if(result)
            return JSON.parse(result);
        else
            return null;
    }
    
    //请求具体的服务，并立即返回
    this.send = function(classCode) {
        if (!this.active)
            return null;
        try {
            var dataOut = {};
            if(this.device == "iphone"){
                this.req.classCode = classCode;
                window.webkit.messageHandlers.nativeMethod.postMessage(this.req);
                this.resp = {
                        "result" : true,
                        "message" : "finish"
                    };
            }else{
                dataOut = this.machine.send(classCode, JSON.stringify(this.req));
                this.resp = JSON.parse(dataOut);
            }
            
            return this.resp.result;
        } catch (e) {
            this.resp = {
                "result" : false,
                "message" : e.message
            };
            return this.resp.result;
        }
    }
    
    this.sync = function(classCode, resultFunction, resultParams){
        this.req.resultFunction = resultFunction;
        this.req.resultParams = resultParams;
        return send();
    }

    this.getData = function() { // 取得执行后结果数据
        if (this.resp.result)
            return this.resp.data;
        else
            return null;
    }

    this.getMessage = function() { // 在执行出错时，取得出错原因
        return this.resp.message;
    }
}

$('.modal').on('show.bs.modal', centerModals);
$(window).on('resize', centerModals);

util.lrFixFooter("#allFooter");

$(function() {
    var height = $(document).outerHeight(true)-$(".header").outerHeight(true)-$(".footer").outerHeight(true) - 350;
    $(".main-con").css("min-height",height);
    
	var speed = 8000;
	var count = 0;
	var newstoplist = jQuery("#newsList p");
	var sumCount = jQuery("#newsList p").length;
	function Marquee() {
		jQuery("#newsList p").hide();
		if(count>sumCount){
			count=0;
		}
		jQuery("#newsList p:eq("+count+")").fadeToggle(2000);
		++count;
	}
	Marquee();
	var MyMar = setInterval(Marquee, speed);
	newstoplist.onmouseover = function() {
		clearInterval(MyMar);
	};
	newstoplist.onmouseout = function() {
		MyMar = setInterval(Marquee, speed);
	};
	

		$(document)
			.ready(
					function() {
						$(".side ul li").hover(function() {
							$(this).find(".sidebox").stop().animate({
								"width" : "200px"
							}, 200).css({
								"opacity" : "1",
								"filter" : "Alpha(opacity=100)",
								"background" : "#e74e19"
							})
						}, function() {
							$(this).find(".sidebox").stop().animate({
								"width" : "84px"
							}, 200).css({
								"opacity" : "0.8",
								"filter" : "Alpha(opacity=80)",
								"background" : "#5e5e5e"
							})
						});
					});
	function goTop() {
		$('html,body').animate({
			'scrollTop' : 0
		}, 500)
	}
});

$(function(){
	$(".leftmenu-folding").on("click",function(){
		var that=$(this);
		$("."+that.data().folding).slideToggle("fast"); 
	});
});

/** *******QQ登录****************************** */	
function openqq(url){
	if(url==null||url==""){
		url=window.location.href;
	}
	window.open('/qqLogin?url='+url,'new','height='+550+',,innerHeight='+550+',width='+800+',innerWidth='+800+',top='+200+',left='+200+',toolbar=no,menubar=no,scrollbars=auto,resizeable=no,location=no,status=no');
}

function showTips(text){
	util.showerrortips("", text, {
		okbtn : function() {
			$('#alertTips').modal('hide');
		}
	});
}

$(function(){
	if(navigator.userAgent.toLowerCase().indexOf("chrome") != -1){
	    var inputers = document.getElementsByTagName("input");  
	    for(var i=0;i<inputers.length;i++){  
	        if((inputers[i].type !== "submit") && (inputers[i].type !== "password")){  
	            inputers[i].disabled= true;  
	        }  
	    }  
	    setTimeout(function(){  
	        for(var i=0;i<inputers.length;i++){  
	            if(inputers[i].type !== "submit"){  
	                inputers[i].disabled= false;  
	            }  
	        }  
	    },100);
	}
	});

/*
 * jQuery placeholder, fix for IE6,7,8,9
 * @author JENA
 * @since 20131115.1504
 * @website ishere.cn
 */
var JPlaceHolder = {
    //检测
    _check : function(){
        return 'placeholder' in document.createElement('input');
    },
    //初始化
    init : function(){
        if(!this._check()){
            this.fix();
        }
    },
    //修复
    fix : function(){
        jQuery(':input[placeholder]').each(function(index, element) {
            var self = $(this), txt = self.attr('placeholder');
            self.wrap($('<div></div>').css({position:'relative', zoom:'1', border:'none', background:'none', padding:'none', margin:'none'}));
            var pos = self.position(), h = self.outerHeight(true), paddingleft = self.css('padding-left');
            var holder = $('<span></span>').text(txt).css({position:'absolute', left:pos.left, top:'8px', height:h, lienHeight:h, paddingLeft:paddingleft, color:'#aaa'}).appendTo(self.parent());
            self.focusin(function(e) {
                holder.hide();
            }).focusout(function(e) {
                if(!self.val()){
                    holder.show();
                }
            });
            holder.click(function(e) {
                holder.hide();
                self.focus();
            });
        });
    }
};
//执行
$(function(){
    JPlaceHolder.init();   
    
    $('.lang,.lanague').hover(
        function() {
            $('.lang').addClass('selected');
            $('.lanague').addClass('selected');
            $('.header').css('z-index', -120);
        },
        function() {
            $('.header').css('z-index', 19);
            $('.lanague').removeClass('selected');
            $('.lang').removeClass('selected');
        }
    );
});