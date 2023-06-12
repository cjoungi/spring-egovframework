<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- Admin 메뉴 시작 -->
<script type="text/javascript" src="${pageContext.request.contextPath}/js/member.js"></script>
<div class="side-bar">
	<ul>
		<li>
			<input type="button" class="menu-btn" value="회원관리" onclick="location.href='${pageContext.request.contextPath}/member/admin_list.do'">
		</li>
	
		
	</ul>
</div>