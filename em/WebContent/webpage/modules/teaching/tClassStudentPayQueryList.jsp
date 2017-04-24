<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>缴费管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox"> 
    
    <div class="ibox-content">
	<sys:message content="${message}"/>
	  
	<!-- 工具栏 
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left"> 
			<shiro:hasPermission name="teaching:tClassStudentPay:export">
	       		<table:exportExcel url="${ctx}/teaching/tClassStudentPay/export?cs.id=${tClassStudentPay.cs.id}"></table:exportExcel> 
	       	</shiro:hasPermission>
	    </div>
	</div>
	</div>-->
	
	<!-- 表格 -->
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr> 
				<th  class="sort-column paytype">付款方式</th>
				<th  class="sort-column amount">缴费金额</th>
				<th  class="sort-column zks">缴费课时数</th>
				<th  class="sort-column createBy.id">收费人</th>
				<th  class="sort-column createDate">收费日期</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tClassStudentPay">
			<tr> 
				<td>
					${fns:getDictLabel(tClassStudentPay.paytype, 'pay_type', '')}
				</td>
				<td>
					${tClassStudentPay.amount}
				</td>
				<td>
					${tClassStudentPay.zks}
				</td>
				<td>
					${tClassStudentPay.cc.name}
				</td>
				<td>
					<fmt:formatDate value="${tClassStudentPay.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	 
	<br/>
	<br/>
	</div>
	</div>
</div>
</body>
</html>