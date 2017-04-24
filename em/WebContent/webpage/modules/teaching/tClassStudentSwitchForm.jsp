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
			$("#pri").val(value.split('_item_')[3]);
		}
	</script>
</head>
<body> 
		<form:form id="" modelAttribute="tClassStudent" action="" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="cc.id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
		   		<tr>
					<td class="width-15 active" style="background-color:#1ab394" colspan="4"><label class="pull-center">原课程报名信息</label></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">课程班级：</label></td>
					<td class="width-35">${tClassStudent.courseclass.classDesc}</td>
					<td class="width-15 active"><label class="pull-right">学生姓名：</label></td>
					<td class="width-35">${tClassStudent.student.name}</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">缴费总金额：</label></td>
					<td class="width-35">${tClassStudent.amount}</td>
					<td class="width-15 active"><label class="pull-right">总课时：</label></td>
					<td class="width-35">${tClassStudent.courseclass.classHour}</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">已完成课时：</label></td>
					<td class="width-35">${iscomplete}</td>
					<td class="width-15 active"><label class="pull-right">每课时费用：</label></td>
					<td class="width-35">${tClassStudent.courseclass.percost}</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">可用余额：</label></td>
					<td class="width-35">${tClassStudent.balance}</td>
					<td class="width-15 active"><label class="pull-right">课程顾问：</label></td>
					<td class="width-35">${tClassStudent.cc.name}</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
	<br/>
	<form:form id="inputForm" modelAttribute="tClassStudentNew" action="${ctx}/teaching/tClassStudent/save?quiteid=${tClassStudent.id}&classid=${tClassStudent.courseclass.id}&source=${source}" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="cc.id"/>
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
		   		<tr>
					<td class="width-15 active" style="background-color:#1ab394" colspan="4"><label class="pull-center">新班级报名信息</label></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">课程：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/subject/tCourseClass/selectlist" id="courseclass" name="courseclass.id"  value="${tClassStudentNew.courseclass.id}"  title="选择课程班级" labelName="courseclass.classDesc" 
							labelValue="${tClassStudentNew.courseclass.classDesc}" cssClass="form-control required" fieldLabels="" fieldKeys="" searchLabel="" searchKey="" width="900px" callback="1"/>
					</td>
					<td class="width-15 active"><label class="pull-right">学生姓名：</label></td>
					<td class="width-35">
						<form:input path="student.name" htmlEscape="false" maxlength="30" class="form-control " readonly="true"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">课程费用：</label></td>
					<td class="width-35">
						<form:input id="pri" path="courseclass.cost" htmlEscape="false" class="form-control number" readonly="true"/>
						<!--<div id="pri">${(tClassStudentNew.courseclass!=null && tClassStudentNew.courseclass.cost!=null)?tClassStudentNew.courseclass.cost:""}</div>-->
					</td>
					<td class="width-15 active"><label class="pull-right">总课时：</label></td>
					<td class="width-35">
						<form:input path="zks" htmlEscape="false" class="form-control number" readonly="true"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">是否已缴费：</label></td>
					<td class="width-35">
						<form:select path="ispay" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right">结转费用：</label></td>
					<td class="width-35">
						<form:input path="parent.balance" htmlEscape="false" class="form-control number" readonly="true"/>
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
					<td class="width-15 active"><label class="pull-right">状态：</label></td>
					<td class="width-35">
						<form:select path="status" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('enter_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right">课程顾问：</label></td>
					<td class="width-35">
						<form:input path="cc.name" htmlEscape="false" maxlength="30" class="form-control " readonly="true"/>
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