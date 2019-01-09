<br><%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"   %>
<jsp:include page="../../common.jsp" />
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>报表系统</title>
<style type="text/css">
body{
	background:#f5f5f5;
}
table{
	border:1;
	margin:5;
	border-collapse:collapse;
	color:blue;
	font-family : 微软雅黑,宋体;
	font-size : 16px;
} 
table td{padding:5;}
</style>
<script type="text/javascript">


function saveDays(){
			var selectDates = new Array(); 
			var curYear = $("#curYear").val();
			$('input[name="checkDate"]:checked').each(function(){ 
				selectDates.push($(this).val()); 
			});
			//alert(selectDates.length);
			if(selectDates.length>0){
				$.ajax({
					 type:"post",
					 url: "<%=request.getContextPath()%>/holiday/add.do",
					 dataType:"json",
					 data:{selectDates:selectDates,curYear:curYear},
					 success:function(data){
					    if (data.msg=='success') {
							    var curYear = $("#curYear").val();
							    var urlDay="<%=request.getContextPath()%>/holiday/queryList.do?curYear="+curYear;
							        window.location.href = urlDay;
	                           Ext.Msg.alert("提示", data.info);
	                       }
	                       else {
	                           Ext.Msg.alert("提示", "系统繁忙");
	                       }
					 }
				 });	
			 }else{
				 $.ajax({
						 type:"post",
						 url: "<%=request.getContextPath()%>/holiday/delete.do",
						 dataType:"json",
						 data:{curYear:curYear},
						 success:function(data){
						    if (data.msg=='success') {
								    var curYear = $("#curYear").val();
								    var urlDay="<%=request.getContextPath()%>/holiday/queryList.do?curYear="+curYear;
								        window.location.href = urlDay;
		                           Ext.Msg.alert("提示", data.info);
		                       }
		                       else {
		                           Ext.Msg.alert("提示", "系统繁忙");
		                       }
						 }
					 });
			 }	        	               
 }
 
 function queryDays(){
   var curYear = $("#curYear").val();
   var urlDay="<%=request.getContextPath()%>/holiday/queryList.do?curYear="+curYear;
       window.location.href = urlDay;
 }

			                
</script>
</head>
<body>

	<table style="text-align: center;margin-left: 300px;font-size: 14px;">
		<tr style="color: black;" >
			<td >
			   年份
			    <select id="curYear" name="curYear" >
			   	 	<c:forEach var="yearItem" items="${year20s}"  varStatus="vs0">	
			      	  <option value="${yearItem}" 
			      	 		<c:if test="${curYear==yearItem}"> selected="selected" </c:if>
			      	  >${yearItem}</option>
			        </c:forEach>
			    </select>
			     
			</td>
			<td >
				<input type="button" onclick="queryDays()" id="query" name="query" value=" 查询   " /> 
			</td>
			<td>
				<input type="button" onclick="saveDays()" id="tijiao" name="tijiao" value=" 保存  " /> 
			</td>
		</tr>
	</table>
	
	
	<table>
	
	<!-- 1行 -->
		<tr>
			<c:forEach var="curMonth" items="${holidayList}" begin="0" end="2" step="1" varStatus="vs0">
				
							<td style="padding: 10px;">
									<table>
										<tr >
										  <td colspan="7" style="color: black;">${curMonth.holidayDate}</td>
										</tr>
										<tr >
										  <td  style="color: black;">&nbsp;&nbsp;&nbsp;&nbsp;一</td>
										  <td  style="color: black;">&nbsp;&nbsp;&nbsp;&nbsp;二</td>
										  <td  style="color: black;">&nbsp;&nbsp;&nbsp;&nbsp;三</td>
										  <td  style="color: black;">&nbsp;&nbsp;&nbsp;&nbsp;四</td>
										  <td  style="color: black;">&nbsp;&nbsp;&nbsp;&nbsp;五</td>
										  <td  style="color: black;">&nbsp;&nbsp;&nbsp;&nbsp;六</td>
										  <td  style="color: black;">&nbsp;&nbsp;&nbsp;&nbsp;日</td>
										</tr>
				
												<c:forEach var="curDay" items="${curMonth.holiday7DayList}" varStatus="vs">
														<c:if test="${vs.index%7==0}">
															<tr>
														</c:if>
														<td <c:if test="${curDay.ifchecked}"> style="color: red;" </c:if>>
														    
														    
														    <c:choose>
															    <c:when test="${curDay.dayNum<10}">
															    	&nbsp;&nbsp;${curDay.dayNum}
															    </c:when>
															    <c:otherwise>
															    	${curDay.dayNum}
															    </c:otherwise>
														    </c:choose>
														    
														    <c:if test="${not empty curDay.dayNum}">
																<input  id="checkDate" name="checkDate" 
																<c:if test="${curDay.ifchecked}"> checked="checked" </c:if>
																 type="checkbox" value="${curDay.date}">
															 </c:if>
															 
														</td>
														<c:if test="${vs.index%7==6 }">
															</tr>
														</c:if>
											   </c:forEach>
												<c:if test="${vs0.index==1}">
															<tr><td colspan="7">&nbsp;</td></tr>
												</c:if>
									</table>
							</td>
				
			</c:forEach>
		</tr>
		
		<!-- 2行 -->
		<tr>
			<c:forEach var="curMonth" items="${holidayList}" begin="3" end="5" step="1" varStatus="vs0">
				
							<td style="padding: 10px;">
									<table>
										<tr >
										  <td colspan="7" style="color: black;">${curMonth.holidayDate}</td>
										</tr>
										<tr >
										  <td  style="color: black;">&nbsp;&nbsp;&nbsp;&nbsp;一</td>
										  <td  style="color: black;">&nbsp;&nbsp;&nbsp;&nbsp;二</td>
										  <td  style="color: black;">&nbsp;&nbsp;&nbsp;&nbsp;三</td>
										  <td  style="color: black;">&nbsp;&nbsp;&nbsp;&nbsp;四</td>
										  <td  style="color: black;">&nbsp;&nbsp;&nbsp;&nbsp;五</td>
										  <td  style="color: black;">&nbsp;&nbsp;&nbsp;&nbsp;六</td>
										  <td  style="color: black;">&nbsp;&nbsp;&nbsp;&nbsp;日</td>
										</tr>
												<c:forEach var="curDay" items="${curMonth.holiday7DayList}" varStatus="vs">
														<c:if test="${vs.index%7==0}">
															<tr>
														</c:if>
														<td <c:if test="${curDay.ifchecked}"> style="color: red;" </c:if>>
														    
														    <c:choose>
															    <c:when test="${curDay.dayNum<10}">
															    	&nbsp;&nbsp;${curDay.dayNum}
															    </c:when>
															    <c:otherwise>
															    	${curDay.dayNum}
															    </c:otherwise>
														    </c:choose>
														    <c:if test="${not empty curDay.dayNum}">
																<input  id="checkDate" name="checkDate" 
																<c:if test="${curDay.ifchecked}"> checked="checked" </c:if>
																 type="checkbox" value="${curDay.date}" />
															 </c:if>
														</td>
														<c:if test="${vs.index%7==6 }">
															</tr>
														</c:if>
											   </c:forEach>
												
									</table>
							</td>
				
			</c:forEach>
		</tr>
		
		<!-- 3行 -->
		<tr>
			<c:forEach var="curMonth" items="${holidayList}" begin="6" end="8" step="1" varStatus="vs0">
				
							<td style="padding: 10px;">
									<table>
										<tr >
										  <td colspan="7" style="color: black;">${curMonth.holidayDate}</td>
										</tr>
										<tr >
										  <td  style="color: black;">&nbsp;&nbsp;&nbsp;&nbsp;一</td>
										  <td  style="color: black;">&nbsp;&nbsp;&nbsp;&nbsp;二</td>
										  <td  style="color: black;">&nbsp;&nbsp;&nbsp;&nbsp;三</td>
										  <td  style="color: black;">&nbsp;&nbsp;&nbsp;&nbsp;四</td>
										  <td  style="color: black;">&nbsp;&nbsp;&nbsp;&nbsp;五</td>
										  <td  style="color: black;">&nbsp;&nbsp;&nbsp;&nbsp;六</td>
										  <td  style="color: black;">&nbsp;&nbsp;&nbsp;&nbsp;日</td>
										</tr>
												<c:forEach var="curDay" items="${curMonth.holiday7DayList}" varStatus="vs">
														<c:if test="${vs.index%7==0}">
															<tr>
														</c:if>
														<td <c:if test="${curDay.ifchecked}"> style="color: red;" </c:if>>
														    
														    <c:choose>
															    <c:when test="${curDay.dayNum<10}">
															    	&nbsp;&nbsp;${curDay.dayNum}
															    </c:when>
															    <c:otherwise>
															    	${curDay.dayNum}
															    </c:otherwise>
														    </c:choose>
														    <c:if test="${not empty curDay.dayNum}">
																<input  id="checkDate" name="checkDate" 
																<c:if test="${curDay.ifchecked}"> checked="checked" </c:if>
																 type="checkbox" value="${curDay.date}"/>
															 </c:if>
														</td>
														<c:if test="${vs.index%7==6 }">
															</tr>
														</c:if>
											   </c:forEach>
												
									</table>
							</td>
				
			</c:forEach>
		</tr>
		
		<!-- 4行 -->
		<tr>
			<c:forEach var="curMonth" items="${holidayList}" begin="9" end="11" step="1" varStatus="vs0">
				
							<td style="padding: 10px;">
									<table>
										<tr >
										  <td colspan="7" style="color: black;">${curMonth.holidayDate}</td>
										</tr>
										<tr >
										  <td  style="color: black;">&nbsp;&nbsp;&nbsp;&nbsp;一</td>
										  <td  style="color: black;">&nbsp;&nbsp;&nbsp;&nbsp;二</td>
										  <td  style="color: black;">&nbsp;&nbsp;&nbsp;&nbsp;三</td>
										  <td  style="color: black;">&nbsp;&nbsp;&nbsp;&nbsp;四</td>
										  <td  style="color: black;">&nbsp;&nbsp;&nbsp;&nbsp;五</td>
										  <td  style="color: black;">&nbsp;&nbsp;&nbsp;&nbsp;六</td>
										  <td  style="color: black;">&nbsp;&nbsp;&nbsp;&nbsp;日</td>
										</tr>
												<c:forEach var="curDay" items="${curMonth.holiday7DayList}" varStatus="vs">
														<c:if test="${vs.index%7==0}">
															<tr>
														</c:if>
														<td <c:if test="${curDay.ifchecked}"> style="color: red;" </c:if>>
														    
														    <c:choose>
															    <c:when test="${curDay.dayNum<10}">
															    	&nbsp;&nbsp;${curDay.dayNum}
															    </c:when>
															    <c:otherwise>
															    	${curDay.dayNum}
															    </c:otherwise>
														    </c:choose>
														    
														     <c:if test="${not empty curDay.dayNum}">
																<input  id="checkDate" name="checkDate" 
																<c:if test="${curDay.ifchecked}"> checked="checked" </c:if>
																 type="checkbox" value="${curDay.date}" />
															 </c:if>
														</td>
														<c:if test="${vs.index%7==6 }">
															</tr>
														</c:if>
											   </c:forEach>
												
									</table>
							</td>
				
			</c:forEach>
		</tr>

	</table>
</body>
</html>
