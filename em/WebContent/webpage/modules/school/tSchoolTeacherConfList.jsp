<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>排课管理</title>
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
		<h5>排课列表 </h5>
		<div class="ibox-tools">
			<a class="collapse-link">
				<i class="fa fa-chevron-up"></i>
			</a>
			<a class="dropdown-toggle" data-toggle="dropdown" href="#">
				<i class="fa fa-wrench"></i>
			</a>
			<ul class="dropdown-menu dropdown-user"> 
			</ul>
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
	<form:form id="searchForm" modelAttribute="TSchoolTeacherConf" action="${ctx}/school/tSchoolTeacherConf/list" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<!--<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/> 支持排序 -->
		<div class="form-group">
		<span>校区：</span>
				<sys:treeselect id="campus" name="teacher.campus.id" value="${TSchoolTeacherConf.teacher.campus.id}" labelName="teacher.campus.name" labelValue="${TSchoolTeacherConf.teacher.campus.name}"
							title="校区" url="/sys/office/treeData?type=1" cssClass="form-control required" notAllowSelectParent="false" notAllowSelectRoot="false"/>
		<span>教师姓名：</span>
				<form:input path="teacher.name" htmlEscape="false" maxlength="30"  class=" form-control input-sm"/>
		</div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<!--<shiro:hasPermission name="school:tSchoolTeacherConf:add">
				<table:addRow url="${ctx}/school/tSchoolTeacherConf/form" title="排课"></table:addRow> 增加按钮 
			</shiro:hasPermission>-->
			<!--<shiro:hasPermission name="school:tSchoolTeacherConf:edit">
			    <table:editRow url="${ctx}/school/tSchoolTeacherConf/form" title="排课" id="contentTable"></table:editRow> 编辑按钮 
			</shiro:hasPermission>-->
			<!--<shiro:hasPermission name="school:tSchoolTeacherConf:del">
				<table:delRow url="${ctx}/school/tSchoolTeacherConf/deleteAll" id="contentTable"></table:delRow> 删除按钮 
			</shiro:hasPermission>-->
			<!--<shiro:hasPermission name="school:tSchoolTeacherConf:import">
				<table:importExcel url="${ctx}/school/tSchoolTeacherConf/import"></table:importExcel> 导入按钮
			</shiro:hasPermission> -->
			<shiro:hasPermission name="school:tSchoolTeacherConf:export">
	       		<table:exportExcel url="${ctx}/school/tSchoolTeacherConf/export"></table:exportExcel><!-- 导出按钮 -->
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
				<th  class="sort-column teacher.name" rowspan="2">教师</th>
				<th  class="sort-column price" rowspan="2">课时价格（元）</th>
				<th  class="sort-column cmin" colspan="2">每课时（分钟）</th>
				<th  class="sort-column ism" colspan="2">上午</th>
				<th  class="sort-column isa" colspan="2">下午</th>
				<th  class="sort-column isn" colspan="2">晚上</th>
				<th  class="sort-column updateDate" rowspan="2">更新日期</th>
				<th rowspan="2">操作</th>
			</tr>
			<tr>
				<th  class="sort-column cmin">时长</th>
				<th  class="sort-column rmin">间隔</th>
				<th  class="sort-column ism">是否教课</th>
				<th  class="sort-column mtime">开始时间</th>
				<th  class="sort-column isa">是否教课</th>
				<th  class="sort-column atime">开始时间</th>
				<th  class="sort-column isn">是否教课</th>
				<th  class="sort-column ntime">开始时间</th> 
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tSchoolTeacherConf">
			<tr>
				<td><a  href="#" onclick="openDialogView('查看排课配置', '${ctx}/school/tSchoolTeacherConf/form?id=${tSchoolTeacherConf.id}','800px', '500px')">
					${tSchoolTeacherConf.teacher.name}
				</a></td>
				<td>
					${tSchoolTeacherConf.price}
				</td>
				<td>
					${tSchoolTeacherConf.cmin}
				</td>
				<td>
					${tSchoolTeacherConf.rmin}
				</td>
				<td>
					${fns:getDictLabel(tSchoolTeacherConf.ism, 'can_teach', '')}
				</td>
				<td>
					<fmt:formatDate value="${tSchoolTeacherConf.mtime}" pattern="HH:mm:ss"/>
				</td>
				<td>
					${fns:getDictLabel(tSchoolTeacherConf.isa, 'can_teach', '')}
				</td>
				<td>
					<fmt:formatDate value="${tSchoolTeacherConf.atime}" pattern="HH:mm:ss"/>
				</td>
				<td>
					${fns:getDictLabel(tSchoolTeacherConf.isn, 'can_teach', '')}
				</td>
				<td>
					<fmt:formatDate value="${tSchoolTeacherConf.ntime}" pattern="HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${tSchoolTeacherConf.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<c:if test="${tSchoolTeacherConf.stuCount ==null || tSchoolTeacherConf.stuCount==0}">
					<shiro:hasPermission name="school:tSchoolTeacherConf:edit">
    					<a href="#" onclick="openDialog('排课', '${ctx}/school/tSchoolTeacherConf/form?id=${tSchoolTeacherConf.id}&teacher.id=${tSchoolTeacherConf.teacher.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 排课</a>
    				</shiro:hasPermission>
    				</c:if>
    				<shiro:hasPermission name="school:tSchoolTeacherConf:view">
    					<a href="#" onclick="openDialog('查看排课结果', '${ctx}/school/tSchoolTeacherConf/view?id=${tSchoolTeacherConf.id}','800px', '500px')" class="btn  btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看排课结果</a>
    				</shiro:hasPermission>
					<!-- 
					<shiro:hasPermission name="school:tSchoolTeacherConf:view">
						<a href="#" onclick="openDialogView('查看排课', '${ctx}/school/tSchoolTeacherConf/form?id=${tSchoolTeacherConf.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="school:tSchoolTeacherConf:edit">
    					<a href="#" onclick="openDialog('修改排课', '${ctx}/school/tSchoolTeacherConf/form?id=${tSchoolTeacherConf.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="school:tSchoolTeacherConf:del">
						<a href="${ctx}/school/tSchoolTeacherConf/delete?id=${tSchoolTeacherConf.id}" onclick="return confirmx('确认要删除该排课吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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