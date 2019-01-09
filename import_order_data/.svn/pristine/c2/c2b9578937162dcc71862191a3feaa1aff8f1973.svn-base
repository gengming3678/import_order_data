<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../common.jsp" />
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>报表系统</title>
<script type="text/javascript" src="<%=request.getContextPath()%>/ext4/ComboTreeBox.js"></script>
<script type="text/javascript">
    Ext.require(['*']);
        Ext.onReady(function () {

			/* Ext.EventManager.onWindowResize(function(){ 
				ReportGrid.getView().refresh(); 
			}); */
			
            var tbar = Ext.create("Ext.toolbar.Toolbar", {
                items: [
                    {
                        text: '添加',
                        iconCls: 'a_add',
                        handler: function () {
                            AddDialog.setTitle("添加");
                            AddDialog.setIconCls("a_add");
                            if (typeof (ReportGrid) == "undefined") {
                                return false;
                            }
                            AddForm.form.reset();
                            Ext.getCmp("btnUpdate").hide();
                            Ext.getCmp("btnAdd").show();
                            AddDialog.show();
                        }
                    }, '-',
                    {
                        text: '修改',
                        iconCls: 'a_edit',
                        handler: function () {
                            AddDialog.setTitle("修改");
                            AddDialog.setIconCls("a_edit");
                            if (typeof (ReportGrid) == "undefined") {
                                return false;
                            }
                            AddForm.form.reset();
                            var rows = ReportGrid.getView().getSelectionModel().getSelection();
                            if (typeof (rows[0]) == "undefined") {
                                Ext.Msg.alert("提示", "请选择要操作的行！");
                                return false;
                            }
                            
                            
                            
                            var nodeName = rows[0].data.nodeName;
                            var nodeId = rows[0].data.nodeId;
                            var id = rows[0].data.id;
                            var colName = rows[0].data.colName;
                            var sqlContent = rows[0].data.sqlContent;
                            var queryCondition = rows[0].data.queryCondition;
                            var ifProcedure = rows[0].data.ifProcedure;
                            
                          
                            AddForm.form.findField("id").setValue(id);
                            AddForm.form.findField("colName").setValue(colName);
                            AddForm.form.findField("sqlContent").setValue(sqlContent);
                            AddForm.form.findField("queryCondition").setValue(queryCondition);
                            AddForm.form.findField("ifProcedure").setValue(ifProcedure);
                            Ext.getCmp("btnUpdate").show();
                            Ext.getCmp("btnAdd").hide(); 
                            AddDialog.show();
                        }
                    },'-',
                    {
                        text: '删除',
                        iconCls: 'a_del',
                        handler: DelReport
                    }, "-", "->",
                    {
                        emptyText: '所属节点',
                        xtype:'textfield',
                        id: 'findName',
                        name: 'findName'
			       },
             		{ 
	             		iconCls:"a_search",
	             		text:"搜索",
	             		handler:findReport
             		}
                ]
            });

            var ReportGridStore = Ext.create('Ext.data.Store', {
                proxy: {
                    type: 'ajax',
                    url: '<%=request.getContextPath()%>/tableManager/getJsonReports.do',
                    method:'post',
                    reader: {   
                        type: 'json'/* ,
                        idProperty : 'nodeId' */
                    }
                },
                fields: ['id','nodeName','nodeId','colName','sqlContent','text','ifProcedure','queryCondition'],
                autoLoad: true  
            });
            
            ReportGridStore.on('beforeload', function (ReportGridStore, options) {  
		        var new_params = {nodeName:Ext.getCmp('findName').value};
		        Ext.apply(ReportGridStore.proxy.extraParams, new_params);   
		    });
		    
            var ReportGrid = Ext.create('Ext.grid.Panel', {
                tbar: tbar,
                border: 0,
                store: ReportGridStore,
                autoHeight : true,
        		autoScroll:true,
                multiSelect: false,
                renderTo: Ext.getBody(),
			 	columns: [{  
                    text: '表头名称',  
                    flex: 3,
                    dataIndex: 'colName',  
                    menuDisabled:true,
	 				sortable: false
                },{  
                    text: '查询条件参数',  
                    flex: 1,
                    dataIndex: 'queryCondition',  
                    menuDisabled:true,
	 				sortable: false
                },{  
                    text: '存储过程',  
                    flex: 0.6,
                    dataIndex: 'ifProcedure',  
                    menuDisabled:true,
	 				sortable: false,
	 				renderer: function (value, meta, record) {
			                    if(value==true) {
			                    	 return "是"; 
			                    }else{
			                    	 return "否"; 
			                    }
					} 
                },{  
                    text: '数据来源',  
                    flex: 5,
                    dataIndex: 'sqlContent',
                    menuDisabled:true,
	 				sortable: false
                },{  
                    text: '所属节点',  
                    flex: 2,
                    dataIndex: 'text',  
                    menuDisabled:true,
	 				sortable: false,
	 				renderer: function (value, meta, record) {
			                    if(value=='null') {
			                    	 return ""; 
			                    }else{
			                    	 return value; 
			                    }
					} 
                }] 
                , listeners: {
                    itemclick: function (record, node) {
                        
                    }
                }
                
            });          

			var AddForm = Ext.create("Ext.form.Panel", {
			                border: false,
			                id:'addReportForm',
			                name:'addReportForm',
			                fieldDefaults: {
			                    msgTarget: 'side',  
			                    labelWidth: 100,
			                    align: "right",
			                    regexText: '格式错误',
			                    allowBlank: false
			                },
			                defaults: {
			                    padding: 10,
			                    width: 500 
			                },
			                defaultType: "textfield",
			                items: [
			                     {
			                         id: 'id',
			                         name: 'id',
			                         xtype : 'hidden'
			                     },
			                     {
			                         fieldLabel: '表头名称',
			                         id: 'colName',
			                         name: 'colName',
			                         xtype : 'textareafield',
			                         height : 50,
			                         width : 500,
			                         allowBlank: false
			                     },
			                     {
			                         fieldLabel: '数据来源',
			                         xtype     : 'textareafield',
			                         height : 150,
			                         width : 500,
			                         id: 'sqlContent',
			                         name: 'sqlContent',
			                         allowBlank: false
			                     },
			                     { 
						            fieldLabel : '查询条件参数',
						            allowBlank : false,
						            id: 'queryCondition',
			                        name: 'queryCondition'
            					},{
			                         fieldLabel: '是否存储过程',
			                         xtype: "checkbox",
			                         id: 'ifProcedure',
			                         name: 'ifProcedure',
			                         inputValue:true,
			                         allowBlank: false
			                     }
			                ]
			            });
			            
			      var AddDialog = Ext.create("Ext.window.Window", {
			                iconCls: 'a_add',
			                closeAction: 'close',       
			                resizable: false,
			                closable: true,           
			                modal: true,
			                id:'addReportDialog',
			                name:'addReportDialog',               
			                items: AddForm,
			                buttons: [{
			                    text: '添加',
			                    id: "btnAdd",
			                    handler: function () {
			                    		if (AddForm.form.isValid()) {
			                                 AddReport();
			                             }
			                    }
			                },{
			                    text: '保存',
			                    id: "btnUpdate",
			                    handler: function () {
			                    		if (AddForm.form.isValid()) {
			                                	UpdateReport();
			                             }
			                    }
			                },
			                {
			                    id: "btnCancel",
			                    text: '关闭',
			                    handler: function () {
			                        ReportGridStore.load();
			                        AddDialog.close();
			                    }
			                }]
			            });
			            
			            
			            function AddReport() {
			                Ext.Ajax.request({
			                    method: "post",
			                    url: "<%=request.getContextPath()%>/tableManager/add.do",
			                    params: {json:JSON.stringify(AddForm.form.getValues())},
			                    callback: function (options, success, response) {
			                    	AddDialog.close();
			                        if (success) {
			                            var json = response.responseText;
			                            json = Ext.decode(json);
			                            ReportGridStore.load();
			                            Ext.Msg.alert("提示", json.info);
			                        }
			                        else {
			                            Ext.Msg.alert("提示", "系统繁忙");
			                        }
			                    }
			                });
			            }
			           
			            
			            function UpdateReport() {
			                var rows = ReportGrid.getView().getSelectionModel().getSelection();
			                if (typeof (rows[0]) == "undefined") {
			                    Ext.Msg.alert("提示", "请选择要操作的行！");
			                    return false;
			                }
			                Ext.Ajax.request({
			                    method: "post",
			                    url: "<%=request.getContextPath()%>/tableManager/edit.do",
			                    params: {json: JSON.stringify(AddForm.form.getValues()) },
			                    callback: function (options, success, response) {
			                    	AddDialog.close();
			                        if (success) {
			                            var json = response.responseText;
			                            json = Ext.decode(json);
			                            ReportGridStore.load();
			                            Ext.Msg.alert("提示", json.info);
			                        }
			                        else {
			                            Ext.Msg.alert("提示", "系统繁忙");
			                        }
			                    }
			                });
			            }
			            
			            function DelReport() {
			                var rows = ReportGrid.getView().getSelectionModel().getSelection();
			                if (typeof (rows[0]) == "undefined") {
			                    Ext.Msg.alert("提示", "请选择要操作的行！");
			                    return false;
			                }
			                Ext.Ajax.request({
			                    method: "post",
			                    url: "<%=request.getContextPath()%>/tableManager/delete.do",
			                    params: { id: rows[0].data.id },
			                    callback: function (options, success, response) {
			                    	AddDialog.close();
			                        if (success) {
			                            var json = response.responseText;
			                            json = Ext.decode(json);
			                            ReportGridStore.load();
			                            Ext.Msg.alert("提示", json.info);
			                        }
			                        else {
			                            Ext.Msg.alert("提示", "系统繁忙");
			                        }
			                    }
			                });
			            } 
			            
			            function findReport(){
			           		 ReportGridStore.load();
			            }
      		
        });
      
    </script>  
</head>  
<body>
</body>
</html>
