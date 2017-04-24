<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>课程方案管理</title>
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
		<h5>课程计划班级列表 </h5>
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
	<form:form id="searchForm" modelAttribute="TCourseClass" action="${ctx}/subject/tCourseClass/enterlist" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>校区：</span>
				<sys:treeselect id="campus" name="campus.id" value="${TCourseClass.campus.id}" labelName="campus.name" labelValue="${TCourseClass.campus.name}"
							title="校区" url="/sys/office/treeData?type=1" cssClass="form-control required" notAllowSelectParent="true" notAllowSelectRoot="true"/>
			<span>学科类别：</span>
				<sys:treeselect id="subject" name="subject.id" value="${TCourseClass.subject.id}" labelName="subject.subjectName" labelValue="${TCourseClass.subject.subjectName}"
							title="学科" url="/school/tCommSubject/treeData" cssClass="form-control required" notAllowSelectParent="true" notAllowSelectRoot="true"/>
			<span>班级名称：</span>
				<form:input path="classDesc" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
			<!--  
			<span>班级类型：</span>
				<form:select path="classType"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('course_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			-->
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
				<!-- <th> <input type="checkbox" class="i-checks"></th> -->
				<th  class="classDesc">班级名称</th>
				<th  class="classType">班级类型</th>
				<th  class="sort-column totalStu">人数</th>
				<th  class="sort-column stuNum">已招人数</th>
				<th  class="sort-column recruit">招生对象</th>
				<th  class="classHour">课次</th>
				<th  class="sort-column classMin">时长<br/>(分钟)</th>
				<th  class="sort-column beginDate">开课日期</th>
				<th  class="sort-column week">上课时间</th>
				<th  class="teacherName">教师名称</th>
				<th  class="sort-column roomDesc">教室</th>
				<th  class="sort-column cost">总费用</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tCourseClass">
			<tr>
				<!-- <td> <input type="checkbox" id="${tCourseClass.id}" class="i-checks"></td> -->
				<td><a  href="#" onclick="openDialogView('查看课程方案', '${ctx}/subject/tCourseClass/form?id=${tCourseClass.id}','800px', '500px')">
					${tCourseClass.classDesc}
				</a></td>
				<td>
					${fns:getDictLabel(tCourseClass.classType, 'course_type', '')}
				</td>
				<td>
					${tCourseClass.totalStu}
				</td>
				<td>
					${tCourseClass.stuNum}
				</td>
				<td>
					${tCourseClass.recruit}
				</td>
				<td>
					${tCourseClass.classHour}
				</td>
				<td>
					${tCourseClass.classMin}
				</td>
				<td>
					<fmt:formatDate value="${tCourseClass.beginDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td> 
					${tCourseClass.teactime}
				</td>
				<td>
				<a  href="#" onclick="openDialogView('查看教师信息', '${ctx}/school/tSchoolTeacher/form?id=${tCourseClass.teacher.id}','800px', '500px')">
					${tCourseClass.teacher.name}
				</a>
				</td>
				<td>
					${tCourseClass.room.roomDesc}
				</td>
				<td>
					${tCourseClass.cost}
				</td>
				<td>
					<c:if test="${tCourseClass.stuNum==null || tCourseClass.totalStu > tCourseClass.stuNum}">
					<shiro:hasPermission name="teaching:tClassStudent:add">
						<a href="#" onclick="openDialog('报名', '${ctx}/teaching/tClassStudent/enterform?ccid=${tCourseClass.id}','800px', '450px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 报名</a>
					</shiro:hasPermission>
					</c:if>
					
					<shiro:hasPermission name="teaching:tClassStudent:list">
						<a href="#" onclick="openDialogView('班级学生列表', '${ctx}/teaching/tClassStudent/stulist?ccid=${tCourseClass.id}','900px', '550px')" class="btn btn-success btn-xs" ><i class="fa fa-search-plus"></i> 学生列表</a>
					</shiro:hasPermission>
					
					<c:if test="${tCourseClass.iscreate == 1}">
						<a href="#" onclick="openDialogView('课程表', '${ctx}/subject/tCourseTimetable/subjectlist?ccid=${tCourseClass.id}','900px', '550px')" class="btn btn-success btn-xs" ><i class="fa fa-search-plus"></i> 查看课程表</a>
    				</c:if>
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