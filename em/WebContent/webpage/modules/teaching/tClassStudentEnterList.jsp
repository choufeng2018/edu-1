<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>报名管理</title>
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
	<form:form id="searchForm" modelAttribute="TClassStudent" action="${ctx}/teaching/tClassStudent/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	</form:form>
	</div>
	</div>
	 
	<div class="row">
	<div class="col-sm-12">
	</div>
	</div>
	
	<!-- 表格 -->
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th  class="sort-column name">学生姓名</th>
				<th  class="sort-column ispay">是否已缴费</th>
				<th  class="sort-column amount">缴费总金额</th>
				<th  class="sort-column balance">可用余额</th>
				<th  class="sort-column status">状态</th>
				<th  class="sort-column employeeCode">课程顾问</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tClassStudent">
			<tr>
				<td>
					${tClassStudent.student.name}
				</td>
				<td>
					${fns:getDictLabel(tClassStudent.ispay, 'yes_no', '')}
				</td>
				<td>
					${tClassStudent.amount}
				</td>
				<td>
					${tClassStudent.balance}
				</td>
				<td>
					${fns:getDictLabel(tClassStudent.status, 'enter_status', '')}
				</td>
				<td>
					${tClassStudent.cc.name}
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