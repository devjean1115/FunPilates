<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta charset="UTF-8" />
<title>Insert title here</title>
<link href="./css/default.css" rel="stylesheet" type="text/css">
<link href="./css/subpage.css" rel="stylesheet" type="text/css">

</head>
<body>
	<div id="sub_img_member"></div>
	<nav id="sub_menu">
		<ul>
			<li><a href="Main.jsp?center=member/myPage.jsp&title=My Page">정보 보기</a></li>
			<li><a href="Main.jsp?center=member/Reservation.jsp&title=Resrvation">예약 하기</a></li>
			<li><a href="ClassController.do?req=reservationList&id=${sessionScope.id}">예약 내역</a></li>
			<li><a href="ClassController.do?req=attendancRecord&id=${sessionScope.id}">출석 내역</a></li>
			<li><a href="ClassController.do?req=delMemberInfo">탈퇴 하기</a></li>
		</ul>
	</nav>
</body>
</html>