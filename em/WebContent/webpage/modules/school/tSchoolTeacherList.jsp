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
	<form:form id="searchForm" modelAttribute="TSchoolTeacher" action="${ctx}/school/tSchoolTeacher/list" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<!--<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/> 支持排序 -->
		<div class="form-group">
			<c:if test="${isparent}">
			<span>校区：</span>
				<sys:treeselect id="campus" name="campus.id" value="${TSchoolTeacher.campus.id}" labelName="campus.name" labelValue="${TSchoolTeacher.campus.name}"
							title="校区" url="/sys/office/treeData?type=1" cssClass="form-control required" notAllowSelectParent="false" notAllowSelectRoot="false"/>
			</c:if>
			<span>姓名：</span>
				<form:input path="name" htmlEscape="false" maxlength="30"  class=" form-control input-sm"/>
			<span>专业：</span>
				<sys:treeselect id="course" name="course.id" value="${TSchoolTeacher.course.id}" labelName="course.subjectName" labelValue="${TSchoolTeacher.course.subjectName}"
							title="学科" url="/school/tCommSubject/treeData" cssClass="form-control required" notAllowSelectParent="true" notAllowSelectRoot="true"/>
			<span>状态：</span>
				<form:radiobuttons class="i-checks" path="status" items="${fns:getDictList('teacher_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="school:tSchoolTeacher:add">
				<table:addRow url="${ctx}/school/tSchoolTeacher/form" title="教师信息" width="900px" height="550px"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<!-- 
			<shiro:hasPermission name="school:tSchoolTeacher:import">
				<table:importExcel url="${ctx}/school/tSchoolTeacher/import"></table:importExcel>导入按钮 
			</shiro:hasPermission>
			-->
			<shiro:hasPermission name="school:tSchoolTeacher:export">
	       		<table:exportExcel url="${ctx}/school/tSchoolTeacher/export"></table:exportExcel><!-- 导出按钮 -->
	       	</shiro:hasPermission>
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
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tSchoolTeacher">
			<tr>
				<!-- <td> <input type="checkbox" id="${tSchoolTeacher.id}" class="i-checks"></td> -->
				<td><a  href="#" onclick="openDialogView('查看教师信息', '${ctx}/school/tSchoolTeacher/form?id=${tSchoolTeacher.id}','900px', '550px')">
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
				<td>
					<!--  
					<shiro:hasPermission name="school:tSchoolTeacher:view">
						<a href="#" onclick="openDialogView('查看教师信息', '${ctx}/school/tSchoolTeacher/form?id=${tSchoolTeacher.id}','900px', '550px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					-->
					<c:if test="${(tSchoolTeacher.type==2 && UserUtils.getUserIsParent()) || tSchoolTeacher.type==1}">
					<shiro:hasPermission name="school:tSchoolTeacher:edit">
    					<a href="#" onclick="openDialog('修改教师信息', '${ctx}/school/tSchoolTeacher/form?id=${tSchoolTeacher.id}','900px', '550px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="school:tSchoolTeacher:del">
						<a href="${ctx}/school/tSchoolTeacher/delete?id=${tSchoolTeacher.id}" onclick="return confirmx('确认要删除该教师信息吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
					</shiro:hasPermission> 
					<shiro:hasPermission name="school:tSchoolTeacher:view">
    					<a href="#" onclick="openDialog('课程安排信息', '${ctx}/subject/tCourseClass/teacherlist?teacher.id=${tSchoolTeacher.id}','900px', '600px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看课表</a>
    				</shiro:hasPermission>
    				<c:if test="${tSchoolTeacher.isoto == 1}">
					<shiro:hasPermission name="school:tSchoolTeacherConf:edit">
    					<a href="#" onclick="openDialog('教师1对1教学时间管理', '${ctx}/school/tSchoolTeacherConf/form?teacher.id=${tSchoolTeacher.id}','900px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 教学时间管理</a>
    				</shiro:hasPermission>
    				</c:if>
    				<!--  
    				<shiro:hasPermission name="school:tSchoolTeacherConf:view">
    					<a href="#" onclick="openDialog('查看排课结果', '${ctx}/school/tSchoolTeacherConf/view?teacher.id=${tSchoolTeacher.id}','800px', '500px')" class="btn  btn-success btn-xs" ><i class="fa fa-search-plus"></i> 查看排课结果</a>
    				</shiro:hasPermission>
    				-->
    				</c:if>
    				<!--  
    				<shiro:hasPermission name="school:tSchoolTeacher:edit">
    					<a href="#" onclick="" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看评价</a>
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