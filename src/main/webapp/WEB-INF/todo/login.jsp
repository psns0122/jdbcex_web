<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>Todo login</title>
</head>
<body>
    <c:if test="${param.result == 'error'}">
        <p style="color: red;">⚠ 로그인 에러입니다!</p>
    </c:if>

    <form action="/login" method="post">
        <input type="text" name="mid">
        <input type="text" name="mpwd">
        <input type="checkbox" name="auto">
        <button type="submit">로그인</button>
    </form>
</body>
</html>
