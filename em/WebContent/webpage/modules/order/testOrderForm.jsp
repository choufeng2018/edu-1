<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>订单管理</title>
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
	<form:form id="inputForm" modelAttribute="testOrder" action="${ctx}/order/testOrder/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">code：</label></td>
					<td class="width-35">
						<form:input path="code" htmlEscape="false" maxlength="255" class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">price：</label></td>
					<td class="width-35">
						<form:input path="price" htmlEscape="false" class="form-control  number"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">custom：</label></td>
					<td class="width-35">
						<form:input path="custom" htmlEscape="false" maxlength="255" class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="form-control "/>
					</td>
				</tr>
		 	</tbody>
		</table>
		
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">订单详细：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane active">
			<a class="btn btn-white btn-sm" onclick="addRow('#testOrderDetailList', testOrderDetailRowIdx, testOrderDetailTpl);testOrderDetailRowIdx = testOrderDetailRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
			<table id="contentTable" class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>num</th>
						<th>price</th>
						<th>ordertime</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="testOrderDetailList">
				</tbody>
			</table>
			<script type="text/template" id="testOrderDetailTpl">//<!--
				<tr id="testOrderDetailList{{idx}}">
					<td class="hide">
						<input id="testOrderDetailList{{idx}}_id" name="testOrderDetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="testOrderDetailList{{idx}}_delFlag" name="testOrderDetailList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<input id="testOrderDetailList{{idx}}_num" name="testOrderDetailList[{{idx}}].num" type="text" value="{{row.num}}" maxlength="11" class="form-control  digits"/>
					</td>
					
					
					<td>
						<input id="testOrderDetailList{{idx}}_price" name="testOrderDetailList[{{idx}}].price" type="text" value="{{row.price}}" class="form-control  number"/>
					</td>
					
					
					<td>
						<input id="testOrderDetailList{{idx}}_ordertime" name="testOrderDetailList[{{idx}}].ordertime" type="text" readonly="readonly" maxlength="20" class="laydate-icon form-control layer-date  "
							value="{{row.ordertime}}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#testOrderDetailList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var testOrderDetailRowIdx = 0, testOrderDetailTpl = $("#testOrderDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(testOrder.testOrderDetailList)};
					for (var i=0; i<data.length; i++){
						addRow('#testOrderDetailList', testOrderDetailRowIdx, testOrderDetailTpl, data[i]);
						testOrderDetailRowIdx = testOrderDetailRowIdx + 1;
					}
				});
			</script>
			</div>
		</div>
		</div>
	</form:form>
</body>
</html>