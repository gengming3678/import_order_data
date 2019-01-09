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


</head>
<body>
<div style="font-size: 20px;color: black;padding: 10px;font-weight: bolder;">
1 >表头名称字段配置举列:
</div> 
	<table style="text-align: center;margin-left: 300px;font-size: 14px;">
		<tr>
			<td align="left">
			  1.1> 表头名称填写如下:<font color="red">(列名与列名之间用英文逗号隔开)</br>
			 列1,列2,列3,列4,列5</font>
			</td>
		</tr>
		<tr>
			<td align="left">
			    生成表头如下：<br>
			    <img  width="700"  height="30" src="<%=request.getContextPath()%>/ext4/img/one-row.png">
			</td>
		</tr>
		
		<tr>
			<td align="left">
			  1.2> 表头名称填写如下:<font color="red">(列名与列名之间用英文逗号隔开,且跨第二行则需用&符号隔开)</br>
			  列1,列2&列2-1&列2-2,列3,列4,列5</font>
			</td>
		</tr>
		<tr>
			<td align="left">
			    生成表头如下：<br>
			    <img  width="700"  height="50" src="<%=request.getContextPath()%>/ext4/img/two-row.png">
			</td>
		</tr>
		
		<tr>
			<td align="left">
			  1.3> 表头名称填写如下:<font color="red">(列名与列名之间用英文逗号隔开,且跨第二行则需用&符号隔开,且跨第三行则需用@符号隔开)</br>
			  列1,列2&列2-1@2-1-1@2-1-2&列2-2@2-2-1@列2-2-2,列3,列4,列5
			  </font>
			</td>
		</tr>
		<tr>
			<td align="left">
			    生成表头如下：<br>
			    <img  width="700"  height="80" src="<%=request.getContextPath()%>/ext4/img/three-row.png">
			</td>
		</tr>
	</table>
	
	
<div style="font-size: 20px;color: black;padding: 10px;font-weight: bolder;">
2 >数据来源字段配置举列:
</div> 
	<table style="text-align: center;margin-left: 300px;font-size: 14px;">
		<tr>
			<td align="left">
			  2.1> 数据来源填写如下:<font color="red">(sql语句)</br>
			 
			 SELECT </br>
			    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;actual_org_organization_name,</br>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;patient_name,</br>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;org_order_organization_name,</br>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;org_operate_organization_name,</br>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;org_fee_type_name,</br>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;org_item_name,</br>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;fee,</br>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;count</br>
			FROM performance_dw.fact.operation_daily</br>
			WHERE </br>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;stat_time >= cast(#startDate# as date)</br>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;and stat_time < dateadd(dd,1,cast(#endDate# as date))</br>

			 </font>
			</td>
		</tr>
		
		<tr>
			<td align="left">
			  2.2> 数据来源填写如下:<font color="red">(存储过程)</br>
			 
			 {call rpt_operation_point_per_doctor_sort(#startDate#,#endDate#)}
			 </font>
			</td>
		</tr>
	</table>
	
	
<div style="font-size: 20px;color:black;padding: 10px;font-weight: bolder;">
3 >查询条件参数字段配置举列:(多个查询控件关键字用英文逗号隔开)
</div> 
	<table style="text-align: center;margin-left: 300px;font-size: 14px;">
		<tr>
			<td align="left">
			  关键字
			</td>
			<td align="right">
			  页面对应查询控件显示
			</td>
		</tr>
		<tr>
			<td align="left">
			   <font color="red">#startDate#</br>
			 </font>
			</td>
			<td align="right">
			   <font color="red">时间段开始时间到日</br>
			 </font>
			</td>
		</tr>
		<tr>
			<td align="left">
			   <font color="red">#endDate#</br>
			 </font>
			</td>
			<td align="right">
			   <font color="red">时间段结束时间到日</br>
			 </font>
			</td>
		</tr>
		
		<tr>
			<td align="left">
			   <font color="red">#startYM#</br>
			 </font>
			</td>
			<td align="right">
			   <font color="red">时间段开始时间到月</br>
			 </font>
			</td>
		</tr>
		
		<tr>
			<td align="left">
			   <font color="red">#endYM#</br>
			 </font>
			</td>
			<td align="right">
			   <font color="red">时间段结束时间到月</br>
			 </font>
			</td>
		</tr>
		
		
		<tr>
			<td align="left">
			   <font color="red">#statYM#</br>
			 </font>
			</td>
			<td align="right">
			   <font color="red">单个时间到月</br>
			 </font>
			</td>
		</tr>
		
		<tr>
			<td align="left">
			   <font color="red">#statYMD#</br>
			 </font>
			</td>
			<td align="right">
			   <font color="red">单个时间到日</br>
			 </font>
			</td>
		</tr>
		
		
		
		<tr>
			<td align="left">
			   <font color="red">#deptName#</br>
			 </font>
			</td>
			<td align="right">
			   <font color="red">科室控件</br>
			 </font>
			</td>
		</tr>
		<!-- 
		<tr>
			<td align="left">
			   <font color="red">#usrCode#</br>
			 </font>
			</td>
			<td align="right">
			   <font color="red">当前登录用户ID</br>
			 </font>
			</td>
		</tr>
		
		<tr>
			<td align="left">
			   <font color="red">#rptId#</br>
			 </font>
			</td>
			<td align="right">
			   <font color="red">当前显示报表ID</br>
			 </font>
			</td>
		</tr> -->
		
		
	</table>
</body>
</html>
