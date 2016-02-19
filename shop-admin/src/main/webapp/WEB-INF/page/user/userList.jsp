<%@ page language="java" import="java.util.*" contentType="text/html; utf-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<link rel="stylesheet" href="<%=basePath%>static/js/easyui/themes/default/easyui.css" />
<link rel="stylesheet" href="<%=basePath%>static/js/easyui/themes/icon.css" />
<script src="<%=basePath%>static/js/jquery-2.2.0.min.js"></script>
<script src="<%=basePath%>static/js/easyui/jquery.easyui.min.js"></script>
<script>
    function updateActions(index){
        $('#userList').datagrid('updateRow', {
            index: index,
            row: {}
        });
    }
    function getRowIndex(target){
        var tr = $(target).closest('tr.datagrid-row');
        return parseInt(tr.attr('datagrid-row-index'));
    }
    function editrow(target){
        $('#userList').datagrid('beginEdit', getRowIndex(target));
    }
    function deleterow(target){alert('deleterow');
        $.messager.confirm('Confirm', '你确定要删除?', function(r){
            if(r){console.log('del: '+$('#userList').datagrid('getRows')[getRowIndex(target)].id);
                $('#userList').datagrid('deleteRow', getRowIndex(target));
                $.ajax({
                    url: '/user/delete.do',
                    data: {uid: $('#userList').datagrid('getRows')[getRowIndex(target)].id},
                    success: function(data){
                        if(data.msg == 'true'){
                            alert('删除成功');
                        }else{
                            alert('删除失败');
                        }
                    }
                });
            }
        });
    }
    function saverow(target){
        $('#userList').datagrid('endEdit', getRowIndex(target));
    }
    function cancelrow(target){
        $('#userList').datagrid('cancelEdit', getRowIndex(target));
    }
    function insert(){
        var row = $('#userList').datagrid('getSelected');
        if(row){
            var index = $('#userList').datagrid('getRowIndex', row);
        }else{
            index = 0;
        }
        $('#userList').datagrid('insertRow', {
            index: index,
            row: {
                status: 'P'
            }
        });
        $('#userList').datagrid('selectRow', index);
        $('#userList').datagrid('beginEdit', index);
    }

    function doSearch(){
        $('#userList').datagrid('load',{
            userName: $('#userName').val(),
            phone: $('#phone').val()
        });
    }

    $(function(){
        $('#userList').datagrid({
            url: '<%=basePath%>user/list.do',
            width: '100%',
            pagination: true,
            singleSelect:true,
            rownumbers : true,//行号
            idField:'id',
            columns: [[
                {field: 'cb', width: '10%', checkbox : true},
                {field: 'id', title: 'id', width: '10%', hidden: true},
                {field: 'userName', title: '用户名', width: '10%', align: 'center',
                    editor:{
                        type: 'text'
                    },
                    formatter:function(value,row,index){
                        return value;
                    }
                },
                {field: 'trueName', title: '姓名', width: '10%', align: 'center',
                    editor:{
                        type: 'text'
                    }
                },
                {field: 'password', title: '密码', width: '10%', align: 'center',
                    editor:{
                        type: 'text'
                    }
                },
                {field: 'email', title: '邮箱', width: '10%', align: 'center',
                    editor:{
                        type: 'text'
                    }
                },
                {field: 'sex', title: '性别', width: '10%', align: 'center',
                    editor:{
                        type: 'text'
                    }
                },
                {field: 'age', title: '年龄', width: '10%', align: 'center',
                    editor:{
                        type: 'text'
                    }
                },
                {field: 'phone', title: '电话', width: '10%', align: 'center',
                    editor:{
                        type: 'text'
                    }
                },
                {field: 'action', title: '操作', width: '10%', align: 'center',
                    formatter: function(value, row, index){
                        if(row.editing){
                            var s = '<a href="#" onclick="saverow(this)">保存</a>';
                            var c = '<a href="#" onclick="cancelrow(this)">取消</a>';
                            return s + c;
                        }else{
                            var e = '<a href="#" onclick="editrow(this)">编辑</a>';
                            var d = '<a href="#" onclick="deleterow(this)">删除</a>';
                            return e + d;
                        }
                    }
                }
            ]],
            onBeforeEdit: function(index, row){
                row.editing = true;
                updateActions(index);
            },
            onAfterEdit: function(index, row){
                row.editing = false;
                updateActions(index);
                $.ajax({
                    url: '/user/add.do',
                    type: 'post',
                    data: {
                        id: row.id,
                        userName: row.userName,
                        trueName: row.trueName,
                        password: row.password,
                        email: row.email,
                        sex: row.sex,
                        age: row.age,
                        phone: row.phone
                    },
                    success: function(msg){
                        alert(msg);
                    }
                });
            },
            onCancelEdit: function(index, row){
                row.editing = false;
                updateActions(index);
            }
        });

    });
</script>
<div id="tb" style="padding:3px">
    <span>用户名:</span>
    <input id="userName" style="line-height:26px;border:1px solid #ccc">
    <span>手机号:</span>
    <input id="phone" style="line-height:26px;border:1px solid #ccc">
    <a href="#" class="easyui-linkbutton" onclick="doSearch()">查询</a>
    <a href="#" class="easyui-linkbutton" onclick="insert()">添加用户</a>
</div>


<div id="userList"></div>