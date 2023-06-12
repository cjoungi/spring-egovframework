<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- 중앙 컨텐츠 시작 -->
<div class="page-main">
	<h2>회원권한수정</h2>
	<form:form action="admin_update.do" id="modify_form" modelAttribute="memberVO">
		<form:hidden path="mem_num"/>
		<form:errors element="div" cssClass="error-color"/>
		<ul>
			<li>
				<label>회원권한</label>
				<c:if test="${memberVO.auth < 9}"><!-- 1,2번 의미 -->
				<form:radiobutton path="auth" value="1"/>정지
				<form:radiobutton path="auth" value="2"/>일반
				</c:if>
				<c:if test="${memberVO.auth==3}">관리</c:if>
			</li>
		</ul>
		<div class="align-center">
			<c:if test="${memberVO.auth!=9}">
			<form:button>전송</form:button>
			</c:if>
			<input type="button" value="회원목록" onclick="location.href='admin_list.do'">
		</div>
		<ul>
			<li>
				<label>이름</label>
				${memberVO.name}
			</li>
			<li>
				<label>전화번호</label>
				${memberVO.phone}
			</li>
			<li>
				<label>이메일</label>
				${memberVO.email}
			</li>
			<li>
				<label>우편번호</label>
				${memberVO.zipcode}
			</li>
			<li>
				<label>주소</label>
				${memberVO.address1} ${memberVO.address2}
			</li>
		</ul>
	</form:form>
</div>
<!-- 중앙 컨텐츠 끝 -->