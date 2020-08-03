<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta charset="UTF-8" />
<title>회원가입</title>
<link href="./css/default.css" rel="stylesheet" type="text/css">
<link href="./css/subpage.css" rel="stylesheet" type="text/css">

<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script type="text/javascript">

	window.onload = function(){
	   document.getElementById("sbtn").onclick = chkData;
	      
	   if(document.fr.password.value == ""){
	      alert("비밀번호를 입력하세요.");
	      document.fr.password.focus();
	      return false;
	   }
	   
	   if(document.fr.password.value != document.fr.password2.value){
		      alert("비밀번호가 맞지 않습니다..");
		      document.fr.password.focus();
		      return false;
		   }
	   
	  if($("#ibtn").val()!="승인 완료"){
		 alert("중복체크하세요")
		 document.fr.ibtn.focus();
		 return false;
	   }
	}		
	
	function fn_process(){
		var phoneNumber = $("#phoneNumber_front").val()+$("#phoneNumber").val();
			$.ajax({
				type : "post",
				async : false,
				url : "MemberController.do?req=memberInfoToJoin",
				data : {phoneNumber:phoneNumber, sel:sel},
				dataType : "text",
				success : function(data,textStatus){
					// 서블릿 클래스로부터 응답받은 메시지를 
					if(data == "notPermitted"){
						$("#message").text("*승인받은 아이디가 아닙니다. *앞번호도 확인해보세요~");
						document.fr.id.focus();
						return;
					} else if(data == "typeMismatch") {
						$("#message").text("*가입 구분이 잘못됐습니다.");
						document.fr.sel.focus();
						return;
					} else{
						var name = data;
						$("#message").text("*승인 받은 아이디입니다.");
						$("#ibtn").val("승인 완료");
						$("#name").val(name);
						return;
					}
				}, 
				error:function(data,textStatus){
					console.log(data);
					alert("에러가 발생했슈");
				}
			}); // $ajax()
	
	} // fn_process()	
</script>	

</head>
<body>
<div id="sub_img_member"></div>

<article>
	<h1>Join Us</h1>
	<p>*FunGym에 오셔서 회원권을 결제하신 분만 가입이 가능합니다.</p>
	<%-- 회원가입을 위해 아래의 디자인에서 가입할 회원정보 입력받고 회원가입버튼을 클릭했을 때...
		joinPro.jsp서버페이지로 회원가입 요청을 한다.  
	 --%>
	<form action="MemberController.do?req=join" id="join" method="post" name="fr">
		<fieldset>
			<label>구분</label>
				<input type="radio" name="sel" value="회원" checked>회원
			<label>MobilePhoneNumber</label>	
				<select id="phoneNumber_front" name="phoneNumber_front">
					<option value="010">010</option>
					<option value="011">011</option>
					<option value="016">016</option>
					<option value="017">017</option>
					<option value="019">019</option>
				</select>
				<input type="text" id="phoneNumber" name="phoneNumber" placeholder="숫자만 입력하세요 ex)45458282" size="30" numberOnly >
				<input type="button" id="ibtn" value="번호 유효성 체크" class="dup" onclick="fn_process()"> <br>
			<label></label>
			<div id=message></div> <br>
			<label>User ID</label>
				<input type="text" id="id" name="id" class="id" placeholder="자동입력됩니다." size="30" readOnly><br>
			<label>Name</label>
				<input type="text" id="name" name="name" placeholder="유효성 체크 후 자동입력됩니다." size="30" readOnly><br> 
			<label>Password</label>
				<input type="password" id="password" name="password" size="30"><br>
			<label>Retype Password</label>
				<input type="password" id="password2" name="password2" size="30"><br>
		</fieldset>
				
		<div class="clear"></div>
		<div id="buttons">
			<input type="submit" id="sbtn" value="회원가입" class="submit">
			<input type="reset" value="다시입력" class="cancel">
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