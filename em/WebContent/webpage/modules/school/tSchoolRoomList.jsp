<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>教室管理</title>
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
		<h5>教室列表 </h5>
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
	<form:form id="searchForm" modelAttribute="TSchoolRoom" action="${ctx}/school/tSchoolRoom/list" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/> 
		<c:if test="${isparent}">
		<div class="form-group">
			<span>校区：</span> 
			<sys:treeselect id="campus" name="campus.id" value="${TSchoolRoom.campus.id}" labelName="campus.name" labelValue="${TSchoolRoom.campus.name}"
				title="校区" url="/sys/office/treeData?type=1" cssClass="form-control required" notAllowSelectParent="false" notAllowSelectRoot="false"/>
		</div> 
		<div class="pull-right">
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
		</div>
		</c:if>
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="school:tSchoolRoom:add">
				<table:addRow url="${ctx}/school/tSchoolRoom/form" title="教室"></table:addRow> 
			</shiro:hasPermission>
			<!-- 
			<shiro:hasPermission name="school:tSchoolRoom:edit">
			    <table:editRow url="${ctx}/school/tSchoolRoom/form" title="教室" id="contentTable"></table:editRow>
			</shiro:hasPermission>
			<shiro:hasPermission name="school:tSchoolRoom:del">
				<table:delRow url="${ctx}/school/tSchoolRoom/deleteAll" id="contentTable"></table:delRow> 
			</shiro:hasPermission>--><!--
			<shiro:hasPermission name="school:tSchoolRoom:import">
				<table:importExcel url="${ctx}/school/tSchoolRoom/import"></table:importExcel> 导入按钮 
			</shiro:hasPermission>-->
			<shiro:hasPermission name="school:tSchoolRoom:export">
	       		<table:exportExcel url="${ctx}/school/tSchoolRoom/export"></table:exportExcel><!-- 导出按钮 -->
	       	</shiro:hasPermission>
	       <button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
		</div>
		<c:if test="${isparent}">
		<div class="pull-right">
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
		</div>
		</c:if>
	</div>
	</div>
	
	<!-- 表格 -->
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th> <input type="checkbox" class="i-checks"></th>
				<th  class="sort-column roomCode">教室编码</th>
				<th  class="sort-column roomDesc">教室名称</th>
				<th  class="sort-column campusCode">所属校区</th>
				<th  class="sort-column delFlag">状态</th>
				<th  class="sort-column useType">用途类型</th>
				<th  class="sort-column course">学科</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tSchoolRoom">
			<tr>
				<td> <input type="checkbox" id="${tSchoolRoom.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看教室', '${ctx}/school/tSchoolRoom/form?id=${tSchoolRoom.id}','800px', '500px')">
					${tSchoolRoom.roomCode}
				</a></td>
				<td>
					${tSchoolRoom.roomDesc}
				</td>
				<td>
					${tSchoolRoom.campus.name}
				</td>
				<td>
					${fns:getDictLabel(tSchoolRoom.status, 'school_status', '')}
				</td>
				<td>
					${fns:getDictLabel(tSchoolRoom.useType, 'use_type', '')}
				</td>
				<td>
					${tSchoolRoom.course.subjectName}
				</td>
				<td>
					<shiro:hasPermission name="school:tSchoolRoom:view">
						<a href="#" onclick="openDialogView('查看教室', '${ctx}/school/tSchoolRoom/form?id=${tSchoolRoom.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="school:tSchoolRoom:edit">
    					<a href="#" onclick="openDialog('修改教室', '${ctx}/school/tSchoolRoom/form?id=${tSchoolRoom.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="school:tSchoolRoom:edit">
						<a href="${ctx}/school/tSchoolRoom/ustatus?id=${tSchoolRoom.id}" onclick="return confirmx('确认要${tSchoolRoom.status=='0'?'启用':'禁用'}该教室吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-edit"></i> ${tSchoolRoom.status=='0'?'启用':'禁用'}</a>
					</shiro:hasPermission>
    				<shiro:hasPermission name="school:tSchoolRoom:del">
						<a href="${ctx}/school/tSchoolRoom/delete?id=${tSchoolRoom.id}" onclick="return confirmx('确认要删除该教室吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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