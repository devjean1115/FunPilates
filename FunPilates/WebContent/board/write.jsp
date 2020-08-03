<%@page import="org.apache.taglibs.standard.lang.jstl.test.PageContextImpl"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 추가 및 수정</title>
<link href="./css/subpage.css" rel="stylesheet" type="text/css">
</head>
<body>
	<%	// form에서 요청한 데이터 한글 처리
	   request.setCharacterEncoding("UTF-8");
	   pageContext.setAttribute("crcn", "\r\n");
	   pageContext.setAttribute("br", "<br/>");
	%>


								<%-- Q&A 좌측 목록 --%>	
	<jsp:include page="Boardlist.jsp"/>
		<%-- -------------------------------------------------------------------- --%>	

<article>
	<h1>Q & A Write</h1>
	<%--새글 정보를 입력 한후 또는 글을 수정한 뒤 업로드 요청을 함 --%>
	<c:choose>
	<c:when test="${!empty bBean.getNum()}">
		<form action="BoardController.do?req=updateQ" method="post">
	</c:when>
	<c:otherwise>	
		<form action="BoardController.do?req=addQ" method="post">
	</c:otherwise>
	</c:choose>
		<table id="writeqna">
			<tr>
				<td>작성자</td>
				<c:choose>
					<c:when test="${!empty sessionScope.id}">
						<td><input type="text" name="id" value="${sessionScope.id}" readOnly>
							* 익명으로 기재됩니다. 단 회원이신 분은 내가 쓴 글로 확인이 가능합니다.
						</td>
					</c:when>
					<c:otherwise>
						<td><input type="text" text-align="center" name="id" value="익명" readOnly></td>
					</c:otherwise>
				</c:choose>
			</tr>
			<tr>
				<td>문의제목</td>
				<td><input type="text" name="subject" value="${bBean.getSubject()}"></td>
			</tr>	
			<tr>
				<td>비밀번호</td>
				<td><input type="password" name="password" value="${bBean.getPassword()}"> * 비밀번호 미입력시 공개글로 게시 됩니다. <br>
				</td>
			<tr>
			<td colspan=2>
			* 회원이 아니신 분이 공개글로 올리실경우 직접 수정 및 삭제가 불가하며, 관리자가 필요한 경우 임의 조치합니다. </td>
			<td>
			</tr>
			<tr>
				<td>글내용</td>
				<td><textarea name="content" rows="13" cols="40">${fn:replace(bBean.getContent(),br,crcn)}</textarea> </td>
			</tr>				
		</table>
		<div id="table_search">
			<input type="hidden" name="status" value="답변대기">
			<input type="hidden" name="num" value="${bBean.getNum()}">
			<input type="submit" value="작성완료" class="btn">
			<input type="reset" value="다시작성" class="btn">
						
			<input type="button" value="글목록" class="btn" onclick="location.href='BoardController.do?req=qna'">				
		</div>
	</form>
	<div class="clear"></div>
	<div id="page_control"></div>

	<div class="clear"></div>	
	</article>
	<div class="clear"></div>

</body>
</html>