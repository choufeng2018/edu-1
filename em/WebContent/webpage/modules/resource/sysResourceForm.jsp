<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>系统资源管理</title>
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
		<form:form id="inputForm" modelAttribute="sysResource" action="${ctx}/resource/sysResource/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">资源名称：</label></td>
					<td class="width-35">
						<form:input path="name" htmlEscape="false" maxlength="50" class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">资源描述：</label></td>
					<td class="width-35">
						<form:textarea path="rdesc" htmlEscape="false" rows="4" maxlength="500" class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">资源级别：</label></td>
					<td class="width-35">
						<form:input path="level" htmlEscape="false" maxlength="11" class="form-control  digits"/>
					</td>
					<td class="width-15 active"><label class="pull-right">资源类型：</label></td>
					<td class="width-35">
						<form:select id="type" path="type" class="form-control m-b">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('resource_type')}" itemValue="value" itemLabel="label" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">是否收费：</label></td>
					<td class="width-35">
						<form:radiobuttons path="isPrice" items="${fns:getDictList('is_price')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks "/>
					</td>
					<td class="width-15 active"><label class="pull-right">资源价格：</label></td>
					<td class="width-35">
						<form:input path="price" htmlEscape="false" class="form-control  number"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">资源单位：</label></td>
					<td class="width-35">
						<form:input path="unit" htmlEscape="false" maxlength="50" class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">备注：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="form-control "/>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>