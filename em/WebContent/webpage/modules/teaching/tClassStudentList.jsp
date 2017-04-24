<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>报名管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
		
		function addSm(){
		openDialog("新增报名【小班制】","${ctx}/teaching/tClassStudent/enterformSmW?source=classstudent","800px", "600px","");
	}
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>报名信息列表 </h5>
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
	<form:form id="searchForm" modelAttribute="TClassStudent" action="${ctx}/teaching/tClassStudent/list" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/> 
		<div class="form-group">
			<!--
			<c:if test="${isparent}">
			<span>校区：</span> 
			<sys:treeselect id="campus" name="campus.id" value="${TClassStudent.campus.id}" labelName="campus.name" labelValue="${TSchoolRoom.campus.name}"
				title="校区" url="/sys/office/treeData?type=1" cssClass="form-control required" notAllowSelectParent="false" notAllowSelectRoot="false"/>
			</c:if>
			-->
			<span>学生：</span>
				<sys:gridselect url="${ctx}/school/tSchoolStudent/selectlist" id="student" name="student.id"  value="${TClassStudent.student.id}"  title="选择学生" labelName="student.name" 
					labelValue="${TClassStudent.student.name}" cssClass="form-control required" fieldLabels="" fieldKeys="" searchLabel="" searchKey="" ></sys:gridselect>
			<span>课程：</span>
				<sys:gridselect url="${ctx}/subject/tCourseClass/selectlist" id="courseclass" name="courseclass.id"  value="${TClassStudent.courseclass.id}"  title="选择课程班级" labelName="courseclass.classDesc" 
					labelValue="${TClassStudent.courseclass.classDesc}" cssClass="form-control required" fieldLabels="" fieldKeys="" searchLabel="" searchKey="" width="900px"></sys:gridselect>
			<span>是否已缴费：</span>
				<form:select path="ispay"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			<span>报名表状态：</span>
				<form:select path="status"  class="form-control m-b">
					<form:option value="" label="所有"/>
					<form:options items="${fns:getDictList('enter_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
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
			<shiro:hasPermission name="teaching:tClassStudent:edit">
			    <table:editRow url="${ctx}/teaching/tClassStudent/form" title="报名" id="contentTable"></table:editRow>
			</shiro:hasPermission> 
			-->
			<shiro:hasPermission name="teaching:tClassStudent:add">
			    <table:addRow url="${ctx}/teaching/tClassStudent/enterformW" title="报名【大班制】" label="新增报名【大班制】"></table:addRow>
			</shiro:hasPermission>
			<shiro:hasPermission name="teaching:tClassStudent:add">
			    <button class="btn btn-white btn-sm" data-toggle="tooltip" data-placement="left" onclick="addSm()" title="新增"><i class="glyphicon glyphicon-plus"></i> 新增报名【1对1】</button>
			</shiro:hasPermission>  
			<shiro:hasPermission name="teaching:tClassStudent:export">
	       		<table:exportExcel url="${ctx}/teaching/tClassStudent/export"></table:exportExcel>
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
				<!--  <th> <input type="checkbox" class="i-checks"></th> -->
				<th  class="sort-column name">学生姓名</th>
				<!--  <th  class="sort-column tel">联系电话</th> -->
				<th  class="sort-column classId">课程</th>
				<th  class="sort-column classId">教师</th>
				<th  class="sort-column classId">开课日期</th>
				<th  class="sort-column createDate">报名时间</th>
				<th  class="sort-column ispay">是否已缴费</th>
				<th  class="sort-column paytype">缴费方式</th>
				<th  class="sort-column amount">缴费金额</th>
				<th  class="sort-column zks">总课时</th>
				<th  class="sort-column ywc">已完成课时</th>
				<!-- <th  class="sort-column balance">可用余额</th> -->
				<th  class="sort-column status">课程状态</th>
				<th  class="sort-column employeeCode">课程顾问</th>
				<th  class="sort-column status">报名表状态</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tClassStudent">
			<tr>
				<!--  <td> <input type="checkbox" id="${tClassStudent.id}" class="i-checks"></td>-->
				
				<td><a  href="#" onclick="openDialogView('查看学生信息', '${ctx}/school/tSchoolStudent/form?id=${tClassStudent.student.id}','800px', '500px')">
					${tClassStudent.student.name}
				</a></td>
				<!--<td>
					${tClassStudent.student.tel}
				</td> -->
				<td><a  href="#" onclick="openDialogView('查看报名详情', '${ctx}/teaching/tClassStudent/form?id=${tClassStudent.id}','800px', '500px')">
					${tClassStudent.courseclass.classDesc}
				</a>
				</td>
				<td>
					${tClassStudent.courseclass.teacher.name}
				</td>
				<td>
					<fmt:formatDate value="${tClassStudent.courseclass.beginDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					<fmt:formatDate value="${tClassStudent.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${fns:getDictLabel(tClassStudent.ispay, 'yes_no', '')}
				</td>
				<td>
					${fns:getDictLabel(tClassStudent.paytype, 'pay_type', '')}
				</td>
				<td>
					${tClassStudent.amount}
				</td>
				<td><a  href="#" onclick="openDialogView('查看报名详情', '${ctx}/teaching/tClassStudent/form?id=${tClassStudent.id}','800px', '500px')">
					<!-- ${tClassStudent.courseclass.classHour} -->
					${tClassStudent.zks}
				</a></td>
				<td>
					${tClassStudent.ywcks}
				</td>
				<td>
					${fns:getDictLabel(tClassStudent.courseclass.status, 'course_status', '')}
				</td>
				<td>
					${tClassStudent.cc.name}
				</td>
				<td>
					${fns:getDictLabel(tClassStudent.status, 'enter_status', '')}
				</td>
				<td>
					<shiro:hasPermission name="teaching:tClassStudent:view">
						<a href="#" onclick="openDialogView('查看报名', '${ctx}/teaching/tClassStudent/form?id=${tClassStudent.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<c:if test="${tClassStudent.courseclass.status<3}">
					<c:if test="${tClassStudent.status==1}">
					<shiro:hasPermission name="teaching:tClassStudent:edit">
    					<a href="#" onclick="openDialog('修改报名', '${ctx}/teaching/tClassStudent/form?id=${tClassStudent.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
					</c:if>
					<c:if test="${tClassStudent.status==2}">
					<shiro:hasPermission name="teaching:tClassStudent:del">
						<a href="${ctx}/teaching/tClassStudent/cancle?id=${tClassStudent.id}" onclick="return confirmx('确认要取消该报名吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 取消</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="teaching:tClassStudent:del">
						<a href="${ctx}/teaching/tClassStudent/delete?id=${tClassStudent.id}" onclick="return confirmx('确认要删除该报名吗，删除后所对应的相关数据也会对应删除，是否确认？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
					</shiro:hasPermission>
					</c:if>
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