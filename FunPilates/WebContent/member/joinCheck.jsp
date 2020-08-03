<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta charset="UTF-8" />
<title>가입 여부 확인</title>
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
	   
	  if($("#ibtn").val()!="가입 가능"){
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
					url : "MemberController.do?req=joinCheck",
					data : {phoneNumber:phoneNumber},
					dataType : "text",
					success : function(data,textStatus){
						// 서블릿 클래스로부터 응답받은 메시지를 
						if(data == "waitingMember"){
							$("#message").text("*승인받은 회원입니다. 가입버튼을 눌러주세요");
							$("#ibtn").val("가입 가능");
							$("#phoneNumber").attr("readOnly",true);
							$("#join").attr("action","Main.jsp?center=member/join.jsp")
							return;
						} else if(data == "oldMember"){
							$("#message").text("*이미 가입된 회원입니다.");
							$("#ibtn").val("재가입 불요");
							$("#phoneNumber").attr("disabled",true);
						} else {
							$("#message").text("*승인받지 않은 번호입니다.");
							$("#ibtn").val("가입 불가");
							$("#phoneNumber").attr("disabled",true);
							document.fr.id.focus();
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
			$("#phoneNumber").removeAttr("disabled");
			$("#phoneNumber").removeAttr("readOnly");
			$("#ibtn").val("번호 유효성 체크");
			
		})
	});	
</script>
<body>
								<%-- About Us 좌측 목록 --%>	
<div id="sub_img_member"></div>
		<%-- -------------------------------------------------------------------- --%>	
	
<article id="aboutUs"> 
	<h1>Join Member</h1>
		* 회원권을 결제하신 분만 가입이 가능합니다.
		<form action="Main.jsp?center=member/join.jsp&title=Join" id="join" method="post" name="fr">
			<fieldset>
				<label>회원권 결제시 등록한 폰번호</label>	
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
					<input type="submit" id="sbtn" value="회원가입" class="submit">
					<input type="reset" id="rbtn" value="다시입력" class="cancel" >
				</div>
			</fieldset>
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