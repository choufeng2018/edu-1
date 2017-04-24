<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ attribute name="id" type="java.lang.String" required="true" description="编号"%>
<%@ attribute name="name" type="java.lang.String" required="true" description="隐藏域名称（ID）"%>
<%@ attribute name="value" type="java.lang.String" required="true" description="隐藏域值（ID）"%>
<%@ attribute name="labelName" type="java.lang.String" required="true" description="输入框名称（Name）"%>
<%@ attribute name="labelValue" type="java.lang.String" required="true" description="输入框值（Name）"%>
<%@ attribute name="fieldLabels" type="java.lang.String" required="true" description="表格Th里显示的名字"%>
<%@ attribute name="fieldKeys" type="java.lang.String" required="true" description="表格Td里显示的值"%>
<%@ attribute name="searchLabel" type="java.lang.String" required="true" description="表格Td里显示的值"%>
<%@ attribute name="searchKey" type="java.lang.String" required="true" description="表格Td里显示的值"%>
<%@ attribute name="title" type="java.lang.String" required="true" description="选择框标题"%>
<%@ attribute name="url" type="java.lang.String" required="true" description="数据地址"%>
<%@ attribute name="cssClass" type="java.lang.String" required="false" description="css样式"%>
<%@ attribute name="cssStyle" type="java.lang.String" required="false" description="css样式"%>
<%@ attribute name="width" type="java.lang.String" required="false" description="宽"%>
<%@ attribute name="height" type="java.lang.String" required="false" description="高"%>
<%@ attribute name="disabled" type="java.lang.String" required="false" description="是否限制选择，如果限制，设置为disabled"%>
<%@ attribute name="callback" type="java.lang.String" required="false" description="高"%>
<%@ attribute name="needcheck" type="java.lang.Boolean" required="false" description="是否由前置校验"%>
<%@ attribute name="checkparam" type="java.lang.String" required="false" description="前置校验属性不能为空"%>
<%@ attribute name="checktip" type="java.lang.String" required="false" description="前置校验属性不能为空"%>
<script type="text/javascript">
function searchGrid${id}(){
	var url = "${url}";
	var ind = url.indexOf("?");
	var param = "${url}";
	if (${needcheck}) {
		if (validata)
		{
			var obj = validata("${checkparam}", "${checktip}");
			if (obj.tag == "0")
			{
				top.layer.msg(obj.str, {icon: 0});
				return true;
			} else { 
				if (obj.str!="" && obj.str!=null)
					param = param + (ind>0?"&":"?") + obj.str; 
			} 
		} else {
			top.layer.msg("系统错误，缺少前置校验函数！", {icon: 0});
			return true;
		}
	}  
	ind = param.indexOf("?");
	top.layer.open({
	    type: 2,  
	    area: ["${width}"==""?'1000px':"${width}", "${height}"==""?'500px':"${height}"],
	    title:"${title}",
	    name:'friend',
	    //content: "${url}&fieldLabels=${fieldLabels}&fieldKeys=${fieldKeys}&url=${url}&searchLabel=${searchLabel}&searchKey=${searchKey}" ,
	    content: ind>0?(param+"&fieldLabels=${fieldLabels}&fieldKeys=${fieldKeys}"):(param+"${url}?fieldLabels=${fieldLabels}&fieldKeys=${fieldKeys}") ,
	    btn: ['确定', '关闭'],
	    yes: function(index, layero){
	    	 var iframeWin = layero.find('iframe')[0].contentWindow; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
	    	 var item = iframeWin.getSelectedItem();

	    	 if(item == "-1"){
		    	 return;
	    	 }
	    	 $("#${id}Id").val(item.split('_item_')[0]);
	    	 $("#${id}Name").val(item.split('_item_')[1]);
	    	 
	    	 if ("${callback}"=="1" && (typeof setValue === 'function') && item.split('_item_').length>2)
	    	 {
	    	 	setValue(item.split('_item_')[2], item.split('_item_')[3]);
	    	 }
	    	 
			 top.layer.close(index);//关闭对话框。
		},
		cancel: function(index){ }
	}); 
};

</script>

	<input id="${id}Id" name="${name}"  type="hidden" value="${value}"/>
	<div class="input-group">
		<input id="${id}Name"  name="${labelName }" ${allowInput?'':'readonly="readonly"'} onclick="searchGrid${id}()" type="text" value="${labelValue}" data-msg-required="${dataMsgRequired}"
		class="${cssClass}" style="${cssStyle}"/>
       		 <span class="input-group-btn">
	       		 <button type="button" onclick="searchGrid${id}()" id="${id}Button" class="btn <c:if test="${fn:contains(cssClass, 'input-sm')}"> btn-sm </c:if><c:if test="${fn:contains(cssClass, 'input-lg')}"> btn-lg </c:if>  btn-primary ${disabled} ${hideBtn ? 'hide' : ''}"><i class="fa fa-search"></i>
	             </button> 
       		 </span>
       		
    </div>
	 <label id="${id}Name-error" class="error" for="${id}Name" style="display:none"></label>
