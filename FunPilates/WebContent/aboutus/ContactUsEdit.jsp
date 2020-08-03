<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta charset="UTF-8" />
<title>Contact Us Edit</title>
<link href="./css/subpage.css" rel="stylesheet" type="text/css">
<link href="./css/default.css" rel="stylesheet" type="text/css">

<script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>

</head>
<body>
	<%	// form에서 요청한 데이터 한글 처리
	   request.setCharacterEncoding("UTF-8");
	   pageContext.setAttribute("crcn", "\r\n");
	   pageContext.setAttribute("br", "<br/>");
	%>
	 <script>
    //본 예제에서는 도로명 주소 표기 방식에 대한 법령에 따라, 내려오는 데이터를 조합하여 올바른 주소를 구성하는 방법을 설명합니다.
    function co_execDaumPostcode() {
        new daum.Postcode({
            oncomplete: function(data) {
                // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

                // 도로명 주소의 노출 규칙에 따라 주소를 표시한다.
                // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                var roadAddr = data.roadAddress; // 도로명 주소 변수
                var extraRoadAddr = ''; // 참고 항목 변수

                // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                    extraRoadAddr += data.bname;
                }
                // 건물명이 있고, 공동주택일 경우 추가한다.
                if(data.buildingName !== '' && data.apartment === 'Y'){
                   extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                if(extraRoadAddr !== ''){
                    extraRoadAddr = ' (' + extraRoadAddr + ')';
                }

                // 우편번호와 주소 정보를 해당 필드에 넣는다.
                document.getElementById('co_Postcode').value = data.zonecode
                document.getElementById('co_RoadAddress').value = roadAddr;
            }
        }).open();
    }
	</script>
	 
	 
								<%-- About Us 좌측 목록 --%>	
	<jsp:include page="AboutUs_list.jsp"/>
		<%-- -------------------------------------------------------------------- --%>	
	<article id="contactUsEdit"> 
	<h1>ContactUs Edit</h1>
	<form action="AboutUsController.do?req=updateCo_Info" method="post" margin="0">
		<c:set var ="co_Info" value="${requestScope.co_Info}"/>
		<table>
			<tr>
				<td align="right">회사이름:</td>
				<td><input type="text" name="co_Name" value="${co_Info.co_Name}"></td>
			</tr>
			<tr>
				<td align="right">전화번호:</td>
				<td><input type="text" name="co_Tel" value="${co_Info.co_Tel}" placeholder="숫자만 입력하세요" numberOnly></td>
			</tr>	
			<tr>
				<td align="right">팩스번호:</td>
				<td><input type="text" name="co_Fax" value="${co_Info.co_Fax}" placeholder="숫자만 입력하세요" numberOnly></td>
			</tr>
			<tr>
				<td align="right">이메일:</td>
				<td><input type="text" name="co_Email" value="${co_Info.co_Email}"></td>
			</tr>
			<tr>
				<td align="right">회사위치</td><td></td>
			</tr>
			<tr>		
				<td align="right">우편번호:</td>
				<td>
					<input type="text" id="co_Postcode" name="co_Postcode" size="25" placeholder="주소찾기 버튼을 눌러주세요" value="${co_Info.co_Postcode}" readonly> 
					<input type="button" class="dup" onclick="co_execDaumPostcode()" value="주소찾기"> 
				</td>	
			</tr>
			<tr> 
				<td align="right">주소:</td>
				<td>
					<input type="text" id="co_RoadAddress" name="co_RoadAddress"  size="50" value="${co_Info.co_RoadAddress}" readonly>
				</td>
			</tr>
			<tr>
				<td></td>	
				<td>
					<span id="guide" style="color:#999;display:none"></span>
					<input type="text" id="co_DetailAddress"  name="co_DetailAddress" size="50" placeholder="상세 주소 입력" value="${co_Info.co_DetailAddress}">
				</td>
			</tr>
			<tr>
				<td colspan="2" align="right"><input type="button" id="button" value="수정 완료" onclick="Check(this.form);"></td>
			</tr>
		</table>	
</form>
	<script type="text/javascript">	
	$("input:text[numberOnly]").on("keyup", function() {
	    $(this).val($(this).val().replace(/[^0-9]/g,""));
	});
	
		function Check(f){
			// forms[] 배열 : <form>태그에 접근하는 방법
			// elements : <form>태그 내부에 있는 모든 <input> 태그들을 말함
			// length : 개수, 길이
			
			// 위 cnt 변수에 들어가는 <input>태그의 개수에 대한 설명 (3개)
			var cnt = f.elements.length;
				
			// 비어있는 정보에 대하여 경고 메시지
			for(i=0; i<cnt-2; i++){
				if(f.elements[i].value == ""){			
					var msg = i+1+ "번째 정보가 누락 되었습니다.\n 올바른 선택을 해주세요";
					alert(msg);
					f.elements[i].focus();
					return;
				} // if	
			} // for문
			
			// form 전송 요청
			f.submit();
		} // Check(); 
		
	</script>
	</article>
	<div class="clear"></div>
</body>
</html>