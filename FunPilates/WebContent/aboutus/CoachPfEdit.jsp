<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta charset="UTF-8" />
<title>Coach Pf Edit</title>
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
	<c:choose>
	<c:when test="${empty coachPf.coachNo}"> 
	<h1>New Coach's Profile</h1>
	</c:when>
	<c:otherwise>
	<h1>Coach's Profile Edit</h1>
	</c:otherwise>
	</c:choose>
		<form action="CoachPfController.do?req=addOrupdateCoachPf" enctype="multipart/form-data" method="post">
			<c:set var ="coachPf" value="${requestScope.coachPf}"/>
			<table>
				<tr>
					<td align="right">이름(필수):</td>
					<td><input type="text" id="coachName" name="coachName" value="${coachPf.coachName}"></td>
				</tr>
				<tr>
					<td align="right">예명(필수):</td>
					<td><input type="text" name="coachNickName" value="${coachPf.coachNickName}"></td>
				</tr>			
				<tr>
					<td></td>
					<td>*예명이 없으면 본명으로 표시 됩니다.</td>
				</tr>
				<tr>
					<td align="right">경력(선택):</td>
					<td><textarea rows=10" cols="70" name="career" width=162>${fn:replace(coachPf.career,br,crcn)}</textarea></td>
				</tr>
				<tr>
					<td align="right">프로필 사진(필수): </td>
					<td><input type="file" id="updatePfImg" name="updatePfImg"/>
						<input type="hidden" name="pfImg" value="${coachPf.pfImg}"/>
					</td>
				</tr>			 
				<c:choose>
				<c:when test="${empty coachPf.coachNo}">
				<tr>
					<td><input type="hidden" name="coachNo" value="add"></td>
					<td align="right"><input type="button" id="button" value="추가 완료" onclick="Check(this.form);"/></td>		
				</tr>	
				</c:when>
				<c:otherwise>
				<tr><td colspan="2" align="right">*업로드 파일이 없으면 기존 이미지로 유지됩니다.</td></tr>
				<tr>
					<td><input type="hidden" name="coachNo" value="${coachPf.coachNo}">
						<input type="hidden" name="className" value="pilates">
						<input type="hidden" name="position" value="필라테스 코치">
					</td>
					<td align="right"><input type="button" id="button" value="수정 완료" onclick="Check(this.form);"></td>
				</tr>
				</c:otherwise>
				</c:choose>	
				</tr>
				
		    </table>	
		</form>
	</article>
	<div class="clear"></div>
</body>

<script>
		// 프로필 사진 누락시 "업로드 되지 않았습니다." <--- 경고 메시지 창을 띄워주고
		// 사용자가 업로드 시 모든 파일을 선택할 수 있게 유도 해야 함.
		function Check(f){
			if(f.button.value=="추가 완료"){
				// 비어있는 정보에 대하여 경고 메시지
				if(f.coachName.value==""){
					alert("이름을 입력해주세요");
					f.coachName.focus();
					return;
				} else if(f.updatePfImg.value==""){
					alert("프로필 사진을 업로드 해주세요");
					f.pfImg.focus();
					return;
				}
			} else {
				if(f.coachName.value==""){
					alert("이름을 입력해주세요");
					f.coachName.focus();
					return;
				}
			}	
			// form 전송 요청
			f.submit();
		} // Check(); 
</script>	
</html>