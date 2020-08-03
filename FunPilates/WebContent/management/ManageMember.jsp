<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta charset="UTF-8" />
<title>관리자 페이지</title>
<link href="./css/default.css" rel="stylesheet" type="text/css">
<link href="./css/subpage.css" rel="stylesheet" type="text/css">

<script src="http://code.jquery.com/jquery-latest.min.js"></script>
</head>
<script type="text/javascript">
	window.onload = function(){
	      document.getElementById("sbtn").onclick = chkData;
	   }

	    function chkData(){
	   if(document.fr.phoneNumber.value == ""){
	   		alert("전화번호를 입력하세요.");
	 	    document.fr.phoneNumber.focus();
	 	    return false;
	   }
	   
	  if($("#ibtn").val()=="번호 유효성 체크"){
		 alert("유효성 체크가 완료되지 않았습니다.");
		 return false;
	   }
	}		
	
	function fn_process(){
		if($("#phoneNumber").val()!=""){
			var phoneNumber = $("#phoneNumber_front").val()+$("#phoneNumber").val();
				$.ajax({
					type : "post",
					async : false,
					url : "MemberController.do?req=memberCheck",
					data : {phoneNumber:phoneNumber},
					dataType : "text",
					success : function(data,textStatus){
						if(data == "new"){
							$("#message").text("*새로운 회원입니다.");
							$("#ibtn").val("새로운 회원");
							$("#sbtn").val("대기 리스트에 추가");
							$("#phoneNumber").attr("readOnly",true);
							document.fr.id.focus();
							return;
						} else if(data == "oldMember") {
							$("#message").text("*기존 회원입니다.");
							$("#ibtn").val("기존 회원");
							$("#sbtn").val("회원 정보 수정");
							$("#phoneNumber").attr("readOnly",true);
							$("#from").val("member");
							$("#join").attr("action","Main.jsp?center=management/UpdateMemberInfo.jsp&title=Update Member");
							return;
						} else if(data == "waitingMember") {
							$("#message").text("*가입대기 회원입니다.");
							$("#ibtn").val("가입대기 회원");
							$("#sbtn").val("등록 정보 수정");
							$("#phoneNumber").attr("readOnly",true);
							$("#from").val("waitlist");
							$("#join").attr("action","Main.jsp?center=management/UpdateMemberInfo.jsp&title=Waiting Member Info");
							return;
						}
					}, 
					error:function(data,textStatus){
						console.log(data);
						alert("에러가 발생했슈");
					}
				}); // $ajax()
		} // if
	} // fn_process()
	
	$(function (){
		$("#rbtn").click(function(){
			$("#phoneNumber").removeAttr("readOnly");
			$("#ibtn").val("번호 유효성 체크");
		})
	});	
</script>
<body>
								<%-- About Us 좌측 목록 --%>	
	<jsp:include page="ManagementList.jsp"/>
		<%-- -------------------------------------------------------------------- --%>	
	
<article id="aboutUs"> 
	<h1>Manage Member</h1>
		<form action="Main.jsp?center=management/AddWaitingMember.jsp&title=Update Wait List" id="join" method="post" name="fr">
		<fieldset>
			<label>추가 또는 변경할 폰번호</label>	
				<select id="phoneNumber_front" name="phNumberFrontCheck">
					<option value="010">010</option>
					<option value="011">011</option>
					<option value="016">016</option>
					<option value="017">017</option>
					<option value="019">019</option>
				</select>
				<input type="text" id="phoneNumber" name="phNumberCheck" placeholder="숫자만 입력하세요 ex)45458282" size="30" numberOnly >
				<input type="button" id="ibtn" value="번호 유효성 체크" class="dup" onclick="fn_process()"> <br>
				<div id="message"></div>
				
		<div class="clear"></div>
		<div id="buttons">
			<input type="hidden" id="from" name="from">
			<input type="submit" id="sbtn" value="정보입력" class="submit">
			<input type="reset" id="rbtn" value="다시입력" class="cancel" >
		</div>
	</form>
	</article>
	<div class="clear"></div>
<div class="clear"></div>	
</article>
<div class="clear"></div>
	
</body>

<script>
	$("input:text[numberOnly]").on("keyup", function() {
	    $(this).val($(this).val().replace(/[^0-9]/g,""));
	});
</script>
</html>