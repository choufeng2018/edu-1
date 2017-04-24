<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>缴费管理</title>
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
		<form:form id="inputForm" modelAttribute="tClassStudentPay" action="${ctx}/teaching/tClassStudentPay/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="cs.id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		<tbody>
				<tr>
					<td class="width-15 active" colspan="4"><font color="red">请仔细填写续费信息，提交时系统后台将自动生成续费课时的课程表信息</font></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">单课时费用：</label></td>
					<td class="width-35">
						${tClassStudentPay.cs.courseclass.percost} (元/节)
					</td>
					<td class="width-15 active"><label class="pull-right">收费人：</label></td>
					<td class="width-35">${tClassStudentPay.cc.name}</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>缴费金额：</label></td>
					<td class="width-35">
						<form:input path="amount" htmlEscape="false" class="form-control number required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>缴费课时：</label></td>
					<td class="width-35">
						<form:input path="zks" htmlEscape="false" class="form-control number required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>缴费方式：</label></td>
					<td class="width-35" colspan="3">
						<form:select path="paytype" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('pay_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">备注：</label></td>
					<td class="width-35" colspan="3">
						<form:textarea path="remarks" htmlEscape="false" rows="2" maxlength="255" class="form-control "/>
					</td>
				</tr>
		</tbody>
		</table>
	</form:form>
</body>
</html>