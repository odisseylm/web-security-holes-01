<%
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0);
%>
<%@ page contentType="text/xml;charset=UTF-8" %>
<%@ page import="com.mvv.security.xxe.XmlService" %>
<%@ page import="static com.mvv.security.xxe.UtilsKt.defaultDocumentBuilder" %>
<% new XmlService(defaultDocumentBuilder()).serviceWhichReturnsFullContentInErrorWithPossibleXxeLocalFile(request, response); %>