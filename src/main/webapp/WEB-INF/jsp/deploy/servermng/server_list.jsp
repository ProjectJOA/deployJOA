<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp"%>

<script type="text/javascript">

/* $(document).ready(function(){
	if('0' != '${patchCount}'){
		if(confirm('DeployJOA Update is Ready. Please update system.')){
			location.href = "/sysconfig/patch_his.do";
		}
	}
}); */

function fn_goPage(pageNo) {
	var frm = document.frm;
    
    $("input[name=page]").val(pageNo);
    
    frm.action="/servmng/targetser_list.do";
    frm.submit();
}    

function fn_goRegister(){
	location.href = "/servmng/targetser_editor.do";
}

function fn_goDetail(sno){
	location.href = "/servmng/targetser_editor.do?sNum="+sno;
}

function fn_goDeploy(sno){
	
	$.blockUI();
	
	var frm = document.frm;
	frm.action = "/deploymng/deploy_move.do";
	frm.sNum.value = sno;
	frm.submit();
}

function fn_goVersion(sno){

	$.blockUI();
	
	var frm = document.frm;
	frm.action = "/vermng/version_move.do";
	frm.sNum.value = sno;
	frm.submit();
}
///servmng/ci_init.do
function doInit(sno){
	if(!confirm("System Setting Activate?")){
		return;	
	}
	
	$.ajax({
		type:'post',
		dataType:'json',
		url:'/servmng/ci_init.do',
		beforeSend: function( xhr ) {
			$.blockUI();				
		},
		data:{"sNum":sno},
		success:function(data) {
			if(data == '001'){
				alert('Activate completed.');
				location.href = "/servmng/targetser_list.do";
			}else if(data == '801'){
				alert('Activate failed.(STEP 2)');
			}else{
				alert('Activate failed.(STEP 1)');
			}
		},
		error:function(xhr, stts, err) {
			alert('Activate failed.');
		},
		complete:function(xhr, stts) {
			$.unblockUI();
		}
	});		
}	
</script>
<style>
.patch_notice{
	padding: 20px;
    border: 1px solid #B91A4D;
    margin-bottom: 10px;
}
.patch_notice_cont{
	text-align:center;
	line-height: 70px;
	font-size: 25px;
	color: #333333;
}
.patch_notice_btn{
	text-align:center;
}
</style>
<div class="container">
    <div class="contents">
        <ul class="path">
            <li><a href="#">Home</a></li>
            <li><a href="#">Target Project Manage.</a></li>
        </ul>
        <c:if test="${patchCount ne '0'}">
	        <div class="patch_notice">
				<div class="patch_notice_cont">DeployJOA Update is Ready. Please update system.</div>
				<div class="patch_notice_btn"><a href="/sysconfig/patch_his.do" class="patch_start_btn">Update</a></div>
	        </div>
	                
        </c:if>        
        <div>&nbsp;</div>
        <div class="pagetitle"><h2>Target Project Manage.</h2></div>

        <div class="section">
        	<form name="frm" id="frm" action="/servmng/targetser_list.do" method="post">
        		<input type="hidden" id="page" name="page" value="" />
        		<input type="hidden" id="sNum" name="sNum" value="" />
        	</form>        
            <div class="aligner" data-bottom="xs">
                <div class="right">
                    <button onclick="javascript:fn_goRegister();">Project Register</button>
                </div>
            </div>
            <div class="table">
                <table>
                    <colgroup>
                        <col style="width: 8%">
                        <col style="width: 15%">
                        <!-- <col style="width: 10%"> -->
                        <!-- <col style="width: 10%"> -->
                        
                        <col style="width: 17%">
                        <col style="width: 20%">
                        <col style="width: 12%">
                        
                        <col style="width: 9%">
                        <col style="width: 9%">
                    </colgroup>
                    <thead>
                    <tr>
                        <th>No.</th>
                        <th>Project Name</th>
                        <!-- <th>서버아이피</th> -->
                        <!-- <th>승인기능사용</th> -->
                        <th>Final Deploy Ver.</th>
                        <th>Final State</th>
                        <th>Final Deploy Time</th>
                        <th>Build Manage.</th>
                        <th>Version View</th>
                    </tr>
                    </thead>
                    <tbody>
                    	<c:forEach items="${servLst }" var="list" varStatus="status">
		                    <tr>
		                        <td>${list.rnum}</td>
		                        <td><a href="javascript:fn_goDetail('${list.sNum}');">${list.sysNm}</a></td>
		                        <%-- <td><a href="javascript:fn_goDetail('${list.sNum}');">${list.sysIp}</a></td> --%>
		                        <%-- <td>
		                        	<c:choose>
		                        		<c:when test="${list.appUseYn eq 'Y' }">사용</c:when>
		                        		<c:otherwise>미사용</c:otherwise>
		                        	</c:choose>
		                        </td> --%>
		                        <td>${list.depoVers}</td>
		                        <td>${list.depoStateNm}</td>
		                        <td>
		                        	<c:choose>
		                        		<c:when test="${empty list.depoTime}"></c:when>
		                        		<c:when test="${fn:length(list.depoTime) < 8}"></c:when>
		                        		<c:otherwise>
		                        			<fmt:parseDate var="dateString" value="${list.depoTime}" pattern="yyyyMMddHHmm" />
		                        			<fmt:formatDate value="${dateString}" pattern="yyyy-MM-dd HH:mm" />
		                        		</c:otherwise>
		                        	</c:choose>		                        
		                        </td>
		                        <c:choose>
		                        	<c:when test="${list.initYn eq 'N' }">
				                        <td colspan="2"><a href="javascript:doInit('${list.sNum}')">Activate</a></td>
		                        	</c:when>
		                        	<c:otherwise>
				                        <td><a href="javascript:fn_goDeploy('${list.sNum}');">Build List</a></td>
				                        <td><a href="javascript:fn_goVersion('${list.sNum}');">Ver. View</a></td>		                        	
		                        	</c:otherwise>
		                        </c:choose>
		                    </tr>                    	
                    	</c:forEach>
                    </tbody>
                </table>
            </div>
           	<div class="pagination">
                <ui:pagination paginationInfo="${paginationInfo}" type="cmsDefault" jsFunction="fn_goPage"/>
            </div>
        </div>
    </div>
</div>

