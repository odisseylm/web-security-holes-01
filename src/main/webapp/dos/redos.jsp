<%@ page import="com.mvv.security.regex.RegexSearchService" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0);
%>
<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%
    String searchRegExStr = request.getParameter("searchRegEx");
    String searchRegExContent = request.getParameter("searchRegExContent");

    System.out.println("searchRegExStr: " + searchRegExStr);
    System.out.println("searchRegExContent: " + searchRegExContent);

    if (searchRegExStr != null && searchRegExContent != null &&
       !searchRegExStr.isBlank() && !searchRegExContent.isBlank()) {
        List<Integer> matches = new RegexSearchService().findMatches(searchRegExContent, searchRegExStr.trim());
        System.out.println("matches: " + matches);
        request.setAttribute("searchResult", matches);
    }
%>
<html>
<head>
    <title>ReDOS (RegexDOS)</title>
</head>
<body>

<b style="color: red">I found vulnerable Java 9+ regex ONLY for 'matches', but did not find once for search.</b>
<br/>
<br/>

<form>
    <label for="searchRegEx" >Search regex</label>
    <%--<input id="searchRegEx" name="searchRegEx" value="^((ab)*)+$" />--%>
    <input id="searchRegEx" name="searchRegEx" value="(ab)" />
    <br/>

    <br/>
    In real application it will be uploaded earlier file (for example, some kind of content management system)
    <br/>

    <label for="searchRegExContent" >Search regex</label>
    <textarea id="searchRegExContent" name="searchRegExContent">ababababababababababababababababababababababababa</textarea>
    <br/>
    <br/>

    <div style="border: black">
        Search results:<br/>
        <c:out value="${searchResult}" />
    </div>

    <br/>
    <input type="submit" />
</form>

</body>
</html>
