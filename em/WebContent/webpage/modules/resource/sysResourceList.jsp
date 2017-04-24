<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>系统资源管理</title>
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
		<h5>系统资源列表 </h5>
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
	<form:form id="searchForm" modelAttribute="sysResource" action="${ctx}/resource/sysResource/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>资源名称：</span>
				<form:input path="name" htmlEscape="false" maxlength="50"  class=" form-control input-sm"/>
			<span>是否收费：</span>
				<form:radiobuttons class="i-checks" path="isPrice" items="${fns:getDictList('is_price')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="resource:sysResource:edit">
			    <table:editRow url="${ctx}/resource/sysResource/form" title="系统资源" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="resource:sysResource:export">
	       		<table:exportExcel url="${ctx}/resource/sysResource/export"></table:exportExcel><!-- 导出按钮 -->
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
				<th  class="sort-column name">资源名称</th>
				<th  class="sort-column level">资源级别</th>
				<th  class="sort-column type">资源类型</th>
				<th  class="sort-column isPrice">是否收费</th>
				<th  class="sort-column price">资源价格</th>
				<th  class="sort-column unit">资源单位</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="sysResource">
			<tr>
				<td> <input type="checkbox" id="${sysResource.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看系统资源', '${ctx}/resource/sysResource/form?id=${sysResource.id}','800px', '500px')">
					${sysResource.name}
				</a></td>
				<td>
					${sysResource.level}
				</td>
				<td>
					${fns:getDictLabel(sysResource.type, 'resource_type', '')}
				</td>
				<td>
					${fns:getDictLabel(sysResource.isPrice, 'is_price', '')}
				</td>
				<td>
					${sysResource.price}
				</td>
				<td>
					${sysResource.unit}
				</td>
				<td>
					<shiro:hasPermission name="resource:sysResource:view">
						<a href="#" onclick="openDialogView('查看系统资源', '${ctx}/resource/sysResource/form?id=${sysResource.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="resource:sysResource:edit">
    					<a href="#" onclick="openDialog('修改系统资源', '${ctx}/resource/sysResource/form?id=${sysResource.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="resource:sysResource:del">
						<a href="${ctx}/resource/sysResource/delete?id=${sysResource.id}" onclick="return confirmx('确认要删除该系统资源吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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