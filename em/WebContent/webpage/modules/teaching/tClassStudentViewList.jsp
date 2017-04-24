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
 
	<!-- 表格 -->
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th  class="sort-column name">学生姓名</th>
				<th  class="sort-column classId">课程</th>
				<th  class="sort-column classId">教师</th>
				<th  class="sort-column classId">开课日期</th>
				<th  class="sort-column createDate">报名时间</th>
				<th  class="sort-column ispay">是否已缴费</th>
				<th  class="sort-column paytype">缴费方式</th>
				<th  class="sort-column amount">缴费金额</th>
				<th  class="sort-column zks">总课时</th>
				<th  class="sort-column ywc">已完成课时</th>
				<th  class="sort-column status">课程状态</th>
				<th  class="sort-column employeeCode">课程顾问</th>
				<th  class="sort-column status">报名表状态</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tClassStudent">
			<tr>
				<td>
					${tClassStudent.student.name}
				</td>
				<td><a  href="#" onclick="openDialogView('查看报名详情', '${ctx}/teaching/tClassStudent/form?id=${tClassStudent.id}','800px', '500px')">
					${tClassStudent.courseclass.classDesc}
				</a>
				</td>
				<td>
					${tClassStudent.courseclass.teacher.name}
				</td>
				<td>
					<fmt:formatDate value="${tClassStudent.courseclass.beginDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					<fmt:formatDate value="${tClassStudent.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${fns:getDictLabel(tClassStudent.ispay, 'yes_no', '')}
				</td>
				<td>
					${fns:getDictLabel(tClassStudent.paytype, 'pay_type', '')}
				</td>
				<td>
					${tClassStudent.amount}
				</td>
				<td>
					${tClassStudent.zks}
				</td>
				<td>
					${tClassStudent.ywcks}
				</td>
				<td>
					${fns:getDictLabel(tClassStudent.courseclass.status, 'course_status', '')}
				</td>
				<td>
					${tClassStudent.cc.name}
				</td>
				<td>
					${fns:getDictLabel(tClassStudent.status, 'enter_status', '')}
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