<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>考情管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox"> 
    
    <div class="ibox-content">
	<sys:message content="${message}"/>
	
	<!--查询条件-->
	<div class="row">
	<div class="col-sm-12">
	<form:form id="searchForm" modelAttribute="TClassStudentCheck" action="${ctx}/teaching/tClassStudentCheck/checklist" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/> 
	</form:form>
	<br/>
	</div>
	</div>
	 
	<!-- 表格 -->
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th> <input type="checkbox" class="i-checks"></th>
				<th  class="sort-column studentName">学生</th>
				<th  class="sort-column status">状态（签到、缺勤、请假）</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tClassStudentCheck">
			<tr>
				<td> <input type="checkbox" id="${tClassStudentCheck.studentId}" class="i-checks"></td>
				<td>
					${tClassStudentCheck.studentName}
				</td>
				<td>
					<!-- ${fns:getDictLabel(tClassStudentCheck.status, 'check_status', '')} -->
					<form:radiobuttons path="status" items="${fns:getDictList('check_status')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks "/>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	 
	<br/>
	<br/>
	</div>
	</div>
</div>
</body>
</html>