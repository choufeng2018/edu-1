<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>课程方案管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  /**
		  if (($("#shour").val() == $("#ehour").val()) && ($("#smin").val() == $("#emin").val()))
		  {
		  	top.layer.msg("上课时间的 起止时间不能相同！", {icon: 0});
		  	return false;
		  }
		  **/
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
		
					//laydate({
			        //    elem: '#begintime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
			        //    event: 'focus' //响应事件。如果没有传入event，则按照默认的click
			        //});
					//laydate({
			        //    elem: '#endtime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
			        //    event: 'focus' //响应事件。如果没有传入event，则按照默认的click
			        //});
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
	</script>
</head>
<body>
		<form:form id="inputForm" modelAttribute="tCourseClass" action="${ctx}/subject/tCourseClass/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="classType"/>
		<form:hidden path="slot"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr style="display:${isparent?'':'none'}">
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>校区：</label></td>
					<td class="width-35"> 
						<sys:treeselect id="campus" name="campus.id" value="${tCourseClass.campus.id}" labelName="campus.name" labelValue="${tCourseClass.campus.name}"
							title="校区" url="/sys/office/treeDataCampus?type=1" cssClass="form-control required" notAllowSelectParent="true" notAllowSelectRoot="true"/>
					</td>
					<td class="width-15 active"></td>
					<td class="width-35"></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>班级编码：</label></td>
					<td class="width-35">
						<form:input path="classCode" htmlEscape="false" maxlength="64" class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>班级名称：</label></td>
					<td class="width-35">
						<form:input path="classDesc" htmlEscape="false" maxlength="64" class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>专业：</label></td>
					<td class="width-35">
						<sys:treeselectcheckcampus id="subject" name="subject.id" value="${tCourseClass.subject.id}" labelName="subject.subjectName" labelValue="${tCourseClass.subject.subjectName}" 
							title="课程" url="/school/tCommSubject/treeData" cssClass="form-control required" notAllowSelectParent="true" notAllowSelectRoot="true" needcheck="true" checkparam="campus" checktip="校区"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>教师：</label></td>
					<td class="width-35">
						<sys:treeselectcheckcampus id="teacher" name="teacher.id" value="${tCourseClass.teacher.id}" labelName="teacher.name" labelValue="${tCourseClass.teacher.name}"
							title="教师" url="/school/tSchoolTeacher/treeData" cssClass="form-control required" notAllowSelectParent="true" notAllowSelectRoot="true" needcheck="true" checkparam="subject"  checktip="学科"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>招生对象：</label></td>
					<td class="width-35">
						<form:input path="recruit" htmlEscape="false" maxlength="200" class="form-control required"/>
					</td>
				
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>招生人数：</label></td>
					<td class="width-35">
						<form:input path="totalStu" htmlEscape="false" maxlength="11" class="form-control  digits required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>总课次：</label></td>
					<td class="width-35">
						<form:input path="classHour" htmlEscape="false" maxlength="11" class="form-control  digits required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>课时时长<br/>(分钟)：</label></td>
					<td class="width-35">
						<form:input path="classMin" htmlEscape="false" maxlength="64" class="form-control  digits required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>开课日期：</label></td>
					<td class="width-35">
						<input id="beginDate" name="beginDate" type="text" maxlength="20" class="laydate-icon form-control layer-date required"
							value="<fmt:formatDate value="${tCourseClass.beginDate}" pattern="yyyy-MM-dd"/>"/>
					</td> 
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>教室：</label></td>
					<td class="width-35">
						<sys:treeselectcheckcampus id="room" name="room.id" value="${tCourseClass.room.id}" labelName="room.roomDesc" labelValue="${tCourseClass.room.roomDesc}" 
							title="教室" url="/school/tSchoolRoom/treeData" cssClass="form-control required" notAllowSelectParent="true" notAllowSelectRoot="true" needcheck="true" checkparam="campus" checktip="校区" queryparam="subject"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>总费用：</label></td>
					<td class="width-35">
						<form:input path="cost" htmlEscape="false" class="form-control  number required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>计费方式：</label></td>
					<td class="width-35">
						<form:select path="chargeType" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('charge_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<!--  
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>上课时段：</label></td>
					<td class="width-35">
						<form:select path="slot" class="form-control required">
							<form:options items="${fns:getDictList('slot')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>-->
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>上课时间：</label></td>
					<td class="width-35" colspan="3">
						<!--<form:select path="week">
							<form:option value="1" label="周一"/>
							<form:option value="2" label="周二"/>
							<form:option value="3" label="周三"/>
							<form:option value="4" label="周四"/>
							<form:option value="5" label="周五"/>
							<form:option value="6" label="周六"/>
							<form:option value="7" label="周日"/>
						</form:select>
						<form:select path="shour">
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
					    <form:select path="smin">
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
					    <form:select path="ehour">
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
					    <form:select path="emin">
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
					    <div class="tabs-container">
				            <div class="tab-content">
							<div id="tab-1" class="tab-pane active">
							<a class="btn btn-white btn-sm" onclick="addRow('#tCourseClassTimeList', tCourseClassTimeRowIdx, tCourseClassTimeTpl);tCourseClassTimeRowIdx = tCourseClassTimeRowIdx + 1;" title="新增" ><i class="fa fa-plus"></i></a>
							<table id="contentTable" class="table table-striped table-bordered table-condensed">
								<!-- <thead>
									<tr>
										<th class="hide"></th>
										<th>开始时间</th>
										<th width="10">&nbsp;</th>
									</tr>  
								</thead> -->
								<tbody id="tCourseClassTimeList">
								</tbody>
							</table>
							<script type="text/template" id="tCourseClassTimeTpl">//<!--
								<tr id="tCourseClassTimeList{{idx}}">
									<td class="hide">
										<input id="tCourseClassTimeList{{idx}}_id" name="tCourseClassTimeList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
										<input id="tCourseClassTimeList{{idx}}_delFlag" name="tCourseClassTimeList[{{idx}}].delFlag" type="hidden" value="0"/>
										<input id="tCourseClassTimeList{{idx}}_begintime" name="tCourseClassTimeList[{{idx}}].begintime" type="hidden" value="{{row.begintime}}"/>
										
									</td>
									<td>
										<select id="tCourseClassTimeList{{idx}}_week" name="tCourseClassTimeList[{{idx}}].week" data-value="{{row.week}}">
											<option value="1" label="周一"/>
											<option value="2" label="周二"/>
											<option value="3" label="周三"/>
											<option value="4" label="周四"/>
											<option value="5" label="周五"/>
											<option value="6" label="周六"/>
											<option value="7" label="周日"/>
										</select> &nbsp;&nbsp;&nbsp;
										<select id="tCourseClassTimeList{{idx}}_shour" name="tCourseClassTimeList[{{idx}}].shour" data-value="{{row.shour}}">
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
					    				<select id="tCourseClassTimeList{{idx}}_smin" name="tCourseClassTimeList[{{idx}}].smin" data-value="{{row.smin}}">
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
									var data = ${fns:toJson(tCourseClass.tCourseClassTimeList)};
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
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>课程内容：</label></td>
					<td class="width-35" colspan="3">
						<form:textarea path="courseDesc" htmlEscape="false" rows="3" maxlength="500" class="form-control required"/>
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