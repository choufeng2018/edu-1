<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>考情管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  $("#inputForm").submit();
			  return true;
		  }
	
		  return false;
		}
		$(document).ready(function() {
			validateForm = $("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
			
		});
	</script>
</head>
<body>
		<form:form id="inputForm" modelAttribute="tClassStudentCheck" action="${ctx}/teaching/tClassStudentCheck/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">课程：</label></td>
					<td class="width-35">
						${tClassStudentCheck.tCourseTimetable.courseclass.classDesc}
					</td>
					<td class="width-15 active"><label class="pull-right">上课日期：</label></td>
					<td class="width-35">
						<fmt:formatDate value="${tClassStudentCheck.tCourseTimetable.courseDate}" pattern="yyyy-MM-dd"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">学生：</label></td>
					<td class="width-35">
						${tClassStudentCheck.studentName}
					</td>
					<td class="width-15 active"><label class="pull-right">任课教师：</label></td>
					<td class="width-35">
						${tClassStudentCheck.tCourseTimetable.teacher.name}
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">考勤状态：</label></td>
					<td class="width-35" colspan="3">
						<form:radiobuttons path="status" items="${fns:getDictList('check_status')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks "/>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>