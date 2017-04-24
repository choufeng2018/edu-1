<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title></title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
	</script>
</head>
<body class="gray-bg">
<div class="wrapper wrapper-content">
	<div class="ibox">
    <div class="ibox-content"> 
	
	<!-- 表格 -->
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
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
		<c:forEach items="${srList}" var="sysResource">
			<tr>
				<td>
					${sysResource.name}
				</td>
				<td>
					${sysResource.level}
				</td>
				<td>
					${fns:getDictLabel(sysResource.type, 'resource_type', '')}
				</td>
				<td>
					${fns:getDictLabel(sysResource.isPrice, 'is_resource', '')}
				</td>
				<td>
					${sysResource.price}
				</td>
				<td>
					${sysResource.unit}
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