<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>

<!DOCTYPE html>
<html lang="fr">

<%@ include file="/views/includes/head.jsp" %>

<body class="bg-gray-50 text-gray-900">
    <%@ include file="/views/includes/menu.jsp" %>

    <div class="mx-auto">
        <jsp:include page="${content}" />
    </div>

    <%@ include file="/views/includes/footer.jsp" %>
</body>
</html>