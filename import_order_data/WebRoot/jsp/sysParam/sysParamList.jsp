<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../common.jsp" />
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>报表系统</title>
<script type="text/javascript">
    Ext.require(['*']);
        Ext.onReady(function () {

            //***表格显示内容*** 
            var tbar = Ext.create("Ext.toolbar.Toolbar", {
                items: [
                    /*{
                        text: '添加参数',
                        iconCls: 'a_add',
                        handler: function () {
                            AddDialog.setTitle("添加参数");
                            AddDialog.setIconCls("a_add");
                            AddForm.form.reset();
                            Ext.getCmp("btnAdd").show();
                            Ext.getCmp("btnEdit").hide();
                            if (typeof (RoleGrid) == "undefined") {
                                return false;
                            }
                            AddDialog.show();
                        }
                    }, */'-',{
                        text: '修改',
                        iconCls: 'a_edit',
                        handler: function () {
                            AddDialog.setTitle("修改参数");
                            AddDialog.setIconCls("a_edit");
                            AddForm.form.reset();
                            Ext.getCmp("btnEdit").show();
                            Ext.getCmp("btnAdd").hide();
                            if (typeof (RoleGrid) == "undefined") {
                                return false;
                            }
                            var rows = RoleGrid.getView().getSelectionModel().getSelection();
                            if (typeof (rows[0]) == "undefined") {
                                Ext.Msg.alert("提示", "请选择要操作的行！");
                                return false;
                            }
                            AddForm.form.setValues(rows[0].data);
                            AddDialog.show();
                        }
                    },'-',
                    {
                        text: '删除',
                        iconCls: 'a_del',
                        handler: DelSysParam
                    }, "-","->",
                    {
                        emptyText: '参数名称',
                        xtype:'textfield',
                        id: 'findName',
                        name: 'findName'
			       },
             		{ 
	             		iconCls:"a_search",
	             		text:"搜索",
	             		handler:function (){
	             			SysParamGridStore.load();
	             		}
             		}
                ]
            });
          


            var SysParamGridStore = Ext.create('Ext.data.Store', {
                pageSize: 15,
                proxy: {
                    type: 'ajax',
                    url: '<%=request.getContextPath()%>/sysParam/getJsonSysParams.do',
                    method:'post',
                    reader: {   //这里的reader为数据存储组织的地方
                        type: 'json', //返回数据类型为json格式
                        root: 'rows',  //数据
                        totalProperty: 'total' //数据总条数
                    }
                },
                fields: ['id','parameterName','description','value','deptOverride'],
                folderSort: true,
                autoLoad: true  //即时加载数据
            });
            
            SysParamGridStore.on('beforeload', function (SysParamGridStore, options) {  
		        var new_params = {findName:Ext.getCmp('findName').value};  
		           Ext.apply(SysParamGridStore.proxy.extraParams, new_params);  
		    });
            var RoleGrid = Ext.create('Ext.grid.Panel', {
                tbar: tbar,
                border: 0,
                autoHeight : true,
        		autoScroll:true,
                store: SysParamGridStore,
                multiSelect: false,
                selModel: { selType: 'checkboxmodel' },
                renderTo: Ext.getBody(),
			 	columns: [/*{  
                    text: 'ID',  //显示的表头列名称
                    flex: 1,
                    dataIndex: 'id',  //对应store的列字段名称
                    sortable: false,
                    menuDisabled:true
                },*/{  
                    text: '参数名称',  //显示的表头列名称
                    flex: 1,
                    dataIndex: 'parameterName',  //对应store的列字段名称
                    sortable: false,
                    menuDisabled:true
                },{  
                    text: '参数值',  //显示的表头列名称
                    flex: 1,
                    dataIndex: 'value',  //对应store的列字段名称
                    sortable: false,
                    menuDisabled:true
                },{  
                    text: '参数描述',  //显示的表头列名称
                    flex: 1,
                    dataIndex: 'description',  //对应store的列字段名称
                    sortable: false,
                    menuDisabled:true
                },{  
                    text: '是否可覆盖',  //显示的表头列名称
                    flex: 1,
                    dataIndex: 'deptOverride',  //对应store的列字段名称
                    sortable: false,
                    menuDisabled:true
                }] 
                , listeners: {
                    //点击行触发事件
                    itemclick: function (record, node) {
                        //把列的qcid传递给QuesGridStore刷新对应的grid或tree
                      //  QuesGridStore.load({ params: { csId: node.data.id } });
                    }
                },
               bbar: [{
	                xtype: 'pagingtoolbar',
	                store: SysParamGridStore,
	                displayMsg: '显示 {0} - {1} 条，共计 {2} 条',
	                emptyMsg: "没有数据",
	                beforePageText: "当前页",
	                afterPageText: "共{0}页",
	                displayInfo: true                 
            	}]
            });          


			//****工具栏操作内容****
			
			
			            
    		//***权限结束
			var AddForm = Ext.create("Ext.form.Panel", {
			                border: false,
			                fieldDefaults: {
			                    msgTarget: 'side',  //提示信息在右旁边显示图标
			                    labelWidth: 105,
			                    align: "right",
			                    regexText: '格式错误', //错误提示
			                    allowBlank: false //不与许为空
			                },
			                defaults: {
			                    padding: 15,
			                    width: 380
			                },
			                defaultType: "textfield",
			                items: [
			                     {
			                         xtype: "hidden",
			                         id: 'id',
			                         name: 'id'
			                     },
			                     {
			                         fieldLabel: '参数名称',
			                         id: 'parameterName',
			                         name: 'parameterName',
			                         readOnly:true,
			                         allowBlank: false
			                     },
			                     {
			                         fieldLabel: '参数值',
			                         id: 'value',
			                         name: 'value',
			                         allowBlank: false
			                     }
			                     ,
			                     {
			                         fieldLabel: '参数描述',
			                         id: 'description',
			                         name: 'description',
			                         allowBlank: false
			                     },
			                     {
			                         fieldLabel: '是否可覆盖',
			                         xtype:'checkbox',
			                         inputValue:'true',
			                         id: 'deptOverride',
			                         name: 'deptOverride',
			                         allowBlank: false
			                     }
			                ]
			            });
			            
			      var AddDialog = Ext.create("Ext.window.Window", {
			                iconCls: 'a_add',
			                width:520,
			                height:260,
			                closeAction: 'close',        //窗口关闭的方式：hide/close
			                resizable: false,
			                closable: true,            //是否可以关闭
			                modal: true,                //是否为模态窗口
			                items: AddForm,
			                buttons: [{
			                    text: '添加',
			                    iconCls: 'a_save',
			                    id: "btnAdd",
			                    handler: function () {
			                        if (AddForm.form.isValid()) {
			                                AddSysParam();
			                        }
			                    }
			                },
			                {
			                    text: '修改',
			                    iconCls: 'a_save',  //样式
			                    id: "btnEdit",
			                    handler: EditSysParam
			                },
			                {
			                    id: "btnCancel",
			                    iconCls: 'a_cross',  //样式
			                    text: '关闭',
			                    handler: function () {
			                        //重置AddForm.form表单
			                        AddDialog.close();
			                    }
			                }]
			            });
			            
			
			            //1 添加
			            function AddSysParam() {
			                Ext.Ajax.request({
			                    method: "post",
			                    url: "<%=request.getContextPath()%>/sysParam/add.do",
			                    params: {json:JSON.stringify(AddForm.form.getValues())},
			                    callback: function (options, success, response) {
			                    	AddDialog.close();
			                        if (success) {
			                            var json = response.responseText;
			                            json = Ext.decode(json);
			                            SysParamGridStore.load();
			                            Ext.Msg.alert("提示", json.info);
			                        }
			                        else {
			                            Ext.Msg.alert("提示", "系统繁忙");
			                        }
			                    }
			                });
			            }
			            
			           
			           
			            //2 修改
			            function EditSysParam() {
			                var rows = RoleGrid.getView().getSelectionModel().getSelection();
			                if (typeof (rows[0]) == "undefined") {
			                    Ext.Msg.alert("提示", "请选择要操作的行！");
			                    return false;
			                }
			                Ext.Ajax.request({
			                    method: "post",
			                    url: "<%=request.getContextPath()%>/sysParam/edit.do",
			                    params: {json: JSON.stringify(AddForm.form.getValues()) },
			                    callback: function (options, success, response) {
			                    	AddDialog.close();
			                        if (success) {
			                            var json = response.responseText;
			                            json = Ext.decode(json);
			                            SysParamGridStore.load();
			                            Ext.Msg.alert("提示", json.info);
			                        }
			                        else {
			                            Ext.Msg.alert("提示", "系统繁忙");
			                        }
			                    }
			                });
			            }
			            
			             //3 删除
			            function DelSysParam() {
			                var rows = RoleGrid.getView().getSelectionModel().getSelection();
			                if (typeof (rows[0]) == "undefined") {
			                    Ext.Msg.alert("提示", "请选择要操作的行！");
			                    return false;
			                }
			                Ext.Ajax.request({
			                    method: "post",
			                    url: "<%=request.getContextPath()%>/sysParam/delete.do",
			                    params: { id: rows[0].data.id },
			                    callback: function (options, success, response) {
			                    	AddDialog.close();
			                        if (success) {
			                            var json = response.responseText;
			                            json = Ext.decode(json);
			                            SysParamGridStore.load();
			                            Ext.Msg.alert("提示", json.info);
			                        }
			                        else {
			                            Ext.Msg.alert("提示", "系统繁忙");
			                        }
			                    }
			                });
			            } 
			            
			            
      		
        });
      
    </script>  
</head>  
<body>
</body>
</html>
