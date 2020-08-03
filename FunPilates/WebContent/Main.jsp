<%@page import="org.apache.taglibs.standard.lang.jstl.test.PageContextImpl"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta charset="UTF-8" />
<title>Fun Pilates</title>
<link href="css/default.css" rel="stylesheet" type="text/css">

</head>
<body>
	<script>
		var page="${param.title}";
		if(!page)
		document.title="Fun Pilates";
		else
		document.title=page;
	</script>


	<%
		request.setCharacterEncoding("UTF-8");
		System.out.println("GitHub test_25line");
	%>

	<div id=wrap>
								<%-- 홈페이지 상단 --%>
	<%-- <c:set var="idck" value="${requestScope.idck}"/> --%>
								
	<jsp:include page="inc/Header.jsp"/> 
	<%-- -------------------------------------------------------------------- --%>
								<%-- 홈페이지 중단--%>	
	<c:set var="center" value="${param.center}"/>					
												
	<c:if test="${center==null}">
		<jsp:include page="inc/MainCenter.jsp"/>
	</c:if>			          
				          						
	<jsp:include page="${center}"/>
	
								<%-- 홈페이지 중단--%>						
	<%-- -------------------------------------------------------------------- --%>
								<%-- 홈페이지 하단 --%>
	<jsp:include page="inc/Footer.jsp"/>
	</div>
</body>
</html>
