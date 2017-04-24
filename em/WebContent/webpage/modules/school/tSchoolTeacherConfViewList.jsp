<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>排课管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() { });
		
		var timeselect = "";
		function getSelectedItem()
		{ 
			var size = $("#contentTable tbody tr td input.i-checks:checked").size();
			if(size == 0 ){
				top.layer.alert('请至少选择一条数据!', {icon: 0, title:'警告'});
				return "-1";
			};
			var value = ""; 
			$("#contentTable tbody tr td input.i-checks:checkbox:checked").each(function() {
				value += $(this).attr("id") + "|";
			});
			return value;
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
				<th rowspan="2"> </th>
				<th class="sort-column ">星期一</th>
				<th class="sort-column ">星期二</th>
				<th class="sort-column ">星期三</th>
				<th class="sort-column ">星期四</th>
				<th class="sort-column ">星期五</th>
				<th class="sort-column ">星期六</th>
				<th class="sort-column ">星期日</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${timeList}" var="tCourseTimetable">
			<tr>
				<td align="center">${tCourseTimetable.teactime}</td>
				<td align="center">
					<c:if test="${!tSchoolTeacherConf.mon}">休</c:if>
					<c:if test="${tSchoolTeacherConf.mon}">
						<c:if test="${!tCourseTimetable.day1}">
						<input type="checkbox" class="i-checks" id="1_ite_${tCourseTimetable.shour}_ite_${tCourseTimetable.smin}_ite_${tCourseTimetable.ehour}_ite_${tCourseTimetable.emin}"/>
						</c:if>
						<c:if test="${tCourseTimetable.day1}">满</c:if>
					</c:if>
				</td> 
				<td align="center">
					<c:if test="${!tSchoolTeacherConf.tue}">休</c:if>
					<c:if test="${tSchoolTeacherConf.tue}">
						<c:if test="${!tCourseTimetable.day2}">
						<input type="checkbox" class="i-checks" id="2_ite_${tCourseTimetable.shour}_ite_${tCourseTimetable.smin}_ite_${tCourseTimetable.ehour}_ite_${tCourseTimetable.emin}"/>
						</c:if>
						<c:if test="${tCourseTimetable.day2 }">满</c:if>
					</c:if>
				</td> 
				<td align="center">
					<c:if test="${!tSchoolTeacherConf.wed}">休</c:if>
					<c:if test="${tSchoolTeacherConf.wed}">
						<c:if test="${tCourseTimetable.day3 == false}">
						<input type="checkbox" class="i-checks" id="3_ite_${tCourseTimetable.shour}_ite_${tCourseTimetable.smin}_ite_${tCourseTimetable.ehour}_ite_${tCourseTimetable.emin}"/>
						</c:if>
						<c:if test="${tCourseTimetable.day3}">满</c:if>
					</c:if>
				</td> 
				<td align="center">
					<c:if test="${!tSchoolTeacherConf.thu}">休</c:if>
					<c:if test="${tSchoolTeacherConf.thu}">
						<c:if test="${tCourseTimetable.day4 == false}">
						<input type="checkbox" class="i-checks" id="4_ite_${tCourseTimetable.shour}_ite_${tCourseTimetable.smin}_ite_${tCourseTimetable.ehour}_ite_${tCourseTimetable.emin}"/>
						</c:if>
						<c:if test="${tCourseTimetable.day4}">满</c:if>
					</c:if>
				</td>
				 
				<td align="center">
					<c:if test="${!tSchoolTeacherConf.fri}">休</c:if>
					<c:if test="${tSchoolTeacherConf.fri}">
						<c:if test="${tCourseTimetable.day5 == false}">
						<input type="checkbox" class="i-checks" id="5_ite_${tCourseTimetable.shour}_ite_${tCourseTimetable.smin}_ite_${tCourseTimetable.ehour}_ite_${tCourseTimetable.emin}"/>
						</c:if>
						<c:if test="${tCourseTimetable.day5}">满</c:if>
					</c:if>
				</td>
				 
				<td align="center">
					<c:if test="${!tSchoolTeacherConf.sat}">休</c:if>
					<c:if test="${tSchoolTeacherConf.sat}">
						<c:if test="${tCourseTimetable.day6 == false}">
						<input type="checkbox" class="i-checks" id="6_ite_${tCourseTimetable.shour}_ite_${tCourseTimetable.smin}_ite_${tCourseTimetable.ehour}_ite_${tCourseTimetable.emin}"/>
						</c:if>
						<c:if test="${tCourseTimetable.day6}">满</c:if>
					</c:if>
				</td>
				 
				<td align="center">
					<c:if test="${!tSchoolTeacherConf.sun}">休</c:if>
					<c:if test="${tSchoolTeacherConf.sun}">
						<c:if test="${tCourseTimetable.day7 == false}">
						<input type="checkbox" class="i-checks" id="7_ite_${tCourseTimetable.shour}_ite_${tCourseTimetable.smin}_ite_${tCourseTimetable.ehour}_ite_${tCourseTimetable.emin}"/> 
						</c:if>
						<c:if test="${tCourseTimetable.day7}">满</c:if>
					</c:if>
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