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
		function selectDate(dateStr)
		{
			if ($("#campusId").val()=="")
			{
				showTip("请选择校区"); return false;
			}
			window.location = "${ctx}/subject/tCourseTimetable/newlist?datestr="+dateStr+"&campusId="+$("#campusId").val();
		}
	</script>
	<style>

.f {
	width: 102px;
	height: 50px;
	position: relative;
	background-color: transparent;
	border-bottom: 1px solid #cdcdcd;
}

.f:before {
	position: absolute;
	top: 0px;
	right: 0;
	left: 0;
	bottom: 0;
	border-bottom: 50px solid #5f79ac;
	border-right: 100px solid transparent;
	content: " ";
}

.f:after {
	position: absolute;
	left: 0;
	right: 1px;
	top: 1px;
	bottom: 1px;
	bottom: 0;
	border-bottom: 50px solid white;
	border-right: 100px solid transparent;
	content: "";
}

.s1 {
	position: absolute;
	left: 10%;
	top: 50%;
	z-index: 200;
	font-size: 14px;
}

.s2 {
	position: absolute;
	left: 50%;
	top: 10%;
	z-index: 200;
	font-size: 14px;
}

</style>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>课程表 </h5>
		<div class="ibox-tools">
			<a class="collapse-link">
				<i class="fa fa-chevron-up"></i>
			</a>  
		</div>
	</div>
    
    <div class="ibox-content">
	<sys:message content="${message}"/>
	
	<!--查询条件-->
	<div class="row">
	<div class="col-sm-12">
		<form:form id="searchForm" modelAttribute="TCourseTimetable" action="${ctx}/subject/tCourseTimetable/newlist" method="post" class="form-inline">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/> 
			<c:if test="${isparent}">
			<div class="form-group">
				<span>校区：</span> 
				<sys:treeselect id="campus" name="campusId" value="${TCourseTimetable.campusId}" labelName="campusName" labelValue="${TCourseTimetable.campusName}"
					title="校区" url="/sys/office/treeData?type=1" cssClass="form-control required" notAllowSelectParent="false" notAllowSelectRoot="false"/>
			</div>  
			<div class="pull-right">
				<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button> 
			</div> 
			</c:if>
		</form:form>
		<br/>
		
	</div>
	</div>
	
	<ul class="nav nav-tabs">
		<c:forEach items="${dayList}" var="tDayTime">
		<c:if test="${isparent}">
			<li class="${tDayTime.istoday}"><a href="javascript:selectDate('${tDayTime.dayString}')">${tDayTime.weekString}(${tDayTime.dayString})</a></li>
		</c:if>
		<c:if test="${!isparent}">
		<li class="${tDayTime.istoday}"><a href="${ctx}/subject/tCourseTimetable/newlist?datestr=${tDayTime.dayString}">${tDayTime.weekString}(${tDayTime.dayString})</a></li>
		</c:if>
		</c:forEach>
	</ul>
		
	<!-- 表格 -->
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th  class="sort-column" style="width:60px">
					<div class="f">
						<span class="s1">时间</span><span class="s2">教室</span>
					</div>
				</th>
				<c:forEach items="${roomList}" var="tSchoolRoom">
					<th class="sort-column" >${tSchoolRoom.roomDesc}</th>
				</c:forEach>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${timeList}" var="tDayTime">
			<tr>
				<td>${tDayTime.timeString}</td>
				<c:forEach items="${tDayTime.ttList}" var="tList">
					<td>
					<c:forEach items="${tList}" var="tCourseTimetable">
					<div class="btn btn-info btn-xs">
					<a  href="#" onclick="openDialogView('查看课程方案', '${ctx}/subject/tCourseClass/form?id=${tCourseTimetable.courseclass==null?'':tCourseTimetable.courseclass.id}','800px', '500px')">
					${tCourseTimetable.courseclass==null?'':tCourseTimetable.courseclass.nametime}
					</a>
					</div>
					</c:forEach>
					</td>
				</c:forEach>
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