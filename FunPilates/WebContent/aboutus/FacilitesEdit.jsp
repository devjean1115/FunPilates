<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta charset="UTF-8" />
<title>Facilites Edit</title>
<link href="./css/subpage.css" rel="stylesheet" type="text/css">

<script src="http://code.jquery.com/jquery-latest.min.js"></script>
</head>
<body>
	<%	// form에서 요청한 데이터 한글 처리
	   request.setCharacterEncoding("UTF-8");
	   pageContext.setAttribute("crcn", "\r\n");
	   pageContext.setAttribute("br", "<br/>");
	%>
	 
								<%-- About Us 좌측 목록 --%>	
	<jsp:include page="AboutUs_list.jsp"/>
		<%-- -------------------------------------------------------------------- --%>	
	<article id="aboutUs"> 
	<h1>Facilites Edit</h1>
	<form action="AboutUsController.do?req=updateFacilites" enctype="multipart/form-data" method="post">
		<c:set var ="ceoMessage" value="${requestScope.facilites}"/>
		*업로드 파일이 없으면 기존 이미지로 유지됩니다.
		<table>
			<tr>
				<td align="right" width="40">사진:<br> <br></td>
				<td width="125"><input type="file" name="updateFacilitesImg1"> <br> 
								<input type="text" name="facilitesImg1" value="${facilites.facilitesImg1}">
				</td>
				<td align="right" widt=:40">시설명:<br> <br></td>
				<td width="125"><input type="text" name="facilitesImg1cap" value="${facilites.facilitesImg1cap}">
				</td>
			</tr>
			<tr>
				<td align="right" width="40">사진:<br> <br></td>
				<td width="125"><input type="file" name="updateFacilitesImg2"> <br> 
								<input type="text" name="facilitesImg2" value="${facilites.facilitesImg2}">
				</td>
				<td align="right" widt=:40">시설명:<br> <br></td>
				<td width="125"><input type="text" name="facilitesImg2cap" value="${facilites.facilitesImg2cap}">
				</td>
			</tr>
			<tr>
				<td align="right" width="40">사진:<br> <br></td>
				<td width="125"><input type="file" name="updateFacilitesImg3"> <br> 
								<input type="text" name="facilitesImg3" value="${facilites.facilitesImg3}">
				</td>
				<td align="right" widt=:40">시설명:<br> <br></td>
				<td width="125"><input type="text" name="facilitesImg3cap" value="${facilites.facilitesImg3cap}">
				</td>
			</tr>
			<tr>
				<td align="right">시설 소개란<td>
			</tr>
			<tr>	
				<td></td>
				<td colspan="4"><textarea rows="10" cols="70" id="ceoMessage" name="facilitesExplain" width=160>${fn:replace(facilites.facilitesExplain,br,crcn)}</textarea></td>
			</tr>
			<tr>
				<td colspan="2" align="right"><input type="submit" id="button" value="수정 완료" ></td>
			</tr>	
	    </table>	
	</form>
	
	<script type="text/javascript">
	// 프로필 사진 누락시 "업로드 되지 않았습니다." <--- 경고 메시지 창을 띄워주고
	// 사용자가 업로드 시 모든 파일을 선택할 수 있게 유도 해야 함.
	function Check(f){
			if(f.ceoName.value==""){
				alert("ceo이름을 입력해주세요");
				f.coachName.focus();
				return;
			} else if(f.ceoMessage.value==""){
				alert("인삿말 내용을 작성해주세요");
				f.ceoMessage.focus();
				return;
			}
		// form 전송 요청
		f.submit();
	} // Check(); 
	</script>
	</article>
	<div class="clear"></div>
</body>
</html>