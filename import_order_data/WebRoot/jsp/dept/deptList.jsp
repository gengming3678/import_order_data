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

 		//定义列  
		    var columns = [  
		        {header:'参数名称',dataIndex:'parameterName',flex:1,  
		            editor:{  
		                allowBlank:false 
		            }
		        }, //sortable:true 可设置是否为该列进行排序  
		        {header:'参数值',dataIndex:'value',flex:1,    
		                editor:{  
		                    allowBlank:true  
		                }
		         }  
		      ];  
	   
	    
    var store = Ext.create('Ext.data.Store', {
                proxy: {
                    type: 'ajax',
                    url: '<%=request.getContextPath()%>/sysParam/getJsonSysParamsAll.do',
                    //method:'post',
                    reader: {   //这里的reader为数据存储组织的地方
                        type: 'json'
                    }
                },
                fields: ['id','parameterName','description','value','deptOverride'],
                folderSort: true,
                autoLoad: true  //即时加载数据
            });
            
        store.on('beforeload', function (store, options) {  
	        //var new_params = {findName:Ext.getCmp('findName').value};  
	          // Ext.apply(store.proxy.extraParams, new_params);  
	    });
    
  var toolbar = Ext.create("Ext.toolbar.Toolbar", {
                items: [
                 {
                        text: '<font color="red">双击单元格即可编辑</font>'
                        }]
                });
	    //创建表格  
	    var grid = new Ext.grid.GridPanel({  
	        autoHeight : true,
        	autoScroll:true,
	        margin:'1',
	        store:store,  
	        tbar: toolbar,
	        columns:columns, //显示列  
	        stripeRows:true, //斑马线效果  
	        //selModel: { selType: 'checkboxmodel' },
	        listeners : {
			    beforeedit : function(editor, e) {
			        if(e.colIdx== 1 ){//设置不可编辑
			            return true;
			        }else{
			            return false;
			        }
			    }
			},  
	        plugins:[  
	                 Ext.create('Ext.grid.plugin.CellEditing',{  
	                     clicksToEdit:2 //设置单击单元格编辑  
	                 })  
	        ]
	    });  
            //***表格显示内容*** 
            var tbar = Ext.create("Ext.toolbar.Toolbar", {
                items: [
                    /* {
                        text: '添加科室',
                        iconCls: 'a_add',
                        handler: function () {
                            AddDialog.setTitle("添加科室");
                            AddDialog.setIconCls("a_add");
                            AddForm.form.reset();
                            Ext.getCmp("btnAdd").show();
                            Ext.getCmp("btnEdit").hide();
                            if (typeof (DeptGrid) == "undefined") {
                                return false;
                            }
                            AddDialog.show();
                        }
                    }, '-',
                    {
                        text: '修改',
                        iconCls: 'a_edit',
                        handler: function () {
                            AddDialog.setTitle("修改科室");
                            AddDialog.setIconCls("a_edit");
                            AddForm.form.reset();
                            Ext.getCmp("btnEdit").show();
                            Ext.getCmp("btnAdd").hide();
                            if (typeof (DeptGrid) == "undefined") {
                                return false;
                            }
                            var rows = DeptGrid.getView().getSelectionModel().getSelection();
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
                        handler: DelDept
                    }, */
                    {
                        text: '科室系数维护',
                        iconCls: 'a_role',
                        handler: function () {
                            AddTreeDialog.setTitle("添加科室系数");
                            AddTreeDialog.setIconCls("a_role");
                            if (typeof (DeptGrid) == "undefined") {
                                return false;
                            }
                            var rows = DeptGrid.getView().getSelectionModel().getSelection();
                            if (typeof (rows[0]) == "undefined") {
                                Ext.Msg.alert("提示", "请选择要操作的行！");
                                return false;
                            }
                           
                             //加载数据  
	    					store.load();
                            AddTreeDialog.show();
                        }
                    },"-","->",
                    {
                        emptyText: '科室名称/科室编码',
                        xtype:'textfield',
                        id: 'findName',
                        name: 'findName'
			       },
             		{ 
	             		iconCls:"a_search",
	             		text:"搜索",
	             		handler:findDept
             		}
                ]
            });
          


            var DeptGridStore = Ext.create('Ext.data.Store', {
                pageSize: ${pageSize},
                proxy: {
                    type: 'ajax',
                    url: '<%=request.getContextPath()%>/dept/getJsonDepts.do',
                    method:'post',
                    reader: {   //这里的reader为数据存储组织的地方
                        type: 'json', //返回数据类型为json格式
                        root: 'rows',  //数据
                        totalProperty: 'total' //数据总条数
                    }
                },
                fields: ['id','name','code','type','paramName','value'],
                folderSort: true,
                autoLoad: true  //即时加载数据
            });
            
            DeptGridStore.on('beforeload', function (DeptGridStore, options) {  
		        var new_params = {findName:Ext.getCmp('findName').value};  
		           Ext.apply(DeptGridStore.proxy.extraParams, new_params);  
		         //alert('beforeload');  
		    });
            var DeptGrid = Ext.create('Ext.grid.Panel', {
                tbar: tbar,
                border: 0,
                autoHeight : true,
        		autoScroll:true,
                store: DeptGridStore,
                multiSelect: false,
                selModel: { selType: 'checkboxmodel' },
                renderTo: Ext.getBody(),
			 	columns: [{  
                    text: '科室名称',  //显示的表头列名称
                    flex: 1,
                    dataIndex: 'name',  //对应store的列字段名称
                    sortable: false,
                    menuDisabled:true,
		            renderer:function(value, cellmeta, record, rowIndex, columnIndex, store){
		                return value+"-"+record.data["type"];
					}
                },{  
                    text: '科室编码',  //显示的表头列名称
                    flex: 1,
                    dataIndex: 'code',  //对应store的列字段名称
                    sortable: false,
                    menuDisabled:true
                },{  
                    text: '系数名字',  //显示的表头列名称
                    flex: 1,
                    dataIndex: 'paramName',  //对应store的列字段名称
                    sortable: false,
                    menuDisabled:true
                },{  
                    text: '系数值',  //显示的表头列名称
                    flex: 1,
                    dataIndex: 'value',  //对应store的列字段名称
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
	                store: DeptGridStore,
	                displayMsg: '显示 {0} - {1} 条，共计 {2} 条',
	                emptyMsg: "没有数据",
	                beforePageText: "当前页",
	                afterPageText: "共{0}页",
	                displayInfo: true                 
            	}]
            });          

	            var AddTreeDialog = Ext.create("Ext.window.Window", {
	                iconCls: 'a_add',
	                closeAction: 'close',        //窗口关闭的方式：hide/close
	                resizable: false,
	                closable: true,            //是否可以关闭
	                modal: true,  
	                height:400,
	                width:600,              //是否为模态窗口
	                items: [grid],
	                buttons: [
	                {
	                    text: '保存',
	                    iconCls: 'a_save',  //样式
	                    id: "btnEditTree",
	                    handler: saveDeptParam
	                },
	                {
	                    id: "btnCancelTree",
	                    iconCls: 'a_cross',  //样式
	                    text: '关闭',
	                    handler: function () {
	                        //重置AddForm.form表单
	                        AddTreeDialog.close();
	                    }
	                }]
	            });

			//****工具栏操作内容****
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
			                         fieldLabel: '科室名称',
			                         id: 'name',
			                         name: 'name',
			                         allowBlank: false
			                     },
			                     {
			                         fieldLabel: '科室编码',
			                         id: 'code',
			                         name: 'code',
			                         allowBlank: false
			                     }/*,
			                     {
			                         fieldLabel: '所属院',
			                         id: 'type',
			                         name: 'type',
			                         allowBlank: false
			                     }*/
			                ]
			            });
			            
			      var AddDialog = Ext.create("Ext.window.Window", {
			                iconCls: 'a_add',
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
			                                AddDept();
			                        }
			                    }
			                },
			                {
			                    text: '修改',
			                    iconCls: 'a_save',  //样式
			                    id: "btnEdit",
			                    handler: EditDept
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
			            function AddDept() {
			                Ext.Ajax.request({
			                    method: "post",
			                    url: "<%=request.getContextPath()%>/dept/add.do",
			                    params: {json:JSON.stringify(AddForm.form.getValues())},
			                    callback: function (options, success, response) {
			                    	AddDialog.close();
			                        if (success) {
			                            var json = response.responseText;
			                            json = Ext.decode(json);
			                            DeptGridStore.load();
			                            Ext.Msg.alert("提示", json.info);
			                        }
			                        else {
			                            Ext.Msg.alert("提示", "系统繁忙");
			                        }
			                    }
			                });
			            }
			           
			            //2 修改
			            function EditDept() {
			                var rows = DeptGrid.getView().getSelectionModel().getSelection();
			                if (typeof (rows[0]) == "undefined") {
			                    Ext.Msg.alert("提示", "请选择要操作的行！");
			                    return false;
			                }
			                Ext.Ajax.request({
			                    method: "post",
			                    url: "<%=request.getContextPath()%>/dept/edit.do",
			                    params: {json: JSON.stringify(AddForm.form.getValues()) },
			                    callback: function (options, success, response) {
			                    	AddDialog.close();
			                        if (success) {
			                            var json = response.responseText;
			                            json = Ext.decode(json);
			                            DeptGridStore.load();
			                            Ext.Msg.alert("提示", json.info);
			                        }
			                        else {
			                            Ext.Msg.alert("提示", "系统繁忙");
			                        }
			                    }
			                });
			            }
			            
			             //3 删除
			            function DelDept() {
			                var rows = DeptGrid.getView().getSelectionModel().getSelection();
			                if (typeof (rows[0]) == "undefined") {
			                    Ext.Msg.alert("提示", "请选择要操作的行！");
			                    return false;
			                }
			                Ext.Ajax.request({
			                    method: "post",
			                    url: "<%=request.getContextPath()%>/dept/delete.do",
			                    params: { id: rows[0].data.id },
			                    callback: function (options, success, response) {
			                    	AddDialog.close();
			                        if (success) {
			                            var json = response.responseText;
			                            json = Ext.decode(json);
			                            DeptGridStore.load();
			                            Ext.Msg.alert("提示", json.info);
			                        }
			                        else {
			                            Ext.Msg.alert("提示", "系统繁忙");
			                        }
			                    }
			                });
			            } 
			            
			            //4查询
			            function findDept(){
			           		 DeptGridStore.load();
			            }
			            
			            
          	
	            
	            //2 修改
	            function saveDeptParam() {
	                var rows = DeptGrid.getView().getSelectionModel().getSelection();
	               
	                if (typeof (rows[0]) == "undefined") {
	                    Ext.Msg.alert("提示", "请选择要操作的行！");
	                    return false;
	                }
	                
	                 var m = store.getModifiedRecords().slice(0);  
		                var jsonArray = [];  
		                Ext.each(m,function(item){  
		                    jsonArray.push(item.data);  
		                });
		             
		                Ext.Ajax.request({  
		                    method:'POST',  
		                    url: "<%=request.getContextPath()%>/dept/saveDeptParam.do",
		                    params:{json:JSON.stringify(jsonArray),deptId:rows[0].data.id},    
		                    success:function(response){
		                    	DeptGridStore.load();
		                    var json = response.responseText;
			                    json = Ext.decode(json);  
		                        Ext.Msg.alert('系统提示',json.info,function(){  
		                            store.load();  
		                        });  
		                    },  
		                    failure:function(){  
		                        Ext.Msg.alert("错误","操作失败!");  
		                    }
		                    
		                });  
	                
	            }
	            
	            
	           
			            
      		
        });
      
    </script>  
</head>  
<body>
</body>
</html>
