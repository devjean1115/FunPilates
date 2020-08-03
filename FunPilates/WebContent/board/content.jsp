<%@page import="java.sql.Timestamp"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="db.BoardBean"%>
<%@page import="java.util.List"%>
<%@page import="db.BoardDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link href="./css/board.css" rel="stylesheet" type="text/css">
</head>
 <script type="text/javascript">  	
	// 삭제 확인 창
	function confirmDel(type, reqNum){
		if(confirm("정말 삭제하시겠습니까?")==true){
			switch(type){
				case "delQ" :
					location.href='BoardController.do?req=delQ&num='+reqNum;
				break;
				
				case "delA" :
					location.href='BoardController.do?req=delA&num='+reqNum;
				break;
			}	
		} else {
			return;
		}
	} // confirmDel()

 </script>

<body>
	<%	// form에서 요청한 데이터 한글 처리
	   request.setCharacterEncoding("UTF-8");
	   pageContext.setAttribute("crcn", "\r\n");
	   pageContext.setAttribute("br", "<br/>");
	%>


								<%-- Q&A 좌측 목록 --%>	
	<jsp:include page="Boardlist.jsp"/>
		<%-- -------------------------------------------------------------------- --%>	
	
<article id="QnA">

<h1>Q & A Content</h1>
<table id="boardQnQ">
	<c:set var="qContent" value="${requestScope.bBean}"/>
	<tr><th class="tno">No.</th>
		<th class="tstatus">Status</th>
	    <th class="ttitle">Title</th>
	    <th class="twriter">Writer</th>
	    <th class="tdate">Date</th>
	</tr>
	<tr>
		<td id="tdNum">${qContent.getNum()}</td>
		<td id="tdStatus">${qContent.getStatus()}</td>
		<td class="left"> 
			<c:if test="${empty qContent.getPassword()}">
		[공개]  
			</c:if>
			<c:if test="${!empty qContent.getPassword()}">
		[비공개]  
			</c:if>
		${qContent.getSubject()}
		</td>
		<td id="tdWriter">익명</td>
		<td id="tdDate">${qContent.getDate()}</td>
	</tr>	
	<tr>
		<th colspan="5" class="tcontent">Q U E S T I O N</th>
	</tr>
	<tr>	
		<td colspan="5">${qContent.getContent()}</td> <hr>
	</tr>
	<tr>
		<td colspan="5"></td>
	</tr>
	<c:if test="${qContent.getAnswer()!=''}">
	<tr><td colspan="5"></td></tr>
	<tr><th colspan="4" class="ta">A N S W E R</th>
		<th colsapn="1" class="taDate" >Date</th>
	</tr>
	<tr><td colspan="4">${qContent.getAnswer()}</td>
		<td colsapn="1" id="taDate">${qContent.getAnswerDate()}</td>
	</tr>
	</c:if>
	</table>
	
	
	<c:if test="${sessionScope.sel=='관리자'}">
	<form action="BoardController.do?req=updateA&num=${qContent.getNum()}" method="post">
	<hr>
	<table>	<!-- 코치가 달 수도 있겠다 -->
	<tr>
		<td colspan="5"><textarea id="replyarea" name="answer">${fn:replace(qContent.getAnswer(),br,crcn)}</textarea></td>
	</tr>
	<tr>
		<c:choose>
		<c:when test="${qContent.getAnswer()==''}">	
		<td colspan="3"><input type="submit" value="답변달기" class="btn">
		</td>
		</c:when>
		<c:otherwise>
		<td colspan="3"><input type="submit" value="답변수정" class="btn">		   
		</td>
		</c:otherwise>
		</c:choose>
		</from>
		<td colspan="2">
		<input type="button" value="답변삭제" class="btn" 
		   onclick="confirmDel('delA',${qContent.getNum()});">
		</td>
	</tr>
	</table>
	</c:if>
	

<hr>

<div id="table_search">
<c:choose>
<%-- 공개글 : 수정은 작성자이자 회원이 글상태가 작성대기중일때만 가능.삭제는 작성자이자회원과 관리자만 가능--%>
<c:when test="${empty qContent.getPassword()}">
	<c:if test="${sessionScope.id==qContent.getId() && qContent.getStatus()=='답변대기'}" >
		<input type="button" 
			   value="글수정" 
			   class="btn" 
			   onclick="location.href='BoardController.do?req=qNaContentToEdit&num=${qContent.getNum()}';">		   
	</c:if>
	<c:if test="${sessionScope.id==qContent.getId() || sessionScope.sel=='관리자'}"> 
		<input type="button" 
			   value="글삭제" 
			   class="btn" 
			   onclick="confirmDel('delQ',${qContent.getNum()});">
	</c:if>
</c:when>
<%-- 비공개글 : 수정은 작성자가 글상태가 작성대기중일때만 가능.삭제는 비공개글에 들어온 사람은 가능--%>
<c:otherwise>
	<c:if test="${sessionScope.sel!='관리자' && qContent.getStatus()=='답변대기'}"> 
		<input type="button" 
			   value="글수정" 
			   class="btn" 
			   onclick="location.href='BoardController.do?req=qNaContentToEdit&num=${qContent.getNum()}';">	
	</c:if>	 
		<input type="button" 
			   value="글삭제" 
			   class="btn" 
			   onclick="confirmDel('delQ',${qContent.getNum()});">
</c:otherwise>
</c:choose>
	<input type="button" value="목록보기" class="btn" onclick="location.href='BoardController.do?req=qna';"> 
</div>
<div class="clear"></div>
	</article>
<div class="clear"></div>
</body>
</html>