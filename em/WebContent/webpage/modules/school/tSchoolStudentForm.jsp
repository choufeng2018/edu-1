<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>学生信息管理</title>
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
			
			laydate({
				elem: '#birthday', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
				event: 'focus' //响应事件。如果没有传入event，则按照默认的click
			});
			laydate({
				elem: '#intenTime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
				event: 'focus' //响应事件。如果没有传入event，则按照默认的click
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
				result.str = checkparam+"Id="+$("#"+checkparam+"Id").val();
			}
			return result;
		}
	</script>
</head>
<body>
		<form:form id="inputForm" modelAttribute="tSchoolStudent" action="${ctx}/school/tSchoolStudent/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="status"/>
		<form:hidden path="schoolId"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>姓名：</label></td>
					<td class="width-35">
						<form:input path="name" htmlEscape="false" maxlength="30" class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>所属校区：</label></td>
					<td class="width-35">
						<sys:treeselect id="campus" name="campus.id" value="${tSchoolStudent.campus.id}" labelName="campus.name" labelValue="${tSchoolStudent.campus.name}"
							title="校区" url="/sys/office/treeData?type=1" cssClass="form-control required" notAllowSelectParent="true" notAllowSelectRoot="true"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>性别：</label></td>
					<td class="width-35">
						<form:select path="sex" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('sex')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>联系电话：</label></td>
					<td class="width-35">
						<form:input path="tel" htmlEscape="false" maxlength="15" class="form-control required digits"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>出生年月：</label></td>
					<td class="width-35">
						<input id=birthday name="birthday" type="text" maxlength="20" class="laydate-icon form-control layer-date required"
							value="<fmt:formatDate value="${tSchoolStudent.birthday}" pattern="yyyy-MM-dd"/>"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>年龄：</label></td>
					<td class="width-35">
						<form:input path="age" htmlEscape="false" maxlength="20" class="form-control required digits"/>
					</td>
					
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">账号：</label></td>
					<td class="width-35">
						<form:input path="account" htmlEscape="false" maxlength="30" class="form-control"/>
					</td>
					<td class="width-15 active"><label class="pull-right">邮箱：</label></td>
					<td class="width-35">
						<form:input path="email" htmlEscape="false" maxlength="20" class="form-control "/>
					</td>  
				</tr> 
				<tr>
					<td class="width-15 active"><label class="pull-right">意向学科：</label></td>
					<td class="width-35" colspan="3">
						<sys:treeselect id="subjects" name="subjectsid" value="${tSchoolStudent.subjectsid}" labelName="subjects" labelValue="${tSchoolStudent.subjects}"
							title="专业学科" url="/school/tCommSubject/treeData" cssClass="form-control" cssStyle="width:500px" checked="true" notAllowSelectParent="true" notAllowSelectRoot="true"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">意向报名时间：</label></td>
					<td class="width-35">
						<input id=intenTime name="intenTime" type="text" maxlength="20" class="laydate-icon form-control layer-date"
							value="<fmt:formatDate value="${tSchoolStudent.intenTime}" pattern="yyyy-MM-dd"/>"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>课程顾问：</label></td>
					<td class="width-35"> 
						<sys:treeselectcheckcampus id="room" name="cc.id" value="${tSchoolStudent.cc.id}" labelName="cc.name" labelValue="${tSchoolStudent.cc.name}" 
							title="课程顾问" url="/sys/user/treeDataCc" cssClass="form-control required" notAllowSelectParent="true" notAllowSelectRoot="true" needcheck="true" checkparam="campus" checktip="校区"/>
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