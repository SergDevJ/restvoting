<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>

<jsp:include page="fragments/headTag.jsp"/>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<html lang="ru">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
<ul>
    <a href="${pageContext.request.contextPath}/voting"><spring:message code="restaurant.voting"/></a>
    <br>
    <br>
    <h6><a><spring:message code="user.administration"/></a></h6>
    <li><a href="${pageContext.request.contextPath}/admin/users-list"><spring:message code="admin.users"/></a></li>
    <li><a href="${pageContext.request.contextPath}/admin/restaurants-list"><spring:message code="admin.restaurants"/></a></li>
    <li><a href="${pageContext.request.contextPath}/admin/dishes-list"><spring:message code="admin.dishes"/></a></li>
</ul>
</body>
</html>

</body>
</html>