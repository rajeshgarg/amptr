<html>
<head><title>Very Simple Session Test</title>
</head>
<body>
<h1>Session Test</h1>
<h3>Session id</h3>
<p><%=session.getId()%><p>
<p><% if (session.getAttribute("abc") != null) session.setAttribute("abc","123"); %><p>
<p><%=session.getAttribute("abc") %><p>
</body>
</html>