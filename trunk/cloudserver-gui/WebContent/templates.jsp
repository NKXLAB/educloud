<%@page import="com.google.educloud.api.entities.Template"%>
<%@page import="java.util.List"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<jsp:useBean id="templateBean"
	class="com.google.educloud.gui.beans.TemplateBean" />
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
	margin: 10px;
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
</style>
</head>
<body>
<div id="container"><jsp:include page="header.jsp" />
<div id="content">
<table class="cloudgrid">
	<tr>
		<th>ID</th>
		<th>Name</th>
		<th>OS Type</th>
	</tr>
	<%
		List<Template> templates = templateBean.getTemplates(session);

		for (Template tpl : templates) {
	%>
	<tr>
		<td><%=tpl.getId()%></td>
		<td><%=tpl.getName()%></td>
		<td><%=tpl.getOsType()%></td>
	</tr>
	<%
		}
	%>
</table>
</div>
<jsp:include page="footer.jsp" /></div>
</body>
</html>
