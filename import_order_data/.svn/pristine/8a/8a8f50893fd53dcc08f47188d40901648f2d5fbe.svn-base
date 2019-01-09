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
    var proto = Ext.picker.Date.prototype, date = Ext.Date;
			    proto.monthNames = date.monthNames;
			    proto.dayNames = date.dayNames;
			    proto.format = date.defaultFormat;
var tag=0;
var toolbar = Ext.create("Ext.toolbar.Toolbar", {
                items: [
                    {
                        text: '加法',
                        iconCls: 'a_add',
                        handler: function () {
                            AddDialog.setTitle("基数加法运算");
                            AddDialog.setIconCls("a_add");
                            AddForm.form.reset();
                            tag=1;
                            if (typeof (grid) == "undefined") {
                                return false;
                            }
                            var rows = grid.getView().getSelectionModel().getSelection();
                            if (typeof (rows[0]) == "undefined") {
                                Ext.Msg.alert("提示", "请选择要操作的行！");
                                return false;
                            }
                            AddForm.form.setValues(rows[0].data);
                            AddDialog.show();
                        }
                    },'-',{
                        text: '减法',
                        iconCls: 'a_del',
                        handler: function () {
                            AddDialog.setTitle("基数减法运算");
                            AddDialog.setIconCls("a_del");
                            AddForm.form.reset();
                            tag=2;
                            if (typeof (grid) == "undefined") {
                                return false;
                            }
                            var rows = grid.getView().getSelectionModel().getSelection();
                            if (typeof (rows[0]) == "undefined") {
                                Ext.Msg.alert("提示", "请选择要操作的行！");
                                return false;
                            }
                            AddForm.form.setValues(rows[0].data);
                            AddDialog.show();
                        }
                    },'-',{
                        text: '乘法',
                        iconCls: 'a_cross',
                        handler: function () {
                            AddDialog.setTitle("基数加法运算");
                            AddDialog.setIconCls("a_cross");
                            AddForm.form.reset();
                            tag=3;
                            if (typeof (grid) == "undefined") {
                                return false;
                            }
                            var rows = grid.getView().getSelectionModel().getSelection();
                            if (typeof (rows[0]) == "undefined") {
                                Ext.Msg.alert("提示", "请选择要操作的行！");
                                return false;
                            }
                            AddForm.form.setValues(rows[0].data);
                            AddDialog.show();
                        }
                    },'-',{
                        text: '提取比例',
                        iconCls: 'a_edit',
                        handler: function () {
                            AddDialog.setTitle("基数提取比例运算");
                            AddDialog.setIconCls("application_edit");
                            AddForm.form.reset();
                            tag=4;
                            if (typeof (grid) == "undefined") {
                                return false;
                            }
                            var rows = grid.getView().getSelectionModel().getSelection();
                            if (typeof (rows[0]) == "undefined") {
                                Ext.Msg.alert("提示", "请选择要操作的行！");
                                return false;
                            }
                            AddForm.form.setValues(rows[0].data);
                            AddDialog.show();
                        }
                    },'-'/*,{
                        text: '删除',
                        iconCls: 'pencil_del',
                        handler: function () {
                            DelCardinalNum();
                        }
                    }*/,'->',
                     {
                         xtype:'datefield',
                         width: 100,
                         id: 'startDate',
                         name: 'startDate',
                         value:'${startDate}',
                         format : 'Y-m'//日期格式
			         }, {
                         xtype:'datefield',
                         width: 100,
                         id: 'endDate',
                         name: 'endDate',
                         value:'${endDate}',
                         format : 'Y-m'//日期格式
			         },
	                 {
	                        emptyText: '科室名称',
	                        xtype:'textfield',
	                        id: 'findName',
	                        name: 'findName'
				       },
             		{ 
	             		iconCls:"a_search",
	             		text:"搜索",
	             		handler:function (){
	             			gridStore.load();
	             		}
             		}]
               });
               
      var gridStore = Ext.create('Ext.data.Store', {
              pageSize: ${pageSize},
              proxy: {
                  type: 'ajax',
                  url: '<%=request.getContextPath()%>/cardinalNum/getJsonCardinalNums.do',
                  method:'post',
                  reader: {   //这里的reader为数据存储组织的地方
                      type: 'json', //返回数据类型为json格式
                      root: 'rows',  //数据
                      totalProperty: 'total' //数据总条数
                  }
              },
              fields: ['id','cardinalNum','allCardinalNum','ifformal','deptId','deptName','updateTimeStr','deptType'],
              folderSort: true,
              autoLoad: true  //即时加载数据
          });
          
          gridStore.on('beforeload', function (gridStore, options) {  
              var new_params = {findName:Ext.getCmp('findName').value};  
              Ext.apply(gridStore.proxy.extraParams, new_params);  
        });
		    
    var grid = Ext.create('Ext.grid.Panel', {
        renderTo: Ext.getBody(),
        store: gridStore,
        border:0,
        tbar: toolbar,
        columns: [{
            text: '科室',
            flex: 1,
            sortable: false,
            menuDisabled:true,
            dataIndex: 'deptName',
            renderer:function(value, cellmeta, record, rowIndex, columnIndex, store){
                return value+"-"+record.data["deptType"];
			}
        },{
            text: '基数',
            flex: 1,
            sortable: false,
            menuDisabled:true,
            dataIndex: 'cardinalNum'
        },{
            text: '全部基数',
            flex: 1,
            sortable: false,
            menuDisabled:true,
            dataIndex: 'allCardinalNum'
        },{
            text: '启用状态',
            flex: 1,
            sortable: false,
            menuDisabled:true,
            dataIndex: 'ifformal',
            renderer: function(value, metaData, record, rowIdx, colIdx, store, view) {
			        if(value == 'false'){        
			            return '测试';
			        }else{
			            return '正式';
			        }
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
	                store: gridStore,
	                displayMsg: '显示 {0} - {1} 条，共计 {2} 条',
	                emptyMsg: "没有数据",
	                beforePageText: "当前页",
	                afterPageText: "共{0}页",
	                displayInfo: true                 
            	}]
    });
    
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
			                     }, {
			                         xtype: "hidden",
			                         id: 'deptId',
			                         name: 'deptId'
			                     },
			                     {
			                         fieldLabel: '基数',
			                         id: 'cardinalNum',
			                         name: 'cardinalNum',
			                         allowBlank: false
			                     },
			                     {
			                         fieldLabel: '全部基数',
			                         id: 'allCardinalNum',
			                         name: 'allCardinalNum',
			                        // readOnly:true,
			                         allowBlank: false
			                     },{
			                         fieldLabel: '是否正式使用',
			                         id: 'ifformal',
			                         name: 'ifformal',
			                         inputValue:true,
			                         xtype: "checkbox",
			                         allowBlank: true
			                     },
			                     
			                     {
			                         fieldLabel: '基数调整系数',
			                         id: 'yuanSuanNum',
			                         name: 'yuanSuanNum'
			                     }
			                ]
			            });
			            
    var AddDialog = Ext.create("Ext.window.Window", {
			                iconCls: 'a_add',
			                width:520,
			                height:270,
			                closeAction: 'close',        //窗口关闭的方式：hide/close
			                resizable: false,
			                closable: true,            //是否可以关闭
			                modal: true,                //是否为模态窗口
			                items: AddForm,
			                buttons: [
			                {
			                    text: '另存',
			                    iconCls: 'a_save',  //样式
			                    id: "btnAdd",
			                    handler: function () { 
			                       SaveCardinalNum();
			                    }
			                },{
			                    text: '保存',
			                    iconCls: 'application_edit',
			                    id: "btnEdit",
			                    handler: function () {
			                        if (AddForm.form.isValid()) {
			                             EditCardinalNum();
			                        }
			                    }
			                },{
				                    id: "btnCancel",
				                    iconCls: 'a_cross',  //样式
				                    text: '关闭',
				                    handler: function () {
				                        //重置AddForm.form表单
				                        AddDialog.close();
				                    }
			                	}]
			            });
			            
			            function EditCardinalNum(){
			               var rows = grid.getView().getSelectionModel().getSelection();
			                if (typeof (rows[0]) == "undefined") {
			                    Ext.Msg.alert("提示", "请选择要操作的行！");
			                    return false;
			                }
			                Ext.Ajax.request({
			                    method: "post",
			                    url: "<%=request.getContextPath()%>/cardinalNum/edit.do",
			                    params: {json: JSON.stringify(AddForm.form.getValues()),operType:tag},
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
			            
			            function SaveCardinalNum(){
				            Ext.Ajax.request({
				                    method: "post",
				                    url: "<%=request.getContextPath()%>/cardinalNum/add.do",
				                    params: {json:JSON.stringify(AddForm.form.getValues()),operType:tag},
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
			            
			            
			             function DelCardinalNum(){
			             	var rows = grid.getView().getSelectionModel().getSelection();
				            Ext.Ajax.request({
				                    method: "post",
				                    url: "<%=request.getContextPath()%>/cardinalNum/delete.do",
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
  

