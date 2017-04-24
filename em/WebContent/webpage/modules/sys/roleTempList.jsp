]<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>角色管理</title>
	<meta name="decorator" content="default"/>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
			<h5>角色模板列表 </h5>
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
	
		<!-- 查询条件 -->
	<div class="row">
	<div class="col-sm-12">
	<form:form id="searchForm" modelAttribute="role" action="${ctx}/sys/role/templist" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
	 
	</form:form>
	<br/>
	</div>
	</div>
	
		<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="sys:role:add">
				<table:addRow url="${ctx}/sys/role/tempform" title="角色" width="700px" height="450px"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
	       <button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
		
			</div>
	</div>
	</div>
	
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th><input type="checkbox" class="i-checks"></th>
				<th>角色名称</th>
				<th>英文名称</th>
				<shiro:hasPermission name="sys:role:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${list}" var="role">
				<tr>
					<td> <input type="checkbox" id="${role.id}" class="i-checks"></td>
					<td>${role.name}</td>
					<td>${role.enname}</td>
					<td>
						<shiro:hasPermission name="sys:role:view">
						<a href="#" onclick="openDialogView('查看角色', '${ctx}/sys/role/tempform?id=${role.id}','600px', '400px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="sys:role:edit"> 
						<a href="#" onclick="openDialog('修改角色', '${ctx}/sys/role/tempform?id=${role.id}','600px', '400px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="sys:role:del"> 
						<a href="${ctx}/sys/role/deleteTemp?id=${role.id}" onclick="return confirmx('确认要删除该角色吗？', this.href)" class="btn  btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="sys:role:assign"> 
						<a href="#" onclick="openDialog('权限设置', '${ctx}/sys/role/tempauth?id=${role.id}','350px', '550px')" class="btn btn-primary btn-xs" ><i class="fa fa-edit"></i> 权限设置</a> 
						</shiro:hasPermission>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	</div>
	</div>
	</div>
</body>
</html>