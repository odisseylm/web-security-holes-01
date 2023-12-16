<%
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0);
%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Tests</title>
</head>
<body>

Every file in this directory is testing page by itself.
Call them from the directory <a href="./">..</a>.

</body>
</html>
