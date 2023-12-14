<%
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0);
%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head><title>Stored XSS attack</title></head>
<body>

Stored XSS attack
<br/>
<br/>

<%
String unescapedValueFromDatabase  = "<script>alert(1)</script>";
%>

Unescaped param: <%= unescapedValueFromDatabase %>

</body>
</html>
