<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta charset="UTF-8" />
<title>Insert title here</title>

<script src="http://code.jquery.com/jquery-latest.min.js"></script>
</head>
<script>
 	window.onload = function(){
	    document.getElementById("sbtn").onclick = chkData;
	 }
	
	function chkData(){
		if(document.fr.name.value == ""){
	 		alert("이름을 입력하세요.");
		    document.fr.name.focus();
		    return false;
	 	}
	} 

	function MemberInfo(){
		var id=$("#id").val();
		$.ajax({
				type : "post",
				asyn : false,
				url : "MemberController.do?req=memberInfoToEditByManager",
				data : {id:id},
				dataType : "JSON",
				success : function(data, textStatus){
						$("#no").val(data.memberInfo.no);
						$("#name").val(data.memberInfo.name);
						$('#membership option[value="'+data.memberInfo.membership+'"]').prop('selected',true);
						$("#beginDate").val(data.memberInfo.beginDate);
						$("#expDate").val(data.memberInfo.expDate);
						$('#level option[value="'+data.memberInfo.level+'"]').prop('selected',true);
						$("#remaining").val(data.memberInfo.remaining);
			   }, //success : function()
				error:function(textStatus){
							console.log(textStatus)
							alert("에러가 발생했슈");
		 	   } // error :
			}); // ajax
		} // ()	  
		
		
	function WaitingMemberInfo(){
		var id=$("#id").val();
		$.ajax({
				type : "post",
				asyn : false,
				url : "MemberController.do?req=waitingMemberInfo",
				data : {id:id},
				dataType : "JSON",
				success : function(data, textStatus){
						$("#no").val(data.memberInfo.no);
						$("#name").val(data.memberInfo.name);
						$('#membership option[value="'+data.memberInfo.membership+'"]').prop('selected',true);
						$("#beginDate").val(data.memberInfo.beginDate);
						$("#expDate").val(data.memberInfo.expDate);
						$('#level option[value="'+data.memberInfo.level+'"]').prop('selected',true);
						$("#join").attr("action","MemberController.do?req=updateMemberInfoByManager&from=waitlist");
			   }, //success : function()
				error:function(textStatus){
							console.log(textStatus)
							alert("에러가 발생했슈");
		 	   } // error :
			}); // ajax
		} // ()	  	
		
			
	$('#phoneNumber_front option[value="${param.phNumberFrontCheck}"]').prop('selected',true);	

	// 삭제 확인 창
	function confirmDel(id){
		if(confirm("정말 삭제하시겠습니까?")==true){
					location.href='MemberController.do?req=delWaitingMember&id='+id;
		} else {
			return;
		}
	} // confirmDel()	
		
</script>
<body>
								<%-- About Us 좌측 목록 --%>	
	<jsp:include page="ManagementList.jsp"/>
		<%-- -------------------------------------------------------------------- --%>	

<article id="aboutUs"> 
	<h1>Update Member Info</h1>
		<form action="MemberController.do?req=updateMemberInfoByManager&from=member" id="join" method="post" name="fr">
		<fieldset>
			<label>구분</label>
				<input type="radio" name="sel" value="회원" checked>회원
			<hr>	
			<label>MobilePhoneNumber</label>	
				<select id="phoneNumber_front" name="phoneNumber_front"
				onFocus="this.initialSelect = this.selectedIndex;" onChange="this.selectedIndex = this.initialSelect;">
					<option value="010">010</option>
					<option value="011">011</option>
					<option value="016">016</option>
					<option value="017">017</option>
					<option value="019">019</option>
				</select>
				<input type="text" id="phoneNumber" name="phoneNumber" value="${param.phNumberCheck}" size="30" ReadOnly>
				<input type="text" id="newPhoneNumber" name="newPhoneNumber" placeholder="번호가 바뀌었으면 입력하세요">
			<hr>
			<label>User ID</label>
				<input type="text" id="id" name="id" class="id" placeholder="자동입력됩니다." value="${param.phNumberCheck}" size="30">
			<hr>
			<label>Name</label>
				<input type="text" id="name" name="name" size="30">
			<hr>
			<label>Membership</label> 
				<select id="membership" name="membership">
					<option value="10">10회</option>
					<option value="30">30회</option>
					<option value="50">50회</option>
					<option value="100">100회</option>
				</select><br><br>
			<hr>	
			<label>Begin Date</label>
				 <input type="date" id="beginDate" name="beginDate">
			<hr>	
			<label>Exp. Date</label>
			 	<input type="date" id="expDate" name="expDate">
			<hr>
			<label>Level</label>
			 	<select id="level" name="level">
					<option value="basic">초급</option>
					<option value="intermediate">중급</option>
					<option value="advanced">고급</option>
				</select><br><br>
			
			<hr>	
		<c:if test="${param.from eq 'member'}">
			<label>Attendance Remaining</label>
			 	<input type="text" id="remaining" name="remaining">
			<hr>
		</c:if>
			
		</fieldset>
		<div class="clear"></div>
		
		<c:choose>
		<c:when test="${param.from eq 'member'}">
			<script>MemberInfo();</script>
		</c:when>
		
		<c:when test="${param.from eq 'waitlist'}">
			<script>WaitingMemberInfo();</script>
		</c:when>
		</c:choose>

		<div id="buttons">
			<input type="hidden" id="no" name="no">
			<input type="submit" id="sbtn" value="수 정" class="submit">
			<input type="button" id="dbtn" value="삭 제" class="submit" onclick="confirmDel(${param.phNumberCheck});">
			<input type="button" id="cbtn" value="취  소" class="submit" onclick="history.back()">
		</div>

	</form>
	</article>
	<div class="clear"></div>
</body>
<script>
	$("input:text[numberOnly]").on("keyup", function() {
	    $(this).val($(this).val().replace(/[^0-9]/g,""));
	});

	$("#phoneNumber").keyup(function(){
		$("#id").val($(this).val());
	});
</script>
</html>