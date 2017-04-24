<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>课程表管理</title>
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
	</script>
</head>
<body>
		<form:form id="inputForm" modelAttribute="tCourseTimetable" action="${ctx}/subject/tCourseTimetable/check?source=${source}" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">课程名称：</label></td>
					<td class="width-35">
						${tCourseTimetable.courseclass.classDesc}
					</td>
					<td class="width-15 active"><label class="pull-right">上课时间：</label></td>
					<td class="width-35" colspan="3">
						<fmt:formatDate value="${tCourseTimetable.courseDate}" pattern="yyyy-MM-dd"/> ${tCourseTimetable.teactime}
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">教室：</label></td>
					<td class="width-35">
						${tCourseTimetable.room.roomDesc}
					</td>
					<td class="width-15 active"><label class="pull-right">类型：</label></td>
					<td class="width-35">
						${fns:getDictLabel(tCourseTimetable.type, 'timetable_type', '')}
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">状态：</label></td>
					<td class="width-35">
						${fns:getDictLabel(tCourseTimetable.status, 'timetable_status', '')}
					</td>
					<td class="width-15 active"><label class="pull-right">教师：</label></td>
					<td class="width-35">
						${tCourseTimetable.teacher.name}
					</td>
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
						<th width="25%" style="text-align:center">学生</th>
						<th width="25%" style="text-align:center">出勤结果</th>
						<th width="50%" style="text-align:center">说明</th>
					</tr>
				</thead>
				<tbody id="tClassStudentCheckList">
				</tbody>
			</table>
			<script type="text/template" id="testDataChildTpl">//<!--
				<tr id="tClassStudentCheckList{{idx}}">
					<td class="hide">
						<input id="tClassStudentCheckList{{idx}}_id" name="tClassStudentCheckList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="tClassStudentCheckList{{idx}}_studentId" name="tClassStudentCheckList[{{idx}}].studentId" type="hidden" value="{{row.studentId}}"/>
					</td>
					
					<td>
						<input id="tClassStudentCheckList{{idx}}_studentName" name="tClassStudentCheckList[{{idx}}].studentName" type="text" value="{{row.studentName}}" class="form-control" readonly="true"/>
					</td>

					<td style="text-align:center">
						<c:forEach items="${fns:getDictList('check_status')}" var="dict" varStatus="dictStatus">
							<span><input id="tClassStudentCheckList{{idx}}_status${dictStatus.index}" name="tClassStudentCheckList[{{idx}}].status" type="radio" class="i-checks" value="${dict.value}" data-value="{{row.status}}"><label for="tClassStudentCheckList{{idx}}_status${dictStatus.index}">${dict.label}</label></span>
						</c:forEach>					
					</td>
					
					<td>
						<textarea id="tClassStudentCheckList{{idx}}_remarks" name="tClassStudentCheckList[{{idx}}].remarks" rows="1" maxlength="255" class="form-control " style="resize: none;">{{row.remarks}}</textarea>
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var testDataChildRowIdx = 0, testDataChildTpl = $("#testDataChildTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(tCourseTimetable.tClassStudentCheckList)};
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