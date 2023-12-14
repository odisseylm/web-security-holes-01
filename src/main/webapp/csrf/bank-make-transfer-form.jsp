<%
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0);
%>
<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
    <title>Bank page - transfer money form</title>
</head>
<body>

<form name="transfer-many"
      method="post"
      action="http://localhost:8080/web-security-holes-01/csrf/bank-make-transfer-by-POST.jsp" >
    <label for="amount">Amount: </label><input type="text" name="amount" id="amount" value=""/>
    <input type="hidden" name="something-other" value="123"/>
    <input type="submit"/>
</form>

</body>
</html>
