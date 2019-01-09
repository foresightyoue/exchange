<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>搜索</title>
    <style>
        * {
      margin: 0;
      padding: 0;
    }

    body {
      background: #efefef;
    }
    .searchBox{
      background: #333333;
    color: #fff;
    display: flex;
    justify-content: center;
    align-items: center;
    height: 3rem;
    }
    .searchBox>div{
      flex: 0 0 80%;
      border: 1px solid rgba(255,255,255,0.6);
      border-radius: 0.3rem;
      display: flex;
      align-items: center;
      height: 1.9rem;
    }
    .searchBox>div>img{
      width:1rem;
      height:1rem;
      margin: 0 0.3rem;
    }
    .searchBox>div>span{
      flex: 0 0 15%;
      text-align: center;
    }
    .searchBox>div>input{
      border: none;
    outline: none;
    background: transparent;
    color: #fff;
    flex: 1;
    }
    .searchBox>span{
      align-self: stretch;
    line-height: 2.9rem;
    flex: 0 0 15%;
    text-align: center;
    }
    .searchRst{
      background:#fff;
    }
    .searchRst .rstTitle{
      height:3rem;
      display:flex;
      padding:0 1rem;
      justify-content: space-between;
      align-items:center;
      border-bottom:1px solid #efefef;
    }
    .rstItem{
        height:2.7rem;
        line-height:2.7rem;
        border-bottom:1px solid #efefef;
        color:#666;
        text-indent:1rem;
    }
    [v-cloak]{
        display: none;
    }
  </style>
    <script type="text/javascript" src="${oss_url}/static/front/app/js/jquery-3.2.1.min.js"></script>
    <script type="text/javascript" src="${oss_url}/static/front/app/js/vue.js"></script>
    <script type="text/javascript" src="${oss_url}/static/front/js/plugin/layer/layer.js"></script>
</head>

<body>
    <div id="body">
        <div class='searchBox'>
            <div>
                <img src="/static/front/app/images/main/main/souyisou.png" alt="LOL">
                <input type="text" id="seach" placeholder='请输入关键字'>
                <span v-on:click="search">搜索</span>
            </div>
            <span class='cancel' onclick="javascript:history.go(-1);">取消</span>
        </div>
        <div class='searchRst'>
            <%--<div class='rstTitle'><span>搜索结果:</span><span v-if="list.length == 0" v-on:click="clearhistory()">清除历史</span></div>--%>
            <div v-for="(item,i) in list" v-on:click="to(i) " v-cloak class='rstItem'>{{item.ftradedesc}} </div>
            <%--<div v-if="list.length != 0" class='rstTitle'><span>历史记录:</span><span v-if="list.length != 0" v-on:click="clearhistory()">清除历史</span></div>--%>
            <%--<div v-for="(item,i) in history" v-on:click="selhistory(item)" class='rstItem'>{{item}} </div>--%>
        </div>
    </div>
</body>
<script type="text/javascript">
    $(function () {
        var history = new Vue({
            el: "#body",
            data: {
                history: objToArray(localStorage.getItem('searchs')),
                list: []
            },
            methods: {
                to: function (index) {
                    window.location.href = '${oss_url}/m/trade/coin.html?tradeType=0&coinType=' +
                        this.list[index].fId;
                },
                selhistory: function (value) {
                    $("#seach").val(value);
                    this.search();
                },
                search: function () {
                    var cat = objToArray(localStorage.getItem('searchs'));
                    var ind = localStorage.getItem('in');
                    if (cat.length == 0) {
                        if ($("#seach").val().replace(/^\s+|\s+$/gm, '').length != 0) {
                            cat[0] = $("#seach").val();
                            localStorage.setItem('searchs', cat);
                            localStorage.setItem('in', 0);
                        }
                    } else {
                        var flag = true;
                        for (var i = 0; i < cat.length; i++) {
                            if (cat[i] == $("#seach").val()) {
                                flag = false;
                                break;
                            }
                        }
                        if ($("#seach").val().replace(/^\s+|\s+$/gm, '').length == 0) {
                            flag = false;
                        }
                        if (flag) {
                            ind = parseInt(ind) + 1;
                            cat[ind] = $("#seach").val();
                        }
                        localStorage.setItem('searchs', cat);
                        localStorage.setItem('in', ind);
                    }
                    history.history = cat;
                    console.log(cat, 8888888);
                    $.post("/m/ryb/search.html", {
                        "key": $("#seach").val().replace(/^\s+|\s+$/gm, '')
                    }, function (obj) {
                        console.log(obj);
                        if (obj.status == 200) {
                            history.list = obj.list;
                        } else {
                            layer.msg(obj.msg)
                        }
                    });
                },
                clearhistory: function () {
                    localStorage.removeItem('searchs');
                    localStorage.removeItem('in');
                    this.history = [];
                }
            },
            watch: {
                date: function () {
                    this.to();
                }
            }
        });
    })
    /*  function serch(){
         var cat = objToArray(localStorage.getItem('searchs'));
         var ind = localStorage.getItem('in');
         if(cat.length == 0){
             if($("#seach").val().replace(/^\s+|\s+$/gm,'').length != 0){
                 cat[0] = $("#seach").val();
                 localStorage.setItem('searchs', cat);
                 localStorage.setItem('in', 0);
             }
         }else{
             var flag = true;
             for(var i = 0;i<cat.length;i++){
                 if(cat[i] == $("#seach").val()){
                     flag = false;
                     break;
                 }
             }
             if($("#seach").val().replace(/^\s+|\s+$/gm,'').length == 0){
                 flag = false;
             }
             if(flag){
                 ind = parseInt(ind) +1;
                 cat[ind] = $("#seach").val();
             }
             localStorage.setItem('searchs', cat);
             localStorage.setItem('in', ind);
         }
         history.history = cat ;
         console.log(cat);
         $.post("/m/ryb/search.html",{"key":$("#seach").val().replace(/^\s+|\s+$/gm,'')},function(obj){
         	console.log(obj);
         	history.list=obj.list;d
         });
     } */

    function objToArray(array) {
        var arr = []
        if (array == null) {
            for (var i in array) {
                arr.push(array[i]);
            }
        } else {
            arr = array.split(',');
        }
        return arr;
    }

    /*  function clearhistory(){
         localStorage.removeItem('searchs');
         localStorage.removeItem('in');
         location.reload();
     } */
</script>

</html>