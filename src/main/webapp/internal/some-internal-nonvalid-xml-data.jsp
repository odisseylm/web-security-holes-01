<%
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0);
%>
<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<!-- It is valid HTML, but invalid XML since it uses non-closed HTML tags. -->

<head><title>Internal HTML file (but invalid as XML)</title></head>
<body>

<p>
Internal data 1

<p>
Internal data 2

</body>
</html>
