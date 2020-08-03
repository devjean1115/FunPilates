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
<script>
	// 삭제 확인 창
	function confirmDel(f){
		if(confirm("정말 삭제하시겠습니까?")==true){

			f.submit();
		}
	} // confirmDel()
</script>
</head>
<body>
								<%-- About Us 좌측 목록 --%>	
	<jsp:include page="myPagelist.jsp"/>
		<%-- -------------------------------------------------------------------- --%>	

 	<article id="aboutUs"> 
	<h1>Reservation List</h1>
		<fieldset id="reservationListField">
				<table> 
					<tr>
						<th class="level">Level</th>
						<th class="class">Class</th>
						<th class="date">Date</th>
						<th class="time">Time</th>
						<th class="btns">Delete</th>
					</tr>
					<c:forEach var="reservList" items="${requestScope.reservList}">
					<tr>
						<td> ${reservList.level}</td>
						<td> 
						<c:choose>
							<c:when test="${reservList.reservClass eq 'equipmentexroom'}">
								대기구 수업
							</c:when> 
							<c:when test="${reservList.reservClass eq 'matexroom'}">
								소기구 수업
							</c:when>
						</c:choose>	 
						</td>
						<td> ${reservList.reservDate} ${reservList.reservDay}</td>
						<td> ${reservList.reservTime}</td>
						<td> 
							<form action="ClassController.do?req=delReservation&from=reservationList" method="post">
								<input type="hidden" name="level" value="${reservList.level}">
								<input type="hidden" name="reservClass" value="${reservList.reservClass}">
								<input type="hidden" name="reservDate" value="${reservList.reservDate}">
								<input type="hidden" name="reservDay" value="${reservList.reservDay}">
								<input type="hidden" name="reservTime" value="${reservList.reservTime}">
								<input type="hidden" name="id" value="${reservList.id}">
								<input type="button" id="rbtn" value="삭제" onclick="confirmDel(this.form);">
							</form>
						</td>
					</c:forEach>
					</tr>	
				</table>

	<div class="clear"></div>
	</article>
	<div class="clear"></div>
</body>
</html>