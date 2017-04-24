<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>课程表管理</title>
	<meta name="decorator" content="default"/>
	<script src="${ctxStatic}/jquery/jquery-2.1.1.min.js"></script>
    <script src="${ctxStatic}/jquery-ui/jquery-ui.min.js"></script>
    <script src="${ctxStatic}/bootstrap/3.3.4/js/bootstrap.min.js"></script>
    <script src="${ctxStatic}/metisMenu/jquery.metisMenu.js"></script>
    <script src="${ctxStatic}/slimscroll/jquery.slimscroll.min.js"></script>
    <link href="${ctxStatic}/bootstrap/3.3.4/css_default/bootstrap.min.css" rel="stylesheet">
    <link href="${ctxStatic}/awesome/4.4/fonts/font-awesome.css" rel="stylesheet">

    <!-- Toastr style -->
    <link href="${ctxStatic}/css/plugins/toastr/toastr.min.css" rel="stylesheet">
    <link href="${ctxStatic}/common/css/animate.css" rel="stylesheet">
    <link href="${ctxStatic}/css/style.css" rel="stylesheet">

    <!-- Custom and plugin javascript -->
    <script src="js/inspinia.js"></script>
    <script src="js/plugins/pace/pace.min.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#todo, #inprogress, #completed").sortable({
                connectWith: ".connectList",
                update: function( event, ui ) {
                    var todo = $( "#todo" ).sortable( "toArray" );
                    var inprogress = $( "#inprogress" ).sortable( "toArray" );
                    var completed = $( "#completed" ).sortable( "toArray" );
                    $('.output').html("ToDo: " + window.JSON.stringify(todo) + "<br/>" + "In Progress: " + window.JSON.stringify(inprogress) + "<br/>" + "Completed: " + window.JSON.stringify(completed));
                }
            }).disableSelection();
		}); 
	</script>
</head>
<body class="gray-bg">
<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>课程表 </h5>
		<div class="ibox-tools">
			<a class="collapse-link">
				<i class="fa fa-chevron-up"></i>
			</a> 
		</div>
	</div>
    
    <div class="ibox-content">
	<sys:message content="${message}"/>
	   
	<!-- 分页代码 -->
	<div class="row">
                <div class="col-lg-4">
                    <div class="ibox">
                        <div class="ibox-content">
                            <h3>昨天（${yesterday}）</h3>
                            <p class="small"><i class="fa fa-hand-o-up"></i> 课程表</p>
                            <ul class="sortable-list connectList agile-list" id="todo">
                            <c:forEach items="${yesterdayList}" var="tCourseTimetable">
                                <li class="${tCourseTimetable.status==1?'warning-element':'success-element'}" id="task1">
                                    ${tCourseTimetable.courseclass.classDesc}
                                    <div class="agile-detail">
                                        <i class="fa fa-clock-o"></i> 
                                        <fmt:formatDate value="${tCourseTimetable.courseDate}" pattern="yyyy-MM-dd"/>
                                        ${tCourseTimetable.teactime}
                                    </div>
                                    <div class="agile-detail">
                                        ${tCourseTimetable.teacher.name}&nbsp;（${tCourseTimetable.teacher.tel}）
                                    </div>
                                    <div class="agile-detail">
                                        ${tCourseTimetable.room.roomDesc}&nbsp;&nbsp;
                                        ${tCourseTimetable.courseclass.stuNum==null?0:tCourseTimetable.courseclass.stuNum} 人
                                    </div>
                                    <div class="agile-detail">
                                    	<c:if test="${tCourseTimetable.status == '1'}">
                                        <a href="javascript:openDialog('考勤', '${ctx}/subject/tCourseTimetable/checkform?id=${tCourseTimetable.id}','900px', '600px')" class="pull-right btn btn-xs btn-primary">考勤</a>
                                        </c:if>
                                        <c:if test="${tCourseTimetable.status == '2'}">
                                        <a href="javascript:openDialogView('查看考勤', '${ctx}/subject/tCourseTimetable/checkform?id=${tCourseTimetable.id}','900px', '600px')" class="pull-right btn btn-xs btn-primary">查看考勤</a>
                                        </c:if>
                                        ${fns:getDictLabel(tCourseTimetable.status, 'timetable_status', '')}
                                    </div>
                                </li> 
                            </c:forEach>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="col-lg-4">
                    <div class="ibox">
                        <div class="ibox-content">
                            <h3>今天（${today}）</h3>
                            <p class="small"><i class="fa fa-hand-o-up"></i> 课程表</p>
                            <ul class="sortable-list connectList agile-list" id="inprogress">
                                <c:forEach items="${todayList}" var="tCourseTimetable">
                                <li class="${tCourseTimetable.status==1?'warning-element':'success-element'}" id="task1">
                                    ${tCourseTimetable.courseclass.classDesc}
                                    <div class="agile-detail">
                                        <i class="fa fa-clock-o"></i> 
                                        <fmt:formatDate value="${tCourseTimetable.courseDate}" pattern="yyyy-MM-dd"/>
                                        ${tCourseTimetable.teactime}
                                    </div>
                                    <div class="agile-detail">
                                        ${tCourseTimetable.teacher.name}&nbsp;（${tCourseTimetable.teacher.tel}）
                                    </div>
                                    <div class="agile-detail">
                                        ${tCourseTimetable.room.roomDesc}&nbsp;&nbsp;
                                        ${tCourseTimetable.courseclass.stuNum==null?0:tCourseTimetable.courseclass.stuNum} 人
                                    </div>
                                    <div class="agile-detail">
                                        <c:if test="${tCourseTimetable.status == '1'}">
                                        	<a href="javascript:openDialog('考勤', '${ctx}/subject/tCourseTimetable/checkform?id=${tCourseTimetable.id}&source=new','900px', '600px')" class="pull-right btn btn-xs btn-primary">考勤</a>
                                        </c:if>
                                        <c:if test="${tCourseTimetable.status == '2'}">
                                        	<a href="javascript:openDialogView('查看考勤', '${ctx}/subject/tCourseTimetable/checkform?id=${tCourseTimetable.id}','900px', '600px')" class="pull-right btn btn-xs btn-primary">查看考勤</a>
                                        </c:if>
                                        ${fns:getDictLabel(tCourseTimetable.status, 'timetable_status', '')}
                                        
                                    </div>
                                </li> 
                            	</c:forEach>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="col-lg-4">
                    <div class="ibox">
                        <div class="ibox-content">
                            <h3>明天（${tomorrow}）</h3>
                            <p class="small"><i class="fa fa-hand-o-up"></i> 课程表</p>
                            <ul class="sortable-list connectList agile-list" id="completed">
                                <c:forEach items="${tomorrowList}" var="tCourseTimetable">
                                <li class="${tCourseTimetable.status==1?'warning-element':'success-element'} " id="task1">
                                    ${tCourseTimetable.courseclass.classDesc}
                                    <div class="agile-detail">
                                        <i class="fa fa-clock-o"></i> 
                                        <fmt:formatDate value="${tCourseTimetable.courseDate}" pattern="yyyy-MM-dd"/>
                                        ${tCourseTimetable.teactime}
                                    </div>
                                    <div class="agile-detail">
                                        ${tCourseTimetable.teacher.name}&nbsp;（${tCourseTimetable.teacher.tel}）
                                    </div>
                                    <div class="agile-detail">
                                        ${tCourseTimetable.room.roomDesc}&nbsp;&nbsp;
                                        ${tCourseTimetable.courseclass.stuNum==null?0:tCourseTimetable.courseclass.stuNum} 人
                                    </div>
                                    <div class="agile-detail">
                                        ${fns:getDictLabel(tCourseTimetable.status, 'timetable_status', '')}
                                    </div>
                                </li> 
                            	</c:forEach>
                            </ul>
                        </div>
                    </div>
                </div>

            </div>
	<br/>
	</div>
	</div>
</div>
</body>
</html>