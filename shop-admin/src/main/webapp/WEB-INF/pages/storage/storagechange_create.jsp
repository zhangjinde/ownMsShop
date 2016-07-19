<%@ page language="java" import="java.util.*" contentType="text/html; utf-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="utf-8"/>
    <title>Tables - Masiis</title>

    <meta name="description" content="Static &amp; Dynamic Tables"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>

    <!-- bootstrap & fontawesome -->
    <link rel="stylesheet" href="<%=basePath%>static/ace2/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="<%=basePath%>static/ace2/css/font-awesome.min.css"/>

    <!-- page specific plugin styles -->
    <link rel="stylesheet" href="<%=basePath%>static/ace2/css/dropzone.css"/>
    <link rel="stylesheet" href="<%=basePath%>static/ace2/css/uncompressed/my-dropzone.css"/>


    <!-- text fonts -->
    <link rel="stylesheet" href="<%=basePath%>static/ace2/css/ace-fonts.css"/>

    <!-- ace styles -->
    <%--<link rel="stylesheet" href="<%=basePath%>static/ace2/css/uncompressed/ace.css" id="main-ace-style" />--%>

    <!--[if lte IE 9]>
    <link rel="stylesheet" href="<%=basePath%>static/ace2/css/ace-part2.min.css"/>
    <![endif]-->
    <link rel="stylesheet" href="<%=basePath%>static/ace2/css/ace-skins.min.css"/>
    <link rel="stylesheet" href="<%=basePath%>static/ace2/css/ace-rtl.min.css"/>
    <link rel="stylesheet" href="<%=basePath%>static/ace2/css/jquery.gritter.css"/>

    <!--[if lte IE 9]>
    <link rel="stylesheet" href="<%=basePath%>static/ace2/css/ace-ie.min.css"/>
    <![endif]-->

    <script type="text/javascript" charset="utf-8" src="<%=basePath%>static/js/jquery-2.2.0.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="<%=basePath%>static/class/ueditor/ueditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="<%=basePath%>static/class/ueditor/ueditor.all.js"></script>
    <script type="text/javascript" charset="utf-8"
            src="<%=basePath%>static/class/upload-plugin/core/zyFile.js"></script>
    <script type="text/javascript" charset="utf-8"
            src="<%=basePath%>static/class/upload-plugin/control/js/zyUpload.js"></script>
    <script type="text/javascript" charset="utf-8" src="<%=basePath%>static/class/upload-plugin/core/jq22.js"></script>
    <!--建议手动加在语言，避免在ie下有时因为加载语言失败导致编辑器加载失败-->
    <!--这里加载的语言文件会覆盖你在配置项目里添加的语言类型，比如你在配置项目里配置的是英文，这里加载的中文，那最后就是中文-->
    <script type="text/javascript" charset="utf-8" src="<%=basePath%>static/class/ueditor/lang/zh-cn/zh-cn.js"></script>

    <!-- ace settings handler -->
    <script src="<%=basePath%>static/ace2/js/uncompressed/ace-extra.js"></script>


    <script type="text/javascript" charset="utf-8"
            src="<%=basePath%>static/class/bootstrap-validator/js/bootstrapValidator.js"></script>

    <!--[if lte IE 8]>
    <script src="<%=basePath%>static/ace2/js/html5shiv.min.js"></script>
    <script src="<%=basePath%>static/ace2/js/respond.min.js"></script>
    <![endif]-->
    <style>
        .list-group-item:hover{
            background: #ccc;
        }
        .list-group-item:active{
            background:#428bca;
        }
        .li_selected{
            background:#428bca;
        }
    </style>
</head>

<body class="no-skin" style="overflow-x: hidden;overflow-y: auto">

<!-- /section:basics/navbar.layout -->
<div class="main-container" id="main-container">
    <script type="text/javascript">
        try {
            ace.settings.check('main-container', 'fixed')
        } catch (e) {
        }
    </script>

    <!-- /section:basics/sidebar -->
    <div class="main-content">

        <!-- /section:basics/content.breadcrumbs -->
        <div class="page-content" >

            <!-- /section:settings.box -->
            <div class="page-content-area">

                <div class="row">
                    <div class="col-xs-10 col-xs-offset-0">
                        <!-- PAGE CONTENT BEGINS -->
                        <form class="form-horizontal" role="form" id="promotionForm">
                            <!-- #section:elements.form -->

                            <div class="form-group">
                                <label for="userName" class="control-label col-sm-2">变更人</label>
                                <div class="col-sm-2">
                                    <label id="userName" class="col-sm-2 form-control" ></label>
                                    <input type="hidden" id="userId"/>
                                </div>
                                <div class="col-sm-1">
                                    <button type="button" id="useSelect" class="btn btn-success">选择</button>
                                </div>
                                <label for="billType" class="col-sm-3 control-label">单据类型</label>
                                <div class="col-sm-3">
                                    <input type="text" readonly="readonly" class="form-control" id="billType" name="billType" placeholder="入库">
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="control-label col-sm-2">添加商品</label>
                                <div class="col-sm-10" style="background-color: gainsboro">
                                    <div class="row">
                                        <label class="control-label col-sm-3" style="text-align: center;">商品名称</label>
                                        <label class="control-label col-sm-2" style="text-align: center;">上级合伙人</label>
                                        <label class="control-label col-sm-2" style="text-align: center;">数量</label>
                                        <label class="control-label col-sm-4" style="text-align: center;">备注</label>
                                        <label class="control-label col-sm-1" style="text-align: center;">操作</label>
                                    </div>
                                </div>
                            </div>

                            <div id="promotion-rule-template" class="form-group promotion-rule" style="display: none;">
                                <label class="control-label col-sm-2 rule-name"></label>
                                <div class="col-sm-10">
                                    <div class="row">
                                        <input type="hidden" attr="ruleId" />
                                        <div class="col-sm-3"><input type="text"class="form-control" attr="ruleValue"> </div>
                                        <input type="hidden" attr="promotionGiftId" />
                                        <div class="col-sm-2">
                                            <input type="text"class="form-control" />
                                        </div>
                                        <div class="col-sm-2"><input type="numbers"class="form-control" attr="quantity"> </div>
                                        <div class="col-sm-4"><input type="text"class="form-control" attr="upperQuantity"> </div>
                                        <div class="col-sm-1"><button type="button" class="btn btn-warning removeRule">删除</button></div>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="addPro" class="control-label col-sm-2"></label>
                                <div class="col-sm-9">
                                    <button type="button" id="addPro" class="btn btn-success">添加</button>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="reason" class="control-label col-sm-2">单据说明</label>
                                <div class="col-sm-10">
                                    <textarea style="max-width: 100%" name="reason" id="reason" cols="50" rows="5" class="form-control"></textarea>
                                </div>
                            </div>
                        </form>

                        <row>
                            <label class="col-sm-6"></label>
                            <div class="col-sm-5">
                                <button type="submit" class="btn btn-lg btn-info" id="promotionSave">保存</button>
                            </div>
                        </row>

                        <div id="modal-library" class="modal fade" tabindex="-1" aria-hidden="true" style="display: none;">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <div class="modal-header no-padding">
                                        <div class="table-header">
                                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                                                <span class="white">×</span>
                                            </button>
                                            选择变更人
                                        </div>
                                    </div>

                                    <div class="modal-body no-padding">
                                        <form class="form-horizontal" role="form" id="library">

                                            <%--<div class="form-group">
                                                <div class="col-xs-offset-1 col-sm-5">
                                                    <input id="useId" class="form-control" type="text" placeholder="请输入用户ID"/>
                                                </div>
                                                <label class="col-sm-0"></label>
                                                <div class="col-sm-5">
                                                    <input id="useName" class="form-control" type="text" placeholder="请输入用户姓名"/>
                                                </div>
                                            </div>--%>

                                            <div class="form-group">
                                                <div class="col-xs-offset-1 col-sm-5">
                                                    <input id="userPhone" type="text" class="form-control" name="userPhone" placeholder="请输入用户手机号">
                                                </div>
                                                <label class="col-sm-0"></label>
                                                <div class="col-sm-5">
                                                    <input id="useWx" class="form-control" type="text" placeholder="请输入用户微信号"/>
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <label class="control-label col-sm-1"></label>
                                                <div class="col-sm-3">
                                                    <button type="button" class="btn btn-success">查询</button>
                                                </div>
                                            </div>

                                            <div class="form-group" style="height: 170px;">
                                                <label class="control-label col-sm-1"></label>
                                                <div class="col-sm-10" style="height: 150px;height: 163px;overflow: auto;">
                                                    <%--<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">--%>
                                                        <%--Action <span class="caret"></span>--%>
                                                    <%--</button>--%>
                                                    <%--<ul class="dropdown-menu" role="menu" style="height:155px;
                                                     overflow: auto; overflow-y: scroll;">
                                                        <li><a href="#">Action</a></li>
                                                        <li><a href="#">Another action</a></li>
                                                        <li><a href="#">Something else here</a></li>
                                                        <li><a href="#">Something else here</a></li>
                                                        <li><a href="#">Something else here</a></li>
                                                        <li><a href="#">Something else here</a></li>
                                                        <li><a href="#">Something else here</a></li>
                                                    </ul>--%>
                                                        <ul class="list-group">
                                                            <li class="list-group-item">揭开CSS3的面纱</li>
                                                            <li class="list-group-item">CSS3选择器</li>
                                                            <li class="list-group-item">CSS3边框</li>
                                                            <li class="list-group-item">CSS3背景</li>
                                                            <li class="list-group-item">CSS3文本</li>
                                                        </ul>
                                                </div>
                                            </div>

                                        </form>
                                    </div>

                                    <div class="modal-footer no-margin-top">
                                        <div class="col-xs-5 col-sm-5 col-sm-offset-5">
                                            <button class="btn btn-sm btn-danger pull-left" id="cancelSave" type="button" data-dismiss="modal">
                                                取消
                                            </button>
                                            <button class="btn btn-sm btn-info pull-left" id="saveLibrary" type="button">
                                                保存
                                            </button>
                                        </div>
                                    </div>
                                </div><!-- /.modal-content -->
                            </div><!-- /.modal-dialog -->
                        </div>

                    </div><!-- /.col -->
                </div><!-- /.row -->
            </div><!-- /.page-content-area -->
        </div><!-- /.page-content -->
    </div><!-- /.main-content -->

</div><!-- /.main-container -->

<!-- basic scripts -->

<!--[if !IE]> -->
<script type="text/javascript">
    window.jQuery || document.write("<script src='<%=basePath%>static/js/jquery-2.2.0.min.js'>" + "<" + "/script>");
</script>

<!-- <![endif]-->

<!--[if IE]>
<script type="text/javascript">
    window.jQuery || document.write("<script src='<%=basePath%>static/js/jquery-2.2.0.min.js'>" + "<" + "/script>");
</script>
<![endif]-->
<script type="text/javascript">
    if ('ontouchstart' in document.documentElement) document.write("<script src='<%=basePath%>static/ace2/js/jquery.mobile.custom.min.js'>" + "<" + "/script>");
</script>
<script src="<%=basePath%>static/ace2/js/bootstrap.min.js"></script>

<!-- page specific plugin scripts -->
<script src="<%=basePath%>static/ace2/js/dropzone.min.js"></script>
<script src="<%=basePath%>static/ace2/js/uncompressed/bootbox.js"></script>

<script>

    $(document).ready(function() {
        $(".list-group .list-group-item").on("click", function(){
            $(this).addClass("li_selected").siblings().removeClass("li_selected");
        });
        // 添加规则
        function addPromotionRule() {
            var $template = $('#promotion-rule-template');
            var newRule = $template.clone(true).removeAttr("id").show();
            $template.before(newRule);
        }

        addPromotionRule();

        $('button#addPro').click(addPromotionRule);

        // 删除规则
        $('button.removeRule').click(function() {
            var $rule = $(this).parents('.promotion-rule');

            if($('.promotion-rule').length < 3) {
                alert("最少要有一件商品 !");
                return;
            }

            bootbox.confirm("确定删除？",function(result) {
                if(result) {
                    $rule.remove();
                }
                return true;
            });

        });

        // 保存表单
        $('#promotionSave').click(function() {
            $('#promotionForm').submit();
        })


        $('#promotionForm').bootstrapValidator({
                    message: '必须填写',
                    feedbackIcons: {
                        valid: 'glyphicon glyphicon-ok',
                        invalid: 'glyphicon glyphicon-remove',
                        validating: 'glyphicon glyphicon-refresh'
                    },
                    fields: {
                        name: {
                            message: '必须填写!',
                            validators: {
                                notEmpty: {}
                            }
                        },
                        remark: {
                            message: '必须填写!',
                            validators: {
                                notEmpty: {}
                            }
                        },
                        personType: {
                            message: '必须填写!',
                            validators: {
                                notEmpty: {}
                            }
                        }

                    }
                })
                .on('success.form.bv', function(e) {
                    // Prevent form submission
                    e.preventDefault();

                    // Get the form instance
                    var $form = $(e.target);

                    // Get the BootstrapValidator instance
                    var bv = $form.data('bootstrapValidator');

                    // Use Ajax to submit form data
                    $.ajax({
                        url: '<%=basePath%>promotion/saveOrUpdate.do',
                        type: 'post',
                        dataType:'json',
                        data: $('#promotionForm').serialize(),
                        success: function(msg){
                            if(msg.promotionId) {
                                loadPromotion(msg.promotionId);
                                alert("成功！");
                            }
                        }
                    });
                });

        $("#useSelect").on("click", function(){
            $("#modal-library").modal("show");
        });

        $("#modal-library").modal("show");
    });
</script>
</body>
</html>
