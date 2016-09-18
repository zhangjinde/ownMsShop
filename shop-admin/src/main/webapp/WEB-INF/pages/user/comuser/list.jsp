<%@ page language="java" import="java.util.*" contentType="text/html; utf-8" pageEncoding="UTF-8" %>
<%--<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>--%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta charset="utf-8" />
    <title>Tables - Ace Admin</title>

    <meta name="description" content="Static &amp; Dynamic Tables" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />

    <!-- bootstrap & fontawesome -->
    <link rel="stylesheet" href="<%=basePath%>static/ace2/css/tab.css" />
    <link rel="stylesheet" href="<%=basePath%>static/ace2/css/bootstrap.min.css" />
    <link rel="stylesheet" href="<%=basePath%>static/ace2/css/font-awesome.min.css" />

    <!-- page specific plugin styles -->

    <!-- text fonts -->
    <link rel="stylesheet" href="<%=basePath%>static/ace2/css/ace-fonts.css" />

    <!-- ace styles -->
    <link rel="stylesheet" href="<%=basePath%>static/ace2/css/uncompressed/ace.css" id="main-ace-style" />

    <!--[if lte IE 9]>
    <link rel="stylesheet" href="<%=basePath%>static/ace2/css/ace-part2.min.css" />
    <![endif]-->
    <link rel="stylesheet" href="<%=basePath%>static/ace2/css/ace-skins.min.css" />
    <link rel="stylesheet" href="<%=basePath%>static/ace2/css/ace-rtl.min.css" />

    <!--[if lte IE 9]>
    <link rel="stylesheet" href="<%=basePath%>static/ace2/css/ace-ie.min.css" />
    <![endif]-->

    <!-- inline styles related to this page -->

    <!-- ace settings handler -->
    <script src="<%=basePath%>static/ace2/js/uncompressed/ace-extra.js"></script>

    <!-- HTML5shiv and Respond.js for IE8 to support HTML5 elements and media queries -->

    <!--[if lte IE 8]>
    <script src="<%=basePath%>static/ace2/js/html5shiv.min.js"></script>
    <script src="<%=basePath%>static/ace2/js/respond.min.js"></script>
    <![endif]-->
</head>

<body class="no-skin">

<!-- /section:basics/navbar.layout -->
<div class="main-container" id="main-container">
    <script type="text/javascript">
        try{ace.settings.check('main-container' , 'fixed')}catch(e){}
    </script>

    <!-- /section:basics/sidebar -->
    <div class="main-content" style="margin: 0;">

        <!-- /section:basics/content.breadcrumbs -->
        <div class="page-content">

            <!-- /section:settings.box -->
            <div class="page-content-area">

                <div class="row">
                    <div class="col-xs-12">
                        <!-- PAGE CONTENT BEGINS -->

                        <div class="row">
                            <div class="col-xs-12">

                                <div>
                                    <div id="toolbar">
                                        <div class="form-inline">
                                            <div class="form-group">
                                                <label for="realName">姓名</label>
                                                <input type="text" class="form-control" id="realName_" name="realName" placeholder="">
                                            </div>
                                            <div class="form-group">
                                                <label for="mobile1">手机号</label>
                                                <input type="text" class="form-control" id="mobile1" name="mobile1" placeholder="">
                                            </div>
                                            <div class="form-group">
                                                <label for="idCard">身份证号</label>
                                                <input type="text" class="form-control" id="idCard" name="idCard" placeholder="">
                                            </div>
                                            <button type="button" class="btn btn-default" id="searchBtn">查询</button>
                                        </div>
                                    </div>
                                    <table class="table table-striped table-bordered table-hover dataTable no-footer" id="table" role="grid" aria-describedby="sample-table-2_info"
                                           data-toolbar="#toolbar"
                                           <%--data-search="true"--%>
                                           <%--data-show-refresh="true"--%>
                                           data-show-toggle="true"
                                    <%--data-show-columns="true"--%>
                                           <%--data-show-export="true"--%>
                                           data-detail-view="false"
                                           data-detail-formatter="detailFormatter"
                                           data-minimum-count-columns="2"
                                    <%--data-show-pagination-switch="true"--%>
                                           data-pagination="true"
                                           data-id-field="id"
                                           data-page-list="[10, 25, 50]"
                                           data-show-footer="false"
                                           data-side-pagination="server"
                                           data-url="/comuser/list.do"
                                           data-response-handler="responseHandler">
                                    </table>

                                </div>
                            </div>
                        </div>

                        <div id="modal-audit" class="modal fade" tabindex="-1">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <div class="modal-header no-padding">
                                        <div class="table-header">
                                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                                                <span class="white">&times;</span>
                                            </button>
                                            会员信息审核
                                        </div>
                                    </div>

                                    <div class="modal-body no-padding">
                                        <div>
                                            <div id="user-profile-1" class="user-profile row">
                                                <div class="col-xs-12 col-sm-12 col-sm-offset-0">

                                                    <!-- #section:pages/profile.info -->
                                                    <div class="profile-user-info profile-user-info-striped">

                                                        <div class="profile-info-row">
                                                            <div class="profile-info-name"> 会员信息 </div>

                                                            <div class="profile-info-value">
                                                                <span class="" id="nickName"> </span>
                                                            </div>
                                                        </div>

                                                        <div class="profile-info-row">
                                                            <div class="profile-info-name"> 姓名 </div>

                                                            <div class="profile-info-value">
                                                                <span class="editable editable-click" id="realName"> </span>
                                                            </div>
                                                        </div>

                                                        <div class="profile-info-row">
                                                            <div class="profile-info-name"> 上级信息 </div>

                                                            <div class="profile-info-value">
                                                                <table class="table table-bordered table-striped">
                                                                    <thead class="thin-border-bottom">
                                                                    <tr>
                                                                        <th>
                                                                            <i class="ace-icon fa fa-caret-right blue"></i>name
                                                                        </th>

                                                                        <th>
                                                                            <i class="ace-icon fa fa-caret-right blue"></i>price
                                                                        </th>

                                                                        <th class="hidden-480">
                                                                            <i class="ace-icon fa fa-caret-right blue"></i>status
                                                                        </th>
                                                                    </tr>
                                                                    </thead>

                                                                    <tbody>
                                                                    <tr>
                                                                        <td>internet.com</td>

                                                                        <td>
                                                                            <small>
                                                                                <s class="red">$29.99</s>
                                                                            </small>
                                                                            <b class="green">$19.99</b>
                                                                        </td>

                                                                        <td class="hidden-480">
                                                                            <span class="label label-info arrowed-right arrowed-in">on sale</span>
                                                                        </td>
                                                                    </tr>

                                                                    <tr>
                                                                        <td>online.com</td>

                                                                        <td>
                                                                            <small>
                                                                                <s class="red"></s>
                                                                            </small>
                                                                            <b class="green">$16.45</b>
                                                                        </td>

                                                                        <td class="hidden-480">
                                                                            <span class="label label-success arrowed-in arrowed-in-right">approved</span>
                                                                        </td>
                                                                    </tr>

                                                                    <tr>
                                                                        <td>newnet.com</td>

                                                                        <td>
                                                                            <small>
                                                                                <s class="red"></s>
                                                                            </small>
                                                                            <b class="green">$15.00</b>
                                                                        </td>

                                                                        <td class="hidden-480">
                                                                            <span class="label label-danger arrowed">pending</span>
                                                                        </td>
                                                                    </tr>

                                                                    <tr>
                                                                        <td>web.com</td>

                                                                        <td>
                                                                            <small>
                                                                                <s class="red">$24.99</s>
                                                                            </small>
                                                                            <b class="green">$19.95</b>
                                                                        </td>

                                                                        <td class="hidden-480">
																	<span class="label arrowed">
																		<s>out of stock</s>
																	</span>
                                                                        </td>
                                                                    </tr>

                                                                    <tr>
                                                                        <td>domain.com</td>

                                                                        <td>
                                                                            <small>
                                                                                <s class="red"></s>
                                                                            </small>
                                                                            <b class="green">$12.00</b>
                                                                        </td>

                                                                        <td class="hidden-480">
                                                                            <span class="label label-warning arrowed arrowed-right">SOLD</span>
                                                                        </td>
                                                                    </tr>
                                                                    </tbody>
                                                                </table>
                                                            </div>
                                                        </div>

                                                        <div class="profile-info-row">
                                                            <div class="profile-info-name"> 身份证号 </div>

                                                            <div class="profile-info-value">
                                                                <span class="" id="linkmanIDCard"> </span>
                                                            </div>
                                                        </div>

                                                        <div class="profile-info-row">
                                                            <div class="profile-info-name"> 身份证扫描件 </div>

                                                            <div class="profile-info-value" style="height: 200px;">
                                                                <img data-action="zoom" class="img-thumbnail" id="idCardF" alt="200x200" src="#" data-holder-rendered="true" style="width: 245px;height: 200px;">
                                                                <img data-action="zoom" class="img-thumbnail" id="idCardB" alt="200x200" src="#" data-holder-rendered="true" style="width: 245px;height: 200px">
                                                            </div>
                                                        </div>

                                                        <div class="profile-info-row">
                                                            <div class="profile-info-name" id="jjT"> 审核备注 </div>

                                                            <div class="profile-info-value" id="jjF">
                                                                <form id="auditForm">
                                                                    <input type="hidden" name="id" id="userId" value="" />
                                                                    <input type="hidden" name="mobile" id="mobile" value="" />
                                                                    <input type="hidden" name="auditStatus" id="auditStatus" value="2" />
                                                                    <textarea name="auditReason" id="auditReason" placeholder="请填写审核记录" rows="3" cols="50"></textarea>
                                                                </form>
                                                            </div>
                                                        </div>

                                                    </div>

                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="modal-footer no-margin-top">
                                        <div class="col-xs-5 col-sm-5 col-sm-offset-4">
                                            <input id="gritter-light" checked="" type="checkbox" class="ace ace-switch ace-switch-5">
                                            <button class="btn btn-sm btn-danger pull-left audit" audit-status="3">
                                                拒绝
                                            </button>
                                            <button class="btn btn-sm btn-info pull-left audit" audit-status="2">
                                                通过
                                            </button>
                                        </div>
                                    </div>
                                </div><!-- /.modal-content -->
                            </div><!-- /.modal-dialog -->
                        </div><!-- PAGE CONTENT ENDS -->

                        <div id="modal-table" class="modal fade active in" tabindex="-1">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <div class="modal-header no-padding">
                                        <div class="table-header">
                                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                                                <span class="white">&times;</span>
                                            </button>
                                            Results for "Latest Registered Domains
                                        </div>
                                    </div>

                                    <div class="modal-body no-padding">
                                        <table class="table table-striped table-bordered table-hover no-margin-bottom no-border-top">
                                            <thead>
                                            <tr>
                                                <th>Domain</th>
                                                <th>Price</th>
                                                <th>Clicks</th>

                                                <th>
                                                    <i class="ace-icon fa fa-clock-o bigger-110"></i>
                                                    Update
                                                </th>
                                            </tr>
                                            </thead>

                                            <tbody>
                                            <tr>
                                                <td>
                                                    <a href="#">ace.com</a>
                                                </td>
                                                <td>$45</td>
                                                <td>3,330</td>
                                                <td>Feb 12</td>
                                            </tr>

                                            <tr>
                                                <td>
                                                    <a href="#">base.com</a>
                                                </td>
                                                <td>$35</td>
                                                <td>2,595</td>
                                                <td>Feb 18</td>
                                            </tr>

                                            <tr>
                                                <td>
                                                    <a href="#">max.com</a>
                                                </td>
                                                <td>$60</td>
                                                <td>4,400</td>
                                                <td>Mar 11</td>
                                            </tr>

                                            <tr>
                                                <td>
                                                    <a href="#">best.com</a>
                                                </td>
                                                <td>$75</td>
                                                <td>6,500</td>
                                                <td>Apr 03</td>
                                            </tr>

                                            <tr>
                                                <td>
                                                    <a href="#">pro.com</a>
                                                </td>
                                                <td>$55</td>
                                                <td>4,250</td>
                                                <td>Jan 21</td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </div>

                                    <div class="modal-footer no-margin-top">
                                        <button class="btn btn-sm btn-danger pull-left" data-dismiss="modal">
                                            <i class="ace-icon fa fa-times"></i>
                                            Close
                                        </button>

                                        <ul class="pagination pull-right no-margin">
                                            <li class="prev disabled">
                                                <a href="#">
                                                    <i class="ace-icon fa fa-angle-double-left"></i>
                                                </a>
                                            </li>

                                            <li class="active">
                                                <a href="#">1</a>
                                            </li>

                                            <li>
                                                <a href="#">2</a>
                                            </li>

                                            <li>
                                                <a href="#">3</a>
                                            </li>

                                            <li class="next">
                                                <a href="#">
                                                    <i class="ace-icon fa fa-angle-double-right"></i>
                                                </a>
                                            </li>
                                        </ul>
                                    </div>
                                </div><!-- /.modal-content -->
                            </div><!-- /.modal-dialog -->
                        </div><!-- PAGE CONTENT ENDS -->
                    </div><!-- /.col -->
                </div><!-- /.row -->
            </div><!-- /.page-content-area -->
        </div><!-- /.page-content -->
    </div><!-- /.main-content -->

    <a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
        <i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
    </a>
</div><!-- /.main-container -->

<!-- basic scripts -->

<!--[if !IE]> -->
<script type="text/javascript">
    window.jQuery || document.write("<script src='<%=basePath%>static/ace2/js/jquery.min.js'>"+"<"+"/script>");
</script>

<!-- <![endif]-->

<!--[if IE]>
<script type="text/javascript">
    window.jQuery || document.write("<script src='<%=basePath%>static/ace2/js/jquery1x.min.js'>"+"<"+"/script>");
</script>
<![endif]-->
<script type="text/javascript">
    if('ontouchstart' in document.documentElement) document.write("<script src='<%=basePath%>static/ace2/js/jquery.mobile.custom.min.js'>"+"<"+"/script>");
</script>
<script src="<%=basePath%>static/ace2/js/bootstrap.min.js"></script>

<!-- page specific plugin scripts -->
<script src="<%=basePath%>static/ace2/js/jquery.dataTables.min.js"></script>
<script src="<%=basePath%>static/ace2/js/jquery.dataTables.bootstrap.js"></script>

<script src="<%=basePath%>static/js/date-util.js"></script>

<script>
    var $table = $('#table'),
            $remove = $('#remove'),
            selections = [];

    function initTable() {
        $table.bootstrapTable({
            //height: getHeight(),
            locale: 'zh-CN',
            striped: true,
            //multipleSearch: true,
            queryParamsType: 'pageNo',
            queryParams: function(params){
                if($('#realName_').val()) params.realName = $('#realName_').val();
                if($('#mobile1').val())    params.mobile1   = $('#mobile1').val();
                if($('#idCard').val())    params.idCard   = $('#idCard').val();
                return params;
            },
            rowStyle: function rowStyle(value, row, index) {
                return {
                    classes: 'text-nowrap another-class',
                    css: {}
                };
            },
            formatShowingRows: function (pageFrom, pageTo, totalRows) {
                return '当前显示 ' + pageFrom + " 到 " + pageTo + ', 总共 ' + totalRows;
            },
            formatRecordsPerPage: function (pageNumber) {
                return '每页显示' + pageNumber + '条数据';
            },
            formatSearch: function () {
                return "请输入关键字";
            },
            formatNoMatches: function () {
                return "没有找到数据哦!";
            },
            columns: [
                [
                    {
                        checkbox: true,
                        align: 'center',
                        valign: 'middle'
                    }, {
                    title: 'ID',
                    field: 'id',
                    align: 'center',
                    valign: 'middle',
                    sortable: true,
                    footerFormatter: totalTextFormatter,
                    formatter: function(value, row, index){
                        if(row.comUser && row.comUser.id){
                            return row.comUser.id;
                        }
                    }
                },
                    {
                        field: 'name',
                        title: '姓名',
                        sortable: true,
                        //editable: true,
                        footerFormatter: totalNameFormatter,
                        align: 'center',
                        formatter: function(value, row, index){
                            if(row.comUser && row.comUser.realName){
                                return row.comUser.realName;
                            }
                        }
                    },
                    {
                        field: 'mobile',
                        title: '手机号码',
                        sortable: true,
                        footerFormatter: totalNameFormatter,
                        align: 'center',
                        formatter: function(value, row, index){
                            if(row.comUser && row.comUser.mobile){
                                return row.comUser.mobile;
                            }
                        }
                    },
                    {
                        field: 'idCard',
                        title: '身份证号',
                        sortable: true,
                        footerFormatter: totalNameFormatter,
                        align: 'center',
                        formatter: function(value, row, index){
                            if(row.comUser && row.comUser.idCard){
                                return row.comUser.idCard;
                            }
                        }
                    },
                    {
                        field: 'level',
                        title: '会员级别',
                        footerFormatter: totalNameFormatter,
                        formatter: function (value, row, index) {
                            if(row.comUser && row.comUser.level){
                                return row.comUser.level;
                            }
                            return '普通';
                        },
                        align: 'center'
                    },
                    {
                        field: 'accountBalance',
                        title: '账户余额',
                        sortable: true,
                        footerFormatter: totalNameFormatter,
                        align: 'center',
                        formatter: function(value, row, index){
                            if(row.comUserAccount){
                                return row.comUserAccount.extractableFee;
                            }
                        }
                    },
                    {
                        align: 'center',
                        field: 'settlementFund',
                        title: '结算中资金',
                        sortable: true,
                        footerFormatter: totalNameFormatter,
                        formatter: function(value, row, index){
                            if(row.comUserAccount){
                                return row.comUserAccount.countingFee;
                            }
                        }
                    },
                    {
                        align: 'center',
                        field: 'createTime',
                        title: '注册日期',
                        sortable: true,
                        footerFormatter: totalNameFormatter,
                        formatter: function(value, row, index){
                            if(row.comUser && row.comUser.createTime){
                                return new Date(row.comUser.createTime).pattern('yyyy-MM-dd HH:mm:ss');
                            }
                        }
                    },
                    {
                        field: 'auditStatus',
                        title: '是否实名认证',
                        sortable: true,
                        footerFormatter: totalNameFormatter,
                        align: 'center',
                        formatter: function(value, row, index){
                            if(row.comUser && row.comUser.auditStatus == 2){
                                return '<span class="label label-sm label-success">已认证</span>';
                            }else{
                                return '<span class="label label-sm label-warning">未认证</span>';
                            }
                        }
                    },
                    {
                        title: '操作项',
                        align: 'center',
                        formatter: function(value, row, index){
                            return [
                                '<a class="personal-info" href="javascript:void(0);">个人信息</a>',
                            ].join('');
                        },
                        events: {
                            'click .personal-info': function (e, value, row, index) {
                                parent.window.$('#myTabbable').add('tab-'+row.comUser.id, '会员信息', '<%=basePath%>comuser/detail.shtml?id='+row.comUser.id);
                            },
                            'click .update-superior': function(e, value, row, index) {
                            }
                        }
                    }
                ]
            ]
        });
        // sometimes footer render error.
        setTimeout(function () {
            $table.bootstrapTable('resetView');
        }, 200);
        $table.on('check.bs.table uncheck.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $remove.prop('disabled', !$table.bootstrapTable('getSelections').length);

            // save your data, here just save the current page
            selections = getIdSelections();
            // push or splice the selections if you want to save all data selections
        });
        $table.on('expand-row.bs.table', function (e, index, row, $detail) {
            $detail.html('数据加载中...');
            $.get('/user/load.shtml', {id: row.id}, function (res) {
                //$detail.html(res.replace(/\n/g, '<br>'));
                $detail.html(res);
            });
        });
        $table.on('all.bs.table', function (e, name, args) {
            console.log(name, args);
        });
        $('#searchBtn').on('click', function(){
            $table.bootstrapTable('refresh');
        });
        $remove.click(function () {
            var ids = getIdSelections();
            console.log('remove: ' + ids);
            $table.bootstrapTable('remove', {
                field: 'id',
                values: ids
            });
            $remove.prop('disabled', true);
        });
        $(window).resize(function () {
            $table.bootstrapTable('resetView', {
                height: getHeight()
            });
        });
    }

    function getIdSelections() {
        return $.map($table.bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }

    function responseHandler(res) {
        $.each(res.rows, function (i, row) {
            row.state = $.inArray(row.id, selections) !== -1;
        });
        return res;
    }

    function detailFormatter(index, row) {
        var html = [];
        $.each(row, function (key, value) {
            html.push('<p><b>' + key + ':</b> ' + value + '</p>');
        });
        return html.join('');
    }

    function operateFormatter(value, row, index) {
        var arr = [];
        arr.push('&nbsp;<a class="edit" href="<%=basePath%>product/edit.shtml?skuId='+ row.comSku.id +'" title="Edit">编辑</a>');
        if(row.comSpu && row.comSpu.isSale == 0){
            arr.push('&nbsp;<a class="putaway" href="javascript:void(0)" title="Putaway">上架</a>');
        }else if(row.comSpu && row.comSpu.isSale == 1){
            arr.push('&nbsp;<a class="putaway" href="javascript:void(0)" title="Putaway">下架</a>');
        }

        return arr.join(' ');
    }

    function totalTextFormatter(data) {
        return 'Total';
    }

    function totalNameFormatter(data) {
        return data.length;
    }

    function totalPriceFormatter(data) {
        var total = 0;
        $.each(data, function (i, row) {
            total += +(row.price.substring(1));
        });
        return '$' + total;
    }

    function getHeight() {
        return $(window).height() - $('h1').outerHeight(true);
    }

    $(function () {
        var scripts = [
                    location.search.substring(1) || '<%=basePath%>static/class/bootstrap-3.3.5-dist/js/bootstrap-table.min.js',
                    '<%=basePath%>static/class/bootstrap-3.3.5-dist/js/bootstrap-table-export.js',
                    '<%=basePath%>static/class/bootstrap-3.3.5-dist/js/tableExport.js',
                    '<%=basePath%>static/class/bootstrap-3.3.5-dist/js/bootstrap-table-editable.js',
                    '<%=basePath%>static/class/bootstrap-3.3.5-dist/js/bootstrap-editable.js'
                ],
                eachSeries = function (arr, iterator, callback) {
                    callback = callback || function () {
                            };
                    if (!arr.length) {
                        return callback();
                    }
                    var completed = 0;
                    var iterate = function () {
                        iterator(arr[completed], function (err) {
                            if (err) {
                                callback(err);
                                callback = function () {
                                };
                            }
                            else {
                                completed += 1;
                                if (completed >= arr.length) {
                                    callback(null);
                                }
                                else {
                                    iterate();
                                }
                            }
                        });
                    };
                    iterate();
                };

        eachSeries(scripts, getScript, initTable);
    });

    function getScript(url, callback) {
        var head = document.getElementsByTagName('head')[0];
        var script = document.createElement('script');
        script.src = url;

        var done = false;
        // Attach handlers for all browsers
        script.onload = script.onreadystatechange = function () {
            if (!done && (!this.readyState ||
                    this.readyState == 'loaded' || this.readyState == 'complete')) {
                done = true;
                if (callback)
                    callback();

                // Handle memory leak in IE
                script.onload = script.onreadystatechange = null;
            }
        };

        head.appendChild(script);

        // We handle everything using the script element injection
        return undefined;
    }

    $('#searchBtn').on('click', function(){

    });

</script>
</body>
</html>
