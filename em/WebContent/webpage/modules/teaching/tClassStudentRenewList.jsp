<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>续费管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
		
		function addSm(){
			openDialog("新增报名【小班制】","${ctx}/teaching/tClassStudent/enterformSmW","800px", "600px","");
		}
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>按课时收费课程 — 学生续费管理列表 </h5>
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
	<form:form id="searchForm" modelAttribute="TClassStudent" action="${ctx}/teaching/tClassStudent/renewlist" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div class="form-group">
			<span>学生姓名：</span>
				<sys:gridselect url="${ctx}/school/tSchoolStudent/selectlist" id="student" name="student.id"  value="${TClassStudent.student.id}"  title="选择学生" labelName="student.name" 
					labelValue="${TClassStudent.student.name}" cssClass="form-control required" fieldLabels="" fieldKeys="" searchLabel="" searchKey="" width="900px" height="550px"></sys:gridselect>
			
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
			<shiro:hasPermission name="teaching:tClassStudent:export">
	       		<table:exportExcel url="${ctx}/teaching/tClassStudent/export"></table:exportExcel>
	       	</shiro:hasPermission>
	       	-->
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
				<th class="sort-column name">学生姓名</th>
				<th class="sort-column classId">课程</th>
				<th class="sort-column classId">教师</th>
				<th class="sort-column createDate">报名时间</th>
				<th class="sort-column classId">开课日期</th>
				
				<th class="sort-column amount">缴费金额</th>
				<th class="sort-column zks">总课时</th>
				<th class="sort-column ywc">已完成课时</th>
				<th class="sort-column balance">可用余额</th>
				<th class="sort-column renewcount">续费次数</th>
				<th class="sort-column employeeCode">课程顾问</th>
				<th class="sort-column status">报名表状态</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tClassStudent">
			<tr>
				<td><a  href="#" onclick="openDialogView('查看学生信息', '${ctx}/school/tSchoolStudent/form?id=${tClassStudent.student.id}','900px', '500px')">
					${tClassStudent.student.name}
				</a></td>
				<td><a  href="#" onclick="openDialogView('查看课程信息', '${ctx}/subject/tCourseClass/form?id=${tClassStudent.courseclass.id}','900px', '500px')">
					${tClassStudent.courseclass.classDesc}
				</a>
				</td>
				<td>
					${tClassStudent.courseclass.teacher.name}
				</td>
				<td>
					<fmt:formatDate value="${tClassStudent.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${tClassStudent.courseclass.beginDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					${tClassStudent.amount}
				</td>
				<td>
					${tClassStudent.zks}
				</td>
				<td>
					${tClassStudent.ywcks}
				</td>
				<td>
					${tClassStudent.balance}
				</td>
				<td>
					${tClassStudent.renewcount}
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
					<c:if test="${tClassStudent.courseclass.status<3 && tClassStudent.status==1}">
					<shiro:hasPermission name="teaching:tClassStudent:edit">
    					<a href="#" onclick="openDialog('续费', '${ctx}/teaching/tClassStudentPay/form?cs.id=${tClassStudent.id}','700px', '400px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 续费</a>
    				</shiro:hasPermission>
    				<c:if test="${tClassStudent.renewcount>0}">
    				<shiro:hasPermission name="teaching:tClassStudent:edit">
    					<a href="#" onclick="openDialog('续费记录查询', '${ctx}/teaching/tClassStudentPay/querylist?cs.id=${tClassStudent.id}','800px', '400px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 查询续费记录</a>
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