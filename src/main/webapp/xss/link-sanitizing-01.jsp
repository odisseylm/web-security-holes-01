<%
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0);

    //response.setHeader("X-XSS-Protection", "1; mode=block");
%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Link Sanitizing</title>
    <%--<meta http-equiv="X-XSS-Protection" content="1; mode=block" />--%>
</head>
<body>

<button onclick="goToLink()">click me 1</button>
<br/>
<button onclick="goToLink2()">click me 2</button>
<br/>
<button onclick="goToLink3_1()">click me 3 (script)</button>
<br/>
<button onclick="goToLink3_2()">click me 3 (plain text)</button>
<br/>
<button onclick="goToLink3_3()">click me 3 (simple rich text)</button>
<br/>

<a id="anchor01">Anchor 01 with javascript protocol</a>
<br/>
<a id="anchor02">Anchor 02 with sanitized (filtered out) javascript protocol</a>
<br/>
<a id="anchor03">Anchor 03 with sanitized http protocol</a>
<br/>

<script src="purify.js"></script>

<%--suppress UnnecessaryUnicodeEscape --%>
<script>

    const userLink = "\u003cscript>alert('hi 1')\u003c/script>";
    function goToLink() {
        window.location.href = `http://http://localhost:8080/web-security-holes-01/xss/` + userLink;
    }

    function goToLink2() {
        alert(`http://http://localhost:8080/web-security-holes-01/xss/` + encodeURIComponent(userLink))
        window.location.href = `http://http://localhost:8080/web-security-holes-01/xss/` + encodeURIComponent(userLink);
    }

    function goToLink3_1() {
        const userLink = "\u003cscript>alert('hi 1')\u003c/script>";
        alert(`http://http://localhost:8080/web-security-holes-01/xss/` + DOMPurify.sanitize(userLink))
        window.location.href = `http://http://localhost:8080/web-security-holes-01/xss/` + DOMPurify.sanitize(userLink);
    }

    function goToLink3_2() {
        const userLink = "plain text"
        alert(`http://http://localhost:8080/web-security-holes-01/xss/` + DOMPurify.sanitize(userLink))
        window.location.href = `http://http://localhost:8080/web-security-holes-01/xss/` + DOMPurify.sanitize(userLink);
    }

    function goToLink3_3() {
        const userLink = "rich <b>text</b>"
        alert(`http://http://localhost:8080/web-security-holes-01/xss/` + DOMPurify.sanitize(userLink))
        window.location.href = `http://http://localhost:8080/web-security-holes-01/xss/` + DOMPurify.sanitize(userLink);
    }


    function sanitizeJSUrl(url) {

        let tempElement = document.createElement('a');
        tempElement.href = url;

        if (tempElement.protocol === 'https:' || tempElement.protocol === 'http:') {
            return url;
        } else {
            return '';
        }
    }

    document.getElementById("anchor01").href = "javascript:alert(456)"
    document.getElementById("anchor02").href = sanitizeJSUrl("javascript:alert(456)")
    document.getElementById("anchor03").href = sanitizeJSUrl("https://www.borland.com")

</script>

<script1> alert(678) </script1>
<br/>
&lt;script> alert(679) &lt;/script>
<br/>

</body>
</html>
