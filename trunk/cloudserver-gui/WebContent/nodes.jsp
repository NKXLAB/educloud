<%@page import="com.google.educloud.api.entities.Node"%>
<%@page import="java.util.List"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<jsp:useBean id="nodeBean" class="com.google.educloud.gui.beans.NodeBean" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"></meta>
<title>EduCloud</title>
<jsp:include page="scripts.jsp" />
<style type="text/css">
table {
	display: table;
	border-collapse: separate;
	border-spacing: 2px 2px;
	border-color: gray;
}

.cloudgrid {
	width: 100%;
	text-align: left;
	border-collapse: collapse;
}

.cloudgrid th {
	background: #d6d6d6;
	display: table-cell;
	vertical-align: inherit;
	padding: 8px;
}

.cloudgrid td {
	display: table-cell;
	vertical-align: inherit;
	border-bottom: 1px solid white;
	border-top: 1px solid transparent;
	padding: 8px;
	background: #ebebeb;
}

input {
	width: auto;
	display: block;
	padding: auto;
	margin: 0 0 0 0;
	font-size: 18px;
	color: #3a3a3a;
	
}
select {
	width: auto;
	display: block;
	padding: auto;
	margin: 0 0 0 0;
	font-size: 12px;
	color: #3a3a3a;
	padding: 0px;
}
</style>
</head>
<body>
<div id="container"><jsp:include page="header.jsp" />
<div id="content">
<div style="margin:10px">
<div>
<div style="background:#ebebeb;padding:5px;">
</div>
<table id="nodesTable" class="cloudgrid">
	<tr>
		<th>ID</th>
		<th>Hostname</th>
		<th>Port</th>
	</tr>
	<%
		List<Node> nodes = nodeBean.getNodes(session);

		for (Node node : nodes) {
	%>
	<tr>
		<td><%=node.getId()%></td>
		<td><%=node.getHostName()%></td>
		<td><%=node.getPort()%></td>
	</tr>
	<%
		}
	%>
</table>
</div>
</div>
</div>
<jsp:include page="footer.jsp" />
</div>
</body>
</html>
