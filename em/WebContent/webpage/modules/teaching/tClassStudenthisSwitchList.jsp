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
			openDialogView("学生转班","${ctx}/teaching/tClassStudent/switchlist?source=hisswitch", "950px", "600px", "");
		}
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>学生转班信息 </h5>
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
	<form:form id="searchForm" modelAttribute="TClassStudent" action="${ctx}/teaching/tClassStudent/hisSwitchlist" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<!-- <table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/>支持排序 -->
		<div class="form-group">
			
			<span>学生姓名：</span>
				<sys:gridselect url="${ctx}/school/tSchoolStudent/selectlist" id="student" name="student.id"  value="${TClassStudent.student.id}"  title="选择学生" labelName="student.name" 
					labelValue="${TClassStudent.student.name}" cssClass="form-control required" fieldLabels="" fieldKeys="" searchLabel="" searchKey="" ></sys:gridselect>
			<span>课程：</span>
				<sys:gridselect url="${ctx}/subject/tCourseClass/selectlist" id="courseclass" name="courseclass.id"  value="${TClassStudent.courseclass.id}"  title="选择课程班级" labelName="courseclass.classDesc" 
					labelValue="${TClassStudent.courseclass.classDesc}" cssClass="form-control required" fieldLabels="" fieldKeys="" searchLabel="" searchKey="" width="900px"></sys:gridselect>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="teaching:tClassStudent:add">
			    <button class="btn btn-white btn-sm" data-toggle="tooltip" data-placement="left" onclick="addSm()" title="学生转班"><i class="glyphicon glyphicon-plus"></i> 学生转班</button>
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
				<!-- <th rowspan="2"> <input type="checkbox" class="i-checks"></th> -->
				<th  class="sort-column name" rowspan="2">学生姓名</th>
				<!-- <th  class="sort-column tel" rowspan="2">联系电话</th> -->
				<th  class="sort-column tel" rowspan="2">课程顾问</th>
				
				<th  class="sort-column classId" colspan="6" style="align:center">原班级</th>
				<th  class="sort-column classId" colspan="6">新班级</th>
			</tr>
			<tr>
				<th  class="sort-column classId">课程</th>
				<th  class="sort-column teacher">教师</th>
				<th  class="sort-column zks">总课时</th>
				<th  class="sort-column zks">剩余课时</th>
				<th  class="sort-column amount">缴费金额</th>
				<th  class="sort-column balance">剩余转出金额</th>
				
				<th  class="sort-column classId">课程</th>
				<th  class="sort-column teacher">教师</th>
				<th  class="sort-column zks">总课时</th>
				<th  class="sort-column zks">已完成课时</th>
				<th  class="sort-column zks">剩余课时</th>
				<th  class="sort-column amount">缴费金额</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tClassStudent">
			<tr>
				<!--<td> <input type="checkbox" id="${tClassStudent.id}" class="i-checks"></td>-->
				<td>${tClassStudent.student.name}</td>
				<!--<td>${tClassStudent.student.tel}</td> -->
				<td>${tClassStudent.cc.name}</td>
				
				<td>${tClassStudent.parent.courseclass.classDesc}</td>
				<td>${tClassStudent.parent.courseclass.teacher.name}</td>
				<td>${tClassStudent.parent.zks}</td>
				<td>${tClassStudent.parent.zks-tClassStudent.parent.ywcks}</td>
				<td>${tClassStudent.parent.amount}</td>
				<td>${tClassStudent.parent.balance}</td>
				
				<td>${tClassStudent.courseclass.classDesc}</td>
				<td>${tClassStudent.courseclass.teacher.name}</td>
				<td>${tClassStudent.zks}</td>
				<td>${tClassStudent.ywcks}</td>
				<td>${tClassStudent.zks-tClassStudent.ywcks}</td>
				<td>${tClassStudent.amount}</td>
				 
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