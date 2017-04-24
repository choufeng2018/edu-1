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
		
		function setValue(value)
		{
			$("#ttstatus").html(value.split('_item_')[5]);
			$("#courseDate").html(value.split('_item_')[4]);
			$("#teactime").html(value.split('_item_')[2]);
			$("#tname").html(value.split('_item_')[3]);
		}
	</script>
</head>
<body>
		<form:form id="inputForm" modelAttribute="tClassStudentCheck" action="${ctx}/teaching/tClassStudentCheck/bksave" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="parent.id"/>
		<form:hidden path="type"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
		   		<tr>
					<td class="width-15 active" style="background-color:#1ab394" colspan="4"><label class="pull-center">原请假/缺课课时信息</label></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">课程：</label></td>
					<td class="width-35">
						${tClassStudentCheck.parent.tCourseTimetable.courseclass.classDesc}
					</td>
					<td class="width-15 active"><label class="pull-right">上课日期：</label></td>
					<td class="width-35">
						<fmt:formatDate value="${tClassStudentCheck.parent.tCourseTimetable.courseDate}" pattern="yyyy-MM-dd"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">学生：</label></td>
					<td class="width-35">
						${tClassStudentCheck.parent.studentName}
					</td>
					<td class="width-15 active"><label class="pull-right">任课教师：</label></td>
					<td class="width-35">
						${tClassStudentCheck.parent.tCourseTimetable.teacher.name}
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">考勤状态：</label></td>
					<td class="width-35" colspan="3">${fns:getDictLabel(tClassStudentCheck.parent.status, 'check_status', '')}</td>
					<!-- 
						<form:radiobuttons path="status" items="${fns:getDictList('check_status')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks "/>
					-->
				</tr>
				<tr><td colspan="4"></td></tr>
				<tr>
					<td class="width-15 active" style="background-color:#1ab394" colspan="4"><label class="pull-center">补课课时信息</label></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">补习课时：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/subject/tCourseTimetable/selectlistformissed?classid=${tClassStudentCheck.parent.tCourseTimetable.courseclass.id}" id="check" name="tCourseTimetable.id"  value="${tClassStudentCheck.tCourseTimetable.id}"  title="选择班级" labelName="tCourseTimetable.courseclass.classDesc" 
							labelValue="${tClassStudentCheck.tCourseTimetable.courseclass.classDesc}" cssClass="form-control required" fieldLabels="" fieldKeys="" searchLabel="" searchKey="" disabled="true" callback="1">
						</sys:gridselect>
					</td>
					<td class="width-15 active"><label class="pull-right">上课日期：</label></td>
					<td class="width-35" id="courseDate">
						<fmt:formatDate value="${tClassStudentCheck.tCourseTimetable.courseDate}" pattern="yyyy-MM-dd"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">上课时间：</label></td>
					<td class="width-35" id="teactime">
						${tClassStudentCheck.tCourseTimetable.teactime}
					</td>
					<td class="width-15 active"><label class="pull-right">任课教师：</label></td>
					<td class="width-35" id="tname">
						${tClassStudentCheck.tCourseTimetable.teacher.name}
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">课时状态：</label></td>
					<td class="width-35" id="ttstatus">${fns:getDictLabel(tClassStudentCheck.tCourseTimetable.status, 'timetable_status', '')}</td>
					<td class="width-15 active"><label class="pull-right">考勤状态：</label></td>
					<td class="width-35" id="tstatus">${fns:getDictLabel(tClassStudentCheck.status, 'check_status', '')}</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>