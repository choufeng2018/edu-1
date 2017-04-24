<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>教室管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if ($("#useType").val() == 2)
		  {
		  	if ($("#courseId").val() == "")
		  	{
		  		//alert("请选择用途学科！");
		  		top.layer.msg("请选择用途学科！", {icon: 0});
		  		return false;
		  	}
		  }
		  
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
		
		function validata(checkparam, tip)
		{
			var result = {tag:"1", str:""};
			if (checkparam == "" || checkparam == null)
			{
				result.tag = "0";
				result.str = "请配置校验参数";
			} else if ($("#"+checkparam+"Id").val() == "")
			{
				result.tag = "0";
				result.str = "请选择"+tip+"！";
			} else { 
				result.str = checkparam+"="+$("#"+checkparam+"Id").val();
			}
			return result;
		}
		function changeUseType()
		{
			if ($("#useType").val() == 2)
			{
				$("#sub").attr("style", "display:''");
				$("#subtitl").attr("style", "display:''");
			} else {
				$("#sub").attr("style", "display:none");
				$("#subtitl").attr("style", "display:none");
			}
		}
	</script>
</head>
<body>
		<form:form id="inputForm" modelAttribute="tSchoolRoom" action="${ctx}/school/tSchoolRoom/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>教室编码：</label></td>
					<td class="width-35">
						<form:input path="roomCode" htmlEscape="false" maxlength="64" class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>教室名称：</label></td>
					<td class="width-35">
						<form:input path="roomDesc" htmlEscape="false" maxlength="64" class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>所属校区：</label></td>
					<td class="width-35">
						<sys:treeselect id="campus" name="campus.id" value="${tSchoolRoom.campus.id}" labelName="campus.name" labelValue="${tSchoolRoom.campus.name}"
							title="校区" url="/sys/office/treeDataCampus?type=1" cssClass="form-control required" notAllowSelectParent="true" notAllowSelectRoot="true"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>状态：</label></td>
					<td class="width-35">
						<form:select path="status" class="form-control required">
							<form:options items="${fns:getDictList('school_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>用途类型：</label></td>
					<td class="width-35">
						<form:select path="useType" class="form-control required" onchange="changeUseType()">
							<form:options items="${fns:getDictList('use_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right" id="subtitl" style="display:${tSchoolRoom.useType=='2'?'':'none'}"><font color="red">*</font>用途学科：</label></td>
					<td class="width-35" id="sub" style="display:${tSchoolRoom.useType=='2'?'':'none'}">
						<sys:treeselectcheckcampus id="course" name="course.id" value="${tSchoolRoom.course.id}" labelName="course.subjectName" labelValue="${tSchoolRoom.course.subjectName}" 
							title="课程" url="/school/tCommSubject/treeData" cssClass="form-control required" notAllowSelectParent="false" notAllowSelectRoot="false" needcheck="true" checkparam="campus" checktip="校区"/>
						<span class="help-inline">请注意：如果是专用教室，请选择用途学科，即专用于哪类或哪门专业学科</span>
					</td>
				</tr>
				<tr>
					<!--  
					<td class="width-15 active"><label class="pull-right">教室用途描述：</label></td>
					<td class="width-35" colspan="3">
						<form:textarea path="useDesc" htmlEscape="false" rows="2" maxlength="200" class="form-control "/>
					</td>
					-->
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