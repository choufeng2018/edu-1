<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>支付管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>支付列表 </h5>
		<div class="ibox-tools">
			<a class="collapse-link">
				<i class="fa fa-chevron-up"></i>
			</a>
			<a class="close-link">
				<i class="fa fa-times"></i>
			</a>
		</div>
	</div>
    
    <div class="ibox-content">
	<sys:message content="${message}"/>
	
	<!--查询条件-->
	<div class="row">
	<div class="col-sm-12">
	<form:form id="searchForm" modelAttribute="TSchoolPay" action="${ctx}/em/pay/tSchoolPay/list" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div class="form-group">
			<span>所属学校：</span>
			<!--<form:input path="school.schoolCode" htmlEscape="false" maxlength="11"  class=" form-control input-sm"/>-->
			<sys:schoolSelect url="${ctx}/em/school/tCommSchool/selectlist" id="category" name="school.schoolCode"  value="${TSchoolPay.school.schoolCode}"  title="选择会员" labelName="school.schoolName" 
							labelValue="${TSchoolPay.school.schoolName}" cssClass="form-control required" fieldLabels="" fieldKeys="" searchLabel="" searchKey="" width="900px" height="600px"></sys:schoolSelect>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="em:pay:tSchoolPay:add">
				<table:addRow url="${ctx}/em/pay/tSchoolPay/form" title="支付"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<!--  
			<shiro:hasPermission name="em:pay:tSchoolPay:edit">
			    <table:editRow url="${ctx}/em/pay/tSchoolPay/form" title="支付" id="contentTable"></table:editRow>
			</shiro:hasPermission>
			<shiro:hasPermission name="em:pay:tSchoolPay:del">
				<table:delRow url="${ctx}/em/pay/tSchoolPay/deleteAll" id="contentTable"></table:delRow>
			</shiro:hasPermission>
			
			<shiro:hasPermission name="em:pay:tSchoolPay:import">
				<table:importExcel url="${ctx}/em/pay/tSchoolPay/import"></table:importExcel>
			</shiro:hasPermission>-->
			<shiro:hasPermission name="em:pay:tSchoolPay:export">
	       		<table:exportExcel url="${ctx}/em/pay/tSchoolPay/export"></table:exportExcel><!-- 导出按钮 -->
	       	</shiro:hasPermission>
	       <button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
		
			</div>
		<div class="pull-right">
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
		</div>
	</div>
	</div>
	
	<!-- 表格 -->
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th> <input type="checkbox" class="i-checks"></th>
				<th  class="sort-column schoolCode">会员学校</th>
				<th  class="sort-column buyDate">购买日期</th>
				<th  class="sort-column payCount">付费金额</th>
				<th  class="sort-column payType">付费方式</th>
				<th  class="sort-column begDate">起始日期</th>
				<th  class="sort-column endDate">有效期</th>
				<th  class="sort-column endDate">收费人</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tSchoolPay">
			<tr>
				<td> <input type="checkbox" id="${tSchoolPay.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看支付', '${ctx}/em/pay/tSchoolPay/form?id=${tSchoolPay.id}','800px', '500px')">
					${tSchoolPay.school.schoolName}
				</a></td>
				<td>
					<fmt:formatDate value="${tSchoolPay.buyDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${tSchoolPay.payCount}
				</td>
				<td>
					${fns:getDictLabel(tSchoolPay.payType, 'paytype', '')}
				</td>
				<td>
					<fmt:formatDate value="${tSchoolPay.begDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					<fmt:formatDate value="${tSchoolPay.endDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					${tSchoolPay.createBy.name}
				</td>
				<td>
					<shiro:hasPermission name="em:pay:tSchoolPay:view">
						<a href="#" onclick="openDialogView('查看支付', '${ctx}/em/pay/tSchoolPay/form?id=${tSchoolPay.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="em:pay:tSchoolPay:edit">
    					<a href="#" onclick="openDialog('修改支付', '${ctx}/em/pay/tSchoolPay/form?id=${tSchoolPay.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="em:pay:tSchoolPay:del">
						<a href="${ctx}/em/pay/tSchoolPay/delete?id=${tSchoolPay.id}" onclick="return confirmx('确认要删除该支付吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
					</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	
		<!-- 分页代码 -->
	<table:page page="${page}"></table:page>
	<br/>
	<br/>
	</div>
	</div>
</div>
</body>
</html>