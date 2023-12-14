<%
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0);
%>
<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html>
<head><title>DOM-Based XSS</title></head>
<body>

DOM-Based XSS (via URL param)<br/><br/>

<div id="id1">No content</div>

<script>
    // It is a bit invalid HTML and validation tools can consider it as valid.
    // but FireFox calls there 'onerror=alert(33)'.
    // const similarToSafeContent = '<noscript> <p title="</noscript><img src=bla-bla onerror=alert(33)>"> Some text and other content'

    // Fully valid HTML, but FireFox also calls there 'onerror=alert(33)'.
    const similarToSafeContent = '<noscript> <p title="</noscript><img src=bla-bla onerror=alert(33)>"/> Some text and other content </noscript>'

    document.getElementById("id1").innerHTML = similarToSafeContent
</script>

<!-- This is also called :-(  Why??? -->
<%--<noscript> <p title="</noscript><img src=bla-bla onerror=alert(34)>"/> Some text and other content </noscript>--%>


</body>
</html>
