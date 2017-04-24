<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>考情管理</title>
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
				elem: '#hopedate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
				event: 'focus' //响应事件。如果没有传入event，则按照默认的click
			});
		});
		function addRow(list, idx, tpl, row){
			$(list).append(Mustache.render(tpl, {
				idx: idx, delBtn: true, row: row
			}));
			$(list+idx).find("select").each(function(){
				$(this).val($(this).attr("data-value"));
			});
			$(list+idx).find("input[type='checkbox'], input[type='radio']").each(function(){
				var ss = $(this).attr("data-value").split(',');
				for (var i=0; i<ss.length; i++){
					if($(this).val() == ss[i]){
						$(this).attr("checked","checked");
					}
				}
			});
		}
		function delRow(obj, prefix){
			var id = $(prefix+"_id");
			var delFlag = $(prefix+"_delFlag");
			if (id.val() == ""){
				$(obj).parent().parent().remove();
			}else if(delFlag.val() == "0"){
				delFlag.val("1");
				$(obj).html("&divide;").attr("title", "撤销删除");
				$(obj).parent().parent().addClass("error");
			}else if(delFlag.val() == "1"){
				delFlag.val("0");
				$(obj).html("&times;").attr("title", "删除");
				$(obj).parent().parent().removeClass("error");
			}
		}
	</script>
</head>
<body>
		<form:form id="inputForm" modelAttribute="tClassStudentCheck" action="${ctx}/teaching/tClassStudentCheck/leavesave" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="status"/>
		<form:hidden path="studentId"/>
		<form:hidden path="studentName"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">学生：</label></td>
					<td class="width-35">
						 ${tClassStudentCheck.studentName} 
						<!--<sys:gridselect url="${ctx}/school/tSchoolStudent/selectlist" id="studentId" name="studentId"  value="${tClassStudentCheck.studentId}"  title="选择学生" labelName="studentName" 
							labelValue="${tClassStudentCheck.studentName}" cssClass="form-control required" fieldLabels="" fieldKeys="name|remarks" searchLabel="" searchKey="" ></sys:gridselect>
						-->
					</td>
					<td class="width-15 active"><label class="pull-right">课程：</label></td>
					<td class="width-35">
						${tClassStudentCheck.tCourseTimetable.courseclass.classDesc}
					</td> 
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">任课教师：</label></td>
					<td class="width-35">
						${tClassStudentCheck.tCourseTimetable.courseclass.teacher.name}
					</td>
					<td class="width-15 active"><label class="pull-right">授课方式：</label></td>
					<td class="width-35">
						${fns:getDictLabel(tClassStudentCheck.tCourseTimetable.courseclass.classType, 'course_type', '')}
					</td>
				</tr>
				 
				<tr>
					<td class="width-15 active"><label class="pull-right">请假课时：</label></td>
					<td class="width-35" colspan="3">
						<sys:gridselect url="${ctx}/subject/tCourseTimetable/selectforleavelist?courseclass.id=${tClassStudentCheck.tCourseTimetable.courseclass.id}" id="courseclass" name="tCourseTimetable.id"  value="${tClassStudentCheck.tCourseTimetable.id}"  title="选择课时" labelName="tCourseTimetable.courseDate" 
							labelValue="${tClassStudentCheck.tCourseTimetable.courseDate}" cssClass="form-control required" cssStyle="width:600px" fieldLabels="" fieldKeys="" searchLabel="" searchKey=""></sys:gridselect>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>是否允许补课：</label></td>
					<td class="width-35">
						<form:radiobuttons path="canmakeup" items="${fns:getDictList('can_makeup')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks required"/>
					</td>
					<td class="width-15 active"><label class="pull-right">意向补课日期：</label></td>
					<td class="width-35">
						<input id="hopedate" name="hopedate" type="text" maxlength="20" class="laydate-icon form-control layer-date"
							value="<fmt:formatDate value="${tClassStudentCheck.hopedate}" pattern="yyyy-MM-dd"/>"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>请假原因：</label></td>
					<td class="width-35" colspan="3">
						<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="500" class="form-control required"/>
						<font color="red">请简单填写请假说明，以便任课教师在考勤时能够知晓</font>
					</td>
				</tr> 
		 	</tbody>
		</table> 
	</form:form>
</body>
</html>