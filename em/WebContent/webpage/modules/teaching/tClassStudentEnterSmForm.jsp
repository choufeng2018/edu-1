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
			
			laydate({
				elem: '#beginDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
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
				result.str = checkparam+"="+$("#"+checkparam+"Id").val();
			}
			return result;
		}
	</script>
</head>
<body>
		<form:form id="inputForm" modelAttribute="tClassStudent" action="${ctx}/teaching/tClassStudent/entersaveSm" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="courseclass.id"/>
		<form:hidden path="cc.id"/>
		<form:hidden path="schoolId"/>
		<form:hidden path="campusId"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
		  	 	<tr>
		   			<td class="width-15 active"><label class="pull-right"><font color="red">*</font>学科分类：</label></td>
					<td class="width-35">
						<sys:treeselectcheckcampus id="subject" name="courseclass.subject.id" value="${tClassStudent.courseclass.subject.id}" labelName="courseclass.subject.subjectName" labelValue="${tClassStudent.courseclass.subject.subjectName}" 
							title="学科分类" url="/school/tCommSubject/treeData" cssClass="form-control required" notAllowSelectParent="true" notAllowSelectRoot="true" needcheck="true" checkparam="campusId" checktip="校区"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>任课教师：</label></td>
					<td class="width-35">
						<sys:treeselectcheckcampus id="teacher" name="courseclass.teacher.id" value="${tClassStudent.courseclass.teacher.id}" labelName="courseclass.teacher.name" labelValue="${tClassStudent.courseclass.teacher.name}"
							title="选择教师" url="/school/tSchoolTeacher/treeData" cssClass="form-control required" notAllowSelectParent="true" notAllowSelectRoot="true" needcheck="true" checkparam="subject"  checktip="学科"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>收费标准：</label></td>
					<td class="width-35">
						<form:select path="courseclass.percost" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('fybz')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select> 
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>学生：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/school/tSchoolStudent/selectlist" id="student" name="student.id"  value="${tClassStudent.student.id}"  title="选择学生" labelName="student.name" 
							labelValue="${tClassStudent.student.name}" cssClass="form-control required" fieldLabels="" fieldKeys="name|remarks" searchLabel="" searchKey="" ></sys:gridselect>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>开课日期：</label></td>
					<td class="width-35">
						<input id="beginDate" name="courseclass.beginDate" type="text" maxlength="20" class="laydate-icon form-control layer-date required"
							value="<fmt:formatDate value="${tClassStudent.courseclass.beginDate}" pattern="yyyy-MM-dd"/>"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>课时时长（分钟）：</label></td>
					<td class="width-35">
						<form:input path="courseclass.classMin" htmlEscape="false" class="form-control digits required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>上课时间：</label></td>
					<td class="width-35" colspan="3">
						<form:select path="courseclass.week">
							<form:option value="1" label="周一"/>
							<form:option value="2" label="周二"/>
							<form:option value="3" label="周三"/>
							<form:option value="4" label="周四"/>
							<form:option value="5" label="周五"/>
							<form:option value="6" label="周六"/>
							<form:option value="7" label="周日"/>
						</form:select>
						<form:select path="courseclass.shour">
					        <form:option value="07" label="07"/>
					        <form:option value="08" label="08"/>
					        <form:option value="09" label="09"/>
					        <form:option value="10" label="10"/>
					        <form:option value="11" label="11"/>
					        <form:option value="12" label="12"/>
					        <form:option value="13" label="13"/>
					        <form:option value="14" label="14"/>
					        <form:option value="15" label="15"/>
					        <form:option value="16" label="16"/>
					        <form:option value="17" label="17"/>
					        <form:option value="18" label="18"/>
					        <form:option value="19" label="19"/>
					        <form:option value="20" label="20"/>
					        <form:option value="21" label="21"/>
					    </form:select>:
					    <form:select path="courseclass.smin">
					    	<form:option value="00" label="00"/>
					    	<form:option value="10" label="10"/>
					        <form:option value="15" label="15"/>
					        <form:option value="20" label="20"/>
					        <form:option value="25" label="25"/>
					        <form:option value="30" label="30"/>
					        <form:option value="35" label="35"/>
					        <form:option value="40" label="40"/>
					        <form:option value="45" label="45"/>
					        <form:option value="50" label="50"/>
					        <form:option value="55" label="55"/>
					    </form:select> 
					    <!-- -- 
					    <form:select path="courseclass.ehour">
					        <form:option value="07" label="07"/>
					        <form:option value="08" label="08"/>
					        <form:option value="09" label="09"/>
					        <form:option value="10" label="10"/>
					        <form:option value="11" label="11"/>
					        <form:option value="12" label="12"/>
					        <form:option value="13" label="13"/>
					        <form:option value="14" label="14"/>
					        <form:option value="15" label="15"/>
					        <form:option value="16" label="16"/>
					        <form:option value="17" label="17"/>
					        <form:option value="18" label="18"/>
					        <form:option value="19" label="19"/>
					        <form:option value="20" label="20"/>
					        <form:option value="21" label="21"/>
					    </form:select>:
					    <form:select path="courseclass.emin">
					    	<form:option value="00" label="00"/>
					    	<form:option value="10" label="10"/>
					        <form:option value="15" label="15"/>
					        <form:option value="20" label="20"/>
					        <form:option value="25" label="25"/>
					        <form:option value="30" label="30"/>
					        <form:option value="35" label="35"/>
					        <form:option value="40" label="40"/>
					        <form:option value="45" label="45"/>
					        <form:option value="50" label="50"/>
					        <form:option value="55" label="55"/>
					    </form:select> -->
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>教室：</label></td>
					<td class="width-35">
						<sys:treeselectcheckcampus id="room" name="courseclass.room.id" value="${tClassStudent.courseclass.room.id}" labelName="courseclass.room.roomDesc" labelValue="${tClassStudent.courseclass.room.roomDesc}" 
							title="教室" url="/school/tSchoolRoom/treeData" cssClass="form-control required" notAllowSelectParent="true" notAllowSelectRoot="true" needcheck="true" checkparam="campus" checktip="校区"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>上课时段：</label></td>
					<td class="width-35">
						<form:select path="courseclass.slot" class="form-control required">
							<form:options items="${fns:getDictList('slot')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>是否已缴费：</label></td>
					<td class="width-35">
						<form:select path="ispay" class="form-control required">
							<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right">缴费方式：</label></td>
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
						<form:input path="amount" htmlEscape="false" class="form-control  number required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>缴费总课时：</label></td>
					<td class="width-35">
						<form:input path="zks" htmlEscape="false" class="form-control number required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>状态：</label></td>
					<td class="width-35">
						<form:select path="status" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('enter_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>课程顾问：</label></td>
					<td class="width-35">
						<form:input path="cc.name" htmlEscape="false" maxlength="30" class="form-control required" readonly="true"/>
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
		<!-- 
		<div class="mail-body text-center tooltip-demo">
        	<button type="button" class="btn btn-primary  btn-sm" onclick="doSubmit()"> <i class="fa fa-up"></i> 提交报名表</button>
        	<a href="${ctx}/iim/mailBox/list" class="btn btn-danger btn-sm" data-toggle="tooltip" data-placement="top" title="Discard email"><i class="fa fa-times"></i> 放弃</a>
       		<button type="button" class="btn btn-primary  btn-sm" onclick="addNew()"> <i class="fa fa-plus"></i> 新增报名</button>
   		</div>
   		 -->
	</form:form>
</body>
</html>