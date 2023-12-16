<%
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0);
%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>SQL injection test page</title>
</head>
<body>

<fieldset>
    <legend>Select SQL service:</legend>

    <div>
        <input type="radio" name="sqlServiceType" value="unsafe" id="unsafe" onclick="setUpFormUrl()" checked />
        <label for="unsafe">Unsafe</label>
    </div>

    <div>
        <input type="radio" name="sqlServiceType" value="safe" id="safe" onclick="setUpFormUrl()" />
        <label for="safe">Safe</label>
    </div>
</fieldset>


<fieldset>
    <legend>Cases:</legend>

    <div>
        <form action="" method="GET" target="_blank" >
            <input type="text" name="accountNamePart" value="main" id="usual" />
            <label for="usual">Unsafe</label>
            <input type="submit" />
        </form>
    </div>

    <div>
        <form action="" method="GET" target="_blank" >
            <input type="text" name="accountNamePart" value="main'; select * from ACCOUNT where 1=1 or '1' = '" id="stealAllAccounts" />
            <label for="stealAllAccounts">Steal all accounts</label>
            <input type="submit" />
        </form>
    </div>

    <div>
        <form action="" method="GET" target="_blank" >
            <input type="text" name="accountNamePart" value="main'; delete from ACCOUNT; select * from ACCOUNT where '1' = '" id="removeAllAccounts" />
            <label for="removeAllAccounts">Remove all accounts</label>
            <input type="submit" />
        </form>
    </div>

    <div>
        <a href="javascript:restoreDatabase()">Restore database</a>
    </div>
</fieldset>


<script>
    function serviceEndPointUrl() {
        const sqlServiceType = document.querySelector('input[name="sqlServiceType"]:checked').value;
        switch (sqlServiceType) {
            case "unsafe" : return "sql-unsafe-endpoint.jsp";
            case "safe"   : return "sql-safe-endpoint.jsp";
            default       : throw  "Error bla-bla bla bla..."
        }
    }

    function restoreDatabase() {
        const req = new XMLHttpRequest();
        req.open("POST", "recreate-sql-data.jsp");
        req.send();
    }

    function setUpFormUrl() {
        //document.forms.forEach((f) => f.action = serviceEndPointUrl())
        for (const f of document.forms) { f.action = serviceEndPointUrl() }
    }

    setUpFormUrl()
</script>

</body>
</html>
