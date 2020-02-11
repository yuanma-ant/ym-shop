    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    request.setCharacterEncoding("UTF-8");
    String htmlData = request.getParameter("detail") != null ? request.getParameter("detail") : "";
%>
<!--_meta 作为公共模版分离出去-->
<!DOCTYPE HTML>
<html>
<head>
    <meta charset="utf-8">
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
    <meta http-equiv="Cache-Control" content="no-siteapp" />
    <link rel="Bookmark" href="/icon/favicon.ico" >
    <link rel="Shortcut Icon" href="/icon/favicon.ico" />
    <!--[if lt IE 9]>
    <script type="text/javascript" src="lib/html5shiv.js"></script>
    <script type="text/javascript" src="lib/respond.min.js"></script>

    <![endif]-->
    <link rel="stylesheet" type="text/css" href="static/h-ui/css/H-ui.min.css" />
    <link rel="stylesheet" type="text/css" href="static/h-ui.admin/css/H-ui.admin.css" />
    <link rel="stylesheet" type="text/css" href="lib/Hui-iconfont/1.0.8/iconfont.css" />

    <link rel="stylesheet" type="text/css" href="static/h-ui.admin/skin/default/skin.css" id="skin" />
    <link rel="stylesheet" type="text/css" href="static/h-ui.admin/css/style.css" />
    <!--[if IE 6]>
    <script type="text/javascript" src="lib/DD_belatedPNG_0.0.8a-min.js" ></script>
    <script>DD_belatedPNG.fix('*');</script>
    <![endif]-->
    <!--/meta 作为公共模版分离出去-->

    <link href="lib/webuploader/0.1.5/webuploader.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div class="page-container">
    <%=htmlData%>
    <form name="seckill-add" action="" method="post" class="form form-horizontal" id="seckill-add">
        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>活动描述：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="" placeholder="" name="desc" id="desc">
            </div>
        </div>
        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>关联商品：</label>
            <div class="formControls col-xs-8 col-sm-6">
                <input type="text" onclick="chooseProduct()" readonly class="input-text" placeholder="请点击选择按钮选择关联商品" id="title" name="title" style="width: 65%">
                <input type="button" onclick="chooseProduct()" class="btn btn-secondary radius" value="选择关联商品">
            </div>
        </div>
        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>商品ID：</label>
            <div class="formControls col-xs-8 col-sm-6">
                <input type="text" onclick="chooseProduct()" readonly class="input-text"  id="productId" name="productId" style="width: 65%" readonly="readonly">
            </div>
        </div>
        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>秒杀价格：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" id="price" name="price" class="input-text" style="width:50%">
            元</div>
        </div>
        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">秒杀库存：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" name="num" id="num" placeholder="0~99999" value="" class="input-text" style="width:50%">
                件</div>
        </div>
        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">活动开始时间：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" onfocus="WdatePicker({ dateFmt:'yyyy-MM-dd HH:mm:ss', maxDate:'#F{$dp.$D(\'endDate\')||\'%y-%M-%d\'}' })" id="startDate" name="startDate" class="input-text Wdate" style="width:220px;">
                </div>
        </div>
        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">活动结束时间：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', minDate:'#F{$dp.$D(\'startDate\')}'})" id="endDate" name="endDate" class="input-text Wdate" style="width:220px;">
                </div>
        </div>
        <div class="row cl">
            <div class="col-xs-8 col-sm-9 col-xs-offset-4 col-sm-offset-2">
                <button id="saveButton" class="btn btn-primary radius" type="submit"><i class="Hui-iconfont">&#xe632;</i> 保存并发布</button>
                <button onClick="layer_close();" class="btn btn-default radius" type="button">&nbsp;&nbsp;取消&nbsp;&nbsp;</button>
            </div>
        </div>
    </form>
</div>

<!--_footer 作为公共模版分离出去-->
<script type="text/javascript" src="lib/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="lib/layer/2.4/layer.js"></script>
<script type="text/javascript" src="static/h-ui/js/H-ui.min.js"></script>
<script type="text/javascript" src="static/h-ui.admin/js/H-ui.admin.js"></script> <!--/_footer 作为公共模版分离出去-->

<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript" src="lib/My97DatePicker/4.8/WdatePicker.js"></script>
<script type="text/javascript" src="lib/jquery.validation/1.14.0/jquery.validate.js"></script>
<script type="text/javascript" src="lib/jquery.validation/1.14.0/validate-methods.js"></script>
<script type="text/javascript" src="lib/jquery.validation/1.14.0/messages_zh.js"></script>
<script type="text/javascript" src="lib/webuploader/0.1.5/webuploader.min.js"></script>
<script type="text/javascript" src="lib/My97DatePicker/4.8/WdatePicker.js"></script>
<link rel="stylesheet" href="lib/kindeditor/themes/default/default.css" />
<link rel="stylesheet" href="lib/kindeditor/plugins/code/prettify.css" />
<script charset="utf-8" src="lib/kindeditor/kindeditor.js"></script>
<script charset="utf-8" src="lib/kindeditor/lang/zh-CN.js"></script>
<script charset="utf-8" src="lib/kindeditor/plugins/code/prettify.js"></script>
<script type="text/javascript">

    function chooseProduct(){
        layer_show("选择展示商品","choose-product",900,600);
    }
    function setIdAndTitle(id,title){
        $("#productId").val(id);
        $("#title").val(title);
    }

    jQuery.validator.addMethod("decimalsValue",function(value, element) {
        var decimalsValue =/^(?!0+(?:\.0+)?$)(?:[1-9]\d*|0)(?:\.\d{1,2})?$/ ;
        return this.optional(element) || (decimalsValue.test(value));
    }, "金额必须大于0并且只能精确到分");

    function giveCid(cid){
        $("#cid").val(cid);
    }

    function giveCName(cname){
        $("#cname").val(cname);
    }

    function chooseCategory(){
        layer_show("选择商品分类","choose-category",300,510);
    }

    //保存发布
    $("#seckill-add").validate({
        rules:{
            desc:{
                required:true,
            },
            productId:{
                required:true,
            },
            price:{
                decimalsValue:true,
                required:true,
            },
            num :{
                required:true,
            },
            startDate:{
                required:true,
            },
            endDate:{
                required:true,
            },
        },
        onkeyup:false,
        focusCleanup:false,
        success:"valid",
        submitHandler:function(form){
            var index = layer.load(3);
            $(form).ajaxSubmit({
                url: "/seckill/add",
                type: "POST",
                success: function(data) {
                    layer.close(index);
                    if(data.success==true){
                        if(parent.location.pathname!='/'){
                            // parent.productCount();
                            parent.refresh();
                            parent.msgSuccess("添加成功!");
                            var index = parent.layer.getFrameIndex(window.name);
                            parent.layer.close(index);
                        }else{
                            layer.confirm('添加成功!', {
                                btn: ['确认'],icon: 1
                            }, function(){
                                var index = parent.layer.getFrameIndex(window.name);
                                parent.layer.close(index);
                            });
                        }
                    }else{
                        layer.alert(data.message, {title: '错误信息',icon: 2});
                    }
                },
                error:function(XMLHttpRequest) {
                    layer.close(index);
                    layer.alert('数据处理失败! 错误码:'+XMLHttpRequest.status,{title: '错误信息',icon: 2});
                }
            });
        }
    });
</script>
</body>
</html>
<%!
    private String htmlspecialchars(String str) {
        str = str.replaceAll("&", "&amp;");
        str = str.replaceAll("<", "&lt;");
        str = str.replaceAll(">", "&gt;");
        str = str.replaceAll("\"", "&quot;");
        return str;
    }
%>
