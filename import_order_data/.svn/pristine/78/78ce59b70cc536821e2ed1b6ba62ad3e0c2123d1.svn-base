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
                items: [{
                        text: '刷新',
                        iconCls: 'a_refresh',
                        handler: function () {
                        	window.location.reload();
                        }
                     },'-',
                    {
                        text: '导出',
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
						        	var urlExcel="<%=request.getContextPath()%>/tableDisplay/exportExcel.do?nodeId=${nodeId}&startDate="+startDate+"&endDate="+endDate+"&deptName="+deptName+"";
		                        	window.location.href = urlExcel;
						        }else{
								    Ext.MessageBox.alert("提示框","时间不能为空!"); 
								 }
					        </c:if>
					        
					        <c:if test="${fn:contains(queryCondition, '#startYM#') && fn:contains(queryCondition, '#endYM#')}">
						        if(!Ext.isEmpty(startYm)&&!Ext.isEmpty(endYm)){
						        	var urlExcel="<%=request.getContextPath()%>/tableDisplay/exportExcel.do?nodeId=${nodeId}&startYm="+startYm+"&endYm="+endYm+"&deptName="+deptName+"";
		                        	window.location.href = urlExcel;
						        }else{
							        Ext.MessageBox.alert("提示框","时间不能为空!"); 
							     }
					        </c:if>
					        <c:if test="${fn:contains(queryCondition, '#statYM#') }">
					         	if(!Ext.isEmpty(statYm)){
						        	var urlExcel="<%=request.getContextPath()%>/tableDisplay/exportExcel.do?nodeId=${nodeId}&statYm="+statYm+"&deptName="+deptName+"";
		                        	window.location.href = urlExcel;
						        }else{
						          	Ext.MessageBox.alert("提示框","时间不能为空!"); 
						        }
					        </c:if>
					        <c:if test="${fn:contains(queryCondition, '#statYMD#') }">
					         if(!Ext.isEmpty(statYmd)){
					        	var urlExcel="<%=request.getContextPath()%>/tableDisplay/exportExcel.do?nodeId=${nodeId}&statYmd="+statYmd+"&deptName="+deptName+"";
	                        	window.location.href = urlExcel;
					        }else{
					          	Ext.MessageBox.alert("提示框","时间不能为空!"); 
					        }
					        </c:if>
                        }
                    },"-","->"
                    
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
						        	var urlDisplay="<%=request.getContextPath()%>/tableDisplay/list.do?nodeId=${nodeId}&startDate="+startDate+"&endDate="+endDate+"&deptName="+deptName+"";
		                        	window.location.href = urlDisplay;
						        }else{
								    Ext.MessageBox.alert("提示框","时间不能为空!"); 
								 }
					        </c:if>
					        
					        <c:if test="${fn:contains(queryCondition, '#startYM#') && fn:contains(queryCondition, '#endYM#')}">
						        if(!Ext.isEmpty(startYm)&&!Ext.isEmpty(endYm)){
						        	var urlDisplay="<%=request.getContextPath()%>/tableDisplay/list.do?nodeId=${nodeId}&startYm="+startYm+"&endYm="+endYm+"&deptName="+deptName+"";
		                        	window.location.href = urlDisplay;
						        }else{
							        Ext.MessageBox.alert("提示框","时间不能为空!"); 
							     }
					        </c:if>
					        <c:if test="${fn:contains(queryCondition, '#statYM#') }">
					         	if(!Ext.isEmpty(statYm)){
						        	var urlDisplay="<%=request.getContextPath()%>/tableDisplay/list.do?nodeId=${nodeId}&statYm="+statYm+"&deptName="+deptName+"";
		                        	window.location.href = urlDisplay;
						        }else{
						          	Ext.MessageBox.alert("提示框","时间不能为空!"); 
						        }
					        </c:if>
					        <c:if test="${fn:contains(queryCondition, '#statYMD#') }">
					         if(!Ext.isEmpty(statYmd)){
					        	var urlDisplay="<%=request.getContextPath()%>/tableDisplay/list.do?nodeId=${nodeId}&statYmd="+statYmd+"&deptName="+deptName+"";
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
                    url: '<%=request.getContextPath()%>/tableDisplay/findReportDisplayList.do',
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
                ],
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
 				border : false ,
 				multiSelect: false,
 				//frame:true,
 				autoHeight : true,
        		autoScroll:true,
 				tbar: tbar,
 				emptyText:'<div  style="color:#000;font-size : 1em;text-align:center">暂无数据</div>',
 				renderTo: Ext.getBody(),
 				region : 'center',
 				columns : [{ xtype: "rownumberer" ,text : '序号',width:50},
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
	 					/*二级显示 */
	 					<c:if test="${not empty  item.subCells}">,
		 					columns : [
			 					    
						 				<c:forEach items="${item.subCells}" var="itemSubTwoLevel" varStatus="stSubTwoThree"> 
						 				{
						 					text : '${itemSubTwoLevel.name}',
						 					menuDisabled:false,
						 					sortable:true,
						 					dataIndex : '${itemSubTwoLevel.code}',
						 					align : 'center' 
						 							/*三级显示 */
						 					      <c:if test="${not empty  itemSubTwoLevel.subCells}">,
									 					columns : [
										 					    
													 				<c:forEach items="${itemSubTwoLevel.subCells}" var="itemSubThreeLevel" varStatus="stSubThreeLevel"> 
													 				{
													 					text : '${itemSubThreeLevel.name}',
													 					menuDisabled:false,
													 					sortable:true,
													 					dataIndex : '${itemSubThreeLevel.code}',
													 					align : 'center' 
													 				}
										                  			<c:if test="${!stSubThreeLevel.last}">,</c:if>
										                  			</c:forEach>
									 					]
								 					</c:if>
						 					
						 				}
			                  			<c:if test="${!stSubTwoThree.last}">,</c:if>
			                  			</c:forEach>
		 					]
	 					</c:if>
	 					
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
  </body>
</html>
