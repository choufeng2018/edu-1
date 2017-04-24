<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>学科分类管理</title>
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
		<form:form id="inputForm" modelAttribute="tCommSubjectField" action="${ctx}/school/tCommSubjectField/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>分类编码：</label></td>
					<td class="width-35">
						<form:input path="fieldCode" htmlEscape="false" maxlength="30" class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>分类名称：</label></td>
					<td class="width-35">
						<form:input path="fieldName" htmlEscape="false" maxlength="30" class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>所属校区：</label></td>
					<td class="width-35">
						<sys:treeselect id="campus" name="campus.id" value="${tCommSubjectField.campus.id}" labelName="campus.name" labelValue="${tCommSubjectField.campus.name}"
							title="校区" url="/sys/office/treeData?type=1" cssClass="form-control required" notAllowSelectParent="true" notAllowSelectRoot="true"/>
					</td>
					<td class="width-15 active"></td>
					<td class="width-35">
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