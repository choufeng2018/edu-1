<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>报名管理</title>
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
		<form:form id="inputForm" modelAttribute="tClassStudent" action="${ctx}/teaching/tClassStudent/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="cc.id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">课程班级：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/subject/tCourseClass/selectlist" id="courseclass" name="courseclass.id"  value="${tClassStudent.courseclass.id}"  title="选择课程班级" labelName="courseclass.classDesc" 
					labelValue="${tClassStudent.courseclass.classDesc}" cssClass="form-control required" fieldLabels="" fieldKeys="" searchLabel="" searchKey="" width="900px"/>
					</td>
					<td class="width-15 active"><label class="pull-right">学生姓名：</label></td>
					<td class="width-35">
						<form:input path="student.name" htmlEscape="false" maxlength="30" class="form-control " readonly="true"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">是否已缴费：</label></td>
					<td class="width-35">
						<form:select path="ispay" class="form-control ">
							<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right">缴费方式：</label></td>
					<td class="width-35">
						<form:select path="paytype" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('pay_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">缴费总金额：</label></td>
					<td class="width-35">
						<form:input path="amount" htmlEscape="false" class="form-control  number"/>
					</td>
					<td class="width-15 active"><label class="pull-right">可用余额：</label></td>
					<td class="width-35">
						<form:input path="balance" htmlEscape="false" class="form-control  number"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">课程顾问：</label></td>
					<td class="width-35">
						<form:input path="cc.name" htmlEscape="false" maxlength="30" class="form-control " readonly="true"/>
					</td>
					<td class="width-15 active"><label class="pull-right">状态：</label></td>
					<td class="width-35">
						<form:select path="status" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('enter_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">备注：</label></td>
					<td class="width-35" colspan="3">
						<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="255" class="form-control "/>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>