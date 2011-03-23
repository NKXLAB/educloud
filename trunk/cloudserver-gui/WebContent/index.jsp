<%@page import="com.google.educloud.api.entities.Node"%>
<%@page import="com.google.educloud.api.clients.EduCloudNodeClient"%>
<%@page import="com.google.educloud.api.entities.VirtualMachine"%>
<%@page import="com.google.educloud.api.clients.EduCloudVMClient"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.google.educloud.api.entities.Template"%>
<%@page import="java.util.List"%>
<%@page import="com.google.educloud.api.EduCloudFactory"%>
<%@page import="com.google.educloud.api.clients.EduCloudTemplateClient"%>
<%@page import="com.google.educloud.api.EduCloudConfig"%>
<jsp:useBean id="cloudBean" scope="session"
	class="com.google.educloud.gui.beans.CloudAPIBean" />
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"></meta>
<title>EduCloud</title>
<jsp:include page="scripts.jsp" />
</head>
<body>
<div id="container"><jsp:include page="header.jsp" />
<div id="content">
<b>Templates:</b>
<table border="1">
	<tr>
		<td>#</td>
		<td>Name</td>
		<td>OS Type</td>
	</tr>
	<%
		EduCloudTemplateClient templateClient = cloudBean
				.getTemplateClient();
		List<Template> templates = templateClient.describeTemplates();

		for (Template t : templates) {
	%>
	<tr>
		<td><%=t.getId()%></td>
		<td><%=t.getName()%></td>
		<td><%=t.getOsType()%></td>
	</tr>
	<%
		}
	%>
</table>

<b>Virtual Machines:</b>
<table border="1">
	<tr>
		<td>#</td>
		<td>Name</td>
		<td>State</td>
		<td>Template #</td>
	</tr>
	<%
		EduCloudVMClient vmClient = cloudBean.getVMClient();
		List<VirtualMachine> vms = vmClient.describeInstances();

		for (VirtualMachine vm : vms) {
			Template tpl = vm.getTemplate();
	%>
	<tr>
		<td><%=vm.getId()%></td>
		<td><%=vm.getName()%></td>
		<td><%=vm.getState()%></td>
		<td><%=tpl.getName()%></td>
	</tr>
	<%
		}
	%>
</table>
<b>Active nodes:</b>
<table border="1">
	<tr>
		<td>#</td>
		<td>Hostname</td>
		<td>Port</td>
	</tr>
	<%
		EduCloudNodeClient nodeClient = cloudBean.getNodeClient();
		List<Node> nodes = nodeClient.decribeNodes();

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
<jsp:include page="footer.jsp" />
</div>
</body>
</html>
