<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp"%>

<script type="text/javascript">
	function openFiles(ver){
	    var left = (screen.availWidth / 2) - 400;
	    var top = (screen.availHeight / 2) - 430;
	    
	    window.open("/vermng/popfile_list.do?ver="+ver,"version file List", "scrollbars=yes,resizable=yes,width=800,height=600,left="+left+",top="+top);
	}
	
	function fn_goPage(pageNo) {
		var frm = document.frm;
	    
	    $("input[name=page]").val(pageNo);
	    
	    frm.action="./version_list.do";
	    frm.submit();
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
				vhtml += "<tr>";
				vhtml += "<td>"+logLst[i].version+"</td>";
				vhtml += "<td class='ellipsis'>"+comment+"</td>";
				vhtml += "<td><a href='#' onclick=\"javascript:openFiles('"+logLst[i].version+"');return false;\">"+logLst[i].filecnt+"</a></td>";
				vhtml += "<td>"+logLst[i].author+"</td>";
				vhtml += "<td>"+logLst[i].reg_date+"</td>";
				vhtml += "</tr>";
				$("#prevEndVersion").val(logLst[i].version);
			}
			$("#versionData").append(vhtml);			
		}
	}
</script>

<div class="container">
    <div class="contents">
        <ul class="path">
            <li><a href="/servmng/targetser_list.do">Home</a></li>
            <li>Version List</li>            
        </ul>
        <div class="pagetitle"><h2>Version List</h2></div>
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
	                    <th>System Name</th>
	                    <td colspan="3">${serverInfo.sysNm }</td>
	                </tr>
	                <tr>
	                    <th>Domain</th>
	                    <td >${serverInfo.sysDomain }</td>
	                </tr>                
	                <tr>
	                    <th>Final Deploy Ver.</th>
	                    <td colspan="3">${serverInfo.depoVers }</td>
	                </tr>
	                <!-- <tr>
	                    <th>최신배포 시간</th>
	                    <td colspan="3">${serverInfo.depoTime }</td>
	                </tr> -->	                                
					</tbody>
				</table>
	        </div>
	        
	    </div>
	    
        <div class="section">
        	<form name="frm" id="frm" action="./version_list.do" method="post">
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
            <!-- <div class="aligner" data-bottom="xs">
                <div class="right">
                    <button>등록</button>
                </div>
            </div> -->

            <div class="table">
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

                    	<c:forEach items="${logLst }" var="lst" varStatus="status">
		                    <tr>                    	
		                        <td>${lst.version}</td>
		                        <td class="ellipsis"><c:choose><c:when test="${empty lst.comment }">[no comment]</c:when><c:otherwise>${lst.comment} </c:otherwise></c:choose>   </td>
		                        <td><a href="#" onclick="javascript:openFiles('${lst.version}');return false;">${lst.filecnt}</a></td>
		                        <td>${lst.author}</td>
		                        <td>${lst.reg_date}</td>
		                    </tr>	                                            		
                    	</c:forEach>
                    </tbody>
                </table>
            </div>
            <div class="aligner" data-bottom="xs">
                <div class="right">
                    <button onclick="javascript:get25More();return false;">25more+</button>
                </div>
            </div>
        </div>
    </div>
</div>

