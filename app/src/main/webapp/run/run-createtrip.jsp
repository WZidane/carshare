<%@ page contentType="text/html;charset=UTF-8" import="java.util.List, java.util.ArrayList" %>

<%
    request.setAttribute("title", "Carshare - Création d'un trajet");
    request.setAttribute("content", "/views/createtrip.jsp");

    List<String> scripts = new ArrayList<>();
    scripts.add(request.getContextPath() + "/js/menu.js");

    request.setAttribute("customScripts", scripts);
%>

<jsp:include page="/layout.jsp" />