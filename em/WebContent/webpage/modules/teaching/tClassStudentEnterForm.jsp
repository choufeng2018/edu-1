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
		function setValue(value)
		{
			$("#zks").val(value.split('_item_')[2]);
			$("#amount").val(value.split('_item_')[3]);
			$("#pri").html(value.split('_item_')[3]);
		}
	</script>
</head>
<body>
		<form:form id="inputForm" modelAttribute="tClassStudent" action="${ctx}/teaching/tClassStudent/entersave?source=${source}" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<!--<form:hidden path="courseclass.id"/>-->
		<form:hidden path="cc.id"/>
		<form:hidden path="schoolId"/>
		<form:hidden path="campusId"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>课程班级：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/subject/tCourseClass/selectlist" id="courseclass" name="courseclass.id"  value="${tClassStudent.courseclass.id}"  title="选择班级" labelName="courseclass.classDesc" 
							labelValue="${tClassStudent.courseclass.classDesc}" cssClass="form-control required" fieldLabels="" fieldKeys="" searchLabel="" searchKey="" disabled="true" callback="1"></sys:gridselect>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>学生：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/school/tSchoolStudent/selectlist" id="studentId" name="student.id"  value="${tClassStudent.student.id}"  title="选择学生" labelName="student.name" 
							labelValue="${tClassStudent.student.name}" cssClass="form-control required" fieldLabels="" fieldKeys="name|remarks" searchLabel="" searchKey="" ></sys:gridselect>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">课程费用（元）：</label></td>
					<td class="width-35" colspan="3"><div id="pri">${(tClassStudent.courseclass!=null && tClassStudent.courseclass.cost!=null)?tClassStudent.courseclass.cost:""}</div></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>是否已缴费：</label></td>
					<td class="width-35">
						<form:select path="ispay" class="form-control required">
							<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><font color="red">*</font><label class="pull-right">缴费方式：</label></td>
					<td class="width-35">
						<form:select path="paytype" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('pay_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>缴费金额：</label></td>
					<td class="width-35">
						<form:input path="amount" htmlEscape="false" class="form-control number"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>缴费总课时：</label></td>
					<td class="width-35">
						<form:input path="zks" htmlEscape="false" class="form-control number"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>课程顾问：</label></td>
					<td class="width-35">
						<form:input path="cc.name" htmlEscape="false" maxlength="30" class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>状态：</label></td>
					<td class="width-35">
						<form:select path="status" class="form-control required">
							<!--<form:option value="" label=""/>-->
							<form:options items="${fns:getDictList('enter_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
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