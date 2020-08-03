<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta charset="UTF-8" />
<title>Insert title here</title>
<link href="./css/subpage.css" rel="stylesheet" type="text/css">

<script src="http://code.jquery.com/jquery-latest.min.js"></script>
</head>
<body>
	<div id="sub_img"></div>
	<nav id="sub_menu">
		<ul>
			<li><a href="AboutUsController.do?req=ceoMessage">Welcome</a></li>
			<li><a href="CoachPfController.do?req=coachPfList">Coaches</a></li>
			<li><a href="AboutUsController.do?req=facilites">Facilites</a></li>
			<li><a href="AboutUsController.do?req=co_Info" id="contactus">Contact Us</a></li>
		</ul>
	</nav>
</body>
</html>