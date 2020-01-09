<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="bat.ke.qq.com">
    <link rel="Shortcut Icon" href="icon/x.png" />
    <title>YMSHOP后台管理系统 v1.0</title>
    <meta name="keywords" content="YMSHOP后台管理系统 v1.0,YMSHOP,YMSHOP购物商城后台管理系统">
    <meta name="description" content="YMSHOP后台管理系统 v1.0，是一款电商后台管理系统，适合中小型CMS后台系统。">

    <!-- Bootstrap core CSS -->
    <link href="lib/flatlab/css/bootstrap.min.css" rel="stylesheet">
    <link href="lib/flatlab/css/bootstrap-reset.css" rel="stylesheet">
    <!--external css-->
    <link href="lib/flatlab/assets/font-awesome/css/font-awesome.css" rel="stylesheet" />
    <link href="lib/flatlab/assets/jquery-easy-pie-chart/jquery.easy-pie-chart.css" rel="stylesheet" type="text/css" media="screen"/>
    <link rel="stylesheet" href="lib/flatlab/css/owl.carousel.css" type="text/css">
    <!-- Custom styles for this template -->
    <link href="lib/flatlab/css/style.css" rel="stylesheet">
    <link href="lib/flatlab/css/style-responsive.css" rel="stylesheet" />

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 tooltipss and media queries -->
    <!--[if lt IE 9]>
    <script src="lib/flatlab/js/html5shiv.js"></script>
    <script src="lib/flatlab/js/respond.min.js"></script>
    <![endif]-->
</head>
<style>
    #main-content {
        margin-left: 20px;
        margin-top: -50px;
    }
    .site-footer .text-center a{
        color: #53bee6;
    }
</style>
<body>
<section id="container" >
    <!--main content start-->
    <section id="main-content">
        <section class="wrapper">
            <!--state overview start-->
            <div class="row state-overview">
                <div class="col-lg-3 col-sm-6">
                    <section class="panel">
                        <div class="symbol terques">
                            <i class="icon-user"></i>
                        </div>
                        <div class="value">
                            <h1 class="count">
                                ...
                            </h1>
                            <p>用户总数</p>
                        </div>
                    </section>
                </div>
                <div class="col-lg-3 col-sm-6">
                    <section class="panel">
                        <div class="symbol red">
                            <i class="icon-tags"></i>
                        </div>
                        <div class="value">
                            <h1 class=" count2">
                                ...
                            </h1>
                            <p>商品总数</p>
                        </div>
                    </section>
                </div>
                <div class="col-lg-3 col-sm-6">
                    <section class="panel">
                        <div class="symbol yellow">
                            <i class="icon-shopping-cart"></i>
                        </div>
                        <div class="value">
                            <h1 class=" count3">
                                ...
                            </h1>
                            <p>订单总数</p>
                        </div>
                    </section>
                </div>
                <div class="col-lg-3 col-sm-6">
                    <section class="panel">
                        <div class="symbol blue">
                            <i class="icon-bar-chart"></i>
                        </div>
                        <div class="value">
                            <h1 class=" count4">
                                ...
                            </h1>
                            <p>浏览量</p>
                        </div>
                    </section>
                </div>
            </div>
            <!--state overview end-->

            <div class="row">
                <div class="col-lg-7">
                    <!--custom chart start-->
                    <div class="border-head">
                        <h3>系统统计</h3>
                    </div>
                    <div class="custom-bar-chart">
                        <ul class="y-axis">
                            <li><span>100</span></li>
                            <li><span>80</span></li>
                            <li><span>60</span></li>
                            <li><span>40</span></li>
                            <li><span>20</span></li>
                            <li><span>0</span></li>
                        </ul>
                        <div class="bar">
                            <div class="title">JAN</div>
                            <div class="value tooltips" data-original-title="80%" data-toggle="tooltip" data-placement="top">80%</div>
                        </div>
                        <div class="bar ">
                            <div class="title">FEB</div>
                            <div class="value tooltips" data-original-title="50%" data-toggle="tooltip" data-placement="top">50%</div>
                        </div>
                        <div class="bar ">
                            <div class="title">MAR</div>
                            <div class="value tooltips" data-original-title="40%" data-toggle="tooltip" data-placement="top">40%</div>
                        </div>
                        <div class="bar ">
                            <div class="title">APR</div>
                            <div class="value tooltips" data-original-title="55%" data-toggle="tooltip" data-placement="top">55%</div>
                        </div>
                        <div class="bar">
                            <div class="title">MAY</div>
                            <div class="value tooltips" data-original-title="20%" data-toggle="tooltip" data-placement="top">20%</div>
                        </div>
                        <div class="bar ">
                            <div class="title">JUN</div>
                            <div class="value tooltips" data-original-title="39%" data-toggle="tooltip" data-placement="top">39%</div>
                        </div>
                        <div class="bar">
                            <div class="title">JUL</div>
                            <div class="value tooltips" data-original-title="75%" data-toggle="tooltip" data-placement="top">75%</div>
                        </div>
                        <div class="bar ">
                            <div class="title">AUG</div>
                            <div class="value tooltips" data-original-title="45%" data-toggle="tooltip" data-placement="top">45%</div>
                        </div>
                        <div class="bar ">
                            <div class="title">SEP</div>
                            <div class="value tooltips" data-original-title="50%" data-toggle="tooltip" data-placement="top">50%</div>
                        </div>
                        <div class="bar ">
                            <div class="title">OCT</div>
                            <div class="value tooltips" data-original-title="42%" data-toggle="tooltip" data-placement="top">42%</div>
                        </div>
                        <div class="bar ">
                            <div class="title">NOV</div>
                            <div class="value tooltips" data-original-title="60%" data-toggle="tooltip" data-placement="top">60%</div>
                        </div>
                        <div class="bar ">
                            <div class="title">DEC</div>
                            <div class="value tooltips" data-original-title="90%" data-toggle="tooltip" data-placement="top">90%</div>
                        </div>
                    </div>
                    <!--custom chart end-->
                </div>
                <div class="col-lg-4">
                    <!--latest product info start-->
                    <section class="panel post-wrap pro-box">
                        <aside>
                            <div class="post-info">
                                <span class="arrow-pro right"></span>
                                <div class="panel-body">
                                    <h1><strong>popular</strong> <br> 本周热门商品</h1>
                                    <div class="desk yellow">
                                        <h3 id="hot-title">商品名称</h3>
                                        <p>共卖出 <span id="hot-num"></span> 件</p>
                                    </div>
                                    <div class="post-btn">
                                        <a href="javascript:;">
                                            <i class="icon-chevron-sign-left"></i>
                                        </a>
                                        <a href="javascript:;">
                                            <i class="icon-chevron-sign-right"></i>
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </aside>
                        <aside class="post-highlight yellow v-align">
                            <div class="panel-body text-center">
                                <div class="pro-thumb">
                                    <img id="hot-img" width="112px" height="112px" src="icon/none.png" alt="">
                                </div>
                            </div>
                        </aside>
                    </section>
                </div>
            </div>
        </section>
    </section>
    <!--main content end-->
</section>

<!-- js placed at the end of the document so the pages load faster -->
<script src="lib/flatlab/js/jquery.js"></script>
<script src="lib/flatlab/js/jquery-1.8.3.min.js"></script>
<script src="lib/flatlab/js/bootstrap.min.js"></script>
<script src="lib/flatlab/js/jquery.scrollTo.min.js"></script>
<script src="lib/flatlab/js/jquery.nicescroll.js" type="text/javascript"></script>
<script src="lib/flatlab/js/jquery.sparkline.js" type="text/javascript"></script>
<script src="lib/flatlab/assets/jquery-easy-pie-chart/jquery.easy-pie-chart.js"></script>
<script src="lib/flatlab/js/owl.carousel.js" ></script>
<script src="lib/flatlab/js/jquery.customSelect.min.js" ></script>
<script src="lib/flatlab/js/respond.min.js" ></script>

<script class="include" type="text/javascript" src="lib/flatlab/js/jquery.dcjqaccordion.2.7.js"></script>

<!--common script for all pages-->
<script src="lib/flatlab/js/common-scripts.js"></script>

<script charset="utf-8" type="text/javascript" src="lib/changyan.js" ></script>
<script async src="lib/busuanzi.pure.mini.js"></script>
<script type="text/javascript" src="lib/layer/2.4/layer.js"></script>

<!--script for this page-->
<script src="lib/flatlab/js/sparkline-chart.js"></script>
<script src="lib/flatlab/js/easy-pie-chart.js"></script>
<script src="lib/flatlab/js/count.js"></script>

<span id="busuanzi_container_site_uv">
  <span style="display: none" id="busuanzi_value_site_uv"></span>
</span>

<script>

    $("#username").html(parent.username);

    var now = new Date(),hour = now.getHours();
    if(hour < 6){ $("#hello").html("凌晨好！");}
    else if (hour < 9){$("#hello").html("早上好！");}
    else if (hour < 12){$("#hello").html("上午好！");}
    else if (hour < 14){$("#hello").html("中午好！");}
    else if (hour < 17){$("#hello").html("下午好！");}
    else if (hour < 19){$("#hello").html("傍晚好！");}
    else if (hour < 22){$("#hello").html("晚上好！");}
    else {$("#hello").html("深夜好！");}

    $(function(){
        setInterval(function(){
            $("#currentTime").text(new Date().toLocaleString());
        },1000);
    });


    window.changyan.api.config({
        appid: 'cyrV7vlR4',
        conf: 'prod_3163726f95fdac5ad0531c2344fc86ea'
    });

    //owl carousel
    $(document).ready(function() {
        $("#owl-demo").owlCarousel({
            navigation : true,
            slideSpeed : 300,
            paginationSpeed : 400,
            singleItem : true,
            autoPlay:true

        });
    });

    //custom select box
    $(function(){
        $('select.styled').customSelect();
    });

    /*统计用户数*/
    $.ajax({
        url:"/member/count",
        type:"GET",
        success:function (data) {
            countUp(data.recordsTotal);
        },
        error:function(XMLHttpRequest){
            layer.alert('数据处理失败! 错误码:'+XMLHttpRequest.status+' 错误信息:'+JSON.parse(XMLHttpRequest.responseText).message,{title: '错误信息',icon: 2});
        }
    });

    $.ajax({
        url:"/item/count",
        type: 'GET',
        success:function (result) {
            countUp2(result.recordsTotal);
        },
        error:function(XMLHttpRequest){
            if(XMLHttpRequest.status!=200){
                layer.alert('数据处理失败! 错误码:'+XMLHttpRequest.status+' 错误信息:'+JSON.parse(XMLHttpRequest.responseText).message,{title: '错误信息',icon: 2});
            }
        }
    });

    $.ajax({
        url:"/order/count",
        type: 'GET',
        success:function (data) {
            countUp3(data.result);
        },
        error:function(XMLHttpRequest){
            if(XMLHttpRequest.status!=200){
                layer.alert('数据处理失败! 错误码:'+XMLHttpRequest.status+' 错误信息:'+JSON.parse(XMLHttpRequest.responseText).message,{title: '错误信息',icon: 2});
            }
        }
    });

    setTimeout('count()',2000);
    function count(){
        countUp4($("#busuanzi_value_site_uv").html());
    }
    //本周热门商品
    $.ajax({
        url:"/sys/weekHot",
        type: 'GET',
        success:function (data) {
            $("#hot-title").html(data.result.title);
            $("#hot-num").html(data.result.total);
            if(data.result.picPath!=""&&data.result.picPath!=null){
                $("#hot-img").attr("src", data.result.picPath);
            }
        },
        error:function(XMLHttpRequest){
            if(XMLHttpRequest.status!=200){
                layer.alert('数据处理失败! 错误码:'+XMLHttpRequest.status+' 错误信息:'+JSON.parse(XMLHttpRequest.responseText).message,{title: '错误信息',icon: 2});
            }
        }
    });

    if($("#hot-title").text().length > 18){
        $("#hot-title").text($("#hot-title").text().substring(0,18) +"...");

    }

    $.ajax({
        url:"/sys/base",
        type: 'GET',
        success:function (data) {
            if(data.success!=true){
                layer.alert(data.message,{title: '错误信息',icon: 2});
                return;
            }
            if(data.result.hasAllNotice==1){
                allNotice(data.result.allNotice);
            }
        },
        error:function(XMLHttpRequest){
            if(XMLHttpRequest.status!=200){
                layer.alert('数据处理失败! 错误码:'+XMLHttpRequest.status+' 错误信息:'+JSON.parse(XMLHttpRequest.responseText).message,{title: '错误信息',icon: 2});
            }
        }
    });

    function allNotice(data){
        layer.open({
            type: 1
            ,title:'通知'
            //,area: ['350px', '230px']
            ,content: '<div style="margin: 10px 20px 10px 20px;">'+data+'</div>'
            ,btn: ['知道了']
            ,shade: 0 //不显示遮罩
            ,yes: function(){
                layer.closeAll();
            }
        });
    }

</script>

</body>
</html>
