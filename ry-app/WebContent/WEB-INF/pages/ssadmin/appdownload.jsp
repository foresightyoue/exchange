<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">上传App管理</h2>


<div class="pageContent">

    <div style="color: #ff3844">&nbsp;&nbsp;&nbsp;&nbsp;   上传版本是请一定要上传具体的app版本,以及版本更新说明和版本号</div>
    <br>
    <form method="post" action="/ssadmin/uptversion.html" enctype="multipart/form-data" class="pageForm required-validate" >
        <div class="pageFormContent nowrap" layoutH="97">
            <dl>
                <dt>设备:</dt>
                <dd>
                    <input type="radio" value="0" name="type" checked class="required" size="50" />安卓
                    <input type="radio" value="1" name="type" class="required" size="50" />IOS
                </dd>
            </dl>
            <dl>
                <dt>版本号:</dt>
                <dd>
                    <input type="text" id="version" name="version" maxlength="20" class="required" size="50" />
                </dd>
            </dl>
            <dl>
                <dt>上传文件:</dt>
                <dd>
                    <!-- <input type="file" id="pic4" onchange="uploadImg4()"> -->
                    <input type="file" name="appName" id="pic4" >
                    <!-- <input type="hidden" id="pic4Url"> -->
                </dd>
            </dl>
            <dl>
                <dt>更新说明:</dt>
                <dd>
                    <textarea type="text" id="context" name="context" cols="52" rows="5" class="required" style="resize:none;"></textarea>
                </dd>
            </dl>
            <dl>
                <dt>是否强制更新:</dt>
                <dd>
                    <input type="radio" value="0" name="UpdateStatus" class="required" size="50" />用户更新
                    <input type="radio" value="1" name="UpdateStatus" class="required" size="50" />强制更新
                </dd>
            </dl>
            <dl>
                <dd>
                    <!-- <button id="submit" onclick="onsub()">提交</button> -->
                    <button id="submit" >提交</button>
                </dd>
            </dl>
        </div>
    </form>
</div>

<script type="text/javascript" src="${oss_url}/static/front/js/plugin/ajaxfileupload.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/plugin/fileCheck.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/comm/msg.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/finance/city.min.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/finance/jquery.cityselect.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/plugin/jquery.qrcode.min.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/plugin/jquery.autocomplete.min.js"></script>
<script type="text/javascript" src="${oss_url}/static/front/js/user/kyc.js"></script>
<script type="text/javascript">
    $(function () {
        $.post("/m/app/api/version.html", {
                "type": $("input[name='phone']:checked").val(),
            },
            function (obj) {
                var data = obj.data;
                if (obj.status == 200) {
                    $("#version").val(data.VersionName);
                    $("#context").val(data.ModifyContent);
                    $("input[name='UpdateStatus']:[value=" + data.UpdateStatus + "]").attr('checked', 'true');
                }else{
                    alert(obj.msg);
                }
            });
    })

    function subfrom(form) {
        var $form = $(form);
        console.log($form.serializeArray() ,'sssssssssssssss');
        var dataParam = $form.serializeArray();
        fiils = $('#pic4')[0].files[0];
        console.log(fiils,'fiilsfiilsfiilsfiilsfiils');

        dataParam.push({"name":"uri","value":fiils})

        console.log(dataParam,'dataParamdataParamdataParamdataParam');

        $.ajax({
            type: form.method || 'POST',
            url:$form.attr("action"),
            data:dataParam,
            dataType:"json",
            cache: false,
            success:function(){
                alert('success')
            },error: function () {
                alert('error');

            }
        });



        return false
    }

    function onsub() {
        var type = $("input[name='phone']:checked").val();
        var version =  $("#version").val();
        var uri = $("#pic4Url").val();
        var context = $("#context").val();
        console.log(type);
        console.log(version);
        console.log(uri);
        console.log(context);
        $.post("ssadmin/uptversion.html", {
                "type": $("input[name='phone']:checked").val(),
                "version": $("#version").val(),
                "uri": $("#pic4Url").val(),
                "context": $("#context").val(),
                "UpdateStatus": $("input[name='UpdateStatus']:checked").val(),
            },
            function (obj) {
                console.log(JSON.parse(obj));
                alert(JSON.parse(obj).msg);
            });
    }
    $('#version').on('input propertychange',function (e){
        console.log(this.value,'ssssssssssssssssss');

    })
</script>