<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>考情管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			laydate({
				elem: '#courseDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
				event: 'focus' //响应事件。如果没有传入event，则按照默认的click
			});
		});
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>待补课学生列表 </h5>
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
	<form:form id="searchForm" modelAttribute="TClassStudentCheck" action="${ctx}/teaching/tClassStudentCheck/missedlist" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<!-- <table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/>支持排序 -->
		<div class="form-group">
			<!--<span>课程：</span>
				<form:input path="studentId" htmlEscape="false" maxlength="30"  class=" form-control input-sm"/>
				<sys:gridselect url="${ctx}/subject/tCourseClass/selectlist" id="courseclass" name="courseclass.id"  value="${tClassStudent.courseclass.id}"  title="选择班级" labelName="courseclass.classDesc" 
							labelValue="${tClassStudent.courseclass.classDesc}" cssClass="form-control required" fieldLabels="" fieldKeys="" searchLabel="" searchKey="" disabled="true"></sys:gridselect>
				-->
			<span>学生：</span>
				<sys:gridselect url="${ctx}/school/tSchoolStudent/selectlist" id="student" name="studentId"  value="${TClassStudentCheck.studentId}"  title="选择学生" labelName="studentName" 
							labelValue="${TClassStudentCheck.studentName}" cssClass="form-control required" fieldLabels="" fieldKeys="name|remarks" searchLabel="" searchKey="" ></sys:gridselect>
			<span>日期：</span>
				<input id="courseDate" name="tCourseTimetable.courseDate" type="text" maxlength="20" class="laydate-icon form-control layer-date "
							value="<fmt:formatDate value="${TClassStudentCheck.tCourseTimetable.courseDate}" pattern="yyyy-MM-dd"/>"/>
			
		</div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
	       <button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
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
				<!--<th> <input type="checkbox" class="i-checks"></th>-->
				<th  class="sort-column studentId" rowspan="2">学生</th>
				<th  class="sort-column ttId" colspan="5">请假课时</th>
				<th  class="sort-column status" rowspan="2">补课状态</th>
				<th  class="sort-column ttId" colspan="4">补课课时</th>
				<th rowspan="2">操作</th>
			</tr>
			<tr>
				<th  class="sort-column ttId">课程</th>
				<th  class="sort-column ttId">授课方式</th>
				<th  class="sort-column coursedate">日期</th>
				<th  class="sort-column teacher">任课教师</th>
				<th  class="sort-column status">请假时间</th>
				 
				<th  class="sort-column ttId">课程</th>
				<th  class="sort-column coursedate">日期</th>
				<th  class="sort-column teacher">任课教师</th>
				<th  class="sort-column ttId">教室</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tClassStudentCheck">
			<tr>
				<!--<td> <input type="checkbox" id="${tClassStudentCheck.id}" class="i-checks"></td>-->
				<td>
					${tClassStudentCheck.studentName}
				</td>
				<td>
					${tClassStudentCheck.tCourseTimetable.courseclass.classDesc}
				</td>
				<td>
					${fns:getDictLabel(tClassStudentCheck.tCourseTimetable.courseclass.classType, 'course_type', '')}
				</td> 
				<td>
					<fmt:formatDate value="${tClassStudentCheck.tCourseTimetable.courseDate}" pattern="yyyy-MM-dd"/>
					(${tClassStudentCheck.tCourseTimetable.teactime})
				</td>
				<td>
					${tClassStudentCheck.tCourseTimetable.teacher.name}
				</td>
				<td>
					<fmt:formatDate value="${tClassStudentCheck.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				
				<td> 
					${fns:getDictLabel(tClassStudentCheck.bkstatus, 'bkstatus', '未设置')}
				</td>
				
				<td>
					${tClassStudentCheck.parent.tCourseTimetable.courseclass.classDesc}
				</td>
				<td>
					<fmt:formatDate value="${tClassStudentCheck.parent.tCourseTimetable.courseDate}" pattern="yyyy-MM-dd"/>
					 ${tClassStudentCheck.parent.tCourseTimetable.teactime}
				</td> 
				<td>
					${tClassStudentCheck.parent.tCourseTimetable.teacher.name}
				</td>
				<td>
					${tClassStudentCheck.parent.tCourseTimetable.room.roomDesc}
				</td> 
				
				<td>
					<shiro:hasPermission name="teaching:tClassStudentCheck:edit">
    				<c:if test="${tClassStudentCheck.bkstatus != '1'}">
    					<a href="#" onclick="openDialog('补课', '${ctx}/teaching/tClassStudentCheck/bkform?parent.id=${tClassStudentCheck.id}','900px', '550px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 补课设置</a>
    				</c:if>
    				<c:if test="${tClassStudentCheck.bkstatus == '1'}">
    					<a href="#" onclick="openDialogView('查看补课', '${ctx}/teaching/tClassStudentCheck/bkform?parent.id=${tClassStudentCheck.id}','900px', '550px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看补课情况</a>
    				</c:if>
    				</shiro:hasPermission>
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