<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>教师信息管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>教师信息列表 </h5>
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
	</div>
	</div>
	
	
	<!-- 表格 -->
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr> 
				<th  class="sort-column name">姓名</th>
				<th  class="sort-column sex">性别</th>
				<th  class="sort-column age">年龄</th>
				<th  class="sort-column birthday">出生年月</th>
				<th  class="sort-column tel">联系电话</th>
				<th  class="sort-column course">专业</th>
				<th  class="sort-column campusId">所属校区</th>
				<th  class="sort-column number">工号</th>
				<th  class="sort-column account">登录账号</th>
				<th  class="sort-column level">级别</th>
				<th  class="sort-column type">类型</th>
				<th  class="sort-column isoto">1对1教学</th>
				<th  class="sort-column status">状态</th> 
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tSchoolTeacher">
			<tr> 
				<td><a  href="#" onclick="openDialogView('查看教师信息', '${ctx}/school/tSchoolTeacher/form?id=${tSchoolTeacher.id}','800px', '500px')">
					${tSchoolTeacher.name}
				</a></td>
				<td>
					${fns:getDictLabel(tSchoolTeacher.sex, 'sex', '')}
				</td>
				<td>
					${tSchoolTeacher.age}
				</td>
				<td>
					<fmt:formatDate value="${tSchoolTeacher.birthday}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					${tSchoolTeacher.tel}
				</td>
				<td>
					${tSchoolTeacher.course.subjectName}
				</td>
				<td>
					${tSchoolTeacher.campus.name}
				</td>
				<td>
					${tSchoolTeacher.number}
				</td>
				<td>
					${tSchoolTeacher.account}
				</td>
				<td>
					${fns:getDictLabel(tSchoolTeacher.level, 'teacher_level', '')}
				</td>
				<td>
					${fns:getDictLabel(tSchoolTeacher.type, 'teacher_type', '')}
				</td>
				<td>
					${fns:getDictLabel(tSchoolTeacher.isoto, 'is_oto', '')}
				</td>
				<td>
					${fns:getDictLabel(tSchoolTeacher.type, 'teacher_status', '')}
				</td> 
			</tr>
		</c:forEach>
		</tbody>
	</table>
	
		<!-- 分页代码 -->
	<table:page page="${page}"></table:page>
	<br/>
	<br/>
	</div>
	</div>
</div>
</body>
</html>