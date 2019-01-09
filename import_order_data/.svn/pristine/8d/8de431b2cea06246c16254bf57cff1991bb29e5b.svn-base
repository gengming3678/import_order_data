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
            var NodeTreeGridStore = Ext.create('Ext.data.TreeStore', {
                nodeParam:'parentId', 
                proxy: {
                    type: 'ajax',
                    url: '<%=request.getContextPath()%>/node/getJsonNodesByPId.do'
                },
                fields: ['text','id','leaf','parentId','url','imgType','xuHao','type','reportId']
            });

            var addOrUpdate;            
            var tbar = Ext.create("Ext.toolbar.Toolbar", {
                items: [
                    {
                        text: '添加同级',
                        iconCls: 'a_add',
                        handler: function () {
                            AddDialog.setTitle("添加同级");
                            AddDialog.setIconCls("a_add");
                            AddForm.form.reset();
                            Ext.getCmp("btnAdd").show();
                            Ext.getCmp("btnEdit").hide();
                            if (typeof (NodeTreeGrid) == "undefined") {
                                return false;
                            }
                            
                            var rows = NodeTreeGrid.getView().getSelectionModel().getSelection();
                            if (typeof (rows[0]) == "undefined") {
                                Ext.Msg.alert("提示", "请选择要操作的行！");
                                return false;
                            }
                            addOrUpdate=1;
                            AddDialog.show();
                        }
                    }, '-',
                     {
                         text: '添加下级',
                         iconCls: 'a_add',
                         handler: function () {
                             AddDialog.setTitle("添加下级");
                             AddDialog.setIconCls("a_add");
                             AddForm.form.reset();
                             Ext.getCmp("btnAdd").show();
                             Ext.getCmp("btnEdit").hide();
                             if (typeof (NodeTreeGrid) == "undefined") {
                                 return false;
                             }
                             var rows = NodeTreeGrid.getView().getSelectionModel().getSelection();
                             if (typeof (rows[0]) == "undefined") {
                                 Ext.Msg.alert("提示", "请选择要操作的行！");
                                 return false;
                             }
                             //AddForm.form.findField("url").setValue("/reportDisplay/list.do");
                             addOrUpdate=1;
                             AddDialog.show();
                         }
                     }, '-',
                    {
                        text: '修改',
                        iconCls: 'a_edit',
                        handler: function () {
                            AddDialog.setTitle("修改节点");
                            AddDialog.setIconCls("a_edit");
                            AddForm.form.reset();
                            Ext.getCmp("btnEdit").show();
                            Ext.getCmp("btnAdd").hide();
                            if (typeof (NodeTreeGrid) == "undefined") {
                                return false;
                            }
                            var rows = NodeTreeGrid.getView().getSelectionModel().getSelection();
                            if (typeof (rows[0]) == "undefined") {
                                Ext.Msg.alert("提示", "请选择要操作的行！");
                                return false;
                            }
                            addOrUpdate=2;
                            AddForm.form.setValues(rows[0].data);
                            AddDialog.show();
                        }
                    }, '-',
                    {
                        text: '删除',
                        iconCls: 'a_del',
                        handler: DelNode
                    }, '-',
                    {
                        text: '配置表格数据源',
                        iconCls: 'a_grid',
                        handler: function () {
                        	var rows = NodeTreeGrid.getView().getSelectionModel().getSelection();
                            if (typeof (rows[0]) == "undefined") {
                                Ext.Msg.alert("提示", "请选择要操作的行！");
                                return false;
                            }else if(rows[0].data.leaf == false){
                            	Ext.Msg.alert("提示", "请选择末级节点！");
                                return false;
                            }
                            
                            ConfigTableDialog.setTitle("配置表格报表数据源");
                            ConfigTableDialog.setIconCls("a_grid");
                            ReportGridStore.load();
                            ConfigTableDialog.show();
                        }
                    }
                ]
            });

            
            var NodeTreeGrid = Ext.create('Ext.tree.Panel', {
                tbar: tbar,
                useArrows: false,
                rootVisible: false,
                autoHeight : true,
        		autoScroll:true,
                border: 0,
                store: NodeTreeGridStore,
                multiSelect: false,
                singleExpand: true,
                renderTo: Ext.getBody(),
                root:{ 
                    text:'根节点', 
                    expanded : true,
                    id:'0',
                    parentId:'0'
			 	},
			 	columns: [{  
                    xtype: 'treecolumn',
                    text: '节点名称', 
                    flex: 2,
                    sortable: false,
            		menuDisabled:true,
                    dataIndex: 'text' 
                },{
                    text: '类别',
                    flex: 1,
                    dataIndex: 'type',
                    sortable: false,
            		menuDisabled:true,
            		renderer:function(value, cellmeta, record, rowIndex, columnIndex, store){
	                    if(value==1){
	                   		 return "表格报表";
	                    }else if(value==2){
	                     	return  "图形报表";
	                    }else if(value==3){
	                     	return "其它";
	                    }
					}
                }, {
                    text: '节点地址',
                    flex: 1,
                    dataIndex: 'url',
                    sortable: false,
            		menuDisabled:true
                },/*  {
                    text: '图标类型',
                    flex: 1,
                    dataIndex: 'imgType',
                    sortable: false,
            		menuDisabled:true
                }, */ {
                    text: '末级节点',
                    flex: 1,
                    dataIndex: 'leaf',
                    sortable: false,
            		menuDisabled:true,
                    renderer: function (value, meta, record) {
			                    if(value==true) {
			                    	 return "是"; 
			                    }else{
			                    	 return "否"; 
			                    }
					} 	
                }, {
                    text: '排序序号 ',
                    flex: 1,
                    dataIndex: 'xuHao',
                    sortable: false,
            		menuDisabled:true
                }] 
                , listeners: {
                    itemclick: function (record, node) {

                    }
                }
            });          

            var AddForm = Ext.create("Ext.form.Panel", {
                border: false,
                fieldDefaults: {
                    msgTarget: 'side', 
                    labelWidth: 105,
                    align: "right",
                    regexText: '格式错误',
                    allowBlank: false 
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
                         fieldLabel: '节点名称',
                         id: 'text',
                         name: 'text',
                         allowBlank: false
                     },{
                         fieldLabel: '类别',
                         xtype: 'radiogroup',
                         margin:'10 0 0 15',
                         items: [
				                {boxLabel: '表格报表', name: 'type', inputValue: 1},
				                {boxLabel: '图形报表', name: 'type', inputValue: 2},
				                {boxLabel: '其它',  name: 'type', inputValue: 3}
			                ],
			             listeners:{
				            change: function(g , newValue , oldValue){
				                if(newValue.type==1){
				                	AddForm.form.findField("url").setValue("/tableDisplay/list.do");
				                }else if(newValue.type==2){
				                	AddForm.form.findField("url").setValue("/chartDisplay/list.do");
				                }/* else if(newValue.type==3){
				                	AddForm.form.findField("url").setValue("");
				                } */
				            }
				        }
			          },{
                          fieldLabel: '节点地址',
                          id: 'url',
                          name: 'url',
                          //hidden :true, //隐藏
                          allowBlank: true
                      },
                     /*  {
                          fieldLabel: '图标类型',
                          id: 'imgType',
                          name: 'imgType',
                          allowBlank: true
                      }, */
                      {
                      	  xtype: "checkbox",
                          fieldLabel: '末级节点',
                          inputValue:true,
                          id: 'leaf',
                          name: 'leaf'
                      },
                      {
                          fieldLabel: '排序序号',
                          id: 'xuHao',
                          name: 'xuHao'
                      }
                ]
            });
            
            var AddDialog = Ext.create("Ext.window.Window", {
                iconCls: 'a_add',
                width:450,
			    height:297,
                closeAction: 'close',       
                resizable: false,
                autoScroll: true,
                closable: true,          
                modal: true,              
                items: AddForm,
                buttons: [{
                    text: '添加',
                    id: "btnAdd",
                    handler: function () {
                        if (AddForm.form.isValid()) {
                            if (AddDialog.title == '添加同级') {
                                //O为同级，1为下级
                                AddNode(0);
                            }
                            else {
                                AddNode(1);
                            }
                        }
                    }
                },
                {
                    text: '修改',
                    id: "btnEdit",
                    handler: EditNode
                },
                {
                    id: "btnCancel",
                    text: '关闭',
                    handler: function () {
                        AddDialog.close();
                    }
                }]
            });
            
            var ReportGridStore = Ext.create('Ext.data.Store', {
                proxy: {
                    type: 'ajax',
                    url: '<%=request.getContextPath()%>/tableManager/getJsonReports.do',
                    method:'post',
                    reader: {   
                        type: 'json',
                        idProperty : 'nodeId'
                    }
                },
                fields: ['id','nodeName','nodeId','colName','sqlContent','text','ifProcedure','queryCondition'] 
            });
            
            ReportGridStore.on('beforeload', function (ReportGridStore, options) {  
     			var new_params = {nodeName:null};
		        Ext.apply(ReportGridStore.proxy.extraParams, new_params);   
		    });
		    
		    ReportGridStore.on('load',function(ReportGridStore, records, successful, eOpts){ 
				    var rows = NodeTreeGrid.getView().getSelectionModel().getSelection();
		            var reportId = rows[0].data.reportId; 
				    var selectRow=-1;  
				    for(var i=0;i<records.length;i++){  
				      var record = records[i];  
				      var id = record.get('id');
				      if(reportId==id){  
				        selectRow=i;
				        break;  
				      }  
				    }
				    if(selectRow!=-1){
				    	ReportGrid.getView().getSelectionModel().selectRange(selectRow,selectRow);
				    }  
  			});  
		    
            var ReportGrid = Ext.create('Ext.grid.Panel', {
                border: 0,
                store: ReportGridStore,
 				height:385,
        		autoScroll:true,
                multiSelect: false,
                selModel: {selType: 'checkboxmodel'},
             
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
                }] 
               
            	});
            	      
		    
		     
            var ConfigTableDialog = Ext.create("Ext.window.Window", {
                iconCls: 'a_grid',
                width:750,
			    height:450,
                closeAction: 'close',       
                resizable: false,
                autoScroll: true,
                closable: true,          
                modal: true,              
                items: [ReportGrid],
                buttons: [{
                    text: '保存',
                    id: "btnAddTable",
                    handler: UpdateNodeReportId
                },
                {
                    id: "btnCancelTable",
                    text: '关闭',
                    handler: function () {
                        ConfigTableDialog.close();
                    }
                }]
            });
            
            function AddNode(type) {
                var rows = NodeTreeGrid.getView().getSelectionModel().getSelection();
                var id = 0;
                var leaf = false;
                //同级
                if (type == 0) {
                    id = rows[0].data.parentId;
                }//下级
                else {
                    id = rows[0].data.id;
                }
                Ext.Ajax.request({
                    method: "post",
                    url: "<%=request.getContextPath()%>/node/add.do",
                    params: {json:JSON.stringify(AddForm.form.getValues()), id: id },
                    callback: function (options, success, response) {
                    	AddDialog.close();
                        if (success) {
                            var json = response.responseText;
                            json = Ext.decode(json);
                            NodeTreeGridStore.load();
                            Ext.Msg.alert("提示", json.info);
                        }
                        else {
                            Ext.Msg.alert("提示", "系统繁忙");
                        }
                    }
                });
            }
           
            function EditNode() {
                var rows = NodeTreeGrid.getView().getSelectionModel().getSelection();
                if (typeof (rows[0]) == "undefined") {
                    Ext.Msg.alert("提示", "请选择要操作的行！");
                    return false;
                }
                 Ext.Ajax.request({
                    method: "post",
                    url: "<%=request.getContextPath()%>/node/edit.do",
                    params: {json: JSON.stringify(AddForm.form.getValues()), id: rows[0].data.id },
                    callback: function (options, success, response) {
                    	AddDialog.close();
                        if (success) {
                            var json = response.responseText;
                            json = Ext.decode(json);
                            NodeTreeGridStore.load();
                            Ext.Msg.alert("提示", json.info);
                        }
                        else {
                            Ext.Msg.alert("提示", "系统繁忙");
                        }
                    }
                }); 
            }
            
            function DelNode() {
                var rows = NodeTreeGrid.getView().getSelectionModel().getSelection();
                if (typeof (rows[0]) == "undefined") {
                    Ext.Msg.alert("提示", "请选择要操作的行！");
                    return false;
                }
                Ext.Ajax.request({
                    method: "post",
                    url: "<%=request.getContextPath()%>/node/delete.do",
                    params: { id: rows[0].data.id },
                    callback: function (options, success, response) {
                    	AddDialog.close();
                        if (success) {
                            var json = response.responseText;
                            json = Ext.decode(json);
                            NodeTreeGridStore.load();
                            Ext.Msg.alert("提示", json.info);
                        }
                        else {
                            Ext.Msg.alert("提示", "系统繁忙");
                        }
                    }
                });
            }
            
            function UpdateNodeReportId(){
         			 var rowsReport = ReportGrid.getView().getSelectionModel().getSelection();
                     if (typeof (rowsReport[0]) == "undefined") {
                         Ext.Msg.alert("提示", "请选择要保存的数据源！");
                         return false;
                     }
                     var rows = NodeTreeGrid.getView().getSelectionModel().getSelection();
	                 if (typeof (rows[0]) == "undefined") {
	                    Ext.Msg.alert("提示", "请选择要操作的行！");
	                    return false;
	                 }
                     var reportId = rowsReport[0].data.id;
                     Ext.Ajax.request({
			                    method: "post",
			                    url: "<%=request.getContextPath()%>/node/updateNodeReportIdById.do",
			                    params: {reportId:reportId,id: rows[0].data.id},
			                    callback: function (options, success, response) {
			                    	ConfigTableDialog.close();
			                        if (success) {
			                            var json = response.responseText;
			                            json = Ext.decode(json);
			                            NodeTreeGridStore.load();
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
