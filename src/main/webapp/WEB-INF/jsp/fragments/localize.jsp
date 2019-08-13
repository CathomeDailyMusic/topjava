<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script type="text/javascript">
    const locale = (navigator.language || 'en-GB').substr(0, 2);

    function localizedMessages(i18n) {
        <c:forEach var="key" items='<%=new String[]{"common.deleted","common.saved","common.enabled","common.disabled","common.errorStatus","common.confirm"}%>'>
        i18n["${key}"] = "<spring:message code="${key}"/>";
        </c:forEach>
    }
</script>