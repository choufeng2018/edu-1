<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>课程表管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			laydate({
	            elem: '#courseDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });
		});
		
		function validata(checkparam, tip)
		{
			var result = {tag:"1", str:""};
			if (checkparam == "" || checkparam == null)
			{
				result.tag = "0";
				result.str = "请配置校验参数";
			} else if ($("#"+checkparam+"Id").val() == "")
			{
				result.tag = "0";
				result.str = "请选择"+tip+"！";
			} else { 
				result.str = checkparam+"="+$("#"+checkparam+"Id").val();
			}
			return result;
		}
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>课程表列表 </h5>
		<div class="ibox-tools">
			<a class="collapse-link">
				<i class="fa fa-chevron-up"></i>
			</a> 
			<a class="close-link">
				<i class="fa fa-times"></i>
			</a>
		</div>
	</div>
    
    <div class="ibox-content">
	<sys:message content="${message}"/>
	
	<!--查询条件-->
	<div class="row">
	<div class="col-sm-12">
	<form:form id="searchForm" modelAttribute="TCourseTimetable" action="${ctx}/subject/tCourseTimetable/checklist" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<!-- <table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/>支持排序 -->
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row"> 
	</div>
	
	<!-- 表格 -->
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th> <input type="checkbox" class="i-checks"></th>
				<th  class="sort-column courseDate">日期</th>
				<th  class="sort-column teactime">上课时间</th>
				<th  class="sort-column roomDesc">教室</th>
				<th  class="sort-column status">状态</th>
				<th  class="sort-column teacherName">教师</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tCourseTimetable">
			<tr>
				<td> <input type="checkbox" id="${tCourseTimetable.id}" class="i-checks"></td>
				<td>
					<fmt:formatDate value="${tCourseTimetable.courseDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					${tCourseTimetable.teactime}
				</td>
				<td>
					${tCourseTimetable.room.roomDesc}
				</td>
				<td>
					${fns:getDictLabel(tCourseTimetable.status, 'timetable_status', '')}
				</td>
				<td>
					${tCourseTimetable.teacher.name}
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	 
	</div>
	</div>
</div>
</body>
</html>