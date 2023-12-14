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

DOM-Based XSS (via URL param)
<br/>
<br/>

Menu
<ul>
    <li><a href="dom-based-xss-01.jsp">Top</a></li>
    <li><a href="dom-based-xss-01.jsp?param1=Some%20text">Safe link 1 => /dom-based-xss-01.jsp/?param1=Some%20text</a> </li>
    <li><a href="dom-based-xss-01.jsp?param1=Some%20<b>text</b>">Safe link 2 => /dom-based-xss-01.jsp/?param1=Some%20<b>text</b></a> </li>
    <li><a href="dom-based-xss-01.jsp?param1=Some%20&lt;b&gt;text&lt;/b&gt;">Safe link 2 => /dom-based-xss-01.jsp/?param1=Some%20<b>text</b></a> </li>
    <li><a href="dom-based-xss-01.jsp?param1=Some+&lt;b&gt;text&lt;/b&gt;">Safe link 3 => /dom-based-xss-01.jsp/?param1=Some+<b>text</b></a> </li>

    <li><a href="dom-based-xss-01.jsp?param1=Some+&lt;img%20src='bla-bla'%20onerror='alert(33)'/&gt;%20&lt;script&gt;alert(22)&lt;/script&gt;">Unsafe link /dom-based-xss-01.jsp/?param1=Some+&lt;script&gt;alert(22)&lt;/script&gt;%20&lt;img src="bla-bla" onerror="alert(33)" /&gt;</a> </li>
</ul>

<div id="content1"> No content </div>

<script>
    const queryString = window.location.search
    console.log("queryString: " + queryString)
    const urlParams = new URLSearchParams(queryString)

    // Injected code execution:
    //   insert directly to HTML content
    //   alert(33) will be called
    document.getElementById("content1").innerHTML = "Base content + content from URL => " + urlParams.get("param1")

    // Injected code execution:
    //   insert to document
    //   both alert(22) and alert(33) will be called
    document.write( "<br/><br/>Base content + content from URL => " + urlParams.get("param1") )
</script>

</body>
</html>
