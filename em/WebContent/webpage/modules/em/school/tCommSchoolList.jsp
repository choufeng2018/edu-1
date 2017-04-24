<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>学校管理</title>
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
		<h5>学校列表 </h5>
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
	<form:form id="searchForm" modelAttribute="TCommSchool" action="${ctx}/em/school/tCommSchool/list" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<!--<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/> 支持排序 -->
		<div class="form-group">
			<span>省：</span>
				<sys:treeselect id="province" name="province.id" value="${TCommSchool.province.id}" labelName="province.name" 
							labelValue="${TCommSchool.province.name}" title="省" url="/sys/area/treeData?type=2" cssClass="form-control required" allowClear="true"/>
			<span>市：</span>
				<sys:areatreeselect id="city" name="city.id" value="${TCommSchool.city.id}" labelName="city.name" 
							labelValue="${TCommSchool.city.name}" preObj="province" title="市" url="/sys/area/treeDataByParent?parent=" cssClass="form-control required" allowClear="true"/>
			<span>区县：</span>
				<sys:areatreeselect id="region" name="region.id" value="${TCommSchool.region.id}" labelName="region.name" 
							labelValue="${TCommSchool.region.name}" preObj="city" title="区县" url="/sys/area/treeDataByParent?parent=" cssClass="form-control required" allowClear="true"/>
			<span>学校名称：</span>
				<form:input path="schoolName" htmlEscape="false" maxlength="100"  class=" form-control input-sm"/>
			<span>状态：</span>	
				<form:select id="schoolStatus" path="schoolStatus" class="form-control m-b">
					<form:option value="" label=""/>
					<form:option value="0" label="禁用"/>
					<form:option value="1" label="正常"/>
				</form:select>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="em:school:tCommSchool:add">
				<table:addRow url="${ctx}/em/school/tCommSchool/form" title="学校"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			
			<shiro:hasPermission name="em:school:tCommSchool:edit">
			    <table:editRow url="${ctx}/em/school/tCommSchool/form" title="学校" id="contentTable"></table:editRow>
			</shiro:hasPermission>
			<shiro:hasPermission name="em:school:tCommSchool:del">
				<table:delRow url="${ctx}/em/school/tCommSchool/deleteAll" id="contentTable"></table:delRow>
			</shiro:hasPermission>
			<!-- <shiro:hasPermission name="em:school:tCommSchool:import">
				<table:importExcel url="${ctx}/em/school/tCommSchool/import"></table:importExcel>导入按钮
			</shiro:hasPermission>
			<shiro:hasPermission name="em:school:tCommSchool:export">
	       		<table:exportExcel url="${ctx}/em/school/tCommSchool/export"></table:exportExcel> 
	       	</shiro:hasPermission>-->
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
				<th  class="sort-column schoolCode">学校编码</th>
				<th  class="sort-column schoolName">学校名称</th>
				<th  class="sort-column province">所在省</th>
				<th  class="sort-column city">所在地市</th>
				<th  class="sort-column region">所属区县</th> 
				<th  class="sort-column contact">联系人</th>
				<th  class="sort-column tel">联系电话</th>
				<th  class="sort-column tSchoolLevel">会员等级</th>
				<th  class="sort-column schoolType">类型</th>
				<th  class="sort-column schoolStatus">状态</th>
				<th  class="sort-column updateDate">创建日期</th> 
				<th  class="sort-column endDate">有效期</th> 
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tCommSchool">
			<tr>
				<td>
				<c:if test="${!(tCommSchool.schoolCode eq '10000')}">
					<input type="checkbox" id="${tCommSchool.id}" class="i-checks">
				</c:if>
				</td>
				<td><a  href="#" onclick="openDialogView('查看学校', '${ctx}/em/school/tCommSchool/form?id=${tCommSchool.id}','800px', '500px')">
					${tCommSchool.schoolCode}
				</a></td>
				<td>
					${tCommSchool.schoolName}
				</td>
				<td>
					${tCommSchool.province.name}
				</td>
				<td>
					${tCommSchool.city.name}
				</td>
				<td>
					${tCommSchool.region.name}
				</td> 
				<td>
					${tCommSchool.contact}
				</td>
				<td>
					${tCommSchool.tel}
				</td>
				<td>
					${tCommSchool.tSchoolLevel.levelDesc}
				</td>
				<td>
					${fns:getDictLabel(tCommSchool.schoolType, 'school_type', '未知')}
				</td>
				<td>
					${fns:getDictLabel(tCommSchool.schoolStatus, 'school_status', '未知')}
				</td>
				<td>
					<fmt:formatDate value="${tCommSchool.updateDate}" pattern="yyyy-MM-dd"/>
				</td> 
				<td>
					<fmt:formatDate value="${tCommSchool.endDate}" pattern="yyyy-MM-dd"/>
				</td> 
				<td>
					<!-- 
					<shiro:hasPermission name="em:school:tCommSchool:edit">
					<c:if test="${!(tCommSchool.schoolCode eq '10000')}">
    					<a href="#" onclick="openDialog('修改学校', '${ctx}/em/school/tCommSchool/form?id=${tCommSchool.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</c:if>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="em:school:tCommSchool:del">
    				<c:if test="${!(tCommSchool.schoolCode eq '10000')}">
						<a href="${ctx}/em/school/tCommSchool/delete?id=${tCommSchool.id}" onclick="return confirmx('确认要删除该学校吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
					</c:if>
					</shiro:hasPermission>
					-->
					<shiro:hasPermission name="em:school:tCommSchool:enableanddisable">
					<c:if test="${!(tCommSchool.schoolCode eq '10000')}">
						<a href="${ctx}/em/school/tCommSchool/enableAndDisable?id=${tCommSchool.id}" onclick="return confirmx('确认要${tCommSchool.schoolStatus==0?'启用':'禁用'}该学校吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-plus"></i> ${tCommSchool.schoolStatus==0?'启用':'禁用'}</a>
					</c:if>
					</shiro:hasPermission> 
					<shiro:hasPermission name="em:school:tCommSchool:assign"> 
					<c:if test="${!(tCommSchool.schoolCode eq '10000')}">
						<a href="#" onclick="openDialog('缴费', '${ctx}/resource/sysResource/listSchoolResource?schoolcode=${tCommSchool.schoolCode}','800px', '500px')" class="btn btn-primary btn-xs" ><i class="fa fa-edit"></i>缴费</a> 
					</c:if>
					</shiro:hasPermission>
					<shiro:hasPermission name="em:school:tCommSchool:assign"> 
					<c:if test="${!(tCommSchool.schoolCode eq '10000')}">
						<a href="#" onclick="openDialog('会员变更', '${ctx}/resource/sysResource/listSchoolResource?schoolcode=${tCommSchool.schoolCode}','800px', '500px')" class="btn btn-primary btn-xs" ><i class="glyphicon glyphicon-plus"></i>会员变更</a> 
					</c:if>
					</shiro:hasPermission>
					<!--  
					<shiro:hasPermission name="em:school:tCommSchool:assign"> 
					<c:if test="${!(tCommSchool.schoolCode eq '10000')}">
						<a href="#" onclick="openDialog('资源列表', '${ctx}/resource/sysResource/listSchoolResource?schoolcode=${tCommSchool.schoolCode}','800px', '500px')" class="btn btn-primary btn-xs" ><i class="fa fa-edit"></i>资源查看</a> 
					</c:if>
					</shiro:hasPermission>
					-->
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