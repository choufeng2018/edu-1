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
		<form:form id="inputForm" modelAttribute="tClassStudent" action="${ctx}/teaching/tClassStudentCheck/leavesave1" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="student.id"/>
		<form:hidden path="student.name"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">学生：</label></td>
					<td class="width-35">
						 ${tClassStudent.student.name} 
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
						<th width="10%" style="text-align:center">是否请假</th>
						<th width="40%" style="text-align:center">上课时间</th> 
						<th width="20%" style="text-align:center">是否允许补课</th>
						<th width="30%" style="text-align:center">意向补课时间</th>
					</tr>
				</thead>
				<tbody id="tClassStudentCheckList">
				</tbody>
			</table>
			<script type="text/template" id="testDataChildTpl">//<!--
				<tr id="tClassStudentCheckList{{idx}}">
					<td class="hide">
						<input id="tClassStudentCheckList{{idx}}_id" name="tClassStudentCheckList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="tClassStudentCheckList{{idx}}_tCourseTimetable" name="tClassStudentCheckList[{{idx}}].tCourseTimetable.id" type="hidden" value="{{row.ttid}}"/>
					</td>

					<td style="text-align:center">
						<c:forEach items="${fns:getDictList('is_qj')}" var="dict" varStatus="dictStatus">
							<span><input id="tClassStudentCheckList{{idx}}_status${dictStatus.index}" name="tClassStudentCheckList[{{idx}}].status" type="checkbox" class="i-checks" value="${dict.value}" data-value="{{row.status}}"></span>
						</c:forEach>
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
						<input id="tClassStudentCheckList{{idx}}_hopedate" name="tClassStudentCheckList[{{idx}}].hopedate" type="text" readonly="readonly" maxlength="20" class="laydate-icon form-control layer-date "
							value="{{row.hopedate}}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',isShowClear:false});"/>					
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