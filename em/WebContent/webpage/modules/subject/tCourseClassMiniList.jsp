<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>课程方案管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
		
		function createTimeTable(url)
		{
			var str = "", tip="";
			var ids = "";
			var iscre = 0;
		  	$("#contentTable tbody tr td input.i-checks:checkbox").each(function(){
		    	if(true == $(this).is(':checked')){
		      		str += $(this).attr("id")+",";
		      		if ($(this).attr("name") == "1")
		      			iscre++;
		    	}
		  	});
		  	if(str.substr(str.length-1)== ','){
		    	ids = str.substr(0, str.length-1);
		  	}
		  	if(ids == ""){
				top.layer.alert('请至少选择一条数据!', {icon: 0, title:'警告'});
				return;
		  	}
		  	if (iscre >0)
		  		tip = '您所选课程中已经有【'+iscre+'】门课程已经创建课程表，如确认创建将完全覆盖原有课程，造成已完成课程考勤等数据丢失。<br/>是否确认要创建课程表吗?';
		    else
		    	tip = '请仔细核查数据后，确认是否要创建课程表吗?';
			top.layer.confirm(tip, {icon: 3, title:'系统提示'}, function(index){
				window.location = url+"?ids="+ids;
				top.layer.close(index);
			});
		}
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>课程信息列表（一对一） </h5>
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
	<form:form id="searchForm" modelAttribute="TCourseClass" action="${ctx}/subject/tCourseClass/minilist" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/> 
		<div class="form-group">
			<c:if test="${isparent}">
			<span>校区：</span>
				<sys:treeselect id="campus" name="campus.id" value="${TCourseClass.campus.id}" labelName="campus.name" labelValue="${TCourseClass.campus.name}"
							title="校区" url="/sys/office/treeData?type=1" cssClass="form-control required" notAllowSelectParent="true" notAllowSelectRoot="true"/>
			</c:if> 
			<span>专业：</span>
				<sys:treeselect id="subject" name="subject.id" value="${TCourseClass.subject.id}" labelName="subject.subjectName" labelValue="${TCourseClass.subject.subjectName}"
							title="专业" url="/school/tCommSubject/treeData" cssClass="form-control required" notAllowSelectParent="true" notAllowSelectRoot="true"/>
			<span>状态：</span>
				<form:radiobuttons class="i-checks" path="status" items="${fns:getDictList('course_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<!--<shiro:hasPermission name="subject:tCourseClass:add">
				<table:addRow url="${ctx}/subject/tCourseClass/form" title="排课" width="900px" height="600px" label="排课"></table:addRow>
			</shiro:hasPermission> 
			<shiro:hasPermission name="subject:tCourseClass:edit">
			    <table:editRow url="${ctx}/subject/tCourseClass/form" title="课程方案" id="contentTable"></table:editRow>
			</shiro:hasPermission>
			<shiro:hasPermission name="subject:tCourseClass:del">
				<table:delRow url="${ctx}/subject/tCourseClass/deleteAll" id="contentTable"></table:delRow>
			</shiro:hasPermission>
			<shiro:hasPermission name="subject:tCourseTimetable:add">
				<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="createTimeTable('${ctx}/subject/tCourseClass/createTimeTable')" title="创建课程表"><i class="glyphicon glyphicon-plus"></i> 创建课程表</button>
			</shiro:hasPermission>-->
			<!--<shiro:hasPermission name="subject:tCourseClass:import">
				<table:importExcel url="${ctx}/subject/tCourseClass/import"></table:importExcel>  
			</shiro:hasPermission>-->
			<shiro:hasPermission name="subject:tCourseClass:export">
	       		<table:exportExcel url="${ctx}/subject/tCourseClass/export"></table:exportExcel><!-- 导出按钮 -->
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
				<c:if test="${UserUtils.getUserIsParent()}">
				<th class="sort-column campusId">校区</th>
				</c:if>
				<th  class="sort-column classDesc">课程名称</th>
				<th  class="sort-column subjectId">专业</th>
				<!--<th  class="sort-column classType">授课方式</th>-->
				<th  class="sort-column beginDate">开课日期</th>
				<th  class="sort-column slot">上课时段</th>
				<th  class="sort-column week">上课时间</th>
				<th  class="sort-column classHour">课次</th>
				<th  class="sort-column classMin">课时时长(分)</th>
				<th  class="sort-column cost">总费用</th>
				<th  class="sort-column chargeType">计费方式</th>
				<!--
				<th  class="sort-column recruit">招生对象</th>
				<th  class="sort-column totalStu">招生人数</th>
				<th  class="sort-column stuNum">已招人数</th>
				-->
				<th  class="sort-column teacherName">教师</th>
				<th  class="sort-column roomDesc">教室</th>
				<th  class="sort-column status">状态</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tCourseClass">
			<tr> 
				<c:if test="${UserUtils.getUserIsParent()}">
				<td>
					${tCourseClass.campus.name}
				</td>
				</c:if>
				<td><a  href="#" onclick="openDialogView('查看课程方案', '${ctx}/subject/tCourseClass/form?id=${tCourseClass.id}','800px', '500px')">
					${tCourseClass.classDesc}
				</a></td>
				<td>
					${tCourseClass.subject.subjectName}
				</td>
				<!--
				<td>
					${fns:getDictLabel(tCourseClass.classType, 'course_type', '')}
				</td>
				-->
				<td>
					<fmt:formatDate value="${tCourseClass.beginDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td> 
					<!-- ${fns:getDictLabel(tCourseClass.slot, 'slot', '')} -->
					${tCourseClass.slotdesc}
				</td>
				<td> 
					${tCourseClass.teactime}
				</td>
				<td>
					${tCourseClass.classHour}
				</td>
				<td>
					${tCourseClass.classMin}
				</td>
				<td>
					${tCourseClass.cost}
				</td>
				<td>
					${fns:getDictLabel(tCourseClass.chargeType, 'charge_type', '')}
				</td>
				<!--
				<td>
					${tCourseClass.recruit}
				</td>
				<td>
					${tCourseClass.totalStu}
				</td>
				<td>
					${tCourseClass.stuNum}
				</td>
				-->
				<td>
				<a  href="#" onclick="openDialogView('查看教师信息', '${ctx}/school/tSchoolTeacher/form?id=${tCourseClass.teacher.id}','800px', '500px')">
					${tCourseClass.teacher.name}
				</a>
				</td>
				<td>
					${tCourseClass.room.roomDesc}
				</td>
				
				<td>
					${fns:getDictLabel(tCourseClass.status, 'course_status', '')}
				</td>
				<td> 
					<shiro:hasPermission name="subject:tCourseClass:view">
						<a href="#" onclick="openDialogView('查看课程方案', '${ctx}/subject/tCourseClass/form?id=${tCourseClass.id}','800px', '600px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
    				<c:if test="${tCourseClass.status<4 && tCourseClass.iscreate==1}"> </c:if>
					<shiro:hasPermission name="subject:tCourseTimetable:edit">
						<a href="#" onclick="openDialogView('课时调整', '${ctx}/subject/tCourseTimetable/editlist?ccid=${tCourseClass.id}','900px', '600px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 课时调整</a>
					</shiro:hasPermission>
    				<c:if test="${tCourseClass.status>1 && tCourseClass.stuNum>0}">
					<shiro:hasPermission name="subject:tCourseClass:list">
						<a href="#" onclick="openDialogView('班级学生列表', '${ctx}/teaching/tClassStudent/stulist?ccid=${tCourseClass.id}','800px', '600px')" class="btn btn-success btn-xs" ><i class="fa fa-search-plus"></i> 查看学员</a>
					</shiro:hasPermission>
					</c:if> 
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