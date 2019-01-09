<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../common.jsp" />
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>绩效工资专项激励系统</title>
<script type="text/javascript">
    Ext.require(['*']);

    Ext.onReady(function() {

        Ext.QuickTips.init();
        Ext.state.Manager.setProvider(Ext.create('Ext.state.CookieProvider'));
        
        	var tabs = Ext.create('Ext.tab.Panel', {
		                region: 'center', // center
		                deferredRender: false,
		                activeTab: 0,     
		                items: [{
		                    title: '首页',
		                    autoScroll: true,
		                    html:''
		                }]
		            });
		            

           
		<c:forEach items="${listNodes}" var="item" varStatus="st"> 
					 
				//树Store
	            var treeStore_${st.count} = Ext.create('Ext.data.TreeStore', {
	            	nodeParam:'parentId', 
	                proxy: {
	                    type: 'ajax',
	                    url: '<%=request.getContextPath()%>/node/getNodesByParentId.do'
	                },
	                fields: ['text','id','leaf','parentId','url']
	            });
            	//树Panel
		       	var panel_${st.count} = Ext.create('Ext.tree.Panel', {
						        title: '${item.text}',
						        <c:if test="${not empty item.imgType}">
						        iconCls: '${item.imgType}' ,
						        </c:if>
						        <c:if test="${empty item.imgType}">
						        iconCls: 'img_04' ,
						        </c:if>
						        rootVisible: false,  //是否隐藏根节点
						        useArrows : false,//是否使用箭头
						        store: treeStore_${st.count}, 
		               			root:{ 
				                    text:'根节点', 
				                    expanded : true,//根节点是否展开 
				                    id:'${item.id}',
				                    parentId:'${item.id}'
				                  } 
						    });
				//树Panel单击事件	    
				panel_${st.count}.on({
				            //目录树单击事件
				            'itemclick' : function(view, rcd, item, idx, event, eOpts) {
				                var tabText = rcd.get('text'); //节点text
				                var leaf = rcd.get('leaf'); 
				                var nodeId = rcd.get('id'); 
				                var url = rcd.get('url')+"?nodeId="+nodeId+"&startDate=&endDate=&deptName="; 
				                if(leaf){//子节点
					                tabs.removeAll();
					                var curPage =  tabs.add({
							            title: tabText,
							            html:'<iframe  src="<%=request.getContextPath()%>'+url+'" width="100%"  height="100%" id="mainWindow" name="mainWindow"  frameborder="0" scrolling="auto"></iframe>'
							        });
					        		tabs.setActiveTab(curPage);
				                } 
				        	}
				        });
		</c:forEach>
                          
            
		
			
				     
        var viewport = Ext.create('Ext.Viewport', {
            id: 'border-example',
            layout: 'border',
            items: [
            Ext.create('Ext.Component', {
                region: 'north',// north
                contentEl: 'north'
            }), Ext.create('Ext.Component', {
                region: 'south',// south
                autoEl: {
                    tag: 'div',
                    html:'<p align="center" style="height:18px;padding-top: 1px;">技术支持 : 上海蓬海涞讯数据技术有限公司</p>'
                }
            }), {
                region: 'west',
                stateId: 'navigation-panel',
                id: 'west-panel', // west
                title: '系统模块',
                split: false,
                width: 200,
                minWidth: 175,
                maxWidth: 400,
                collapsible: false,
                animCollapse: false,
                margins: '0 2 -15 2',
                layout: 'accordion',
                items: [
                 <c:forEach items="${listNodes}" var="item" varStatus="st"> 
                 	panel_${st.count} 
                 	 <c:if test="${!st.last}">,</c:if>
                 </c:forEach>
				    ]
            },
           tabs
            ]
        });
        
         
    });
    </script>  
</head>  
<body >
    <div id="north"  class="NorthBody">
		<div style="font-size: 24px;padding:10px 0 0 50px;height: 60px;">
			山东省立医院报表系统
				<span style="padding:5px 10px 0 0;float:right;font-size: 14px;text-align: right;"><img  style="margin: 0 0 0 0" src="../ext4/resources/icons/fam/user.png"  />欢迎你,${sessionScope.userSession.name}<a href="<%=request.getContextPath()%>/login/logout.do">退出</a></span>
		</div>
    </div>
</body>
</html>