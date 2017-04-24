<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>学生信息管理</title>
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
		<h5>学生信息列表 </h5>
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
	<form:form id="searchForm" modelAttribute="TSchoolStudent" action="${ctx}/school/tSchoolStudent/list" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/> 
		<div class="form-group">
			<span>姓名：</span>
				<form:input path="name" htmlEscape="false" maxlength="30"  class=" form-control input-sm"/> 
			<c:if test="${isparent}">
			<span>校区：</span>
				<sys:treeselect id="campus" name="campus.id" value="${TSchoolStudent.campus.id}" labelName="campus.name" labelValue="${TSchoolStudent.campus.name}"
					title="校区" url="/sys/office/treeData?type=1" cssClass="form-control required" notAllowSelectParent="true" notAllowSelectRoot="true"/>
			</c:if>
			<span>状态：</span>
				<form:radiobuttons class="i-checks" path="status" items="${fns:getDictList('student_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="school:tSchoolStudent:add">
				<table:addRow url="${ctx}/school/tSchoolStudent/form" title="学生信息" label="学生登记"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission> 
			<shiro:hasPermission name="school:tSchoolStudent:export">
	       		<table:exportExcel url="${ctx}/school/tSchoolStudent/export"></table:exportExcel><!-- 导出按钮 -->
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
				<th> <input type="checkbox" class="i-checks"></th>
				<th  class="sort-column campusId">姓名</th>
				<th  class="sort-column sex">性别</th>
				<th  class="sort-column age">年龄</th>
				<th  class="sort-column birthday">出生年月</th>
				<th  class="sort-column name">所属校区</th>
				<th  class="sort-column tel">联系电话</th>
				<th  class="sort-column subjects">意向学科</th>
				<!-- <th  class="sort-column account">登陆账号</th> -->
				<th  class="sort-column status">学员状态</th>
				<th  class="sort-column cc">课程顾问</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tSchoolStudent">
			<tr>
				<td> <input type="checkbox" id="${tSchoolStudent.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看学生信息', '${ctx}/school/tSchoolStudent/form?id=${tSchoolStudent.id}','800px', '500px')">
					${tSchoolStudent.name}
				</a></td>
				<td>
					${fns:getDictLabel(tSchoolStudent.sex, 'sex', '')}
				</td>
				<td>
					${tSchoolStudent.age}
				</td>
				<td>
					<fmt:formatDate value="${tSchoolStudent.birthday}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					${tSchoolStudent.campus.name}
				</td>
				<td>
					${tSchoolStudent.tel}
				</td>
				<td>
					${tSchoolStudent.subjects}
				</td>
				<!-- <td>
					${tSchoolStudent.account}
				</td> -->
				<td>
					${fns:getDictLabel(tSchoolStudent.status, 'student_status', '')}
				</td>
				<td>
					${tSchoolStudent.cc.name}
				</td>
				<td>
					<shiro:hasPermission name="school:tSchoolStudent:view">
						<a href="#" onclick="openDialogView('查看学生信息', '${ctx}/school/tSchoolStudent/form?id=${tSchoolStudent.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="school:tSchoolStudent:edit">
    					<a href="#" onclick="openDialog('修改学生信息', '${ctx}/school/tSchoolStudent/form?id=${tSchoolStudent.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<c:if test="${tSchoolStudent.status != 2}">
    				<shiro:hasPermission name="school:tSchoolStudent:del">
						<a href="${ctx}/school/tSchoolStudent/delete?id=${tSchoolStudent.id}" onclick="return confirmx('切勿随意删除学生信息，以免造成不必要的损失，确认要删除该学生信息吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
					</shiro:hasPermission>
					</c:if>
					<shiro:hasPermission name="school:tSchoolStudent:view">
						<a href="#" onclick="openDialogView('查看已报名课程', '${ctx}/teaching/tClassStudent/viewlist?student.id=${tSchoolStudent.id}','900px', '550px')"   class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i> 已报课程</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="teaching:tClassStudent:add">
						<a href="#" onclick="openDialog('报名（大班制）', '${ctx}/teaching/tClassStudent/enterform?student.id=${tSchoolStudent.id}&source=studentlist','900px', '500px')"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 报名（大班制）</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="teaching:tClassStudent:add">
						<a href="#" onclick="openDialog('报名（1对1）', '${ctx}/teaching/tClassStudent/enterformSmW?student.id=${tSchoolStudent.id}&source=studentlist','900px', '550px')"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 报名（1对1）</a>
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