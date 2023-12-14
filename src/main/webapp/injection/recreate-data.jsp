<%@ page import="com.mvv.security.database.SafeSQLService"%>
<%
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0);
%>
<%@ page contentType="text/plain;charset=UTF-8" %>
<% new SafeSQLService().recreateData(); %>

Database data are restored successfully.
