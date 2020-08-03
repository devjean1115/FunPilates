<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta charset="UTF-8" />
<title>Insert title here</title>
<title>About Us</title>
<link href="./css/subpage.css" rel="stylesheet" type="text/css">
<link href="./css/default.css" rel="stylesheet" type="text/css">

<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=08641f3ad73f6bed41af6dab27b511c3&libraries=services"></script>
</head>

<body>
								<%-- About Us 좌측 목록 --%>	
	<jsp:include page="AboutUs_list.jsp"/>
		<%-- -------------------------------------------------------------------- --%>	
	<article>
	 <div>
	 	<h1>Send Email</h1>
        <form action="AboutUsController.do?req="sendMail" method="post">
            <table>
                <tr><th colspan="2">JSP 메일 보내기</th></tr>
                <tr><td>from</td><td><input type="text" name="from" /></td></tr>
                <tr><td>to</td><td><input type="text" name="to" /></td></tr>
                <tr><td>subject</td><td><input type="text" name="subject" /></td></tr>
                <tr><td>content</td><td><textarea name="content" style="width:300px; height:200px;"></textarea></td></tr>
                <tr><td colspan="2" style="text-align:right;"><input type="submit" value="Transmission"/></td></tr>
            </table>
        </form>
    </div>
	<div class="clear"></div>
	</article>
	<div class="clear"></div>
</body>
</html>