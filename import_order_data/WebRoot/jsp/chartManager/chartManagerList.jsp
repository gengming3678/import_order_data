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

		
			//菜单树
			var treeStore_ = Ext.create('Ext.data.TreeStore', {
			        nodeParam:'parentId', 
	                proxy: {
	                    type: 'ajax',
	                    url: '<%=request.getContextPath()%>/node/getCheckNodesByNodeReportId.do'
	                },
	                fields: ['text','id','leaf','parentId','url','checked','expanded'],
	                root:{ 
	                    text:'根节点', 
	                    expanded : true,//根节点是否展开 
	                    id:0,
	                    parentId:0
	                  }/* ,
	                autoLoad:true */
			    });
			
				/*  treeStore_.on('beforeload', function (treeStore_, options) {  
					//var rows = RoleGrid.getView().getSelectionModel().getSelection();
					//alert(rows[0].data.id);
			        //var new_params = {roleId:null};  
			            Ext.apply(treeStore_.proxy.extraParams, new_params);  
			         //alert('beforeload');  
			    });  */


			var ComboTreeBox = Ext.create('ComboTreeBox',{
 				fieldLabel : '所属节点',
 				id: 'nodeIds',
			    name: 'nodeIds',
			    labelWidth: 100,
 				margin:'10 0 0 10',
 				hiddenName:'id',
 				width: 500 ,
 				displayField: "text",
                valueField: "id",
 				store : treeStore_,
 				queryMode: 'remote'
 			});
 			
 		 
            //***工具栏内容*** 
            var tbar = Ext.create("Ext.toolbar.Toolbar", {
                items: [
                    {
                        text: '添加',
                        iconCls: 'a_add',
                        handler: function () {
                            AddDialog.setTitle("添加");
                            AddDialog.setIconCls("a_add");
                            if (typeof (ChartGrid) == "undefined") {
                                return false;
                            }
                            if(Ext.getCmp("nodeIds")!=null){
                            	Ext.getCmp("nodeIds").show();
                            	Ext.getCmp("nodeName").hide(); 
                            }
                            treeStore_.load();
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
                            if (typeof (ChartGrid) == "undefined") {
                                return false;
                            }
                            AddForm.form.reset();
                            var rows = ChartGrid.getView().getSelectionModel().getSelection();
                            if (typeof (rows[0]) == "undefined") {
                                Ext.Msg.alert("提示", "请选择要操作的行！");
                                return false;
                            }
                            
                            
                            
                            var nodeName = rows[0].data.nodeName;
                            var nodeId = rows[0].data.nodeId;
                            var id = rows[0].data.id;
                            var specialLine = rows[0].data.specialLine;
                            var sqlContent = rows[0].data.sqlContent;
                            var type = rows[0].data.type;
                            
                            var ifProcedure = rows[0].data.ifProcedure;
                            var queryCondition = rows[0].data.queryCondition;
                            
                            
                            //alert(Ext.getCmp("nodeId"));
                            var ss= Ext.getCmp("nodeIds");
                            if(Ext.getCmp("nodeIds")!=null){
                            	Ext.getCmp("nodeIds").setRawValue(nodeId);
                            	Ext.getCmp("nodeName").setRawValue(nodeName); 
                            	Ext.getCmp("nodeIds").setDisabled(true); 
                            	Ext.getCmp("nodeIds").hide();
                            	Ext.getCmp("nodeName").show();
                            	
                            }
                            AddForm.form.findField("id").setValue(id);
                            AddForm.form.findField("specialLine").setValue(specialLine);
                            AddForm.form.findField("sqlContent").setValue(sqlContent);
                            AddForm.form.findField("type").setValue(type);
                            
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
                        handler: DelChartManager
                    },"-","->",
                    {
                        emptyText: '所属节点',
                        xtype:'textfield',
                        id: 'findName',
                        name: 'findName'
			       },
             		{ 
	             		iconCls:"a_search",
	             		text:"搜索",
	             		handler:findChartManager
             		}
                ]
            });
          	//***工具栏内容结束*** 
          	
          	/**
          		数据表格内容开始
          	*/
            var ChartGridStore = Ext.create('Ext.data.Store', {
                proxy: {
                    type: 'ajax',
                    url: '<%=request.getContextPath()%>/chartManager/getJsonChartManagers.do',
                    method:'post',
                    reader: {   //这里的reader为数据存储组织的地方
                        type: 'json',
                        idProperty : 'nodeId'
                    }
                },
                fields: ['id','nodeName','nodeId','specialLine','sqlContent','type','ifProcedure','queryCondition'],
                autoLoad: true  //即时加载数据
            });
            
            ChartGridStore.on('beforeload', function (ChartGridStore, options) {  
		        var new_params = {nodeName:Ext.getCmp('findName').value};
		        Ext.apply(ChartGridStore.proxy.extraParams, new_params);   
		    });
		    
            var ChartGrid = Ext.create('Ext.grid.Panel', {
                tbar: tbar,
                border: 0,
                store: ChartGridStore,
                autoHeight : true,
        		autoScroll:true,
                multiSelect: false,
                selModel: { selType: 'checkboxmodel' },
                renderTo: Ext.getBody(),
			 	columns: [{  
                    text: '所属节点',  //显示的表头列名称
                    flex: 1,
                    dataIndex: 'nodeName',  //对应store的列字段名称
                    menuDisabled:true,
	 				sortable: false
                },{  
                    text: '图形类别',  //显示的表头列名称
                    flex: 1,
                    dataIndex: 'type',  //对应store的列字段名称
                    menuDisabled:true,
	 				sortable: false,
	 				renderer:function(value, cellmeta, record, rowIndex, columnIndex, store){
	                    if(value==1){
	                   		 return "折线图";
	                    }else if(value==2){
	                     	return  "条形图";
	                    }else if(value==3){
	                     	return "饼图";
	                    }
					}
                },{  
                    text: '图形特殊行',  //显示的表头列名称
                    flex: 1,
                    dataIndex: 'specialLine',  //对应store的列字段名称
                    menuDisabled:true,
	 				sortable: false
                },{  
                    text: '数据来源',  //显示的表头列名称
                    flex: 2,
                    dataIndex: 'sqlContent',  //对应store的列字段名称
                    menuDisabled:true,
	 				sortable: false
                },{  
                    text: '是否存储过程',  //显示的表头列名称
                    flex: 1,
                    dataIndex: 'ifProcedure',  //对应store的列字段名称
                    menuDisabled:true,
	 				sortable: false
                },{  
                    text: '查询条件',  //显示的表头列名称
                    flex: 1,
                    dataIndex: 'queryCondition',  //对应store的列字段名称
                    menuDisabled:true,
	 				sortable: false
                }] 
                , listeners: {
                    //点击行触发事件
                    itemclick: function (record, node) {
                        //把列的qcid传递给QuesGridStore刷新对应的grid或tree
                      //  QuesGridStore.load({ params: { csId: node.data.id } });
                    }
                }
            });          
			/**
          	  数据表格内容结束
          	*/

 
			var AddForm = Ext.create("Ext.form.Panel", {
			                border: false,
			                id:'addReportForm',
			                name:'addReportForm',
			                fieldDefaults: {
			                    msgTarget: 'side',  //提示信息在右旁边显示图标
			                    labelWidth: 100,
			                    align: "right",
			                    regexText: '格式错误', //错误提示
			                    allowBlank: false //不与许为空
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
			                     ComboTreeBox,{
			                         fieldLabel: '所属节点',
			                         id: 'nodeName',
			                         name: 'nodeName',
			                         readOnly:true,
			                         //disabled:true,
			                         allowBlank: false
			                     },{
			                         fieldLabel: '图形类别',
			                         xtype: 'radiogroup',
			                         allowBlank: false,
			                         margin:'10 0 0 10',
			                         items: [
							                {boxLabel: '折线图', name: 'type', inputValue: 1},
							                {boxLabel: '条形图', name: 'type', inputValue: 2},
							                {boxLabel: '饼图',  name: 'type', inputValue: 3}
						                ]
			                     },{
			                         fieldLabel: '图形特殊行',
			                         id: 'specialLine',
			                         name: 'specialLine',
			                         allowBlank: false
			                     },{
			                         fieldLabel: '数据来源',
			                         xtype     : 'textareafield',
			                         height : 150,
			                         width : 500,
			                         id: 'sqlContent',
			                         name: 'sqlContent',
			                         allowBlank: false
			                     },{
			                         fieldLabel: '是否存储过程',
			                         xtype: "checkbox",
			                         id: 'ifProcedure',
			                         name: 'ifProcedure',
			                         inputValue:true,
			                         allowBlank: false
			                     }, { 
						            fieldLabel : '查询条件参数',
						            allowBlank : false,
						            id: 'queryCondition',
			                        name: 'queryCondition'
            					}
			                ]
			            });
			            
			      var AddDialog = Ext.create("Ext.window.Window", {
			                iconCls: 'a_add',
			                closeAction: 'close',        //窗口关闭的方式：hide/close
			                resizable: false,
			                closable: true,            //是否可以关闭
			                modal: true,
			                id:'addReportDialog',
			                name:'addReportDialog',                //是否为模态窗口
			                items: AddForm,
			                buttons: [{
			                    text: '添加',
			                    iconCls: 'a_save',
			                    id: "btnAdd",
			                    handler: function () {
			                                AddChartManager();   
			                    }
			                },{
			                    text: '保存',
			                    iconCls: 'a_save',
			                    id: "btnUpdate",
			                    handler: function () {
			                        //if (AddForm.form.isValid()) {
			                                UpdateChartManager();
			                       // }
			                    }
			                },
			                {
			                    id: "btnCancel",
			                    iconCls: 'a_cross',  //样式
			                    text: '关闭',
			                    handler: function () {
			                        //重置AddForm.form表单
			                        ChartGridStore.load();
			                        AddDialog.close();
			                    }
			                }]
			            });
			            
			            
			            
			           
			
			            //1 添加
			            function AddChartManager() {
			           // var nodeIds = Ext.getCmp("nodeIds").getValue();
			            //alert(nodeIds);
			                Ext.Ajax.request({
			                    method: "post",
			                    url: "<%=request.getContextPath()%>/chartManager/add.do",
			                    params: {json:JSON.stringify(AddForm.form.getValues())},
			                    callback: function (options, success, response) {
			                    	AddDialog.close();
			                        if (success) {
			                            var json = response.responseText;
			                            json = Ext.decode(json);
			                            ChartGridStore.load();
			                            Ext.Msg.alert("提示", json.info);
			                        }
			                        else {
			                            Ext.Msg.alert("提示", "系统繁忙");
			                        }
			                    }
			                });
			            }
			           
			            
			            
			            
			            //2 修改
			            function UpdateChartManager() {
			           // alert(JSON.stringify(AddForm.form.getValues()));
			                var rows = ChartGrid.getView().getSelectionModel().getSelection();
			                if (typeof (rows[0]) == "undefined") {
			                    Ext.Msg.alert("提示", "请选择要操作的行！");
			                    return false;
			                }
			                Ext.Ajax.request({
			                    method: "post",
			                    url: "<%=request.getContextPath()%>/chartManager/edit.do",
			                    params: {json: JSON.stringify(AddForm.form.getValues()) },
			                    callback: function (options, success, response) {
			                    	AddDialog.close();
			                        if (success) {
			                            var json = response.responseText;
			                            json = Ext.decode(json);
			                            ChartGridStore.load();
			                            Ext.Msg.alert("提示", json.info);
			                        }
			                        else {
			                            Ext.Msg.alert("提示", "系统繁忙");
			                        }
			                    }
			                });
			            }
			            
			             //3 删除
			            function DelChartManager() {
			                var rows = ChartGrid.getView().getSelectionModel().getSelection();
			                if (typeof (rows[0]) == "undefined") {
			                    Ext.Msg.alert("提示", "请选择要操作的行！");
			                    return false;
			                }
			                Ext.Ajax.request({
			                    method: "post",
			                    url: "<%=request.getContextPath()%>/chartManager/delete.do",
			                    params: { id: rows[0].data.id },
			                    callback: function (options, success, response) {
			                    	AddDialog.close();
			                        if (success) {
			                            var json = response.responseText;
			                            json = Ext.decode(json);
			                            ChartGridStore.load();
			                            Ext.Msg.alert("提示", json.info);
			                        }
			                        else {
			                            Ext.Msg.alert("提示", "系统繁忙");
			                        }
			                    }
			                });
			            } 
			            
			            //4查询
			            function findChartManager(){
			           		 ChartGridStore.load();
			            }
      		
        });
      
    </script>  
</head>  
<body>
</body>
</html>
