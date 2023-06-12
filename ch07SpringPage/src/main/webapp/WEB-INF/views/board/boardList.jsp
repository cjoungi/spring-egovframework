<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- 중앙 컨텐츠 시작 -->
<script type="text/javascript">
	/*
	RedirectAttributes 객체를 이용해서 리다이렉트한 후 데이터를 표시하기 위해서 자바스크립트로 View에서 메시지 처리를 함
	이럴 때 다른 페이지로 이동한 후 브라우저 뒤로가기를 했을 때 메시지가 사라지지 않고 그대로 노출
	이런 현상을 방지하기 위해 값이 남아있어도 hash 값이 달라지면 메시지가 다시 호출되지 않도록 처리
	*/
	let result = '${result}';
	if(!location.hash && result =='success'){//hash : 페이지 url 뒤에 #
		alert('글쓰기 완료');
		history.replaceState('','','#rs');
	}
	
	$(function(){
		//검색 유효성 체크
		$('#search_form').submit(function(){
			if($('#keyword').val().trim()==''){
				alert('검색어를 입력하세요');
				$('#keyword').val('').focus();
				return false;
			}
		});
	});
</script>
<div class="page-main">
	<h2>게시판 목록</h2>
	<form action="list.do" id="search_form" method="get"><!-- 검색은 get 방식 --> 
		<ul class="search">
			<li>
				<select name="keyfield" id="keyfield">
					<option value="1" <c:if test="${param.keyfield==1}">selected</c:if>>제목</option>
					<option value="2" <c:if test="${param.keyfield==2}">selected</c:if>>ID+별명</option>
					<option value="3" <c:if test="${param.keyfield==3}">selected</c:if>>내용</option>
					<option value="4" <c:if test="${param.keyfield==4}">selected</c:if>>제목+내용</option>
				</select>
			</li>
			<li>
				<input type="search" name="keyword" id="keyword" value="${param.keyword}">
			</li>
			<li>
				<input type="submit" value="전송">
				<input type="button" value="목록" onclick="location.href='list.do'">
			</li>
		</ul>
	</form>
	<c:if test="${!empty user}">
	<div class="align-right">
		<input type="button" value="글쓰기" onclick="location.href='write.do'">
	</div>
	</c:if>
	
	<c:if test="${count==0}">
	<div class="result-display">표시할 게시물이 없습니다.</div>
	</c:if>
	<c:if test="${count > 0}">
	<table class="striped-table">
		<tr>
			<th>번호</th>
			<th>제목</th>
			<th>작성자</th>
			<th>작성일</th>
			<th>조회수</th>
		</tr>
		<c:forEach var="board" items="${list}">
			<tr>
				<td>${board.board_num}</td>
				<td><a href="detail.do?board_num=${board.board_num}">${board.title}</a></td>
				<td>
					<c:if test="${empty board.nick_name}">${board.id}</c:if>
					<c:if test="${!empty board.nick_name}">${board.nick_name}</c:if>
				</td>
				<td>${board.reg_date}</td>
				<td>${board.hit}</td>
			</tr>
		</c:forEach>
	</table>
	<div class="align-center">${page}</div>
	</c:if>
</div>
<!-- 중앙 컨텐츠 끝 -->