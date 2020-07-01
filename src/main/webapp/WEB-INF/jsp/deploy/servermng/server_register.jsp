<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp"%>

<script type="text/javascript">
function fn_save(){
	
	if($("#sysNm").val() == ''){
		alert("Poject Name is Required fields!!!");
		$("#sNum").focus();
		return;
	}
	
	if($("#sysDomain").val() == ''){
		alert("Domain is Required fields!!!");
		$("#sysDomain").focus();
		return;
	}
	
	if($("#projectId").val() == ''){
		alert("project Id is Required fields!!!");
		$("#projectId").focus();
		return;
	}	
	
	if($("#projectVersion").val() == ''){
		alert("project Version is Required fields!!!");
		$("#projectVersion").focus();
		return;
	}	
	
	if($("#reposUrl").val() == ''){
		alert("Repository URL is Required fields!!!");
		$("#reposUrl").focus();
		return;
	}		
	
	if($("#verContUserId").val() == ''){
		alert("CI User ID is Required fields!!!");
		$("#verContUserId").focus();
		return;
	}	

	if($("#verContPwd").val() == ''){
		alert("CI User Passwd is Required fields!!!");
		$("#verContPwd").focus();
		return;
	}	

	if($("#tarSrcRtpath").val() == ''){
		alert("Build Target Path is Required fields!!!");
		$("#tarSrcRtpath").focus();
		return;
	}
	
	if($("#deployPath").val() == ''){
		alert("Deploy Output Dir. is Required fields!!!");
		$("#deployPath").focus();
		return;
	}		
	
	var buildType =  $(":input:radio[name=buildType]:checked").val();
	if(buildType == ''){
		alert("Build Type is Required fields!!!");
		$("#buildType1").focus();
		return;
	}
	
	<c:if test="${empty serverInfo.buildRefFile1}">
		if($("#attach_file").val() == ''){
			alert("Build Ref File is Required fields!!!");		
			$("#attach_file").focus();
			return;
		}
	</c:if>
	
	var frm = document.frm;
	frm.target = "_self";
	frm.action = "./targetser_save.do";
	frm.submit();
}

function downloadSampleFile(){
	document.frm.target = "downloadframe";
	document.frm.action = "/servmng/down_samplefile.do";
	document.frm.submit();
}

function doInit(sno){
	if(!confirm("Project Setting Activate?")){
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

<div class="container">
    <div class="contents">
        <ul class="path">
            <li><a href="#">Home</a></li>
            <li><a href="#">Target Project Manage.</a></li>
            <li><a href="#">Project Register</a></li>
        </ul>
        <div class="pagetitle"><h2>Project Register</h2></div>
        <div>&nbsp;</div>
		
		<form name="frm" id="frm" action="./targetser_save.do" method="post" enctype="multipart/form-data">
			<input type="hidden" name="sNum" id="sNum" value="${serverInfo.sNum}"/>

        <div class="section">
	        <div class="aligner" data-bottom="xs">
	        	<div class="left">Target System register</div>
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
	                    <th>Project Name<span class="mark">*</span></th>
	                    <td><input type="text" name="sysNm" id="sysNm" value="${serverInfo.sysNm}"></td>
	                    <th>Domain<span class="mark">*</span></th>
	                    <td><input type="text" class="expanded" name="sysDomain" id="sysDomain" value="${serverInfo.sysDomain}"></td>	                    
	                </tr>
	                <tr>
	                    <th>project ID<span class="mark">*</span></th>
	                    <td ><input type="text" class="expanded" name="projectId" id="projectId" value="${serverInfo.projectId}"></td>
	                    <th>project Version<span class="mark">*</span></th>
	                    <td ><input type="text" class="expanded" name="projectVersion" id="projectVersion" value="${serverInfo.projectVersion}"></td>	                    
	                </tr>
	                <%-- <tr>
	                    <th>결재기능<span class="mark">*</span></th>
	                    <td colspan="3">
	                        <ul class="selection-group">
	                            <li>
	                                <input type="radio" id="appUseYn_1" name="appUseYn" value="Y" <c:if test="${serverInfo.appUseYn eq 'Y' }"> checked="checked"</c:if>>
	                                <label for="appUseYn_1">사용</label>
	                            </li>
	                            <li>
	                                <input type="radio" id="appUseYn_2" name="appUseYn" value="N" <c:if test="${serverInfo.appUseYn eq 'N' }"> checked="checked"</c:if>>
	                                <label for="appUseYn_2">미사용</label>
	                            </li>
	                        </ul>
	                    </td>                    
	                </tr> --%>                
					</tbody>
				</table>
	        </div>
	        
	    </div>
	    <!-- contents end -->
	    
        <div class="section">
	        <div class="aligner" data-bottom="xs">
	        	<div class="left">Source Version Management</div>
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
	                    <th>Version Manage.<span class="mark">*</span></th>
	                    <td colspan="3">
	                        <select name="verContType" id="verContType">
	                            <option value="svn">SubVersion</option>
	                        </select>
	                    </td>
	                </tr>
	                <tr>
	                    <th>Repository URL<span class="mark">*</span></th>
	                    <td colspan="3"><input type="text" class="expanded" id="reposUrl" name="reposUrl"  value="${serverInfo.reposUrl}"></td>
	                </tr>                
	                <tr>	                    
	                    <th>Port<span class="mark">*</span></th>
	                    <td colspan="3"><input type="text" id="verContPort" name="verContPort"  value="${serverInfo.verContPort}"></td>                                        
	                </tr>                
	                <tr>
	                    <th>CI User ID<span class="mark">*</span></th>
	                    <td colspan="3"><input type="text" id="verContUserId" name="verContUserId"  value="${serverInfo.verContUserId}"></td>
	                </tr>                
	                <tr>	                    
	                    <th>CI User Passwd<span class="mark">*</span></th>
	                    <td colspan="3"><input type="password" id="verContPwd" name="verContPwd"  value="${serverInfo.verContPwd}"></td>                                        
	                </tr>                
					</tbody>
				</table>
	        </div>
	        
	    </div>	    
	    
        <div class="section">
	        <div class="aligner" data-bottom="xs">
	        	<div class="left">Deploy Setting</div>
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
	                    <th>Build Target Path<span class="mark">*</span></th>
	                    <td colspan="3"><input type="text" id="tarSrcRtpath" name="tarSrcRtpath"  value="${serverInfo.tarSrcRtpath}"></td>
	                </tr>                
<%-- 	                <tr>	                    
	                    <th>Build Output path<span class="mark">*</span></th>
	                    <td colspan="3"><input type="text" id="tarBldRtpath" name="tarBldRtpath"  value="${serverInfo.tarBldRtpath}"></td>                                        
	                </tr> --%>                              
	                <tr>
	                    <th>Deploy Output Dir.(webapps)<span class="mark">*</span></th>
	                    <td colspan="3"><input type="text" class="expanded" name="deployPath" id="deployPath" value="${serverInfo.deployPath}"></td>
	                </tr>
	                <tr>
	                    <th>Build Type</th>
	                    <td colspan="3">
	                        <ul class="selection-group">
	                            <li>
	                                <input type="radio" id="buildType_1" name="buildType" value="A" <c:if test="${serverInfo.buildType eq 'A' }"> checked="checked"</c:if>>
	                                <label for="buildType_1">Java Ant Build</label>
	                            </li>
	                            <li>
	                                <input type="radio" id="buildType_2" name="buildType" value="M" <c:if test="${serverInfo.buildType eq 'M' }"> checked="checked"</c:if>>
	                                <label for="buildType_2">Java Maven Build</label>
	                            </li>
	                            <li>
	                                <input type="radio" id="buildType_3" name="buildType" value="N" <c:if test="${serverInfo.buildType eq 'N' }"> checked="checked"</c:if>>
	                                <label for="buildType_3">None Build or Not Java</label>
	                            </li>	                            
	                        </ul>
					    </td>
	                </tr>	
	                <tr>
	                    <th>Build Ref File</th>
	                    <td colspan="3">
							<input type="file" id="attach_file" name="attach_file" onchange="javascript:fn_fileMultiAtt(this, 'attach_file','N');">
	                        <ul class="file attach_file">
	                            <c:if test="${!empty serverInfo.buildRefFile1}">
	                                <li id="att_fileno_buildRefFile1" class="${fn:substring(file.fileExt,0,3)}">${serverInfo.buildRefFile1}
	                                    <a href="javascript:fn_delAttFile('att_fileno_buildRefFile1');"><img src="/resources/images/icon-delete.png" alt="delete btn"></a>
	                                </li>
                                </c:if>     
	                        </ul>
							<br/>
							<button onclick="javascript:downloadSampleFile();return false;" style="background:#D5DBDB">Sample File Download</button>
							<br/>							
							<font color="red"><b>※ download sample file, modify build properties and then upload that file.</b></font>	                        							
					    </td>
	                </tr>		                                        
					</tbody>
				</table>
	        </div>
	        
	    </div>	    	    
	    

	    
	    </form>
	    
	    		  
	    <div>&nbsp;</div>  
        <div class="aligner" data-bottom="xs">
            <div class="right">
            	<c:if test="${not empty serverInfo.sNum}">
            		<button onclick="javascript:doInit('${serverInfo.sNum}');">Activate</button>
            	</c:if>
                <button onclick="javascript:fn_save();">Save</button>
                <button onclick="javascript:location.href='/servmng/targetser_list.do';">Cancel</button>
            </div>
        </div>	    
	</div>
</div>
<iframe name="downloadframe"  width="0px" height="0px" src="" frameborder="0"></iframe>
