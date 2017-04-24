<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
	<title>班级管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
	</script>
</head>
<body class="gray-bg">
<%@ include file="/webpage/include/echarts.jsp"%>
	<div class="wrapper wrapper-content">
	<div >
		<!--  -->
		<div id="pie"  class="main000" style="float:left"></div>
		<echarts:pie
		    id="pie"
			title="课程统计" 
			subtitle="班级人数"
			orientData="${orientData}"/>
			
		<div id="pie2" class="main000" style="float:right"></div>
		<echarts:pie
		    id="pie2"
			title="人数统计" 
			subtitle="各分校教授、学生数"
			orientData="${orientData}"/>
		</div>
		
			
		<div id="content-main" class="mainheight" style="height:100%">
			<iframe class="J_iframe" name="iframe0" width="100%" height="100%" src="${ctx}/subject/tCourseTimetable/daychecklist" frameborder="0" data-id="" seamless></iframe>
		</div>
	</div>
	</div>
</body>
</html>