<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp"%>

<script type="text/javascript">
	function doPatch(verId, verNo){
		var frm = document.frm;
		alert("System update start!");
		
		frm.patchVer.value = verId;
		frm.sVerNo.value = verNo;
		frm.submit();
	}
</script>

<div class="container">
    <div class="contents">
        <ul class="path">
            <li><a href="#">Home</a></li>
            <li><a href="#">System Config</a></li>
            <li><a href="#">Update History</a></li>
        </ul>
        <div class="pagetitle"><h2>System Update History</h2></div>
        <div>&nbsp;</div>

        <div class="section">
        	<form name="frm" id="frm" action="/sysconfig/do_patch.do" method="post">
        		<input type="hidden" name="patchVer" id="patchVer" value=""/>
        		<input type="hidden" name="sVerNo" id="sVerNo" value=""/>
        	</form>
	        <!-- <div class="aligner" data-bottom="xs">
	        	<div class="left">Target System register</div>
	        </div> -->
            
            <c:forEach items="${patchHist }" var="patchHist" varStatus="status">
            	<div class="patch_top">
            		<div class="patch_ver_top">
            			<c:if test="${empty patchHist.patchedYn or patchHist.patchedYn eq 'N' }">
            				<strong class="patch_ready">Ready</strong>
            			</c:if>
            			<c:if test="${patchHist.patchedYn eq 'Y' }">
            				<strong class="patch_updated">Patched</strong>
            			</c:if>            			
	            		<span class="patch_ver_title">${patchHist.sVerNo}</span>
	            		<div class="patch_btn_time">
		                   	<c:choose>
		                   		<c:when test="${empty patchHist.patchedYn or patchHist.patchedYn eq 'N'}">
		                   			<a href='#' class="patch_start_btn" onclick="javascript:doPatch('${fn:replace(patchHist.sVerNo,'.','-')}','${patchHist.sVerNo}')" >Start patch</a>
		                   		</c:when>
		                   		<c:otherwise>
		                   			(Updated Time : ${fn:substring(patchHist.patchedTime,0,4)}-${fn:substring(patchHist.patchedTime,4,6)}-${fn:substring(patchHist.patchedTime,6,8)})
		                   		</c:otherwise>
		                   	</c:choose>
	                   	</div>
                   	</div>
                   	<div class="patch_cont_top">${patchHist.patchedCont}</div>	            		
            	</div>
            </c:forEach>
        </div>
	</div>
</div>			

