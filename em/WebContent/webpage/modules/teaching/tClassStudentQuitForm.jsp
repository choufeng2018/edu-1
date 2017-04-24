<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>退课管理</title>
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
		<form:form id="inputForm" modelAttribute="tClassStudentQuit" action="${ctx}/teaching/tClassStudentQuit/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="schoolId"/>
		<form:hidden path="campusId"/>
		<form:hidden path="tClassStudent.id"/>
		<form:hidden path="tClassStudent.student.id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">课程班级：</label></td>
					<td class="width-35">
						<form:input path="tClassStudent.courseclass.classDesc" htmlEscape="false" maxlength="50" class="form-control " readonly="true"/>
					</td>
					<td class="width-15 active"><label class="pull-right">学生姓名：</label></td>
					<td class="width-35">
						<form:input path="tClassStudent.student.name" htmlEscape="false" maxlength="30" class="form-control " readonly="true"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">缴费总金额：</label></td>
					<td class="width-35">
						<form:input path="tClassStudent.courseclass.cost" htmlEscape="false" class="form-control  number" readonly="true"/>
					</td>
					<td class="width-15 active"><label class="pull-right">单课时费用：</label></td>
					<td class="width-35">
						<form:input path="perAmount" htmlEscape="false" class="form-control  number" readonly="true"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>退费金额：</label></td>
					<td class="width-35">
						<form:input path="backAmount" htmlEscape="false" class="form-control  number required"/>
					</td>
					<td class="width-15 active"><label class="pull-right">总课时：</label></td>
					<td class="width-35">
						<form:input path="tClassStudent.courseclass.classHour" htmlEscape="false" maxlength="11" class="form-control  digits" readonly="true"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">可用余额：</label></td>
					<td class="width-35">
						<form:input path="classComplete" htmlEscape="false" maxlength="11" class="form-control  digits" readonly="true"/>
					</td>
					<td class="width-15 active"><label class="pull-right">剩余课时：</label></td>
					<td class="width-35">
						<form:input path="classRest" htmlEscape="false" maxlength="11" class="form-control  digits" readonly="true"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">缺勤课时：</label></td>
					<td class="width-35">
						<form:input path="classMissed" htmlEscape="false" maxlength="11" class="form-control  digits"/>
					</td>
					<td class="width-15 active"><label class="pull-right">请假课时：</label></td>
					<td class="width-35">
						<form:input path="classLeave" htmlEscape="false" maxlength="11" class="form-control  digits"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">补课课时：</label></td>
					<td class="width-35">
						<form:input path="classAdd" htmlEscape="false" maxlength="11" class="form-control  digits"/>
					</td>
					<td class="width-15 active"><label class="pull-right">课程顾问：</label></td>
					<td class="width-35">
						<sys:treeselect id="user" name="user.id" value="${tClassStudentQuit.user.id}" labelName="user.name" labelValue="${tClassStudentQuit.user.name}" disabled="disabled"
							title="用户" url="/sys/office/treeData?type=3" cssClass="form-control " allowClear="true" notAllowSelectParent="true"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">备注：</label></td>
					<td class="width-35" colspan="3">
						<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="form-control "/>
					</td>
		  		</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>