<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인</title>
</head>
<body>
<form:form action="login.do" modelAttribute="loginVO">
	<!-- 필드 없이 에러코드만 설정했을 경우 에러문구를 읽어오기위해 
	     path 속성을 명시하지 않고 아래와 같이 지정 -->
	<!-- span태그가 아니라 div태그가 만들어짐 -->
	<form:errors element="div"/>
	아이디 : <form:input path="userId"/>
	<form:errors path="userId"/>
	<br>
	비밀번호 : <form:password path="password"/>
	<form:errors path="password"/>
	<br>
	<form:button>전송</form:button>
</form:form>
</body>
</html>