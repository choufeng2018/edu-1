<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>学生信息管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
		
		function getSelectedItem(){
			var size = $("#contentTable tbody tr td input.i-checks:checked").size();
			if(size == 0 ){
				top.layer.alert('请至少选择一条数据!', {icon: 0, title:'警告'});
				return "-1";
			}

			if(size > 1 ){
				top.layer.alert('只能选择一条数据!', {icon: 0, title:'警告'});
				return "-1";
			}
			var id =  $("#contentTable tbody tr td input.i-checks:checkbox:checked").attr("id");
			return id;
		}
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
	<form:form id="searchForm" modelAttribute="TSchoolStudent" action="${ctx}/school/tSchoolStudent/selectlist" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<!-- <table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/>支持排序 -->
		<div class="form-group">
			<span>姓名：</span>
				<form:input path="name" htmlEscape="false" maxlength="30"  class=" form-control input-sm"/>
			<span>电话：</span>
				<form:input path="tel" htmlEscape="false" maxlength="30"  class=" form-control input-sm"/>
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
	        <button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
		</div>
		<div class="pull-right">
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
		</div>
	</div>
	</div>
	
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th> <input type="checkbox" class="i-checks"></th>
				<th  class="sort-column campusId">姓名</th>
				<th  class="sort-column name">所属校区</th>
				<th  class="sort-column birthday">出生年月</th>
				<th  class="sort-column sex">性别</th>
				<th  class="sort-column tel">联系电话</th>
				<th  class="sort-column subjects">意向学科</th>
				<th  class="sort-column status">状态</th>
				<th  class="sort-column cc">课程顾问</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tSchoolStudent">
			<tr>
				<td> <input type="checkbox" id="${tSchoolStudent.id}_item_${tSchoolStudent.name}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看学生信息', '${ctx}/school/tSchoolStudent/form?id=${tSchoolStudent.id}','800px', '500px')">
					${tSchoolStudent.name}
				</a></td>
				<td>
					${tSchoolStudent.campus.name}
				</td>
				<td>
					<fmt:formatDate value="${tSchoolStudent.birthday}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					${fns:getDictLabel(tSchoolStudent.sex, 'sex', '')}
				</td>
				<td>
					${tSchoolStudent.tel}
				</td>
				<td>
					${tSchoolStudent.subjects}
				</td>
				<td>
					${fns:getDictLabel(tSchoolStudent.status, 'student_status', '')}
				</td>
				<td>
					${tSchoolStudent.cc.name}
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