<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta charset="UTF-8" />
<title>로그인</title>
<link href="./css/default.css" rel="stylesheet" type="text/css">
<link href="./css/subpage.css" rel="stylesheet" type="text/css">
</head>
<body>
<div id="sub_img_member"></div>

<article>
	<h1>Login</h1>
	
	<%--loginPro.jsp서버페이지로 로그인 요청을 위해 아래에 아이디와 비밀번호 입력후 로그인버튼을 클릭함 --%>
	<form action="MemberController.do?req=login" id="join" method="post">
		<fieldset>
			<legend>Login Info</legend>
			<label>User ID</label>
			<input type="text" name="id" placeholder="ID는 전화번호 뒷자리입니다."><br>
			<label>Password</label>
			<input type="password" name="password"><br>
		</fieldset>
		<div class="clear"></div>
		<div id="buttons">
			<input type="submit" value="로그인" class="submit">
			<input type="reset" value="다시작성" class="cancel">
		</div>
	</form>
</article>

<div class="clear"></div>
</body>
</html>

<script>
	if("${requestScope.userCheck}"=="idError"){
			window.alert('아이디 에러');
			$("#id").focus();
	} else if("${requestScope.userCheck}"=="passwordError"){
	   		window.alert('비밀번호 에러');
	   		$("#id").focus();		
	} 
</script>