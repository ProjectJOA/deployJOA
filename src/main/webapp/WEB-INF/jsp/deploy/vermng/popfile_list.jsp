<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
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
        
</head>
<body>
<div class="container" style="margin:10px">
    <div id="contents">
    	<div>&nbsp;</div>
        <div class="pagetitle"><h2>Version File List</h2></div>
        <div>&nbsp;</div>    
        <div class="section">
        	<div class="aligner" data-bottom="xs">
               	<div class="left">File count : <span class="point1">${fn:length(fileLst)}</span>[Version : ${targetRevision}]</div>
               </div>
            <div class="table">
                <table>
                    <colgroup>
                    <col style="width: 10%">
                       <col style="width: 10%">
                       <col style="width: 10%">
                       <col style="width: 70%">
                   </colgroup>
                <thead>
                    <tr>
                    	<th>No.</td>
                        <th>Changed Type</th>
                        <th>File/Dir</th>
                        <th>file Path</th>
                    </tr>
                   </thead>
                    <tbody>
                    	<c:forEach items="${fileLst}" var="list" varStatus="status">
		                    <tr>
		                    	<td>${fn:length(fileLst)-status.index}</td>
                       			<td>
                       				<c:choose>
                       					<c:when test="${list.fileType eq 'M'}">Modify</c:when>
                       					<c:when test="${list.fileType eq 'A'}">ADD</c:when>
                       					<c:otherwise>${list.fileType}</c:otherwise>
                       				</c:choose>
                       			</td>
                       			<td>${list.fileKind}</td>
		                        <td>${list.filePath}</td>
		                    </tr>	                    	
                    	</c:forEach>
                    </tbody>
                </table>
            </div>
            <div class="aligner" data-top="xs">
                <div class="right"><button onclick="self.close()">Close</button></div>
           	</div>
        </div>
    </div>
</div>
</body>
</html>