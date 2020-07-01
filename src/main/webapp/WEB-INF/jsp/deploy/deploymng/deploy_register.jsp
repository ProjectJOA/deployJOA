<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp"%>

<script type="text/javascript">

$( document ).ready(function() {
console.log("deployInfo.depoType:${deployInfo.depoType}");	
    if('${deployInfo.depoType}' == 'B'){
    	$("#tr_depoType_titl_one").attr("style","display:none");
    	$("#tr_depoType_titl_sec").attr("style","display:block");
    	
    	$("#tr_depoType_one").attr("style","display:none");
    	$("#tr_depoType_sec").attr("style","display:block");
    	
    }else{
    	$("#tr_depoType_titl_one").attr("style","display:block");
    	$("#tr_depoType_titl_sec").attr("style","display:none");
    	
    	$("#tr_depoType_one").attr("style","display:block");
    	$("#tr_depoType_sec").attr("style","display:none");    	
    }
});

function fn_list(){
	location.href = "./deploy_list.do";
}

function fn_setDepoType(obj){

	if($(obj).val() == 'P'){
		$("#tr_depoType_one").attr("style","display:block");
		$("#tr_depoType_sec").attr("style","display:none");		
		
		$("#tr_depoType_titl_one").attr("style","display:block");
		$("#tr_depoType_titl_sec").attr("style","display:none");		
	}else{
		$("#tr_depoType_one").attr("style","display:none");
		$("#tr_depoType_sec").attr("style","display:block");

		$("#tr_depoType_titl_one").attr("style","display:none");
		$("#tr_depoType_titl_sec").attr("style","display:block");		
	}
}

function fn_setSecVersion(){
	var startVerNo = $("#dpT_startVer").val();
	var endVerNo = $("#dpT_endVer").val();
	
	if(startVerNo == ''){
		alert('input the start version no.');
		$("#dpT_startVer").focus();
		return;
	}
	
	if(endVerNo == ''){
		alert('input the end version no.');
		$("#dpT_endVer").focus();
		return;
	}	
	
	$.ajax({
		type:'post',
		dataType:'json',
		url:'/vermng/get_sec_verInfos.do',
		beforeSend: function( xhr ) {
			$.blockUI();				
		},
		data:{dpT_startVer:$("#dpT_startVer").val(), dpT_endVer:$("#dpT_endVer").val()},
		success:function(data) {
			$("#versionData").empty();
			$("#depoVers").val("");
			
			setVerList(data.logLst);
		},
		error:function(xhr, stts, err) {
			alert('Save failed. please re-check and save again.');
		},
		complete:function(xhr, stts) {
			$.unblockUI();
		}
	});			
	///vermng/get_sec_verInfos.do
}

function fn_save(){
	//var frm = document.frm;
	//frm.submit();
	var verLen = $("input[name=bSnum]").length;
	
	if($("#depoNm").val() == ''){
		alert("Please input Deploy Name.");
		return;
	}
	if(verLen < 1){
		alert("Please register versions!!");
		return;
	}
	
	var paramArr = new Array();
	if(verLen > 0){
		for(var i=0;i<verLen;i++){
			var versObj = new Object();
			versObj.bSnum = $("input[name=bSnum]").eq(i).val();
			versObj.fileCnt = $("input[name=fileCnt]").eq(i).val();
			versObj.devComment = $("input[name=devComment]").eq(i).val();
			versObj.devId = $("input[name=devId]").eq(i).val();
			versObj.svnRegTime = $("input[name=svnRegTime]").eq(i).val();
			
			paramArr.push(versObj);
		}
	}
	
	var dataMap = new Object();
	dataMap.sNum = $("#sNum").val();
	dataMap.bNum = $("#bNum").val();
	dataMap.depoNm = $("#depoNm").val();
	dataMap.wasRestartYn = $("input:radio[name=wasRestartYn]:checked").val();
	dataMap.depoVers = $("#depoVers").val();
	dataMap.depoType =  $("input:radio[name=depoType]:checked").val(); 

	dataMap.versArr = paramArr;
	
	var jsonInfo = JSON.stringify(dataMap);
	console.log(jsonInfo);

	$.ajax({
		type:'post',
		dataType:'json',
		url:'/deploymng/deploy_save.do',
		beforeSend: function( xhr ) {
			$.blockUI();				
		},
		data:{"jsonInfo":jsonInfo},
		success:function(data) {
			//setMoreList(data);
			if(data.resultCd == '000'){
				setVerList(data.logLst);
			}
			alert('Save completed!');
			location.href = "/deploymng/deploy_list.do";
		},
		error:function(xhr, stts, err) {
			alert('Save failed. please re-check and save again.');
		},
		complete:function(xhr, stts) {
			$.unblockUI();
		}
	});		
	
}

function setVerList(logLst){
	var vhtml = "";
	var vers = "";
	if(logLst.length>0){
		for(var i=0;i<logLst.length;i++){
			if(vers != ''){
				vers += ","+logLst[i].version;
			}else{
				vers += logLst[i].version;
			}
			
			var comment = "[no comment]";
			if(logLst[i].comment != ""){
				comment = logLst[i].comment; 
			}
			vhtml += "<tr id='version_"+logLst[i].version+"'>";
			//vhtml += "<td>";
			//vhtml += "<input type='checkbox' id='checkbox_"+logLst[i].version+"' name='depoVers' value='"+logLst[i].version+"'/>";
			//vhtml += "<label for='checkbox_"+logLst[i].version+"'></label>";
			//vhtml += "</td>";
			vhtml += "<td>"+logLst[i].version+"</td>";
			vhtml += "<td class='ellipsis'>"+comment+"</td>";
			vhtml += "<td><a href='#' onclick=\"javascript:openFiles('"+logLst[i].version+"');return false;\">"+logLst[i].filecnt+"</a></td>";
			vhtml += "<td>"+logLst[i].author+"</td>";
			vhtml += "<td>"+logLst[i].reg_date;
				vhtml += "<input type='hidden' name='bSnum' value='"+logLst[i].version+"'/>"; 
				vhtml += "<input type='hidden' name='fileCnt' value='"+logLst[i].filecnt+"'/>";
				vhtml += "<input type='hidden' name='devComment' value='"+logLst[i].comment+"'/>";
				vhtml += "<input type='hidden' name='devId' value='"+logLst[i].author+"'/>";
				vhtml += "<input type='hidden' name='svnRegTime' value='"+logLst[i].reg_date+"'/>";
			vhtml += "</td>";
			vhtml += "<td><a href=\"#\" onclick=\"javascript:delVer(this);\">Delete</a></td>";
			vhtml += "</tr>";
			//$("#prevEndVersion").val(logLst[i].version);
		}
		$("#versionData").append(vhtml);
		$("#depoVers").val(vers);
	}
}

function openVerList(){
    var left = (screen.availWidth / 2) - 400;
    var top = (screen.availHeight / 2) - 430;
    
    window.open("/vermng/popver_list.do?ver=","Version List", "scrollbars=yes,resizable=yes,width=1024,height=800,left="+left+",top="+top);
}

function openFiles(ver){
    var left = (screen.availWidth / 2) - 400;
    var top = (screen.availHeight / 2) - 430;
    
    window.open("/vermng/popfile_list.do?ver="+ver,"Version File List", "scrollbars=yes,resizable=yes,width=800,height=600,left="+left+",top="+top);
}

function delVer(obj){
	if(!confirm("Delete selected version?")){
		return;
	}
	
	$(obj).closest("tr").remove();
	resetVers();
}

function resetVers(){
	var vers = "";
	var $trs = $("#versionData").find('tr');

	if($trs.length>0){
		
		for(var i=0;i<$trs.length;i++){
			var verData = $trs.eq(i).children().first().text();
			if(vers != ''){
				vers += ","+verData;
			}else{
				vers += verData;
			}
		}
	}
	
	$("#depoVers").val(vers);
}

</script>

<div class="container">
    <div class="contents">
        <ul class="path">
            <li><a href="/servmng/targetser_list.do">Home</a></li>
            <li><a href="/deploymng/deploy_list.do">Build List</a></li>
            <li>Build Register</li>
        </ul>
        <div class="pagetitle"><h2>Build Register</h2></div>
        <div>&nbsp;</div>
		
		<form name="frm" id="frm" action="./deploy_save.do" method="post">
			<input type="hidden" name="sNum" id="sNum" value="${params.sNum}"/>
			<input type="hidden" name="bNum" id="bNum" value="${params.bNum}"/>

        <div class="section">
	        <div class="aligner" data-bottom="xs">
	        	<div class="left">Build register</div>
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
	                    <td colspan="3"><input type="text" name="depoNm" id="depoNm" value="${deployInfo.depoNm}"></td>
	                </tr>
	                <tr>
	                    <th>WAS Restart(Y/N)<span class="mark">*</span></th>
	                    <td colspan="3">
	                        <ul class="selection-group">
	                            <li>
	                                <input type="radio" id="wasRestartYn_1" name="wasRestartYn" value="Y" <c:if test="${deployInfo.wasRestartYn eq 'Y'}">checked="checked"</c:if>>
	                                <label for="wasRestartYn_1">Restart</label>
	                            </li>
	                            <li>
	                                <input type="radio" id="wasRestartYn_2" name="wasRestartYn" value="N" <c:if test="${deployInfo.wasRestartYn eq 'N'}">checked="checked"</c:if>>
	                                <label for="wasRestartYn_2">None</label>
	                            </li>
	                        </ul>
	                    </td>                    
	                </tr>
	                <tr>
	                	<th>Deploy Type<span class="mark">*</span></th>
	                	<td colspan="3">
	                		<input type="radio" id="depoType1" name="depoType" value="P" onclick="javascript:fn_setDepoType(this);" <c:if test="${deployInfo.depoType eq 'P' or empty deployInfo.depoType}">checked="checked"</c:if>/>
	                		<label for="depoType1">One by one</label>
	                		<input type="radio" id="depoType2" name="depoType" value="B" onclick="javascript:fn_setDepoType(this);" <c:if test="${deployInfo.depoType eq 'B'}">checked="checked"</c:if>/>
	                		<label for="depoType2">Section</label>
	                		<!-- <input type="radio" id="depoType3" name="depoType" value="L"/>
	                		<label for="depoType3">Latest Vers</label> -->
	                	</td>
	                </tr>
	                <tr >
	                    <th>
	                    	<span id="tr_depoType_titl_one">Target Versions<span class="mark">*</span></span>
	                    	<span id="tr_depoType_titl_sec" style="display:none;">Section Versions<span class="mark">*</span></span>
	                    </th>
	                    <td colspan="3">
	                    	<div id="tr_depoType_one">
		                    	<input type="text" name="depoVers" id="depoVers" value="${deployInfo.depoVers}" style="display: inline-block;">
		                    	<button type="button" onclick="javascript:openVerList();return false;">Version Select</button>
	                    	</div>
	                    	<div id="tr_depoType_sec" style="display:none;">
	                    		<c:set var="vstVerNo" value="0" />
	                    		<c:set var="vedVerNo" value="0" />
		                    	<c:if test="${deployInfo.depoType eq 'B'}">
		                    		<c:set var="vVerSplitNos" value="${fn:split(deployInfo.depoVers,',')}" />
		                    		<c:if test="${fn:length(vVerSplitNos) > 1 }">
			                    		<c:set var="vstVerNo" value="${vVerSplitNos[0] }" />
			                    		<c:set var="vedVerNo" value="${vVerSplitNos[1] }" />		                    		
		                    		</c:if>
		                    	</c:if>
	                			<input type="text" id="dpT_startVer" name="dpT_startVer" value="${vstVerNo }" placeholder="start no." maxlength="5" style="width:130px;display: inline-block;" onkeypress="return fn_press(event, 'numbers');" onkeydown="fn_press_han(this);"/> 
	                			<span style="display: inline-block;">~</span>
	                			<input type="text" id="dpT_endVer" name="dpT_endVer" value="${vedVerNo }" placeholder="end no." maxlength="5" style="width:130px;display: inline-block;" onkeypress="return fn_press(event, 'numbers');" onkeydown="fn_press_han(this);"/>&nbsp;<button type="button" onclick="javascript:fn_setSecVersion();return false;">Version Select</button>
	                			<br/>
	                			<span style="font:red;">Section Deploy, SVN Files of versions from start SVN Version to end SVN Version will be deployed </span>	                    	
	                    	</div>	                    	
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
                        <col style="width: 40%">
                        <col style="width: 10%">
                        <col style="width: 10%">
                        <col style="width: 20%">
                        <col style="width: 10%">
                    </colgroup>
                    <thead>
                    <tr>
                        <th>Ver.</th>
                        <th>Comment</th>
                        <th>File Count</th>
                        <th>Author</th>
                        <th>Reg. Date</th>
                        <th>Del</th>
                    </tr>
                    </thead>
                    <tbody id="versionData">
                    	<c:if test="${not empty deployVers }">
                    		<c:forEach items="${deployVers }" var="lst" varStatus="status">
	                    		<tr>
			                        <td>${lst.bSnum}</td>
			                        <td class="ellipsis">${lst.devComment}</td>
			                        <td><a href="#" onclick="javascript:openFiles('${lst.bSnum}');return false;">${lst.fileCnt}</a></td>
			                        <td>${lst.devId}</td>
			                        <td>${lst.svnRegTime}
			                        	<input type="hidden" name="bSnum" value="${lst.bSnum}"/>
			                        	<input type="hidden" name="fileCnt" value="${lst.fileCnt}"/>
			                        	<input type="hidden" name="devComment" value="${lst.devComment}"/>
			                        	<input type="hidden" name="devId" value="${lst.devId}"/>
			                        	<input type="hidden" name="svnRegTime" value="${lst.svnRegTime}"/>
			                        </td>
			                        <td><a href="#" onclick="javascript:delVer(this);">Delete</a></td>
			                    </tr>                    			
                    		</c:forEach>
                    	</c:if>                    
                    </tbody>
                </table>
	        </div>
	        
	    </div>	    
	    
	    </form>
	    		  
	    <div>&nbsp;</div>  
        <div class="aligner" data-bottom="xs">
            <div class="right">
                <button type="button" onclick="javascript:fn_save();">Save</button>
                <button type="button" onclick="javascript:fn_list();">Cancel</button>
            </div>
        </div>	    
	</div>
</div>
