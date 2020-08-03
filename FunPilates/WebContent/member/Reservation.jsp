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
<script>	  
	function MemberInfo(){
		var id=${sessionScope.id};
		$.ajax({
				type : "post",
				asyn : false,
				url : "MemberController.do?req=infoForReservation",
				data : {id:id},
				dataType : "JSON",
				success : function(data, textStatus){
						if(data.memberInfo.remaining<=0){
							alert("남은 수강 횟수는 0회 입니다. 센터에 문의하세요");
							location.href='Main.jsp';
						}
						$("#no").val(data.memberInfo.no);
						$("#from").val("reservation");
						$("#phoneNumber").val(data.memberInfo.id);
						$("#id").val(data.memberInfo.id);
						$("#idForChkResv").val(data.memberInfo.id);
						$("#name").val(data.memberInfo.name);
						$("#remaining").val(data.memberInfo.remaining);
						$("#reservDate").attr("min", data.memberInfo.beginDate);
						$("#reservDate").attr("max", data.memberInfo.expDate);
						$('#level option[value="'+data.memberInfo.level+'"]').prop('selected',true);
			   }, //success : function()
				error:function(textStatus){
							console.log(textStatus)
							alert("에러가 발생했슈");
		 	   } // error :
			}); // ajax
		} //MemberInfo()	
	  
;
		
</script>
<body>
								<%-- About Us 좌측 목록 --%>	
	<jsp:include page="myPagelist.jsp"/>
		<%-- -------------------------------------------------------------------- --%>	

 	<article id="aboutUs"> 
	<h1>My Info</h1>
		<script>MemberInfo();</script>
		<fieldset id="reservationField">
			<label>구분</label>
				<input type="radio" name="sel" value="회원" checked>회원
			<hr>	
			<label>Name</label>
				<input type="text" id="name" name="name" size="30" readOnly>
			<hr>
			<label>Attendance Remaining</label>
			 	<input type="text" id="remaining" name="remaining" readOnly>
			<hr>
			
			<form action="ClassController.do?req=availableTime" id="join" method="post" name="fr">
			<label>Level</label>
			 	<select id="level" name="level" 
			 			onFocus="this.initialSelect = this.selectedIndex;" 
			 			onChange="this.selectedIndex = this.initialSelect;">
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
				 <input type="hidden" id="id" name="id">
				 <input type="submit" id="resrvBtn" name="reservBtn" value="예약가능시간">
				 ${requestScope.cBean.getReservDate()}
			<hr>
			</form>
			<form action="ClassController.do?req=reservation" id="join" method="post" name="fr">
			<label>Available Time <br> ${requestScope.cBean.getReservDate()}</label>
				<table> 
					<tr><th class="time">오전</th><th class="time">오후</th><th class="time">저녁</th></tr>
					<tr>
					<c:forEach var="atList" items="${requestScope.atList}">
						<td> ${atList} <br>
						<c:if test="${!(fn:indexOf(atList, requestScope.availableT) eq -1)}"> 

							<input type="hidden" name="level" value="${requestScope.cBean.getLevel()}">
							<input type="hidden" name="reservClass" value="${requestScope.cBean.getReservClass()}">
							<input type="hidden" name="reservDate" value="${requestScope.cBean.getReservDate()}">	
							<input type="hidden" name="reservDay" value="${requestScope.cBean.getReservDay()}">
						<c:forEach var="reservTime" items="${requestScope.reservTimeList}">
							<c:if test="${!(fn:indexOf(atList, reservTime) eq -1)}"> 
								<input type="hidden" name="reservTime" value="${reservTime}">
							</c:if>
						</c:forEach>
							<input type="hidden" name="id" value="${requestScope.cBean.getId()}">							
							<input type="submit" name="sbtn" value="예약하기">
						</c:if>
						</td> 
					</c:forEach>
					</tr>	
				</table>
		</fieldset>
		<hr>
		</form>	
	<div class="clear"></div>
	</article>
	<div class="clear"></div>
</body>
</html>