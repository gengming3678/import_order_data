<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../common.jsp" />
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>报表系统</title>
<script type="text/javascript">
 
Ext.require(['Ext.data.*', 'Ext.grid.*']);
Ext.onReady(function() {
    


var toolbar = Ext.create("Ext.toolbar.Toolbar", {
                items: [
                 {
                        text: '保存',
                        iconCls: 'a_save',
                        handler: function () {
	                        var m = RewardRuleGridStore.getModifiedRecords().slice(0);  
			                var jsonArray = [];  
			                Ext.each(m,function(item){  
			                    jsonArray.push(item.data);  
			                });
			             
			                Ext.Ajax.request({  
			                    method:'POST',  
			                    url: "<%=request.getContextPath()%>/rewardRule/add.do",
			                    params:{json:JSON.stringify(jsonArray)},    
			                    success:function(response){
			                    	RewardRuleGridStore.load();
			                    	var json = response.responseText;
				                    json = Ext.decode(json);  
			                        Ext.Msg.alert('系统提示',json.info);  
			                    },  
			                    failure:function(){  
			                        Ext.Msg.alert("错误","操作失败!");  
			                    }
			                    
			                });
                        }
                    }
                   /* {
                        text: '修改',
                        iconCls: 'a_edit',
                        handler: function () {
                            AddDialog.setTitle("修改计奖规则");
                            AddDialog.setIconCls("a_edit");
                            AddForm.form.reset();
                           // tag=1;
                            if (typeof (grid) == "undefined") {
                                return false;
                            }
                            var rows = grid.getView().getSelectionModel().getSelection();
                            if (typeof (rows[0]) == "undefined") {
                                Ext.Msg.alert("提示", "请选择要操作的行！");
                                return false;
                            }
                            AddForm.form.setValues(rows[0].data);
                            //Ext.getCmp("tiDuiTypeId").setValue(rows[0].data.tiDuiTypeId);
                            AddDialog.show();
                        }
                    },'-',{
                        text: '删除',
                        iconCls: 'a_del',
                        handler: function () {
                           DelRewardRule();
                        }
                    },*/,'-',{
                        text: '查看历史',
                        iconCls: 'a_search',
                        handler: function () {
                       		 if (typeof (grid) == "undefined") {
                                return false;
                            }
                            var rows = grid.getView().getSelectionModel().getSelection();
                            if (typeof (rows[0]) == "undefined") {
                                Ext.Msg.alert("提示", "请选择要操作的行！");
                                return false;
                            }
                           RewardRuleGridHistoryStore.load();
                           ShowHistoryListDialog.show();
                        }
                    },'-',{
                        text: '导出Excel',
                        iconCls: 'page_excel',
                        handler: function () {
                          window.location.href = "<%=request.getContextPath()%>/rewardRule/exportExcel.do";
                        }
                    },'-',{
                        text: '<font color="red">鼠标双击单元格即可编辑</font>'
                    },'->',
                    {
                        emptyText: '科室名称',
                        xtype:'textfield',
                        id: 'findName',
                        name: 'findName'
			       },
             		{ 
	             		iconCls:"a_search",
	             		text:"搜索",
	             		handler:function () {
	             			RewardRuleGridStore.load();
	             		}
             		}]
               });
               
      var RewardRuleGridStore = Ext.create('Ext.data.Store', {
      			pageSize: ${pageSize},
                proxy: {
                    type: 'ajax',
                    url: '<%=request.getContextPath()%>/rewardRule/getJsonRewardRule.do',
                    method:'post',
                    reader: {   //这里的reader为数据存储组织的地方
                        type: 'json',
                        root: 'rows',  //数据
                        totalProperty: 'total' //数据总条数
                    }
                },
                fields: ['id','beiShu','zengFuBiLi','tiDuiName','biaoZhun',
                'tiDuiTypeId','deptId','deptName','deptType','updateTimeStr'],
                folderSort: true,
                autoLoad: true
            });
            
         RewardRuleGridStore.on('beforeload', function (RewardRuleGridStore, options) { 
            var findName = Ext.getCmp("findName").getValue(); 
	        var new_params = {findName:findName};  
	           Ext.apply(RewardRuleGridStore.proxy.extraParams, new_params);  
	    });          
               
               
    var grid = Ext.create('Ext.grid.Panel', {
        renderTo: Ext.getBody(),
        store: RewardRuleGridStore,
        border:0,
        tbar: toolbar,
        autoHeight : true,
        autoScroll:true,
        plugins:[  
                 Ext.create('Ext.grid.plugin.CellEditing',{  
                     clicksToEdit:2 //设置单击单元格编辑  
                 })  
        ],  
        columns: [{
            text: '科室',
            flex: 1,
            menuDisabled:true,
 		    sortable: false,
            dataIndex: 'deptName',
            renderer:function(value, cellmeta, record, rowIndex, columnIndex, store){
                return value+"-"+record.data["deptType"];
			}
        },{
            text: '梯队',
            flex: 1,
            sortable: false,
            menuDisabled:true,
            dataIndex: 'tiDuiName'
        },{
            text: '倍数',
            flex: 1,
            sortable: false,
            menuDisabled:true,
            dataIndex: 'beiShu',
            editor:{  
                allowBlank:true  
            }
        },{
            text: '增幅比例',
            flex: 1,
            sortable: false,
            menuDisabled:true,
            dataIndex: 'zengFuBiLi',
            editor:{  
                allowBlank:true  
            }
        },{
            text: '标准',
            flex: 1,
            sortable: false,
            menuDisabled:true,
            dataIndex: 'biaoZhun',
            editor:{  
                allowBlank:true  
            }
        },{
            text: '更新时间',
            flex: 1,
            sortable: false,
            menuDisabled:true,
            dataIndex: 'updateTimeStr'
        }  
        ],
        bbar: [{
	                xtype: 'pagingtoolbar',
	                store: RewardRuleGridStore,
	                displayMsg: '显示 {0} - {1} 条，共计 {2} 条',
	                emptyMsg: "没有数据",
	                beforePageText: "当前页",
	                afterPageText: "共{0}页",
	                displayInfo: true                 
            	}]
    });
    
    	
		//
		 var RewardRuleGridHistoryStore = Ext.create('Ext.data.Store', {
                proxy: {
                    type: 'ajax',
                    url: '<%=request.getContextPath()%>/rewardRule/getJsonRewardRuleHistory.do',
                    method:'post',
                    reader: {   //这里的reader为数据存储组织的地方
                        type: 'json'
                    }
                },
                fields: ['id','beiShu','zengFuBiLi','tiDuiName','biaoZhun',
                'tiDuiTypeId','deptId','deptName','updateTimeStr'],
                folderSort: true
            });
            
            RewardRuleGridHistoryStore.on('beforeload', function (RewardRuleGridHistoryStore, options) { 
            	var rows = grid.getView().getSelectionModel().getSelection(); 
		        var new_params = {deptId:rows[0].data.deptId,tiDuiTypeId:rows[0].data.tiDuiTypeId};  
		           Ext.apply(RewardRuleGridHistoryStore.proxy.extraParams, new_params);  
		         //alert('beforeload');  
		    });
            var RewardRuleHisrotyGrid = Ext.create('Ext.grid.Panel', {
                border: 0,
                store: RewardRuleGridHistoryStore,
                multiSelect: false,
                height:400,
                //renderTo: Ext.getBody(),
			 	columns: [{
				            text: '科室',
				            flex: 1,
				            sortable: false,
				            dataIndex: 'deptName'
				        },{
				            text: '梯队',
				            flex: 1,
				            sortable: false,
				            dataIndex: 'tiDuiName'
				        },{
				            text: '倍数',
				            flex: 1,
				            sortable: false,
				            dataIndex: 'beiShu'
				        },{
				            text: '增幅比例',
				            flex: 1,
				            sortable: false,
				            dataIndex: 'zengFuBiLi'
				        },{
				            text: '标准',
				            flex: 1,
				            sortable: false,
				            dataIndex: 'biaoZhun'
				        },{
				            text: '更新时间',
				            flex: 2,
				            sortable: false,
				            dataIndex: 'updateTimeStr'
				        }] 
                , listeners: {
                    //点击行触发事件
                    itemclick: function (record, node) {
                        //把列的qcid传递给QuesGridStore刷新对应的grid或tree
                      //  QuesGridStore.load({ params: { csId: node.data.id } });
                    }
                }
            });          
		
    			
			            
			            
			            var ShowHistoryListDialog = Ext.create("Ext.window.Window", {
			                iconCls: 'a_search',
			                title:'查看历史数据',
			                closeAction: 'close',        //窗口关闭的方式：hide/close
			                resizable: false,
			                closable: true,            //是否可以关闭
			                modal: true,
			                width:700,                //是否为模态窗口
			                height:350,
			                items: RewardRuleHisrotyGrid
			            });
			            
			           
			            
			            <%-- function SaveRewardRule(){
				            Ext.Ajax.request({
				                    method: "post",
				                    url: "<%=request.getContextPath()%>/rewardRule/add.do",
				                    params: {json:JSON.stringify(AddForm.form.getValues())},
				                    callback: function (options, success, response) {
				                    	AddDialog.close();
				                        if (success) {
				                            var json = response.responseText;
				                            json = Ext.decode(json);
				                            window.location.reload();
				                            //grid.reload();
				                            Ext.Msg.alert("提示", json.info);
				                        }
				                        else {
				                            Ext.Msg.alert("提示", "系统繁忙");
				                        }
				                    }
				                });
			            } --%>
			            
			             function EditRewardRule(){
			                var rows = grid.getView().getSelectionModel().getSelection();
			                if (typeof (rows[0]) == "undefined") {
			                    Ext.Msg.alert("提示", "请选择要操作的行！");
			                    return false;
			                }
			                Ext.Ajax.request({
			                    method: "post",
			                    url: "<%=request.getContextPath()%>/rewardRule/add.do",
			                    params: {json: JSON.stringify(AddForm.form.getValues())},
			                    callback: function (options, success, response) {
			                    	AddDialog.close();
			                        if (success) {
			                            var json = response.responseText;
			                            json = Ext.decode(json);
			                            window.location.reload();
			                            //grid.reload();
			                            Ext.Msg.alert("提示", json.info);
			                        }
			                        else {
			                            Ext.Msg.alert("提示", "系统繁忙");
			                        }
			                    }
			                }); 
			            }
			             function DelRewardRule(){
			             	var rows = grid.getView().getSelectionModel().getSelection();
				            Ext.Ajax.request({
				                    method: "post",
				                    url: "<%=request.getContextPath()%>/rewardRule/delete.do",
				                    params: {id: rows[0].data.id},
				                    callback: function (options, success, response) {
				                    	AddDialog.close();
				                        if (success) {
				                            var json = response.responseText;
				                            json = Ext.decode(json);
				                            window.location.reload();
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
  

