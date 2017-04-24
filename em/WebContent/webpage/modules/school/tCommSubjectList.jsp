<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>课程信息管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/webpage/include/treetable.jsp" %>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#treeTable").treeTable({expandLevel : 2, column:0}).show();
		});
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>课程信息列表 </h5>
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
	 
	 <form:form id="searchForm" modelAttribute="TCommSubject" action="${ctx}/school/tCommSubject/list" method="post" class="form-inline">
	 </form:form>
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="school:tCommSubject:add">
				<table:addRow url="${ctx}/school/tCommSubject/formFL" title="分类" width="700px" height="400px" label="新增大类"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
	       <button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
	       
		</div>
	</div>
	</div>
	
	<form id="listForm" method="post">
		<table id="treeTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
			<thead><tr><!-- <th><input type="checkbox" class="i-checks"></th> --><th>名称</th><th>编码</th><shiro:hasPermission name="school:tCommSubject:edit"><th>操作</th></shiro:hasPermission></tr></thead>
			<tbody><c:forEach items="${list}" var="tCommSubject">
				<tr id="${tCommSubject.id}" pId="${tCommSubject.parent.id ne '1'?tCommSubject.parent.id:'0'}">
					<!-- <td> <input type="checkbox" id="${tCommSubject.id}" class="i-checks"></td> -->
					<td nowrap><a  href="#" onclick="openDialogView('查看', '${ctx}/school/tCommSubject/form${tCommSubject.parent.id == '0'?'FL':''}?id=${tCommSubject.id}','700px', '400px')">${tCommSubject.subjectName}</a></td>
					<td nowrap>${tCommSubject.subjectCode} </td>
					<td nowrap>
						<shiro:hasPermission name="school:tCommSubject:view">
							<a href="#" onclick="openDialogView('查看', '${ctx}/school/tCommSubject/form${tCommSubject.parent.id == '0'?'FL':''}?id=${tCommSubject.id}','700px', '400px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="school:tCommSubject:edit">
	    					<a href="#" onclick="openDialog('修改', '${ctx}/school/tCommSubject/form${tCommSubject.parent.id == '0'?'FL':''}?id=${tCommSubject.id}','700px', '400px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
	    				</shiro:hasPermission>
	    				<shiro:hasPermission name="school:tCommSubject:del">
							<a href="${ctx}/school/tCommSubject/delete?id=${tCommSubject.id}" onclick="return confirmx('要删除该菜单及所有子菜单项吗？', this.href)" class="btn btn-danger btn-xs" ><i class="fa fa-trash"></i> 删除</a>
						</shiro:hasPermission>
						<c:if test="${tCommSubject.parent.id == '0'}">
						<shiro:hasPermission name="school:tCommSubject:add">
							<a href="#" onclick="openDialog('添加学科', '${ctx}/school/tCommSubject/form?parent.id=${tCommSubject.id}','700px', '400px')" class="btn btn-primary btn-xs" ><i class="fa fa-plus"></i> 添加小类</a>
						</shiro:hasPermission>
						</c:if>
					</td>
				</tr>
			</c:forEach></tbody>
		</table>
	 </form>
	  
	<br/> 
	</div>
	</div>
</div>
</body>
</html>