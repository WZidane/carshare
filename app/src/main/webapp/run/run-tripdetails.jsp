<%@ page contentType="text/html;charset=UTF-8" import="java.util.List, java.util.ArrayList" %>

<%
    request.setAttribute("title", "Carshare - Trajet");
    request.setAttribute("content", "/views/tripdetails.jsp");

    List<String> scripts = new ArrayList<>();
    scripts.add(request.getContextPath() + "/js/menu.js");
    scripts.add(request.getContextPath() + "/js/modal.js");

    request.setAttribute("customScripts", scripts);
%>

<jsp:include page="/layout.jsp" />