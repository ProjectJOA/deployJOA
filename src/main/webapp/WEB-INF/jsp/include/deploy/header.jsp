<%@ page contentType="text/html;charset=utf-8" session="true"%>
<%@ page import="kr.co.projectJOA.com.util.DateUtil" %>
<%@ page import="java.util.List" %>
<%@ page import="kr.co.projectJOA.com.util.ResultMap" %>
<!DOCTYPE html>
<html lang="ko">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
    <meta charset="UTF-8">
    <title>ProjectJOA - DeployJOA</title>
    <link rel="stylesheet" href="/resources/css/basecamp.min.css">
    <link rel="stylesheet" href="/resources/css/pages.css">
    <link rel="stylesheet" href="/resources/css/font-awesome.css">
    <script src="/resources/js/lib.js"></script>
    <script src="/resources/js/script.js"></script>
    <script src="/resources/js/jquery.blockUI.js"></script>
    <script src="/resources/js/comm_fileupload.js"></script>
    <script src="/resources/js/basecamp.min.js"></script>
</head>
<body>
<div class="header">
    <div class="inner">
        <h1><a href="/servmng/targetser_list.do"><font color="#ffffff">DeployJOA</font></a></h1>
        <h1><a href="/sysconfig/patch_his.do"><img src="/resources/images/icon-config.png" class="img_config"/></a></h1>
        <!-- <ul class="gnb">
            <li>
                <a href="/servmng/targetser_list.do">Target System Manage.</a>
                 <ul>
                    <li><a href="#">하위메뉴1</a></li>
                    <li><a href="#">하위메뉴2</a></li>
                    <li><a href="#">하위메뉴3</a></li>
                </ul>
            </li>
            <li>
                <a href="/deploymng/deploy_list.do">Build List</a>
                <ul>
                    <li><a href="/deploymng/deploy_list.do">Build List</a></li>
                    <li><a href="#">하위메뉴2</a></li>
                    <li><a href="#">하위메뉴3</a></li>
                    <li><a href="#">하위메뉴4</a></li>
                </ul>
            </li>
            <li>
                <a href="/vermng/version_list.do">Version List</a>
                <ul>
                    <li><a href="/vermng/version_list.do">Version List</a></li>
                </ul>
            </li>
        </ul> -->
    </div>
</div>