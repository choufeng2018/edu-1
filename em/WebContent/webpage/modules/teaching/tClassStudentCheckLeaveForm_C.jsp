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
		});
		function addRow(list, idx, tpl, row){
			laydate({
				elem: list+idx+'_hopedate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
				event: 'focus' //响应事件。如果没有传入event，则按照默认的click
			});
			
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
		<form:form id="inputForm" modelAttribute="tClassStudent" action="${ctx}/teaching/tClassStudentCheck/leavesave" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="status"/>
		<form:hidden path="student.id"/>
		<form:hidden path="student.name"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">学生：</label></td>
					<td class="width-35">
						 ${tClassStudent.student.name} 
						<!--<sys:gridselect url="${ctx}/school/tSchoolStudent/selectlist" id="studentId" name="studentId"  value="${tClassStudentCheck.studentId}"  title="选择学生" labelName="studentName" 
							labelValue="${tClassStudentCheck.studentName}" cssClass="form-control required" fieldLabels="" fieldKeys="name|remarks" searchLabel="" searchKey="" ></sys:gridselect>
						-->
					</td>
					<td class="width-15 active"><label class="pull-right">课程：</label></td>
					<td class="width-35">
						${tClassStudent.courseclass.classDesc}
					</td> 
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">任课教师：</label></td>
					<td class="width-35">
						${tClassStudent.courseclass.teacher.name}
					</td>
					<!--  
					<td class="width-15 active"><label class="pull-right">考勤状态：</label></td>
					<td class="width-35" >
						${fns:getDictLabel(tClassStudentCheck.status, 'check_status', '')}
					</td>-->
				</tr>
				
		 	</tbody>
		</table>
		<div class="tabs-container">
            <div class="tab-content">
			<div id="tab-1" class="tab-pane active">
			<table id="contentTable" class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th width="25%" style="text-align:center">上课日期</th>
						<th width="25%" style="text-align:center">上课时间</th>
						<th width="50%" style="text-align:center">教室</th>
						<th width="50%" style="text-align:center">是否允许补课</th>
						<th width="50%" style="text-align:center">意向补课日期</th>
						<th width="50%" style="text-align:center">意向补课时间</th>
					</tr>
				</thead>
				<tbody id="tClassStudentCheckList">
				</tbody>
			</table>
			<script type="text/template" id="testDataChildTpl">//<!--
				<tr id="tClassStudentCheckList{{idx}}">
					<td class="hide">
						<input id="tClassStudentCheckList{{idx}}_id" name="tClassStudentCheckList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
					</td>
					
					<td>
						<input id="tClassStudentCheckList{{idx}}_courseDate" name="tClassStudentCheckList[{{idx}}].courseDate" type="text" value="{{row.courseDate}}" class="form-control" readonly="true"/>
					</td>

					<td>
						<input id="tClassStudentCheckList{{idx}}_teactime" name="tClassStudentCheckList[{{idx}}].teactime" type="text" value="{{row.teactime}}" class="form-control" readonly="true"/>
					</td>

					<td style="text-align:center">
						<c:forEach items="${fns:getDictList('can_makeup')}" var="dict" varStatus="dictStatus">
							<span><input id="tClassStudentCheckList{{idx}}_canmakeup${dictStatus.index}" name="tClassStudentCheckList[{{idx}}].canmakeup" type="radio" class="i-checks" value="${dict.value}" data-value="{{row.canmakeup}}"><label for="tClassStudentCheckList{{idx}}_status${dictStatus.index}">${dict.label}</label></span>
						</c:forEach>					
					</td>
					
					<td>
						<input id="tClassStudentCheckList{{idx}}_hopedate" name="tClassStudentCheckList{{idx}}_hopedate" type="text" maxlength="20" class="laydate-icon form-control layer-date"
							value="<fmt:formatDate value="{{row.hopedate}}" pattern="yyyy-MM-dd"/>"/>					
					</td>
					<td>
						<select id="tClassStudentCheckList{{idx}}_shour" name="tClassStudentCheckList[{{idx}}].shour" data-value="{{row.shour}}">
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
					    <select id="tClassStudentCheckList{{idx}}_smin" name="tClassStudentCheckList[{{idx}}].smin" data-value="{{row.smin}}">
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
						{{#delBtn}}<span class="close" onclick="delRow(this, '#tClassStudentCheckList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var testDataChildRowIdx = 0, testDataChildTpl = $("#testDataChildTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(tClassStudent.tClassStudentCheckList)};
					for (var i=0; i<data.length; i++){
						addRow('#tClassStudentCheckList', testDataChildRowIdx, testDataChildTpl, data[i]);
						testDataChildRowIdx = testDataChildRowIdx + 1;
					}
				});
			</script>
			</div>
			</div>
		</div>
	</form:form>
</body>
</html>