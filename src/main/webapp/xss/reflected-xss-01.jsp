<%
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0);
%>
<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head><title>Reflected XSS attack</title></head>
<body>

Reflected XSS attack
<br/>
It is named 'reflected' because injection is done on <b>server</b> side and applied on client side (like, code injection reflected via server).
<br/>

Usage: <a href="http://localhost:8080/web-security-holes-01/reflected-xss-01.jsp?param1=<script>alert(1)</script>">http://localhost:8080/web-security-holes-01/reflected-xss-01.jsp?param1=&lt;script&gt;alert(1)&lt;/script&gt;</a>
<br/>
<br/>

Unescaped param: <%= request.getParameter("param1") %>

</body>
</html>
