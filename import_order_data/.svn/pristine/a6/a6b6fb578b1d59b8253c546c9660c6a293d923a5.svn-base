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
1 >折线图配置举列:
</div> 
	<table style="text-align: center;margin-left: 300px;font-size: 14px;">
		<tr>
			<td align="left">
			  1.1> 数据来源字段返回如下格式数据:
			  </br><img width="700"  height="100" src="<%=request.getContextPath()%>/ext4/img/bar-line-data.png">
			</td>
		</tr>
		<tr>
			<td align="left">
			    1.2> 生成折线图如下：
			    </br><img  width="700"  height="200" src="<%=request.getContextPath()%>/ext4/img/line.png">
			</td>
		</tr>
	</table>
	
<div style="font-size: 20px;color: black;padding: 10px;font-weight: bolder;">
2 >条形图配置举列:
</div> 
<table style="text-align: center;margin-left: 300px;font-size: 14px;">
	<tr>
		<td align="left">
		  2.1> 数据来源字段返回如下格式数据:
		  </br><img width="700"  height="100" src="<%=request.getContextPath()%>/ext4/img/bar-line-data.png">
		</td>
	</tr>
	<tr>
		<td align="left">
		    2.2> 生成折线图如下：
		    </br><img  width="700"  height="200" src="<%=request.getContextPath()%>/ext4/img/bar.png">
		</td>
	</tr>
</table>
	

<div style="font-size: 20px;color: black;padding: 10px;font-weight: bolder;">
3 >饼图配置举列:
</div> 
<table style="text-align: center;margin-left: 300px;font-size: 14px;">
	<tr>
		<td align="left">
		  3.1> 数据来源字段返回如下格式数据:
		  </br><img width="700"  height="150" src="<%=request.getContextPath()%>/ext4/img/pie-data.png">
		</td>
	</tr>
	<tr>
		<td align="left">
		    3.2 > 生成折线图如下：
		    </br><img  width="700"  height="200" src="<%=request.getContextPath()%>/ext4/img/pie.png">
		</td>
	</tr>
</table>



<div style="font-size: 20px;color: black;padding: 10px;font-weight: bolder;">
4 >折线图和条形图混合配置举列:
</div> 
<table style="text-align: center;margin-left: 300px;font-size: 14px;">
	<tr>
		<td align="left">
		  3.1> 数据来源字段返回如下格式数据:
		  </br><img width="700"  height="100" src="<%=request.getContextPath()%>/ext4/img/bar-line-data.png">
		</td>
	</tr>
	<tr>
		<td align="left">
		  3.2> <font color="red"></br>图形类别选择折线图的话,</br>且图形特殊行字段如填写儿科(多个的话用分号隔开),
		  </br>则儿科显示为条形图，</br>其余显示为折线图,</br>反之同理</font>
		  
		</td>
	</tr>
	<tr>
		<td align="left">
		    3.3 > 生成折线图和条形图混合如下：
		    </br><img  width="700"  height="200" src="<%=request.getContextPath()%>/ext4/img/line-bar.png">
		</td>
	</tr>
</table>


<div style="font-size: 20px;color:black;padding: 10px;font-weight: bolder;">
5 >查询条件参数字段配置举列:(多个查询控件关键字用英文逗号隔开)
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
