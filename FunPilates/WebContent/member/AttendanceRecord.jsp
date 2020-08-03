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
	<jsp:include page="myPagelist.jsp"/>
		<%-- -------------------------------------------------------------------- --%>	

 	<article id="aboutUs"> 
	<h1>Attendance List</h1>
		<h2>현재 출석 일수 : ${requestScope.attendanceCnt}일</h2>
		<fieldset id="reservationListField">
				<table> 
					<tr>
						<th class="level">Level</th>
						<th class="class">Class</th>
						<th class="date">Date</th>
						<th class="time">Time</th>
					</tr>
					<c:forEach var="attendanceList" items="${requestScope.attendanceList}">
					<tr>
						<td> ${attendanceList.level}</td>
						<td> 
						<c:choose>
							<c:when test="${attendanceList.reservClass eq 'equipmentexroom'}">
								대기구 수업
							</c:when> 
							<c:when test="${attendanceList.reservClass eq 'matexroom'}">
								소기구 수업
							</c:when>
						</c:choose>	 
						</td>
						<td> ${attendanceList.reservDate} ${attendanceList.reservDay}</td>
						<td> ${attendanceList.reservTime}</td>
					</c:forEach>
					</tr>	
				</table>

	<div class="clear"></div>
	</article>
	<div class="clear"></div>
</body>
</html>