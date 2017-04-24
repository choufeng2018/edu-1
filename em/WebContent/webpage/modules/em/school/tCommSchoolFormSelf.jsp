<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>学校管理</title>
	<meta name="decorator" content="default"/>
	<script src="${ctxStatic}/common/tool.js" type="text/javascript"></script>
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  	if($("#tel").val() != "" && vaildTelNum($("#tel").val()) && validateForm.form()){
			  	$("#inputForm").submit();
			  	return true;
		  	} else {
		  		showTipDef("手机号码格式不正确");
		  		return false;
		  	}
		  	if ($("#tel").val() != "" && !vaildEmail($("#email").val()))
		  	{
		  		showTipDef("邮箱格式不正确");
		  		return false;
		  	}
		}
		$(document).ready(function() {
			validateForm = $("#inputForm").validate({
				rules: {
					tel: {remote: "${ctx}/em/school/tCommSchool/checkTel?oldtel=" + encodeURIComponent("${tCommSchool.tel}")},
					email: {remote: "${ctx}/em/school/tCommSchool/checkEmail?oldemail=" + encodeURIComponent("${tCommSchool.email}")}
				},
				messages: {
					tel: {remote: "手机号码已存在"},
					email: {remote: "该邮箱已存在"}
				},
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
		<form:form id="inputForm" modelAttribute="tCommSchool" action="${ctx}/em/school/tCommSchool/saveEdit" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="schoolCode"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>学校名称：</label></td>
					<td class="width-35" colspan="3">
						<form:input path="schoolName" htmlEscape="false" maxlength="100" class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>联系人：</label></td>
					<td class="width-35">
						<form:input path="contact" htmlEscape="false" maxlength="100" class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>联系电话<br/>(手机)：</label></td>
					<td class="width-35">
						<form:input path="tel" htmlEscape="false" maxlength="30" class="form-control required" onkeyup="this.value=this.value.replace(/[^0-9]/g, '')"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>所在省：</label></td>
					<td class="width-35">
						<sys:treeselect id="province" name="province.id" value="${tCommSchool.province.id}" labelName="province.name" 
							labelValue="${tCommSchool.province.name}" title="省" url="/sys/area/treeData?type=2" cssClass="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>所在地市：</label></td>
					<td class="width-35">
						<sys:areatreeselect id="city" name="city.id" value="${tCommSchool.city.id}" labelName="city.name" 
							labelValue="${tCommSchool.city.name}" preObj="province" title="市" url="/sys/area/treeDataByParent?parent=" cssClass="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>所属区县：</label></td>
					<td class="width-35">
						<sys:areatreeselect id="region" name="region.id" value="${tCommSchool.region.id}" labelName="region.name" 
							labelValue="${tCommSchool.region.name}" preObj="city" title="区县" url="/sys/area/treeDataByParent?parent=" cssClass="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right">邮箱：</label></td>
					<td class="width-35">
						<form:input path="email" htmlEscape="false" maxlength="36" class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">学校主页：</label></td>
					<td class="width-35" colspan="3">
						<form:input path="homepage" htmlEscape="false" maxlength="40" class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">学校地址：</label></td>
					<td class="width-35" colspan="3">
						<form:input path="address" htmlEscape="false" maxlength="100" class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">学校简介：</label></td>
					<td class="width-35" colspan="3">
						<form:textarea path="summary" htmlEscape="false" rows="5" maxlength="2000" class="form-control"/>
					</td> 
		  		</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>