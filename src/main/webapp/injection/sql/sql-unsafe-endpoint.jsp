<%@ page import="com.mvv.security.database.UnsafeSQLService"%><%
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0);
%>
<%@ page contentType="text/plain;charset=UTF-8" %>
<% new UnsafeSQLService().getAccountsByNameLike(request, response); %>