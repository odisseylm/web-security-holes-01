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
    if (!request.getMethod().equals("POST"))
        throw new IllegalStateException("Improper method."); // proper response code should be returned

    Integer amountToTransfer = Integer.parseInt(request.getParameter("amount"));
    //Integer amountToTransfer = Integer.parseInt(request.getAsyncContext());
%>

You transfered $<%= amountToTransfer %> to your main account.
</body>
</html>


<%--

    StringBuilder sb = new StringBuilder();
    BufferedReader reader = request.getReader();
    String line;
    while ((line = reader.readLine()) != null) {
      sb.append(line);
    }
    String requestBody = sb.toString();

--%>
