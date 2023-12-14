<%
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0);
%>
<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
    <title>Bank page - transfer money</title>
</head>
<body>

<%
    Integer amountToTransfer = Integer.parseInt(request.getParameter("amount"));
%>

    You transfered $<%= amountToTransfer %> to your main account.
</body>
</html>
