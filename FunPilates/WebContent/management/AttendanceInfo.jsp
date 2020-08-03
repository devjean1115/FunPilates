<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta charset="UTF-8" />
<title>Insert title here</title>
<link href="./css/default.css" rel="stylesheet" type="text/css">
<link href="./css/reservation.css" rel="stylesheet" type="text/css">

<script src="http://code.jquery.com/jquery-latest.min.js"></script>
</head>
<body>
								<%-- About Us 좌측 목록 --%>	
	<jsp:include page="ManagementList.jsp"/>
		<%-- -------------------------------------------------------------------- --%>	

 	<article id="aboutUs"> 
	<h1>Check Attendance</h1>
		<fieldset id="reservationField">
			<form action="ClassController.do?req=reservMemberToCheckAttendance" id="join" method="post" name="fr">
			<label>Level</label>
			 	<select id="level" name="level">
					<option value="basic">basic</option>
					<option value="intermediate">intermediate</option>
					<option value="advanced">advanced</option>
				</select><br><br>	
			<hr>
			
			<label>Class Name</label>
				<select id="reservClass" name="reservClass">
					<option value="equipmentexroom">대기구</option>
					<option value="matexroom">소기구</option>
				</select><br><br>
			<hr>
			<label>Reservation Date</label>
				 <input type="date" id="reservDate" name="reservDate">
			<hr>
			
			<label>Reservation Time</label>
			 	<select id="reservTime" name="reservTime">
					<option value="morningTime">오전</option>
					<option value="dayTime">오후</option>
					<option value="nightTime">저녁</option>
				</select><br><br>	
			<hr>
			
				<div class="clear"></div>
				<div id="buttons">
				<input type="submit" id="sbtn" value="출석부 불러오기" class="submit">
				</div>
			</form>	
	<div class="clear"></div>
	</article>
	<div class="clear"></div>
</body>
</html>