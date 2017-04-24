<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>学科分类管理</title>
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
		<h5>学科分类列表 </h5>
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
	<form:form id="searchForm" modelAttribute="TCommSubjectField" action="${ctx}/school/tCommSubjectField/list" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>校区：</span>
			<sys:treeselect id="campus" name="campus.id" value="${TCommSubjectField.campus.id}" labelName="campus.name" labelValue="${TCommSubjectField.campus.name}"
							title="校区" url="/sys/office/treeData?type=1" cssClass="form-control required" notAllowSelectParent="true" notAllowSelectRoot="true"/>
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
		</div>
	</div>
	</div>
	
	<!-- 表格 -->
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th> <input type="checkbox" class="i-checks"></th>
				<th  class="sort-column subjectCode">分类编码</th>
				<th  class="sort-column subjectName">分类名称</th>
				<th  class="sort-column campusCode">所属校区</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tCommSubjectField">
			<tr>
				<td> <input type="checkbox" id="${tCommSubjectField.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看学科分类', '${ctx}/school/tCommSubjectField/form?id=${tCommSubjectField.id}','800px', '500px')">
					${tCommSubjectField.fieldCode}
				</a></td>
				<td>
					${tCommSubjectField.fieldName}
				</td>
				<td>
					${tCommSubjectField.campus.name}
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