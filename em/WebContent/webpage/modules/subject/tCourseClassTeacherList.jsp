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
			var str = "";
			var ids = "";
		  	$("#contentTable tbody tr td input.i-checks:checkbox").each(function(){
		    	if(true == $(this).is(':checked')){
		      		str += $(this).attr("id")+",";
		    	}
		  	});
		  	if(str.substr(str.length-1)== ','){
		    	ids = str.substr(0,str.length-1);
		  	}
		  	if(ids == ""){
				top.layer.alert('请至少选择一条数据!', {icon: 0, title:'警告'});
				return;
		  	}
			top.layer.confirm('确认要创建课程表吗?', {icon: 3, title:'系统提示'}, function(index){
				window.location = url+"?ids="+ids;
			    top.layer.close(index);
		    });
		}
	</script>
</head>
<body class="gray-bg">
 
    <div class="ibox-content">
	<sys:message content="${message}"/>
 
	<!-- 表格 -->
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th  class="sort-column classDesc">班级名称</th>
				<th  class="sort-column classType">班级类型</th>
				<th  class="sort-column subjectId">学科</th>
				<th  class="sort-column totalStu">人数</th>
				<th  class="sort-column stuNum">已招人数</th>
				<th  class="sort-column recruit">招生对象</th>
				<th  class="sort-column classHour">课次</th>
				<th  class="sort-column classMin">课时时长</th>
				<th  class="sort-column beginDate">开课日期</th>
				<th  class="sort-column week">上课时间</th>
				<th  class="sort-column roomDesc">教室</th>
				<th  class="sort-column cost">总费用</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tCourseClass">
			<tr> 
				<td><a  href="#" onclick="openDialogView('查看课程方案', '${ctx}/subject/tCourseClass/form?id=${tCourseClass.id}','800px', '500px')">
					${tCourseClass.classDesc}
				</a></td>
				<td>
					${fns:getDictLabel(tCourseClass.classType, 'course_type', '')}
				</td>
				<td>
					${tCourseClass.subject.subjectName}
				</td>
				<td>
					${tCourseClass.totalStu}
				</td>
				<td>
					${tCourseClass.stuNum}
				</td>
				<td>
					${tCourseClass.recruit}
				</td>
				<td>
					${tCourseClass.classHour}
				</td>
				<td>
					${tCourseClass.classMin}
				</td>
				<td>
					<fmt:formatDate value="${tCourseClass.beginDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td> 
					${tCourseClass.teactime}
				</td>
				<td>
					${tCourseClass.room.roomDesc}
				</td>
				<td>
					${tCourseClass.cost}
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	 
</div>
</body>
</html>