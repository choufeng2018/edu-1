<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>课程方案管理</title>
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
    
    <div class="ibox-content">
	<sys:message content="${message}"/>
	
	<!-- 表格 -->
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th> <input type="checkbox" class="i-checks"></th>
				<th  class="sort-column classDesc">星期</th>
				<th  class="sort-column subjectId">时间</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tCourseClass">
			<tr>
				<td><input type="checkbox" id="" class="i-checks"></td>
				<td>星期一</td>
				<td>
					<form:select path="courseclass.shour">
						<form:option value="07" label="07"/>
					    <form:option value="08" label="08"/>
					    <form:option value="09" label="09"/>
					    <form:option value="10" label="10"/>
					    <form:option value="11" label="11"/>
					    <form:option value="12" label="12"/>
					    <form:option value="13" label="13"/>
					    <form:option value="14" label="14"/>
					    <form:option value="15" label="15"/>
					    <form:option value="16" label="16"/>
					    <form:option value="17" label="17"/>
					    <form:option value="18" label="18"/>
					    <form:option value="19" label="19"/>
					    <form:option value="20" label="20"/>
					    <form:option value="21" label="21"/>
					</form:select>:
					<form:select path="courseclass.smin">
					    <form:option value="00" label="00"/>
					  	<form:option value="10" label="10"/>
					    <form:option value="15" label="15"/>
					  	<form:option value="20" label="20"/>
					  	<form:option value="25" label="25"/>
					  	<form:option value="30" label="30"/>
					  	<form:option value="35" label="35"/>
					  	<form:option value="40" label="40"/>
					  	<form:option value="45" label="45"/>
						<form:option value="50" label="50"/>
					  	<form:option value="55" label="55"/>
					</form:select> 
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