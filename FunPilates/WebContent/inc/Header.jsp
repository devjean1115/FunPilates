<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta charset="UTF-8" />
<title>Insert title here</title>
</head>
<body>
	<header>
	<c:choose>
	<c:when test="${empty sessionScope.id}">
		<div id="login">
			<a href="Main.jsp?center=member/login.jsp&title=Log In">login</a> | 
			<a href="Main.jsp?center=member/joinCheck.jsp&title=Join Check">join</a> |	
		</div>
	</c:when>
	<c:when test="${sessionScope.sel=='관리자'}">
		<div id="login">
			관리자님 어서 오시고! |
			<a href="Main.jsp?center=management/ManageMember.jsp&title=Management">관리자 페이지 | 
			<a href="MemberController.do?req=logout">logout</a> | 
		</div>
	</c:when>	
	<c:otherwise>
		<div id="login">
		<c:if test="${sessionScope.sel=='회원'}">
			${sessionScope.name} 회원님 어서 오세요! |
			<a href="Main.jsp?center=member/myPage.jsp&title=My Page">마이페이지</a> | 
			<a href="MemberController.do?req=logout&title=Log Out>">logout</a> | 
		</c:if>
		</div>
	</c:otherwise>
	</c:choose>	
			
	<div class="clear"></div>
	<!-- 로고들어가는 곳 -->
	<div id="logo"><img src="images/logo.gif" width="265" height="62" alt="Fun Web"></div>
	<!-- 로고들어가는 곳 -->
	<nav id="top_menu">
				<ul>
					<li><a href="Main.jsp?title=Fun Pilates">Home</a></li>
					<li><a href="AboutUsController.do?req=ceoMessage">About Us</a></li>
					<li><a href="#">Class</a></li>
					<li><a href="BoardController.do?req=qna">Q & A</a></li>
					<li><a href="AboutUsController.do?req=co_Info">Contact Us</a></li>
				</ul>
	</nav>
	</header>
</body>
</html>