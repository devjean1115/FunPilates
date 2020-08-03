<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta charset="UTF-8" />
<title>Ceo Message Edit</title>
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
	<h1>WelCome Edit</h1>
	<form action="AboutUsController.do?req=updateCeoMessage" enctype="multipart/form-data" method="post">
		<c:set var ="ceoMessage" value="${requestScope.ceoMessage}"/>
		<table>
			<tr>
				<td align="right" width="40">이름:</td>
				<td width="125"><input type="text" id="ceoName" name="ceoName" value="${ceoMessage.ceoName}"></td>
				
			</tr>
			<tr>
				<td align="right" width="40">사진:<br> <br></td>
				<td width="125"><input type="file" name="updateCeoImg"> <br> *업로드 파일이 없으면 기존 이미지로 유지됩니다.
								<input type="hidden" name="ceoImg" value="${ceoMessage.ceoImg}">
				</td>
			</tr>
			<tr>
				<td align="right">인삿말<td>
			</tr>
			<tr>	
				<td></td>
				<td colspan="2"><textarea rows="10" cols="70" id="ceoMessage" name="ceoMessage" width=160>${fn:replace(ceoMessage.ceoMessage,br,crcn)}</textarea></td>
			</tr>
			<tr>
				<td colspan="2" align="right"><input type="button" id="button" value="수정 완료" onclick="Check(this.form);"></td>
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