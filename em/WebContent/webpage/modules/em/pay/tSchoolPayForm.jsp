<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>支付管理</title>
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
			            elem: '#buyDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
			            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
			        });
					laydate({
			            elem: '#begDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
			            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
			        });
					laydate({
			            elem: '#endDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
			            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
			        });
		});
	</script>
</head>
<body>
		<form:form id="inputForm" modelAttribute="tSchoolPay" action="${ctx}/em/pay/tSchoolPay/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>会员学校：</label></td>
					<td class="width-35">
						<sys:schoolSelect url="${ctx}/em/school/tCommSchool/selectlist" id="category" name="school.schoolCode"  value="${tSchoolPay.school.schoolCode}"  title="选择会员" labelName="school.schoolName" 
							labelValue="${tSchoolPay.school.schoolName}" cssClass="form-control required" fieldLabels="" fieldKeys="" searchLabel="" searchKey="" width="900px" height="600px"></sys:schoolSelect>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>购买日期：</label></td>
					<td class="width-35">
						<input id="buyDate" name="buyDate" type="text" maxlength="20" class="laydate-icon form-control layer-date required"
							value="<fmt:formatDate value="${tSchoolPay.buyDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>付费金额：</label></td>
					<td class="width-35">
						<form:input path="payCount" htmlEscape="false" class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>付费方式：</label></td>
					<td class="width-35">
						<form:select path="payType" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('paytype')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>起始日期：</label></td>
					<td class="width-35">
						<input id="begDate" name="begDate" type="text" maxlength="20" class="laydate-icon form-control layer-date required"
							value="<fmt:formatDate value="${tSchoolPay.begDate}" pattern="yyyy-MM-dd"/>"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>有效期：</label></td>
					<td class="width-35">
						<input id="endDate" name="endDate" type="text" maxlength="20" class="laydate-icon form-control layer-date required"
							value="<fmt:formatDate value="${tSchoolPay.endDate}" pattern="yyyy-MM-dd"/>"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">续费周期说明：</label></td>
					<td class="width-35" colspan="3">
						<form:textarea path="payPeriod" htmlEscape="false" rows="2" maxlength="64" class="form-control "/>
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