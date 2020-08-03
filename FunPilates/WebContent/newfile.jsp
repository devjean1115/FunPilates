<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta charset="UTF-8" />
<title>About Us</title>
<link href="./css/subpage.css" rel="stylesheet" type="text/css">
<link href="./css/default.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/bxslider/4.2.12/jquery.bxslider.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script src="https://cdn.jsdelivr.net/bxslider/4.2.12/jquery.bxslider.min.js"></script>
<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=cb60b05bdbf5b25e898673908bbd1746&libraries=services"></script>

</head>

<body>
							<%-- Facilites 클릭 -> Facilites 소개 --%>

		<h1>Facilites|시설</h1>
			<h2>필라테스룸</h2>
			<form action="AboutUsController.do?req=facilitesToEdit" method="post">
				<c:set var ="facilites" value="${requestScope.facilites}"/>
					<ul class="bxslider">
						<li>
							<img src="./images/facilites/${facilites.facilitesImg1}" 
							 width="300" height="200" title="${facilites.facilitesImg1cap}">
						</li>
						<li>
							<img src="./images/facilites/${facilites.facilitesImg2}" 
							 width="300" height="200" title="${facilites.facilitesImg2cap}">
						</li>
						<li>
							<img src="./images/facilites/${facilites.facilitesImg3}" 
							 width="300" height="200" title="${facilites.facilitesImg3cap}">
						</li>					
					</ul>
				
					<p>${facilites.facilitesExplain} </p>
				
				<c:if test="${sessionScope.id==94105678}">
					<div>
						<input type="submit" value="시설 수정" class="btn"/>
					</div>
				</c:if>
			</form>
			<script type="text/javascript"> 
			var j = $.noConflict(true);
			j(document).ready(function(){
				    j('.bxslider').bxSlider({
				    	auto:false,
				    	speed:500,
				    	pause:5000,
				    	mode:'fade',
				    	autoControls:true,
				    	pager:true,
				    	captions:true,
				    });
				  });
			</script>


</html>