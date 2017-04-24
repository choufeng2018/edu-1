<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>退课管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
		function addSm(){
			openDialogView("学生转班","${ctx}/teaching/tClassStudent/quitlist", "900px", "600px", "");
		}
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>退课记录列表 </h5>
		<div class="ibox-tools">
			<a class="collapse-link">
				<i class="fa fa-chevron-up"></i>
			</a>
		</div>
	</div>
    
    <div class="ibox-content">
	<sys:message content="${message}"/>
	
	<!--查询条件-->
	<div class="row">
	<div class="col-sm-12">
	<form:form id="searchForm" modelAttribute="TClassStudentQuit" action="${ctx}/teaching/tClassStudentQuit/list" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<!--<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/> 支持排序 -->
		<div class="form-group">
			<span>学生姓名：</span>
				<form:input path="tClassStudent.student.name" htmlEscape="false" maxlength="30"  class=" form-control input-sm"/>
		</div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<!--
			<shiro:hasPermission name="teaching:tClassStudentQuit:add">
				<table:addRow url="${ctx}/teaching/tClassStudentQuit/form" title="退课"></table:addRow>
			</shiro:hasPermission>
			<shiro:hasPermission name="teaching:tClassStudentQuit:edit">
			    <table:editRow url="${ctx}/teaching/tClassStudentQuit/form" title="退课" id="contentTable"></table:editRow>
			</shiro:hasPermission>
			<shiro:hasPermission name="teaching:tClassStudentQuit:del">
				<table:delRow url="${ctx}/teaching/tClassStudentQuit/deleteAll" id="contentTable"></table:delRow>
			</shiro:hasPermission>
			<shiro:hasPermission name="teaching:tClassStudentQuit:import">
				<table:importExcel url="${ctx}/teaching/tClassStudentQuit/import"></table:importExcel> 
			</shiro:hasPermission>
			-->
			<shiro:hasPermission name="teaching:tClassStudentQuit:add">
				<button class="btn btn-white btn-sm" data-toggle="tooltip" data-placement="left" onclick="addSm()" title="学生退课"><i class="glyphicon glyphicon-plus"></i> 退课</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="teaching:tClassStudentQuit:export">
	       		<table:exportExcel url="${ctx}/teaching/tClassStudentQuit/export"></table:exportExcel><!-- 导出按钮 -->
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
				
				<th  class="sort-column name">学生姓名</th>
				<th  class="sort-column csId">课程</th>
				<th  class="sort-column amount">缴费总金额</th>
				<th  class="sort-column perAmount">单课时费用</th>
				<th  class="sort-column backAmount">退费金额</th>
				<th  class="sort-column classAll">总课时</th>
				<th  class="sort-column classComplete">已完成课时</th>
				<th  class="sort-column classRest">剩余课时</th>
				<th  class="sort-column quitdate">退课日期</th>
				<th  class="sort-column cc">操作人</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tClassStudentQuit">
			<tr>
				<!--<td> <input type="checkbox" id="${tClassStudentQuit.id}" class="i-checks"></td> -->
				<td>
					${tClassStudentQuit.tClassStudent.student.name}
				</td>
				<td>
					${tClassStudentQuit.tClassStudent.courseclass.classDesc}
				</td>
				
				<td>
					${tClassStudentQuit.tClassStudent.courseclass.cost}
				</td>
				<td>
					${tClassStudentQuit.perAmount}
				</td>
				<td>
					${tClassStudentQuit.backAmount}
				</td>
				<td>
					${tClassStudentQuit.tClassStudent.courseclass.classHour}
				</td>
				<td>
					${tClassStudentQuit.classComplete}
				</td>
				<td>
					${tClassStudentQuit.classRest}
				</td>
				<td>
					<fmt:formatDate value="${tClassStudentQuit.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${tClassStudentQuit.user.name}
				</td>
				<td>
					<shiro:hasPermission name="teaching:tClassStudentQuit:view">
						<a href="#" onclick="openDialogView('查看退课', '${ctx}/teaching/tClassStudentQuit/form?id=${tClassStudentQuit.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<!--<shiro:hasPermission name="teaching:tClassStudentQuit:edit">
    					<a href="#" onclick="openDialog('修改退课', '${ctx}/teaching/tClassStudentQuit/form?id=${tClassStudentQuit.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="teaching:tClassStudentQuit:del">
						<a href="${ctx}/teaching/tClassStudentQuit/delete?id=${tClassStudentQuit.id}" onclick="return confirmx('确认要删除该退课吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
					</shiro:hasPermission>-->
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