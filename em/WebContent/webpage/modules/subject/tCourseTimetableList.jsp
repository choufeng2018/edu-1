<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>课程表管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			laydate({
	            elem: '#courseDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });
		});
		
		function validata(checkparam, tip)
		{
			var result = {tag:"1", str:""};
			if (checkparam == "" || checkparam == null)
			{
				result.tag = "0";
				result.str = "请配置校验参数";
			} else if ($("#"+checkparam+"Id").val() == "")
			{
				result.tag = "0";
				result.str = "请选择"+tip+"！";
			} else { 
				result.str = checkparam+"="+$("#"+checkparam+"Id").val();
			}
			return result;
		}
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>课程表列表 </h5>
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
	<form:form id="searchForm" modelAttribute="TCourseTimetable" action="${ctx}/subject/tCourseTimetable/list" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>校区：</span>
				<sys:treeselect id="campus" name="campusId" value="${TCourseTimetable.campusId}" labelName="campusName" labelValue="${TCourseTimetable.campusName}"
							title="校区" url="/sys/office/treeData?type=1" cssClass="form-control required" notAllowSelectParent="true" notAllowSelectRoot="true"/>
			<!--  
			<span>学科：</span>
				<sys:treeselect id="subject" name="subject.id" value="${TCourseTimetable.subject.id}" labelName="subject.subjectName" labelValue="${TCourseTimetable.subject.subjectName}"
							title="课程" url="/school/tCommSubject/treeData" cssClass="form-control required" notAllowSelectParent="true" notAllowSelectRoot="true"/>
			-->
			<span>课程：</span>
				<sys:gridselect url="${ctx}/subject/tCourseClass/selectlist" id="courseclass" name="courseclass.id"  value="${TCourseTimetable.courseclass.id}"  title="选择课程" labelName="courseclass.classDesc" 
							labelValue="${TCourseTimetable.courseclass.classDesc}" cssClass="form-control required" fieldLabels="" fieldKeys="" searchLabel="" searchKey="" disabled="true"></sys:gridselect>
			<span>教室：</span>
				<sys:treeselectcheckcampus id="room" name="room.id" value="${TCourseTimetable.room.id}" labelName="room.roomDesc" labelValue="${TCourseTimetable.room.roomDesc}" 
							title="教室" url="/school/tSchoolRoom/treeData" cssClass="form-control required" notAllowSelectParent="true" notAllowSelectRoot="true" needcheck="true" checkparam="campus" checktip="校区"/>
			<span>日期：</span>
				<input id="courseDate" name="courseDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${TCourseTimetable.courseDate}" pattern="yyyy-MM-dd"/>"/>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<!--
			<shiro:hasPermission name="subject:tCourseTimetable:add">
				<table:addRow url="${ctx}/subject/tCourseTimetable/form" title="课程表"></table:addRow>   
			</shiro:hasPermission> 
			-->
			<shiro:hasPermission name="subject:tCourseTimetable:export">
	       		<table:exportExcel url="${ctx}/subject/tCourseTimetable/export"></table:exportExcel><!-- 导出按钮 -->
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
				<th  class="sort-column courseDesc">课程名称</th>
				<th  class="sort-column courseDate">日期</th>
				<th  class="sort-column teactime">上课时间</th>
				<th  class="sort-column roomDesc">教室</th>
				<th  class="sort-column type">类型</th>
				<th  class="sort-column status">状态</th>
				<th  class="sort-column teacherName">教师</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tCourseTimetable">
			<tr>
				<td> <input type="checkbox" id="${tCourseTimetable.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看课程表', '${ctx}/subject/tCourseTimetable/form?id=${tCourseTimetable.id}','800px', '500px')">
					${tCourseTimetable.courseclass.classDesc}
				</a></td>
				<td>
					<fmt:formatDate value="${tCourseTimetable.courseDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					${tCourseTimetable.teactime}
				</td>
				<td>
					${tCourseTimetable.room.roomDesc}
				</td>
				<td>
					${fns:getDictLabel(tCourseTimetable.type, 'timetable_type', '')}
				</td>
				<td>
					${fns:getDictLabel(tCourseTimetable.status, 'timetable_status', '')}
				</td>
				<td>
					${tCourseTimetable.teacher.name}
				</td>
				<td>
					<shiro:hasPermission name="subject:tCourseTimetable:view">
						<a href="#" onclick="openDialogView('查看课程表', '${ctx}/subject/tCourseTimetable/form?id=${tCourseTimetable.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<c:if test="${tCourseTimetable.status == '1' && tCourseTimetable.type!='2'}">
					<shiro:hasPermission name="subject:tCourseTimetable:edit">
						<a href="#" onclick="openDialog('更改时间', '${ctx}/subject/tCourseTimetable/timeform?id=${tCourseTimetable.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 更改时间</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="subject:tCourseTimetable:edit">
						<a href="#" onclick="openDialog('更改教师', '${ctx}/subject/tCourseTimetable/teacherform?id=${tCourseTimetable.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 更改教师</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="subject:tCourseTimetable:edit">
						<a href="#" onclick="openDialog('更改教室', '${ctx}/subject/tCourseTimetable/roomform?id=${tCourseTimetable.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 更改教室</a>
					</shiro:hasPermission>
					</c:if>
					<!--  
					<c:if test="${tCourseTimetable.status == '1' && tCourseTimetable.type!='2'}">
					<shiro:hasPermission name="subject:tCourseTimetable:edit">
    					<a href="#" onclick="openDialog('修改课程表', '${ctx}/subject/tCourseTimetable/form?id=${tCourseTimetable.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="subject:tCourseTimetable:del">
						<a href="${ctx}/subject/tCourseTimetable/delete?id=${tCourseTimetable.id}" onclick="return confirmx('确认要取消该课程表吗，取消后需补课？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 取消</a>
					</shiro:hasPermission>
					</c:if>
					
					<c:if test="${tCourseTimetable.status == '1' && tCourseTimetable.type=='2'}">
					<shiro:hasPermission name="subject:tCourseTimetable:edit">
    					<a href="#" onclick="openDialog('补课', '${ctx}/subject/tCourseTimetable/form?id=${tCourseTimetable.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 补课</a>
    				</shiro:hasPermission>
					</c:if>
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