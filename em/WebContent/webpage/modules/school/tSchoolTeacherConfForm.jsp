<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>排课管理</title>
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
				elem: '#mtime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
				event: 'focus' //响应事件。如果没有传入event，则按照默认的click
			});
			laydate({
				elem: '#atime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
				event: 'focus' //响应事件。如果没有传入event，则按照默认的click
			});
			laydate({
				elem: '#ntime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
				event: 'focus' //响应事件。如果没有传入event，则按照默认的click
			});
			
		});
		function openView(id, teacherid)
		{
			if ($("#cmin").val()=="") {
				showTip("请填写课时时长");
				return false;
			} else if ($("#rmin").val()=="") {
				showTip("请填写课间休息时长");
				return false;
			} else if ($('input[name="ism"]:checked').val()==undefined && $('input[name="isa"]:checked').val()==undefined && $('input[name="isn"]:checked').val()==undefined) {
				showTip("请至少选择一个上课时间段");
				return false;
			} else {
				var url = "${ctx}/school/tSchoolTeacherConf/view?id="+id+"teacher.id="+teacherid;
				url += "&cmin="+$("#cmin").val();
				url += "&rmin="+$("#rmin").val();
				if ($('input[name="ism"]:checked').val()!=undefined) {
					url += "&ism="+$('input[name="ism"]:checked').val();
					url += "&mtimeshour="+$("#mtimeshour").val();
					url += "&mtimesmin="+$("#mtimesmin").val();
				}
				if ($('input[name="isa"]:checked').val()!=undefined) {
					url += "&isa="+$('input[name="isa"]:checked').val();
					url += "&atimeehour="+$("#atimeehour").val();
					url += "&atimeemin="+$("#atimeemin").val();
				}
				if ($('input[name="isn"]:checked').val()!=undefined) {
					url += "&isn="+$('input[name="isn"]:checked').val();
					url += "&ntimeehour="+$("#ntimeehour").val();
					url += "&ntimeemin="+$("#ntimeemin").val();
				}
				openDialogView("课程预览", url, "800px", "500px");
			}
		}
	</script>
</head>
<body>
		<form:form id="inputForm" modelAttribute="tSchoolTeacherConf" action="${ctx}/school/tSchoolTeacherConf/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="teacher.id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>教师：</label></td>
					<td class="width-35">
						<form:input path="teacher.name" htmlEscape="false" maxlength="11" class="form-control" disabled="true"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>收费标准：</label></td>
					<td class="width-35">
						<form:select path="price" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('fybz')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>每节课时长<br/>（分钟）：</label></td>
					<td class="width-35">
						<!--<form:input path="cmin" htmlEscape="false" maxlength="11" class="form-control required digits"/>-->
						<form:select path="cmin" class="form-control">
					    	<form:option value="30" label="30"/>
					    	<form:option value="40" label="40"/>
					        <form:option value="45" label="45"/>
					        <form:option value="50" label="50"/>
					        <form:option value="55" label="55"/>
					        <form:option value="60" label="60"/>
					        <form:option value="90" label="90"/>
					        <form:option value="100" label="100"/>
					        <form:option value="110" label="110"/>
					        <form:option value="120" label="120"/>
					    </form:select> 
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>课间休息<br/>（分钟）：</label></td>
					<td class="width-35">
						<!--<form:input path="rmin" htmlEscape="false" maxlength="11" class="form-control  digits"/>-->
						<form:select path="rmin" class="form-control">
							<form:option value="0" label="0"/>
					    	<form:option value="5" label="5"/>
					    	<form:option value="10" label="10"/>
					        <form:option value="15" label="15"/>
					        <form:option value="20" label="20"/>
					        <form:option value="25" label="25"/>
					        <form:option value="30" label="30"/>
					    </form:select> 
					</td>
				</tr> 
				<tr>
					<td class="width-15 active"><label class="pull-right">星期：</label></td>
					<td class="width-35" colspan="3">
						<form:checkbox path="mon" label="一" class="i-checks"></form:checkbox>&nbsp;&nbsp;
						<form:checkbox path="tue" label="二" class="i-checks"></form:checkbox>&nbsp;&nbsp;
						<form:checkbox path="wed" label="三" class="i-checks"></form:checkbox>&nbsp;&nbsp;
						<form:checkbox path="thu" label="四" class="i-checks"></form:checkbox>&nbsp;&nbsp;
						<form:checkbox path="fri" label="五" class="i-checks"></form:checkbox>&nbsp;&nbsp;
						<form:checkbox path="sat" label="六" class="i-checks"></form:checkbox>&nbsp;&nbsp;
						<form:checkbox path="sun" label="日" class="i-checks"></form:checkbox>
					</td>
				</tr> 
				<tr>
					<td class="width-15 active"><label class="pull-right">上午：</label></td>
					<td class="width-35" colspan="3">
						<form:radiobuttons path="ism" items="${fns:getDictList('can_teach')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks "/>
				 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<form:select path="mtimeshour">
					        <form:option value="07" label="07"/>
					        <form:option value="08" label="08"/>
					        <form:option value="09" label="09"/>
					        <form:option value="10" label="10"/>
					        <form:option value="11" label="11"/>
					        <form:option value="12" label="12"/>
					    </form:select>:
					    <form:select path="mtimesmin">
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
					    --
					    <form:select path="emtimeshour">
					        <form:option value="07" label="07"/>
					        <form:option value="08" label="08"/>
					        <form:option value="09" label="09"/>
					        <form:option value="10" label="10"/>
					        <form:option value="11" label="11"/>
					        <form:option value="12" label="12"/>
					    </form:select>:
					    <form:select path="emtimesmin">
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
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">下午：</label></td>
					<td class="width-35" colspan="3">
						<form:radiobuttons path="isa" items="${fns:getDictList('can_teach')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks "/>
					 	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<form:select path="atimeehour">
					        <form:option value="12" label="12"/>
					        <form:option value="13" label="13"/>
					        <form:option value="14" label="14"/>
					        <form:option value="15" label="15"/>
					        <form:option value="16" label="16"/>
					        <form:option value="17" label="17"/>
					        <form:option value="18" label="18"/>
					         <form:option value="19" label="19"/>
					    </form:select>:
					    <form:select path="atimeemin">
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
					    --
					    <form:select path="eatimeehour">
					        <form:option value="12" label="12"/>
					        <form:option value="13" label="13"/>
					        <form:option value="14" label="14"/>
					        <form:option value="15" label="15"/>
					        <form:option value="16" label="16"/>
					        <form:option value="17" label="17"/>
					        <form:option value="18" label="18"/>
					        <form:option value="19" label="19"/>
					    </form:select>:
					    <form:select path="eatimeemin">
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
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">晚上：</label></td>
					<td class="width-35" colspan="3">
						<form:radiobuttons path="isn" items="${fns:getDictList('can_teach')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks "/>
					 	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<form:select path="ntimeehour">
					        <form:option value="19" label="19"/>
					        <form:option value="20" label="20"/>
					        <form:option value="21" label="21"/>
					    </form:select>:
					    <form:select path="ntimeemin">
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
					    --
					    <form:select path="entimeehour">
					        <form:option value="19" label="19"/>
					        <form:option value="20" label="20"/>
					        <form:option value="21" label="21"/>
					    </form:select>:
					    <form:select path="entimeemin">
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
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">提示：</label></td>
					<td class="width-35" colspan="3">
						<font color="red">请仔细核对有效时间设置，避免频繁修改，影响学生报名，请仔细核对。</font>
					</td>
				</tr>
		 	</tbody>
		</table>
		<!-- -->
		<div class="mail-body text-center tooltip-demo">
        	<button type="button" class="btn btn-primary  btn-sm" onclick="openView(${tSchoolTeacherConf.id}, ${tSchoolTeacherConf.teacher.id})"> <i class="fa fa-up"></i> 预览结果</button>
   		</div>
   		 
	</form:form>
</body>
</html>