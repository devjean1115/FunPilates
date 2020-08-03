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
	function confirmAbsent(f, name, id){
		if(confirm(name+"(id:****"+id+") 회원님을 결석처리 하시겠습니까?")==true){
			$("#fca").attr("action","ClassController.do?req=delReservation&from=checkAttendance");
			f.submit();
		} else {
			return;
		}
	} // confirmAbsent()
	
	function confrimAttend(f, name, id){
		if(confirm(name+"(id:****"+id+") 회원님을 출석처리 하시겠습니까?")==true){
			f.submit();
		} else {
			return;
		}
	}
</script>




</head>
<body>
								<%-- About Us 좌측 목록 --%>	
	<jsp:include page="ManagementList.jsp"/>
		<%-- -------------------------------------------------------------------- --%>	

 	<article id="aboutUs"> 
	<h1>Check Attendance</h1>
		<fieldset id="reservationField">
			<label>Level</label>
					${requestScope.cBean.level}
			<hr>
			
			<label>Class Name</label>
				<c:choose>
					<c:when test="${requestScope.cBean.reservClass eq 'equipmentexroom'}">
					대기구 수업
					</c:when> 
					<c:when test="${requestScope.cBean.reservClass eq 'matexroom'}">
					소기구 수업
					</c:when>
				</c:choose>	 
			<hr>
			
			<label>Reservation Date</label>
				 ${requestScope.cBean.reservDate} ${requestScope.cBean.reservDay}
			<hr>
			
			<label>Reservation Time</label>
			 	 ${requestScope.cBean.reservTime}
			<hr>
			<c:forEach var="mList" items="${requestScope.mList}">
				<label>Member</label>
					<c:set  var="length" value="${fn:length(mList.id)}"/>
					<c:set var="substringid" value="${fn:substring(mList.id,length-4,length)}"/>
					${mList.name}(id : ****${substringid})
				<form action="ClassController.do?req=checkAttendance" id="fca" method="post">
					<input type="hidden" name="level" value="${cBean.level}">
					<input type="hidden" name="reservClass" value="${cBean.reservClass}">
					<input type="hidden" name="reservDate" value="${cBean.reservDate}">
					<input type="hidden" name="reservDay" value="${cBean.reservDay}">
					<input type="hidden" name="reservTime" value="${cBean.reservTime}">
					<input type="hidden" name="id" value="${mList.id}">
					<input type="button" id="absent" class="submit" value="결석확인" onclick="confirmAbsent(this.form,'${mList.name}','${substringid}');">
					<input type="button" id="attend" class="submit" value="출석확인" onclick="confrimAttend(this.form,'${mList.name}','${substringid}');">
				</form>
				<hr>
			</c:forEach>
		</fieldset>
	<div class="clear"></div>
	</article>
	<div class="clear"></div>
</body>
</html>