<%@ page pageEncoding="UTF-8" import="java.util.List" isELIgnored="false" %>

<head>
    <meta charset="UTF-8">
    <link rel="icon" href="favicon.ico" />
    <title><%= request.getAttribute("title") != null ? request.getAttribute("title") : "Carshare" %></title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script src="https://cdn.jsdelivr.net/npm/@tailwindcss/browser@4"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
    <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
    
    <%
        List<String> scriptList = (List<String>) request.getAttribute("customScripts");
        if (scriptList != null) {
            for (String src : scriptList) {
    %>
    <script src="<%= src %>"></script>
    <%
            }
        }
    %>
</head>