<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>机构管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/webpage/include/treetable.jsp" %>
	<script type="text/javascript">
		$(document).ready(function() {
			var tpl = $("#treeTableTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
			var data = ${fns:toJson(list)}, rootId = "${not empty office.id ? office.id : '0'}";
			addRow("#treeTableList", tpl, data, rootId, true);
			$("#treeTable").treeTable({expandLevel : 5});
		});
		function addRow(list, tpl, data, pid, root){
			for (var i=0; i<data.length; i++){
				var row = data[i];
				if ((${fns:jsGetVal('row.parentId')}) == pid){
					$(list).append(Mustache.render(tpl, {
						dict: {
							type: getDictLabel(${fns:toJson(fns:getDictList('sys_office_type'))}, row.type),
							button1: row.parentIds == "0," ? "none" : "",
							button: row.parentIds == "0," ? "" : "none"
						}, pid: (root?0:pid), row: row
					}));
					addRow(list, tpl, data, row.id);
				}
			}
		}
		function refresh(){//刷新或者排序，页码不清零
			window.location="${ctx}/sys/office/list";
    	}
    	//function 
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>部门、校区列表 </h5>
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
		<!-- 工具栏 -->
	<div class="row">
	<!--  
	<div class="col-sm-12">
		<div class="pull-left">
			<!--
			<shiro:hasPermission name="sys:office:add">
				<table:addRow url="${ctx}/sys/office/form?parent.id=${office.id}" title="机构" width="800px" height="620px" target="officeContent"></table:addRow> 增加按钮
			</shiro:hasPermission>
			 
	        <button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="refresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
		</div>
	</div>
	-->
	</div>
	<table id="treeTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead><tr><th>名称</th><th>归属区域</th><th>编码</th><th>类型</th><th>联系电话</th><shiro:hasPermission name="sys:office:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody id="treeTableList"></tbody>
	</table>
	<script type="text/template" id="treeTableTpl">
		<tr id="{{row.id}}" pId="{{pid}}">
			<td><a href="#" onclick="openDialogView('查看', '${ctx}/sys/office/form?id={{row.id}}','800px', '620px')">{{row.name}}</a></td>
			<td>{{row.area.name}}</td>
			<td>{{row.code}}</td>
			<td>{{dict.type}}</td>
			<td>{{row.phone}}</td>
			<td>
				<shiro:hasPermission name="sys:office:view">
					<a href="#" onclick="openDialogView('查看', '${ctx}/sys/office/form?id={{row.id}}','800px', '520px')" class="btn btn-info btn-xs" style="display:{{dict.button1}}"><i class="fa fa-search-plus"></i> 查看</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="sys:office:edit">
					<a href="#" onclick="openDialog('修改', '${ctx}/sys/office/form?id={{row.id}}','800px', '520px')" class="btn btn-success btn-xs " style="display:{{dict.button1}}"><i class="fa fa-edit"></i> 修改</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="sys:office:del">
					<a href="${ctx}/sys/office/delete?id={{row.id}}" onclick="return confirmx('要删除该节点及所有子节点项吗？', this.href)" class="btn btn-danger btn-xs" style="display:{{dict.button1}}"><i class="fa fa-trash"></i> 删除</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="sys:office:add">
					<a href="#" onclick="openDialog('添加分校', '${ctx}/sys/office/form?parent.id={{row.id}}&type=1', '800px', '520px')" class="btn  btn-primary btn-xs" style="display:{{dict.button}}"><i class=\"fa fa-plus\"></i> 添加分校</a>
					<a href="#" onclick="openDialog('添加部门', '${ctx}/sys/office/form?parent.id={{row.id}}&type=2', '800px', '520px')" class="btn  btn-primary btn-xs" style="display:{{dict.button}}"><i class=\"fa fa-plus\"></i> 添加部门</a>
				</shiro:hasPermission>
			</td>
		</tr>
	</script>
	</div>
	</div>
</body>
</html>