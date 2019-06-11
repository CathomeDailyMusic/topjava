<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Meal</title>
</head>
<body>
<h3><a href="meals">List of meals</a></h3>
<form method="POST" action="meals" name="frmMeal">
    <p>
        <input
                type="hidden" name="id" readonly="readonly"
                value="<c:out value="${meal.id}" />"/>
        <fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" type="both" var="parsedDateTime"/>
        <label for="date">Date</label>
        <input
                type="date" name="date" id="date"
                value="<fmt:formatDate value="${parsedDateTime}" pattern="yyyy-MM-dd" />"/><br/>
        <label for="time">Time</label>
        <input
                type="time" name="time" id="time"
                value="<fmt:formatDate value="${parsedDateTime}" pattern="HH:mm" />"/><br/>
        <label for="description">Description</label>
        <input
                type="text" name="description" id="description"
                value="<c:out value="${meal.description}" />"/> <br/>
        <label for="calories">Calories</label>
        <input
                type="number" name="calories" id="calories"
                value="<c:out value="${meal.calories}" />"/>
    </p>
    <input type="submit" value="Submit"/>
</form>
</body>
</html>