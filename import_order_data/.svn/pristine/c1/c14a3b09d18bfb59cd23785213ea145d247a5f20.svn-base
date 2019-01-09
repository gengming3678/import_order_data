<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:include page="../../common.jsp" />
<html>
  <head>
  </head>
  <body>
 	<script type="text/javascript">
 		Ext.onReady(function(){
 			var proto = Ext.picker.Date.prototype, date = Ext.Date;
			    proto.monthNames = date.monthNames;
			    proto.dayNames = date.dayNames;
			    proto.format = date.defaultFormat;
				var queryCondition = '${queryCondition}';
				
			var tbar = Ext.create("Ext.toolbar.Toolbar", {
			 	margin:'0',
				renderTo: 'ToolbarDiv',
                items: [/* {
                        text: '表格报表',
                        id:'tableReport',
                        iconCls: 'a_grid',
                        handler: function () {
                            store.load();
	                        $("#chartsDiv").height(0);
	                        Ext.getCmp("gridData").hide();
	                        Ext.get("chartsDiv").hide();
	                        //
	                        Ext.getCmp("tableReport").hide();
	                        Ext.getCmp("importExcelFile").hide();
	                        Ext.getCmp("chartReport").hide();
                        }
                     },{
                        text: '图形报表',
                        id:'chartReport',
                        hidden:true,
                        iconCls: 'img_04',
                        handler: function () {
	                        $("#chartsDiv").height(400);
	                        Ext.getCmp("gridData").hide();
	                        Ext.get("chartsDiv").show();
	                        
	                        Ext.getCmp("tableReport").show();
	                        Ext.getCmp("importExcelFile").hide();
	                        Ext.getCmp("chartReport").hide();
                        }
                     }, '-',*/
                    {
                        text: '导出',
                        id:'importExcelFile',
                        hidden:true,
                        iconCls: 'page_excel',
                        handler: function () {
                        	
	                        var startDate = Ext.isEmpty(Ext.getCmp('startDate'))?"":Ext.util.Format.date(Ext.getCmp('startDate').value,'Y-m-d');
					        var endDate = Ext.isEmpty(Ext.getCmp('endDate'))?"":Ext.util.Format.date(Ext.getCmp('endDate').value,'Y-m-d');
					        var startYm = Ext.isEmpty(Ext.getCmp('startYm'))?"":Ext.util.Format.date(Ext.getCmp('startYm').value,'Y-m');
					        var endYm = Ext.isEmpty(Ext.getCmp('endYm'))?"":Ext.util.Format.date(Ext.getCmp('endYm').value,'Y-m');
					        var statYm = Ext.isEmpty(Ext.getCmp('statYm'))?"":Ext.util.Format.date(Ext.getCmp('statYm').value,'Y-m');
					        var statYmd = Ext.isEmpty(Ext.getCmp('statYmd'))?"":Ext.util.Format.date(Ext.getCmp('statYmd').value,'Y-m-d');

					        var deptName = Ext.isEmpty(Ext.getCmp('deptName'))?"":Ext.getCmp('deptName').value;
					        <c:if test="${fn:contains(queryCondition, '#startDate#') && fn:contains(queryCondition, '#endDate#')}">
						        if(!Ext.isEmpty(startDate)&&!Ext.isEmpty(endDate)){
						        	var urlExcel="<%=request.getContextPath()%>/chartDisplay/exportExcel.do?nodeId=${nodeId}&startDate="+startDate+"&endDate="+endDate+"&deptName="+deptName+"";
		                        	window.location.href = urlExcel;
						        }else{
								    Ext.MessageBox.alert("提示框","时间不能为空!"); 
								 }
					        </c:if>
					        
					        <c:if test="${fn:contains(queryCondition, '#startYM#') && fn:contains(queryCondition, '#endYM#')}">
						        if(!Ext.isEmpty(startYm)&&!Ext.isEmpty(endYm)){
						        	var urlExcel="<%=request.getContextPath()%>/chartDisplay/exportExcel.do?nodeId=${nodeId}&startYm="+startYm+"&endYm="+endYm+"&deptName="+deptName+"";
		                        	window.location.href = urlExcel;
						        }else{
							        Ext.MessageBox.alert("提示框","时间不能为空!"); 
							     }
					        </c:if>
					        <c:if test="${fn:contains(queryCondition, '#statYM#') }">
					         	if(!Ext.isEmpty(statYm)){
						        	var urlExcel="<%=request.getContextPath()%>/chartDisplay/exportExcel.do?nodeId=${nodeId}&statYm="+statYm+"&deptName="+deptName+"";
		                        	window.location.href = urlExcel;
						        }else{
						          	Ext.MessageBox.alert("提示框","时间不能为空!"); 
						        }
					        </c:if>
					        <c:if test="${fn:contains(queryCondition, '#statYMD#') }">
					         if(!Ext.isEmpty(statYmd)){
					        	var urlExcel="<%=request.getContextPath()%>/chartDisplay/exportExcel.do?nodeId=${nodeId}&statYmd="+statYmd+"&deptName="+deptName+"";
	                        	window.location.href = urlExcel;
					        }else{
					          	Ext.MessageBox.alert("提示框","时间不能为空!"); 
					        }
					        </c:if>
                        }
                    },"->"
                    
                     <c:if test="${fn:contains(queryCondition, '#startDate#')}">
                    ,
                    {
                         xtype:'datefield',
                         width: 100,
                         id: 'startDate',
                         name: 'startDate',
                         value:'${startDate}',
                         format : 'Y-m-d'//日期格式
			         }
			         </c:if>
			         <c:if test="${fn:contains(queryCondition, '#endDate#')}">
			         ,
                     {
                         xtype:'datefield',
                         width: 100,
                         id: 'endDate',
                         name: 'endDate',
                         value:'${endDate}',
                         format : 'Y-m-d'
                     }
                     </c:if>
                     
                     <c:if test="${fn:contains(queryCondition, '#startYM#')}">
                    ,
                    {
                         xtype:'datefield',
                         width: 100,
                         id: 'startYm',
                         name: 'startYm',
                         value:'${startYm}',
                         format : 'Y-m'//日期格式
			         }
			         </c:if>
			         <c:if test="${fn:contains(queryCondition, '#endYM#')}">
			         ,
                     {
                         xtype:'datefield',
                         width: 100,
                         id: 'endYm',
                         name: 'endYm',
                         value:'${endYm}',
                         format : 'Y-m'
                     }
                     </c:if>
                     <c:if test="${fn:contains(queryCondition, '#statYM#')}">
			         ,
                     {
                         xtype:'datefield',
                         width: 100,
                         id: 'statYm',
                         name: 'statYm',
                         value:'${statYm}',
                         format : 'Y-m'
                     }
                     </c:if>
                     <c:if test="${fn:contains(queryCondition, '#statYMD#')}">
			         ,
                     {
                         xtype:'datefield',
                         width: 100,
                         id: 'statYmd',
                         name: 'statYmd',
                         value:'${statYmd}',
                         format : 'Y-m-d'
                     }
                     </c:if>
                     <c:if test="${fn:contains(queryCondition, '#deptName#')}">
	                    ,
	                    {
	                        emptyText: '科室',
	                        xtype:'textfield',
	                        width: 100,
	                        id: 'deptName',
	                        name: 'deptName',
	                        value:'${deptName}',
				        }
			        </c:if>
			       ,
             		{ 
	             		iconCls:"a_search",
	             		text:"搜索",
	             		handler:function (){
	             			
		             		var startDate = Ext.isEmpty(Ext.getCmp('startDate'))?"":Ext.util.Format.date(Ext.getCmp('startDate').value,'Y-m-d');
					        var endDate = Ext.isEmpty(Ext.getCmp('endDate'))?"":Ext.util.Format.date(Ext.getCmp('endDate').value,'Y-m-d');
					        var startYm = Ext.isEmpty(Ext.getCmp('startYm'))?"":Ext.util.Format.date(Ext.getCmp('startYm').value,'Y-m');
					        var endYm = Ext.isEmpty(Ext.getCmp('endYm'))?"":Ext.util.Format.date(Ext.getCmp('endYm').value,'Y-m');
					        var statYm = Ext.isEmpty(Ext.getCmp('statYm'))?"":Ext.util.Format.date(Ext.getCmp('statYm').value,'Y-m');
					        var statYmd = Ext.isEmpty(Ext.getCmp('statYmd'))?"":Ext.util.Format.date(Ext.getCmp('statYmd').value,'Y-m-d');
	
					        var deptName = Ext.isEmpty(Ext.getCmp('deptName'))?"":Ext.getCmp('deptName').value;
					        <c:if test="${fn:contains(queryCondition, '#startDate#') && fn:contains(queryCondition, '#endDate#')}">
						        if(!Ext.isEmpty(startDate)&&!Ext.isEmpty(endDate)){
						        	var urlDisplay="<%=request.getContextPath()%>/chartDisplay/list.do?nodeId=${nodeId}&startDate="+startDate+"&endDate="+endDate+"&deptName="+deptName+"";
		                        	window.location.href = urlDisplay;
						        }else{
								    Ext.MessageBox.alert("提示框","时间不能为空!"); 
								 }
					        </c:if>
					        
					        <c:if test="${fn:contains(queryCondition, '#startYM#') && fn:contains(queryCondition, '#endYM#')}">
						        if(!Ext.isEmpty(startYm)&&!Ext.isEmpty(endYm)){
						        	var urlDisplay="<%=request.getContextPath()%>/chartDisplay/list.do?nodeId=${nodeId}&startYm="+startYm+"&endYm="+endYm+"&deptName="+deptName+"";
		                        	window.location.href = urlDisplay;
						        }else{
							        Ext.MessageBox.alert("提示框","时间不能为空!"); 
							     }
					        </c:if>
					        <c:if test="${fn:contains(queryCondition, '#statYM#') }">
					         	if(!Ext.isEmpty(statYm)){
						        	var urlDisplay="<%=request.getContextPath()%>/chartDisplay/list.do?nodeId=${nodeId}&statYm="+statYm+"&deptName="+deptName+"";
		                        	window.location.href = urlDisplay;
						        }else{
						          	Ext.MessageBox.alert("提示框","时间不能为空!"); 
						        }
					        </c:if>
					        <c:if test="${fn:contains(queryCondition, '#statYMD#') }">
					         if(!Ext.isEmpty(statYmd)){
					        	var urlDisplay="<%=request.getContextPath()%>/chartDisplay/list.do?nodeId=${nodeId}&statYmd="+statYmd+"&deptName="+deptName+"";
	                        	window.location.href = urlDisplay;
					        }else{
					          	Ext.MessageBox.alert("提示框","时间不能为空!"); 
					        }
					        </c:if>
	             		}
             		}
                ]
            });
			
			var store = Ext.create('Ext.data.Store', {
				pageSize: ${pageSize},
                proxy: {
                    type: 'ajax',
                    url: '<%=request.getContextPath()%>/chartDisplay/findChartGridDisplayList.do',
                    method:'post',
                    reader: {   //这里的reader为数据存储组织的地方
                        type: 'json',
                        root: 'rows',  //数据
                        totalProperty: 'total' //数据总条数
                    }
                },
                fields : [
                <c:forEach items="${coloumnFields}" var="item" varStatus="st"> 
                  '${item}'<c:if test="${!st.last}">,</c:if>
                </c:forEach>
                ]
                 ,
                autoLoad: true  //即时加载数据
            });
            
            store.on('beforeload', function (store, options) {  
       				var startDate = Ext.isEmpty(Ext.getCmp('startDate'))?"":Ext.util.Format.date(Ext.getCmp('startDate').value,'Y-m-d');
			        var endDate = Ext.isEmpty(Ext.getCmp('endDate'))?"":Ext.util.Format.date(Ext.getCmp('endDate').value,'Y-m-d');
			        var startYm = Ext.isEmpty(Ext.getCmp('startYm'))?"":Ext.util.Format.date(Ext.getCmp('startYm').value,'Y-m');
			        var endYm = Ext.isEmpty(Ext.getCmp('endYm'))?"":Ext.util.Format.date(Ext.getCmp('endYm').value,'Y-m');
			        var statYm = Ext.isEmpty(Ext.getCmp('statYm'))?"":Ext.util.Format.date(Ext.getCmp('statYm').value,'Y-m');
			        var statYmd = Ext.isEmpty(Ext.getCmp('statYmd'))?"":Ext.util.Format.date(Ext.getCmp('statYmd').value,'Y-m-d');
			      
			        var deptName = Ext.isEmpty(Ext.getCmp('deptName'))?"":Ext.getCmp('deptName').value;
			        var new_params = {
					        startDate:startDate,
					        endDate:endDate,
					        startYm:startYm,
					        endYm:endYm,
					        statYm:statYm,
					        statYmd:statYmd,
					        deptName:deptName,
					        nodeId:'${nodeId}' 
			        };  
		        Ext.apply(store.proxy.extraParams, new_params);  
		    });
		                
 			var grid= Ext.create('Ext.grid.Panel',{
 				store : store,
 				border : true ,
 				multiSelect: false,
 				margin:'0 75 0 50',
 				hidden:false,
 				id:'gridData',
 				autoHeight : true,
        		autoScroll:true,
 				emptyText:'<div  style="color:#000;font-size : 1em;text-align:center">暂无数据</div>',
 				renderTo: 'gridDiv',
 				region : 'center',
 				columns : [{ xtype: 'rownumberer' ,text : '序号',width:50},
 					/*一级显示 */
	 				<c:forEach items="${listColoumnFields}" var="item" varStatus="st"> 
	 				{
	 					text : '${item.name}',
	 					menuDisabled:false,
	 					sortable:true,
	 					<c:if test="${not empty  item.code}">
	 					dataIndex : '${item.code}',
	 					flex:1 ,
	 					</c:if>
	 					align : 'center'
	 				}
	                  <c:if test="${!st.last}">,</c:if>
	                </c:forEach>
 				],
 				bbar: [{
	                xtype: 'pagingtoolbar',
	                store: store,
	                displayMsg: '显示 {0} - {1} 条，共计 {2} 条',
	                emptyMsg: "没有数据",
	                beforePageText: "当前页",
	                afterPageText: "共{0}页",
	                displayInfo: true                 
            	}]
 			});
 			
 			
 		});

</script>
	<!-- toolBar -->
 	<div id="ToolbarDiv" ></div>
 	<!-- chart -->
 	<div id="chartsDiv" style="height:350px;margin-top: 10px;"></div>
    <script src="../echart/build/dist/echarts.js"></script>
    <script type="text/javascript">
        // 路径配置
        require.config({
            paths: {
                echarts: '../echart/build/dist'
            }
        });
        
        // 使用
        require(
            [
                'echarts',
                'echarts/chart/bar',
                'echarts/chart/line',
                'echarts/chart/pie'
            ],
            function (ec) {
            	var option;
                Ext.Ajax.request({
                	 method:'post',
				   	 url: '<%=request.getContextPath()%>/chartDisplay/getChartOptions.do',
				     params: {
							nodeId:${nodeId},
							startDate:${startDate},
							endDate:${endDate},
							startYm:${startYm},
							endYm:${endYm},
							statYm:${statYm},
							statYmd:${statYmd},
							deptName:'${deptName}'
				     },
				     async: false,
					// 遍历返回的数据
					success: function (response) 
					{
						option = Ext.decode(response.responseText);
					}
				}); 
                var myChart = ec.init(document.getElementById('chartsDiv')); 
                myChart.setOption(option); 
            }
        );
    </script>
 	<div id="gridDiv" ></div>
  </body>
</html>
