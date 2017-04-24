<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>报名管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
		
		function addSm() {
			openDialog("请假", "${ctx}/teaching/tClassStudent/todoleavelist", "900px", "550px", "");
		}
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>请假列表 </h5>
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
	<form:form id="searchForm" modelAttribute="TClassStudent" action="${ctx}/teaching/tClassStudent/leavelist" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div class="form-group">
			<span>学生姓名：</span>
				<sys:gridselect url="${ctx}/school/tSchoolStudent/selectlist" id="student" name="student.id"  value="${TClassStudent.student.id}"  title="选择学生" labelName="student.name" 
					labelValue="${TClassStudent.student.name}" cssClass="form-control required" fieldLabels="" fieldKeys="" searchLabel="" searchKey="" ></sys:gridselect>
			<span>课程：</span>
				<sys:gridselect url="${ctx}/subject/tCourseClass/selectlist" id="courseclass" name="courseclass.id"  value="${TClassStudent.courseclass.id}"  title="选择课程班级" labelName="courseclass.classDesc" 
					labelValue="${TClassStudent.courseclass.classDesc}" cssClass="form-control required" fieldLabels="" fieldKeys="" searchLabel="" searchKey="" width="900px"></sys:gridselect>
			
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<button class="btn btn-white btn-sm" data-toggle="tooltip" data-placement="left" onclick="addSm()" title="请假"><i class="glyphicon glyphicon-plus"></i> 请假</button>
			<button class="btn btn-white btn-sm" data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
		</div>
		<div class="pull-right">
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
		</div>
	</div>
	</div>
	
	<!-- 表格 -->
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th  class="sort-column name">学生姓名</th>
				<!-- <th  class="sort-column name">联系方式</th> -->
				<th  class="sort-column classId">课程</th>
				<th  class="sort-column teacher">教师</th>
				<th  class="sort-column begindate">开课日期</th>
				<th  class="sort-column createDate">报名时间</th>
				<th  class="sort-column amount">缴费总金额</th>
				<th  class="sort-column amount">缴费总课次</th>
				<th  class="sort-column ispay">已完成课次</th>
				<th  class="sort-column balance">请假课次</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tClassStudent">
			<tr>
				<td><a  href="#" onclick="openDialogView('查看学生信息', '${ctx}/school/tSchoolStudent/form?id=${tClassStudent.student.id}','800px', '500px')">
					${tClassStudent.student.name}
				</a></td>
				<!--<td>
					${tClassStudent.student.tel}
				</td> -->
				<td><a  href="#" onclick="openDialogView('查看报名详情', '${ctx}/teaching/tClassStudent/form?id=${tClassStudent.id}','800px', '500px')">
					${tClassStudent.courseclass.classDesc}
				</a></td>
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
					${tClassStudent.amount}
				</td>
				<td>
					${tClassStudent.zks}
				</td>
				<td>
					${tClassStudent.zks}
				</td>
				<td>
					${tClassStudent.leavecount}
				</td>
				
				<td>
					<c:if test="${tClassStudent.leavecount>0}"></c:if>
					<shiro:hasPermission name="teaching:tClassStudentCheck:list">
						<a href="#" onclick="openDialogView('查看请假详情', '${ctx}/teaching/tClassStudentCheck/stuCheckLeavelist?tCourseTimetable.courseclass.id=${tClassStudent.courseclass.id}&studentId=${tClassStudent.student.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看请假详情</a>
					</shiro:hasPermission>
					<!--  
					<shiro:hasPermission name="teaching:tClassStudent:edit">
    					<a href="#" onclick="openDialog('请假', '${ctx}/teaching/tClassStudentCheck/leaveform?tCourseTimetable.courseclass.id=${tClassStudent.courseclass.id}&studentId=${tClassStudent.student.id}','800px', '450px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 请假</a>
    				</shiro:hasPermission>
    				-->
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