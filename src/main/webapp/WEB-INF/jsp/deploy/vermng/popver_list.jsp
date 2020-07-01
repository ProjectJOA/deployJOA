<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>DeployJOA - Version file </title>
    <link rel="stylesheet" href="/resources/css/basecamp.css">
    <link rel="stylesheet" href="/resources/css/pages.css">
    <link rel="stylesheet" href="/resources/css/font-awesome.css">
    <script src="/resources/js/lib.js"></script>
    <script src="/resources/js/script.js"></script>
    <script src="/resources/js/jquery.blockUI.js"></script>
    
<script type="text/javascript">
	function openFiles(ver){
	    var left = (screen.availWidth / 2) - 400;
	    var top = (screen.availHeight / 2) - 430;
	    
	    window.open("/vermng/popfile_list.do?ver="+ver,"Version File List", "scrollbars=yes,resizable=yes,width=800,height=600,left="+left+",top="+top);
	}
	
	function get25More(){
		$.ajax({
			type:'post',
			dataType:'json',
			url:'/vermng/version_listmore.do',
			beforeSend: function( xhr ) {
				$.blockUI();				
			},
			data:$('#frm').serialize(),
			success:function(data) {
				setMoreList(data);
			},
			error:function(xhr, stts, err) {
				alert('versions search failed.\nPlease check and search again.');
			},
			complete:function(xhr, stts) {
				$.unblockUI();
			}
		});		
	}
	
	function setMoreList(logLst){
		var vhtml = "";
		if(logLst.length>0){
			for(var i=0;i<logLst.length;i++){
				var comment = "[no comment]";
				if(logLst[i].comment != ""){
					comment = logLst[i].comment; 
				}
				vhtml += "<tr id='version_"+logLst[i].version+"'>";
				vhtml += "<td>";
				vhtml += "<input type='checkbox' id='checkbox_"+logLst[i].version+"' name='depoVers' value='"+logLst[i].version+"'/>";
				vhtml += "<label for='checkbox_"+logLst[i].version+"'></label>";
				vhtml += "</td>";
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
				vhtml += "</tr>";
				$("#prevEndVersion").val(logLst[i].version);
			}
			$("#versionData").append(vhtml);			
		}
	}
	
	function setDeployVers(){
		
		if(!confirm("register selected versions?")){
			return;
		}
		
		var vers = "";

		$("input:checkbox[name='depoVers']:checked").each(function(pi,obj){
			if(vers != ''){
				vers += ","+obj.value;
			}else{
				vers += obj.value;
			}
			var vtr = $("#version_"+obj.value).html();
			var vfirst = vtr.indexOf("</td>");
			var vhtml = "";
			if(vfirst > 0){
				vtr = vtr.substring(vfirst+5,vtr.length);	
			}
			vhtml = "<tr>"+vtr+"<td><a href=\"#\" onclick=\"javascript:delVer(this);\">Delete</a></td></tr>";
			$(opener.document).find("#versionData").append(vhtml);
		});
		$(opener.document).find("#depoVers").val(vers);
		
		self.close();
	}	
</script>    
</head>
<body>
<div class="container" style="margin:10px">
    <div class="contents"  style="width:940px">
        <div class="pagetitle"><h2>Version Select</h2></div>
        <div>&nbsp;</div>
	    
        <div class="section">
        	<form name="frm" id="frm" action="./popver_list.do" method="post">
        		<input type="hidden" id="page" name="page" value="" />
				<input type="hidden" id="prevEndVersion" name="prevEndVersion" value="${endRevision }"/>        		
            <ul class="search">
                <li>
                    <div class="key">Search Versions</div>
                    <div class="value">
                        <div class="grouping">
                        	<div class="key">(Start Ver.)</div>
                            <div><input type="text" name="srchStartVersion" id="srchStartVersion" value="${srchStartVersion}"></div>
                            <div>~</div>
                            <div class="key">(End Ver.)</div>
                            <div><input type="text" name="srchEndVersion" id="srchEndVersion" value="${srchEndVersion }"></div>
                            <div>
                                <button onclick="submit();">Search</button>
                            </div>
                        </div>
                    </div>
                </li>
            </ul>
            </form>
        </div>
        	    
        <div class="section">
            <div class="aligner" data-bottom="xs">
                <div class="right">
                    <button type="button" onclick="javascript:setDeployVers();">Select Ver.</button>
                </div>
            </div>

            <div class="table">
                <table>
                    <colgroup>
                    	<col style="width: 8%">
                        <col style="width: 10%">
                        <col style="width: 42%">
                        <col style="width: 10%">
                        <col style="width: 10%">
                        <col style="width: 20%">
                    </colgroup>
                    <thead>
                    <tr>
                    	<th>Select</th>
                        <th>Ver.</th>
                        <th>Comment</th>
                        <th>File Count</th>
                        <th>Author</th>
                        <th>Reg. Date</th>
                    </tr>
                    </thead>
                    <tbody id="versionData">

                    	<c:forEach items="${logLst }" var="lst" varStatus="status">
		                    <tr id="version_${lst.version}">                   
		                    	<td>
	                               	<input type="checkbox" id="checkbox_${lst.version}" name="depoVers" value="${lst.version}"/>
	                               	<label for="checkbox_${lst.version}"></label>
		                    	</td>		                     	
		                        <td>${lst.version}</td>
		                        <td class="ellipsis"><c:choose><c:when test="${empty lst.comment }">[no comment]</c:when><c:otherwise>${lst.comment} </c:otherwise></c:choose>   </td>
		                        <td><a href="#" onclick="javascript:openFiles('${lst.version}');return false;">${lst.filecnt}</a></td>
		                        <td>${lst.author}</td>
		                        <td>${lst.reg_date}
		                        	<input type="hidden" name="bSnum" value="${lst.version}"/>
		                        	<input type="hidden" name="fileCnt" value="${lst.filecnt}"/>
		                        	<input type="hidden" name="devComment" value="${lst.comment}"/>
		                        	<input type="hidden" name="devId" value="${lst.author}"/>
		                        	<input type="hidden" name="svnRegTime" value="${lst.reg_date}"/>
		                        </td>
		                    </tr>	                                            		
                    	</c:forEach>
                    </tbody>
                </table>
            </div>
            <div>&nbsp;</div> 
            <div class="aligner" data-bottom="xs">
                <div class="right">
                    <button onclick="javascript:get25More();return false;">25more+</button>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>