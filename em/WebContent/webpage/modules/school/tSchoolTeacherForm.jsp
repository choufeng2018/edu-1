<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>教师信息管理</title>
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
				rules: {
					mobile: {remote: "${ctx}/school/tSchoolTeacher/checkMobile?oldtel=" + encodeURIComponent("${tSchoolTeacher.tel}")},
					loginName: {remote: "${ctx}/school/tSchoolTeacher/checkLoginName?oldAccount=" + encodeURIComponent('${tSchoolTeacher.account}')}//设置了远程验证，在初始化时必须预先调用一次。
				},
				messages: {
					mobile: {remote: "该手机号码已存在"},
					loginName: {remote: "用户登录名已存在"}
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
			
			laydate({
				elem: '#birthday', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
				event: 'focus' //响应事件。如果没有传入event，则按照默认的click
			});
		});
	</script>
</head>
<body>
		<form:form id="inputForm" modelAttribute="tSchoolTeacher" action="${ctx}/school/tSchoolTeacher/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
		   		<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>姓名：</label></td>
					<td class="width-35">
						<form:input path="name" htmlEscape="false" maxlength="30" class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>性别：</label></td>
					<td class="width-35">
						<form:select path="sex" class="form-control required">
							<form:options items="${fns:getDictList('sex')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>出生日期：</label></td>
					<td class="width-35">
						<input id="birthday" name="birthday" type="text" maxlength="20" class="laydate-icon form-control layer-date required"
							value="<fmt:formatDate value="${tSchoolTeacher.birthday}" pattern="yyyy-MM-dd"/>"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>年龄：</label></td>
					<td class="width-35">
						<form:input path="age" htmlEscape="false" maxlength="30" class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>所属校区：</label></td>
					<td class="width-35">
						<sys:treeselect id="campus" name="campus.id" value="${tSchoolTeacher.campus.id}" labelName="campus.name" labelValue="${tSchoolTeacher.campus.name}"
							title="校区" url="/sys/office/treeData?type=1" cssClass="form-control required" notAllowSelectParent="true" notAllowSelectRoot="true"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>专业：</label></td>
					<td class="width-35">
						<sys:treeselect id="course" name="course.id" value="${tSchoolTeacher.course.id}" labelName="course.subjectName" labelValue="${tSchoolTeacher.course.subjectName}"
							title="专业" url="/school/tCommSubject/treeData" cssClass="form-control required" notAllowSelectParent="true" notAllowSelectRoot="true"/>
					</td>
					
				</tr>
				<tr>  
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>类型：</label></td>
					<td class="width-35">
						<form:select path="type" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('teacher_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>级别：</label></td>
					<td class="width-35">
						<form:select path="level" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('teacher_level')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">工号：</label></td>
					<td class="width-35">
						<form:input path="number" htmlEscape="false" maxlength="30" class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>手机号码：</label></td>
					<td class="width-35">
						<form:input path="tel" htmlEscape="false" maxlength="30" class="form-control required digits"/>
					</td>
					
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">邮箱：</label></td>
					<td class="width-35">
						<form:input path="email" htmlEscape="false" maxlength="30" class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">状态：</label></td>
					<td class="width-35">
						<form:select path="status" class="form-control required">
							<form:options items="${fns:getDictList('teacher_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr> 
				<tr> 
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>参与1对1教学：</label></td>
					<td class="width-35">
						<form:select path="isoto" class="form-control required">
							<form:options items="${fns:getDictList('is_oto')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15" colspan="3" ><label class="pull-left"><font color="red">只有参与1对1教学的教师才能在学生1对1报名时选择</font></label></td>
				</tr>
			 	<!--  
				<c:if test="${empty tSchoolTeacher.id}">
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>登录账号：</label></td>
					<td class="width-35">
						<form:input path="account" htmlEscape="false" maxlength="30" class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>密码：</label></td>
					<td class="width-35">
						<form:input path="password" htmlEscape="false" maxlength="30" class="form-control "/>
					</td> 
				</tr>
				</c:if>
				-->
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