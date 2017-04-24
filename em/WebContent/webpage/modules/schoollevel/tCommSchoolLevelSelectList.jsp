<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>租户等级管理</title>
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
			    
			var label = $("#contentTable tbody tr td input.i-checks:checkbox:checked").parent().parent().parent().find("#ldesc").html();
			return id+"_item_"+$.trim(label);
		}
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
    
    <div class="ibox-content">
	<sys:message content="${message}"/>
	
	<!--查询条件-->
	<div class="row">
	<div class="col-sm-12">
	<form:form id="searchForm" modelAttribute="tCommSchoolLevel" action="${ctx}/schoollevel/tCommSchoolLevel/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div class="form-group">
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th> </th>
				<th  class="sort-column levelDesc">等级描述</th>
				<th  class="sort-column levelPrice">年费（元/年）</th>
				<th  class="sort-column updateDate">更新日期</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tCommSchoolLevel">
			<tr>
				<td > <input type="checkbox" id="${tCommSchoolLevel.id}" class="i-checks"></td>
				<td id="ldesc">
					${tCommSchoolLevel.levelDesc}
				</td>
				<td>
					${tCommSchoolLevel.levelPrice}
				</td>
				<td>
					<fmt:formatDate value="${tCommSchoolLevel.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	 
	<br/>
	<br/>
	</div>
	</div>
</div>
</body>
</html>