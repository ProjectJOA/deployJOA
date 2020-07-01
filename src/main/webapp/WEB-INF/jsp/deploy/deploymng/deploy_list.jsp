<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp"%>

<script type="text/javascript">
	var logviewer;
	var todayHHmiss = "";
	$(document).ready(function () {
		logviewer = new Popup('log-pop');
		var d = new Date();
		todayHHmiss = d.getFullYear()+""+(d.getMonth() + 1)+""+d.getDate()+""+d.getHours()+""+d.getMinutes()+""+d.getSeconds();
	});

	function fn_goPage(pageNo){
		var frm = document.frm;
	    
	    $("input[name=page]").val(pageNo);
	    
	    frm.action="./deploy_list.do";
	    frm.submit();		
	}
	
	function popCancel(){
		//$(".btn-close").trigger("click");
		logviewer.closePopup();
		disconnect();
		location.href = "/deploymng/deploy_list.do";
	}
	
	function openLogView(){
		logviewer.openPopup();
	}
	
	function register(){
		location.href = "/deploymng/deploy_register.do";
	}
	
	function goDetailView(bNum){
		location.href = "./deploy_view.do?bNum="+bNum;			
	}
	
	var wSocket = null;
	
	function doDeploy(sno,bno){
	//function log(){
		$.ajax({
			type:'post',
			dataType:'json',
			url:'/deploymng/doDeploy.do',
			beforeSend: function( xhr ) {
				$.blockUI();
				log(bno);
			},
			data:{"sNum":sno,"bNum":bno},
			success:function(data) {
				if(data == '001'){
					
					$("#loading").attr("style","display:none");
					alert('build Complete!!');
					//disconnect();
					//location.href = "/deploymng/deploy_list.do";
					
				}else if(data == '900'){
					alert('build failed. check log view then rebuild.');
				}else{
					alert('build failed. check log view then rebuild.');
				}
			},
			error:function(xhr, stts, err) {
				alert('build failed. check log view then rebuild.');
				//disconnect();
			},
			complete:function(xhr, stts) {
				$.unblockUI();
				//disconnect();
			}
		});		
	}	
	
	//function doDeploy(sno,bno){
    function log(bno){
    	
    	openLogView();
    	
    	wSocket = null;
        wSocket = new WebSocket("ws://localhost:<%=request.getServerPort()%>/cmmn/log.do"); <%/* 웹소켓 요청 URL */%>
        wSocket.onmessage = function(e){
        	
            var pre = document.createElement("p");
            pre.innerHTML = e.data;
            
            $("#log-content").append(pre);
            $("#log-content").scrollTop($("#log-content")[0].scrollHeight);            	
        }
        
        wSocket.onopen = function(e){ 
        	var logfile = "${serverInfo.tarSrcRtpath}"+"/logs/deploy_log_"+bno+".log";
            wSocket.send(logfile);
            $("#log-content").append("<p> loading..</p>");
            $("#loading").attr("style","display:block");
        } 
        
        wSocket.onclose = function(e){
        	console.log(wSocket);
        	console.log(e);
            $("#log-content").append("<p>server connect closed</p>");
            $("#loading").attr("style","display:none");
        }
        
        wSocket.onerror = function(e){ 
            console.log("==error==");
        }        
    }	
    
    function disconnect() {
        if (wSocket) {
        	wSocket.close();
        	wSocket = null;
        }
    }    
    
    function rollback(sno,bno){

		$.ajax({
			type:'post',
			dataType:'json',
			url:'deploymng/doRollback.do',
			beforeSend: function( xhr ) {
				$.blockUI();
				log(bno);
			},
			data:{"sNum":sno,"bNum":bno},
			success:function(data) {
				if(data == '001'){
					alert('롤백을 완료하였습니다.');
					//disconnect();
					//location.href = "/deploymng/deploy_list.do";
					
				}else if(data == '900'){
					alert('롤백을 실패하였습니다. 로그정보를 확인하시고 난후 배포를 다시 시작하십시오.');
				}else{
					alert('롤백을 실패하였습니다. 로그정보를 확인하시고 난후 배포를 다시 시작하십시오.');
				}
			},
			error:function(xhr, stts, err) {
				alert('롤백을 실패하였습니다. 로그정보를 확인하시고 난후 배포를 다시 시작하십시오.');
				//disconnect();
			},
			complete:function(xhr, stts) {
				$.unblockUI();
				//disconnect();
			}
		});		    		
    }
</script>

<div class="container">
    <div class="contents">
        <ul class="path">
            <li><a href="/servmng/targetser_list.do">Home</a></li>
            <li>Build List</li>
        </ul>
        <div class="pagetitle"><h2>Build List</h2></div>
        <div>&nbsp;</div>
        
        <div class="section">

	        <div class="vertical table">
	            <table>
	                <colgroup>
	                    <col style="width: 200px;">
	                    <col>
	                    <col style="width: 200px;">
	                    <col>
	                </colgroup>
	                <tbody>
	                <tr>
	                    <th>Project Name</th>
	                    <td colspan="3">${serverInfo.sysNm }</td>
	                </tr>
	                <tr>
	                    <th>Domain</th>
	                    <td >${serverInfo.sysDomain }</td>
	                </tr>                
	                <tr>
	                    <th>Final build Ver.</th>
	                    <td colspan="3">${serverInfo.depoVers }</td>
	                </tr>
	                <!-- <tr>
	                    <th>최신배포 시간</th>
	                    <td colspan="3">${serverInfo.depoTime }</td>
	                </tr>-->		                                
					</tbody>
				</table>
	        </div>
	        
	    </div>
	    
        <div class="section">
        	<form name="frm" id="frm" action="./deploy_list.do" method="post">
        		<input type="hidden" id="page" name="page" value="" />
        	</form>         
            <div class="aligner" data-bottom="xs">
                <div class="right">
                    <button onclick="javascript:register();">Build Register</button>
                </div>
            </div>
            <div class="table">
                <table>
                    <colgroup>
                        <col style="width: 5%">
                        <col style="width: 15%">
                        <col style="width: 18%">
                        <col style="width: 5%">
                        <!-- <col style="width: 7%"> -->
                        <col style="width: 10%">
                        <!-- <col style="width: 10%"> -->
                        <col style="width: 9%">
                        <col style="width: 12%">
                        <col style="width: 18%">
                    </colgroup>
                    <thead>
                    <tr>
                        <th>Ver.</th>
                        <th>Deploy Name</th>
                        <th>Versions</th>
                        <th>File Count</th>
                        <!-- <th>결재상태</th> -->
                        <th>WAS Restart(Y/N)</th>
                        <!-- <th>배포상태</th> -->
                        <th>Build State</th>
                        <th>Build Time</th>
                        <th>Etc</th>
                    </tr>
                    </thead>
                    <tbody>
                    	<c:if test="${not empty deployList }">
                    		<c:forEach items="${deployList }" var="list" varStatus="status">
                    			<tr>
			                        <td>${list.bNum}</td>
			                        <td class="ellipsis"><a href="#" onclick="javascript:goDetailView('${list.bNum}');">${list.depoNm }</a></td>
			                        <td>${list.depoVers }</td>
			                        <td>${list.fileCnt }</td>
			                        <%-- <td>${list.appState }</td> --%>
			                        <td>${list.wasRestartYn }/${list.depoType }</td>
			                        <%-- <td>${list.depoState }</td> --%>
			                        <td>
			                        	<c:choose>
			                        		<c:when test="${list.depoState eq 'W0'}">
			                        			<a href="#" onclick="javascript:doDeploy('${params.sNum}','${list.bNum}');return false;">Build Start</a>
			                        		</c:when>
			                        		<c:when test="${list.depoState eq 'WX'}">
			                        			<a href="#" onclick="javascript:doDeploy('${params.sNum}','${list.bNum}');return false;">ReBuild</a>
			                        		</c:when>
			                        		<c:when test="${list.depoState eq 'W9'}">
			                        			Build Comp.
			                        		</c:when>			                        		
			                        		<c:otherwise>on Build</c:otherwise>
			                        	</c:choose>
			                        
			                        	
			                        </td>                        
			                        <td>
			                        	<c:if test="${list.depoState eq 'W9'}">
				                        	<c:choose>
				                        		<c:when test="${empty list.depoTime}"></c:when>
				                        		<c:when test="${fn:length(list.depoTime) < 8}">${list.depoTime}</c:when>
				                        		<c:when test="${fn:length(list.depoTime) > 12}">${list.depoTime}</c:when>
				                        		<c:otherwise>
				                        			<fmt:parseDate var="dateString" value="${list.depoTime}" pattern="yyyyMMddHHmm" />
				                        			<fmt:formatDate value="${dateString}" pattern="yyyy-MM-dd HH:mm" />
				                        		</c:otherwise>
				                        	</c:choose>
			                        	</c:if>
			                        </td>
			                        <td>
				                        <c:choose>
				                        	<c:when test="${list.depoState eq 'W9'}">
				                        		<a href="#" onclick="javascript:log(${list.bNum});">Log View</a> |
				                        		<a href="/deploymng/down_deployfile.do?bno=${list.bNum}">Deploy Files</a> |
				                        		<!-- <c:if test="${list.canRollback eq 'Y' }">| <a href="#" onclick="javascript:rollback('${params.sNum}','${list.bNum}');return false;">롤백하기</a> </c:if>-->
				                        		<a href="#" onclick="javascript:doDeploy('${params.sNum}','${list.bNum}');return false;">reBuild</a>
				                        	</c:when>
				                        	<c:when test="${list.depoState eq 'W9' or list.depoState eq 'WX' }">
				                        		<a href="#" onclick="javascript:log(${list.bNum});">Log View</a>
				                        	</c:when>				                        	
				                        	<c:otherwise></c:otherwise>
				                        </c:choose>
			                        </td>
		                        </tr>                    		
                    		</c:forEach>
                    	</c:if>
                    	<c:if test="${empty deployList }">
                    		<tr><td colspan="8">No Data</td></tr>
                    	</c:if>
                    </tbody>
                </table>
            </div>
           	<div class="pagination">
                <ui:pagination paginationInfo="${paginationInfo}" type="cmsDefault" jsFunction="fn_goPage"/>
            </div>
        </div>
    </div>
    <div id="logviewer" class="popup-mask log-pop">
        <div class="popup">
            <div class="popup-head">
                Log Viewer
            </div>
            <div class="popup-body">
				<div id="log-content" style="overflow:auto; height:400px;">
				</div> 
				<div id="loading" class="log_loading" style="display:none;">
					<img src="/resources/images/ajax_loader3.gif" style="heigth:50px;width:50px;"/>
				</div>
                <div class="aligner" data-top="lg">
                    <div class="center">
                        <button onclick="javascript:popCancel();">Close</button>
                    </div>
                </div>
            </div>
            <a href="#" class="btn-close" onclick="javascript:popCancel();"><i class="fa fa-times"></i></a>
        </div>
    </div>
    
</div>


