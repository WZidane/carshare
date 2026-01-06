<%@ page contentType="text/html;charset=UTF-8" import="java.util.List, java.util.ArrayList" %>

<%
    request.setAttribute("title", "Carshare - Profil");
    request.setAttribute("content", "/views/profile.jsp");

    List<String> scripts = new ArrayList<>();
    scripts.add(request.getContextPath() + "/js/menu.js");
    scripts.add(request.getContextPath() + "/js/avatar.js");
    scripts.add(request.getContextPath() + "/js/edit-profile.js");

    request.setAttribute("customScripts", scripts);
%>

<jsp:include page="/layout.jsp" />