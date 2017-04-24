<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>学校管理</title>
	<meta name="decorator" content="default"/>
	<script src="${ctxStatic}/common/tool.js" type="text/javascript"></script>
	<script type="text/javascript">
		var validateForm;
		$(document).ready(function() {
			if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端，就使用自适应大小弹窗
				width='auto';
				height='auto';
			}else{//如果是PC端，根据用户设置的width和height显示。
				width='800px';
				height='600px';
			}
			  
			$("#userImageBtn").click(function(){
				top.layer.open({
				    type: 2,  
				    area: [width, height],
				    title:"上传证件",
				    content: "${ctx}/em/school/tCommSchool/imageEdit" ,
				  //  btn: ['确定', '关闭'],
				    yes: function(index, layero){
				    	 var body = top.layer.getChildFrame('body', index);
				         var inputForm = body.find('#inputForm');
				         var top_iframe = top.getActiveTab().attr("name");//获取当前active的tab的iframe 
				         inputForm.attr("target",top_iframe);//表单提交成功后，从服务器返回的url在当前tab中展示
				         inputForm.validate();
				         if(inputForm.valid()){
				        	  loading("正在提交，请稍等...");
				        	  inputForm.submit();
				          }else{
					          return;
				          }
				        
						 top.layer.close(index);//关闭对话框。
						
					  },
					  cancel: function(index){ 
		    	      }
				}); 
			});
			
			$("#userInfoBtn").click(function(){
				top.layer.open({
				    type: 2,  
				    area: [width, height],
				    title:"机构信息编辑",
				    content: "${ctx}/em/school/tCommSchool/formself?id=${tCommSchool.id}" ,
				    btn: ['确定', '关闭'],
				    yes: function(index, layero){
				    	 var body = top.layer.getChildFrame('body', index);
				         var inputForm = body.find('#inputForm');
				         var top_iframe = top.getActiveTab().attr("name");//获取当前active的tab的iframe 
				         inputForm.attr("target",top_iframe);//表单提交成功后，从服务器返回的url在当前tab中展示
				         inputForm.validate();
				         if(inputForm.valid()){
				        	  loading("正在提交，请稍等...");
				        	  inputForm.submit();
				          }else{
					          return;
				          }
				        
						 top.layer.close(index);//关闭对话框。
						
					  },
					  cancel: function(index){ 
		    	      }
				}); 
			});
			
			$("#refreshBtn").click(function(){
				window.location.href = "${ctx}/em/school/tCommSchool/info";
			});
		});
	</script>
</head>
<body>
<div class="col-sm-9">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h5>基础资料</h5>
							<div class="ibox-tools">
								<a class="dropdown-toggle" data-toggle="dropdown" href="#">
									操作<i class="fa fa-wrench"></i>
								</a>
								<ul class="dropdown-menu dropdown-user">
									<c:if test="${fns:getUser().isSysAdmin eq '1'}">
									<li><a id="userImageBtn" data-toggle="modal" data-target="#register">更换执照</a>
									</li>
									<li><a id="userInfoBtn" data-toggle="modal" data-target="#register">编辑资料</a>
									</li>
									</c:if>
									<li><a id="refreshBtn" data-toggle="modal" data-target="#register">刷新</a>
									</li>
								</ul>
								
							</div>
						</div>
						<div class="ibox-content">
							<div class="row">
								<div class="col-sm-8">
									<div class="table-responsive"> 
										<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
		  	<tr>
					<td class="width-15 active"><label class="pull-right">学校名称：</label></td>
					<td class="width-35">${tCommSchool.schoolName}</td>
					<td class="width-15 active"><label class="pull-right">营业执照:</label></td>
		         	<td class="width-35"><img alt="image" class="img-responsive" src="${tCommSchool.zjphoto}"/></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">联系人：</label></td>
					<td class="width-35">${tCommSchool.contact}</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>联系电话<br/>(手机)：</label></td>
					<td class="width-35">${tCommSchool.tel}</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">所在省：</label></td>
					<td class="width-35">${tCommSchool.province.name}</td>
					<td class="width-15 active"><label class="pull-right">所在地市：</label></td>
					<td class="width-35">${tCommSchool.city.name}</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">所属区县：</label></td>
					<td class="width-35">${tCommSchool.region.name}</td>
					<td class="width-15 active"><label class="pull-right">邮箱：</label></td>
					<td class="width-35">${tCommSchool.email}</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">会员级别：</label></td>
					<td class="width-35">${tCommSchool.tSchoolLevel.levelDesc}</td>
					<td class="width-15 active"><label class="pull-right">学校主页：</label></td>
					<td class="width-35">${tCommSchool.homepage}</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">学校地址：</label></td>
					<td class="width-35" colspan="3">${tCommSchool.address}</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">学校简介：</label></td>
					<td class="width-35" colspan="3">${tCommSchool.summary}</td> 
		  		</tr>
	      </tbody>
	      </table>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				
		<form:form id="inputForm" modelAttribute="tCommSchool" action="${ctx}/em/school/tCommSchool/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="schoolCode"/>
		<sys:message content="${message}"/>	
		 
	</form:form>
</body>
</html>