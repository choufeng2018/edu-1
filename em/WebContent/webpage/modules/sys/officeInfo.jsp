<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>机构管理</title>
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
			if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端，就使用自适应大小弹窗
				width='auto';
				height='auto';
			}else{//如果是PC端，根据用户设置的width和height显示。
				width='700px';
				height='500px';
			}
			  
			$("#userImageBtn").click(function(){
				top.layer.open({
				    type: 2,  
				    area: [width, height],
				    title:"上传证件",
				    content: "${ctx}/sys/user/imageEdit" ,
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
				    content: "${ctx}/sys/officeForm" ,
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
									编辑<i class="fa fa-wrench"></i>
								</a>
								<ul class="dropdown-menu dropdown-user">
									<li><a id="userImageBtn" data-toggle="modal" data-target="#register">更换执照</a>
									</li>
									<li><a id="userInfoBtn" data-toggle="modal" data-target="#register">编辑资料</a>
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
		      	 <td class="width-15 active"><label class="pull-right">机构名称:</label></td>
		         <td class="width-35">${office.name}</td>
		         <td class="width-15 active"><label class="pull-right">营业执照:</label></td>
		         <td class="width-35" rowspan=2><img alt="image" class="img-responsive" src="${office.photo}"/></td>
		      </tr>
		      <tr>
		         <td class="width-15" active><label class="pull-right">归属区域:</label></td>
		         <td class="width-35">${office.area.name}</td>
		      </tr>
		      <tr>
		         <td class="width-15" active><label class="pull-right">主负责人:</label></td>
		         <td class="width-35">${office.primaryPerson.name}</td>
		         <td class="width-15 active"><label class="pull-right">副负责人:</label></td>
		         <td class="width-35">${office.deputyPerson.name}</td>
			  </tr>
		      <tr>
		         <td class="width-15" active><label class="pull-right">联系地址:</label></td>
		         <td class="width-35">${office.address}</td>
		         <td class="width-15" active><label class="pull-right">邮政编码:</label></td>
		         <td class="width-35">${office.zipCode}</td>
		      </tr>
		      <tr>
		         <td class="width-15" active><label class="pull-right">电话:</label></td>
		         <td class="width-35">${office.phone}</td>
		         <td  class="width-15" active><label class="pull-right">传真:</label></td>
		         <td class="width-35">${office.fax}</td>
		      </tr>
		      <tr>
		         <td class="width-15 active"><label class="pull-right">邮箱:</label></td>
		         <td class="width-35">${office.email}</td>
		         <td  class="width-15" active><label class="pull-right">备注:</label></td>
		         <td class="width-35">${office.remarks}</td>
		      </tr>
	      </tbody>
	      </table>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
	<form:form id="inputForm" modelAttribute="office" action="" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="schoolCode"/>
		<form:hidden path="code"/>
		<form:hidden path="grade" value="1"/>
		<form:hidden path="useable" value="1"/>
		<form:hidden path="parent.id"/>
		<sys:message content="${message}"/>
		
	</form:form>
</body>
</html>