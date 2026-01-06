<%@ page contentType="text/html;charset=UTF-8" import="java.util.List, java.util.ArrayList" %>

<%
    request.setAttribute("title", "Carshare - Trajets Recherchés");
    request.setAttribute("content", "/views/searchtrips.jsp");

    List<String> scripts = new ArrayList<>();
    scripts.add(request.getContextPath() + "/js/menu.js");

    request.setAttribute("customScripts", scripts);
%>

<jsp:include page="/layout.jsp" />