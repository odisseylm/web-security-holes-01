<%
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0);
%>
<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
    <title>CSS injection</title>
    <link rel="stylesheet" href="unsafe-css.css" />
</head>
<body>

<form>
    <label for="income">Income</label>
    <input id="income" type="text" />
    <br/>
</form>

<div>DIV</div>
<br/>
<div data-var="2">DIV with data-var="2"</div>
<br/>
<div data-var="" id="div3">DIV with data-var=""</div>
<br/>

<button onclick="document.getElementById('div3').setAttribute('data-var','3')">Set div3 data-var="3"</button>
<br/>
<button onclick="document.getElementById('income').value = '100'">Set value = 100</button>
<br/>
<button onclick="document.getElementById('income').setAttribute('value','100')">Set value attribute = 100</button>
<br/>


</body>
</html>
