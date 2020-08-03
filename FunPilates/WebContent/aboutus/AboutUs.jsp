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

<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=cb60b05bdbf5b25e898673908bbd1746&libraries=services"></script>

<script>
	  
	// 코치 프로필 삭제 확인 창
	function confirmDel(f){
		if(confirm("정말 삭제하시겠습니까?")==true){
			f.submit();
		} else {
			return;
		}
	} // confirmDel()
	
	//지도 생성 및 회사 위치 표시 : Kakao Maps API 사용
	function ContactUs_Map(){  	
		var container = document.getElementById('contactus_map'),
	    mapOption = {
			center: new kakao.maps.LatLng(33.450701, 126.570667),
			level: 3
	    };
	
		// 지도 생성
		var map = new kakao.maps.Map(container, mapOption);
		
		// 주소 변환 객체
		var geocoder = new kakao.maps.services.Geocoder();	
		
		// 주소로 좌표 검색
		geocoder.addressSearch("${co_Info.co_RoadAddress}", function(result, status){
			
			if(status==kakao.maps.services.Status.OK){
				var coords = new kakao.maps.LatLng(result[0].y, result[0].x);
		
				// 위치를 마커로 표시
				var marker = new kakao.maps.Marker({
					map: map,
		            position: coords
		        });
				
				// 장소 설명창
				var infowindow = new kakao.maps.InfoWindow({
					content: '<div style="width:150px; text-align:center; padding:6px 0;">${co_Info.co_Name}</div>',
					removable:true
				});
				
				infowindow.open(map, marker);
				
				// 지도 중심으로 위치 보내기
				map.setCenter(coords);
			} // if
		}); // addressSearch();	

	} // ContactUs_Map()	
</script>

</head>

<body>
								<%-- About Us 좌측 목록 --%>	
	<jsp:include page="AboutUs_list.jsp"/>
		<%-- -------------------------------------------------------------------- --%>	
	
	<article id="aboutUs">
		<c:set var="subcenter" value="${param.subcenter}"/>	
		<c:choose>
		<%-- -------------------------------------------------------------------- --%>
								<%-- About Us main 화면 및 Wecome 클릭 -> CEO 인삿말 --%>	
		<c:when test="${subcenter=='ceoMessage'}">
		<h1>Welcome</h1>
			<form action="AboutUsController.do?req=ceoMessageToEdit" method="post">
				<c:set var ="ceoMessage" value="${requestScope.ceoMessage}"/>
					<figure class="ceo"><img src="./images/ceo/${ceoMessage.ceoImg}" width="196" height="226" padding="20" alt="CEO사진">
						<figcaption>Fun Gym CEO|${ceoMessage.ceoName}</figcaption>
					</figure>
					
					<p>${ceoMessage.ceoMessage} </p>
				
				<c:if test="${sessionScope.id==94105678}">
					<div>
						<input type="submit" value="CEO인사말 수정" class="btn"/>
					</div>
				</c:if>
			</form>
		</c:when>

		<%-- -------------------------------------------------------------------- --%>
								<%-- Coaches 클릭 -> Coaches 소개 --%>
		<c:when test="${subcenter=='coaches'}">
		<h1>Coaches|코치</h1>						
				<table id="pilatesCoach">
 					<c:set var="j" value="0"/>
					<c:forEach var="list" items="${requestScope.coachPfList}">
						<c:if test="${j%3==0}">
							<tr align="center">
						</c:if> 
							<td align='center'>
								<img src='./images/coaches/${list.pfImg}' width='180' height='220'>
								<hr>
								${list.position} | ${list.coachNickName}				
							  	<br>
							 <c:if test="${sessionScope.id==94105678}">
								<form action="CoachPfController.do?req=coachPfToEdit" method="post"> 
							 		<input type='hidden' name='coachNo' value="${list.coachNo}">
			 	  					<input type='submit' value='수정' style='float:left;'> 
								</form>
								<form action="CoachPfController.do?req=coachPfToDel" method="post">
									<input type='hidden' name='coachNo' value="${list.coachNo}">
									<input type='button' value='삭제' onclick='confirmDel(this.form);' style='float:left;'>
								</form>	
							</c:if>
						 	</td>
						 <c:if test="${j%3==2}">
							</tr>
						</c:if> 	
						 	<div class="clear"></div>
					<c:set var="j" value="${j+1}"/>	 		
					</c:forEach>						
				</table>
					
				<div class="clear"></div>
				<%-- -------------------------- --%>
				<hr>
				<c:if test="${sessionScope.id==94105678}">
					<div>
						<input type="button" value="코치 프로필 추가" class="btn" 
			 	  			onclick="location.href='Main.jsp?center=aboutus/CoachPfEdit.jsp&title=New Coach\'s Profile';"> 
					</div>
				</c:if>				
			</form>
		</c:when>		
		<%-- -------------------------------------------------------------------- --%>
							<%-- Facilites 클릭 -> Facilites 소개 --%>
		<c:when test="${subcenter =='facilites'}">	
			<link rel="stylesheet" href="https://cdn.jsdelivr.net/bxslider/4.2.12/jquery.bxslider.css">
			<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
			<script src="https://cdn.jsdelivr.net/bxslider/4.2.12/jquery.bxslider.min.js"></script>
			
			<script type="text/javascript">
			//<![CDATA[var j = $.noConflict(true);
			$(document).ready(function(){
				    $('.bxslider').bxSlider({
				    	maxSlides:2,
				    	minSlides:2,
				    	auto:true,
				    	speed:500,
				    	pause:3000,
				    	mode:'fade',
				    	autoControls:true,
				    	pager:true,
				    	captions:true,
				    });
				  });
			 //]]> 
			</script>
		
			<h1>Facilites|시설</h1>
			<form action="AboutUsController.do?req=facilitesToEdit" method="post">
				<c:set var ="facilites" value="${requestScope.facilites}"/>
				
				<p aligin="center">${facilites.facilitesExplain} </p>
				
					<ul class="bxslider">
						<li align="ceneter">
							<img src="./images/facilites/${facilites.facilitesImg1}" align="ceneter"
							 width="650" height="250" title="${facilites.facilitesImg1cap}">
						</li>
						<li>
							<img src="./images/facilites/${facilites.facilitesImg2}" 
							 width="650" height="250" title="${facilites.facilitesImg2cap}">
						</li>
						<li>
							<img src="./images/facilites/${facilites.facilitesImg3}" 
							 width="650" height="250" title="${facilites.facilitesImg3cap}">
						</li>					
					</ul>
				
				<c:if test="${sessionScope.id==94105678}">
					<div>
						<input type="submit" value="시설 수정" class="btn"/>
					</div>
				</c:if>
			</form>

		</c:when>
		<%-- -------------------------------------------------------------------- --%>
							<%-- Contact Us 클릭 -> 위치 안내 --%>
		<c:when test="${subcenter =='contactUs'}">
		<c:set var ="co_Info" value="${requestScope.co_Info}"/>
		<h1>Contact Us</h1>
			<table>
			<tr><td>전     화 :</td><td>${co_Info.co_Tel}</td></tr>
			<tr><td>F a x :</td><td>${co_Info.co_Fax}</td></tr>
			<tr><td>Email :</td><td>${co_Info.co_Email} 
							        <!-- <input type="button" value="메일보내기" onclick="location.href='Main.jsp?center=aboutus/SendMail.jsp'"> -->
							</td></tr>
			<tr><td>위     치 :</td><td>${co_Info.co_RoadAddress} ${co_Info.co_DetailAddress}</td></tr>
			</table>
			<div id="contactus_map" style="width:400px; height:300px;"></div>
			<script>ContactUs_Map();</script>
			<br>
			
	
			
		<c:if test="${sessionScope.id==94105678}">
				<div>
					<input type="button" value="수정" class="btn" 
					       onClick="location.href='AboutUsController.do?req=co_InfoToEdit'"/>
				</div>
		</c:if>
		</c:when>
		</c:choose>
	<div class="clear"></div>
	</article>
	<div class="clear"></div>
</body>
</html>