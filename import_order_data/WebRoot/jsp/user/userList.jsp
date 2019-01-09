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
                    {
                        text: '添加用户',
                        iconCls: 'a_add',
                        handler: function () {
                            AddDialog.setTitle("添加用户");
                            AddDialog.setIconCls("a_add");
                            AddForm.form.reset();
                            Ext.getCmp("btnAdd").show();
                            Ext.getCmp("btnEdit").hide();
                            if (typeof (UserGrid) == "undefined") {
                                return false;
                            }
                            AddDialog.show();
                        }
                    }, '-',
                    {
                        text: '修改',
                        iconCls: 'a_edit',
                        handler: function () {
                            AddDialog.setTitle("修改用户");
                            AddDialog.setIconCls("a_edit");
                            AddForm.form.reset();
                            Ext.getCmp("btnEdit").show();
                            Ext.getCmp("btnAdd").hide();
                            if (typeof (UserGrid) == "undefined") {
                                return false;
                            }
                            var rows = UserGrid.getView().getSelectionModel().getSelection();
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
                        handler: DelUser
                    },
                    
                    "-",
                    {
                        text: '人员角色维护',
                        iconCls: 'a_role',
                        handler: function () {
                            AddTreeDialog.setTitle("添加人员角色");
                            AddTreeDialog.setIconCls("a_role");
                            if (typeof (UserGrid) == "undefined") {
                                return false;
                            }
                            var rows = UserGrid.getView().getSelectionModel().getSelection();
                            if (typeof (rows[0]) == "undefined") {
                                Ext.Msg.alert("提示", "请选择要操作的行！");
                                return false;
                            }
                            StoreTreeRole.load();
                            AddTreeDialog.show();
                        }
                    },
                    "->",
                    {
                        emptyText: '登录名/真实姓名',
                        xtype:'textfield',
                        id: 'findName',
                        name: 'findName'
			       },
             		{ 
	             		iconCls:"a_search",
	             		text:"搜索",
	             		handler:function (){
	             			UserGridStore.load();
	             		}
             		}
                ]
            });
          
          
          	//权限树
			var StoreTreeRole = Ext.create('Ext.data.TreeStore', {
			        nodeParam:'parentId', 
	                proxy: {
	                    type: 'ajax',
	                    url: '<%=request.getContextPath()%>/user/getUserSelectRolesByUserId.do'
	                },
	                fields: ['id','text','checked','expanded','leaf'],
	                root:{ 
	                    text:'根节点', 
	                    expanded : true,//根节点是否展开 
	                    id:0,
	                    parentId:0
	                  },
	                autoLoad:true
			    });
			
				StoreTreeRole.on('beforeload', function (StoreTreeRole, options) {  
					var rows = UserGrid.getView().getSelectionModel().getSelection();
			        var new_params = {userId:rows[0].data.id};  
			            Ext.apply(StoreTreeRole.proxy.extraParams, new_params);    
			    });
			    var TreeRolePanel = Ext.create('Ext.tree.Panel', {
			        store: StoreTreeRole,
			        rootVisible: false,
			        width: 500,
			        height: 400,
			        //添加监听事件
				  	listeners: {
					    checkchange: function(node, state) { 
					    //如果被勾选的节点有子节点，则将其子节点全部改为根节点状态
					      if (node.hasChildNodes()) {
					        for (var j = 0; j < node.childNodes.length; j++) {
					            node.childNodes[j].set('checked', state);
					        }
					      }
					    }
				    }
			    });
			    
			    var AddTreeDialog = Ext.create("Ext.window.Window", {
			                iconCls: 'a_add',
			                width:520,
			                height:440,
			                closeAction: 'close',        //窗口关闭的方式：hide/close
			                resizable: false,
			                closable: true,            //是否可以关闭
			                modal: true,                //是否为模态窗口
			                items: TreeRolePanel,
			                buttons: [
			                {
			                    text: '保存',
			                    iconCls: 'a_save',  //样式
			                    id: "btnEditTree",
			                    handler: saveRoleUser
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


            var UserGridStore = Ext.create('Ext.data.Store', {
                pageSize: 15,
                proxy: {
                    type: 'ajax',
                    url: '<%=request.getContextPath()%>/user/getJsonUsers.do',
                    reader: {   //这里的reader为数据存储组织的地方
                        type: 'json', //返回数据类型为json格式
                        root: 'rows',  //数据
                        totalProperty: 'total' //数据总条数
                    }
                },
                fields: ['id','name','password','realName','roleId','roleName','deptName','deptId'],
                folderSort: true,
                autoLoad: true  //即时加载数据
            });
            
            UserGridStore.on('beforeload', function (UserGridStore, options) {  
		        var new_params = {findName:Ext.getCmp('findName').getValue()};  
		        Ext.apply(UserGridStore.proxy.extraParams, new_params);   
		    });
            var UserGrid = Ext.create('Ext.grid.Panel', {
                tbar: tbar,
                border: 0,
                store: UserGridStore,
                multiSelect: false,
                autoHeight : true,
        	    autoScroll:true,
                selModel: { selType: 'checkboxmodel' },
                renderTo: Ext.getBody(),
			 	columns: [{  
                    text: 'ID',  //显示的表头列名称
                    flex: 1, 
                    dataIndex: 'id',  //对应store的列字段名称
                    sortable: false,
            		menuDisabled:true
                },{  
                    text: '登录名',  //显示的表头列名称
                    flex: 1,
                    dataIndex: 'name',  //对应store的列字段名称
                    sortable: false,
                    menuDisabled:true
                },{  
                    text: '真实姓名',  //显示的表头列名称
                    flex: 1,
                    dataIndex: 'realName',  //对应store的列字段名称
                    sortable: false,
                    menuDisabled:true
                }/* , {
                    text: '密码',
                    flex: 1,
                    dataIndex: 'password',
                    sortable: false,
                    menuDisabled:true
                } */] 
                , listeners: {
                    //点击行触发事件
                    itemclick: function (record, node) {
                        //把列的qcid传递给QuesGridStore刷新对应的grid或tree
                      //  QuesGridStore.load({ params: { csId: node.data.id } });
                    }
                },
               bbar: [{
	                xtype: 'pagingtoolbar',
	                store: UserGridStore,
	                displayMsg: '显示 {0} - {1} 条，共计 {2} 条',
	                emptyMsg: "没有数据",
	                beforePageText: "当前页",
	                afterPageText: "共{0}页",
	                displayInfo: true                 
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
			                         fieldLabel: '登录名',
			                         id: 'name',
			                         name: 'name',
			                         allowBlank: false
			                     },{  
				                    fieldLabel: '真实姓名',  
				                    id: 'realName',  
				                    name: 'realName', 
				                    allowBlank: false
				                },
			                      {
			                          fieldLabel: '密码',
			                          inputType:'password',
			                          id: 'password',
			                          name: 'password',
			                          allowBlank: false
			                      }
			                ]
			            });
			            
			      var AddDialog = Ext.create("Ext.window.Window", {
			                iconCls: 'a_add',
			                closeAction: 'close',        //窗口关闭的方式：hide/close
			                resizable: false,
			                width:500,
			                height:220,
			                closable: true,            //是否可以关闭
			                modal: true,                //是否为模态窗口
			                items: AddForm,
			                buttons: [{
			                    text: '添加',
			                    iconCls: 'a_save',
			                    id: "btnAdd",
			                    handler: function () {
			                        if (AddForm.form.isValid()) {
			                                AddUser();
			                        }
			                    }
			                },
			                {
			                    text: '修改',
			                    iconCls: 'a_save',  //样式
			                    id: "btnEdit",
			                    handler: EditUser
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
			            function AddUser() {
			                Ext.Ajax.request({
			                    method: "post",
			                    url: "<%=request.getContextPath()%>/user/add.do",
			                    params: {json:JSON.stringify(AddForm.form.getValues())},
			                    callback: function (options, success, response) {
			                    	AddDialog.close();
			                        if (success) {
			                            var json = response.responseText;
			                            json = Ext.decode(json);
			                            UserGridStore.load();
			                            Ext.Msg.alert("提示", json.info);
			                        }
			                        else {
			                            Ext.Msg.alert("提示", "系统繁忙");
			                        }
			                    }
			                });
			            }
			           
			            //2 修改
			            function EditUser() {
			                var rows = UserGrid.getView().getSelectionModel().getSelection();
			                if (typeof (rows[0]) == "undefined") {
			                    Ext.Msg.alert("提示", "请选择要操作的行！");
			                    return false;
			                }
			                Ext.Ajax.request({
			                    method: "post",
			                    url: "<%=request.getContextPath()%>/user/edit.do",
			                    params: {json: JSON.stringify(AddForm.form.getValues()) },
			                    callback: function (options, success, response) {
			                    	AddDialog.close();
			                        if (success) {
			                            var json = response.responseText;
			                            json = Ext.decode(json);
			                            UserGridStore.load();
			                            Ext.Msg.alert("提示", json.info);
			                        }
			                        else {
			                            Ext.Msg.alert("提示", "系统繁忙");
			                        }
			                    }
			                });
			            }
			            
			            //2 修改
			            function saveRoleUser() {
			                var rows = UserGrid.getView().getSelectionModel().getSelection();
			                var recIds = TreeRolePanel.getView().getChecked();
			                var ids = [];
		                    Ext.Array.each(recIds, function(rec){
		                        ids.push(rec.get('id'));
		                    });
			                if (typeof (rows[0]) == "undefined") {
			                    Ext.Msg.alert("提示", "请选择要操作的行！");
			                    return false;
			                }
			                Ext.Ajax.request({
			                    method: "post",
			                    url: "<%=request.getContextPath()%>/user/saveRoleUser.do",
			                    params: {json: ids.join(','),userId:rows[0].data.id},
			                    callback: function (options, success, response) {
			                    	
			                        if (success) {
			                            var json = response.responseText;
			                            json = Ext.decode(json);
			                            UserGridStore.load();
			                            Ext.Msg.alert("提示", json.info);
			                            AddDialog.close();
			                        }
			                        else {
			                            Ext.Msg.alert("提示", "系统繁忙");
			                        }
			                    }
			                });
			            }
			            
			             //3 删除
			            function DelUser() {
			                var rows = UserGrid.getView().getSelectionModel().getSelection();
			                if (typeof (rows[0]) == "undefined") {
			                    Ext.Msg.alert("提示", "请选择要操作的行！");
			                    return false;
			                }
			                Ext.Ajax.request({
			                    method: "post",
			                    url: "<%=request.getContextPath()%>/user/delete.do",
			                    params: { id: rows[0].data.id },
			                    callback: function (options, success, response) {
			                    	AddDialog.close();
			                        if (success) {
			                            var json = response.responseText;
			                            json = Ext.decode(json);
			                            UserGridStore.load();
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
