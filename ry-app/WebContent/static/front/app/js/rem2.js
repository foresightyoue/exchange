(function (win, doc) {
            win.isMobile = ! ! /Android|webOS|iPhone|iPod|BlackBerry|IEMobile|Opera Mini|MicroMessenger/i.test(navigator.userAgent);
            win.isIOS = ! ! /iPhone|iPad|iPod/i.test(navigator.userAgent);
            win.isIOS7 = ! ! /(iPhone|iPad|iPod) OS 7/i.test(navigator.userAgent);
            win.isAndroid = ! ! /Android/i.test(navigator.userAgent);
            win.isWechat = ! ! /MicroMessenger/i.test(navigator.userAgent);
            win.isIE = ! ! /Trident|Edge/i.test(navigator.userAgent);

            var baseWidth = 640;
            var documentHTML = doc.documentElement;

            function setRootFont () {
                var docWidth = documentHTML.getBoundingClientRect().width;
                var scale = docWidth / baseWidth;
                documentHTML.style.fontSize = scale * 12.8 + 'px';
                $(function(){
                	$("body").show();
                })
            }

            setRootFont();
            win.addEventListener('resize', setRootFont, false);
        })(window, document);