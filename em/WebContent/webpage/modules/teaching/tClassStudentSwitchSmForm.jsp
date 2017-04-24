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
		function setValue(price, item)
		{
			// 先删除所有
			$("#tCourseClassTimeList").text("");
			tCourseClassTimeRowIdx = 0;
			$("#percost").val(price.split(",")[0]);
			$("#classmin").val(price.split(",")[1]);
			//$("#courseclass.percost").find("option[value="+price+"]").attr("selected",true);
			
			var data = item.split('|');
			for (var i=0; i<data.length-1; i++){
				var dv = {week:data[i].split('_ite_')[0], shour:data[i].split('_ite_')[1], smin:data[i].split('_ite_')[2]};
				addRow('#tCourseClassTimeList', tCourseClassTimeRowIdx, tCourseClassTimeTpl, dv);
				tCourseClassTimeRowIdx = tCourseClassTimeRowIdx + 1;
			}
		}
	</script>
</head>
<body> 
		<form:form id="" modelAttribute="tClassStudent" action="" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="cc.id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
		   		<tr>
					<td class="width-15 active" style="background-color:#1ab394" colspan="4"><label class="pull-center">原课程报名信息</label></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">课程班级：</label></td>
					<td class="width-35">${tClassStudent.courseclass.classDesc}</td>
					<td class="width-15 active"><label class="pull-right">学生姓名：</label></td>
					<td class="width-35">${tClassStudent.student.name}</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">缴费总金额：</label></td>
					<td class="width-35">${tClassStudent.amount}</td>
					<td class="width-15 active"><label class="pull-right">总课时：</label></td>
					<td class="width-35">${tClassStudent.courseclass.classHour}</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">已完成课时：</label></td>
					<td class="width-35">${iscomplete}</td>
					<td class="width-15 active"><label class="pull-right">每课时费用：</label></td>
					<td class="width-35">${tClassStudent.courseclass.percost}</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">可用余额：</label></td>
					<td class="width-35">${tClassStudent.balance}</td>
					<td class="width-15 active"><label class="pull-right">课程顾问：</label></td>
					<td class="width-35">${tClassStudent.cc.name}</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
	<br/>
	<form:form id="inputForm" modelAttribute="tClassStudentNew" action="${ctx}/teaching/tClassStudent/entersaveSmW?quiteid=${tClassStudent.id}&classid=${tClassStudent.courseclass.id}&source=${source}" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="courseclass.id"/>
		<form:hidden path="cc.id"/>
		<form:hidden path="student.id"/>
		<form:hidden path="schoolId"/>
		<form:hidden path="campusId"/>
		<form:hidden path="status"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
		   		<tr>
					<td class="width-15 active" style="background-color:#1ab394" colspan="4"><label class="pull-center">新班级报名信息（小班制1对1教学）</label></td>
				</tr>
		  	 	<tr>
		   			<td class="width-15 active"><label class="pull-right"><font color="red">*</font>专业：</label></td>
					<td class="width-35">
						<sys:treeselectcheckcampus id="subject" name="courseclass.subject.id" value="${tClassStudentNew.courseclass.subject.id}" labelName="courseclass.subject.subjectName" labelValue="${tClassStudentNew.courseclass.subject.subjectName}" 
							title="专业学科分类" url="/school/tCommSubject/treeData" cssClass="form-control required" notAllowSelectParent="true" notAllowSelectRoot="true" needcheck="true" checkparam="campusId" checktip="校区"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>任课教师：</label></td>
					<td class="width-35">
						<!--<sys:treeselectcheckcampus id="teacher" name="courseclass.teacher.id" value="${tClassStudentNew.courseclass.teacher.id}" labelName="courseclass.teacher.name" labelValue="${tClassStudentNew.courseclass.teacher.name}"
							title="选择教师" url="/school/tSchoolTeacher/treeData" cssClass="form-control required" notAllowSelectParent="true" notAllowSelectRoot="true" needcheck="true" checkparam="subject"  checktip="学科"/>
						-->
						<sys:gridselectcheck id="teacher" url="${ctx}/school/tSchoolTeacher/selectlist" name="courseclass.teacher.id"  value="${tClassStudentNew.courseclass.teacher.id}"  title="选择教师" labelName="courseclass.teacher.name"
							labelValue="${tClassStudentNew.courseclass.teacher.name}" cssClass="form-control required" fieldLabels="" fieldKeys="name|remarks" searchLabel="" searchKey="" needcheck="true" checkparam="subject" callback="1" checktip="学科"></sys:gridselectcheck>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>学生：</label></td>
					<td class="width-35">
						<form:input path="student.name" htmlEscape="false" maxlength="30" class="form-control " readonly="true"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>教室：</label></td>
					<td class="width-35">
						<sys:treeselectcheckcampus id="room" name="courseclass.room.id" value="${tClassStudentNew.courseclass.room.id}" labelName="courseclass.room.roomDesc" labelValue="${tClassStudentNew.courseclass.room.roomDesc}" 
							title="教室" url="/school/tSchoolRoom/treeData" cssClass="form-control required" notAllowSelectParent="true" notAllowSelectRoot="true" needcheck="true" checkparam="campus" checktip="校区"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>开课日期：</label></td>
					<td class="width-35">
						<input id="beginDate" name="courseclass.beginDate" type="text" maxlength="20" class="laydate-icon form-control layer-date required"
							value="<fmt:formatDate value="${tClassStudentNew.courseclass.beginDate}" pattern="yyyy-MM-dd"/>"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>课时时长（分钟）：</label></td>
					<td class="width-35">
						<form:input id="classmin" path="courseclass.classMin" htmlEscape="false" class="form-control digits required"/>
					</td>
				</tr>
				<tr>
					<!-- 
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>上课时段：</label></td>
					<td class="width-35">
						<form:select path="courseclass.slot" class="form-control required">
							<form:options items="${fns:getDictList('slot')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>-->
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>上课时间：</label></td>
					<td class="width-35" colspan="3">  
						<div class="tabs-container">
				            <div class="tab-content">
							<div id="tab-1" class="tab-pane active">
							<!-- 
							<a class="btn btn-white btn-sm" onclick="addRow('#tCourseClassTimeList', tCourseClassTimeRowIdx, tCourseClassTimeTpl);tCourseClassTimeRowIdx = tCourseClassTimeRowIdx + 1;" title="新增" ><i class="fa fa-plus"></i></a>
							-->
							<table id="contentTable" class="table table-striped table-bordered table-condensed">
								<tbody id="tCourseClassTimeList">
								</tbody>
							</table>
							<script type="text/template" id="tCourseClassTimeTpl">//<!--
								<tr id="tCourseClassTimeList{{idx}}">
									<td class="hide">
										<input id="tCourseClassTimeList{{idx}}_id" name="courseclass.tCourseClassTimeList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
										<input id="courseclass.tCourseClassTimeList{{idx}}_delFlag" name="courseclass.tCourseClassTimeList[{{idx}}].delFlag" type="hidden" value="0"/>
									</td>
									<td>
										<select id="courseclass.tCourseClassTimeList{{idx}}_week" name="courseclass.tCourseClassTimeList[{{idx}}].week" data-value="{{row.week}}">
											<option value="1" label="周一"/>
											<option value="2" label="周二"/>
											<option value="3" label="周三"/>
											<option value="4" label="周四"/>
											<option value="5" label="周五"/>
											<option value="6" label="周六"/>
											<option value="7" label="周日"/>
										</select> &nbsp;&nbsp;&nbsp;
										<select id="courseclass.tCourseClassTimeList{{idx}}_shour" name="courseclass.tCourseClassTimeList[{{idx}}].shour" data-value="{{row.shour}}">
					        				<option value="07" label="07"/>
					        				<option value="08" label="08"/>
					        				<option value="09" label="09"/>
					        				<option value="10" label="10"/>
					        				<option value="11" label="11"/>
					        				<option value="12" label="12"/>
					        				<option value="13" label="13"/>
					        				<option value="14" label="14"/>
					        				<option value="15" label="15"/>
					        				<option value="16" label="16"/>
					        				<option value="17" label="17"/>
					        				<option value="18" label="18"/>
					        				<option value="19" label="19"/>
					        				<option value="20" label="20"/>
					        				<option value="21" label="21"/>
					    				</select>:
					    				<select id="courseclass.tCourseClassTimeList{{idx}}_smin" name="courseclass.tCourseClassTimeList[{{idx}}].smin" data-value="{{row.smin}}">
					    					<option value="00" label="00"/>
					    					<option value="10" label="10"/>
					        				<option value="15" label="15"/>
					        				<option value="20" label="20"/>
					       					<option value="25" label="25"/>
					        				<option value="30" label="30"/>
					        				<option value="35" label="35"/>
					        				<option value="40" label="40"/>
					        				<option value="45" label="45"/>
					        				<option value="50" label="50"/>
					        				<option value="55" label="55"/>
					    				</select> &nbsp;&nbsp;&nbsp;
										{{#delBtn}}<span class="close" onclick="delRow(this, '#tCourseClassTimeList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
									</td>
								</tr>//-->
							</script>
							<script type="text/javascript">
								var tCourseClassTimeRowIdx = 0, tCourseClassTimeTpl = $("#tCourseClassTimeTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
								$(document).ready(function() {
									var data = ${fns:toJson(tClassStudentNew.courseclass.tCourseClassTimeList)};
									for (var i=0; i<data.length; i++){
										addRow('#tCourseClassTimeList', tCourseClassTimeRowIdx, tCourseClassTimeTpl, data[i]);
										tCourseClassTimeRowIdx = tCourseClassTimeRowIdx + 1;
									}
								});
							</script>
							</div>
						</div>
						</div>
					</td>
					
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>收费标准：</label></td>
					<td class="width-35">
						<form:select id="percost" path="courseclass.percost" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('fybz')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select> 
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>是否已缴费：</label></td>
					<td class="width-35">
						<form:select path="ispay" class="form-control required">
							<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>缴费方式：</label></td>
					<td class="width-35">
						<form:select path="paytype" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('pay_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>缴费金额：</label></td>
					<td class="width-35">
						<form:input path="amount" htmlEscape="false" class="form-control  number"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>缴费总课时：</label></td>
					<td class="width-35">
						<form:input path="zks" htmlEscape="false" class="form-control number required"/>
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
	</form:form>
</body>
</html>