<%@ page pageEncoding="UTF-8" import="java.time.LocalDate" isELIgnored="false" %>

<footer class="py-6 bg-blue-100 text-center text-gray-500">
    &copy; <%= LocalDate.now().getYear() %> Carshare. Tous droits réservés.
</footer>

<script src="${pageContext.request.contextPath}/js/menu.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/avatar.js" type="text/javascript"></script>