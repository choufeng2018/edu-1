<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>课程信息管理</title>
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
		<form:form id="inputForm" modelAttribute="tCommSubject" action="${ctx}/school/tCommSubject/saveFL" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>分类编码：</label></td>
					<td class="width-35">
						<form:input path="subjectCode" htmlEscape="false" maxlength="30" class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>分类名称：</label></td>
					<td class="width-35">
						<form:input path="subjectName" htmlEscape="false" maxlength="30" class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">课程描述：</label></td>
					<td class="width-35" colspan="3">
						<form:textarea path="subjectDesc" htmlEscape="false" rows="3" maxlength="500" class="form-control "/>
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