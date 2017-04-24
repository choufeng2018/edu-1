<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>学校管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
		
		function getSelectedItem(){
			var size = $("#contentTable tbody tr td input.i-checks:checked").size();
			if(size == 0 ){
				top.layer.alert('请至少选择一条数据!', {icon: 0, title:'警告'});
				return "-1";
			}

			if(size > 1 ){
				top.layer.alert('只能选择一条数据!', {icon: 0, title:'警告'});
				return "-1";
			}
			var id =  $("#contentTable tbody tr td input.i-checks:checkbox:checked").attr("id");
			return id;
		}
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
	<form:form id="searchForm" modelAttribute="TCommSchool" action="${ctx}/em/school/tCommSchool/selectlist" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
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
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tCommSchool">
		<c:if test="${!(tCommSchool.schoolCode eq '10000')}">
			<tr>
				<td>
				<c:if test="${!(tCommSchool.schoolCode eq '10000')}">
					<input type="checkbox" id="${tCommSchool.schoolCode}_item_${tCommSchool.schoolName}_item_${tCommSchool.tSchoolLevel.levelPrice}_item_${tCommSchool.endDateStr}" class="i-checks">
				</c:if>
				</td>
				<td id="ldesc">
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
			</tr>
		</c:if>
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