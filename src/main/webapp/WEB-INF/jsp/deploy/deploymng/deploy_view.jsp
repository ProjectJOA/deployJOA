<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp"%>

<script type="text/javascript">
function fn_list(){
	location.href = "./deploy_list.do";
}

function fn_goUpdate(){
	var frm = document.frm;
	frm.action ="./deploy_editor.do"
	frm.submit();
}

function openVerList(){
    var left = (screen.availWidth / 2) - 400;
    var top = (screen.availHeight / 2) - 430;
    
    window.open("/vermng/popver_list.do?ver=","Version List", "scrollbars=yes,resizable=yes,width=960,height=600,left="+left+",top="+top);
}

function openFiles(ver){
    var left = (screen.availWidth / 2) - 400;
    var top = (screen.availHeight / 2) - 430;
    
    window.open("/vermng/popfile_list.do?ver="+ver,"Version File List", "scrollbars=yes,resizable=yes,width=800,height=600,left="+left+",top="+top);
}

</script>

<div class="container">
    <div class="contents">
        <ul class="path">
            <li><a href="/servmng/targetser_list.do">Home</a></li>
            <li><a href="/deploymng/deploy_list.do">Build List</a></li>
            <li>Build Detail</li>
        </ul>
        <div class="pagetitle"><h2>Build Detail View</h2></div>
        <div>&nbsp;</div>
		
		<form name="frm" id="frm" action="" method="post">
			<input type="hidden" name="sNum" id="sNum" value="${params.sNum}"/>
			<input type="hidden" name="bNum" id="bNum" value="${params.bNum}"/>

        <div class="section">
	        <div class="aligner" data-bottom="xs">
	        	<div class="left">Build Detail View</div>
	            <div class="right"><span class="mark">*</span> Required fields</div>
	        </div>
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
	                    <th>Deploy Name<span class="mark">*</span></th>
	                    <td colspan="3">${deployInfo.depoNm}</td>
	                </tr>
	                <tr>
	                    <th>WAS Restart(Y/N)<span class="mark">*</span></th>
	                    <td colspan="3">
	                    	<c:choose>
	                    		<c:when test="${deployInfo.wasRestartYn eq 'Y'}">Restart</c:when>
	                    		<c:otherwise>None</c:otherwise>
	                    	</c:choose>
	                    </td>                    
	                </tr>
	                <tr>
	                    <th>Deploy Type<span class="mark">*</span></th>
	                    <td colspan="3">
	                    	<c:choose>
	                    		<c:when test="${deployInfo.depoType eq 'B'}">
	                    			Deploy Section Versions&nbsp;(Start Vers. No ~ End Vers. No)	                    			
	                    		</c:when>
	                    		<c:otherwise>Deploy selected Versions</c:otherwise>
	                    	</c:choose>
	                    </td>                    
	                </tr>	                
	                <tr>
	                    <th>Build Versions<span class="mark">*</span></th>
	                    <td colspan="3">
	                    	<c:choose>
	                    		<c:when test="${deployInfo.depoType eq 'B'}">
			                    	<c:if test="${deployInfo.depoType eq 'B'}">
			                    		<c:set var="vVerSplitNos" value="${fn:split(deployInfo.depoVers,',')}" />
			                    		<c:if test="${fn:length(vVerSplitNos) > 1 }">
				                    		Start Ver. No : ${vVerSplitNos[0] }<br/>
				                    		End Ver. No : ${vVerSplitNos[1] }		                    		
			                    		</c:if>
			                    	</c:if>	                    	
	                    		</c:when>
	                    		<c:otherwise>${deployInfo.depoVers}</c:otherwise>
	                    	</c:choose>
	                    </td>
	                </tr>
	                <tr>
	                <%--     <th>결재상태<span class="mark">*</span></th>
	                    <td colspan="3">${deployInfo.appState}</td>
	                </tr> --%>
	                <tr>
	                    <th>Build State<span class="mark">*</span></th>
	                    <td colspan="3">
	                       	<c:choose>
	                       		<c:when test="${deployInfo.depoState eq 'W0'}">
	                       			Build Ready
	                       		</c:when>
	                       		<c:when test="${deployInfo.depoState eq 'WX'}">
	                       			Build Error
	                       		</c:when>
	                       		<c:when test="${deployInfo.depoState eq 'W9'}">
	                       			Build Compeleted.
	                       		</c:when>			                        		
	                       		<c:otherwise>on Build</c:otherwise>
	                       	</c:choose>	                    
	                    </td>
	                </tr>
	                <tr>
	                    <th>Build Time<span class="mark">*</span></th>
	                    <td colspan="3">
	                       	<c:choose>
	                       		<c:when test="${empty deployInfo.depoTime}"></c:when>
	                       		<c:when test="${fn:length(deployInfo.depoTime) < 8}">${deployInfo.depoTime}</c:when>
	                       		<c:when test="${fn:length(deployInfo.depoTime) > 12}">${deployInfo.depoTime}</c:when>
	                       		<c:otherwise>
	                       			<fmt:parseDate var="dateString" value="${deployInfo.depoTime}" pattern="yyyyMMddHHmm" />
	                       			<fmt:formatDate value="${dateString}" pattern="yyyy-MM-dd HH:mm" />
	                       		</c:otherwise>
	                       	</c:choose>		                    
	                    </td>
	                </tr>	                	                	                	                                
					</tbody>
				</table>
	        </div>
	        
	    </div>
	    <!-- contents end -->
	    
        <div class="section">
	        <div class="aligner" data-bottom="xs">
	        	<div class="left">Selected Version List</div>
	        </div>
	        <div class="vertical table">
                <table>
                    <colgroup>
                        <col style="width: 10%">
                        <col style="width: 50%">
                        <col style="width: 10%">
                        <col style="width: 10%">
                        <col style="width: 20%">
                    </colgroup>
                    <thead>
                    <tr>
                        <th>Ver.</th>
                        <th>Comment</th>
                        <th>File Count</th>
                        <th>Author</th>
                        <th>Reg. Date</th>
                    </tr>
                    </thead>
                    <tbody id="versionData">
                    	<c:if test="${not empty deployVers }">
                    		<c:forEach items="${deployVers }" var="lst" varStatus="status">
	                    		<tr>
			                        <td>${lst.bSnum}</td>
			                        <td class="ellipsis">
			                        	<c:choose>
			                        		<c:when test="${empty lst.devComment}">[no comment]</c:when>
			                        		<c:otherwise>${lst.devComment}</c:otherwise>
			                        	</c:choose>
			                        </td>
			                        <td><a href="#" onclick="javascript:openFiles('${lst.bSnum}');return false;">${lst.fileCnt}</a></td>
			                        <td>${lst.devId}</td>
			                        <td>${lst.svnRegTime}</td>
			                    </tr>                    			
                    		</c:forEach>
                    	</c:if>
                    	<c:if test="${empty deployVers }">
                    		<tr><td colspan="5">No Data</td></tr>
                    	</c:if>
                    </tbody>
                </table>
	        </div>
	        
	    </div>	    
	    
	    </form>
	    		  
	    <div>&nbsp;</div>  
        <div class="aligner" data-bottom="xs">
            <div class="right">
                <button type="button" onclick="javascript:fn_goUpdate();">Edit</button>
                <button type="button" onclick="javascript:fn_list();">List</button>
            </div>
        </div>	    
	</div>
</div>
